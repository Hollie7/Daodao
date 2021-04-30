package com.zzb.hello.method;

import com.zzb.hello.pojo.*;
import com.zzb.hello.mapper.WordMapper;
import java.util.*;

public class Evaluate {

    //依据very、most等词语类型确定情感词的前置权重分数
    public static double WeightPoint(String word, WordMapper wordMapper){
        VeryWord very = wordMapper.findVeryWord(word);
        MostWord most = wordMapper.findMostWord(word);
        MoreWord more = wordMapper.findMoreWord(word);
        OverWord over = wordMapper.findOverWord(word);
        IshWord ish = wordMapper.findIshWord(word);
        InverseWord inverse = wordMapper.findInverseWord(word);
        InsufficientlyWord insufficiently = wordMapper.findInsufficientlyWord(word);
        if(most!=null) {
            return 2;
        }
        else if(over!=null) {
            return 1.5;
        }
        else if(very!=null) {
            return 1.25;
        }
        else if(more!=null) {
            return 1.2;
        }
        else if(ish!=null) {
            return 0.8;
        }
        else if(insufficiently!=null) {
            return 0.5;
        }
        else if(inverse!=null) {
            return -1;
        }
        else {
            return 1;
        }
    }

    //输入参数为分词的词语的一维数组，筛选出neg词和pos词，及各自的前置副词
    public static List<String[][]> WordFilter(String[] wordsDivided, WordMapper wordMapper){
        int wordsSize = wordsDivided.length;
        String emotionWord;
        String preWord;
        List<String> PosWords = new ArrayList<>();
        List<String> NegWords = new ArrayList<>();
        List<String> PrePosWords = new ArrayList<>();
        List<String> PreNegWords = new ArrayList<>();

        //long time1 = System.currentTimeMillis();


        /*for (int i = 1; i < wordsSize; i++){
            preWord = wordsDivided[i-1];
            emotionWord = wordsDivided[i];

            //筛选分词中的neg、pos词语
            NegWord neg = wordMapper.findNegWord(emotionWord);
            PosWord pos = wordMapper.findPosWord(emotionWord);
            if(pos != null){
                PosWords.add(emotionWord);
                PrePosWords.add(preWord);
            }
            else if(neg != null){
                NegWords.add(emotionWord);
                PreNegWords.add(preWord);
            }
        }*/

        for (int i = 1; i < wordsSize; i++){
            preWord = wordsDivided[i-1];
            emotionWord = wordsDivided[i];

            //筛选分词中的neg、pos词语
            PosWord pos = wordMapper.findPosWord(emotionWord);
            if(pos != null){
                PosWords.add(emotionWord);
                PrePosWords.add(preWord);
            }
            else{
                NegWord neg = wordMapper.findNegWord(emotionWord);
                if(neg != null){
                    NegWords.add(emotionWord);
                    PreNegWords.add(preWord);
                }
            }
        }


        //类型转换
        String[] Poses=PosWords.toArray(new String[0]);
        String[] Negs=NegWords.toArray(new String[0]);
        String[] PrePoses=PrePosWords.toArray(new String[0]);
        String[] PreNegs=PreNegWords.toArray(new String[0]);

        String[][] FinalPos = new String[2][Poses.length];
        String[][] FinalNeg = new String[2][Negs.length];

        FinalPos[0] = Poses;
        FinalPos[1] = PrePoses;

        FinalNeg[0] = Negs;
        FinalNeg[1] = PreNegs;
        List<String[][]> FilterWords = new ArrayList<>();
        FilterWords.add(FinalPos);
        FilterWords.add(FinalNeg);
        return FilterWords;
    }

    //计算情感分数
    public static double EmotionScore(List<String[][]> FilterWords, WordMapper wordMapper){
        String[][] FinalPos = FilterWords.get(0);
        String[][] FinalNeg = FilterWords.get(1);
        double score = 0;

        long time1 = System.currentTimeMillis();

        for (int i = 0; i < FinalPos[0].length; i++) {
            score += 1 * Evaluate.WeightPoint(FinalPos[1][i], wordMapper);
        }
        for (int i = 0; i < FinalNeg[0].length; i++) {
            score += (-1) * Evaluate.WeightPoint(FinalNeg[1][i], wordMapper);
        }

        long time2 = System.currentTimeMillis();
        long time = time2 - time1;
        System.out.println("算分数执行了："+time+"ms！");

        return score;
    }

    //筛选情感词，并依据筛选出的情感词的情感类型进行分类排序，如果情感词前有否定词，默认扣除一个相应分类的数量
    public static String[][] EmotionClass(String[][] SegmentFilter, WordMapper wordMapper){
        //long time1 = System.currentTimeMillis();

        //初始化
        int WordSum = SegmentFilter[0].length;
        //System.out.println(WordSum);
        List<Word> selectWord;
        List<String> EmoClass = new ArrayList<>();
        List<String> inverseWords = new ArrayList<>();

        //筛选情绪词
        for (int i = 1; i < WordSum; i++) {
            selectWord = wordMapper.queryWordByWord(SegmentFilter[0][i]);
            String preWord = SegmentFilter[0][i-1];
            String ClassWord = "";
            if(selectWord.size() == 0) {
                continue;
            }
            //如果情绪词数目1
            else if(selectWord.size() == 1){
                EmoClass.add(selectWord.get(0).emotionClass);
                ClassWord = selectWord.get(0).emotionClass;
                //System.out.println(SegmentFilter[0][i]+" "+ClassWord);
            }
            //如果情绪词为多个含义
            else{
                String wordClass = SegmentFilter[1][i];
                for (Word word : selectWord) {
                    if (wordClass.equals(word.wordClass)) {
                        EmoClass.add(word.emotionClass);
                        ClassWord = word.emotionClass;
                        //System.out.println(SegmentFilter[0][i]+" "+ClassWord);
                        break;
                    }
                }
            }
            if(wordMapper.findInverseWord(preWord) != null) {
                inverseWords.add(ClassWord);
            }
        }

        //long time2 = System.currentTimeMillis();
        //long time = time2 - time1;
        //System.out.println("执行了："+time+"ms！");

        //类型转换+否定词处理
        String[] temp1 = EmoClass.toArray(new String[0]);
        String[] temp2 = inverseWords.toArray(new String[0]);
        String[][] WordsEmotionClass = Segment.WordSort(temp1);
        String[][] Inverse = Segment.WordSort(temp2);
        for (int i = 0; i < Inverse[0].length; i++) {
            for (int j = 0; j < WordsEmotionClass[0].length; j++) {
                if(Inverse[0][i].equals(WordsEmotionClass[0][j])){
                    int num = Integer.parseInt(WordsEmotionClass[1][j]) - Integer.parseInt(Inverse[1][i]);
                    WordsEmotionClass[1][j] = ""+num;
                }
            }
        }
        //System.out.println(Arrays.toString(Inverse[0]));

        //重新排序
        FinalSort(WordsEmotionClass);
        return WordsEmotionClass;
    }

    //使用数组将数据库内容全部读取后，再进行情感词筛选、分类和数目计算
    public static String[][] EmotionClassArray(String[][] SegmentFilter, WordMapper wordMapper){
        //long time1 = System.currentTimeMillis();

        long time1 = System.currentTimeMillis();
        //获取数据库所有数据
        List<Word> AllWords = wordMapper.getAllWords();

        long time2 = System.currentTimeMillis();
        long time = time2 - time1;
        System.out.println("读取所有数据执行了："+time+"ms！");

        String[][] AllWordsString = new String[7][AllWords.size()];
        //List转为String二维数组
        for (int i = 0; i < AllWords.size(); i++) {
            AllWordsString[0][i] = AllWords.get(i).wordSelf;
            AllWordsString[1][i] = AllWords.get(i).wordClass;
            AllWordsString[2][i] = ""+AllWords.get(i).wordMeanNum;
            AllWordsString[3][i] = ""+AllWords.get(i).wordMeanIndex;
            AllWordsString[4][i] = AllWords.get(i).emotionClass;
            AllWordsString[5][i] = ""+AllWords.get(i).emotionIntensity;
            AllWordsString[6][i] = ""+AllWords.get(i).emotionPolarity;
        }
        //System.out.println(AllWordsString[0].length+" "+AllWordsString.length);

        int WordSum = SegmentFilter[0].length;
        //System.out.println(WordSum);
        //List<Word> selectWord = wordMapper.queryWordByWord("阻力");
        //String selectWord = "";
        List<String> EmoClass = new ArrayList<>();
        List<String> inverseWords = new ArrayList<>();
        for (int i = 1; i < WordSum; i++) {
            int[] wordIndex = getIndex(AllWordsString[0], SegmentFilter[0][i]);
            //selectWord = wordMapper.queryWordByWord(SegmentFilter[0][i]);
            String preWord = SegmentFilter[0][i-1];
            String ClassWord = "";
            if(wordIndex == null) {
                continue;
            } else if(wordIndex.length == 1){
                EmoClass.add(AllWordsString[4][wordIndex[0]]);
                ClassWord = AllWordsString[4][wordIndex[0]];
                //System.out.println(SegmentFilter[0][i]+" "+ClassWord);
            }
            else{
                String wordClass = SegmentFilter[1][i];
                for (int index : wordIndex) {
                    if (wordClass.equals(AllWordsString[1][index])) {
                        EmoClass.add(AllWordsString[4][index]);
                        ClassWord = AllWordsString[4][index];
                        //System.out.println(SegmentFilter[0][i]+" "+ClassWord);
                        break;
                    }
                }
            }
            if(wordMapper.findInverseWord(preWord) != null) {
                inverseWords.add(ClassWord);
            }
        }


        String[] temp1 = EmoClass.toArray(new String[0]);
        String[] temp2 = inverseWords.toArray(new String[0]);
        String[][] EmotionClassTemp = Segment.WordSort(temp1);
        String[][] Inverse = Segment.WordSort(temp2);
        for (int i = 0; i < Inverse[0].length; i++) {
            for (int j = 0; j < EmotionClassTemp[0].length; j++) {
                if(Inverse[0][i].equals(EmotionClassTemp[0][j])){
                    int num = Integer.parseInt(EmotionClassTemp[1][j]) - Integer.parseInt(Inverse[1][i]);
                    EmotionClassTemp[1][j] = ""+num;
                }
            }
        }
        //System.out.println(Arrays.toString(Inverse[0]));
        FinalSort(EmotionClassTemp);

        String[][] WordsEmotionClass = new String[3][EmotionClassTemp[0].length];
        WordsEmotionClass[0] = EmotionClassTemp[0];
        WordsEmotionClass[1] = EmotionClassTemp[1];
        for (int i = 0; i < EmotionClassTemp[0].length; i++) {
            switch (EmotionClassTemp[0][i]) {
                case "PA":
                    WordsEmotionClass[2][i] = "快乐";
                    break;
                case "PE":
                    WordsEmotionClass[2][i] = "安心";
                    break;
                case "PD":
                    WordsEmotionClass[2][i] = "尊敬";
                    break;
                case "PH":
                    WordsEmotionClass[2][i] = "赞扬";
                    break;
                case "PG":
                    WordsEmotionClass[2][i] = "相信";
                    break;
                case "PB":
                    WordsEmotionClass[2][i] = "喜爱";
                    break;
                case "PK":
                    WordsEmotionClass[2][i] = "祝愿";
                    break;
                case "NA":
                    WordsEmotionClass[2][i] = "愤怒";
                    break;
                case "NB":
                    WordsEmotionClass[2][i] = "悲伤";
                    break;
                case "NJ":
                    WordsEmotionClass[2][i] = "失望";
                    break;
                case "NH":
                    WordsEmotionClass[2][i] = "内疚";
                    break;
                case "PF":
                    WordsEmotionClass[2][i] = "思";
                    break;
                case "NI":
                    WordsEmotionClass[2][i] = "慌";
                    break;
                case "NC":
                    WordsEmotionClass[2][i] = "恐惧";
                    break;
                case "NG":
                    WordsEmotionClass[2][i] = "羞";
                    break;
                case "NE":
                    WordsEmotionClass[2][i] = "烦闷";
                    break;
                case "ND":
                    WordsEmotionClass[2][i] = "憎恶";
                    break;
                case "NN":
                    WordsEmotionClass[2][i] = "贬责";
                    break;
                case "NK":
                    WordsEmotionClass[2][i] = "妒忌";
                    break;
                case "NL":
                    WordsEmotionClass[2][i] = "怀疑";
                    break;
                case "PC":
                    WordsEmotionClass[2][i] = "惊奇";
                    break;
            }
        }
        return WordsEmotionClass;
    }

    //查找value在数组arr中的0个、1个或是多个索引位置
    public static int[] getIndex(String[] arr, String value) {
        List<Integer>  index = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                index.add(i);
            }
        }
        return index.stream().mapToInt(Integer::intValue).toArray();
    }

    //对第一列为词语、第二列为词语数量的String数组按照数量多少排序
    public static void FinalSort(String[][] Array) {
        if(Array != null && Array[0].length > 1){
            for(int i = 0; i < Array[0].length - 1; i++){
                // 初始化一个布尔值
                boolean flag = true;
                for(int j = 0; j < Array[0].length - i - 1 ; j++){
                    if(Integer.parseInt(Array[1][j]) < Integer.parseInt(Array[1][j+1])){
                        String classTemp;
                        String numTemp;
                        classTemp = Array[0][j];
                        Array[0][j] = Array[0][j+1];
                        Array[0][j+1] = classTemp;
                        numTemp = Array[1][j];
                        Array[1][j] = Array[1][j+1];
                        Array[1][j+1] = numTemp;
                        // 改变flag
                        flag = false;
                    }
                }
                if(flag){
                    break;
                }
            }
        }
    }

    //对第一列为小类情感（NE、PA等)、第二列为该类数目的String数组计算大类的数目并排序
    public static String LargeEmotionClass(String[][] WordsEmotionClassArray){
        int num = WordsEmotionClassArray[0].length;
        int[] LargeNum = new int[7];
        String[][] LargeEmotion = new String[2][7];
        String[] ChineseDescribe = new String[num];
        LargeEmotion[0][0] = "乐";
        LargeEmotion[0][1] = "好";
        LargeEmotion[0][2] = "怒";
        LargeEmotion[0][3] = "哀";
        LargeEmotion[0][4] = "惧";
        LargeEmotion[0][5] = "恶（wù）";
        LargeEmotion[0][6] = "惊";

        for (int i = 0; i < num; i++) {
            int EmotionNum = Integer.parseInt(WordsEmotionClassArray[1][i]);
            switch (WordsEmotionClassArray[0][i]){
                case "PA":
                    ChineseDescribe[i] = "快乐";
                    LargeNum[0] += EmotionNum;
                    break;
                case "PE":
                    ChineseDescribe[i] = "安心";
                    LargeNum[0] += EmotionNum;
                    break;
                case "PD":
                    ChineseDescribe[i] = "尊敬";
                    LargeNum[1] += EmotionNum;
                    break;
                case "PH":
                    ChineseDescribe[i] = "赞扬";
                    LargeNum[1] += EmotionNum;
                    break;
                case "PG":
                    ChineseDescribe[i] = "相信";
                    LargeNum[1] += EmotionNum;
                    break;
                case "PB":
                    ChineseDescribe[i] = "喜爱";
                    LargeNum[1] += EmotionNum;
                    break;
                case "PK":
                    ChineseDescribe[i] = "祝愿";
                    LargeNum[1] += EmotionNum;
                    break;
                case "NA":
                    ChineseDescribe[i] = "愤怒";
                    LargeNum[2] += EmotionNum;
                    break;
                case "NB":
                    ChineseDescribe[i] = "悲伤";
                    LargeNum[3] += EmotionNum;
                    break;
                case "NJ":
                    ChineseDescribe[i] = "失望";
                    LargeNum[3] += EmotionNum;
                    break;
                case "NH":
                    ChineseDescribe[i] = "内疚";
                    LargeNum[3] += EmotionNum;
                    break;
                case "PF":
                    ChineseDescribe[i] = "思";
                    LargeNum[3] += EmotionNum;
                    break;
                case "NI":
                    ChineseDescribe[i] = "慌";
                    LargeNum[4] += EmotionNum;
                    break;
                case "NC":
                    ChineseDescribe[i] = "恐惧";
                    LargeNum[4] += EmotionNum;
                    break;
                case "NG":
                    ChineseDescribe[i] = "羞";
                    LargeNum[4] += EmotionNum;
                    break;
                case "NE":
                    ChineseDescribe[i] = "烦闷";
                    LargeNum[5] += EmotionNum;
                    break;
                case "ND":
                    ChineseDescribe[i] = "憎恶";
                    LargeNum[5] += EmotionNum;
                    break;
                case "NN":
                    ChineseDescribe[i] = "贬责";
                    LargeNum[5] += EmotionNum;
                    break;
                case "NK":
                    ChineseDescribe[i] = "妒忌";
                    LargeNum[5] += EmotionNum;
                    break;
                case "NL":
                    ChineseDescribe[i] = "怀疑";
                    LargeNum[5] += EmotionNum;
                    break;
                case "PC":
                    ChineseDescribe[i] = "惊奇";
                    LargeNum[6] += EmotionNum;
                    break;
            }
        }
        LargeEmotion[1][0] = "" + LargeNum[0];
        LargeEmotion[1][1] = "" + LargeNum[1];
        LargeEmotion[1][2] = "" + LargeNum[2];
        LargeEmotion[1][3] = "" + LargeNum[3];
        LargeEmotion[1][4] = "" + LargeNum[4];
        LargeEmotion[1][5] = "" + LargeNum[5];
        LargeEmotion[1][6] = "" + LargeNum[6];
//        for (int i = 0; i < 7; i++) {
//            System.out.println(LargeNum[i]);
//        }

        FinalSort(LargeEmotion);
        String Describe;
        switch (ChineseDescribe.length){
            case 1:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0];
                break;
            case 2:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0] + "、" + ChineseDescribe[1];
                break;
            case 3:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0] + "、" + ChineseDescribe[1] + "、" + ChineseDescribe[2];
                break;
            case 4:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0] + "、" + ChineseDescribe[1]
                        + "，同时内心还有些" + ChineseDescribe[2] + "、" + ChineseDescribe[3];
                break;
            case 5:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0] + "、" + ChineseDescribe[1]
                        + "，同时内心还有些" + ChineseDescribe[2] + "、" + ChineseDescribe[3] + "、" + ChineseDescribe[4];
                break;
            default:
                Describe = "你总体感觉到" + LargeEmotion[0][0] + "，感觉" + ChineseDescribe[0] + "、" + ChineseDescribe[1] + "和" + ChineseDescribe[2]
                        + "，同时内心还有些" + ChineseDescribe[3] + "、" + ChineseDescribe[4] + "、" + ChineseDescribe[5];
        }
        return Describe;
    }

    public static String[][] SqlTest(String[][] SegmentFilter, WordMapper wordMapper){
        //批量读取数据库，筛选情感词（乱序、无重复）
        String[][] WordsList = Segment.WordSort(SegmentFilter[0]);
        List<Word> EmotionWords = wordMapper.queryWords(WordsList[0]);

        //对所有情感词按顺序排列，得到情感词的前置词，情感类型分类
        List<String> EmoClass = new ArrayList<>();
        List<String> inverseWords = new ArrayList<>();
        for (int i = 2; i < SegmentFilter[0].length; i++) {
            for(Word word: EmotionWords){
                if(SegmentFilter[0][i].equals(word.wordSelf)){
                    String WordClass = word.emotionClass;
                    EmoClass.add(WordClass);
                    if ((wordMapper.InverseJudge(SegmentFilter[0][i-1]) || wordMapper.InverseJudge(SegmentFilter[0][i-2]))) {
                        inverseWords.add(WordClass);
                    }
                    break;
                }
            }
        }

        String[] temp1 = EmoClass.toArray(new String[0]);
        String[] temp2 = inverseWords.toArray(new String[0]);
        String[][] EmotionClassTemp = Segment.WordSort(temp1);
        String[][] Inverse = Segment.WordSort(temp2);
        for (int i = 0; i < Inverse[0].length; i++) {
            for (int j = 0; j < EmotionClassTemp[0].length; j++) {
                if(Inverse[0][i].equals(EmotionClassTemp[0][j])){
                    int num = Integer.parseInt(EmotionClassTemp[1][j]) - Integer.parseInt(Inverse[1][i]);
                    EmotionClassTemp[1][j] = ""+num;
                }
            }
        }
        FinalSort(EmotionClassTemp);

        String[][] WordsEmotionClass = new String[3][EmotionClassTemp[0].length];
        WordsEmotionClass[0] = EmotionClassTemp[0];
        WordsEmotionClass[1] = EmotionClassTemp[1];
        for (int i = 0; i < EmotionClassTemp[0].length; i++) {
            switch (EmotionClassTemp[0][i]) {
                case "PA":
                    WordsEmotionClass[2][i] = "快乐";
                    break;
                case "PE":
                    WordsEmotionClass[2][i] = "安心";
                    break;
                case "PD":
                    WordsEmotionClass[2][i] = "尊敬";
                    break;
                case "PH":
                    WordsEmotionClass[2][i] = "赞扬";
                    break;
                case "PG":
                    WordsEmotionClass[2][i] = "相信";
                    break;
                case "PB":
                    WordsEmotionClass[2][i] = "喜爱";
                    break;
                case "PK":
                    WordsEmotionClass[2][i] = "祝愿";
                    break;
                case "NA":
                    WordsEmotionClass[2][i] = "愤怒";
                    break;
                case "NB":
                    WordsEmotionClass[2][i] = "悲伤";
                    break;
                case "NJ":
                    WordsEmotionClass[2][i] = "失望";
                    break;
                case "NH":
                    WordsEmotionClass[2][i] = "内疚";
                    break;
                case "PF":
                    WordsEmotionClass[2][i] = "思";
                    break;
                case "NI":
                    WordsEmotionClass[2][i] = "慌";
                    break;
                case "NC":
                    WordsEmotionClass[2][i] = "恐惧";
                    break;
                case "NG":
                    WordsEmotionClass[2][i] = "羞";
                    break;
                case "NE":
                    WordsEmotionClass[2][i] = "烦闷";
                    break;
                case "ND":
                    WordsEmotionClass[2][i] = "憎恶";
                    break;
                case "NN":
                    WordsEmotionClass[2][i] = "贬责";
                    break;
                case "NK":
                    WordsEmotionClass[2][i] = "妒忌";
                    break;
                case "NL":
                    WordsEmotionClass[2][i] = "怀疑";
                    break;
                case "PC":
                    WordsEmotionClass[2][i] = "惊奇";
                    break;
            }
        }

        return WordsEmotionClass;

    }

    public static double WordFilterPosNeg(String[] InitialSegment, String[] FinalSegment, WordMapper wordMapper){
        //批量读取数据库，筛选情感词（乱序、无重复）
        List<PosNegWord> PosNegs = wordMapper.FindPosNeg(FinalSegment);

        //对所有情感词按顺序排列，得到情感词的前置词，情感类型分类
        //List<String> EmoClass = new ArrayList<>();
        //List<String> inverseWords = new ArrayList<>();
        double score = 0;
        for (int i = 2; i < InitialSegment.length; i++) {
            for(PosNegWord posNeg : PosNegs){
                if(InitialSegment[i].trim().equals(posNeg.word.trim())){
                    double ExtentScore1 = 0;
                    double ExtentScore2 = 0;
                    double ExtentWord = 0;
                    if("pos".equals(posNeg.WordClass)){
                        ExtentWord = 1;
                    }
                    else if("neg".equals(posNeg.WordClass)){
                        ExtentWord = -1;
                    }

                    String pre1, pre2;
                    if(wordMapper.FindExtent(InitialSegment[i-1]) != null){
                        pre1 = wordMapper.FindExtent(InitialSegment[i-1]).Extent;
                    }
                    else{ pre1 = ""; }
                    if(wordMapper.FindExtent(InitialSegment[i-2]) != null){
                        pre2 = wordMapper.FindExtent(InitialSegment[i-2]).Extent;
                    }
                    else{ pre2 = ""; }

                    //System.out.println(pre1 + " " + pre2);
                    switch (pre1){
                        case "most":
                            ExtentScore1 = 2;
                            break;
                        case "over":
                            ExtentScore1 = 1.5;
                            break;
                        case "very":
                            ExtentScore1 = 1.25;
                            break;
                        case "more":
                            ExtentScore1 = 1.2;
                            break;
                        case "ish":
                            ExtentScore1 = 0.8;
                            break;
                        case "insufficiently":
                            ExtentScore1 = 0.5;
                            break;
                        case "inverse":
                            ExtentScore1 = -1;
                            break;
                        default:
                            ExtentScore1 = 1;
                    }
                    switch (pre2){
                        case "most":
                            ExtentScore2 = 2;
                            break;
                        case "over":
                            ExtentScore2 = 1.5;
                            break;
                        case "very":
                            ExtentScore2 = 1.25;
                            break;
                        case "more":
                            ExtentScore2 = 1.2;
                            break;
                        case "ish":
                            ExtentScore2 = 0.8;
                            break;
                        case "insufficiently":
                            ExtentScore2 = 0.5;
                            break;
                        case "inverse":
                            ExtentScore2 = -1;
                            break;
                        default:
                            ExtentScore2 = 1;
                    }

                    score += ExtentWord * ExtentScore1 * ExtentScore2;
                    //System.out.println(posNeg.word + " " + InitialSegment[i-2] + " " + InitialSegment[i-1] + " " + ExtentWord + " " + ExtentScore1 + " " + ExtentScore2);
                }
            }
        }

        return score;
    }

}



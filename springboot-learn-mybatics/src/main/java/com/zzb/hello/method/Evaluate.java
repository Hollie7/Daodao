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
        if(most!=null) return 2;
        else if(over!=null) return 1.5;
        else if(very!=null) return 1.25;
        else if(more!=null) return 1.2;
        else if(ish!=null) return 0.8;
        else if(insufficiently!=null) return 0.5;
        else if(inverse!=null) return -1;
        else return 1;
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

        for (int i = 1; i < wordsSize; i++){
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
        }

        //long time2 = System.currentTimeMillis();
        //int time = (int) ((time2 - time1)/1000);
        //System.out.println("执行了："+time+"秒！");

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
        for (int i = 0; i < FinalPos[0].length; i++) {
            score += 1 * Evaluate.WeightPoint(FinalPos[1][i], wordMapper);
        }
        for (int i = 0; i < FinalNeg[0].length; i++) {
            score += (-1) * Evaluate.WeightPoint(FinalNeg[1][i], wordMapper);
        }
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
            if(selectWord.size() == 0) continue;
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
            if(wordMapper.findInverseWord(preWord) != null) inverseWords.add(ClassWord);
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
        long time1 = System.currentTimeMillis();

        //获取数据库所有数据
        List<Word> AllWords = wordMapper.getAllWords();
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
        System.out.println(WordSum);
        //List<Word> selectWord = wordMapper.queryWordByWord("阻力");
        //String selectWord = "";
        List<String> EmoClass = new ArrayList<>();
        List<String> inverseWords = new ArrayList<>();
        for (int i = 1; i < WordSum; i++) {
            int[] wordIndex = getIndex(AllWordsString[0], SegmentFilter[0][i]);
            //selectWord = wordMapper.queryWordByWord(SegmentFilter[0][i]);
            String preWord = SegmentFilter[0][i-1];
            String ClassWord = "";
            if(wordIndex == null) continue;
            else if(wordIndex.length == 1){
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
            if(wordMapper.findInverseWord(preWord) != null) inverseWords.add(ClassWord);
        }

        long time2 = System.currentTimeMillis();
        long time = time2 - time1;
        System.out.println("执行了："+time+"ms！");

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
        FinalSort(WordsEmotionClass);
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
    public static void FinalSort(String[][] WordsEmotionClass) {
        if(WordsEmotionClass != null && WordsEmotionClass[0].length > 1){
            for(int i = 0; i < WordsEmotionClass[0].length - 1; i++){
                // 初始化一个布尔值
                boolean flag = true;
                for(int j = 0; j < WordsEmotionClass[0].length - i - 1 ; j++){
                    if(Integer.parseInt(WordsEmotionClass[1][j]) < Integer.parseInt(WordsEmotionClass[1][j+1])){
                        String classTemp;
                        String numTemp;
                        classTemp = WordsEmotionClass[0][j];
                        WordsEmotionClass[0][j] = WordsEmotionClass[0][j+1];
                        WordsEmotionClass[0][j+1] = classTemp;
                        numTemp = WordsEmotionClass[1][j];
                        WordsEmotionClass[1][j] = WordsEmotionClass[1][j+1];
                        WordsEmotionClass[1][j+1] = numTemp;
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
}



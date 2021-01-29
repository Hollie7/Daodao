package com.zzb.hello.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzb.hello.mapper.WordMapper;
import com.zzb.hello.method.*;
import com.zzb.hello.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SpringController {
    //此处为输入的一段话，后续由Test中的方式从小程序获得
    static String str2 = "最近一段时间，大概从 12 月开始的，情绪特别低落，12 月是觉得不开心和沮丧，1 月份以来的最近几天，每天都会哭，" +
            "不知道为什么，莫名就觉得孤单，被抛弃，自己一无是处。以前情绪不好的时候，哭过一次大概就好了，三天前，因为觉得特别孤独，又没人可以说，" +
            "特别难过，哭了一个晚上，但是第二天，电话里，男票的一句很普通的话戳中了我，他说——下学期准备考驾照，反正你也不想考。我瞬间就有被抛弃的感觉了，" +
            "觉得我不想长大，只想躲起来，看着人群来来往往，却没有人可以陪我，和我同路，听我说话，我只能是一个人。可能是大三上学期就要结束了，" +
            "不是考研就是要找工作了，回首了下目前为止的大学，想的好多要做的事，都没有做，大部分精力都用在和惰性斗争了，心里有点恐惧和自责。" +
            "这几天几乎每天都要哭，还是觉得情绪里有无穷无尽的悲伤，低沉地我不知道怎么办了，我性格本来就有点内向，不能尽情开心，压抑着自己的那种，" +
            "平常大多是不高兴也不悲伤的情绪，看过一些心理学的书，怀疑自己可能有神经症，大一暑假去号脉的时候，医生说我有神经官能紊乱，那时候就有点不开心，" +
            "现在情绪的情况这样，要不要去看医生吃药？真的好难受啊。" ;

    String str1;
    String[][] InitialSegment;
    String[][] SegmentFilterWords;
    List<String[]> WordsClass;
    String[][] WordsSort;
    List<String[][]> PosNegWords;
    double EmotionScore;
    String[][] WordsEmotionClass;
    String Describe;

    @Autowired
    private WordMapper wordMapper;

    @RequestMapping("/Wordcloud")
    @ResponseBody
    public String WordCloud(){
        JSONObject json = new JSONObject(new LinkedHashMap());
        JSONArray wordCloud = new JSONArray();
        JSONObject radarPlot = new JSONObject(new LinkedHashMap());
        for (int i = 0; i < 30; i++) {
            JSONObject o1 = new JSONObject(new LinkedHashMap());
            int num = 5 * new Double(Math.round(Math.sqrt(5 * Integer.parseInt(WordsSort[1][i])))).intValue();
            o1.put("name", WordsSort[0][i]);
            o1.put("textSize", num);
            wordCloud.add(o1);
        }

        String[] Emotion = new String[WordsEmotionClass[0].length];
        for (int i = 0; i < WordsEmotionClass[0].length; i++) {
            Emotion[i]= WordsEmotionClass[0][i] +'('+ WordsEmotionClass[1][i] + ')';
        }

        radarPlot.put("categories", Emotion);
        JSONObject series = new JSONObject(new LinkedHashMap());
        series.put("name", "情绪特征雷达图");
        series.put("data", WordsEmotionClass[1]);
        radarPlot.put("series", series);


        String ScoreOfEmotion = "" + EmotionScore;
        json.put("wordCloud", wordCloud);
        json.put("RadarPlot", radarPlot);
        json.put("EmotionScore", ScoreOfEmotion);
        json.put("EmotionDescribe", Describe);
        return json.toJSONString();
    }

    @RequestMapping("/Spring")
    public List<String[]> Spring(){
        List<String[]> wordsDivided = Segment.SegmentWordClass(str2);

        return wordsDivided;
    }

    //目前主要的实现
    @RequestMapping("/Words")
    public List<String[]> Words(){
        long time1 = System.currentTimeMillis();
        //初始分词结果，包括词语及其词性
        String[][] wordsDivided = Segment.SegmentWord(str2);

        //仅保留名词、动词、形容词、副词、数词和成语，包括词语及其词性
        String[][] SegmentFilter = Segment.SegmentFilter(wordsDivided);

        //依据名词、动词、形容词、副词、数词、成语顺序归纳整理词语，只包括词语
        List<String[]> ClassWords = Segment.WordClass(wordsDivided);

        //仅保留动词、名词、形容词、副词、数词（包括时间）和成语及代词，仅包括词语
        String[] Words = SegmentFilter[0];

        //将词语按照出现次数排序，返回词语及其数量，用作绘制词云图，需要返回给小程序
        String[][] SortWords = Segment.WordSort(Words);
        for (int i = 0; i < SortWords[0].length; i++) {
            System.out.println(SortWords[0][i]+" "+SortWords[1][i]);
        }
        //System.out.println(Arrays.toString(SortWords[0])+" "+Arrays.toString(SortWords[1]));

        //词语分类，筛选出正向词、负向词及各自的前一个词，用作后续计算情感值
        List<String[][]> FilterWords = Evaluate.WordFilter(Words, wordMapper);
        String[][] FinalPos = FilterWords.get(0);
        String[][] FinalNeg = FilterWords.get(1);
        System.out.println("正向词"+Arrays.toString(FinalPos[0]) +" "+ Arrays.toString(FinalPos[1]));
        System.out.println("负向词"+Arrays.toString(FinalNeg[0]) +" "+ Arrays.toString(FinalNeg[1]));

        //计算情感值，需要返回给小程序
        System.out.println(Evaluate.EmotionScore(FilterWords, wordMapper));

        //情感分类，情感类型呈现，需要返回给小程序
        //String[][] WordsEmotionClass = Evaluate.EmotionClass(SegmentFilter, wordMapper);
        //System.out.println(Arrays.toString(WordsEmotionClass[0]) +" "+ Arrays.toString(WordsEmotionClass[1]));

        String[][] WordsEmotionClass2 = Evaluate.EmotionClassArray(SegmentFilter, wordMapper);
        System.out.println(Arrays.toString(WordsEmotionClass2[0]) +" "+ Arrays.toString(WordsEmotionClass2[1]));

        long time2 = System.currentTimeMillis();
        int time = (int) ((time2 - time1)/1000);
        System.out.println("执行了："+time+"秒！");
        return ClassWords;
    }

    //此处为从微信获取文本的尝试
    @RequestMapping("/Test")
    @ResponseBody
    public String Test(@RequestParam(value = "text") String text){
        System.out.println(text);
        str1 = text;
        DoneAll(str1);
        return WordCloud();
    }

    //处理所有步骤，获得需要的所有变量值
    @RequestMapping("/DoneAll")
    public String DoneAll(String s){
        String str = s;
        //初始分词结果，包括词语及其词性
        InitialSegment = Segment.SegmentWord(str);

        //仅保留名词、动词、形容词、副词、数词和成语，包括词语及其词性
        SegmentFilterWords = Segment.SegmentFilter(InitialSegment);

        //依据名词、动词、形容词、副词、数词、成语顺序归纳整理词语，只包括词语
        WordsClass = Segment.WordClass(InitialSegment);

        //仅保留动词、名词、形容词、副词、数词（包括时间）和成语及代词，仅包括词语
        String[] Words = SegmentFilterWords[0];

        //将词语按照出现次数排序，返回词语及其数量，用作绘制词云图，需要返回给小程序
        WordsSort = Segment.WordSort(Words);
        for (int i = 0; i < WordsSort[0].length; i++) {
            System.out.println(WordsSort[0][i]+" "+WordsSort[1][i]);
        }

        //词语分类，筛选出正向词、负向词及各自的前一个词，用作后续计算情感值
        PosNegWords = Evaluate.WordFilter(Words, wordMapper);
        String[][] FinalPos = PosNegWords.get(0);
        String[][] FinalNeg = PosNegWords.get(1);
        System.out.println("正向词"+Arrays.toString(FinalPos[0]) +" "+ Arrays.toString(FinalPos[1]));
        System.out.println("负向词"+Arrays.toString(FinalNeg[0]) +" "+ Arrays.toString(FinalNeg[1]));

        //计算情感值，需要返回给小程序
        EmotionScore = Evaluate.EmotionScore(PosNegWords, wordMapper);
        System.out.println(EmotionScore);

        //情感分类，情感类型呈现，需要返回给小程序
        WordsEmotionClass = Evaluate.EmotionClassArray(SegmentFilterWords, wordMapper);
        System.out.println(Arrays.toString(WordsEmotionClass[0]) +" "+ Arrays.toString(WordsEmotionClass[1]));

        Describe = Evaluate.LargeEmotionClass(WordsEmotionClass);

        return "Operate Successfully";
    }

    //初始分词结果
    @RequestMapping("/InitialSegment")
    public String[][] InitialSegment(){
        return InitialSegment;
    }

    //初筛后的分词结果
    @RequestMapping("/SegmentFilter")
    public String[][] SegmentFilter(){
        return SegmentFilterWords;
    }

    //按照词性分类的词语结果
    @RequestMapping("/WordsClass")
    public List<String[]> WordsClass(){
        return WordsClass;
    }

    //按照词语出现频率排序的结果
    @RequestMapping("/WordsSort")
    public String[][] WordsSort(){
        return WordsSort;
    }

    //正负词性分类
    @RequestMapping("/PosNegWords")
    public List<String[][]> PosNegWords(){
        return PosNegWords;
    }

    //情感分数
    @RequestMapping("/EmotionScore")
    public double EmotionScore(){
        return EmotionScore;
    }

    //情感分类
    @RequestMapping("/WordsEmotionClass")
    public String[][] WordsEmotionClass(){
        return WordsEmotionClass;
    }
}

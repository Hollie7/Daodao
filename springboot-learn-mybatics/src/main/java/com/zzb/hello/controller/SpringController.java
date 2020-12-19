package com.zzb.hello.controller;

import com.zzb.hello.mapper.WordMapper;
import com.zzb.hello.method.*;
import com.zzb.hello.pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SpringController {
    //此处为输入的一段话，后续由Test中的方式从小程序获得
    static String str = "最近一段时间，大概从 12 月开始的，情绪特别低落，12 月是觉得不开心和沮丧，1 月份以来的最近几天，每天都会哭，" +
            "不知道为什么，莫名就觉得孤单，被抛弃，自己一无是处。以前情绪不好的时候，哭过一次大概就好了，三天前，因为觉得特别孤独，又没人可以说，" +
            "特别难过，哭了一个晚上，但是第二天，电话里，男票的一句很普通的话戳中了我，他说——下学期准备考驾照，反正你也不想考。我瞬间就有被抛弃的感觉了，" +
            "觉得我不想长大，只想躲起来，看着人群来来往往，却没有人可以陪我，和我同路，听我说话，我只能是一个人。可能是大三上学期就要结束了，" +
            "不是考研就是要找工作了，回首了下目前为止的大学，想的好多要做的事，都没有做，大部分精力都用在和惰性斗争了，心里有点恐惧和自责。" +
            "这几天几乎每天都要哭，还是觉得情绪里有无穷无尽的悲伤，低沉地我不知道怎么办了，我性格本来就有点内向，不能尽情开心，压抑着自己的那种，" +
            "平常大多是不高兴也不悲伤的情绪，看过一些心理学的书，怀疑自己可能有神经症，大一暑假去号脉的时候，医生说我有神经官能紊乱，那时候就有点不开心，" +
            "现在情绪的情况这样，要不要去看医生吃药？真的好难受啊。" ;


    @Autowired
    private WordMapper wordMapper;

    @RequestMapping("/Spring")
    public List<String[]> Spring(){
        List<String[]> wordsDivided = Segment.SegmentWordClass(str);

        return wordsDivided;
    }

    //目前主要的实现
    @RequestMapping("/Words")
    public List<String[]> Words(){
        long time1 = System.currentTimeMillis();
        //初始分词结果，包括词语及其词性
        String[][] wordsDivided = Segment.SegmentWord(str);

        //仅保留名词、动词、形容词、副词、数词和成语，包括词语及其词性
        String[][] SegmentFilter = Segment.SegmentFilter(wordsDivided);

        //依据名词、动词、形容词、副词、数词、成语顺序归纳整理词语，只包括词语
        List<String[]> ClassWords = Segment.WordClass(wordsDivided);

        //仅保留动词、名词、形容词、副词、数词（包括时间）和成语及代词，仅包括词语
        String[] Words = SegmentFilter[0];

        //将词语按照出现次数排序，返回词语及其数量，用作绘制词云图，需要返回给小程序
        String[][] SortWords = Segment.WordSort(Words);
        System.out.println(Arrays.toString(SortWords[0])+" "+Arrays.toString(SortWords[1]));

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
    public String Test(@RequestParam(value = "text") String text){
        //System.out.println(text);
        String[][] wordsDivided = Segment.SegmentWord(text);
        int wordNum = wordsDivided[0].length;
        System.out.println(wordNum);
        List<Word> selectWord;
        for (int i = 0; i < wordNum; i++) {
            selectWord = wordMapper.queryWordByWord(wordsDivided[0][i]);
            if(selectWord == null) continue;
            System.out.print(selectWord.get(0).wordSelf+' '+selectWord.get(0).wordClass+' '+selectWord.get(0).emotionClass);
            System.out.println();
        }
        System.out.println("success");
        return  "success";
    }
}

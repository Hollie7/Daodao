package com.zzb.hello.method;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.*;
import org.ansj.splitWord.analysis.*;

import java.util.*;

public class Segment {
    public static String[][] SegmentWord(String str){
        //定义一个int变量，用于存放分词后的词语数目
        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("w"); //过滤词性
        filter.insertStopNatures("null");
        filter.insertStopWords("我"); //过滤单词
        filter.insertStopWords("就"); //过滤单词
        filter.insertStopWords("要"); //过滤单词
        filter.insertStopWords("有"); //过滤单词
        filter.insertStopWords("是"); //过滤单词
        filter.insertStopWords("都"); //过滤单词
        filter.insertStopWords("说"); //过滤单词
        filter.insertStopWords("好"); //过滤单词
        filter.insertStopWords("想"); //过滤单词
        filter.insertStopWords("可能"); //过滤单词
        filter.insertStopWords("大"); //过滤单词
        filter.insertStopWords("小"); //过滤单词

        Result result = DicAnalysis.parse(str).recognition(filter); //分词结果的一个封装，主要是一个List<Term>的terms

        List<Term> terms = result.getTerms(); //拿到terms
        int termsSize = terms.size();

        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> natureStrs = new ArrayList<>();
        int validNum = 0;
        for (int i=0; i<termsSize; i++){
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            words.add(word);
            natureStrs.add(natureStr);
            validNum++;
        }

        String[] wordsString = words.toArray(new String[validNum]);
        String[] natureStrsString = natureStrs.toArray(new String[validNum]);
        String[][] wordsDivided = new String[2][validNum];
        wordsDivided[0] = wordsString;
        wordsDivided[1] = natureStrsString;

        return wordsDivided;
    }

    public static List<String[]> SegmentWordClass(String str){
        //定义一个int变量，用于存放分词后的词语数目
        int termsSize;

        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("w"); //过滤词性
        filter.insertStopNatures("null");
        filter.insertStopWords("我"); //过滤单词
        filter.insertStopWords("就"); //过滤单词
        filter.insertStopWords("要"); //过滤单词
        filter.insertStopWords("有"); //过滤单词
        filter.insertStopWords("是"); //过滤单词
        filter.insertStopWords("都"); //过滤单词
        filter.insertStopWords("说"); //过滤单词
        filter.insertStopWords("好"); //过滤单词
        filter.insertStopWords("想"); //过滤单词
        filter.insertStopWords("可能"); //过滤单词
        filter.insertStopWords("大"); //过滤单词
        filter.insertStopWords("小"); //过滤单词

        Result result = DicAnalysis.parse(str).recognition(filter); //分词结果的一个封装，主要是一个List<Term>的terms
        //System.out.println(result.getTerms());

        List<Term> terms = result.getTerms(); //拿到terms
        termsSize = terms.size();

        List<String> NounWords = new ArrayList<>();
        List<String> VerbWords = new ArrayList<>();
        List<String> AdjWords = new ArrayList<>();
        List<String> AdvWords = new ArrayList<>();
        List<String> NumTimeWords = new ArrayList<>();
        List<String> OtherWords = new ArrayList<>();

        String word;
        String natureStr;
        for (int i=0; i<termsSize; i++){
            word = terms.get(i).getName(); //拿到词
            natureStr = terms.get(i).getNatureStr(); //拿到词性
            /*if(natureStr.equals("w") || natureStr.equals("null")) {
                continue;
            }*/
            //数词与量词组合
            if(i<termsSize-1 & "m".equals(natureStr) & "q".equals(terms.get(i+1).getNatureStr())) {
                System.out.println(word);
                System.out.println(terms.get(i+1).getName());
                natureStr = "mq";
                word = word + terms.get(i+1).getName();
                i++;

            }

            //形容词
            if("a".equals(natureStr) || "Ag".equals(natureStr)){
                AdjWords.add(word);
            }
            //名词
            else if("an".equals(natureStr) || "Ng".equals(natureStr) || "n".equals(natureStr) || "nr".equals(natureStr) ||
                    "ns".equals(natureStr) || "nt".equals(natureStr)  || "nx".equals(natureStr) ||
                    "nz".equals(natureStr) || "Vg".equals(natureStr) || "vn".equals(natureStr)){
                NounWords.add(word);
            }
            //动词
            else if("v".equals(natureStr)){
                VerbWords.add(word);
            }
            //副词
            else if("d".equals(natureStr) || "vd".equals(natureStr) || "ad".equals(natureStr) || "Dg".equals(natureStr) ||
                    "z".equals(natureStr)){
                AdvWords.add(word);
            }
            //数词与时间
            else if("m".equals(natureStr) || "mg".equals(natureStr) || "mq".equals(natureStr) || "t".equals(natureStr) ||
                    "tg".equals(natureStr) || "q".equals(natureStr)){
                NumTimeWords.add(word);
            }
            else{
                OtherWords.add(word);
            }
        }
        String[] Nouns=NounWords.toArray(new String[0]);
        String[] Verbs=VerbWords.toArray(new String[0]);
        String[] Adjs=AdjWords.toArray(new String[0]);
        String[] Advs=AdvWords.toArray(new String[0]);
        String[] NumTimes=NumTimeWords.toArray(new String[0]);
        String[] Others=OtherWords.toArray(new String[0]);

        List<String[]> list = new ArrayList<>();
        list.add(Nouns);
        list.add(Verbs);
        list.add(Adjs);
        list.add(Advs);
        list.add(NumTimes);
        list.add(Others);
        return list;
    }

    public static List<String[]> WordClass(String[][] wordsDivided){
        int wordsSize = wordsDivided[0].length;
        //System.out.println(wordsSize);

        List<String> NounWords = new ArrayList<>();
        List<String> VerbWords = new ArrayList<>();
        List<String> AdjWords = new ArrayList<>();
        List<String> AdvWords = new ArrayList<>();
        List<String> NumTimeWords = new ArrayList<>();
        List<String> OtherWords = new ArrayList<>();
        String word;
        String natureStr;
        for (int i=0; i<wordsSize; i++){
            word = wordsDivided[0][i]; //拿到词
            natureStr = wordsDivided[1][i]; //拿到词性
            if("w".equals(natureStr) || "null".equals(natureStr)) {
                continue;
            }
            //数词与量词组合
            if(i<wordsSize-2) {
                if("m".equals(natureStr) & "q".equals(wordsDivided[1][i+1])){
                    natureStr = "mq";
                    word = word + wordsDivided[0][i+1];
                    System.out.println(word);
                    i++;
                }
            }

            //形容词
            if("an".equals(natureStr) || "a".equals(natureStr) || "Ag".equals(natureStr) || "ad".equals(natureStr) ){
                AdjWords.add(word);
            }
            //名词
            else if("Ng".equals(natureStr) || "n".equals(natureStr) || "nr".equals(natureStr) ||
                    "ns".equals(natureStr) || "nt".equals(natureStr)  || "nx".equals(natureStr) ||
                    "nz".equals(natureStr) ){
                NounWords.add(word);
            }
            //动词
            else if("v".equals(natureStr) || "vd".equals(natureStr) || "Vg".equals(natureStr) || "vn".equals(natureStr)){
                VerbWords.add(word);
            }
            //副词
            else if("d".equals(natureStr) || "Dg".equals(natureStr)){
                AdvWords.add(word);
            }
            //数词与时间
            else if("m".equals(natureStr) || "mg".equals(natureStr) || "mq".equals(natureStr) || "t".equals(natureStr) ||
                    "tg".equals(natureStr) || "q".equals(natureStr)){
                NumTimeWords.add(word);
            }
            //成语或代词
            else if("i".equals(natureStr) || "r".equals(natureStr)){
                OtherWords.add(word);
            }
        }
        String[] Nouns=NounWords.toArray(new String[0]);
        String[] Verbs=VerbWords.toArray(new String[0]);
        String[] Adjs=AdjWords.toArray(new String[0]);
        String[] Advs=AdvWords.toArray(new String[0]);
        String[] NumTimes=NumTimeWords.toArray(new String[0]);
        String[] Others=OtherWords.toArray(new String[0]);

        List<String[]> list = new ArrayList<>();
        list.add(Nouns);
        list.add(Verbs);
        list.add(Adjs);
        list.add(Advs);
        list.add(NumTimes);
        list.add(Others);
        return list;
    }

    public static String[][] WordSort(String[] args) {
        /*TreeMap < String, Integer > map = new TreeMap <> (new Comparator<>(){
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });*/
        Map<String, Integer> map = new TreeMap<String, Integer>();
        for (String n : args) {
            if (null == map.get(n)) {
                map.put(n, 1);
            } else {
                int x = map.get(n);
                map.put(n, x + 1);
            }

        }
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
                //return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        String[][] SortWords = new String[2][infoIds.size()];
        for (int i = 0; i < infoIds.size(); i++) {
            //System.out.println(infoIds.get(i).getKey()+' '+infoIds.get(i).getValue());
            SortWords[0][i] = infoIds.get(i).getKey();
            SortWords[1][i] = infoIds.get(i).getValue().toString();
        }
        return SortWords;
    }

    public static String[][] SegmentFilter(String[][] wordsDivided){
        //定义一个int变量，用于存放分词后的词语数目
        int termsSize = wordsDivided[0].length;

        List<String> Words = new ArrayList<>();
        List<String> NatureStrs= new ArrayList<>();

        String word;
        String natureStr;
        for (int i=0; i<termsSize; i++){
            word = wordsDivided[0][i]; //拿到词
            natureStr = wordsDivided[1][i]; //拿到词性
            if("w".equals(natureStr) || "null".equals(natureStr)) {
                continue;
            }
            if(i<termsSize-2) {
                if("m".equals(natureStr) & "q".equals(wordsDivided[1][i+1])){
                    natureStr = "mq";
                    word = word + wordsDivided[0][i+1];
                    i++;
                }
            }

            //形容词

            if("an".equals(natureStr) || "a".equals(natureStr) || "Ag".equals(natureStr) || "ad".equals(natureStr) ){
                Words.add(word);
                NatureStrs.add("adj");
                //System.out.println(word+" "+natureStr);
            }
            //名词
            else if("Ng".equals(natureStr) || "n".equals(natureStr) || "nr".equals(natureStr) ||
                    "ns".equals(natureStr) || "nt".equals(natureStr)  || "nx".equals(natureStr) ||
                    "nz".equals(natureStr) ){
                Words.add(word);
                NatureStrs.add("noun");
                //System.out.println(word+" "+natureStr);
            }
            //动词
            else if("v".equals(natureStr) || "vd".equals(natureStr) || "Vg".equals(natureStr) || "vn".equals(natureStr)){
                Words.add(word);
                NatureStrs.add("verb");
                //System.out.println(word+" "+natureStr);
            }
            //副词
            else if("d".equals(natureStr) || "Dg".equals(natureStr)){
                Words.add(word);
                NatureStrs.add("adv");
                //System.out.println(word+" "+natureStr);
            }
            //数词与时间
            else if("m".equals(natureStr) || "mg".equals(natureStr) || "mq".equals(natureStr) || "t".equals(natureStr) ||
                    "tg".equals(natureStr) || "q".equals(natureStr)){
                Words.add(word);
                NatureStrs.add("num_time");
                //System.out.println(word+" "+natureStr);
            }
            //成语或代词
            else if("i".equals(natureStr) || "r".equals(natureStr)){
                Words.add(word);
                NatureStrs.add("idiom");
                //System.out.println(word+" "+natureStr);
            }
        }

        String[] wordsString = Words.toArray(new String[0]);
        String[] natureStrsString = NatureStrs.toArray(new String[0]);
        String[][] FilterWords = new String[2][NatureStrs.size()];
        FilterWords[0] = wordsString;
        FilterWords[1] = natureStrsString;
        return FilterWords;
    }
}

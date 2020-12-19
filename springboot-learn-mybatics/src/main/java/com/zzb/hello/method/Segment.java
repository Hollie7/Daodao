package com.zzb.hello.method;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.*;

import java.util.*;

public class Segment {
    public static String[][] SegmentWord(String str){
        //定义一个int变量，用于存放分词后的词语数目
        int termsSize = 0;

        Result result = DicAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms

        List<Term> terms = result.getTerms(); //拿到terms
        termsSize = terms.size();

        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> natureStrs = new ArrayList<>();
        int validNum = 0;
        for (int i=0; i<termsSize; i++){
            String word = terms.get(i).getName().toString(); //拿到词
            String natureStr = terms.get(i).getNatureStr().toString(); //拿到词性
            if(natureStr.equals("w") || natureStr.equals("null")) {
                continue;
            }

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
        int termsSize = 0;

        Result result = DicAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms
        //System.out.println(result.getTerms());

        List<Term> terms = result.getTerms(); //拿到terms
        termsSize = terms.size();

        List<String> NounWords = new ArrayList<>();
        List<String> VerbWords = new ArrayList<>();
        List<String> AdjWords = new ArrayList<>();
        List<String> AdvWords = new ArrayList<>();
        List<String> NumTimeWords = new ArrayList<>();
        List<String> OtherWords = new ArrayList<>();

        String word = "";
        String natureStr = "";
        for (int i=0; i<termsSize; i++){
            word = terms.get(i).getName(); //拿到词
            natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(natureStr.equals("w") || natureStr.equals("null")) {
                continue;
            }
            //数词与量词组合
            if(i<termsSize-1 & natureStr.equals("m") & terms.get(i+1).getNatureStr().equals("q")) {
                System.out.println(word);
                System.out.println(terms.get(i+1).getName());
                natureStr = "mq";
                word = (String)word + terms.get(i+1).getName();
                i++;

            }

            //形容词
            if(natureStr.equals("a") || natureStr.equals("Ag")){
                AdjWords.add(word);
            }
            //名词
            else if(natureStr.equals("an") || natureStr.equals("Ng") || natureStr.equals("n") || natureStr.equals("nr") ||
                    natureStr.equals("ns") || natureStr.equals("nt")  || natureStr.equals("nx") ||
                    natureStr.equals("nz") || natureStr.equals("Vg") || natureStr.equals("vn")){
                NounWords.add(word);
            }
            //动词
            else if(natureStr.equals("v")){
                VerbWords.add(word);
            }
            //副词
            else if(natureStr.equals("d") || natureStr.equals("vd") || natureStr.equals("ad") || natureStr.equals("Dg") ||
                    natureStr.equals("z")){
                AdvWords.add(word);
            }
            //数词与时间
            else if(natureStr.equals("m") || natureStr.equals("mg") || natureStr.equals("mq") || natureStr.equals("t") ||
                    natureStr.equals("tg") || natureStr.equals("q")){
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

        List<String[]> list = new ArrayList<String[]>();
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
        System.out.println(wordsSize);

        List<String> NounWords = new ArrayList<>();
        List<String> VerbWords = new ArrayList<>();
        List<String> AdjWords = new ArrayList<>();
        List<String> AdvWords = new ArrayList<>();
        List<String> NumTimeWords = new ArrayList<>();
        List<String> OtherWords = new ArrayList<>();
        String word = "";
        String natureStr = "";
        for (int i=0; i<wordsSize; i++){
            word = wordsDivided[0][i]; //拿到词
            natureStr = wordsDivided[1][i]; //拿到词性
            if(natureStr.equals("w") || natureStr.equals("null")) {
                continue;
            }
            //数词与量词组合
            if(i<wordsSize-2) {
                if(natureStr.equals("m") & wordsDivided[1][i+1].equals("q")){
                    natureStr = "mq";
                    word = (String)word + wordsDivided[0][i+1];
                    System.out.println(word);
                    i++;
                }
            }

            //形容词
            if(natureStr.equals("an") || natureStr.equals("a") || natureStr.equals("Ag") || natureStr.equals("ad") ){
                AdjWords.add(word);
            }
            //名词
            else if(natureStr.equals("Ng") || natureStr.equals("n") || natureStr.equals("nr") ||
                    natureStr.equals("ns") || natureStr.equals("nt")  || natureStr.equals("nx") ||
                    natureStr.equals("nz") ){
                NounWords.add(word);
            }
            //动词
            else if(natureStr.equals("v") || natureStr.equals("vd") || natureStr.equals("Vg") || natureStr.equals("vn")){
                VerbWords.add(word);
            }
            //副词
            else if(natureStr.equals("d") || natureStr.equals("Dg")){
                AdvWords.add(word);
            }
            //数词与时间
            else if(natureStr.equals("m") || natureStr.equals("mg") || natureStr.equals("mq") || natureStr.equals("t") ||
                    natureStr.equals("tg") || natureStr.equals("q")){
                NumTimeWords.add(word);
            }
            //成语或代词
            else if(natureStr.equals("i") || natureStr.equals("r")){
                OtherWords.add(word);
            }
        }
        String[] Nouns=NounWords.toArray(new String[0]);
        String[] Verbs=VerbWords.toArray(new String[0]);
        String[] Adjs=AdjWords.toArray(new String[0]);
        String[] Advs=AdvWords.toArray(new String[0]);
        String[] NumTimes=NumTimeWords.toArray(new String[0]);
        String[] Others=OtherWords.toArray(new String[0]);

        List<String[]> list = new ArrayList<String[]>();
        list.add(Nouns);
        list.add(Verbs);
        list.add(Adjs);
        list.add(Advs);
        list.add(NumTimes);
        list.add(Others);
        return list;
    }

    public static String[][] WordSort(String[] args) {
        TreeMap < String, Integer > map = new TreeMap < String, Integer > (new Comparator<String>(){
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        int num = args.length;

        for (int i = 0; i < num; i++) {
            String n = args[i];
            if (null == map.get(n)) {
                map.put(n, 1);
            } else {
                int x = map.get(n);
                map.put(n, x + 1);
            }

        }
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
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

        String word = "";
        String natureStr = "";
        for (int i=0; i<termsSize; i++){
            word = wordsDivided[0][i]; //拿到词
            natureStr = wordsDivided[1][i]; //拿到词性
            if(natureStr.equals("w") || natureStr.equals("null")) {
                continue;
            }
            if(i<termsSize-2) {
                if(natureStr.equals("m") & wordsDivided[1][i+1].equals("q")){
                    natureStr = "mq";
                    word = (String)word + wordsDivided[0][i+1];
                    i++;
                }
            }

            //形容词

            if(natureStr.equals("an") || natureStr.equals("a") || natureStr.equals("Ag") || natureStr.equals("ad") ){
                Words.add(word);
                NatureStrs.add("adj");
                System.out.println(word+" "+natureStr);
            }
            //名词
            else if(natureStr.equals("Ng") || natureStr.equals("n") || natureStr.equals("nr") ||
                    natureStr.equals("ns") || natureStr.equals("nt")  || natureStr.equals("nx") ||
                    natureStr.equals("nz") ){
                Words.add(word);
                NatureStrs.add("noun");
                System.out.println(word+" "+natureStr);
            }
            //动词
            else if(natureStr.equals("v") || natureStr.equals("vd") || natureStr.equals("Vg") || natureStr.equals("vn")){
                Words.add(word);
                NatureStrs.add("verb");
                System.out.println(word+" "+natureStr);
            }
            //副词
            else if(natureStr.equals("d") || natureStr.equals("Dg")){
                Words.add(word);
                NatureStrs.add("adv");
                System.out.println(word+" "+natureStr);
            }
            //数词与时间
            else if(natureStr.equals("m") || natureStr.equals("mg") || natureStr.equals("mq") || natureStr.equals("t") ||
                    natureStr.equals("tg") || natureStr.equals("q")){
                Words.add(word);
                NatureStrs.add("num_time");
                System.out.println(word+" "+natureStr);
            }
            //成语或代词
            else if(natureStr.equals("i") || natureStr.equals("r")){
                Words.add(word);
                NatureStrs.add("idiom");
                System.out.println(word+" "+natureStr);
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

package com.zzb.hello.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramTotal {
    private String str1;
    private String[][] InitialSegment;
    private String[][] SegmentFilterWords;
    private List<String[]> WordsClass;
    private String[][] WordsSort;
    private List<String[][]> PosNegWords;
    private double EmotionScore;
    private String[][] WordsEmotionClass;
    private String Describe;
    private int SentenceLen;

    public void setStr1(String s){
        this.str1 = s;
    }
    public String getStr1(){
        return this.str1;
    }


    public void setInitialSegment(String[][] s){
        this.InitialSegment = s;
    }
    public String[][] getInitialSegment(){
        return this.InitialSegment;
    }


    public void setSegmentFilterWords(String[][] s){
        this.SegmentFilterWords = s;
    }
    public String[][] getSegmentFilterWords(){
        return this.SegmentFilterWords;
    }


    public void setWordsClass(List<String[]> s){
        this.WordsClass = s;
    }
    public List<String[]> getWordsClass(){
        return this.WordsClass;
    }


    public void setWordsSort(String[][] s){
        this.WordsSort = s;
    }
    public String[][] getWordsSort(){
        return this.WordsSort;
    }


    public void setPosNegWords(List<String[][]> s){
        this.PosNegWords = s;
    }
    public List<String[][]> getPosNegWords(){
        return this.PosNegWords;
    }


    public void setEmotionScore(double s){
        this.EmotionScore = s;
    }
    public double getEmotionScore(){
        return this.EmotionScore;
    }


    public void setWordsEmotionClass(String[][] s){
        this.WordsEmotionClass = s;
    }
    public String[][] getWordsEmotionClass(){
        return this.WordsEmotionClass;
    }


    public void setDescribe(String s){
        this.Describe = s;
    }
    public String getDescribe(){
        return this.Describe;
    }


    public void setSentenceLen(int s){
        this.SentenceLen = s;
    }
    public int getSentenceLen(){
        return this.SentenceLen;
    }
}

package com.zzb.hello.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    //词语本身
    public String wordSelf;
    //词性
    public String wordClass;
    //词义数
    public int wordMeanNum;
    //词义序号
    public int wordMeanIndex;
    //情感分类
    public String emotionClass;
    //情感强度
    public int emotionIntensity;
    //情感极性
    public int emotionPolarity;

    //辅助情感分类
    private String subEmotionClass;
    //辅助情感强度
    private int subEmotionIntensity;
    //辅助情感极性
    private int subEmotionPolarity;

}

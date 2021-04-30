package com.zzb.hello.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Remark {
    private String nickName;
    private double total;
    private double accuracy;
    private double ontime;
    private double fluency;
    private String text;

    public void setRemark(String n, double total, double accuracy, double ontime, double fluency, String t){
        this.nickName = n;
        this.total = total;
        this.accuracy = accuracy;
        this.ontime = ontime;
        this.fluency = fluency;
        this.text = t;
    }
}

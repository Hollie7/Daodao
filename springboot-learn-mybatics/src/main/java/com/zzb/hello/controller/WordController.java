package com.zzb.hello.controller;

import com.zzb.hello.mapper.WordMapper;
import com.zzb.hello.pojo.*;
import com.zzb.hello.method.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordController {
    static String str = "对头";
    static String str1 = "老大";
    static String str2 = "极度";
    static String str3 = "更加";
    static String str4 = "过分";
    static String str5 = "点点滴滴";
    static String str6 = "不";
    static String str7 = "半点";
    static String str8 = "哀痛欲绝";
    static String str9 = "蔼然可亲 ";
    @Autowired
    private WordMapper wordMapper;


    //此处为各种数据库查词的尝试
    @RequestMapping("/SelectWord")
    public Word queryWordByWord(){
        List<Word> selectWord = wordMapper.queryWordByWord(str);
        VeryWord very = wordMapper.findVeryWord(str1);
        MostWord most = wordMapper.findMostWord(str2);
        MoreWord more = wordMapper.findMoreWord(str3);
        OverWord over = wordMapper.findOverWord(str4);
        IshWord ish = wordMapper.findIshWord(str5);
        InverseWord inverse = wordMapper.findInverseWord(str6);
        InsufficientlyWord insufficiently = wordMapper.findInsufficientlyWord(str7);
        NegWord neg = wordMapper.findNegWord(str8);
        PosWord pos = wordMapper.findPosWord(str9);
        System.out.println(very.very);
        System.out.println(most.most);
        System.out.println(more.more);
        System.out.println(over.overWord);
        System.out.println(ish.ish);
        System.out.println(inverse.inverse);
        System.out.println(insufficiently.insufficiently);
        System.out.println(neg.Neg);
        System.out.println(pos.Pos);

        System.out.println(selectWord.get(2).wordClass);
        System.out.println(Evaluate.WeightPoint(str2, wordMapper));
        System.out.println(Evaluate.WeightPoint(str4, wordMapper));
        System.out.println(Evaluate.WeightPoint(str1, wordMapper));
        System.out.println(Evaluate.WeightPoint(str3, wordMapper));
        System.out.println(Evaluate.WeightPoint(str5, wordMapper));
        System.out.println(Evaluate.WeightPoint(str7, wordMapper));
        System.out.println(Evaluate.WeightPoint(str6, wordMapper));
        System.out.println(Evaluate.WeightPoint(str8, wordMapper));

        return selectWord.get(0);
    }
}

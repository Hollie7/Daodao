package com.zzb.hello.mapper;

import com.zzb.hello.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;


@Repository
@Mapper
@Service
public interface WordMapper {
    //依据词语本身在情感词库本体中查找该词语
    List<Word> queryWordByWord(String wordSelf);

    //获取情感词库本体中所有词语
    List<Word> getAllWords();

    //依据词语本身在Very词库中查找该词语
    VeryWord findVeryWord(String very);

    //依据词语本身在Most词库中查找该词语
    MostWord findMostWord(String very);

    //依据词语本身在More词库中查找该词语
    MoreWord findMoreWord(String more);

    //依据词语本身在Ish词库中查找该词语
    IshWord findIshWord(String ish);

    //依据词语本身在Over词库中查找该词语
    OverWord findOverWord(String overWord);

    //依据词语本身在Inverse词库中查找该词语
    InverseWord findInverseWord(String inverse);

    //依据词语本身在Insufficiently词库中查找该词语
    InsufficientlyWord findInsufficientlyWord(String insufficiently);

    //依据词语本身在Neg词库中查找该词语
    NegWord findNegWord(String neg);

    //依据词语本身在Pos词库中查找该词语
    PosWord findPosWord(String pos);



    //获取情感词库本体中所有词语
    List<Word> queryWords(String[] list);

    //依据词语本身在Inverse词库中查找该词语
    boolean InverseJudge(String word);

    List<PosNegWord> FindPosNeg(String[] words);

    ExtentWord FindExtent(String word);
}

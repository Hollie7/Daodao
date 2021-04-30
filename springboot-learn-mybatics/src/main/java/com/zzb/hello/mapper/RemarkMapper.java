package com.zzb.hello.mapper;

import com.zzb.hello.pojo.Remark;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


@Repository
@Mapper
@Service
public interface RemarkMapper {
    Remark find(String nickName);
    void addRemark(Remark Id);
    void update(Remark Id);
}

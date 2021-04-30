package com.zzb.hello.mapper;

import com.zzb.hello.pojo.Login;
import com.zzb.hello.pojo.Remark;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
@Mapper
@Service
public interface LoginMapper {
    Login find(String nickName);
    void addLogin(Login login);
}

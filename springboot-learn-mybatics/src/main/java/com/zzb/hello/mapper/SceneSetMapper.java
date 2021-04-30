package com.zzb.hello.mapper;

import com.zzb.hello.pojo.SceneSet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
@Mapper
@Service
public interface SceneSetMapper {
    SceneSet find(String nickName);
    void update(SceneSet sceneSet);
}

package com.zzb.hello.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SceneSet {
    private String nickName;
    private String gender;
    private String scene;

    public void set(String nickName,String gender, String scene){
        this.nickName = nickName;
        this.gender = gender;
        this.scene = scene;
    }

    public String[] getInf(){
        String[] s = {this.nickName, this.gender, this.scene};
        return s;
    }

}

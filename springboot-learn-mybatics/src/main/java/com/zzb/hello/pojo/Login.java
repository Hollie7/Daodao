package com.zzb.hello.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Login {
    private String nickName;
    private String code;

    public void setLogin(String name, String code){
        this.nickName = name;
        this.code = code;
    }

    public String getName(){
        return this.nickName;
    }
}

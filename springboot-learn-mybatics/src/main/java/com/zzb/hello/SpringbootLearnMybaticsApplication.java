package com.zzb.hello;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication
@MapperScan("com.zzb.hello.mapper")
public class SpringbootLearnMybaticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootLearnMybaticsApplication.class, args);

	}

}

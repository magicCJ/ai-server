package com.zhibai.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.xunbai.ai.mapper")
@ComponentScan(basePackages = {"com.xunbai.ai.*"})
@SpringBootApplication
public class AiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiServerApplication.class, args);
	}

}

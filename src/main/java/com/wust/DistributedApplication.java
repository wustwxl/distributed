package com.wust;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//开启事务管理
@EnableTransactionManagement
public class DistributedApplication {

	public static void main(String[] args) {

		//常规开启Banner
		//SpringApplication.run(DistributedApplication.class, args);


		//修改Banner的模式为OFF
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DistributedApplication.class);
		builder.bannerMode(Banner.Mode.OFF).run(args);

	}
}

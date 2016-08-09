package com.jtech.springboot_mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.jtech.springboot_mongodb.svc.MongoDBBizSvc;

@SpringBootApplication
@ComponentScan("com.jtech.springboot_mongodb")
public class MongoApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(MongoApplication.class, args);
		MongoDBBizSvc controller = (MongoDBBizSvc) ctx.getBean("mongoDBBizSvc");
		
		controller.generateJsonFromMongoDB("spam00.json");
		
	}

}
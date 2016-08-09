package com.jtech.springboot_mongodb.cfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.jtech.springboot_mongodb.util.PropertiesUtil;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
// @EnableMongoRepositories(basePackages = { "com.example.store.repository" })
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Bean
	public Mongo mongo() throws Exception {
		return mongoClient();
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory(), null);
	}

	@Override
	protected String getDatabaseName() {
		return PropertiesUtil.getString("mongodb_db");
	}

	@Bean
	public MongoClient mongoClient() throws Exception {
		List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
		credentialsList.add(MongoCredential.createCredential(PropertiesUtil.getString("mongodb_id"), getDatabaseName(),
				PropertiesUtil.getString("mongodb_pass").toCharArray()));
		ServerAddress primary = new ServerAddress(PropertiesUtil.getString("mongodb_url"),
				PropertiesUtil.getInt("mongodb_port"));
		MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(4).socketKeepAlive(true)
				.build();
		return new MongoClient(Arrays.asList(primary), credentialsList, mongoClientOptions);
	}

}
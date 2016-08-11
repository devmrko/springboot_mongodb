package com.jtech.springboot_mongodb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.jtech.springboot_mongodb.svc.MongoDBBizSvc;

@SpringBootApplication
@ComponentScan("com.jtech.springboot_mongodb")
public class MongoApplication {

	public static void main(String[] args) throws Exception {

		Options options = new Options();

		Option input = new Option("i", "input", true, "input mongo collection name");
		input.setRequired(true);
		options.addOption(input);
		
		Option inputFilter = new Option("f", "filter", true, "input filter argument");
		inputFilter.setRequired(true);
		options.addOption(inputFilter);

		Option output = new Option("o", "output", true, "output file(json file name)");
		output.setRequired(true);
		options.addOption(output);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			String inputFilePath = cmd.getOptionValue("input");
			String[] inputFilterArgs = cmd.getOptionValue("filter").split("|");
			String outputFilePath = cmd.getOptionValue("output");

			ApplicationContext ctx = SpringApplication.run(MongoApplication.class, args);
			MongoDBBizSvc controller = (MongoDBBizSvc) ctx.getBean("mongoDBBizSvc");

			controller.generateJsonFromMongoDB(inputFilePath, inputFilterArgs, outputFilePath);

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("MongoApplication", options);
			return;
		}

	}

}
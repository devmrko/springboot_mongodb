package com.jtech.springboot_mongodb.svc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("mongoDBBizSvc")
public class MongoDBBizSvc extends BizServiceImpl {

	@Autowired
	MongoTemplate mongoTemplate;
	public void generateJsonFromMongoDB(String colStr, String[] inputFilterArgs, String inputStr) throws IOException {
		DB db = mongoTemplate.getDb();
		DBCollection dbCol = db.getCollection(colStr);

//		BasicDBObject fields = new BasicDBObject();
//		fields.put("_id", 0);
		
		BasicDBList searchOrCriteria = new BasicDBList();

		for(int i = 0; i < inputFilterArgs.length; i++) {
			BasicDBObject searchCriteria = new BasicDBObject();
			searchCriteria.put("contents", new BasicDBObject("$regex", inputFilterArgs[i]));
			searchOrCriteria.add(searchCriteria);
			
		}
		
		DBObject query = new BasicDBObject("$and", searchOrCriteria);
		
//		DBCursor dbCur = dbCol.find(query).sort(new BasicDBObject("_id",-1)).limit(50);
		DBCursor dbCur = dbCol.find(query).sort(new BasicDBObject("_id",-1));
		DBObject dbObj = null;
		Gson gson = new GsonBuilder().create();
		gson = new GsonBuilder().create();
		Map<String, String> rowMap = null;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		while (dbCur.hasNext()) {
			dbObj = dbCur.next();
			rowMap = new HashMap<String, String>();
			rowMap.put("url", dbObj.get("_id").toString());
			String tempStr = dbObj.get("contents").toString().replaceAll("[()]", "");
			rowMap.put("contents", tempStr.length() > 2000 ? tempStr.substring(0, 2000) : tempStr);
			rowMap.put("category", inputFilterArgs.toString());
			rowMap.put("save_by", "system");
			resultList.add(resultList.size(), rowMap);
			log.info("json: " + gson.toJson(rowMap));
		}
		
		try (Writer writer = new FileWriter(inputStr)) {
		    gson.toJson(resultList, writer);
		}
		
	}

}
package com.jtech.springboot_mongodb.dao;

import java.util.List;
import java.util.Map;

public interface StoreDao {

	List<Map<String, Object>> selectStoreListForTag(Map<String, Object> params);
	
	List<Map<String, Object>> selectCrawledSpainBlogLinks(Map<String, Object> params);
	
	List<Map<String, Object>> selectCommonWords(Map<String, Object> params);
	
	List<Map<String, Object>> selectUnwantedWords(Map<String, Object> params);
	
	List<Map<String, Object>> selectCrawledBlogLinks(Map<String, Object> params);
	
	void deleteStoreTag(Map<String, Object> params);
	
	void updateStoreTagYn(Map<String, Object> params);
	
	Map<String, Object> selectCodeCnt(Map<String, Object> params);
	
	void insertCode(Map<String, Object> params);
	
	void insertStoreTag(Map<String, Object> params);
	
	Map<String, Object> selectCodeByName(Map<String, Object> params);
		
}

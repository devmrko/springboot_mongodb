package com.jtech.springboot_mongodb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface MariaDBTestByMyBatis {

	@Select("SELECT * FROM REGION LIMIT 1")
	List<Map<String, Object>> getUserList();

	List<Map<String, Object>> getUserDetailById(Map<String, Object> params);
	
	List<Map<String, Object>> getBatchJobFromQueTb(Map<String, Object> params);
	
	Map<String, Object> getCrawlerInfo(Map<String, Object> params);
	
	void updateBatchJobStatus(Map<String, String> params);
	
}

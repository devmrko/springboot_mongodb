package com.jtech.springboot_mongodb.dto;

import java.util.HashMap;

public interface DataHandler {

	public static final String PARAM_KEY = "PARAM_MAP";

	public IListData convertToIListData(HashMap<String, Object> obj) throws Exception;

}

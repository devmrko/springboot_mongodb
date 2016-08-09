package com.jtech.springboot_mongodb.dto;

import java.util.Map;

public interface IRowData extends Map<Object, Object> {
	
	public static final String ORG_KEY = "ORG_";

	public static final String STATUS_INSERT = "insert";
	public static final String STATUS_UPDATE = "update";
	public static final String STATUS_DELETE = "delete";

	public abstract String getRowStatus();

	public abstract void setRowStatus(String s);

	public abstract String getLog();

	public abstract String toString();
	
}

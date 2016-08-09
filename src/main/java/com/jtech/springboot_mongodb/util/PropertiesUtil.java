package com.jtech.springboot_mongodb.util;

import java.util.Properties;
import org.apache.log4j.Logger;

public class PropertiesUtil {

	private static Properties props = null;
	private static String propertiesFileName = "springboot.properties";

	private static Logger log = Logger.getLogger(PropertiesUtil.class);

	static {
		if (props == null) {
			props = new Properties();
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				props.load(loader.getResource(propertiesFileName).openStream());
			} catch (Exception e) {
				log.error("<<<<< <<<<< <<<<< PropertiesUtil - static - Exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private PropertiesUtil() {
	}

	public static Properties getProps() {
		return props;
	}

	public static String getString(String str) {
		String propertyValue = props.getProperty(str);
		return propertyValue == null ? "" : propertyValue.trim();
	}

	public static boolean getBoolean(String key) {
		boolean value = false;
		try {
			String propertyValue = props.getProperty(key);
			value = (new Boolean(propertyValue == null ? "" : propertyValue.trim())).booleanValue();
		} catch (Exception e) {
			log.error("<<<<< <<<<< <<<<< PropertiesUtil - getBoolean - Exception: " + e.getMessage());
			throw new IllegalArgumentException("Illegal Boolean Key : " + key);
		}
		return value;
	}

	public static int getInt(String key) {
		int value = -1;
		try {
			String propertyValue = props.getProperty(key);
			value = Integer.parseInt(propertyValue == null ? "" : propertyValue.trim());
		} catch (Exception e) {
			log.error("<<<<< <<<<< <<<<< PropertiesUtil - getInt - Exception: " + e.getMessage());
			throw new IllegalArgumentException("Illegal Integer Key : " + key);
		}
		return value;
	}

	public static long getLong(String key) {
		long value = -1;
		try {
			String propertyValue = props.getProperty(key);
			value = Long.parseLong(propertyValue == null ? "" : propertyValue.trim());
		} catch (Exception e) {
			log.error("<<<<< <<<<< <<<<< PropertiesUtil - getLong - Exception: " + e.getMessage());
			throw new IllegalArgumentException("Illegal Long Key : " + key);
		}
		return value;
	}

}
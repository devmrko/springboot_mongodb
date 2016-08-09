package com.jtech.springboot_mongodb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RowDataImpl implements IRowData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411668902352182741L;

	private String rowStatus;
	private HashMap map;

	public RowDataImpl() {
		rowStatus = "";
		map = null;
		map = new HashMap();
	}

	public String getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

	public String getVal(String key) {
		String val = null;
		Object obj = map.get(key);
		if (obj == null)
			return val;
		if (obj instanceof String)
			val = (String) obj;
		else
			val = String.valueOf(obj);
		return val;
	}

	public String getLog() {
		StringBuffer sb = new StringBuffer();
		sb.append("DataRowMap -> getLog\n");
		sb.append("[rowStatus = ");
		sb.append(getRowStatus());
		sb.append("]\n");
		List list = new ArrayList(map.keySet());
		Collections.sort(list);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (!key.startsWith("ORG_")) {
				Object value = map.get(key);
				if (getRowStatus().equals("update")) {
					Object orgValue = map.get("ORG_" + key);
					sb.append(key);
					sb.append(" = [");
					sb.append(value);
					// sb.append("]\n");

					sb.append("], org = [");
					sb.append(orgValue);
					sb.append("]\n");
				} else {
					sb.append(key);
					sb.append(" = [");
					sb.append(value);
					sb.append("]\n");
				}
			}
		}

		return sb.toString();
	}

	public String toString() {
		return map.toString();
	}

	public void clear() {
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map t) {
		map.putAll(t);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}

	public Object clone() {
		return map.clone();
	}

}

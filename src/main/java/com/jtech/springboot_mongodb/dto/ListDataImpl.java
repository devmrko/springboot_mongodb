package com.jtech.springboot_mongodb.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListDataImpl implements IListData {

	private String result;
	private HashMap map;

	public ListDataImpl() {
		result = null;
		map = null;
		map = new HashMap();
	}

	public ListDataImpl(String result) {
		this.result = null;
		map = null;
		map = new HashMap();
		setResult(result);
	}

	public List getDataList(String id) {
		List list = (List) map.get(id);
		if (list == null)
			list = new ArrayList();
		return list;
	}

	public void setDataList(String id, List list) {
		map.put(id, list);
	}

	public HashMap getDataListMap() {
		return this.map;
	}

	public String getParameter(String key) {
		if (getParameterMap() != null && getParameterMap().containsKey(key))
			return (String) getParameterMap().get(key);
		else
			return null;
	}

	public void setParameterMap(Map paramMap) {
		map.put("PARAM_MAP", paramMap);
	}

	public Map getParameterMap() {
		Map paramMap = (Map) map.get("PARAM_MAP");
		return paramMap;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public String getLog() {
		StringBuffer sb = new StringBuffer();
		sb.append("HistDataListMap -> getLog\n");
		sb.append("[param]\n");
		Map paramMap = getParameterMap();
		Iterator iterator = null;
		List sortList = null;
		if (paramMap != null) {
			sortList = new ArrayList(paramMap.keySet());
			Collections.sort(sortList);
			for (iterator = sortList.iterator(); iterator.hasNext(); sb.append("]\n")) {
				String key = (String) iterator.next();
				sb.append(key);
				sb.append(" = [");
				sb.append(paramMap.get(key));
			}

			sb.append("\n");
		}
		sortList = new ArrayList(this.map.keySet());
		Collections.sort(sortList);
		iterator = sortList.iterator();
		Iterator iter = null;
		IRowData rowMap = null;
		Map map = null;
		Object obj = null;
		List list = null;
		String id = null;
		List sortList1 = null;
		while (iterator.hasNext()) {
			id = (String) iterator.next();
			if (!id.equals("PARAM_MAP")) {
				list = (List) this.map.get(id);
				sb.append("[DatasetId = " + id + "]\n");
				for (int i = 0; i < list.size(); i++) {
					obj = list.get(i);
					if (obj instanceof IRowData) {
						rowMap = (IRowData) list.get(i);
						sb.append("[rowStatus = " + rowMap.getRowStatus() + "]\n");
						sortList1 = new ArrayList(rowMap.keySet());
						Collections.sort(sortList1);
						for (iter = sortList1.iterator(); iter.hasNext();) {
							String key = (String) iter.next();
							if (!key.startsWith("ORG_")) {
								Object value = rowMap.get(key);
								if (rowMap.getRowStatus().equals("update")) {
									Object orgValue = rowMap.get("ORG_" + key);
									sb.append(key);
									sb.append(" = [");
									sb.append(value);
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
					} else if (obj instanceof Map) {
						map = (Map) list.get(i);
						sortList1 = new ArrayList(map.keySet());
						Collections.sort(sortList1);
						for (iter = sortList1.iterator(); iter.hasNext(); sb.append("]\n")) {
							String key = (String) iter.next();
							Object value = map.get(key);
							sb.append(key);
							sb.append(" = [");
							sb.append(value);
						}
					}
					sb.append("\n");
				}
			}
		}

		return sb.toString();
	}

	public void addVariable(String key, String val) {
		Map variableMap = null;
		if (map.containsKey("VARIABLE_MAP"))
			variableMap = (Map) map.get("VARIABLE_MAP");
		else
			variableMap = new HashMap();
		variableMap.put(key, val);
		setVariableMap(variableMap);
	}

	public void setVariableMap(Map variableMap) {
		map.put("VARIABLE_MAP", variableMap);
	}

	public Map getVariableMap() {
		Map variableMap = (Map) map.get("VARIABLE_MAP");
		return variableMap;
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

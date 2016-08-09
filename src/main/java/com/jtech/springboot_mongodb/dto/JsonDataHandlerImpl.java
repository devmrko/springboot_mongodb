package com.jtech.springboot_mongodb.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Service("jsonDataHandlerImpl")
public class JsonDataHandlerImpl implements DataHandler {

	protected final Logger log = Logger.getLogger(getClass());

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IListData convertToIListData(HashMap<String, Object> jObj) throws Exception {

		log.debug(">>>>> >>>>> >>>>> convertToIListData");

		IListData listData = new ListDataImpl();

		listData.setParameterMap((Map) jObj.get(PARAM_KEY));

		Iterator iterator = jObj.entrySet().iterator();
		Entry entry = null;
		String keyName = "";
		List gridList = null;
		Map eachRowData;

		Gson gson = new GsonBuilder().create();

		while (iterator.hasNext()) {
			entry = (Entry) iterator.next();
			keyName = String.valueOf(entry.getKey());

			if (!PARAM_KEY.equals(keyName)) {

				// grid 변경 데이터가 아닐때(grid 변경은 화면에서 chg로 끝나는 형태로 넘겨야 한다)
				if (!"chg".equals(keyName.substring(keyName.length() - 3, keyName.length()))) {
					listData.setDataList(keyName, (List) jObj.get(keyName));

				} else {
					if (!"{}".equals(String.valueOf(entry.getValue()))) {
						ObjectMapper om = new ObjectMapper();
						Map<String, Object> m2 = om.readValue(gson.toJson(entry.getValue()),
								new TypeReference<Map<String, Object>>() {
								});

						gridList = new ArrayList();

						int i = 0;
						for (String key : m2.keySet()) {
							eachRowData = (Map) m2.get(String.valueOf(key));
							gridList.add(i, eachRowData);
							i++;
						}

						listData.setDataList(keyName, gridList);

					} else {
						listData.setDataList(keyName, null);
					}
				}
			}
		}

		return listData;
	}

	@SuppressWarnings("static-access")
	public JSONObject convertToJSONObject(IListData listData) throws Exception {

		log.debug(">>>>> >>>>> >>>>> convertToJSONObject");

		JSONObject jsonObj = new JSONObject();

		if (null != listData) {
			JSONSerializer jsonSlz = new JSONSerializer();
			Iterator<Entry<Object, Object>> entryIter = listData.entrySet().iterator();
			while (entryIter.hasNext()) {
				Entry<Object, Object> entry = entryIter.next();
				log.debug(">>>>> >>>>> >>>>> model.put [" + entry.getKey() + "]");
				jsonObj.put(entry.getKey(), jsonSlz.toJSON(entry.getValue()));
			}
		}

		return jsonObj;
	}

}

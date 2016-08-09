package com.jtech.springboot_mongodb.svc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jtech.springboot_mongodb.dao.StoreDao;
import com.jtech.springboot_mongodb.dto.IListData;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


/**
 * <B>Project Name : </B>itrend<br/>
 * <B>Package Name : </B>com.jm.cmm.svc<br/>
 * <B>File Name : </B>TagHandlingSvc<br/>
 * <B>Description</B>
 * <ul>
 * <li>tag 정보 생성 관리
 * <li>논리적으로 내용이 분리되는 경우 li 태그를 사용하여 개행 합니다.
 * </ul>
 * 
 * @author developer
 * @since Oct 8, 2015
 */
@Service("tagHandlingSvc")
public class TagHandlingSvc extends BizServiceImpl {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	private StoreDao storeDao;
	
	String[] suffix = 
		{"은", "는", "이", "가", "를", 
		"의", "과", "으로", "도", "들이", 
		"랑", "거", "점", "역", "에서", 
		"식", "집", "하고", "한", "해서",
		"하게", "시", "임", "라", "에",
		"동안", "에는", "시면", "과는",
		"와", "만", "풍", "로", "을",
		"인", "이었다"};
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>의미없는 단어 조합인지 여부 검사 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param inputStr
		 * @return
		 */
	public boolean getIsMeaninglessWord(String inputStr) {

		String[] meaninglessWords = 
			{"있습니다."
				,"있으며,"
				,"저작권은"
				,"이용하는"
				,"저작권법"
				,"법적책임을"
				,"콘텐츠의"
				,"크기입니다."
				,"언론사"
				,"페이지로"
				,"이동합니다."
				,"네이버"
				,"오늘의"
				,"최종수정"
				,"로그인"
				,"기사입력"
				,"기사의"
				,"(월)"
				,"댓글을"
				,"상태로"
				,"조회수"
				,"확인해"
				,"하시면"
				,"등급과"
				,"목록을"
				,"내댓글"
				,"제공처에"
				,"작성된"
				,"해외야구"
				,"유지됩니다."
				,"e스포츠&게임"
				,"맨위로"
				,"로그인해주세요."
				,"펼치기"
				,"설정을"
				,"해외축구"
				,"설정되어"
				,"영상뉴스"
				,"보시려면"
				,"변경해주세요"
				,"접기로"
				,"설정하시면"
				,"영역이"
				,"SNS공유하기"
				,"만드는"
				,"풀영상"
				,"요청해주세요'"
				,"기사에"
				,"재배포"
				,"오피니언"
				,"SNS"
				,"내용을"
				,"보내기"
				,"제공처"
				,"네이버에"
				,"1000자"
				,"의견을"
				,"작성한"
				,"작성하실"
				,"관계없는"
				,"내외로"
				,"내용과"
				,"도움말"
				,"부합하지"
				,"언론사(기자)는"
				,"ID로"
				,"작성자명"
				,"1회만"
				,"작성할"
				,"매체로"
				,"작성이"
				,"뉴스검색"
				,"언론사(기자)가"
				,"본문에"
				,"본문을"
				,"제한되며,"
				,"공간입니다."
				,"언론사별"
				,"게시글은"
				,"명시된"
				,"관련없는"
				,"이해당사자나"
				,"이해당사자는"
				,"추천해요"
				,"제한됩니다."
				,"단체로,"
				,"삭제됩니다.본문"
				,"영역은"
				,"NAVER"
				,"기자]"
				,"[프리뷰]"
				,"기억들’"
				,"이호근의"
				,"이끌다"
				,"발리뷰]"
				,"것으로"
				,"밝혔다."
				,"남자부"
				,"말했다."
				,"무단전재"
				,"금지]"
				,"12월"
				,"OSEN"
				,"2015.11.23"
				,"메뉴에"
				,"'경제M'이"
				,"추가됩니다"
				,"All"
				,"Copyright"
				,"reserved"
				,"byte"
				,"등록번호"
				,"rights"
				,"다른기사"
				,"발행인"
				,"편집인"
				,"Tel"
				,"<저작권자"
				,"대표전화"
				,"기자  |  "
				,"자동등록방지용"
				,"입력하세요!"
				,"mail"
				,"등록일"
				,"TEL"
				,"온라인"
				,"최종편집"
				,"관련기사"
				,"right"
				,"  |  "
				,"******"
				,"()-"
				,"Reserved"
				,"FAX"
				,"핫이슈"
				,"Fax"
				,"pyright"
				," | "
				,"[--"
				,"전체기사"
				,"Rights"
				,"회원가입"
				,"개인정보취급방침"
				,"check"
				,"재배포금지"
			};
		/*
		
		String[] meaninglessWords = 
				{"번", "층", "세", "호", "원",
				"명", "m", "만원", "g", "회", 
				"번지", "년", "kg", "개소", "등급",
				"명", "인당", "번지", "pm", "평",
				"개소", "억원", "대", "천원", "여가지",
				"인", "평", "도", "가지", "마리",
				"탑", "분", "시", "th", "st", 
				"nd", "rd", "점", "평형", "cc",
				"cm", "df", "호점", "분", "개",
				"km", "차로", "타경", "차", "회",
				"제차", "제회", "맛집", "시간"};
				*/
		
		inputStr = inputStr.replaceAll("['\",.-[0-9]]", "");
//		inputStr = inputStr.replaceAll("^[0-9]+", "");
		if(inputStr.length() == 0) return true;
		
		for (int i = 0, j = meaninglessWords.length; i < j; i++) {
			if(inputStr.equalsIgnoreCase(meaninglessWords[i])) 
				return true;
			
			for (int a = 0, b = suffix.length; a < b; a++) {
				if(inputStr.equalsIgnoreCase(meaninglessWords[i] + suffix[a])) 
					return true;
			}
			
		}
		
		return false;
	}

	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>두 단어가 동일한지 여부를 리턴(suffix 붙여서 비교) 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param inputStr
		 * @param targetStr
		 * @return
		 */
	public boolean getIsSameKindWord(String inputStr, String targetStr) {
		
		if(inputStr.equalsIgnoreCase(targetStr)) return true;
		if(inputStr.equals(targetStr)) return true;
		
		for (int i = 0, j = suffix.length; i < j; i++) {
			if(inputStr.equalsIgnoreCase(targetStr + suffix[i]) || 
					targetStr.equalsIgnoreCase(inputStr + suffix[i])) 
				return true;
		}
		
		return false;
	}
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>keyword 값이 list에 있는지 여부를 리턴
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param list
		 * @param keyWord
		 * @return
		 */
	@SuppressWarnings("rawtypes")
	public boolean isKeywordIntheList(List list, String keyWord) {
		
		Map rowData = null;
		String comparedKeyword = "";
		
		for (int t = 0; t < list.size(); t++) {

			rowData = (Map) list.get(t);
			if(rowData != null) {
				comparedKeyword = String.valueOf(rowData.get("NAME")).trim();
				
				if (keyWord.equalsIgnoreCase(comparedKeyword)) 
					return true;
				
				if (getIsSameKindWord(keyWord, comparedKeyword))
					return true;
				
			}
			
		}
		
		return false;
	}
	
	
	@SuppressWarnings("rawtypes")
	public boolean isKeywordIntheArry(List array, String keyWord) {
		
		String comparedKeyword = "";
		
		for (int t = 0; t < array.size(); t++) {

			comparedKeyword = ((String) array.get(t)).trim();

			if(comparedKeyword.length() != 0) {
				if (keyWord.equalsIgnoreCase(comparedKeyword)) 
					return true;
				
				if (getIsSameKindWord(keyWord, comparedKeyword))
					return true;
				
			}
			
		}
		
		return false;
	}
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>공통 키워드 목록에 키워드에 해당하는 값이 있는지 확인하여 있다면 해당값, 없다면 입력받은 키워드 리턴 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param commonKeywordList
		 * @param Keyword
		 * @return
		 */
	public String getCommonKeywordList(List<?> commonKeywordList, String Keyword) {

		Map rowData = null;
		String comparedKeyword = "";
		
		for (int i = 0; i < commonKeywordList.size(); i++) {
			
			rowData = (Map) commonKeywordList.get(i);
			comparedKeyword = String.valueOf(rowData.get("NAME")).trim();
			
			if (getIsSameKindWord(Keyword, comparedKeyword))
				return comparedKeyword;
			
		}

		return Keyword;
	}

	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 13, 2015
		 * <li>Developer : developer
		 * <li>입력된 array의 중복을 제거해서 array형태로 리턴해준다 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 13, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param contentsWordArray
		 * @return
		 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String[] getDistinctList(String[] contentsWordArray) {
		
		List returnList = null;
		for (int i = 0, j = contentsWordArray.length; i < j; i++) {
			
			if(returnList == null) {
				returnList = new ArrayList();
				returnList.add(contentsWordArray[i]);
			} else {
				if(!isKeywordIntheArry(returnList, contentsWordArray[i])){
					returnList.add(contentsWordArray[i]);
				}
			}
			
		}
		
		String[] returnArry = (String[]) returnList.toArray(new String[returnList.size()]);
		
		return returnArry;
	}
	
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>list에 해당하는 contents 정보로 각 keywords/반복횟수를 구한다. 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param rowData
		 * @param blogList
		 * @return
		 */
	@SuppressWarnings({"rawtypes", "unchecked", "unused"})
	public HashMap getAccumualtedTags(Map rowData, List blogList, List unwantedWordList, List commonKeywordList) {
		
		long time = System.nanoTime();
		
		HashMap<String, String> returnKeywords = new HashMap();
		Map blogListRowData = null;
		String curBlogLink, keyWord = "";
		StringBuffer sb = new StringBuffer();
		String[] contentsWordArray = null;
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		
		String cityNm = String.valueOf(rowData.get("CITY_NM"));
		String guNm = String.valueOf(rowData.get("GU_NM"));
		String storeId = String.valueOf(rowData.get("STORE_ID"));
		String storeNmTemp = String.valueOf(rowData.get("STORE_NM"));
		if (storeNmTemp.split(" ").length > 1)
			storeNmTemp = storeNmTemp.split(" ")[0];
		final String storeNm = storeNmTemp.trim();
		
		ConcurrentMap concurrentMap = new ConcurrentHashMap();
		concurrentMap.put("N/A", 0);
		
		int cnt = 0;
		
		for (int i = 0, j = blogList.size(); i < j; i++) {
			
			blogListRowData = (Map) blogList.get(i);
			curBlogLink = String.valueOf(blogListRowData.get("BLOG_LINK"));
			curBlogLink.replace("'||chr(38)||'", "&");
			
			sb.setLength(0);
			sb.append(getColValViaMongoDB("blogs_contents", "_id", curBlogLink, "contents"));
//			log.debug("contents: " + sb.toString());
			
			if (sb.length() != 0 && sb.length() < 20000) {
//				contentsWordArray = getDistinctList();
				Stream<String> uniqueWords = Stream.of(sb.toString().split(" ")).distinct();
//				contentsWordArray = uniqueWords.toArray(String[]::new);
				
				uniqueWords.parallel().forEach(
					s -> {
						s = s.trim();
						if(2 < s.length() && s.length() < 50 &&
							!getIsSameKindWord(s, storeNm) &&
							!keyWord.contains(cityNm) && 
							!keyWord.contains(guNm)) {
							
							s = getCommonKeywordList(commonKeywordList, s);
							if(concurrentMap.containsKey(s) && 
								!getIsMeaninglessWord(s) &&
								!isKeywordIntheList(unwantedWordList, s)) {
								concurrentMap.replace(s, concurrentMap.get(s), Integer.valueOf(String.valueOf(concurrentMap.get(s))) + 1);
							} else {
								concurrentMap.put(s, 1);
							}
						}

					});
				
				/*
				for (int k = 0; k < contentsWordArray.length; k++) {
					keyWord = String.valueOf(contentsWordArray[k]);
					keyWord = keyWord.replaceAll(match, "").trim();
					if (keyWord.length() < 50 && 2 < keyWord.length() && 
							!getIsMeaninglessWord(keyWord) &&
							!isKeywordIntheList(unwantedWordList, keyWord) &&
							!getIsSameKindWord(keyWord, storeNm) &&
							!keyWord.contains(cityNm) && 
							!keyWord.contains(guNm)
							) {
						
						keyWord = getCommonKeywordList(commonKeywordList, keyWord);
							
						if (returnKeywords.containsKey(keyWord)) {
							cnt = Integer.valueOf(String.valueOf(returnKeywords.get(keyWord))) + 1;
							returnKeywords.replace(keyWord, String.valueOf(cnt));
						} else {
							cnt = 1;
							returnKeywords.put(keyWord, String.valueOf(cnt));
						}

					}

				}
				*/
				
			}
			
		}
		
		
		
//		for (Map.Entry elem : returnKeywords.entrySet()) {
//			keyWord = (String) elem.getKey();
//			if (!getIsMeaninglessWord(keyWord) && 
//				!isKeywordIntheList(unwantedWordList, keyWord) && 
//				!getIsSameKindWord(keyWord, storeNm) && 
//				!keyWord.contains(cityNm) && 
//				!keyWord.contains(guNm)) {
//				returnKeywords.remove(keyWord);
//			}
//		}
		
		log.debug("The getAccumualtedTags time of " + storeNm + ": " + String.valueOf((System.nanoTime() - time) / 10000000) + " ns");
		HashMap returnMap = new HashMap();
		returnMap.putAll(concurrentMap);
		
		return returnMap;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked", "unused"})
	public HashMap getSpainAccumualtedTags(Map rowData, List blogList, List unwantedWordList, List commonKeywordList) {
		
		long time = System.nanoTime();
		
		HashMap<String, String> returnKeywords = new HashMap();
		Map blogListRowData = null;
		String curBlogLink, keyWord = "";
		StringBuffer sb = new StringBuffer();
		String[] contentsWordArray = null;
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		
		String cityNm = String.valueOf(rowData.get("CITY_NM"));
		String guNm = String.valueOf(rowData.get("GU_NM"));
		String storeId = String.valueOf(rowData.get("STORE_ID"));
		String storeNmTemp = String.valueOf(rowData.get("STORE_NM"));
		if (storeNmTemp.split(" ").length > 1)
			storeNmTemp = storeNmTemp.split(" ")[0];
		final String storeNm = storeNmTemp.trim();
		
		ConcurrentMap concurrentMap = new ConcurrentHashMap();
		concurrentMap.put("N/A", 0);
		
		int cnt = 0;
		
		for (int i = 0, j = blogList.size(); i < j; i++) {
			
			blogListRowData = (Map) blogList.get(i);
			curBlogLink = String.valueOf(blogListRowData.get("BLOG_LINK"));
			curBlogLink.replace("'||chr(38)||'", "&");
			
			sb.setLength(0);
			log.info("mongodb - key: " + "_id" + ", search value: " + curBlogLink + ", " + (i+1) + "th job is running");
			sb.append(getColValViaMongoDB("spain_blogs_contents", "_id", curBlogLink, "contents"));
//			log.debug("contents: " + sb.toString());
			
			if (sb.length() != 0 && sb.length() < 20000) {
//				contentsWordArray = getDistinctList();
				Stream<String> uniqueWords = Stream.of(sb.toString().split(" ")).distinct();
//				contentsWordArray = uniqueWords.toArray(String[]::new);
				
				uniqueWords.parallel().forEach(
					s -> {
						s = s.trim();
						if(2 < s.length() && s.length() < 50 &&
							!getIsSameKindWord(s, storeNm) &&
							!keyWord.contains(cityNm) && 
							!keyWord.contains(guNm)) {
							
							s = getCommonKeywordList(commonKeywordList, s);
							if(concurrentMap.containsKey(s) && 
								!getIsMeaninglessWord(s) &&
								!isKeywordIntheList(unwantedWordList, s)) {
								concurrentMap.replace(s, concurrentMap.get(s), Integer.valueOf(String.valueOf(concurrentMap.get(s))) + 1);
							} else {
								concurrentMap.put(s, 1);
							}
						}

					});
				
				/*
				for (int k = 0; k < contentsWordArray.length; k++) {
					keyWord = String.valueOf(contentsWordArray[k]);
					keyWord = keyWord.replaceAll(match, "").trim();
					if (keyWord.length() < 50 && 2 < keyWord.length() && 
							!getIsMeaninglessWord(keyWord) &&
							!isKeywordIntheList(unwantedWordList, keyWord) &&
							!getIsSameKindWord(keyWord, storeNm) &&
							!keyWord.contains(cityNm) && 
							!keyWord.contains(guNm)
							) {
						
						keyWord = getCommonKeywordList(commonKeywordList, keyWord);
							
						if (returnKeywords.containsKey(keyWord)) {
							cnt = Integer.valueOf(String.valueOf(returnKeywords.get(keyWord))) + 1;
							returnKeywords.replace(keyWord, String.valueOf(cnt));
						} else {
							cnt = 1;
							returnKeywords.put(keyWord, String.valueOf(cnt));
						}

					}

				}
				*/
				
			}
			
		}
		
		
		
//		for (Map.Entry elem : returnKeywords.entrySet()) {
//			keyWord = (String) elem.getKey();
//			if (!getIsMeaninglessWord(keyWord) && 
//				!isKeywordIntheList(unwantedWordList, keyWord) && 
//				!getIsSameKindWord(keyWord, storeNm) && 
//				!keyWord.contains(cityNm) && 
//				!keyWord.contains(guNm)) {
//				returnKeywords.remove(keyWord);
//			}
//		}
		
		log.debug("The getAccumualtedTags time of " + storeNm + ": " + String.valueOf((System.nanoTime() - time) / 10000000) + " ns");
		HashMap returnMap = new HashMap();
		returnMap.putAll(concurrentMap);
		
		return returnMap;
	}
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>mon
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param collectionNm
		 * @param keyNm
		 * @param searchVal
		 * @param returnColumn
		 * @return
		 */
	public String getColValViaMongoDB(String collectionNm, String keyNm, String searchVal, String returnColumn) {

		DB db = mongoTemplate.getDb();

		DBCollection dbCol = db.getCollection(collectionNm);
		
		DBObject dbObj = dbCol.findOne(new BasicDBObject(keyNm, searchVal));

		return String.valueOf(dbObj.get(returnColumn));
	}
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>입력받은 map의 value값으로 sort시킨 list를 리턴한다
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param keywords
		 * @return
		 * @throws Exception
		 */
	public List sortMapByValue(Map keywords) throws Exception {

		List returnList = new ArrayList();
		Map curMap = null;
		int minimumCnt = 3;
		int maximumKeywords = 300;

		Set<Entry<String, String>> entries = keywords.entrySet();
		List<Entry<String, String>> listOfEntries = new ArrayList<Entry<String, String>>(entries);

		Comparator<Entry<String, String>> valueComparator = new Comparator<Entry<String, String>>() {
			@Override
			public int compare(Entry<String, String> e1, Entry<String, String> e2) {
				int v1 = Integer.valueOf(String.valueOf(e1.getValue()));
				int v2 = Integer.valueOf(String.valueOf(e2.getValue()));
				return v1 - v2;
			}
		};

		Collections.sort(listOfEntries, valueComparator);
		Collections.reverse(listOfEntries);
		
		LinkedHashMap<String, String> sortedByValue = new LinkedHashMap<String, String>(listOfEntries.size());

		for (Entry<String, String> entry : listOfEntries) {
			if (Integer.valueOf(String.valueOf(entry.getValue())) > minimumCnt) {
				sortedByValue.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}

		Set<Entry<String, String>> entrySetSortedByValue = sortedByValue.entrySet();

		boolean bool = false;
		int cnt = 0;
		String keyStr, cntStr = "";
		
		for (Entry<String, String> mapping : entrySetSortedByValue) {

			keyStr = String.valueOf(mapping.getKey());
			cntStr = String.valueOf(mapping.getValue());
			if (isInteger(cntStr) && cnt < maximumKeywords) {
				curMap = new HashMap();
				curMap.put("key", keyStr);
				curMap.put("cnt", cntStr);
				returnList.add(returnList.size(), curMap);
				cnt++;
				log.debug(">>>>> keyword: " + keyStr + ", cnt: " + cntStr);
			}

		}
		
		return returnList;
		
	}
	
	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>int 값인지 여부를 리턴 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param s
		 * @return
		 */
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void tagListInsertHandler(Map storeRowData, List tagList) throws Exception {
		
		Map rowData, curRowMap, codeMap = null;
		
		for (int a = 0, b = tagList.size(); a < b; a++) {
			rowData = (Map) tagList.get(a);
			rowData.put("NAME", rowData.get("key"));
			curRowMap = (Map) storeDao.selectCodeByName(rowData);
			if (curRowMap == null) {
				rowData.put("CATEGORY", "623");
				rowData.put("REG_ID", "admin");
				storeDao.insertCode(rowData);
				curRowMap = new HashMap();
				curRowMap = storeDao.selectCodeByName(rowData);
			}
			
//			for( Object key : curRowMap.keySet() ){
//	            codeMap = (Map) curRowMap.get(key);
			
				rowData.put("CODE", curRowMap.get("CODE"));
//				rowData.put("CODE", codeMap.get("CODE"));
				rowData.put("STORE_ID", storeRowData.get("STORE_ID"));
				rowData.put("RANK", String.valueOf(a + 1));
				rowData.put("REG_ID", "admin");
				storeDao.insertStoreTag(rowData);
//			}

		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public void koreanAirTagCreationHandler(IListData listData) throws Exception {
		HashMap rowData, keywordsMap = null;
		StringBuffer sb = new StringBuffer();
		
		
		
		DB db = mongoTemplate.getDb();
		DBCollection dbCol = db.getCollection("koreanAirNews");
		ConcurrentMap concurrentMap = new ConcurrentHashMap();
		
//		BasicDBList entireQuery = new BasicDBList();
//		entireQuery.add(new BasicDBObject("contents", new BasicDBObject("$not", java.util.regex.Pattern.compile("배구"))));
//		entireQuery.add(new BasicDBObject("contents", new BasicDBObject("$not", java.util.regex.Pattern.compile("스포츠"))));
//		entireQuery.add(new BasicDBObject("contents", new BasicDBObject("$ne", "")));
//		DBCursor dbCur = dbCol.find(new BasicDBObject("$and", entireQuery));
		
		DBCursor dbCur = dbCol.find();
		
		
		DBObject dbObj = null;
		while (dbCur.hasNext()) {
			dbObj = dbCur.next();
			sb.setLength(0);
			sb.append(dbObj.get("contents"));
			log.debug("contents: " + sb.toString());
			
			Stream<String> uniqueWords = Stream.of(sb.toString().split(" ")).distinct();
			uniqueWords.parallel().forEach(
					s -> {
						s = s.trim();
						s = s.replaceAll("['\"”“‘’ㅣ,.-…:[0-9]]", "");
						if(2 < s.length() && s.length() < 50) {
							if(concurrentMap.containsKey(s) && !getIsMeaninglessWord(s)) {
								concurrentMap.replace(s, concurrentMap.get(s), Integer.valueOf(String.valueOf(concurrentMap.get(s))) + 1);
							} else {
								concurrentMap.put(s, 1);
							}
						}

					});

		}
		
		HashMap returnMap = new HashMap();
		returnMap.putAll(concurrentMap);
		
		List tagList = sortMapByValue(returnMap);
		
	}

	/**
		 * <B>History</B>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>store 별 검색어 tag를 생성한다. 
		 * </ul>
		 * <ul>
		 * <li>Date : Oct 8, 2015
		 * <li>Developer : developer
		 * <li>초기에 Method를 수정하는 경우 위의 ul~/ul 태그를 복사하여 아래에 붙여 넣고 수정된 내용에 대한 기록을 한다.
		 * </ul>
		 *  
		 * @param listData
		 * @throws Exception
		 */
	@SuppressWarnings("rawtypes")
	public void tagCreationHandler(IListData listData) throws Exception {

		HashMap paramMap = new HashMap();
		paramMap.put("STORE_ID", "12889");
		
		List tagStoreList = storeDao.selectStoreListForTag(paramMap);
		List commonKeywordList = storeDao.selectCommonWords(paramMap);
		List unwantedWordList = storeDao.selectUnwantedWords(paramMap);
		
		HashMap rowData, keywordsMap = null;
		String storeNm = "";
		List blogList, tagList = null;
		long time = System.nanoTime();
		
		for (int i = 0, j = tagStoreList.size(); i < j; i++) {
			time = System.nanoTime();
			rowData = (HashMap) tagStoreList.get(i);
			storeNm = String.valueOf(rowData.get("STORE_NM"));
			
			blogList = storeDao.selectCrawledBlogLinks(rowData);
			keywordsMap = getAccumualtedTags(rowData, blogList, unwantedWordList, commonKeywordList);
			
			log.debug(">>>>> store name: " + storeNm);
			tagList = sortMapByValue(keywordsMap);
			
			storeDao.deleteStoreTag(rowData);
			storeDao.updateStoreTagYn(rowData);
			tagListInsertHandler(rowData, tagList);
			
			log.debug("Tag creation time of " + storeNm + ": " + String.valueOf((System.nanoTime() - time) / 10000000) + " ns");
		}


	}
	
	public void spainTagCreationHandler(IListData listData) throws Exception {

		HashMap paramMap = new HashMap();
		String cityNm = "바르셀로나";
		paramMap.put("REGION_NM", cityNm);
		
//		List tagStoreList = storeDao.selectStoreListForTag(paramMap);
		List commonKeywordList = storeDao.selectCommonWords(paramMap);
		List unwantedWordList = storeDao.selectUnwantedWords(paramMap);
		
		HashMap rowData, keywordsMap = null;
		String storeNm = "";
		List blogList, tagList = null;
		long time = System.nanoTime();
		
//		for (int i = 0, j = tagStoreList.size(); i < j; i++) {
			time = System.nanoTime();
//			rowData = (HashMap) tagStoreList.get(i);
//			storeNm = String.valueOf(rowData.get("STORE_NM"));
			
			blogList = storeDao.selectCrawledSpainBlogLinks(paramMap);
			keywordsMap = getSpainAccumualtedTags(paramMap, blogList, unwantedWordList, commonKeywordList);
			
			log.debug(">>>>> store name: " + storeNm);
			tagList = sortMapByValue(keywordsMap);
			
//			storeDao.deleteStoreTag(rowData);
//			storeDao.updateStoreTagYn(rowData);
//			tagListInsertHandler(rowData, tagList);
			
			log.debug("Tag creation time of " + cityNm + ": " + String.valueOf((System.nanoTime() - time) / 10000000) + " ns");
			
			for (int i = 0, j = tagList.size(); i < j; i++) {
				rowData = (HashMap) tagList.get(i);
				log.info("key " + rowData.get("key") + ": " + "count: " + rowData.get("cnt"));
				
			}
//		}


	}

}
package com.jtech.springboot_mongodb.svc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtech.springboot_mongodb.dao.MariaDBTestByMyBatis;
import com.jtech.springboot_mongodb.dto.IListData;
import com.jtech.springboot_mongodb.dto.ListDataImpl;
import com.jtech.springboot_mongodb.util.ScrapyHandler;

@Service("storeBizSvcImpl")
public class StoreBizSvcImpl extends BizServiceImpl {

	@Autowired
	private MariaDBTestByMyBatis mariaDBTestByMyBatis;

	@Autowired
	private ScrapyHandler scrapyHandler;

	@SuppressWarnings({ "rawtypes", "unused" })
	public IListData selectListFromDB() throws Exception {

		log.info(">>>>> selectBlogList");

		IListData resultList = new ListDataImpl();

		List anotationQueryResult = mariaDBTestByMyBatis.getUserList();
		List xmlQueryResult = mariaDBTestByMyBatis.getUserDetailById(null);

		return resultList;
	}

	public void scrapyCmdSample() throws Exception {

		List resultList = mariaDBTestByMyBatis.getBatchJobFromQueTb(null);

		if (resultList.size() != 0) {
			
			Map rowMap = (Map) resultList.get(0);
			
			String batchJob = String.valueOf(rowMap.get("BATCHJOBID"));
			String attribute = String.valueOf(rowMap.get("ATTRIBUTE"));
			String regDate = String.valueOf(rowMap.get("REG_DATE"));
			String hostName = getHostName();
			log.info("batch job name: " + batchJob);
			log.info("Attribute: " + attribute);
			log.info("Register date: " + regDate);
			log.info("Host name: " + hostName);
			
			if("".equals(hostName)) {
				log.info("This machine does not have a hostname.");
			} else {
				Map paramMap = new HashMap();
				paramMap.put("HOSTNAME", hostName);
				paramMap.put("BATCHJOBID", batchJob);
				Map returnMap = mariaDBTestByMyBatis.getCrawlerInfo(paramMap);
				String crawlerLocation = returnMap.get("CRAWLER_LOCATION") == null ? "" : String.valueOf(returnMap.get("CRAWLER_LOCATION"));
				log.info(">>> " + hostName + ", crawler location: " + returnMap.get("CRAWLER_LOCATION"));
				if("".equals(crawlerLocation)) {
					log.info("There is no crawler location for this host.");
				} else {
				  
				  paramMap.put("SEQ", rowMap.get("SEQ"));
				  paramMap.put("QUE_STATUS", "L");
				  mariaDBTestByMyBatis.updateBatchJobStatus(paramMap);
				  
				  try {
				    scrapyHandler.runScrapy(crawlerLocation, batchJob, attribute);
			    } catch (Exception e) {
			      log.error("<<<<< " + e.getStackTrace());
			      paramMap.replace("QUE_STATUS", "F");
	          mariaDBTestByMyBatis.updateBatchJobStatus(paramMap);
			    }
					
					paramMap.replace("QUE_STATUS", "D");
          mariaDBTestByMyBatis.updateBatchJobStatus(paramMap);
				}
			}
			
			/*
			 * String crawlerPath = "C:\\devkit\\scrapy\\getBlogContents\\";
			 * String crawlerPath = "/data/scrapy/getBlogContents/"; String
			 * crawlerName = "getBlogContents"; // 13635:가양화로닭발 String etc_args
			 * = "-a p_args=" + "1591";-a p_args=" + "1591
			 * 
			 * scrapyHandler.runScrapy(crawlerPath, crawlerName, etc_args);
			 */
			
		} else {
			log.info("There is no more job to run.");
		}

	}

	public boolean batchJobHandler() {

		return false;
	}

	public String getHostName() {
		String hostname = "";
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException ex) {
			log.info("Hostname can not be resolved.");
		}
		return hostname;
	}

}

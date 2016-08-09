package com.jtech.springboot_mongodb;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	protected Logger log = Logger.getLogger(getClass());

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//	@Autowired
//	private StoreBizSvcImpl storeBizSvcImpl;
	
//	@Autowired
//	private TagHandlingSvc tagHandlingSvc;

	// 1000 = 1 second
	@Scheduled(fixedRate = 300000000)
	public void reportCurrentTime() throws Exception {
		log.info(">>>>> The time is now " + dateFormat.format(new Date()));
		
//		storeBizSvcImpl.selectListFromDB();
//		storeBizSvcImpl.scrapyCmdSample();
//		IListData listData = new ListDataImpl();
		
//		tagHandlingSvc.spainTagCreationHandler(null);
		
	}
	
}

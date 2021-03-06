package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;

@Component
public class WowzaLogScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(WowzaLogScheduler.class);
			
	@Autowired
	private FileInstanceService fileServ;
	
//	/** 활성 여부 설정 */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.WowzaLogScheduler.enable}")
//	private final boolean enableTask = true;
	
	//매달 15일 1시 실행
	@Scheduled(cron = "0 0 1 15 * *")
//	@Scheduled(cron = "10 * * * * *")
	public void wowzaLogCheck()
	{
//		if (!enableTask) {
//			return;
//		}
		
		List<File> logFiles = fileServ.getFiles("WOWZALOG1");

		if (logFiles == null) {
			return;
		}
		
		
		
		for (File file : logFiles) {
			
			
			
			
			boolean fileInfo = file.delete();
			
			if(fileInfo != true) {
				logger.error("Wowza Log File delete error [fileName = {}]", file.getPath());
			}
		}
		
		
		
		logger.info("Wowza Log File delete info [scheduler run info ]");
		
		
//		List<File> logFiles2 = fileServ.getFiles("WOWZALOG2");
//
//		if (logFiles2 == null) {
//			return;
//		}
//		
//		for (File file : logFiles2) {
//			
//			boolean fileInfo = file.delete();
//			
//			if(fileInfo != true) {
//				logger.error("Wowza2 Log File delete error [fileName = {}]", file.getPath());
//			}
//		}
//		
//		dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS-z");
//		
//		logger.info("Wowza2 Log File delete info [scheduler run info = {}]", dateFormat);
		
		
	}
	

}

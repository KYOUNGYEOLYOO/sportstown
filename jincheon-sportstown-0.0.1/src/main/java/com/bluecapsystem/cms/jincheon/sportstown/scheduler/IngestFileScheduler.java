package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class IngestFileScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(IngestFileScheduler.class);
			
	@Autowired
	private FileInstanceService fileServ;
	
//	/** 활성 여부 설정 */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.WowzaLogScheduler.enable}")
//	private final boolean enableTask = true;
	
	//월요일 한시 실행
	@Scheduled(cron = "0 0 1 ? * MON")
	public void ingestFileCheck()
	{
//		if (!enableTask) {
//			return;
//		}
		
		List<File> logFiles = fileServ.getFiles("INGEST");

		if (logFiles == null) {
			return;
		}
		
		Date today = new Date();
	    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
	    String toDay = date.format(today);


        Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(today);
        cal.add(cal.DATE, -7);// 일주일 빼기
        String temp = date.format(cal.getTime());

		
		for (File file : logFiles) {
			
			BasicFileAttributes attrs;
			try {
				attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				
				FileTime time = attrs.creationTime();
			    
			    String pattern = "yyyyMMdd";
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				
			    String formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

			    
			    
			    if(Integer.parseInt(temp) > Integer.parseInt(formatted)){
					boolean fileInfo = file.delete();
					
					if(fileInfo != true) {
						logger.error("ingest  File delete error [fileName = {}]", file.getPath());
					}
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS-z");
		
		logger.info("ingest File delete info [scheduler run info = {}]", dateFormat);
		
		
		
		
	}
	

}

package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;

@Component
public class StreamStatusScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(StreamStatusScheduler.class);
			
	

	@Scheduled(fixedDelay=2000)
	public void streamStatusCheck()
	{
		WowzaCURLApi wowza = new WowzaCURLApi();
		
		
		// wowza.incomingStreams
	}
	

}

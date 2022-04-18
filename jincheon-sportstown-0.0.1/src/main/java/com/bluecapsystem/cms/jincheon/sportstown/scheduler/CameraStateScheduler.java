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

import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.dao.StorageInfoDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.StorageInfo;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraState;
import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.StorageInfoManageService;

@Component
public class CameraStateScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(CameraStateScheduler.class);
			
	@Autowired
	private StorageInfoManageService simServ;

	@Autowired
	private CodeService codeServ;
	
	@Autowired
	private CameraManageService camServ;
	
//	/** 활성 여부 설정 */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.WowzaLogScheduler.enable}")
//	private final boolean enableTask = true;
	
	//매일 한시 실행
	@Scheduled(cron = "0 0 1 * * *")
//	@Scheduled(fixedDelay=5000)
	public void storageCheck()
	{

		
		List<Camera> staticCameras ;

		List<Code> sprotsEvents;
		try {
			sprotsEvents = codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null));
			
			for(int i=0; i< sprotsEvents.size(); i++) {
				CameraSelectCondition camCondition = new CameraSelectCondition();
				camCondition.setSportsEventCode(sprotsEvents.get(i).getCodeId());
				
				staticCameras = camServ.getCameras(camCondition);
				
				for(int j=0; j < staticCameras.size(); j++) {
					camServ.changeStateCamera(staticCameras.get(j).getCamId(), CameraState.DisCon);
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		
		
		
		
	}
	

}

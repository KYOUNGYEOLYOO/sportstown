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

import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.dao.StorageInfoDaoImpl;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.StorageInfo;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;
import com.bluecapsystem.cms.jincheon.sportstown.service.StorageInfoManageService;

@Component
public class StorageInfoScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(StorageInfoScheduler.class);
			
	@Autowired
	private StorageInfoManageService simServ;

	
	
//	/** 활성 여부 설정 */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.WowzaLogScheduler.enable}")
//	private final boolean enableTask = true;
	
	//매일 한시 실행
//	@Scheduled(cron = "0 0 1 * * *")
	@Scheduled(fixedDelay=5000)
	public void storageCheck()
	{
//		if (!enableTask) {
//			return;
//		}
		
		
		
		String  drive;
		double  totalSize, freeSize, useSize;     
		
		String total="";
		String free="";

		File[] roots = File.listRoots();

		for (File root : roots) {

		          

			drive = root.getAbsolutePath();
	
			totalSize = root.getTotalSpace() / Math.pow(1024, 4);
			useSize = root.getUsableSpace() / Math.pow(1024, 4);
			freeSize = totalSize - useSize;
	
			 
			if(drive.equals("Z:\\")) {
				
				System.out.println("\n하드 디스크 이름 : " + drive + "\n");
				
				System.out.println("전체 디스크 용량 : " + totalSize + " TB \n");
		
				System.out.println("디스크 사용 용량 : " + freeSize + " TB \n");
		
				System.out.println("디스크 남은 용량 : " + useSize + " TB \n");
			}
				
			total = String.format("%.2f", totalSize);
			free = String.format("%.2f", freeSize);
			
			List<StorageInfo> storageInfos = null;
			StorageInfo storageInfo = new StorageInfo();
			
			storageInfo.setTotalInfo(total);
			storageInfo.setUseInfo(free);
			
			try {
				storageInfos = simServ.getStorageInfos();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("storageInfos.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+storageInfos.size());
			
			if(storageInfos.size() > 0) {
				IResult resultCode = simServ.deleteStorageInfo();
				
				
				if(resultCode == CommonResult.Success) {
					
					simServ.registStorageInfo(storageInfo);
				}
			}else {
				
				
				simServ.registStorageInfo(storageInfo);
			}
			
			

		}
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS-z");
		
		logger.info("Storage info [scheduler run info = {}]", dateFormat);
		
		
		
		
		
		
		
	}
	

}

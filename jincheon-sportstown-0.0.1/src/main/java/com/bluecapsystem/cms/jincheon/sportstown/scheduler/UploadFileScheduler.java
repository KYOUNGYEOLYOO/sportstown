package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;

@Component
public class UploadFileScheduler 
{
	private static final Logger logger = LoggerFactory.getLogger(UploadFileScheduler.class);
			
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private FileInstanceService fileServ;
	
//	/** 활성 여부 설정 */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.WowzaLogScheduler.enable}")
//	private final boolean enableTask = true;
	
	//매달 15일 2시 실행
	@Scheduled(cron = "0 0 2 15 * *")
//	@Scheduled(fixedDelay = 2000)
	public void uploadFileCheck()
	{
//		if (!enableTask) {
//			return;
//		}
		DateFormat df = new SimpleDateFormat("yyyyMM");

		Calendar cal = Calendar.getInstance( );
//		cal.add ( cal.MONTH, + 1 ); //다음달
		cal.add ( cal.MONTH, -1 ); 
		
		
		String date = df.format(cal.getTime());  
		String year = date.substring(0,4);
		String month = date.substring(4,6);


		File root = new File(StoragePathProperties.getDiretory("UPLOAD"), year+"\\"+month);	
		
		if(root.exists()) {
			
			logger.info("upload File delete info [scheduler run info ]");
			
			
			deleteFolder(root.toString());
//			boolean deleteInfo = root.delete();
//			
//			if(deleteInfo != true) {
//				logger.error("upload  File delete error [path = {}]", root);
//			}
        }else {
        	logger.info("upload File delete 폴더 없음 [scheduler run info path = {}]", root);
        }
	}
	

	public void deleteFolder(String path) {
		
	    File folder = new File(path);
	    try {
	    	if(folder.exists()){
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기
				
                for (int i = 0; i < folder_list.length; i++) {
                	if(folder_list[i].isFile()) {
                		
                		EntityManager em = emf.createEntityManager();
                		IResult resultCode = CommonResult.UnknownError;
                		
                		
            			em.getTransaction().begin();
                		
            			String fileId[] = folder_list[i].getName().split("\\.");
                		
                		
                		resultCode = fileServ.deleteFile(em, fileId[0]);
                		
                		if(resultCode ==  CommonResult.Success ) {
                			folder_list[i].delete();
                			em.getTransaction().commit();
						}
                		
                		
                	}else {
                		deleteFolder(folder_list[i].getPath()); //재귀함수호출
			
                	}
                	folder_list[i].delete();
                }
                folder.delete(); //폴더 삭제
	       }
	   } catch (Exception e) {
		e.getStackTrace();
	   }
    }
}

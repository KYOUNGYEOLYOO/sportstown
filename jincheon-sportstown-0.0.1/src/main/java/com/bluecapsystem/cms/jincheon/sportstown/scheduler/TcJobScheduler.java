package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Transient;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bcs.transcorder.ffmpeg.FFMpegProcess;
import com.bcs.transcorder.ffmpeg.FFmpegStream;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.properties.FFMpegProperties;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.FileInstanceResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.TcJobSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownFileInstanceMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.TcJob;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.TcJobService;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@Component
public class TcJobScheduler {
	private static final Logger logger = LoggerFactory.getLogger(TcJobScheduler.class);

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private TcJobService tcServ;

	@Autowired
	private ThumbnailInstanceService thumbnailServ;

	@Autowired
	private UserManageService userServ;

	@Autowired
	private FileInstanceService fileServ;

	@Autowired
	private CodeService codeServ;

	/** ?????? ?????? ?????? */
//	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.FileIngestScheduler.enable}")
//	private final boolean enableTask = true;

//	@Scheduled(fixedDelay = 2000L)
	@Scheduled(fixedDelay = 30000)
	
	@Transient
	public void registIngestFile() {

//		if (!enableTask) {
//			return;
//		}
		
		
		
			
		_TRANS: {
			List<TcJob> jobs = null;
			String jobId= "";
			try {
				
				jobs = tcServ.getTcJobs(new TcJobSelectCondition(null,"I"));
				
					
				
				
				if(jobs.size()>1) {
					break _TRANS;
				}
				
				jobs = null;
				
				jobs = tcServ.getTcJobs(new TcJobSelectCondition(null,"W"));
				
				
				for(int i= 0; i < jobs.size(); i++) {
					StringBuilder ffmpegLog = new StringBuilder();
					int retValue = -1;
					
					jobId = jobs.get(i).getTcId();
					
					String filePath = jobs.get(i).getFilePath();
					String fileName = jobs.get(i).getFileName();
					String temp[] = fileName.split("\\.");
					
					
					tcServ.modifyTcJob(jobs.get(i).getTcId(), "I");
					
					File contentDir = StoragePathProperties.getDiretory("CONTENT");
										
					File input = new File(contentDir , filePath+fileName);
					File input2 = new File(contentDir , filePath+temp[0]+".mp4");
					File output = new File(contentDir , filePath+temp[0]+"_0.mp4");
					
					FFMpegProcess ffProc = new  FFMpegProcess(FFMpegProperties.getFFfmpeg(), input, "",  output);
					
					String command = ffProc.commandString();
					
					Process p = ffProc.createProcess();
					FFmpegStream stream = new FFmpegStream(p);
					
					
					if(stream.initailize() == false)
					{
						logger.error(" ?????? ????????? ???????????? ?????? ??????????????? [command={}]", command);
						continue;	
					}
					
					String str = null;
					while((str = stream.readLine()) != null)
					{
						ffmpegLog.append(str);
						ffmpegLog.append("\n");
						
					}
					
					
					
					retValue = p.exitValue();
					
					
					stream.close();
					ffProc.close(p);
					
					if(retValue != 0) {
						tcServ.modifyTcJob(jobs.get(i).getTcId(), "E");
						logger.error("tc ?????? ?????? [tcId={}] => ", jobs.get(i).getTcId());
					}else {
						tcServ.modifyTcJob(jobs.get(i).getTcId(), "C");
						logger.debug("tc ?????? ?????? [tcId={}] => ", jobs.get(i).getTcId());
						
						if(input.delete()) {
							boolean result = output.renameTo(input2);

							output = null;
							
							
							EntityManager em = emf.createEntityManager();
							em.getTransaction().begin();
							
							FileInstance fileInstance = fileServ.getFileinstance(em, jobs.get(i).getFileId());
							
							fileInstance.setExtension("mp4");
							fileInstance.setFileName(temp[0]+".mp4");
							
							IResult resultCode = fileServ.updateFile(em, fileInstance);
							
							if (resultCode != CommonResult.Success) {
								em.getTransaction().rollback();
								logger.error("tc ?????? ?????? ?????? ?????? ?????? [fileId={}] => ", jobs.get(i).getFileId());
							}else {
								logger.debug("tc ?????? ?????? ?????? ?????? ?????? [fileId={}] => ", jobs.get(i).getFileId());
								em.getTransaction().commit();
							}
								
							
							
							em.close();
						}
						
						
					
					}

					
					
					
					

					
				}
				
			} catch (Exception ex) {
				tcServ.modifyTcJob(jobId, "E");
				logger.error("tc ?????? ?????? [tcId={}] => \n{}",  jobId, ExceptionUtils.getFullStackTrace(ex));
				break _TRANS;
			}
		}
			
		
		
		
	}
}

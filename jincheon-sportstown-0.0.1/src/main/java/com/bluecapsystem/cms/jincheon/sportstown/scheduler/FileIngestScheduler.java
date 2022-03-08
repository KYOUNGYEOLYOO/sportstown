package com.bluecapsystem.cms.jincheon.sportstown.scheduler;

import java.io.File;
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

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.FileInstanceResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownFileInstanceMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@Component
public class FileIngestScheduler {
	private static final Logger logger = LoggerFactory.getLogger(FileIngestScheduler.class);

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private FileInstanceService fileServ;

	@Autowired
	private ThumbnailInstanceService thumbnailServ;

	@Autowired
	private UserManageService userServ;

	@Autowired
	private CameraManageService camServ;

	@Autowired
	private CodeService codeServ;

	/** 활성 여부 설정 */
	@Value("${com.bluecapsystem.cms.jincheon.sportstown.scheduler.FileIngestScheduler.enable}")
	private final boolean enableTask = true;

//	@Scheduled(fixedDelay = 2000)
	@Scheduled(fixedDelay = 2000000)
	
	@Transient
	public void registIngestFile() {

		if (!enableTask) {
			return;
		}

		List<File> srcFiles = fileServ.getFiles("INGEST");

		if (srcFiles == null) {
			return;
		}

		for (File file : srcFiles) {
			if (file.getName().toLowerCase().endsWith(".tmp") || file.getName().toLowerCase().endsWith(".png")) {
				continue;
			}

			EntityManager em = emf.createEntityManager();

			IResult resultCode = CommonResult.UnknownError;
			try {
				em.getTransaction().begin();

				_TRANSACTION: {
					FileInstance fileInst = FileInstance.createInstance("INGEST", "RECORD", file);
					String[] fileNameCodes = file.getName().split("_");

					if (fileNameCodes.length < 4) {
						logger.error("File name format error [fileName = {}]", file.getPath());
						break _TRANSACTION;
					}

					if (fileInst == null) {
						resultCode = FileInstanceResult.CannotAccessFile;

					} else {
						// thumbnail 생성
						ThumbnailInstance thumbnail = thumbnailServ.createThumbnail("FILE", file);

						if (thumbnail == null) {
							logger.error("fail of create thumbnail [fileName = {}]", file.getPath());
							break _TRANSACTION;
						}

						// meta 정보를 생성 한다
						SimpleDateFormat recordFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS-z");
						Camera camera = camServ.getCamera(fileNameCodes[0]);
						Code sportEvent = codeServ.getCode(fileNameCodes[1]);
						User recordUser = userServ.getUser(fileNameCodes[2], null);
						Date recordDate = recordFormat.parse(fileNameCodes[3]);

						if (camera == null || sportEvent == null || recordUser == null || recordDate == null) {
							logger.error("fail of parse meta codes [fileName = {}]", file.getPath());
							break _TRANSACTION;
						}

						SportstownFileInstanceMeta meta = new SportstownFileInstanceMeta();
						meta.setCamId(camera.getCamId());
						meta.setRecordDate(recordDate);
						meta.setRecordUserId(recordUser.getUserId());
						meta.setSportsEventCode(sportEvent.getCodeId());

						String orignFileName = String.format("%s_%s_%s.%s", camera.getName(), recordUser.getUserName(), //
							recordFormat.format(recordDate), fileInst.getExtension());
						fileInst.setOrignFileName(orignFileName);

						// thumbnail 부터 db 에 등록 한다
						resultCode = thumbnailServ.registThumbnail(em, thumbnail);
						if (resultCode != CommonResult.Success) {
							logger.error("fail of regist thumbnail");
							break _TRANSACTION;
						}

						fileInst.setThumbnailId(thumbnail.getThumbnailId());
						resultCode = fileServ.registFile(em, fileInst, meta);
						if (resultCode != CommonResult.Success) {
							logger.error("fail of regist file instance [fileInst = {}]", fileInst);
							break _TRANSACTION;
						}

						SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
						String descFilePath = String.format("%s/%s.%s", sdf.format(new Date()), fileInst.getFileId(),
							fileInst.getExtension());

						resultCode = fileServ.transferFile(em, fileInst, "UPLOAD", descFilePath);
						if (resultCode != CommonResult.Success) {
							logger.error("fail of transfer file instance => [fileInst = {}]", fileInst);
							break _TRANSACTION;
						}
						
//						boolean fileInfo = file.delete();
//						
//						if(fileInfo != true) {
//							logger.error("ingest  File delete error [fileName = {}]", file.getPath());
//							break _TRANSACTION;
//						}
					}
					logger.debug("FileIngestScheduler.registIngestFile 에서 파일을 등록 결과 [fileInstance={}] => {}", fileInst, resultCode);
				}

			} catch (Exception ex) {
				logger.error("파일 인스턴스 생성 실패 [file={}]\n{}", file, ExceptionUtils.getFullStackTrace(ex));
			} finally {
				logger.debug("transation 종료");
				if (resultCode != CommonResult.Success) {
					em.getTransaction().rollback();
				} else {
					em.getTransaction().commit();
				}
				em.close();
				logger.debug("EntityManager Closed");
			}
		}
	}
}

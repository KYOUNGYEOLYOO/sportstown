package com.bluecapsystem.cms.jincheon.sportstown.json.content;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.ContentInstance;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.ContentService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownContentSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData.DataType;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownFileInstanceMeta;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.service.DashboardDataManageService;

@RestController
@RequestMapping("/service/content")
public class ContentJsonController {

	private static final Logger logger = LoggerFactory.getLogger(ContentJsonController.class);

	@Autowired
	private ContentService contentServ;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private FileInstanceService fileServ;

	@Autowired
	private ThumbnailInstanceService thumbnailServ;
	
	@Autowired
	private DashboardDataManageService dashboardDataManageServ;
	
	

	@RequestMapping("/registContentWithFile")
	public ModelAndView registContentWithFile(@ModelAttribute Content content, @ModelAttribute SportstownContentMeta meta,
			@RequestParam("file") MultipartFile file,
			@ModelAttribute DashboardData dashboardData) {

		logger.debug("upload file name [content={}], [meta={}] => {}", content, meta, file.getOriginalFilename());
		IResult resultCode = CommonResult.UnknownError;

		// logger.debug("컨텐츠 & 파일 등록 결과 [content={}] => {}", content, resultCode);

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		_TRANSACTION: {
			// 파일 부터 등록 시작
			File uploadDir = StoragePathProperties.getDiretory("UPLOAD");
			File uploadFile = new File(uploadDir, file.getOriginalFilename());

			FileInstance fileInst = null;
			try {
				file.transferTo(uploadFile);
				fileInst = FileInstance.createInstance("UPLOAD", "UPLOAD", uploadFile);
				SportstownFileInstanceMeta fileInstMeta = new SportstownFileInstanceMeta();
				fileInstMeta.setSportsEventCode(meta.getSportsEventCode());
				fileInstMeta.setRecordUserId(meta.getRecordUserId());
				fileInst.setOrignFileName(file.getOriginalFilename());

				ThumbnailInstance thumbnail = thumbnailServ.createThumbnail("FILE", uploadFile);
				if (thumbnail != null) {
					// thumbnail 부터 db 에 등록 한다
					resultCode = thumbnailServ.registThumbnail(em, thumbnail);
					if (resultCode != CommonResult.Success)
						break _TRANSACTION;

					fileInst.setThumbnailId(thumbnail.getThumbnailId());
				}
				resultCode = fileServ.registFile(em, fileInst, fileInstMeta);

				if (resultCode != CommonResult.Success)
					break _TRANSACTION;

				SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
				String descFilePath = String.format("%s/%s.%s", sdf.format(new Date()), fileInst.getFileId(), fileInst.getExtension());

				resultCode = fileServ.transferFile(em, fileInst, "UPLOAD", descFilePath);
				if (resultCode != CommonResult.Success)
					break _TRANSACTION;

				ContentInstance ctInst = content.getInstances().get(0);
				ctInst.setFileId(fileInst.getFileId());

			} catch (Exception ex) {
				logger.error("컨텐츠 & 파일 저장 중 오류 발생 [content={}] \n{}", content, ExceptionUtils.getFullStackTrace(ex));
			}

			// 컨텐츠 등록
			content.setContentMeta(meta);
			resultCode = contentServ.registContent(em, content);
			
			if (resultCode != CommonResult.Success)
				break _TRANSACTION;
			
			dashboardData.setUserType(DataType.Contents);
			dashboardData.setRegistDate(content.getRegistDate());
			dashboardData.setUserId(meta.getRecordUserId());
			dashboardData.setSportsEventCode(meta.getSportsEventCode());
			
			dashboardDataManageServ.registDashboardData(dashboardData);
			
		}

		logger.debug("컨텐츠 & 파일 저장결과 [content={}] => {}", content, resultCode);

		if (resultCode != CommonResult.Success)
			em.getTransaction().rollback();
		else
			em.getTransaction().commit();

		em.close();

		ModelAndView mnv = new ModelAndView("jsonView");
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@RequestMapping("/registContent")
	public ModelAndView registContent(@ModelAttribute Content content, @ModelAttribute SportstownContentMeta meta,
			@ModelAttribute DashboardData dashboardData) {
		ModelAndView mnv = new ModelAndView("jsonView");
		
		EntityManager em = emf.createEntityManager();

		IResult resultCode = CommonResult.UnknownError;
		
		em.getTransaction().begin();

		_TRANSACTION: {
			
			content.setContentMeta(meta);

			resultCode = contentServ.registContent(content);
			
			if (resultCode != CommonResult.Success)
				break _TRANSACTION;
			
			dashboardData.setUserType(DataType.Archive);
			dashboardData.setRegistDate(content.getRegistDate());
			dashboardData.setUserId(meta.getRecordUserId());
			dashboardData.setSportsEventCode(meta.getSportsEventCode());
			
			dashboardDataManageServ.registDashboardData(dashboardData);
			
		}
		


		logger.debug("컨텐츠 등록 결과 [content={}] => {}", content, resultCode);
		mnv.addObject("content", content);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@RequestMapping("/modifyContent")
	public ModelAndView modifyContent(@ModelAttribute Content content, @ModelAttribute SportstownContentMeta meta) {
		ModelAndView mnv = new ModelAndView("jsonView");

		content.setContentMeta(meta);

		IResult resultCode = contentServ.modifyContent(content);

		logger.debug("컨텐츠 수정 결과 [content={}] => {}", content, resultCode);
		mnv.addObject("content", content);
		mnv.addObject("resultCode", resultCode);

		return mnv;
	}


	@RequestMapping("/deleteContent")
	public ModelAndView modifyContent(@RequestParam(value="contentId") String contentId ) {
		ModelAndView mnv = new ModelAndView("jsonView");

		IResult resultCode = contentServ.deleteContent(contentId);

		logger.debug("컨텐츠 삭제 결과 [contentId={}] => {}", contentId, resultCode);
		mnv.addObject("contentId", contentId);
		mnv.addObject("resultCode", resultCode);

		return mnv;
	}

	@CrossOrigin
	@RequestMapping("/getContents")
	public ModelAndView getContents(HttpServletRequest request, @ModelAttribute SportstownContentSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = CommonResult.UnknownError;
		List<ContentMeta> contents = null;

		try {
			Paging paging = JqGridParameterParser.getPaging(request);
			condition.setPaging(paging);

			contents = contentServ.getContentList(condition);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("contents", contents);

			if (condition.getPaging() != null && condition.getPaging().getEnablePaging()) {
				JqGridParameterParser.setPaging(mnv, condition.getPaging());
			}
		}

		return mnv;
	}
}

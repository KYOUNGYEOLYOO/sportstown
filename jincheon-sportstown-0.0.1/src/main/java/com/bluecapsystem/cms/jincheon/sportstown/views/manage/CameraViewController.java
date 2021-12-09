package com.bluecapsystem.cms.jincheon.sportstown.views.manage;


import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.Property;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.PropertyService;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownFileInstanceSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;

@Controller
@RequestMapping("/camera")
public class CameraViewController {

	private static final Logger logger = LoggerFactory.getLogger(CameraViewController.class);

	@Autowired
	private CameraManageService camServ;


	@Autowired
	private CodeService codeServ;


	@Autowired
	private FileInstanceService fileServ;


	@Autowired
	private PropertyService propServ;


	@RequestMapping("/monitor")
	public ModelAndView monitor()
	{
		ModelAndView mnv = new ModelAndView("/camera/cameraModitor");
		try
		{
			loadCodes(mnv);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return mnv;
	}

	@RequestMapping("/manage")
	public ModelAndView manage()
	{
		ModelAndView mnv = new ModelAndView("/camera/cameraManage");
		try
		{
			loadCodes(mnv);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return mnv;
	}

	@RequestMapping("/regist")
	public ModelAndView regist()
	{

		ModelAndView mnv = new ModelAndView("/camera/cameraRegist");

		try
		{
			CodeSelectCondition condition = new CodeSelectCondition("CAMERA_LOCATION", null);
			condition.setHasNotUsed(false);

			loadCodes(mnv);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return mnv;
	}

	@RequestMapping("/modify/{camId}")
	public ModelAndView modify(
			@PathVariable("camId") String camId)
	{
		ModelAndView mnv = new ModelAndView("/camera/cameraModify");
		try
		{
			CameraSelectCondition condition = new CameraSelectCondition(camId);
			condition.setHasStreamMeta(true);
			Camera camera = camServ.getCamera(condition);
			mnv.addObject("camera", camera);

			loadCodes(mnv);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}


		return mnv;
	}


	@RequestMapping("/list")
	public ModelAndView list(
			@RequestParam(name="listId", defaultValue="") String listId,
			@RequestParam(name="pagerId", defaultValue="") String pagerId)
	{

		ModelAndView mnv = new ModelAndView("/camera/cameraList");

		mnv.addObject("listId", listId);
		mnv.addObject("pagerId", pagerId);

		return mnv;
	}

	@RequestMapping("/detail/{camId}")
	public ModelAndView detail(
			@PathVariable("camId") String camId)
	{
		ModelAndView mnv = new ModelAndView("/camera/cameraDetail");
		try
		{
			if(camId != null)
			{
				CameraSelectCondition condition = new CameraSelectCondition(camId);
				condition.setHasStreamMeta(true);
				Camera camera = camServ.getCamera(condition);
				mnv.addObject("camera", camera);
			}

			loadCodes(mnv);
		}catch(Exception ex) {
			logger.error("카메라 상세 조회 오류 [camId = {}]\n{}", camId,
					ExceptionUtils.getFullStackTrace(ex));
		}

		return mnv;
	}


	@RequestMapping("/live/{camId}")
	public ModelAndView live(
			@PathVariable("camId") String camId)
	{
		ModelAndView mnv = new ModelAndView("/camera/cameraLive");
		try
		{
			Property streamer = propServ.getProperty("WOWZA_PROPERTIES", "BASE_LIVE_URL");
			mnv.addObject("streamer", streamer.valueToString());

			CameraSelectCondition condition = new CameraSelectCondition(camId);
			condition.setHasStreamMeta(true);
			Camera camera = camServ.getCamera(condition);
			mnv.addObject("camera", camera);

		}catch(Exception ex)
		{
			logger.error("카메라 실시간 조회 오류 [camId = {}]\n{}", camId,
					ExceptionUtils.getFullStackTrace(ex));
		}

		return mnv;
	}

	@RequestMapping("/recordFiles/{camId}")
	public ModelAndView recordFiles(
			@PathVariable("camId") String camId,
			@RequestParam(name="currentFileId", required = false) String currentFileId  )
	{
		ModelAndView mnv = new ModelAndView("/camera/recordFiles");

		SportstownFileInstanceSelectCondition condition = new SportstownFileInstanceSelectCondition();

		condition.setHasNotDeleted(false);
		condition.setCamId(camId);


		try
		{
			List<FileInstance> files = fileServ.getFileinstances(condition);

			mnv.addObject("files", files);
			mnv.addObject("currentFileId", currentFileId);


			String vodStreamer = propServ.getProperty("WOWZA_PROPERTIES", "BASE_VOD_URL").valueToString();
			mnv.addObject("vodStreamer", vodStreamer);

			Camera camera = camServ.getCamera(camId);
			mnv.addObject("camera", camera);

			if(EmptyChecker.isNotEmpty(currentFileId))
			{
				FileInstance currentFile = fileServ.getFileinstance(currentFileId);

				// 녹화 등록된 파일에 관하여 체크 필요
				String fileRootUri = propServ.getProperty("FILE_URL_ROOT", currentFile.getLocationRootCode()).valueToString();
				mnv.addObject("fileRootUri", fileRootUri);

				mnv.addObject("currentFile", currentFile);
			}

		}catch(Exception ex)
		{
			logger.error("카메라 녹화 파일 조회 오류 [camId = {}, currentFileId = {}]\n{}",
					camId, currentFileId,
					ExceptionUtils.getFullStackTrace(ex));
		}


		return mnv;
	}

	private void loadCodes(ModelAndView mnv) throws Exception
	{
		/**
		 * 카메라 위치
		 */
		mnv.addObject("locations", codeServ.getCodes(new CodeSelectCondition("CAMERA_LOCATION", null)));

		/**
		 * 스포츠 종목
		 */
		mnv.addObject("sprotsEvents", codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null)));


		/**
		 * WOWZA Application list
		 */
		mnv.addObject("applications", codeServ.getCodes(new CodeSelectCondition("WOWZA_APP_LIST", null)));


		// wowza stream server list
		mnv.addObject("streamServers", codeServ.getCodes(new CodeSelectCondition("STREAM_SERVERS", null)));
	}
}

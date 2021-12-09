package com.bluecapsystem.cms.jincheon.sportstown.json.camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.PropertyService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraState;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraType;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.CameraStreamMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.CameraStreamMeta.WowzaMetaClass;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/service/camera")
public class CameraJsonController {

	private static final Logger logger = LoggerFactory.getLogger(CameraJsonController.class);

	private static final String MARKUP_STREAM_SERVER = "{STREAM_SERVER}";

	@Autowired
	private CameraManageService camServ;

	@CrossOrigin
	@RequestMapping(value = "/getCameras")
	public ModelAndView getCameras(HttpServletRequest request, @ModelAttribute("condition") CameraSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		Paging paging = JqGridParameterParser.getPaging(request);
		condition.setPaging(paging);

		IResult resultCode = CommonResult.UnknownError;
		List<Camera> cameras = null;
		try {
			condition.setHasStreamMeta(true);
			cameras = camServ.getCameras(condition);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("cameras", cameras);
			if (condition.getPaging() != null && condition.getPaging().getEnablePaging()) {
				JqGridParameterParser.setPaging(mnv, condition.getPaging());
			}
		}

		return mnv;
	}

	/**
	 * 스포츠 종목에 소속된 모든 camera 정보를 가져온다 (유동 포함)
	 * @param sportsEventCode
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getAllCameras/{sportsEventCode}")
	public ModelAndView getAllCameras(@PathVariable("sportsEventCode") String sportsEventCode) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = CommonResult.UnknownError;

		CameraSelectCondition condition = new CameraSelectCondition();
		List<Camera> staticCameras = Collections.emptyList();
		List<Camera> shiftCameras = Collections.emptyList();

		try {
			condition.setSportsEventCode(sportsEventCode);
			condition.setCameraType(CameraType.Static);
			staticCameras = camServ.getCameras(condition);

			if(staticCameras.size() > 0) {
				Code locationCode = staticCameras.get(0).getLocation();
				condition = new CameraSelectCondition();
				condition.setSportsEventCode(sportsEventCode);
				condition.setCameraType(CameraType.Shift);
				condition.setLocationCode(locationCode.getCodeId());
				shiftCameras = camServ.getCameras(condition);
			}

			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {

			List<Camera> cameras = new ArrayList<Camera>();
			cameras.addAll(staticCameras);
			cameras.addAll(shiftCameras);

			mnv.addObject("resultCode", resultCode);
			mnv.addObject("cameras", cameras);
			mnv.addObject("staticCameras", staticCameras);
			mnv.addObject("shiftCameras", shiftCameras);
		}

		return mnv;
	}

	@CrossOrigin
	@RequestMapping(value = "/getCamera/{camId}")
	public ModelAndView getCamera(@PathVariable("camId") String camId, @ModelAttribute CameraSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		IResult resultCode = CommonResult.UnknownError;
		Camera camera = null;
		try {
			condition.setCamId(camId);
			camera = camServ.getCamera(condition);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("camera", camera);
		}

		return mnv;
	}

	// @CrossOrigin
	@RequestMapping(value = "/registCamera")
	public ModelAndView registCamera(@ModelAttribute Camera camera) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = camServ.registCamera(camera);

		logger.debug("카메라 등록 요청 결과 [camera={}] => {}", camera, resultCode);

		mnv.addObject("camera", camera);
		mnv.addObject("resultCode", resultCode);

		return mnv;
	}

	@RequestMapping(value = "/modifyCamera")
	public ModelAndView modifyCamera(@ModelAttribute Camera camera) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = camServ.modifyCamera(camera);

		logger.debug("카메라 수정 요청 결과 [camera={}] => {}", camera, resultCode);

		camera = camServ.getCamera(camera.getCamId());

		mnv.addObject("camera", camera);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@RequestMapping(value = "/removeCamera/{camId}")
	public ModelAndView removeCamera(@PathVariable("camId") String camId) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = camServ.deleteCamera(camId);

		logger.debug("카메라 삭제 요청 결과 [camId={}] => {}", camId, resultCode);

		mnv.addObject("camId", camId);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@Autowired
	private PropertyService propServ;

	// recording
	@ResponseBody
	@RequestMapping("/record/{camId}")
	public ModelAndView record(@PathVariable("camId") String camId, @RequestParam(name = "recordUserId") String recordUserId,
			@RequestParam(name = "sportsEventCode") String sportsEventCode) {
		ModelAndView mnv = new ModelAndView("jsonView");
		Boolean isSuccess = false;
		String message = "";
		String chkData = "";
		
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		Camera camera = null;

		try {
			CameraSelectCondition condition = new CameraSelectCondition(camId);
			condition.setHasStreamMeta(true);
			camera = camServ.getCamera(condition);
			logger.info("record - camera : {}", camera);
			
			String streamFile = null;
			String outputPath = null;
			String outputFile = null;
			String application = null;
			String streamServer = null;
			String baseUrl = null;

			for (CameraStreamMeta meta : camera.getStreamMetaItems()) {
				if (EmptyChecker.isEmpty(meta.getStreamName()))
					continue;

				if (meta.getMetaClass() != WowzaMetaClass.HD)
					continue;

				streamFile = meta.getStreamName();

				if (meta.getApplication() != null) {
					application = meta.getApplication().getName();
					streamServer = meta.getStreamServer().getName();
					// 2018.01.23 서버 ip 설정 문제로 하드 코드함.
					// streamServer = convertStreamServerIp(streamServer);
					streamServer = IPFilterConstant.getInstance().convertToInternalAddress(streamServer);
				}
				outputFile = String.format("%s_%s_%s.mp4", camera.getCamId(), sportsEventCode, recordUserId);
			}

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
			outputPath = propServ.getProperty("WOWZA_PROPERTIES", "RECORD_OUTPUT_PATH").valueToString();
			chkData = "baseUrl : " + baseUrl + " application : " + application.toString() + " streamFile : " + streamFile + " outputPath : " + outputPath + " outputFile : " + outputFile;
			if (EmptyChecker.isEmpty(baseUrl) || EmptyChecker.isEmpty(application) || EmptyChecker.isEmpty(streamFile) || EmptyChecker.isEmpty(outputPath)
					|| EmptyChecker.isEmpty(outputFile)) {
				logger.error("record ===> 인자가 잘못 되었습니다. [baseUrl={}, application={}, streamFile={}, outputPath={}, outputFile={}]", //
						baseUrl, application, streamFile, //
						outputPath, outputFile);
			} else {
				String result = wowzaApi.recordStream(baseUrl, application, streamFile, outputPath, outputFile);
				Map stopResultMap = new Gson().fromJson(result, Map.class);
				isSuccess = (Boolean) stopResultMap.get("success");
				message = (String) stopResultMap.get("message");
				camServ.changeStateCamera(camId, CameraState.Recording);
			}

		} catch (Exception ex) {
			logger.error("녹화 시작 오류 발생 recordStop [camId = {}]\n{}", //
					camId, //
					ExceptionUtils.getFullStackTrace(ex));
			isSuccess = false;
			message = "시스템 오류 발생_녹화시작 - " + ex.toString();
		} finally {
			mnv.addObject("isSuccess", isSuccess);
			mnv.addObject("message", message);
			mnv.addObject("camId", camId);
			mnv.addObject("chkData", chkData);
		}

		return mnv;

	}

	@RequestMapping(value = "/stopRecord/{camId}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView recordStop(@PathVariable("camId") String camId, @RequestParam(value = "isCoercion", defaultValue = "false") Boolean isCoercion) {
		ModelAndView mnv = new ModelAndView("jsonView");

		Boolean isSuccess = false;
		String message = "";

		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		Camera camera = null;

		try {
			CameraSelectCondition condition = new CameraSelectCondition(camId);
			condition.setHasStreamMeta(true);
			camera = camServ.getCamera(condition);

			String streamFile = null;
			String application = null;
			String streamServer = null;
			String baseUrl = null;

			for (CameraStreamMeta meta : camera.getStreamMetaItems()) {
				if (EmptyChecker.isEmpty(meta.getStreamName()))
					continue;

				if (meta.getMetaClass() != WowzaMetaClass.HD) {
					continue;
				}
				streamFile = meta.getStreamName();
				if (meta.getApplication() != null) {
					application = meta.getApplication().getName();
					streamServer = meta.getStreamServer().getName();
					// 2018.01.23 서버 ip 설정 문제로 하드 코드함.
					// streamServer = convertStreamServerIp(streamServer);
					streamServer = IPFilterConstant.getInstance().convertToInternalAddress(streamServer);
				}
			}

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

			if (EmptyChecker.isEmpty(baseUrl) || EmptyChecker.isEmpty(application) || EmptyChecker.isEmpty(streamFile)) {
				logger.error("recordStrop ===> 인자가 잘못 되었습니다. [baseUrl={}, application={}, streamFile={}]", //
						baseUrl, application, streamFile);
			} else {
				String result = wowzaApi.actionStream(baseUrl, application, streamFile, "stopRecording");
				Map stopResultMap = new Gson().fromJson(result, Map.class);
				isSuccess = (Boolean) stopResultMap.get("success");
				message = (String) stopResultMap.get("message");

				camServ.changeStateCamera(camId, CameraState.Wait);
			}

		} catch (Exception ex) {
			logger.error("녹화 중지 오류 발생 recordStop [camId = {}]\n{}", //
					camId, //
					ExceptionUtils.getFullStackTrace(ex));
			
			logger.info("녹화 중지 오류 발생 recordStop [camId = {}]\n{}", //
					camId, //
					ExceptionUtils.getFullStackTrace(ex));
			isSuccess = false;
			message = "시스템 오류 발생_녹화종료 - " + ex;
		} finally {

			if (isCoercion == true) {
				camServ.changeStateCamera(camId, CameraState.Wait);
				isSuccess = true;
			}

			mnv.addObject("isSuccess", isSuccess);
			mnv.addObject("message", message);
			mnv.addObject("camId", camId);
		}
		return mnv;
	}
}

package com.bluecapsystem.cms.jincheon.sportstown.json.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
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
import com.bluecapsystem.cms.core.data.entity.Property;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.EmResult;
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
	public ModelAndView getCameras(HttpServletRequest request,
			@ModelAttribute("condition") CameraSelectCondition condition) {
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
	 * 
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

			if (staticCameras.size() > 0) {
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
	public ModelAndView getCamera(@PathVariable("camId") String camId,
			@ModelAttribute CameraSelectCondition condition) {
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
		// 카메라 등록하는 부분
		ModelAndView mnv = new ModelAndView("jsonView");
		/* IResult resultCode = camServ.registCamera(camera); */
		IResult _resultCode = CommonResult.UnknownError;
		EmResult resultCode = null;
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		EntityManager em = null;
		String finalUrl = "";
		
		try {
			_TRANS: {
				resultCode = camServ.registCamera(camera);
				_resultCode = resultCode.getResult();
//				em = resultCode.getEm();
				
				if (_resultCode != CommonResult.Success) {
					break _TRANS;
				}

				em = resultCode.getEm();
				String applicationCode = null;
				String streamName = null;
				String streamSourceUrl = null;
				String streamServer = null;

				String baseUrl = null;
				String application = null;
				String streamFile = null;

				String result = "";
				applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
				streamName = camera.getStreamMetaItems().get(0).getStreamName();
				streamName = streamName.replace(".stream", "");
				streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
				

				System.out.println(camera.getStreamMetaItems().get(0).getStreamServer());
				streamSourceUrl = camera.getStreamMetaItems().get(0).getStreamSourceUrl();

				Code code = em.find(Code.class, applicationCode);
				applicationCode = code.getName();

				code = em.find(Code.class, streamServer);
				streamServer = code.getName();

				baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
				baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

				switch (applicationCode) {
				case "Dlive":
					applicationCode = "Dlive";
					result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
					finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
					break;
				case "live":
					applicationCode = "live";
					result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
					finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
					break;
				case "vod":
					applicationCode = "vod";
					result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
					finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
					break;
				default:
					applicationCode = "Dlive";
					result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
					finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
					break;
				}

				Map stopResultMap = new Gson().fromJson(result, Map.class);

				Boolean isSuccess = false;

				isSuccess = (Boolean) stopResultMap.get("success");

				if (isSuccess != true) {
					_resultCode = CommonResult.SystemError;
					break _TRANS;
				}
			}
		} catch (Exception e) {
			_resultCode = CommonResult.SystemError;
		} finally {
			if (_resultCode != CommonResult.Success) {
				em.getTransaction().rollback();
			} else {
				em.getTransaction().commit();
			}
			em.close();
		}

		mnv.addObject("camera", camera);
		/* mnv.addObject("resultCode", resultCode); */
		mnv.addObject("resultCode", _resultCode);

		return mnv;
	}
	

	/*
	 * @RequestMapping(value = "/registCameraW") public ModelAndView
	 * registCameraW(HttpServletRequest request) { ModelAndView mnv = new
	 * ModelAndView("jsonView");
	 * 
	 * Boolean isSuccess = false; String message = ""; String applicationCode =
	 * null; String streamName = null; String streamSourceUrl = null; String
	 * streamServer = null; WowzaCURLApi wowzaApi = new WowzaCURLApi();
	 * 
	 * 
	 * 20211224 trycatch문으로 고치는 중
	 * 
	 * try { String baseUrl = null; String application = null; String streamFile =
	 * null;
	 * 
	 * String result = ""; applicationCode = (String) request.getParameter("app");
	 * streamName = (String) request.getParameter("streamName"); streamServer =
	 * (String) request.getParameter("streamServer"); streamSourceUrl = (String)
	 * request.getParameter("streamSourceUrl");
	 * 
	 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
	 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+applicationCode);
	 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+streamName);
	 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+streamServer);
	 * System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+streamSourceUrl);
	 * 
	 * 
	 * baseUrl = propServ.getProperty("WOWZA_PROPERTIES",
	 * "BASE_REST_URL").valueToString(); // 여기서 에러가 남... nullpointerException이래
	 * baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
	 * 
	 * switch( applicationCode ) { case "Dlive": applicationCode = "Dlive"; result =
	 * wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
	 * break; case "live" : applicationCode = "live"; result =
	 * wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
	 * break; case "vod" : applicationCode = "vod"; result =
	 * wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
	 * break; default: applicationCode = "Dlive"; result =
	 * wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
	 * break; }
	 * 
	 * Map stopResultMap = new Gson().fromJson(result, Map.class); isSuccess =
	 * (Boolean) stopResultMap.get("success"); message = (String)
	 * stopResultMap.get("message"); }catch (Exception ex){
	 * logger.error(ExceptionUtils.getFullStackTrace(ex)); isSuccess = false;
	 * message = "시스템 오류 발생_카메라_와우자등록" + ex.toString();
	 * logger.error("에러에러에러에러에러에러 >>>>> : " + applicationCode);
	 * logger.error("에러에러에러에러에러에러 >>>>> : " + streamServer);
	 * logger.error("에러에러에러에러에러에러 >>>>< : "); Enumeration params =
	 * request.getParameterNames(); while(params.hasMoreElements()) { String name =
	 * (String) params.nextElement(); System.out.print(name + " : " +
	 * request.getParameter(name) + "     "); } System.out.println(); } finally {
	 * mnv.addObject("isSuccess",isSuccess); mnv.addObject("message",message);
	 * mnv.addObject("app", applicationCode); mnv.addObject("streamName",
	 * streamName); mnv.addObject("streamServer", streamServer);
	 * mnv.addObject("streamSourceUrl", streamSourceUrl); } return mnv; }
	 */
	
	@RequestMapping(value = "/connectStreamW/{camId}")
	public ModelAndView connectStreamW(@PathVariable("camId") String camId,
			@ModelAttribute CameraSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		String applicationCode = "";
		String streamName = "";
		String streamServer = "";

//		IResult resultCode = CommonResult.UnknownError;
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		String finalUrl = "";


		condition = new CameraSelectCondition(camId);
		condition.setHasStreamMeta(true);
		Camera camera = camServ.getCamera(condition);
//		try {
//			condition.setCamId(camId);
//			camera = camServ.getCamera(condition);
//			resultCode = CommonResult.Success;
//		} catch (Exception ex) {
//			resultCode = CommonResult.SystemError;
//		}
		
		try {
			String baseUrl = null;

			applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
			streamName = camera.getStreamMetaItems().get(0).getStreamName();
			streamName = streamName.replace(".stream", ""); 
			streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
//			applicationCode = (String) request.getParameter("applicationName");
//			streamName = (String) request.getParameter("streamName");
//			streamServer = (String) request.getParameter("serverName");

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

			switch (applicationCode) {
			case "Dlive":
				applicationCode = "Dlive";
				finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
				break;
			case "live":
				applicationCode = "live";
				finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
				break;
			case "vod":
				applicationCode = "vod";
				finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
				break;
			default:
				applicationCode = "Dlive";
				finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
				break;
			}
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getFullStackTrace(ex));
//			Enumeration params = request.getParameterNames();
//			while (params.hasMoreElements()) {
//				String name = (String) params.nextElement();
//				System.out.print(name + " : " + request.getParameter(name) + "     ");
//			}
			System.out.println();
		} finally {
			mnv.addObject("applicationName", applicationCode);
			mnv.addObject("streamName", streamName);
			mnv.addObject("serverName", streamServer);
			mnv.addObject("finalUrl", finalUrl);
		}

		return mnv;
	}

	@RequestMapping(value = "/disconnectStreamW/{camId}")
	public ModelAndView disconnectStreamW(@PathVariable("camId") String camId,
			@ModelAttribute CameraSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		String applicationCode = "";
		String streamName = "";
		String streamServer = "";

//		IResult resultCode = CommonResult.UnknownError;
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		String finalUrl = "";

		condition = new CameraSelectCondition(camId);
		condition.setHasStreamMeta(true);
		Camera camera = camServ.getCamera(condition);
//		try {
//			condition.setCamId(camId);
//			camera = camServ.getCamera(condition);
//			resultCode = CommonResult.Success;
//		} catch (Exception ex) {
//			resultCode = CommonResult.SystemError;
//		}
		/*
		 * 20211228 connect disconnect 진행중
		 */

		try {
			String baseUrl = null;

			
			applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
			streamName = camera.getStreamMetaItems().get(0).getStreamName();
//			streamName = streamName.replace(".stream", ""); disconnect 는 stream이 들어가야됨
			streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
//			applicationCode = (String) request.getParameter("applicationName");
//			streamName = (String) request.getParameter("streamName");
//			streamServer = (String) request.getParameter("serverName");

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

			switch (applicationCode) {
			case "Dlive":
				applicationCode = "Dlive";
				finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
				break;
			case "live":
				applicationCode = "live";
				finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
				break;
			case "vod":
				applicationCode = "vod";
				finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
				break;
			default:
				applicationCode = "Dlive";
				finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
				break;
			}
			camServ.changeStateCamera(camId, CameraState.DisCon);
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getFullStackTrace(ex));
//			Enumeration params = request.getParameterNames();
//			while (params.hasMoreElements()) {
//				String name = (String) params.nextElement();
//				System.out.print(name + " : " + request.getParameter(name) + "     ");
//			}
			System.out.println();
		} finally {
//			mnv.addObject("resultCode", resultCode);
			mnv.addObject("applicationName", applicationCode);
			mnv.addObject("streamName", streamName);
			mnv.addObject("serverName", streamServer);
			mnv.addObject("finalUrl", finalUrl);
		}

		return mnv;
	}

	@RequestMapping(value = "/deleteCameraW")
	public ModelAndView deleteCameraW(HttpServletRequest request) {
		ModelAndView mnv = new ModelAndView("jsonView");

		String applicationCode = null;
		String streamName = null;
		String streamServer = null;
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		String finalUrl = "";

		/*
		 * 20211224 trycatch문으로 고치는 중
		 */
		try {
			String baseUrl = null;
			String application = null;
			String streamFile = null;

			applicationCode = (String) request.getParameter("applicationName");
			streamName = (String) request.getParameter("streamName");
			streamServer = (String) request.getParameter("serverName");

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + applicationCode);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + streamName);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + streamServer);

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			// 여기서 에러가 남... nullpointerException이래 해결..
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

			switch (applicationCode) {
			case "Dlive":
				applicationCode = "Dlive";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			case "live":
				applicationCode = "live";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			case "vod":
				applicationCode = "vod";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			default:
				applicationCode = "Dlive";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			}
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getFullStackTrace(ex));
			Enumeration params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String name = (String) params.nextElement();
				System.out.print(name + " : " + request.getParameter(name) + "     ");
			}
			System.out.println();
		} finally {
			mnv.addObject("applicationName", applicationCode);
			mnv.addObject("streamName", streamName);
			mnv.addObject("serverName", streamServer);
			mnv.addObject("finalUrl", finalUrl);
		}
		return mnv;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/updateCameraW")
	public ModelAndView updateCameraW(HttpServletRequest request) {
		ModelAndView mnv = new ModelAndView("jsonView");

		String applicationCode = null;
		String streamName = null;
		String streamServer = null;
		String streamSourceUrl = null;
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		String finalUrl = "";

		/*
		 * 20220104 update test 중
		 */
		try {
			String baseUrl = null;
			String application = null;
			String streamFile = null;

			applicationCode = (String) request.getParameter("applicationName");
			streamName = (String) request.getParameter("streamName");
			streamServer = (String) request.getParameter("serverName");
			streamSourceUrl = (String) request.getParameter("streamSourceUrl");

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + applicationCode);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + streamName);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + streamServer);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + streamSourceUrl);

			baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
			// 여기서 에러가 남... nullpointerException이래 해결..
			baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);

			switch (applicationCode) {
			case "Dlive":
				applicationCode = "Dlive";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			case "live":
				applicationCode = "live";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			case "vod":
				applicationCode = "vod";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			default:
				applicationCode = "Dlive";
				finalUrl = wowzaApi.deleteStream(baseUrl, applicationCode, streamName);
				break;
			}
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getFullStackTrace(ex));
			Enumeration params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String name = (String) params.nextElement();
				System.out.print(name + " : " + request.getParameter(name) + "     ");
			}
			System.out.println();
		} finally {
			mnv.addObject("applicationName", applicationCode);
			mnv.addObject("streamName", streamName);
			mnv.addObject("serverName", streamServer);
			mnv.addObject("finalUrl", finalUrl);
		}
		return mnv;
	}
	

	@RequestMapping(value = "/modifyCamera")
	public ModelAndView modifyCamera(@ModelAttribute Camera camera) {
		ModelAndView mnv = new ModelAndView("jsonView");
//		IResult resultCode = camServ.modifyCamera(camera);
		IResult _resultCode = CommonResult.UnknownError;
		EmResult resultCode = null;
		EntityManager em = null;
//		String streamNameBefore = request.getParameter("streamNameBefore");
		String streamName = camera.getStreamMetaItems().get(0).getStreamName();
		String streamNameBefore = camera.getStreamMetaItems().get(0).getStreamNameBefore();
		String applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
		String streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
		String filePath = "Y:\\content\\"+streamNameBefore+".stream";// 파일 형식이 .stream임 ( 텍스트는 .txt )
		File deleteFile = new File(filePath);
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		String ErrorCode = "";
		String finalUrl = "";
		
		try {
			_TRANS:{
				
				resultCode = camServ.modifyCamera(camera);
				_resultCode = resultCode.getResult();
				em = resultCode.getEm();
				
				Code code = em.find(Code.class, streamServer);
				streamServer = code.getName();
				
				code = em.find(Code.class, applicationCode);
				applicationCode = code.getName();
				
				String baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
				baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
				
				switch (applicationCode) {
				case "Dlive":
					applicationCode = "Dlive";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamNameBefore);
					break;
				case "live":
					applicationCode = "live";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamNameBefore);
					break;
				case "vod":
					applicationCode = "vod";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamNameBefore);
					break;
				default:
					applicationCode = "Dlive";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamNameBefore);
					break;
				}
				
				if(_resultCode != CommonResult.Success) {
					break _TRANS;
				}
				
				
				if(deleteFile.exists()) {
					deleteFile.delete();
					_resultCode = CommonResult.Success;
					logger.debug("1111111111111111111111111111o1");
					ErrorCode = "success";
				}else{
					_resultCode = CommonResult.UnknownError;
					logger.debug("1111111111111111111111111111o");
					ErrorCode = "delete error";
				}
				// 여기까지가 삭제
				
				
				// Wowza 등록해야됨...
				
				try {
					applicationCode = null;
					streamName = null;
					String streamSourceUrl = null;
					streamServer = null;
					
					
					baseUrl = null;
					String application = null;
					String streamFile = null;
					
					String result = "";
					
					applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
					streamName = camera.getStreamMetaItems().get(0).getStreamName();
					streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
					streamSourceUrl = camera.getStreamMetaItems().get(0).getStreamSourceUrl();

					code = em.find(Code.class, applicationCode);
					applicationCode = code.getName();

					code = em.find(Code.class, streamServer);
					streamServer = code.getName();
					
					baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
					baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
					
					switch (applicationCode) {
					case "Dlive":
						applicationCode = "Dlive";
						result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
						finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
						break;
					case "live":
						applicationCode = "live";
						result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
						finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
						break;
					case "vod":
						applicationCode = "vod";
						result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
						finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
						break;
					default:
						applicationCode = "Dlive";
						result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
						finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
						break;
					}
					
					Map stopResultMap = new Gson().fromJson(result, Map.class);

					Boolean isSuccess = false;

					isSuccess = (Boolean) stopResultMap.get("success");

					ErrorCode = "add success";
					if (isSuccess != true) {
						_resultCode = CommonResult.UnknownError;
						ErrorCode = "add fail";
						break _TRANS;
					}
				}catch(Exception e) {
					_resultCode = CommonResult.UnknownError;
					ErrorCode = "add fail exception";
				}
				
			}
		}catch(Exception e){
			_resultCode = CommonResult.SystemError;
			ErrorCode = "fail exception";
		}finally {
			if (_resultCode != CommonResult.Success) {
				em.getTransaction().rollback();
				if (_resultCode == CommonResult.UnknownError) {
					// Wowza에 롤백된 값을 등록해야됨...
					
					try {
						applicationCode = null;
						streamName = null;
						String streamSourceUrl = null;
						streamServer = null;

						String baseUrl = null;
						String application = null;
						String streamFile = null;
						
						String result = "";
						
						applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
						streamName = camera.getStreamMetaItems().get(0).getStreamName();
						streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
						streamSourceUrl = camera.getStreamMetaItems().get(0).getStreamSourceUrl();

						Code code = em.find(Code.class, applicationCode);
						applicationCode = code.getName();

						code = em.find(Code.class, streamServer);
						streamServer = code.getName();
						
						baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
						baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
						
						switch (applicationCode) {
						case "Dlive":
							applicationCode = "Dlive";
							result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
							finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
							break;
						case "live":
							applicationCode = "live";
							result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
							finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
							break;
						case "vod":
							applicationCode = "vod";
							result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
							finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
							break;
						default:
							applicationCode = "Dlive";
							result = wowzaApi.addStream(baseUrl, applicationCode, streamName, streamSourceUrl);
							finalUrl = wowzaApi.connectStream(baseUrl, applicationCode, streamName);
							break;
						}
						
						Map stopResultMap = new Gson().fromJson(result, Map.class);

						Boolean isSuccess = false;

						isSuccess = (Boolean) stopResultMap.get("success");

						if (isSuccess != true) {
							_resultCode = CommonResult.SystemError;
							logger.debug("롤백된 값 Wowza에 등록해야되는데 실패함...");
							ErrorCode = "final add fail";
						}
					}catch(Exception e) {
						_resultCode = CommonResult.SystemError;
						logger.debug("롤백된 값 Wowza에 등록해야되는데 실패함...222");
						ErrorCode = "final add fail exception";
					}
				}
			}else {
				em.getTransaction().commit();
			}
			em.close();
		}
		
		logger.debug("카메라 수정 요청 결과 [camera={}] => {}", camera, _resultCode);

		camera = camServ.getCamera(camera.getCamId());

		mnv.addObject("stream", ErrorCode+deleteFile);
		mnv.addObject("camera", camera);
		mnv.addObject("resultCode", _resultCode);
		return mnv;
	}

	@RequestMapping(value = "/removeCamera/{camId}")
	public ModelAndView removeCamera(@PathVariable("camId") String camId) {
		ModelAndView mnv = new ModelAndView("jsonView");
//		IResult resultCode = camServ.deleteCamera(camId);
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		IResult _resultCode = CommonResult.UnknownError;
		EmResult resultCode = null;
		EntityManager em = null;
		CameraSelectCondition condition = new CameraSelectCondition(camId);
		condition.setHasStreamMeta(true);
		Camera camera = camServ.getCamera(condition);
		String applicationCode = camera.getStreamMetaItems().get(0).getApplicationCode();
		String streamServer = camera.getStreamMetaItems().get(0).getStreamServerCode();
		String streamName = camera.getStreamMetaItems().get(0).getStreamName();
		String finalUrl = "";
		
		
		String filePath = "Y:\\content\\"+streamName+".stream";// 파일 형식이 .stream임 ( 텍스트는 .txt )
		File deleteFile = new File(filePath);
		try {
			_TRANS:{
				resultCode = camServ.deleteCamera(camId);
				_resultCode = resultCode.getResult();
				em = resultCode.getEm();
				
				Code code = em.find(Code.class, streamServer);
				streamServer = code.getName();
				
				code = em.find(Code.class, applicationCode);
				applicationCode = code.getName();
				
				String baseUrl = propServ.getProperty("WOWZA_PROPERTIES", "BASE_REST_URL").valueToString();
				baseUrl = baseUrl.replace(MARKUP_STREAM_SERVER, streamServer);
				
				switch (applicationCode) {
				case "Dlive":
					applicationCode = "Dlive";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
					break;
				case "live":
					applicationCode = "live";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
					break;
				case "vod":
					applicationCode = "vod";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
					break;
				default:
					applicationCode = "Dlive";
					finalUrl = wowzaApi.disconnectStream(baseUrl, applicationCode, streamName);
					break;
				}
				
				
				if(_resultCode != CommonResult.Success) {
					break _TRANS;
				}
				
				
				
				if(deleteFile.exists()) {
					deleteFile.delete();
					_resultCode = CommonResult.Success;
					logger.debug("1111111111111111111111111111o1");
				}else{
					_resultCode = CommonResult.SystemError;
					logger.debug("1111111111111111111111111111o");
				}
			}
		}catch(Exception e){
			_resultCode = CommonResult.SystemError;
		}finally {
			if (_resultCode != CommonResult.Success) {
				em.getTransaction().rollback();
			}else {
				em.getTransaction().commit();
			}
			em.close();
		}

		logger.debug("카메라 삭제 요청 결과 [camId={}] => {}", camId, _resultCode);

		mnv.addObject("finalUrl", "/"+ applicationCode +"/"+ streamName);
		mnv.addObject("camId", camId);
		mnv.addObject("resultCode", _resultCode);
		return mnv;
	}

	@Autowired
	private PropertyService propServ;

	// recording
	@ResponseBody
	@RequestMapping("/record/{camId}")
	public ModelAndView record(@PathVariable("camId") String camId,
			@RequestParam(name = "recordUserId") String recordUserId,
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
			chkData = "baseUrl : " + baseUrl + " application : " + application.toString() + " streamFile : "
					+ streamFile + " outputPath : " + outputPath + " outputFile : " + outputFile;
			if (EmptyChecker.isEmpty(baseUrl) || EmptyChecker.isEmpty(application) || EmptyChecker.isEmpty(streamFile)
					|| EmptyChecker.isEmpty(outputPath) || EmptyChecker.isEmpty(outputFile)) {
				logger.error(
						"record ===> 인자가 잘못 되었습니다. [baseUrl={}, application={}, streamFile={}, outputPath={}, outputFile={}]", //
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
	public ModelAndView recordStop(@PathVariable("camId") String camId,
			@RequestParam(value = "isCoercion", defaultValue = "false") Boolean isCoercion) {
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

			if (EmptyChecker.isEmpty(baseUrl) || EmptyChecker.isEmpty(application)
					|| EmptyChecker.isEmpty(streamFile)) {
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

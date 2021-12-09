package com.bluecapsystem.cms.jincheon.sportstown.views.record;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Property;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.PropertyService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraType;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.service.CameraManageService;

@Controller
@RequestMapping("/record")
public class RecordViewController {

	@Autowired
	private CodeService codeServ;

	@Autowired
	private CameraManageService cameraServ;

	@Autowired
	private PropertyService propServ;

	@RequestMapping("/manage")
	public ModelAndView manage(HttpSession session) {
		ModelAndView mnv = new ModelAndView("/record/recordManage");

		try {
			mnv.addObject("sprotsEvents", codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null)));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mnv;
	}

	@RequestMapping("/player/{camId}")
	public ModelAndView player(HttpSession session, @PathVariable("camId") String camId) {
		ModelAndView mnv = new ModelAndView("/record/player");
		try {

			User user = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

			Camera camera = cameraServ.getCamera(camId);

			Property liveStreamer = propServ.getProperty("WOWZA_PROPERTIES", "BASE_LIVE_URL");
			mnv.addObject("liveStreamer", liveStreamer.valueToString());
			
			Property dvrStreamer = propServ.getProperty("WOWZA_PROPERTIES", "LIVE_DVR_URL");
			mnv.addObject("dvrStreamer", dvrStreamer.valueToString());
			
			
			System.out.println("livestread : " + liveStreamer + "dvrstreamer : " + dvrStreamer);
			System.out.println("camera : " + camera);
			mnv.addObject("camera", camera);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mnv;
	}

	@RequestMapping("/cameraList/{sportsEventCode}")
	public ModelAndView cameraList(@PathVariable("sportsEventCode") String sportsEventCode) {
		ModelAndView mnv = new ModelAndView("/record/cameraList");
		
		try {
			CameraSelectCondition camCondition = new CameraSelectCondition();
			camCondition.setSportsEventCode(sportsEventCode);
			List<Camera> staticCameras = cameraServ.getCameras(camCondition);
			mnv.addObject("staticCameras", staticCameras);

			System.out.println("/////////////////static camera : " + staticCameras);
			camCondition = new CameraSelectCondition();
			camCondition.setCameraType(CameraType.Shift);

			List<Camera> shiftCameras = Collections.emptyList();
			if (staticCameras.size() > 0 && EmptyChecker.isNotEmpty(staticCameras.get(0).getLocationCode())) {
				camCondition.setLocationCode(staticCameras.get(0).getLocationCode());
				shiftCameras = cameraServ.getCameras(camCondition);
			}
			mnv.addObject("shiftCameras", shiftCameras);
			System.out.println("shift camera : " + shiftCameras);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mnv;
	}

}

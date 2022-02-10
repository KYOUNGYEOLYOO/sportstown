package com.bluecapsystem.cms.jincheon.sportstown.views.content;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.codehaus.plexus.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.DateUtil;
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.ContentInstance;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.ContentService;
import com.bluecapsystem.cms.core.service.PropertyService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.UserType;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@Controller
@RequestMapping("/content")
public class ContentController {
	private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

	@Autowired
	private ContentService contentServ;

	@Autowired
	private CodeService codeServ;

	@Autowired
	private UserManageService userServ;

	@Autowired
	private PropertyService propServ;

	@Autowired
	private ThumbnailInstanceService thumbnailServ;

	@RequestMapping("/search")
	public ModelAndView search() {
		ModelAndView mnv = new ModelAndView("/content/contentSearch");
		return mnv;
	}

	@RequestMapping("/manage")
	public ModelAndView manage() {
		ModelAndView mnv = new ModelAndView("/content/contentManage");
		mnv.addObject("fromDate", DateUtil.addDate(new Date(), -10));
		loadCodes(mnv);

		return mnv;
	}


	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mnv = new ModelAndView("/content/contentRegist");
		loadCodes(mnv);

		return mnv;
	}


	@RequestMapping("/registFile")
	public ModelAndView registFile() {
		ModelAndView mnv = new ModelAndView("/content/contentFileRegist");
		loadCodes(mnv);
		return mnv;
	}

	@RequestMapping("/modify")
	public ModelAndView modify() {
		ModelAndView mnv = new ModelAndView("/content/contentModify");
		return mnv;
	}

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mnv = new ModelAndView("/content/contentList");
		return mnv;
	}

	@RequestMapping("/detail/{contentId}")
	public ModelAndView detail(final HttpSession session, @PathVariable("contentId") final String contentId) {
		ModelAndView mnv = new ModelAndView("/content/contentDetail");
		// ModelAndView mnv = new ModelAndView("/content/contentModify");
		try {
			SportstownContentMeta meta = (SportstownContentMeta) contentServ.getContent(contentId);

			User loginUser = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

			if( meta.getRecordUserId().equals(loginUser.getUserId()) //
				|| (meta.getSportsEventCode().equals(loginUser.getSportsEventCode()) && loginUser.getUserType() == UserType.Coach)
					|| loginUser.getIsDeveloper() || loginUser.getIsAdmin() || loginUser.getUserType() == UserType.Admin) {
				mnv.setViewName("/content/contentModify");
			}
			mnv.addObject("contentMeta", meta);

			loadCodes(mnv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mnv;
	}
	
	@RequestMapping("/detail/{contentId}"+"/video")
	public ModelAndView detail_video(final HttpSession session, @PathVariable("contentId") final String contentId) {
		ModelAndView mnv = new ModelAndView("/content/contentDetails");
		String resultCode = "false";
		try {
			SportstownContentMeta meta = (SportstownContentMeta) contentServ.getContent(contentId);

			User loginUser = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

//			if( meta.getRecordUserId().equals(loginUser.getUserId()) //
//				|| (meta.getSportsEventCode().equals(loginUser.getSportsEventCode()) && loginUser.getUserType() == UserType.Coach)
//					|| loginUser.getIsDeveloper() || loginUser.getIsAdmin() || loginUser.getUserType() == UserType.Admin) {
//				mnv.setViewName("/content/contentModify");
//			}
			resultCode = "Success";
			mnv.addObject("contentMeta", meta);
			mnv.addObject("resultCode", resultCode);

			loadCodes(mnv);
			logger.debug("111111111111111" + resultCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mnv;
	}



	private boolean loadCodes(final ModelAndView mnv) {

		try {
			String vodStreamer = propServ.getProperty("WOWZA_PROPERTIES", "BASE_VOD_URL").valueToString();
			mnv.addObject("vodStreamer", vodStreamer);

			String contentRootUri = propServ.getProperty("FILE_URL_ROOT", "CONTENT").valueToString();
			mnv.addObject("contentRootUri", contentRootUri);

			String ingestRootUri = propServ.getProperty("FILE_URL_ROOT", "UPLOAD").valueToString();
			mnv.addObject("uploadRootUri", ingestRootUri);

			UserSelectCondition userCondition = new UserSelectCondition();
			userCondition.setHasDeleted(false);
			List<User> users = userServ.getUsers(userCondition);
			List<Code> sprotsEvents = codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null));

			mnv.addObject("users", users);
			mnv.addObject("sprotsEvents", sprotsEvents);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	@ResponseBody
	@RequestMapping(value = "/thumbnail/{contentId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] thumbnail(@PathVariable("contentId") final String contentId) {
		byte[] image = null;
		ContentMeta meta = contentServ.getContent(contentId);

		List<ContentInstance> instances = meta.getContent().getInstances();

		if (instances == null || instances.size() == 0) {
			return null;
		}

		try {
			String thumbnailId = instances.get(0).getFile().getThumbnailId();
			ThumbnailInstance thumbnail = thumbnailServ.getThumbnail(thumbnailId);
			image = thumbnail.getImage();
		} catch (Exception ex) {
			logger.error("thumbnail 조회중 오휴 발생 [contentId={}]\n{}", contentId, ExceptionUtils.getFullStackTrace(ex));
			image = null;
		}

		return image;
	}

}

package com.bluecapsystem.cms.jincheon.sportstown.json.contentAuth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.ContentAuthSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentAuth;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.ConnectLocation;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.service.ContentAuthManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@RestController
@RequestMapping("/service/contentAuth")
public class ContentAuthJsonController {

	private static final Logger logger = LoggerFactory.getLogger(ContentAuthJsonController.class);

	@Autowired
	private ContentAuthManageService contentAuthServ;

	@CrossOrigin
	@RequestMapping(value = "/getContentAuths")
	public ModelAndView getContentAuths(HttpServletRequest request, @ModelAttribute("condition") ContentAuthSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		Paging paging = JqGridParameterParser.getPaging(request);
		condition.setPaging(paging);

		IResult resultCode = CommonResult.UnknownError;
		List<ContentAuth> contentAuths = null;
		try {
			contentAuths = contentAuthServ.getContentAuths(condition);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("contentAuths", contentAuths);
			if (condition.getPaging() != null && condition.getPaging().getEnablePaging()) {
				JqGridParameterParser.setPaging(mnv, condition.getPaging());
			}
		}

		return mnv;
	}

	

	@CrossOrigin
	@RequestMapping(value = "/getContentAuth/{contentId}/{userId}")
	public ModelAndView getContentAuth(@PathVariable("contentId") String contentId,
			@PathVariable("userId") String userId) {
		ModelAndView mnv = new ModelAndView("jsonView");

		IResult resultCode = CommonResult.UnknownError;
		ContentAuth contentAuth = null;
		try {
			contentAuth = contentAuthServ.getContentAuth(contentId,userId,"");
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("contentAuth", contentAuth);
		}

		return mnv;
	}

	// @CrossOrigin
	@RequestMapping(value = "/registContentAuth")
	public ModelAndView registContentAuth(@ModelAttribute() ContentAuth contentAuth) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = contentAuthServ.registContentAuth(contentAuth);

		try {
			contentAuth = contentAuthServ.getContentAuth(new ContentAuthSelectCondition(contentAuth.getContentId(), contentAuth.getUserId(),""));
		} catch (Exception ex) {
			logger.error("승인 정보 가져오기 실패 [contentAuthId={}] \n{}", contentAuth.getContentAuthId(), ExceptionUtils.getFullStackTrace(ex));
		}

		logger.debug("승인 등록 요청 결과 [contentAuth={}] => {}", contentAuth, resultCode);

		mnv.addObject("contentAuth", contentAuth);
		mnv.addObject("resultCode", resultCode);

		return mnv;
	}

	@RequestMapping(value = "/returnContentAuth/{contentId}/{userId}")
	public ModelAndView returnContentAuth(@PathVariable("contentId") String contentId,
			@PathVariable("userId") String userId) {
		ModelAndView mnv = new ModelAndView("jsonView");

		

		IResult resultCode = contentAuthServ.modifyReturnContentAuth(contentId,userId);

		logger.debug("승인 수정 요청 결과 [contentAuth={}] => {}", contentId, userId, resultCode);
		ContentAuth contentAuth = null;
		try {
			contentAuth = contentAuthServ.getContentAuth(new ContentAuthSelectCondition( contentId, userId,""));
		} catch (Exception ex) {
			logger.error("승인 정보 가져오기 실패 [contentAuthId={}] \n{}", contentAuth.getContentAuthId(), ExceptionUtils.getFullStackTrace(ex));
		}

		mnv.addObject("contentAuth", contentAuth);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}
	
	@RequestMapping(value = "/approvalContentAuth/{contentId}/{userId}")
	public ModelAndView approvalContentAuth(@PathVariable("contentId") String contentId,
			@PathVariable("userId") String userId) {
		ModelAndView mnv = new ModelAndView("jsonView");

		

		IResult resultCode = contentAuthServ.modifyApprovalContentAuth(contentId,userId);

		logger.debug("승인 수정 요청 결과 [contentAuth={}] => {}", contentId, userId, resultCode);
		ContentAuth contentAuth = null;
		try {
			contentAuth = contentAuthServ.getContentAuth(new ContentAuthSelectCondition( contentId, userId,""));
		} catch (Exception ex) {
			logger.error("승인 정보 가져오기 실패 [contentAuthId={}] \n{}", contentAuth.getContentAuthId(), ExceptionUtils.getFullStackTrace(ex));
		}

		mnv.addObject("contentAuth", contentAuth);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	
}

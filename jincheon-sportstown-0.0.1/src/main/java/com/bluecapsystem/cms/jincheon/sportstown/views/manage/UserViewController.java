package com.bluecapsystem.cms.jincheon.sportstown.views.manage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;


@Controller
@RequestMapping("/user")
public class UserViewController
{
	private static final Logger logger = LoggerFactory.getLogger(UserViewController.class);

	@Autowired
	private UserManageService userServ;


	@Autowired
	private CodeService codeServ;


	@RequestMapping("/manage")
	public ModelAndView manage()
	{
		ModelAndView mnv = new ModelAndView("/user/userManage");
		return mnv;
	}


	@RequestMapping("/regist")
	public ModelAndView regist()
	{
		ModelAndView mnv = new ModelAndView("/user/userRegist");

		loadCodes(mnv);

		return mnv;
	}
	
	@RequestMapping("/passwordModify")
	public ModelAndView passwordModify(final HttpSession session)
	{
		ModelAndView mnv = new ModelAndView("/user/userPwdModify");

//		loadCodes(mnv);
		User user = null;
		try {
			User loginUser = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

			user = userServ.getUser(loginUser.getUserId(), null);
			mnv.addObject("user", user);
			
//			loadCodes(mnv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mnv;
	} 
	
	@RequestMapping("/mainModify")
	public ModelAndView mainModify(final HttpSession session)
	{
		ModelAndView mnv = new ModelAndView("/user/userMainModify");

//		loadCodes(mnv);
		User user = null;
		try {
			User loginUser = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

			user = userServ.getUser(loginUser.getUserId(), null);
			mnv.addObject("user", user);
			
//			loadCodes(mnv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mnv;
	} 


	@RequestMapping("/select")
	public ModelAndView select(
			@ModelAttribute UserSelectCondition condition)
	{
		ModelAndView mnv = new ModelAndView("/user/userSelect");

		try
		{
			List<User> users = userServ.getUsers(condition);
			mnv.addObject("users", users);
		}catch(Exception ex)
		{
			logger.error("사용자 목록 조회중 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
		}


		return mnv;
	}

	@RequestMapping("/modify/{userId}")
	public ModelAndView modify(
			@PathVariable("userId") String userId)
	{
		ModelAndView mnv = new ModelAndView("/user/userModify");


		loadCodes(mnv);

		try
		{
			User user = userServ.getUser(userId, null);
			mnv.addObject("user", user);
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

		ModelAndView mnv = new ModelAndView("/user/userList");

		mnv.addObject("listId", listId);
		mnv.addObject("pagerId", pagerId);

		return mnv;
	}





	private Boolean loadCodes(ModelAndView mnv)
	{
		Boolean isSus = false;

		try
		{
			/**
			 * 스포츠 종목
			 */
			mnv.addObject("sprotsEvents", codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null)));
		}catch(Exception ex)
		{
			logger.error("사용자 관리 코드 목록 조회중 오류 발생 \n{}",
					ExceptionUtils.getFullStackTrace(ex));

		}

		return isSus;
	}

}

package com.bluecapsystem.cms.jincheon.sportstown.json.user;

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
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.ConnectLocation;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@RestController
@RequestMapping("/service/user")
public class UserJsonController {

	private static final Logger logger = LoggerFactory.getLogger(UserJsonController.class);

	@Autowired
	private UserManageService userServ;

	@CrossOrigin
	@RequestMapping(value = "/getUsers")
	public ModelAndView getUsers(HttpServletRequest request, @ModelAttribute("condition") UserSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		Paging paging = JqGridParameterParser.getPaging(request);
		condition.setPaging(paging);

		IResult resultCode = CommonResult.UnknownError;
		List<User> users = null;
		try {
			users = userServ.getUsers(condition);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("users", users);
			if (condition.getPaging() != null && condition.getPaging().getEnablePaging()) {
				JqGridParameterParser.setPaging(mnv, condition.getPaging());
			}
		}

		return mnv;
	}

	@CrossOrigin
	@RequestMapping(value = "/getUserFromLoginId/{loginId}")
	public ModelAndView getUserFromLoginId(@PathVariable("loginId") String loginId) {
		ModelAndView mnv = new ModelAndView("jsonView");

		IResult resultCode = CommonResult.UnknownError;
		User user = null;
		try {
			user = userServ.getUser(null, loginId);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("user", user);
		}

		return mnv;
	}

	@CrossOrigin
	@RequestMapping(value = "/getUser/{userId}")
	public ModelAndView getUser(@PathVariable("userId") String userId) {
		ModelAndView mnv = new ModelAndView("jsonView");

		IResult resultCode = CommonResult.UnknownError;
		User user = null;
		try {
			user = userServ.getUser(userId, null);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("user", user);
		}

		return mnv;
	}

	// @CrossOrigin
	@RequestMapping(value = "/registUser")
	public ModelAndView registUser(@ModelAttribute() User user) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = userServ.registUser(user);

		try {
			user = userServ.getUser(new UserSelectCondition(user.getUserId(), null));
		} catch (Exception ex) {
			logger.error("사용자 정보 가져오기 실패 [userId={}] \n{}", user.getUserId(), ExceptionUtils.getFullStackTrace(ex));
		}

		logger.debug("사용자 등록 요청 결과 [user={}] => {}", user, resultCode);

		mnv.addObject("user", user);
		mnv.addObject("resultCode", resultCode);

		return mnv;
	}

	@RequestMapping(value = "/modifyUser")
	public ModelAndView modifyUser(@ModelAttribute User user, @RequestParam(value = "newPassword") String newPassword) {
		ModelAndView mnv = new ModelAndView("jsonView");

		if (EmptyChecker.isNotEmpty(newPassword)) {
			user.setPassword(newPassword);
		}

		IResult resultCode = userServ.modifyUser(user);

		logger.debug("사용자 수정 요청 결과 [user={}] => {}", user, resultCode);

		try {
			user = userServ.getUser(new UserSelectCondition(user.getUserId(), null));
		} catch (Exception ex) {
			logger.error("사용자 정보 가져오기 실패 [userId={}] \n{}", user.getUserId(), ExceptionUtils.getFullStackTrace(ex));
		}

		mnv.addObject("user", user);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}
	
	@RequestMapping(value = "/modifyUserPassword/{userId}/{password}")
	public ModelAndView modifyUserPassword(@PathVariable("userId") String userId, @PathVariable("password") String password) {
		ModelAndView mnv = new ModelAndView("jsonView");

		
		IResult resultCode = userServ.modifyUserPassword(userId, password);

		logger.debug("사용자 password 수정 요청 결과 [userId={}] => {}", userId, resultCode);

		User user = null;
		try {
			user = userServ.getUser(new UserSelectCondition(userId, null));
		} catch (Exception ex) {
			logger.error("사용자 정보 가져오기 실패 [userId={}] \n{}", user.getUserId(), ExceptionUtils.getFullStackTrace(ex));
		}

		mnv.addObject("user", user);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@RequestMapping(value = "/removeUser/{userId}")
	public ModelAndView removeUser(@PathVariable("userId") String userId) {
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = userServ.deleteUser(userId);

		logger.debug("사용자 삭제 요청 결과 [userId={}] => {}", userId, resultCode);

		mnv.addObject("userId", userId);
		mnv.addObject("resultCode", resultCode);
		return mnv;
	}

	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request, HttpSession session, @RequestParam(name = "loginId") String loginId,
			@RequestParam(name = "password") String password) {

		ModelAndView mnv = new ModelAndView("jsonView");

		UserSelectCondition condition = new UserSelectCondition();
		condition.setHasDeleted(false);
		condition.setHasNotUsed(false);
		condition.setLoginId(loginId);
		IResult resultCode = CommonResult.UnknownError;
		try {
			_TRANSACTION: {
				User user = userServ.getUser(condition);
				if (user == null) {
					resultCode = UserResult.UserNotFound;
					break _TRANSACTION;
				}
				
				
				// 비밀번호 변경주기 체크
				
				
				
				
				

				System.out.println("user : " + user);
				if (user.getPassword().equals(password) == false) {
					resultCode = UserResult.WorngPassword;
					break _TRANSACTION;
				}

				// 내부 / 외부 접속인지를 판단 한다
				ConnectLocation cl = IPFilterConstant.checkConnectLocation(request.getRequestURL().toString());
				user.setConnectLocation(cl);

				// session 에 login 정보를 입력 한다
				session.setAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY, user);
				// session timeout 24h
				session.setMaxInactiveInterval(60*60*24);
				
			
				
				resultCode = CommonResult.Success;
				System.out.println("result code : " + resultCode);
				System.out.println("session data : " + user);
				mnv.addObject("user", user);
			}
		} catch (Exception ex) {
			System.out.println("login error : " + ex);
			resultCode = CommonResult.SystemError;
			ex.printStackTrace();
		}

		mnv.addObject("resultCode", resultCode);
		

		return mnv;

	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mnv = new ModelAndView("jsonView");
		session.removeAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);
		mnv.addObject("resultCode", CommonResult.Success);
		return mnv;
	}
}

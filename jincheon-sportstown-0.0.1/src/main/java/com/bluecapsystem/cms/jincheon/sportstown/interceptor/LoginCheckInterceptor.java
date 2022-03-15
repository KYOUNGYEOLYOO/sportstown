package com.bluecapsystem.cms.jincheon.sportstown.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;
import com.bluecapsystem.cms.jincheon.sportstown.service.DashboardDataManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.LoginDataManageService;



public class LoginCheckInterceptor implements HandlerInterceptor 
{
	
	private final Logger logger = LoggerFactory.getLogger(LoginCheckInterceptor.class);
	
	@Autowired
	private LoginDataManageService loginDataManageServ;
	
	
	public LoginCheckInterceptor()
	{
		
		
		
		
		
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		HttpSession session = request.getSession();
		String userIp = request.getRemoteAddr();
		User user = (User) session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);

		if(user == null)
		{
			logger.info("로그인 정보 없음 [ip={}]", userIp);
			String reqUrl = request.getRequestURL().toString();
			int stxParam = reqUrl.indexOf("?");
			
			if(stxParam > 0)
			{
				reqUrl = reqUrl.substring(0, stxParam);
			}
			
			if(reqUrl.indexOf("/service/") != -1)
			{
				ModelAndView mnv = new ModelAndView("jsonView");
				mnv.addObject("resultCode", UserResult.NoLoginUser);
				throw new ModelAndViewDefiningException(mnv);
			}else
			{
				reqUrl = URLEncoder.encode(reqUrl, "UTF-8");
				
				System.out.println("request.getContextPath() : " + request.getContextPath());
				response.sendRedirect(request.getContextPath() + "/login");
			}
		}
		
		if(user != null) {
			
			LoginData loginData = new LoginData();
			loginData.setUserId(user.getUserId());
			
			loginDataManageServ.registLoginData(loginData);
		}
		
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		HttpSession session = request.getSession();
		
		if(modelAndView == null) return;
		
		
		User loginUser = (User)session.getAttribute(UserSessionDefine.LOGIN_USER_SESSION_KEY);
		
		if(loginUser == null)
		{
			return;
		}else
		{
			modelAndView.addObject("loginUser", loginUser);
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
	}
	
}

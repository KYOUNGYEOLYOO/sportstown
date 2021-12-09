package com.bluecapsystem.cms.jincheon.sportstown.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.DateUtil;



public class CommonValuesInterceptor implements HandlerInterceptor 
{
	
	
	public CommonValuesInterceptor()
	{
		
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		
		if(modelAndView != null)
			modelAndView.addObject("currentDate", DateUtil.getCurrentDate());
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
	}
	
}

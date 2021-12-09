package com.bluecapsystem.cms.jincheon.sportstown.views.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/include")
public class IncludeController 
{
	@RequestMapping("/head")
	public ModelAndView head()
	{
		return new ModelAndView("/include/head");
	}
	
	@RequestMapping("/footer")
	public ModelAndView footer()
	{
		return new ModelAndView("/include/footer");
	}
	
	
	@RequestMapping("/top")
	public ModelAndView top(
			@RequestParam(name="mainMenu", defaultValue="main") String mainMenu,
			@RequestParam(name="subMenu", defaultValue="main") String subMenu)
	{
		ModelAndView mnv = new ModelAndView("/include/top");
		
		mnv.addObject("mainMenu", mainMenu);
		mnv.addObject("subMenu", subMenu);
		
		return mnv;
	}
}

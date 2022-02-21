package com.bluecapsystem.cms.jincheon.sportstown.views.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@RequestMapping("/adminGraph1")
	public ModelAndView adminGraph1()
	{
		return new ModelAndView("/admin/adminGraph1");
	}
	
	@RequestMapping("/adminGraph2")
	public ModelAndView footer()
	{
		return new ModelAndView("/admin/adminGraph2");
	}
	
}

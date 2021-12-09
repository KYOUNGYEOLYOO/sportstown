package com.bluecapsystem.cms.jincheon.sportstown.views.index;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexViewController {

	@RequestMapping("")
	public ModelAndView index(HttpServletRequest request) {

		return new ModelAndView("/index/index");
	}
}

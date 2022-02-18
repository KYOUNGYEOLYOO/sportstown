package com.bluecapsystem.cms.jincheon.sportstown.views.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/canvas")
public class CanvasController 
{
	@RequestMapping("/canvasPop")
	public ModelAndView adminGraph1()
	{
		return new ModelAndView("/canvas/canvas");
	}

}

package com.bluecapsystem.cms.jincheon.sportstown.views.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/login")
public class LoginViewController {

	@RequestMapping("")
	public ModelAndView login()
	{
		ModelAndView mnv = new ModelAndView("/login/login");

		return mnv;
	}

}

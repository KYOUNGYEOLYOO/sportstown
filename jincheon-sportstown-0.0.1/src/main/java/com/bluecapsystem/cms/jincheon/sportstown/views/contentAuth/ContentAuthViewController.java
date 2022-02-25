package com.bluecapsystem.cms.jincheon.sportstown.views.contentAuth;

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
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentAuth;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.service.ContentAuthManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;


@Controller
@RequestMapping("/contentAuth")
public class ContentAuthViewController
{
	private static final Logger logger = LoggerFactory.getLogger(ContentAuthViewController.class);

	@Autowired
	private ContentAuthManageService contentAuthServ;




	@RequestMapping("/manage")
	public ModelAndView manage()
	{
		ModelAndView mnv = new ModelAndView("/contentAuth/authManage");
		return mnv;
	}


	@RequestMapping("/reqAuth/{contentId}/{userId}")
	public ModelAndView reqAuth(
			@PathVariable("contentId") String contentId, @PathVariable("userId") String userId)
	{
		ModelAndView mnv = new ModelAndView("/contentAuth/authRegist");

		mnv.addObject("contentId", contentId);
		mnv.addObject("userId", userId);

		return mnv;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(
			@RequestParam(name="listId", defaultValue="") String listId,
			@RequestParam(name="pagerId", defaultValue="") String pagerId)
	{

		ModelAndView mnv = new ModelAndView("/contentAuth/authList");

		mnv.addObject("listId", listId);
		mnv.addObject("pagerId", pagerId);

		return mnv;
	}
	
	@RequestMapping("/modify/{contentId}/{userId}")
	public ModelAndView modify(@PathVariable("contentId") String contentId,
			@PathVariable("userId") String userId)
	{
		ModelAndView mnv = new ModelAndView("/contentAuth/authModify");


		try
		{
			ContentAuth contentAuth = contentAuthServ.getContentAuth(contentId, userId, "");
			mnv.addObject("uscontentAuther", contentAuth);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}


		return mnv;
	}

}

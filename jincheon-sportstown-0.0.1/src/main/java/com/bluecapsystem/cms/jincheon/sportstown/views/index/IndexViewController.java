package com.bluecapsystem.cms.jincheon.sportstown.views.index;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDaoImpl;
import com.bluecapsystem.cms.jincheon.sportstown.dao.LoginDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.views.manage.UserViewController;

@Controller
public class IndexViewController {

	
	
	
	private static final Logger logger = LoggerFactory.getLogger(IndexViewController.class);
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {

		
		
		ModelAndView mnv = new ModelAndView("/index/index");
				
		
//		String test="20191023 국방뉴스 육군기계화학교, 기갑/기계화부대 전투발전 세미나";
//		
//		test.replace("/", "^").replaceAll("\\\\", "^");
//		
//		System.out.println(">>>>>>>"+test);
		return mnv;
		
	}
	
	
}

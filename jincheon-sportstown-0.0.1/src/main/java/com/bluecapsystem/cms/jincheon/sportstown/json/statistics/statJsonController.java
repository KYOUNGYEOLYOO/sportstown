package com.bluecapsystem.cms.jincheon.sportstown.json.statistics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.UserSessionDefine;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.LoginDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.ContentAuthSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentAuth;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData.DataType;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.StorageInfo;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.ConnectLocation;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;
import com.bluecapsystem.cms.jincheon.sportstown.service.ContentAuthManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.StorageInfoManageService;
import com.bluecapsystem.cms.jincheon.sportstown.service.UserManageService;

@RestController
@RequestMapping("/service/statistics")
public class statJsonController {

	private static final Logger logger = LoggerFactory.getLogger(statJsonController.class);

	@Autowired
	private LoginDataDao loginDataDao;
	
	@Autowired
	private DashboardDataDao dashboardDataDao;
	
	
	
	@Autowired
	private CodeService codeServ;
	
	@Autowired
	private StorageInfoManageService simServ;
	
	
	
	@RequestMapping("/sportsEventCode/{year}/{month}")
	public ModelAndView sportsEventCode(HttpServletRequest request,
			@PathVariable("year") String year,@PathVariable("month") String month) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		try {
			
			dashboardDatas = dashboardDataDao.statSportsCode(year+""+month);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/typeCount/{year}/{month}")
	public ModelAndView typeCount(HttpServletRequest request,
			@PathVariable("year") String year,@PathVariable("month") String month) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		try {
			
			dashboardDatas = dashboardDataDao.typeCount(year+""+month);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
}

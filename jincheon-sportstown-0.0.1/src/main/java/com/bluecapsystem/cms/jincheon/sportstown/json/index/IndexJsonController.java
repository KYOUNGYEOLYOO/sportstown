package com.bluecapsystem.cms.jincheon.sportstown.json.index;

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
@RequestMapping("/service/index")
public class IndexJsonController {

	private static final Logger logger = LoggerFactory.getLogger(IndexJsonController.class);

	@Autowired
	private LoginDataDao loginDataDao;
	
	@Autowired
	private DashboardDataDao dashboardDataDao;
	
	
	
	@Autowired
	private CodeService codeServ;
	
	@Autowired
	private StorageInfoManageService simServ;
	
	@RequestMapping("/selectCode")
	public ModelAndView selectCode(HttpServletRequest request) {

		
		ModelAndView mnv = new ModelAndView("jsonView");
		
		IResult resultCode = CommonResult.UnknownError;
		List<Code> codes = null; 
		
		try {
			
			codes  = codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null));
			
			mnv.addObject("codes", codes);
			
			
			
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("codes", codes);
			
		}
		
		return mnv;
		
	}
	
	@RequestMapping("/selectBestCode/{year}")
	public ModelAndView selectBestCode(HttpServletRequest request,
			@PathVariable("year") String year) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		//DashboardData dashboardData = null;
		List<DashboardData> dashboardData  = null;
		try {
			
			dashboardData = dashboardDataDao.findBestCodeData(year, DataType.Contents);
			
			
			
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardData", dashboardData);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/columnChartData/{sportsEventCode}/{year}")
	public ModelAndView columnchartData(HttpServletRequest request,
			@PathVariable("sportsEventCode") String sportsEventCode,
			@PathVariable("year") String year) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		
		try {
			
			dashboardDatas = dashboardDataDao.findGroupByColumnData(sportsEventCode,year, DataType.Contents);
			
			
			
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/monthData/{year}")
	public ModelAndView monthData(HttpServletRequest request,
			@PathVariable("year") String year) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		try {
			
			dashboardDatas = dashboardDataDao.findGroupByMonthData(year, DataType.Contents);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/drawChartData")
	public ModelAndView drawChartData(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		try {
			
			dashboardDatas = dashboardDataDao.findGroupByData(DataType.Contents);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/contentCnt")
	public ModelAndView contentCnt(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> allContent = null;
		List<DashboardData> todayContent = null;
		try {
			
			Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
			// 년월일시분초 14자리 포멧
			SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMdd"); 
			
			Calendar cal = Calendar.getInstance();
			 
			cal.setTime(date_now);
			
			String today = fourteen_format.format(cal.getTime());
			
			allContent = dashboardDataDao.findGroupByAllContent(DataType.Contents);
			todayContent = dashboardDataDao.findGroupByTodayContent(today, DataType.Contents);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("allContent", allContent);
			mnv.addObject("todayContent", todayContent);
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/loginCnt")
	public ModelAndView loginCnt(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<LoginData> loginDatas = null;
		try {

			Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
			// 년월일시분초 14자리 포멧
			SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			
			// Java 시간 더하기
			 
			Calendar cal = Calendar.getInstance();
			 
			cal.setTime(date_now);
			cal.add(Calendar.MINUTE, -5);
			
			String today = fourteen_format.format(cal.getTime());
			
			loginDatas = loginDataDao.findGroupByLoginData(today);


			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("loginDatas", loginDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/diskInfo")
	public ModelAndView diskInfo(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		IResult resultCode = CommonResult.UnknownError;
		
		List<StorageInfo> storageInfos = null;
		try {
			storageInfos = simServ.getStorageInfos();
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("storageInfos", storageInfos);
			
		}
		
		resultCode = CommonResult.Success;
		mnv.addObject("resultCode", resultCode);
		
		return mnv;
		
	}
}

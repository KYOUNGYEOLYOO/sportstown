package com.bluecapsystem.cms.jincheon.sportstown.views.index;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDaoImpl;
import com.bluecapsystem.cms.jincheon.sportstown.dao.LoginDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;

@Controller
public class IndexViewController {

	
	@Autowired
	private LoginDataDao loginDataDao;
	
	@Autowired
	private DashboardDataDao dashboardDataDao;
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {

		
		
		ModelAndView mnv = new ModelAndView("/index/index");
		
//		List<LoginData> loginDatas = loginDataDao.findGroupByData();
		
		List<DashboardData> dashboardDatas = dashboardDataDao.findGroupByData(DashboardData.DataType.Contents);
		
//		System.out.println(">>>Content>>>>"+loginDatas.size());
//		for(int i=0; i < dashboardDatas.size(); i++) {
//			System.out.println(">>>Content>>>>"+dashboardDatas.get(i).getDataType());
//		}
//		System.out.println(">>>로그인수>>>>"+loginDatas.size());
		
		mnv.addObject("dashboardDatas", dashboardDatas);
		
		return mnv;
		
	}
	
	@RequestMapping("/columnChartData")
	public ModelAndView columnchartData(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<DashboardData> dashboardDatas = null;
		try {
			
			dashboardDatas = dashboardDataDao.findGroupByData(DashboardData.DataType.Contents);
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
			
			dashboardDatas = dashboardDataDao.findGroupByData(DashboardData.DataType.Archive);
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("dashboardDatas", dashboardDatas);
			
		}

		
		
		return mnv;
		
	}
	
	@RequestMapping("/loginCnt")
	public ModelAndView loginCnt(HttpServletRequest request) {

		ModelAndView mnv = new ModelAndView("jsonView");
		
		
				
		IResult resultCode = CommonResult.UnknownError;
		List<LoginData> loginDatas = null;
		try {
			
			loginDatas = loginDataDao.findGroupByData();
			resultCode = CommonResult.Success;
		} catch (Exception ex) {
			resultCode = CommonResult.SystemError;
		} finally {
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("loginDatas", loginDatas);
			
		}

		
		
		return mnv;
		
	}
}

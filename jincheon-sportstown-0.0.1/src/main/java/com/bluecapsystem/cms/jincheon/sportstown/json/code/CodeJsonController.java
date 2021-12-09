package com.bluecapsystem.cms.jincheon.sportstown.json.code;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.CodeGroup;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.core.service.CodeService;

@RestController
@RequestMapping("/service/code")
public class CodeJsonController 
{

	private static final Logger logger = LoggerFactory.getLogger(CodeJsonController.class);
	
	@Autowired
	private CodeService codeServ;
	
	/**
	 * 코드 그룹 목록을 조회 한다
	 * @param request
	 * @param hasSystem
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getCodeGroups")
	public ModelAndView getCodeGroups(HttpServletRequest request, 
			@RequestParam(name="hasSystem", defaultValue="false") Boolean hasSystem)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = CommonResult.UnknownError;
		
		List<CodeGroup> groups = null;
		try
		{
			groups = codeServ.getCodeGroups(hasSystem);
			resultCode = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("코드그룹 목록 조회중 오류 발생 [hasSystem = {}] \n{}",
					hasSystem,
					ExceptionUtils.getFullStackTrace(ex));
			groups = null;
			resultCode = CommonResult.SystemError;
		}finally
		{
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("groups", groups);
		}
		return mnv;
	}
	
	/**
	 * 코드 그룹을 조회한다
	 * @param request
	 * @param groupCode
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getCodeGroup/{groupCode}")
	public ModelAndView getCodeGroup(HttpServletRequest request,
			@PathVariable("groupCode") String groupCode)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = CommonResult.UnknownError;
		
		CodeGroup group = null;
		try
		{
			group = codeServ.getCodeGroup(groupCode);
			resultCode = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("코드 그룹 조회중 오류 발생 [groupCode = {}] \n{}",
					groupCode,
					ExceptionUtils.getFullStackTrace(ex));
			group = null;
			resultCode = CommonResult.SystemError;
		}finally
		{
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("group", group);
		}
		
		return mnv;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/getCodes/{groupCode}")
	public ModelAndView getCodes(HttpServletRequest request,
			@PathVariable("groupCode") String groupCode,
			@RequestParam(name="hasNotUsed", defaultValue="false") Boolean hasNotUsed)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = CommonResult.UnknownError;
		
		List<Code> codes = null; 
		try
		{
			codes = codeServ.getCodes(new CodeSelectCondition(groupCode, null, hasNotUsed));
			resultCode = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("코드 그룹 조회중 오류 발생 [groupCode = {}] \n{}",
					groupCode,
					ExceptionUtils.getFullStackTrace(ex));
			codes = null;
			resultCode = CommonResult.SystemError;
		}finally
		{
			mnv.addObject("resultCode", resultCode);
			mnv.addObject("codes", codes);
		}
		
		return mnv;
	}
	
	
	@RequestMapping(value = "/registCode")
	public ModelAndView registCode(
			@ModelAttribute() Code code)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = codeServ.registCode(code);
		
		mnv.addObject("code", code);
		mnv.addObject("resultCode", resultCode);
		
		logger.debug("코드 등록 요청 결과 [code={}] => {}", code, resultCode);
		return mnv;
	}
	
	@RequestMapping(value = "/modifyCode")
	public ModelAndView modifyCode(
			@ModelAttribute() Code code)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = codeServ.modifyCode(code);
		
		mnv.addObject("code", code);
		mnv.addObject("resultCode", resultCode);
		
		logger.debug("코드 수정 요청 결과 [code={}] => {}", code, resultCode);
		return mnv;
	}
	
	
	@RequestMapping(value = "/removeCode/{codeId}")
	public ModelAndView removeCode(
			@PathVariable("codeId") String codeId)
	{
		ModelAndView mnv = new ModelAndView("jsonView");
		IResult resultCode = codeServ.removeCode(codeId);
		
		mnv.addObject("codeId", codeId);
		mnv.addObject("resultCode", resultCode);
		
		logger.debug("코드 삭제 요청 결과 [codeId={}] => {}", codeId, resultCode);
		return mnv;
	}

}

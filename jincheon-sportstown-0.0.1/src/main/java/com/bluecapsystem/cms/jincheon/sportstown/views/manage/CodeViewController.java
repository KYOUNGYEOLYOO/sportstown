package com.bluecapsystem.cms.jincheon.sportstown.views.manage;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.CodeGroup;
import com.bluecapsystem.cms.core.service.CodeService;

@Controller
@RequestMapping("/code")
public class CodeViewController 
{
	private static final Logger logger = LoggerFactory.getLogger(CodeViewController.class);
	
	@Autowired
	private CodeService codeServ;
	
	@RequestMapping("/manage")
	public ModelAndView manage()
	{
		ModelAndView mnv = new ModelAndView("/code/codeManage");
	
		List<CodeGroup> groups = null;
		try
		{
			groups = codeServ.getCodeGroups(false);
		}catch(Exception ex)
		{
			logger.error("코드그룹 목록 조회중 오류 발생\n{}", 
					ExceptionUtils.getFullStackTrace(ex));
			groups = null;
		}finally
		{
			mnv.addObject("groups", groups);
		}
		return mnv;
	}
	
	
	@RequestMapping("/regist/{groupCode}")
	public ModelAndView regist(
			@PathVariable("groupCode") String groupCode)
	{
		ModelAndView mnv = new ModelAndView("/code/codeRegist");
	
		CodeGroup group = null;
		try
		{
			group = codeServ.getCodeGroup(groupCode);
		}catch(Exception ex)
		{
			logger.error("코드그룹 조회중 오류 발생\n{}", 
					ExceptionUtils.getFullStackTrace(ex));
			group = null;
		}finally
		{
			mnv.addObject("group", group);
		}		
		return mnv;
	}
	
	
	@RequestMapping("/modify/{codeId}")
	public ModelAndView modify(
			@PathVariable("codeId") String codeId)
	{
		ModelAndView mnv = new ModelAndView("/code/codeModify");
		Code code = null;
		try
		{
			code = codeServ.getCode(codeId); 
		}catch(Exception ex)
		{
			logger.error("코드 조회중 오류 발생 [codeId={}] \n{}",
					codeId,
					ExceptionUtils.getFullStackTrace(ex));
			code = null;
		}finally
		{
			mnv.addObject("code", code);
		}		
		return mnv;
	}
	
	@RequestMapping("/codeList")
	public ModelAndView codeList(@RequestParam(name="listId", defaultValue="") String listId)
	{
		
		ModelAndView mnv = new ModelAndView("/code/codeList");
		
		mnv.addObject("listId", listId);
		
		return mnv;
	}
	
	
}

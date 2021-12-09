package com.bluecapsystem.cms.jincheon.sportstown.views.media;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.service.CodeService;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.PropertyService;


@Controller
@RequestMapping("/mediaPlay")
public class MediaPlayController {
	
	private static final Logger logger = LoggerFactory.getLogger(MediaPlayController.class);
	
	@Autowired
	private CodeService codeServ;
	
	@Autowired
	private FileInstanceService fileServ;
	
	
	@Autowired
	private PropertyService propServ;
	

	@RequestMapping(value = "")
	public ModelAndView mediaPlay(HttpServletRequest request)
	{
		ModelAndView mnv = new ModelAndView("/mediaPlay/mediaPlay");
		
		logger.info("URL TEST : '{}'",request.getRequestURL());
		logger.info("URI TEST : '{}'",request.getRequestURI()); // 컨트롤러로 들어오는 매핑된 URI 로그
		logger.info("JAVA CLASS PATH : '" + this.getClass().getName()+"'"); // 해당 클래스 경로가 어디인지 출력 
		System.out.println("Method NAME : "+Thread.currentThread().getStackTrace()[1].getMethodName()); // 실행중인 메소드 이름 출력
		
		try
		{
			mnv.addObject("sprotsEvents", codeServ.getCodes(new CodeSelectCondition("SPORTS_EVENT", null)));
		}catch(Exception ex){
			logger.error("/mediaPlay 화면 Load 실패 \n{}", 
					ExceptionUtils.getFullStackTrace(ex));
		}	
		
		
		return mnv;
	}

	
	@RequestMapping("/player/{fileId}")
	public ModelAndView player( @PathVariable("fileId") String fileId, HttpServletRequest request)
	{
		
		logger.info("URL TEST : '{}'",request.getRequestURL());
		logger.info("URI TEST : '{}'",request.getRequestURI()); // 컨트롤러로 들어오는 매핑된 URI 로그
		logger.info("JAVA CLASS PATH : '" + this.getClass().getName()+"'"); // 해당 클래스 경로가 어디인지 출력 
		System.out.println("Method NAME : "+Thread.currentThread().getStackTrace()[1].getMethodName()); // 실행중인 메소드 이름 출력
		
		ModelAndView mnv = new ModelAndView("/mediaPlay/player");
		FileInstance file = null;
		
		try
		{
			file = fileServ.getFileinstance(fileId);
			
			String vodStreamer = propServ.getProperty("WOWZA_PROPERTIES", "BASE_VOD_URL").valueToString();
			mnv.addObject("vodStreamer", vodStreamer);
			
			String rootUri = propServ.getProperty("FILE_URL_ROOT", file.getLocationRootCode()).valueToString();
			mnv.addObject("rootUri", rootUri);
			
			
			mnv.addObject("file",file);
		}catch(Exception ex){
			logger.error("/mediaPlay/palyer 화면 Load 실패 [fileId={}]\n{}", 
					fileId,
					ExceptionUtils.getFullStackTrace(ex));
		}
		
		return mnv;
	}
	
}

package com.bluecapsystem.cms.jincheon.sportstown.json.file;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownFileInstanceSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.json.utils.JqGridParameterParser;

@RestController
@RequestMapping("/service/file")
public class FileJsonController {

	private static final Logger logger = LoggerFactory.getLogger(FileJsonController.class);

	@Autowired
	private FileInstanceService fileServ;

	@RequestMapping("/getFileList/{locationRootCode}")
	public ModelAndView getFileList(HttpServletRequest request, @PathVariable("locationRootCode") String locationRootCode) {
		SportstownFileInstanceSelectCondition condition = new SportstownFileInstanceSelectCondition();
		condition.setLocationRootCode(locationRootCode);
		ModelAndView mnv = this.getFileList(request, condition);
		return mnv;
	}

	@RequestMapping("/getFileList")
	public ModelAndView getFileList(HttpServletRequest request, @ModelAttribute SportstownFileInstanceSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("jsonView");

		List<FileInstance> files = null;
		try {

			Paging paging = JqGridParameterParser.getPaging(request);
			condition.setPaging(paging);

			files = fileServ.getFileinstances(condition);
			logger.info("FileJsonController - /service/filem : condition = {}", condition);
			logger.info("fileServ.getFileinstances(condition) : {}", files);
		} catch (Exception ex) {
			logger.error("파일 목록 조회 오휴 [condition={}] \n{}", condition, ExceptionUtils.getFullStackTrace(ex));
			files = null;
		} finally {
			mnv.addObject("files", files);

			if (condition.getPaging() != null && condition.getPaging().getEnablePaging()) {
				JqGridParameterParser.setPaging(mnv, condition.getPaging());
			}
		}

		return mnv;
	}

}

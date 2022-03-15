package com.bluecapsystem.cms.jincheon.sportstown.views.file;

import java.io.File;

import org.codehaus.plexus.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.service.FileInstanceService;
import com.bluecapsystem.cms.core.service.ThumbnailInstanceService;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownFileInstanceSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData.DataType;

@Controller
@RequestMapping("/file")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileInstanceService fileServ;

	@Autowired
	private ThumbnailInstanceService thumbnailServ;

	@RequestMapping(value = "/download/{fileId}")
	public ModelAndView download(@PathVariable("fileId") String fileId) throws Exception {
		ModelAndView mnv = new ModelAndView("fileDownloadView");
		File localFile = null;

		FileInstance file = null;
		file = fileServ.getFileinstance(fileId);

		File root = StoragePathProperties.getDiretory(file.getLocationRootCode());
		File dir = new File(root, file.getFilePath());
		localFile = new File(dir, file.getFileName());

		mnv.addObject("fileName", file.getOrignFileName());
		mnv.addObject("file", localFile);
		return mnv;
	}

	@RequestMapping(value = "/search")
	public ModelAndView search(@ModelAttribute SportstownFileInstanceSelectCondition condition) {
		ModelAndView mnv = new ModelAndView("/file/fileSearch");

		mnv.addObject("condition", condition);
		return mnv;
	}

	@ResponseBody
	@RequestMapping(value = "/thumbnail/{fileId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] thumbnail(@PathVariable("fileId") String fileId) {
		byte[] image = null;

		FileInstance file = null;

		try {
			file = fileServ.getFileinstance(fileId);

			if (file == null) {
				logger.error("fileInstance 를 찾을수 없습니다. [fileId={}]", fileId);
				return null;
			}

			String thumbnailId = file.getThumbnailId();
			ThumbnailInstance thumbnail = thumbnailServ.getThumbnail(thumbnailId);
			image = thumbnail.getImage();
		} catch (Exception ex) {
			logger.error("thumbnail 조회중 오휴 발생 [fileId={}]\n{}", fileId, ExceptionUtils.getFullStackTrace(ex));
			image = null;
		}

		return image;
	}

}

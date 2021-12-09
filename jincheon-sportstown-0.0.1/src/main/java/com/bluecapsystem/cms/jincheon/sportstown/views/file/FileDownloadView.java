
package com.bluecapsystem.cms.jincheon.sportstown.views.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;


public class FileDownloadView extends AbstractView
{
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadView.class);
	
	public void Download(){
		setContentType("application/download; utf-8");
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
		
		boolean isSusDownload = false;
		File file = (File) model.get("file");
		String fileName = (String)model.get("fileName");
		
		if(fileName == null) fileName = file.getName();
		
		
		logger.debug("파일 다운로드 시작 [file={}] [fileName={}]", file.getPath(), fileName);
		
		String userAgent = request.getHeader("User-Agent");
		boolean ie = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1;

		// ie 관련 파일명 처리
		if(ie)
			fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");
		else
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentType(getContentType());
		response.setContentLength((int)file.length());

		
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		
		try{
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis,  out);
			
			out.flush();
			isSusDownload = true;
		}catch(Exception ex){
			logger.error("파일 다운로드 중 오류 발생 [file={}]\n{}", 
					file.getPath(),
					ExceptionUtils.getFullStackTrace(ex));
			throw ex;
		}finally{
			if(fis != null){
				try{fis.close();} catch(Exception ex){}
			}
		}
		
		if(isSusDownload)
			logger.debug("파일 다운로드 완료 [file={}]",  file.getPath());
		else
			logger.error("파일 다운로드 실패 [file={}]",  file.getPath());
		
	}
}

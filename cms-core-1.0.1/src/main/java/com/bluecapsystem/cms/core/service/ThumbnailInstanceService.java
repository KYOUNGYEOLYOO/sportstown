package com.bluecapsystem.cms.core.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcs.transcorder.ffmpeg.FFMpegProcess;
import com.bcs.transcorder.ffmpeg.FFmpegStream;
import com.bluecapsystem.cms.core.dao.ThumbnailInstanceDao;
import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.properties.FFMpegProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;

@Service("ThumbnailService")
public class ThumbnailInstanceService 
{
	private static final Logger logger = LoggerFactory.getLogger(ThumbnailInstanceService.class);
	
	
	
	@Autowired
	private ThumbnailInstanceDao thumbnailDao;
	
	@Autowired
	private EntityManagerFactory emf;
	
	
	public ThumbnailInstance getThumbnail(String thumbnailId) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		ThumbnailInstance thunbnail = null;
		try
		{
			thunbnail =  getThumbnail(em, thumbnailId);
		}catch(Exception ex)
		{
			em.close();
			throw ex;
		}
		
		em.getTransaction().commit();
		em.close();
		
		return thunbnail;
	}
	
	public ThumbnailInstance getThumbnail(EntityManager em, String thumbnailId) throws Exception
	{
		return thumbnailDao.selectThumbnail(em, thumbnailId);
	}
	

	public IResult registThumbnail(ThumbnailInstance thumbnail)
	{
		IResult result = CommonResult.UnknownError;
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		result = registThumbnail(em, thumbnail);
		
		if(result == CommonResult.Success)
			em.getTransaction().commit();
		else
			em.getTransaction().rollback();
		
		em.close();
		return result;
	}
	
	public IResult registThumbnail(EntityManager em, ThumbnailInstance thumbnail)
	{
		IResult result = CommonResult.UnknownError;
		
		try
		{
			thumbnailDao.insertThumbnail(em, thumbnail);
			em.flush();
			result = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("Thumbnail ?????? ??? ?????? ?????? [thumbnail = {}] \n{}",
					thumbnail,
					ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		}finally{
			
			logger.debug("Thumbnail ?????? ?????? [file = {}] => {}", thumbnail, result);
		}
		
		return result;
	}
	
	public IResult deleteFile(EntityManager em, String thumbnailId)
	{
		IResult result = CommonResult.UnknownError;
		
		_TRANSACTION :
		{
			try
			{
				ThumbnailInstance orgign = this.getThumbnail(em, thumbnailId);
				if(orgign == null)
				{
					result = CommonResult.NotFoundInstanceError;
					break _TRANSACTION;
				}
				
				orgign.setDeletedDate(new Date());
				orgign.setDeleted(true);
				
				result = CommonResult.Success;
			}catch(Exception ex)
			{
				logger.error("Thumbnail ?????? ??? ?????? ?????? [thumbnailId = {}] \n{}",
						thumbnailId,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			}finally{}
			
		}
		logger.debug("Thumbnail ?????? ?????? [fileId = {}] => {}", thumbnailId, result);
		return result;
	}
	
	
	
	public ThumbnailInstance createThumbnail(String thumbnailType, File sourceFile)
	{
		
		ThumbnailInstance thumbnail = null;
		
		_TRANSACTION :
		{
			try
			{
				// thumbnail ??????
				File thumbnailFile = makeThumbnail(sourceFile);
				if(thumbnailFile == null)
					break _TRANSACTION;
				
				byte[] thumbnailBuffer = readTumbnailToBytes(thumbnailFile);
				
				if(thumbnailBuffer == null)
					break _TRANSACTION;
				
				
				thumbnail = new ThumbnailInstance();
				thumbnail.setThumbnailType(thumbnailType);
				thumbnail.setImage(thumbnailBuffer);
			}catch(Exception ex)
			{
				logger.error("ThumbnailInstance ?????? ?????? [sourceFile = {}]\n{}", 
						sourceFile, 
						ExceptionUtils.getFullStackTrace(ex));
				thumbnail = null;
				break _TRANSACTION;
			}
		}
		
		return thumbnail;
	}

	public File makeThumbnail(File sourceFile)
	{
		File output = new File(sourceFile.getParent(), sourceFile.getName() + ".png");
		int retValue = -1;
		
		StringBuilder ffmpegLog = new StringBuilder();
		try
		{
			FFMpegProcess ffProc = new  FFMpegProcess(FFMpegProperties.getFFfmpeg(), sourceFile, "-ss 00:00:01", "-y -vframes 1 -an -s cga -q 2", output);
			
			String command = ffProc.commandString();
			
			Process p = ffProc.createProcess();
			FFmpegStream stream = new FFmpegStream(p);
			
			_TRANSCODER :
			{
				if(stream.initailize() == false)
				{
					logger.error("Thumnbnail ????????? ?????? ????????? ???????????? ?????? ??????????????? [command={}]", command);
					break _TRANSCODER;	
				}
				
				String str = null;
				while((str = stream.readLine()) != null)
				{
					ffmpegLog.append(str);
				}
				
				retValue = p.exitValue();
			}
			
			
			stream.close();
			ffProc.close(p);
			
			if(retValue != 0)
				output = null;
			
		}catch(Exception ex)
		{
			logger.error("Thumbnail ?????? ?????? ?????? ?????? [sourceFile={}]\n{}", 
					sourceFile,
					ExceptionUtils.getFullStackTrace(ex));
			output = null;
		}
		
		logger.debug("thumbnail ?????? ?????? ?????? [sourceFile={}] [outputFile={}]\n{}",
				sourceFile, output, ffmpegLog);
		
		return output;
	}
		
	public byte[] readTumbnailToBytes(File file)
	{
		Path path = Paths.get(file.getPath());
		byte[] buffer = null;
		
		try
		{
			buffer = Files.readAllBytes(path);
		}catch(Exception ex)
		{
			
			logger.error("Thumbnail ?????? ?????? ????????? ?????? ?????? [file={}]\n{}", 
					file,
					ExceptionUtils.getFullStackTrace(ex));
			buffer = null;
		}finally
		{
			file.delete();
		}
		
		return buffer;
	}
	
}

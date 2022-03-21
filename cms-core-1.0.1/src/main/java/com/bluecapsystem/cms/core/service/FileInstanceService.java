package com.bluecapsystem.cms.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.dao.FileInstanceDao;
import com.bluecapsystem.cms.core.dao.IFileInstanceMetaRepository;
import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.FileInstanceMeta;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.FileInstanceResult;
import com.bluecapsystem.cms.core.result.IResult;

@Service("FileService")
public class FileInstanceService {
	private static final Logger logger = LoggerFactory.getLogger(FileInstanceService.class);

	@Autowired
	private FileInstanceDao fileDao;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired(required = false)
	@Qualifier("fileInstanceMetaRepository")
	private IFileInstanceMetaRepository metaDao;

	/**
	 * 파일 리스트를 가져온다
	 *
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<FileInstance> getFileinstances(FileInstanceSelectCondition condition) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<FileInstance> list = null;
		try {
			list = getFileinstances(em, condition);
			em.close();
		} catch (Exception ex) {
			em.close();
			throw ex;
		}
		return list;
	}

	/**
	 * 파일 리스트를 가져온다
	 *
	 * @param em
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<FileInstance> getFileinstances(EntityManager em, FileInstanceSelectCondition condition) throws Exception {
		return fileDao.selectFileList(em, condition, metaDao);
	}

	/**
	 * 파일을 조회 한다
	 *
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public FileInstance getFileinstance(String fileId) throws Exception {
		EntityManager em = emf.createEntityManager();
		FileInstance file = null;
		try {
			file = getFileinstance(em, fileId);
			em.close();
		} catch (Exception ex) {
			em.close();
			throw ex;
		}
		return file;
	}

	/**
	 * 파일을 조회 한다
	 *
	 * @param em
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public FileInstance getFileinstance(EntityManager em, String fileId) throws Exception {
		return fileDao.selectFile(em, fileId);
	}

	/**
	 * 파일을 등록 한다
	 *
	 * @param file
	 * @return
	 */
	public IResult registFile(FileInstance file) {
		IResult result = CommonResult.UnknownError;

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		result = registFile(em, file);

		if (result == CommonResult.Success)
			em.getTransaction().commit();
		else
			em.getTransaction().rollback();

		em.close();
		return result;
	}

	/**
	 * 파일을 등록 한다
	 *
	 * @param em
	 * @param file
	 * @return
	 */
	public IResult registFile(EntityManager em, FileInstance file) {
		IResult result = CommonResult.UnknownError;

		try {
			_TRANSACTION: {
				FileInstanceSelectCondition condition = new FileInstanceSelectCondition();

				condition.setFileName(file.getFileName());
				condition.setFilePath(file.getFilePath());
				condition.setLocationRootCode(file.getLocationRootCode());

				FileInstance oldFile = fileDao.selectFile(em, condition, metaDao);

				if (oldFile != null) {
					result = FileInstanceResult.AlreadyRegistFile;
					break _TRANSACTION;
				}

				fileDao.insertFile(em, file);
				em.flush();
				result = CommonResult.Success;
			}
		} catch (Exception ex) {
			logger.error("파일 등록 중 오류 발생 [file = {}] \n{}", file, ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		} finally {

			logger.debug("파일 등록 결과 [file = {}] => {}", file, result);
		}

		return result;
	}

	public IResult registFile(EntityManager em, FileInstance file, FileInstanceMeta meta) {
		IResult result = CommonResult.UnknownError;

		try {
			_TRANSACTION: {
				if ((result = registFile(em, file)) != CommonResult.Success)
					break _TRANSACTION;

				if (metaDao != null)
					metaDao.insert(em, file, meta);
				
				
				result = CommonResult.Success;
			}
		} catch (Exception ex) {
			logger.error("파일 등록 중 오류 발생 [file = {}] \n{}", file, ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		} finally {

			logger.debug("파일 등록 결과 [file = {}] => {}", file, result);
		}

		return result;
	}

	/**
	 * 파일을 삭제한다
	 *
	 * @param em
	 * @param fileId
	 * @return
	 */
	public IResult deleteFile(EntityManager em, String fileId) {
		IResult result = CommonResult.UnknownError;

		_TRANSACTION: {
			try {
				FileInstance orgFile = this.getFileinstance(em, fileId);
				if (orgFile == null) {
					result = CommonResult.NotFoundInstanceError;
					break _TRANSACTION;
				}

				orgFile.setDeleteDate(new Date());
				orgFile.setIsDeleted(true);

				fileDao.updateFile(em, orgFile);

				result = CommonResult.Success;
			} catch (Exception ex) {
				logger.error("파일 삭제 중 오류 발생 [fileId = {}] \n{}", fileId, ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			} finally {
			}

		}
		logger.debug("파일 삭제 결과 [fileId = {}] => {}", fileId, result);
		return result;
	}
	
	
	/**
	 * 파일을 삭제한다
	 *
	 * @param em
	 * @param fileId
	 * @return
	 */
	public IResult updateFile(EntityManager em, FileInstance file) {
		IResult result = CommonResult.UnknownError;

		_TRANSACTION: {
			try {
				FileInstance orgFile = this.getFileinstance(em, file.getFileId());
				if (orgFile == null) {
					result = CommonResult.NotFoundInstanceError;
					break _TRANSACTION;
				}
				orgFile.setFileName(file.getFileName());
				orgFile.setExtension(file.getExtension());

				fileDao.updateFile(em, orgFile);

				result = CommonResult.Success;
			} catch (Exception ex) {
				logger.error("파일 수정 중 오류 발생 [fileId = {}] \n{}", file.getFileId(), ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			} finally {
			}

		}
		logger.debug("파일 삭제 결과 [fileId = {}] => {}", file.getFileId(), result);
		return result;
	}

	/**
	 * 경로 안의 파일 목록을 가져온다
	 *
	 * @param directoryKey
	 * @return
	 */
	public List<File> getFiles(String directoryKey) {
		File dir = StoragePathProperties.getDiretory(directoryKey);

		if (dir == null) {
			logger.error("경로를 찾을 수 없습니다 [directoryKey = {}]", directoryKey);
			return null;
		}

		File[] files = dir.listFiles();
		if (files == null)
			files = new File[0];

		return Arrays.asList(files);
	}

	public IResult transferFile(EntityManager em, FileInstance file, String descLocationRootCode, String descFilePath) throws Exception {
		IResult result = CommonResult.UnknownError;

		File descRoot = StoragePathProperties.getDiretory(descLocationRootCode);
		File descFile = new File(descRoot, descFilePath);

		if (descFile.getParentFile().exists() == false) {
			if (descFile.getParentFile().mkdirs() == false)
				throw new FileNotFoundException(String.format("대상 결로를 생성 할 수 없습니다 [descFile=%s]", descFile));
		} else {
			if (descFile.getParentFile().isDirectory() == false)
				throw new FileNotFoundException(String.format("대상 결로가 디렉토리가 아닙니다 [descFile=%s]", descFile));
		}

		file.transfer(descLocationRootCode, descFile);
		fileDao.updateFile(em, file);
		result = CommonResult.Success;

		return result;
	}

}

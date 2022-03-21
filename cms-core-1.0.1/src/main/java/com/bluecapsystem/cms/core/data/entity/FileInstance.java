package com.bluecapsystem.cms.core.data.entity;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="file_instance_tbl")
public class FileInstance
{
	@Id
	private String fileId;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+9")
	private Date registDate;

	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+9")
	private Date deleteDate;

	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isDeleted;

	@Column(nullable = true)
	private String extension;

	@Column(nullable = true)
	private String fileName;

	@Column(nullable = true)
	private String orignFileName;

	@Column(nullable = true)
	private Long fileSize;

	@Column(nullable = true)
	private String locationRootCode;


	@Column(nullable = true)
	private String fileTypeCode;

	@Column(nullable = true)
	private String filePath;


	@Column(nullable = true)
	private String thumbnailId;

	public FileInstance()
	{
		registDate = new Date();
		isDeleted = false;
	}

	public static FileInstance createInstance(String locationRootCode, File file) throws Exception
	{
		return createInstance(locationRootCode, null, file);
	}

	public static FileInstance createInstance(String locationRootCode, String fileTypeCode, File file) throws Exception
	{
		FileInstance fileInst = new FileInstance();

		fileInst.setOrignFileName(file.getName());
		fileInst.parseFile(locationRootCode, file);
		fileInst.setFileTypeCode(fileTypeCode);

		return fileInst;
	}

	public void parseFile(String locationRootCode, File file) throws Exception
	{
		this.setLocationRootCode(locationRootCode);
		File root = StoragePathProperties.getDiretory(this.locationRootCode);
		parseFile(root, file);
	}

	public void parseFile(File file) throws Exception
	{
		File root = StoragePathProperties.getDiretory(this.locationRootCode);
		parseFile(root, file);
	}

	public void parseFile(File root, File file) throws Exception
	{
		if(root == null || root.exists() == false || root.isDirectory() == false)
			throw new FileNotFoundException(String.format("디렉토리를 찾을 수 없습니다 [rootDirectory=%s]", root));

		if(file.exists() == false)
			throw new FileNotFoundException(String.format("파일을 찾을 수 없습니다 [file=%s]", file));

		if(file.getPath().startsWith(root.getPath()) == false)
		{
			throw new IllegalArgumentException(
					String.format("root 경로 안에 file 이 존재하지 않습니다 [rootDirecotry=%s, file=%s] ", root, file));
		}

		this.fileSize = file.length();
		this.fileName = file.getName();
		this.setFilePath(file.getPath().replace(root.getPath(), "").replace(file.getName(), ""));

		int tempIdx = file.getName().lastIndexOf(".");
		if(tempIdx < 0)
			this.extension = "";
		else
			this.extension = file.getName().substring(tempIdx + 1).toLowerCase();
	}

	public File getFile(File root)
	{
		String filePath = String.format("%s/%s", this.filePath, this.fileName);
		File f = new File(root, filePath);
		return f;
	}

	public File getFile()
	{
		File root = StoragePathProperties.getDiretory(this.locationRootCode);
		return getFile(root);
	}

	public void transfer(String descRootCode, File descFile) throws Exception
	{

		File descRootDir = StoragePathProperties.getDiretory(descRootCode);
		if(descRootDir == null || descRootDir.exists() == false || descRootDir.isDirectory() == false)
		{
			throw new FileNotFoundException(String.format("대상 결로를 찾을 수 없습니다 [descRootDir=%s]", descRootDir));
		}

		File srcFile = this.getFile();
		_TRANS :
		{
			if(srcFile.exists() == false)
			{
				throw new FileNotFoundException(String.format("원본파일을 찾을 수 없습니다 [srcFile=%s]", srcFile));
			}

			if(srcFile.equals(descFile))
			{
				this.setLocationRootCode(descRootCode);
				this.parseFile(descRootDir, descFile);
				break _TRANS;
			}

			File descParentDir = descFile.getParentFile();
			if( descParentDir.exists() == false && descParentDir.mkdirs() == false)
			{
				throw new FileSystemException(String.format("이동 대상 경로 생성 실패 [descParentDir=%s]", descParentDir));
			}

			if(descFile.exists() == true && descFile.delete() == false)
			{
				throw new FileSystemException(
						String.format("기존 파일 삭제 실패 [descFile=%s]", descFile));
			}

			if(this.getFile().renameTo(descFile) == true)
			{
				this.setLocationRootCode(descRootCode);
				this.parseFile(descRootDir, descFile);
			}else
			{
				throw new FileSystemException(
						String.format("파일 이동 실패 [srcFile=%s, descFile=%s]", srcFile, descFile));
			}

		}

	}

	@Override
	public String toString()
	{
		return String.format("%s["
				+ "fileId=%s, fileName=%s, extension=%s, fileTypeCode=%s, isDeleted=%s, fileName=%s, orignFileName=%s, extension=%s, locationRootCode=%s, filePath=%s, thumbnailId=%s"
				+ "]",
				this.getClass().getSimpleName(),
				fileId, fileName, extension, fileTypeCode,
				isDeleted, thumbnailId,
				fileName, getOrignFileName(), extension, locationRootCode, filePath);
	}



	// ===================================================================================== //
	// setter nad getter


	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getLocationRootCode() {
		return locationRootCode;
	}

	public void setLocationRootCode(String locationRootCode) {
		this.locationRootCode = locationRootCode;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {

		String temp = null;
		if(filePath == null)
			temp = "";
		else
		{
			temp = filePath.replace(File.separator, "/");
			if(temp.length() == 1)
				temp = "";
		}

		this.filePath = temp;
	}

	public String getOrignFileName() {
		return orignFileName;
	}

	public void setOrignFileName(String orignFileName) {
		this.orignFileName = orignFileName;
	}

	public String getThumbnailId() {
		return thumbnailId;
	}

	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public String getFileTypeCode() {
		return fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}



}

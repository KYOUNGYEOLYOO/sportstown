package com.bluecapsystem.cms.core.data.condition;


import com.bluecapsystem.cms.core.data.condition.ISelectCondition;

public class FileInstanceSelectCondition implements ISelectCondition
{
	private String keyword;
	
	private String fileId;
	
	private Boolean hasNotDeleted;
	
	private String locationRootCode;
	
	
	private String filePath;
	private String fileName;
	
	
	public FileInstanceSelectCondition()
	{
		this(null, null);
	}
	
	public FileInstanceSelectCondition(String fileId, String keyword)
	{
		this(fileId, null, keyword, false);
	}
	
	public FileInstanceSelectCondition(String fileId, String locationRootCode, String keyword, Boolean hasNotDeleted)
	{
		this.fileId = fileId;
		this.keyword = keyword;
		this.locationRootCode = locationRootCode;
		this.hasNotDeleted = hasNotDeleted;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[fileId=%s, locationRootCode=%s, filePath=%s, fileName=%s, keyword=%s, hasNotDeleted=%s]", 
				this.getClass().getSimpleName(),
				fileId, locationRootCode, filePath, fileName, keyword, hasNotDeleted);
	}

	public String getLocationRootCode() {
		return locationRootCode;
	}

	public void setLocationRootCode(String locationRootCode) {
		this.locationRootCode = locationRootCode;
	}

	public Boolean getHasNotDeleted() {
		return hasNotDeleted;
	}

	public void setHasNotDeleted(Boolean hasNotDeleted) {
		this.hasNotDeleted = hasNotDeleted;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}

package com.bluecapsystem.cms.core.data.condition;


public class Paging 
{
	private Long totalCount;
	private Integer page;
	private Integer pageSize;
	
	private Boolean enablePaging;
	
	
	public Paging()
	{
		this(null, null, false);
	}

	public Paging(Integer page, Integer rowCount, Boolean enablePaging)
	{
		this.page = page;
		this.pageSize = rowCount;
		
		this.enablePaging = enablePaging;
		
		this.totalCount = null;
	}
	
	
	public Long getTotalPageCount()
	{
		if(enablePaging == false)
			return 0L;
		try
		{
			return this.totalCount / this.pageSize + (this.totalCount % this.pageSize > 0 ? 1 : 0);
		}catch(Exception ex)
		{
			return 0L;
		}
	}
	
	public Integer getFirstResult()
	{
		Integer page = this.page;
		
		if(page == null || page <= 0)
			page = 1;
		
		return (page - 1) * this.pageSize;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getEnablePaging() {
		return enablePaging;
	}

	public void setEnablePaging(Boolean enablePaging) {
		this.enablePaging = enablePaging;
	}
}
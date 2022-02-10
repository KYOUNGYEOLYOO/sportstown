package com.bluecapsystem.cms.core.result;

import javax.persistence.EntityManager;

public class EmResult {

	private IResult result;
	private EntityManager em;
	
	
	
	public EmResult() {
		this.result = null;
		this.em = null;
	}
	
	public IResult getResult() {
		return result;
	}
	public void setResult(IResult result) {
		this.result = result;
	}
	public EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}	
}



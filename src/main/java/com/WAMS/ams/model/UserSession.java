package com.WAMS.ams.model;

import com.webhawks.Hawks_model.HEmployee;

/* ========================================
* WAMS v. 1.0 class library
* ========================================
*
* http://www.WAMS.com
*
* (C) Copyright 2000-2010, by WebHawksIT.
*
* --------------------
* UserSession.java
* --------------------
* Created on Jan 01, 2016
*
* $Revision: $
* $Author: $
* $Source: $
* $Id:  $
*
* Jan 01, 2016: Original version (Monsur)
*
*/

public class UserSession {
	private int sessionId;
	private String clientIpAddress;
	protected HEmployee emp = null;
	
	public UserSession(){
		emp = new HEmployee();
	}
	
	public int getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	public HEmployee getEmp() {
		return emp;
	}

	public void setEmp(HEmployee emp) {
		this.emp = emp;
	}

	public String getClientIpAddress() {
	    return clientIpAddress;
	}

	public void setClientIpAddress(String clientIpAddress) {
	    this.clientIpAddress = clientIpAddress;
	}
	
}

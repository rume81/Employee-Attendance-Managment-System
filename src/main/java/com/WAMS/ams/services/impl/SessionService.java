package com.WAMS.ams.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.WAMS.ams.dao.interfaces.ISessionDAO;
import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.Session;
import com.WAMS.ams.model.UserSession;
import com.WAMS.ams.services.interfaces.ISessionService;

/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2000-2010, by WebHawksIT.
*
* --------------------
* SessionService.java
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

public class SessionService implements ISessionService {
	
	private final Logger logger = LoggerFactory.getLogger(SessionService.class);
	
	private ISessionDAO sessionDao;
	
	private UserSession userSession;
	
	public void setSessionDao(ISessionDAO sessionDao) {
		this.sessionDao = sessionDao;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public UserSession insertSession(HttpServletRequest request, HEmployee emp) throws Exception {
		if (!isSessionValid()) {
		    // sets session info
		    Session session = new Session();
		    session.setId(-1);
		    session.setDeleted(false);
		    session.setModId(emp.getAvator());
		    session.setUser(emp);
		    session.setReferrer(request.getHeader("referer"));
		    session.setBrowser(request.getHeader("user-agent"));
		    session.setSearchstring(request.getQueryString());

		    String requestUrl = request.getRequestURL().toString();
		    requestUrl = requestUrl.replaceAll("'", "''");
		    session.setLocation(requestUrl);
		    String ip = request.getRemoteAddr();
		    session.setClientIpAddress(ip);
		    Date date = new Date();
			
		    session.setSessionStart(date);
		    session.setSessionEnd(date);

		    // insert session info in DB
		    int sessionId = sessionDao.insertSession(session);

		    // update current web session
		    userSession.setSessionId(sessionId);
		    userSession.setClientIpAddress(ip);
		    userSession.setEmp(emp);
		}// session not set

		logger.info("User Session Id - > " + userSession.getSessionId());
		return userSession;
	}

	public void setEmployee(HEmployee emp){
		userSession.setEmp(emp);
	}
	
	public boolean isSessionValid() {
		return (userSession.getSessionId() > 0 ? true : false);
	}

	public void invalidateSession(int sessionId) {
		userSession.setSessionId(0);
		
	}

}

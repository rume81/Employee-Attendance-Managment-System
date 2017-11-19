package com.WAMS.ams.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.WAMS.ams.utils.JdbcService;
import com.WAMS.ams.utils.JdbcServiceAcc;

/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2000-2010, by WebHawksIT.
*
* --------------------
* BaseDao.java
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
public class BaseDAO {
	private JdbcService jdbcService;
	private JdbcServiceAcc jdbcServiceAcc;
	
	public JdbcService getJdbcService() {
		return jdbcService;
	}


	public void setJdbcService(JdbcService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public JdbcServiceAcc getJdbcServiceAcc() {
	    return jdbcServiceAcc;
	}


	public void setJdbcServiceAcc(JdbcServiceAcc jdbcServiceAcc) {
	    this.jdbcServiceAcc = jdbcServiceAcc;
	}


	protected String getCommonArgs() {

		return "deleted = 0, modifierid = 1";
	}
}

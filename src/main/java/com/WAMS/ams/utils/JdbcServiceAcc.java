/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* JdbcServiceAcc.java
* --------------------
* Created on Aug 12, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* Aug 12, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author OS-10 Monsur
 *
 */
public class JdbcServiceAcc extends JdbcAccessor {
    private static Logger log = LoggerFactory.getLogger(JdbcService.class);

    private JdbcTemplate jdbcTemplate;

    private PlatformTransactionManager transactionManager;

    public TransactionStatus beginTran() {

	if (transactionManager == null) {
	    log.warn("null txn manager");
		return null;
	}

	TransactionDefinition txn = new DefaultTransactionDefinition();
	return transactionManager.getTransaction(txn);
    }

    public void commitTran(TransactionStatus status) {
	if (transactionManager == null) {
	    log.warn("null txn manager");
		return;
	}
	transactionManager.commit(status);
    }

    public void rollbackTran(TransactionStatus status) {

	if (transactionManager == null) {
	    log.warn("null txn manager");
	    return;
	}

	transactionManager.rollback(status);
    }
	
    /*public Object queryForObject(String sql, RowMapper mapper){
	Object ob = new Object();
	try {
		ob = getJdbcTemplate()
			.queryForObject(sql, mapper);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ob;
	}*/

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
	this.transactionManager = transactionManager;
    }    
}

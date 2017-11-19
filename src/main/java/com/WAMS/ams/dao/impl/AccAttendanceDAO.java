/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* AccAttendanceDAO.java
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
package com.WAMS.ams.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.WAMS.ams.dao.interfaces.IAccAttendanceDAO;
import com.webhawks.Hawks_mapper.CardAttMapper;
import com.webhawks.Hawks_model.HCardAttendance;

/**
 * @author OS-10 Monsur
 *
 */
public class AccAttendanceDAO extends BaseDAO implements IAccAttendanceDAO {
    private final Logger logger = LoggerFactory.getLogger(AccAttendanceDAO.class);
    @Override
    public List<HCardAttendance> getAllAttendance(String CHECKTIME) {
	List<HCardAttendance> allAtt = new ArrayList<HCardAttendance>();
	try {
	    if(CHECKTIME==null){
		allAtt = getJdbcServiceAcc().getJdbcTemplate().query(
	    		"SELECT * FROM CHECKINOUT ORDER BY CHECKTIME",new Object[] {}, new CardAttMapper());
	    } else{
		allAtt = getJdbcServiceAcc().getJdbcTemplate().query(
	    		"SELECT * FROM CHECKINOUT WHERE CHECKINOUT.CHECKTIME > #"+CHECKTIME+"#  ORDER BY CHECKTIME",new Object[] {}, new CardAttMapper());
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	//logger.info("HCardAttendance feactch =================="+allAtt.size());
	return allAtt;
    }
    
    @Override
    public List<HCardAttendance> getCardAttendanceOfDate(String date) {
	List<HCardAttendance> allAtt = new ArrayList<HCardAttendance>();
	try {
	    	allAtt = getJdbcServiceAcc().getJdbcTemplate().query(
	    		"SELECT * FROM CHECKINOUT WHERE CHECKINOUT.CHECKTIME > #"+date+"#  ORDER BY CHECKTIME",new Object[] {}, new CardAttMapper());
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	//logger.info("HCardAttendance feactch =================="+allAtt.size());
	return allAtt;
    }
}

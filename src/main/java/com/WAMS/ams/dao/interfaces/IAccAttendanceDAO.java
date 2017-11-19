/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* IAccAttendanceDAO.java
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
package com.WAMS.ams.dao.interfaces;

import java.util.List;

import com.webhawks.Hawks_model.HCardAttendance;

/**
 * @author OS-10 Monsur
 *
 */
public interface IAccAttendanceDAO {
    public List<HCardAttendance> getAllAttendance(String CHECKTIME);
    
    public List<HCardAttendance> getCardAttendanceOfDate(String date);
}

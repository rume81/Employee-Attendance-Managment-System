/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* ExampleJob.java
* --------------------
* Created on May 27, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* May 27, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.webhawks.Hawks_model.HAttendance;
import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.webhawks.Hawks_model.HWorkInfo;
import com.WAMS.ams.services.interfaces.IWamsService;

/**
 * @author OS-10 Monsur
 *
 */
public class BatchSignoutJob extends QuartzJobBean {
    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    
    /**
     * Setter called after the ExampleJob is instantiated
     * with the value from the JobDetailBean (5)
     */ 
    private ApplicationContext getApplicationContext(JobExecutionContext context ) throws Exception {
	ApplicationContext appCtx = null;
	appCtx = (ApplicationContext)context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
	if (appCtx == null) {
	    throw new JobExecutionException(
		    "No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");
	}
	return appCtx;
    }
    
    protected SimpleDateFormat DateFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	return df;
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	Date today = new Date();
	String outDate = DateFormateforDB().format(today);
	System.out.println("=======================================Run Batch Signout:"+outDate+"=================================");
	String EmpTime = "18:00:00";
	    
	//find employee not give signout
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	IWamsService wamsService = (IWamsService) appCtx.getBean("wamsService");
	List<HEmployee> notGivenSignOutEmp = wamsService.getEmpNotGivenSignout(outDate);
	
	Integer insert = -2;
	for(HEmployee employee:notGivenSignOutEmp)
	{
	    HWorkInfo info = wamsService.getWorkInfoById(employee.getEmp_id(),outDate);
	    float startTime = info.getOffice_start();
	    float workHour = info.getWorking_hour();
	    float endTime = startTime + workHour;
	    EmpTime = getEndTime(endTime);
	    HAttendance att = new HAttendance();
	    att.setEmp_id(employee.getEmp_id());
	    att.setAtt_date(outDate);
	    att.setAtt_time(EmpTime);
	    att.setAtt_inout(1);
	    att.setModId("som");
	    att.setDeleted(false);
		
	    wamsService.insertHAttendance(att);
	}
	
	System.out.println("=======================================End Batch Signout:"+outDate+"=================================");
    }
    
    private String getEndTime(float et)
    {
	String endTime="";
	int intEt = (int) et;
	
	endTime=intEt+":"+(((et-intEt)*60)==0? "00" :((et-intEt)*60))+":00";
	
	return endTime;
    }
}

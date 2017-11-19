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
import java.util.Calendar;
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
public class ReportGenerateJob extends QuartzJobBean {
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
	System.out.println("=======================================Run Report Generate:"+outDate+"=================================");
	
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	IWamsService wamsService = (IWamsService) appCtx.getBean("wamsService");
	
	//==================================For Setup Attendance===================================
	Date pdate = yesterday(-1);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String prev_date = df.format(pdate);
	List<HAttendance> todayAtt = wamsService.getTodayAttendance(prev_date);
	
	for(HAttendance att:todayAtt){
	    String st = convertDateFormate(att.getAtt_date());
	    att.setAtt_date(st);
	    wamsService.insertHAttendance_bak(att);
	}
	
	boolean delall = wamsService.deleteHAttendance(prev_date);
	Date ludate = yesterday(-2);
	String last_update_date = df.format(ludate);
	boolean updateAtt = wamsService.alterHAttendance_Auto_inc(last_update_date);
	
	wamsService.insertFirstInLastOut(prev_date);
	
	//==================================For ReGenerate Report===================================
	Date dd = new Date();
	SimpleDateFormat dfd = new SimpleDateFormat("dd");
	int day = Integer.parseInt(dfd.format(dd));
	
	SimpleDateFormat dfm = new SimpleDateFormat("MM");
	int mon = Integer.parseInt(dfm.format(dd));
	
	SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	int year = Integer.parseInt(dfy.format(dd));
	
	//int endDate = endDateofMon(mon,year);
	
	if(day==1){
	    if(mon==1){
		mon=12;
		year=year-1;
	    }else{
		mon=mon-1;
	    }
	}
	
	List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(1, mon, year);
	//==================================For ReGenerate Report===================================
	
	System.out.println("=======================================Run Report Generate:"+outDate+"=================================");
    }
    
    public int endDateofMon(int mon,int year)
    {
	int day= 0;
	if((mon==1)||(mon==3)||(mon==5)||(mon==7)||(mon==8)||(mon==10)||(mon==12))
	    day = 31;
	else if((mon==4)||(mon==6)||(mon==9)||(mon==11))
	    day = 30;
	else if((mon==2) && (LeapYear(year)))
	    day = 29;
	else
	    day = 28;
	    
	return day;
    }
    
    private boolean LeapYear(int year)
    {
	 boolean leapyear = false;
	 if ((year % 4 == 0) && year % 100 != 0)
	 {
	     leapyear = true;
	 }
	 else if ((year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0))
	 {
	     leapyear = true;
	 }
	 else
	 {
	     leapyear = false;
	 }
	    
	 return leapyear;
    }
    
    private Date yesterday(int amount) {
	final Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, amount);
	return cal.getTime();
    }
    
    public String convertDateFormate(String dt) {//25-01-2016  25  01  2016   output 2016-01-25
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[2] + "-" + splitdt[1] + "-" + splitdt[0];

	return formatedDate;

    }
}

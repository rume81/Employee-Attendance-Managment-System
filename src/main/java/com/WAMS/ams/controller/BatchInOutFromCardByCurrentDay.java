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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.webhawks.Hawks_model.HAttendance;
import com.webhawks.Hawks_model.HCardAttendance;
import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HEmployeeparam;
import com.webhawks.Hawks_model.HLunch;
import com.webhawks.Hawks_model.HWorkInfo;
import com.WAMS.ams.services.interfaces.IWamsService;

/**
 * @author OS-10 Monsur
 *
 */
public class BatchInOutFromCardByCurrentDay extends QuartzJobBean {
    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    private IWamsService wamsService;
    private String autolunchentry;
    
    private List<Integer> lunchInstance = new ArrayList<Integer>();
    private List<HAttendance> attSequence = new ArrayList<HAttendance>();
    
    public void setAutolunchentry(String autolunchentry) {
        this.autolunchentry = autolunchentry;
    }

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
	System.out.println("=======================================Run Batch In Out From Card By Current Date:"+today+"=================================");
	
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	wamsService = (IWamsService) appCtx.getBean("wamsService");
	//Get last input date time
	//HCardAttendance lastAtt = wamsService.getLastAttendanceFromDb1();
	//List<HCardAttendance> todayattendance = wamsService.getCardAttendanceOfDate(outDate);
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat dfdb = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dtdb = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dtwd = new SimpleDateFormat("EEEE");
	String strLastdt = null;
	String lastAtt = outDate+" 00:00:00";
	try {
	    if(lastAtt != null)
	    {
		Date lastdt = df.parse(lastAtt);
		strLastdt = df.format(lastdt);
	    }
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	
	List<HCardAttendance> atten = new ArrayList<HCardAttendance>();
	//atten = wamsService.getCardAttendance(strLastdt);
	atten = wamsService.getCardAttendanceOfDate(strLastdt);
		
	
	StringBuffer sql = new StringBuffer("INSERT INTO checkinout ");
	sql.append("(USERID, CHECKTIME, CHECKTYPE, VERIFYCODE, SENSORID, Memoinfo, WorkCode, sn, UserExtFmt, deleted, modifierid) VALUES");

	StringBuffer sqlAtt = new StringBuffer("INSERT INTO attendance ");
	sqlAtt.append("(emp_id, att_date, att_time, att_inout, day_sequence, deleted, modifierid) VALUES");
				
	StringBuffer sqlLun = new StringBuffer("INSERT INTO lunch ");
	sqlLun.append("(emp_id, name, lunch_date, lunch_status, lunch_count, deleted, modifierid) VALUES");
	
	int size=0;
	int attSize=0;
	int lunchSize=0;
	
	for(int i=0;i<atten.size();i++){
	    HCardAttendance att= atten.get(i);
	    
	    Date att_date = new Date();
	    try {
		att_date = df.parse(att.getCHECKTIME());
	    } catch (ParseException e1) {
		e1.printStackTrace();
	    }
	    String attdt= df.format(att_date);
	    att.setCHECKTIME(attdt);
	    if(!wamsService.checkCardAttendance(att)){
		    size++;
		    
        	    att.setDeleted(false);
        	    att.setModId("sys");
        	    long VERIFYCODE = att.getVERIFYCODE();
        	    boolean haveLunch = false;
        	    if(VERIFYCODE!=0){
        		HEmployee emp = wamsService.getEmployeeByEmpnumber(att.getUSERID());
        		
        		int inout =0;
        		String EmpDate="";
        		String EmpTime="";
        		String Weekday = "";
        		Date empdt = new Date();
        		try {
        		    if(att.getCHECKTIME() != null)
        		    {
        			empdt = df.parse(att.getCHECKTIME());
        			EmpDate = dfdb.format(empdt);
        			EmpTime = dtdb.format(empdt);
        			Weekday = dtwd.format(empdt);
        		    }
        		} catch (ParseException e) {
        		    e.printStackTrace();
        		}
        		
        		if((att.getVERIFYCODE()==2) || (att.getVERIFYCODE()==1))
        		{
        		    inout = 0; 
        		} else if(att.getVERIFYCODE()==101){
        		    inout = 1;
        		} else{
        		    inout = 0;
        		}
        		//Add Attendance
        		HAttendance attendance = new HAttendance();
        		Integer daySeq = 0;
        		if(emp.getEmp_id() != null){
        		    attendance.setEmp_id(emp.getEmp_id());
        		    attendance.setAtt_date(EmpDate);
        		    attendance.setAtt_time(EmpTime);
            		    attendance.setAtt_inout(inout);
        		    attendance.setModId(att.getSn());
        		    attendance.setDeleted(false);
        		    attendance.setDay_sequence(0);
            		    		    
        		    daySeq = getDaySequence(attendance);
        		    attendance.setDay_sequence(daySeq+1);
        		    
        		    attSequence.add(attendance);
        		}
        				
        		//Add Lunch
        		HLunch lunch = new HLunch();
        		HEmployeeparam autoLunchState = new HEmployeeparam();
        		if(emp.getEmp_id() != null){
        		    autoLunchState = wamsService.getAutoLunch(emp.getEmp_id());
        		    //System.out.println(autolunchentry+" "+Weekday+" "+autoLunchState.getAutolunchinput()+" "+daySeq+" "+findLunchOrder(emp.getEmp_id()));
                	    if((autolunchentry.equals("1")) && (!Weekday.equals("Saturday")) && (!Weekday.equals("Sunday")) && (autoLunchState.getAutolunchinput()==true) && (daySeq==0) && (!findLunchOrder(emp.getEmp_id()))){
                		lunch.setEmp_id(emp.getEmp_id());
                		lunch.setLunch_date(EmpDate);
                		lunch.setName(emp.getAvator());
                		lunch.setLunch_status(true);
                		lunch.setLunch_count(1);
                		lunch.setDeleted(false);
                		lunch.setModId(att.getSn());
                		    
                		lunch.escapeEcmaScript();
                		haveLunch = true;
                	    }
        		}
        		//Prepare SQL Command
        		if(i==(atten.size()-1)){
        		    sql.append("("+att.getUSERID() + ",'" + att.getCHECKTIME() + "','" 
        				+ att.getCHECKTYPE() + "'," + att.getVERIFYCODE() + ",'" 
        				+ att.getSENSORID() + "','" + att.getMemoinfo() + "','" 
        				+ att.getWorkCode() + "','" + att.getSn() + "'," 
        				+ att.getUserExtFmt() + "," + att.getDeleted()
        				+ ",'" + att.getModId() + "');");
        		    
        		    if(emp.getEmp_id() != null){
        			attSize++;
        			sqlAtt.append("("+attendance.getEmp_id() + ",'" + attendance.getAtt_date() + "','" 
        				+ attendance.getAtt_time() + "'," + attendance.getAtt_inout() + "," 
        				+ (daySeq +1) + ","
        				+ attendance.getDeleted() + ",'" + attendance.getModId()+ "');");
        		    }
        		    if(haveLunch){
        			lunchSize++;
        			sqlLun.append("("+lunch.getEmp_id() + ",'" + lunch.getName() + "','" 
            				+ lunch.getLunch_date() + "'," + lunch.getLunch_status() + "," + lunch.getLunch_count() + ","
            				+ lunch.getDeleted() + ",'" + lunch.getModId()+ "');");
            			}
        		} else{
        		    sql.append("("+att.getUSERID() + ",'" + att.getCHECKTIME() + "','" 
        				+ att.getCHECKTYPE() + "'," + att.getVERIFYCODE() + ",'" 
        				+ att.getSENSORID() + "','" + att.getMemoinfo() + "','" 
        				+ att.getWorkCode() + "','" + att.getSn() + "'," 
        				+ att.getUserExtFmt() + "," + att.getDeleted()
        				+ ",'" + att.getModId() + "'),");
        		    
        		    if(emp.getEmp_id() != null){
        			attSize++;
        			sqlAtt.append("("+attendance.getEmp_id() + ",'" + attendance.getAtt_date() + "','" 
        				+ attendance.getAtt_time() + "'," + attendance.getAtt_inout() + "," 
        				+ (daySeq +1) + ","
        				+ attendance.getDeleted() + ",'" + attendance.getModId()+ "'),");
        		    }
        		    if(haveLunch){
        			lunchSize++;
        			sqlLun.append("("+lunch.getEmp_id() + ",'" + lunch.getName() + "','" 
        				+ lunch.getLunch_date() + "'," + lunch.getLunch_status() + "," + lunch.getLunch_count() + ","
        				+ lunch.getDeleted() + ",'" + lunch.getModId()+ "'),");
        		    }
        		}
        		
        	    }
	    	}
	}
	System.out.println("HCardAttendance feactch =================="+size);
	if(size>0){
	    int len = sql.length();
	    sql.deleteCharAt(len-1);
	    sql.append(";");
	    wamsService.insertHCardAttendance(sql);
	}
	if(attSize>0){
	    int len = sqlAtt.length();
	    sqlAtt.deleteCharAt(len-1);
	    sqlAtt.append(";");
	    wamsService.insertHAttendance(sqlAtt);
	}
	if(lunchSize>0){
	    int len = sqlLun.length();
	    sqlLun.deleteCharAt(len-1);
	    sqlLun.append(";");
	    wamsService.insertHLunch(sqlLun);
	}
	today = new Date();
	System.out.println("=======================================End Batch In Out From Card By Current Date:"+today+"=================================");
    }
    
    private boolean findLunchOrder(int id)
    {
	boolean fo = false;
	for(Integer user:lunchInstance){
	    if(user==id){
		fo=true;
	    }
	}
	if(!fo){
	    lunchInstance.add(id);
	}
	return fo;
    }
    
    private int getDaySequence(HAttendance att){
	int dsq = 0;
	for(HAttendance attdence:attSequence){
	    if((attdence.getEmp_id() == att.getEmp_id()) && (attdence.getAtt_date().equals(att.getAtt_date())) ){
		if(dsq<attdence.getDay_sequence())
		{
		    dsq = attdence.getDay_sequence();
		}
	    }
	}
	if(dsq==0)
	    dsq = wamsService.getAttSequence(att);
	
	return dsq;
    }
    
    /*private String getEndTime(float et)
    {
	String endTime="";
	int intEt = (int) et;
	
	endTime=intEt+":"+(((et-intEt)*60)==0? "00" :((et-intEt)*60))+":00";
	
	return endTime;
    }*/
}

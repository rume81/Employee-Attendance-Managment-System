package com.WAMS.ams.services.impl;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.WAMS.ams.services.interfaces.IWamsService;
import com.WAMS.ams.dao.impl.AccAttendanceDAO;
import com.WAMS.ams.dao.interfaces.IAccAttendanceDAO;
import com.WAMS.ams.dao.interfaces.IWamsDAO;
import com.webhawks.Hawks_model.HAttendance;
import com.webhawks.Hawks_model.HCardAttendance;
import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HEmployeeparam;
import com.webhawks.Hawks_model.HHoliday;
import com.webhawks.Hawks_model.HLeave;
import com.webhawks.Hawks_model.HLeaveApprover;
import com.webhawks.Hawks_model.HLeaveQuota;
import com.webhawks.Hawks_model.HLeaveStatus;
import com.webhawks.Hawks_model.HLunch;
import com.webhawks.Hawks_model.HMAttendance;
import com.webhawks.Hawks_model.HMail;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.webhawks.Hawks_model.HOfficeOutgoing;
import com.webhawks.Hawks_model.HOfficeOutgoingApprover;
import com.webhawks.Hawks_model.HOfficeOutgoingStatus;
import com.webhawks.Hawks_model.HSupportingData;
import com.webhawks.Hawks_model.HTeam;
import com.webhawks.Hawks_model.HTypesOfLeave;
import com.webhawks.Hawks_model.HWorkInfo;
import com.webhawks.Hawks_model.HYearlyLeave;

/*========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* ProfileService.java
* --------------------
* Created on Jan 04, 2016
*
* $Revision: $
* $Author: $
* $Source: $
* $Id:  $
*
* Jan 04, 2016: Original version (Monsur)
*
*/

public class WamsService implements IWamsService {
	
	private IWamsDAO wamsDao;
	private IAccAttendanceDAO accAttendanceDAO;

	public void setWamsDao(IWamsDAO wamsDao) {
	    this.wamsDao = wamsDao;
	}
	
	public void setAccAttendanceDAO(IAccAttendanceDAO accAttendanceDAO) {
	    this.accAttendanceDAO = accAttendanceDAO;
	}

	public HEmployee getUserValidation(HEmployee user) {
	    HEmployee returnUser = wamsDao.getEmployeeByPassword(false,user.getAvator(),user.getPassword());
    	    /*for(HEmployee luser: allUser){
    		if((luser.getAvator().equalsIgnoreCase(user.getAvator())) && (luser.getPassword().equals(user.getPassword()))){
    		    returnUser = new HEmployee();
    		    returnUser = luser;
    		    break;
    		}
    	    }*/
    	    return returnUser;
	}
	
	public HEmployee getEmployeeById(Integer id) {
	    return wamsDao.getEmployeeById(id);
	}
	
	public List<HEmployee> getAllEmployee(boolean isDeleted,String userType){
	    return wamsDao.getAllEmployee(isDeleted, userType);
	}

	public Integer editHEmployee(HEmployee emp) {
	    return wamsDao.editHEmployee(emp);
	}
	
	public Integer insertHEmployee(HEmployee emp) {
	    return wamsDao.insertHEmployee(emp);
	}

	public Integer insertHWorkInfo(HWorkInfo work) {
	    return wamsDao.insertHWorkInfo(work);
	}
	/*public String insertHAcademicInfo(HAcademicInfo empEdu) {
		return profileDao.insertHAcademicInfo(empEdu);
	}
	
	public List<HAcademicInfo> getAcademicInfo(String id, boolean isDeleted)
	{
		List<HAcademicInfo> infos = profileDao.getAcademicInfo(id, isDeleted);
		List<HAcademicInfo> acdInf = new ArrayList<HAcademicInfo>();
		for(HAcademicInfo inf:infos)
		{
			//Get Type obj
			HSupportingdata degreeType = inf.getDegreetype();
			inf.setDegreetype(supportingDataDao.getSupportingDataById(degreeType.getId()));
			acdInf.add(inf);
		}
		
		return acdInf;
	}*/

	@Override
	public Integer getAttSequence(HAttendance att) {
	    return wamsDao.getAttSequence(att);
	}

	@Override
	public Integer insertHAttendance(HAttendance empAtt) {
	    return wamsDao.insertHAttendance(empAtt);
	}
	
	@Override
	public Integer insertHAttendance(StringBuffer sql) {
	    return wamsDao.insertHAttendance(sql);
	}
	@Override
	public List<HAttendance> getTodayAttendance(String date, Integer emp_id) {
	    return wamsDao.getTodayAttendance(date,emp_id);
	}

	@Override
	public List<HEmployee> getAllEmployee(boolean isDeleted) {
	    return wamsDao.getAllEmployee(isDeleted);
	}

	@Override
	public Integer insertHHolidayInfo(HHoliday holiday) {
	    return wamsDao.insertHHolidayInfo(holiday);
	}

	@Override
	public List<HHoliday> getAllHoliday(boolean isDeleted, int year) {
	    return wamsDao.getAllHoliday(isDeleted,year);
	}

	@Override
	public List<HMAttendance> getAttendanceForMonth(int mon, int year, int emp_id,boolean isCurdatechk) {
	    List<HMAttendance> monAtt = new ArrayList<HMAttendance>();
	    List<HMAttendance> blankmonAtt = new ArrayList<HMAttendance>();
	    	    
	    HEmployee curEmp = wamsDao.getEmployeeById(emp_id);
	    String join_date = curEmp.getJoin_date();
	    int startDate = 1; 
	    if((join_date.equals("")) || (join_date==null))
	    {
		join_date ="2016-05-18";
	    }
	    if((!join_date.equals("")) && (join_date!=null))
	    {
        	    String[] splitjoindt = join_date.split("-");
        	    int join_mon = 0,join_year=0;
        	    if(!splitjoindt[1].equals(""))
        		join_mon = Integer.parseInt(splitjoindt[1]);
        	    if(!splitjoindt[2].equals(""))
        		join_year = Integer.parseInt(splitjoindt[2]);
        	    
        	    if((mon==join_mon) && (year==join_year))
        	    {
        		startDate = Integer.parseInt(splitjoindt[0]);
        	    }
	    }
	    
	    int endDate = endDateofMon(mon,year);
	    Date today = new Date();
	    SimpleDateFormat dfd = new SimpleDateFormat("dd");
	    int curdd = Integer.parseInt(dfd.format(today));
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curmm = Integer.parseInt(dfm.format(today));
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int curyy = Integer.parseInt(dfy.format(today));
	    
	    curdd = curdd - 1;
	    if(curdd==0)
		curdd = 1;
	    if((!isCurdatechk) && (mon==curmm) && (year==curyy) && (endDate>curdd))
	    {
		endDate = curdd;
	    }
	    boolean dataFound = false;
	    for(int dd=startDate;dd<=endDate;dd++)
	    {
		
		String curDate = year+"-"+mon+"-"+dd;
		//System.out.println(curDate);
		String currentDate= dd+"-"+mon+"-"+year;
		SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
		Date dt1 = null;
		try {
		    dt1=format1.parse(currentDate);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		SimpleDateFormat format2=new SimpleDateFormat("EEEE");
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		String weekDay="";
		if(dt1!=null)
		    weekDay=format2.format(dt1);
		    
		//System.out.println(weekDay);
		
		
		HAttendance startAtt = wamsDao.getDailyAtt(curDate,0,emp_id);
		HAttendance endAtt = wamsDao.getDailyAtt(curDate,1,emp_id);
		
		String dayforshow = dd+"-"+mon+"-"+year;
		HMAttendance dailyAtt = new HMAttendance();
		dailyAtt.setAtt_date(dayforshow);
		if(null != startAtt.getAtt_time())
		    dailyAtt.setAtt_in(changeTimeFormate(startAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_in("");
		
		if(null != endAtt.getAtt_time())
		    dailyAtt.setAtt_out(changeTimeFormate(endAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_out("");
		
		dailyAtt.setEmp_id(emp_id);
		dailyAtt.setEmployee_name(curEmp.getEmployee_name());
		
		HWorkInfo info = wamsDao.getWorkInfoById(emp_id,curDate);
		//satus need to write code for Late
		if(info.getOffice_start()!=null && startAtt.getAtt_time()!=null)
		{
		    float officeStart = info.getOffice_start();
		    //String startTime = ((int) officeStart) + ":"+"00:00";
		    String startTime = getOfficeStart(officeStart);
		    String attTime = startAtt.getAtt_time();
		    
		    Date stdate=null;
		    Date atdate=null;
		    try {
			atdate = formatter.parse(attTime);
			stdate = formatter.parse(startTime);
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		    
		    long diffInMillis =    stdate.getTime() - atdate.getTime();
		    if(diffInMillis<0)
		    {
			long sec = (Math.abs(diffInMillis) /1000);
			long min = 0;
			long hour = 0;
			if(sec>60)
			{
			    min = sec/60;
			    sec = sec%60;
			}
			if(min>60)
			{
			    hour = min/60;
			    min = min%60;
			}
			String stMsg="";
			boolean fo = false;
			if(hour>0){
			    stMsg=stMsg+hour+" Hour ";
			    fo = true;
			}
			if(min>0){
			    if(min==1)
				stMsg=stMsg+min+" Minute ";
			    else
				stMsg=stMsg+min+" Minutes ";
			    fo = true;
			}
			if(sec>0){
			    //stMsg=stMsg+sec+" Second ";
			    //fo = true;
			}
			if(fo){
			    stMsg= stMsg+"Late";
			    dailyAtt.setLate(true);
			}
			dailyAtt.setStatus(stMsg);
		    }
		    dataFound = true;
		}
		//satus need to write code for Late
		//weekday check
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    for(String w:weekends)
		    {
			if(w.equals(weekDay)){
			    dailyAtt.setWeekend(true);
			    /*if(dailyAtt.getStatus()!=null)
				dailyAtt.setStatus(dailyAtt.getStatus()+" WeekDay");
			    else*/
				dailyAtt.setStatus("WeekDay");
			}
		    }
		}
		//weekday check		
		//holiday check
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(curDate);
		for(HHoliday holidayinfo:holidayinfoList)
		{
    			if(holidayinfo!=null)
    			{
    			    if(null!=holidayinfo.getId())
    			    {
    				dailyAtt.setHoliday(true);
    				if(dailyAtt.getStatus()!=null)
    				    dailyAtt.setStatus(dailyAtt.getStatus()+" HoliDay");
    				else
    				    dailyAtt.setStatus("HoliDay");
    				break;
    			    }
    			}
		}
		//holiday check
		//Leave check
		boolean lFo = false;
		lFo=wamsDao.getLeaveInfoByDate(curDate,emp_id,0);
		if(lFo)
		{
		    //dailyAtt.setLeave(true);
		    /*if(dailyAtt.getStatus()!=null)
			dailyAtt.setStatus(dailyAtt.getStatus()+" Leave");
		    else*/
		    List<HLeave> leaves = wamsDao.getLeaveDataByDate(curDate, emp_id);
		    for(HLeave leave:leaves){
			if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend())){
        			if(leave.getStatus()== 1){
                		    dailyAtt.setLeave(false);
                		    dailyAtt.setLwp(false);
                		    continue;
                		}
                		if(leave.getStatus()== 0){
                		    dailyAtt.setStatus("Applied For Leave");
                		    dailyAtt.setLeave(true);
                		    if(leave.getLeavetype().getId()==3){//lwp
                			dailyAtt.setLwp(true);
                		    } else  {
                			dailyAtt.setLwp(false);
                		    }
                		} else if(leave.getStatus()== 2){
                		    dailyAtt.setStatus("Leave");
                		    dailyAtt.setLeave(true);
                		    if(leave.getLeavetype().getId()==3){//lwp
                			dailyAtt.setLwp(true);
                		    } else  {
                			dailyAtt.setLwp(false);
                		    }
                		}
			}
		    }
		}
		//Leave check
		//Absent check
		if((startAtt.getAtt_time()==null) && (dd<=endDate))
		{
		    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
		    {
			dailyAtt.setAbsent(true);
			if(dailyAtt.getStatus()!=null)
			    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
			else
			    dailyAtt.setStatus("Absent");
		    }
		}
		//Absent check
		monAtt.add(dailyAtt);
	    }
	    if(isCurdatechk)
		return monAtt;
	    else{
    	    	if(dataFound)
    	    	    return monAtt;
    	    	else
    	    	    return blankmonAtt;
	    }
	}
	
	@Override
	public List<HEmployee> getAttendanceInStatus(String date) {
	    List<HEmployee> notIn = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();
	    
	    String currentDate= date;
	    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
	    Date dt1 = null;
	    String dtwork = null;
	    try {
		dt1=format1.parse(currentDate);
		dtwork=format1.format(dt1);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		
	    String weekDay="";
	    if(dt1!=null)
		weekDay=format2.format(dt1);
	    
	    allEmp = wamsDao.getAllEmployee(false);	   
	    
	    for(HEmployee emp:allEmp)
	    {
		HAttendance startAtt = wamsDao.getDailyAtt(date,0,emp.getEmp_id());
		
		HWorkInfo info = wamsDao.getWorkInfoById(emp.getEmp_id(),dtwork);
		//weekday check
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    for(String w:weekends)
		    {
			if(w.equals(weekDay)){
			   continue;
			}
		    }
		}
		//weekday check
		//holiday check
		/*HHoliday holidayinfo = wamsDao.getHolidayByDate(date);
		if(holidayinfo!=null)
		{
		    if(null!=holidayinfo.getId())
		    {
			continue;
		    }
		}*/
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(date);
		for(HHoliday holidayinfo:holidayinfoList)
		{
    			if(holidayinfo!=null)
    			{
    			    if(null!=holidayinfo.getId())
    			    {
    				
    				break;
    			    }
    			}
		}
		//holiday check
		//Leave check
		boolean lFo = wamsDao.getLeaveInfoByDate(date, emp.getEmp_id(),2);
		if(lFo)
		{
		    continue;
		}
		//Leave check
		//Absent check
		if(startAtt.getAtt_time()==null)
		{
		    notIn.add(emp);
		}
		//Absent check
		
	    }
	    
	    return notIn;
	}
	
	/*@Override
	public List<HEmployee> getAttendanceOutStatus(String date) {
	    List<HEmployee> notIn = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();
	    
	    String currentDate= date;
	    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
	    Date dt1 = null;
	    try {
		dt1=format1.parse(currentDate);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		
	    String weekDay="";
	    if(dt1!=null)
		weekDay=format2.format(dt1);
	    
	    allEmp = wamsDao.getAllEmployee(false);	   
	    
	    for(HEmployee emp:allEmp)
	    {
		HAttendance startAtt = wamsDao.getDailyAtt(date,0,emp.getEmp_id());
		
		//weekday check
		HWorkInfo info = wamsDao.getWorkInfoById(emp.getEmp_id());
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    for(String w:weekends)
		    {
			if(w.equals(weekDay)){
			   continue;
			}
		    }
		}
		//weekday check
		//holiday check
		HHoliday holidayinfo = wamsDao.getHolidayByDate(date);
		if(holidayinfo!=null)
		{
		    if(null!=holidayinfo.getId())
		    {
			continue;
		    }
		}
		//holiday check
		//Leave check
		
		//Leave check
		//Absent check
		if(startAtt.getAtt_time()==null)
		{
		    notIn.add(emp);
		}
		//Absent check
		
	    }
	    
	    return notIn;
	}*/
	
	@Override
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

	@Override
	public HWorkInfo getActiveWorkInfo(Integer empid) {
	    return wamsDao.getActiveWorkInfo(empid);
	}

	@Override
	public Integer insertHLeaveInfo(HLeave leave) {
	    return wamsDao.insertHLeaveInfo(leave);
	}
	
	@Override
	public Integer insertHOfficeOutgoingInfo(HOfficeOutgoing outgoing) {
		return wamsDao.insertHOfficeOutgoingInfo(outgoing);
	}
	
	@Override
	public List<HMAttendance> getAttendanceByDay(Date date) {
	    List<HMAttendance> todayAtt = new ArrayList<HMAttendance>();
	    
	    //int endDate = endDateofMon(mon,year);
	    Date today = date;
	    SimpleDateFormat dfd = new SimpleDateFormat("dd");
	    int curdd = Integer.parseInt(dfd.format(today));
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curmm = Integer.parseInt(dfm.format(today));
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int curyy = Integer.parseInt(dfy.format(today));
	    
	    /*if((mon==curmm) && (year==curyy) && (endDate>curdd))
	    {
		endDate = curdd;
	    }*/
	    List <HEmployee> allEmp = wamsDao.getAllEmployee(false);
	    
	    for(HEmployee emp:allEmp)
	    {
		String curDate = curyy+"-"+curmm+"-"+curdd;
		//System.out.println(curDate);
		String currentDate= curdd+"-"+curmm+"-"+curyy;
		SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
		Date dt1 = null;
		try {
		    dt1=format1.parse(currentDate);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		
		String weekDay="";
		if(dt1!=null)
		    weekDay=format2.format(dt1);
		    
		//System.out.println(weekDay);
		
		
		HAttendance startAtt = wamsDao.getDailyAtt(curDate,0,emp.getEmp_id());
		HAttendance endAtt = wamsDao.getDailyAtt(curDate,1,emp.getEmp_id());
		
		String dayforshow = curdd+"-"+curmm+"-"+curyy;
		HMAttendance dailyAtt = new HMAttendance();
		dailyAtt.setAtt_date(dayforshow);
		
		if(null != startAtt.getAtt_time())
		    dailyAtt.setAtt_in(changeTimeFormate(startAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_in("");
		
		if(null != endAtt.getAtt_time())
		    dailyAtt.setAtt_out(changeTimeFormate(endAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_out("");
		dailyAtt.setEmp_id(emp.getEmp_id());
		dailyAtt.setEmployee_name(emp.getEmployee_name()+" ("+emp.getAvator()+")");
		HWorkInfo info = wamsDao.getWorkInfoById(emp.getEmp_id(),curDate);
		//satus need to write code
		if(info.getOffice_start()!=null && startAtt.getAtt_time()!=null)
		{
		    float officeStart = info.getOffice_start();
		    //String startTime = ((int) officeStart) + ":"+"00:00";
		    String startTime = getOfficeStart(officeStart);
		    String attTime = startAtt.getAtt_time();
		    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		    Date stdate=null;
		    Date atdate=null;
		    try {
			atdate = formatter.parse(attTime);
			stdate = formatter.parse(startTime);
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		    
		    long diffInMillis =    stdate.getTime() - atdate.getTime();
		    if(diffInMillis<0)
		    {
			long sec = (Math.abs(diffInMillis) /1000);
			long min = 0;
			long hour = 0;
			if(sec>60)
			{
			    min = sec/60;
			    sec = sec%60;
			}
			if(min>60)
			{
			    hour = min/60;
			    min = min%60;
			}
			String stMsg="";
			boolean fo = false;
			if(hour>0){
			    stMsg=stMsg+hour+" Hour ";
			    fo = true;
			}
			if(min>0){
			    if(min==1)
				stMsg=stMsg+min+" Minute ";
			    else
				stMsg=stMsg+min+" Minutes ";
			    fo = true;
			}
			if(sec>0){
			    //stMsg=stMsg+sec+" Second ";
			    //fo = true;
			}
			if(fo)
			    stMsg= stMsg+"Late";
			dailyAtt.setStatus(stMsg);
		    }
		}
		//satus need to write code
		//weekday check
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    for(String w:weekends)
		    {
			if(w.equals(weekDay)){
			    dailyAtt.setWeekend(true);
			    /*if(dailyAtt.getStatus()!=null)
				dailyAtt.setStatus(dailyAtt.getStatus()+" WeekDay");
			    else*/
				dailyAtt.setStatus("WeekDay");
			}
		    }
		}
		//weekday check
		//holiday check
		/*HHoliday holidayinfo = wamsDao.getHolidayByDate(curDate);
		if(holidayinfo!=null)
		{
		    if(null!=holidayinfo.getId())
		    {
			dailyAtt.setHoliday(true);
			if(dailyAtt.getStatus()!=null)
			    dailyAtt.setStatus(dailyAtt.getStatus()+" HoliDay");
			else
			    dailyAtt.setStatus("HoliDay");
		    }
		}*/
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(curDate);
		for(HHoliday holidayinfo:holidayinfoList)
		{
    			if(holidayinfo!=null)
    			{
    			    if(null!=holidayinfo.getId())
    			    {
    				dailyAtt.setHoliday(true);
    				if(dailyAtt.getStatus()!=null)
    				    dailyAtt.setStatus(dailyAtt.getStatus()+" HoliDay");
    				else
    				    dailyAtt.setStatus("HoliDay");
    				break;
    			    }
    			}
		}
		//holiday check
		//Leave check
		boolean lFo = wamsDao.getLeaveInfoByDate(curDate, emp.getEmp_id(),0);
		if(lFo)
		{
		    //dailyAtt.setLeave(true);
		    /*if(dailyAtt.getStatus()!=null){
			if((dailyAtt.isWeekend()==false) && (dailyAtt.isHoliday()==false))
			    dailyAtt.setStatus(dailyAtt.getStatus()+" Leave");
		    } else*/
		    List<HLeave> leaves = wamsDao.getLeaveDataByDate(curDate, emp.getEmp_id());
		    for(HLeave leave:leaves){
			if(leave.getStatus()== 1){
        		    dailyAtt.setLeave(false);
        		    continue;
        		}
        		if(leave.getStatus()== 0){
        		    dailyAtt.setStatus("Applied For Leave");
        		    dailyAtt.setLeave(true);
        		} else if(leave.getStatus()== 2){
        		    dailyAtt.setStatus("Leave");
        		    dailyAtt.setLeave(true);
        		}
		    }
		}
		//Leave check
		//Absent check
		if(startAtt.getAtt_time()==null)
		{
		    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
		    {
			dailyAtt.setAbsent(true);
			if(dailyAtt.getStatus()!=null)
			    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
			else
			    dailyAtt.setStatus("Absent");
		    }
		}
		//Absent check
		todayAtt.add(dailyAtt);
	    }
	    
	    return todayAtt;
	}

	@Override
	public List<String> calculateLeaveDate(String fdate, String tdate, int emp_id, int reqFrom) {
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	    SimpleDateFormat dbformatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date startDate = new Date();
	    Date endDate = new Date();
	    try {
		startDate = formatter.parse(fdate);
		endDate = formatter.parse(tdate);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    	    
	    Calendar start = Calendar.getInstance();
	    start.setTime(startDate);
	    Calendar end = Calendar.getInstance();
	    end.setTime(endDate);
	    end.add(Calendar.DAY_OF_MONTH, 1);
	    
	    List<String> liveDay = new ArrayList<String>();
	    for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
	        	        
	       	SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		
		String weekDay="";
		if(date!=null)
		    weekDay=format2.format(date);
		
		String curDate =formatter.format(date);
		String curDateDb = dbformatter.format(date);
		//weekday check
		boolean wFo = false;
		boolean hFo = false;
		HWorkInfo info = wamsDao.getWorkInfoById(emp_id,curDateDb);
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    
		    for(String w:weekends)
		    {
			if(w.equals(weekDay))
			{
			    wFo = true;
			    break;
			}
		    }
		}
		//weekday check		
		//holiday check
		/*HHoliday holidayinfo = wamsDao.getHolidayByDate(curDateDb);
		if(holidayinfo!=null)
		{
		    if(null!=holidayinfo.getId())
		    {
			hFo = true;
		    }
		}*/
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(curDateDb);
		for(HHoliday holidayinfo:holidayinfoList)
		{
    			if(holidayinfo!=null)
    			{
    			    if(null!=holidayinfo.getId())
    			    {
    				hFo = true;
    				break;
    			    }
    			}
		}
		//holiday check
		
		//Leave check
		if(reqFrom==0){
        		boolean lFo = wamsDao.getLeaveInfoByDate(curDateDb, emp_id, -1);
        		if(lFo)
        		{
        		    liveDay = new ArrayList<String>();
        		    return liveDay;
        		}
		}
		//Leave check
		
		if((!wFo) && (!hFo))
		{
		    liveDay.add(curDate +"   "+weekDay);
		    //System.out.println(date+" "+weekDay+"  "+curDate);
		}
	    }
	    	
	    return liveDay;
	}

	@Override
	public List<HTypesOfLeave> getLeaveType(boolean isDeleted) {
	    return wamsDao.getLeaveType(isDeleted);
	}

	@Override
	public List<HLeave> getAllLeaveByEmpId(boolean isDeleted, int emp_id) {
	    return wamsDao.getAllLeaveByEmpId(isDeleted, emp_id);
	}
	
	@Override
	public List<HOfficeOutgoing> getAllOfficeOutgoingByEmpId(boolean isDeleted, int emp_id){
		return wamsDao.getAllOfficeOutgoingByEmpId(isDeleted, emp_id);
	}
	
	@Override
	public HTypesOfLeave getLeaveTypeById(Integer id) {
	    return wamsDao.getLeaveTypeById(id);
	}

	@Override
	public HHoliday getHolidayById(Integer id) {
	    return wamsDao.getHolidayById(id);
	}

	@Override
	public Integer editHHoliday(HHoliday holiday) {
	    return wamsDao.editHHoliday(holiday);
	}

	@Override
	public List<HLeave> getAllLeaveByApproverId(boolean isDeleted, int approver_id) {
	    
	    List<HLeave> allLeave = wamsDao.getAllLeaveByApproverId(isDeleted, approver_id);
	    
	    List<HLeave> effectiveLeave = new ArrayList<HLeave>();
	    
	    for(HLeave leave:allLeave)
	    {
		if(leave.getApprovar1()==approver_id){
		    if(leave.getApp1state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar2()==approver_id){
		    if(leave.getApp1state() == true && leave.getApp2state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar3()==approver_id){
		    if(leave.getApp1state() == true && leave.getApp2state() == true && leave.getApp3state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar4()==approver_id){
		    if(leave.getApp1state() == true && leave.getApp2state() == true && leave.getApp3state() == true && leave.getApp4state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar5()==approver_id){
		    if(leave.getApp1state() == true && leave.getApp2state() == true && leave.getApp3state() == true && leave.getApp4state() == true && leave.getApp5state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		}
	    }
	    
	    return effectiveLeave;
	}
	
	@Override
	public List<HLeave> getAllLeaveAppliedForApprovedByApproverId(boolean isDeleted, int approver_id) {
	    
	    List<HLeave> allLeave = wamsDao.getAllLeaveByApproverId(isDeleted, approver_id);
	    
	    List<HLeave> effectiveLeave = new ArrayList<HLeave>();
	    
	    for(HLeave leave:allLeave)
	    {
		if(leave.getApprovar1()==approver_id){
		    if(leave.getApp1state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar2()==approver_id){
		    if(leave.getApp2state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar3()==approver_id){
		    if(leave.getApp3state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar4()==approver_id){
		    if(leave.getApp4state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		} else if(leave.getApprovar5()==approver_id){
		    if(leave.getApp5state() == false)
		    {
			effectiveLeave.add(leave);
		    }
		}
	    }
	    
	    return effectiveLeave;
	}
	
	@Override
	public List<HOfficeOutgoing> getAllOfficeOutgoingByApproverId(boolean isDeleted, int approver_id) {
	    
	    List<HOfficeOutgoing> allOutgoing = wamsDao.getAllOfficeOutgoingByApproverId(isDeleted, approver_id);
	    	    
	    List<HOfficeOutgoing> effectiveOutgoing = new ArrayList<HOfficeOutgoing>();
	    
	    for(HOfficeOutgoing outgoing:allOutgoing)
	    {
		if(outgoing.getApprovar1()==approver_id){
		    if(outgoing.getApp1state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar2()==approver_id){
		    if(outgoing.getApp1state() == true && outgoing.getApp2state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar3()==approver_id){
		    if(outgoing.getApp1state() == true && outgoing.getApp2state() == true && outgoing.getApp3state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar4()==approver_id){
		    if(outgoing.getApp1state() == true && outgoing.getApp2state() == true && outgoing.getApp3state() == true && outgoing.getApp4state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar5()==approver_id){
		    if(outgoing.getApp1state() == true && outgoing.getApp2state() == true && outgoing.getApp3state() == true && outgoing.getApp4state() == true && outgoing.getApp5state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		}
	    }
	    
	    return effectiveOutgoing;
	}
	
	@Override
	public List<HOfficeOutgoing> getAllOfficeOutgoingAppliedForApprovedByApproverId(boolean isDeleted, int approver_id) {
	    
		List<HOfficeOutgoing> allOutgoing = wamsDao.getAllOfficeOutgoingByApproverId(isDeleted, approver_id);
	    
		List<HOfficeOutgoing> effectiveOutgoing = new ArrayList<HOfficeOutgoing>();
	    
		for(HOfficeOutgoing outgoing:allOutgoing)
	    {
		if(outgoing.getApprovar1()==approver_id){
		    if(outgoing.getApp1state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar2()==approver_id){
		    if(outgoing.getApp2state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar3()==approver_id){
		    if(outgoing.getApp3state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar4()==approver_id){
		    if(outgoing.getApp4state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		} else if(outgoing.getApprovar5()==approver_id){
		    if(outgoing.getApp5state() == false)
		    {
		    	effectiveOutgoing.add(outgoing);
		    }
		}
	    }
	    
	    return effectiveOutgoing;
	}

	@Override
	public HLeave getLeaveById(Integer id) {
	    return wamsDao.getLeaveById(id);
	}
	
	@Override
	public HOfficeOutgoing getOfficeOutgoingById(Integer id){
		return wamsDao.getOfficeOutgoingById(id);
	}

	@Override
	public Integer editHEmployeePass(HEmployee emp) {
	    return wamsDao.editHEmployeePass(emp);
	}

	@Override
	public Integer approveRejectLeave(HLeaveStatus status,int finalapp) {
	    return wamsDao.approveRejectLeave(status,finalapp);
	}
	
	@Override
	public Integer approveRejectOfficeOutgoing(HOfficeOutgoingStatus status, int finalapp){
		return wamsDao.approveRejectOfficeOutgoing(status, finalapp);
	}
	
	@Override
	public List<HLeaveStatus> getAllLeaveStatusByLeaveId(boolean isDeleted, int leaveId) {
	    return wamsDao.getAllLeaveStatusByLeaveId(isDeleted,leaveId);
	}
	
	@Override
	public List<HOfficeOutgoingStatus> getAllOfficeOutgoingStatusByOutgoingId(boolean isDeleted, int outgoingId){
		return wamsDao.getAllOfficeOutgoingStatusByOutgoingId(isDeleted, outgoingId);
	}

	@Override
	public Integer insertHLeaveApprover(List<HLeaveApprover> approvers) {
	    return wamsDao.insertHLeaveApprover(approvers);
	}

	@Override
	public List<HLeaveApprover> getLeaveApprovers(boolean isDeleted, int emp_id) {
	    return wamsDao.getLeaveApprovers(isDeleted, emp_id);
	}
	
	@Override
	public Integer insertHOfficeOutgoingApprover(List<HOfficeOutgoingApprover> approvers){
		return wamsDao.insertHOfficeOutgoingApprover(approvers);
	}
	
	@Override
	public List<HOfficeOutgoingApprover> getOfficeOutgoingApprovers(boolean isDeleted, int emp_id){
		return wamsDao.getOfficeOutgoingApprovers(isDeleted, emp_id);
	}

	@Override
	public Integer editHLeave(HLeave leave) {
	    return wamsDao.editHLeave(leave);
	}
	
	@Override
	public Integer editHOfficeOutgoing(HOfficeOutgoing outgoing){
		return wamsDao.editHOfficeOutgoing(outgoing);
	}

	@Override
	public List<HEmployee> getEmpNotGivenSignout(String date) {
	    
	    List<HEmployee> allEmp = wamsDao.getAllEmployee(false);
	    List<HEmployee> notGivenSignOutEmp = new ArrayList<HEmployee>(); 
	    for(HEmployee employee:allEmp)
	    {
		HAttendance startAtt = wamsDao.getDailyAtt(date,0,employee.getEmp_id());
		HAttendance endAtt = wamsDao.getDailyAtt(date,1,employee.getEmp_id());
		//Leave check
		boolean lFo = wamsDao.getLeaveInfoByDate(date, employee.getEmp_id(),2);
		//Leave check		
		if((null!=startAtt.getAtt_id()) && (null==endAtt.getAtt_id()) && (!lFo))
		{
		    notGivenSignOutEmp.add(employee);
		}
	    }
	    return notGivenSignOutEmp;
	}
	
	private String changeTimeFormate(String input)
	{
	    String output="";
	    try{
		DateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
		output = outputFormat.format(inputFormat.parse(input));
	    } catch (ParseException e1) {
		e1.printStackTrace();
	    }
	    return output;
	}
	
	public String changeReverseTimeFormate(String input)
	{
	    String output="";
	    try{
		DateFormat inputFormat = new SimpleDateFormat("KK:mm a");
		DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
		output = outputFormat.format(inputFormat.parse(input));
	    } catch (ParseException e1) {
		e1.printStackTrace();
	    }
	    return output;
	}

	@Override
	public Integer insertHLunch(HLunch lunch) {
	    return wamsDao.insertHLunch(lunch);
	}
		
	@Override
	public Integer insertHLunch(StringBuffer sql) {
	    return wamsDao.insertHLunch(sql);
	}
	
	@Override
	public HLunch getLunchByEmp(String date,Integer emp_id) {
	    return wamsDao.getLunchByEmp(date,emp_id);
	}

	@Override
	public List<HLunch> getLunchListByDate(boolean isDeleted, boolean isCancel, String date) {
	    return wamsDao.getLunchListByDate(isDeleted,isCancel,date);
	}

	@Override
	public Integer editHLunch(HLunch lunch) {
	    return wamsDao.editHLunch(lunch);
	}

	@Override
	public List<HMonthlyStatus> getEmployeeMonthlyStatus(int startDate, int mon, int year) {
	    List<HEmployee> aEmp = getAllEmployee(false);
	    List<HMonthlyStatus> monthlyStates = new ArrayList<HMonthlyStatus>();
	    for(HEmployee curEmp:aEmp)
	    {
		HMonthlyStatus monthlyState = new HMonthlyStatus();
		int emp_id = curEmp.getEmp_id();
		monthlyState.setEmp_id(emp_id);
		monthlyState.setEmployee_name(curEmp.getEmployee_name()+" ("+curEmp.getAvator()+")");
		monthlyState.setMonth(String.valueOf(mon));
		monthlyState.setYear(String.valueOf(year));
		List<HMAttendance> thisMonEmpAtt = getAttendanceForMonth(mon,year, emp_id,false);
		int present = 0;
		int  absent = 0;
		int leave = 0;
		int lwp = 0;
		int late = 0;
		int totalday = 0;
		long totalWorkHour = 0;
		long absulateWorkHour = 0;
		long totalOvertime = 0;
		long deficit = 0;
		long holidayOvertime = 0;
		float workingTime = 0;
		int lunchExcluded = 0;
		
		long presentWorkingHours =0;
		
		for(HMAttendance thisMonAtt:thisMonEmpAtt){
		    int attday = getDay(thisMonAtt.getAtt_date());
		    
		    if((attday==6) && (curEmp.getEmp_id()==79)){
			System.out.println(curEmp.getEmp_number());
			System.out.println(attday);
		    }
		    
		    HWorkInfo winfo = getWorkInfoById(emp_id,changetDateFormate(thisMonAtt.getAtt_date()));
		    if(null==winfo.getOffice_start()){
			continue;
		    }
		    String workStartTime = getOfficeStart(winfo.getOffice_start());
		    workingTime = winfo.getWorking_hour();
		    lunchExcluded = winfo.getLunchExclude();
		    if(attday>=startDate)
		    {
			if(!thisMonAtt.isAbsent() && !thisMonAtt.isLeave() && !thisMonAtt.isHoliday() && !thisMonAtt.isWeekend()){
			    present = present + 1;
			    if(lunchExcluded==1){
				presentWorkingHours = presentWorkingHours + ((long) ((workingTime)*60*60*1000));
			    } else{
				presentWorkingHours = presentWorkingHours + ((long) ((workingTime-1)*60*60*1000));
			    }
        		} else if(thisMonAtt.isAbsent()){
        		    absent = absent + 1;
        		} else if(thisMonAtt.isLeave() && thisMonAtt.isLwp()) {
        		    lwp = lwp + 1;
        		} else if(thisMonAtt.isLeave() && (!thisMonAtt.isLwp())) {
        		    leave = leave + 1;
        		} 
        			
        		if(thisMonAtt.isLate() && (!thisMonAtt.isLeave()) && (!thisMonAtt.isHoliday()) && (!thisMonAtt.isWeekend())) {
        		    late = late + 1;
        		}
        			
        		if(!thisMonAtt.isHoliday() && !thisMonAtt.isWeekend()){
        		    totalday = totalday +1;
        		}
		    }
		    //////////////////////
		    if((!thisMonAtt.isHoliday()) && (!thisMonAtt.isWeekend()))
		    {
			String start = "",end="",usalstart="";
			usalstart = thisMonAtt.getAtt_date()+" "+workStartTime;//changeReverseTimeFormate("09:00 AM");
	    	    	if(!thisMonAtt.getAtt_in().equals("")){
	    	    	    start = thisMonAtt.getAtt_date()+" "+changeReverseTimeFormate(thisMonAtt.getAtt_in());
	    	    	}
	    	    	if(!thisMonAtt.getAtt_out().equals("")){
	    	    	    end = thisMonAtt.getAtt_date()+" "+changeReverseTimeFormate(thisMonAtt.getAtt_out());
	    	    	}
	    	    
	    	    	if((!start.equals("")) && (!end.equals("")))
	    	    	{
	    	    	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	    	    	    Date d1 = null;
	    	    	    Date d2 = null;
	    	    	    Date u1 = null;
	    	    	    /*if((emp_id==1) && (start.startsWith("14"))){
				System.out.println("tt "+emp_id);
	    	    	    }*/
	    	    	    try {
	    	    		d1 = format.parse(start);
	    	    		d2 = format.parse(end);
	    	    		u1 = format.parse(usalstart);

	    	    		//in milliseconds
	    	    		long diff = 0;
	    	    		if(d1.getTime()<u1.getTime()){
	    	    		    diff = d2.getTime() - u1.getTime();
	    	    		} else{
	    	    		    diff = d2.getTime() - d1.getTime();
	    	    		}
	    	    		long standardWorkTime = (long) (workingTime*60*60*1000);
	    	    		long lunchTime = 1*60*60*1000;
	    	    		long overtime = 0;
	    	    		if(diff>standardWorkTime){
	    	    		    overtime = diff - standardWorkTime;
	    	    		}
	    	    		if(diff<=((standardWorkTime-1)/2))
	    	    		{
	    	    		    totalWorkHour = totalWorkHour+diff;
	    	    		    absulateWorkHour = absulateWorkHour + (diff-overtime);
	    	    		} else{
	    	    		    if(lunchExcluded==1){
	    	    			totalWorkHour = totalWorkHour+(diff);
	    	    			absulateWorkHour = absulateWorkHour + ((diff)-overtime);
	    	    		    } else{
	    	    			totalWorkHour = totalWorkHour+(diff-lunchTime);
	    	    			absulateWorkHour = absulateWorkHour + ((diff-lunchTime)-overtime);
	    	    		    }
	    	    		}
	    	    		
	    	    		
	    	    		
	    	    		totalOvertime = totalOvertime+overtime;
	    	    		//System.out.println(totalWorkHour);
	    	    	    } catch (Exception e) {
	    	    		e.printStackTrace();
	    	    	    }
	    	    	}
		    }
		    else{
			String start = "",end="",usalstart="";
			usalstart = thisMonAtt.getAtt_date()+" "+workStartTime;//changeReverseTimeFormate("09:00 AM");
	    	    	if(!thisMonAtt.getAtt_in().equals("")){
	    	    	    start = thisMonAtt.getAtt_date()+" "+changeReverseTimeFormate(thisMonAtt.getAtt_in());
	    	    	}
	    	    	if(!thisMonAtt.getAtt_out().equals("")){
	    	    	    end = thisMonAtt.getAtt_date()+" "+changeReverseTimeFormate(thisMonAtt.getAtt_out());
	    	    	}
	    	    
	    	    	if((!start.equals("")) && (!end.equals("")))
	    	    	{
	    	    	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	    	    	    Date d1 = null;
	    	    	    Date d2 = null;
	    	    	    Date u1 = null;
	    	    	    /*if((emp_id==1) && (start.startsWith("14"))){
				System.out.println("tt "+emp_id);
	    	    	    }*/
	    	    	    try {
	    	    		d1 = format.parse(start);
	    	    		d2 = format.parse(end);
	    	    		u1 = format.parse(usalstart);

	    	    		//in milliseconds
	    	    		long diff = 0;
	    	    		if(d1.getTime()<u1.getTime()){
	    	    		    diff = d2.getTime() - u1.getTime();
	    	    		} else{
	    	    		    diff = d2.getTime() - d1.getTime();
	    	    		}
	    	    		long standardWorkTime = (long) (workingTime*60*60*1000);
	    	    		long lunchTime = 1*60*60*1000;
	    	    		long overtime = 0;
	    	    		if(diff<=((standardWorkTime-1)/2)){
	    	    		    overtime = diff;
	    	    		}
	    	    		else{
	    	    		    overtime = diff - lunchTime;
	    	    		}
	    	    		
	    	    		holidayOvertime = holidayOvertime+overtime;
	    	    		//System.out.println(totalWorkHour);
	    	    	    } catch (Exception e) {
	    	    		e.printStackTrace();
	    	    	    }
	    	    	}
		    }
		    //////////////////////
		    
		}
		///////////////////////////////////Bug Need to Fix///////////////////////
		//long presentWorkingHours = (long) (present*(workingTime-1)*60*60*1000);
		if((absulateWorkHour<presentWorkingHours) && (totalOvertime>0)){
		    long hourdiff = presentWorkingHours-absulateWorkHour;
		    if(totalOvertime>=hourdiff){
			totalOvertime = totalOvertime - hourdiff;
			absulateWorkHour = absulateWorkHour + hourdiff;
		    }
		    else if(totalOvertime<hourdiff){
			absulateWorkHour = absulateWorkHour + totalOvertime;
			totalOvertime = 0;
		    }
		}
		
		if(totalOvertime>0){
		    deficit = 0;
		} else{
		    deficit = presentWorkingHours - totalWorkHour;
		}
		monthlyState.setPresentWorkingHours(convertLongToTime(presentWorkingHours));
		monthlyState.setTotalworkinghours(convertLongToTime(totalWorkHour));
		monthlyState.setAbsulateworkinghours(convertLongToTime(absulateWorkHour));
		monthlyState.setOvertimehours(convertLongToTime(totalOvertime));
		monthlyState.setDeficithours(convertLongToTime(deficit));
		monthlyState.setHolidayovertime(convertLongToTime(holidayOvertime));
		monthlyState.setAbsent(absent);
		monthlyState.setPresent(present);
		monthlyState.setLeave(leave);
		monthlyState.setLwp(lwp);
		monthlyState.setLate(late);
		monthlyState.setTotal_working_day(totalday);
		
		monthlyStates.add(monthlyState);
	    }
	    
	    boolean fo = wamsDao.insertHMonthlyStatus(monthlyStates);
	    
	    return monthlyStates;
	}
	
	public Integer getDay(String dt) {//25-01-2016  01   output 01
	    String formatedDate = "";

	    String[] splitdt = dt.split("-");
	    formatedDate = splitdt[0];
		
	    int day = Integer.parseInt(formatedDate);

	    return day;

	}

	@Override
	public HLeave getLeaveByEmpIdAndDate(Integer empid, String date,int status) {
	    return wamsDao.getLeaveByEmpIdAndDate(empid,date,status);
	}

	@Override
	public Integer sendMail(HMail mail) {
	    return wamsDao.sendMail(mail);
	}

	@Override
	public List<HMail> getMail(boolean isDeleted, String state) {
	    return wamsDao.getMail(isDeleted, state);
	}

	@Override
	public Integer editHMail(HMail mail) {
	    return wamsDao.editHMail(mail);
	}
	
	private String getOfficeStart(float st)
	{
	    String sttime="";
	    if(st==8)
		sttime="08:00:00";
	    else if(st==8.5)
		sttime="08:30:00";
	    else if(st==9)
		sttime="09:00:00";
	    else if(st==9.5)
		sttime="09:30:00";
	    else if(st==10)
		sttime="10:00:00";
	    else if(st==10.5)
		sttime="10:30:00";
	    else if(st==11)
		sttime="11:00:00";
	    else if(st==11.5)
		sttime="11:30:00";
	    else if(st==12)
		sttime="12:00:00";
	    else if(st==12.5)
		sttime="12:30:00";
	    else if(st==13)
		sttime="13:00:00";
	    else if(st==13.5)
		sttime="13:30:00";
	    else if(st==14)
		sttime="14:00:00";
	    else if(st==14.5)
		sttime="14:30:00";
	    else if(st==15)
		sttime="15:00:00";
	    else if(st==15.5)
		sttime="15:30:00";
	    else if(st==16)
		sttime="16:00:00";
	    else if(st==16.5)
		sttime="16:30:00";
	    else if(st==17)
		sttime="17:00:00";
	    else if(st==17.5)
		sttime="17:30:00";
	    else if(st==18)
		sttime="18:00:00";
	    else if(st==18.5)
		sttime="18:30:00";
	    else if(st==19)
		sttime="19:00:00";
	    else if(st==19.5)
		sttime="19:30:00";
	    else if(st==20)
		sttime="20:00:00";
	    else if(st==20.5)
		sttime="20:30:00";
	    else if(st==21)
		sttime="21:00:00";
	    else if(st==21.5)
		sttime="21:30:00";
	    else if(st==22)
		sttime="22:00:00";
	    else if(st==22.5)
		sttime="22:30:00";
	    
	    return sttime;
	    
	}

	@Override
	public HWorkInfo getWorkInfoById(Integer id,String date) {
	    return wamsDao.getWorkInfoById(id,date);
	}

	@Override
	public List<HTeam> getAllTeam(boolean isDeleted) {
	    return wamsDao.getAllTeam(isDeleted);
	}

	@Override
	public HTeam getTeamById(int id) {
	    return wamsDao.getTeamById(id);
	}

	@Override
	public List<HLeave> getAllApprovedLeaveByApproverId(boolean isDeleted, int approver_id) {
	    return wamsDao.getAllApprovedLeaveByApproverId(isDeleted, approver_id);
	}
	
	@Override
	public List<HLunch> getLunchForMonth(int mon, int year, int emp_id,boolean isCurdatechk) {
	    //List<HLunch> monLunchTemp = new ArrayList<HLunch>();
	    List<HLunch> monLunch = new ArrayList<HLunch>();
	    	    
	    int endDate = endDateofMon(mon,year);
	    Date today = new Date();
	    SimpleDateFormat dfd = new SimpleDateFormat("dd");
	    int curdd = Integer.parseInt(dfd.format(today));
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curmm = Integer.parseInt(dfm.format(today));
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int curyy = Integer.parseInt(dfy.format(today));
	    
	    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    
	    if((!isCurdatechk) && (mon==curmm) && (year==curyy) && (endDate>curdd))
	    {
		endDate = curdd;
	    }
	    //boolean dataFound = false;
	    //String startDate= year+"-"+mon+"-01";
	    //String lastDate= year+"-"+mon+"-"+endDate;
	    
	    //monLunchTemp = wamsDao.getLunchListByEmp(false, startDate, lastDate, emp_id);
	    
	    for(int dd=1;dd<=endDate;dd++)
	    {
		String curDate= year+"-"+mon+"-"+dd;
		String curDateForView = dd+"-"+mon+"-"+year;
		HLunch todayLunch = new HLunch();
		
		Date dt1 = null;
		try {
		    dt1=df.parse(curDateForView);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		
		todayLunch.setLunch_date(df.format(dt1));
		todayLunch.setLunch_count(0);
		todayLunch.setLunch_status(false);
		
		HLunch lunch = wamsDao.getLunchByEmp(curDate,emp_id);
		if(lunch!=null)
		{
		    todayLunch = lunch;
		}
		monLunch.add(todayLunch);
	    }
	    return monLunch;
	}

	@Override
	public List<HLeave> getAllLeaveForMonth(boolean isDeleted, int mon, int year, int status) {
		
		int endDate = endDateofMon(mon,year);
		String startDate = year+"-"+mon+"-"+"01";
		String lastDate = year+"-"+mon+"-"+endDate;
		
		return wamsDao.getAllLeaveForMonth(isDeleted, startDate, lastDate, status);
	}
	
	@Override
	public List<HLeave> getAllLeaveForMonth(boolean isDeleted, int mon, int year, String status) {
		
		int endDate = endDateofMon(mon,year);
		String startDate = year+"-"+mon+"-"+"01";
		String lastDate = year+"-"+mon+"-"+endDate;
		
		return wamsDao.getAllLeaveForMonth(isDeleted, startDate, lastDate, status);
	}
	
	public Hashtable getEmployeeAbsendList(int mon, int year)
	{
	    List<HEmployee> emps = getAllEmployee(false);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    Hashtable absent = new Hashtable();
	    for(HEmployee emp:emps)
	    {
		List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
		thisMonAtt = getAttendanceForMonth(mon,year, emp.getEmp_id(),false);
		HWorkInfo joinInfo = getJoiningWorkInfoById(emp.getEmp_id());
		List<String> absentDate = new ArrayList<String>();
		if(!thisMonAtt.isEmpty()){
		    try {
			Date date1 = sdf.parse(joinInfo.getFrom_date());
			for(HMAttendance monthlyAtt:thisMonAtt){
			    Date date2 = sdf.parse(monthlyAtt.getAtt_date());
			    
			    if((date1.compareTo(date2)<0) && (monthlyAtt.isAbsent()==true))
			    {
				absentDate.add(monthlyAtt.getAtt_date());
			    }
			}
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		}
		absent.put(emp.getEmp_id(), absentDate);
	    }
	    return absent;
	}
	
	@Override
	public List<HLeave> getAllLeaveBetweenDates(boolean isDeleted, String sdate, String edate, int status) {
		return wamsDao.getAllLeaveForMonth(isDeleted, sdate, edate, status);
	}

	/* (non-Javadoc)
	 * @see com.WAMS.ams.services.interfaces.IWamsService#getEmployeeYearlyLeave(int)
	 */
	@Override
	public List<HYearlyLeave> getEmployeeYearlyLeave(int year) {
	    List<HYearlyLeave> empYearlyLeaveState = new ArrayList<HYearlyLeave>();
	    List<HEmployee> allEmp = wamsDao.getAllEmployee(false);
	    
	    //List<HLeave> leaves = wamsDao.getLeaveDataByDate(curDate, emp.getEmp_id());
	    
	    List<HLeave> leaveForJul = getAllLeaveForMonth(false, 7, year, "0,2");
	    Hashtable absentForJul= getEmployeeAbsendList(7, year);
	    
	    List<HLeave> leaveForAug = getAllLeaveForMonth(false, 8, year, "0,2");
	    Hashtable absentForAug= getEmployeeAbsendList(8, year);
	    
	    List<HLeave> leaveForSep = getAllLeaveForMonth(false, 9, year, "0,2");
	    Hashtable absentForSep= getEmployeeAbsendList(9, year);
	    
	    List<HLeave> leaveForOct = getAllLeaveForMonth(false, 10, year, "0,2");
	    Hashtable absentForOct= getEmployeeAbsendList(10, year);
	    
	    List<HLeave> leaveForNov = getAllLeaveForMonth(false, 11, year, "0,2");
	    Hashtable absentForNov= getEmployeeAbsendList(11, year);
	    
	    List<HLeave> leaveForDec = getAllLeaveForMonth(false, 12, year, "0,2");
	    Hashtable absentForDec= getEmployeeAbsendList(12, year);
	    
	    List<HLeave> leaveForJan = getAllLeaveForMonth(false, 1, year+1, "0,2");
	    Hashtable absentForJan= getEmployeeAbsendList(1, year+1);
	    
	    List<HLeave> leaveForFeb = getAllLeaveForMonth(false, 2, year+1, "0,2");
	    Hashtable absentForFeb= getEmployeeAbsendList(2, year+1);
	    
	    List<HLeave> leaveForMar = getAllLeaveForMonth(false, 3, year+1, "0,2");
	    Hashtable absentForMar= getEmployeeAbsendList(3, year+1);
	    
	    List<HLeave> leaveForApr = getAllLeaveForMonth(false, 4, year+1, "0,2");
	    Hashtable absentForApr= getEmployeeAbsendList(4, year+1);
	    
	    List<HLeave> leaveForMay = getAllLeaveForMonth(false, 5, year+1, "0,2");
	    Hashtable absentForMay= getEmployeeAbsendList(5, year+1);
	    
	    List<HLeave> leaveForJun = getAllLeaveForMonth(false, 6, year+1, "0,2");
	    Hashtable absentForJun= getEmployeeAbsendList(6, year+1);
	    
	    for(HEmployee emp:allEmp){
		HYearlyLeave yleave = new HYearlyLeave();
		yleave.setEmp_id(emp.getEmp_id());
		yleave.setEmployee_name(emp.getEmployee_name());
		int sum_casual = 0,sum_sick=0,sum_lwp=0;
		for(HLeave leave:leaveForJan){
		    if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"01",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"01",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"01",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforJan = (List<String>) absentForJan.get(emp.getEmp_id());
		int absentsum = absforJan.size();
		Integer lvjan[]=new Integer[4];
		lvjan[0]=sum_casual;
		lvjan[1]=sum_sick;
		lvjan[2]=sum_lwp;
		lvjan[3]=absentsum;
		yleave.setJan(lvjan);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForFeb){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"02",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"02",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"02",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforFeb = (List<String>) absentForFeb.get(emp.getEmp_id());
		absentsum = absforFeb.size();
		Integer lvfeb[]=new Integer[4];
		lvfeb[0]=sum_casual;
		lvfeb[1]=sum_sick;
		lvfeb[2]=sum_lwp;
		lvfeb[3]=absentsum;
		yleave.setFeb(lvfeb);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForMar){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"03",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"03",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"03",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforMar = (List<String>) absentForMar.get(emp.getEmp_id());
		absentsum = absforMar.size();
		Integer lvmar[]=new Integer[4];
		lvmar[0]=sum_casual;
		lvmar[1]=sum_sick;
		lvmar[2]=sum_lwp;
		lvmar[3]=absentsum;
		yleave.setMar(lvmar);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForApr){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"04",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"04",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"04",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforApr = (List<String>) absentForApr.get(emp.getEmp_id());
		absentsum = absforApr.size();
		Integer lvapr[]=new Integer[4];
		lvapr[0]=sum_casual;
		lvapr[1]=sum_sick;
		lvapr[2]=sum_lwp;
		lvapr[3]=absentsum;
		yleave.setApr(lvapr);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForMay){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"05",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"05",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"05",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforMay = (List<String>) absentForMay.get(emp.getEmp_id());
		absentsum = absforMay.size();
		Integer lvmay[]=new Integer[4];
		lvmay[0]=sum_casual;
		lvmay[1]=sum_sick;
		lvmay[2]=sum_lwp;
		lvmay[3]=absentsum;
		yleave.setMay(lvmay);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForJun){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"06",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"06",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"06",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforJun = (List<String>) absentForJun.get(emp.getEmp_id());
		absentsum = absforJun.size();
		Integer lvjun[]=new Integer[4];
		lvjun[0]=sum_casual;
		lvjun[1]=sum_sick;
		lvjun[2]=sum_lwp;
		lvjun[3]=absentsum;
		yleave.setJun(lvjun);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForJul){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"07",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"07",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"07",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforJul = (List<String>) absentForJul.get(emp.getEmp_id());
		absentsum = absforJul.size();
		Integer lvjul[]=new Integer[4];
		lvjul[0]=sum_casual;
		lvjul[1]=sum_sick;
		lvjul[2]=sum_lwp;
		lvjul[3]=absentsum;
		yleave.setJul(lvjul);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForAug){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"08",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"08",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"08",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforAug = (List<String>) absentForAug.get(emp.getEmp_id());
		absentsum = absforAug.size();
		Integer lvaug[]=new Integer[4];
		lvaug[0]=sum_casual;
		lvaug[1]=sum_sick;
		lvaug[2]=sum_lwp;
		lvaug[3]=absentsum;
		yleave.setAug(lvaug);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForSep){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"09",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"09",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"09",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforSep = (List<String>) absentForSep.get(emp.getEmp_id());
		absentsum = absforSep.size();
		Integer lvsep[]=new Integer[4];
		lvsep[0]=sum_casual;
		lvsep[1]=sum_sick;
		lvsep[2]=sum_lwp;
		lvsep[3]=absentsum;
		yleave.setSep(lvsep);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForOct){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"10",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"10",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"10",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforOct = (List<String>) absentForOct.get(emp.getEmp_id());
		absentsum = absforOct.size();
		Integer lvoct[]=new Integer[4];
		lvoct[0]=sum_casual;
		lvoct[1]=sum_sick;
		lvoct[2]=sum_lwp;
		lvoct[3]=absentsum;
		yleave.setOct(lvoct);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForNov){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"11",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"11",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"11",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforNov = (List<String>) absentForNov.get(emp.getEmp_id());
		absentsum = absforNov.size();
		Integer lvnov[]=new Integer[4];
		lvnov[0]=sum_casual;
		lvnov[1]=sum_sick;
		lvnov[2]=sum_lwp;
		lvnov[3]=absentsum;
		yleave.setNov(lvnov);
		
		sum_casual = 0;sum_sick=0;sum_lwp=0;
		for(HLeave leave:leaveForDec){
			if(leave.getEmp_id()==emp.getEmp_id()){
		    	if(leave.getLeavetype().getId()==1){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"12",emp.getEmp_id());
		    		sum_casual = sum_casual + days;
		    	} else if(leave.getLeavetype().getId()==2){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"12",emp.getEmp_id());
		    		sum_sick = sum_sick + days;
		    	} else if(leave.getLeavetype().getId()==3){
		    		int days = noOfDays(leave.getFdate(),leave.getTdate(),"12",emp.getEmp_id());
		    		sum_lwp = sum_lwp + days;
		    	} 
		    }
		}
		List<String> absforDec = (List<String>) absentForDec.get(emp.getEmp_id());
		absentsum = absforDec.size();
		Integer lvdec[]=new Integer[4];
		lvdec[0]=sum_casual;
		lvdec[1]=sum_sick;
		lvdec[2]=sum_lwp;
		lvdec[3]=absentsum;
		yleave.setDec(lvdec);
		
		empYearlyLeaveState.add(yleave);
	    }
	    
	    return empYearlyLeaveState;
	}
	
	public int noOfDays(String fdate,String tdate,String mon,int emp_id){
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	    SimpleDateFormat dbformatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date startDate = new Date();
	    Date endDate = new Date();
	    try {
		startDate = formatter.parse(fdate);
		endDate = formatter.parse(tdate);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    	    
	    Calendar start = Calendar.getInstance();
	    start.setTime(startDate);
	    Calendar end = Calendar.getInstance();
	    end.setTime(endDate);
	    end.add(Calendar.DAY_OF_MONTH, 1);
	    
	    int dayCount = 0;
	    
	    for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
	 	SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		
		String weekDay="";
		if(date!=null)
		    weekDay=format2.format(date);
			
		String curDate =formatter.format(date);
		String curDateDb = dbformatter.format(date);
		//weekday check
		boolean wFo = false;
		boolean hFo = false;
		HWorkInfo info = wamsDao.getWorkInfoById(emp_id,curDateDb);
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    
		    for(String w:weekends)
		    {
			if(w.equals(weekDay))
			{
			    wFo = true;
			    break;
			}
		    }
		}
		
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(curDateDb);
		for(HHoliday holidayinfo:holidayinfoList)
		{
	    		if(holidayinfo!=null)
	    		{
	    		    if(null!=holidayinfo.getId())
	    		    {
	    			hFo = true;
	    			break;
	    		    }
	    		}
		}
		//holiday check
					
		if((!wFo) && (!hFo))
		{
		    SimpleDateFormat formatterMon = new SimpleDateFormat("MM");
		    String leavemon = formatterMon.format(date);
		    if(leavemon.equals(mon)){
			dayCount++;
		    }
		}
	    }
	    
	    return dayCount;
	}
	
	@Override
	public List<HMAttendance> getEmployeeStatusForMonth(int mon, int year, int emp_id,boolean isCurdatechk,String call) {
	    List<HMAttendance> monAtt = new ArrayList<HMAttendance>();
	    List<HMAttendance> blankmonAtt = new ArrayList<HMAttendance>();
	   
	    int endDate = endDateofMon(mon,year);
	    Date today = new Date();
	    SimpleDateFormat dfd = new SimpleDateFormat("dd");
	    int curdd = Integer.parseInt(dfd.format(today));
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curmm = Integer.parseInt(dfm.format(today));
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int curyy = Integer.parseInt(dfy.format(today));
	    
	    if((!isCurdatechk) && (mon==curmm) && (year==curyy) && (endDate>curdd))
	    {
		endDate = curdd;
	    }
	    boolean dataFound = false;
	    for(int dd=1;dd<=endDate;dd++)
	    {
		String curDate = year+"-"+mon+"-"+dd;
		//System.out.println(curDate);
		String currentDate= dd+"-"+mon+"-"+year;
		SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
		Date dt1 = null;
		try {
		    dt1=format1.parse(currentDate);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		SimpleDateFormat format2=new SimpleDateFormat("EEEE");
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		String weekDay="";
		if(dt1!=null)
		    weekDay=format2.format(dt1);
		    
		//System.out.println(weekDay);
		
		
		HAttendance startAtt = wamsDao.getDailyAtt(curDate,0,emp_id);
		HAttendance endAtt = wamsDao.getDailyAtt(curDate,1,emp_id);
		
		String dayforshow = dd+"-"+mon+"-"+year;
		HMAttendance dailyAtt = new HMAttendance();
		dailyAtt.setAtt_date(dayforshow);
		if(null != startAtt.getAtt_time())
		    dailyAtt.setAtt_in(changeTimeFormate(startAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_in("");
		
		if(null != endAtt.getAtt_time())
		    dailyAtt.setAtt_out(changeTimeFormate(endAtt.getAtt_time()));
		else
		    dailyAtt.setAtt_out("");
		
		dailyAtt.setEmp_id(emp_id);
		dailyAtt.setEmployee_name(wamsDao.getEmployeeById(emp_id).getEmployee_name());
		
		HWorkInfo info = wamsDao.getWorkInfoById(emp_id,curDate);
		//satus need to write code for Late
		if(info.getOffice_start()!=null && startAtt.getAtt_time()!=null)
		{
		    float officeStart = info.getOffice_start();
		    //String startTime = ((int) officeStart) + ":"+"00:00";
		    String startTime = getOfficeStart(officeStart);
		    String attTime = startAtt.getAtt_time();
		    
		    Date stdate=null;
		    Date atdate=null;
		    try {
			atdate = formatter.parse(attTime);
			stdate = formatter.parse(startTime);
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		    
		    long diffInMillis =    stdate.getTime() - atdate.getTime();
		    if(diffInMillis<0)
		    {
			long sec = (Math.abs(diffInMillis) /1000);
			long min = 0;
			long hour = 0;
			if(sec>60)
			{
			    min = sec/60;
			    sec = sec%60;
			}
			if(min>60)
			{
			    hour = min/60;
			    min = min%60;
			}
			String stMsg="";
			boolean fo = false;
			if(hour>0){
			    stMsg=stMsg+hour+" Hour ";
			    fo = true;
			}
			if(min>0){
			    if(min==1)
				stMsg=stMsg+min+" Minute ";
			    else
				stMsg=stMsg+min+" Minutes ";
			    fo = true;
			}
			if(sec>0){
			    //stMsg=stMsg+sec+" Second ";
			    //fo = true;
			}
			if(fo){
			    stMsg= stMsg+"Late";
			    dailyAtt.setLate(true);
			}
			dailyAtt.setStatus(stMsg);
		    }
		    dataFound = true;
		}
		//satus need to write code for Late
		//weekday check
		if(info.getWeekend()!=null)
		{
		    String weekends[] = info.getWeekend().split(",");
		    for(String w:weekends)
		    {
			if(w.equals(weekDay)){
			    dailyAtt.setWeekend(true);
			    /*if(dailyAtt.getStatus()!=null)
				dailyAtt.setStatus(dailyAtt.getStatus()+" WeekDay");
			    else*/
				dailyAtt.setStatus("WeekDay");
			}
		    }
		}
		//weekday check		
		//holiday check
		List<HHoliday> holidayinfoList = wamsDao.getHolidayListByDate(curDate);
		for(HHoliday holidayinfo:holidayinfoList)
		{
    			if(holidayinfo!=null)
    			{
    			    if(null!=holidayinfo.getId())
    			    {
    				dailyAtt.setHoliday(true);
    				if(dailyAtt.getStatus()!=null)
    				    dailyAtt.setStatus(dailyAtt.getStatus()+" HoliDay");
    				else
    				    dailyAtt.setStatus("HoliDay");
    				break;
    			    }
    			}
		}
		//holiday check
		//Leave check
		boolean lFo = false;
		lFo=wamsDao.getLeaveInfoByDate(curDate,emp_id,0);
		if(lFo)
		{
		    //dailyAtt.setLeave(true);
		    /*if(dailyAtt.getStatus()!=null)
			dailyAtt.setStatus(dailyAtt.getStatus()+" Leave");
		    else*/
		    List<HLeave> leaves = wamsDao.getLeaveDataByDate(curDate, emp_id);
		    for(HLeave leave:leaves){
			if(leave.getStatus()== 1){
        		    dailyAtt.setLeave(false);
        		    continue;
        		}
        		if(leave.getStatus()== 0){
        		    dailyAtt.setStatus("Applied For Leave");
        		    dailyAtt.setLeave(true);
        		} else if(leave.getStatus()== 2){
        		    dailyAtt.setStatus("Leave");
        		    dailyAtt.setLeave(true);
        		}
		    }
		}
		//Leave check
		//Absent check
		if(call.equals("monthlyleavestatus") || call.equals("monthlyleavedownload")){
        		if((startAtt.getAtt_time()==null) && (dd<curdd))
        		{
        		    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
        		    {
        			dailyAtt.setAbsent(true);
        			if(dailyAtt.getStatus()!=null)
        			    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
        			else
        			    dailyAtt.setStatus("Absent");
        		    }
        		} 
        		else if((startAtt.getAtt_time()==null) && ((dd>curdd) && (mon<curmm)))
        		{
        		    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
        		    {
        			dailyAtt.setAbsent(true);
        			if(dailyAtt.getStatus()!=null)
        			    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
        			else
        			    dailyAtt.setStatus("Absent");
        		    }
        		}
        		else if((startAtt.getAtt_time()==null) && ((dd>curdd) && (mon>curmm) && (year<curyy)))
        		{
        		    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
        		    {
        			dailyAtt.setAbsent(true);
        			if(dailyAtt.getStatus()!=null)
        			    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
        			else
        			    dailyAtt.setStatus("Absent");
        		    }
        		}
		} else {
		    	if((startAtt.getAtt_time()==null) && (dd<=endDate))
    			{
		    	    if((!dailyAtt.isHoliday()) && (!dailyAtt.isWeekend()) && (!dailyAtt.isLeave()))
		    	    {
		    		dailyAtt.setAbsent(true);
		    		if(dailyAtt.getStatus()!=null)
		    		    dailyAtt.setStatus(dailyAtt.getStatus()+" Absent");
		    		else
		    		    dailyAtt.setStatus("Absent");
		    	    }
    			}		    
		}
		//Absent check
		monAtt.add(dailyAtt);
	    }
	    if(isCurdatechk)
		return monAtt;
	    else{
    	    	if(dataFound)
    	    	    return monAtt;
    	    	else
    	    	    return blankmonAtt;
	    }
	}

	@Override
	public HWorkInfo getJoiningWorkInfoById(int emp_id) {
	    List<HWorkInfo> workinfos = wamsDao.getJoiningWorkInfoById(emp_id);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	    HWorkInfo joinInfo = new HWorkInfo();
	    try {
		Date date1 = sdf.parse(sdf.format(new Date()));
	    
    	    	Date date2 = new Date();
    	    	
    	    	for(HWorkInfo workinfo:workinfos){
		
    	    	    date2 = sdf.parse(workinfo.getFrom_date());
    	    	    if(date1.compareTo(date2)>0){
    	    		//System.out.println("Date1 is after Date2");
    	    		date1 = date2;
    	    		joinInfo = workinfo;
    	    	    }
    	    	}
	    
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	    return joinInfo;
	}

	@Override
	public List<HCardAttendance> getCardAttendance(String CHECKTIME) {
	    //return accAttendanceDAO.getAllAttendance(CHECKTIME);
	    RestTemplate restTemplate = new RestTemplate();
	    
	    ResponseEntity<HCardAttendance[]> response = restTemplate.getForEntity("http://123.200.15.18:8080/WAMSAPI/cardattendance/"+CHECKTIME,HCardAttendance[].class);
	            
	    HCardAttendance[] cardData = response.getBody();
	    
	    if(null!=cardData){
	    	return Arrays.asList(cardData);
	    } else{
	    	List<HCardAttendance> card = new ArrayList<HCardAttendance>();
	    	return card;
	    }
	}
	
	public List<HCardAttendance> getCardAttendanceOfDate(String date){
	    RestTemplate restTemplate = new RestTemplate();
	    
	    ResponseEntity<HCardAttendance[]> response = restTemplate.getForEntity("http://123.200.15.18:8080/WAMSAPI/cardattendanceofdate/"+date,HCardAttendance[].class);
	            
	    HCardAttendance[] cardData = response.getBody();
	    
	    if(null!=cardData){
	    	return Arrays.asList(cardData); 
	    } else{
	    	List<HCardAttendance> card = new ArrayList<HCardAttendance>();
	    	return card;
	    }
	    
	    
	}

	@Override
	public HCardAttendance getLastAttendanceFromDb1() {
	    return wamsDao.getLastAttendanceFromDb1();
	}

	@Override
	public Integer insertHCardAttendance(StringBuffer sql) {
	    return wamsDao.insertHCardAttendance(sql);
	}

	@Override
	public HEmployee getEmployeeByEmpnumber(long emp_number) {
	    return wamsDao.getEmployeeByEmpnumber(emp_number);
	}

	@Override
	public Integer setAutoLunch(HEmployeeparam param) {
	    return wamsDao.setAutoLunch(param);
	}

	@Override
	public HEmployeeparam getAutoLunch(int emp_id) {
	    return wamsDao.getAutoLunch(emp_id);
	}

	
	@Override
	public Integer insertHTeam(HTeam team) {
	    return  wamsDao.insertHTeam(team);
	}
	
	public String convertLongToTime(long diff){
	    //long diffSeconds = diff / 1000 % 60;
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000);
	    //long diffDays = diff / (24 * 60 * 60 * 1000);
		
	    String wrkTime = "";
	    if(diffMinutes==0 && diffHours ==0){
		wrkTime = "-";
	    } else{
		wrkTime = diffHours+":"+diffMinutes;
	    }
	    
	    return wrkTime;
	}
	
	public String changetDateFormate(String dt) {//15-4-2016  4  15  2016   output 2016-04-15
	    String formatedDate = "";

	    String[] splitdt = dt.split("-");
	    formatedDate = splitdt[2] + "-" + splitdt[1] + "-" + splitdt[0];

	    return formatedDate;

	}

	@Override
	public Boolean checkCardAttendance(HCardAttendance att) {
	    return wamsDao.checkCardAttendance(att);
	}

	@Override
	public Integer insertHTypesOfLeave(HTypesOfLeave type) {
	    return wamsDao.insertHTypesOfLeave(type);
	}

	@Override
	public Integer editHTypesOfLeave(HTypesOfLeave type) {
	    return wamsDao.editHTypesOfLeave(type);
	}

	@Override
	public Integer insertHLeaveQuota(HLeaveQuota type) {
	    return wamsDao.insertHLeaveQuota(type);
	}

	@Override
	public List<HLeaveQuota> getLeaveQuotas(int empId,String year) {
	    return wamsDao.getLeaveQuotas(empId, year);
	}

	@Override
	public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year) {
	    return wamsDao.getHMonthlyStatus(mon, year);
	}

	@Override
	public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year, int emp_id) {
	    return wamsDao.getHMonthlyStatus(mon, year, emp_id);
	}
	
	/*@Override
	public boolean test(int startDate, int mon, int year) {
	    //List<HMAttendance> todayAtt = new ArrayList<HMAttendance>();
	    
	    int curdd = startDate;
	    int curmm = mon;
	    int curyy = year;
	    int endDate = endDateofMon(mon,year);
	    
	    List <HEmployee> allEmp = wamsDao.getAllEmployee(true);
	    for(curdd=startDate;curdd<=endDate;curdd++)
	    {
	       	    for(HEmployee emp:allEmp)
        	    {
        		String curDate = curyy+"-"+curmm+"-"+curdd;
        		        		
        		HAttendance startAtt = wamsDao.getDailyAtt(curDate,0,emp.getEmp_id());
        		HAttendance endAtt = wamsDao.getDailyAtt(curDate,1,emp.getEmp_id());
        		if(startAtt.getAtt_date()!=null){
        		    String st = convertDateFormate(startAtt.getAtt_date());
        		    startAtt.setAtt_date(st);
        		    //wamsDao.insertHAttendance(startAtt);
        		}
        		
        		if(endAtt.getAtt_date()!=null){
        		    String et = convertDateFormate(endAtt.getAtt_date());
        		    endAtt.setAtt_date(et);
        		    //wamsDao.insertHAttendance(endAtt);
        		}
        	    }
	    }
	    
	    return true;
	}*/
	
	public String convertDateFormate(String dt) {//25-01-2016  25  01  2016   output 2016-01-25
		String formatedDate = "";

		String[] splitdt = dt.split("-");
		formatedDate = splitdt[2] + "-" + splitdt[1] + "-" + splitdt[0];

		return formatedDate;

	    }

	
	@Override
	public List<HAttendance> getTodayAttendance(String date) {
	    return wamsDao.getTodayAttendance(date);
	}

	@Override
	public Integer insertHAttendance_bak(HAttendance empAtt) {
	    return wamsDao.insertHAttendance_bak(empAtt);
	}
	
	@Override
	public boolean deleteHAttendance(String date){
	    return wamsDao.deleteHAttendance(date);
	}
	
	@Override
	public boolean alterHAttendance_Auto_inc(String date){
	    return wamsDao.alterHAttendance_Auto_inc(date);
	}
	
	@Override
	public HAttendance getDailyAttFrom_bak(String curDate, int inout, int emp_id){
	    return wamsDao.getDailyAttFrom_bak(curDate, inout, emp_id);
	}
	
	@Override
	public boolean insertFirstInLastOut(String pdate) {  
	   List <HEmployee> allEmp = wamsDao.getAllEmployee(false);
	    
	    for(HEmployee emp:allEmp)
	    {
        	String curDate = pdate;
		        		
		HAttendance startAtt = wamsDao.getDailyAttFrom_bak(curDate,0,emp.getEmp_id());
		HAttendance endAtt = wamsDao.getDailyAttFrom_bak(curDate,1,emp.getEmp_id());
		if(startAtt.getAtt_date()!=null){
		    String st = convertDateFormate(startAtt.getAtt_date());
		    startAtt.setAtt_date(st);
		    wamsDao.insertHAttendance(startAtt);
		}
		
		if(endAtt.getAtt_date()!=null){
		    String et = convertDateFormate(endAtt.getAtt_date());
		    endAtt.setAtt_date(et);
		    wamsDao.insertHAttendance(endAtt);
		}
	    }
	    
	    
	    return true;
	}
	
	@Override
	public List<HLeaveQuota> getQuotaYear(){
	    return wamsDao.getQuotaYear();
	}

	@Override
	public HLeaveQuota getLeaveQuota(int empId, String year, int type_id) {
	    return wamsDao.getLeaveQuota(empId, year, type_id);
	}
	
	@Override
	public List<HSupportingData> getSupportingData(String type){
	    return wamsDao.getSupportingData(type);
	}
	
	@Override
	public List<HHoliday> getAllHoliday(boolean isDeleted){
		return wamsDao.getAllHoliday(isDeleted);
	}
}

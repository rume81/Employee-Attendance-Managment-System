package com.WAMS.ams.services.interfaces;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

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


/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* IProfileService.java
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

public interface IWamsService {
    
    public HEmployee getUserValidation(HEmployee user);
    
    public Integer getAttSequence(HAttendance att);
    
    public Integer insertHAttendance(HAttendance empAtt); 
    
    public Integer insertHAttendance(StringBuffer sql); 
    
    public List<HAttendance> getTodayAttendance(String date,Integer emp_id);
    
    public List<HEmployee> getAllEmployee(boolean isDeleted);
    
    public List<HEmployee> getAllEmployee(boolean isDeleted,String userType);
	
    public HEmployee getEmployeeById(Integer id);
    
    public Integer editHEmployee(HEmployee emp); 
	
    public Integer insertHEmployee(HEmployee emp); 
	
    public Integer insertHWorkInfo(HWorkInfo work); 
    
    public Integer insertHHolidayInfo(HHoliday holiday);
    
    public List<HHoliday> getAllHoliday(boolean isDeleted,int year);
    
    public List<HMAttendance> getAttendanceForMonth(int mon,int year,int emp_id,boolean isCurdatechk);
    
    public HWorkInfo getActiveWorkInfo(Integer empid);
    
    public List<HEmployee> getAttendanceInStatus(String date);
    
    //public List<HEmployee> getAttendanceOutStatus(String date);
    
    public Integer insertHLeaveInfo(HLeave leave);
    
    public List<HMAttendance> getAttendanceByDay(Date date);
    
    public List<String> calculateLeaveDate(String fdate,String tdate,int emp_id, int reqFrom);
    
    public List<HTypesOfLeave> getLeaveType(boolean isDeleted);
    
    public List<HLeave> getAllLeaveByEmpId(boolean isDeleted,int emp_id);
    
    public HTypesOfLeave getLeaveTypeById(Integer id);
    
    public HHoliday getHolidayById(Integer id);
    
    public Integer editHHoliday(HHoliday holiday); 
    
    public List<HLeave> getAllLeaveByApproverId(boolean isDeleted, int approver_id);
    
    public List<HLeave> getAllLeaveAppliedForApprovedByApproverId(boolean isDeleted, int approver_id);
    
    public List<HOfficeOutgoing> getAllOfficeOutgoingByEmpId(boolean isDeleted, int emp_id);
    
    public List<HLeave> getAllApprovedLeaveByApproverId(boolean isDeleted, int approver_id);
    
    public HLeave getLeaveById(Integer id);
    
    public Integer editHEmployeePass(HEmployee emp);
    
    public Integer approveRejectLeave(HLeaveStatus status,int finalapp);
    
    public List<HLeaveStatus> getAllLeaveStatusByLeaveId(boolean isDeleted,int leaveId);
    
    public Integer insertHLeaveApprover(List<HLeaveApprover> approvers); 
    
    public List<HLeaveApprover> getLeaveApprovers(boolean isDeleted, int emp_id);
    
    public Integer editHLeave(HLeave leave);
    
    public List<HEmployee> getEmpNotGivenSignout(String date);
    
    public Integer insertHLunch(HLunch lunch);
    
    public Integer insertHLunch(StringBuffer sql);
    
    public Integer setAutoLunch(HEmployeeparam param);
    
    public Integer editHLunch(HLunch lunch);
    
    public HLunch getLunchByEmp(String date,Integer emp_id);
    
    public List<HLunch> getLunchListByDate(boolean isDeleted,boolean isCancel,String date);
    
    public List<HMonthlyStatus> getEmployeeMonthlyStatus(int startDate,int mon, int year);
    
    public int endDateofMon(int mon,int year);
    
    public HLeave getLeaveByEmpIdAndDate(Integer empid,String date,int status);
    
    public Integer sendMail(HMail mail);
    
    public List<HMail> getMail(boolean isDeleted, String state);
    
    public Integer editHMail(HMail mail);
    
    public HWorkInfo getWorkInfoById(Integer id,String date);
    
    public List<HTeam> getAllTeam(boolean isDeleted);
    
    public HTeam getTeamById(int id);
    
    public List<HLunch> getLunchForMonth(int mon, int year, int emp_id,boolean isCurdatechk);
    
    public List<HLeave> getAllLeaveForMonth(boolean isDeleted,int mon,int year,int status);
    
    public List<HLeave> getAllLeaveForMonth(boolean isDeleted, int mon, int year, String status);
    
    public List<HLeave> getAllLeaveBetweenDates(boolean isDeleted, String sdate, String edate, int status);
    
    public List<HYearlyLeave> getEmployeeYearlyLeave(int year);
    
    public List<HMAttendance> getEmployeeStatusForMonth(int mon, int year, int emp_id,boolean isCurdatechk,String call);
    
    public HWorkInfo getJoiningWorkInfoById(int emp_id);
    
    public List<HCardAttendance> getCardAttendance(String CHECKTIME);
    
    public List<HCardAttendance> getCardAttendanceOfDate(String date);
    
    public HCardAttendance getLastAttendanceFromDb1();
    
    public Boolean checkCardAttendance(HCardAttendance att);
    
    public Integer insertHCardAttendance(StringBuffer sql);
    
    public HEmployee getEmployeeByEmpnumber(long emp_number);
    
    public HEmployeeparam getAutoLunch(int emp_id);
    
    public Integer insertHTeam(HTeam team);
    
    public Integer insertHTypesOfLeave(HTypesOfLeave type);
    
    public Integer editHTypesOfLeave(HTypesOfLeave type);
    
    public Integer insertHLeaveQuota(HLeaveQuota type);
    
    public List<HLeaveQuota> getLeaveQuotas(int empId,String year);
    
    public Hashtable getEmployeeAbsendList(int mon, int year);
    
    public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year);
    
    public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year, int emp_id);
    
    //public boolean test(int startDate, int mon, int year);
    
    public List<HAttendance> getTodayAttendance(String date);
    
    public Integer insertHAttendance_bak(HAttendance empAtt);
    
    public boolean deleteHAttendance(String date);
    
    public boolean alterHAttendance_Auto_inc(String date);
    
    public boolean insertFirstInLastOut(String pdate);
    
    public HAttendance getDailyAttFrom_bak(String curDate, int inout, int emp_id);
    
    public List<HLeaveQuota> getQuotaYear();
    
    public HLeaveQuota getLeaveQuota(int empId,String year,int type_id);
    
    public Integer insertHOfficeOutgoingInfo(HOfficeOutgoing outgoing);
    
    public Integer insertHOfficeOutgoingApprover(List<HOfficeOutgoingApprover> approvers);

    public List<HOfficeOutgoingApprover> getOfficeOutgoingApprovers(boolean isDeleted, int emp_id);
    
    public List<HOfficeOutgoing> getAllOfficeOutgoingByApproverId(boolean isDeleted, int approver_id);
    
    public List<HOfficeOutgoing> getAllOfficeOutgoingAppliedForApprovedByApproverId(boolean isDeleted, int approver_id);
    
    public HOfficeOutgoing getOfficeOutgoingById(Integer id);
    
    public List<HOfficeOutgoingStatus> getAllOfficeOutgoingStatusByOutgoingId(boolean isDeleted, int outgoingId);
    
    public Integer editHOfficeOutgoing(HOfficeOutgoing outgoing);
    
    public Integer approveRejectOfficeOutgoing(HOfficeOutgoingStatus status, int finalapp);
    
    public List<HSupportingData> getSupportingData(String type);
    
    public List<HHoliday> getAllHoliday(boolean isDeleted);
}

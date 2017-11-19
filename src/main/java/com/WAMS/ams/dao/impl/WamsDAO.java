package com.WAMS.ams.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.WAMS.ams.dao.interfaces.IWamsDAO;
import com.webhawks.Hawks_mapper.AttendanceMapper;
import com.webhawks.Hawks_mapper.CardAttMapper;
import com.webhawks.Hawks_mapper.EmpMapper;
import com.webhawks.Hawks_mapper.EmployeeparamMapper;
import com.webhawks.Hawks_mapper.HMonthlyStatusMapper;
import com.webhawks.Hawks_mapper.HolidayMapper;
import com.webhawks.Hawks_mapper.LeaveApproverMapper;
import com.webhawks.Hawks_mapper.LeaveMapper;
import com.webhawks.Hawks_mapper.LeaveQuotaMapper;
import com.webhawks.Hawks_mapper.LeaveStatusMapper;
import com.webhawks.Hawks_mapper.LunchMapper;
import com.webhawks.Hawks_mapper.MailMapper;
import com.webhawks.Hawks_mapper.OfficeOutgoingApproverMapper;
import com.webhawks.Hawks_mapper.OfficeOutgoingMapper;
import com.webhawks.Hawks_mapper.OfficeOutgoingStatusMapper;
import com.webhawks.Hawks_mapper.SupportingDataMapper;
import com.webhawks.Hawks_mapper.TeamMapper;
import com.webhawks.Hawks_mapper.TypesOfLeaveMapper;
import com.webhawks.Hawks_mapper.WorkInfoMapper;
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
import com.webhawks.Hawks_model.HMail;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.webhawks.Hawks_model.HOfficeOutgoing;
import com.webhawks.Hawks_model.HOfficeOutgoingApprover;
import com.webhawks.Hawks_model.HOfficeOutgoingStatus;
import com.webhawks.Hawks_model.HSupportingData;
import com.webhawks.Hawks_model.HTeam;
import com.webhawks.Hawks_model.HTypesOfLeave;
import com.webhawks.Hawks_model.HWorkInfo;

public class WamsDAO extends BaseDAO implements IWamsDAO {// isDeleted= true
										// means deleted
													// record retrive
															// also

	private final Logger logger = LoggerFactory.getLogger(WamsDAO.class);

	public List<HEmployee> getAllEmployee(boolean isDeleted) {
		List<HEmployee> allEmp = new ArrayList<HEmployee>();
		try {
		    allEmp = getJdbcService().getJdbcTemplate().query("CALL getAllEmployee("+isDeleted+")", new Object[] {}, new EmpMapper());
			/*if (!isDeleted)
			    
				allEmp = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM employee WHERE DELETED = 0 AND usertype<>'Watcher' ORDER BY employee_name ASC",
						new Object[] {}, new EmpMapper());
			else
				allEmp = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM employee WHERE usertype<>'Watcher' ORDER BY employee_name ASC", new Object[] {},
						new EmpMapper());*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allEmp;
	}

	@Override
	public HEmployee getEmployeeByPassword(boolean isDeleted, String userName, String pass) {
		HEmployee profile = null;
		try {
			/*profile = getJdbcService().getJdbcTemplate().queryForObject("SELECT * FROM employee WHERE avator ='"
					+ userName + "' AND password=MD5('" + pass + "') AND DELETED = " + isDeleted, new EmpMapper());*/
		    profile = getJdbcService().getJdbcTemplate().queryForObject(
			    "CALL validateUser('" + userName + "',MD5('" + pass + "'))", new EmpMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return profile;
	}

	public List<HEmployee> getAllEmployee(boolean isDeleted, String userType) {
		List<HEmployee> allEmp = new ArrayList<HEmployee>();
		try {//getAllEmployeeByType
		    
		    allEmp = getJdbcService().getJdbcTemplate().query("CALL getAllEmployeeByType("+isDeleted+",'"+userType+"')",new Object[] {}, new EmpMapper());
			/*if (!isDeleted) {
				if (userType.equalsIgnoreCase("ALL")) {
					allEmp = getJdbcService().getJdbcTemplate().query("SELECT * FROM employee WHERE DELETED = 0",
							new Object[] {}, new EmpMapper());
				} else {
					allEmp = getJdbcService().getJdbcTemplate().query(
							"SELECT * FROM employee WHERE DELETED = 0 AND usertype='" + userType + "'", new Object[] {},
							new EmpMapper());
				}
			} else {
				if (userType.equalsIgnoreCase("ALL")) {
					allEmp = getJdbcService().getJdbcTemplate().query("SELECT * FROM employee", new Object[] {},
							new EmpMapper());
				} else {
					allEmp = getJdbcService().getJdbcTemplate().query(
							"SELECT * FROM employee WHERE usertype='" + userType + "'", new Object[] {},
							new EmpMapper());
				}
			}*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allEmp;
	}

	public HEmployee getEmployeeById(Integer id) {
		HEmployee profile = new HEmployee();
		try {
		    profile = getJdbcService().getJdbcTemplate().queryForObject("CALL getEmployee(" + id + ")",
				new EmpMapper());
			/*profile = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM employee WHERE emp_id ='" + id + "' AND DELETED = 0", new EmpMapper());*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return profile;
	}

	public Integer editHEmployee(HEmployee emp) {
		Integer empId = -1;
		emp.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("UPDATE employee SET ");
			sql.append("employee_name = '" + emp.getEmployee_name() + "'," + "avator = '" + emp.getAvator() + "',"
					+ "email = '" + emp.getEmail() + "'," + "designation = '" + emp.getDesignation() + "',"
					+ "skype_id = '" + emp.getSkype_id() + "'," + "bloodgroup = '" + emp.getBloodgroup() + "',"
					+ "birthdate_certificate = '" + emp.getBirthdate_certificate() + "'," + "birthdate_real = '"
					+ emp.getBirthdate_real() + "'," + "nid = '" + emp.getNid() + "'," + "personal_email = '"
					+ emp.getPersonal_email() + "'," + "usertype = '" + emp.getUsertype() + "'," + "mobile = '"
					+ emp.getMobile() + "'," + "deleted = " + emp.getDeleted() + "," + "modifierid = '" + emp.getModId()
					+ "'," + "mobile = '" + emp.getMobile() + "'," + "emp_number = '" + emp.getEmp_number() + "',"
					+ "teamId =" + emp.getTeamId() + "," + "join_date = '" + emp.getJoin_date() + "' "
					+ "WHERE emp_id =" + emp.getEmp_id());

			logger.info("HEmployee Update Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			empId = emp.getEmp_id();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	public Integer editHEmployeePass(HEmployee emp) {
		Integer empId = -1;
		emp.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("UPDATE employee SET ");
			sql.append("password = MD5('" + emp.getPassword() + "')," + "deleted = " + emp.getDeleted() + ","
					+ "modifierid = '" + emp.getModId() + "' WHERE emp_id =" + emp.getEmp_id());

			logger.info("HEmployee Update Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			empId = emp.getEmp_id();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	public Integer insertHEmployee(HEmployee emp) {
		Integer empId = 0;
		emp.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO employee ");

			sql.append(
					"(employee_name, avator, email, designation, skype_id, bloodgroup, birthdate_certificate, birthdate_real, nid, personal_email, password, usertype, deleted, modifierid, mobile, emp_number,teamId,join_date) VALUES( '");

			sql.append(emp.getEmployee_name() + "','" + emp.getAvator() + "','" + emp.getEmail() + "','"
					+ emp.getDesignation() + "','" + emp.getSkype_id() + "','" + emp.getBloodgroup() + "','"
					+ emp.getBirthdate_certificate() + "','" + emp.getBirthdate_real() + "','" + emp.getNid() + "','"
					+ emp.getPersonal_email() + "',MD5('" + emp.getPassword() + "'),'" + emp.getUsertype() + "',"
					+ emp.getDeleted() + ",'" + emp.getModId() + "','" + emp.getMobile() + "','" + emp.getEmp_number()
					+ "'," + emp.getTeamId() + ",'" + emp.getJoin_date() + "')");

			logger.info("HEmployee Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			empId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	public HWorkInfo getActiveWorkInfo(Integer empid) {
		HWorkInfo workprofile = new HWorkInfo();
		Boolean ST = true;
		try {
			workprofile = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM workinfo WHERE emp_id =" + empid + " AND status = " + ST + " AND DELETED = 0",
					new WorkInfoMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return workprofile;
	}

	public Integer insertHWorkInfo(HWorkInfo work) {
		Integer workId = 0;
		Boolean ST = false;
		try {
			StringBuffer sql = new StringBuffer("UPDATE workinfo SET ");
			sql.append("status = " + ST + " WHERE emp_id =" + work.getEmp_id());

			logger.info("HWorkInfo Update Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			StringBuffer sql = new StringBuffer("INSERT INTO workinfo ");

			sql.append(
					"(emp_id, office_start, working_hour, weekend, from_date, status, deleted, modifierid) VALUES( ");

			sql.append(work.getEmp_id() + "," + work.getOffice_start() + "," + work.getWorking_hour() + ",'"
					+ work.getWeekend() + "','" + work.getFrom_date() + "'," + work.getStatus() + ","
					+ work.getDeleted() + ",'" + work.getModId() + "')");

			logger.info("HWorkInfo Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			workId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return workId;
	}

	public Integer insertHHolidayInfo(HHoliday holiday) {
		Integer holidayId = 0;
		holiday.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO holiday ");

			sql.append("(holiday_desc, date_from, date_to, holiday_year, deleted, modifierid) VALUES( '");

			sql.append(holiday.getHoliday_desc() + "','" + holiday.getDate_from() + "','" + holiday.getDate_to() + "',"
					+ holiday.getHoliday_year() + "," + holiday.getDeleted() + ",'" + holiday.getModId() + "')");

			logger.info("HHoliday Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			holidayId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return holidayId;
	}
	
	@Override
	public List<HHoliday> getAllHoliday(boolean isDeleted, int year) {
		List<HHoliday> allHoliday = new ArrayList<HHoliday>();
		try {
			if (!isDeleted)
				allHoliday = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM holiday WHERE DELETED = 0 AND holiday_year=" + year + " ORDER BY date_from",
						new Object[] {}, new HolidayMapper());
			else
				allHoliday = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM holiday WHERE holiday_year=" + year + " ORDER BY date_from", new Object[] {},
						new HolidayMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allHoliday;
	}
	
	@Override
	public List<HHoliday> getAllHoliday(boolean isDeleted) {
		List<HHoliday> allHoliday = new ArrayList<HHoliday>();
		try {
			if (!isDeleted)
				allHoliday = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM holiday WHERE DELETED = 0 ORDER BY id",
						new Object[] {}, new HolidayMapper());
			else
				allHoliday = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM holiday ORDER BY id", new Object[] {},
						new HolidayMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allHoliday;
	}

	@Override
	public Integer getAttSequence(HAttendance att) {
		Integer seq = 0;
		try {
			seq = getJdbcService().getJdbcTemplate()
					.queryForInt("SELECT Max(day_sequence) FROM attendance WHERE emp_id =" + att.getEmp_id()
							+ " AND att_date = '" + att.getAtt_date() + "'");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return seq;
	}

	@Override
	public Integer insertHAttendance(HAttendance empAtt) {
		Integer insert = -1;
		try {

			Integer timeSeq = getAttSequence(empAtt);

			StringBuffer sql = new StringBuffer("INSERT INTO attendance ");

			sql.append("(emp_id, att_date, att_time, att_inout, day_sequence, deleted, modifierid) VALUES( ");

			sql.append(empAtt.getEmp_id() + ",'" + empAtt.getAtt_date() + "','" + empAtt.getAtt_time() + "',"
					+ empAtt.getAtt_inout() + "," + (timeSeq + 1) + "," + empAtt.getDeleted() + ",'" + empAtt.getModId()
					+ "')");

			logger.info("HAttendance Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public Integer insertHAttendance_bak(HAttendance empAtt) {
		Integer insert = -1;
		try {

			Integer timeSeq = getAttSequence(empAtt);

			StringBuffer sql = new StringBuffer("INSERT INTO attendance_bak ");

			sql.append("(emp_id, att_date, att_time, att_inout, day_sequence, deleted, modifierid) VALUES( ");

			sql.append(empAtt.getEmp_id() + ",'" + empAtt.getAtt_date() + "','" + empAtt.getAtt_time() + "',"
					+ empAtt.getAtt_inout() + "," + (timeSeq + 1) + "," + empAtt.getDeleted() + ",'" + empAtt.getModId()
					+ "')");

			logger.info("HAttendance_bak Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public Integer insertHAttendance(StringBuffer sql) {
		Integer insert = -1;
		try {
			logger.info("HAttendance Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public List<HAttendance> getTodayAttendance(String date, Integer emp_id) {
		List<HAttendance> allAtt = new ArrayList<HAttendance>();
		try {

			allAtt = getJdbcService().getJdbcTemplate().query("SELECT * FROM attendance WHERE DELETED = 0 AND emp_id ="
					+ emp_id + " AND att_date='" + date + "' ORDER BY att_time", new Object[] {},
					new AttendanceMapper());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allAtt;
	}

	@Override
	public List<HAttendance> getTodayAttendance(String date) {
		List<HAttendance> allAtt = new ArrayList<HAttendance>();
		try {

			allAtt = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM attendance WHERE DELETED = 0 AND att_date='" + date + "' ORDER BY att_id",
					new Object[] {}, new AttendanceMapper());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allAtt;
	}

	@Override
	public HAttendance getDailyAtt(String curDate, int inout, int emp_id) {
		HAttendance att = new HAttendance();
		try {

			if (inout == 0) {
				att = getJdbcService().getJdbcTemplate()
						.queryForObject("SELECT * FROM attendance WHERE att_inout = " + inout + " AND att_date='"
								+ curDate + "' AND emp_id=" + emp_id + " AND "
								+ "att_id = (SELECT att_id FROM ams.attendance WHERE  att_inout = " + inout
								+ " AND att_date='" + curDate + "' AND emp_id=" + emp_id
								+ " ORDER BY att_time LIMIT 1)", new Object[] {}, new AttendanceMapper());
				/*
				 * att = getJdbcService().getJdbcTemplate().queryForObject(
				 * "SELECT * FROM attendance WHERE att_inout = "+inout+
				 * " AND att_date='"+curDate+"' AND emp_id="+emp_id+ " AND "+
				 * "day_sequence = (SELECT MIN(day_sequence) FROM ams.attendance WHERE  att_inout = "
				 * +inout+" AND att_date='"+curDate+"' AND emp_id="+emp_id+")",
				 * new Object[] {}, new AttendanceMapper());
				 */
			} else {
				att = getJdbcService().getJdbcTemplate()
						.queryForObject("SELECT * FROM attendance WHERE att_inout = " + inout + " AND att_date='"
								+ curDate + "' AND emp_id=" + emp_id + " AND "
								+ "att_id = (SELECT att_id FROM ams.attendance WHERE  att_inout = " + inout
								+ " AND att_date='" + curDate + "' AND emp_id=" + emp_id
								+ " ORDER BY att_time DESC LIMIT 1)", new Object[] {}, new AttendanceMapper());
				/*
				 * att = getJdbcService().getJdbcTemplate().queryForObject(
				 * "SELECT * FROM attendance WHERE att_inout = "+inout+
				 * " AND att_date='"+curDate+"' AND emp_id="+emp_id+ " AND "+
				 * "day_sequence = (SELECT MAX(day_sequence) FROM ams.attendance WHERE  att_inout = "
				 * +inout+" AND att_date='"+curDate+"' AND emp_id="+emp_id+")",
				 * new Object[] {}, new AttendanceMapper());
				 */

			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return att;
	}

	@Override
	public HAttendance getDailyAttFrom_bak(String curDate, int inout, int emp_id) {
		HAttendance att = new HAttendance();
		try {

			if (inout == 0) {
				att = getJdbcService().getJdbcTemplate()
						.queryForObject("SELECT * FROM attendance_bak WHERE att_inout = " + inout + " AND att_date='"
								+ curDate + "' AND emp_id=" + emp_id + " AND "
								+ "att_id = (SELECT att_id FROM attendance_bak WHERE  att_inout = " + inout
								+ " AND att_date='" + curDate + "' AND emp_id=" + emp_id
								+ " ORDER BY att_time LIMIT 1)", new Object[] {}, new AttendanceMapper());

			} else {
				att = getJdbcService().getJdbcTemplate()
						.queryForObject("SELECT * FROM attendance_bak WHERE att_inout = " + inout + " AND att_date='"
								+ curDate + "' AND emp_id=" + emp_id + " AND "
								+ "att_id = (SELECT att_id FROM attendance_bak WHERE  att_inout = " + inout
								+ " AND att_date='" + curDate + "' AND emp_id=" + emp_id
								+ " ORDER BY att_time DESC LIMIT 1)", new Object[] {}, new AttendanceMapper());

			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return att;
	}

	@Override
	public HWorkInfo getWorkInfoById(Integer emp_id, String date) {
		HWorkInfo workInfo = new HWorkInfo();
		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM workinfo where emp_id = " + emp_id
					+ " AND id=(SELECT MAX(id) FROM workinfo WHERE emp_id = " + emp_id + " AND from_date <= '" + date
					+ "')");
			logger.info("HWorkInfo Query - > " + sql.toString());

			workInfo = getJdbcService().getJdbcTemplate().queryForObject(sql.toString(), new Object[] {},
					new WorkInfoMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return workInfo;
	}

	@Override
	public HHoliday getHolidayByDate(String date) {
		HHoliday holiday = new HHoliday();
		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM holiday WHERE date_from >='" + date + "' AND date_to <='"
					+ date + "'  AND deleted = false");

			logger.info("HHoliday Query - > " + sql.toString());
			holiday = getJdbcService().getJdbcTemplate().queryForObject(sql.toString(), new Object[] {},
					new HolidayMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return holiday;
	}

	@Override
	public List<HHoliday> getHolidayListByDate(String date) {
		List<HHoliday> holiday = new ArrayList<HHoliday>();
		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM holiday WHERE date_from <='" + date + "' AND date_to >='"
					+ date + "'  AND deleted = false");

			logger.info("HHoliday Query - > " + sql.toString());
			holiday = getJdbcService().getJdbcTemplate().query(sql.toString(), new Object[] {}, new HolidayMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return holiday;
	}

	@Override
	public Integer insertHLeaveInfo(HLeave leave) {
		Integer empId = 0;
		leave.escapeEcmaScript();
		
		TransactionStatus status = getJdbcService().getTransactionStatus();
	      
		getJdbcService().beginTran();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO leavesrequest ");

			sql.append(
					"(emp_id, fdate, tdate, days, leavetype, quotayear, reason, status, approvar1, approvar2, approvar3, approvar4, approvar5, app1state, app2state, app3state, app4state, app5state, deleted, modifierid,apllieddate,modifydate) VALUES(");

			sql.append(leave.getEmp_id() + ",'" + leave.getFdate() + "','" + leave.getTdate() + "'," + leave.getDays()
					+ "," + leave.getLeavetype().getId() + ",'" + leave.getLeavequota().getYear() + "','"
					+ leave.getReason() + "'," + leave.getStatus() + "," + leave.getApprovar1() + ","
					+ leave.getApprovar2() + "," + leave.getApprovar3() + "," + leave.getApprovar4() + ","
					+ leave.getApprovar5() + "," + leave.getApp1state() + "," + leave.getApp2state() + ","
					+ leave.getApp3state() + "," + leave.getApp4state() + "," + leave.getApp5state() + ","
					+ leave.getDeleted() + ",'" + leave.getModId() + "','" + leave.getApllieddate() + "','"
					+ leave.getModifydate() + "')");

			logger.info("HLeave Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			StringBuffer sql1 = new StringBuffer("CALL insertleavequota(" + leave.getDays() + "," + leave.getEmp_id()
					+ "," + leave.getLeavetype().getId() + ",'" + leave.getLeavequota().getYear() + "')");

			getJdbcService().getJdbcTemplate().execute(sql1.toString());
			empId = leave.getEmp_id();
			
			getJdbcService().commitTran(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			getJdbcService().rollbackTran(status);
		}
		return empId;
	}
	
	@Override
	public Integer insertHOfficeOutgoingInfo(HOfficeOutgoing outgoing) {
		Integer empId = 0;
		outgoing.escapeEcmaScript();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO officeoutgoing ");

			sql.append("(emp_id, date, ftime, ttime, reason, status, approvar1, approvar2, approvar3, approvar4, approvar5, app1state, app2state, app3state, app4state, app5state, deleted, modifierid, apllieddate, modifydate) VALUES(");

			sql.append(outgoing.getEmp_id() + ",'" + df.format(outgoing.getDate()) + "','" + outgoing.getFtime() + "','" + outgoing.getTtime()
					+ "','"	+ outgoing.getReason() + "'," + outgoing.getStatus() + "," + outgoing.getApprovar1() + ","
					+ outgoing.getApprovar2() + "," + outgoing.getApprovar3() + "," + outgoing.getApprovar4() + ","
					+ outgoing.getApprovar5() + "," + outgoing.getApp1state() + "," + outgoing.getApp2state() + ","
					+ outgoing.getApp3state() + "," + outgoing.getApp4state() + "," + outgoing.getApp5state() + ","
					+ outgoing.getDeleted() + ",'" + outgoing.getModId() + "','" + outgoing.getApllieddate() + "','"
					+ outgoing.getModifydate() + "')");

			logger.info("HOfficeOutgoing Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			empId = outgoing.getEmp_id();
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	@Override
	public List<HTypesOfLeave> getLeaveType(boolean isDeleted) {
		List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
		try {
			if (!isDeleted)
				allLeaveType = getJdbcService().getJdbcTemplate().query("SELECT * FROM typesofleave WHERE DELETED = 0",
						new Object[] {}, new TypesOfLeaveMapper());
			else
				allLeaveType = getJdbcService().getJdbcTemplate().query("SELECT * FROM typesofleave", new Object[] {},
						new TypesOfLeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeaveType;
	}

	@Override
    public Boolean getLeaveInfoByDate(String date, int emp_id, int status) {
		Boolean Fo = false;
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			if (status == -1) {
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM leavesrequest where fdate <='" + date + "' AND tdate >='" + date
								+ "' AND emp_id=" + emp_id + " AND status<>1 AND deleted = false", new Object[] {},
						new LeaveMapper());
			} else {
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM leavesrequest where fdate <='" + date + "' AND tdate >='" + date
								+ "' AND emp_id=" + emp_id + " AND status>=" + status + " AND deleted = false",
						new Object[] {}, new LeaveMapper());
			}
			if (allLeave.size() > 0)
				Fo = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Fo;
	}

	@Override
	public List<HLeave> getLeaveDataByDate(String date, int emp_id) {
		List<HLeave> leaves = new ArrayList<HLeave>();
		try {

			leaves = getJdbcService()
					.getJdbcTemplate().query(
							"SELECT * FROM leavesrequest where fdate <='" + date + "' AND tdate >='" + date
									+ "' AND emp_id=" + emp_id + "  AND deleted = false",
							new Object[] {}, new LeaveMapper());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return leaves;
	}

	@Override
	public List<HLeave> getAllLeaveByEmpId(boolean isDeleted, int emp_id) {
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			if (!isDeleted)
				allLeave = getJdbcService()
						.getJdbcTemplate().query(
								"SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.DELETED = 0 AND leavesrequest.emp_id="
										+ emp_id + " order by leavesrequest.fdate asc",
								new Object[] {}, new LeaveMapper());
			else
				allLeave = getJdbcService()
						.getJdbcTemplate().query(
								"SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.emp_id="
										+ emp_id + " order by leavesrequest.fdate asc",
								new Object[] {}, new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeave;
	}
	
	@Override
	public List<HOfficeOutgoing> getAllOfficeOutgoingByEmpId(boolean isDeleted, int emp_id) {
		List<HOfficeOutgoing> allOfficeOutgoing = new ArrayList<HOfficeOutgoing>();
		try {
			if (!isDeleted)
				allOfficeOutgoing = getJdbcService()
						.getJdbcTemplate().query(
								"SELECT * FROM officeoutgoing WHERE DELETED = 0 AND emp_id="+ emp_id + " order by date asc",
								new Object[] {}, new OfficeOutgoingMapper());
			else
				allOfficeOutgoing = getJdbcService()
						.getJdbcTemplate().query(
								"SELECT * FROM officeoutgoing WHERE emp_id="+ emp_id + " order by date asc",
								new Object[] {}, new OfficeOutgoingMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allOfficeOutgoing;
	}

	@Override
	public List<HLeave> getAllLeaveByApproverId(boolean isDeleted, int approver_id) {
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			if (!isDeleted)
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.DELETED = 0 AND (leavesrequest.approvar1="
								+ approver_id + " OR leavesrequest.approvar2=" + approver_id
								+ " OR leavesrequest.approvar3=" + approver_id + " OR leavesrequest.approvar4="
								+ approver_id + " OR leavesrequest.approvar5=" + approver_id + ") AND status=0 ORDER BY fdate asc",
						new Object[] {}, new LeaveMapper());
			else
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.approvar1="
								+ approver_id + " OR leavesrequest.approvar2=" + approver_id
								+ " OR leavesrequest.approvar3=" + approver_id + " OR leavesrequest.approvar4="
								+ approver_id + " OR leavesrequest.approvar5=" + approver_id + " AND status=0 ORDER BY fdate asc",
						new Object[] {}, new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeave;
	}
	
	@Override
	public List<HOfficeOutgoing> getAllOfficeOutgoingByApproverId(boolean isDeleted, int approver_id) {
		List<HOfficeOutgoing> alloutgoing = new ArrayList<HOfficeOutgoing>();
		try {
			if (!isDeleted)
				alloutgoing = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM officeoutgoing WHERE officeoutgoing.DELETED = 0 AND (officeoutgoing.approvar1="
								+ approver_id + " OR officeoutgoing.approvar2=" + approver_id
								+ " OR officeoutgoing.approvar3=" + approver_id + " OR officeoutgoing.approvar4="
								+ approver_id + " OR officeoutgoing.approvar5=" + approver_id + ") AND status=0 ORDER BY date asc",
						new Object[] {}, new OfficeOutgoingMapper());
			else
				alloutgoing = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM officeoutgoing WHERE officeoutgoing.approvar1="
								+ approver_id + " OR officeoutgoing.approvar2=" + approver_id
								+ " OR officeoutgoing.approvar3=" + approver_id + " OR officeoutgoing.approvar4="
								+ approver_id + " OR officeoutgoing.approvar5=" + approver_id + " AND status=0 ORDER BY date asc",
						new Object[] {}, new OfficeOutgoingMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return alloutgoing;
	}

	@Override
	public HTypesOfLeave getLeaveTypeById(Integer id) {
		HTypesOfLeave leave = new HTypesOfLeave();
		try {
			leave = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM typesofleave WHERE id =" + id + " AND DELETED = 0", new TypesOfLeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return leave;
	}

	@Override
	public HHoliday getHolidayById(Integer id) {
		HHoliday holiday = new HHoliday();
		try {
			holiday = getJdbcService().getJdbcTemplate()
					.queryForObject("SELECT * FROM holiday WHERE id =" + id + " AND DELETED = 0", new HolidayMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return holiday;
	}

	@Override
	public Integer editHHoliday(HHoliday holiday) {
		Integer holidayId = -1;
		holiday.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("UPDATE holiday SET ");
			sql.append("holiday_desc = '" + holiday.getHoliday_desc() + "'," + "date_from = '" + holiday.getDate_from()
					+ "'," + "date_to = '" + holiday.getDate_to() + "'," + "holiday_year = " + holiday.getHoliday_year()
					+ "," + "deleted = " + holiday.getDeleted() + "," + "modifierid = '" + holiday.getModId() + "'"
					+ " WHERE id =" + holiday.getId());

			logger.info("HHoliday Update Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			holidayId = holiday.getId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return holidayId;
	}

	@Override
	public HLeave getLeaveById(Integer id) {
		HLeave leave = new HLeave();
		try {
			leave = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.id ="
							+ id + " AND leavesrequest.DELETED = 0",
					new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return leave;
	}
	
	
	@Override
	public HOfficeOutgoing getOfficeOutgoingById(Integer id) {
		HOfficeOutgoing outgoing = new HOfficeOutgoing();
		try {
			outgoing = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM officeoutgoing WHERE id ="+ id + " AND DELETED = 0",
					new OfficeOutgoingMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return outgoing;
	}

	@Override
	public Integer approveRejectLeave(HLeaveStatus status, int finalapp) {
		Integer empId = 0;
		status.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO leavestatus ");

			sql.append(
					"(leave_id, approver_id, comments, approvalstate, date, approvarSeq, deleted, modifierid) VALUES(");

			sql.append(status.getLeave_id() + "," + status.getApprover_id() + ",'" + status.getComments() + "',"
					+ status.getApprovalstate() + ",'" + status.getDate() + "'," + status.getApprovarSeq() + ","
					+ status.getDeleted() + ",'" + status.getModId() + "')");

			logger.info("HLeaveStatus Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			String field = "app" + status.getApprovarSeq() + "state";
			// When Approved by final Approver
			if (finalapp == 1 && status.getApprovalstate() == 1) {
				// Get
				HLeave leave = getLeaveById(status.getLeave_id());
				leave.escapeEcmaScript();
				// Update
				StringBuffer sql1 = new StringBuffer("UPDATE leavesrequest SET ");
				sql1.append(field + "=1,status = " + (leave.getStatus() + 2) + "," + "deleted = " + status.getDeleted()
						+ "," + "modifierid = '" + status.getModId() + "'" + " WHERE id =" + status.getLeave_id());

				logger.info("HLeave Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
			}
			// When Rejected by final Approver
			else if (finalapp == 1 && status.getApprovalstate() == 2) {
			    	// Get
				HLeave leave = getLeaveById(status.getLeave_id());
				leave.escapeEcmaScript();
				//Remove quota
				int newDays = 0;
			    	StringBuffer sql2 = new StringBuffer("CALL updateleavequota(" + leave.getId() + "," + newDays + "," + leave.getEmp_id()
				+ "," + leave.getLeavetype().getId() + ",'" + leave.getLeavequota().getYear() + "')");

			    	getJdbcService().getJdbcTemplate().execute(sql2.toString());
			    	// Update
				StringBuffer sql1 = new StringBuffer("UPDATE leavesrequest SET ");
				sql1.append(field + "=1,status = 1," + "deleted = " + status.getDeleted() + "," + "modifierid = '"
						+ status.getModId() + "'" + " WHERE id =" + status.getLeave_id());

				logger.info("HLeave Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
			}
			// When Approved by others Approver
			else {
				// Update
				StringBuffer sql1 = new StringBuffer("UPDATE leavesrequest SET ");
				sql1.append(field + "=1," + "deleted = " + status.getDeleted() + "," + "modifierid = '"
						+ status.getModId() + "'" + " WHERE id =" + status.getLeave_id());

				logger.info("HLeave Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
			}
			empId = status.getLeave_id();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}
	
	
	@Override
	public Integer approveRejectOfficeOutgoing(HOfficeOutgoingStatus status, int finalapp) {
		Integer empId = 0;
		status.escapeEcmaScript();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO officeoutgoingstatus ");

			sql.append(
					"(outgoing_id, approver_id, comments, approvalstate, date, approvarSeq, deleted, modifierid) VALUES(");

			sql.append(status.getOutgoing_id() + "," + status.getApprover_id() + ",'" + status.getComments() + "',"
					+ status.getApprovalstate() + ",'" + status.getDate() + "'," + status.getApprovarSeq() + ","
					+ status.getDeleted() + ",'" + status.getModId() + "')");

			logger.info("HOfficeOutgoingStatus Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			String field = "app" + status.getApprovarSeq() + "state";
			// When Approved by final Approver
			if (finalapp == 1 && status.getApprovalstate() == 1) {
				// Get
				HOfficeOutgoing outgoing = getOfficeOutgoingById(status.getOutgoing_id());
				outgoing.escapeEcmaScript();
				// Update
				StringBuffer sql1 = new StringBuffer("UPDATE officeoutgoing SET ");
				sql1.append(field + "=1,status = " + (outgoing.getStatus() + 2) + "," + "deleted = " + status.getDeleted()
						+ "," + "modifierid = '" + status.getModId() + "'" + " WHERE id =" + status.getOutgoing_id());

				logger.info("HOfficeOutgoing Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
				
				HAttendance att = new HAttendance();
				att.setEmp_id(outgoing.getEmp_id());
				att.setAtt_date(df.format(outgoing.getDate()));
				att.setAtt_time(outgoing.getFtime());
				att.setAtt_inout(0);
				att.setModId(status.getModId());
				att.setDeleted(false);
				
				insertHAttendance(att);
				
				att.setAtt_time(outgoing.getTtime());
				att.setAtt_inout(1);
				
				insertHAttendance(att);
			}
			// When Rejected by final Approver
			else if (finalapp == 1 && status.getApprovalstate() == 2) {
			    // Get
				HOfficeOutgoing outgoing = getOfficeOutgoingById(status.getOutgoing_id());
				outgoing.escapeEcmaScript();
				// Update
				StringBuffer sql1 = new StringBuffer("UPDATE officeoutgoing SET ");
				sql1.append(field + "=1,status = 1," + "deleted = " + status.getDeleted() + "," + "modifierid = '"
						+ status.getModId() + "'" + " WHERE id =" + status.getOutgoing_id());

				logger.info("HOfficeOutgoing Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
			}
			// When Approved by others Approver
			else {
				// Update
				StringBuffer sql1 = new StringBuffer("UPDATE officeoutgoing SET ");
				sql1.append(field + "=1," + "deleted = " + status.getDeleted() + "," + "modifierid = '"
						+ status.getModId() + "'" + " WHERE id =" + status.getOutgoing_id());

				logger.info("HOfficeOutgoing Update Query - > " + sql1.toString());

				getJdbcService().getJdbcTemplate().execute(sql1.toString());
			}
			empId = status.getOutgoing_id();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	@Override
	public List<HLeaveStatus> getAllLeaveStatusByLeaveId(boolean isDeleted, int leaveId) {
		List<HLeaveStatus> allLeaveStatus = new ArrayList<HLeaveStatus>();
		try {
			if (!isDeleted)
				allLeaveStatus = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM leavestatus WHERE leavestatus.DELETED = 0 AND leavestatus.leave_id="
								+ leaveId + " ORDER BY approvarSeq", new Object[] {}, new LeaveStatusMapper());
			else
				allLeaveStatus = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM leavestatus WHERE leavestatus.leave_id=" + leaveId + "ORDER BY approvarSeq",
						new Object[] {}, new LeaveStatusMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeaveStatus;
	}
	
	
	@Override
	public List<HOfficeOutgoingStatus> getAllOfficeOutgoingStatusByOutgoingId(boolean isDeleted, int outgoingId) {
		List<HOfficeOutgoingStatus> allOutgoingStatus = new ArrayList<HOfficeOutgoingStatus>();
		try {
			if (!isDeleted)
				allOutgoingStatus = getJdbcService().getJdbcTemplate()
						.query("SELECT * FROM officeoutgoingstatus WHERE DELETED = 0 AND outgoing_id="
								+ outgoingId + " ORDER BY approvarSeq", new Object[] {}, new OfficeOutgoingStatusMapper());
			else
				allOutgoingStatus = getJdbcService().getJdbcTemplate().query(
						"SELECT * FROM officeoutgoingstatus WHERE outgoing_id=" + outgoingId + "ORDER BY approvarSeq",
						new Object[] {}, new OfficeOutgoingStatusMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allOutgoingStatus;
	}

	@Override
	public Integer insertHLeaveApprover(List<HLeaveApprover> approvers) {
		Integer success = 0;
		try {
			int emp_id = 0;// String mod="";
			if (approvers.size() > 0) {
				emp_id = approvers.get(0).getEmp_id();
				// mod = approvers.get(0).getModId();
			}
			// Delete all first
			StringBuffer sql1 = new StringBuffer("DELETE FROM leaveapprover WHERE emp_id=" + emp_id);
			/*
			 * StringBuffer sql1 = new StringBuffer("UPDATE leaveapprover SET "
			 * ); sql1.append("deleted = true," + "modifierid = '" + mod +"'" +
			 * " WHERE emp_id ="+emp_id);
			 */

			logger.info("HLeaveApprover Delete Query - > " + sql1.toString());

			getJdbcService().getJdbcTemplate().execute(sql1.toString());

			// Insert one by one
			for (HLeaveApprover app : approvers) {
				StringBuffer sql = new StringBuffer("INSERT INTO leaveapprover ");
				sql.append("(emp_id, approver_id, deleted, modifierid) VALUES(");
				sql.append(app.getEmp_id() + "," + app.getApprover_id() + "," + app.getDeleted() + ",'" + app.getModId()
						+ "')");

				logger.info("HLeaveApprover Insert Query - > " + sql.toString());

				getJdbcService().getJdbcTemplate().execute(sql.toString());
				success = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	@Override
	public List<HLeaveApprover> getLeaveApprovers(boolean isDeleted, int emp_id) {
		List<HLeaveApprover> allApp = new ArrayList<HLeaveApprover>();
		try {
			allApp = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM leaveapprover WHERE DELETED = " + isDeleted + " AND emp_id = " + emp_id,
					new Object[] {}, new LeaveApproverMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allApp;
	}

	@Override
	public Integer editHLeave(HLeave leave) {
		Integer delStat = -1;
		leave.escapeEcmaScript();
		
	    TransactionStatus status = getJdbcService().getTransactionStatus();
	      
		getJdbcService().beginTran();
		try {
			Float newDays = 0f;
			if(!leave.getDeleted()){
				newDays = leave.getDays();
			}
			StringBuffer sql = new StringBuffer("CALL updateleavequota(" + leave.getId() + "," + newDays + "," + leave.getEmp_id()
					+ "," + leave.getLeavetype().getId() + ",'" + leave.getLeavequota().getYear() + "')");

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			StringBuffer sql1 = new StringBuffer("UPDATE leavesrequest SET ");
			sql1.append("emp_id =" + leave.getEmp_id() + "," + "fdate ='" + leave.getFdate() + "'," + "tdate ='"
					+ leave.getTdate() + "'," + "days =" + leave.getDays() + "," + "leavetype ="
					+ leave.getLeavetype().getId() + "," + "reason ='" + leave.getReason() + "'," + "status ="
					+ leave.getStatus() + "," + "approvar1 =" + leave.getApprovar1() + "," + "approvar2 ="
					+ leave.getApprovar2() + "," + "approvar3 =" + leave.getApprovar3() + "," + "approvar4 ="
					+ leave.getApprovar4() + "," + "approvar5 =" + leave.getApprovar5() + "," + "app1state ="
					+ leave.getApp1state() + "," + "app2state =" + leave.getApp2state() + "," + "app3state ="
					+ leave.getApp3state() + "," + "app4state =" + leave.getApp4state() + "," + "app5state ="
					+ leave.getApp5state() + "," + "deleted =" + leave.getDeleted() + "," + "modifierid = '"
					+ leave.getModId() + "'," + "modifydate = '" + leave.getModifydate() + "'" + " WHERE id ="
					+ leave.getId());

			logger.info("HLeave Update Query - > " + sql1.toString());

			getJdbcService().getJdbcTemplate().execute(sql1.toString());

			delStat = 1;
			getJdbcService().commitTran(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			getJdbcService().rollbackTran(status);
		}

		return delStat;
	}
	
	
	@Override
	public Integer editHOfficeOutgoing(HOfficeOutgoing outgoing) {
		Integer delStat = -1;
		outgoing.escapeEcmaScript();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer sql1 = new StringBuffer("UPDATE officeoutgoing SET ");
			sql1.append("emp_id =" + outgoing.getEmp_id() + "," + "date ='" + df.format(outgoing.getDate()) + "'," + "ftime ='"
					+ outgoing.getFtime() + "'," + "ttime ='" + outgoing.getTtime() + "'," + " reason ='" + outgoing.getReason() 
					+ "'," + "status ="	  + outgoing.getStatus() + "," + "approvar1 =" + outgoing.getApprovar1() + "," + "approvar2 ="
					+ outgoing.getApprovar2() + "," + "approvar3 =" + outgoing.getApprovar3() + "," + "approvar4 ="
					+ outgoing.getApprovar4() + "," + "approvar5 =" + outgoing.getApprovar5() + "," + "app1state ="
					+ outgoing.getApp1state() + "," + "app2state =" + outgoing.getApp2state() + "," + "app3state ="
					+ outgoing.getApp3state() + "," + "app4state =" + outgoing.getApp4state() + "," + "app5state ="
					+ outgoing.getApp5state() + "," + "deleted =" + outgoing.getDeleted() + "," + "modifierid = '"
					+ outgoing.getModId() + "'," + "modifydate = '" + outgoing.getModifydate() + "'" + " WHERE id ="
					+ outgoing.getId());

			logger.info("HOfficeOutgoing Update Query - > " + sql1.toString());

			getJdbcService().getJdbcTemplate().execute(sql1.toString());

			delStat = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return delStat;
	}

	@Override
	public Integer insertHLunch(HLunch lunch) {
		Integer insert = -1;
		lunch.escapeEcmaScript();
		try {

			StringBuffer sql = new StringBuffer("INSERT INTO lunch ");

			sql.append("(emp_id, name, lunch_date, lunch_status, lunch_count, deleted, modifierid) VALUES( ");

			sql.append(lunch.getEmp_id() + ",'" + lunch.getName() + "','" + lunch.getLunch_date() + "',"
					+ lunch.getLunch_status() + "," + lunch.getLunch_count() + "," + lunch.getDeleted() + ",'"
					+ lunch.getModId() + "')");

			logger.info("HLunch Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public Integer insertHLunch(StringBuffer sql) {
		Integer insert = -1;
		try {
			logger.info("HLunch Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public HLunch getLunchByEmp(String date, Integer emp_id) {
		HLunch lunch = null;
		try {
			lunch = getJdbcService().getJdbcTemplate()
					.queryForObject("SELECT * FROM lunch WHERE DELETED = false AND emp_id = " + emp_id
							+ " AND lunch_date='" + date + "'", new Object[] {}, new LunchMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return lunch;
	}

	@Override
	public List<HLunch> getLunchListByDate(boolean isDeleted, boolean isCancel, String date) {
		List<HLunch> allLunch = new ArrayList<HLunch>();
		try {
			allLunch = getJdbcService().getJdbcTemplate().query("SELECT * FROM lunch WHERE DELETED =" + isDeleted
					+ " AND lunch_status = " + isCancel + " AND lunch_date='" + date + "'", new Object[] {},
					new LunchMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLunch;
	}

	@Override
	public Integer editHLunch(HLunch lunch) {
		Integer insert = -1;
		lunch.escapeEcmaScript();
		try {

			StringBuffer sql = new StringBuffer("UPDATE lunch SET ");

			sql.append("emp_id =" + lunch.getEmp_id() + "," + "name ='" + lunch.getName() + "'," + "lunch_date ='"
					+ lunch.getLunch_date() + "'," + "lunch_status =" + lunch.getLunch_status() + "," + "lunch_count ="
					+ lunch.getLunch_count() + "," + "deleted =" + lunch.getDeleted() + "," + "modifierid ='"
					+ lunch.getModId() + "' " + "WHERE lunch_id=" + lunch.getLunch_id());

			logger.info("HLunch Edit Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public HLeave getLeaveByEmpIdAndDate(Integer empid, String date, int status) {
		HLeave leave = new HLeave();
		try {
			leave = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.emp_id ="
							+ empid + " AND leavesrequest.fdate<='" + date + "' AND leavesrequest.tdate>='" + date
							+ "' AND leavesrequest.DELETED = 0 AND leavesrequest.status = " + status,
					new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return leave;
	}

	@Override
	public Integer sendMail(HMail mail) {
		Integer mailId = 0;
		mail.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO mails ");

			sql.append(
					"(mail_from, mail_to, cc, bcc, subject, date, msg, status, sendTime, deleted, modifierid) VALUES( '");

			sql.append(mail.getFrom() + "','" + mail.getTo() + "','" + mail.getCc() + "','" + mail.getBcc() + "','"
					+ mail.getSubject() + "','" + mail.getDate() + "','" + mail.getMsg() + "','" + mail.getStatus()
					+ "','" + mail.getSendTime() + "'," + mail.getDeleted() + ",'" + mail.getModId() + "')");

			logger.info("HMail Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			mailId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mailId;
	}

	@Override
	public List<HMail> getMail(boolean isDeleted, String state) {
		List<HMail> mail = new ArrayList<HMail>();
		try {
			mail = getJdbcService().getJdbcTemplate().query("SELECT * FROM mails WHERE mails.DELETED =" + isDeleted
					+ " AND mails.status In(" + state + ") order by mails.date ASC", new MailMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mail;
	}

	@Override
	public Integer editHMail(HMail mail) {
		Integer insert = -1;
		mail.escapeEcmaScript();
		try {

			StringBuffer sql = new StringBuffer("UPDATE mails SET ");

			sql.append("status =" + mail.getStatus() + "," + "sendTime ='" + mail.getSendTime() + "' "
					+ "WHERE mail_id=" + mail.getMail_id());

			logger.info("HMail Edit Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insert;
	}

	@Override
	public List<HTeam> getAllTeam(boolean isDeleted) {
		List<HTeam> allTeam = new ArrayList<HTeam>();
		try {
			allTeam = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM team WHERE DELETED =" + isDeleted + " ORDER BY teamName ASC", new Object[] {},
					new TeamMapper());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allTeam;
	}

	@Override
	public HTeam getTeamById(int id) {
		HTeam team = new HTeam();
		try {
			team = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM team WHERE teamId =" + id + " AND DELETED = false", new TeamMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return team;
	}

	@Override
	public Integer insertHTeam(HTeam team) {
		Integer teamId = 0;
		team.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO team ");

			sql.append("(teamName, deleted, modifierid) VALUES( '");

			sql.append(team.getTeamName() + "'," + team.getDeleted() + ",'" + team.getModId() + "')");

			logger.info("HTeam Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			teamId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return teamId;
	}

	@Override
	public List<HLeave> getAllApprovedLeaveByApproverId(boolean isDeleted, int approver_id) {
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			if (!isDeleted)
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.DELETED = 0 AND (leavesrequest.approvar1="
								+ approver_id + " OR leavesrequest.approvar2=" + approver_id
								+ " OR leavesrequest.approvar3=" + approver_id + " OR leavesrequest.approvar4="
								+ approver_id + " OR leavesrequest.approvar5=" + approver_id + ") AND status<>0",
						new Object[] {}, new LeaveMapper());
			else
				allLeave = getJdbcService().getJdbcTemplate()
						.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE leavesrequest.approvar1="
								+ approver_id + " OR leavesrequest.approvar2=" + approver_id
								+ " OR leavesrequest.approvar3=" + approver_id + " OR leavesrequest.approvar4="
								+ approver_id + " OR leavesrequest.approvar5=" + approver_id + " AND status<>0",
						new Object[] {}, new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeave;
	}

	@Override
	public List<HLunch> getLunchListByEmp(boolean isDeleted, String stDate, String enDate, int emp_id) {
		List<HLunch> allLunch = new ArrayList<HLunch>();
		try {
			allLunch = getJdbcService()
					.getJdbcTemplate().query(
							"SELECT * FROM lunch WHERE DELETED =" + isDeleted + " AND lunch_date BETWEEN '" + stDate
									+ "' AND '" + enDate + "' AND emp_id=" + emp_id,
							new Object[] {}, new LunchMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLunch;
	}

	@Override
	public List<HLeave> getAllLeaveForMonth(boolean isDeleted, String start, String end, int status) {
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			// allLeave = getJdbcService().getJdbcTemplate().query("SELECT
			// leavesrequest.*,typesofleave.leavetype AS typename FROM
			// leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype =
			// typesofleave.id WHERE fdate >='"+start+"' AND tdate <='"+end+"'
			// AND status="+status+" AND leavesrequest.deleted="+isDeleted, new
			// Object[] {}, new LeaveMapper());
			allLeave = getJdbcService().getJdbcTemplate()
					.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE status="
							+ status + " AND leavesrequest.deleted=" + isDeleted + " AND (fdate between '" + start
							+ "' AND '" + end + "' OR tdate between '" + start + "' AND '" + end + "')",
					new Object[] {}, new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeave;
	}

	@Override
	public List<HLeave> getAllLeaveForMonth(boolean isDeleted, String start, String end, String status) {
		List<HLeave> allLeave = new ArrayList<HLeave>();
		try {
			// allLeave = getJdbcService().getJdbcTemplate().query("SELECT
			// leavesrequest.*,typesofleave.leavetype AS typename FROM
			// leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype =
			// typesofleave.id WHERE fdate >='"+start+"' AND tdate <='"+end+"'
			// AND status="+status+" AND leavesrequest.deleted="+isDeleted, new
			// Object[] {}, new LeaveMapper());
			allLeave = getJdbcService().getJdbcTemplate()
					.query("SELECT leavesrequest.*,typesofleave.leavetype AS typename FROM leavesrequest LEFT JOIN typesofleave ON leavesrequest.leavetype = typesofleave.id WHERE status IN("
							+ status + ") AND leavesrequest.deleted=" + isDeleted + " AND (fdate between '" + start
							+ "' AND '" + end + "' OR tdate between '" + start + "' AND '" + end + "')",
					new Object[] {}, new LeaveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allLeave;
	}

	@Override
	public List<HWorkInfo> getJoiningWorkInfoById(int emp_id) {
		List<HWorkInfo> workInfos = new ArrayList<HWorkInfo>();
		try {
			workInfos = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM workinfo where emp_id = " + emp_id + " AND deleted=false", new Object[] {},
					new WorkInfoMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return workInfos;
	}

	@Override
	public HCardAttendance getLastAttendanceFromDb1() {
		HCardAttendance lastAtt = new HCardAttendance();
		try {
			lastAtt = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM checkinout WHERE DELETED = false ORDER BY ATTID DESC LIMIT 1", new CardAttMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lastAtt;
	}

	@Override
	public Boolean checkCardAttendance(HCardAttendance att) {
		Boolean Fo = false;
		try {
			StringBuffer str = new StringBuffer("SELECT * FROM checkinout WHERE DELETED = false AND CHECKTIME = '"
					+ att.getCHECKTIME() + "' AND USERID = " + att.getUSERID());
			System.out.println("============================= >" + str);
			List<HCardAttendance> todayAtt = getJdbcService().getJdbcTemplate().query(str.toString(), new Object[] {},
					new CardAttMapper());
			if (todayAtt != null && todayAtt.size() > 0) {
				Fo = true;
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		return Fo;
	}

	@Override
	public Integer insertHTypesOfLeave(HTypesOfLeave type) {
		Integer typeId = 0;
		type.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("INSERT INTO typesofleave ");

			sql.append("(leavetype, deleted, modifierid, isquota, iscontinue, leavededuct, deductfrom) VALUES( '");

			sql.append(type.getLeavetype() + "'," + type.getDeleted() + ",'" + type.getModId() + "',"
					+ type.getIsquota() + "," + type.getIscontinue() + "," + type.getLeavededuct() + ","
					+ type.getDeductfrom() + ")");

			logger.info("HTypesOfLeave Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			typeId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return typeId;
	}

	@Override
	public Integer insertHCardAttendance(StringBuffer sql) {
		Integer empId = 0;
		// emp.escapeEcmaScript();
		try {
			/*
			 * StringBuffer sql = new StringBuffer("INSERT INTO checkinout ");
			 * 
			 * sql.append(
			 * "(USERID, CHECKTIME, CHECKTYPE, VERIFYCODE, SENSORID, Memoinfo, WorkCode, sn, UserExtFmt, deleted, modifierid) VALUES( "
			 * );
			 * 
			 * sql.append(att.getUSERID() + ",'" + att.getCHECKTIME() + "','" +
			 * att.getCHECKTYPE() + "'," + att.getVERIFYCODE() + ",'" +
			 * att.getSENSORID() + "','" + att.getMemoinfo() + "','" +
			 * att.getWorkCode() + "','" + att.getSn() + "'," +
			 * att.getUserExtFmt() + "," + att.getDeleted() + ",'" +
			 * att.getModId() + "')");
			 */

			logger.info("HCardAttendance Insert Query - > " + sql.toString());
			getJdbcService().getJdbcTemplate().execute(sql.toString());
			empId = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}

	@Override
	public HEmployee getEmployeeByEmpnumber(long emp_number) {
		HEmployee profile = new HEmployee();
		try {
			profile = getJdbcService().getJdbcTemplate().queryForObject("SELECT * FROM employee WHERE emp_number ='"
					+ emp_number + "' AND DELETED = 0 AND usertype<>'Watcher'", new EmpMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		return profile;
	}

	@Override
	public Integer setAutoLunch(HEmployeeparam param) {
		int insert = -1;
		HEmployeeparam autoLunchState = null;
		try {
			autoLunchState = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM employeeparam WHERE DELETED = false AND emp_id=" + param.getEmp_id(),
					new EmployeeparamMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		try {
			StringBuffer sql = new StringBuffer();
			if (null != autoLunchState) {
				sql = new StringBuffer("Update employeeparam SET autolunchinput =" + param.getAutolunchinput()
						+ " WHERE id=" + autoLunchState.getId());
				logger.info("HEmployeeparam Edit Query - > " + sql.toString());

			} else {
				sql = new StringBuffer("INSERT INTO employeeparam (emp_id,autolunchinput,deleted,modifierid) VALUES("
						+ param.getEmp_id() + "," + param.getAutolunchinput() + "," + param.getDeleted() + ",'"
						+ param.getModId() + "')");
				logger.info("HEmployeeparam Insert Query - > " + sql.toString());
			}
			getJdbcService().getJdbcTemplate().execute(sql.toString());

			insert = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return insert;
	}

	@Override
	public HEmployeeparam getAutoLunch(int emp_id) {
		HEmployeeparam autoLunchState = new HEmployeeparam();
		autoLunchState.setEmp_id(emp_id);
		autoLunchState.setAutolunchinput(false);
		try {
			autoLunchState = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM employeeparam WHERE DELETED = false AND emp_id=" + emp_id,
					new EmployeeparamMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return autoLunchState;
	}

	public Integer editHTypesOfLeave(HTypesOfLeave type) {
		Integer typeId = -1;
		type.escapeEcmaScript();
		try {
			StringBuffer sql = new StringBuffer("UPDATE typesofleave SET ");
			sql.append("leavetype = '" + type.getLeavetype() + "'," + "deleted = " + type.getDeleted() + ","
					+ "modifierid = '" + type.getModId() + "'," + "isquota = " + type.getIsquota() + ","
					+ "iscontinue = " + type.getIscontinue() + "," + "leavededuct = " + type.getLeavededuct() + ","
					+ "deductfrom = " + type.getDeductfrom() + " WHERE id=" + type.getId());

			logger.info("HTypesOfLeave Update Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());
			typeId = type.getId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return typeId;
	}

	@Override
	public Integer insertHLeaveQuota(HLeaveQuota quota) {
		HLeaveQuota leaveQuota = null;

		try {
			leaveQuota = getJdbcService().getJdbcTemplate()
					.queryForObject(
							"SELECT * FROM leavequota WHERE DELETED = false AND emp_id=" + quota.getEmp_id()
									+ " AND type_id=" + quota.getType_id() + " AND year='" + quota.getYear() + "'",
							new LeaveQuotaMapper());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		int quotaId = 0;
		if (null != leaveQuota) {// Update
			try {
				StringBuffer sql = new StringBuffer("UPDATE leavequota SET ");
				sql.append("emp_id = " + quota.getEmp_id() + "," + "type_id = " + quota.getType_id() + "," + "year = '"
						+ quota.getYear() + "'," + "expdate = '" + quota.getExpdate() + "'," + "quota = "
						+ quota.getQuota() + "," + "used = " + quota.getUsed() + "," + "deleted = " + quota.getDeleted()
						+ "," + "modifierid = '" + quota.getModId() + "'" + " WHERE emp_id=" + quota.getEmp_id()
						+ " AND type_id =" + quota.getType_id() + " AND year='" + quota.getYear() + "'");

				logger.info("HLeaveQuota Update Query - > " + sql.toString());

				getJdbcService().getJdbcTemplate().execute(sql.toString());
				quotaId = 1;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {// Insert
			try {
				StringBuffer sql = new StringBuffer("INSERT INTO leavequota ");

				sql.append("(emp_id, type_id, year, expdate, quota, used,deleted,modifierid) VALUES( ");

				sql.append(quota.getEmp_id() + "," + quota.getType_id() + ",'" + quota.getYear() + "','"
						+ quota.getExpdate() + "'," + quota.getQuota() + "," + quota.getUsed() + ","
						+ quota.getDeleted() + ",'" + quota.getModId() + "')");

				logger.info("HLeaveQuota Insert Query - > " + sql.toString());

				getJdbcService().getJdbcTemplate().execute(sql.toString());
				quotaId = 1;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return quotaId;
	}

	@Override
	public List<HLeaveQuota> getLeaveQuotas(int empId, String year) {
		List<HLeaveQuota> leaveQuotas = new ArrayList<HLeaveQuota>();

		try {
			leaveQuotas = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM leavequota WHERE DELETED = false AND emp_id=" + empId + " AND year='" + year + "'",
					new Object[] {}, new LeaveQuotaMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return leaveQuotas;
	}

	@Override
	public HLeaveQuota getLeaveQuota(int empId, String year, int type_id) {
		HLeaveQuota leaveQuotas = new HLeaveQuota();
		HTypesOfLeave leaveType = new HTypesOfLeave();
		try {
			leaveType = getJdbcService().getJdbcTemplate().queryForObject(
					"SELECT * FROM typesofleave WHERE id=" + type_id, new Object[] {}, new TypesOfLeaveMapper());
			int deductFrom = type_id;
			if (leaveType != null && leaveType.getDeductfrom() > 0) {
				deductFrom = leaveType.getDeductfrom();
			}
			leaveQuotas = getJdbcService().getJdbcTemplate()
					.queryForObject("SELECT * FROM leavequota WHERE DELETED = false AND emp_id=" + empId + " AND year='"
							+ year + "' AND type_id=" + deductFrom, new Object[] {}, new LeaveQuotaMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return leaveQuotas;
	}

	@Override
	public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year) {
		List<HMonthlyStatus> status = new ArrayList<HMonthlyStatus>();

		try {
			status = getJdbcService().getJdbcTemplate().query("SELECT * FROM monthlystatus WHERE month='" + mon
					+ "' AND year='" + year + "' order by employee_name", new Object[] {}, new HMonthlyStatusMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return status;
	}

	@Override
	public List<HMonthlyStatus> getHMonthlyStatus(int mon, int year, int emp_id) {
		List<HMonthlyStatus> status = new ArrayList<HMonthlyStatus>();

		try {
			status = getJdbcService().getJdbcTemplate().query("SELECT * FROM monthlystatus WHERE month='" + mon
					+ "' AND year='" + year + "' AND emp_id=" + emp_id, new Object[] {}, new HMonthlyStatusMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean deleteHAttendance(String date) {
		boolean fo = false;
		try {
			StringBuffer sql = new StringBuffer("DELETE FROM attendance WHERE att_date='" + date + "'");
			logger.info("DELETE HAttendance Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			fo = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fo;
	}

	@Override
	public boolean alterHAttendance_Auto_inc(String date) {
		boolean fo = false;
		long lastAtt = 1;
		try {
			lastAtt = getJdbcService().getJdbcTemplate()
					.queryForInt("SELECT Max(att_id) FROM attendance where att_date='" + date + "'");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		lastAtt = lastAtt + 1;
		try {
			StringBuffer sql = new StringBuffer("ALTER TABLE attendance AUTO_INCREMENT =" + lastAtt);
			logger.info("Alter HAttendance Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			fo = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fo;
	}

	@Override
	public boolean insertHMonthlyStatus(List<HMonthlyStatus> status) {
		boolean fo = false;
		if (status == null || status.size() == 0)
			return false;

		String mon = status.get(0).getMonth();
		String year = status.get(0).getYear();
		try {
			StringBuffer sql = new StringBuffer(
					"DELETE FROM monthlystatus WHERE month='" + mon + "' AND year='" + year + "'");
			logger.info("DELETE Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			StringBuffer sql = new StringBuffer("INSERT INTO monthlystatus ");

			sql.append(
					"(emp_id, employee_name, month, year, total_working_day, present, absent, leaves, lwp, late, overtimehours, deficithours, holidayovertime, totalworkinghours, absulateworkinghours, presentWorkingHours) VALUES ");

			int count = status.size();
			for (int i = 0; i < count; i++) {
				HMonthlyStatus st = status.get(i);
				if ((i + 1) == count) {// last record
					sql.append("(" + st.getEmp_id() + ",'" + st.getEmployee_name() + "','" + st.getMonth() + "','"
							+ st.getYear() + "'," + st.getTotal_working_day() + "," + st.getPresent() + ","
							+ st.getAbsent() + "," + st.getLeave() + "," +st.getLwp()+ "," + st.getLate() + ",'" + st.getOvertimehours()
							+ "','" + st.getDeficithours() + "','" + st.getHolidayovertime() + "','"
							+ st.getTotalworkinghours() + "','" + st.getAbsulateworkinghours() + "','"
							+ st.getPresentWorkingHours() + "')");
				} else {
					sql.append("(" + st.getEmp_id() + ",'" + st.getEmployee_name() + "','" + st.getMonth() + "','"
							+ st.getYear() + "'," + st.getTotal_working_day() + "," + st.getPresent() + ","
							+ st.getAbsent() + "," + st.getLeave() + "," +st.getLwp()+ "," + st.getLate() + ",'" + st.getOvertimehours()
							+ "','" + st.getDeficithours() + "','" + st.getHolidayovertime() + "','"
							+ st.getTotalworkinghours() + "','" + st.getAbsulateworkinghours() + "','"
							+ st.getPresentWorkingHours() + "'),");
				}
			}
			logger.info("insertHMonthlyStatus Insert Query - > " + sql.toString());

			getJdbcService().getJdbcTemplate().execute(sql.toString());

			fo = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fo;
	}

	@Override
	public List<HLeaveQuota> getQuotaYear() {
		List<HLeaveQuota> year = new ArrayList<HLeaveQuota>();

		try {
			year = getJdbcService().getJdbcTemplate().query(
					"SELECT distinct year FROM leavequota", new Object[] {},
					//SELECT distinct year FROM leavequota WHERE expdate >= now()
					new LeaveQuotaMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return year;
	}
	
	@Override
	public Integer insertHOfficeOutgoingApprover(List<HOfficeOutgoingApprover> approvers) {
		Integer success = 0;
		try {
			int emp_id = 0;// String mod="";
			if (approvers.size() > 0) {
				emp_id = approvers.get(0).getEmp_id();
				// mod = approvers.get(0).getModId();
			}
			// Delete all first
			StringBuffer sql1 = new StringBuffer("DELETE FROM officeoutgoingapprover WHERE emp_id=" + emp_id);
			/*
			 * StringBuffer sql1 = new StringBuffer("UPDATE leaveapprover SET "
			 * ); sql1.append("deleted = true," + "modifierid = '" + mod +"'" +
			 * " WHERE emp_id ="+emp_id);
			 */

			logger.info("HOfficeOutgoingApprover Delete Query - > " + sql1.toString());

			getJdbcService().getJdbcTemplate().execute(sql1.toString());

			// Insert one by one
			for (HOfficeOutgoingApprover app : approvers) {
				StringBuffer sql = new StringBuffer("INSERT INTO officeoutgoingapprover ");
				sql.append("(emp_id, approver_id, deleted, modifierid) VALUES(");
				sql.append(app.getEmp_id() + "," + app.getApprover_id() + "," + app.getDeleted() + ",'" + app.getModId()
						+ "')");

				logger.info("HOfficeOutgoingApprover Insert Query - > " + sql.toString());

				getJdbcService().getJdbcTemplate().execute(sql.toString());
				success = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	@Override
	public List<HOfficeOutgoingApprover> getOfficeOutgoingApprovers(boolean isDeleted, int emp_id) {
		List<HOfficeOutgoingApprover> allApp = new ArrayList<HOfficeOutgoingApprover>();
		try {
			allApp = getJdbcService().getJdbcTemplate().query(
					"SELECT * FROM officeoutgoingapprover WHERE DELETED = " + isDeleted + " AND emp_id = " + emp_id,
					new Object[] {}, new OfficeOutgoingApproverMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allApp;
	}
	
	@Override
	public List<HSupportingData> getSupportingData(String type) {
	    List<HSupportingData> allType = new ArrayList<HSupportingData>();

	    try {
		allType = getJdbcService().getJdbcTemplate().query(
			    "SELECT * from supportingdata WHERE valuetype = '"+type+"'", new Object[] {},
			    new SupportingDataMapper());
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    return allType;
	}

}

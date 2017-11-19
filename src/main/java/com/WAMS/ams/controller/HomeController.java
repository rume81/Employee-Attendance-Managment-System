package com.WAMS.ams.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import com.webhawks.Hawks_model.HAttendance;
import com.webhawks.Hawks_model.HCardAttendance;
import com.webhawks.Hawks_model.HEmpLeaveQuota;
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
import com.WAMS.ams.model.UserSession;
import com.WAMS.ams.services.interfaces.IWamsService;
/*import com.skype.Chat;
import com.skype.Group;
import com.skype.Skype;*/

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private ApplicationContext applicationContext;

    private IWamsService wamsService;

    private String ipList;

    private String timeAdjust;

    private String autolunchentry;

    private JavaMailSenderImpl mailSender;

    private static String lastURL;

    public void setWamsService(IWamsService wamsService) {
	this.wamsService = wamsService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	this.applicationContext = applicationContext;

    }

    public void setIpList(String ipList) {
	this.ipList = ipList;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
	this.mailSender = mailSender;
    }

    public void setTimeAdjust(String timeAdjust) {
	this.timeAdjust = timeAdjust;
    }

    public void setAutolunchentry(String autolunchentry) {
	this.autolunchentry = autolunchentry;
    }

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (validSession) {
	    return new ModelAndView("redirect:/home");
	}

	return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView validation(HttpServletRequest request) throws Exception {

	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (validSession) {
	    return new ModelAndView("redirect:/");
	}

	return new ModelAndView("login", mm);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    if (null != lastURL && !lastURL.equals("")) {
		String redirecturl = lastURL;
		lastURL = "";
		return new ModelAndView("redirect:" + redirecturl);
	    }
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if (emp.getUsertype().equalsIgnoreCase("Watcher")) {
		return new ModelAndView("redirect:/monthlyattendance");
	    }
	    mm.addAttribute("selUrl", "home");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    String ClientIP = getSessionService().getUserSession().getClientIpAddress();
	    /*
	     * String ips[] = ipList.split("#"); boolean ipFound = false;
	     * for(String ip:ips) { if(ClientIP.equals(ip)) { ipFound = true;
	     * break; } }
	     * 
	     * if(!ipFound) { return new ModelAndView("outsidehome", mm); } else
	     * {
	     */
	    //List<HCardAttendance> test = wamsService.getCardAttendanceOfDate("2017-10-10 12:48:39");
	    
	    List<HAttendance> todayAtt = new ArrayList<HAttendance>();
	    Date dd = new Date();
	    String curDate = DateFormateforDB().format(dd);
	    Integer emp_id = emp.getEmp_id();
	    todayAtt = wamsService.getTodayAttendance(curDate, emp_id);

	    curDate = DateFormateforView().format(dd);
	    Integer inOut = 1;
	    Integer lastSeq = 0;
	    for (HAttendance att : todayAtt) {
		if (att.getDay_sequence() > lastSeq) {
		    lastSeq = att.getDay_sequence();
		    inOut = att.getAtt_inout();
		    curDate = att.getAtt_date();
		}
	    }

	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));
	    List<HMonthlyStatus> monthlyStates = wamsService.getHMonthlyStatus(mon, year, emp.getEmp_id());
	    
	    String sysDate = DateFormateforViewWithTime().format(dd);
	    
	    mm.addAttribute("att", todayAtt);
	    mm.addAttribute("inOut", inOut);
	    mm.addAttribute("curDate", curDate);
	    mm.addAttribute("sysDate", sysDate);
	    mm.addAttribute("matt", monthlyStates);

	    // }
	}
	return new ModelAndView("home", mm);
    }

    @RequestMapping(value = "/empinfo", method = RequestMethod.GET)
    public ModelAndView empInfo(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) || (emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		allEmp = wamsService.getAllEmployee(false);
		mm.addAttribute("selUrl", "adminpanel");
	    } else {
		allEmp.add(emp);
		mm.addAttribute("selUrl", "empinfo");
	    }
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("employeeinfo", mm);
    }

    @RequestMapping(value = "/watcherinfo", method = RequestMethod.GET)
    public ModelAndView watcherInfo(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    /*
	     * if(emp.getUsertype().equalsIgnoreCase("Admin")) {
	     */
	    allEmp = wamsService.getAllEmployee(false, "Watcher");
	    /*
	     * } else{ allEmp.add(emp); }
	     */
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("watcherinfo", mm);
    }

    @RequestMapping(value = "/employeeAdd", method = RequestMethod.GET)
    public ModelAndView addNewemp(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	    
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);
	mm.addAttribute("selUrl", "adminpanel");

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    List<HSupportingData> userType = wamsService.getSupportingData("USERTYPE");
	    List<HSupportingData> designation = wamsService.getSupportingData("POSITION");
	    List<HSupportingData> blood = wamsService.getSupportingData("BLOOD");
		
	    mm.addAttribute("emp", emp);
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    mm.addAttribute("userType", userType);
	    mm.addAttribute("desig", designation);
	    mm.addAttribute("blood", blood);
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	}

	return new ModelAndView("employeeadd", mm);
    }

    @RequestMapping(value = "/watcherAdd", method = RequestMethod.GET)
    public ModelAndView addNewWatcher(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);
	mm.addAttribute("selUrl", "adminpanel");

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	}

	return new ModelAndView("watcheradd", mm);
    }

    @RequestMapping(value = "/empWorkInfo", method = RequestMethod.GET)
    public ModelAndView empWorkInfo(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		allEmp = wamsService.getAllEmployee(false);
	    } else {
		allEmp.add(emp);
	    }
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("employeeworkinfolist", mm);
    }

    @RequestMapping(value = "/addEmployeeWorkInfo/{id}", method = RequestMethod.GET)
    public ModelAndView addEmployeeWorkInfo(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("EmpId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HEmployee empInfo = wamsService.getEmployeeById(id);
	    mm.addAttribute("selemp", empInfo);
	    HWorkInfo work = wamsService.getActiveWorkInfo(id);
	    mm.addAttribute("workinfo", work);
	    List<HLeaveApprover> approvers = wamsService.getLeaveApprovers(false, id);
	    List<HOfficeOutgoingApprover> offapprovers = wamsService.getOfficeOutgoingApprovers(false, id);

	    List<HEmployee> tempEmp = new ArrayList<HEmployee>();
	    mm.addAttribute("approvers", approvers);
	    mm.addAttribute("offapprovers", offapprovers);
	    tempEmp = wamsService.getAllEmployee(false);

	    for (HEmployee curemp : tempEmp) {
		if (curemp.getEmp_id() == empInfo.getEmp_id()) {
		    tempEmp.remove(curemp);
		    break;
		}
	    }
	    List<HEmployee> allEmp1 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp2 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp3 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp4 = new ArrayList<HEmployee>();

	    int size = tempEmp.size();
	    if (size == 1)
		allEmp1 = tempEmp;
	    else {
		int last = size / 4;
		for (int i = 0; i < last; i++) {
		    allEmp1.add(tempEmp.get(i));
		}
		for (int i = last; i < (last * 2); i++) {
		    allEmp2.add(tempEmp.get(i));
		}
		for (int i = (last * 2); i < (last * 3); i++) {
		    allEmp3.add(tempEmp.get(i));
		}
		for (int i = (last * 3); i < size; i++) {
		    allEmp4.add(tempEmp.get(i));
		}
	    }
	    mm.addAttribute("allEmp1", allEmp1);
	    mm.addAttribute("allEmp2", allEmp2);
	    mm.addAttribute("allEmp3", allEmp3);
	    mm.addAttribute("allEmp4", allEmp4);
	}

	return new ModelAndView("employeeworkinfo", mm);
    }

    @RequestMapping(value = "/employeeautolunchsetup", method = RequestMethod.GET)
    public ModelAndView employeeautolunchsetup(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> tempEmp = new ArrayList<HEmployee>();
	    List<HEmployeeparam> emplunch = new ArrayList<HEmployeeparam>();

	    tempEmp = wamsService.getAllEmployee(false);

	    List<HEmployee> allEmp1 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp2 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp3 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp4 = new ArrayList<HEmployee>();

	    int size = tempEmp.size();
	    if (size == 1)
		allEmp1 = tempEmp;
	    else {
		int last = size / 4;
		for (int i = 0; i < last; i++) {
		    allEmp1.add(tempEmp.get(i));
		    HEmployeeparam param = wamsService.getAutoLunch(tempEmp.get(i).getEmp_id());
		    emplunch.add(param);
		}
		for (int i = last; i < (last * 2); i++) {
		    allEmp2.add(tempEmp.get(i));
		    HEmployeeparam param = wamsService.getAutoLunch(tempEmp.get(i).getEmp_id());
		    emplunch.add(param);
		}
		for (int i = (last * 2); i < (last * 3); i++) {
		    allEmp3.add(tempEmp.get(i));
		    HEmployeeparam param = wamsService.getAutoLunch(tempEmp.get(i).getEmp_id());
		    emplunch.add(param);
		}
		for (int i = (last * 3); i < size; i++) {
		    allEmp4.add(tempEmp.get(i));
		    HEmployeeparam param = wamsService.getAutoLunch(tempEmp.get(i).getEmp_id());
		    emplunch.add(param);
		}
	    }
	    mm.addAttribute("allEmp1", allEmp1);
	    mm.addAttribute("allEmp2", allEmp2);
	    mm.addAttribute("allEmp3", allEmp3);
	    mm.addAttribute("allEmp4", allEmp4);
	    mm.addAttribute("emplunch", emplunch);
	}

	return new ModelAndView("employeeautolunchsetup", mm);
    }

    @RequestMapping(value = "/employeeWorkInfoDisplay/{id}", method = RequestMethod.GET)
    public ModelAndView employeeWorkInfoDisplay(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("EmpId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HEmployee empInfo = wamsService.getEmployeeById(id);
	    mm.addAttribute("selemp", empInfo);
	    HWorkInfo work = wamsService.getActiveWorkInfo(id);
	    mm.addAttribute("workinfo", work);
	    List<HLeaveApprover> approver = wamsService.getLeaveApprovers(false, id);
	    List<HOfficeOutgoingApprover> offapprover = wamsService.getOfficeOutgoingApprovers(false, id);
	    mm.addAttribute("approver", approver);
	    mm.addAttribute("offapprover", offapprover);
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("employeeworkinfodisplay", mm);
    }

    @RequestMapping(value = "/holidainfo", method = RequestMethod.GET)
    public ModelAndView holidaInfo(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HHoliday> allHoliday = new ArrayList<HHoliday>();
	    Date dd = new Date();
	    SimpleDateFormat df = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(df.format(dd));
	    allHoliday = wamsService.getAllHoliday(false);

	    mm.addAttribute("allHoliday", allHoliday);
	}

	return new ModelAndView("holidayinfo", mm);
    }

    @RequestMapping(value = "/monthlyattendance", method = RequestMethod.GET)
    public ModelAndView monthlyAtt(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "monthlyattendance");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));

	    int emp_id = emp.getEmp_id();

	    List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
	    thisMonAtt = wamsService.getAttendanceForMonth(mon, year, emp_id, false);

	    mm.addAttribute("monAtt", thisMonAtt);
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    if (emp.getUsertype().equalsIgnoreCase("Admin") || emp.getUsertype().equalsIgnoreCase("Watcher")
		    || emp.getUsertype().equalsIgnoreCase("Manager") || (emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		allEmp = wamsService.getAllEmployee(false);
	    } else {
		allEmp.add(emp);
	    }
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("monthlyattendance", mm);
    }

    @RequestMapping(value = "/downloadattendance", method = RequestMethod.GET)
    public ModelAndView downloadAttendance(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "downloadattendance");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> tempEmp = new ArrayList<HEmployee>();

	    tempEmp = wamsService.getAllEmployee(false);
	    List<HEmployee> allEmp1 = new ArrayList<HEmployee>();
	    List<HEmployee> allEmp2 = new ArrayList<HEmployee>();

	    int size = tempEmp.size();
	    if (size == 1)
		allEmp1 = tempEmp;
	    else {
		for (int i = 0; i < (size / 2); i++) {
		    allEmp1.add(tempEmp.get(i));
		}
		for (int i = (size / 2); i < size; i++) {
		    allEmp2.add(tempEmp.get(i));
		}
	    }
	    mm.addAttribute("allEmp1", allEmp1);
	    mm.addAttribute("allEmp2", allEmp2);
	}

	return new ModelAndView("downloadAttendance", mm);
    }

    @RequestMapping(value = "/employeeEdit/{id}", method = RequestMethod.GET)
    public ModelAndView editEmp(@PathVariable int id, HttpServletRequest request) throws Exception {

	logger.info("EmpId=" + id);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    List<HSupportingData> userType = wamsService.getSupportingData("USERTYPE");
	    List<HSupportingData> designation = wamsService.getSupportingData("POSITION");
	    List<HSupportingData> blood = wamsService.getSupportingData("BLOOD");
	    
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    mm.addAttribute("userType", userType);
	    mm.addAttribute("desig", designation);
	    mm.addAttribute("blood", blood);
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    /*
	     * if(!emp.getUsertype().equalsIgnoreCase("Admin")) { return new
	     * ModelAndView("redirect:/"); }
	     */
	    if ((id != emp.getEmp_id()) && (!emp.getUsertype().equals("Admin"))) {
		id = emp.getEmp_id();
	    }
	    HEmployee empInfo = wamsService.getEmployeeById(id);

	    mm.addAttribute("empInfo", empInfo);
	}

	return new ModelAndView("employeeedit", mm);
    }

    @RequestMapping(value = "/watcherEdit/{id}", method = RequestMethod.GET)
    public ModelAndView watcherEdit(@PathVariable int id, HttpServletRequest request) throws Exception {

	logger.info("WatcherId=" + id);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HEmployee empInfo = wamsService.getEmployeeById(id);

	    mm.addAttribute("empInfo", empInfo);
	}

	return new ModelAndView("watcheredit", mm);
    }

    @RequestMapping(value = "/EmployeeAttendance", method = RequestMethod.GET)
    public ModelAndView downloadEmpAtt(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    Map<String, String[]> parameterMap = request.getParameterMap();
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    int mon = Integer.parseInt(parameterMap.get("mon")[0]);
	    int year = Integer.parseInt(parameterMap.get("year")[0]);
	    String[] emp_id = parameterMap.get("chk");
	    Map<String, List<HMAttendance>> attList = new HashMap<String, List<HMAttendance>>();
	    for (int i = 0; i < emp_id.length; i++) {
		List<HMAttendance> att = new ArrayList<HMAttendance>();
		att = wamsService.getAttendanceForMonth(mon, year, Integer.parseInt(emp_id[i]), false);
		String empName = wamsService.getEmployeeById(Integer.parseInt(emp_id[i])).getEmployee_name();
		attList.put(empName, att);
	    }

	    return new ModelAndView("AttendanceListExcel", "attList", attList);
	}
    }

    @RequestMapping(value = "/dailyEmpAttdownload", method = RequestMethod.GET)
    public ModelAndView downloadEmpDailyAtt(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Map<String, String[]> parameterMap = request.getParameterMap();

	    String dd = parameterMap.get("date")[0];
	    String action = parameterMap.get("action")[0];

	    Date date = new Date();
	    if (!dd.equals("")) {
		try {
		    date = DateFormateforView().parse(dd);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	    }
	    List<HMAttendance> att = wamsService.getAttendanceByDay(date);
	    if (action.equalsIgnoreCase("PDF")) {
		return new ModelAndView("DailyAttendanceListPdf", "att", att);
	    } else {
		return new ModelAndView("DailyAttendanceListExcel", "att", att);
	    }
	}
    }

    @RequestMapping(value = "/dailyEmpAttdownloadByTeam", method = RequestMethod.GET)
    public ModelAndView downloadEmpDailyAttByTeam(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Map<String, String[]> parameterMap = request.getParameterMap();

	    String dd = parameterMap.get("date")[0];
	    String team = parameterMap.get("team")[0];
	    int teamid = 0;
	    Date date = new Date();
	    if (!dd.equals("")) {
		try {
		    date = DateFormateforView().parse(dd);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	    }
	    if (team.equals("")) {
		teamid = emp.getTeamId();
	    } else {
		teamid = Integer.parseInt(team);
	    }

	    // List<HEmployee> absEmp = new ArrayList<HEmployee>();

	    // String dateView = DateFormateforDB().format(dd);
	    HTeam myTeam = wamsService.getTeamById(teamid);
	    List<HMAttendance> tempatt = wamsService.getAttendanceByDay(date);
	    List<HMAttendance> att = new ArrayList<HMAttendance>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HMAttendance aAtt : tempatt) {
		for (HEmployee aEmp : allEmp) {
		    if (aAtt.getEmp_id() == aEmp.getEmp_id()) {
			if (aEmp.getTeamId() != null && emp.getTeamId() != null && aEmp.getTeamId() == teamid) {

			    String start = "", end = "";
			    if (!aAtt.getAtt_in().equals(""))
				start = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_in());
			    if (!aAtt.getAtt_out().equals(""))
				end = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_out());

			    if ((!start.equals("")) && (!end.equals(""))) {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				Date d1 = null;
				Date d2 = null;

				try {
				    d1 = format.parse(start);
				    d2 = format.parse(end);

				    // in milliseconds
				    long diff = d2.getTime() - d1.getTime();

				    long diffSeconds = diff / 1000 % 60;
				    long diffMinutes = diff / (60 * 1000) % 60;
				    long diffHours = diff / (60 * 60 * 1000) % 24;
				    long diffDays = diff / (24 * 60 * 60 * 1000);

				    String wrkTime = diffHours + " Hours " + diffMinutes + " Minutes";
				    aAtt.setWrkTime(wrkTime);

				} catch (Exception e) {
				    e.printStackTrace();
				}
			    }
			    aAtt.setTeam(myTeam.getTeamName());
			    att.add(aAtt);
			    break;
			}
		    }
		}
	    }

	    // return excel view
	    return new ModelAndView("PdfAttendanceReportView", "att", att);
	}

    }

    @RequestMapping(value = "/attendancemanipulation", method = RequestMethod.GET)
    public ModelAndView attendanceManipulation(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    String ClientIP = getSessionService().getUserSession().getClientIpAddress();
	    /*
	     * String ips[] = ipList.split("#"); boolean ipFound = false;
	     * for(String ip:ips) { if(ClientIP.equals(ip)) { ipFound = true;
	     * break; } }
	     * 
	     * if(!ipFound) { return new ModelAndView("outsidehome", mm); } else
	     * {
	     */
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> tempEmp = new ArrayList<HEmployee>();

	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) || (emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		allEmp = wamsService.getAllEmployee(false);
	    } else {
		allEmp.add(emp);
	    }
	    mm.addAttribute("allEmp", allEmp);
	}
	// }

	return new ModelAndView("attendancemanipulation", mm);
    }

    @RequestMapping(value = "/attendancestatus", method = RequestMethod.GET)
    public ModelAndView attendanceStatus(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> absEmp = new ArrayList<HEmployee>();
	    Date dd = new Date();
	    String date = DateFormateforDB().format(dd);
	    absEmp = wamsService.getAttendanceInStatus(date);
	    List<HMAttendance> att = wamsService.getAttendanceByDay(dd);
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("att", att);
	    mm.addAttribute("absEmp", absEmp);
	}

	return new ModelAndView("attendancestatus", mm);
    }

    @RequestMapping(value = "/attendancestatusbyteam", method = RequestMethod.GET)
    public ModelAndView attendanceStatusbyteam(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> absEmp = new ArrayList<HEmployee>();
	    Date dd = new Date();
	    String date = DateFormateforDB().format(dd);
	    absEmp = wamsService.getAttendanceInStatus(date);
	    List<HMAttendance> tempatt = wamsService.getAttendanceByDay(dd);
	    List<HMAttendance> att = new ArrayList<HMAttendance>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HMAttendance aAtt : tempatt) {
		for (HEmployee aEmp : allEmp) {
		    if (aAtt.getEmp_id() == aEmp.getEmp_id()) {
			if (aEmp.getTeamId() != null && emp.getTeamId() != null
				&& aEmp.getTeamId() == emp.getTeamId()) {

			    String start = "", end = "";
			    if (!aAtt.getAtt_in().equals(""))
				start = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_in());
			    if (!aAtt.getAtt_out().equals(""))
				end = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_out());

			    if ((!start.equals("")) && (!end.equals(""))) {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

				Date d1 = null;
				Date d2 = null;

				try {
				    d1 = format.parse(start);
				    d2 = format.parse(end);

				    // in milliseconds
				    long diff = d2.getTime() - d1.getTime();

				    long diffSeconds = diff / 1000 % 60;
				    long diffMinutes = diff / (60 * 1000) % 60;
				    long diffHours = diff / (60 * 60 * 1000) % 24;
				    long diffDays = diff / (24 * 60 * 60 * 1000);

				    String wrkTime = diffHours + " Hours " + diffMinutes + " Minutes";
				    aAtt.setWrkTime(wrkTime);

				} catch (Exception e) {
				    e.printStackTrace();
				}
			    }
			    att.add(aAtt);
			    break;
			}
		    }
		}
	    }
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("att", att);
	    mm.addAttribute("absEmp", absEmp);
	}

	return new ModelAndView("teamattendancestatus", mm);
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public ModelAndView userLogout(HttpServletRequest request) throws Exception {

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	getSessionService().invalidateSession(getSessionService().getUserSession().getSessionId());

	return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/leaverequest", method = RequestMethod.GET)
    public ModelAndView leaveRequest(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "leavelist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    List<HEmployee> aEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		if (employee.getEmp_id() == emp.getEmp_id()) {
		    allEmp.remove(employee);
		    break;
		}
	    }
	    List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);
	    List<HLeaveApprover> approver = wamsService.getLeaveApprovers(false, emp.getEmp_id());
	    mm.addAttribute("approver", approver);

	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("aEmp", aEmp);

	    // Leave Quotas
	    Date today = new Date();
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curyy = Integer.parseInt(dfy.format(today));
	    int curmm = Integer.parseInt(dfm.format(today));

	    List<HLeaveQuota> quotaYear = wamsService.getQuotaYear();
	    mm.addAttribute("quotaYear", quotaYear);

	    String selectYear = "";
	    if (curmm <= 6) {
		selectYear = String.valueOf(curyy - 1) + "/" + String.valueOf(curyy).substring(2);
	    } else {
		selectYear = String.valueOf(curyy) + "/" + String.valueOf(curyy + 1).substring(2);
	    }
	    mm.addAttribute("selectYear", selectYear);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(emp.getEmp_id(), selectYear);

	    mm.addAttribute("quotas", quotas);

	    List<HTypesOfLeave> leavetype = new ArrayList<HTypesOfLeave>();
	    for (HTypesOfLeave type : allleavetype) {
		int typeid = type.getId();
		int deductfromid = type.getDeductfrom();
		if ((type.getIsquota()) || (deductfromid != 0)) {
		    for (HLeaveQuota quota : quotas) {
			if ((quota.getType_id() == typeid) || (quota.getType_id() == deductfromid)) {
			    if ((quota.getQuota() - quota.getUsed()) >= type.getLeavededuct()) {
				leavetype.add(type);
			    }
			}
		    }
		} else {
		    leavetype.add(type);
		}
	    }

	    mm.addAttribute("leavetype", leavetype);
	    mm.addAttribute("allleavetype", allleavetype);
	    // Leave Quotas
	}
	return new ModelAndView("leave", mm);
    }

    @RequestMapping(value = "/officeoutgoingrequest", method = RequestMethod.GET)
    public ModelAndView officeoutgoingrequest(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "officeoutgoinglist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    List<HEmployee> aEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		if (employee.getEmp_id() == emp.getEmp_id()) {
		    allEmp.remove(employee);
		    break;
		}
	    }

	    List<HOfficeOutgoingApprover> approver = wamsService.getOfficeOutgoingApprovers(false, emp.getEmp_id());
	    mm.addAttribute("approver", approver);

	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("aEmp", aEmp);
	}
	return new ModelAndView("officeoutgoing", mm);
    }

    @RequestMapping(value = "/editleaverequest/{id}", method = RequestMethod.GET)
    public ModelAndView editleaverequest(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("LeaveId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    // List<HEmployee> aEmp = wamsService.getAllEmployee(false);

	    HLeave curleave = wamsService.getLeaveById(id);
	    HEmployee curEmp = wamsService.getEmployeeById(curleave.getEmp_id());

	    List<HTypesOfLeave> leavetype = wamsService.getLeaveType(false);
	    List<HLeaveApprover> approver = wamsService.getLeaveApprovers(false, emp.getEmp_id());
	    mm.addAttribute("approver", approver);
	    mm.addAttribute("leavetype", leavetype);
	    mm.addAttribute("curEmp", curEmp);
	    // mm.addAttribute("aEmp",aEmp);
	    mm.addAttribute("leave", curleave);

	}
	return new ModelAndView("editleave", mm);
    }

    @RequestMapping(value = "/editleavebyIndivitual/{id}", method = RequestMethod.GET)
    public ModelAndView editleavebyIndivitual(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("LeaveId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "leavelist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    // List<HEmployee> aEmp = wamsService.getAllEmployee(false);

	    HLeave curleave = wamsService.getLeaveById(id);
	    HEmployee curEmp = wamsService.getEmployeeById(curleave.getEmp_id());

	    // List<HTypesOfLeave> leavetype = wamsService.getLeaveType(false);
	    List<HLeaveApprover> approver = wamsService.getLeaveApprovers(false, emp.getEmp_id());

	    List<String> nod = wamsService.calculateLeaveDate(curleave.getFdate(), curleave.getTdate(),
		    curleave.getEmp_id(), 1);
	    if (nod.size() > 0) {
		mm.addAttribute("nod", nod);
	    }

	    // Leave Quotas
	    List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);
	    /*
	     * Date today = new Date(); SimpleDateFormat dfy = new
	     * SimpleDateFormat("yyyy"); SimpleDateFormat dfm = new
	     * SimpleDateFormat("MM"); int curyy =
	     * Integer.parseInt(dfy.format(today)); int curmm =
	     * Integer.parseInt(dfm.format(today));
	     */

	    // List<HLeaveQuota> quotaYear = wamsService.getQuotaYear();
	    // mm.addAttribute("quotaYear",quotaYear);

	    /*
	     * String selectYear = ""; if(curmm<=6){ selectYear =
	     * String.valueOf(curyy-1)+"/"+String.valueOf(curyy).substring(2); }
	     * else{ selectYear =
	     * String.valueOf(curyy)+"/"+String.valueOf(curyy+1).substring(2); }
	     */
	    // mm.addAttribute("selectYear",selectYear);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(emp.getEmp_id(), curleave.getLeavequota().getYear());

	    mm.addAttribute("quotas", quotas);

	    List<HTypesOfLeave> leavetype = new ArrayList<HTypesOfLeave>();
	    for (HTypesOfLeave type : allleavetype) {
		int typeid = type.getId();
		int deductfromid = type.getDeductfrom();
		if ((type.getIsquota()) || (deductfromid != 0)) {
		    for (HLeaveQuota quota : quotas) {
			if ((quota.getType_id() == typeid) || (quota.getType_id() == deductfromid)) {
			    if ((quota.getQuota() - quota.getUsed()) >= type.getLeavededuct()) {
				leavetype.add(type);
			    }
			}
		    }
		} else {
		    leavetype.add(type);
		}
	    }

	    mm.addAttribute("leavetype", leavetype);
	    mm.addAttribute("allleavetype", allleavetype);
	    // Leave Quotas

	    mm.addAttribute("approver", approver);
	    // mm.addAttribute("leavetype",leavetype);
	    mm.addAttribute("curEmp", curEmp);
	    // mm.addAttribute("aEmp",aEmp);
	    mm.addAttribute("leave", curleave);

	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    List<HEmployee> aEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		if (employee.getEmp_id() == emp.getEmp_id()) {
		    allEmp.remove(employee);
		    break;
		}
	    }

	    mm.addAttribute("allEmp", allEmp);

	}
	return new ModelAndView("editleaveindividual", mm);
    }

    @RequestMapping(value = "/editoutgoingbyIndivitual/{id}", method = RequestMethod.GET)
    public ModelAndView editoutgoingbyIndivitual(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("OutgoingId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "officeoutgoinglist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    HOfficeOutgoing curoutgoing = wamsService.getOfficeOutgoingById(id);
	    // curoutgoing.setDate(convertStringToDateFormateForView(curoutgoing.getDate()));
	    curoutgoing.setFtime(revartTimeFormat(curoutgoing.getFtime()));
	    curoutgoing.setTtime(revartTimeFormat(curoutgoing.getTtime()));
	    HEmployee curEmp = wamsService.getEmployeeById(curoutgoing.getEmp_id());

	    List<HOfficeOutgoingApprover> approver = wamsService.getOfficeOutgoingApprovers(false, emp.getEmp_id());

	    mm.addAttribute("approver", approver);
	    mm.addAttribute("curEmp", curEmp);
	    mm.addAttribute("outgoing", curoutgoing);

	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    List<HEmployee> aEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		if (employee.getEmp_id() == emp.getEmp_id()) {
		    allEmp.remove(employee);
		    break;
		}
	    }

	    mm.addAttribute("allEmp", allEmp);

	}
	return new ModelAndView("editoutgoingindividual", mm);
    }

    @RequestMapping(value = "/leavelist", method = RequestMethod.GET)
    public ModelAndView leavelist(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "leavelist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HLeave> allLeave = new ArrayList<HLeave>();

	    allLeave = wamsService.getAllLeaveByEmpId(false, emp.getEmp_id());

	    mm.addAttribute("leaves", allLeave);
	}

	return new ModelAndView("leaveinfo", mm);
    }

    @RequestMapping(value = "/officeoutgoinglist", method = RequestMethod.GET)
    public ModelAndView officeoutgoinglist(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "officeoutgoinglist");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HOfficeOutgoing> allOutgoing = new ArrayList<HOfficeOutgoing>();

	    allOutgoing = wamsService.getAllOfficeOutgoingByEmpId(false, emp.getEmp_id());

	    mm.addAttribute("outgoing", allOutgoing);
	}

	return new ModelAndView("outgoinginfo", mm);
    }

    @RequestMapping(value = "/approvedleave", method = RequestMethod.GET)
    public ModelAndView approvedleave(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HLeave> allLeave = new ArrayList<HLeave>();

	    Date today = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curmm = Integer.parseInt(dfm.format(today));
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int curyy = Integer.parseInt(dfy.format(today));

	    allLeave = wamsService.getAllLeaveForMonth(false, curmm, curyy, 2);
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    mm.addAttribute("leaves", allLeave);
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("approvedleave", mm);
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.GET)
    public ModelAndView changepass(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "changepass");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    mm.addAttribute("allEmp", allEmp);

	}

	return new ModelAndView("changepass", mm);
    }

    @RequestMapping(value = "/leaveapprovalList", method = RequestMethod.GET)
    public ModelAndView leaveapprovalList(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    lastURL = "/leaveapprovalList";
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "leaveapprovalList");
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();
	    allEmp = wamsService.getAllEmployee(false);
	    List<HLeave> allLeave = new ArrayList<HLeave>();
	    List<HLeave> alleffectiveLeave = new ArrayList<HLeave>();
	    alleffectiveLeave = wamsService.getAllLeaveByApproverId(false, emp.getEmp_id());
	    allLeave = wamsService.getAllLeaveAppliedForApprovedByApproverId(false, emp.getEmp_id());
	    if (allLeave.size() == 0) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", allLeave.size());
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    mm.addAttribute("leaves", allLeave);
	    mm.addAttribute("emps", allEmp);
	    mm.addAttribute("effective", alleffectiveLeave);
	}

	return new ModelAndView("leaveapprovalList", mm);
    }

    @RequestMapping(value = "/outgoingapprovalList", method = RequestMethod.GET)
    public ModelAndView outgoingapprovalList(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    lastURL = "/outgoingapprovalList";
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    int approvalSize = getApprovarRequestSizeForOutgoing(emp);
	    if (approvalSize == 0) {
		return new ModelAndView("redirect:/");
	    }
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "outgoingapprovalList");
	    List<HEmployee> allEmp = new ArrayList<HEmployee>();
	    allEmp = wamsService.getAllEmployee(false);
	    List<HOfficeOutgoing> allOutgoing = new ArrayList<HOfficeOutgoing>();
	    List<HOfficeOutgoing> alleffectiveOutgoing = new ArrayList<HOfficeOutgoing>();
	    alleffectiveOutgoing = wamsService.getAllOfficeOutgoingByApproverId(false, emp.getEmp_id());
	    allOutgoing = wamsService.getAllOfficeOutgoingAppliedForApprovedByApproverId(false, emp.getEmp_id());
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", approvalSize);
	    // for approvel Sign
	    mm.addAttribute("outgoing", allOutgoing);
	    mm.addAttribute("emps", allEmp);
	    mm.addAttribute("effective", alleffectiveOutgoing);
	}

	return new ModelAndView("outgoingapprovalList", mm);
    }

    @RequestMapping(value = "/leaveapprovedList", method = RequestMethod.GET)
    public ModelAndView leaveapprovedList(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "leaveapprovedList");

	    List<HEmployee> allEmp = new ArrayList<HEmployee>();
	    allEmp = wamsService.getAllEmployee(true);

	    List<HLeave> allAppLeave = new ArrayList<HLeave>();
	    allAppLeave = wamsService.getAllApprovedLeaveByApproverId(false, emp.getEmp_id());
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("leaves", allAppLeave);
	}

	return new ModelAndView("leaveapprovedlist", mm);
    }

    @RequestMapping(value = "/viewleaverequest/{id}/{call}", method = RequestMethod.GET)
    public ModelAndView viewleaverequest(@PathVariable("id") int id, @PathVariable("call") int call,
	    HttpServletRequest request) throws Exception {

	logger.info("LeaveId=" + id + " Call=" + call);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    HLeave leave = wamsService.getLeaveById(id);
	    if ((call == 0) && (!emp.getUsertype().equalsIgnoreCase("Admin"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		mm.addAttribute("selUrl", "leavelist");
		if (leave.getEmp_id() != emp.getEmp_id()) {
		    return new ModelAndView("redirect:/");
		}
	    } else {
		mm.addAttribute("selUrl", "leaveapprovalList");
		if (call != 0) {
		    List<HLeave> allLeave = new ArrayList<HLeave>();
		    allLeave = wamsService.getAllLeaveAppliedForApprovedByApproverId(false, emp.getEmp_id());
		    boolean Fo = false;
		    for (HLeave lv : allLeave) {
			if (lv.getId() == id) {
			    Fo = true;
			}
		    }
		    if (!Fo) {
			return new ModelAndView("redirect:/");
		    } else {
			List<HLeave> alleffectiveLeave = new ArrayList<HLeave>();
			alleffectiveLeave = wamsService.getAllLeaveByApproverId(false, emp.getEmp_id());
			mm.addAttribute("effective", alleffectiveLeave);
		    }

		} else {
		    List<HLeave> alleffectiveLeave = new ArrayList<HLeave>();
		    alleffectiveLeave = wamsService.getAllLeaveByApproverId(false, emp.getEmp_id());
		    mm.addAttribute("effective", alleffectiveLeave);
		}
	    }

	    HEmployee reqEmp = new HEmployee();
	    List<String> approvState = new ArrayList<String>();
	    List<String> approvCommants = new ArrayList<String>();
	    if (leave != null) {
		reqEmp = wamsService.getEmployeeById(leave.getEmp_id());
		List<HLeaveStatus> leavestatus = new ArrayList<HLeaveStatus>();
		leavestatus = wamsService.getAllLeaveStatusByLeaveId(false, leave.getId());
		if (leave.getApprovar1() > 0) {
		    HEmployee approvar1 = wamsService.getEmployeeById(leave.getApprovar1());
		    String appStat = approvar1.getEmployee_name() + "(" + approvar1.getAvator() + ") :   ";
		    if (leave.getApp1state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar1()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar2() > 0) {
		    HEmployee approvar2 = wamsService.getEmployeeById(leave.getApprovar2());
		    String appStat = approvar2.getEmployee_name() + "(" + approvar2.getAvator() + ") :";
		    if (leave.getApp2state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar2()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar3() > 0) {
		    HEmployee approvar3 = wamsService.getEmployeeById(leave.getApprovar3());
		    String appStat = approvar3.getEmployee_name() + "(" + approvar3.getAvator() + ") :";
		    if (leave.getApp3state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar3()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar4() > 0) {
		    HEmployee approvar4 = wamsService.getEmployeeById(leave.getApprovar4());
		    String appStat = approvar4.getEmployee_name() + "(" + approvar4.getAvator() + ") :";
		    if (leave.getApp4state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar4()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar5() > 0) {
		    HEmployee approvar5 = wamsService.getEmployeeById(leave.getApprovar5());
		    String appStat = approvar5.getEmployee_name() + "(" + approvar5.getAvator() + ") :";
		    if (leave.getApp5state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar5()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}
	    }

	    List<String> nod = wamsService.calculateLeaveDate(leave.getFdate(), leave.getTdate(), leave.getEmp_id(), 1);
	    if (nod.size() > 0) {
		mm.addAttribute("nod", nod);
	    }

	    // Leave Quotas
	    List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(leave.getEmp_id(), leave.getLeavequota().getYear());

	    mm.addAttribute("quotas", quotas);

	    List<HTypesOfLeave> leavetype = new ArrayList<HTypesOfLeave>();
	    for (HTypesOfLeave type : allleavetype) {
		int typeid = type.getId();
		int deductfromid = type.getDeductfrom();
		if ((type.getIsquota()) || (deductfromid != 0)) {
		    for (HLeaveQuota quota : quotas) {
			if ((quota.getType_id() == typeid) || (quota.getType_id() == deductfromid)) {
			    if ((quota.getQuota() - quota.getUsed()) >= type.getLeavededuct()) {
				leavetype.add(type);
			    }
			}
		    }
		} else {
		    leavetype.add(type);
		}
	    }

	    mm.addAttribute("leavetype", leavetype);
	    mm.addAttribute("allleavetype", allleavetype);
	    // Leave Quotas
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    mm.addAttribute("leave", leave);
	    mm.addAttribute("reqemp", reqEmp);
	    mm.addAttribute("call", call);
	    mm.addAttribute("approvState", approvState);
	    mm.addAttribute("approvCommants", approvCommants);
	}

	return new ModelAndView("leaveview", mm);
    }

    @RequestMapping(value = "/viewoutgoingrequest/{id}/{call}", method = RequestMethod.GET)
    public ModelAndView viewoutgoingrequest(@PathVariable("id") int id, @PathVariable("call") int call,
	    HttpServletRequest request) throws Exception {

	logger.info("LeaveId=" + id + " Call=" + call);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    HOfficeOutgoing outgoing = wamsService.getOfficeOutgoingById(id);
	    if (call == 0) {
		mm.addAttribute("selUrl", "officeoutgoinglist");
		if (outgoing.getEmp_id() != emp.getEmp_id()) {
		    return new ModelAndView("redirect:/");
		}
	    } else {
		mm.addAttribute("selUrl", "outgoingapprovalList");
		List<HOfficeOutgoing> allOutgoing = new ArrayList<HOfficeOutgoing>();
		allOutgoing = wamsService.getAllOfficeOutgoingAppliedForApprovedByApproverId(false, emp.getEmp_id());
		boolean Fo = false;
		for (HOfficeOutgoing lv : allOutgoing) {
		    if (lv.getId() == id) {
			Fo = true;
		    }
		}
		if (!Fo) {
		    return new ModelAndView("redirect:/");
		} else {
		    List<HOfficeOutgoing> alleffectiveOutgoing = new ArrayList<HOfficeOutgoing>();
		    alleffectiveOutgoing = wamsService.getAllOfficeOutgoingByApproverId(false, emp.getEmp_id());
		    mm.addAttribute("effective", alleffectiveOutgoing);
		}
	    }

	    HEmployee reqEmp = new HEmployee();
	    List<String> approvState = new ArrayList<String>();
	    List<String> approvCommants = new ArrayList<String>();
	    if (outgoing != null) {
		reqEmp = wamsService.getEmployeeById(outgoing.getEmp_id());
		List<HOfficeOutgoingStatus> outgoingstatus = new ArrayList<HOfficeOutgoingStatus>();
		outgoingstatus = wamsService.getAllOfficeOutgoingStatusByOutgoingId(false, outgoing.getId());
		if (outgoing.getApprovar1() > 0) {
		    HEmployee approvar1 = wamsService.getEmployeeById(outgoing.getApprovar1());
		    String appStat = approvar1.getEmployee_name() + "(" + approvar1.getAvator() + ") :   ";
		    if (outgoing.getApp1state()) {
			String Commants = "";
			for (HOfficeOutgoingStatus st : outgoingstatus) {
			    if (st.getApprover_id() == outgoing.getApprovar1()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((outgoing.getStatus() == 2) || (outgoing.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (outgoing.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (outgoing.getApprovar2() > 0) {
		    HEmployee approvar2 = wamsService.getEmployeeById(outgoing.getApprovar2());
		    String appStat = approvar2.getEmployee_name() + "(" + approvar2.getAvator() + ") :";
		    if (outgoing.getApp2state()) {
			String Commants = "";
			for (HOfficeOutgoingStatus st : outgoingstatus) {
			    if (st.getApprover_id() == outgoing.getApprovar2()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((outgoing.getStatus() == 2) || (outgoing.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (outgoing.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (outgoing.getApprovar3() > 0) {
		    HEmployee approvar3 = wamsService.getEmployeeById(outgoing.getApprovar3());
		    String appStat = approvar3.getEmployee_name() + "(" + approvar3.getAvator() + ") :";
		    if (outgoing.getApp3state()) {
			String Commants = "";
			for (HOfficeOutgoingStatus st : outgoingstatus) {
			    if (st.getApprover_id() == outgoing.getApprovar3()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((outgoing.getStatus() == 2) || (outgoing.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (outgoing.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (outgoing.getApprovar4() > 0) {
		    HEmployee approvar4 = wamsService.getEmployeeById(outgoing.getApprovar4());
		    String appStat = approvar4.getEmployee_name() + "(" + approvar4.getAvator() + ") :";
		    if (outgoing.getApp4state()) {
			String Commants = "";
			for (HOfficeOutgoingStatus st : outgoingstatus) {
			    if (st.getApprover_id() == outgoing.getApprovar4()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((outgoing.getStatus() == 2) || (outgoing.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (outgoing.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (outgoing.getApprovar5() > 0) {
		    HEmployee approvar5 = wamsService.getEmployeeById(outgoing.getApprovar5());
		    String appStat = approvar5.getEmployee_name() + "(" + approvar5.getAvator() + ") :";
		    if (outgoing.getApp5state()) {
			String Commants = "";
			for (HOfficeOutgoingStatus st : outgoingstatus) {
			    if (st.getApprover_id() == outgoing.getApprovar5()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((outgoing.getStatus() == 2) || (outgoing.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (outgoing.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}
	    }

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    mm.addAttribute("outgoing", outgoing);
	    mm.addAttribute("reqemp", reqEmp);
	    mm.addAttribute("call", call);
	    mm.addAttribute("approvState", approvState);
	    mm.addAttribute("approvCommants", approvCommants);
	}

	return new ModelAndView("outgoingview", mm);
    }

    @RequestMapping(value = "/lunch", method = RequestMethod.GET)
    public ModelAndView lunch(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);
	mm.addAttribute("selUrl", "lunch");
	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {

	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    List<HLunch> todayLunch = new ArrayList<HLunch>();
	    Date dd = new Date();
	    String curDate = DateFormateforDB().format(dd);
	    // int month = getMonth(DateFormateforView().format(dd));
	    // int year = getYear(DateFormateforView().format(dd));
	    Integer emp_id = emp.getEmp_id();
	    todayLunch = wamsService.getLunchListByDate(false, true, curDate);
	    HLunch guestLunch = wamsService.getLunchByEmp(curDate, 0);
	    curDate = DateFormateforView().format(dd);
	    Integer inOut = 0;
	    List<HEmployee> aEmp = wamsService.getAllEmployee(false);

	    for (HLunch lnch : todayLunch) {
		if (lnch.getEmp_id() == emp_id) {
		    if (lnch.getLunch_status())
			inOut = 1;
		    else
			inOut = 0;
		    // break;
		}
		for (HEmployee em : aEmp) {
		    if (lnch.getEmp_id() == em.getEmp_id()) {
			String avator = lnch.getName();
			lnch.setName(em.getEmployee_name() + " (" + avator + ")");
			break;
		    } /*
		       * else if(lnch.getEmp_id() == 0){
		       * 
		       * }
		       */
		}
	    }
	    HEmployeeparam autoLunchState = wamsService.getAutoLunch(emp.getEmp_id());

	    mm.addAttribute("guestLunch", guestLunch);
	    mm.addAttribute("lunchs", todayLunch);
	    mm.addAttribute("inOut", inOut);
	    mm.addAttribute("curDate", curDate);
	    mm.addAttribute("autoLunch", autoLunchState);

	}
	return new ModelAndView("lunch", mm);
    }

    @RequestMapping(value = "/adminpanel", method = RequestMethod.GET)
    public ModelAndView adminpanel(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	}

	return new ModelAndView("adminpanel", mm);
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView reports(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	}

	return new ModelAndView("reports", mm);
    }

    @RequestMapping(value = "/empmonthlystatus", method = RequestMethod.GET)
    public ModelAndView empmonthlystatus(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));

	    List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(1, mon, year);

	    mm.addAttribute("repmon", mon);
	    mm.addAttribute("repyear", year);
	    mm.addAttribute("att", monthlyStates);
	}
	return new ModelAndView("employeemonthlystatus", mm);
    }

    @RequestMapping(value = "/empmonthlyovertime", method = RequestMethod.GET)
    public ModelAndView empmonthlyovertime(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));

	    // List<HMonthlyStatus> monthlyStates =
	    // wamsService.getEmployeeMonthlyStatus(1, mon, year);
	    List<HMonthlyStatus> monthlyStates = wamsService.getHMonthlyStatus(mon, year);

	    mm.addAttribute("repmon", mon);
	    mm.addAttribute("repyear", year);
	    mm.addAttribute("att", monthlyStates);
	}
	return new ModelAndView("employeemonthlyovertimestatus", mm);
    }

    @RequestMapping(value = "/empmonthlyovertimestatusdownload", method = RequestMethod.GET)
    public ModelAndView empmonthlyovertimestatusdownload(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Map<String, String[]> parameterMap = request.getParameterMap();

	    String steam = parameterMap.get("team")[0];
	    String smon = parameterMap.get("mon")[0];
	    String syear = parameterMap.get("year")[0];

	    String action = parameterMap.get("action")[0];

	    int startDate = 1;
	    int year = 0;

	    Date dd = new Date();
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");

	    if (!syear.equals("")) {
		year = Integer.parseInt(syear);
	    } else {
		year = Integer.parseInt(dfy.format(dd));
	    }

	    int mon = 0;
	    if (!smon.equals("")) {
		mon = Integer.parseInt(smon);
	    } else {
		mon = Integer.parseInt(dfm.format(dd));
	    }

	    int team = 0;
	    if (!steam.equals("")) {
		team = Integer.parseInt(steam);
	    }

	    // List<HMonthlyStatus> monthlyStates =
	    // wamsService.getEmployeeMonthlyStatus(startDate, mon, year);
	    List<HMonthlyStatus> monthlyStates = wamsService.getHMonthlyStatus(mon, year);
	    if (team != 0) {
		List<HMonthlyStatus> filteredmonthlyStates = new ArrayList<HMonthlyStatus>();
		List<HEmployee> allEmp = wamsService.getAllEmployee(false);
		for (HMonthlyStatus att : monthlyStates) {
		    for (HEmployee selemp : allEmp) {
			if ((att.getEmp_id() == selemp.getEmp_id()) && (selemp.getTeamId() == team)) {
			    filteredmonthlyStates.add(att);
			    break;
			}
		    }
		}
		if (action.equalsIgnoreCase("PDF")) {
		    return new ModelAndView("EmployeeOvertimePdf", "overtime", filteredmonthlyStates);
		} else {
		    return new ModelAndView("EmployeeOvertimeExcel", "overtime", filteredmonthlyStates);
		}
	    } else {
		if (action.equalsIgnoreCase("PDF")) {
		    return new ModelAndView("EmployeeOvertimePdf", "overtime", monthlyStates);
		} else {
		    return new ModelAndView("EmployeeOvertimeExcel", "overtime", monthlyStates);
		}
	    }
	}
    }

    @RequestMapping(value = "/dailyempmonthlystatus", method = RequestMethod.GET)
    public ModelAndView downloadempmonthlystatus(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Map<String, String[]> parameterMap = request.getParameterMap();

	    String smon = parameterMap.get("mon")[0];
	    String syear = parameterMap.get("year")[0];
	    String steam = parameterMap.get("team")[0];

	    int startDate = 1;

	    int mon = Integer.parseInt(smon);
	    int year = Integer.parseInt(syear);
	    int team = Integer.parseInt(steam);

	    /*
	     * if(!sdd.equals("")) { startDate = getDay(sdd); mon =
	     * getMonth(sdd); year = getYear(sdd); } else{ Date dd = new Date();
	     * SimpleDateFormat dfm = new SimpleDateFormat("MM"); mon =
	     * Integer.parseInt(dfm.format(dd));
	     * 
	     * SimpleDateFormat dfy = new SimpleDateFormat("yyyy"); year =
	     * Integer.parseInt(dfy.format(dd)); startDate = 1; }
	     */

	    List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(startDate, mon, year);
	    if (team != 0) {
		List<HMonthlyStatus> filteredmonthlyStates = new ArrayList<HMonthlyStatus>();
		List<HEmployee> allEmp = wamsService.getAllEmployee(false);
		for (HMonthlyStatus att : monthlyStates) {
		    for (HEmployee curemp : allEmp) {
			if ((att.getEmp_id() == curemp.getEmp_id()) && (curemp.getTeamId() == team)) {
			    filteredmonthlyStates.add(att);
			    break;
			}
		    }
		}

		return new ModelAndView("MonthlyEmpStatusListExcel", "att", filteredmonthlyStates);
	    } else {
		return new ModelAndView("MonthlyEmpStatusListExcel", "att", monthlyStates);
	    }
	}
    }

    @RequestMapping(value = "/monthlyleavestatus", method = RequestMethod.GET)
    public ModelAndView monthlyleavestatus(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));
	    // List for get Date for month
	    int lastdateofmon = wamsService.endDateofMon(mon, year);

	    Map<String, List<HMAttendance>> empMonthlyState = new HashMap<String, List<HMAttendance>>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
		thisMonAtt = wamsService.getEmployeeStatusForMonth(mon, year, employee.getEmp_id(), true,
			"monthlyleavestatus");
		if (!thisMonAtt.isEmpty())
		    empMonthlyState.put(String.valueOf(employee.getEmp_id()), thisMonAtt);
	    }
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("estate", empMonthlyState);
	    mm.addAttribute("lastday", lastdateofmon);
	    mm.addAttribute("mon", mon);
	    mm.addAttribute("year", year);
	}
	return new ModelAndView("monthlyleavestatus", mm);
    }

    @RequestMapping(value = "/viewleavedetails/{empid}/{date}/{stat}", method = RequestMethod.GET)
    public ModelAndView viewleavedetails(@PathVariable("empid") int emp_id, @PathVariable("date") String date,
	    @PathVariable("stat") int stat, HttpServletRequest request) throws Exception {

	logger.info("Employee Id=" + emp_id + " Date=" + date + " stat=" + stat);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    mm.addAttribute("selUrl", "reports");
	    int status = 0;
	    if (stat == 0)
		status = 0;
	    else
		status = 2;
	    HLeave leave = wamsService.getLeaveByEmpIdAndDate(emp_id, convertDateFormate(date), status);
	    HEmployee reqEmp = new HEmployee();
	    List<String> approvState = new ArrayList<String>();
	    List<String> approvCommants = new ArrayList<String>();
	    if (leave != null) {
		reqEmp = wamsService.getEmployeeById(leave.getEmp_id());
		List<HLeaveStatus> leavestatus = new ArrayList<HLeaveStatus>();
		leavestatus = wamsService.getAllLeaveStatusByLeaveId(false, leave.getId());
		if (leave.getApprovar1() > 0) {
		    HEmployee approvar1 = wamsService.getEmployeeById(leave.getApprovar1());
		    String appStat = approvar1.getEmployee_name() + "(" + approvar1.getAvator() + ") :   ";
		    if (leave.getApp1state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar1()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar2() > 0) {
		    HEmployee approvar2 = wamsService.getEmployeeById(leave.getApprovar2());
		    String appStat = approvar2.getEmployee_name() + "(" + approvar2.getAvator() + ") :";
		    if (leave.getApp2state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar2()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar3() > 0) {
		    HEmployee approvar3 = wamsService.getEmployeeById(leave.getApprovar3());
		    String appStat = approvar3.getEmployee_name() + "(" + approvar3.getAvator() + ") :";
		    if (leave.getApp3state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar3()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar4() > 0) {
		    HEmployee approvar4 = wamsService.getEmployeeById(leave.getApprovar4());
		    String appStat = approvar4.getEmployee_name() + "(" + approvar4.getAvator() + ") :";
		    if (leave.getApp4state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar4()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}

		if (leave.getApprovar5() > 0) {
		    HEmployee approvar5 = wamsService.getEmployeeById(leave.getApprovar5());
		    String appStat = approvar5.getEmployee_name() + "(" + approvar5.getAvator() + ") :";
		    if (leave.getApp5state()) {
			String Commants = "";
			for (HLeaveStatus st : leavestatus) {
			    if (st.getApprover_id() == leave.getApprovar5()) {
				Commants = st.getComments();
				break;
			    }
			}
			if ((leave.getStatus() == 2) || (leave.getStatus() == 0))
			    appStat = appStat + " Approved.";
			else if (leave.getStatus() == 1)
			    appStat = appStat + " Rejected.";
			approvCommants.add(Commants);
		    } else {
			appStat = appStat + " Not Approved.";
			approvCommants.add("");
		    }
		    approvState.add(appStat);
		}
	    }

	    // Leave Quotas
	    List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(leave.getEmp_id(), leave.getLeavequota().getYear());

	    mm.addAttribute("quotas", quotas);

	    List<HTypesOfLeave> leavetype = new ArrayList<HTypesOfLeave>();
	    for (HTypesOfLeave type : allleavetype) {
		int typeid = type.getId();
		int deductfromid = type.getDeductfrom();
		if ((type.getIsquota()) || (deductfromid != 0)) {
		    for (HLeaveQuota quota : quotas) {
			if ((quota.getType_id() == typeid) || (quota.getType_id() == deductfromid)) {
			    if ((quota.getQuota() - quota.getUsed()) >= type.getLeavededuct()) {
				leavetype.add(type);
			    }
			}
		    }
		} else {
		    leavetype.add(type);
		}
	    }

	    mm.addAttribute("leavetype", leavetype);
	    mm.addAttribute("allleavetype", allleavetype);
	    // Leave Quotas

	    List<String> nod = wamsService.calculateLeaveDate(leave.getFdate(), leave.getTdate(), leave.getEmp_id(), 1);
	    if (nod.size() > 0) {
		mm.addAttribute("nod", nod);
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    mm.addAttribute("leave", leave);
	    mm.addAttribute("reqemp", reqEmp);
	    mm.addAttribute("call", 2);
	    mm.addAttribute("approvState", approvState);
	    mm.addAttribute("approvCommants", approvCommants);
	}

	return new ModelAndView("leaveview", mm);
    }

    @RequestMapping(value = "/monthlylunchstatus", method = RequestMethod.GET)
    public ModelAndView monthlylunchstatus(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));
	    // List for get Date for month
	    int lastdateofmon = wamsService.endDateofMon(mon, year);

	    Map<String, List<HLunch>> empMonthlyLunch = new HashMap<String, List<HLunch>>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		List<HLunch> thisMonLunch = new ArrayList<HLunch>();
		thisMonLunch = wamsService.getLunchForMonth(mon, year, employee.getEmp_id(), true);
		if (!thisMonLunch.isEmpty())
		    empMonthlyLunch.put(String.valueOf(employee.getEmp_id()), thisMonLunch);
	    }
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("estate", empMonthlyLunch);
	    mm.addAttribute("lastday", lastdateofmon);
	    mm.addAttribute("mon", mon);
	    mm.addAttribute("year", year);
	}
	return new ModelAndView("monthlylunchstatus", mm);
    }

    @RequestMapping(value = "/editholiday/{id}", method = RequestMethod.GET)
    public ModelAndView editholiday(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("HolidayId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "holidainfo");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HHoliday curHoliday = wamsService.getHolidayById(id);

	    mm.addAttribute("holiday", curHoliday);

	}
	return new ModelAndView("editholiday", mm);
    }

    @RequestMapping(value = "/employeeleaves", method = RequestMethod.GET)
    public ModelAndView employeeleaves(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    Date dd = new Date();
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(dfm.format(dd));

	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(dfy.format(dd));
	    int loopstyear = year - 2;
	    int loopenyear = year + 1;
	    List<Integer> disyear = new ArrayList<Integer>();
	    for (int i = loopstyear; i < loopenyear; i++) {
		disyear.add(i);
	    }

	    if (mon == 1 || mon == 2 || mon == 3 || mon == 4 || mon == 5 || mon == 6)
		year = year - 1;
	    // List for get Date for month
	    List<HYearlyLeave> empYearlyLeaveState = new ArrayList<HYearlyLeave>();
	    empYearlyLeaveState = wamsService.getEmployeeYearlyLeave(year);

	    mm.addAttribute("disyear", disyear);
	    mm.addAttribute("yleave", empYearlyLeaveState);
	    mm.addAttribute("year", year);
	}
	return new ModelAndView("yearlyleavestatus", mm);
    }

    @RequestMapping(value = "/monthlyleavedownload", method = RequestMethod.GET)
    public ModelAndView monthlyleavedownload(HttpServletRequest request, HttpServletResponse response) {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    Map<String, String[]> parameterMap = request.getParameterMap();

	    String smon = parameterMap.get("mon")[0];
	    String syear = parameterMap.get("year")[0];

	    /*
	     * Date dd = new Date(); SimpleDateFormat dfm = new
	     * SimpleDateFormat("MM");
	     */
	    int mon = Integer.parseInt(smon);

	    /* SimpleDateFormat dfy = new SimpleDateFormat("yyyy"); */
	    int year = Integer.parseInt(syear);
	    // List for get Date for month
	    int lastdateofmon = wamsService.endDateofMon(mon, year);

	    Map<String, List<HMAttendance>> empMonthlyState = new HashMap<String, List<HMAttendance>>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	    for (HEmployee employee : allEmp) {
		List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
		thisMonAtt = wamsService.getEmployeeStatusForMonth(mon, year, employee.getEmp_id(), true,
			"monthlyleavedownload");
		if (!thisMonAtt.isEmpty()) {
		    empMonthlyState.put(String.valueOf(employee.getEmp_id()), thisMonAtt);

		}
	    }

	    // return Pdf view
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("period", mon + "/" + year);
	    mm.addAttribute("lastdateofmon", lastdateofmon);
	    mm.addAttribute("estate", empMonthlyState);

	    return new ModelAndView("PdfMonthlyLeaveReportView", mm);
	}

    }

    @RequestMapping(value = "/teaminfo", method = RequestMethod.GET)
    public ModelAndView teaminfo(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HTeam> allTeam = new ArrayList<HTeam>();
	    allTeam = wamsService.getAllTeam(false);
	    if (!emp.getUsertype().equalsIgnoreCase("Admin") && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");

	    }
	    mm.addAttribute("allTeam", allTeam);
	}

	return new ModelAndView("teaminfo", mm);
    }

    @RequestMapping(value = "/addteam", method = RequestMethod.GET)
    public ModelAndView addteam(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);
	mm.addAttribute("selUrl", "adminpanel");

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    List<HTeam> allTeam = wamsService.getAllTeam(false);
	    mm.addAttribute("allTeam", allTeam);
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	}
	return new ModelAndView("addteam", mm);
    }

    @RequestMapping(value = "/leavetype", method = RequestMethod.GET)
    public ModelAndView leavetype(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
	    allLeaveType = wamsService.getLeaveType(false);

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");

	    }
	    mm.addAttribute("allLeaveType", allLeaveType);
	}

	return new ModelAndView("leavetypes", mm);
    }

    @RequestMapping(value = "/addleavetype", method = RequestMethod.GET)
    public ModelAndView addleavetype(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);
	mm.addAttribute("selUrl", "adminpanel");

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
	    allLeaveType = wamsService.getLeaveType(false);
	    mm.addAttribute("leavetype", allLeaveType);

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	}
	return new ModelAndView("addleavetype", mm);
    }

    @RequestMapping(value = "/leaveTypeEdit/{id}", method = RequestMethod.GET)
    public ModelAndView leaveTypeEdit(@PathVariable int id, HttpServletRequest request) throws Exception {

	logger.info("TypeId=" + id);
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
	    allLeaveType = wamsService.getLeaveType(false);
	    mm.addAttribute("leavetype", allLeaveType);

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HTypesOfLeave type = wamsService.getLeaveTypeById(id);

	    mm.addAttribute("type", type);
	}

	return new ModelAndView("editleavetype", mm);
    }

    @RequestMapping(value = "/leaveQuota", method = RequestMethod.GET)
    public ModelAndView leaveQuota(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    List<HEmployee> allEmp = new ArrayList<HEmployee>();

	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) || (emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		allEmp = wamsService.getAllEmployee(false);
	    } else {
		allEmp.add(emp);
	    }
	    mm.addAttribute("allEmp", allEmp);
	}

	return new ModelAndView("employeeleavequota", mm);
    }

    @RequestMapping(value = "/addEmployeeLeaveQuota/{id}", method = RequestMethod.GET)
    public ModelAndView addEmployeeLeaveQuota(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("EmpId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HEmployee empInfo = wamsService.getEmployeeById(id);
	    mm.addAttribute("selemp", empInfo);

	    List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
	    allLeaveType = wamsService.getLeaveType(false);

	    mm.addAttribute("leavetype", allLeaveType);
	    Date today = new Date();
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curyy = Integer.parseInt(dfy.format(today));
	    int curmm = Integer.parseInt(dfm.format(today));
	    List<String> displayYear = new ArrayList<String>();
	    for (int i = (curyy - 5); i < (curyy + 10); i++) {
		displayYear.add(String.valueOf(i) + "/" + String.valueOf(i + 1).substring(2));
	    }
	    mm.addAttribute("displayYear", displayYear);

	    String selectYear = "";
	    if (curmm <= 6) {
		selectYear = String.valueOf(curyy - 1) + "/" + String.valueOf(curyy).substring(2);
	    } else {
		selectYear = String.valueOf(curyy) + "/" + String.valueOf(curyy + 1).substring(2);
	    }

	    mm.addAttribute("selectYear", selectYear);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(id, selectYear);

	    mm.addAttribute("quotas", quotas);
	}

	return new ModelAndView("addemployeeleavequota", mm);
    }

    @RequestMapping(value = "/viewEmployeeLeaveQuota/{id}", method = RequestMethod.GET)
    public ModelAndView viewEmployeeLeaveQuota(@PathVariable int id, HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();
	logger.info("EmpId=" + id);
	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "adminpanel");
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign
	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }

	    HEmployee empInfo = wamsService.getEmployeeById(id);
	    mm.addAttribute("selemp", empInfo);

	    List<HTypesOfLeave> allLeaveType = new ArrayList<HTypesOfLeave>();
	    allLeaveType = wamsService.getLeaveType(false);

	    mm.addAttribute("leavetype", allLeaveType);
	    Date today = new Date();
	    SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int curyy = Integer.parseInt(dfy.format(today));
	    int curmm = Integer.parseInt(dfm.format(today));
	    // List<String> displayYear = new ArrayList<String>();
	    // for(int i=(curyy-5);i<(curyy+10);i++){
	    // displayYear.add(String.valueOf(i)+"/"+String.valueOf(i+1).substring(2));
	    // }
	    // mm.addAttribute("displayYear",displayYear);
	    String selectYear = "";
	    if (curmm <= 6) {
		selectYear = String.valueOf(curyy - 1) + "/" + String.valueOf(curyy).substring(2);
	    } else {
		selectYear = String.valueOf(curyy) + "/" + String.valueOf(curyy + 1).substring(2);
	    }
	    mm.addAttribute("selectYear", selectYear);

	    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(id, selectYear);

	    mm.addAttribute("quotas", quotas);
	}

	return new ModelAndView("viewemployeeleavequota", mm);
    }

    @RequestMapping(value = "/reportgeneratorpage", method = RequestMethod.GET)
    public ModelAndView reportgeneratorpage(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);

	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		mm.addAttribute("selUrl", "adminpanel");
	    } else {
		mm.addAttribute("selUrl", "empinfo");
	    }

	}

	return new ModelAndView("reportgenerator", mm);
    }

    @RequestMapping(value = "/manageleave", method = RequestMethod.GET)
    public ModelAndView manageleave(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    if ((emp.getUsertype().equalsIgnoreCase("Admin")) || (emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		// for approvel Sign
		mm.addAttribute("isApproval", getApprovarRequestSize(emp));
		mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
		// for approvel Sign
		mm.addAttribute("selUrl", "adminpanel");
		
		// for approvel Sign
		List<HEmployee> allEmp = wamsService.getAllEmployee(false);
		mm.addAttribute("allEmp",allEmp);
		
		List<HEmployee> tempEmp = new ArrayList<HEmployee>();

		tempEmp = wamsService.getAllEmployee(false);
		List<HEmpLeaveQuota> allEmp1 = new ArrayList<HEmpLeaveQuota>();
		List<HEmpLeaveQuota> allEmp2 = new ArrayList<HEmpLeaveQuota>();
		List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);
		
		// Leave Quotas
		Date today = new Date();
		SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
		SimpleDateFormat dfm = new SimpleDateFormat("MM");
		int curyy = Integer.parseInt(dfy.format(today));
		int curmm = Integer.parseInt(dfm.format(today));

		List<HLeaveQuota> quotaYear = wamsService.getQuotaYear();
		mm.addAttribute("quotaYear", quotaYear);

		String selectYear = "";
		if (curmm <= 6) {
		    selectYear = String.valueOf(curyy - 1) + "/" + String.valueOf(curyy).substring(2);
		} else {
		    selectYear = String.valueOf(curyy) + "/" + String.valueOf(curyy + 1).substring(2);
		}
		mm.addAttribute("selectYear", selectYear);
		
		int size = tempEmp.size();
		if (size == 1){
		    HEmpLeaveQuota leavequot = new HEmpLeaveQuota();
		    List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(tempEmp.get(0).getEmp_id(), selectYear);
		    leavequot.setEmp(tempEmp.get(0));
		    leavequot.setQuota(quotas);
		    allEmp1.add(leavequot);
		} else {
		    for (int i = 0; i < (size / 2); i++) {
			HEmpLeaveQuota leavequot = new HEmpLeaveQuota();
			List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(tempEmp.get(i).getEmp_id(), selectYear);
			leavequot.setEmp(tempEmp.get(i));
			leavequot.setQuota(quotas);
			allEmp1.add(leavequot);
		    }
		    for (int i = (size / 2); i < size; i++) {
			HEmpLeaveQuota leavequot = new HEmpLeaveQuota();
			List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(tempEmp.get(i).getEmp_id(), selectYear);
			leavequot.setEmp(tempEmp.get(i));
			leavequot.setQuota(quotas);
			allEmp2.add(leavequot);
		    }
		}
		mm.addAttribute("allEmp1", allEmp1);
		mm.addAttribute("allEmp2", allEmp2);
		
		HLeaveApprover approver = new HLeaveApprover();
		approver.setEmp_id(emp.getEmp_id());
		approver.setApprover_id(emp.getEmp_id());
		
		List<HLeaveApprover> approvers = new ArrayList<HLeaveApprover>();
		approvers.add(approver);
		mm.addAttribute("approver", approvers);
	    }
	}

	return new ModelAndView("manageleave", mm);
    }

    // =====================================================================================================================
    @RequestMapping(value = "/lunchdetailssearch", method = RequestMethod.POST)
    public ModelAndView lunchdetailssearch(HttpServletRequest request) throws Exception {

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 1) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    // Date dd = new Date();
	    // SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(requestUriSplit[0]);

	    // SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(requestUriSplit[1]);
	    // List for get Date for month
	    int lastdateofmon = wamsService.endDateofMon(mon, year);

	    Map<String, List<HLunch>> empMonthlyLunch = new HashMap<String, List<HLunch>>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		List<HLunch> thisMonLunch = new ArrayList<HLunch>();
		thisMonLunch = wamsService.getLunchForMonth(mon, year, employee.getEmp_id(), true);
		if (!thisMonLunch.isEmpty())
		    empMonthlyLunch.put(String.valueOf(employee.getEmp_id()), thisMonLunch);
	    }
	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("estate", empMonthlyLunch);
	    mm.addAttribute("lastday", lastdateofmon);
	    mm.addAttribute("mon", mon);
	    mm.addAttribute("year", year);
	}
	return new ModelAndView("monthlylunchlistsearch", mm);
    }

    @RequestMapping(value = "/leavedetailssearch", method = RequestMethod.POST)
    public ModelAndView leavedetailssearch(HttpServletRequest request) throws Exception {

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 1) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	boolean validSession = getSessionService().isSessionValid();

	ModelMap mm = new ModelMap();
	mm.addAttribute("validSession", validSession);

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	} else {
	    HEmployee emp = getSessionService().getUserSession().getEmp();
	    mm.addAttribute("emp", emp);
	    mm.addAttribute("selUrl", "reports");

	    if ((!emp.getUsertype().equalsIgnoreCase("Admin")) && (!emp.getUsertype().equalsIgnoreCase("Watcher"))
		    && (!emp.getUsertype().equalsIgnoreCase("Manager")) && (!emp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
		return new ModelAndView("redirect:/");
	    }
	    // for approvel Sign
	    mm.addAttribute("isApproval", getApprovarRequestSize(emp));
	    mm.addAttribute("isOutgoing", getApprovarRequestSizeForOutgoing(emp));
	    // for approvel Sign

	    // Date dd = new Date();
	    // SimpleDateFormat dfm = new SimpleDateFormat("MM");
	    int mon = Integer.parseInt(requestUriSplit[0]);

	    // SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	    int year = Integer.parseInt(requestUriSplit[1]);
	    // List for get Date for month
	    int lastdateofmon = wamsService.endDateofMon(mon, year);

	    Map<String, List<HMAttendance>> empMonthlyState = new HashMap<String, List<HMAttendance>>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HEmployee employee : allEmp) {
		List<HMAttendance> thisMonAttTemp = new ArrayList<HMAttendance>();
		List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
		thisMonAttTemp = wamsService.getAttendanceForMonth(mon, year, employee.getEmp_id(), true);
		for (int st = 1; st <= lastdateofmon; st++) {
		    String dt = st + "-" + mon + "-" + year;
		    boolean Fo = false;
		    for (HMAttendance at : thisMonAttTemp) {
			if (dt.equals(at.getAtt_date())) {
			    thisMonAtt.add(at);
			    Fo = true;
			    break;
			}
		    }
		    if (!Fo) {
			HMAttendance hatt = new HMAttendance();
			hatt.setAtt_date(dt);
			thisMonAtt.add(hatt);
		    }
		}
		if (!thisMonAtt.isEmpty())
		    empMonthlyState.put(String.valueOf(employee.getEmp_id()), thisMonAtt);
	    }

	    mm.addAttribute("allEmp", allEmp);
	    mm.addAttribute("estate", empMonthlyState);
	    mm.addAttribute("lastday", lastdateofmon);
	    mm.addAttribute("mon", mon);
	    mm.addAttribute("year", year);
	}
	return new ModelAndView("monthlyleavelistsearch", mm);
    }

    @RequestMapping(value = "/searchattstatus", method = RequestMethod.POST)
    public ModelAndView searchattstatus(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	/*
	 * if (requestUriSplit.length < 3) { logger.warn(
	 * "Expecting atleast 1 arguments but received " +
	 * requestUriSplit.length); return null; }
	 */
	ModelMap mm = new ModelMap();
	String smon = requestUriSplit[0];
	String syear = requestUriSplit[1];
	String steam = requestUriSplit[2];

	int startDate = 1;
	// int endDate = getDay(edd);

	int mon = Integer.parseInt(smon);
	int year = Integer.parseInt(syear);
	int team = Integer.parseInt(steam);

	List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(startDate, mon, year);
	if (team != 0) {
	    List<HMonthlyStatus> filteredmonthlyStates = new ArrayList<HMonthlyStatus>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HMonthlyStatus att : monthlyStates) {
		for (HEmployee emp : allEmp) {
		    if ((att.getEmp_id() == emp.getEmp_id()) && (emp.getTeamId() == team)) {
			filteredmonthlyStates.add(att);
			break;
		    }
		}
	    }
	    mm.addAttribute("att", filteredmonthlyStates);
	} else {
	    mm.addAttribute("att", monthlyStates);
	}

	return new ModelAndView("employeemonthlystatussearch", mm);
    }

    @RequestMapping(value = "/searchovertimestatus", method = RequestMethod.POST)
    public ModelAndView searchovertimestatus(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	/*
	 * if (requestUriSplit.length < 3) { logger.warn(
	 * "Expecting atleast 1 arguments but received " +
	 * requestUriSplit.length); return null; }
	 */
	ModelMap mm = new ModelMap();
	String smon = requestUriSplit[0];
	String syear = requestUriSplit[1];
	String steam = requestUriSplit[2];

	int startDate = 1;
	// int endDate = getDay(edd);

	int mon = Integer.parseInt(smon);
	int year = Integer.parseInt(syear);
	int team = Integer.parseInt(steam);

	// List<HMonthlyStatus> monthlyStates =
	// wamsService.getEmployeeMonthlyStatus(startDate, mon, year);
	List<HMonthlyStatus> monthlyStates = wamsService.getHMonthlyStatus(mon, year);
	if (team != 0) {
	    List<HMonthlyStatus> filteredmonthlyStates = new ArrayList<HMonthlyStatus>();
	    List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	    for (HMonthlyStatus att : monthlyStates) {
		for (HEmployee emp : allEmp) {
		    if ((att.getEmp_id() == emp.getEmp_id()) && (emp.getTeamId() == team)) {
			filteredmonthlyStates.add(att);
			break;
		    }
		}
	    }
	    mm.addAttribute("att", filteredmonthlyStates);
	} else {
	    mm.addAttribute("att", monthlyStates);
	}

	return new ModelAndView("employeemonthlyovertimesearch", mm);
    }

    @RequestMapping(value = "/users/valideduser", method = RequestMethod.POST)
    public ModelAndView userVerify(HttpServletRequest request) throws Exception {

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	String uname = requestUriSplit[0];
	String pass = requestUriSplit[1];

	HEmployee emp = new HEmployee();
	emp.setAvator(uname);
	emp.setPassword(pass);

	HEmployee user = wamsService.getUserValidation(emp);
	String avator = "-1";
	if (user != null) {
	    UserSession session = getSessionService().insertSession(request, user);
	    avator = user.getAvator();
	}

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", avator);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/employee/att", method = RequestMethod.POST)
    public ModelAndView attendance(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	}

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 4) {
	    logger.warn("Expecting atleast 4 arguments but received " + requestUriSplit.length);
	    return null;
	}

	String inout = requestUriSplit[0];
	String EmpDate = requestUriSplit[1];
	String EmpTime = requestUriSplit[2];
	String emp_id = requestUriSplit[3];

	Date now = new Date();
	int empId = Integer.parseInt(emp_id);
	EmpDate = getFormatedDate(now);
	HAttendance att = new HAttendance();
	att.setEmp_id(empId);
	att.setAtt_date(EmpDate);

	Integer daySeq = wamsService.getAttSequence(att);

	if (daySeq == 0)
	    EmpTime = getFormatedTime(now, inout, timeAdjust);
	else
	    EmpTime = getFormatedTime(now);

	// System.out.println(emp_id+" "+inout+" "+EmpDate+" "+EmpTime);
	/*
	 * String message = "My In time is :"+now;
	 * 
	 * //Skype.chat("EY_WH_BD").send(message); Chat group = null;
	 * 
	 * //Group group1=SkypeImpl.getContactList().getGroup("ey_wh_bd");
	 * 
	 * for (Chat c : Skype.getAllChats()){ group = c; String id =
	 * group.getId(); System.out.println(id+
	 * " ================================ "+group.getWindowTitle());
	 * if(id.equalsIgnoreCase(
	 * "#abdullah_al_monsur/$ey_wh_bd;66c89d7aeed43601")){
	 * //Skype.chat("EY_WH_BD").send(message); group.send(message); break; }
	 * }
	 */

	att.setAtt_time(EmpTime);
	att.setAtt_inout(Integer.parseInt(inout));
	att.setModId(getSessionService().getUserSession().getEmp().getAvator());
	att.setDeleted(false);

	Integer insert = wamsService.insertHAttendance(att);
	// Add Lunch
	if (autolunchentry.equals("1") && (daySeq == 0)) {
	    HEmployee emp = wamsService.getEmployeeById(empId);
	    HLunch lunch = new HLunch();
	    lunch.setEmp_id(empId);
	    lunch.setLunch_date(EmpDate);
	    lunch.setName(emp.getAvator());
	    lunch.setLunch_status(true);
	    lunch.setLunch_count(1);
	    lunch.setDeleted(false);
	    lunch.setModId(getSessionService().getUserSession().getEmp().getAvator());
	    wamsService.insertHLunch(lunch);
	}

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", insert);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updateemp", method = RequestMethod.POST)
    public ModelAndView updateEmp(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HEmployee logedemp = getSessionService().getUserSession().getEmp();

	HEmployee empInfo = new HEmployee();
	Integer emp_id = Integer.parseInt(parameterMap.get("emp_id")[0]);

	empInfo = wamsService.getEmployeeById(emp_id);

	if ((logedemp.getUsertype().equals("Admin")) || (logedemp.getUsertype().equalsIgnoreCase("Talent Manager"))) {
	    empInfo.setEmp_id(emp_id);
	    empInfo.setAvator(parameterMap.get("emp_avator")[0]);
	    empInfo.setUsertype(parameterMap.get("usertype")[0]);
	    String join_date = parameterMap.get("join_date")[0];
	    if (join_date.equals(""))
		join_date = "01-01-1970";
	    empInfo.setJoin_date(convertDateFormate(join_date));
	    empInfo.setTeamId(Integer.parseInt(parameterMap.get("team")[0]));
	    empInfo.setEmp_number(parameterMap.get("emp_number")[0]);
	} else {
	    empInfo.setEmp_id(logedemp.getEmp_id());
	    empInfo.setAvator(logedemp.getAvator());
	    empInfo.setUsertype(logedemp.getUsertype());
	    empInfo.setJoin_date(convertDateFormate(logedemp.getJoin_date()));
	    empInfo.setTeamId(logedemp.getTeamId());
	    empInfo.setEmp_number(logedemp.getEmp_number());
	}

	empInfo.setEmployee_name(parameterMap.get("emp_name")[0]);
	empInfo.setEmail(parameterMap.get("emp_email")[0]);
	empInfo.setDesignation(parameterMap.get("emp_desig")[0]);
	empInfo.setSkype_id(parameterMap.get("emp_skype")[0]);
	empInfo.setBloodgroup(parameterMap.get("bloodgroup")[0]);
	String birthCertificate = parameterMap.get("birthdate_certificate")[0];
	if (birthCertificate.equals(""))
	    birthCertificate = "01-01-1970";
	empInfo.setBirthdate_certificate(convertDateFormate(birthCertificate));
	String birthReal = parameterMap.get("original_birthdate")[0];
	if (birthReal.equals(""))
	    birthReal = "01-01-1970";
	empInfo.setBirthdate_real(convertDateFormate(birthReal));
	empInfo.setNid(parameterMap.get("emp_nid")[0]);
	empInfo.setPersonal_email(parameterMap.get("emp_pemail")[0]);

	empInfo.setMobile(parameterMap.get("emp_contact")[0]);
	empInfo.setDeleted(false);
	empInfo.setModId(session.getEmp().getAvator());

	Integer empid = wamsService.editHEmployee(empInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addemp", method = RequestMethod.POST)
    public ModelAndView addEmp(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HEmployee empInfo = new HEmployee();

	// empInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	empInfo.setEmployee_name(parameterMap.get("emp_name")[0]);
	empInfo.setAvator(parameterMap.get("emp_avator")[0]);
	empInfo.setEmail(parameterMap.get("emp_email")[0]);
	empInfo.setDesignation(parameterMap.get("emp_desig")[0]);
	empInfo.setSkype_id(parameterMap.get("emp_skype")[0]);
	empInfo.setBloodgroup(parameterMap.get("bloodgroup")[0]);
	String birthCertificate = parameterMap.get("birthdate_certificate")[0];
	if (birthCertificate.equals(""))
	    birthCertificate = "01-01-1970";
	empInfo.setBirthdate_certificate(convertDateFormate(birthCertificate));
	String birthReal = parameterMap.get("original_birthdate")[0];
	if (birthReal.equals(""))
	    birthReal = "01-01-1970";
	empInfo.setBirthdate_real(convertDateFormate(birthReal));
	empInfo.setNid(parameterMap.get("emp_nid")[0]);
	empInfo.setPersonal_email(parameterMap.get("emp_pemail")[0]);
	empInfo.setUsertype(parameterMap.get("usertype")[0]);
	empInfo.setMobile(parameterMap.get("emp_contact")[0]);
	empInfo.setDeleted(false);
	empInfo.setModId(session.getEmp().getAvator());
	empInfo.setPassword(parameterMap.get("emp_pass")[0]);
	empInfo.setEmp_number(parameterMap.get("emp_number")[0]);
	empInfo.setTeamId(Integer.parseInt(parameterMap.get("team")[0]));
	String join_date = parameterMap.get("join_date")[0];
	if (join_date.equals(""))
	    join_date = "01-01-1970";
	empInfo.setJoin_date(convertDateFormate(join_date));

	Integer empid = wamsService.insertHEmployee(empInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addempwork", method = RequestMethod.POST)
    public ModelAndView addEmpWork(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HWorkInfo empworkInfo = new HWorkInfo();
	int emp_id = Integer.parseInt(parameterMap.get("emp_id")[0]);
	empworkInfo.setEmp_id(emp_id);
	empworkInfo.setOffice_start(Float.parseFloat(parameterMap.get("off_start")[0]));
	empworkInfo.setWorking_hour(Float.parseFloat(parameterMap.get("working_hour")[0]));
	String[] weekends = parameterMap.get("weekend");
	String[] approver_id = parameterMap.get("chk");
	String[] outgoing_approver_id = parameterMap.get("chkoff");
	String weekend = "";
	for (int i = 0; i < weekends.length; i++) {
	    if (i == 0)
		weekend = weekends[i];
	    else
		weekend = weekend + "," + weekends[i];
	}
	empworkInfo.setWeekend(weekend);
	empworkInfo.setFrom_date(convertDateFormate(parameterMap.get("from_date")[0]));
	empworkInfo.setStatus(true);
	empworkInfo.setDeleted(false);
	empworkInfo.setModId(session.getEmp().getAvator());

	List<HLeaveApprover> appr = new ArrayList<HLeaveApprover>();
	for (String app : approver_id) {
	    HLeaveApprover approver = new HLeaveApprover();
	    approver.setEmp_id(emp_id);
	    approver.setApprover_id(Integer.parseInt(app));
	    approver.setDeleted(false);
	    approver.setModId(session.getEmp().getAvator());
	    appr.add(approver);
	}

	List<HOfficeOutgoingApprover> approff = new ArrayList<HOfficeOutgoingApprover>();
	for (String app : outgoing_approver_id) {
	    HOfficeOutgoingApprover approver = new HOfficeOutgoingApprover();
	    approver.setEmp_id(emp_id);
	    approver.setApprover_id(Integer.parseInt(app));
	    approver.setDeleted(false);
	    approver.setModId(session.getEmp().getAvator());
	    approff.add(approver);
	}

	Integer workid = wamsService.insertHWorkInfo(empworkInfo);

	Integer approver = wamsService.insertHLeaveApprover(appr);

	Integer approveroff = wamsService.insertHOfficeOutgoingApprover(approff);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", workid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addholidaywork", method = RequestMethod.POST)
    public ModelAndView addholidayWork(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HHoliday holidayInfo = new HHoliday();

	holidayInfo.setHoliday_desc(parameterMap.get("holiday_desc")[0]);
	holidayInfo.setDate_from(convertDateFormate(parameterMap.get("from_date")[0]));
	holidayInfo.setDate_to(convertDateFormate(parameterMap.get("to_date")[0]));
	holidayInfo.setHoliday_year(getYear(parameterMap.get("from_date")[0]));
	holidayInfo.setDeleted(false);
	holidayInfo.setModId(session.getEmp().getAvator());

	Integer workid = wamsService.insertHHolidayInfo(holidayInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", workid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/editholidaywork", method = RequestMethod.POST)
    public ModelAndView editholidaywork(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	String id = parameterMap.get("id")[0];
	if (id == null) {
	    return null;
	}
	int holidayId = Integer.parseInt(id);
	HHoliday holidayInfo = wamsService.getHolidayById(holidayId);

	holidayInfo.setId(holidayId);
	holidayInfo.setHoliday_desc(parameterMap.get("holiday_desc")[0]);
	holidayInfo.setDate_from(convertDateFormate(parameterMap.get("from_date")[0]));
	holidayInfo.setDate_to(convertDateFormate(parameterMap.get("to_date")[0]));
	holidayInfo.setHoliday_year(getYear(parameterMap.get("from_date")[0]));
	holidayInfo.setDeleted(false);
	holidayInfo.setModId(session.getEmp().getAvator());

	Integer workid = wamsService.editHHoliday(holidayInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", workid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/searchMonthlyAtt", method = RequestMethod.POST)
    public ModelAndView searchMonthlyAtt(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 3) {
	    logger.warn("Expecting atleast 3 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String month = requestUriSplit[0];
	String yer = requestUriSplit[1];
	String empId = requestUriSplit[2];

	// if(month.equals("") || yer.equals("") || empId.equals(""))

	// Date dd = new Date();
	int mon = Integer.parseInt(month);
	int year = Integer.parseInt(yer);

	int emp_id = Integer.parseInt(empId);

	List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
	thisMonAtt = wamsService.getAttendanceForMonth(mon, year, emp_id, false);

	mm.addAttribute("monAtt", thisMonAtt);

	return new ModelAndView("monthlyattsearch", mm);
    }

    @RequestMapping(value = "/attmodify", method = RequestMethod.POST)
    public ModelAndView attmodify(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Map<String, String[]> parameterMap = request.getParameterMap();

	String inout = parameterMap.get("inout")[0];
	String EmpDate = parameterMap.get("att_date")[0];
	String EmpTimehh = parameterMap.get("hour")[0];
	String EmpTimemm = parameterMap.get("minute")[0];
	String emp_id = parameterMap.get("emp_id")[0];

	ModelMap mm = new ModelMap();
	boolean Fo = findFutureDate(EmpDate);
	if (Fo) {

	    mm.addAttribute("msg", "-1");

	    return new ModelAndView("result", mm);
	}

	EmpDate = convertDateFormate(EmpDate);
	String EmpTime = getFormatedNumber(EmpTimehh) + ":" + getFormatedNumber(EmpTimemm) + ":00";

	System.out.println(emp_id + "  " + inout + "  " + EmpDate + "   " + EmpTime);

	HAttendance att = new HAttendance();
	att.setEmp_id(Integer.parseInt(emp_id));
	att.setAtt_date(EmpDate);
	att.setAtt_time(EmpTime);
	att.setAtt_inout(Integer.parseInt(inout));
	att.setModId(getSessionService().getUserSession().getEmp().getAvator());
	att.setDeleted(false);

	Integer insert = wamsService.insertHAttendance(att);

	mm.addAttribute("msg", insert);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addleaverequest", method = RequestMethod.POST)
    public ModelAndView addleaverequest(HttpServletRequest request, HttpServletResponse response)
	    throws MessagingException {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HLeave leaveInfo = new HLeave();

	leaveInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	HTypesOfLeave obj = new HTypesOfLeave();
	Integer leaveId = Integer.parseInt(parameterMap.get("leavetype")[0]);
	obj = wamsService.getLeaveTypeById(leaveId);
	leaveInfo.setLeavetype(obj);
	leaveInfo.setFdate(convertDateFormate(parameterMap.get("fdate")[0]));
	leaveInfo.setTdate(convertDateFormate(parameterMap.get("tdate")[0]));
	leaveInfo.setDays(Float.parseFloat(parameterMap.get("days")[0]));
	leaveInfo.setReason(parameterMap.get("reason")[0]);
	HLeaveQuota objquota = new HLeaveQuota();
	String quotayear = parameterMap.get("year")[0];
	objquota = wamsService.getLeaveQuota(leaveInfo.getEmp_id(), quotayear, leaveInfo.getLeavetype().getId());
	leaveInfo.setLeavequota(objquota);

	int noa = Integer.parseInt(parameterMap.get("appsize")[0]);

	String app1 = "", app2 = "", app3 = "", app4 = "", app5 = "";

	if (noa > 0) {
	    app1 = parameterMap.get("approvar1")[0];
	    if (!app1.equals(""))
		leaveInfo.setApprovar1(Integer.parseInt(app1));
	}
	if (noa > 1) {
	    app2 = parameterMap.get("approvar2")[0];
	    if (!app2.equals(""))
		leaveInfo.setApprovar2(Integer.parseInt(app2));
	}
	if (noa > 2) {
	    app3 = parameterMap.get("approvar3")[0];
	    if (!app3.equals(""))
		leaveInfo.setApprovar3(Integer.parseInt(app3));
	}
	if (noa > 3) {
	    app4 = parameterMap.get("approvar4")[0];
	    if (!app4.equals(""))
		leaveInfo.setApprovar4(Integer.parseInt(app4));
	}
	if (noa > 4) {
	    app5 = parameterMap.get("approvar5")[0];
	    if (!app5.equals(""))
		leaveInfo.setApprovar5(Integer.parseInt(app5));
	}

	leaveInfo.setStatus(0);
	leaveInfo.setApp1state(false);
	leaveInfo.setApp2state(false);
	leaveInfo.setApp3state(false);
	leaveInfo.setApp4state(false);
	leaveInfo.setApp5state(false);
	leaveInfo.setDeleted(false);
	leaveInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	leaveInfo.setApllieddate(DateTimeFormateforDB().format(now));
	leaveInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer emid = wamsService.insertHLeaveInfo(leaveInfo);
	// Update Leave Quota
	// HLeaveQuota quota = wamsService.getLeaveQuotas(empId, year);
	// Update Leave Quota
	HEmployee employee = wamsService.getEmployeeById(emid);
	// Send Mail to Approver for Approve Leave request
	if ((emid == leaveInfo.getEmp_id()) && (!app1.equals("")) && (app1 != null)) {
	    String EMailto[] = new String[1];
	    HEmployee approvarProfile = wamsService.getEmployeeById(Integer.parseInt(app1));
	    EMailto[0] = approvarProfile.getEmail();
	    String subject = "Leave Approval Request";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#LEAVE_TYPE#>", leaveInfo.getLeavetype().getLeavetype());
	    email.put("<#REASON#>", leaveInfo.getReason());
	    email.put("<#NO_OF_DAYS#>",
		    (leaveInfo.getDays() <= 1 ? leaveInfo.getDays() + " day" : leaveInfo.getDays() + " day's"));
	    email.put("<#DATES#>", (leaveInfo.getDays() <= 1 ? leaveInfo.getFdate()
		    : leaveInfo.getFdate() + " to " + leaveInfo.getTdate()));
	    email.put("<#EMP_NAME#>", employee.getEmployee_name());
	    email.put("<#DESIGNATION#>", employee.getDesignation());
	    email.put("<#EMP_EMAIL#>", employee.getEmail());

	    /*
	     * String link = request.getHeader("referer"); int index =
	     * link.indexOf("WAMS"); String requestUri = link.substring(0,
	     * index);
	     */
	    //String requestUri = "http://123.200.15.18:8080/";
	    String requestUri = "http://sharing.com.bd:8181/";
	    email.put("<#APPROVE_LINK#>", requestUri + "WAMS/leaveapprovalList");

	    String body = getEmialTemplateFromResource("leaverequest", email);

	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc(employee.getEmail());
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(session.getEmp().getAvator());
	    if (mail.getTo().equals("") || mail.getTo().equals("null")) {
		mail.setTo("ala@webhawksit.com");
		mail.setSubject("null value find for Mail to address");
	    }

	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, employee.getEmail(), subject, body,
	    // mailSender);

	}
	// Send Mail to Approver for Approve Leave request

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", emid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addofficeoutgoingrequest", method = RequestMethod.POST)
    public ModelAndView addofficeoutgoingrequest(HttpServletRequest request, HttpServletResponse response)
	    throws MessagingException {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HOfficeOutgoing outgoingInfo = new HOfficeOutgoing();

	outgoingInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	outgoingInfo.setDate(convertStringToDateFormate(parameterMap.get("date")[0]));
	outgoingInfo.setFtime(convertTimeFormat(parameterMap.get("fromtime")[0]));
	outgoingInfo.setTtime(convertTimeFormat(parameterMap.get("totime")[0]));
	outgoingInfo.setReason(parameterMap.get("reason")[0]);

	int noa = Integer.parseInt(parameterMap.get("appsize")[0]);

	String app1 = "", app2 = "", app3 = "", app4 = "", app5 = "";

	if (noa > 0) {
	    app1 = parameterMap.get("approvar1")[0];
	    if (!app1.equals(""))
		outgoingInfo.setApprovar1(Integer.parseInt(app1));
	}
	if (noa > 1) {
	    app2 = parameterMap.get("approvar2")[0];
	    if (!app2.equals(""))
		outgoingInfo.setApprovar2(Integer.parseInt(app2));
	}
	if (noa > 2) {
	    app3 = parameterMap.get("approvar3")[0];
	    if (!app3.equals(""))
		outgoingInfo.setApprovar3(Integer.parseInt(app3));
	}
	if (noa > 3) {
	    app4 = parameterMap.get("approvar4")[0];
	    if (!app4.equals(""))
		outgoingInfo.setApprovar4(Integer.parseInt(app4));
	}
	if (noa > 4) {
	    app5 = parameterMap.get("approvar5")[0];
	    if (!app5.equals(""))
		outgoingInfo.setApprovar5(Integer.parseInt(app5));
	}

	outgoingInfo.setStatus(0);
	outgoingInfo.setApp1state(false);
	outgoingInfo.setApp2state(false);
	outgoingInfo.setApp3state(false);
	outgoingInfo.setApp4state(false);
	outgoingInfo.setApp5state(false);
	outgoingInfo.setDeleted(false);
	outgoingInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	outgoingInfo.setApllieddate(DateTimeFormateforDB().format(now));
	outgoingInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer emid = wamsService.insertHOfficeOutgoingInfo(outgoingInfo);

	HEmployee employee = wamsService.getEmployeeById(emid);
	// Send Mail to Approver for Approve Leave request
	if ((emid == outgoingInfo.getEmp_id()) && (!app1.equals("")) && (app1 != null)) {
	    String EMailto[] = new String[1];
	    HEmployee approvarProfile = wamsService.getEmployeeById(Integer.parseInt(app1));
	    EMailto[0] = approvarProfile.getEmail();
	    String subject = "Office Outgoing Approval Request";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#REASON#>", outgoingInfo.getReason());
	    email.put("<#DATE#>", outgoingInfo.getDate().toString());
	    email.put("<#FROM_TIME#>", outgoingInfo.getFtime());
	    email.put("<#TO_TIME#>", outgoingInfo.getTtime());
	    email.put("<#EMP_NAME#>", employee.getEmployee_name());
	    email.put("<#DESIGNATION#>", employee.getDesignation());
	    email.put("<#EMP_EMAIL#>", employee.getEmail());

	    /*
	     * String link = request.getHeader("referer"); int index =
	     * link.indexOf("WAMS"); String requestUri = link.substring(0,
	     * index);
	     */
	    //String requestUri = "http://123.200.15.18:8080/";
	    String requestUri = "http://sharing.com.bd:8181/";
	    email.put("<#APPROVE_LINK#>", requestUri + "WAMS/outgoingapprovalList");

	    String body = getEmialTemplateFromResource("officeoutgoingrequest", email);

	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc(employee.getEmail());
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(session.getEmp().getAvator());
	    if (mail.getTo().equals("") || mail.getTo().equals("null")) {
		mail.setTo("ala@webhawksit.com");
		mail.setSubject("null value find for Mail to address");
	    }

	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, employee.getEmail(), subject, body,
	    // mailSender);

	}
	// Send Mail to Approver for Approve Leave request

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", emid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/searchDailyAtt", method = RequestMethod.POST)
    public ModelAndView searchDailyAtt(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 1) {
	    logger.warn("Expecting atleast 1 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String dd = requestUriSplit[0];
	Date date = new Date();
	try {
	    date = DateFormateforView().parse(dd);
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	List<HMAttendance> att = wamsService.getAttendanceByDay(date);

	mm.addAttribute("date", dd);
	mm.addAttribute("att", att);

	return new ModelAndView("dailyattsearch", mm);
    }

    @RequestMapping(value = "/searchDailyAttByTeam", method = RequestMethod.POST)
    public ModelAndView searchDailyAttByTeam(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String dd = requestUriSplit[0];
	String team = requestUriSplit[1];
	Date date = new Date();
	try {
	    date = DateFormateforView().parse(dd);
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	List<HMAttendance> tempatt = wamsService.getAttendanceByDay(date);
	List<HMAttendance> att = new ArrayList<HMAttendance>();
	List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	for (HMAttendance aAtt : tempatt) {
	    for (HEmployee aEmp : allEmp) {
		if (aAtt.getEmp_id() == aEmp.getEmp_id()) {
		    if (aEmp.getTeamId() != null && aEmp.getTeamId() == Integer.parseInt(team)) {
			String start = "", end = "";
			if (!aAtt.getAtt_in().equals(""))
			    start = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_in());
			if (!aAtt.getAtt_out().equals(""))
			    end = aAtt.getAtt_date() + " " + changeTimeFormate(aAtt.getAtt_out());

			if ((!start.equals("")) && (!end.equals(""))) {
			    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			    Date d1 = null;
			    Date d2 = null;

			    try {
				d1 = format.parse(start);
				d2 = format.parse(end);

				// in milliseconds
				long diff = d2.getTime() - d1.getTime();

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				String wrkTime = diffHours + " Hours " + diffMinutes + " Minutes";
				aAtt.setWrkTime(wrkTime);

			    } catch (Exception e) {
				e.printStackTrace();
			    }
			}
			att.add(aAtt);
			break;
		    }
		}
	    }
	}

	mm.addAttribute("date", dd);
	mm.addAttribute("att", att);

	return new ModelAndView("dailyattsearchbyteam", mm);
    }

    @RequestMapping(value = "/calculateLeave", method = RequestMethod.POST)
    public ModelAndView calculateLeave(HttpServletRequest request) throws Exception {
	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 7) {
	    logger.warn("Expecting atleast 7 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String fdate = requestUriSplit[0];
	String tdate = requestUriSplit[1];
	int empId = Integer.parseInt(requestUriSplit[2]);
	int excludeLeave = Integer.parseInt(requestUriSplit[3]);
	int leavetypeid = Integer.parseInt(requestUriSplit[4]);
	String selectYear = requestUriSplit[5];
	float seldays = Float.parseFloat(requestUriSplit[6]);

	HTypesOfLeave type = wamsService.getLeaveTypeById(leavetypeid);
	// 7 days check
	boolean oldmorethen7 = false;
	/*
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date
	 * toDate = new Date(); String formateddate = convertDateFormate(fdate);
	 * Date fromDate = sdf.parse(formateddate);
	 * 
	 * Calendar c = Calendar.getInstance(); c.setTime(fromDate);
	 * c.add(Calendar.DATE, 10); if (c.getTime().compareTo(toDate) < 0) {
	 * oldmorethen7 = true; }
	 */
	// 7 days check

	List<String> nod = wamsService.calculateLeaveDate(fdate, tdate, empId, excludeLeave);

	// Leave Quotas
	boolean validQuota = true;
	if (type.getIsquota())
	    validQuota = DateCompare(fdate, tdate, selectYear);

	List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(empId, selectYear);

	float nodselected = nod.size();
	if ((type.getLeavededuct() < 1) && (type.getLeavededuct() > 0)) {
	    nodselected = type.getLeavededuct();
	}
	boolean leaveAvail = false;
	float noofdayavail = 0;
	if (!type.getIsquota()) {
	    leaveAvail = true;
	} else {
	    for (HLeaveQuota qutoa : quotas) {
		if ((qutoa.getType_id() == type.getId()) || (qutoa.getType_id() == type.getDeductfrom())) {
		    if (excludeLeave == 0) {
			if ((qutoa.getQuota() - qutoa.getUsed()) >= nodselected) {
			    leaveAvail = true;
			    noofdayavail = qutoa.getQuota() - qutoa.getUsed();
			    break;
			} else {
			    leaveAvail = false;
			    noofdayavail = qutoa.getQuota() - qutoa.getUsed();
			    break;
			}
		    } else {
			if (((qutoa.getQuota() - qutoa.getUsed()) + seldays) >= nodselected) {
			    leaveAvail = true;
			    noofdayavail = (qutoa.getQuota() - qutoa.getUsed()) + seldays;
			    break;
			} else {
			    leaveAvail = false;
			    noofdayavail = (qutoa.getQuota() - qutoa.getUsed()) + seldays;
			    break;
			}
		    }
		}
	    }
	}
	// Leave Quotas
	if ((nod.size() > 0) && (leaveAvail) && (validQuota) && (!oldmorethen7)) {
	    mm.addAttribute("nod", nod);
	    mm.addAttribute("nsize", nodselected);

	    return new ModelAndView("leavelist", mm);
	} else if (!leaveAvail) {
	    mm.addAttribute("msg", "Not Available Quota for " + type.getLeavetype());
	    return new ModelAndView("result", mm);
	} else if (!validQuota) {
	    mm.addAttribute("msg", "Selected Quota Not Match With Selected Date");
	    return new ModelAndView("result", mm);
	} else if (oldmorethen7) {
	    mm.addAttribute("msg", "You Did Not raised 7 days old leave request.");
	    return new ModelAndView("result", mm);
	} else {
	    mm.addAttribute("msg", "-1");

	    return new ModelAndView("result", mm);
	}
    }

    @RequestMapping(value = "/updatepass", method = RequestMethod.POST)
    public ModelAndView updatepass(HttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();
	HEmployee currentUser = session.getEmp();
	Map<String, String[]> parameterMap = request.getParameterMap();
	ModelMap mm = new ModelMap();
	String newPass = parameterMap.get("new_pass")[0];

	HEmployee emp = new HEmployee();
	HEmployee passemp = null;
	int emp_id = Integer.parseInt(parameterMap.get("emp_id")[0]);

	emp = wamsService.getEmployeeById(emp_id);

	if ((!currentUser.getUsertype().equals("Admin")) && (!currentUser.getUsertype().equalsIgnoreCase("Talent Manager"))) {
	    if (currentUser.getEmp_id() != emp_id) {
		mm.addAttribute("msg", "-1");
		return new ModelAndView("result", mm);
	    }
	    String oldPass = parameterMap.get("old_pass")[0];
	    emp.setPassword(oldPass);
	    passemp = wamsService.getUserValidation(emp);
	    /*
	     * if(!emp.getPassword().equals(oldPass)) { mm.addAttribute("msg",
	     * "-1"); return new ModelAndView("result", mm); }
	     */
	    if (passemp == null) {
		mm.addAttribute("msg", "-1");
		return new ModelAndView("result", mm);
	    }
	}
	emp.setPassword(newPass);
	emp.setBirthdate_certificate(convertDateFormate(emp.getBirthdate_certificate()));
	emp.setBirthdate_real(convertDateFormate(emp.getBirthdate_real()));
	emp.setDeleted(false);
	emp.setModId(session.getEmp().getAvator());

	Integer emid = wamsService.editHEmployeePass(emp);

	mm.addAttribute("msg", emid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/employeeDelete", method = RequestMethod.POST)
    public ModelAndView employeeDelete(HttpServletRequest request) throws Exception {
	String id = getStringFromHttpRequest(request);
	logger.info("EmpId=" + id);
	UserSession session = getSessionService().getUserSession();

	HEmployee empInfo = new HEmployee();
	Integer emp_id = Integer.parseInt(id);

	empInfo = wamsService.getEmployeeById(emp_id);
	empInfo.setBirthdate_certificate(convertDateFormate(empInfo.getBirthdate_certificate()));
	empInfo.setBirthdate_real(convertDateFormate(empInfo.getBirthdate_real()));
	empInfo.setJoin_date(convertDateFormate(empInfo.getJoin_date()));
	empInfo.setDeleted(true);
	empInfo.setModId(session.getEmp().getAvator());

	Integer empid = wamsService.editHEmployee(empInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/holidayDelete", method = RequestMethod.POST)
    public ModelAndView holidayDelete(HttpServletRequest request) throws Exception {
	String id = getStringFromHttpRequest(request);
	logger.info("HolidayId=" + id);
	UserSession session = getSessionService().getUserSession();

	HHoliday holidayInfo = new HHoliday();
	Integer holiday_id = Integer.parseInt(id);

	holidayInfo = wamsService.getHolidayById(holiday_id);
	holidayInfo.setDate_from(convertDateFormate(holidayInfo.getDate_from()));
	holidayInfo.setDate_to(convertDateFormate(holidayInfo.getDate_to()));
	holidayInfo.setDeleted(true);
	holidayInfo.setModId(session.getEmp().getAvator());

	Integer empid = wamsService.editHHoliday(holidayInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/addwatcher", method = RequestMethod.POST)
    public ModelAndView addwatcher(HttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HEmployee empInfo = new HEmployee();

	// empInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	empInfo.setEmployee_name(parameterMap.get("emp_name")[0]);
	empInfo.setAvator(parameterMap.get("emp_avator")[0]);
	empInfo.setEmail(parameterMap.get("emp_email")[0]);
	empInfo.setDesignation(parameterMap.get("emp_desig")[0]);
	empInfo.setSkype_id(parameterMap.get("emp_skype")[0]);
	// empInfo.setBloodgroup(parameterMap.get("bloodgroup")[0]);
	String birthCertificate = "";
	if (birthCertificate.equals(""))
	    birthCertificate = "01-01-1970";
	empInfo.setBirthdate_certificate(convertDateFormate(birthCertificate));
	String birthReal = "";
	if (birthReal.equals(""))
	    birthReal = "01-01-1970";
	empInfo.setBirthdate_real(convertDateFormate(birthReal));
	// empInfo.setNid(parameterMap.get("emp_nid")[0]);
	// empInfo.setPersonal_email(parameterMap.get("emp_pemail")[0]);
	empInfo.setUsertype(parameterMap.get("usertype")[0]);
	empInfo.setMobile(parameterMap.get("emp_contact")[0]);
	empInfo.setDeleted(false);
	empInfo.setModId(session.getEmp().getAvator());
	empInfo.setPassword(parameterMap.get("emp_pass")[0]);
	empInfo.setEmp_number(parameterMap.get("emp_number")[0]);

	Integer empid = wamsService.insertHEmployee(empInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updatewatcher", method = RequestMethod.POST)
    public ModelAndView updatewatcher(HttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HEmployee empInfo = new HEmployee();
	Integer emp_id = Integer.parseInt(parameterMap.get("emp_id")[0]);

	empInfo = wamsService.getEmployeeById(emp_id);

	empInfo.setEmp_id(emp_id);
	empInfo.setEmployee_name(parameterMap.get("emp_name")[0]);
	empInfo.setAvator(parameterMap.get("emp_avator")[0]);
	empInfo.setEmail(parameterMap.get("emp_email")[0]);
	empInfo.setDesignation(parameterMap.get("emp_desig")[0]);
	empInfo.setSkype_id(parameterMap.get("emp_skype")[0]);
	// empInfo.setBloodgroup(parameterMap.get("bloodgroup")[0]);
	String birthCertificate = "";
	if (birthCertificate.equals(""))
	    birthCertificate = "01-01-1970";
	empInfo.setBirthdate_certificate(convertDateFormate(birthCertificate));
	String birthReal = "";
	if (birthReal.equals(""))
	    birthReal = "01-01-1970";
	empInfo.setBirthdate_real(convertDateFormate(birthReal));
	// empInfo.setNid(parameterMap.get("emp_nid")[0]);
	// empInfo.setPersonal_email(parameterMap.get("emp_pemail")[0]);
	empInfo.setUsertype(parameterMap.get("usertype")[0]);
	empInfo.setMobile(parameterMap.get("emp_contact")[0]);
	empInfo.setDeleted(false);
	empInfo.setModId(session.getEmp().getAvator());
	empInfo.setEmp_number(parameterMap.get("emp_number")[0]);

	Integer empid = wamsService.editHEmployee(empInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/approveRejectLeave", method = RequestMethod.POST)
    public ModelAndView approveRejectLeave(HttpServletRequest request) throws Exception {
	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 3) {
	    logger.warn("Expecting atleast 3 arguments but received " + requestUriSplit.length);
	    return null;
	}
	HEmployee emp = getSessionService().getUserSession().getEmp();
	Date approveDate = new Date();

	ModelMap mm = new ModelMap();
	String commants = requestUriSplit[0];
	String leaveId = requestUriSplit[1];
	String appReject = requestUriSplit[2];

	int intappReject = Integer.parseInt(appReject);
	int appSeq = 0;
	int nextAppId = 0;
	HLeave leave = wamsService.getLeaveById(Integer.parseInt(leaveId));
	if ((null != leave.getApprovar1()) && (leave.getApprovar1() == emp.getEmp_id())) {
	    appSeq = 1;
	    if (null != leave.getApprovar2() && intappReject == 1)
		nextAppId = leave.getApprovar2();
	} else if ((null != leave.getApprovar2()) && (leave.getApprovar2() == emp.getEmp_id())) {
	    appSeq = 2;
	    if (null != leave.getApprovar3() && intappReject == 1)
		nextAppId = leave.getApprovar3();
	} else if ((null != leave.getApprovar3()) && (leave.getApprovar3() == emp.getEmp_id())) {
	    appSeq = 3;
	    if (null != leave.getApprovar4() && intappReject == 1)
		nextAppId = leave.getApprovar4();
	} else if ((null != leave.getApprovar4()) && (leave.getApprovar4() == emp.getEmp_id())) {
	    appSeq = 4;
	    if (null != leave.getApprovar5() && intappReject == 1)
		nextAppId = leave.getApprovar5();
	} else {
	    appSeq = 5;
	    nextAppId = 0;
	}

	HLeaveStatus status = new HLeaveStatus();
	status.setLeave_id(Integer.parseInt(leaveId));
	status.setApprover_id(emp.getEmp_id());
	status.setComments(commants);
	status.setApprovalstate(intappReject);
	status.setDate(DateFormateforDB().format(approveDate));
	status.setApprovarSeq(appSeq);
	status.setDeleted(false);
	status.setModId(emp.getAvator());

	int appSt = -1;
	if (nextAppId != 0)
	    appSt = wamsService.approveRejectLeave(status, 0);
	else
	    appSt = wamsService.approveRejectLeave(status, 1);

	HEmployee employee = wamsService.getEmployeeById(leave.getEmp_id());
	List<HLeaveStatus> allStatus = wamsService.getAllLeaveStatusByLeaveId(false, Integer.parseInt(leaveId));
	// Send Mail to Approver for Approve Leave request
	if (nextAppId != 0) {
	    String EMailto[] = new String[1];
	    HEmployee approvarProfile = wamsService.getEmployeeById(nextAppId);
	    EMailto[0] = approvarProfile.getEmail();
	    String subject = "Leave Approval Request";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#LEAVE_TYPE#>", leave.getLeavetype().getLeavetype());
	    email.put("<#REASON#>", leave.getReason());
	    email.put("<#NO_OF_DAYS#>", (leave.getDays() <= 1 ? leave.getDays() + " day" : leave.getDays() + " day's"));
	    email.put("<#DATES#>",
		    (leave.getDays() <= 1 ? leave.getFdate() : leave.getFdate() + " to " + leave.getTdate()));
	    email.put("<#EMP_NAME#>", employee.getEmployee_name());
	    email.put("<#DESIGNATION#>", employee.getDesignation());
	    email.put("<#EMP_EMAIL#>", employee.getEmail());

	    /*
	     * String link = request.getHeader("referer"); int index =
	     * link.indexOf("WAMS"); String requestUrl = link.substring(0,
	     * index);
	     */
	    //String requestUrl = "http://123.200.15.18:8080/";
	    String requestUrl = "http://sharing.com.bd:8181/";
	    email.put("<#APPROVE_LINK#>", requestUrl + "WAMS/leaveapprovalList");
	    StringBuffer approval = new StringBuffer("<table  border='1'>");
	    for (HLeaveStatus ls : allStatus) {
		if (ls.getApprovalstate() == 1 || ls.getApprovalstate() == 2) {
		    String approverName = wamsService.getEmployeeById(ls.getApprover_id()).getEmployee_name();
		    approval.append("<tr><td>Approver ");
		    approval.append(approverName);
		    approval.append(" Comments:</td><td>");
		    approval.append(ls.getComments());
		    approval.append("</td></tr>");
		}
	    }
	    approval.append("</table>");
	    email.put("<#approvals#>", approval.toString());

	    String body = getEmialTemplateFromResource("leaverequestapproved", email);
	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc(employee.getEmail());
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(emp.getAvator());
	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, employee.getEmail(), subject, body,
	    // mailSender);
	} else {
	    String EMailto[] = new String[1];
	    HEmployee empProfile = wamsService.getEmployeeById(leave.getEmp_id());
	    EMailto[0] = empProfile.getEmail();
	    String subject = "Leave Approval Request";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#LEAVE_TYPE#>", leave.getLeavetype().getLeavetype());
	    email.put("<#EMP_NAME#>", employee.getEmployee_name());

	    if (intappReject == 1)
		email.put("<#status#>", "approved");
	    else
		email.put("<#status#>", "rejected");
	    StringBuffer approval = new StringBuffer("<table  border='1'>");
	    for (HLeaveStatus ls : allStatus) {
		if (ls.getApprovalstate() == 1 || ls.getApprovalstate() == 2) {
		    String approverName = wamsService.getEmployeeById(ls.getApprover_id()).getEmployee_name();
		    approval.append("<tr><td>Approver ");
		    approval.append(approverName);
		    approval.append(" Comments:</td><td>");
		    approval.append(ls.getComments());
		    approval.append("</td></tr>");
		}
	    }
	    approval.append("</table>");
	    email.put("<#approvals#>", approval.toString());

	    String body = getEmialTemplateFromResource("leavestatus", email);
	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc("");
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(emp.getAvator());
	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, "", subject, body, mailSender);
	}
	// Send Mail to Approver for Approve Leave request

	mm.addAttribute("msg", appSt);

	return new ModelAndView("result", mm);

    }

    @RequestMapping(value = "/approveRejectOutgoing", method = RequestMethod.POST)
    public ModelAndView approveRejectOutgoing(HttpServletRequest request) throws Exception {
	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 3) {
	    logger.warn("Expecting atleast 3 arguments but received " + requestUriSplit.length);
	    return null;
	}
	HEmployee emp = getSessionService().getUserSession().getEmp();
	Date approveDate = new Date();

	ModelMap mm = new ModelMap();
	String commants = requestUriSplit[0];
	String outgoingId = requestUriSplit[1];
	String appReject = requestUriSplit[2];

	int intappReject = Integer.parseInt(appReject);
	int appSeq = 0;
	int nextAppId = 0;
	HOfficeOutgoing outgoing = wamsService.getOfficeOutgoingById(Integer.parseInt(outgoingId));
	if ((null != outgoing.getApprovar1()) && (outgoing.getApprovar1() == emp.getEmp_id())) {
	    appSeq = 1;
	    if (null != outgoing.getApprovar2() && intappReject == 1)
		nextAppId = outgoing.getApprovar2();
	} else if ((null != outgoing.getApprovar2()) && (outgoing.getApprovar2() == emp.getEmp_id())) {
	    appSeq = 2;
	    if (null != outgoing.getApprovar3() && intappReject == 1)
		nextAppId = outgoing.getApprovar3();
	} else if ((null != outgoing.getApprovar3()) && (outgoing.getApprovar3() == emp.getEmp_id())) {
	    appSeq = 3;
	    if (null != outgoing.getApprovar4() && intappReject == 1)
		nextAppId = outgoing.getApprovar4();
	} else if ((null != outgoing.getApprovar4()) && (outgoing.getApprovar4() == emp.getEmp_id())) {
	    appSeq = 4;
	    if (null != outgoing.getApprovar5() && intappReject == 1)
		nextAppId = outgoing.getApprovar5();
	} else {
	    appSeq = 5;
	    nextAppId = 0;
	}

	HOfficeOutgoingStatus status = new HOfficeOutgoingStatus();
	status.setOutgoing_id(Integer.parseInt(outgoingId));
	status.setApprover_id(emp.getEmp_id());
	status.setComments(commants);
	status.setApprovalstate(intappReject);
	status.setDate(DateFormateforDB().format(approveDate));
	status.setApprovarSeq(appSeq);
	status.setDeleted(false);
	status.setModId(emp.getAvator());

	int appSt = -1;
	if (nextAppId != 0)
	    appSt = wamsService.approveRejectOfficeOutgoing(status, 0);
	else
	    appSt = wamsService.approveRejectOfficeOutgoing(status, 1);

	HEmployee employee = wamsService.getEmployeeById(outgoing.getEmp_id());
	List<HOfficeOutgoingStatus> allStatus = wamsService.getAllOfficeOutgoingStatusByOutgoingId(false,
		Integer.parseInt(outgoingId));
	// Send Mail to Approver for Approve Leave request
	if (nextAppId != 0) {
	    String EMailto[] = new String[1];
	    HEmployee approvarProfile = wamsService.getEmployeeById(nextAppId);
	    EMailto[0] = approvarProfile.getEmail();
	    String subject = "Office Outgoing Approval Request";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#REASON#>", outgoing.getReason());
	    email.put("<#DATE#>", outgoing.getDate().toString());
	    email.put("<#FROM_TIME#>", outgoing.getFtime());
	    email.put("<#TO_TIME#>", outgoing.getTtime());
	    email.put("<#EMP_NAME#>", employee.getEmployee_name());
	    email.put("<#DESIGNATION#>", employee.getDesignation());
	    email.put("<#EMP_EMAIL#>", employee.getEmail());
	    /*
	     * String link = request.getHeader("referer"); int index =
	     * link.indexOf("WAMS"); String requestUrl = link.substring(0,
	     * index);
	     */
	    //String requestUrl = "http://123.200.15.18:8080/";
	    String requestUrl = "http://sharing.com.bd:8181/";
	    email.put("<#APPROVE_LINK#>", requestUrl + "WAMS/outgoingapprovalList");
	    StringBuffer approval = new StringBuffer("<table  border='1'>");
	    for (HOfficeOutgoingStatus ls : allStatus) {
		if (ls.getApprovalstate() == 1 || ls.getApprovalstate() == 2) {
		    String approverName = wamsService.getEmployeeById(ls.getApprover_id()).getEmployee_name();
		    approval.append("<tr><td>Approver ");
		    approval.append(approverName);
		    approval.append(" Comments:</td><td>");
		    approval.append(ls.getComments());
		    approval.append("</td></tr>");
		}
	    }
	    approval.append("</table>");
	    email.put("<#approvals#>", approval.toString());

	    String body = getEmialTemplateFromResource("officeoutgoingrequestapproved", email);
	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc(employee.getEmail());
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(emp.getAvator());
	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, employee.getEmail(), subject, body,
	    // mailSender);
	} else {
	    String EMailto[] = new String[1];
	    HEmployee empProfile = wamsService.getEmployeeById(outgoing.getEmp_id());
	    EMailto[0] = empProfile.getEmail();
	    String subject = "Office Outgoing Approval Status";
	    Map<String, String> email = new HashMap<String, String>();

	    email.put("<#EMP_NAME#>", employee.getEmployee_name());

	    if (intappReject == 1)
		email.put("<#status#>", "approved");
	    else
		email.put("<#status#>", "rejected");
	    StringBuffer approval = new StringBuffer("<table  border='1'>");
	    for (HOfficeOutgoingStatus ls : allStatus) {
		if (ls.getApprovalstate() == 1 || ls.getApprovalstate() == 2) {
		    String approverName = wamsService.getEmployeeById(ls.getApprover_id()).getEmployee_name();
		    approval.append("<tr><td>Approver ");
		    approval.append(approverName);
		    approval.append(" Comments:</td><td>");
		    approval.append(ls.getComments());
		    approval.append("</td></tr>");
		}
	    }
	    approval.append("</table>");
	    email.put("<#approvals#>", approval.toString());

	    String body = getEmialTemplateFromResource("officeoutgoingstatus", email);
	    // prepare for save
	    Date date = new Date();
	    HMail mail = new HMail();
	    mail.setTo(EMailto[0]);
	    mail.setFrom("info.webhawksit@gmail.com");
	    mail.setSubject(subject);
	    mail.setCc("");
	    mail.setBcc("");
	    mail.setDate(DateTimeFormateforDB().format(date));
	    mail.setMsg(escapeHtml4(body));
	    mail.setStatus(0);
	    mail.setSendTime(DateTimeFormateforDB().format(date));
	    mail.setDeleted(false);
	    mail.setModId(emp.getAvator());
	    int stat = wamsService.sendMail(mail);
	    // prepare for save
	    // sendEmail(EMailto, "", subject, body, mailSender);
	}
	// Send Mail to Approver for Approve Leave request

	mm.addAttribute("msg", appSt);

	return new ModelAndView("result", mm);

    }

    @RequestMapping(value = "/approverList", method = RequestMethod.POST)
    public ModelAndView approverList(HttpServletRequest request) throws Exception {
	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String selemp = requestUriSplit[0];
	String call = requestUriSplit[1];

	int semp = 0;
	semp = Integer.parseInt(selemp);

	HEmployee emp = getSessionService().getUserSession().getEmp();
	mm.addAttribute("emp", emp);
	mm.addAttribute("selUrl", "leavelist");

	List<HEmployee> allEmp = wamsService.getAllEmployee(false);

	for (HEmployee employee : allEmp) {
	    if (employee.getEmp_id() == emp.getEmp_id()) {
		allEmp.remove(employee);
		break;
	    }
	}
	if (call.equals("leave")) {
	    List<HLeaveApprover> approver = wamsService.getLeaveApprovers(false, semp);
	    mm.addAttribute("approver", approver);
	} else {
	    List<HOfficeOutgoingApprover> approver = wamsService.getOfficeOutgoingApprovers(false, semp);
	    mm.addAttribute("approver", approver);
	}
	mm.addAttribute("allEmp", allEmp);

	return new ModelAndView("approverlist", mm);
    }

    @RequestMapping(value = "/cancelLeave", method = RequestMethod.POST)
    public ModelAndView cancelLeave(HttpServletRequest request) throws Exception {
	String id = getStringFromHttpRequest(request);
	logger.info("LeaveId=" + id);
	UserSession session = getSessionService().getUserSession();

	HLeave leaveInfo = new HLeave();
	Integer leave_id = Integer.parseInt(id);

	leaveInfo = wamsService.getLeaveById(leave_id);
	leaveInfo.setFdate(convertDateFormate(leaveInfo.getFdate()));
	leaveInfo.setTdate(convertDateFormate(leaveInfo.getTdate()));
	leaveInfo.setDeleted(true);
	leaveInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	leaveInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer empid = wamsService.editHLeave(leaveInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/cancelOutgoing", method = RequestMethod.POST)
    public ModelAndView cancelOutgoing(HttpServletRequest request) throws Exception {
	String id = getStringFromHttpRequest(request);
	logger.info("OutgoingId=" + id);
	UserSession session = getSessionService().getUserSession();

	HOfficeOutgoing outgoingInfo = new HOfficeOutgoing();
	Integer outgoing_id = Integer.parseInt(id);

	outgoingInfo = wamsService.getOfficeOutgoingById(outgoing_id);
	outgoingInfo.setDeleted(true);
	outgoingInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	outgoingInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer empid = wamsService.editHOfficeOutgoing(outgoingInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updateLeave", method = RequestMethod.POST)
    public ModelAndView updateLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HLeave leaveInfo = new HLeave();

	Integer leave_id = Integer.parseInt(parameterMap.get("leave_id")[0]);

	leaveInfo = wamsService.getLeaveById(leave_id);

	leaveInfo.setFdate(convertDateFormate(parameterMap.get("fdate")[0]));
	leaveInfo.setTdate(convertDateFormate(parameterMap.get("tdate")[0]));
	leaveInfo.setDays(Float.parseFloat(parameterMap.get("days")[0]));
	leaveInfo.setDeleted(false);
	leaveInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	leaveInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer empid = wamsService.editHLeave(leaveInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updateLeaveIndividual", method = RequestMethod.POST)
    public ModelAndView updateLeaveIndividual(HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HLeave leaveInfo = new HLeave();

	Integer leave_id = Integer.parseInt(parameterMap.get("leave_id")[0]);

	leaveInfo = wamsService.getLeaveById(leave_id);

	leaveInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	HTypesOfLeave obj = new HTypesOfLeave();
	Integer leaveId = Integer.parseInt(parameterMap.get("leavetype")[0]);
	obj = wamsService.getLeaveTypeById(leaveId);
	leaveInfo.setLeavetype(obj);
	leaveInfo.setFdate(convertDateFormate(parameterMap.get("fdate")[0]));
	leaveInfo.setTdate(convertDateFormate(parameterMap.get("tdate")[0]));
	leaveInfo.setDays(Float.parseFloat(parameterMap.get("days")[0]));
	leaveInfo.setReason(parameterMap.get("reason")[0]);

	int noa = Integer.parseInt(parameterMap.get("appsize")[0]);

	String app1 = "", app2 = "", app3 = "", app4 = "", app5 = "";

	if (noa > 0) {
	    app1 = parameterMap.get("approvar1")[0];
	    if (!app1.equals(""))
		leaveInfo.setApprovar1(Integer.parseInt(app1));
	}
	if (noa > 1) {
	    app2 = parameterMap.get("approvar2")[0];
	    if (!app2.equals(""))
		leaveInfo.setApprovar2(Integer.parseInt(app2));
	}
	if (noa > 2) {
	    app3 = parameterMap.get("approvar3")[0];
	    if (!app3.equals(""))
		leaveInfo.setApprovar3(Integer.parseInt(app3));
	}
	if (noa > 3) {
	    app4 = parameterMap.get("approvar4")[0];
	    if (!app4.equals(""))
		leaveInfo.setApprovar4(Integer.parseInt(app4));
	}
	if (noa > 4) {
	    app5 = parameterMap.get("approvar5")[0];
	    if (!app5.equals(""))
		leaveInfo.setApprovar5(Integer.parseInt(app5));
	}

	leaveInfo.setStatus(0);
	leaveInfo.setApp1state(false);
	leaveInfo.setApp2state(false);
	leaveInfo.setApp3state(false);
	leaveInfo.setApp4state(false);
	leaveInfo.setApp5state(false);
	leaveInfo.setDeleted(false);
	leaveInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	leaveInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer empid = wamsService.editHLeave(leaveInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updateOutgoingIndividual", method = RequestMethod.POST)
    public ModelAndView updateOutgoingIndividual(HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HOfficeOutgoing outgoingInfo = new HOfficeOutgoing();

	Integer outgoing_id = Integer.parseInt(parameterMap.get("outgoing_id")[0]);

	outgoingInfo = wamsService.getOfficeOutgoingById(outgoing_id);

	outgoingInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	outgoingInfo.setDate(convertStringToDateFormate(parameterMap.get("date")[0]));
	outgoingInfo.setFtime(convertTimeFormat(parameterMap.get("ftime")[0]));
	outgoingInfo.setTtime(convertTimeFormat(parameterMap.get("ttime")[0]));
	outgoingInfo.setReason(parameterMap.get("reason")[0]);

	int noa = Integer.parseInt(parameterMap.get("appsize")[0]);

	String app1 = "", app2 = "", app3 = "", app4 = "", app5 = "";

	if (noa > 0) {
	    app1 = parameterMap.get("approvar1")[0];
	    if (!app1.equals(""))
		outgoingInfo.setApprovar1(Integer.parseInt(app1));
	}
	if (noa > 1) {
	    app2 = parameterMap.get("approvar2")[0];
	    if (!app2.equals(""))
		outgoingInfo.setApprovar2(Integer.parseInt(app2));
	}
	if (noa > 2) {
	    app3 = parameterMap.get("approvar3")[0];
	    if (!app3.equals(""))
		outgoingInfo.setApprovar3(Integer.parseInt(app3));
	}
	if (noa > 3) {
	    app4 = parameterMap.get("approvar4")[0];
	    if (!app4.equals(""))
		outgoingInfo.setApprovar4(Integer.parseInt(app4));
	}
	if (noa > 4) {
	    app5 = parameterMap.get("approvar5")[0];
	    if (!app5.equals(""))
		outgoingInfo.setApprovar5(Integer.parseInt(app5));
	}

	outgoingInfo.setStatus(0);
	outgoingInfo.setApp1state(false);
	outgoingInfo.setApp2state(false);
	outgoingInfo.setApp3state(false);
	outgoingInfo.setApp4state(false);
	outgoingInfo.setApp5state(false);
	outgoingInfo.setDeleted(false);
	outgoingInfo.setModId(session.getEmp().getAvator());
	Date now = new Date();
	outgoingInfo.setModifydate(DateTimeFormateforDB().format(now));

	Integer empid = wamsService.editHOfficeOutgoing(outgoingInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/batchSignout", method = RequestMethod.POST)
    public ModelAndView batchSignout(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Map<String, String[]> parameterMap = request.getParameterMap();

	String outDate = parameterMap.get("signout_date")[0];

	ModelMap mm = new ModelMap();
	boolean Fo = findFutureDate(outDate);
	if (Fo) {

	    mm.addAttribute("msg", "-1");

	    return new ModelAndView("result", mm);
	}

	outDate = convertDateFormate(outDate);
	String EmpTime = "18:00:00";

	// find employee not give signout
	List<HEmployee> notGivenSignOutEmp = wamsService.getEmpNotGivenSignout(outDate);
	Integer insert = -2;
	for (HEmployee employee : notGivenSignOutEmp) {
	    HAttendance att = new HAttendance();
	    att.setEmp_id(employee.getEmp_id());
	    att.setAtt_date(outDate);
	    att.setAtt_time(EmpTime);
	    att.setAtt_inout(1);
	    att.setModId(getSessionService().getUserSession().getEmp().getAvator());
	    att.setDeleted(false);

	    insert = wamsService.insertHAttendance(att);
	}
	mm.addAttribute("msg", insert);

	return new ModelAndView("result", mm);
    }

    private Integer getApprovarRequestSize(HEmployee emp) {
	// for approvel Sign
	List<HLeave> allLeave = new ArrayList<HLeave>();
	// allLeave = wamsService.getAllLeaveByApproverId(false,
	// emp.getEmp_id());
	allLeave = wamsService.getAllLeaveAppliedForApprovedByApproverId(false, emp.getEmp_id());
	int appSize = allLeave.size();
	// for approvel Sign

	return appSize;
    }

    private Integer getApprovarRequestSizeForOutgoing(HEmployee emp) {
	// for approvel Sign
	List<HOfficeOutgoing> allOut = new ArrayList<HOfficeOutgoing>();
	// allLeave = wamsService.getAllLeaveByApproverId(false,
	// emp.getEmp_id());
	allOut = wamsService.getAllOfficeOutgoingAppliedForApprovedByApproverId(false, emp.getEmp_id());
	int appSize = allOut.size();
	// for approvel Sign

	return appSize;
    }

    @RequestMapping(value = "/employee/lunch", method = RequestMethod.POST)
    public ModelAndView lunchAdd(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	}

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	String inout = requestUriSplit[0];
	String emp_id = requestUriSplit[1];

	Date now = new Date();
	int empId = Integer.parseInt(emp_id);
	String EmpDate = getFormatedDate(now);

	HLunch todayLunch = wamsService.getLunchByEmp(EmpDate, empId);
	int insert = -1;
	HEmployee emp = wamsService.getEmployeeById(empId);
	// Add Lunch
	if (null == todayLunch) {
	    HLunch lunch = new HLunch();
	    lunch.setEmp_id(empId);
	    lunch.setLunch_date(EmpDate);
	    lunch.setName(emp.getAvator());
	    if (inout.equals("0")) {
		lunch.setLunch_status(true);
		lunch.setLunch_count(1);
	    } else {
		lunch.setLunch_status(false);
		lunch.setLunch_count(0);
	    }
	    lunch.setDeleted(false);
	    lunch.setModId(getSessionService().getUserSession().getEmp().getAvator());
	    insert = wamsService.insertHLunch(lunch);
	} else {
	    todayLunch.setEmp_id(empId);
	    todayLunch.setLunch_date(EmpDate);
	    todayLunch.setName(emp.getAvator());
	    if (inout.equals("0")) {
		todayLunch.setLunch_status(true);
		todayLunch.setLunch_count(1);
	    } else {
		todayLunch.setLunch_status(false);
		todayLunch.setLunch_count(1);
	    }
	    todayLunch.setDeleted(false);
	    todayLunch.setModId(getSessionService().getUserSession().getEmp().getAvator());

	    insert = wamsService.editHLunch(todayLunch);
	}

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", insert);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/employee/guestlunch", method = RequestMethod.POST)
    public ModelAndView lunchGuestAdd(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	}

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	int numberOfGuest = Integer.parseInt(requestUriSplit[0]);
	String emp_id = requestUriSplit[1];

	Date now = new Date();
	int empId = Integer.parseInt(emp_id);
	String EmpDate = getFormatedDate(now);

	HLunch todayLunch = wamsService.getLunchByEmp(EmpDate, empId);
	int insert = -1;
	// Add Lunch
	if (null == todayLunch) {
	    HLunch lunch = new HLunch();
	    lunch.setEmp_id(empId);
	    lunch.setLunch_date(EmpDate);
	    lunch.setName("Guest");
	    if (numberOfGuest > 0) {
		lunch.setLunch_status(true);
		lunch.setLunch_count(numberOfGuest);
	    } else {
		lunch.setLunch_status(false);
		lunch.setLunch_count(0);
	    }
	    lunch.setDeleted(false);
	    lunch.setModId(getSessionService().getUserSession().getEmp().getAvator());
	    insert = wamsService.insertHLunch(lunch);
	} else {
	    todayLunch.setEmp_id(empId);
	    todayLunch.setLunch_date(EmpDate);
	    todayLunch.setName("Guest");
	    if (numberOfGuest > 0) {
		todayLunch.setLunch_status(true);
		todayLunch.setLunch_count(numberOfGuest);
	    } else {
		todayLunch.setLunch_status(false);
		todayLunch.setLunch_count(0);
	    }
	    todayLunch.setDeleted(false);
	    todayLunch.setModId(getSessionService().getUserSession().getEmp().getAvator());

	    insert = wamsService.editHLunch(todayLunch);
	}

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", insert);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/searchapprovedleave", method = RequestMethod.POST)
    public ModelAndView searchapprovedleave(HttpServletRequest request) throws Exception {
	// UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}
	ModelMap mm = new ModelMap();
	String sdd = requestUriSplit[0];
	String edd = requestUriSplit[1];

	String startDate = convertDateFormate(sdd);
	String endDate = convertDateFormate(edd);

	List<HEmployee> allEmp = wamsService.getAllEmployee(false);
	List<HLeave> leaves = wamsService.getAllLeaveBetweenDates(false, startDate, endDate, 2);
	mm.addAttribute("leaves", leaves);
	mm.addAttribute("allEmp", allEmp);

	return new ModelAndView("approvedleavesearch", mm);
    }

    @RequestMapping(value = "/employee/autolunchentry", method = RequestMethod.POST)
    public ModelAndView autolunchentry(HttpServletRequest request) throws Exception {
	boolean validSession = getSessionService().isSessionValid();

	if (!validSession) {
	    return new ModelAndView("redirect:/");
	}

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}

	String state = requestUriSplit[0];
	int emp_id = Integer.parseInt(requestUriSplit[1]);

	int insert = -1;

	HEmployeeparam param = new HEmployeeparam();
	param.setEmp_id(emp_id);
	if (state.equalsIgnoreCase("true"))
	    param.setAutolunchinput(true);
	else
	    param.setAutolunchinput(false);
	param.setDeleted(false);
	param.setModId(getSessionService().getUserSession().getEmp().getAvator());
	insert = wamsService.setAutoLunch(param);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", 1);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/saveteam", method = RequestMethod.POST)
    public ModelAndView saveteam(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HTeam teamInfo = new HTeam();

	// empInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	teamInfo.setTeamName(parameterMap.get("team_name")[0]);
	teamInfo.setDeleted(false);
	teamInfo.setModId(session.getEmp().getAvator());

	Integer teamid = wamsService.insertHTeam(teamInfo);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", teamid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/saveleavetype", method = RequestMethod.POST)
    public ModelAndView saveleavetype(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HTypesOfLeave leaveType = new HTypesOfLeave();

	// empInfo.setEmp_id(Integer.parseInt(parameterMap.get("emp_id")[0]));
	int deductfrom = Integer.parseInt(parameterMap.get("deductfrom")[0]);
	leaveType.setDeductfrom(deductfrom);
	leaveType.setLeavetype(parameterMap.get("leavetype")[0]);
	leaveType.setIsquota(false);
	leaveType.setIscontinue(false);
	String deduct = parameterMap.get("leavededuct")[0];
	float ded = 0;
	if (deduct != null) {
	    ded = Float.parseFloat(deduct);
	}
	leaveType.setLeavededuct(ded);
	String isQuota[] = parameterMap.get("isquota");
	String isContinue[] = parameterMap.get("iscontinue");

	if (null != isQuota) {
	    if ((isQuota[0].equals("1")) && (deductfrom == 0)) {
		leaveType.setIsquota(true);
	    }
	}
	if (null != isContinue) {
	    if ((isContinue[0].equals("1")) && (deductfrom == 0)) {
		leaveType.setIscontinue(true);
	    }
	}

	leaveType.setDeleted(false);
	leaveType.setModId(session.getEmp().getAvator());

	Integer leavetypeid = wamsService.insertHTypesOfLeave(leaveType);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", leavetypeid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/updateleavetype", method = RequestMethod.POST)
    public ModelAndView updateleavetype(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HTypesOfLeave leaveType = new HTypesOfLeave();

	leaveType.setId(Integer.parseInt(parameterMap.get("id")[0]));
	int deductfrom = Integer.parseInt(parameterMap.get("deductfrom")[0]);
	leaveType.setDeductfrom(deductfrom);
	leaveType.setLeavetype(parameterMap.get("leavetype")[0]);
	leaveType.setIsquota(false);
	leaveType.setIscontinue(false);

	String deduct = parameterMap.get("leavededuct")[0];
	float ded = 0;
	if (deduct != null) {
	    ded = Float.parseFloat(deduct);
	}
	leaveType.setLeavededuct(ded);
	String isQuota[] = parameterMap.get("isquota");
	String isContinue[] = parameterMap.get("iscontinue");

	if (null != isQuota) {
	    if ((isQuota[0].equals("1")) && (deductfrom == 0)) {
		leaveType.setIsquota(true);
	    }
	}
	if (null != isContinue) {
	    if ((isContinue[0].equals("1")) && (deductfrom == 0)) {
		leaveType.setIscontinue(true);
	    }
	}

	leaveType.setDeleted(false);
	leaveType.setModId(session.getEmp().getAvator());

	Integer empid = wamsService.editHTypesOfLeave(leaveType);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", empid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/saveempleavequota", method = RequestMethod.POST)
    public ModelAndView saveempleavequota(MultipartHttpServletRequest request, HttpServletResponse response) {
	UserSession session = getSessionService().getUserSession();

	Map<String, String[]> parameterMap = request.getParameterMap();

	HLeaveQuota leaveQuota = new HLeaveQuota();

	String emp_id = parameterMap.get("emp_id")[0];
	String year = parameterMap.get("year")[0];
	String type_id[] = parameterMap.get("type_id");

	String quota[] = parameterMap.get("quota");
	String used[] = parameterMap.get("used");
	String expdate[] = parameterMap.get("expdate");

	int empId = 0;
	if (!emp_id.equals(""))
	    empId = Integer.parseInt(emp_id);

	leaveQuota.setEmp_id(empId);
	leaveQuota.setYear(year);
	leaveQuota.setModId(session.getEmp().getAvator());
	leaveQuota.setDeleted(false);

	Integer leavetypeid = 0;

	for (int tid = 0; tid < type_id.length; tid++) {
	    int typeId = 0;
	    if (!type_id[tid].equals(""))
		typeId = Integer.parseInt(type_id[tid]);

	    float fquota = 0.0f;
	    if (!quota[tid].equals(""))
		fquota = Float.parseFloat(quota[tid]);

	    float fused = 0.0f;
	    if (!used[tid].equals(""))
		fused = Float.parseFloat(used[tid]);

	    String expd = year.substring(0, 2) + year.substring(5) + "-06-30";
	    if (!expdate[tid].equals(""))
		expd = convertDateFormate(expdate[tid]);

	    leaveQuota.setType_id(typeId);
	    leaveQuota.setQuota(fquota);
	    leaveQuota.setUsed(fused);
	    leaveQuota.setExpdate(expd);

	    leavetypeid = wamsService.insertHLeaveQuota(leaveQuota);
	}

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", leavetypeid);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/reportregenerator", method = RequestMethod.POST)
    public ModelAndView reportregenerator(HttpServletRequest request) throws Exception {
	UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}
	String m = requestUriSplit[0];
	String y = requestUriSplit[1];

	// Date dd = new Date();
	// SimpleDateFormat dfm = new SimpleDateFormat("MM");
	int mon = Integer.parseInt(m);

	// SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	int year = Integer.parseInt(y);

	List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(1, mon, year);

	ModelMap mm = new ModelMap();
	mm.addAttribute("msg", 1);

	return new ModelAndView("result", mm);
    }

    @RequestMapping(value = "/getQuota", method = RequestMethod.POST)
    public ModelAndView getQuota(HttpServletRequest request) throws Exception {
	UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 2) {
	    logger.warn("Expecting atleast 2 arguments but received " + requestUriSplit.length);
	    return null;
	}
	int emp_id = Integer.parseInt(requestUriSplit[0]);
	String year = requestUriSplit[1];

	List<HTypesOfLeave> allleavetype = wamsService.getLeaveType(false);

	List<HLeaveQuota> quotas = wamsService.getLeaveQuotas(emp_id, year);

	ModelMap mm = new ModelMap();

	mm.addAttribute("allleavetype", allleavetype);
	mm.addAttribute("quotas", quotas);

	return new ModelAndView("quota", mm);
    }

    @RequestMapping(value = "/yearlyleavesearch", method = RequestMethod.POST)
    public ModelAndView yearlyleavesearch(HttpServletRequest request) throws Exception {
	UserSession session = getSessionService().getUserSession();

	String requestUri = getStringFromHttpRequest(request);

	logger.info("requestUri=" + requestUri);

	String[] requestUriSplit = requestUri.split("~");

	if (requestUriSplit.length < 1) {
	    logger.warn("Expecting atleast 1 arguments but received " + requestUriSplit.length);
	    return null;
	}
	int year = Integer.parseInt(requestUriSplit[0]);

	ModelMap mm = new ModelMap();

	List<HYearlyLeave> empYearlyLeaveState = new ArrayList<HYearlyLeave>();
	empYearlyLeaveState = wamsService.getEmployeeYearlyLeave(year);

	mm.addAttribute("yleave", empYearlyLeaveState);
	mm.addAttribute("year", year);

	return new ModelAndView("yearlyleavestatussearch", mm);
    }
    
    @RequestMapping(value = "/addbatchleave", method = RequestMethod.POST)
    public ModelAndView addbatchleave(HttpServletRequest request, HttpServletResponse response)
	    throws MessagingException {
	boolean validSession = getSessionService().isSessionValid();
	ModelMap mm = new ModelMap();
	
	
	if (!validSession) {
	    mm.addAttribute("msg", "Ok");
	    return new ModelAndView("result", mm);
	}
	
	HEmployee emp = getSessionService().getUserSession().getEmp();
	
	Map<String, String[]> parameterMap = request.getParameterMap();
	
	String[] emp_id = parameterMap.get("chk");
	for(int i=0;i<emp_id.length;i++) {
	
	    HLeave leaveInfo = new HLeave();

	    leaveInfo.setEmp_id(Integer.parseInt(emp_id[i]));
	    HTypesOfLeave obj = new HTypesOfLeave();
	    Integer leaveId = Integer.parseInt(parameterMap.get(emp_id[i])[0]);
	    obj = wamsService.getLeaveTypeById(leaveId);
	    leaveInfo.setLeavetype(obj);
	    leaveInfo.setFdate(convertDateFormate(parameterMap.get("fdate")[0]));
	    leaveInfo.setTdate(convertDateFormate(parameterMap.get("tdate")[0]));
	    leaveInfo.setDays(Float.parseFloat(parameterMap.get("days")[0]));
	    leaveInfo.setReason(parameterMap.get("reason")[0]);
	    HLeaveQuota objquota = new HLeaveQuota();
	    String quotayear = parameterMap.get("year")[0];
	    objquota = wamsService.getLeaveQuota(leaveInfo.getEmp_id(), quotayear, leaveInfo.getLeavetype().getId());
	    leaveInfo.setLeavequota(objquota);

	    int noa = Integer.parseInt(parameterMap.get("appsize")[0]);

	    String app1 = "", app2 = "", app3 = "", app4 = "", app5 = "";
	    
	    leaveInfo.setStatus(0);
	    if (noa > 0) {
		app1 = parameterMap.get("approvar1")[0];
        	if (!app1.equals("")){
        	    leaveInfo.setApprovar1(Integer.parseInt(app1));
        	}
	    }
	    if (noa > 1) {
		app2 = parameterMap.get("approvar2")[0];
		if (!app2.equals("")){
		    leaveInfo.setApprovar2(Integer.parseInt(app2));
		}
	    }
            if (noa > 2) {
        	app3 = parameterMap.get("approvar3")[0];
        	if (!app3.equals("")){
        	    leaveInfo.setApprovar3(Integer.parseInt(app3));
        	}
            }
            if (noa > 3) {
        	app4 = parameterMap.get("approvar4")[0];
        	if (!app4.equals("")){
        	    leaveInfo.setApprovar4(Integer.parseInt(app4));
        	}
            }
            if (noa > 4) {
        	app5 = parameterMap.get("approvar5")[0];
        	if (!app5.equals("")){
        	    leaveInfo.setApprovar5(Integer.parseInt(app5));
        	}
            }
            
            leaveInfo.setApp1state(false);
	    leaveInfo.setApp2state(false);
	    leaveInfo.setApp3state(false);
	    leaveInfo.setApp4state(false);
	    leaveInfo.setApp5state(false);
            leaveInfo.setDeleted(false);
            leaveInfo.setModId(emp.getAvator());
            
            Date now = new Date();
            leaveInfo.setApllieddate(DateTimeFormateforDB().format(now));
            leaveInfo.setModifydate(DateTimeFormateforDB().format(now));

            Integer emid = wamsService.insertHLeaveInfo(leaveInfo);
            
            HLeave leave = wamsService.getLeaveByEmpIdAndDate(leaveInfo.getEmp_id(), leaveInfo.getFdate(), leaveInfo.getStatus());
	// Update Leave Quota
	// HLeaveQuota quota = wamsService.getLeaveQuotas(empId, year);
	// Update Leave Quota
            
            HLeaveStatus status = new HLeaveStatus();
            status.setLeave_id(leave.getId());
            status.setApprover_id(leave.getEmp_id());
            status.setComments("Approved By Managment.");
            status.setApprovalstate(1);
            status.setDate(DateFormateforDB().format(new Date()));
            status.setApprovarSeq(1);
            status.setDeleted(false);
            status.setModId(emp.getAvator());
        
            wamsService.approveRejectLeave(status, 1);
	}
	

	
	mm.addAttribute("msg", "Ok");

	return new ModelAndView("result", mm);
    }
    
    /*
     * @RequestMapping(value = "/test", method = RequestMethod.POST) public
     * ModelAndView test(HttpServletRequest request) throws Exception {
     * UserSession session = getSessionService().getUserSession();
     * 
     * String requestUri = getStringFromHttpRequest(request);
     * 
     * logger.info("requestUri="+requestUri);
     * 
     * String[] requestUriSplit = requestUri.split("~");
     * 
     * if (requestUriSplit.length < 2) { logger.warn(
     * "Expecting atleast 2 arguments but received " + requestUriSplit.length);
     * return null; } String m = requestUriSplit[0]; String y =
     * requestUriSplit[1];
     * 
     * //Date dd = new Date(); //SimpleDateFormat dfm = new
     * SimpleDateFormat("MM"); int mon = Integer.parseInt(m);
     * 
     * //SimpleDateFormat dfy = new SimpleDateFormat("yyyy"); int year =
     * Integer.parseInt(y);
     * 
     * boolean Fo = wamsService.test(1, mon, year);
     * 
     * ModelMap mm = new ModelMap(); mm.addAttribute("msg", 1);
     * 
     * return new ModelAndView("result", mm); }
     */
}

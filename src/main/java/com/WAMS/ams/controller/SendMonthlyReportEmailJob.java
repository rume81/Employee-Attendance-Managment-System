/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* SendMonthlyReportEmailJob.java
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HLeaveStatus;
import com.webhawks.Hawks_model.HMail;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.WAMS.ams.services.interfaces.IWamsService;

/**
 * @author OS-10 Monsur
 *
 */
public class SendMonthlyReportEmailJob extends QuartzJobBean {

    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    private int reportstartdate;

    private ApplicationContext getApplicationContext(JobExecutionContext context ) throws Exception {
	ApplicationContext appCtx = null;
	appCtx = (ApplicationContext)context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
	if (appCtx == null) {
	    throw new JobExecutionException(
		    "No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");
	}
	return appCtx;
    }
    public void setReportstartdate(int reportstartdate) {
        this.reportstartdate = reportstartdate;
    }
    protected SimpleDateFormat DateFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	return df;
    }
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	Date dd = new Date();
	String outDate = DateFormateforDB().format(dd);
	System.out.println("=======================================Run Send Mail Report:"+outDate+"=================================");
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	IWamsService wamsService = (IWamsService) appCtx.getBean("wamsService");
	JavaMailSenderImpl mailSender = (JavaMailSenderImpl) appCtx.getBean("mailSenderWeb");
	
	SimpleDateFormat dfm = new SimpleDateFormat("MM");
	int mon = Integer.parseInt(dfm.format(dd));
		
	SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	int year = Integer.parseInt(dfy.format(dd));
	
	
	if(mon==1){
	    mon=12;
	    year=year-1;
	}else{
	    mon=mon-1;
	}
				
	List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(reportstartdate, mon, year);
	List<HEmployee> emps = wamsService.getAllEmployee(false);
	
	for(HMonthlyStatus mos:monthlyStates)
	{
	    for(HEmployee emp:emps)
	    {
		if(mos.getEmp_id()== emp.getEmp_id() && mos.getTotal_working_day()!=0)
		{
		    //prepare Mail
		    String EMailto[] = new String[1];
		    EMailto[0] = emp.getEmail();
		    String subject = "Monthly Attendance & Leave Status of "+emp.getEmployee_name()+" ("+emp.getAvator()+")";
		    Map<String, String> email = new HashMap<String, String>();

		    email.put("<#EMP_NAME#>", emp.getAvator());
		    email.put("<#MON#>", String.valueOf(mon));
		    email.put("<#YEAR#>", String.valueOf(year));
		    
		    StringBuffer report = new StringBuffer("<table  border='1'>");
		    report.append("<tr><td>Total Working Day</td><td>Present</td><td>Leave</td><td>Absent</td><td>Late</td><td>Present %</td><td>Late %</td></tr><tr><td>");
		    report.append(mos.getTotal_working_day()+"</td><td>");
		    report.append(mos.getPresent()+"</td><td>");
		    report.append(mos.getLeave()+"</td><td>");
		    report.append(mos.getAbsent()+"</td><td>");
		    report.append(mos.getLate()+"</td><td>");
		    int presentPercent = (mos.getPresent()*100)/mos.getTotal_working_day();
		    report.append(presentPercent+"%</td><td>");
		    int latePercent = (mos.getLate()*100)/mos.getPresent();
		    report.append(latePercent+"%</td></tr>");
		    report.append("</table>");
		    email.put("<#REPORT#>", report.toString());

		    String body = getEmialTemplateFromResource("statusreport", email);
		    //prepare for save
		    Date date = new Date();
		    HMail mail = new HMail();
		    mail.setTo(EMailto[0]);
		    mail.setCc("");
		    mail.setBcc("");
		    mail.setSubject(subject);
		    mail.setFrom("info.webhawksit@gmail.com");
		    mail.setDate(DateTimeFormateforDB().format(date));
		    mail.setMsg(escapeHtml4(body));
		    mail.setStatus(0);
		    mail.setSendTime(DateTimeFormateforDB().format(date));
		    mail.setDeleted(false);
		    mail.setModId("sys");
		    int stat = wamsService.sendMail(mail);
		    //prepare for save
		    /*try {
			sendEmail(EMailto, "", subject, body, mailSender);
		    } catch (MessagingException e) {
			e.printStackTrace();
		    }*/
		    break;
		}
	    }
	}
	
	System.out.println("=======================================End Send Mail Report:"+outDate+"=================================");
    }
    
    protected SimpleDateFormat DateTimeFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return df;
    }
    public String getEmialTemplateFromResource(String templateName,Map<String,String> replaceList)
    {
	String emailContent ="";
	
	InputStream is = null;
	is = getClass().getResourceAsStream("/" + templateName+".txt");
	int i = 0;
	try {
	    while ((i = is.read()) != -1) {
	    	emailContent = emailContent + (char) i;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	for (Map.Entry<String, String> entry : replaceList.entrySet())
	{
	    emailContent = emailContent.replace(entry.getKey(), entry.getValue());
	}
		
	return emailContent;
    }
    
    /**
     * Method for send email
     * 
     * @throws MessagingException
     */
    /*public void sendEmail(String EMailto[], String EMailCC, String subject, String msgBody, JavaMailSenderImpl mailSender)
	    throws MessagingException {
	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(EMailto);
	if(!EMailCC.equals(""))
	    helper.setCc(EMailCC);
	helper.setSubject(subject);
	helper.setFrom("info.webhawksit@gmail.com");

	message.setContent(msgBody, "text/html");
	try {
	    mailSender.send(message);
	} catch (MailException ex) {
	    System.out.println(ex.getMessage());
	}

    }*/
}

/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* AbsentCheckJob.java
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

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.webhawks.Hawks_model.HAttendance;
import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HMAttendance;
import com.webhawks.Hawks_model.HMail;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.webhawks.Hawks_model.HWorkInfo;
import com.WAMS.ams.services.interfaces.IWamsService;

/**
 * @author OS-10 Monsur
 *
 */
public class AbsentCheckJob extends QuartzJobBean {
    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    
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
	Date dd = new Date();
	String outDate = DateFormateforDB().format(dd);
	System.out.println("=======================================Run Send Absent Mail :"+outDate+"=================================");
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	IWamsService wamsService = (IWamsService) appCtx.getBean("wamsService");
	
	SimpleDateFormat dfm = new SimpleDateFormat("MM");
	int mon = Integer.parseInt(dfm.format(dd));
		
	SimpleDateFormat dfy = new SimpleDateFormat("yyyy");
	int year = Integer.parseInt(dfy.format(dd));
	
	//List<HMonthlyStatus> monthlyStates = wamsService.getEmployeeMonthlyStatus(reportstartdate, mon, year);
	List<HEmployee> emps = wamsService.getAllEmployee(false);
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    for(HEmployee emp:emps)
	    {
		List<HMAttendance> thisMonAtt = new ArrayList<HMAttendance>();
		thisMonAtt = wamsService.getAttendanceForMonth(mon,year, emp.getEmp_id(),false);
		HWorkInfo joinInfo = wamsService.getJoiningWorkInfoById(emp.getEmp_id());
		if(!thisMonAtt.isEmpty()){
		    List<String> absentDate = new ArrayList<String>();
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
		    if(absentDate.size()>0)
		    {
			//prepare Mail
			String EMailto[] = new String[1];
			EMailto[0] = emp.getEmail();
			String subject = "Absent List of "+emp.getEmployee_name()+" ("+emp.getAvator()+")";
			Map<String, String> email = new HashMap<String, String>();

			email.put("<#EMP_NAME#>", emp.getAvator());
			    
			StringBuffer report = new StringBuffer("<table  border='1'>");
			report.append("<tr><td>Month:</td><td>");
			report.append(mon);
			report.append("</td><td>Year:</td><td>");
			report.append(year);
			report.append("</td></tr>");
			for(String dt:absentDate)
			{
			    report.append("<tr><td colspan='4'>");
			    report.append(dt);
			    report.append("</td></tr>");
			    
			}
			report.append("</table>");
			email.put("<#REPORT#>", report.toString());

			String body = getEmialTemplateFromResource("absentmail", email);
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
		    }
		}
		
	    }
	
	System.out.println("=======================================End Send Absent Mail :"+outDate+"=================================");
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
       
}

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
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeEcmaScript;

import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HLeaveStatus;
import com.webhawks.Hawks_model.HMail;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.WAMS.ams.services.interfaces.IWamsService;

/**
 * @author OS-10 Monsur
 *
 */
public class SendEmailJob extends QuartzJobBean {

    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    private int maximumSendMail;

    private ApplicationContext getApplicationContext(JobExecutionContext context ) throws Exception {
	ApplicationContext appCtx = null;
	appCtx = (ApplicationContext)context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
	if (appCtx == null) {
	    throw new JobExecutionException(
		    "No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");
	}
	return appCtx;
    }
    
    public void setMaximumSendMail(int maximumSendMail) {
        this.maximumSendMail = maximumSendMail;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	Date dd = new Date();
	String outDate = DateTimeFormateforDB().format(dd);
	System.out.println("=======================================Run Send Mail:"+outDate+"=================================");
	ApplicationContext appCtx = null;
	try {
	    appCtx = getApplicationContext(context);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	IWamsService wamsService = (IWamsService) appCtx.getBean("wamsService");
	JavaMailSenderImpl mailSender = (JavaMailSenderImpl) appCtx.getBean("mailSenderWeb");
	
			
	List<HMail> mails = wamsService.getMail(false, "0,1");
	int count = 0;
	for(HMail mail:mails)
	{
	    boolean Done = false;
	    if(count<=maximumSendMail)
	    {
		try {
		    if((!mail.getTo().split(",").equals(""))&&(!mail.getTo().split(",").equals("null"))){
        		    String[] EMailto = mail.getTo().split(",");
        		    String msgbodyfromDb = mail.getMsg();
        		    String replaceNewline =  unescapeEcmaScript(msgbodyfromDb);
        		    String body = unescapeHtml4(replaceNewline);
        		    Done = sendEmail(EMailto,mail.getCc(),mail.getBcc(),mail.getSubject(), body,mail.getFrom(), mailSender);
		    }
		    
		} catch (MessagingException e) {
		    Done = false;
		    e.printStackTrace();
		}
		count++;
		
		Date sendDt = new Date();
		mail.setSendTime(DateTimeFormateforDB().format(sendDt));
		if(Done)
		    mail.setStatus(2);//send 
		else
		    mail.setStatus(1);//not send
		wamsService.editHMail(mail);
		
	    }    
	}
	System.out.println("=======================================End Send Mail:"+outDate+"=================================");
    }
    
    protected SimpleDateFormat DateTimeFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return df;
    }
        
    /**
     * Method for send email
     * 
     * @throws MessagingException
     */
    public boolean sendEmail(String EMailto[], String EMailCC, String EMailBCC, String subject, String msgBody, String from, JavaMailSenderImpl mailSender)
	    throws MessagingException {
	boolean Send = false;
	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(EMailto);
	if(!EMailCC.equals(""))
	    helper.setCc(EMailCC);
	if(!EMailBCC.equals(""))
	    helper.setBcc(EMailBCC);
	helper.setSubject(subject);
	helper.setFrom(from);

	message.setContent(msgBody, "text/html");
	try {
	    mailSender.send(message);
	    Send = true;
	} catch (MailException ex) {
	    Send = false;
	    System.out.println(ex.getMessage());
	    
	}
	return Send;
    }
}

/* ========================================
* WAMS v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* BaseController.java
* --------------------
* Created on Apr 12, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* Apr 12, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.WAMS.ams.services.interfaces.ISessionService;


/**
 * @author OS-10 Monsur
 *
 */
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    private ISessionService sessionService;

    public ISessionService getSessionService() {
	return sessionService;
    }

    public void setSessionService(ISessionService sessionService) {
	this.sessionService = sessionService;
    }

    public String getStringFromHttpRequest(HttpServletRequest request) throws Exception {

	InputStream is = request.getInputStream();
	String str = "";

	if (is != null) {
	    Writer writer = new StringWriter();

	    char[] buffer = new char[1024];
	    try {
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
		    writer.write(buffer, 0, n);
		}
	    } finally {
		is.close();
	    }
	    str = writer.toString();
	} else {
	    str = "";
	}
	return str;
    }

    public String convertDateFormate(String dt) {//25-01-2016  25  01  2016   output 2016-01-25
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[2] + "-" + splitdt[1] + "-" + splitdt[0];

	return formatedDate;

    }
    
    public Date convertStringToDateFormate(String dt) {//25-01-2016  25  01  2016   output 2016-01-25
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[2] + "-" + splitdt[1] + "-" + splitdt[0];
	
	Date condate = new Date();
	try {
		condate = DateFormateforDB().parse(formatedDate);
	} catch (ParseException e) {
		e.printStackTrace();
	}

	return condate;

    }
    
    public Date convertStringToDateFormateForView(Date dt) {//25-01-2016  25  01  2016   output 2016-01-25
    	String formatedDate = "";
    	
    	formatedDate = DateFormateforView().format(dt);
    	
    	Date condate = new Date();
    	try {
    		condate = DateFormateforView().parse(formatedDate);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}

    	return condate;

    }
    
    public Integer getDay(String dt) {//25-01-2016  01   output 01
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[0];
	
	int day = Integer.parseInt(formatedDate);

	return day;

    }
    
    public Integer getMonth(String dt) {//25-01-2016  01   output 01
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[1];
	
	int mon = Integer.parseInt(formatedDate);

	return mon;

    }
    
    public Integer getYear(String dt) {//25-01-2016  2016   output 2016
	String formatedDate = "";

	String[] splitdt = dt.split("-");
	formatedDate = splitdt[2];
	
	int year = Integer.parseInt(formatedDate);

	return year;

    }
    
    public String changetDateFormate(String dt) {//4/15/2016  4  15  2016   output 2016-04-15
	String formatedDate = "";

	String[] splitdt = dt.split("/");
	formatedDate = splitdt[2] + "-" + splitdt[0] + "-" + splitdt[1];

	return formatedDate;

    }
    
    
    public String changetTimeFormate(String dt) {//9:41:32 PM  9  41  32   output 9:41:32
	String formatedTime = "";

	String[] splitt1 = dt.trim().split(" ");
	String[] splitt2 = splitt1[0].split(":");
	
	String houre = splitt2[0];
	String minite = splitt2[1];
	String secend = splitt2[2];
	
	if(houre.equalsIgnoreCase("PM"))
	{
	   int inhoure = Integer.parseInt(houre);
	   inhoure = inhoure + 12;
	   houre = String.valueOf(inhoure);
	}
	
	formatedTime = houre + ":" + minite + ":" + secend;

	return formatedTime;

    }
    
    protected SimpleDateFormat DateFormateforView() {
	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	return df;
    }
    
    protected SimpleDateFormat DateFormateforViewWithTime() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
	return df;
    }

    protected SimpleDateFormat DateFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	return df;
    }
    
    protected String getFormatedDate(Date dd) {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String dt = df.format(dd);
	return dt;
    }
    
    protected SimpleDateFormat DateTimeFormateforView() {
	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	return df;
    }

    protected SimpleDateFormat DateTimeFormateforDB() {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return df;
    }
    
    protected String getFormatedTime(Date dd,String inout,String tt) {
	String dt = "";
	if(inout.equals("0"))
	{
	    final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	    Calendar date = Calendar.getInstance();
	    date.setTime(dd);
	    long t= date.getTimeInMillis();
	    int timeAdjust = Integer.parseInt(tt);
	    Date afterMinusMins=new Date(t - (timeAdjust * ONE_MINUTE_IN_MILLIS));
	
	    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	    dt = df.format(afterMinusMins);
	} else{
	    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	    dt = df.format(dd);
	}
	return dt;
    }
    
    protected String getFormatedTime(Date dd) {
	String dt = "";
	
	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	dt = df.format(dd);
	
	return dt;
    }
    
    protected String getFormatedNumber(String n){
	String num ="00";
	if(n.equals("1"))
	    num="01";
	else if(n.equals("2"))
	    num="02";
	else if(n.equals("3"))
	    num="03";
	else if(n.equals("4"))
	    num="04";
	else if(n.equals("5"))
	    num="05";
	else if(n.equals("6"))
	    num="06";
	else if(n.equals("7"))
	    num="07";
	else if(n.equals("8"))
	    num="08";
	else if(n.equals("9"))
	    num="09";
	else if(n.equals("0"))
	    num="00";
	else
	    num = n;
	return num;
    }
    
    protected boolean findFutureDate(String date)
    {
	boolean fo = false;
	
	String[] splitdt = date.split("-");
	int yy=0,mm=0,dd=0;
	if(!splitdt[0].equals(""))
	    dd = Integer.parseInt(splitdt[0]);
	if(!splitdt[1].equals(""))
	    mm = Integer.parseInt(splitdt[1]);
	mm = mm-1;
	if(!splitdt[2].equals(""))
	    yy = Integer.parseInt(splitdt[2]);
	// create calendar objects.
	Calendar cal = Calendar.getInstance();
	Calendar future = Calendar.getInstance();

	// print the current date
	//System.out.println("Current date: " + cal.getTime());
	
	// change date in future calendar
	future.set(yy,mm,dd);
	//System.out.println("Year is " + future.get(Calendar.YEAR));

	// check if calendar date is after current date
	Date time = future.getTime();
	if (future.after(cal)) {
	    //System.out.println("Date " + time + " is after current date.");
	    fo = true;
	}
	
	return fo;
    }
    
    
    public String convertTimeFormat(String giventime) {
    	String form="";
    	if(giventime.equals("09:00 AM"))
    		form = "09:00:00";
    	else if(giventime.equals("09:30 AM"))
    		form = "09:30:00";
    	else if(giventime.equals("10:00 AM"))
    		form = "10:00:00";
    	else if(giventime.equals("10:30 AM"))
    		form = "10:30:00";
    	else if(giventime.equals("11:00 AM"))
    		form = "11:00:00";
    	else if(giventime.equals("11:30 AM"))
    		form = "11:30:00";
    	else if(giventime.equals("12:00 PM"))
    		form = "12:00:00";
    	else if(giventime.equals("12:30 PM"))
    		form = "12:30:00";
    	else if(giventime.equals("01:00 PM"))
    		form = "13:00:00";
    	else if(giventime.equals("01:30 PM"))
    		form = "13:30:00";
    	else if(giventime.equals("02:00 PM"))
    		form = "14:00:00";
    	else if(giventime.equals("02:30 PM"))
    		form = "14:30:00";
    	else if(giventime.equals("03:00 PM"))
    		form = "15:00:00";
    	else if(giventime.equals("03:30 PM"))
    		form = "15:30:00";
    	else if(giventime.equals("04:00 PM"))
    		form = "16:00:00";
    	else if(giventime.equals("04:30 PM"))
    		form = "16:30:00";
    	else if(giventime.equals("05:00 PM"))
    		form = "17:00:00";
    	else if(giventime.equals("05:30 PM"))
    		form = "17:30:00";
    	else if(giventime.equals("06:00 PM"))
    		form = "18:00:00";
    	
    	return form;
    }
    
    public String revartTimeFormat(String giventime) {
    	String form="";
    	if(giventime.equals("09:00:00"))
    		form = "09:00 AM";
    	else if(giventime.equals("09:30:00"))
    		form = "09:30 AM";
    	else if(giventime.equals("10:00:00"))
    		form = "10:00 AM";
    	else if(giventime.equals("10:30:00"))
    		form = "10:30 AM";
    	else if(giventime.equals("11:00:00"))
    		form = "11:00 AM";
    	else if(giventime.equals("11:30:00"))
    		form = "11:30 AM";
    	else if(giventime.equals("12:00:00"))
    		form = "12:00 PM";
    	else if(giventime.equals("12:30:00"))
    		form = "12:30 PM";
    	else if(giventime.equals("13:00:00"))
    		form = "01:00 PM";
    	else if(giventime.equals("13:30:00"))
    		form = "01:30 PM";
    	else if(giventime.equals("14:00:00"))
    		form = "02:00 PM";
    	else if(giventime.equals("14:30:00"))
    		form = "02:30 PM";
    	else if(giventime.equals("15:00:00"))
    		form = "03:00 PM";
    	else if(giventime.equals("15:30:00"))
    		form = "03:30 PM";
    	else if(giventime.equals("16:00:00"))
    		form = "04:00 PM";
    	else if(giventime.equals("16:30:00"))
    		form = "04:30 PM";
    	else if(giventime.equals("17:00:00"))
    		form = "05:00 PM";
    	else if(giventime.equals("17:30:00"))
    		form = "05:30 PM";
    	else if(giventime.equals("18:00:00"))
    		form = "06:00 PM";
    	
    	return form;
    }
    /**
     * Method for send email
     * 
     * @throws MessagingException
     */
    public void sendEmail(String EMailto[], String subject, String msgBody, JavaMailSenderImpl mailSender)
	    throws MessagingException {
	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(EMailto);
	// helper.setBcc("");
	helper.setSubject(subject);
	helper.setFrom("info.webhawksit@gmail.com");

	message.setContent(msgBody, "text/html");
	try {
	    mailSender.send(message);
	} catch (MailException ex) {
	    logger.error(ex.getMessage());
	}

    }
    
    /**
     * Method for send email
     * 
     * @throws MessagingException
     */
    public void sendEmail(String EMailto[], String EMailCC, String subject, String msgBody, JavaMailSenderImpl mailSender)
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
	    logger.error(ex.getMessage());
	}

    }

    /**
     * Method for send email with attachment
     * 
     * @throws MessagingException
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    public void sendEmailWithAttachment(String EMailto[], String subject, String from, String msgBody,
	    JavaMailSenderImpl mailSender, String Bcc, ByteArrayOutputStream attFile, boolean sendImage)
		    throws MessagingException, FileNotFoundException, URISyntaxException {

	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(EMailto);
	helper.setBcc(Bcc);
	helper.setSubject(subject);
	helper.setFrom(from);
	if (null != attFile) {
	    helper.addAttachment("order.tsv", new ByteArrayResource(attFile.toByteArray()));
	}
	helper.setText(msgBody, true);

	if (sendImage) {
	    FileSystemResource res = new FileSystemResource(
		    new File(getClass().getResource("/icon-reorder-small.png").toURI()));
	    helper.addInline("image12", res);
	}

	try {
	    mailSender.send(message);
	} catch (MailException ex) {
	    logger.error(ex.getMessage());
	}

    }

    /**
     * Method for send email with attachment
     * 
     * @throws MessagingException
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    public void sendEmailWithImage(String EMailto[], String subject, String from, String Bcc, String msgBody,
	    JavaMailSenderImpl mailSender, boolean sendImage) throws MessagingException, URISyntaxException {

	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(EMailto);
	helper.setBcc(Bcc);
	helper.setSubject(subject);
	helper.setFrom(from);
	helper.setText(msgBody, true);

	if (sendImage) {
	    FileSystemResource res = new FileSystemResource(
		    new File(getClass().getResource("/icon-reorder-small.png").toURI()));
	    helper.addInline("image12", res);
	}

	try {
	    mailSender.send(message);
	} catch (MailException ex) {
	    logger.error(ex.getMessage());
	}

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
    
    public String changeTimeFormate(String input)
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
    
    private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {
	 
	InputStream inputStream = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try {

		inputStream = new FileInputStream(fileName);
		byte[] buffer = new byte[1024];
		baos = new ByteArrayOutputStream();

		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	return baos;
    }

    public boolean DateCompare(String Fd,String Td,String Sy){
	boolean fo = false;
	SimpleDateFormat df = DateFormateforView();
	String splitY[] = Sy.split("/");
	if(splitY.length==2){
	    String minSt = "01-07-"+splitY[0];
	    String maxSt = "30-06-"+splitY[0].substring(0,2)+splitY[1];
	    Date mindt=new Date();
	    Date maxdt=new Date();
	    Date fromdt=new Date();
	    Date todt=new Date();
	    try {
		mindt = df.parse(minSt);
		maxdt = df.parse(maxSt);
		fromdt= df.parse(Fd);
		todt = df.parse(Td);
		
		if((mindt.getTime() <= fromdt.getTime() && fromdt.getTime() <= maxdt.getTime()) && 
			(mindt.getTime() <= todt.getTime() && todt.getTime() <= maxdt.getTime()) && (fromdt.getTime()<=todt.getTime())){
		    fo = true;
		}
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	  
	    
	    
	}
	return fo;
    }
}

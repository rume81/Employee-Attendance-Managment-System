/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* CreatePDF.java
* --------------------
* Created on Jun 16, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* Jun 16, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.utils;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
/**
 * @author OS-10 Monsur
 *
 */
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.webhawks.Hawks_model.HEmployee;
import com.webhawks.Hawks_model.HMAttendance;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfMonthlyLeaveReportView extends AbstractPdfView {

    private Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    private Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    private Font boldFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
    private Font normalFont = new Font(Font.TIMES_ROMAN, 7, Font.NORMAL);

    @Override
    protected void buildPdfDocument(Map model, Document document, PdfWriter writer, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Map <String, List<HMAttendance>> empMonthlyState =(Map<String,List<HMAttendance>>) model.get("estate");
	List<HEmployee> allEmp = (List<HEmployee>) model.get("allEmp");
	String time = (String) model.get("period");
	int lastday =  (Integer) model.get("lastdateofmon");
	
	String fname = "Leave"+".pdf";
	
	response.setHeader("Content-Disposition", "attachment; filename=\""+fname+"\"");
	
	//document.setPageSize(new Rectangle(17,11));
	addMetaData(document);
	
	Paragraph paragraph = new Paragraph();
	creteEmptyLine(paragraph, 1);
	document.add(paragraph);
	
	
	
	addTitlePage(document,time);
	
	PdfPTable table = new PdfPTable(lastday+5);
	
	PdfPCell c = new PdfPCell(new Phrase("Employee Name",normalFont));
	c.setHorizontalAlignment(Element.ALIGN_LEFT);
	c.setColspan(5);
	table.setWidthPercentage(100);
	table.addCell(c);
	
	for(int i=1;i<=lastday;i++){
	    PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(i),normalFont));
	    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(c1);
	}
	
	for(HEmployee emp:allEmp){
        	Iterator itt = empMonthlyState.entrySet().iterator();
        	while (itt.hasNext()) {
        	    Map.Entry pair = (Map.Entry)itt.next();
        	    String key = (String) pair.getKey();
        	    if(key.equals(String.valueOf(emp.getEmp_id()))){
        		
                	    List<HMAttendance> attList = (List<HMAttendance>) pair.getValue();
                	                   	    
                	    PdfPCell c1 = new PdfPCell(new Phrase(emp.getEmployee_name(),normalFont));
                	    c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                	    c1.setColspan(5);
                	    table.addCell(c1);
                		
                	    for (HMAttendance att : attList) {
                	    	PdfPCell c2 = null;
                	    	if(att.isWeekend()){
                			c2 = new PdfPCell(new Phrase("",normalFont));
                			c2.setBackgroundColor(new Color(204,204,204));
                			c2.setBorderColor(Color.BLACK);
                	    	} else if(att.isHoliday()){
                			c2 = new PdfPCell(new Phrase("",normalFont));
                			c2.setBackgroundColor(new Color(204,255,255));
                			c2.setBorderColor(Color.BLACK);
                	    	} else if(att.isAbsent()){
                	    	    	c2 = new PdfPCell(new Phrase("",normalFont));
                	    	    	c2.setBackgroundColor(new Color(255,102,102));
                	    	    	c2.setBorderColor(Color.BLACK);
                	    	} else if(att.isLeave()){
                	    	    if(att.getStatus().equals("Leave")){
                			c2 = new PdfPCell(new Phrase("L",normalFont));
                			c2.setBackgroundColor(new Color(0,153,102));
                			c2.setBorderColor(Color.BLACK);
                	    	    } else{
                	    		c2 = new PdfPCell(new Phrase("A",normalFont));
                			c2.setBackgroundColor(new Color(0,204,255));
                			c2.setBorderColor(Color.BLACK);
                	    	    }
                	    	} else{
                			c2 = new PdfPCell(new Phrase("",normalFont));
                	    	}
                	    	c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                	    	table.addCell(c2);
                	    }
                	    break;
        	    }
        	}
	}
	document.add(table);
    }

    private void addMetaData(Document document) {
	document.addTitle("Generate PDF report");
	document.addSubject("Generate PDF report");
	// document.addAuthor("Java Honk");
	// document.addCreator("Java Honk");
    }

    private void addTitlePage(Document document,String time) throws DocumentException {

	Paragraph preface = new Paragraph();
	preface.add(new Paragraph("Monthly Leave Status", TIME_ROMAN));
	preface.setAlignment(1);

	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Period: "+time, TIME_ROMAN_SMALL));
		
	creteEmptyLine(preface, 1);
	
	PdfPTable tableTitle = new PdfPTable(5);
	tableTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
	tableTitle.setWidthPercentage(30);
	
	PdfPCell c = new PdfPCell(new Phrase("Weekday",normalFont));
	c.setHorizontalAlignment(Element.ALIGN_CENTER);
	c.setBackgroundColor(new Color(204,204,204));
	c.setBorderColor(Color.BLACK);
	tableTitle.addCell(c);
	
	PdfPCell c1 = new PdfPCell(new Phrase("Holiday",normalFont));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setBackgroundColor(new Color(204,255,255));
	c1.setBorderColor(Color.BLACK);
	tableTitle.addCell(c1);
	
	PdfPCell c2 = new PdfPCell(new Phrase("Applied",normalFont));
	c2.setHorizontalAlignment(Element.ALIGN_CENTER);
	c2.setBackgroundColor(new Color(0,204,255));
	c2.setBorderColor(Color.BLACK);
	tableTitle.addCell(c2);
	
	PdfPCell c3 = new PdfPCell(new Phrase("Leave",normalFont));
	c3.setHorizontalAlignment(Element.ALIGN_CENTER);
	c3.setBackgroundColor(new Color(0,153,102));
	c3.setBorderColor(Color.BLACK);
	tableTitle.addCell(c3);
	
	PdfPCell c4 = new PdfPCell(new Phrase("Absent",normalFont));
	c4.setHorizontalAlignment(Element.ALIGN_CENTER);
	c4.setBackgroundColor(new Color(255,102,102));
	c4.setBorderColor(Color.BLACK);
	tableTitle.addCell(c4);
	
	
	Paragraph preface1 = new Paragraph();
	creteEmptyLine(preface1, 1);
	
	document.add(preface);
	document.add(tableTitle);
	document.add(preface1);
    }

    private void creteEmptyLine(Paragraph paragraph, int number) {
	for (int i = 0; i <=number; i++) {
	    paragraph.add(new Paragraph(" "));
	}
    }
}

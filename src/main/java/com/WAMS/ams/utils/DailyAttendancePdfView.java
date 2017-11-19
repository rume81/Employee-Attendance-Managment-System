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

import com.webhawks.Hawks_model.HMAttendance;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class DailyAttendancePdfView extends AbstractPdfView {

    private Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    private Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    private Font boldFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
    private Font normalFont = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);

    @Override
    protected void buildPdfDocument(Map model, Document document, PdfWriter writer, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	List<HMAttendance> att = (List<HMAttendance>) model.get("att");

	String date = att.get(0).getAtt_date();
	
	//String team = att.get(0).getTeam();
	
	String fname = "Attendance_"+date+".pdf";
	
	response.setHeader("Content-Disposition", "attachment; filename=\""+fname+"\"");
	
	addMetaData(document);
	 
	addTitlePage(document,date);
	
	createTable(document,att);
    }

    private void addMetaData(Document document) {
	document.addTitle("Generate PDF report");
	document.addSubject("Generate PDF report");
	// document.addAuthor("Java Honk");
	// document.addCreator("Java Honk");
    }

    private void addTitlePage(Document document,String date) throws DocumentException {

	Paragraph preface = new Paragraph();
	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Attendance Status", TIME_ROMAN));

	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Date :" + date, TIME_ROMAN_SMALL));
	
	creteEmptyLine(preface, 1);
	
	PdfPTable tableTitle = new PdfPTable(4);
	tableTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
	tableTitle.setWidthPercentage(40);
	
	PdfPCell c = new PdfPCell(new Phrase("Weekday",normalFont));
	c.setHorizontalAlignment(Element.ALIGN_CENTER);
	c.setBackgroundColor(new Color(0,153,51));
	c.setBorderColor(Color.BLACK);
	tableTitle.addCell(c);
	
	PdfPCell c1 = new PdfPCell(new Phrase("Holiday",normalFont));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setBackgroundColor(new Color(51,153,255));
	c1.setBorderColor(Color.BLACK);
	tableTitle.addCell(c1);
	
	/*PdfPCell c2 = new PdfPCell(new Phrase("Applied",normalFont));
	c2.setHorizontalAlignment(Element.ALIGN_CENTER);
	c2.setBackgroundColor(new Color(0,204,255));
	c2.setBorderColor(Color.BLACK);
	tableTitle.addCell(c2);*/
	
	PdfPCell c3 = new PdfPCell(new Phrase("Leave",normalFont));
	c3.setHorizontalAlignment(Element.ALIGN_CENTER);
	c3.setBackgroundColor(new Color(204,204,204));
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
	for (int i = 0; i < number; i++) {
	    paragraph.add(new Paragraph(" "));
	}
    }
    
    private void createTable(Document document,List<HMAttendance> attList) throws DocumentException {
	Paragraph paragraph = new Paragraph();
	creteEmptyLine(paragraph, 2);
	document.add(paragraph);
	
	PdfPTable table = new PdfPTable(4);
	
	PdfPCell c1 = new PdfPCell(new Phrase("Employee Name",boldFont));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c1);
	
	//table.addCell("Office In");
	PdfPCell c2 = new PdfPCell(new Phrase("Office In",boldFont));
	c2.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c2);
	
	//table.addCell("Office Out");
	PdfPCell c3 = new PdfPCell(new Phrase("Office Out",boldFont));
	c3.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c3);
	
	//table.addCell("Working Time");
	/*PdfPCell c4 = new PdfPCell(new Phrase("Work Time",boldFont));
	c4.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c4);*/
	
	//table.addCell("Status");
	PdfPCell c5 = new PdfPCell(new Phrase("Status",boldFont));
	c5.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c5);
	
	for (HMAttendance att : attList) {
	    
	    table.setWidthPercentage(100);
	    //table.addCell(att.getEmployee_name())
	    PdfPCell d1 = new PdfPCell(new Phrase(att.getEmployee_name(),normalFont));
	    d1.setHorizontalAlignment(Element.ALIGN_LEFT);
	    
	    
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	    
	    PdfPCell d2 = new PdfPCell(new Phrase(att.getAtt_in(),normalFont));
	    
	    
	    PdfPCell d3 = new PdfPCell(new Phrase(att.getAtt_out(),normalFont));
	    
	    
	    /*PdfPCell d4 = new PdfPCell(new Phrase(att.getWrkTime(),normalFont));
	    table.addCell(d4);*/
	    
	    PdfPCell d5 = new PdfPCell(new Phrase(att.getStatus(),normalFont));
	    
	    if(null!= att.getStatus()){
        	    if(att.getStatus().equals("WeekDay"))
        	    {
        		d1.setBackgroundColor(new Color(0,153,51));
        		d1.setBorderColor(Color.BLACK);
        		
        		d2.setBackgroundColor(new Color(0,153,51));
        		d2.setBorderColor(Color.BLACK);
        		
        		d3.setBackgroundColor(new Color(0,153,51));
        		d3.setBorderColor(Color.BLACK);
        		
        		d5.setBackgroundColor(new Color(0,153,51));
        		d5.setBorderColor(Color.BLACK);
        		
        	    } else if(att.getStatus().equals("HoliDay"))
        	    {
        		d1.setBackgroundColor(new Color(51,153,255));
        		d1.setBorderColor(Color.BLACK);
        		
        		d2.setBackgroundColor(new Color(51,153,255));
        		d2.setBorderColor(Color.BLACK);
        		
        		d3.setBackgroundColor(new Color(51,153,255));
        		d3.setBorderColor(Color.BLACK);
        		
        		d5.setBackgroundColor(new Color(51,153,255));
        		d5.setBorderColor(Color.BLACK);
        		
        	    } else if(att.getStatus().equals("Absent"))
        	    {
        		d1.setBackgroundColor(new Color(255,102,102));
        		d1.setBorderColor(Color.BLACK);
        		
        		d2.setBackgroundColor(new Color(255,102,102));
        		d2.setBorderColor(Color.BLACK);
        		
        		d3.setBackgroundColor(new Color(255,102,102));
        		d3.setBorderColor(Color.BLACK);
        		
        		d5.setBackgroundColor(new Color(255,102,102));
        		d5.setBorderColor(Color.BLACK);
        		
        	    } else if(att.getStatus().equals("Leave"))
        	    {
        		d1.setBackgroundColor(new Color(204,204,204));
        		d1.setBorderColor(Color.BLACK);
        		
        		d2.setBackgroundColor(new Color(204,204,204));
        		d2.setBorderColor(Color.BLACK);
        		
        		d3.setBackgroundColor(new Color(204,204,204));
        		d3.setBorderColor(Color.BLACK);
        		
        		d5.setBackgroundColor(new Color(204,204,204));
        		d5.setBorderColor(Color.BLACK);
        		
        	    }
	    }
	    
	    table.addCell(d1);
	    table.addCell(d2);
	    table.addCell(d3);
	    table.addCell(d5);
	}

	document.add(table);
    }
}

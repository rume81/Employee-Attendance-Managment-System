/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* EmployeeOvertimePdfView.java
* --------------------
* Created on Dec 1, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* Dec 1, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.utils;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.webhawks.Hawks_model.HMAttendance;
import com.webhawks.Hawks_model.HMonthlyStatus;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author OS-10 Monsur
 *
 */
public class EmployeeOvertimePdfView extends AbstractPdfView {

    private Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    private Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    private Font boldFont = new Font(Font.TIMES_ROMAN, 9, Font.BOLD);
    private Font normalFont = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);

    @Override
    protected void buildPdfDocument(Map model, Document document, PdfWriter writer, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	List<HMonthlyStatus> overtime = (List<HMonthlyStatus>) model.get("overtime");

	String month = overtime.get(0).getMonth();
	String year = overtime.get(0).getYear();
	
	//String team = overtime.get(0).getTeam();
	
	String fname = "Overtime_"+month+"_"+year+".pdf";
	
	response.setHeader("Content-Disposition", "attachment; filename=\""+fname+"\"");
	
	addMetaData(document);
	 
	addTitlePage(document,month,year);
	
	createTable(document,overtime);
    }

    private void addMetaData(Document document) {
	document.addTitle("Generate PDF report");
	document.addSubject("Generate PDF report");
    }

    private void addTitlePage(Document document,String mon,String year) throws DocumentException {

	Paragraph preface = new Paragraph();
	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Employee Monthly Status", TIME_ROMAN));

	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Month :" + mon, TIME_ROMAN_SMALL));
	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Year :" + year, TIME_ROMAN_SMALL));
	
	creteEmptyLine(preface, 1);
	
	
	
	Paragraph preface1 = new Paragraph();
	creteEmptyLine(preface1, 1);
	
	document.add(preface);
	document.add(preface1);

    }

    private void creteEmptyLine(Paragraph paragraph, int number) {
	for (int i = 0; i < number; i++) {
	    paragraph.add(new Paragraph(" "));
	}
    }
    
    private void createTable(Document document,List<HMonthlyStatus> attList) throws DocumentException {
	Paragraph paragraph = new Paragraph();
	creteEmptyLine(paragraph, 2);
	document.add(paragraph);
	
	PdfPTable table = new PdfPTable(13);
	
	PdfPCell c1 = new PdfPCell(new Phrase("Employee Name",boldFont));
	c1.setHorizontalAlignment(Element.ALIGN_LEFT);
	table.addCell(c1);
	
	//table.addCell("Office In");
	PdfPCell c2 = new PdfPCell(new Phrase("Working Day",boldFont));
	c2.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c2);
	
	//table.addCell("Office Out");
	PdfPCell c3 = new PdfPCell(new Phrase("Present",boldFont));
	c3.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c3);
	
	//table.addCell("Status");
	PdfPCell c5 = new PdfPCell(new Phrase("Working Hrs in Month",boldFont));
	c5.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c5);
	
	//table.addCell("Status");
	PdfPCell c6 = new PdfPCell(new Phrase("Hrs Worked",boldFont));
	c6.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c6);
		
	//table.addCell("Status");
	PdfPCell c7 = new PdfPCell(new Phrase("Hrs Worked Without Overtime",boldFont));
	c7.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c7);
		
	//table.addCell("Status");
	PdfPCell c8 = new PdfPCell(new Phrase("Hrs of Overtime",boldFont));
	c8.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c8);
	
	//table.addCell("Status");
	PdfPCell c9 = new PdfPCell(new Phrase("Hrs of Deficit",boldFont));
	c9.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c9);
	
	//table.addCell("Status");
	PdfPCell c10 = new PdfPCell(new Phrase("Holiday Working Hrs",boldFont));
	c10.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c10);
		
	//table.addCell("Status");
	PdfPCell c11 = new PdfPCell(new Phrase("Leave",boldFont));
	c11.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c11);
	
	//table.addCell("Status");
	PdfPCell c13 = new PdfPCell(new Phrase("LWP",boldFont));
	c13.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c13);
		
	//table.addCell("Status");
	PdfPCell c12 = new PdfPCell(new Phrase("Absent",boldFont));
	c12.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c12);
		
	//table.addCell("Status");
	PdfPCell c4 = new PdfPCell(new Phrase("Late",boldFont));
	c4.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c4);
	
	for (HMonthlyStatus att : attList) {
	    
	    table.setWidthPercentage(100);
	    //table.addCell(att.getEmployee_name())
	    PdfPCell d1 = new PdfPCell(new Phrase(att.getEmployee_name(),normalFont));
	    d1.setHorizontalAlignment(Element.ALIGN_LEFT);
	    
	    
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	    
	    PdfPCell d2 = new PdfPCell(new Phrase(String.valueOf(att.getTotal_working_day()),normalFont));
	    	    
	    PdfPCell d3 = new PdfPCell(new Phrase(String.valueOf(att.getPresent()),normalFont));
	   	       
	    PdfPCell d4 = new PdfPCell(new Phrase(String.valueOf(att.getPresentWorkingHours()),normalFont));
	    
	    PdfPCell d5 = new PdfPCell(new Phrase(String.valueOf(att.getTotalworkinghours()),normalFont));
	    
	    PdfPCell d6 = new PdfPCell(new Phrase(String.valueOf(att.getAbsulateworkinghours()),normalFont));
	    
	    PdfPCell d7 = new PdfPCell(new Phrase(String.valueOf(att.getOvertimehours()),normalFont));
	    
	    PdfPCell d8 = new PdfPCell(new Phrase(String.valueOf(att.getDeficithours()),normalFont));
	    
	    PdfPCell d9 = new PdfPCell(new Phrase(String.valueOf(att.getHolidayovertime()),normalFont));
	    
	    PdfPCell d10 = new PdfPCell(new Phrase(String.valueOf(att.getLeave()),normalFont));
	    
	    PdfPCell d13 = new PdfPCell(new Phrase(String.valueOf(att.getLwp()),normalFont));
	    
	    PdfPCell d11 = new PdfPCell(new Phrase(String.valueOf(att.getAbsent()),normalFont));
	    
	    PdfPCell d12 = new PdfPCell(new Phrase(String.valueOf(att.getLate()),normalFont));
	    
	    /*if(null!= att.getStatus()){
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
	    }*/
	    
	    table.addCell(d1);
	    table.addCell(d2);
	    table.addCell(d3);
	    table.addCell(d4);
	    table.addCell(d5);
	    table.addCell(d6);
	    table.addCell(d7);
	    table.addCell(d8);
	    table.addCell(d9);
	    table.addCell(d10);
	    table.addCell(d13);
	    table.addCell(d11);
	    table.addCell(d12);
	}

	document.add(table);
    }

}

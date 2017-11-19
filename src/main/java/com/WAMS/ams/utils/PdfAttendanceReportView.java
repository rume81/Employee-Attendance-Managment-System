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

public class PdfAttendanceReportView extends AbstractPdfView {

    private Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    private Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    private Font boldFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
    private Font normalFont = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);

    @Override
    protected void buildPdfDocument(Map model, Document document, PdfWriter writer, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	List<HMAttendance> att = (List<HMAttendance>) model.get("att");

	String date = att.get(0).getAtt_date();
	
	String team = att.get(0).getTeam();
	
	String fname = "Attendance_"+team+"_"+date+".pdf";
	
	response.setHeader("Content-Disposition", "attachment; filename=\""+fname+"\"");
	
	addMetaData(document);
	 
	addTitlePage(document,date,team);
	
	createTable(document,att);
    }

    private void addMetaData(Document document) {
	document.addTitle("Generate PDF report");
	document.addSubject("Generate PDF report");
	// document.addAuthor("Java Honk");
	// document.addCreator("Java Honk");
    }

    private void addTitlePage(Document document,String date, String team) throws DocumentException {

	Paragraph preface = new Paragraph();
	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Attendance Status", TIME_ROMAN));

	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Date :" + date, TIME_ROMAN_SMALL));
		
	creteEmptyLine(preface, 1);
	preface.add(new Paragraph("Team :" + team, TIME_ROMAN_SMALL));
	document.add(preface);

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
	
	PdfPTable table = new PdfPTable(5);
	
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
	PdfPCell c4 = new PdfPCell(new Phrase("Work Time",boldFont));
	c4.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c4);
	
	//table.addCell("Status");
	PdfPCell c5 = new PdfPCell(new Phrase("Status",boldFont));
	c5.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(c5);
	
	for (HMAttendance att : attList) {
	    table.setWidthPercentage(100);
	    //table.addCell(att.getEmployee_name())
	    PdfPCell d1 = new PdfPCell(new Phrase(att.getEmployee_name(),normalFont));
	    d1.setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.addCell(d1);
	    
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	    
	    PdfPCell d2 = new PdfPCell(new Phrase(att.getAtt_in(),normalFont));
	    table.addCell(d2);
	    
	    PdfPCell d3 = new PdfPCell(new Phrase(att.getAtt_out(),normalFont));
	    table.addCell(d3);
	    
	    PdfPCell d4 = new PdfPCell(new Phrase(att.getWrkTime(),normalFont));
	    table.addCell(d4);
	    
	    PdfPCell d5 = new PdfPCell(new Phrase(att.getStatus(),normalFont));
	    if(att.getStatus()!=null && att.getStatus().contains("Late"))
		table.addCell("");
	    else
		table.addCell(d5);
	}

	document.add(table);
    }
}

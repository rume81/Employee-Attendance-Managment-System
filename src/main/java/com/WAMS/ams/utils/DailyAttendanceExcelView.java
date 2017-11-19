/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* DailyAttendanceExcelView.java
* --------------------
* Created on May 3, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* May 3, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.webhawks.Hawks_model.HMAttendance;

/**
 * @author OS-10 Monsur
 *
 */
public class DailyAttendanceExcelView extends AbstractExcelView {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	response.setHeader("Content-Disposition", "attachment; filename=\"EmpDailyAttendance.xls\"");
	List<HMAttendance> att = (List<HMAttendance>) model.get("att");
	
	String date = att.get(0).getAtt_date();
	HSSFSheet excelSheet = workbook.createSheet(date);
	setExcelHeader(excelSheet,date);
	setExcelRows(excelSheet,att);
	
    }
    public void setExcelHeader(HSSFSheet excelSheet,String date) {
	HSSFRow exceldesc = excelSheet.createRow(2);
	exceldesc.createCell(2).setCellValue("Date:");
	exceldesc.createCell(3).setCellValue(date);
	HSSFRow excelHeader = excelSheet.createRow(5);
	//excelHeader.createCell(0).setCellValue("EmpId");
	excelHeader.createCell(2).setCellValue("Emp Name");
	excelHeader.createCell(3).setCellValue("In Time");
	excelHeader.createCell(4).setCellValue("Out Time");
	excelHeader.createCell(5).setCellValue("Status");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<HMAttendance> attList) {
	int record = 6;
	for (HMAttendance att : attList) {
	    HSSFRow excelRow = excelSheet.createRow(record++);
	    /*if(att.getEmp_id()!=null)
		excelRow.createCell(0).setCellValue(att.getEmp_id());
	    else
		excelRow.createCell(0).setCellValue("");*/
	    if(att.getEmployee_name()!=null)
		excelRow.createCell(2).setCellValue(att.getEmployee_name());
	    else
		excelRow.createCell(2).setCellValue("");
	    if(att.getAtt_in()!=null)
		excelRow.createCell(3).setCellValue(att.getAtt_in());
	    else
		excelRow.createCell(3).setCellValue("");
	    if(att.getAtt_out()!=null)
		excelRow.createCell(4).setCellValue(att.getAtt_out());
	    else
		excelRow.createCell(4).setCellValue("");
	    if(att.getStatus()!=null)
		excelRow.createCell(5).setCellValue(att.getStatus());
	    else
		excelRow.createCell(5).setCellValue("");
	}
    }
}

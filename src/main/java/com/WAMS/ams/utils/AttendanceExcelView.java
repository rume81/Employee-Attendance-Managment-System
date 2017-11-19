/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* AttendanceExcelView.java
* --------------------
* Created on Apr 28, 2016
*
* Revision: 
* Author: 
* Source: 
* Id:  
*
* Apr 28, 2016: Original version (Monsur)
*
*/
package com.WAMS.ams.utils;

import java.util.HashMap;
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
public class AttendanceExcelView extends AbstractExcelView {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.view.document.AbstractExcelView#
     * buildExcelDocument(java.util.Map,
     * org.apache.poi.hssf.usermodel.HSSFWorkbook,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	response.setHeader("Content-Disposition", "attachment; filename=\"Emp_Attendance.xls\"");
	Map<String,List<HMAttendance>> attList = (Map<String,List<HMAttendance>>) model.get("attList");
	Iterator it = attList.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry pair = (Map.Entry)it.next();
	    //System.out.println(pair.getKey() + " = " + pair.getValue());
	    HSSFSheet excelSheet = workbook.createSheet(String.valueOf(pair.getKey()));
	    setExcelHeader(excelSheet);
		
	    //List<HMAttendance> att = (List<HMAttendance>) model.get("attList");
	    List<HMAttendance> att = (List<HMAttendance>) pair.getValue();
	    setExcelRows(excelSheet,att);
	    it.remove(); // avoids a ConcurrentModificationException
	}
    }

    public void setExcelHeader(HSSFSheet excelSheet) {
	HSSFRow excelHeader = excelSheet.createRow(0);
	//excelHeader.createCell(0).setCellValue("EmpId");
	excelHeader.createCell(0).setCellValue("Date");
	excelHeader.createCell(1).setCellValue("In Time");
	excelHeader.createCell(2).setCellValue("Out Time");
	excelHeader.createCell(3).setCellValue("Status");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<HMAttendance> attList) {
	int record = 1;
	for (HMAttendance att : attList) {
	    HSSFRow excelRow = excelSheet.createRow(record++);
	    /*if(att.getEmp_id()!=null)
		excelRow.createCell(0).setCellValue(att.getEmp_id());
	    else
		excelRow.createCell(0).setCellValue("");*/
	    if(att.getAtt_date()!=null)
		excelRow.createCell(0).setCellValue(att.getAtt_date());
	    else
		excelRow.createCell(0).setCellValue("");
	    if(att.getAtt_in()!=null)
		excelRow.createCell(1).setCellValue(att.getAtt_in());
	    else
		excelRow.createCell(1).setCellValue("");
	    if(att.getAtt_out()!=null)
		excelRow.createCell(2).setCellValue(att.getAtt_out());
	    else
		excelRow.createCell(2).setCellValue("");
	    if(att.getStatus()!=null)
		excelRow.createCell(3).setCellValue(att.getStatus());
	    else
		excelRow.createCell(3).setCellValue("");
	}
    }

}

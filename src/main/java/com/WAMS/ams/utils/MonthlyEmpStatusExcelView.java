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

import com.webhawks.Hawks_model.HMonthlyStatus;

/**
 * @author OS-10 Monsur
 *
 */
public class MonthlyEmpStatusExcelView extends AbstractExcelView {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	response.setHeader("Content-Disposition", "attachment; filename=\"EmployeeMonthlyStatus.xls\"");
	List<HMonthlyStatus> att = (List<HMonthlyStatus>) model.get("att");
	
	String Month = att.get(0).getMonth();
	String Year = att.get(0).getYear();
	HSSFSheet excelSheet = workbook.createSheet(Month+" "+Year);
	setExcelHeader(excelSheet,Month+"/"+Year);
	setExcelRows(excelSheet,att);
	
    }
    public void setExcelHeader(HSSFSheet excelSheet,String monyear) {
	HSSFRow exceldesc = excelSheet.createRow(1);
	exceldesc.createCell(1).setCellValue("Month/year:");
	exceldesc.createCell(2).setCellValue(monyear);
	HSSFRow excelHeader = excelSheet.createRow(2);
	//excelHeader.createCell(0).setCellValue("EmpId");
	excelHeader.createCell(1).setCellValue("Employee Name");
	excelHeader.createCell(2).setCellValue("Working Day");
	excelHeader.createCell(3).setCellValue("Present");
	excelHeader.createCell(4).setCellValue("Leave");
	excelHeader.createCell(5).setCellValue("Absent");
	excelHeader.createCell(6).setCellValue("Late");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<HMonthlyStatus> attList) {
	int record = 3;
	for (HMonthlyStatus att : attList) {
	    HSSFRow excelRow = excelSheet.createRow(record++);
	    
	    if(att.getEmployee_name()!=null)
		excelRow.createCell(1).setCellValue(att.getEmployee_name());
	    else
		excelRow.createCell(1).setCellValue("");
	    if(att.getTotal_working_day()!=null)
		excelRow.createCell(2).setCellValue(att.getTotal_working_day());
	    else
		excelRow.createCell(2).setCellValue("");
	    if(att.getPresent()!=null)
		excelRow.createCell(3).setCellValue(att.getPresent());
	    else
		excelRow.createCell(3).setCellValue("");
	    if(att.getLeave()!=null)
		excelRow.createCell(4).setCellValue(att.getLeave());
	    else
		excelRow.createCell(4).setCellValue("");
	    if(att.getAbsent()!=null)
		excelRow.createCell(5).setCellValue(att.getAbsent());
	    else
		excelRow.createCell(5).setCellValue("");
	    if(att.getLate()!=null)
		excelRow.createCell(6).setCellValue(att.getLate());
	    else
		excelRow.createCell(6).setCellValue("");
	}
    }
}

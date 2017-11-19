/* ========================================
* Scooby v. 1.0 class library
* ========================================
*
* http://www.scooby.com
*
* (C) Copyright 2010-2020, by WebHawksIT.
*
* --------------------
* EmployeeOvertimeExcelView.java
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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.webhawks.Hawks_model.HMAttendance;
import com.webhawks.Hawks_model.HMonthlyStatus;

/**
 * @author OS-10 Monsur
 *
 */
public class EmployeeOvertimeExcelView extends AbstractExcelView {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	List<HMonthlyStatus> overtime = (List<HMonthlyStatus>) model.get("overtime");
	
	String month = overtime.get(0).getMonth();
	String year = overtime.get(0).getYear();
	
	response.setHeader("Content-Disposition", "attachment; filename=\"Overtime_"+month+"_"+year+".xls\"");
	
	HSSFSheet excelSheet = workbook.createSheet(month+"_"+year);
	setExcelHeader(excelSheet,month,year);
	setExcelRows(excelSheet,overtime);
	
    }
    public void setExcelHeader(HSSFSheet excelSheet,String mon,String year) {
	HSSFRow excelName = excelSheet.createRow(1);
	excelName.createCell(2).setCellValue("Employee Monthly Status");
	HSSFRow exceldesc = excelSheet.createRow(2);
	exceldesc.createCell(2).setCellValue("Month/Year:");
	exceldesc.createCell(3).setCellValue(mon+"/"+year);
	HSSFRow excelHeader = excelSheet.createRow(5);
	//excelHeader.createCell(0).setCellValue("EmpId");
	excelHeader.createCell(2).setCellValue("Emp Name");
	excelHeader.createCell(3).setCellValue("Working Day");
	excelHeader.createCell(4).setCellValue("Present");
	excelHeader.createCell(5).setCellValue("Working Hrs in Month");
	excelHeader.createCell(6).setCellValue("Hrs Worked");
	excelHeader.createCell(7).setCellValue("Hrs Worked Without Overtime");
	excelHeader.createCell(8).setCellValue("Hrs of Overtime");
	excelHeader.createCell(9).setCellValue("Hrs of Deficit");
	excelHeader.createCell(10).setCellValue("Holiday Working Hrs");
	excelHeader.createCell(11).setCellValue("Leave");
	excelHeader.createCell(12).setCellValue("LWP");
	excelHeader.createCell(13).setCellValue("Absent");
	excelHeader.createCell(14).setCellValue("Late");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<HMonthlyStatus> attList) {
	int record = 6;
	for (HMonthlyStatus att : attList) {
	    HSSFRow excelRow = excelSheet.createRow(record++);
	    if(att.getEmployee_name()!=null)
		excelRow.createCell(2).setCellValue(att.getEmployee_name());
	    else
		excelRow.createCell(2).setCellValue("");
	   
	    if(att.getTotal_working_day()!=null)
		excelRow.createCell(3).setCellValue(att.getTotal_working_day());
	    else
		excelRow.createCell(3).setCellValue("");
	    
	    if(att.getPresent()!=null)
		excelRow.createCell(4).setCellValue(att.getPresent());
	    else
		excelRow.createCell(4).setCellValue("");
	    
	    if(att.getPresentWorkingHours()!=null)
		excelRow.createCell(5).setCellValue(att.getPresentWorkingHours());
	    else
		excelRow.createCell(5).setCellValue("");
	    
	    if(att.getTotalworkinghours()!=null)
		excelRow.createCell(6).setCellValue(att.getTotalworkinghours());
	    else
		excelRow.createCell(6).setCellValue("");
	    
	    if(att.getAbsulateworkinghours()!=null)
		excelRow.createCell(7).setCellValue(att.getAbsulateworkinghours());
	    else
		excelRow.createCell(7).setCellValue("");
	    
	    if(att.getOvertimehours()!=null)
		excelRow.createCell(8).setCellValue(att.getOvertimehours());
	    else
		excelRow.createCell(8).setCellValue("");
	    
	    if(att.getDeficithours()!=null)
		excelRow.createCell(9).setCellValue(att.getDeficithours());
	    else
		excelRow.createCell(9).setCellValue("");
	    
	    if(att.getDeficithours()!=null)
		excelRow.createCell(10).setCellValue(att.getHolidayovertime());
	    else
		excelRow.createCell(10).setCellValue("");
	    
	    if(att.getLeave()!=null)
		excelRow.createCell(11).setCellValue(att.getLeave());
	    else
		excelRow.createCell(11).setCellValue("");
	    
	    if(att.getLwp()!=null)
		excelRow.createCell(12).setCellValue(att.getLwp());
		else
		excelRow.createCell(12).setCellValue("");
	    
	    if(att.getAbsent()!=null)
		excelRow.createCell(13).setCellValue(att.getAbsent());
	    else
		excelRow.createCell(13).setCellValue("");
	    
	    if(att.getLate()!=null)
		excelRow.createCell(14).setCellValue(att.getLate());
	    else
		excelRow.createCell(14).setCellValue("");
	}
    }

}

<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Employee Attendance Download</center></h3>
<form name="downloadempatt" id="downloadempatt" action="${rc.contextPath}/EmployeeAttendance" method="get" onsubmit="return validateAttendanceDownloadForm()">
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
    	<#if emp.usertype == 'Admin' || emp.usertype == 'Watcher' || emp.usertype == 'Manager' || emp.usertype == 'Talent Manager'>
	        <div class="col-xs-12 no_padding">
	            <table class="table table-bordered table-responsive">
	                <tr>
	                    <td class="static_data">Month</td>
	                    <td class="static_data">Year</td>
	                </tr>
	                <tr>
	                    <td>
	                    	<select name="mon" id="mon" class="form-control SlectBox">
		                    	<option value="">Select Month</option>
		                        <option value="1">January</option>
		                        <option value="2">February</option>
		                        <option value="3">March</option>
		                        <option value="4">April</option>
		                        <option value="5">May</option>
		                        <option value="6">June</option>
		                        <option value="7">July</option>
		                        <option value="8">August</option>
		                        <option value="9">September</option>
		                        <option value="10">October</option>
		                        <option value="11">November</option>
		                        <option value="12">December</option>          
	                    	</select>
	                    </td>
	                    <td>
	                    	<input type="text" value="" name="year" id="year" placeholder="Input Year" class="form-control">
	                    </td>
	                </tr>
	            </table>
	        </div>
	        <div class="col-xs-12 no_padding">
	            <table class="table table-bordered table-responsive">
	                <tr>
	                    <td class="static_data">Employee</td>
	                </tr>
	                <tr>
                		<td>
                			<input type="checkbox" name="" id="chkAll" onClick="toggle(this)" value="All"> All<br>
                			<#list allEmp1 as employee>
                				<input type="checkbox" name="chk" onClick="toggleselect()" id="chk${employee.emp_id}" value="${employee.emp_id}"> ${employee.employee_name!''}(${employee.avator!''})<br>
                			</#list>
                		</td>
                		<td>
                			<#list allEmp2 as employee>
                				<input type="checkbox" name="chk" onClick="toggleselect()" id="chk${employee.emp_id}" value="${employee.emp_id}"> ${employee.employee_name!''}(${employee.avator!''})<br>
                			</#list>
                		</td>    	
	                </tr>
	                <tr>
	                	<td  colspan="2">
	                		<!--<button type="button" class="btn btn-primary" onclick="empAttDownload('${rc.contextPath}');">Download</button>-->
	                		<input type="submit" class="btn btn-primary" value="Download">
	                	</td>
	                </tr>
	            </table>
	        </div>
        </#if>
    </div>
</div>
</form>
	
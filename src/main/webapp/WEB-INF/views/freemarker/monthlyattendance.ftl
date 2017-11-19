<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Employee Monthly Attendance</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
    	
	        <div class="col-xs-12 no_padding">
	            <table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">Month</td>
	                    <td class="static_data">Year</td>
	                    <#if emp.usertype == 'Admin' || emp.usertype == 'Watcher' || emp.usertype == 'Manager' || emp.usertype == 'Talent Manager'>
	                    <td class="static_data">EmpId</td>
	                    </#if>
	                    <td class="static_data" colspan="2">Search</td>
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
	                    <#if emp.usertype == 'Admin' || emp.usertype == 'Watcher' || emp.usertype == 'Manager' || emp.usertype == 'Talent Manager'>
	                    <td>
	                    	<select name="emp_id" id="emp_id" class="form-control SlectBox">
		                    	<option value="">Select Employee</option>
		                    	<#list allEmp as employee>
		                        <option value="${employee.emp_id}">${employee.employee_name!''}(${employee.avator!''})</option>
		                        </#list>       
	                    	</select>
	                    </td>
	                    <#else>
	                    <input type="hidden" id="emp_id" name="emp_id" value="${emp.emp_id!''}"/>
	                    </#if>
	                    <td colspan="2">
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="empAttSearch('${rc.contextPath}');">Search</button>
	                    </td>
	                </tr>
	            </table>
	        </div>
        
        <h4 class="user_heading">Attendance Information</h4>
		<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
	    	<table class="table table-bordered table-responsive">
		        <tr>
		            <td class="static_data">Date</td>
		            <td class="static_data">In Time</td>
		            <td class="static_data">Out Time</td>
		            <td class="static_data">Status</td>
		        </tr>
		        <#if (monAtt?size>0)>
			        <#list monAtt as att>
			        <tr <#if att.weekend?? && att.isWeekend()>bgcolor="#009933"<#elseif att.holiday?? && att.isHoliday()>bgcolor="#3399FF"<#elseif att.absent?? && att.isAbsent()>bgcolor="#FF6666"<#elseif att.leave?? && att.isLeave()>bgcolor="#CCCCCC"</#if>>
			        	<td>${att.att_date!''}</td>
			        	<td>${att.att_in!''}</td>
			        	<td>${att.att_out!''}</td>
			        	<td>${att.status!''}</td>
			        </tr>
			        </#list>
			    <#else>
			    	<tr>
			    		<td colspan="4"><center><b>Attendance not found</b></center></td>
			    	</tr>
			    </#if>
		    </table>
		</div>
    </div>
</div>
	
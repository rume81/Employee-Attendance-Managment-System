<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<script>
$(document).ready(function() {
	$('#sdate').datepicker({ dateFormat: 'dd-mm-yy' });	
})
</script>
<h3><center>Employee Monthly Status</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
    	<form name="dailyempmonthlystatus" id="dailyempmonthlystatus" action="${rc.contextPath}/dailyempmonthlystatus" method="get">
	    	<div class="col-xs-12 no_padding">
	    		<table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">Team</td>
	                    <td class="static_data">Month</td>
	                    <td class="static_data">Year</td>
	                    <td class="static_data"  colspan="2">Search</td>
	                </tr>
	                <tr>
	                	<td>
		                	<select name="team" id="team" class="form-control SlectBox">
		                    	<option value="0">Select Team</option>
		                    	<#list allTeam as team>
		                        <option value="${team.teamId}">${team.teamName!''}</option>
		                        </#list>
		                    </select>
	                	</td>
	                    <td>
	                    	<select name="mon" id="mon" class="form-control SlectBox">
		                    	<option value="">Select Month</option>
		                        <option value="1" <#if repmon==1>selected</#if>>January</option>
		                        <option value="2" <#if repmon==2>selected</#if>>February</option>
		                        <option value="3" <#if repmon==3>selected</#if>>March</option>
		                        <option value="4" <#if repmon==4>selected</#if>>April</option>
		                        <option value="5" <#if repmon==5>selected</#if>>May</option>
		                        <option value="6" <#if repmon==6>selected</#if>>June</option>
		                        <option value="7" <#if repmon==7>selected</#if>>July</option>
		                        <option value="8" <#if repmon==8>selected</#if>>August</option>
		                        <option value="9" <#if repmon==9>selected</#if>>September</option>
		                        <option value="10" <#if repmon==10>selected</#if>>October</option>
		                        <option value="11" <#if repmon==11>selected</#if>>November</option>
		                        <option value="12" <#if repmon==12>selected</#if>>December</option>          
	                    	</select>
	                    </td>
	                    <td>
	                    	<input type="text" value="${repyear}" name="year" id="year" placeholder="Input Year" class="form-control">
	                    </td>
	                    <td>
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="empAttStatus('${rc.contextPath}')">Search</button>
	                    </td>
	                </tr>
	            </table>
	    	</div>
			<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
		    	<table class="table table-bordered table-responsive">
			        <tr class="static_data">
			        	<!--<td><input type="checkbox" name="" id="chkAll" onClick="toggle(this)" value="All"></td>-->
			        	<td>Employee Name</td>
			        	<td>Total Working Day</td>
			        	<td>Present</td>
			        	<td>Leave</td>
			        	<td>LWP</td>
			        	<td>Absent</td>
			        	<td>Late</td>
			        </tr>
			        <#list att as at>
			        <tr>
			        	<!--<td><input type="checkbox" name="chk" onClick="toggleselect()" id="chk${at.emp_id}" value="${at.emp_id}"></td>-->
			        	<td>${at.employee_name!''}</td>
			        	<td>${at.total_working_day!''}</td>
			        	<td>${at.present!''}</td>
			        	<td>${at.leave!''}</td>
			        	<td>${at.lwp!''}</td>
			        	<td>${at.absent!''}</td>
			        	<td>${at.late!''}</td>
			        </tr>
			        </#list>
			        <tr>
	                	<td  colspan="7">
	                		<input type="submit" class="btn btn-primary" value="Download">
	                		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
	                	</td>
	                </tr>
			    </table>
			</div>
		</form>
    </div>
</div>

	
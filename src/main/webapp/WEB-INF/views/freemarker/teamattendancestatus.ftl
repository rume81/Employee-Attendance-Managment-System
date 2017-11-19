<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<script>
$(document).ready(function() {
	$('#date').datepicker({ dateFormat: 'dd-mm-yy' });	
})
</script>
<h3><center>Attendance Status</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <form name="downloadempatt" id="downloadempatt" action="${rc.contextPath}/dailyEmpAttdownloadByTeam" method="get">
        	<div class="col-xs-12 no_padding">
        		<table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">Date</td>
	                    <td class="static_data">Team</td>
	                    <td class="static_data" colspan="2">Search</td>
	                </tr>
	                <tr>
	                    <td>
	                    	<input type="text" value="" name="date" id="date" placeholder="Date" class="form-control">
	                    </td>
	                    <td>
		                	<select name="team" id="team" class="form-control SlectBox">
		                    	<option value="">Select Team</option>
		                    	<#list allTeam as team>
		                        <option value="${team.teamId}">${team.teamName!''}</option>
		                        </#list>
		                    </select>
	                	</td>
	                    <td colspan="2">
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="empDailyAttSearchByTeam('${rc.contextPath}');">Search</button>
	                    </td>
	                </tr>
	            </table>
        	</div>
			<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
		    	<table class="table table-bordered table-responsive">
			        <tr>
			            <td colspan="5" class="static_data"><center>Today's Attendance</center></td>
			        </tr>
			        <tr class="static_data">
			        	<td>Employee Name</td>
			        	<td>Office In</td>
			        	<td>Office Out</td>
			        	<td>Work Time</td>
			        	<td>Status</td>
			        </tr>
			        <#list att as at>
			        <tr>
			        	<td>${at.employee_name!''}</td>
			        	<td>${at.att_in!''}</td>
			        	<td>${at.att_out!''}</td>
			        	<td></td>
			        	<td>${at.status!''}</td>
			        </tr>
			        </#list>
			        <tr>
	                	<td  colspan="5">
	                		<!--<button type="button" class="btn btn-primary" onclick="empAttDownload('${rc.contextPath}');">Download</button>-->
	                		<input type="submit" class="btn btn-primary" value="Download">
	                		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
	                	</td>
	                </tr>
			    </table>
			</div>
		</form>
    </div>
</div>
	
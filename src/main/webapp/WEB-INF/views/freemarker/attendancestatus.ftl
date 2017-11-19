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
        <form name="downloadempatt" id="downloadempatt" action="${rc.contextPath}/dailyEmpAttdownload" method="get">
        	<div class="col-xs-12 no_padding">
        		<table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">Date</td>
	                    <td class="static_data" colspan="2">Search</td>
	                </tr>
	                <tr>
	                    <td>
	                    	<input type="text" value="" name="date" id="date" placeholder="Date" class="form-control">
	                    </td>
	                    <td colspan="2">
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="empDailyAttSearch('${rc.contextPath}');">Search</button>
	                    </td>
	                </tr>
	            </table>
        	</div>
			<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
		    	<table class="table table-bordered table-responsive">
			        <tr>
			            <td colspan="4" class="static_data"><center>Today's Attendance</center></td>
			        </tr>
			        <tr class="static_data">
			        	<td>Employee Name</td>
			        	<td>Office In</td>
			        	<td>Office Out</td>
			        	<td>Status</td>
			        </tr>
			        <#list att as at>
			        <tr <#if at.status?? && at.status=='WeekDay'>bgcolor="#009933"<#elseif  at.status?? &&  at.status=='HoliDay'>bgcolor="#3399FF"<#elseif  at.status?? && at.status=='Absent'>bgcolor="#FF6666"<#elseif at.status?? &&  at.status=='Leave'>bgcolor="#CCCCCC"</#if>>
			        	<td>${at.employee_name!''}</td>
			        	<td>${at.att_in!''}</td>
			        	<td>${at.att_out!''}</td>
			        	<td>${at.status!''}</td>
			        </tr>
			        </#list>
			        <tr>
	                	<td  colspan="4">
	                		<!--<button type="button" class="btn btn-primary" onclick="empAttDownload('${rc.contextPath}');">Download</button>-->
	                		<button type="submit" class="btn btn-primary" name="action" value="Excel">Excel Download</button>
	                		<button type="submit" class="btn btn-primary" name="action" value="PDF">PDF Download</button>
	                		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
	                	</td>
	                </tr>
			    </table>
			</div>
		</form>
    </div>
</div>
<!--<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
		<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
	    	<table class="table table-bordered table-responsive">
		        <tr>
		            <td class="static_data"><center>Today's Not IN</center></td>
		        </tr>
		        <#list absEmp as emp>
		        <tr>
		        	<td>${emp.employee_name!''}(${emp.avator!''})</td>
		        </tr>
		        </#list>
		    </table>
		</div>
    </div>
</div>-->
	
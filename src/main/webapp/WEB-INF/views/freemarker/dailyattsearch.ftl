<table class="table table-bordered table-responsive">
    <tr>
        <td colspan="4" class="static_data"><center>Attendance of ${date!''}</center></td>
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
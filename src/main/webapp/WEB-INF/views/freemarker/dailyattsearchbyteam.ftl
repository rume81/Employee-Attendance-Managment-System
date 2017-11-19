<table class="table table-bordered table-responsive">
    <tr>
        <td colspan="5" class="static_data"><center>Attendance of ${date!''}</center></td>
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
    	<td>${at.wrkTime!''}</td>
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
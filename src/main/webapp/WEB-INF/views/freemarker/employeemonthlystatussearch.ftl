<table class="table table-bordered table-responsive">
    <tr class="static_data">
    	<!--<td><input type="checkbox" name="" id="chkAll" onClick="toggle(this)" value="All"></td>-->
    	<td>Employee Name</td>
    	<td>Total Working Day</td>
    	<td>Present</td>
    	<td>Leave</td>
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
			
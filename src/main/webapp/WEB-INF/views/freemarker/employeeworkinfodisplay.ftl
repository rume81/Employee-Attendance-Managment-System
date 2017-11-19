<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#from_date').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Add Employee Work Info</center></h3>
<#setting number_format="0.##">
<form name="addempworkInfo" id="addempworkInfo" action="${rc.contextPath}/addempwork" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	        	<tr>
	        		<td class="name">Employee Id</td>
	        		<td><input type="hidden" id="emp_id" name="emp_id" value="${selemp.emp_id!''}"/>${selemp.emp_number!''}</td>
	        		<td class="name">Employee Name</td>
	        		<td>${selemp.employee_name!''}</td>
	        	</tr>
	        	<tr>
	        		<td class="name">Employee Avator</td>
	        		<td>${selemp.avator!''}</td>
	        		<td class="name">Office Start Time</td>
	                <td>${workinfo.office_start!''}</td>
	    		</tr>
	    		<tr>
	                <td class="name">Working Hour</td>
	                <td>${workinfo.working_hour!''}</td>
	                <td class="name">Weekend</td>
	                <td>${workinfo.weekend!''}</td>
	           	</tr>
	            <tr>
	            	<td class="name">Applied Date</td>
	                <td>${workinfo.from_date!''}</td>
	                <td class="name"></td>
	                <td></td>
	            </tr>
	            <tr>
	            	<td class="name">Leave Approvers</td>
	                <td>
	                <#list approver as apr>
	                	<#list allEmp as employee>
	                		<#if apr.approver_id==employee.emp_id>
	                			-- ${employee.employee_name!''}(${employee.avator!''})<br>
	                			<#break>
	                		</#if>
	                	</#list>
	                </#list>
	                </td>
	                <td class="name">Office Outgoing Approvers</td>
	                <td>
	                <#list offapprover as apr>
	                	<#list allEmp as employee>
	                		<#if apr.approver_id==employee.emp_id>
	                			-- ${employee.employee_name!''}(${employee.avator!''})<br>
	                			<#break>
	                		</#if>
	                	</#list>
	                </#list>
	                </td>
	            </tr>
	        </table>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
		</div>
	</div>
</form>
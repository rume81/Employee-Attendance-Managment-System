<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	<#list leavetype as type>
		<#if type.isquota>
			<#if type.iscontinue==false>
			$('#expdate${type.id}').datepicker({ dateFormat: 'dd-mm-yy' });
			</#if>
		</#if>
	</#list>
})
</script>
<h3><center>Employee Leave Quota</center></h3>
<form name="saveempleavequota" id="saveempleavequota" action="${rc.contextPath}/saveempleavequota" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	        	<tr>
	        		<td class="name">Employee Id</td>
	        		<td><input type="hidden" id="emp_id" name="emp_id" value="${selemp.emp_id!''}"/>${selemp.emp_number!''}</td>
	        		<td class="name">Employee Name</td>
	        		<td>${selemp.employee_name!''}</td>
	        		<td class="name">Employee Avator</td>
	        		<td>${selemp.avator!''}</td>
	        	</tr>
	        </table>
	        <br/>
	        <table class="table table-bordered table-responsive">
	        	<tr>
			        <td>Year</td>
			        <td>
			        	${selectYear!''}
			        </td>
			        <td>Leave Quota</td>
			        <td>Leave Used</td>
			        <td>Expire Date</td>
			    </tr>
	        	<#list leavetype as type>
	        		<#if type.isquota>
	        			<#assign qto = 0.0>
	        			<#assign usd = 0.0>
	        			<#assign exd = "">
	        			<#list quotas as quota>
	        				<#if type.id==quota.type_id>
	        					<#assign qto = quota.quota?c>
	        					<#assign usd = quota.used?c>
	        					<#assign exd = quota.expdate>
	        					<#break>
	        				</#if>	
	        			</#list>
			        	<tr>
			        		<td colspan="2">${type.leavetype!''}<input type="hidden" id="type_id" name="type_id" value="${type.id!''}"/></td>
			                <td>${qto}</td>
			                <td>${usd}</td>
			                <td>
			                	<#if type.iscontinue==false>
			                		${exd}
			                	<#else>
			                		
			                	</#if>
			                </td>
			    		</tr>
			    	</#if>
	    		</#list>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Edit emp leave quota"  class="btn btn-primary" onclick="addEmpLeaveQuota('${rc.contextPath}', ${selemp.emp_id!''})">Edit</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>

<div class="col-xs-12 manipulation">
	<h3><center>Change Password</center></h3>
	<div class="col-xs-12 manipulation">
	<form class="form-horizontal" name="changepass" id="changepass" action="${rc.contextPath}/updatepass" method="post">
		<div class="form-group">
			<label for="" class="col-xs-3">
			<#if emp.usertype == 'Admin' || emp.usertype == 'Talent Manager'>
	    		Employee Name *
	    	<#else>
	    		Employee Name
	    	</#if>
	    	</label>
			<div class="col-xs-4">
				<#if emp.usertype == 'Admin' || emp.usertype == 'Talent Manager'>
				<select name="emp_id" id="emp_id" class="form-control SlectBox">
                	<option value="">Select Employee</option>
                	<#list allEmp as employee>
                    <option value="${employee.emp_id}">${employee.employee_name!''}(${employee.avator!''})</option>
                    </#list>       
            	</select>
            	<#else>
					${emp.employee_name}(${emp.avator})
			    	<input type="hidden" id="emp_id" name="emp_id" value="${emp.emp_id!''}"/>
				</#if>
			</div>
		</div>
		<#if emp.usertype != 'Admin'>
		<div class="form-group">
			<label for="" class="col-sm-3">Old Password *</label>
			<div class="col-sm-4">
				<input class="form-control login_input" required placeholder="Old Password" name="old_pass" id="old_pass" type="password">
			</div>
		</div>
		</#if>
		<div class="form-group">
			<label for="" class="col-sm-3">New Password *</label>
			<div class="col-sm-4">
				<input class="form-control login_input" required placeholder="New Password" name="new_pass" id="new_pass" type="password">
			</div>
		</div>
		<div class="form-group">
			<label for="" class="col-sm-3">Confirm Password *</label>
			<div class="col-sm-4">
				<input class="form-control login_input" required placeholder="Confirm Password" name="confirm_pass" id="confirm_pass" type="password">
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-7">
				<button type="button" title="Change Attendance" class="btn btn-primary pull-right" onclick="changePass('${rc.contextPath}','${emp.usertype}');">Change Password</button>
			</div>
		</div>
	</form>
	</div>
</div>
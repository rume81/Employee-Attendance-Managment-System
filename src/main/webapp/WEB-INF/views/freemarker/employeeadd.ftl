<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#birthdate_certificate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#original_birthdate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#join_date').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Add New Employee</center></h3>
<form name="addempInfo" id="addempInfo" action="${rc.contextPath}/addemp" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td class="name">Employee Number*</td>
	    			<td>
	    				<input type="text" id="emp_number" name="emp_number" class="form-control" value=""/>
	    			</td>
	    			<td class="name">Employee Name *</td>
	                <td><input type="text" id="emp_name" name="emp_name" class="form-control" value=""></td>
	    		</tr>
	        	<tr>
	        		<!--<td class="name">Employee Id</td>
	        		<td><input type="hidden" id="emp_id" name="emp_id" value=""/></td>-->
	        		<td class="name">Avator *</td>
	                <td><input type="text" id="emp_avator" name="emp_avator" class="form-control" value=""/></td>
	                <td class="name">Office Email *</td>
	                <td><input type="text" id="emp_email" name="emp_email" class="form-control" value=""></td>
	    		</tr>
	    		<tr>
	                <td class="name">Designation *</td>
	                <td>
		                <select name="emp_desig" id="emp_desig" class="form-control SlectBox">
		                	<option value="">Select Designation</option>
		                    <#list desig as des>
		                    	<option value="${des.id}">${des.datavalue}</option>
		                    </#list>    
		                </select>
	                </td>
	                <td class="name">Skype Id</td>
	                <td><input type="text" id="emp_skype" name="emp_skype" class="form-control" value=""></td>
	           	</tr>
	            <tr>
	                <td class="name">Blood Group</td>
	                <td>
	                	<select name="bloodgroup" id="bloodgroup" class="form-control SlectBox">
	                    	<option value="">Select Blood Group</option>
	                        <#list blood as blo>
	                        	<option value="${blo.id}">${blo.datavalue}</option>
	                        </#list>           
	                    </select>
	                </td>
	                <td class="name">Certificate Birth Date</td>
	                <td><input type="text" value="" name="birthdate_certificate" id="birthdate_certificate" placeholder="dd-mm-yyyy" class="form-control"></td>
	            </tr>
	            <tr>
	            	<td class="name">Original Birth Date</td>
	                <td><input type="text" value="" name="original_birthdate" id="original_birthdate" placeholder="dd-mm-yyyy" class="form-control"></td>
	                <td class="name">NID</td>
	                <td><input type="text" id="emp_nid" name="emp_nid" class="form-control" value=""></td>
	            </tr>
	            <tr>
	                <td class="name">Personal Email</td>
	                <td><input type="text" id="emp_pemail" name="emp_pemail" class="form-control" value=""></td>
	                <td class="name">Personal Contact *</td>
	                <td><input type="text" id="emp_contact" name="emp_contact" class="form-control" value=""></td>
	            </tr>
	            <tr>
	                <td class="name">User Type *</td>
	                <td>
	                	<select name="usertype" id="usertype" class="form-control SlectBox">
	                    	<option value="">Select User Type</option>
	                        <#list userType as utp>
	                        	<option value="${utp.id}">${utp.datavalue}</option>
	                        </#list>  
	                    </select>
	                </td>
	                <td class="name">Team </td>
	                <td>
	                	<select name="team" id="team" class="form-control SlectBox">
	                    	<option value="">Select Team</option>
	                    	<#list allTeam as team>
	                        <option value="${team.teamId}">${team.teamName!''}</option>
	                        </#list>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	            	<td class="name">Joining Date</td>
	                <td><input type="text" value="" name="join_date" id="join_date" placeholder="dd-mm-yyyy" class="form-control"></td>
	                <td colspan="2"></td>
	            </tr>
	            <tr>
	                <td class="name">Employee Password *</td>
	                <td><input type="password" id="emp_pass" name="emp_pass" class="form-control" value=""></td>
	                <td class="name">Repet Password *</td>
	                <td><input type="password" id="emp_pass1" name="emp_pass1" class="form-control" value=""></td>
	            </tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Save Employee" class="btn btn-primary" onclick="addEmployee('${rc.contextPath}')">Save</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
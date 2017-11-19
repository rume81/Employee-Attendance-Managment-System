<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#birthdate_certificate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#original_birthdate').datepicker({ dateFormat: 'dd-mm-yy' });
	
})
</script>
<h3><center>Update Watcher Information</center></h3>
<form name="watcherInfo" id="watcherInfo" action="${rc.contextPath}/updatewatcher" method="post">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	        		<td class="name">Watcher Number</td>
	        		<td>
	        			<input type="hidden" id="emp_id" name="emp_id" value="${empInfo.emp_id!''}"/>
	        			<input type="hidden" id="emp_number" name="emp_number" value="${empInfo.emp_number!''}"/>
	        			${empInfo.emp_number!''}
	        		</td>
	        		<td class="name">Watcher Name*</td>
	                <td><input type="text" id="emp_name" name="emp_name" class="form-control" value="${empInfo.employee_name!''}"></td>
	    		</tr>
	    		<tr>
	            	<td class="name">User Name</td>
	                <td><input type="hidden" id="emp_avator" name="emp_avator" value="${empInfo.avator!''}"/>${empInfo.avator!''}</td>
	                <td class="name">Office Email*</td>
	                <td><input type="text" id="emp_email" name="emp_email" class="form-control" value="${empInfo.email!''}"></td>
	           	</tr>
	            <tr>
	                <td class="name">Designation</td>
	                <td><input type="text" id="emp_desig" name="emp_desig" class="form-control" value="${empInfo.designation!''}"></td>
	                <td class="name">Skype Id</td>
	                <td><input type="text" id="emp_skype" name="emp_skype" class="form-control" value="${empInfo.skype_id!''}"></td>
	            </tr>
	            <!--<tr>
	                <td class="name">Blood Group</td>
	                <td>
	                	<select name="bloodgroup" id="bloodgroup" class="form-control SlectBox">
	                    	<option value="">Select Blood Group</option>
	                        <option value="O+" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "O+"> selected="selected" </#if>>O+</option>
	                        <option value="O-" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "O-"> selected="selected" </#if>>O-</option>
	                        <option value="A+" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "A+"> selected="selected" </#if>>A+</option>
	                        <option value="A-" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "A-"> selected="selected" </#if>>A-</option>
	                        <option value="B+" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "B+"> selected="selected" </#if>>B+</option>
	                        <option value="B-" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "B-"> selected="selected" </#if>>B-</option>
	                        <option value="AB+" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "AB+"> selected="selected" </#if>>O+</option>
	                        <option value="AB-" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == "AB-"> selected="selected" </#if>>AB-</option>          
	                    </select>
	                </td>
	                <td class="name">Certificate Birth Date</td>
	                <td><input type="text" value="${empInfo.birthdate_certificate!''}" name="birthdate_certificate" id="birthdate_certificate" placeholder="dd-mm-yyyy" class="form-control"></td>
	            </tr>
	            <tr>
	                <td class="name">Original Birth Date</td>
	                <td><input type="text" value="${empInfo.birthdate_real!''}" name="original_birthdate" id="original_birthdate" placeholder="dd-mm-yyyy" class="form-control"></td>
	                <td class="name">NID</td>
	                <td><input type="text" id="emp_nid" name="emp_nid" class="form-control" value="${empInfo.nid!''}"></td>
	            </tr>-->
	            <tr>
	                <!--<td class="name">Personal Email</td>
	                <td><input type="text" id="emp_pemail" name="emp_pemail" class="form-control" value="${empInfo.personal_email!''}"></td>-->
	                <td class="name">Personal Contact</td>
	                <td><input type="text" id="emp_contact" name="emp_contact" class="form-control" value="${empInfo.mobile!''}">
	                <input type="hidden" id="usertype" name="usertype" value="${empInfo.usertype!''}"/>
	                </td>
	            </tr>
	            </tr>
	            <!--<tr>
	                <td class="name">User Type</td>
	                <td>
	                	<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	<select name="usertype" id="usertype" class="form-control SlectBox">
	                    	<option value="">Select User Type</option>
	                        <option value="Admin" <#if empInfo.usertype == "Admin"> selected="selected" </#if>>Admin</option>
	                        <option value="Admin" <#if empInfo.usertype == "Talent Manager"> selected="selected" </#if>>Talent Manager</option>
	                        <option value="User" <#if empInfo.usertype == "User"> selected="selected" </#if>>User</option>    
	                    </select>
	                    <#else>
	                    	<input type="hidden" id="usertype" name="usertype" value="${empInfo.usertype!''}"/>
	                    	${empInfo.usertype!''}
	                    </#if>
	                </td>
	            </tr>-->
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Update Watcher" class="btn btn-primary" onclick="updateWatcher('${rc.contextPath}')">Update</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
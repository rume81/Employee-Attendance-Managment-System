<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#birthdate_certificate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#original_birthdate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#join_date').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Update Employee Information</center></h3>
<form name="empInfo" id="empInfo" action="${rc.contextPath}/updateemp" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	        		<td class="name">Employee Number</td>
	        		<td>
	        			<input type="hidden" id="emp_id" name="emp_id" value="${empInfo.emp_id!''}"/>
	        			<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	        				<input type="text" id="emp_number" name="emp_number" class="form-control" value="${empInfo.emp_number!''}"/>
	        			<#else>
	        				<input type="hidden" id="emp_number" name="emp_number" value="${empInfo.emp_number!''}"/>
	        				${empInfo.emp_number!''}
	        			</#if>
	        		</td>
	        		<td class="name">Name*</td>
	                <td><input type="text" id="emp_name" name="emp_name" class="form-control" value="${empInfo.employee_name!''}"></td>
	    		</tr>
	    		<tr>
	            	<td class="name">Avator</td>
	                <td><input type="hidden" id="emp_avator" name="emp_avator" value="${empInfo.avator!''}"/>${empInfo.avator!''}</td>
	                <td class="name">Office Email*</td>
	                <td><input type="text" id="emp_email" name="emp_email" class="form-control" value="${empInfo.email!''}"></td>
	           	</tr>
	            <tr>
	                <td class="name">Designation*</td>
	                <td>
	                <#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	<select name="emp_desig" id="emp_desig" class="form-control SlectBox">
	                    	<option value="">Select Designation</option>
	                        <#list desig as des>
	                        	<option value="${des.id}" <#if empInfo.designation?? && empInfo.designation == des.datavalue> selected="selected" </#if>>${des.datavalue}</option>
	                        </#list>    
	                    </select>
	                <#else>
	                	<input type="hidden" id="emp_desig" name="emp_desig" value="<#list desig as des><#if des.datavalue==empInfo.designation>${des.id}<#break></#if></#list>"/>
	                    ${empInfo.designation!''}
	                </#if>
	                
	                </td>
	                <td class="name">Skype Id</td>
	                <td><input type="text" id="emp_skype" name="emp_skype" class="form-control" value="${empInfo.skype_id!''}"></td>
	            </tr>
	            <tr>
	                <td class="name">Blood Group</td>
	                <td>
	                	<select name="bloodgroup" id="bloodgroup" class="form-control SlectBox">
	                    	<option value="">Select Blood Group</option>
	                    	<#list blood as blo>
	                        	<option value="${blo.id}" <#if empInfo.bloodgroup?? && empInfo.bloodgroup == blo.datavalue> selected="selected" </#if>>${blo.datavalue}</option>
	                        </#list>          
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
	            </tr>
	            <tr>
	                <td class="name">Personal Email</td>
	                <td><input type="text" id="emp_pemail" name="emp_pemail" class="form-control" value="${empInfo.personal_email!''}"></td>
	                <td class="name">Personal Contact*</td>
	                <td><input type="text" id="emp_contact" name="emp_contact" class="form-control" value="${empInfo.mobile!''}"></td>
	            </tr>
	            </tr>
	            <tr>
	                <td class="name">
	                <#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	User Type*
	                <#else>
	                	User Type
	                </#if>
	                </td>
	                <td>
	                	<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	<select name="usertype" id="usertype" class="form-control SlectBox">
	                    	<option value="">Select User Type</option>
	                        <#list userType as utp>
	                        	<option value="${utp.id}" <#if empInfo.usertype == utp.datavalue> selected="selected" </#if>>${utp.datavalue}</option>
	                        </#list>    
	                    </select>
	                    <#else>
	                    	<input type="hidden" id="usertype" name="usertype" value="<#list userType as utp><#if utp.datavalue==empInfo.usertype>${utp.id}<#break></#if></#list>"/>
	                    	${empInfo.usertype!''}
	                    </#if>
	                </td>
	                <td class="name">Team </td>
	                <td>
	                	<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	<select name="team" id="team" class="form-control SlectBox">
	                    	<option value="">Select Team</option>
	                    	<#list allTeam as team>
	                        <option value="${team.teamId}" <#if empInfo.teamId == team.teamId> selected="selected" </#if>>${team.teamName!''}</option>
	                        </#list>
	                    </select>
	                    <#else>
	                    	<input type="hidden" id="team" name="team" value="${empInfo.teamId!''}"/>
	                    	<#list allTeam as team>
		                    	<#if empInfo.teamId == team.teamId>
		                    		${team.teamName!''}
		                    	</#if>
	                    	</#list>
	                    </#if>
	                </td>
	            </tr>
	            <tr>
	            	<td class="name">Joining Date</td>
	                <td>
	                	<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                	<input type="text" value="${empInfo.join_date!''}" name="join_date" id="join_date" placeholder="dd-mm-yyyy" class="form-control"></td>
	                	<#else>
	                	<input type="hidden" id="join_date" name="join_date" value="${empInfo.join_date!''}"/>${empInfo.join_date!''}
	                	</#if>
	                <td colspan="2"></td>
	            </tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Update Employee" class="btn btn-primary" onclick="updateEmployee('${rc.contextPath}')">Update</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
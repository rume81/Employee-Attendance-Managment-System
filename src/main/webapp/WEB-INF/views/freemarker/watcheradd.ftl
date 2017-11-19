<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#birthdate_certificate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#original_birthdate').datepicker({ dateFormat: 'dd-mm-yy' });
	
})
</script>
<h3><center>Add Watcher</center></h3>
<form name="addwatcherInfo" id="addwatcherInfo" action="${rc.contextPath}/addwatcher" method="post">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td class="name">Watcher Number*</td>
	    			<td>
	    				<input type="text" id="emp_number" name="emp_number" class="form-control" value=""/>
	    			</td>
	    		</tr>
	        	<tr>
	        		<!--<td class="name">Employee Id</td>
	        		<td><input type="hidden" id="emp_id" name="emp_id" value=""/></td>-->
	        		<td class="name">Watcher Name *</td>
	                <td><input type="text" id="emp_name" name="emp_name" class="form-control" value=""></td>
	                <td class="name">User Name *</td>
	                <td><input type="text" id="emp_avator" name="emp_avator" class="form-control" value=""/></td>
	    		</tr>
	    		<tr>
	                <td class="name">Office Email *</td>
	                <td><input type="text" id="emp_email" name="emp_email" class="form-control" value=""></td>
	                <td class="name">Designation</td>
	                <td><input type="text" id="emp_desig" name="emp_desig" class="form-control" value=""></td>
	           	</tr>
	            <tr>
	                <td class="name">Skype Id</td>
	                <td><input type="text" id="emp_skype" name="emp_skype" class="form-control" value=""></td>
	                <td class="name">Personal Contact</td>
	                <td><input type="text" id="emp_contact" name="emp_contact" class="form-control" value="">
	                	<input type="hidden" id="usertype" name="usertype" value="Watcher"/>
	                </td>
	            </tr>
	            <!--<tr>
	            	<td class="name">Certificate Birth Date</td>
	                <td><input type="text" value="" name="birthdate_certificate" id="birthdate_certificate" placeholder="dd-mm-yyyy" class="form-control"></td>
	                <td class="name">Original Birth Date</td>
	                <td><input type="text" value="" name="original_birthdate" id="original_birthdate" placeholder="dd-mm-yyyy" class="form-control"></td>
	            </tr>
	            <tr>
	                <td class="name">NID</td>
	                <td><input type="text" id="emp_nid" name="emp_nid" class="form-control" value=""></td>
	                <td class="name">Personal Email</td>
	                <td><input type="text" id="emp_pemail" name="emp_pemail" class="form-control" value=""></td>
	            </tr>-->
	            <!--<tr>
	                <td class="name">Blood Group</td>
	                <td>
	                	<select name="bloodgroup" id="bloodgroup" class="form-control SlectBox">
	                    	<option value="">Select Blood Group</option>
	                        <option value="O+">O+</option>
	                        <option value="O-">O-</option>
	                        <option value="A+">A+</option>
	                        <option value="A-">A-</option>
	                        <option value="B+">B+</option>
	                        <option value="B-">B-</option>
	                        <option value="AB+">AB+</option>
	                        <option value="AB-">AB-</option>          
	                    </select>
	                </td>
	                <td class="name">User Type *</td>
	                <td>
	                	<select name="usertype" id="usertype" class="form-control SlectBox">
	                    	<option value="">Select User Type</option>
	                        <option value="Admin">Admin</option>
	                        <option value="User">User</option> 
	                    </select>
	                </td>
	            </tr>-->
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
        		<button type="button" title="Save Employee" class="btn btn-primary" onclick="addWatcherInfo('${rc.contextPath}')">Save</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
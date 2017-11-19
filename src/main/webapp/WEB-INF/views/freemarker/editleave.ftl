<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#fdate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#tdate').datepicker({ dateFormat: 'dd-mm-yy' });
	
	var text_max = 2000;
    $('#textarea_feedback').html(text_max + ' characters remaining');

    $('#reason').keyup(function() {
        var text_length = $('#reason').val().length;
        var text_remaining = text_max - text_length;

        $('#textarea_feedback').html(text_remaining + ' characters remaining');
    });
    
    $('#copydate').hide();
    $('#fdate').change(function() {
    	var t_length = $('#fdate').val().length;
    	if(t_length>0){
    		$('#copydate').show();
    	}
    });
});
</script>
<h3><center>Edit Leave Request</center></h3>
<form name="leaveInfo" id="leaveInfo" action="${rc.contextPath}/updateLeave" method="post">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		
	    		<tr>
	    			<td class="name">
	    				Employee Name
	    				
	    			</td>
	    			<td>
	                    ${curEmp.employee_name}(${curEmp.avator})
	                    <input type="hidden" id="leave_id" name="leave_id" value="${leave.id!''}"/>
	    				<input type="hidden" id="emp_id" name="emp_id" value="${curEmp.emp_id!''}"/>
	    			</td>
	    			<td class="name">Leave Type</td>
	    			<td>
	                    ${leave.leavetype.leavetype!''}
	                    <input type="hidden" name="leavetype" id="leavetype" value="${leave.leavetype.id!''}"/>
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td class="name">Employee Number</td>
	    			<td>
	    				<input type="hidden" id="emp_number" name="emp_number" value="${emp.emp_number!''}"/>${emp.emp_number!''}
	    			</td>
	    			<td class="name">Number Of Days</td>
	    			<td><input type="text" id="days" name="days" class="form-control" value="${leave.days!''}" readonly></td>
	    		</tr>
	    		<tr>
	    			<td class="name">Leave From Date*</td>
	                <td><input type="text" id="fdate" name="fdate" class="form-control" value="${leave.fdate!''}">
	                <div id="copydate"><input type="checkbox" id="oneday" name="oneday" onclick="copyValue(this);" value="1"> One day leave</div>
	                </td>
	                <td class="name">Leave To Date *</td>
	                <td><input type="text" id="tdate" name="tdate" class="form-control" value="${leave.tdate!''}"></td>
	    		</tr>
	    		<tr>
	    			<td class="name">Leave Day List</td>
	    			<td colspan="2">
	    				<div id='leavelist' style='overflow:auto; width:300px;height:70px;'>
		    				<table class="table-responsive">
		    					<tr>
		    						<td>
		    							
		    						</td>
		    					</tr>
		    				</table>
	    				</div>
	    			</td>
	    			<td>
	    				<button type="button" title="Leave date verify" class="btn btn-primary" onclick="noOfLeaveCalculation('${rc.contextPath}',1)">Leave Validate</button>
	    			</td>
	    		</tr>
	        	<tr>
	        		<td class="name">Reason *</td>
	                <td colspan="3">
	                	<textarea rows="4" cols="50" id="reason" name="reason" class="form-control" maxlength="2000" readonly>${leave.reason!''}</textarea>
	                	<div id="textarea_feedback"></div>
	                </td>
	    		</tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<div id="lazy" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        		<button id="btnedit" type="button" title="Edit Leave request" class="btn btn-primary" onclick="UpdateLeave('${rc.contextPath}')">Edit</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<h3><center>Manage Leave</center></h3>
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
<form name="batchleave" id="batchleave" action="${rc.contextPath}/addbatchleave" method="post">
<div class="col-xs-12 no_padding">
    <table class="table table-bordered table-responsive">
        <tr>
            <td class="static_data" colspan="4">
            Employee List<br>
            <input type="checkbox" name="" id="chkAll" onClick="toggle(this)" value="All"> Select All
            </td>
        </tr>
        <tr>
    		<td colspan="2">
    			<table class="table-bordered table-responsive">	
    			<tr>
    				<td>Name</td>
    				<td>Casual Leave</td>
    				<td>Sick Leave</td>
    			</tr>		
    			<#list allEmp1 as employee>
    				<tr>
    					<td><input type="checkbox" name="chk" onClick="toggleselect()" id="chk${employee.emp.emp_id}" value="${employee.emp.emp_id}"> ${employee.emp.employee_name!''}(${employee.emp.avator!''})</td>
    					<#list employee.quota as quot>
    					<td>
    						<#if quot.type_id == 1>
    						<input type="radio" name="${quot.emp_id}" value="1" checked>
    						${quot.getAvailable()!''}
    						<#else>
    						<input type="radio" name="${quot.emp_id}" value="2">
    						${quot.getAvailable()!''}
    						</#if>
    					</td>
    					</#list>
    				</tr>
    			</#list>
    			</table>
    		</td>
    		<td colspan="2">
    			<table class="table-bordered table-responsive">	
    			<tr>
    				<td>Name</td>
    				<td>Casual Leave</td>
    				<td>Sick Leave</td>
    			</tr>	
    			<#list allEmp2 as employee>
    				<tr>
    					<td><input type="checkbox" name="chk" onClick="toggleselect()" id="chk${employee.emp.emp_id}" value="${employee.emp.emp_id}"> ${employee.emp.employee_name!''}(${employee.emp.avator!''})</td>
    					<#list employee.quota as quot>
    					<td>
    						<#if quot.type_id == 1>
    						<input type="radio" name="${quot.emp_id}" value="1" checked>
    						${quot.getAvailable()!''}
    						<#else>
    						<input type="radio" name="${quot.emp_id}" value="2">
    						${quot.getAvailable()!''}
    						</#if>
    					</td>
    					</#list>
    				</tr>
    			</#list>
    			</table>
    		</td>    	
        </tr>
        <tr>
	        <td>Quota Year</td>
	        <td>
	        	<select name="year" id="year" class="form-control SlectBox" onChange="getQuota('${rc.contextPath}')">
                	<option value="">Select Year</option>
                	<#list quotaYear as y>
                    <option value="${y.year}" <#if selectYear==y.year>selected="selected"</#if>>${y.year}</option>
                    </#list>          
                </select>
	        </td>	
	        <td class="name">Number Of Days</td>
	    	<td><input type="text" id="days" name="days" class="form-control" value="" readonly></td>				        
	    </tr>
	    <tr>
			<td class="name">Leave From Date*</td>
            <td><input type="text" id="fdate" name="fdate" class="form-control" value="">
            <div id="copydate"><input type="checkbox" id="oneday" name="oneday" onclick="copyValue(this);" value="1"> One day leave</div>
            </td>
            <td class="name">Leave To Date *</td>
            <td><input type="text" id="tdate" name="tdate" class="form-control" value=""></td>
		</tr>
		<tr>
			<td class="name"></td>
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
			<td colspan="2">
				<button type="button" title="Leave date verify" class="btn btn-primary" onclick="noOfDaysCalculation('${rc.contextPath}')">Leave Validate</button>
			</td>
		</tr>
		<tr>
    		<td class="name">Reason *</td>
            <td colspan="3">
            	<textarea rows="4" cols="50" id="reason" name="reason" class="form-control" maxlength="2000"></textarea>
            	<div id="textarea_feedback"></div>
            </td>
		</tr>
		<tr>
			<td colspan="4">
	    		<div id="approverList">
		    		<input type="hidden" id="appsize" name="appsize" value="${approver?size}"/>
		    		<#if (approver?size > 0)>
		    		<table class="table table-responsive">
			    		<#list 1..approver?size as i>
				    		<tr id="app${i}">
				                <td class="name" colspan="2">
				                	<#if i==1>
				                		First Approvar *
				                	<#elseif i==2>
				                		Second Approvar
				                	<#elseif i==3>
				                		Thierd Approvar
				                	<#elseif i==4>
				                		Fourth Approvar
				                	<#else>
				                		Fifth Approvar
				                	</#if>
				                </td>
				                <td colspan="2">
				                	<select name="approvar${i}" id="approvar${i}" class="form-control SlectBox" onchange="checkDuplicate(${i},${approver?size});">
				                    	<option value="">
				                    		<#if i==1>
						                		Select First Approvar
						                	<#elseif i==2>
						                		Select Second Approvar
						                	<#elseif i==3>
						                		Select Thierd Approvar
						                	<#elseif i==4>
						                		Select Fourth Approvar
						                	<#else>
						                		Select Fifth Approvar
						                	</#if>
				                    	</option>
				                    	<#list approver as apr>
				                			<#list allEmp as appemp>
				                				<#if apr.approver_id==appemp.emp_id>
				                					<option value="${appemp.emp_id}">${appemp.employee_name}(${appemp.avator})</option>
				                					<#break>
				                				</#if>
				                			</#list>
				                		</#list>          
				                    </select>
				                    <!--<input type="hidden" id="approverSize" name="approverSize" value="approver?size"/>-->
				                </td>
				            </tr>
				        </#list>
			        </table>
			        </#if>
	            </div>
	    	</td>
        </tr>
    </table>
</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<div id="lazy" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        		<button id="applied" type="button" title="Leave request" class="btn btn-primary" onclick="addBatchLeave('${rc.contextPath}')">Applied</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>

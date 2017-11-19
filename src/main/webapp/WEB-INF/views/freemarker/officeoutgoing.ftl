<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#date').datepicker({ dateFormat: 'dd-mm-yy' });
	   
	var text_max = 2000;
    $('#textarea_feedback').html(text_max + ' characters remaining');

    $('#reason').keyup(function() {
        var text_length = $('#reason').val().length;
        var text_remaining = text_max - text_length;

        $('#textarea_feedback').html(text_remaining + ' characters remaining');
    });
});
</script>
<h3><center>Office Outgoing Request</center></h3>
<form name="outgoingInfo" id="outgoingInfo" action="${rc.contextPath}/addofficeoutgoingrequest" method="post">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		
	    		<tr>
	    			<td class="name">
	    				<#if emp.usertype == 'Admin' || emp.usertype == 'Talent Manager'>
	    				Employee Name *
	    				<#else>
	    				Employee Name
	    				</#if>
	    			</td>
	    			<td>
	    				<#if emp.usertype == 'Admin'  || emp.usertype == 'Talent Manager'>
	    				<select name="emp_id" id="emp_id" class="form-control SlectBox" onChange="getApprovar('${rc.contextPath}',this,'officeoutgoing')">
	                    	<option value="">Select Employee</option>
	                    	<#list aEmp as appemp>
	                        	<option value="${appemp.emp_id}" <#if emp.emp_id == appemp.emp_id>selected="selected"</#if>>${appemp.employee_name}(${appemp.avator})</option>
	                        </#list>          
	                    </select>
	                    <#else>
	                    	${emp.employee_name}(${emp.avator})
	    					<input type="hidden" id="emp_id" name="emp_id" value="${emp.emp_id!''}"/>
	    				</#if>
	    			</td>
	    			<td class="name"></td>
	    			<td>
	    				
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="name">Employee Number</td>
	    			<td>
	    				<input type="hidden" id="emp_number" name="emp_number" value="${emp.emp_number!''}"/>${emp.emp_number!''}
	    			</td>
	    			<td class="name">Date*</td>
	    			<td><input type="text" id="date" name="date" class="form-control" value=""></td>
	    		</tr>
	    		<tr>
	    			<td class="name">From Time *</td>
	                <td>
	                	<select name="fromtime" id="fromtime" class="form-control SlectBox">
	                    	<option value=""></option>
	                        <option value="09:00 AM">09:00 AM</option>
	                        <option value="09:30 AM">09:30 AM</option>
	                        <option value="10:00 AM">10:00 AM</option>
	                        <option value="10:30 AM">10:30 AM</option>
	                        <option value="11:00 AM">11:00 AM</option>
	                        <option value="11:30 AM">11:30 AM</option>
	                        <option value="12:00 PM">12:00 PM</option>
	                        <option value="12:30 PM">12:30 PM</option>
	                        <option value="01:00 PM">01:00 PM</option>
	                        <option value="01:30 PM">01:30 PM</option>
	                        <option value="02:00 PM">02:00 PM</option>
	                        <option value="02:30 PM">02:30 PM</option>
	                        <option value="03:00 PM">03:00 PM</option>
	                        <option value="03:30 PM">03:30 PM</option>
	                        <option value="04:00 PM">04:00 PM</option>
	                        <option value="04:30 PM">04:30 PM</option>
	                        <option value="05:00 PM">05:00 PM</option>
	                        <option value="05:30 PM">05:30 PM</option>
	                        <option value="06:00 PM">06:00 PM</option>        
	                    </select>
	                </td>
	                <td class="name">To Time *</td>
	                <td>
	                	<select name="totime" id="totime" class="form-control SlectBox">
	                    	<option value=""></option>
	                        <option value="09:00 AM">09:00 AM</option>
	                        <option value="09:30 AM">09:30 AM</option>
	                        <option value="10:00 AM">10:00 AM</option>
	                        <option value="10:30 AM">10:30 AM</option>
	                        <option value="11:00 AM">11:00 AM</option>
	                        <option value="11:30 AM">11:30 AM</option>
	                        <option value="12:00 PM">12:00 PM</option>
	                        <option value="12:30 PM">12:30 PM</option>
	                        <option value="01:00 PM">01:00 PM</option>
	                        <option value="01:30 PM">01:30 PM</option>
	                        <option value="02:00 PM">02:00 PM</option>
	                        <option value="02:30 PM">02:30 PM</option>
	                        <option value="03:00 PM">03:00 PM</option>
	                        <option value="03:30 PM">03:30 PM</option>
	                        <option value="04:00 PM">04:00 PM</option>
	                        <option value="04:30 PM">04:30 PM</option>
	                        <option value="05:00 PM">05:00 PM</option>
	                        <option value="05:30 PM">05:30 PM</option>
	                        <option value="06:00 PM">06:00 PM</option>        
	                    </select>
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
	    			<td colspan="2">
			    		<div id="approverList">
				    		<input type="hidden" id="appsize" name="appsize" value="${approver?size}"/>
				    		<#if (approver?size > 0)>
				    		<table class="table table-responsive">
					    		<#list 1..approver?size as i>
						    		<tr id="app${i}">
						                <td class="name">
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
						                <td>
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
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<div id="lazy" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        		<button id="applied" type="button" title="Leave request" class="btn btn-primary" onclick="addOfficeOutgoing('${rc.contextPath}')">Applied</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
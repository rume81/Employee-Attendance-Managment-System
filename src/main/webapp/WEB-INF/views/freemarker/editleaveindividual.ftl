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
<form name="editleaveInfo" id="editleaveInfo" action="${rc.contextPath}/updateLeaveIndividual" method="post">
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
	    		<#if leave.leavequota.year!="null">
	    		<tr>
	    			<td colspan="4">
	    			<table class="table table-bordered table-responsive">
			        	<tr>
					        <td colspan="2">Quota Year</td>
					        <td colspan="3">
					        	
			                    ${leave.getLeavequota().getYear()!''}
			                    <input type="hidden" name="year" id="year" value="${leave.getLeavequota().getYear()!''}"/>
					        </td>					        
					    </tr>
					    <tr>
					    	<td colspan="5">
							    <div id="quotadisplay">
								    <table class="table table-bordered table-responsive" style="background-color:#aed6f1">
								    	<tr>
								    		<td colspan="2">Leave Type</td>
								    		<td>Leave Quota</td>
					        				<td>Leave Used</td>
					        				<td>Expire Date</td>
								    	</tr>							    
							        	<#list allleavetype as type>
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
			    			</td>
			    		</tr>
			        </table>
			        </td>
	    		</tr>
	    		</#if>
	    		<tr>
	    			<td class="name">Employee Number</td>
	    			<td>
	    				<input type="hidden" id="emp_number" name="emp_number" value="${emp.emp_number!''}"/>${emp.emp_number!''}
	    			</td>
	    			<td class="name">Number Of Days</td>
	    			<td>
	    				<input type="text" id="days" name="days" class="form-control" value="${leave.days?c}" readonly>
	    				<input type="hidden" id="prevdays" name="prevdays" value="${leave.days?c}"/>
	    			</td>
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
								<#list nod as n>
								<tr>
									<td>
										* ${n!''}
									</td>
								</tr>
								</#list>
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
	                	<textarea rows="4" cols="50" id="reason" name="reason" class="form-control" maxlength="2000">${leave.reason!''}</textarea>
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
						                					<#if i==1>
						                						<option value="${appemp.emp_id}" <#if leave.approvar1==appemp.emp_id>selected</#if>>${appemp.employee_name}(${appemp.avator})</option>
						                					<#elseif i==2>
						                						<option value="${appemp.emp_id}" <#if leave.approvar2==appemp.emp_id>selected</#if>>${appemp.employee_name}(${appemp.avator})</option>
						                					<#elseif i==3>
						                						<option value="${appemp.emp_id}" <#if leave.approvar3==appemp.emp_id>selected</#if>>${appemp.employee_name}(${appemp.avator})</option>
						                					<#elseif i==4>
						                						<option value="${appemp.emp_id}" <#if leave.approvar4==appemp.emp_id>selected</#if>>${appemp.employee_name}(${appemp.avator})</option>
						                					<#else>
						                						<option value="${appemp.emp_id}" <#if leave.approvar5==appemp.emp_id>selected</#if>>${appemp.employee_name}(${appemp.avator})</option>
						                					</#if>
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
        		<button id="btnedit" type="button" title="Edit Leave request" class="btn btn-primary" onclick="UpdateLeaveIndivitual('${rc.contextPath}')">Edit</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#fdate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#tdate').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Employee Leave Request</center></h3>
<form name="leaveInfo" id="leaveInfo" action="${rc.contextPath}/addleaverequest" method="post">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td class="name">
	    				Employee Name
	    			</td>
	    			<td>
	    				${reqemp.employee_name!''}(${reqemp.avator!''})
	    			</td>
	    			<td class="name">Leave Type</td>
	    			<td>
	    				${leave.leavetype.leavetype!''}
	    			</td>
	    		</tr>
	    		<#if leave.leavequota.year!="null">
	    		<tr>
	    			<td colspan="4">
	    			<table class="table table-bordered table-responsive">
			        	<tr>
					        <td colspan="2">Quota Year</td>
					        <td colspan="3">
					        	<input type="hidden" id="year" name="year" value="${leave.leavequota.year!''}">
			                    ${leave.leavequota.year!''}
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
	    				${reqemp.emp_number!''}
	    			</td>
	    			<td class="name">Number Of Days</td>
	    			<td>
	    				${leave.days!''}
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="name">Leave From Date</td>
	                <td>
	                	${leave.fdate!''}
	                </td>
	                <td class="name">Leave To Date</td>
	                <td>
	                	${leave.tdate!''}
	                </td>
	    		</tr>
	    		<tr>
	    			<td class="name">Leave Day List</td>
	    			<td colspan="3">
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
	    		</tr>
	        	<tr>
	        		<td class="name">Reason</td>
	                <td colspan="3">
	                	${leave.reason!''}
	                </td>
	    		</tr>
	    		<tr>
	        		<td class="name">Status</td>
	                <td>
	                	<#if leave.status==0>Not Approved<#elseif leave.status==1>Rejected<#elseif  leave.status==2>Approved</#if>
	                </td>
	                <td colspan="2"></td>
	    		</tr>
	    		<tr>
	    			<td colspan="4">
	                	<table  class="table table-bordered table-responsive">
	                		<#list approvState as aps>
	                		<tr style="background-color:#e5e8e8">
	                			<td>
	                				Approver ${aps?counter}
	                			</td>
	                			<td>
	                				${aps!''}
	                			</td>
	                		</tr>
	                		<tr>
	                			<td colspan="2">
	                				<#list approvCommants as apc>
	                					<#if aps?counter == apc?counter>
	                						Comments:<font color="green">${apc!''}</font>
	                						<#break>
	                					</#if>
	                				</#list>
	                			</td>
	                		</tr>
	                		</#list>
	                	</table>
	                </td>
	    		</tr>
	    		<tr>
	    			<td  colspan="4">
	    				<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="goBack()">Back</button>
	    			</td>
	    		</tr>
	    		
	        </table>
		</div>
	</div>
</form>
<#if call==1>
<#list effective as lv>
	<#if lv.id==leave.id>
		<div class="col-xs-3">
		  	<table class="table table-responsive">
				<tr>
		        	<td>
		        		<button type="button" title="Leave request" class="btn btn-primary" data-toggle="modal" data-target="#pA">Approve</button>
		        	</td>
		        	<td>
		        		<button type="button" title="Leave request" class="btn btn-primary" data-toggle="modal" data-target="#pR">Reject</button>
		        	</td>
		        	
		        </tr>
			</table>
		</div>
		<div class="modal fade" id="pA" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		    <div class="modal-dialog" role="document">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		                <h4 class="modal-title" id="myModalLabel"></h4>
		            </div>
		            <div class="modal-body">
		                Approval comments <input type="text" class="form-control" value="" placeholder="" id="approval_comments${leave.id}" name="approval_comments">
		            </div>
		            <div class="modal-footer">
		            	<div id="lazyap${leave.id}" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
		                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		                <button type="button" id="approved${leave.id}" class="btn btn-primary" onclick="approveRejectLeave('${rc.contextPath}','${leave.id}',1)">Approve</button>
		            </div>
		        </div>
		    </div>
		</div>
		<div class="modal fade" id="pR" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		    <div class="modal-dialog" role="document">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		                <h4 class="modal-title" id="myModalLabel"></h4>
		            </div>
		            <div class="modal-body">
		                Rejecting reason <input type="text" class="form-control" value="" placeholder="" id="reject_comments${leave.id}" name="reject"> 
		            </div>
		            <div class="modal-footer">
		                <div id="lazyre${leave.id}" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
		                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		                <button type="button" id="reject${leave.id}" class="btn btn-primary" onclick="approveRejectLeave('${rc.contextPath}','${leave.id}',2)">Reject</button>
		            </div>
		        </div>
		    </div>
		</div>
		<#break>
	</#if>
</#list>
</#if>
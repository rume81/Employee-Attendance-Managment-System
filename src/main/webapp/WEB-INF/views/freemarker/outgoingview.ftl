<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<h3><center>Employee Office Outgoing Request</center></h3>
<form name="outgoingInfo" id="outgoingInfo">
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
	    			<td class="name"></td>
	    			<td></td>
	    		</tr>    		
	    		<tr>
	    			<td class="name">Employee Number</td>
	    			<td>
	    				${reqemp.emp_number!''}
	    			</td>
	    			<td class="name">Date</td>
	    			<td>
	    				${outgoing.date?string["dd-MM-yyyy"]}
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="name">Outgoing From Time</td>
	                <td>
	                	${outgoing.ftime!''}
	                </td>
	                <td class="name">Outgoing To Time</td>
	                <td>
	                	${outgoing.ttime!''}
	                </td>
	    		</tr>
	        	<tr>
	        		<td class="name">Reason</td>
	                <td colspan="3">
	                	${outgoing.reason!''}
	                </td>
	    		</tr>
	    		<tr>
	        		<td class="name">Status</td>
	                <td>
	                	<#if outgoing.status==0>Not Approved<#elseif outgoing.status==1>Rejected<#elseif  outgoing.status==2>Approved</#if>
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
	<#if lv.id==outgoing.id>
		<div class="col-xs-3">
		  	<table class="table table-responsive">
				<tr>
		        	<td>
		        		<button type="button" title="Outgoing request" class="btn btn-primary" data-toggle="modal" data-target="#pA">Approve</button>
		        	</td>
		        	<td>
		        		<button type="button" title="Outgoing request" class="btn btn-primary" data-toggle="modal" data-target="#pR">Reject</button>
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
		                Approval comments <input type="text" class="form-control" value="" placeholder="" id="approval_comments${outgoing.id}" name="approval_comments">
		            </div>
		            <div class="modal-footer">
		            	<div id="lazyap${outgoing.id}" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
		                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		                <button type="button" id="approved${outgoing.id}" class="btn btn-primary" onclick="approveRejectOutgoing('${rc.contextPath}','${outgoing.id}',1)">Approve</button>
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
		                Rejecting reason <input type="text" class="form-control" value="" placeholder="" id="reject_comments${outgoing.id}" name="reject"> 
		            </div>
		            <div class="modal-footer">
		                <div id="lazyre${outgoing.id}" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
		                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		                <button type="button" id="reject${outgoing.id}" class="btn btn-primary" onclick="approveRejectOutgoing('${rc.contextPath}','${outgoing.id}',2)">Reject</button>
		            </div>
		        </div>
		    </div>
		</div>
		<#break>
	</#if>
</#list>
</#if>
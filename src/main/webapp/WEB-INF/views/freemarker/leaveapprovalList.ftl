<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable();
})
</script>
<h3><center>Leave Approval Request</center></h3>
	<div class="col-xs-12">
		<table id="empList" class="display" cellspacing="0" width="100%">
			<thead>
	            <tr>
	                <th>From Date</th>
	                <th>To Date</th>
	                <th>Apply Date</th>
	                <th>Employee Name</th>
	                <th>Days</th>
	                <th>Leave Type</th>
	                <!--<th>Status</th>-->
	                <th>Action</th>
	            </tr>
	        </thead>
	        <tfoot>
	            <tr>
	                <th>From Date</th>
	                <th>To Date</th>
	                <th>Apply Date</th>
	                <th>Employee Name</th>
	                <th>Days</th>
	                <th>Leave Type</th>
	                <!--<th>Status</th>-->
	                <th>Action</th>
	            </tr>
	        </tfoot>
	        <tbody>
	        	<#list leaves as leave>
		        	<tr>
		                <td>${leave.sortdate?date}</td>
		                <td>${leave.tdate!''}</td>
		                <td><#if leave.apllieddate == '01-01-1970'><#else>${leave.apllieddate!''}</#if></td>
		                <td>
		                	<#list emps as emp>
		                		<#if emp.emp_id == leave.emp_id>
		                			${emp.employee_name!''}
		                			<#break>
		                		</#if>
		                	</#list>
		                </td>
		                <td>${leave.days!''}</td>
		                <td>${leave.leavetype.leavetype!''}</td>
		                <!--<td><#if leave.status==0>Not Approved<#elseif leave.status==1>Rejected<#else>Approved</#if></td>-->
		                <td>
		                    <div class="btn-group">
		                        <button type="button" title="View Leave Info" class="btn btn-primary" onclick="viewLeavePage('${rc.contextPath}','${leave.id!''}','1');"><i class="fa fa-file-text"></i></button>
		                        <#list effective as eleave>
		                        	<#if eleave.id==leave.id>
		                        		<button type="button" title="Approve Leave" class="btn btn-success" data-toggle="modal" data-target="#pA${leave.id}"><i class="fa fa-check"></i></button>
		                        		<button type="button" title="Reject Leave" class="btn btn-primary" data-toggle="modal" data-target="#pR${leave.id}"><i class="fa fa-times"></i></button>
		                        	</#if>
		                        </#list>
		                    </div>
		                </td>
		            </tr>
	            </#list>
	        </tbody>
	    </table>
	</div>
	<#list leaves as leave>
	<div class="modal fade" id="pA${leave.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
	<div class="modal fade" id="pR${leave.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
	</#list>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
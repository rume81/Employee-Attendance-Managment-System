<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable();
})
</script>
<h3><center>Office Outgoing Approval Request</center></h3>
	<div class="col-xs-12">
		<table id="empList" class="display" cellspacing="0" width="100%">
			<thead>
	            <tr>
	                <th>Date</th>
	                <th>Form Time</th>
	                <th>To Time</th>
	                <th>Employee Name</th>
	                <!--<th>Status</th>-->
	                <th>Action</th>
	            </tr>
	        </thead>
	        <tfoot>
	            <tr>
	                <th>Date</th>
	                <th>Form Time</th>
	                <th>To Time</th>
	                <th>Employee Name</th>
	                <!--<th>Status</th>-->
	                <th>Action</th>
	            </tr>
	        </tfoot>
	        <tbody>
	        	<#list outgoing as outg>
		        	<tr>
		                <td>${outg.date?string["dd-MM-yyyy"]}</td>
		                <td>${outg.ftime!''}</td>
		                <td><#if outg.apllieddate == '01-01-1970'><#else>${outg.apllieddate!''}</#if></td>
		                <td>
		                	<#list emps as emp>
		                		<#if emp.emp_id == outg.emp_id>
		                			${emp.employee_name!''}
		                			<#break>
		                		</#if>
		                	</#list>
		                </td>
		                <td>
		                    <div class="btn-group">
		                        <button type="button" title="View Outgoing Info" class="btn btn-primary" onclick="viewOutgoingPage('${rc.contextPath}','${outg.id!''}','1');"><i class="fa fa-file-text"></i></button>
		                        <#list effective as eleave>
		                        	<#if eleave.id==outg.id>
		                        		<button type="button" title="Approve Outgoing" class="btn btn-success" data-toggle="modal" data-target="#pA${outg.id}"><i class="fa fa-check"></i></button>
		                        		<button type="button" title="Reject Outgoing" class="btn btn-primary" data-toggle="modal" data-target="#pR${outg.id}"><i class="fa fa-times"></i></button>
		                        	</#if>
		                        </#list>
		                    </div>
		                </td>
		            </tr>
	            </#list>
	        </tbody>
	    </table>
	</div>
	<#list outgoing as outgoing>
	<div class="modal fade" id="pA${outgoing.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
	<div class="modal fade" id="pR${outgoing.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
	</#list>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#LeaveType').DataTable();
})
</script>
<h3><center>Leave Types Information</center></h3>
<div class="col-xs-12">
	<table id="LeaveType" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Type Name</th>
                <th>Is Quota</th>
                <th>Is Continue</th>
                <th>Leave Deduct</th>
                <th>Edit / Delete</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
            	<th>Type Name</th>
                <th>Is Quota</th>
                <th>Is Continue</th>
                <th>Leave Deduct</th>
                <th>Edit / Delete</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list allLeaveType as type>
	        	<tr>
	        		<td>${type.leavetype!''}</td>
	                <td>${type.isquota?string}</td>
	                <td>${type.iscontinue?string}</td>
	                <td>${type.leavededuct?c}</td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="Edit Leave Type" class="btn btn-primary" onclick="editLeaveType('${rc.contextPath}','${type.id}');"><i class="fa fa-pencil-square-o"></i></button>
	                        <button type="button" title="Delete Leave Type" class="btn btn-success" onclick="deleteLeaveType('${rc.contextPath}','${type.id}');"><i class="fa fa-times"></i></button>
	         			</div>
	                </td>
	            </tr>
            </#list>
        </tbody>
    </table>
</div>
<#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Add New Employee" class="btn btn-primary" onclick="location.href='${rc.contextPath}/addleavetype'">Add Leave Type</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
</#if>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
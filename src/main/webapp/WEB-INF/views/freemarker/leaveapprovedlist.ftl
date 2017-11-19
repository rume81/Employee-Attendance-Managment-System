<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#approveList').DataTable();
})
</script>
<h3><center>Leave Approved List</center></h3>
<div class="col-xs-12">
	<table id="approveList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
            	<th>Requested Emp</th>
                <th>From Date</th>
                <th>To Date</th>
                <th>Days</th>
                <th>Leave Type</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
            	<th>Requested Emp</th>
                <th>From Date</th>
                <th>To Date</th>
                <th>Days</th>
                <th>Leave Type</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list leaves as leave>
	        	<tr>
	        		<td><#list allEmp as emp><#if emp.emp_id==leave.emp_id>${emp.employee_name!''}</#if></#list></td>
	                <td>${leave.fdate!''}</td>
	                <td>${leave.tdate!''}</td>
	                <td>${leave.days!''}</td>
	                <td>${leave.leavetype.leavetype!''}</td>
	                <td><#if leave.status==0>Not Approved<#elseif leave.status==1>Rejected<#else>Approved</#if></td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="View Leave Info" class="btn btn-primary" onclick="viewLeavePage('${rc.contextPath}','${leave.id!''}','0');"><i class="fa fa-file-text"></i></button>
	                        <!--<button type="button" title="Cancel Leave Request" class="btn btn-primary" onclick="CancelLeave('${rc.contextPath}','${leave.id!''}')"><i class="fa fa-times"></i></button>-->
	                    </div>
	                </td>
	            </tr>
            </#list>
        </tbody>
    </table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable( {
        "order": [[ 0, "desc" ]]
    });
})
</script>
<h3><center>Employee Leave Information</center></h3>
<div class="col-xs-12">
	<table id="empList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>From Date</th>
                <th>To Date</th>
                <th>Apply Date</th>
                <th>Days</th>
                <th>Leave Type</th>
                <th>Status</th>
                <th>View/Cancel</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>From Date</th>
                <th>To Date</th>
                <th>Apply Date</th>
                <th>Days</th>
                <th>Leave Type</th>
                <th>Status</th>
                <th>View/Cancel</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list leaves as leave>
	        	<tr>
	                <td>${leave.sortdate?date}</td>
	                <td>${leave.tdate!''}</td>
	                <td><#if leave.apllieddate == '01-01-1970'><#else>${leave.apllieddate!''}</#if></td>
	                <td>${leave.days?c}</td>
	                <td>${leave.leavetype.leavetype!''}</td>
	                <td><#if leave.status==0>Not Approved<#elseif leave.status==1>Rejected<#else>Approved</#if></td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="View Leave Info" class="btn btn-success" onclick="viewLeavePage('${rc.contextPath}','${leave.id!''}','0');"><i class="fa fa-file-text"></i></button>
	                        <button type="button" title="Edit Leave" <#if (leave.status==1) || (leave.status>=2) || (leave.app1state) || (leave.app2state) || (leave.app3state) || (leave.app4state) || (leave.app5state)>disabled</#if> class="btn btn-primary" onclick="EditLeaveByIndivitual('${rc.contextPath}','${leave.id!''}');"><i class="fa fa-pencil-square-o"></i></button>
	                        <button type="button" title="Cancel Leave Request" <#if (leave.status==1) || (leave.status>=2) || (leave.app1state) || (leave.app2state) || (leave.app3state) || (leave.app4state) || (leave.app5state)>disabled</#if> class="btn btn-success" onclick="CancelLeave('${rc.contextPath}','${leave.id!''}')"><i class="fa fa-times"></i></button>
	                    </div>
	                </td>
	            </tr>
            </#list>
        </tbody>
    </table>
</div>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Add Leave" class="btn btn-primary" onclick="addLeavePage('${rc.contextPath}');">Add Leave</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
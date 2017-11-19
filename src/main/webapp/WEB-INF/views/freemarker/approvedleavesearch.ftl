<table id="empList" class="display" cellspacing="0" width="100%">
	<thead>
        <tr>
            <th>From Date</th>
            <th>To Date</th>
            <th>Days</th>
            <th>Employee Name</th>
            <th>Leave Type</th>
            <!--<th>Status</th>-->
            <th>View/Delete</th>
        </tr>
    </thead>
    <tfoot>
        <tr>
            <th>From Date</th>
            <th>To Date</th>
            <th>Days</th>
            <th>Employee Name</th>
            <th>Leave Type</th>
            <!--<th>Status</th>-->
            <th>View/Delete</th>
        </tr>
    </tfoot>
    <tbody>
    	<#list leaves as leave>
        	<tr>
                <td>${leave.fdate!''}</td>
                <td>${leave.tdate!''}</td>
                <td>${leave.days!''}</td>
                <td>
                <#list allEmp as employee>
                	<#if employee.emp_id==leave.emp_id>
                		${employee.employee_name!''}
                		<#break>
                	</#if>
                </#list>
                </td>
                <td>${leave.leavetype.leavetype!''}</td>
                <!--<td><#if leave.status==0>Not Approved<#elseif leave.status==1>Rejected<#else>Approved</#if></td>-->
                <td>
                    <div class="btn-group">
                        <button type="button" title="View Leave" class="btn btn-primary" onclick="viewLeavePage('${rc.contextPath}','${leave.id!''}','0');"><i class="fa fa-file-text"></i></button>
                        <button type="button" title="Edit Leave" class="btn btn-success" onclick="EditLeave('${rc.contextPath}','${leave.id!''}')"><i class="fa fa-pencil-square-o"></i></button>
                        <button type="button" title="Delete Leave" class="btn btn-primary" onclick="DeleteLeave('${rc.contextPath}','${leave.id!''}')"><i class="fa fa-times"></i></button>
                    </div>
                </td>
            </tr>
        </#list>
    </tbody>
</table>
<script>
$(document).ready(function() {
	$('#empList').DataTable({bFilter: false, bInfo: false});
})
</script>
<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable();
})
</script>
<h3><center>Employee Information</center></h3>
<div class="col-xs-12">
	<table id="empList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Employee Name (Avator)</th>
                <th>Office Email</th>
                <th>Personal Contact</th>
                <th>View</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Employee Name (Avator)</th>
                <th>Office Email</th>
                <th>Personal Contact</th>
                <th>View</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list allEmp as empInfo>
	        	<tr>
	                <td>${empInfo.employee_name!''}(${empInfo.avator!''})</td>
	                <td>${empInfo.email!''}</td>
	                <td>${empInfo.mobile!''}</td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="Edit Employee" class="btn btn-primary" onclick="editEmp('${rc.contextPath}','${empInfo.emp_id}');"><i class="fa fa-pencil-square-o"></i></button>
	                        <#if emp.usertype=="Admin" || emp.usertype=="admin" || emp.usertype=="Talent Manager">
	                        <button type="button" title="Delete Employee" class="btn btn-success" onclick="deleteEmp('${rc.contextPath}','${empInfo.emp_id}');"><i class="fa fa-times"></i></button>
	                        </#if>
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
        		<button type="button" title="Add New Employee" class="btn btn-primary" onclick="addEmp('${rc.contextPath}');">Add New Employee</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
</#if>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
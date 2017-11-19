<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable();
})
</script>
<h3><center>Employee Leave Quota</center></h3>
<div class="col-xs-12">
	<table id="empList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Employee Name (Avator)</th>
                <th>Office Email</th>
                <th>Personal Contact</th>
                <th>View / Add New</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Employee Name (Avator)</th>
                <th>Office Email</th>
                <th>Personal Contact</th>
                <th>View / Add New</th>
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
	                        <button type="button" title="View Employee Work Info" class="btn btn-primary" onclick="viewEmpLeaveQuota('${rc.contextPath}','${empInfo.emp_id}');"><i class="fa fa-file-text"></i></button>
	                        <button type="button" title="Add Employee Work Info" class="btn btn-success" onclick="addEmpLeaveQuota('${rc.contextPath}','${empInfo.emp_id}');"><i class="fa fa-floppy-o"></i></button>
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
        		<!--<button type="button" title="Add Employee Work Info" class="btn btn-primary" onclick="addEmp('${rc.contextPath}');">Add New Employee</button>-->
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
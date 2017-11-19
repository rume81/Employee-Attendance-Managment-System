<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#teamList').DataTable();
})
</script>
<h3><center>Team Information</center></h3>
<div class="col-xs-12">
	<table id="teamList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Team Id</th>
                <th>Team Name</th>
                <th>Edit</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
            	<th>Team Id</th>
                <th>Team Name</th>
                <th>Edit</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list allTeam as team>
	        	<tr>
	        		<td>${team.teamId!''}</td>
	                <td>${team.teamName!''}</td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="Edit Team" class="btn btn-primary" onclick=""><i class="fa fa-pencil-square-o"></i></button>
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
        		<button type="button" title="Add New Employee" class="btn btn-primary" onclick="location.href='${rc.contextPath}/addteam'">Add New Team</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
</#if>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
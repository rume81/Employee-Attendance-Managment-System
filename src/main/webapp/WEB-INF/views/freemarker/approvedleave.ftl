<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#empList').DataTable({bFilter: false, bInfo: false});
	$('#sdate').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#edate').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Approved Leave Information</center></h3>
<div class="col-xs-12 no_padding">
	<div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
    	<form name="dailyempmonthlystatus" id="dailyempmonthlystatus" action="${rc.contextPath}/dailyempmonthlystatus" method="get">
	    	<div class="col-xs-12 no_padding">
	    		<table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">From Date</td>
	                    <td class="static_data">End Date</td>
	                    <td class="static_data">Search</td>
	                </tr>
	                <tr>
	                    <td>
	                    	<input type="text" value="" name="sdate" id="sdate" placeholder="Date" class="form-control">
	                    </td>
	                    <td>
	                    	<input type="text" value="" name="edate" id="edate" placeholder="Date" class="form-control">
	                    </td>
	                    <td>
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="ApprovedLeaveSearch('${rc.contextPath}')">Search</button>
	                    </td>
	                </tr>
	            </table>
	    	</div>
	    	<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
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
			</div>
		</form>
	</div>
</div>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
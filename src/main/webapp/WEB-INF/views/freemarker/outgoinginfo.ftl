<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<script>
$(document).ready(function() {
	$('#outgoingList').DataTable( {
        "order": [[ 0, "desc" ]]
    });
})
</script>
<h3><center>Employee Office Outgoing Information</center></h3>
<div class="col-xs-12">
	<table id="outgoingList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
                <th>Date</th>
                <th>From Time</th>
                <th>To Time</th>
                <th>Apply Date</th>
                <th>Status</th>
                <th>View/Cancel</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Date</th>
                <th>From Time</th>
                <th>To Time</th>
                <th>Apply Date</th>
                <th>Status</th>
                <th>View/Cancel</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list outgoing as outg>
	        	<tr>
	                <td>${outg.date?string["dd-MM-yyyy"]}</td>
	                <td>${outg.ftime!''}</td>
	                <td>${outg.ttime!''}</td>
	                <td><#if outg.apllieddate == '01-01-1970'><#else>${outg.apllieddate!''}</#if></td>
	                <td><#if outg.status==0>Not Approved<#elseif outg.status==1>Rejected<#else>Approved</#if></td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="View Outgoing Info" class="btn btn-success" onclick="viewOutgoingPage('${rc.contextPath}','${outg.id!''}','0');"><i class="fa fa-file-text"></i></button>
	                        <button type="button" title="Edit Outgoing" <#if (outg.status==1) || (outg.status>=2) || (outg.app1state) || (outg.app2state) || (outg.app3state) || (outg.app4state) || (outg.app5state)>disabled</#if> class="btn btn-primary" onclick="EditOutgoingByIndivitual('${rc.contextPath}','${outg.id!''}');"><i class="fa fa-pencil-square-o"></i></button>
	                        <button type="button" title="Cancel Outgoing Request" <#if (outg.status==1) || (outg.status>=2) || (outg.app1state) || (outg.app2state) || (outg.app3state) || (outg.app4state) || (outg.app5state)>disabled</#if> class="btn btn-success" onclick="CancelOutgoing('${rc.contextPath}','${outg.id!''}')"><i class="fa fa-times"></i></button>
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
        		<button type="button" title="Add Leave" class="btn btn-primary" onclick="addOfficeOutgoingPage('${rc.contextPath}');">Add Outgoing</button>
        	</td>
        </tr>
	</table>
</div>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
<h3><center>Administrative Panel</center></h3>
<script type="text/javascript">
function wait() {
	jQuery.blockUI({ css: { 
                     border: 'none', 
                     padding: '15px', 
                     backgroundColor: '#000', 
                     '-webkit-border-radius': '10px', 
                     '-moz-border-radius': '10px', 
                     opacity: .5, 
                     color: '#fff' 
                 } }); 
}

</script>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <div class="col-xs-12 no_padding">
    		<table class="table table-bordered table-responsive">
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Employee Register" class="btn btn-primary" onclick="location.href='${rc.contextPath}/empinfo'">Employee Register</button>
                    	
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Watcher Register" class="btn btn-primary" onclick="location.href='${rc.contextPath}/watcherinfo'">Watcher Register</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Employee Work info Register" class="btn btn-primary" onclick="location.href='${rc.contextPath}/empWorkInfo' ">Employee Work info Register</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Holiday Information" class="btn btn-primary" onclick="location.href='${rc.contextPath}/holidainfo'">Holiday Information</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Attendance Manipulation" class="btn btn-primary" onclick="location.href='${rc.contextPath}/attendancemanipulation'">Attendance Manipulation</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Approved Leave" class="btn btn-primary" onclick="location.href='${rc.contextPath}/approvedleave'">Approved Leave</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Add New Team" class="btn btn-primary" onclick="location.href='${rc.contextPath}/teaminfo'">Team Information</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Leave Type" class="btn btn-primary" onclick="location.href='${rc.contextPath}/leavetype'">Leave Type</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Leave Quota" class="btn btn-primary" onclick="location.href='${rc.contextPath}/leaveQuota'">Employee Leave Quota</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Leave Quota" class="btn btn-primary" onclick="location.href='${rc.contextPath}/employeeautolunchsetup'">Setup Auto Lunch of Employee</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Report Regenarat" class="btn btn-primary" onclick="location.href='${rc.contextPath}/reportgeneratorpage'">Report Re-Generator</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Manage Leave" class="btn btn-primary" onclick="location.href='${rc.contextPath}/manageleave'">Manage Leave</button>
                    </td>
                </tr>
                
            </table>
    	</div>
    </div>
</div>
<h3><center>All Reports</center></h3>
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
                    	<button type="button" title="Attendance Status" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/attendancestatus'">Daily Attendance Status</button>
                    	<!--<a href="${rc.contextPath}/attendancestatus">Attendance Status</a>-->
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/empmonthlystatus'">Monthly Attendance Status</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Employee Leave Information" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/monthlyleavestatus' ">Monthly  Leave  Status</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Attendance Status by Team" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/attendancestatusbyteam'">Attendance Status by Team</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Employee Lunch status" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/monthlylunchstatus'">Monthly  Lunch Status</button>
                    </td>
                    <td class="static_data">
                    	<button type="button" title="Employee Yearly Leave" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/employeeleaves'">Employee Yearly Leave</button>
                    </td>
                </tr>
                <tr>
                    <td class="static_data">
                    	<button type="button" title="Employee Overtime status" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/empmonthlyovertime'">Monthly Overtime Status</button>
                    </td>
                    <td class="static_data">
                    	<!--<button type="button" title="Employee Yearly Leave" class="btn btn-primary" onclick="wait(); location.href='${rc.contextPath}/employeeleaves'">Employee Yearly Leave</button>-->
                    </td>
                </tr>
            </table>
    	</div>
    </div>
</div>
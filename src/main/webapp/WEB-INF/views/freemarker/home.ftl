<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Employee Dashboard </center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <!--<div class="col-xs-12 no_padding">
            <table class="table table-bordered table-responsive">
                <tr>
        	        <td colspan="7">
        	        	<table width="60%">
        	        		<tr>
        	        			<td width="45%">
        	        				<div id="lazyIn" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        	        				<button type="button" id="btnIn" title="Office In" <#if inOut==0>disabled<#else>onclick="attendanceAdd('${rc.contextPath}','${emp.emp_id}','0');"</#if> class="btn btn-primary btn-block">Office In</button>
        	        			</td>
        	        			<td width="10%"></td>
        	        			<td width="45%">
        	        				<div id="lazyOut" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        	        				<button type="button" id="btnOut" title="Office Out" <#if inOut==1>disabled<#else>onclick="attendanceAdd('${rc.contextPath}','${emp.emp_id}','1');"</#if> class="btn btn-primary btn-block">Office Out</button>
        	        			</td>
        	        		</tr>
        	        	</table>
        	        </td>
        	    </tr>
            </table>
        </div>-->
        <h4 class="user_heading">Monthly Status</h4>
		<div class="col-xs-12 no_padding">
	    	<table class="table table-bordered table-responsive">
		        <tr class="static_data">
		        	<td>Working Day</td>
		        	<td>Present</td>
		        	<td>Working Hrs in Month</td>
		        	<td>Hrs Worked</td>
		        	<td>Hrs Worked Without Overtime</td>
		        	<td>Hrs of Overtime</td>
		        	<td>Hrs of Deficit</td>
		        	<td>Holiday Working Hrs</td>
		        	<td>Leave</td>
		        	<td>LWP</td>
		        	<td>Absent</td>
		        	<td>Late</td>
		        </tr>
		        <#list matt as at>
		        <tr>
		        	<td>${at.total_working_day!''}</td>
		        	<td>${at.present!''}</td>
		        	<td>${at.presentWorkingHours!''}</td>
		        	<td>${at.totalworkinghours?string}</td>
		        	<td>${at.absulateworkinghours?string}</td>
		        	<td><font color="green">${at.overtimehours?string}</font></td>
					<td><font color="red">${at.deficithours?string}</font></td>
					<td><font color="green">${at.holidayovertime?string}</font></td>
		        	<td>${at.leave!''}</td>
		        	<td>${at.lwp!''}</td>
		        	<td>${at.absent!''}</td>
		        	<td>${at.late!''}</td>
		        </tr>
		        </#list>
		    </table>
		</div>
		
        <h4 class="user_heading">Today's Attendance Information</h4>
		<div class="col-xs-12 no_padding">
	    	<table class="table table-bordered table-responsive">
		        <tr>
		        	<td>Date:</td>
		        	<td colspan="2">${sysDate!''}</td>
		        </tr>
		        <tr>
		            <td class="static_data">SlNo</td>
		            <td class="static_data">Time</td>
		            <td class="static_data">IN/OUT</td>
		        </tr>
		        <#list att as at>
		        <tr>
		            <td>${at?index+1}</td>
		            <td>${at.att_time!''}</td>
		            <td><#if at.att_inout?? && at.att_inout==0>IN<#else>OUT</#if></td>
		        </tr>
		        </#list>
		    </table>
		</div>
    </div>
</div>
	
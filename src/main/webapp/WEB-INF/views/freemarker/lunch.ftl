<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Employee Lunch</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <div class="col-xs-12 no_padding">
            <table class="table table-bordered table-responsive">
                <tr>
        	        <td colspan="7">
        	        	<table class="table table-bordered table-responsive">
        	        		<tr>
        	        			<td width="45%">
        	        				<div id="lazyIn" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        	        				<button type="button" id="btnIn" title="Lunch Request" <#if inOut==1>disabled</#if> class="btn btn-primary btn-block" onclick="lunchAdd('${rc.contextPath}','${emp.emp_id}','0');">Lunch Request</button>
        	        			</td>
        	        			<td width="10%"></td>
        	        			<td width="45%">
        	        				<div id="lazyOut" style="display:none"><img src="${rc.contextPath}/css/common/images/lazyload.gif" alt="loading" height="35" width="35">  Please Wait</div>
        	        				<button type="button" id="btnOut" title="Cancel Lunch" <#if inOut==0>disabled</#if> class="btn btn-primary btn-block" onclick="lunchAdd('${rc.contextPath}','${emp.emp_id}','1');">Cancel Lunch</button>
        	        			</td>
        	        		</tr>
        	        		<tr>
        	        			<td colspan="3">
        	        				<input class="msgCheck" type="checkbox" id="autolunch" name="autolunch" value="Auto Lunch Input with Attendance" <#if autoLunch.autolunchinput?? && autoLunch.autolunchinput=true>checked</#if> onchange="setAutoLunch('${rc.contextPath}','${emp.emp_id}')"> Auto Lunch Input with Attendance<br>
        	        			</td>
        	        		</tr>
        	        		<#if emp.usertype == 'Admin' || emp.usertype == 'Talent Manager'>
        	        		<tr>
        	        			<td colspan="3"><h4 class="user_heading">Add Guest Lunch</h4></td>
        	        		</tr>
        	        		<tr>
        	        			<td>Number of Guest</td>
        	        			<td colspan="2"><input type="text" id="lcount" style="text-align:right" name="lcount" class="form-control" value="<#if guestLunch??>${guestLunch.lunch_count!'0'}</#if>"></td>
        	        		</tr>
        	        		<tr>
        	        			<td  colspan="3">
        	        				<button type="button" title="Add Guest Lunch Request" class="btn btn-primary" onclick="lunchAddForGuest('${rc.contextPath}');">Add Guest Lunch</button>
        	        			</td>
        	        		</tr>
        	        		</#if>
        	        	</table>
        	        </td>
        	    </tr>
            </table>
        </div>
        <h4 class="user_heading">Lunch Information</h4>
		<div class="col-xs-12 no_padding">
	    	<table class="table table-bordered table-responsive">
		        <tr>
		        	<td><b>Date:</b></td>
		        	<td colspan="2"><b>${curDate!''}</b></td>
		        </tr>
		        <tr>
		            <td class="static_data"><b>Employee Name</b></td>
		            <td class="static_data"><b>Lunch Count</b></td>
		        </tr>
		        <#assign total = 0>
		        <#list lunchs as lunch>
		        <#assign total = total + lunch.lunch_count>
		        <tr>
		            <td>${lunch.name!''}</td>
		            <!--<td><#if lunch.lunch_status==true>Lunch Taken<#else>Lunch Not Taken</#if></td>-->
		            <td>${lunch.lunch_count!''}</td>
		        </tr>
		        </#list>
		        <tr>
		        	<td><b>Total</b></td>
		        	<td><b>${total}</b></td>
		        </tr>
		    </table>
		</div>
    </div>
</div>
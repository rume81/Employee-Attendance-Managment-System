<table class="table table-bordered table-responsive">
    <tr>
        <td class="static_data">Date</td>
        <td class="static_data">In Time</td>
        <td class="static_data">Out Time</td>
        <td class="static_data">Status</td>
    </tr>
    <#if (monAtt?size>0)>
	    <#list monAtt as att>
	    <tr <#if att.weekend?? && att.isWeekend()>bgcolor="#009933"<#elseif att.holiday?? && att.isHoliday()>bgcolor="#3399FF"<#elseif att.absent?? && att.isAbsent()>bgcolor="#FF6666"<#elseif att.leave?? && att.isLeave()>bgcolor="#CCCCCC"</#if>>
	    	<td>${att.att_date!''}</td>
	    	<td>${att.att_in!''}</td>
	    	<td>${att.att_out!''}</td>
	    	<td>${att.status!''}</td>
	    </tr>
	    </#list>
    <#else>
    	<tr>
    		<td colspan="4"><center><b>Attendance not found</b></center></td>
    	</tr>
    </#if>
</table>
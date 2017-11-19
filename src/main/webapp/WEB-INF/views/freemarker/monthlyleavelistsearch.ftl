<h4><center><#if mon ==1>January
    <#elseif mon ==2>February
    <#elseif mon ==3>March
    <#elseif mon ==4>April
    <#elseif mon ==5>May
    <#elseif mon ==6>June
    <#elseif mon ==7>July
    <#elseif mon ==8>August
    <#elseif mon ==9>September
    <#elseif mon ==10>October
    <#elseif mon ==11>November
    <#else>December
    </#if> / ${year!''}</center></h4>
<table class="table table-bordered table-responsive" style="overflow:hidden;table-layout:fixed;">
	<tr class="static_data">
    	<td style="width: 120px">Employee Name</td>
    	<#list 1..lastday as day>
    	<td id="rotate" style="width: 20px">${day}</td>
    	</#list>
    </tr>
    <#list allEmp as emp>
    <tr>
    	<#list estate?keys as key>
        	<#if emp.emp_id?string == key>
        		<td style="font-size:10px">${emp.employee_name!''}</td>
	        	<#list estate[key] as est>
	        	<td <#if est.weekend==true>bgcolor="#CCCCCC"<#elseif est.holiday>bgcolor="#CCFFFF"<#elseif  est.absent>bgcolor="#FF6666"<#elseif est.leave><#if est.status=="Leave">bgcolor="#009966"<#else>bgcolor="#00CCFF"</#if></#if>>
	        		<#if est.weekend==true><#elseif est.holiday>
	        		<#elseif est.leave>
	        			<#if est.status=="Leave">
	        				<a href="javascript:getLeave('${rc.contextPath}','${emp.emp_id}','${est.att_date}',1)"><font color="000000">&#10004;</font></a>
	        			<#else>
	        				<a href="javascript:getLeave('${rc.contextPath}','${emp.emp_id}','${est.att_date}',0)"><font color="FFFFFF">&#10004;</font></a>
	        			</#if>
	        		</#if>
	        	</td>
	        	</#list>
	        	<#break>
        	</#if>
    	</#list>
    <tr>
    </#list> 
</table>
<input type="submit" class="btn btn-primary" value="Download">
<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
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
    	<td style="width: 60px">Total</td>
    </tr>
    <#list allEmp as emp>
    <tr>
    	<#list estate?keys as key>
        	<#if emp.emp_id?string == key>
        		<td style="font-size:10px">${emp.employee_name!''}</td>
        		<#assign total = 0>
	        	<#list estate[key] as est>
	        	<td>
	        		<#if est.lunch_status==true>
	        			${est.lunch_count}
	        			<#assign total = total + est.lunch_count>
	        		<#else>
	        			
	        		</#if>
	        	</td>
	        	</#list>
	        	<td>${total}</td>
	        	<#break>
        	</#if>
    	</#list>
    <tr>
    </#list>
</table>
<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
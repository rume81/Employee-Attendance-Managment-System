<table class="table table-bordered table-responsive" style="background-color:#aed6f1">
	<tr>
		<td colspan="2">Leave Type</td>
		<td>Leave Quota</td>
		<td>Leave Used</td>
		<td>Expire Date</td>
	</tr>							    
	<#list allleavetype as type>
		<#if type.isquota>
			<#assign qto = 0.0>
			<#assign usd = 0.0>
			<#assign exd = "">
			<#list quotas as quota>
				<#if type.id==quota.type_id>
					<#assign qto = quota.quota?c>
					<#assign usd = quota.used?c>
					<#assign exd = quota.expdate>
					<#break>
				</#if>	
			</#list>
        	<tr>
        		<td colspan="2">${type.leavetype!''}<input type="hidden" id="type_id" name="type_id" value="${type.id!''}"/></td>
                <td>${qto}</td>
                <td>${usd}</td>
                <td>
                	<#if type.iscontinue==false>
                		${exd}
                	<#else>
                		
                	</#if>
                </td>
    		</tr>
    	</#if>
	</#list>
</table>
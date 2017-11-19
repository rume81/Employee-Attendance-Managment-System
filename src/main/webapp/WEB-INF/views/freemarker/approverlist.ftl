<input type="hidden" id="appsize" name="appsize" value="${approver?size}"/>
<#if (approver?size > 0)>
<table class="table table-responsive">
	<#list 1..approver?size as i>
		<tr id="app${i}">
            <td class="name">
            	<#if i==1>
            		First Approvar *
            	<#elseif i==2>
            		Second Approvar
            	<#elseif i==3>
            		Thierd Approvar
            	<#elseif i==4>
            		Fourth Approvar
            	<#else>
            		Fifth Approvar
            	</#if>
            </td>
            <td>
            	<select name="approvar${i}" id="approvar${i}" class="form-control SlectBox">
                	<option value="">
                		<#if i==1>
	                		Select First Approvar 
	                	<#elseif i==2>
	                		Select Second Approvar
	                	<#elseif i==3>
	                		Select Thierd Approvar
	                	<#elseif i==4>
	                		Select Fourth Approvar
	                	<#else>
	                		Select Fifth Approvar
	                	</#if>
                	</option>
                	<#list approver as apr>
            			<#list allEmp as appemp>
            				<#if apr.approver_id==appemp.emp_id>
            					<option value="${appemp.emp_id}">${appemp.employee_name}(${appemp.avator})</option>
            					<#break>
            				</#if>
            			</#list>
            		</#list>          
                </select>
                <!--<input type="hidden" id="appshow" name="appshow" value="1"/>-->
            </td>
        </tr>
    </#list>
</table>
</#if>
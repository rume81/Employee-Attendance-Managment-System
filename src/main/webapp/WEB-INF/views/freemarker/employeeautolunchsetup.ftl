<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Employee Auto Lunch Setup By Admin</center></h3>
<div class="col-xs-12">
	<div class="col-xs-12">
    	<table class="table table-bordered table-responsive">
    		<tr>
        		<td>
        			<#list allEmp1 as employee>
            			<input type="checkbox" name="chk" id="chk_${employee.emp_id}" value="${employee.emp_id}" <#list emplunch as apr><#if apr.emp_id==employee.emp_id && apr.autolunchinput==true>checked='checked'</#if></#list> onchange="setAutoLunchbyadmin('${rc.contextPath}','${employee.emp_id}')"> ${employee.employee_name!''}(${employee.avator!''})<br>
            		</#list>
        		</td>
        		<td>
        			<#list allEmp2 as employee>
            			<input type="checkbox" name="chk" id="chk_${employee.emp_id}" value="${employee.emp_id}" <#list emplunch as apr><#if apr.emp_id==employee.emp_id && apr.autolunchinput==true>checked='checked'</#if></#list> onchange="setAutoLunchbyadmin('${rc.contextPath}','${employee.emp_id}')"> ${employee.employee_name!''}(${employee.avator!''})<br>
            		</#list>
        		</td>
        		<td>
        			<#list allEmp3 as employee>
            			<input type="checkbox" name="chk" id="chk_${employee.emp_id}" value="${employee.emp_id}" <#list emplunch as apr><#if apr.emp_id==employee.emp_id && apr.autolunchinput==true>checked='checked'</#if></#list> onchange="setAutoLunchbyadmin('${rc.contextPath}','${employee.emp_id}')"> ${employee.employee_name!''}(${employee.avator!''})<br>
            		</#list>
        		</td>
        		<td>
        			<#list allEmp4 as employee>
            			<input type="checkbox" name="chk" id="chk_${employee.emp_id}" value="${employee.emp_id}" <#list emplunch as apr><#if apr.emp_id==employee.emp_id && apr.autolunchinput==true>checked='checked'</#if></#list> onchange="setAutoLunchbyadmin('${rc.contextPath}','${employee.emp_id}')"> ${employee.employee_name!''}(${employee.avator!''})<br>
            		</#list>
        		</td>
        	</tr>
        </table>
    </div>
</div>

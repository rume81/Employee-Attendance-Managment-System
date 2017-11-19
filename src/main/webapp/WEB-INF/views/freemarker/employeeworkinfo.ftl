<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#from_date').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Add Employee Work Info</center></h3>
<form name="addempworkInfo" id="addempworkInfo" action="${rc.contextPath}/addempwork" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	        	<tr>
	        		<td class="name">Employee Id</td>
	        		<td><input type="hidden" id="emp_id" name="emp_id" value="${selemp.emp_id!''}"/>${selemp.emp_number!''}</td>
	        		<td class="name">Employee Name</td>
	        		<td>${selemp.employee_name!''}</td>
	        	</tr>
	        	<tr>
	        		<td class="name">Employee Avator</td>
	        		<td>${selemp.avator!''}</td>
	        		<td class="name">Office Start Time*</td>
	                <td>
	                	<select name="off_start" id="off_start" class="form-control SlectBox">
	                    	<option value="">Select Office Start Time</option>
	                        <option value="8" <#if workinfo.office_start?? && workinfo.office_start==8>selected="selected"</#if>>08:00 AM</option>
	                        <option value="8.5" <#if workinfo.office_start?? && workinfo.office_start==8.5>selected="selected"</#if>>08:30 AM</option>
	                        <option value="9" <#if workinfo.office_start?? && workinfo.office_start==9>selected="selected"</#if>>09:00 AM</option>
	                        <option value="9.5" <#if workinfo.office_start?? && workinfo.office_start==9.5>selected="selected"</#if>>09:30 AM</option>
	                        <option value="10" <#if workinfo.office_start?? && workinfo.office_start==10>selected="selected"</#if>>10:00 AM</option>
	                        <option value="10.5" <#if workinfo.office_start?? && workinfo.office_start==10.5>selected="selected"</#if>>10:30 AM</option>
	                        <option value="11" <#if workinfo.office_start?? && workinfo.office_start==11>selected="selected"</#if>>11:00 AM</option>
	                        <option value="11.5" <#if workinfo.office_start?? && workinfo.office_start==11.5>selected="selected"</#if>>11:30 AM</option>
	                        <option value="12" <#if workinfo.office_start?? && workinfo.office_start==12>selected="selected"</#if>>12:00 AM</option>
	                        <option value="12.5" <#if workinfo.office_start?? && workinfo.office_start==12.5>selected="selected"</#if>>12:30 AM</option>
	                        <option value="13" <#if workinfo.office_start?? && workinfo.office_start==13>selected="selected"</#if>>01:00 PM</option>
	                        <option value="13.5" <#if workinfo.office_start?? && workinfo.office_start==13.5>selected="selected"</#if>>01:30 PM</option>
	                        <option value="14" <#if workinfo.office_start?? && workinfo.office_start==14>selected="selected"</#if>>02:00 PM</option>
	                        <option value="14.5" <#if workinfo.office_start?? && workinfo.office_start==14.5>selected="selected"</#if>>02:30 PM</option>
	                        <option value="15" <#if workinfo.office_start?? && workinfo.office_start==15>selected="selected"</#if>>03:00 PM</option>
	                        <option value="15.5" <#if workinfo.office_start?? && workinfo.office_start==15.5>selected="selected"</#if>>03:30 PM</option>
	                        <option value="16" <#if workinfo.office_start?? && workinfo.office_start==16>selected="selected"</#if>>04:00 PM</option>
	                        <option value="16.5" <#if workinfo.office_start?? && workinfo.office_start==16.5>selected="selected"</#if>>04:30 PM</option>
	                        <option value="17" <#if workinfo.office_start?? && workinfo.office_start==17>selected="selected"</#if>>05:00 PM</option>
	                        <option value="17.5" <#if workinfo.office_start?? && workinfo.office_start==17.5>selected="selected"</#if>>05:30 PM</option>
	                        <option value="18" <#if workinfo.office_start?? && workinfo.office_start==18>selected="selected"</#if>>06:00 PM</option>
	                        <option value="18.5" <#if workinfo.office_start?? && workinfo.office_start==18.5>selected="selected"</#if>>06:30 PM</option>
	                        <option value="19" <#if workinfo.office_start?? && workinfo.office_start==19>selected="selected"</#if>>07:00 PM</option>
	                        <option value="19.5" <#if workinfo.office_start?? && workinfo.office_start==19.5>selected="selected"</#if>>07:30 PM</option>
	                        <option value="20" <#if workinfo.office_start?? && workinfo.office_start==20>selected="selected"</#if>>08:00 PM</option>
	                        <option value="20.5" <#if workinfo.office_start?? && workinfo.office_start==20.5>selected="selected"</#if>>08:30 PM</option>
	                        <option value="21" <#if workinfo.office_start?? && workinfo.office_start==21>selected="selected"</#if>>09:00 PM</option>
	                        <option value="21.5" <#if workinfo.office_start?? && workinfo.office_start==21.5>selected="selected"</#if>>09:30 PM</option>
	                        <option value="22" <#if workinfo.office_start?? && workinfo.office_start==22>selected="selected"</#if>>10:00 PM</option>
	                        <option value="22.5" <#if workinfo.office_start?? && workinfo.office_start==22.5>selected="selected"</#if>>10:30 PM</option>          
	                    </select>
	                </td>
	    		</tr>
	    		<tr>
	                <td class="name">Working Hour*</td>
	                <td>
	                	<select name="working_hour" id="working_hour" class="form-control SlectBox">
	                    	<option value="">Select Working Hour</option>
	                        <option value="1" <#if workinfo.working_hour?? && workinfo.working_hour==1>selected="selected"</#if>>1 Hour</option>
	                        <option value="1.5" <#if workinfo.working_hour?? && workinfo.working_hour==1.5>selected="selected"</#if>>1.5 Hour</option>
	                        <option value="2" <#if workinfo.working_hour?? && workinfo.working_hour==2>selected="selected"</#if>>2 Hour</option>
	                        <option value="2.5" <#if workinfo.working_hour?? && workinfo.working_hour==2.5>selected="selected"</#if>>2.5 Hour</option>
	                        <option value="3" <#if workinfo.working_hour?? && workinfo.working_hour==3>selected="selected"</#if>>3 Hour</option>
	                        <option value="3.5" <#if workinfo.working_hour?? && workinfo.working_hour==3.5>selected="selected"</#if>>3.5 Hour</option>
	                        <option value="4" <#if workinfo.working_hour?? && workinfo.working_hour==4>selected="selected"</#if>>4 Hour</option>
	                        <option value="4.5" <#if workinfo.working_hour?? && workinfo.working_hour==4.5>selected="selected"</#if>>4.5 Hour</option>
	                        <option value="5" <#if workinfo.working_hour?? && workinfo.working_hour==5>selected="selected"</#if>>5 Hour</option>
	                        <option value="5.5" <#if workinfo.working_hour?? && workinfo.working_hour==5.5>selected="selected"</#if>>5.5 Hour</option>
	                        <option value="6" <#if workinfo.working_hour?? && workinfo.working_hour==6>selected="selected"</#if>>6 Hour</option>
	                        <option value="6.5" <#if workinfo.working_hour?? && workinfo.working_hour==6.5>selected="selected"</#if>>6.5 Hour</option>
	                        <option value="7" <#if workinfo.working_hour?? && workinfo.working_hour==7>selected="selected"</#if>>7 Hour</option>
	                        <option value="7.5" <#if workinfo.working_hour?? && workinfo.working_hour==7.5>selected="selected"</#if>>7.5 Hour</option>
	                        <option value="8" <#if workinfo.working_hour?? && workinfo.working_hour==8>selected="selected"</#if>>8 Hour</option>
	                        <option value="8.5" <#if workinfo.working_hour?? && workinfo.working_hour==8.5>selected="selected"</#if>>8.5 Hour</option>
	                        <option value="9" <#if workinfo.working_hour?? && workinfo.working_hour==9>selected="selected"</#if>>9 Hour</option>
	                        <option value="9.5" <#if workinfo.working_hour?? && workinfo.working_hour==9.5>selected="selected"</#if>>9.5 Hour</option>
	                        <option value="10" <#if workinfo.working_hour?? && workinfo.working_hour==10>selected="selected"</#if>>10 Hour</option>
	                        <option value="10.5" <#if workinfo.working_hour?? && workinfo.working_hour==10.5>selected="selected"</#if>>10.5 Hour</option>
	                        <option value="11" <#if workinfo.working_hour?? && workinfo.working_hour==11>selected="selected"</#if>>11 Hour</option>
	                        <option value="11.5" <#if workinfo.working_hour?? && workinfo.working_hour==11.5>selected="selected"</#if>>11.5 Hour</option>
	                        <option value="12" <#if workinfo.working_hour?? && workinfo.working_hour==12>selected="selected"</#if>>12 Hour</option>
	                        <option value="12.5" <#if workinfo.working_hour?? && workinfo.working_hour==12.5>selected="selected"</#if>>12.5 Hour</option>
	                        <option value="13" <#if workinfo.working_hour?? && workinfo.working_hour==13>selected="selected"</#if>>13 Hour</option>
	                        <option value="13.5" <#if workinfo.working_hour?? && workinfo.working_hour==13.5>selected="selected"</#if>>13.5 Hour</option>
	                        <option value="14" <#if workinfo.working_hour?? && workinfo.working_hour==14>selected="selected"</#if>>14 Hour</option>
	                        <option value="14.5" <#if workinfo.working_hour?? && workinfo.working_hour==14.5>selected="selected"</#if>>14.5 Hour</option>
	                        <option value="15" <#if workinfo.working_hour?? && workinfo.working_hour==15>selected="selected"</#if>>15 Hour</option>
	                        <option value="15.5" <#if workinfo.working_hour?? && workinfo.working_hour==15.5>selected="selected"</#if>>15.5 Hour</option>          
	                    </select>
	                </td>
	                <td class="name">Weekend*</td>
	                <td>
	                	<input class="msgCheck" type="checkbox" name="weekend" value="Monday" <#if workinfo.weekend?? && workinfo.weekend?contains("Monday")>checked</#if>> Monday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Tuesday" <#if workinfo.weekend?? && workinfo.weekend?contains("Tuesday")>checked</#if>> Tuesday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Wednesday" <#if workinfo.weekend?? && workinfo.weekend?contains("Wednesday")>checked</#if>> Wednesday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Thursday" <#if workinfo.weekend?? && workinfo.weekend?contains("Thursday")>checked</#if>> Thursday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Friday" <#if workinfo.weekend?? && workinfo.weekend?contains("Friday")>checked</#if>> Friday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Saturday" <#if workinfo.weekend?? && workinfo.weekend?contains("Saturday")>checked</#if>> Saturday<br>
  						<input class="msgCheck" type="checkbox" name="weekend" value="Sunday" <#if workinfo.weekend?? && workinfo.weekend?contains("Sunday")>checked</#if>> Sunday<br>
	                </td>
	           	</tr>
	            <tr>
	            	<td class="name">Applied Date*</td>
	                <td><input type="text" value="${workinfo.from_date!''}" name="from_date" id="from_date" placeholder="dd-mm-yyyy" class="form-control"></td>
	                
	                <td colspan="2"><input class="msgCheck" type="checkbox" name="lunchexc" value="" <#if workinfo.lunchExclude?? && workinfo.lunchExclude == 1>checked</#if>> Lunch Exclude From Working Hour</td>
	            </tr>
	        </table>
		</div>
	</div>
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td colspan="4" class="name"><h4>Select Leave Approver for ${selemp.employee_name!''} (Maximum 5)</h4></td>
	    		</tr>
	        	<tr>
	        		<td>
	        			<#list allEmp1 as employee>
                			<input type="checkbox" name="chk" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list approvers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp2 as employee>
                			<input type="checkbox" name="chk" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list approvers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp3 as employee>
                			<input type="checkbox" name="chk" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list approvers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp4 as employee>
                			<input type="checkbox" name="chk" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list approvers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        	</tr>
	        </table>
	    </div>
	</div>
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td colspan="4" class="name"><h4>Select Office Outgoing Approver for ${selemp.employee_name!''} (Maximum 2)</h4></td>
	    		</tr>
	        	<tr>
	        		<td>
	        			<#list allEmp1 as employee>
                			<input type="checkbox" name="chkoff" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list offapprovers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp2 as employee>
                			<input type="checkbox" name="chkoff" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list offapprovers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp3 as employee>
                			<input type="checkbox" name="chkoff" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list offapprovers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        		<td>
	        			<#list allEmp4 as employee>
                			<input type="checkbox" name="chkoff" onClick="toggleselect('chk${employee.emp_id}')" id="chk${employee.emp_id}" value="${employee.emp_id}" <#list offapprovers as apr><#if apr.approver_id==employee.emp_id>checked='checked'</#if></#list>> ${employee.employee_name!''}(${employee.avator!''})<br>
                		</#list>
	        		</td>
	        	</tr>
	        </table>
	    </div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Add Employee Work Info"  class="btn btn-primary" onclick="addEmployeeWorkInfo('${rc.contextPath}')">Save</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
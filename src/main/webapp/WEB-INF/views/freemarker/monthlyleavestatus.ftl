<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>
<h3><center>Monthly Leave Status</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <form name="downloadempleave" id="downloadempleave" action="${rc.contextPath}/monthlyleavedownload" method="get">
        	<div class="col-xs-12 no_padding">
        		<table class="table table-bordered table-responsive">
	                <tr>
	                    <td rowspan="2" class="static_data">Search</td>
	                    <td class="static_data">Month</td>
	                    <td class="static_data">Year</td>
	                    <td class="static_data"  colspan="2">Search</td>
	                </tr>
	                <tr>
	                    <td>
	                    	<select name="mon" id="mon" class="form-control SlectBox">
		                    	<option value="">Select Month</option>
		                        <option value="1" <#if mon==1>Selected=selected</#if>>January</option>
		                        <option value="2" <#if mon==2>Selected=selected</#if>>February</option>
		                        <option value="3" <#if mon==3>Selected=selected</#if>>March</option>
		                        <option value="4" <#if mon==4>Selected=selected</#if>>April</option>
		                        <option value="5" <#if mon==5>Selected=selected</#if>>May</option>
		                        <option value="6" <#if mon==6>Selected=selected</#if>>June</option>
		                        <option value="7" <#if mon==7>Selected=selected</#if>>July</option>
		                        <option value="8" <#if mon==8>Selected=selected</#if>>August</option>
		                        <option value="9" <#if mon==9>Selected=selected</#if>>September</option>
		                        <option value="10" <#if mon==10>Selected=selected</#if>>October</option>
		                        <option value="11" <#if mon==11>Selected=selected</#if>>November</option>
		                        <option value="12" <#if mon==12>Selected=selected</#if>>December</option>          
	                    	</select>
	                    </td>
	                    <td>
	                    	<input type="text" value="${year!''}" name="year" id="year" placeholder="Input Year" class="form-control">
	                    </td>
	                    <td colspan="2">
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="getLeaveSearch('${rc.contextPath}');">Search</button>
	                    </td>
	                </tr>
	            </table>
        	</div>
			<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
				<div class="col-xs-2 no_padding">
					<table class="table table-bordered table-responsive">
			    		<tr>
			    			<td bgcolor="#CCCCCC">Weekday	    				
			    			</td>
			    			<td bgcolor="#CCFFFF">Holiday	    				
			    			</td>
			    			<td bgcolor="#00CCFF">Applied	    				
			    			</td>
			    			<td bgcolor="#009966">Leave
			    			</td> 
			    			<td bgcolor="#FF6666">Absent
			    			</td>
			    		</tr>
			    	</table>
		    	</div>
		    	<div class="col-xs-12 no_padding" id="leavelist">
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
			    </div>
			</div>
		</form>
    </div>
</div>
	
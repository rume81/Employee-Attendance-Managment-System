<script type="text/javascript" src="${rc.contextPath}/js/lunch.js"></script>
<h3><center>Monthly Lunch Status</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <form name="downloadempleave" id="downloadempleave" action="" method="get">
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
		                        <option value="1">January</option>
		                        <option value="2">February</option>
		                        <option value="3">March</option>
		                        <option value="4">April</option>
		                        <option value="5">May</option>
		                        <option value="6">June</option>
		                        <option value="7">July</option>
		                        <option value="8">August</option>
		                        <option value="9">September</option>
		                        <option value="10">October</option>
		                        <option value="11">November</option>
		                        <option value="12">December</option>          
	                    	</select>
	                    </td>
	                    <td>
	                    	<input type="text" value="" name="year" id="year" placeholder="Input Year" class="form-control">
	                    </td>
	                    <td colspan="2">
	                    	<button type="button" title="Search Attendance" class="btn btn-primary" onclick="getLunchSearch('${rc.contextPath}');">Search</button>
	                    </td>
	                </tr>
	            </table>
        	</div>
			<div class="col-xs-12 no_padding" id="searchRes" name="searchRes">
				
		    	<div class="col-xs-12 no_padding" id="lunchlist">
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
			    </div>
			</div>
		</form>
    </div>
</div>
	
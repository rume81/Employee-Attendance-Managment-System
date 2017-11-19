				<div class="col-xs-12 no_padding" id="leavelist">
					<h4><center>Leave Year: ${year!''}/${year+1!''}</center></h4>
		    		<table class="table table-bordered table-responsive" style="overflow:hidden;table-layout:fixed;">
			    		<tr class="static_data">
				        	<td style="width: 120px;height:120px">Employee Name</td>
				        	<td style="width: 50px"></td>
				        	<td id="rotate" style="width: 30px">Jul/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Aug/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Sep/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Oct/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Nov/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Dec/${year!''}</td>
				        	<td id="rotate" style="width: 30px">Jan/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">Feb/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">Mar/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">Apr/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">May/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">Jun/${year+1!''}</td>
				        	<td id="rotate" style="width: 30px">Total</td>
				        </tr>
				        
				        <#list yleave as leave>
				        <#assign r=1>
				        <tr>
				        	<td rowspan="4" style="font-size:10px" <#if r==1>bgcolor="#FFFFCC"</#if>>${leave.employee_name!''}<#assign total=0></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px">Casual Leave</td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.jul[0]>0)>${leave.jul[0]!''}<#assign total=total+leave.jul[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.aug[0]>0)>${leave.aug[0]!''}<#assign total=total+leave.aug[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.sep[0]>0)>${leave.sep[0]!''}<#assign total=total+leave.sep[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.oct[0]>0)>${leave.oct[0]!''}<#assign total=total+leave.oct[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.nov[0]>0)>${leave.nov[0]!''}<#assign total=total+leave.nov[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.dec[0]>0)>${leave.dec[0]!''}<#assign total=total+leave.dec[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.jan[0]>0)>${leave.jan[0]!''}<#assign total=total+leave.jan[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.feb[0]>0)>${leave.feb[0]!''}<#assign total=total+leave.feb[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.mar[0]>0)>${leave.mar[0]!''}<#assign total=total+leave.mar[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.apr[0]>0)>${leave.apr[0]!''}<#assign total=total+leave.apr[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.may[0]>0)>${leave.may[0]!''}<#assign total=total+leave.may[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000"><#if (leave.jun[0]>0)>${leave.jun[0]!''}<#assign total=total+leave.jun[0]></#if></td>
				        	<td <#if r==1>bgcolor="#abebc6 "</#if> style="font-size:10px;color:#000000">${total!'0'}</td>
				        	
				        		<#assign r=r+1>
				        	
				        </tr>
				        <tr>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px" <#assign total=0>>Sick Leave</td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.jul[1]>0)>${leave.jul[1]!''}<#assign total=total+leave.jul[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.aug[1]>0)>${leave.aug[1]!''}<#assign total=total+leave.aug[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.sep[1]>0)>${leave.sep[1]!''}<#assign total=total+leave.sep[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.oct[1]>0)>${leave.oct[1]!''}<#assign total=total+leave.oct[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.nov[1]>0)>${leave.nov[1]!''}<#assign total=total+leave.nov[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.dec[1]>0)>${leave.dec[1]!''}<#assign total=total+leave.dec[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.jan[1]>0)>${leave.jan[1]!''}<#assign total=total+leave.jan[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.feb[1]>0)>${leave.feb[1]!''}<#assign total=total+leave.feb[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.mar[1]>0)>${leave.mar[1]!''}<#assign total=total+leave.mar[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.apr[1]>0)>${leave.apr[1]!''}<#assign total=total+leave.apr[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.may[1]>0)>${leave.may[1]!''}<#assign total=total+leave.may[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000"><#if (leave.jun[1]>0)>${leave.jun[1]!''}<#assign total=total+leave.jun[1]></#if></td>
				        	<td <#if r==2>bgcolor="#f2f3f4 "</#if> style="font-size:10px;color:#000000">${total!'0'}</td>
				        	<#assign r=r+1>
				        </tr>
				        <tr>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px" <#assign total=0>>LWP</td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.jul[2]>0)>${leave.jul[2]!''}<#assign total=total+leave.jul[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.aug[2]>0)>${leave.aug[2]!''}<#assign total=total+leave.aug[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.sep[2]>0)>${leave.sep[2]!''}<#assign total=total+leave.sep[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.oct[2]>0)>${leave.oct[2]!''}<#assign total=total+leave.oct[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.nov[2]>0)>${leave.nov[2]!''}<#assign total=total+leave.nov[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.dec[2]>0)>${leave.dec[2]!''}<#assign total=total+leave.dec[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.jan[2]>0)>${leave.jan[2]!''}<#assign total=total+leave.jan[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.feb[2]>0)>${leave.feb[2]!''}<#assign total=total+leave.feb[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.mar[2]>0)>${leave.mar[2]!''}<#assign total=total+leave.mar[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.apr[2]>0)>${leave.apr[2]!''}<#assign total=total+leave.apr[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.may[2]>0)>${leave.may[2]!''}<#assign total=total+leave.may[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000"><#if (leave.jun[2]>0)>${leave.jun[2]!''}<#assign total=total+leave.jun[2]></#if></td>
				        	<td <#if r==3>bgcolor="#f5b7b1"</#if> style="font-size:10px;color:#000000">${total!'0'}</td>
				        	<#assign r=r+1>
				        </tr>
				        <tr>				        	
			        			
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff" <#assign total=0>>Absent</td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.jul[3]>0)>${leave.jul[3]!''}<#assign total=total+leave.jul[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.aug[3]>0)>${leave.aug[3]!''}<#assign total=total+leave.aug[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.sep[3]>0)>${leave.sep[3]!''}<#assign total=total+leave.sep[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.oct[3]>0)>${leave.oct[3]!''}<#assign total=total+leave.oct[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.nov[3]>0)>${leave.nov[3]!''}<#assign total=total+leave.nov[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.dec[3]>0)>${leave.dec[3]!''}<#assign total=total+leave.dec[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.jan[3]>0)>${leave.jan[3]!''}<#assign total=total+leave.jan[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.feb[3]>0)>${leave.feb[3]!''}<#assign total=total+leave.feb[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.mar[3]>0)>${leave.mar[3]!''}<#assign total=total+leave.mar[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.apr[3]>0)>${leave.apr[3]!''}<#assign total=total+leave.apr[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.may[3]>0)>${leave.may[3]!''}<#assign total=total+leave.may[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff"><#if (leave.jun[3]>0)>${leave.jun[3]!''}<#assign total=total+leave.jun[3]></#if></td>
				        	<td <#if r==4>bgcolor="#78281f"</#if> style="font-size:10px;color:#ffffff">${total!'0'}</td>
				        	<#assign r=0>
				        <tr>
				        </#list>
				    </table>
				    <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="location.href='${rc.contextPath}/reports'">Back</button>
			    </div>
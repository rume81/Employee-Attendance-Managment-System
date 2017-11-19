<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<h3><center>Re Generate Report</center></h3>
<div class="col-xs-12 no_padding">
    <div class="col-xs-1">
    </div>
    <div class="col-xs-10 no_padding">
        <div class="col-xs-12 no_padding">
            <table class="table table-bordered table-responsive">
                <tr>
        	        <td colspan="7">
        	        	<table class="table table-bordered table-responsive">
        	        		<tr>
        	        			<td width="50%">
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
        	        			<td width="50%">
        	        				<input type="text" value="" name="year" id="year" placeholder="Input Year" class="form-control">
        	        			</td>
        	        		</tr>        	        		
        	        		<tr>
        	        			<td  colspan="3">
        	        				<button type="button" title="Report Regenarat" class="btn btn-primary" onclick="reportReGenerator('${rc.contextPath}');">Report Re-Generator</button>
        	        				<!--<button type="button" title="Report Regenarat" class="btn btn-primary" onclick="test('${rc.contextPath}');">Report Re-Generator</button>-->
        	        			</td>
        	        		</tr>        	        		
        	        	</table>
        	        </td>
        	    </tr>
            </table>
        </div>
	</div>
</div>	
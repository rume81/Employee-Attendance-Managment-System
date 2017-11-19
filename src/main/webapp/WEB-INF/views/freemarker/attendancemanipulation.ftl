<script type="text/javascript" src="${rc.contextPath}/js/attendance.js"></script>
<script>
$(document).ready(function() {
	$('#att_date').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#signout_date').datepicker({ dateFormat: 'dd-mm-yy' });
	
})
</script>
<div class="col-xs-12 manipulation">
	<h3><center>Employee Attendance Manipulation</center></h3>
	<div class="col-xs-12 manipulation">
	<form class="form-horizontal" name="attmanipulation" id="attmanipulation" action="${rc.contextPath}/attmodify" method="post">
		<div class="form-group">
			<label for="" class="col-xs-3">Employee Name *</label>
			<div class="col-xs-4">
				<select name="emp_id" id="emp_id" class="form-control SlectBox">
                	<option value="">Select Employee</option>
                	<#list allEmp as employee>
                    <option value="${employee.emp_id}">${employee.employee_name!''}(${employee.avator!''})</option>
                    </#list>       
            	</select>
			</div>
		</div>
		<div class="form-group">
			<label for="" class="col-sm-3">Attendance Date *</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="att_date" name="att_date">
			</div>
		</div>
		<div class="form-group">
			<label for="" class="col-sm-3">Attendance Time *</label>
			<div class="col-sm-4">
				<select id="hour" name="hour" class="form-control" style="width:50%;float: left;">
					<option value="">Hour</option>
					<#list 1..24 as h>
    					<option value="${h}"><#if h<12>${h} AM<#else>${h-12} PM</#if></option>
  					</#list>
				</select>
				<select id="minute" name="minute" class="form-control" style="width:50%;float: left;">
					<option  value="">Minute</option>
					<#list 0..59 as m>
    					<option value="${m}">${m}</option>
  					</#list>
				</select>
				<!--<select id="sec" class="form-control" style="width:33%;float: left;">
					<option>SS</option>
					<#list 0..59 as s>
    					<option value="${s}">${s}</option>
  					</#list>
				</select>-->
			</div>
		</div>
		<div class="form-group">
			<label for="" class="col-xs-3">Attendance Type *</label>
			<div class="col-xs-4">
				<select class="form-control" id="inout" name="inout">
					<option value="0">In</option>
					<option value="1">Out</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-7">
				<button type="button" title="Save attendance" class="btn btn-primary pull-right" onclick="empAttModify('${rc.contextPath}');">Save</button>
			</div>
		</div>
	</form>	
	<!--<div class="table-responsive col-xs-7">
	    <table class="table table-bordered table-striped">
	        <thead>
	            <tr>
	                <th>1</th>
	                <th>2</th>
	                <th>3</th>
	            </tr>
	        </thead>
	        <tbody>
	            <tr>
	                <td>1</td>
	                <td>2</td>
	                <td>3</td>
	            </tr>
	        </tbody>
	    </table>
	</div>-->
	</div>
	<div class="col-xs-8 manipulation">
	<h3>Batch Signout</h3>
	<form class="form-horizontal search"  name="batchSignout" id="batchSignout" action="${rc.contextPath}/batchSignout" method="post">
		<div class="form-group">
			<label for="" class="col-xs-4 search_label">Signout Date</label>
			<div class="col-xs-5">
				<input type="text" class="form-control" id="signout_date" name="signout_date">
			</div>
			<div class="col-xs-2">
				<button type="button" title="batch attendance" class="btn btn-primary" onclick="empBatchSignout('${rc.contextPath}');">Give Signout</button>
			</div>
		</div>
	</form>
	</div>
</div>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
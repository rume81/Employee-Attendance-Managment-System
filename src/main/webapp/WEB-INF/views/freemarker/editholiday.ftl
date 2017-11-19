<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#from_date').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#to_date').datepicker({ dateFormat: 'dd-mm-yy' });
})
</script>
<h3><center>Edit Holiday Info</center></h3>
<form name="editHolidaykInfo" id="editHolidaykInfo" action="${rc.contextPath}/editholidaywork" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	        	<tr>
	        		<td class="name">Holiday Description*</td>
	        		<td colspan="3">
	        			<input type="hidden" id="id" name="id" value="${holiday.id!''}"/>
	        			<input type="text" id="holiday_desc" name="holiday_desc" class="form-control" value="${holiday.holiday_desc!''}">
	        		</td>
	        	</tr>
	        	<tr>
	        		<td class="name">Holiday From Date*</td>
	        		<td><input type="text" value="${holiday.date_from!''}" name="from_date" id="from_date" placeholder="dd-mm-yyyy" class="form-control">
	        		<br><input type="checkbox" name="oneday" onclick="copyValue(this);" value="1"> One day holiday
	        		</td>
	        		<td class="name">Holiday To Date*</td>
	                <td><input type="text" value="${holiday.date_to!''}" name="to_date" id="to_date" placeholder="dd-mm-yyyy" class="form-control"></td>
	    		</tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Edit" class="btn btn-primary" onclick="editHolidayInfo('${rc.contextPath}')">Edit</button>
        	</td>
        </tr>
	</table>
</div>
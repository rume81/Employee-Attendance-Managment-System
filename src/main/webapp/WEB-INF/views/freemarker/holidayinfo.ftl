<link rel="stylesheet" href="${rc.contextPath}/css/common/jquery.dataTables.min.css" type="text/css"/>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/employee.js"></script>
<script>
$(document).ready(function() {
	$('#from_date').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#to_date').datepicker({ dateFormat: 'dd-mm-yy' });
	$('#holidayList').DataTable({
        "order": [[ 0, "desc" ]]
    } );
})
</script>
<h3><center>Add Holiday Info</center></h3>
<form name="addHolidaykInfo" id="addHolidaykInfo" action="${rc.contextPath}/addholidaywork" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	        	<tr>
	        		<td class="name">Holiday Description*</td>
	        		<td colspan="3"><input type="text" id="holiday_desc" name="holiday_desc" class="form-control" value=""></td>
	        	</tr>
	        	<tr>
	        		<td class="name">Holiday From Date*</td>
	        		<td><input type="text" value="" name="from_date" id="from_date" placeholder="dd-mm-yyyy" class="form-control">
	        		<br><input type="checkbox" name="oneday" onclick="copyValue(this);" value="1"> One day holiday
	        		</td>
	        		<td class="name">Holiday To Date*</td>
	                <td><input type="text" value="" name="to_date" id="to_date" placeholder="dd-mm-yyyy" class="form-control"></td>
	    		</tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Save" class="btn btn-primary" onclick="addHolidayInfo('${rc.contextPath}')">Save</button>
        	</td>
        </tr>
	</table>
</div>
<div class="col-xs-12" id="holiday_list">
	<table id="holidayList" class="display" cellspacing="0" width="100%">
		<thead>
            <tr>
            	<th>Holiday Year</th>
                <th>Holiday Description</th>
                <th>From Date</th>
                <th>To Date</th>
                <th>Action</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
            	<th>Holiday Year</th>
                <th>Holiday Description</th>
                <th>From Date</th>
                <th>To Date</th>
                <th>Action</th>
            </tr>
        </tfoot>
        <tbody>
        	<#list allHoliday as holiday>
	        	<tr>
	        		<td>${holiday.holiday_year!''}</td>
	                <td>${holiday.holiday_desc!''}</td>
	                <td>${holiday.date_from!''}</td>
	                <td>${holiday.date_to!''}</td>
	                <td>
	                    <div class="btn-group">
	                        <button type="button" title="View holiday info" class="btn btn-primary" onclick=""><i class="fa fa-file-text"></i></button>
	                        <button type="button" title="Update holiday info" class="btn btn-success" onclick="editHoliday('${rc.contextPath}','${holiday.id}')"><i class="fa fa-pencil-square-o"></i></button>
	                        <button type="button" title="Delete holiday info" class="btn btn-primary" onclick="deleteHoliday('${rc.contextPath}','${holiday.id}');"><i class="fa fa-times"></i></button>
	                    </div>
	                </td>
	            </tr>
           	</#list>
        </tbody>
    </table>
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
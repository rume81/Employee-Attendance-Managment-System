<script type="text/javascript" src="${rc.contextPath}/js/leave.js"></script>

<h3><center>Add Leave Type</center></h3>
<form name="addleavetype" id="addleavetype" action="${rc.contextPath}/saveleavetype" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td class="name">Leave Type *</td>
	                <td><input type="text" id="leavetype" name="leavetype" class="form-control" value=""></td>
	    		</tr>
	    		<tr>
	    			<td class="name">Deduct From</td>
	                <td>
	                	<select name="deductfrom" id="deductfrom" class="form-control SlectBox" onChange="setFields()">
	                    	<option value="0">Own</option>
	                    	<#list leavetype as type>
	                    	<#if type.isquota>
	                        <option value="${type.id}">${type.leavetype!''}</option>
	                        </#if>
	                        </#list>          
	                    </select>
	                </td>
	    		</tr>
	    		<tr>
	    			<td class="name">Is Quota Available</td>
	                <td><input class="msgCheck" type="checkbox" name="isquota" id="isquota" value="1"> Is Quota Available</td>
	    		</tr>
	    		<tr>
	    			<td class="name">Is Continue</td>
	                <td><input class="msgCheck" type="checkbox" name="iscontinue" id="iscontinue" value="1"> Is Continue</td>
	    		</tr>
	    		<tr>
	    			<td class="name">Leave Deduct *</td>
	                <td><input type="text" id="leavededuct" name="leavededuct" class="form-control" value=""></td>
	    		</tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Save Employee" class="btn btn-primary" onclick="addLeaveType('${rc.contextPath}')">Save</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
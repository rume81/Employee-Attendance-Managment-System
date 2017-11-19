<script type="text/javascript" src="${rc.contextPath}/js/team.js"></script>

<h3><center>Add New Team</center></h3>
<form name="addteamInfo" id="addteamInfo" action="${rc.contextPath}/saveteam" method="post" enctype="multipart/form-data">
	<div class="col-xs-12">
		<div class="col-xs-12">
	    	<table class="table table-bordered table-responsive">
	    		<tr>
	    			<td class="name">Team Name *</td>
	                <td><input type="text" id="team_name" name="team_name" class="form-control" value=""></td>
	    		</tr>
	        </table>
		</div>
	</div>
</form>
<div class="col-xs-12">
  	<table class="table table-responsive">
		<tr>
        	<td>
        		<button type="button" title="Save Employee" class="btn btn-primary" onclick="addTeam('${rc.contextPath}')">Save</button>
        		<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.history.back();">Back</button>
        	</td>
        </tr>
	</table>
</div>
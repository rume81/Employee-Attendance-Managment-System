function addTeam(base){
	var team_name = jQuery('#team_name').val();
		
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(team_name == "")
	{
		fo = true;
		msg = msg + "* Team Name\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#addteamInfo').ajaxForm({
		success:function(res) {
			if(res > 0 ){
				window.location.href=base+"/teaminfo";
			}
		},
		dataType:"text"
	}).submit();
	
}
function myKeyPress(base,e){
    if(e.keycode == 13){
    	loginValided(base);
    }
}

function loginValided(base){
	
	var uname = jQuery('#username').val();
	var pass = jQuery('#password').val();
	
	if(uname == '')
	{
		jQuery('#errorMsg').html('* Please Insert User Name').show();
		return false;
	} else{
		jQuery('#errorMsg').html('').hide();
	}
	
	if(pass == '')
	{
		jQuery('#errorMsg').html('* Please Insert Password').show();
		return false;
	} else{
		jQuery('#errorMsg').html('').hide();
	}
	
	var pars =  uname+"~"+pass;
	ajaxHelper.complexAjaxExecute(base+"/users/valideduser", pars, function(res){
	   	if(res!="-1"){
	   		jQuery('#errorMsg').html('').hide();
	   		window.location.href=base+"/home";
	    }else{
	    	jQuery('#errorMsg').html('* Invalid username or password').show();
	    }
	 });
}

function logout(base){
	
	var pars =  "";
	ajaxHelper.complexAjaxExecute(base+"/user/logout", pars, function(res){
	   	if(res!="-1"){
	   		window.location.href=base+"/user/login";
	    }
	 });
}
/*function calcTime(offset) {
    // create Date object for current location
    var d = new Date();

    // convert to msec
    // subtract local time zone offset
    // get UTC time in msec
    var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
    //alert(utc);
    // create new Date object for different city
    // using supplied offset
    var difoff= 3600000*offset;
    //alert(difoff);
    var nd = new Date(utc + difoff);
    //alert(nd);
    // return time as a string
    //alert(nd.getDate()+"-"+(nd.getMonth()+1)+"-"+nd.getFullYear()+"    "+(nd.getHours()+1)+":"+(nd.getMinutes()+1)+":"+(nd.getSeconds()+1));	
    
    return nd.toLocaleString();
}*/


function attendanceAdd(base,emp_id,inout){
	
	// create Date object for current location
    //var d = new Date();
    //var offset = '+6.0';
    // convert to msec
    // subtract local time zone offset
    // get UTC time in msec
    //var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
    //alert(utc);
    // create new Date object for different city
    // using supplied offset
    //var difoff= 3600000*offset;
    //alert(difoff);
    //var nd = new Date(utc + difoff);
    
    //alert(nd);
	
	var date = 0;//nd.getFullYear()+"-"+(nd.getMonth()+1)+"-"+nd.getDate();
	var time = 0;//(nd.getHours())+":"+(nd.getMinutes())+":"+(nd.getSeconds());
	if(inout=='0')
	{
		jQuery("#lazyIn").show();
		jQuery("#btnIn").hide();
	} else{
		jQuery("#lazyOut").show();
		jQuery("#btnOut").hide();
	}
	//alert(date+"   "+time);
	var pars =  inout+"~"+date+"~"+time+"~"+emp_id;
	ajaxHelper.complexAjaxRequest(base+"/employee/att", pars, function(res){
	   	if(res!="-1"){
	   		if(inout=='0')
	   		{
	   			jQuery("#lazyIn").hide();
	   			jQuery("#btnIn").show();
	   		} else{
	   			jQuery("#lazyOut").hide();
	   			jQuery("#btnOut").show();
	   		}
	   		window.location.href=base+"/home";
	    }
	 });
}


function empAttSearch(base){
	var month = jQuery('#mon').val();
	var year = jQuery('#year').val();
	var emp_id = jQuery('#emp_id').val();
	if(month=="" || year == "" || emp_id == "")
	{
		alert("Please Select Month / Year / Employee Id.");
		return;
	}
	var pars =  month+"~"+year+"~"+emp_id;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchMonthlyAtt", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function empDailyAttSearch(base){
	var cdate = jQuery('#date').val();
	if(cdate=="")
	{
		alert("Please Select Date.");
		return;
	}
	var pars =  cdate;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchDailyAtt", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function empDailyAttSearchByTeam(base){
	var cdate = jQuery('#date').val();
	var team = jQuery('#team').val();
	if(cdate=="")
	{
		alert("Please Select Date.");
		return;
	}
	if(team=="")
	{
		alert("Please Select Team.");
		return;
	}
	var pars =  cdate+"~"+team;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchDailyAttByTeam", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function toggle(source) {
  checkboxes = document.getElementsByName('chk');
  for(var i=0, n=checkboxes.length;i<n;i++) {
    checkboxes[i].checked = source.checked;
  }
}

function toggleselect() {
  checkboxes = document.getElementsByName('chk');
  var se = 1;
  for(var i=0, n=checkboxes.length;i<n;i++) {
	  if(!checkboxes[i].checked)
	  {
		  se = 0;
		  break;
	  }
  }
  if(se==0)
  {
	  document.getElementById('chkAll').checked  = false;
  }else{
	  document.getElementById('chkAll').checked  = true;
  }
}

//using jquery.form.js
function empAttModify(base){
	var emp_id = jQuery('#emp_id').val();
	var att_date = jQuery('#att_date').val();
	var hour = jQuery('#hour').val();
	var minute = jQuery('#minute').val();
	var inout = jQuery('#inout').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(emp_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(att_date == "")
	{
		fo = true;
		msg = msg + "* Attendance Date\n";
	}
	if(hour == "")
	{
		fo = true;
		msg = msg + "* Attendance Hour\n";
	}
	if(minute == "")
	{
		fo = true;
		msg = msg + "* Attendance Minute\n";
	}
	if(inout == "")
	{
		fo = true;
		msg = msg + "* Attendance Inout\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#attmanipulation').ajaxForm({
    success:function(res) {
    	if(res!="-1"){
	 	   	window.location.href=base+"/home";
	    } else{
	    	alert("Future date is not acceptable.");
	    }
     },
     dataType:"text"
   }).submit();
}


//using jquery.form.js
function empBatchSignout(base){
	var signout_date = jQuery('#signout_date').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(signout_date == "")
	{
		fo = true;
		msg = msg + "* Signout Date\n";
	}
			
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#batchSignout').ajaxForm({
		success:function(res) {
			if(res!="-1" && res!="-2"){
	 	   		window.location.href=base+"/attendancestatus";
			} else if(res=="-2"){ 
				alert("All employee give there signout time.")
			} else{
				alert("Future date is not acceptable.");
			}
		},
		dataType:"text"
   }).submit();
}


function lunchAdd(base,emp_id,inout){
	
	if(inout=='0')
	{
		jQuery("#lazyIn").show();
		jQuery("#btnIn").hide();
	} else{
		jQuery("#lazyOut").show();
		jQuery("#btnOut").hide();
	}
	//alert(date+"   "+time);
	var pars =  inout+"~"+emp_id;
	ajaxHelper.complexAjaxRequest(base+"/employee/lunch", pars, function(res){
	   	if(res!="-1"){
	   		if(inout=='0')
	   		{
	   			jQuery("#lazyIn").hide();
	   			jQuery("#btnIn").show();
	   		} else{
	   			jQuery("#lazyOut").hide();
	   			jQuery("#btnOut").show();
	   		}
	   		window.location.href=base+"/lunch";
	    }
	 });
}

function lunchAddForGuest(base){
	
	var numberOfGuest = jQuery('#lcount').val();
	var emp_id = 0; //for Guest
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(numberOfGuest == "")
	{
		fo = true;
		msg = msg + "* Number of Guest\n";
	}
			
	if(fo)
	{
		alert(msg);
		return;
	}
	
	var pars =  numberOfGuest+"~"+emp_id;
	ajaxHelper.complexAjaxRequest(base+"/employee/guestlunch", pars, function(res){
	   	if(res!="-1"){
	   		window.location.href=base+"/lunch";
	    }
	 });
}

function empAttStatus(base){
	var mon = jQuery('#mon').val();
	var year = jQuery('#year').val();
	var team = jQuery('#team').val();
	if(mon=="")
	{
		alert("Please Select Month");
		return;
	}
	if(year=="")
	{
		alert("Please Select Year");
		return;
	}
		
	var pars =  mon+"~"+year+"~"+team;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchattstatus", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function empOvertimeStatus(base){
	var mon = jQuery('#mon').val();
	var year = jQuery('#year').val();
	var team = jQuery('#team').val();
	if(mon=="")
	{
		alert("Please Select Month");
		return;
	}
	if(year=="")
	{
		alert("Please Select Year");
		return;
	}
		
	var pars =  mon+"~"+year+"~"+team;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchovertimestatus", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function validateAttendanceDownloadForm() {
    var mon = jQuery('#mon').val();
    var year = jQuery('#year').val();
    
    if (mon == "" || year == "") {
        alert("Please Select Month and Year");
        return false;
    }
}

function setAutoLunch(base,emp_id){
	var lunchState = document.getElementById('autolunch');
	var state = lunchState.checked;
	var pars =  state+"~"+emp_id;
	ajaxHelper.complexAjaxRequest(base+"/employee/autolunchentry", pars, function(res){
	   	if(res!="-1"){
	   		window.location.href=base+"/lunch";
	    }
	 });
}

function setAutoLunchbyadmin(base,emp_id){
var lunchState = document.getElementById('chk_'+emp_id);
var state = lunchState.checked;
var pars =  state+"~"+emp_id;
ajaxHelper.complexAjaxRequest(base+"/employee/autolunchentry", pars, function(res){
   	if(res!="-1"){
    }
 });
}

function reportReGenerator(base){
	var mon = jQuery('#mon').val();
    var year = jQuery('#year').val();
    
    if(mon=="" || year=="")
    	return;
    
	var pars =  mon+"~"+year;
	ajaxHelper.complexAjaxRequest(base+"/reportregenerator", pars, function(res){
	   	if(res!="-1"){
	   		alert("Report Re generated.")
	   		//window.location.href=base+"/reportgeneratorpage";
	    }
	 });
}

/*function test(base){
	var mon = jQuery('#mon').val();
    var year = jQuery('#year').val();
    
    if(mon=="" || year=="")
    	return;
    
	var pars =  mon+"~"+year;
	ajaxHelper.complexAjaxRequest(base+"/test", pars, function(res){
	   	if(res!="-1"){
	   		alert("Report Re generated.")
	   		//window.location.href=base+"/reportgeneratorpage";
	    }
	 });
}*/


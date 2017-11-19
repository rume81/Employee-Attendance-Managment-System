function editEmp(base, id)
{
	window.location.href=base+"/employeeEdit/"+id;
}

function editWatcher(base, id)
{
	window.location.href=base+"/watcherEdit/"+id;
}

function addEmp(base)
{
	window.location.href=base+"/employeeAdd";
}

function addWatcher(base)
{
	window.location.href=base+"/watcherAdd";
}

function deleteWatcher(base, id)
{
	var con = confirm("Do you want to delete this Watcher?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/employeeDelete", pars, function(res){
			if(res!="-1"){
				window.location.href=base+"/watcherinfo";
			}
		});
	}
}

function addEmpWorkInfo(base, id)
{
	window.location.href=base+"/addEmployeeWorkInfo/"+id;
}

function viewEmpWorkInfo(base, id)
{
	window.location.href=base+"/employeeWorkInfoDisplay/"+id;
}

//using jquery.form.js
function updateEmployee(base){
	//var emp_number = jQuery('#emp_number').val();
	var emp_name = jQuery('#emp_name').val();
	//var emp_avator = jQuery('#emp_avator').val();
	var emp_email = jQuery('#emp_email').val();
	var emp_desig = jQuery('#emp_desig').val();
	var emp_contact = jQuery('#emp_contact').val();
	var usertype = jQuery('#usertype').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(emp_name == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	
	if(emp_email == "")
	{
		fo = true;
		msg = msg + "* Employee Office Email\n";
	}
	if(emp_desig == "")
	{
		fo = true;
		msg = msg + "* Employee Designation\n";
	}
	if(emp_contact == "")
	{
		fo = true;
		msg = msg + "* Employee Contact Number\n";
	}
	if(usertype == "")
	{
		fo = true;
		msg = msg + "* Employee UserType\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	var te = validateEmail(emp_email);
	if(!te)
	{
		alert("Invalid Email Address.");
		return;
	}
	
	var employee_id = jQuery('#emp_id').val();
	jQuery('#empInfo').ajaxForm({
    success:function(res) {
    	if(res == employee_id){
	 	   	window.location.href=base+"/empinfo";
	    }
     },
     dataType:"text"
   }).submit();
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function addEmployee(base){
	var emp_number = jQuery('#emp_number').val();
	var emp_name = jQuery('#emp_name').val();
	var emp_avator = jQuery('#emp_avator').val();
	var emp_email = jQuery('#emp_email').val();
	var emp_desig = jQuery('#emp_desig').val();
	var emp_contact = jQuery('#emp_contact').val();
	var emp_pass = jQuery('#emp_pass').val();
	var emp_pass1 = jQuery('#emp_pass1').val();
	var usertype = jQuery('#usertype').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(emp_number == "")
	{
		fo = true;
		msg = msg + "* Employee Number\n";
	}
	if(emp_name == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(emp_avator == "")
	{
		fo = true;
		msg = msg + "* Employee Avator\n";
	}
	if(emp_email == "")
	{
		fo = true;
		msg = msg + "* Employee Office Email\n";
	}
	if(emp_desig == "")
	{
		fo = true;
		msg = msg + "* Employee Designation\n";
	}
	if(emp_contact == "")
	{
		fo = true;
		msg = msg + "* Employee Contact Number\n";
	}
	if(emp_pass == "")
	{
		fo = true;
		msg = msg + "* Employee Password\n";
	}
	if(emp_pass1 == "")
	{
		fo = true;
		msg = msg + "* Employee Confirm Password\n";
	}
	if(usertype == "")
	{
		fo = true;
		msg = msg + "* Employee UserType\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	var te = validateEmail(emp_email);
	if(!te)
	{
		alert("Invalid Email Address.");
		return;
	}
	
	if(emp_pass==emp_pass1){
		jQuery('#addempInfo').ajaxForm({
			success:function(res) {
				if(res > 0 ){
					window.location.href=base+"/empinfo";
				}
			},
			dataType:"text"
		}).submit();
	} else{
		alert("Password not matched.")
	}
}

function addWatcherInfo(base){
	var emp_number = jQuery('#emp_number').val();
	var emp_name = jQuery('#emp_name').val();
	var emp_avator = jQuery('#emp_avator').val();
	var emp_email = jQuery('#emp_email').val();
	var emp_pass = jQuery('#emp_pass').val();
	var emp_pass1 = jQuery('#emp_pass1').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(emp_number == "")
	{
		fo = true;
		msg = msg + "* Watcher Number\n";
	}
	if(emp_name == "")
	{
		fo = true;
		msg = msg + "* Watcher Name\n";
	}
	if(emp_avator == "")
	{
		fo = true;
		msg = msg + "* User Name\n";
	}
	if(emp_email == "")
	{
		fo = true;
		msg = msg + "* Office Email\n";
	}
	if(emp_pass == "")
	{
		fo = true;
		msg = msg + "* Watcher Password\n";
	}
	if(emp_pass1 == "")
	{
		fo = true;
		msg = msg + "* Watcher Confirm Password\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	var te = validateEmail(emp_email);
	if(!te)
	{
		alert("Invalid Email Address.");
		return;
	}
	
	if(emp_pass==emp_pass1){
		jQuery('#addwatcherInfo').ajaxForm({
			success:function(res) {
				if(res > 0 ){
					window.location.href=base+"/watcherinfo";
				}
			},
			dataType:"text"
		}).submit();
	} else{
		alert("Password not matched.")
	}
}

function updateWatcher(base){
	var emp_name = jQuery('#emp_name').val();
	var emp_email = jQuery('#emp_email').val();
	//var emp_desig = jQuery('#emp_desig').val();
	//var emp_contact = jQuery('#emp_contact').val();
	//var usertype = jQuery('#usertype').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(emp_name == "")
	{
		fo = true;
		msg = msg + "* Watcher Name\n";
	}
	
	if(emp_email == "")
	{
		fo = true;
		msg = msg + "* Office Email\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	var te = validateEmail(emp_email);
	if(!te)
	{
		alert("Invalid Email Address.");
		return;
	}
	
	var employee_id = jQuery('#emp_id').val();
	jQuery('#watcherInfo').ajaxForm({
    success:function(res) {
    	if(res == employee_id){
	 	   	window.location.href=base+"/watcherinfo";
	    }
     },
     dataType:"text"
   }).submit();
}

function addEmployeeWorkInfo(base){
	var off_start = jQuery('#off_start').val();
	var working_hour = jQuery('#working_hour').val();
	var from_date = jQuery('#from_date').val();
	
	var checkedValue = ""; 
	var inputElements = document.getElementsByClassName('msgCheck');
	for(var i=0; inputElements[i]; ++i){
	      if(inputElements[i].checked){
	           checkedValue = checkedValue+" "+inputElements[i].value;
	          
	      }
	}
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(off_start == "")
	{
		fo = true;
		msg = msg + "* Office Start Time\n";
	}
	if(working_hour == "")
	{
		fo = true;
		msg = msg + "* Working hour\n";
	}
	if(checkedValue == "")
	{
		fo = true;
		msg = msg + "* Weekend\n";
	}
	if(from_date == "")
	{
		fo = true;
		msg = msg + "* From Date\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#addempworkInfo').ajaxForm({
		success:function(res) {
			if(res > 0){
				window.location.href=base+"/empWorkInfo";
			}
		},
		dataType:"text"
	}).submit();
}

function addHolidayInfo(base){
	var holiday_desc = jQuery('#holiday_desc').val();
	var from_date = jQuery('#from_date').val();
	var to_date = jQuery('#to_date').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(holiday_desc == "")
	{
		fo = true;
		msg = msg + "* Holiday Description\n";
	}
	if(from_date == "")
	{
		fo = true;
		msg = msg + "* From Date\n";
	}
	if(to_date == "")
	{
		fo = true;
		msg = msg + "* To Date\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#addHolidaykInfo').ajaxForm({
		success:function(res) {
			if(res > 0){
				window.location.href=base+"/holidainfo";
			}
		},
		dataType:"text"
	}).submit();
}

function editHolidayInfo(base){
	var holiday_desc = jQuery('#holiday_desc').val();
	var from_date = jQuery('#from_date').val();
	var to_date = jQuery('#to_date').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(holiday_desc == "")
	{
		fo = true;
		msg = msg + "* Holiday Description\n";
	}
	if(from_date == "")
	{
		fo = true;
		msg = msg + "* From Date\n";
	}
	if(to_date == "")
	{
		fo = true;
		msg = msg + "* To Date\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#editHolidaykInfo').ajaxForm({
		success:function(res) {
			if(res > 0){
				window.location.href=base+"/holidainfo";
			}
		},
		dataType:"text"
	}).submit();
}

function copyValue(cb) {
  if(cb.checked)
  {
	  var fromdate = jQuery('#from_date').val();
	  if(fromdate != "")
	  {
		  jQuery('#to_date').val(fromdate);
	  }
  } else{
	  jQuery('#to_date').val('');
  }
}

function changePass(base,userType)
{
	var employee_id = jQuery('#emp_id').val();
	var old_pass = "";
	if(userType!="Admin")
		old_pass = jQuery('#old_pass').val();
	var new_pass = jQuery('#new_pass').val();
	var confirm_pass = jQuery('#confirm_pass').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	if(employee_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(old_pass == "" && userType!="Admin")
	{
		fo = true;
		msg = msg + "* Old Password\n";
	}
	if(new_pass == "")
	{
		fo = true;
		msg = msg + "* New Password\n";
	}
	if(confirm_pass == "")
	{
		fo = true;
		msg = msg + "* Confirm Password\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	if(confirm_pass != new_pass)
	{
		alert("New password and Confirm Password not matched.");
		return;
	}
	
	jQuery('#changepass').ajaxForm({
    success:function(res) {
    	if(res != "-1" ){
	 	   	window.location.href=base+"/";
	    } 
    	else
	    {
    		alert("Password not update, Old password not matched.");
	    }
     },
     dataType:"text"
   }).submit();
}

function deleteEmp(base, id)
{
	var con = confirm("Do you want to delete this Employee?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/employeeDelete", pars, function(res){
			if(res!="-1"){
				window.location.href=base+"/empinfo";
			}
		});
	}
}
function editHoliday(base, id)
{
	window.location.href=base+"/editholiday/"+id;
}
function deleteHoliday(base, id)
{
	var con = confirm("Do you want to delete this holiday?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/holidayDelete", pars, function(res){
		   	if(res!="-1"){
		   		window.location.href=base+"/holidainfo";
		    }
		 });
	}
}

function toggle(source) {
  checkboxes = document.getElementsByName('chk');
  for(var i=0, n=checkboxes.length;i<n;i++) {
    checkboxes[i].checked = source.checked;
  }
}

function toggleselect(me) {
  checkboxes = document.getElementsByName('chk');
  var se = 0;
  for(var i=0, n=checkboxes.length;i<n;i++) {
	  if(checkboxes[i].checked)
	  {
		  se = se + 1;
	  }
  }
  if(se>5)
  {
	  document.getElementById(me).checked  = false;
	  alert("Not possible to select more then 5 approver.")
  }
}

function viewEmpLeaveQuota(base, id)
{
	window.location.href=base+"/viewEmployeeLeaveQuota/"+id;
}

function addEmpLeaveQuota(base, id)
{
	window.location.href=base+"/addEmployeeLeaveQuota/"+id;
}

function empleavequotasave(base)
{
	
	var year = jQuery('#year').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(year == "")
	{
		fo = true;
		msg = msg + "* Year\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#saveempleavequota').ajaxForm({
		success:function(res) {
			if(res > 0){
				window.location.href=base+"/leaveQuota";
			}
		},
		dataType:"text"
	}).submit();
}
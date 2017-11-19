function toggle_it(itemID){ 
	if(itemID == 'appshow')
	{
		var nextitem = Number(document.getElementById(itemID).value);
		if(nextitem<5)
		{
			nextitem = nextitem +1;
			document.getElementById('appshow').value = nextitem;
			document.getElementById('app'+nextitem).style.display = '';
		}
	}
} 
function toggle_hide(itemID){
	if(itemID == 'appshow')
	{
		var curitem = Number(document.getElementById(itemID).value);
		if(curitem>1)
		{
			document.getElementById('app'+curitem).style.display = 'none';
			curitem = curitem - 1;
			document.getElementById('appshow').value = curitem;
		}
	}
}

function changeDateFormat(date)
{
	var spdate = date.split("-");
	var fdate = spdate[1]+"/"+spdate[0]+"/"+spdate[2];
	
	return fdate;
}

function noOfLeaveCalculation(base,excludeLeave)//0 for includeleave, 1 for excludeLeave
{
	var employee_id = jQuery('#emp_id').val();
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	var leavetypeid = jQuery('#leavetype').val();
	var selectedyear= jQuery('#year').val();
	var days=0;
	if(excludeLeave==1){
		days= jQuery('#prevdays').val();
	}
	if(fdate !="" && tdate != "" && employee_id !="" && leavetypeid != "" && selectedyear != "")
	{
		var date1 = new Date(changeDateFormat(fdate));
		var date2 = new Date(changeDateFormat(tdate));
		var timeDiff = date2.getTime() - date1.getTime();
		var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
		if(diffDays<0)
		{	
			alert("Please select valid date.");
			return;
		}
		var pars =  fdate+"~"+tdate+"~"+employee_id+"~"+excludeLeave+"~"+leavetypeid+"~"+selectedyear+"~"+days;
		ajaxHelper.complexAjaxRequest(base+"/calculateLeave", pars, function(res){
		   	if((res!="-1") && (!res.includes("Not"))){
		   		var data = res.split('~');
		   		jQuery('#days').val(data[1]);
		   		jQuery('#leavelist').html(data[0]);
		    } else if(res.includes("Not")){ 
		    	jQuery('#days').val('');
		   		jQuery('#leavelist').html('');
		    	alert(res);
		    } else{
		    	jQuery('#days').val('');
		   		jQuery('#leavelist').html('');
		    	alert("Invalid live date, Select valid date.");
		    }
		 });
	} else{
		alert("Please fill the Required field.");
	}
}

//using jquery.form.js
function addLeave(base){
	var employee_id = jQuery('#emp_id').val();
	var leavetype = jQuery('#leavetype').val();
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	var reason = jQuery('#reason').val();
	var days = jQuery('#days').val();
	var approverSize = jQuery('#appsize').val();
	var fo = false;
	var msg = "Please fill required field:\n";
	if(employee_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(leavetype == "")
	{
		fo = true;
		msg = msg + "* Leave Type\n";
	}
	if(fdate == "")
	{
		fo = true;
		msg = msg + "* Leave From Date\n";
	}
	if(tdate == "")
	{
		fo = true;
		msg = msg + "* Leave To Date\n";
	}
	if(reason == "")
	{
		fo = true;
		msg = msg + "* Reason\n";
	}
	if(days == "")
	{
		fo = true;
		msg = msg + "* Leave Validate\n";
	}
	if(approverSize>=1){
		var approvar1 = jQuery('#approvar1').val();
		if(approvar1 == "")
		{
			fo = true;
			msg = msg + "* First Approvar\n";
		}
	}
	if(approverSize>=2){
		var approvar2 = jQuery('#approvar2').val();
		if(approvar2 == "")
		{
			fo = true;
			msg = msg + "* Second Approvar\n";
		}
	}
	if(approverSize>=3){
		var approvar3 = jQuery('#approvar3').val();
		if(approvar3 == "")
		{
			fo = true;
			msg = msg + "* Thierd Approvar\n";
		}
	}
	if(approverSize>=4){
		var approvar4 = jQuery('#approvar4').val();
		if(approvar4 == "")
		{
			fo = true;
			msg = msg + "* Fourth Approvar\n";
		}
	}
	if(approverSize>=5){
		var approvar5 = jQuery('#approvar5').val();
		if(approvar5 == "")
		{
			fo = true;
			msg = msg + "* Fifth Approvar\n";
		}
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	jQuery("#lazy").show();
	jQuery("#applied").hide();
	jQuery('#leaveInfo').ajaxForm({
    success:function(res) {
    	if(res == employee_id){
	 	   	window.location.href=base+"/leavelist";
	    }
     },
     dataType:"text"
   }).submit();
}


function addLeavePage(base)
{
	window.location.href=base+"/leaverequest";
}

function addOfficeOutgoing(base){
	var employee_id = jQuery('#emp_id').val();
	var date = jQuery('#date').val();
	var ftime = jQuery('#ftime').val();
	var ttime = jQuery('#ttime').val();
	var reason = jQuery('#reason').val();
	var approverSize = jQuery('#appsize').val();
	var fo = false;
	var msg = "Please fill required field:\n";
	if(employee_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(date == "")
	{
		fo = true;
		msg = msg + "* Outgoing Date\n";
	}
	if(ftime == "")
	{
		fo = true;
		msg = msg + "* Outgoing From Time\n";
	}
	if(ttime == "")
	{
		fo = true;
		msg = msg + "* Outgoing To Time\n";
	}
	if(reason == "")
	{
		fo = true;
		msg = msg + "* Reason\n";
	}
	
	if(approverSize>=1){
		var approvar1 = jQuery('#approvar1').val();
		if(approvar1 == "")
		{
			fo = true;
			msg = msg + "* First Approvar\n";
		}
	}
	if(approverSize>=2){
		var approvar2 = jQuery('#approvar2').val();
		if(approvar2 == "")
		{
			fo = true;
			msg = msg + "* Second Approvar\n";
		}
	}
	if(approverSize>=3){
		var approvar3 = jQuery('#approvar3').val();
		if(approvar3 == "")
		{
			fo = true;
			msg = msg + "* Thierd Approvar\n";
		}
	}
	if(approverSize>=4){
		var approvar4 = jQuery('#approvar4').val();
		if(approvar4 == "")
		{
			fo = true;
			msg = msg + "* Fourth Approvar\n";
		}
	}
	if(approverSize>=5){
		var approvar5 = jQuery('#approvar5').val();
		if(approvar5 == "")
		{
			fo = true;
			msg = msg + "* Fifth Approvar\n";
		}
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	jQuery("#lazy").show();
	jQuery("#applied").hide();
	jQuery('#outgoingInfo').ajaxForm({
    success:function(res) {
    	if(res == employee_id){
	 	   	window.location.href=base+"/officeoutgoinglist";
	    }
     },
     dataType:"text"
   }).submit();
}
function addOfficeOutgoingPage(base)
{
	window.location.href=base+"/officeoutgoingrequest";
}

function viewLeavePage(base, id, call)
{
	window.location.href=base+"/viewleaverequest/"+id+"/"+call;
}

function viewOutgoingPage(base, id, call)
{
	window.location.href=base+"/viewoutgoingrequest/"+id+"/"+call;
}

function approveRejectLeave(base,lid,appRej)
{
	var comments=""; 
	if(appRej==1)
	{
		comments = jQuery("#approval_comments"+lid).val();
	} 
	else
	{
		comments = jQuery("#reject_comments"+lid).val();
	}
	//alert(comments+"  "+lid+"  "+appRej);
	if(appRej==1){
		jQuery("#lazyap"+lid).show();
		jQuery("#approved"+lid).attr('disabled','disabled');
	} else{
		jQuery("#lazyre"+lid).show();
		jQuery("#reject"+lid).attr('disabled','disabled');
	}
	
	var pars =  comments+"~"+lid+"~"+appRej;
	ajaxHelper.complexAjaxRequest(base+"/approveRejectLeave", pars, function(res){
	 	if(res!="-1"){
	 		if(appRej==1)
	 		{
	 			jQuery("#approval_comments"+lid).val('');
	 			jQuery("#lazyap"+lid).hide();
	 			jQuery("#approved"+lid).removeAttr('disabled');
	 			
	 			window.location.href=base+"/leaveapprovalList";
	 		} 
	 		else
	 		{
	 			jQuery("#reject_comments"+lid).val('');
	 			jQuery("#lazyre"+lid).hide();
 				jQuery("#reject"+lid).removeAttr('disabled');
	 			window.location.href=base+"/leaveapprovalList";
	 		}
	    } else{
	    	alert("Not able to Save.");
	    }
	});
}

function approveRejectOutgoing(base,lid,appRej)
{
	var comments=""; 
	if(appRej==1)
	{
		comments = jQuery("#approval_comments"+lid).val();
	} 
	else
	{
		comments = jQuery("#reject_comments"+lid).val();
	}
	//alert(comments+"  "+lid+"  "+appRej);
	if(appRej==1){
		jQuery("#lazyap"+lid).show();
		jQuery("#approved"+lid).attr('disabled','disabled');
	} else{
		jQuery("#lazyre"+lid).show();
		jQuery("#reject"+lid).attr('disabled','disabled');
	}
	
	var pars =  comments+"~"+lid+"~"+appRej;
	ajaxHelper.complexAjaxRequest(base+"/approveRejectOutgoing", pars, function(res){
	 	if(res!="-1"){
	 		if(appRej==1)
	 		{
	 			jQuery("#approval_comments"+lid).val('');
	 			jQuery("#lazyap"+lid).hide();
	 			jQuery("#approved"+lid).removeAttr('disabled');
	 			
	 			window.location.href=base+"/outgoingapprovalList";
	 		} 
	 		else
	 		{
	 			jQuery("#reject_comments"+lid).val('');
	 			jQuery("#lazyre"+lid).hide();
 				jQuery("#reject"+lid).removeAttr('disabled');
	 			window.location.href=base+"/outgoingapprovalList";
	 		}
	    } else{
	    	alert("Not able to Save.");
	    }
	});
}

function CancelLeave(base, id)
{
	var con = confirm("Do you want to cancel this Leave?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/cancelLeave", pars, function(res){
		 	if(res!="-1"){
		 		window.location.href=base+"/leavelist";
		    }
		});
	}
}

function CancelOutgoing(base, id)
{
	var con = confirm("Do you want to cancel this Office Outgoing request?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/cancelOutgoing", pars, function(res){
		 	if(res!="-1"){
		 		window.location.href=base+"/officeoutgoinglist";
		    }
		});
	}
}

function DeleteLeave(base, id)
{
	var con = confirm("Do you want to delete this Leave?");
	
	if(con == true)
	{
		var pars =  id;
		ajaxHelper.complexAjaxRequest(base+"/cancelLeave", pars, function(res){
		 	if(res!="-1"){
		 		window.location.href=base+"/approvedleave";
		    }
		});
	}
}

function EditLeave(base, id)
{
	window.location.href=base+"/editleaverequest/"+id;
}

function EditLeaveByIndivitual(base, id)
{
	window.location.href=base+"/editleavebyIndivitual/"+id;
}

function EditOutgoingByIndivitual(base, id)
{
	window.location.href=base+"/editoutgoingbyIndivitual/"+id;
}

function UpdateLeave(base)
{
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(fdate == "")
	{
		fo = true;
		msg = msg + "* Leave From Date\n";
	}
	if(tdate == "")
	{
		fo = true;
		msg = msg + "* Leave To Date\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery("#lazy").show();
	jQuery("#btnedit").hide();
	jQuery('#leaveInfo').ajaxForm({
    success:function(res) {
    	if(res == 1){
	 	   	window.location.href=base+"/approvedleave";
	    }
     },
     dataType:"text"
   }).submit();
}

function UpdateLeaveIndivitual(base){
	var employee_id = jQuery('#emp_id').val();
	var leavetype = jQuery('#leavetype').val();
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	var reason = jQuery('#reason').val();
	var days = jQuery('#days').val();
	var approverSize = jQuery('#appsize').val();
	var fo = false;
	var msg = "Please fill required field:\n";
	if(employee_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(leavetype == "")
	{
		fo = true;
		msg = msg + "* Leave Type\n";
	}
	if(fdate == "")
	{
		fo = true;
		msg = msg + "* Leave From Date\n";
	}
	if(tdate == "")
	{
		fo = true;
		msg = msg + "* Leave To Date\n";
	}
	if(reason == "")
	{
		fo = true;
		msg = msg + "* Reason\n";
	}
	if(days == "")
	{
		fo = true;
		msg = msg + "* Leave Validate\n";
	}
	if(approverSize>=1){
		var approvar1 = jQuery('#approvar1').val();
		if(approvar1 == "")
		{
			fo = true;
			msg = msg + "* First Approvar\n";
		}
	}
	if(approverSize>=2){
		var approvar2 = jQuery('#approvar2').val();
		if(approvar2 == "")
		{
			fo = true;
			msg = msg + "* Second Approvar\n";
		}
	}
	if(approverSize>=3){
		var approvar3 = jQuery('#approvar3').val();
		if(approvar3 == "")
		{
			fo = true;
			msg = msg + "* Thierd Approvar\n";
		}
	}
	if(approverSize>=4){
		var approvar4 = jQuery('#approvar4').val();
		if(approvar4 == "")
		{
			fo = true;
			msg = msg + "* Fourth Approvar\n";
		}
	}
	if(approverSize>=5){
		var approvar5 = jQuery('#approvar5').val();
		if(approvar5 == "")
		{
			fo = true;
			msg = msg + "* Fifth Approvar\n";
		}
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	jQuery("#lazy").show();
	jQuery("#btnedit").hide();
	jQuery('#editleaveInfo').ajaxForm({
    success:function(res) {
    	if(res == 1){
	 	   	window.location.href=base+"/leavelist";
	    }
     },
     dataType:"text"
   }).submit();
}

function UpdateOutgoingIndivitual(base){
	var employee_id = jQuery('#emp_id').val();
	var date = jQuery('#date').val();
	var ftime = jQuery('#ftime').val();
	var ttime = jQuery('#ttime').val();
	var reason = jQuery('#reason').val();
	var approverSize = jQuery('#appsize').val();
	var fo = false;
	var msg = "Please fill required field:\n";
	if(employee_id == "")
	{
		fo = true;
		msg = msg + "* Employee Name\n";
	}
	if(date == "")
	{
		fo = true;
		msg = msg + "* Outgoing Date\n";
	}
	if(ftime == "")
	{
		fo = true;
		msg = msg + "* Outgoing From Time\n";
	}
	if(ttime == "")
	{
		fo = true;
		msg = msg + "* Outgoing To Time\n";
	}
	if(reason == "")
	{
		fo = true;
		msg = msg + "* Reason\n";
	}
	if(approverSize>=1){
		var approvar1 = jQuery('#approvar1').val();
		if(approvar1 == "")
		{
			fo = true;
			msg = msg + "* First Approvar\n";
		}
	}
	if(approverSize>=2){
		var approvar2 = jQuery('#approvar2').val();
		if(approvar2 == "")
		{
			fo = true;
			msg = msg + "* Second Approvar\n";
		}
	}
	if(approverSize>=3){
		var approvar3 = jQuery('#approvar3').val();
		if(approvar3 == "")
		{
			fo = true;
			msg = msg + "* Thierd Approvar\n";
		}
	}
	if(approverSize>=4){
		var approvar4 = jQuery('#approvar4').val();
		if(approvar4 == "")
		{
			fo = true;
			msg = msg + "* Fourth Approvar\n";
		}
	}
	if(approverSize>=5){
		var approvar5 = jQuery('#approvar5').val();
		if(approvar5 == "")
		{
			fo = true;
			msg = msg + "* Fifth Approvar\n";
		}
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	jQuery("#lazy").show();
	jQuery("#btnedit").hide();
	jQuery('#editoutgoingInfo').ajaxForm({
    success:function(res) {
    	if(res == 1){
	 	   	window.location.href=base+"/officeoutgoinglist";
	    }
     },
     dataType:"text"
   }).submit();
}

function getApprovar(base, selItem,call)
{
	var value = selItem.value; 
	if(value=="")
	{
		return;
	}
	var pars =  value+"~"+call;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/approverList", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#approverList').html(res);
	    }
	 });	
}

function getLeave(base,emp_id,date,stat)
{
	//alert(emp_id+" "+date);
	window.location.href=base+"/viewleavedetails/"+emp_id+"/"+date+"/"+stat;
}

function goBack() {
    window.history.back();
}

function getLeaveSearch(base)
{
	var mon = jQuery("#mon").val();
	var year = jQuery("#year").val();
	
	if(mon==" " || year == "")
		return;
	var pars =  mon+"~"+year;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/leavedetailssearch", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#leavelist').html(res);
	    }
	 });	
}

function getYearlyLeave(base)
{
	var year = jQuery("#year").val();
	
	if(year == "")
		return;
	var pars =  year;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/yearlyleavesearch", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });	
}

function copyValue(cb) {
  if(cb.checked)
  {
	  var fromdate = jQuery('#fdate').val();
	  if(fromdate != "")
	  {
		  jQuery('#tdate').val(fromdate);
	  }
  } else{
	  jQuery('#tdate').val('');
  }
}

function ApprovedLeaveSearch(base){
	var sdate = jQuery('#sdate').val();
	var edate = jQuery('#edate').val();
	if(sdate=="")
	{
		alert("Please Select From Date");
		return;
	}
	
	if(edate=="")
	{
		alert("Please Select To Date");
		return;
	}
	var pars =  sdate+"~"+edate;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/searchapprovedleave", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#searchRes').html(res);
	    }
	 });
}

function checkDuplicate(id,size)
{
	var cur_approvar = jQuery('#approvar'+id).val();
	for(var x=1;x<=size;x++)
	{
		if(x!=id)
		{
			var approvar = jQuery('#approvar'+x).val();
			if(cur_approvar==approvar)
			{
				alert("You select one approvar in multiple times.");
				jQuery('#approvar'+id).val(''); 
			}
			
		}
	}
}

function addLeaveType(base){
	var leavetype = jQuery('#leavetype').val();
	var leavededuct = jQuery('#leavededuct').val();
		
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(leavetype == "")
	{
		fo = true;
		msg = msg + "* Leave Type\n";
	}
	
	if(leavededuct == "")
	{
		fo = true;
		msg = msg + "* Leave Deduct\n";
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#addleavetype').ajaxForm({
		success:function(res) {
			if(res > 0 ){
				window.location.href=base+"/leavetype";
			}
		},
		dataType:"text"
	}).submit();
	
}

function editLeaveType(base, id)
{
	window.location.href=base+"/leaveTypeEdit/"+id;
}

function updateLeaveType(base){
	var leavetype = jQuery('#leavetype').val();
	var leavededuct = jQuery('#leavededuct').val();
		
	var fo = false;
	var msg = "Please fill required field:\n";
	
	if(leavetype == "")
	{
		fo = true;
		msg = msg + "* Leave Type\n";
	}
	
	if(leavededuct == "")
	{
		fo = true;
		msg = msg + "* Leave Deduct\n";
	}
		
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery('#editleavetype').ajaxForm({
		success:function(res) {
			if(res > 0 ){
				window.location.href=base+"/leavetype";
			}
		},
		dataType:"text"
	}).submit();
	
}

function setFields(){
	var deductfrom = jQuery('#deductfrom').val();
	
	if(deductfrom!=0){
		jQuery('#isquota').attr("disabled", true);
		jQuery('#iscontinue').attr("disabled", true);
		
	} else{
		jQuery('#isquota').removeAttr("disabled");
		jQuery('#iscontinue').removeAttr("disabled");
	}
}

function getQuota(base)
{
	var quotayear = jQuery('#year').val();
	var emp_id = jQuery('#emp_id').val();
	
	if(quotayear!="" && emp_id != "") {
		var pars =  emp_id+"~"+quotayear;
		ajaxHelper.complexAjaxRequest(base+"/getQuota", pars, function(res){
			 if(res!="-1"){
				// alert(res);
			 	jQuery('#quotadisplay').html(res);
			 }
		});
	}	
}

function noOfDaysCalculation(base)
{
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	var selectedyear= jQuery('#year').val();
	var days=0;
	
	if(fdate !="" && tdate != "" && selectedyear != "")
	{
		var date1 = new Date(changeDateFormat(fdate));
		var date2 = new Date(changeDateFormat(tdate));
		var timeDiff = date2.getTime() - date1.getTime();
		var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
		if(diffDays<0)
		{	
			alert("Please select valid date.");
			return;
		}
		jQuery('#days').val(diffDays+1);
	} else{
		alert("Please fill the Required field.");
	}
}

function addBatchLeave(base){
	//var employee_id = jQuery('#emp_id').val();
	//var leavetype = jQuery('#leavetype').val();
	checkboxes = document.getElementsByName('chk');
	//alert(checkboxes);
	var fdate = jQuery('#fdate').val();
	var tdate = jQuery('#tdate').val();
	var reason = jQuery('#reason').val();
	var days = jQuery('#days').val();
	var approverSize = jQuery('#appsize').val();
	var fo = false;
	var msg = "Please fill required field:\n";
	
	var select = 0;
	var n=checkboxes.length;
	for(var i=0; i<n;i++) {
		if(checkboxes[i].checked)
		{
			select = 1;
			break;
		}
	}
	
	if(select != 1)
	{
		fo = true;
		msg = msg + "* Select Employee \n";
	}
	/*if(leavetype == "")
	{
		fo = true;
		msg = msg + "* Leave Type\n";
	}*/
	if(fdate == "")
	{
		fo = true;
		msg = msg + "* Leave From Date\n";
	}
	if(tdate == "")
	{
		fo = true;
		msg = msg + "* Leave To Date\n";
	}
	if(reason == "")
	{
		fo = true;
		msg = msg + "* Reason\n";
	}
	if(days == "")
	{
		fo = true;
		msg = msg + "* Leave Validate\n";
	}
	if(approverSize>=1){
		var approvar1 = jQuery('#approvar1').val();
		if(approvar1 == "")
		{
			fo = true;
			msg = msg + "* First Approvar\n";
		}
	}
	if(approverSize>=2){
		var approvar2 = jQuery('#approvar2').val();
		if(approvar2 == "")
		{
			fo = true;
			msg = msg + "* Second Approvar\n";
		}
	}
	if(approverSize>=3){
		var approvar3 = jQuery('#approvar3').val();
		if(approvar3 == "")
		{
			fo = true;
			msg = msg + "* Thierd Approvar\n";
		}
	}
	if(approverSize>=4){
		var approvar4 = jQuery('#approvar4').val();
		if(approvar4 == "")
		{
			fo = true;
			msg = msg + "* Fourth Approvar\n";
		}
	}
	if(approverSize>=5){
		var approvar5 = jQuery('#approvar5').val();
		if(approvar5 == "")
		{
			fo = true;
			msg = msg + "* Fifth Approvar\n";
		}
	}
	
	if(fo)
	{
		alert(msg);
		return;
	}
	
	jQuery("#lazy").show();
	jQuery("#applied").hide();
	jQuery('#batchleave').ajaxForm({
		success:function(res) {
	    	if(res == 'Ok'){
		 	   	window.location.href=base+"/adminpanel";
		    }
	     },
	     dataType:"text"
   }).submit();
}

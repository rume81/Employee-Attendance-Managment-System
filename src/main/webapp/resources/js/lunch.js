function getLunchSearch(base)
{
	var mon = jQuery("#mon").val();
	var year = jQuery("#year").val();
	
	if(mon==" " || year == "")
		return;
	var pars =  mon+"~"+year;
	//alert(pars);
	ajaxHelper.complexAjaxRequest(base+"/lunchdetailssearch", pars, function(res){
	   	if(res!="-1"){
	   		//alert(res);
	   		jQuery('#lunchlist').html(res);
	    }
	 });	
}
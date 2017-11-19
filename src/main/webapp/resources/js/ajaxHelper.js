 /**
 * Class  with  help  methods for ajax
 */
function ajaxHelper(objName) {
	this.objName = objName;
    //this.loadingMessageTitle = "Loading Window";
    //this.loadingMessage = "Loading data. Please wait...";
    
    /**
     * Simple ajax request
     * @param url
     * @param pars
     * @param actionInsertId
     */
    this.simpleAjaxRequest = function(url, pars, actionInsertId){//alert(url+" "+pars+" "+actionInsertId);
        jQuery.ajax({
            type:"POST",
            timeout: 300000, // 5 - minutes
            async:true,
            url: url,
            data: pars,
            cache:false,
            beforeSend:function() {
                
            },

            success: function(responce) {
                if(jQuery("#" + actionInsertId)!=null && jQuery("#" + actionInsertId)!=undefined){
                	jQuery("#" + actionInsertId).html(responce);
                }
            },

            error:function(xhr) {
                if(xhr!=null && xhr!=undefined){
                    if(xhr.status==310){
                      alert(xhr.responseText);
                    }else if(xhr.status==320){
                    	alert(xhr.responseText);
                    }else if(xhr.status==330){
                    	alert(xhr.responseText);
                    }else if(xhr.status==403){
                    	alert(xhr.responseText);
                    }else if(xhr.status==401){
                    	alert(xhr.responseText);
                    }else{
                    	alert(xhr.responseText);
                    	//jQuery("#" + actionInsertId).html('<input type="hidden" id="viewType" value="0"/><font class="black_text" style="color:red;">Désolé, mais aucune réponse ne correspond à cette requête.<br />Modifiez vos critères de recherche.</font>');
                    }
                }
            }
        });
    };
    
    /**
     * Complex ajax call
     * @param url
     * @param pars
     * @param callBakFuncName
     */
     this.complexAjaxRequest = function(url, pars, callBakFuncName){//alert(url+" "+pars+" "+actionInsertId);
         jQuery.ajax({
             type:"POST",
             timeout: 300000, // 5 - minutes
             async:true,
             url: url,
             data: pars,
             cache:false,
             beforeSend:function() {
            	 jQuery.blockUI({ css: { 
                     border: 'none', 
                     padding: '15px', 
                     backgroundColor: '#000', 
                     '-webkit-border-radius': '10px', 
                     '-moz-border-radius': '10px', 
                     opacity: .5, 
                     color: '#fff' 
                 } }); 
             },

             success: function(responce) {
            	 if(callBakFuncName!=null && callBakFuncName!=undefined){
            	        // you can use this param as parameter in callBackFunctionName: alert(res)
            	        eval(callBakFuncName(responce));
            	        jQuery.unblockUI();
            	    }
             },

             error:function(xhr) {
                 if(xhr!=null && xhr!=undefined){
                     if(xhr.status==310){
                       alert(xhr.responseText);
                     }else if(xhr.status==320){
                     	alert(xhr.responseText);
                     }else if(xhr.status==330){
                     	alert(xhr.responseText);
                     }else if(xhr.status==403){
                     	alert(xhr.responseText);
                     }else if(xhr.status==401){
                     	alert(xhr.responseText);
                     }else{
                     	alert(xhr.responseText);
                     	//jQuery("#" + actionInsertId).html('<input type="hidden" id="viewType" value="0"/><font class="black_text" style="color:red;">Désolé, mais aucune réponse ne correspond à cette requête.<br />Modifiez vos critères de recherche.</font>');
                     }
                 }
             }
         });
     };
     
     /**
      * Complex ajax Request without screen block
      * @param url
      * @param pars
      * @param callBakFuncName
      */
      this.complexAjaxExecute = function(url, pars, callBakFuncName){//alert(url+" "+pars+" "+actionInsertId);
          jQuery.ajax({
              type:"POST",
              timeout: 300000, // 5 - minutes
              async:true,
              url: url,
              data: pars,
              cache:false,
              beforeSend:function() {
              },

              success: function(responce) {
             	 if(callBakFuncName!=null && callBakFuncName!=undefined){
             	        // you can use this param as parameter in callBackFunctionName: alert(res)
             	        eval(callBakFuncName(responce));
             	    }
              },

              error:function(xhr) {
                  if(xhr!=null && xhr!=undefined){
                      if(xhr.status==310){
                        alert(xhr.responseText);
                      }else if(xhr.status==320){
                      	alert(xhr.responseText);
                      }else if(xhr.status==330){
                      	alert(xhr.responseText);
                      }else if(xhr.status==403){
                      	alert(xhr.responseText);
                      }else if(xhr.status==401){
                      	alert(xhr.responseText);
                      }else{
                      	alert(xhr.responseText);
                      	//jQuery("#" + actionInsertId).html('<input type="hidden" id="viewType" value="0"/><font class="black_text" style="color:red;">Désolé, mais aucune réponse ne correspond à cette requête.<br />Modifiez vos critères de recherche.</font>');
                      }
                  }
              }
          });
      };
     
     /**
      * Complex ajax call
      * @param url
      * @param pars
      * @param callBakFuncName
      */
      this.complexAjax = function(url, pars, callBakFuncName){//alert(url+" "+pars+" "+actionInsertId);
          jQuery.ajax({
              type:"POST",
              timeout: 300000, // 5 - minutes
              async:true,
              url: url,
              dataType:'json',
              data: {pars:pars},
              cache:false,
              beforeSend:function() {
                  
              },

              success: function(responce) {
             	 if(callBakFuncName!=null && callBakFuncName!=undefined){
             	        // you can use this param as parameter in callBackFunctionName: alert(res)
             	        eval(callBakFuncName(responce));
             	    }
              },

              error:function(xhr) {
                  if(xhr!=null && xhr!=undefined){
                      if(xhr.status==310){
                        alert(xhr.responseText);
                      }else if(xhr.status==320){
                      	alert(xhr.responseText);
                      }else if(xhr.status==330){
                      	alert(xhr.responseText);
                      }else if(xhr.status==403){
                      	alert(xhr.responseText);
                      }else if(xhr.status==401){
                      	alert(xhr.responseText);
                      }else{
                      	alert(xhr.responseText);
                      	//jQuery("#" + actionInsertId).html('<input type="hidden" id="viewType" value="0"/><font class="black_text" style="color:red;">Désolé, mais aucune réponse ne correspond à cette requête.<br />Modifiez vos critères de recherche.</font>');
                      }
                  }
              }
          });
      };
    
    /**
     * Simple ajax request
     * @param url
     * @param pars
     * @param actionInsertId
     */
    this.simpleAjaxRequestWithLoadingMsg = function(url, pars, actionInsertId, loadingInsertId){//alert(url+" "+pars+" "+actionInsertId);
        jQuery.ajax({
            type:"POST",
            timeout: 300000, // 5 - minutes
            async:true,
            url: url,
            data: pars,
            cache:false,
            beforeSend:function() {
            	jQuery("#" + actionInsertId).hide();
  			  	jQuery("#" + loadingInsertId).css('display','block');
  			  jQuery("#" + loadingInsertId).css('height', jQuery('#ahr_container').height());
            },

            success: function(responce) {
                if(jQuery("#" + actionInsertId)!=null && jQuery("#" + actionInsertId)!=undefined){
                	jQuery("#" + actionInsertId).html(responce);
                	jQuery("#" + actionInsertId).show();
      			  	jQuery("#" + loadingInsertId).hide();
                }
            },

            error:function(xhr) {
                if(xhr!=null && xhr!=undefined){
                    if(xhr.status==310){
                      alert(xhr.responseText);
                    }else if(xhr.status==320){
                    	alert(xhr.responseText);
                    }else if(xhr.status==330){
                    	alert(xhr.responseText);
                    }else if(xhr.status==403){
                    	alert(xhr.responseText);
                    }else if(xhr.status==401){
                    	alert(xhr.responseText);
                    }else{
                    	jQuery("#" + actionInsertId).html('<font class="black_text" style="color:red;">Désolé, mais aucune réponse ne correspond à cette requête.<br />Modifiez vos critères de recherche.</font>');
                    	jQuery("#" + actionInsertId).show();
          			  	jQuery("#" + loadingInsertId).hide();
                    }
                }
            }
        });
    };
}

var ajaxHelper = new ajaxHelper("ajaxHelper");
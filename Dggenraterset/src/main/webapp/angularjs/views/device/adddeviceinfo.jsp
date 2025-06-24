<script src="js/jquery.validate.min.js"></script>
   <script src="js/JSONAjax.js"></script> 
   <script type="text/javascript">
   
   function clear_form_elements(ele)
 	{
 	    $(ele).find(':input').each(function() {
 	        switch(this.type) {
 	            case 'password':

 	           case 'select-multiple':

 	            case 'select-one':

 	            case 'text':
 	            	
 	          case 'number':

 	            case 'textarea':

 	               $(this).val('');

 	                break;

 	           case 'checkbox':

 	            case 'radio':

 	                this.checked = false;

 	        }

 	    });

 	 

 	}
 	
    function addbecondata() 
    {
   	  var postForm = $( '.form-horizontal' );
   	 var jsonData = function( form ) {
   	 	 var arrData = form.serializeArray(),
      	  objData = {};   
   		     
   		  //  alert("in addbecondata");
   		    $.each( arrData, function( index, elem ) {
   		        objData[elem.name] = elem.value;
   		    });
   		     
   		    return JSON.stringify( objData );
   		};
   		 alert(jsonData( postForm ));
   		
   		 var datass=JSON.parse(jsonData( postForm ));
   		 console.log(datass['prid']);
   		 callPOSTJSONAjax("admin/savedeviceinfo/"+datass['prid'],jsonData( postForm ),savejsondata); 
    }
    
function savejsondata(JSONObject) 
{
	 JSONObject=JSON.parse(JSONObject);
	// alert(JSONObject+"         "+JSONObject['status']);
                if (JSONObject['status'])
                {
                        swal({
                            title: JSONObject['message'],
                            text: "Successfully Added !",
                            type: "success"
                        });
                  //  alert("Device Added successfully");
                }else
               	 {
               	 swal({
                        title: "Get Error",
                        text: "Get Error",
                        type: "error"
                    });
               	}

            //    getdata();  
           }
    $(document).ready(function() {
    	
    	
    	  callGETJSONAjax("admin/profilename",getprofilename);
    	   function getprofilename(msg2){
    			var conten = "";
    			var msg = eval(msg2);

    			var select = '<select name="prid" id="prid"   class="form-control"  data-placeholder="Select a Profilename" >';
    			for (var i = 0; i < msg.length; i++) {
    				//alert(msg[i].profilename)
    				conten = conten
    						+ ' <option  value="'+msg[i].prid+'">'
    						+ msg[i].profilename + '</option>';
    			}
    			$("#profilenames").html(select + conten + "</select>");
    		   
    		   
    	   }
    	
    	//alert("hhh");
    	//addbecondata();
    	$('.form-horizontal').validate({
            rules: {
            	devicename: {
                    required: true
                },
                devicedescription:{
                	required: true
                },imei:{
                	required: true
                },simcardno:
                	{
                	required: true
                	},devicemodel:{
                		required: true
                	},
                prid:
                        {
                            required: true
                        }
            },
            highlight: function(label) {
                $(label).closest('.control-group').addClass('error');
            },
            success: function(label) {
                label.text('OK!').addClass('valid').closest('.control-group').addClass('success');
            },
            messages:
                    {
                        required: "Please enter required value"
                    }, submitHandler: function(form) {
                   	 addbecondata();
            }
        });
    });
     </script> 
 <form class="form-horizontal" style="margin: 10px;">
      <div id="displaystatus"></div>  
                 <div class="row"><div class="col-lg-12">
                   <label class="control-label" >Device Name: </label>
                      <input type="text" class="form-control"  placeholder="Enter Device Name" name="devicename"  id="devicename" />
                   </div>
                </div> 
                
                <div class="row"><div class="col-lg-12">
                   <label class="control-label" >Device Description: </label>
                      <input type="text" class="form-control"  placeholder="Enter Device Decription" name="devicedescription"  id="devicedescription" />
                   </div>
                </div> 
                <div class="row"><div class="col-lg-12">
                   <label class="control-label" >IMEI No: </label>
                      <input type="text" class="form-control"  placeholder="Enter IMEI No" name="imei"  id="imei" />
                   </div>
                </div> 
                <div class="row"><div class="col-lg-12">
                   <label class="control-label" >SIM Card No: </label>
                      <input type="text" class="form-control"  placeholder="Enter Sim Card No" name="simcardno"  id="simcardno" />
                   </div>
                </div> 
                <div class="row"><div class="col-lg-12">
                   <label class="control-label" >Model No: </label>
                      <select id="devicemodel" name="devicemodel"  class="form-control">
                      <option value="Gt06">GT06</option>
                      <option value="Tk103">Tk103</option>
                      </select>
                      <input type="hidden"  id="flagcondition" name="flagcondition" value="true" />
                   </div>
                </div> 
  				 <div class="row"><div class="col-lg-12">
                <label class="control-label" >Device profile:</label>
                 <div id="profilenames" ></div>
                </div></div>
					<div align="center" style="margin-top: 2%;">
                        <input class="btn btn-primary" type="submit" value="SAVE" >
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
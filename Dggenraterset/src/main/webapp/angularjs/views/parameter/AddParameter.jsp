<script src="js/jquery.validate.min.js"></script>
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
   		 console.log(jsonData( postForm ));
   		 callPOSTJSONAjax("admin/parameter",jsonData( postForm ),savejsondata); 
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

                getdata();  
           }
    $(document).ready(function() {
    	//alert("hhh");
    	//addbecondata();
    	$('.form-horizontal').validate({
            rules: {
            	prmname: {
                    required: true
                },
                prmtype:
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
                 <div class="row"><div class="col-lg-4">
                    <label class="control-label" >Parameter Name: </label>
                      <input type="text" class="form-control"  placeholder="Enter Parameter Name" name="prmname"  id="prmname" />
                   </div>
                </div> 
  				 <div class="row"><div class="col-lg-4">
                    <label class="control-label" >Parameter Type:</label>
                  <select class="form-control" id="prmtype" name="prmtype">
      						<option value="Digital">Digital</option>
      						<option value="Analog">Analog</option>
      						<option value="RS232">RS232</option>
      					 </select>
                </div> </div>
                    
					<div align="center" style="margin-top: 2%;">
                        <input class="btn btn-primary" type="submit" value="SAVE" >
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
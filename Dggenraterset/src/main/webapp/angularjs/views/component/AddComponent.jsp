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
   		 callPOSTJSONAjax("admin/componet",jsonData( postForm ),savejsondata); 
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
            	componet: {
                    required: true
                },
                unit:
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
                    <label class="control-label" >Component Name: </label>
                 <input type="text" name="componet" class="form-control"  id="componet" placeholder="Enter Component Name"/>
                   </div>
                </div> 
  				 <div class="row"><div class="col-lg-4">
                    <label class="control-label" >Component Unit:</label>
                  <input type="text" name="unit" class="form-control"  id="unit" placeholder="Enter Component Unit"/>
                  <input type="hidden" name="image" class="form-control"  id="image" value="N/A" />
                </div> </div>
                    
					<div align="center" style="margin-top: 2%;">
                        <input class="btn btn-primary" type="submit" value="SAVE" >
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
<script src="js/jquery.validate.min.js"></script>
      <style type="text/css">
    label.valid {
        width: 24px;
        height: 24px;
        background: url(./img/valid.png) center center no-repeat;
        display: inline-block;
        text-indent: -9999px;
    }
    label.error {
        font-weight: bold;
        color: red;
        padding: 2px 8px;
        margin-top: 2px;
    }
    em.error {
        background:url("img/validationimg/unchecked.gif") no-repeat 0px 0px;
        padding-left: 15px;
        margin-top:5px;
        font-style:italic;
    }
    em.success {
        background:url("img/validationimg/checked.gif") no-repeat 0px 0px;
        padding-left: 15px;
    }

    form.cmxform label.error {
        margin-left: auto;
        width: 250px;
    }
    em.error { color: black; }
    #warning { display: none; }
    </style>
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
    		     
    		    $.each( arrData, function( index, elem ) {
    		        objData[elem.name] = elem.value;
    		    });
    		     
    		    return JSON.stringify( objData );
    		};
    		 console.log(jsonData( postForm ));
    		 callPUTJSONAjax("admin/componet/"+$("#cid").val(),jsonData( postForm ),savejsondata); 
     }
     
 function savejsondata(JSONObject) 
 {
	 JSONObject=JSON.parse(JSONObject);
	// alert(JSONObject+"         "+JSONObject['status']);
                 if (JSONObject['status'])
                 {
                         swal({
                             title: JSONObject['message'],
                             text: "Successfully Updated !",
                             type: "success"
                         });
                     //alert("Device Added successfully");
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
    	 callGETJSONAjax("admin/componet/"+$("#cid").val(),getobjectjsondata); 
    	 
     });
     function getobjectjsondata(JSONObject)
     {
    	$("#componet").val(JSONObject['componet']);
    	$("#unit").val(JSONObject['unit']);
    	$("#image").val(JSONObject['image']);
     }
     </script>
      <form class="form-horizontal" style="margin: 10px;">
      <div id="displaystatus"></div>  
                 <div class="row"><div class="col-lg-4">
                    <label class="control-label" >Component Name: </label>
                 <input type="text" name="componet" class="form-control"  id="componet" />
                   </div>
                </div> 
  				 <div class="row"><div class="col-lg-4">
                    <label class="control-label" >Component Unit:</label>
                  <input type="text" name="unit" class="form-control"  id="unit" />
                  <input type="hidden" name="image" class="form-control"  id="image" value="" />
                </div> </div>
                    
					<div align="center" style="margin-top: 2%;">
                        <input class="btn btn-primary" type="submit" value="SAVE" >
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
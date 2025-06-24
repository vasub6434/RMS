  <script src="js/jquery.validate.min.js"></script>
  <script type="text/javascript">
  var sum=0;
  var mainflag=1;
  var total=0;
  var analogdata=new Array();
   
  $(document).ready(function () {
	   	
	  callGETJSONAjax("admin/deviceinfolist",getprofilename);
	  
	    $('.form-horizontal').validate({
          rules: {
        	  analogrange1:{
                  required: true
              },
              analogrange2:{
                    required: true
               },
               analoglevel1:{
                     required: true
               },
               analoglevel2:{
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
  
  function getprofilename(msg2){
		var conten = "";
		var msg = eval(msg2);

		var select = '<select name="devicedata" id="devicedata"  class="chosen-select" data-placeholder="Select a Profilename"  onchange="getdata()" style="width: 100%;" >';
		for (var i = 0; i < msg.length; i++) {
			//alert(msg[i].profilename)
			conten = conten	+ ' <option  value="'+msg[i].prid+'##'+msg[i].deviceinfo_id+'">'
					  + msg[i].devicename + '</option>';
		}
		$("#deviceenames").html(select + conten + "</select>");
	    getdata();
  }

  function getdata()
  {
	var proid= $("#devicedata").val().split("##");
	callGETJSONAjax("admin/deviceprofile/"+proid[0],getobjectjsondata); 
 // callGETJSONAjax("parameters/Analog",getanalogdata);
  }
  
  function getobjectjsondata(JSONObject1)
  {
	  var JSONObject=eval(JSONObject1);
	 var json = JSONObject['parameters'];
	 var analogarry=json["Analog"];
	 var conten="";
	 var select = '<select name="analogdata" id="analogdata"  class="chosen-select" data-placeholder="Select a Profilename"  style="width: 100%;" >';
	 for (var i = 0; i < analogarry.length; i++) {
			conten = conten	+ ' <option  value="'+analogarry[i].Analoginput+'">'
					 + analogarry[i].analogname + '</option>';
		}
		$("#analogssss").html(select + conten + "</select>");
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
       delete objData["devicedata"];
       delete objData["analogdata"];
 	   return JSON.stringify( objData );
 	};
 		console.log(jsonData( postForm ));
 		var proid= $("#devicedata").val().split("##");
 		 
 		callPOSTJSONAjax("admin/analogdata/"+proid[1]+"/"+$("#analogdata").val(),jsonData( postForm ),savejsondata); 
  }
  function savejsondata(JSONObject) 
  {
  	 JSONObject=JSON.parse(JSONObject);
  	 if (JSONObject['status']){
         swal({
         		title: JSONObject['message'],
                text: "Successfully Added !",
                type: "success"
             });
      }else{
    	  swal({
                title: "Get Error",
                text: "Get Error",
                type: "error"
               });
            }
   }

  </script> 
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Analog Device Setting</h3>
            </div> 
    <div class="box-body ">
    <form class="form-horizontal">
  <div class="form-group" style="margin-left:10px;">
    <label for="profile">Device Name:</label>
    	<div id="deviceenames" style="width:40%;"></div>
  </div>
<div class="form-group " style="width:40%;margin-left:10px;">
<label for="range1">Analogs:</label>
  <div id="analogssss"></div>
</div>
 
 <div class="form-group" style="width:40%;margin-left:10px;">
    <label for="range1">Range 1:</label>
    <input type="text" class="form-control" name="analogrange1" id="analogrange1" placeholder="Enter Range 1">
  </div>
  
  <div class="form-group" style="width:40%;margin-left:10px;">
    <label for="range2">Range 2:</label>
    <input type="text" class="form-control" name="analogrange2" id="analogrange2" placeholder="Enter Range 2">
  </div>
  
  <div class="form-group" style="width:40%;margin-left:10px;">
    <label for="level1">Level 1:</label>
    <input type="text" class="form-control" name="analoglevel1" id="analoglevel1" placeholder="Enter Level 1">
  </div>
  
  <div class="form-group" style="width:40%;margin-left:10px;">
    <label for="devicename">Level 2:</label>
    <input type="text" class="form-control"  name="analoglevel2" id="analoglevel2" placeholder="Enter Level 2">
  </div>
 
 <button type="submit" class="btn btn-default" >Save</button>
</form>
 </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
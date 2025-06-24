<!DOCTYPE html>
<html>
<head>
<style type="text/css">

table { table-layout:fixed; } td{ overflow:hidden; text-overflow: ellipsis; }
</style>

<!-- <script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> -->
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script> -->
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<!--<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> -->

<script src="http://code.jquery.com/ui/1.8.24/jquery-ui.min.js" type="text/javascript"></script>
<link href="http://code.jquery.com/ui/1.8.24/themes/blitzer/jquery-ui.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="css/choosen/chosen.css">

<!--   <script type="text/javascript" src="js/jquery.js"></script> -->
    <link rel='stylesheet' href='css/jquery.bonsai.css'>
  <script src='js/jquery.bonsai.js'></script>
  <script src='js/jquery.qubit.js'></script>
  <script src='js/jquery.json-list.js'></script>
<!-- <script src="js/choosen/prism.js"></script>
<script src="js/choosen/init.js"></script> -->

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Device  Setup</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.5 -->
  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/dataTables.jqueryui.min.css">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="font-awesome-4.5.0/css/font-awesome.min.css">
<!-- jQuery 2.1.4 -->
 
   <script type="text/javascript">
   var coords=[];
   var iiid=[];
        $(document).ready(function () {
        	getdata();
        	getcomponentdata();
        	//$( ".draggable").draggable();
        	
           $("#imgggggggg").droppable({
        	   accept:".draggable",
        	
               drop: function (event, ui) {
            	    $(this).append($(ui.helper).clone());
            	    $( ".draggable").draggable();
            	    
            	     var dropPositionX = event.pageX - $(this).offset().left;
    				 var dropPositionY = event.pageY - $(this).offset().top;
    			 
                	  coords.push({x:dropPositionX, y:dropPositionY});
                      for(var i = 0; i < coords.length; i++) {
                        alert("item::"+coords[i].x + ' ' + coords[i].y);               
                       }
                     
                   },
               out: function (event, ui) {
                    var self = ui;
                    ui.helper.off('mouseup').on('mouseup', function () {
                        $(this).remove();
                        self.draggable.remove();
                    });
                }
                   
            });

      });
       
       function getcomponentdata()
        {
           callGETJSONAjax("componet",getcomponetjsondata);
      	  // $("#imagedetails").html('<center> <img src="~/../images/loading.gif"/></center>');
       }
         function getcomponetjsondata(msg2)
        {	
         	var msg = eval(msg2);
			var componentimage="";
			var ss="sd";
    		for (var i = 0; i < msg.length; i++) {
    			//alert(msg[i].image)
    			if(msg[i].image!= "N/A"){
    			    	componentimage+='<img alt="" id="'+msg[i].id+'" class="draggable ui-widget-content ui-draggable" src="compimages/'+msg[i].image+'"  style="float:left; margin: 5px; border-radius:10px;width: 38px;height: 32px" />'
    			   //  	componentimage+=$('<img alt="" id="'+msg[i].id+'" class="draggable ui-widget-content ui-draggable" src="compimages/'+msg[i].image+'"  style="float:left; margin: 5px; border-radius:10px;width: 38px;height: 32px" />').draggable();
    			}
    		}
    		$("#labels").html(componentimage);
    		  $( ".draggable").draggable({
    			 helper: 'clone'
    		   });  
    	}
        function getdata()
        {
     	   callGETJSONAjax("deviceinfolist",getjsondata);
     	   $("#imagedetails").html('<center> <img src="~/../images/loading.gif"/></center>');
     	}
        function getjsondata(msg2) {
    		// alert(msg2);
    		var conten = "";
    		var msg = eval(msg2);
    		  var option="";
		       var select = '<option value="0" selected="selected">Select Devicename</option>';
		       var tree=' <ol id="checkboxes2"><li value="root">Select DeviceName<ol>';
    		
		       //var select = '<select name="devicenames" id="devicenames"  class="chosen-select" data-placeholder="Select a Images" style="width: 100%;" onchange="loadimg()">';
    		for (var i = 0; i < msg.length; i++) {
    			  select += '<option value="'+msg[i].prid+'">'+msg[i].devicename+'</option>';
    			 tree+='<li value="'+msg[i].prid+'">'+msg[i].devicename+'<ol></li></ol>';
    		    			 /* 	conten = conten
    					+ ' <option  value="'+msg[i].prid_fk+'">'
    					+ msg[i].devicename + '</option>'; */
    		}
    		$("#devicename").html(select+ "</select>");
    		$("#devicetree1").html(tree+"</ol></li></ol>");
    		console.log(tree+"</ol></li></ol>")
    		 $('#checkboxes2').bonsai({});
    		//	chosen-select
   /*  	  $("#devicenames").chosen({
    width: "25%"
  });
    		loadimg(); */
    	}
        function loadimg() {
   //  alert("heloo")
        	//alert($("#devicename").val());
        	callGETJSONAjax("deviceprofile/"+$("#devicename").val(),savejsondata); 
        	function savejsondata(JSONObject)
        	
        	{
        		var  json= eval(JSONObject);
          	//  alert(json['profilename'])
          	  
          	    var select = '<option value="0" selected="selected" >Select ProfileName</option>';
          	  select += '<option value="'+json['parameters']+'">'+json['profilename']+'</option>';
          	$("#profilename").html(select+ "</select>"); 
            	//$("#devicename").val(json['devicename']);
             //   $("#profilenames").val(json['profilename']);
        	}
        	}
        
        function profiledata()
         {
        	
        	alert($("#profilename").val());
        	
         }
      
     </script> 
<!--  <script>
      jQuery(function() {
        $('#checkboxes').bonsai({
          expandAll: false,
          checkboxes: false, // depends on jquery.qubit plugin
          handleDuplicateCheckboxes: false // optional
        });
      });
      </script> -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">

  <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">
 
</head>
<body class="hold-transition skin-blue sidebar-mini">
 <jsp:include page="header.jsp"/>
    <div class="wrapper">
   <div class="content-wrapper">
        <section class="content">
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Device Master</h3>
            </div> 
            
           
   <!-- <ol id='checkboxes'>
        <li value='root'> All
          <ol>
            
            <li value='2'>
             One
              <ol>
                <li value='3'>
                   Two
                  <ol>
                   
                    <li value='5' >Tree</li>
                  </ol>
                </li>
              </ol>
            </li>
           
          </ol>
        </li>
      </ol> -->
 
       <div  id="devicetree1">     
            
            <div>
              <div id="devicenamea"><select class="form-control" name="devicename"  id="devicename" style="width: 25%;" onchange="loadimg()"></select></div>
             
             <br>
             <br>
              <div id="profilenamess"><select class="form-control" name="profilename"  id="profilename" style="width: 25%;" onchange="profiledata()"></select></div>
              
              <div id="labels" style="background-color: white; width:20%;height:400px;float:left;margin-top:10px;">
   			  <div id="labelsimg"  style="margin-left:8px;">
   					
   			  </div>
   			  </div>
       <div class="box-body" style="margin-left: 20%;">
    <div id="imagecomponentname" ></div> 
		<div id="imgggggggg" ></div>
           </div>
            <!-- /.box-body -->
          <!--   <input type="text" id="myField" /> -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  </div>

<!-- Bootstrap 3.3.5 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<script src="js/JSONAjax.js"></script>
<!-- AdminLTE App -->
<!-- <script src="dist/js/app.min.js"></script> -->
<!-- <script type="text/javascript" src="js/interactjs/dist/interact.js"></script>
<script type="text/javascript" src="js/interactjs/examples/js/html_svg.js"></script>
<link rel="stylesheet" href="js/interactjs/examples/css/html_svg.css" type="text/css"/> -->
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<script src="js/choosen/chosen.jquery.js"></script>
<script src="js/choosen/chosen.jquery.min.js"></script>

</body>
</html>

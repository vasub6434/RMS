
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

<!--<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> -->

<script src="http://code.jquery.com/ui/1.8.24/jquery-ui.min.js" type="text/javascript"></script>
<link href="http://code.jquery.com/ui/1.8.24/themes/blitzer/jquery-ui.css" rel="stylesheet" type="text/css" />

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
           callGETJSONAjax("admin/componet",getcomponetjsondata);
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
     	   callGETJSONAjax("admin/image",getjsondata);
     	   $("#imagedetails").html('<center> <img src="~/../images/loading.gif"/></center>');
     	}
        function getjsondata(msg2) {
    		// alert(msg2);
    		var conten = "";
    		var msg = eval(msg2);

    		var select = '<select name="img" id="imgg"  class="chosen-select" data-placeholder="Select a Images" style="width: 100%;" onchange="loadimg()">';
    		for (var i = 0; i < msg.length; i++) {
    			conten = conten
    					+ ' <option  value="'+msg[i].image+'">'
    					+ msg[i].imagename + '</option>';
    		}
    		$("#imagedetails").html(select + conten + "</select>");
    	//	chosen-select
    	  $("#imgg").chosen({
    width: "35%"
  });
    		loadimg();
    	}
        function loadimg() {
        //alert()
        $('#imgggggggg').html('');
        	$('#imgggggggg').prepend('<img id="theImg" src="images/'+$("#imgg").val()+'" width=860 height=350/>')
		}
      
     </script> 
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Device Setup</h3>
            </div> 
            
              <div id="imagedetails" ></div>
              
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
   
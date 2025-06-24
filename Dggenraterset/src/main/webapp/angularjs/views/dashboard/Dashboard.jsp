	<div class="row">
		<div class="col-md-1"><div class="form-group"><b>Select Device</b></div></div>
		<div class="col-md-3">
			<div class="form-group" id="mydevice">
				 <select class="form-control selectpicker" data-live-search="true" id="device">
					<option value="0">Select Device</option>
				</select>
			</div>
		</div>


		<div class="col-md-3">
			<!-- <p class="pull-left"> -->
			<div class="form-group">
				 <a  onclick="getData1()"
					class="btn btn-success btn-sm ad-click-event"
					> SHOW DATA </a>
			</div>
		</div>

		<div class="col-md-4" >
			<h3 style="margin-top: 10px;">
				Your Data Will Refresh in <span class="c" id="10"></span> Seconds.
			</h3>
		</div>

	</div>
	<section class="content">
		<div class="container-fluid">
			<div class="row" id="data">
			</div>
		</div>
	</section>
<script>

$(document).ready(function(){
	c();
	getDeviceList();
	
	timerID= setInterval(function(){
		getData1();
	},10000); 
	});
	
function c(){
	var n=$('.c').attr('id');
    var c=n;
	$('.c').text(c);
	setInterval(function(){
		c--;
		if(c>=0){
			$('.c').text(c);
		}
        if(c==0){
            $('.c').text(n);
        }
	},1000);
}
function getData1()
{
	//alert($("#device").val())
	
	if($("#device").val()!="0" && typeof($("#device").val())!== "undefined"){
	$.ajax({
        type: "get",
        url: "user/GetDashboard/sajan/sajan",
      //  url: "user/GetDashboard/"+$("#device").val()+"",
        success: function(responseData, textStatus, jqXHR) {
        	  $("#data").html("")
        	
        	 if ( typeof(responseData) !== "undefined" && responseData !== null ) {
        		 $("#myData").html(''+responseData.analogdigidata.Analog[0].BATTERY_CURRENT.split(" ")[0]+'<sup style="font-size: 20px">'+responseData.analogdigidata.Analog[0].BATTERY_CURRENT.split(" ")[1]+'</sup>');	 
        		 var items = ["small-box bg-danger", "small-box bg-warning", "small-box bg-success","small-box bg-info"];
        		 $.each(responseData.analogdigidata.Analog, function(key, value){
        			    $.each(value, function(key, value){
        			    	var html="";
        			        html+="<div class='col-lg-3 col-6'><div class='"+items[Math.floor(Math.random()*items.length)]+"'>";
        			        html+="<div class='inner'><h5 >Site Name : "+responseData.analogdigidata.DeviceName+"</h3><h4><b>"+key.replace("_"," ")+"</b></h4>";
        			        html+="<h3 id='myData'>"+parseFloat(value.split(" ")[0]).toFixed(2)+"<sup style='font-size: 20px'>"+value.split(" ")[1]+"</sup></h3><h5 id='ddate'>Last Time : "+responseData.deviceDate+"</h5>";
        			        html+="</div><div class='icon'><i class='ion ion-pie-graph'></i></div>";
        			        html+="<a href='#' class='small-box-footer'>More info <i class='fa fa-arrow-circle-right'></i></a></div></div>";
        			   $("#data").append(html)
        			    });
        			});
             	}
        	else  
        		$("#data").html("<h2>Opps!Data Not Found.</h2>")
        },
        error: function(jqXHR, textStatus, errorThrown) {
        //	alert(errorThrown)
        	$("#data").html("<h2>Opps!Data Not Found.</h2>");
            console.log(errorThrown);
        }
    });
}
}

function getDeviceList()
{
	  $.ajax({
   	  	  url: "user/GetDeviceList/",
   	  	  type: 'GET',
   	  	  success: function(data) {
   	  		var html="<option value='0'>Select Device</option>";
   	  	$.each(data, function(key, value){
   	  	html+='<option value='+value.deviceid+'>'+value.devicename+'</option>")';
   	  	});
   	  	$('#mydevice').html("<select class='form-control'  id='device' >"+html+"</select>");
   	  	  },
   	  	 error: function(e) {
   	  		console.log(e.message);
   	  	  }
   	  	} ); 
}
</script>
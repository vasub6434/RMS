<body class="hold-transition sidebar-mini">
	<!-- <div class="row">
		<div class="col-md-1"><div class="form-group"><b>Select Device</b></div></div>
		<div class="col-md-3">
			<div class="form-group" id="mydevice">
				 <select class="form-control selectpicker" data-live-search="true" id="device">
					<option value="0">Select Device</option>
				</select>
			</div>
		</div>


		<div class="col-md-3">
			<p class="pull-left">
			<div class="form-group">
				 <a href="#" onclick="getData1()"
					class="btn btn-success btn-sm ad-click-event"
					> SHOW DATA </a>
			</div>
		</div>

		<div class="col-md-4" >
			<h3 style="margin-top: 10px;">
				Your Data Will Refresh in <span class="c" id="10"></span> Seconds.
			</h3>
		</div>

	</div> -->
	<section class="content">
		<div class="container-fluid">
			<div class="row" id="data">
			</div>
		</div>
	</section>
</body>
<script>

$(document).ready(function(){
	GetEnergyMeterDashboard();
	 setInterval(function(){
	GetEnergyMeterDashboard();
},10000); 
});
function GetEnergyMeterDashboard()
{
	$.ajax({
        type: "get",
        url: "user/GetEnergyMeterDashboard/281",
        success: function(responseData, textStatus, jqXHR) {
        	//$("#data").html("<h2>"+responseData+"</h2>");
        	 $("#data").html("")
        	         		 var items = ["small-box bg-danger", "small-box bg-warning", "small-box bg-success","small-box bg-info"];

        	  if ( typeof(responseData) !== "undefined" && responseData !== null ) {
        		  console.log(responseData.RS323)
        		  $.each(responseData.RS323, function(key, value){
        			  console.log(key+" :: "+value);
        			  var html="";
  			        html+="<div class='col-lg-3 col-6'><div class='"+items[Math.floor(Math.random()*items.length)]+"'>";
  			        html+="<div class='inner'><h5 >Device Name : "+responseData.DeviceName+"</h3><h4><b>"+key+"</b></h4>";
  			     var i=value.split(" ")[0].toString();
  			        if (typeof i === "undefined" || i==="") {
    			        html+="<h4 id='myData'>Data Not Available<sup style='font-size: 10px'></sup></h4><h5 id='ddate'>Last Time : "+responseData.DeviceDate+"</h5>";
  			    }else
  			        html+="<h3 id='myData'>"+value.split(" ")[0]+"<sup style='font-size: 20px'>"+value.split(" ")[1]+"</sup></h3><h5 id='ddate'>Last Time : "+responseData.DeviceDate+"</h5>";
  			        
  			        html+="</div><div class='icon'><i class='ion ion-pie-graph'></i></div>";
  			        html+="<a href='#' class='small-box-footer'>More info <i class='fa fa-arrow-circle-right'></i></a></div></div>";
  			   $("#data").append(html)
        		  });
        	 } 
        },
        error: function(jqXHR, textStatus, errorThrown) {
        //	alert(errorThrown)
        	$("#data").html("<h2>"+errorThrown+"</h2>");
            console.log(errorThrown);
        }
    });
}
</script>
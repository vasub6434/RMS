<body class="hold-transition sidebar-mini">
	<div class="row">
		<div class="col-md-1"><div class="form-group"><b>Select Device</b></div></div>
		<div class="col-md-3">
			<div class="form-group" id="mydevice111">
				 <select class="form-control" id="device2">
					<option value="0">Select Device</option>
				</select>
			</div>
		</div>


		<div class="col-md-3">
			<!-- <p class="pull-left"> -->
			<div class="form-group">
				 <a onclick="getData2()"
					class="btn btn-success btn-sm ad-click-event"
					> SHOW DATA </a>
			</div>
		</div>

		<div class="col-md-4" >
			<h3 style="margin-top: 10px;">
				Sajan Your Data Will Refresh in <span class="c" id="10"></span> Seconds.
			</h3>
		</div>

	</div>
	<div id="data2"></div>
	<script>
$(document).ready(function(){
//	c();
	//getDeviceList();
	/*setInterval(function(){c();getData();
	},10000);*/
});
/*function c(){
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
}*/
function getData2()
{
	alert("before");
//	if($("#device").val()!="0"){
	$.ajax({
        type: "get",
        url: "user/GetDigitalDashboard/282",
        success: function(responseData, textStatus, jqXHR) {
        	alert("after");
        	if ( typeof(responseData) !== "undefined" && responseData !== null  ) {
        		 // alert("GetDigitalDashboard :: "+responseData)
        		 $("#data2").html(""); 
        		$.each(responseData, function(key, digital){
        			var html="<section class='content-header'><h1>"+digital.analogdigidata.DeviceName+"</h1></section><section class=''>";
        		 	 $.each(digital.analogdigidata.Digital, function(key, value){
           				$.each(value, function (index, data) {
           			       var st="OFF";
           			       var cls="bg-red"
           			       if(data==="0"){
           			    	   st="ON"
           			    		cls="bg-green" //DeviceDate
           			       }
           					html+="<div class='col-md-4 col-sm-6 col-xs-12'><div class='info-box "+cls+"'>";
           					html+="<span class='info-box-icon'><i class='fa fa-toggle-on'></i></span><div class='info-box-content'>";
           					html+="<span class='info-box-text'>"+digital.analogdigidata.DeviceName+"</span><span class='info-box-number'>"+index+" <b>"+st+"</b></span>";
           					html+="<div class='progress'><div class='progress-bar' style='width: 100%'></div></div>";
           					html+="<span class='info-box-number'>"+digital.DeviceDate+"</span></div></div></div>";
           			    });
           			 }); 
        		 	html+=" </div></section>";
        			 $("#data2").append(html); 	
        		});
        		
        		}
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
	
//	}
}

</script>
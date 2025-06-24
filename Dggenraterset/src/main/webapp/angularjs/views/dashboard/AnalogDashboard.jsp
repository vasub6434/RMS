<!-- <div id="myData1">
1236547896
</div> -->
<body class="hold-transition sidebar-mini">
<section class="content">
      <div class="container-fluid">
       <!-- Info boxes -->
        <div class="row">
          <div class="col-lg-3 col-6">
            <!-- small box -->
            <div class="small-box bg-info">
              <div class="inner">
                <h5 id="dId">SAJAN Device ID : 00</h5>
  <h2 >Energy Meter</h2>
 <h3 id="myData">00<sup style="font-size: 20px">KWH</sup></h3>
    <h5 id="ddate">Last Time : 0000-00-00 00:00:00</h5>
               <!--  <p>Energy Meter</p> -->
              </div>
              <div class="icon">
                <i class="ion ion-bag"></i>
              </div>
              <a href="#" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-3 col-6">
            <!-- small box -->
            <div class="small-box bg-success">
              <div class="inner">
                 <h5 >Device ID : 00</h5>
                <h2 >Energy Meter</h2>
 <h3 id="myData">00<sup style="font-size: 20px">KWH</sup></h3>
   <h5 id="ddate">Last Time : 0000-00-00 00:00:00</h5>
              </div>
              <div class="icon">
                <i class="ion ion-stats-bars"></i>
              </div>
              <a href="#" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-3 col-6">
            <!-- small box -->
            <div class="small-box bg-warning">
              <div class="inner">
               <h5 >Device ID : 00</h5>
               <h2 >Energy Meter</h2>
 <h3 id="myData">00<sup style="font-size: 20px">KWH</sup></h3>
   <h5 id="ddate">Last Time : 0000-00-00 00:00:00</h5>
              </div>
              <div class="icon">
                <i class="ion ion-person-add"></i>
              </div>
              <a href="#" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-3 col-6">
            <!-- small box -->
            <div class="small-box bg-danger">
              <div class="inner">
               <h5 >Device ID : 00</h5>
             <h2 >Energy Meter</h2>
 <h3 id="myData">00<sup style="font-size: 20px">KWH</sup></h3>
   <h5 id="ddate">Last Time : 0000-00-00 00:00:00</h5>
              </div>
              <div class="icon">
                <i class="ion ion-pie-graph"></i>
              </div>
              <a href="#" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
        </div>
      </div>
      </section>
      </body>
<script>

$(document).ready(function(){
	
	 setInterval(function(){ getData(); }, 3000);
});

function getData()
{
	$.ajax({
        type: "get",
        url: "user/GetDashboard/",
        success: function(responseData, textStatus, jqXHR) {
        	/* if ( typeof(responseData) !== "undefined" && responseData !== null ) {
        		$("#dId").html("Device ID : "+responseData.deviceId+"");
        		$("#myData").html(''+responseData.analogdigidata.Rs232[0].rs232value+'<sup style="font-size: 20px">KWH</sup>');
        		$("#ddate").html('Last Time : '+responseData.deviceDate+'');
        	}
        	else
        		{
        		$("#myData").html("Data Not Received")
        		} */
        	
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}


</script>
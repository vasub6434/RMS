<!DOCTYPE html>
<html>
<head>
<style type="text/css">

table { table-layout:fixed; } td{ overflow:hidden; text-overflow: ellipsis; }
</style>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <script type="text/javascript">
   var analogdata=new Array();
   var digitaldata=new Array();
   var rs232data=new Array();
   function getdata()
   {
	
	   callGETJSONAjax("admin/Dashboard",getdashboarddata);
	   callGETJSONAjax("admin/deviceinfolist",getjsondata);
   }
   
   function getjsondata(msg2)
   {
     	var  json= eval(msg2);
           var conten="";
   		var select = '<select name="deviceidss" id="deviceidss"  class="chosen-select"  style="width: 100%;" onchange="setprofile()" ><option value="">Please Select</option>';
   		for (var i = 0; i < json.length; i++) 
   		{
   			conten = conten+ ' <option  value="'+json[i].deviceinfo_id+'">'+ json[i].devicename + '</option>';
   		}
   		$("#devicelist").html(select + conten + "</select>");
   }
   function getdashboarddata(msg2)
   {
		var  json= eval(msg2);
	   var conten="";
		var select = '<select name="dashboard" id="dashboard"  class="chosen-select"  style="width: 100%;" ><option value="">Please Select</option>';
		for (var i = 0; i < json.length; i++) 
		{
			conten = conten+ ' <option  value="'+json[i].id+'">'+ json[i].dashboardname + '</option>';
		}
		$("#dashboardlist").html(select + conten + "</select>");
   }
   
   function setprofile()
   {
	   var deviceid=$("#deviceidss").val();
	  // alert(deviceid);
	   callGETJSONAjax("admin/deviceinfolist/"+deviceid,getdevicejsondata);
	   
   }
   function getdevicejsondata(msg2)
   {
	  var  json= eval(msg2);
	  rs232data=json.dp.parameters.Rs232;
	  analogdata=json.dp.parameters.Analog;
	  digitaldata=json.dp.parameters.Digital;
   }
   
   function paralisttt()
   {
	   var paratype=$("#parametertype").val();
	   var conten="";
	   var select ='<select name="parameters" id="parametertypes"  class="chosen-select"  style="width: 100%;"><option value="">Please Select</option>';
	   if($("#deviceidss").val()!="")
		   {
		    if(paratype=="digi")
		    	{
		    	conten+=getDigitaldata2();
		    	}else if(paratype=='analog')
		    		{
		    		conten+=getanalogdata2();
		    		}else{
		    			conten+=getRS232data2();
		    		}
		    $("#paratypelisttt").html(select + conten + "</select>");
		   }else{
			   swal("please select Device");
		   }
   }
   function getanalogdata2()
   {
	   var conten="";
		for (var i = 0; i < analogdata.length; i++) {
			conten = conten+ ' <option  value="'+analogdata[i].analogname+'">'+ analogdata[i].analogname + '</option>';
		}
		return  conten;
   }
   
   function getDigitaldata2()
   {
	   var conten="";
		for (var i = 0; i < digitaldata.length; i++) {
			conten = conten+ ' <option  value="'+digitaldata[i].parametername+'">'+ digitaldata[i].parametername + '</option>';
		}
		//alert(html);
		return  conten;
   }
   
   function getRS232data2()
   {
	   var conten="";
		for (var i = 0; i < rs232data.length; i++) {
			conten = conten+ ' <option  value="'+rs232data[i].parametername+'">'+ rs232data[i].parametername + '</option>';
		}
		return  conten;
   }
   $(document).ready(function () {
   	getdata();
 });
 
  </script> 
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Device Profile</h3>
            </div> 
   <form action="admin/savedeviceprofile" method="post">
    <div class="box-body">
       <div class="row">
          <div class="col-md-4" >
                 <label class="control-label" >Dashboard: </label>
                <div id="dashboardlist"></div>
          </div>
          <div class="col-md-4" >
                 <label class="control-label" >Device: </label>
                <div id="devicelist"></div>
          </div>
          <div class="col-md-4" >
                 <label class="control-label" >Parameter Type: </label>
             <select name="parametertype" id="parametertype"  class="chosen-select"  style="width: 100%;" onchange="paralisttt()" >
             <option value="">Please Select</option>
             <option value="digi">Digital</option>
              <option value="analog">Analog</option>
               <option value="rs232">RS232</option>
             </select>
          </div>
       </div>
        <div class="row">
          <div class="col-md-4" >
                 <label class="control-label" >Parameter List: </label>
                <div id="paratypelisttt"></div>
          </div>
           <div class="col-md-4" >
              <label class="control-label" >Wiget List: </label><br/>
              <select id="wigetlist" name="wigetlist" >
              <option value="Animatedspeedometer">Animated Speedometer</option>
              <option value="Animatedgaugemeter">Animated Gauge Meter</option>
              <option value="TempGauge">Temp Gauge</option>
              <option></option>
              </select> 
          </div>
       </div>
       
       
 
       <br/>
       
 
      
  
   </div>
   <input type="submit" class="btn btn-primary" id="add" style="margin-left: 20px;">
   </form>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
   
<script>

function deletealert(id)
{
        swal({
            title: "Are You Sure to Deleted Selected Componet Data??",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, Deleted it!",
            closeOnConfirm: false
        }, function () {
        	callDELETEJSONAjax("componet/"+id,deletejsondata);
        });
}
function deletejsondata(json)
{
	 swal("successfully Deleted.", "success");
   	 getdata();
}

  function inittable() 
  {
  $('#expiryreport')
  .dataTable(
  {
	              
	            
  "sPaginationType" : "full_numbers",
  "oLanguage" : {
  "sLengthMenu" : "Entries per page:<span class='lenghtMenu'> _MENU_</span><span class='lengthLabel'></span>"
  },
  //"sDom" : '<"tbl-tools-searchbox"fl<"clear">>,<"tbl_tools"CT<"clear">>,<"table_content"t>,<"widget-bottom"p<"clear">>',
   "sDom": 'T<"clear">lfrtip',
  "oTableTools" : {
  "sSwfPath" : "plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
  }
             
  
               
  });
  $("div.tbl-tools-searchbox select").addClass(
  'tbl_length');
  }
</script>
</body>
</html>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">

table { table-layout:fixed; } td{ overflow:hidden; text-overflow: ellipsis; }
</style>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>DG Generator</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.5 -->
  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/dataTables.jqueryui.min.css">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="font-awesome-4.5.0/css/font-awesome.min.css">
  <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables/dataTables.bootstrap.css">
  <link rel="stylesheet" href="plugins/datatables/extensions/TableTools/css/dataTables.tableTools.min.css">
  <!-- Sweet Alert -->
    <link href="css/sweetalert/sweetalert.css" rel="stylesheet">
  
<!-- jQuery 2.1.4 -->
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
   <script type="text/javascript" language="javascript">
   var sum=0;
   var mainflag=1;
   var total=0;
   var analogdata=new Array();
   var digitaldata=new Array();
   var rs232data=new Array();

   function getdata()
   {
	   callGETJSONAjax("admin/deviceprofile/"+sessionStorage.getItem('prid'),getobjectjsondata); 
	  // alert(sessionStorage.getItem('prid'));
	   callGETJSONAjax("admin/parameters/Analog",getanalogdata);
	  callGETJSONAjax("admin/parameters/Digital",getDigitaldata);
	   callGETJSONAjax("admin/parameters/RS232",getRS232data);
   }
   
   function getobjectjsondata(JSONObject)
   {
	 // alert(JSONObject['parameters']);
	   var json = JSON.parse(JSONObject['parameters']);
	 // alert(json["Analog"].toString())
	   for(var i=0;i<json["Analog"].length;i++){
			//alert("Analoginput:-"+json.Analog[i].Analoginput);
			//alert("Analogformula:-"+json.Analog[i].Analogformula);
			//alert("Analogunit:-"+json.Analog[i].Analogunit);
			var table = document.getElementById("myanalogTableData");
		       var rowCount = table.rows.length;
		       var row = table.insertRow(rowCount);
		       row.insertCell(0).innerHTML= '<tr><td>'+getanalogdata2()+'</td>';
		       row.insertCell(1).innerHTML= '<td><input type="text" name="unit[]" placeholder="Enter Unit" id="unit[]" value="'+json.Analog[i].Analogformula+'"></td>'
		   	   row.insertCell(2).innerHTML= '<td><input type="text" name="formula[]" placeholder="Enter Formula" id="formula[]" value="'+json.Analog[i].Analogunit+'"></td>';
		       row.insertCell(3).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deleteanalogRow(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
		       mainflag++;
			
	  } 
	 
	   //alert(json["Rs232"].length)
	   for(var j=0;j<json["Rs232"].length;j++){
		  // alert("RS232:-"+json.Rs232[0].rs232data);	   
	      // alert("Reverse:-"+json.Rs232[j].rsreverse);
		   var table = document.getElementById("myrs232TableData");
		   
	       var rowCount = table.rows.length;
	       var row = table.insertRow(rowCount);
	       row.insertCell(0).innerHTML=  '<tr><td>'+getRS232data2()+'</td>';
	       row.insertCell(1).innerHTML= '<td><select name="rsreverse[]" id="rsreverse[]"><option value="True">True</option><option value="False">False</option></select></td>'
	   	   row.insertCell(2).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deleters232Row(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
	       mainflag++;
	   }
	 
	    for(var k=0;k<json["Digital"].length;k++){
		// alert("Digitaldata:-"+json.Digital[k].digitaldata);
		//    alert("Digitaldreverse:-"+json.Digital[k].digitaldreverse);
	  
		
	    	var table = document.getElementById("mydigitalTableData");
	        var rowCount = table.rows.length;
	        var row = table.insertRow(rowCount);
	        row.insertCell(0).innerHTML=  '<tr><td>'+getDigitaldata2()+'</td>';
	        row.insertCell(1).innerHTML= '<td><select name="dreverse[]" id="dreverse[]" value="'+json.Digital[k].digitaldata+'"><option value="True">True</option><option value="False">False</option></select></td>'
	    	row.insertCell(2).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deletedigitalRow(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
	        mainflag++;
		
	 }

	   $("#devicename").val(JSONObject['profilename']);
	   $("#d_id").val(sessionStorage.getItem('prid'));
  	//$("#prmname").val(JSONObject['prmname']);
  //	$("#prmtype").val(JSONObject['prmtype']);
  
   }
   
    function getanalogdata(msg2)
   {
	   analogdata = msg2;
	  // addanalogRow();
   } 
   
   function getDigitaldata(msg2)
   {
	   digitaldata = msg2;
	 //  adddigitalRow();
   } 
   
    function getRS232data(msg2)
   {
	   rs232data = msg2;
	   //addrs232Row();
   } 
   function getanalogdata2()
   {
	   
	   var conten="";
		var select = '<select name="analogdata[]" id="analogdata[]"  class="chosen-select"  style="width: 100%;" >';
		for (var i = 0; i < analogdata.length; i++) {
			conten = conten+ ' <option  value="'+analogdata[i].id+'">'+ analogdata[i].prmname + '</option>';
		}
		var html=select + conten + "</select>";
	//	alert(html);
		return  html;
   }
   
   function getDigitaldata2()
   {
	   var conten="";
		var select = '<select name="digitaldata[]" id="digitaldata[]"  class="chosen-select"  style="width: 100%;" >';
		for (var i = 0; i < digitaldata.length; i++) {
			conten = conten+ ' <option  value="'+digitaldata[i].id+'">'+ digitaldata[i].prmname + '</option>';
		}
		var html=select + conten + "</select>";
		//alert(html);
		return  html;
   }
   
   function getRS232data2()
   {
	   var conten="";
		var select = '<select name="rs232data[]" id="rs232data[]"  class="chosen-select"  style="width: 100%;" >';
		for (var i = 0; i < rs232data.length; i++) {
			conten = conten+ ' <option  value="'+rs232data[i].id+'">'+ rs232data[i].prmname + '</option>';
		}
		var html=select + conten + "</select>";
		//alert(html);
		return  html;
   }
   
   $(document).ready(function () {
   	getdata();
 });
   function addanalogRow() 
   {
   	var table = document.getElementById("myanalogTableData");
       var rowCount = table.rows.length;
       var row = table.insertRow(rowCount);
       row.insertCell(0).innerHTML= '<tr><td>'+getanalogdata2()+'</td>';
       row.insertCell(1).innerHTML= '<td><input type="text" name="unit[]" placeholder="Enter Unit" id="unit[]" ></td>'
   	   row.insertCell(2).innerHTML= '<td><input type="text" name="formula[]" placeholder="Enter Formula" id="formula[]"></td>';
       row.insertCell(3).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deleteanalogRow(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
       mainflag++;
   }
  function deleteanalogRow(obj) 
   {
     //  alert(total1[index]);
       var index = obj.parentNode.parentNode.rowIndex;
       //alert(index);
       var table = document.getElementById("myanalogTableData");
       table.deleteRow(index);
   	mainflag--;
   }
   
    function adddigitalRow() 
   {
   	var table = document.getElementById("mydigitalTableData");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    row.insertCell(0).innerHTML=  '<tr><td>'+getDigitaldata2()+'</td>';
    row.insertCell(1).innerHTML= '<td><select name="dreverse[]" id="dreverse[]"><option value="True">True</option><option value="False">False</option></select></td>'
	row.insertCell(2).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deletedigitalRow(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
    mainflag++;
   }
   
   function deletedigitalRow(obj) 
   {
     //  alert(total1[index]);
       var index = obj.parentNode.parentNode.rowIndex;
       //alert(index);
       var table = document.getElementById("mydigitalTableData");
       table.deleteRow(index);
   		mainflag--;
     
   }
   
   function addrs232Row() 
   {
   	var table = document.getElementById("myrs232TableData");
  
       var rowCount = table.rows.length;
       var row = table.insertRow(rowCount);
       row.insertCell(0).innerHTML=  '<tr><td>'+getRS232data2()+'</td>';
       row.insertCell(1).innerHTML= '<td><select name="rsreverse[]" id="rsreverse[]"><option value="True">True</option><option value="False">False</option></select></td>'
   	row.insertCell(2).innerHTML= '<td><a class="btn btn-danger"   onClick="Javacsript:deleters232Row(this)"><i class="glyphicon glyphicon-trash icon-white"></i></a></td></tr>';
       mainflag++;
   }
   
    function deleters232Row(obj) 
    
   {
     //  alert(total1[index]);
       var index = obj.parentNode.parentNode.rowIndex;
       //alert(index);
       var table = document.getElementById("myrs232TableData");
       table.deleteRow(index);
   	mainflag--;
   }
    
    function tabledata(){
    	alert("in tabledata");
    	var oTable = document.getElementById('myanalogTableData');
    	 var rowLength = oTable.rows.length;
    	 
        alert("rowLength::"+rowLength);
         for (i = 1; i < rowLength; i++){
             var oCells = oTable.rows.item(i).cells;
			 var cellLength = oCells.length;
			 alert("cellLength::"+cellLength);
			 
			 for(var j=0; j < cellLength; j++){
			 var cellVal = oCells.item(j).textContent;
                    alert("cellVal::"+cellVal);
                 }
          }      
    }
  </script> 
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">
 
</head>
<body class="hold-transition skin-blue sidebar-mini">
 <jsp:include page="../header.jsp"/>
    <div class="wrapper">
   <div class="content-wrapper">
        <section class="content">
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Edit Device Profile</h3>
            </div> 
   <form action="admin/updatedeviceprofile" method="post">
    <div class="box-body">
       <div class="row">
          <div class="col-md-12" >
                 <label class="control-label" >Profile Name: </label>
                 <input type="text" name="devicename" class="form-control" id="devicename" placeholder="Enter Device Name" />
          </div>
       </div>
       <br/>
       
       <h2> Analog Data <input type="button" class="btn btn-success" id="add" value="Add" onClick="Javascript:addanalogRow(this)" style="margin-left: 20px;"></h2> 
      
      <table  class="table table-striped table-bordered displays" name="myanalogTableData" id="myanalogTableData" style="width: 100%;float:left;" >  
	              <thead>
                  <tr>
   					<th><label>Analog Input</label></th>
   					<th><label>Unit</label></th>
                    <th><label>Formula</label></th>
                   </tr>
                  </thead> 
      </table>
      
       <h2> Digital Data <input type="button" class="btn btn-success" id="add" value="Add" onClick="Javascript:adddigitalRow(this)" style="margin-left: 20px;"></h2>
      <table  class="table table-striped table-bordered displays"  id="mydigitalTableData" style="width: 100%;float:left;" >  
	              <thead>
                  <tr>
   					<th><label>Digital Input</label></th>
   					<th><label>Reverse</label></th>
                   </tr>
                  </thead> 
      </table>
      
       <h2> RS232 Data <input type="button" class="btn btn-success" id="add" value="Add" onClick="Javascript:addrs232Row(this)" style="margin-left: 20px;"></h2>
      <table  class="table table-striped table-bordered displays"  id="myrs232TableData" style="width: 100%;float:left;" >  
	              <thead>
                  <tr>
   					<th><label>RS232</label></th>
   					<th><label>Reverse</label></th>
                   </tr>
                  </thead> 
      </table>
   </div>
   <input type="hidden" name="d_id" id="d_id">
   <input type="submit" class="btn btn-primary" id="Update" style="margin-left: 20px;">
   </form>
            <!-- /.box-body -->
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
<!-- DataTables -->
<script src="plugins/datatables/jquery.dataTables.min.js"></script>
<script src="plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="plugins/datatables/extensions/TableTools/js/dataTables.tableTools.min.js"></script>
<!-- SlimScroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<script src="js/JSONAjax.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
  <!-- Sweet alert -->
    <script src="js/sweetalert/sweetalert.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<!-- page script -->
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

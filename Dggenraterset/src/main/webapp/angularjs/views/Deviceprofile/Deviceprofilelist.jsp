<script src="js/jquery.validate.min.js"></script>
   <script type="text/javascript" language="javascript">
   var jsonObj = [];
   function getdata()
   {
	   callGETJSONAjax("user/deviceprofile",getjsondata);
	//   callGETJSONAjax("user/deviceprofile/"+$.session.get('KEY'),getjsondata);
   }
        $(document).ready(function () {
        	getdata();
        	$("#addModal").on('hidden.bs.modal', function () {
        	    $(this).data('bs.modal', null);
        	});
      });

        function edit(prid)
        {
        	sessionStorage.setItem('prid',prid);
        } 
      
        function getjsondata(msg2)
        {
          	var  json= eval(msg2);
          	  var tablebody="";
          	  
            var tableheader = '<table id="expiryreport" class="table table-striped" style="border-color:#3C8DBC;"><thead style="background-color:#3C8DBC; color:white;"><tr><th>PrID.</th><th >PrName</th><th >RS232 Data</th><th >GPS_Data</th><th>Edit</th></tr></thead><tbody>';
                for (var i = 0; i < json.length; i++)
                {
                  jsonObj.push(json[i].parameters);
              	  tablebody += '<tr><td>'+json[i].prid+'</td><td>' + json[i].profilename + '</td><td><button type="button" class="btn btn-success btn-xs" onclick="showRS232Data(\'' + i + '\')">Show Data</button></td><td>' + json[i].gpsdata + '</td><td><button  type="button" class="btn btn-info"><a href="editdeviceprofile" onclick="edit('+json[i].prid+')">Edit</a></button></td></tr>';
                }
                $("#userdetails").html(tableheader+tablebody+ "</tbody></table>");
                inittable();
        }
        
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
       
       function showRS232Data(id)
       {
    	   $("#dynamicTable").html("");
     // create table
 	   var $table = $('<table style="width:100%;height:50%;" class="table table-striped"> ');
 	   // caption
 	   $table.append('<caption>Analog Data</caption>')
 	   // thead
 	   .append('<thead>').children('thead')
 	   .append('<tr />').children('tr').append('<th>Analogunit</th><th>Analogname</th><th>Analoginput</th><th>Analog Formula</th>');

 	   //tbody
 	   var $tbody = $table.append('<tbody />').children('tbody');
            	   for(i=0;i<jsonObj[id].Analog.length;i++)
                	   {
                	   // add row
                	   $tbody.append('<tr />').children('tr:last')
                	   .append("<td>"+jsonObj[id].Analog[i].Analogunit+"</td>")
                	   .append("<td>"+jsonObj[id].Analog[i].analogname+"</td>")
                	  .append("<td>"+jsonObj[id].Analog[i].Analoginput+"</td>")
                	   .append("<td>"+jsonObj[id].Analog[i].Analogformula+"</td>")

                	   // add table to dom
                	   $table.appendTo('#dynamicTable');
                	   $('#dynamicTable table').addClass("dynamicTable");
                       $('#dynamicTable table thead').addClass("dynamicTableTh");
                       $('#dynamicTable table tbody td').addClass("dynamicTableTd");
                           	   
                	   }
            	// create table
             	   var $table = $('<table style="width:100%;height:50%;" class="table table-striped"> ');
             	   // caption
             	   $table.append('<caption>Rs232 Data</caption>')
             	   .append('<thead>').children('thead')
             	   .append('<tr />').children('tr').append('<th>Parameter Name</th><th>Reverse</th>');

             	   var $tbody = $table.append('<tbody />').children('tbody');
                        	   for(i=0;i<jsonObj[id].Rs232.length;i++)
                            	   {
                        		   $tbody.append('<tr />').children('tr:last')
                        		    .append("<td>"+jsonObj[id].Rs232[i].parametername+"</td>")
                            	   .append("<td>"+jsonObj[id].Rs232[i].reverse.toString().toUpperCase()+"</td>")
                            	  

                            	   $table.appendTo('#dynamicTable');
                            	   $('#dynamicTable table').addClass("dynamicTable");
                                   $('#dynamicTable table thead').addClass("dynamicTableTh");
                                   $('#dynamicTable table tbody td').addClass("dynamicTableTd");
                            	   }

                        	   var $table = $('<table style="width:100%;height:50%;" class="table table-striped"> ');

                        	   $table.append('<caption>Digital</caption>')
                         	   .append('<thead hegith=30%>').children('thead')
                         	   .append('<tr />').children('tr').append('<th> Parameter Name</th>Reverse<th></th>');

                         	   var $tbody = $table.append('<tbody />').children('tbody');
                                    	   for(i=0;i<jsonObj[id].Digital.length;i++)
                                        	   {
                                        	   $tbody.append('<tr />').children('tr:last')
                                        	   .append("<td>"+jsonObj[id].Digital[i].parametername+"</td>")
                                        	   .append("<td>"+jsonObj[id].Digital[i].reverse.toString().toUpperCase()+"</td>")
                                        	   

                                        	   $table.appendTo('#dynamicTable');
                                        	   $('#dynamicTable table').addClass("dynamicTable");
                                               $('#dynamicTable table thead').addClass("dynamicTableTh");
                                               $('#dynamicTable table tbody td').addClass("dynamicTableTd");
                                        	   }
            	   $('#myModal').modal();
       }
     </script> 
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Profile List</h3>
            </div> 
    <div class="box-body">
     <div class="row  border-bottom white-bg dashboard-header">
                    <div class="col-xs-4 col-xs-offset-4">
                      <a href="#addprofile" class="btn btn-primary">Add Profile</a>
                        <input  class="btn btn-info" type="button" value="Refresh" onclick="getdata()"/>
                   </div>
					<input type="hidden" id="cid" >
            </div>
       <div id="userdetails"></div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </div>
    
    <!-- View Modal -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Profile Data</h4>
      </div>
      <div class="modal-body" id="">
        <div id="dynamicTable"></div>
      <div id="Rs232"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>


<!-- Edit Modal -->
<div id="EditModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
        <p>Some text in the modal.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
<script>
 function inittable() 
  {
  $('#expiryreport')
  .dataTable(
  {
  "sPaginationType" : "full_numbers",
  "oLanguage" : {
  "sLengthMenu" : "Entries per page:<span class='lenghtMenu'> _MENU_</span><span class='lengthLabel'></span>"
  },
   "sDom": 'T<"clear">lfrtip',
  "oTableTools" : {
  "sSwfPath" : "plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
  }
  });
  $("div.tbl-tools-searchbox select").addClass(
  'tbl_length');
  }
</script>

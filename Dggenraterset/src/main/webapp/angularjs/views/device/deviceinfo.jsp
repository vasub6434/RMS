   <script type="text/javascript">
   function getdata()
   {
	   callGETJSONAjax("admin/deviceinfolist",getjsondata);
   }
        $(document).ready(function () {
        	getdata();
        	$("#addModal").on('hidden.bs.modal', function () {
        	    $(this).data('bs.modal', null);
        	});
        	

      });

        function edit(id)
        {
        	$("#cid").val(id);
            $('#addModal').modal({show:true, remote: "angularjs/views/device/editdeviceinfo.jsp"});
        } 
      
        function getjsondata(msg2)
        {
          	var  json= eval(msg2);
          	  var tablebody="";
          	  
            var tableheader = '<table id="expiryreport" class="table table-striped" style="border-color:#3C8DBC;"><thead style="background-color:#3C8DBC; color:white;"><tr><th>No.</th><th >Device Name</th><th >Profile Name</th><th>Edit</th></tr></thead><tbody>';
                for (var i = 0; i < json.length; i++)
                {
              	  tablebody += '<tr><td>'+(i+1)+'</td><td>' + json[i].devicename + '</td><td>' + json[i].profilename + '</td><td><button type="button" data-toggle="modal" class="btn btn-info"  onClick="edit('+json[i].deviceinfo_id+')">Edit</button> <button type="button" data-toggle="modal" class="btn btn-danger"  onClick="deletealert('+json[i].deviceinfo_id+');">Delete</button></td></td></tr>';
                }
              // alert(tableheader+tablebody);
                $("#deviceinfo").html(tableheader+tablebody+ "</tbody></table>");
                inittable();
               
        }
       
       
     </script> 
      <div class="row">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Device Info</h3>
            </div> 
    <div class="box-body">
     <div class="row  border-bottom white-bg dashboard-header">
                    <div class="col-xs-4 col-xs-offset-4">
                      <a href="angularjs/views/device/adddeviceinfo.jsp" class="btn btn-primary" data-toggle="modal" data-target="#addModal">Add DeviceInfo</a>
                        <input  class="btn btn-info" type="button" value="Refresh" onclick="getdata()"/>
                   </div>
					<input type="hidden" id="cid" >
            </div>
       <div id="deviceinfo"></div>

           </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
      
  <!-- Modal -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content animated flipInY">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
        Please wait.............
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>
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
        	callDELETEJSONAjax("admin/deviceinfo/"+id,deletejsondata);
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

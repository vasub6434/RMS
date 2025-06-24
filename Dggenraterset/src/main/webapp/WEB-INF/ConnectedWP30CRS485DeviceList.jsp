<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="org.jboss.netty.channel.MessageEvent"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.Socket"%>
 <%@page import="com.bonrix.dggenraterset.DTO.DGHashMap"%>
 <%@page import="com.bonrix.dggenraterset.DTO.WebSocketObj"%>
  <%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WP30CRS485 Device Relay Configuration</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<style>

</style>       
</head>
<body>


<div class="container">
  <div class="page-header">
    <h1 class="text-center">GPRS Based Configurations</h1>      
  </div>      
</div>

<div class="modal fade bd-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Modal title</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" id="sajan">
        <p>Modal body text goes here.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="sendcommand()">Send Command</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<!-- The Modal -->

<%-- <form action="${pageContext.request.contextPath}/SendSocketMesage" method="get">
 --%><table border="1" align="center" id="dataTT" class="table table-bordered">
<%
out.println("<thead>");
out.println("<th>Last Update Time</th>");
out.println("<th>IMEI</th>");
out.println("<th>Action</th>");
out.println("</thead>");
out.println("<tbody>");
for(Map.Entry<String, WebSocketObj> entry : DGHashMap.getInstance().connectedClient.entrySet())
{	
				WebSocketObj wskt=entry.getValue();
				 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                       out.println("<td class='success'>"+wskt.entDate+"</td>");
                       out.println("<td class='success'>"+entry.getKey()+"</td>");  
                       out.println("<td class='success'> <button type='button' id='send-btn' class='btn btn-primary' data-toggle='modal' data-target='#myModal'>Open Command Writer</button></td>");
                       out.println("</tr></form>");
                    
		} 
out.println("</tbody>");
%>

    </table>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
	<script>
	
	var dataTable=null;
	var imei="";
	var cmd="";

$(document).ready(function() {
//	data()
	$.fn.dataTable.ext.errMode = function ( settings, helpPage, message ) { 
	    console.log(message);
	};
	
	var table= $('#dataTT').DataTable({
	"paging": false,
    "bFilter": false,
    "ordering": false,
    "searching": false,
	"info": false
	});
	
	 $('#dataTT tbody').on( 'click', '#send-btn', function () {
        var data = table.row( $(this).parents('tr') ).data();
		imei=data[1];
        console.log(data[1]);
    } );
});
  
/*function data()
{
	$.ajax({
	  	  url:"http://168.119.191.222/api/GetProfile/8",
	  	  type: 'GET',
	  	  success: function(data) {
	  		var dataSet=null;
	  		var my_columns = [];
			var dataSet=data;
			$.each( dataSet[0], function( key, value ) {
					var my_item = {};
					my_item.data = key;
					my_item.title = key;
					my_columns.push(my_item);
			});
		setData(dataSet,my_columns);
	  	  },
	  	 error: function(e) {
	  		console.log(e.message);
	  	  }
	  }); 
}*/
/*
function setData(dataSet,my_columns)
{
	$("#sajan").html('<table id="example" class="table table-bordered" cellspacing="0" width="100%"></table>')
	dataTable=$('#example').DataTable({
    data: dataSet,
    "columns": my_columns,
    paging: false,
    bFilter: false,
    ordering: false,
    searching: false,
	info: false
  });  
}*/

function sendcommand(){
	  cmd="";
	  var start="$MSG,MTR@01:01:";
	  var end="#@;<6906>&";
	  for (i = 1; i <=20; i++) {
		  if($("#address"+i+"").val()!=="" || $("#bytes"+i+"").val()!=="" ||$("#registertype"+i+"").val()!="")
		  {
		  if( !$("#address"+i+"").val().trim() == '' ) {
			  
			  if(i==1)
			cmd+=$("#address"+i+"").val()+":"+"0"+$("#bytes"+i+"").val()+":0"+$("#registertype"+i+"").val()+""+":"+$("#newregistertype"+i+"").val()+"@";
			else
				cmd+="01:0"+i+":"+$("#address"+i+"").val()+":"+"0"+$("#bytes"+i+"").val()+":0"+$("#registertype"+i+"").val()+""+":"+$("#newregistertype"+i+"").val()+"@";
		  }
		  
		  
		/*if(i==1)
			cmd+=$("#address"+i+"").val()+":"+"0"+$("#bytes"+i+"").val()+":01"+":0"+$("#registertype"+i+"").val()+"@";
			else
				cmd+="01:0"+i+":"+$("#address"+i+"").val()+":"+"0"+$("#bytes"+i+"").val()+":01"+":0"+$("#registertype"+i+"").val()+"@";
		  }*/
		  }
		
	  }
	  
	  var sendCMD=start+cmd+end;
	 // console.log(sendCMD);
	 hello(sendCMD);
	}
	
	function hello(sendCMD)
	{
		alert("Snding Message is "+sendCMD);
		$.ajax({
  type: "POST",
  url: "/SendSocketMesage?imei="+imei+"&msg="+encodeURIComponent(sendCMD)+"",
  cache: false,
  success: function(data){
	  $('#msg').val('');
     alert("Message Successfully Sent.!");
  },
 error : function(e) {
				console.log(e.message);
				alert("Something Went Wrong")
			}
});
	}
	
	
	</script>
<!--     </form>
 --></body>
</html>
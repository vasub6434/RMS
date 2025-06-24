<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="org.jboss.netty.channel.MessageEvent"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.Socket"%>
<%@page import="com.bonrix.dggenraterset.DTO.MODBUSHashMap"%>
<%@page import="com.bonrix.dggenraterset.DTO.LogObject"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MODBUS HashMap</title>
</head>  
<body>
	<table border="1" align="center" class="table table-bordered">
		<%
out.println("<thead>");
out.println("<th>IMEI</th>");
out.println("<th>Last Update</th>");
out.println("<th>Message</th>");
out.println("</thead>");
out.println("<tbody>");
for(Map.Entry<String, LogObject> entry : MODBUSHashMap.getInstance().connectedClient.entrySet())
{	
	LogObject wskt=entry.getValue();
				 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                       out.println("<td class='success'><input type='text' name='imei' value='"+entry.getKey()+"'></td>");
                       out.println("<td class='success'><input type='text' name='wskt' value='"+wskt.getEntryDate()+"'></td>");
                       out.println("<td class='danger'>"+wskt.getMsg()+"</td>");  
                       out.println("</tr></form>");   
		} 
out.println("</tbody>");
%>

	</table>

</body>
</html>
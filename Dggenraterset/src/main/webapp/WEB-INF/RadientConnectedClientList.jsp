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
<title>Insert title here</title>
</head>
<body>
<table border="1" align="center" class="table table-bordered">
<%
out.println("<thead>");
out.println("<th>Entry Time</th>");
out.println("<th>Client</th>");
out.println("<th>Session Object</th>");
out.println("<th>Status</th>");
out.println("<th>Action</th>");
out.println("</thead>");
out.println("<tbody>");
for(Map.Entry<String, WebSocketObj> entry : DGHashMap.getInstance().connectedClient.entrySet())
{	
				if(entry.getKey().equalsIgnoreCase("869867034765646"))
				{
					WebSocketObj wskt=entry.getValue();
					 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                System.out.println(formatter.format(wskt.entDate));                      
	                       out.println("<form action='/SendSocketMesage' method='get'><tr >");    
	                       out.println("<td class='success'>"+wskt.entDate+"</td>");
	                       out.println("<td class='success'><input type='text' name='imei' value='"+entry.getKey()+"'></td>");
	                       out.println("<td class='success'><input type='text' name='wskt' value='"+wskt+"'></td>");
	                       out.println("<td class='danger'>"+wskt.getSkt().getRemoteAddress()+"</td>");  
	                       out.println("<td class='success'> <input type='submit' name='button3' value='Send' /></td>");
	                       out.println("</tr></form>");
				}
		} 
out.println("</tbody>");
%>

    </table>
</body>
</html>
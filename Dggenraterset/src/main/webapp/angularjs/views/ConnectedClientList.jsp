<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="org.jboss.netty.channel.MessageEvent"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.Socket"%>
 <%@page import="com.bonrix.dggenraterset.DTO.DGHashMap"%>
 <%@page import="com.bonrix.dggenraterset.DTO.WebSocketObj"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form method="get">
<table border="1" align="center" class="table table-bordered">
<%
out.println("<thead>");
out.println("<th>Client</th>");
out.println("<th>Session Object</th>");
out.println("<th>Status</th>");
out.println("<th>Action</th>");
out.println("</thead>");
out.println("<tbody>");
for(Map.Entry<String, WebSocketObj> entry : DGHashMap.getInstance().connectedClient.entrySet())
{	
				WebSocketObj wskt=entry.getValue();
                       out.println("<tr >");
                       out.println("<td class='success'>"+entry.getKey()+"</td>");
                       out.println("<td class='success'>"+wskt+"</td>");
                       out.println("<td class='danger'>"+wskt.getSkt().getRemoteAddress()+"</td>");
                       out.println("<td class='success'><button type='button'>Click Me!</button></td>");
                       /* if(wskt.isConnected()!=true)
                       out.println("<td class='danger'>Disconnected</td>");
                       else
                    	   out.println("<td class='success'>Connected</td>"); */
                      
                       out.println("</tr>");
                    
		} 
out.println("</tbody>");
%>

    </table>
    </form>
</body>
</html>
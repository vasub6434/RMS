<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.Map"%>
    
     <%@page import="com.bonrix.dggenraterset.DTO.EmiotsHashMap"%>
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
out.println("<th>Message</th>");

out.println("</thead>");
out.println("<tbody>");
for(Map.Entry<String, String> entry : EmiotsHashMap.getInstance().connectedClient.entrySet())
{	
                        
                   
                       out.println("<tr >");
                       out.println("<td class='success'>"+entry.getKey()+"</td>");
                       out.println("<td class='info'>"+entry.getValue()+"</td>");
                       out.println("</tr>");
                    
		} 
out.println("</tbody>");
%>

    </table>
    </form>
</body>
</html>
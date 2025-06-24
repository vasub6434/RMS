package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MODBUSData")
public class MODBUSData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MODBUSData() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/MODBUSDataString.jsp").forward(request, response);
	}


}

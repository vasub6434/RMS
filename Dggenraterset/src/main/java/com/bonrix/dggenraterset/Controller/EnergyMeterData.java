package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EnergyMeterData
 */
@WebServlet("/EnergyMeterData")
public class EnergyMeterData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EnergyMeterData() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/DataString.jsp").forward(request, response);
	}
}

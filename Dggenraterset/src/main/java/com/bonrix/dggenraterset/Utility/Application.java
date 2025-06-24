package com.bonrix.dggenraterset.Utility;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.apache.log4j.PropertyConfigurator;
import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer.HandlerEnergyMeter;




public class Application implements WebApplicationInitializer{
	private Logger log = Logger.getLogger(HandlerEnergyMeter.class);
		public void onStartup(ServletContext servletContext) throws ServletException {
			
		    try {
		    //	PropertyConfigurator.configure("log4j.properties");
		    	// step-1 : set hostName into System's property, which will use by log4j
				System.setProperty("hostName", InetAddress.getLocalHost().getHostName());
		    //step - 2 : set currentDate into System's property, which will use by log4j
		    System.setProperty("currentDate", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
		    log.info("Test "+ InetAddress.getLocalHost().getHostName());
		    } catch (UnknownHostException e) {
				e.printStackTrace();
			}
		    log.info("hiii");
			  WebApplicationContext context = getContext();
		        servletContext.addListener(new ContextLoaderListener(context));
		        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		        dispatcher.setLoadOnStartup(1);
		        dispatcher.addMapping("/");
			
		}
		   private AnnotationConfigWebApplicationContext getContext() {
		        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		        context.setConfigLocation("com.bonrix");
		        return context;
		    }
}

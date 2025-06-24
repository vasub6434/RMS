package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bonrix.common.Log4jsetupForSpring.LMAppender;



@Controller
public class LogController {
	Logger appLog=null;
	
	Logger log=Logger.getLogger(LogController.class);
	@RequestMapping(value = "/Changelogsetup", method = RequestMethod.GET)
	public void homepage(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		 ServletOutputStream out = response.getOutputStream();
		 LMAppender appender = new LMAppender(out);
		String loggerName= request.getParameter("loggerName");
		String action = request.getParameter("action");
		String logLevel = request.getParameter("logLevel");
		if(loggerName!=null)
		{
		 appLog = Logger.getRootLogger();
		}else {
			appLog = Logger.getLogger(loggerName);
		}
		
		
		  if (appLog != null) {
	            if ("resume".equals(action)) {
	            	System.out.println("\"resume\".equals(action)");
	                if (logLevel != null) {
	                	System.out.println("Set level");
	                    appLog.setLevel(Level.toLevel(logLevel));
	                    Enumeration appenders = appLog.getAllAppenders();
	                    while (appenders.hasMoreElements()) {
	                        Appender apndr = (Appender) appenders.nextElement();
	                        if (apndr instanceof LMAppender) {
	                            LMAppender myApndr = (LMAppender) apndr;
	                            myApndr.setLogLevel(logLevel);
	                            myApndr.addLText("Changing log level to " + logLevel);
	                        }
	                    }
	                }
	                System.out.println("setResuming True");
	                appender.setResuming(true, appLog.getEffectiveLevel());
	            } else {
	            	  System.out.println("setResuming false");
	                appender.setResuming(false, appLog.getEffectiveLevel());
	            }
	            System.out.println("appender Add");
	         
	            appLog.addAppender(appender);
	            appender.out.print(" ");
                appender.out.flush();
	            appender.close();
	            //appLog.removeAppender(appender);
	        }
	}
	
	@RequestMapping(value = "/logtest", method = RequestMethod.GET)
	public void logtest(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		System.out.println("In logtest");
		log.info("Print info Log......");
		log.fatal("Print fatal Log......");
		log.trace("Print trace Log......");
		log.debug("Print debug Log......");
	}
}

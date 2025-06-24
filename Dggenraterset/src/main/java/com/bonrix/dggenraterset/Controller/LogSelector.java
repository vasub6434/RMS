/*
   Copyright Jaimon Mathew <mail@jaimon.co.uk>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogSelector  {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Logger rootLogger = Logger.getRootLogger();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Please select the Logger(s) you wish to monitor</title>");
            out.println("<link type='text/css' rel='stylesheet' href='logger/livelog.css'/>");
            out.println("</head>");
            out.println("<body class='tah'>");
            out.println("<form method='GET' action='logger/livelog.html'>");
            out.println("<div id='crc'><div id='crl'>Live Log Monitor v0.1</div><div id='crr'>&copy; <a href='http://www.jaimon.co.uk' target='_blank'>Jaimon Mathew</a></div></div>");
            out.println("<h4>Please select the Logger you wish to monitor</h4>");
            out.println("<select name='loggerName' id='loggerName' class='logsel'>");
            out.println("<option value='root'>Root Logger (" + rootLogger.getEffectiveLevel() + ")</option>");
            Enumeration allLoggers = rootLogger.getLoggerRepository().getRootLogger().getLoggerRepository().getCurrentLoggers();
            while (allLoggers.hasMoreElements()){
                Logger logger = (Logger) allLoggers.nextElement();
                String level = logger.getEffectiveLevel() == null ? "Not set" : logger.getEffectiveLevel().toString();
                out.println("<option value='" + logger.getName() + "'>" + logger.getName() + " (" + level + ")</option>");
            }
            out.println("</select><p />");
            out.println("<input type='submit' value='Start monitoring' class='sbbtn'/>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    } 

    @RequestMapping(value="/LogSelector" ,method=RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	System.out.println("IN LogSelector");
        processRequest(request, response);
    } 


}

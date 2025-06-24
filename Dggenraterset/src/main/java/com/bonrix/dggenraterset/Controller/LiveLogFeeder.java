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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jaimon.LMAppender;
import jaimon.LogMessageBean;
import jaimon.ResourceIO;

@Controller
public class LiveLogFeeder  {

  
	private final static Logger log = Logger.getLogger("LiveLogFeeder");
    private final static Lock lock = new ReentrantLock(true);
    private final static List<Long> runningIds = Collections.synchronizedList(new ArrayList<Long>());
    private final static List<Long> closingIds = Collections.synchronizedList(new ArrayList<Long>());
    private static int noOfClientsAllowed = 10;
    private static int maxNoOfMessagesToWriteOnce = 100;
    private static int intervalBetweenWrites = 500; //In milliseconds

   

  
    private void removeId(Long rid) {
        int noOfTries = 0;
        runningIds.remove(rid);
        closingIds.add(rid);
        while (closingIds.contains(rid) && noOfTries < 20) {
            try {
                //Thread.yield();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
            ++noOfTries;
        }
        if (closingIds.contains(rid)) {
            closingIds.remove(rid);
        }
    }

    @RequestMapping(value="/livelog" ,method=RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = null;
        String logLevel = null;
        long runningId = 0;
        Logger appLog = null;
        ServletOutputStream out = null;
        LMAppender appender = null;
        String loggerName = null;
        long lastRunTime = 0;
        long rid = 0;
        //resume
        action = request.getParameter("action");
        //INFO
        logLevel = request.getParameter("logLevel");
        ///root
        loggerName = request.getParameter("loggerName");

        
        try {
        	//528289983103462
            rid = Long.parseLong(request.getParameter("runningId"));
        } catch (Exception e) {
            rid = 0;
        }
        if (rid == 0) { //New client..
            try {
                lock.lock();
                if (runningIds.size() >= noOfClientsAllowed) {
                    log.trace("Allowed no. of clients exceeded. Kicking out first one");
                    removeId(runningIds.get(0));
                }
                runningId = System.nanoTime();
                runningIds.add(runningId);
                log.trace("New Client. Assigning running id to " + runningId + " (" + runningIds.size() + ")");
            } finally {
                lock.unlock();
            }
        } else {
            removeId(rid);
            if (!"stop".equals(action)) {
                try {
                    lock.lock();
                    runningId = System.nanoTime(); //You may use some other method to get unique ids
                    runningIds.add(runningId);
                    log.trace("Existing Client. Assigning new running id to " + runningId + " (" + runningIds.size() + ")");
                } finally {
                    lock.unlock();
                }
            }
        }

        out = response.getOutputStream();
        response.setContentType("text/html");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html><head><script type='text/javascript'>");
        ResourceIO.writeResource(out, "livelog.js");
        out.println("lmg.runningId=" + runningId + ";");
        out.println("</script><style type='text/css'>");
        ResourceIO.writeResource(out, "livelog.css");
        out.println("</style></head><body>");
        out.println("<!-- ");
        for (int i = 0; i <= 20; i++) {
            //IE waits for a few bytes before it can start rendering the page.
            out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
        out.println("-->");
        out.flush();

        appender = new LMAppender(out);

        if ("stop".equals(action)) {
            appender.close();
        } else {
            if (loggerName == null) {
                appender.addLText("Please specify a logger name");
            } else {
                if("root".equals(loggerName)) {
                    appLog = Logger.getRootLogger();
                }else {
                    appLog = Logger.getLogger(loggerName);
                }
            }
        }
        if (appLog != null) {
            appender.setName("CustomAppender" + runningId);
            if ("resume".equals(action)) {
                if (logLevel != null) {
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
                appender.setResuming(true, appLog.getEffectiveLevel());
            } else {
                appender.setResuming(false, appLog.getEffectiveLevel());
            }
            appLog.addAppender(appender);

            while (runningIds.contains(runningId)) {
                try {
                    if (appender.messages.size() > 0) {
                        writeMessagePortion(appender);
                        lastRunTime = System.currentTimeMillis();
                    }else {
                        if( ((System.currentTimeMillis() - lastRunTime) / 1000) >= 60) {
                            //Send a message every minute to see if the client is still connected, when no message is ready to be written
                            //When disconnected, WebLogic and Glassfish will raise an IOException immediatly
                            //In OC4J, this can take a minute before the container raises an IOException
                            appender.out.print(" ");
                            appender.out.flush();
                            lastRunTime = System.currentTimeMillis();
                        }
                    }
                    Thread.sleep(intervalBetweenWrites);
                } catch (Throwable e) {
                    //System.out.println("Exception " + e.toString());
                    //e.printStackTrace(System.out);
                    runningIds.remove(runningId);
                    break;
                }
            }
            log.trace("Out of the loop for " + runningId + " (" + runningIds.size() + ")");
            closingIds.remove(runningId);
            appender.close();
            appLog.removeAppender(appender);
        }
        out.println("</body></html>");
        out.close();
    }

    private void writeMessagePortion(LMAppender appender) throws Throwable {
        int len;
        LogMessageBean message = null;
        
        len = appender.messages.size();
        if(len > maxNoOfMessagesToWriteOnce) {
            len = maxNoOfMessagesToWriteOnce;
        }
        StringBuilder sb = new StringBuilder();
        for(int cnt = 0;cnt < len; cnt++) {
            message = appender.messages.get(cnt);
            sb.append("lmg.addText('" + message.getMessage().replaceAll("'", "\\\\'") + "','l_" + message.getLevel().toLowerCase() + "');");
        }
        if(message != null) {
            appender.runScript(sb.toString());
            appender.insertText();
        }
        sb = null;
        appender.messages.subList(0, len).clear();
    }
}
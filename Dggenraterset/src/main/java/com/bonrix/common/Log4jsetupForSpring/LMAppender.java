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

package com.bonrix.common.Log4jsetupForSpring;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletOutputStream;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class LMAppender extends AppenderSkeleton {

    public ServletOutputStream out;
    boolean connClosed;
    private Layout appLayout;
    private SimpleDateFormat sdf;
    public List<LogMessageBean> messages;
    public Lock msgLock = new ReentrantLock(true);
    public int maxMessageQLength = 1000;

    public LMAppender(ServletOutputStream out) throws IOException {
        this.out = out;
        connClosed = false;
        appLayout = new PatternLayout("%d %5p - %m%n"); //default layout
        sdf = new SimpleDateFormat("HH:mm:ss");
        messages = Collections.synchronizedList(new ArrayList<LogMessageBean>());
    }

    @Override
    public void append(LoggingEvent event) {
        if(!connClosed && messages.size() < maxMessageQLength) {
            String message = appLayout.format(event).replaceAll("\n", "<br/>").replaceAll("\r", "").replaceAll(" ", "&nbsp;");
            messages.add(new LogMessageBean(message, event.getLevel().toString()));
        }
    }

    public void setResuming(boolean resuming, Level level) throws IOException {
        if (resuming) {
            addLText("Resuming Live Logger (" + level + ")");
        } else {
            addLText("Starting Live Logger (" + level + ")");
        }
        runScript("lmg.stopped=false;window.status='Showing Live Log';parent.lmc.disableButtons(false);parent.lmc.setLogLevel('" + level + "');");
    }

    public void close() {
        if (!connClosed) {
            connClosed = true;
            try {
                runScript("lmg.stopped=true;window.status='Live logger is stopped';parent.lmc.disableButtons(true);");
                addLText("Live logger is stopped");
            } catch (Exception e) {
            }
        }
    }

    public boolean requiresLayout() {
        return true;
    }

    public void setLogLevel(String level) throws IOException {
        runScript("parent.lmc.setLogLevel('" + level + "');");
    }

    public void addLText(String text) throws IOException {
        runScript("lmg.addLText('" + sdf.format(new Date()) + " " + text.replaceAll("'", "\\\\'") + "');");
    }

    public void insertText() throws IOException {
        runScript("lmg.insertText();");
    }

    public void runScript(String text) throws IOException {
        out.print("<script type='text/javascript'>" + text + "</script>");
        out.flush();
    }
}

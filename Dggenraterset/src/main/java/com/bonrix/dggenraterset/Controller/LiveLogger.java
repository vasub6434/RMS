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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jaimon.ResourceIO;

import javax.servlet.ServletOutputStream;

@Controller
public class LiveLogger  {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getPathInfo().substring(1);
        String ext = fileName.split("\\.")[1];
        response.setContentType(getContentType(ext) + ";charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            ResourceIO.writeResource(out, fileName);
        } finally { 
            out.close();
        }
    }

    private String getContentType(String ext) {
        String ct = null;
        if("css".equals(ext)) {
            ct = "text/css";
        }else if("js".equals(ext)) {
            ct = "text/javascript";
        }else {
            ct = "text/html";
        }
        return ct;
    }

    @RequestMapping(value="/LiveLogger" ,method=RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

 
}

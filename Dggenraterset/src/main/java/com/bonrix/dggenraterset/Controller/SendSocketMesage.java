package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.MessageEvent;

import com.bonrix.dggenraterset.DTO.DGHashMap;
import com.bonrix.dggenraterset.DTO.WebSocketObj;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.SocketMessageLog;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.SocketMessageLogRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
 
@WebServlet("/SendSocketMesage")
public class SendSocketMesage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(SendSocketMesage.class);

	SocketMessageLogRepository socketmsgrepository = ApplicationContextHolder.getContext()
			.getBean(SocketMessageLogRepository.class);
	
	DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
			.getBean(DevicemasterRepository.class);

	public SendSocketMesage() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 PrintWriter out = response.getWriter();
		    String imei=request.getParameter("imei");
		    String msg=request.getParameter("msg");
			Devicemaster device = devicemasterRepository.findByImei(imei);
			log.info("SendSocketMesage :: imei "+imei);
			log.info("SendSocketMesage :: msg "+URLDecoder.decode(msg,"UTF-8"));
		    for(Map.Entry<String, WebSocketObj> entry : DGHashMap.getInstance().connectedClient.entrySet())
		    {
		    	if(entry.getKey().equalsIgnoreCase(imei))
		    	{
		    		log.info("SendSocketMesage :: In Map Found");
			    	WebSocketObj wskt=entry.getValue();
			    	MessageEvent e=wskt.getSkt();  
			    	//e.getChannel().write("$MSG,MTR@01:02:003032:02:01@01:03:003034:02:01@#@;<6906>&");
			    	e.getChannel().write(URLDecoder.decode(msg,"UTF-8"));  
			    	log.info("SendSocketMesage :: Message Sent!");
			    	SocketMessageLog sktmsg=new SocketMessageLog();
			    	sktmsg.setDeviceId(device.getDeviceid());
			    	sktmsg.setManagerId(device.getManagerId());
			    	sktmsg.setMessage(URLDecoder.decode(msg,"UTF-8"));
			    	sktmsg.setResponce("None");
			    	socketmsgrepository.saveAndFlush(sktmsg);
			    	log.info("SendSocketMesage :: Log Saved.");
		    	}
		    }
		    return;
	}
}

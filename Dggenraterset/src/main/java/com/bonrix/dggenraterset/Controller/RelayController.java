package com.bonrix.dggenraterset.Controller;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.DTO.DGHashMap;
import com.bonrix.dggenraterset.DTO.WebSocketObj;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.SocketMessageLog;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.SocketMessageLogRepository;

@CrossOrigin(origins = { "*" })
@Transactional
@RestController
public class RelayController {
	private Logger log = Logger.getLogger(RelayController.class);
	
	@Autowired
	SocketMessageLogRepository socketmsgrepository;
	
	@Autowired
	DevicemasterRepository devicemasterRepository;
	
	@RequestMapping(value = { "/api/SendRelayCommand/{imei}/{status}/{password}" }, produces = { "application/json" })
	public String SendRelayCommand(@PathVariable String imei, @PathVariable String status, @PathVariable String password){
		Devicemaster device = devicemasterRepository.findByImei(imei);
		log.info("SendSocketMesage SendRelayCommand is called");
		ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
		String relayPassword = rb.getString("relayPassword");
		if(relayPassword.equals(password)) {
		for(Map.Entry<String, WebSocketObj> entry : DGHashMap.getInstance().connectedClient.entrySet())
	    {
	    	if(entry.getKey().equalsIgnoreCase(imei))
	    	{
	    		log.info("SendSocketMesage :: In Map Found");
		    	WebSocketObj wskt=entry.getValue();
		    	MessageEvent e=wskt.getSkt();  
		    	   
		    	if(status.equalsIgnoreCase("ON")) {
		    		//off
		    		log.info("SendSocketMesage ON :: $MSG,ARM<6906>&");
		    		 e.getChannel().write("$MSG,ARM<6906>&");
		    	}
		    	else if(status.equalsIgnoreCase("OFF")) {
		    		log.info("SendSocketMesage OFF :: $MSG,DISARM<6906>&");
		    		e.getChannel().write("$MSG,DISARM<6906>&");//on
		    	}else
		    	{
		    		log.info("SendSocketMesage Invalid Message ");
		    	}
		    
		    
		    	log.info("SendSocketMesage :: Message Sent!");
		    	SocketMessageLog sktmsg=new SocketMessageLog();
		    	sktmsg=socketmsgrepository.findOne(device.getDeviceid());
		    	if(sktmsg==null) {
		    		SocketMessageLog sktmsgNew=new SocketMessageLog();
		    		sktmsgNew.setSocketid(device.getDeviceid());
		    		sktmsgNew.setDeviceId(device.getDeviceid());
		    		sktmsgNew.setManagerId(device.getManagerId());
		    		sktmsgNew.setMessage(status);
		    		sktmsgNew.setCommandtime(new Date());
		    		sktmsgNew.setResponce("None");
		    		socketmsgrepository.saveAndFlush(sktmsgNew);
		    	}else
		    	{
		    		sktmsg.setMessage(status);
			    	sktmsg.setCommandtime(new Date());
			    	socketmsgrepository.saveAndFlush(sktmsg);
		    	}
		    	
		    	log.info("SendSocketMesage :: Log Saved.");
		    
		    	
	    	}
	    }
		return new SpringException(true, "Success").toString();
	}else
		return new SpringException(true, "Invalid Aurhorization").toString();
	}

}

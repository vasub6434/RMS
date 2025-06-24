package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.DTO.DGHashMap;
import com.bonrix.dggenraterset.DTO.WebSocketObj;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Radient {
	public static class HandlerRadient extends SimpleChannelUpstreamHandler {
		private Logger log=Logger.getLogger(Radient.class);
		
		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);
		
		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);
		
		static DGHashMap map = new DGHashMap();

		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)  
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			log.info("RADIENT In HandlerRadient");   
			String strmsg = (String) e.getMessage();
			log.info("RADIENT String :: " + strmsg);
			
			WebSocketObj getentry = map.getClient("869867034765646");
		//	Devicemaster device = devicemasterRepository.findByImei("861359037504121");
			Devicemaster device = devicemasterRepository.findByImei("869867034765646");
   
			if (getentry == null) {
				WebSocketObj webSocketLog = new WebSocketObj();
				webSocketLog.setEntDate(new Date());
				webSocketLog.setImei(device.getImei());
				webSocketLog.setSkt(e);
				webSocketLog.setStatus(true);
				map.AddClient(device.getImei(), webSocketLog);
			} else {
				getentry.setEntDate(new Date());   
				getentry.setSkt(e);
				map.AddClient(device.getImei(), getentry);
			}
			log.info("RADIENT String :: " + device.getDeviceid());
			Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),new Date(),
					new Date(), new ObjectMapper().readValue(strmsg, Map.class),
					new ObjectMapper().readValue(strmsg, Map.class));
			
			lasttrackrepository.saveAndFlush(lTrack);

	}
	}
}

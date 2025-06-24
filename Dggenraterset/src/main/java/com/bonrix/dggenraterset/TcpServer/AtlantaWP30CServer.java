package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.text.ParseException;
   
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bonrix.common.exception.BonrixException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AtlantaWP30CServer {
	
	public static class HandleAtlantaWP30C extends SimpleChannelUpstreamHandler
	{
		private Logger log = Logger.getLogger(AtlantaWP30CServer.class);
		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg = (String) e.getMessage();
			log.info("AtlantaWP30CServer Main String :: " + msg);
		}
	}
}

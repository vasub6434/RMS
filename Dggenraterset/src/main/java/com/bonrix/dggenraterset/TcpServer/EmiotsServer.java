package com.bonrix.dggenraterset.TcpServer;
 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.DTO.EmiotsHashMap;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class EmiotsServer {

	public static class HandlerEmiotsServer extends SimpleChannelUpstreamHandler {
		private Logger log = Logger.getLogger(EmiotsServer.class);
  
		NumberFormat formatter = new DecimalFormat("#0.000");
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
		static EmiotsHashMap DataHashmap = new EmiotsHashMap();

		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {

			String msg = (String) e.getMessage();
			log.info("EmiotsServer Main String :: " + msg);
			if (!msg.trim().equals("")) {

				String[] splitMessage = msg.split(",");
				String imeinumber = splitMessage[2];
				log.info("EmiotsServer ::: IMEI Convert ::::::::::: " + imeinumber);
				DataHashmap.AddClient(imeinumber, msg);
				try {
					PrintWriter localPrintWriter = new PrintWriter(
							new BufferedWriter(new FileWriter("/opt/tomcat8/webDG/ROOT/EmiotsServer/EmiotsServer_"
									+ this.sdf2.format(new Date()) + ".txt", true)));
					localPrintWriter
							.println("==========================================================================");
					localPrintWriter.println(this.sdf3.format(new Date()) + " EmiotsServer ::" + imeinumber);
					localPrintWriter.println(this.sdf3.format(new Date()) + " EmiotsServer ::" + msg);
					localPrintWriter
							.println("==========================================================================");
					localPrintWriter.close();
				} catch (Exception localException) {
					log.info(localException);
				}
			}

		}

	}
}

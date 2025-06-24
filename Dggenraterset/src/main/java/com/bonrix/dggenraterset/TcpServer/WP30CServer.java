package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.CharsetUtil;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.Model.GPSElement;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.Utility.StringToolsV3;

public class WP30CServer {

	 private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
	//private static Logger LOG = Logger.getLogger(TCPL100Server.class);

	public static class HandlerL100 extends SimpleChannelUpstreamHandler {
	LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
			.getBean(LasttrackRepository.class);

	DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
			.getBean(DevicemasterRepository.class);
	HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	

		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg= (String)e.getMessage();
	    	String msggg[]= msg.split("ATL");
	    	 System.out.println(msg);
	    	String islive = printHexBinary(msggg[0].getBytes(CharsetUtil.UTF_16BE)).toLowerCase();
	    	 System.out.println("islive ::" + islive);
	      
	       System.out.println("L100Listeners:: Msg :Len: "+msg.length());
	      
	      if(msg.length()>10){
	    	  
	      }
	      
	      
	      try{
	    	  String[] msgary=msg.split(",");
	          GPSElement ge=new GPSElement();

	          if(msg.length()>10){
	              
	        	      ge.setImei(msgary[0].substring(5));
	        	      
	        	      String datestr=msgary[10]+msgary[2];
	        	      
	        	      ge.setIsvalid("A".equals(msgary[3]));
	        	      Double spd = 0.0;
	        	      
	        	      if(msgary[8]=="" || msgary[8]==null || msgary[8].isEmpty())
	        	      { 	  
	        	    	   spd=0.0;
	        	      }
	        	      else
	        	      {
	        	    	   spd=Double.parseDouble(msgary[8])*1.852;
	        	      }
	        	      
	        	      System.out.println("HEX::"+DatatypeConverter.printHexBinary(msg.substring(0,2).getBytes()));
	        	      
	        	         
	        	      ge.setIslive(islive.equals("00200001"));
	        	      
	        	      if(msgary[4]=="" || msgary[4]==null)
	        	      { 	  
	        	    	  ge.setLatitude(StringToolsV3.parseLatitude("0.0","N")+"");
	        	      }
	        	      else
	        	      {
	        	    	  ge.setLatitude(StringToolsV3.parseLatitude(msgary[4],"N")+"");
	        	      }
	        	    	  
	        	      if(msgary[6]=="" || msgary[6]==null)
	        	      { 	  
	        	    	  ge.setLangitude(StringToolsV3.parseLatitude("0.0","E")+"");
	        	      }
	        	      else
	        	      {
	        	    	  ge.setLangitude(StringToolsV3.parseLatitude(msgary[6],"E")+"");
	        	      }
	        	      
	        	      
	        	      ge.setSpeed(StringToolsV3.roundTwoDecimals(spd)+"");
	        	      
	        	      if(msgary[9]=="" || msgary[9]==null || msgary[9].isEmpty())
	        	      { 	  
	        	    	  ge.setAngle("0.0");
	        	      }
	        	      else
	        	      {
	        	    	  ge.setAngle(msgary[9]);
	        	      }
	        	      
	        	      
	        	      ge.setOdometer(msgary[18]);
	        	      
	        	      
	        	      BigDecimal bd=new BigDecimal(msgary[16]).multiply(new BigDecimal(12));
	        	      ge.setAna1(bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	        	       System.out.println("Analog 2 :: "+bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	        	      
	        	      String inputs = msgary[14];
	        	   //#01111011000101
	        	      
	        	       System.out.println("Main INPUTS :: "+inputs);
	        	       System.out.println("GREEN :: "+inputs+"  ::  "+inputs.substring(1,2));
	        	       System.out.println("Green Black :: "+inputs+"  ::  "+inputs.substring(6,7));
	        	       System.out.println("Yellow  :: "+inputs+"  ::  "+inputs.substring(3,4));
	        	       System.out.println("Yellow Ornge  :: "+inputs+"  ::  "+inputs.substring(2,3));
	        	       System.out.println("Yellow Black :: "+inputs+"  ::  "+inputs.substring(4,5));
	        	       System.out.println("Yellow Brown :: "+inputs+"  ::  "+inputs.substring(5,6));
	        	       System.out.println("Yellow Red :: "+inputs+"  ::  "+inputs.substring(7,8));
	        	      	  ge.setDig1("1".equals(inputs.substring(1,2)));
	        	    	  ge.setDig2("0".equals(inputs.substring(2,3)));
	        	    	  ge.setDig3("0".equals(inputs.substring(3, 4)));
	        	    	  ge.setDig4("0".equals(inputs.substring(4,5)));
	        	    	  ge.setDig5("0".equals(inputs.substring(5,6)));
	        	    	  ge.setDig6("1".equals(inputs.substring(6,7)));
	        	    	  ge.setDig7("0".equals(inputs.substring(7,8)));
	        	    	  ge.setDig8(false);
	        	      
	        	      ge.setAna2("0.0");
	        	      ge.setAna3("0.0");
	        	      
	        	       System.out.println("DIG6 :: "+ge.getDig6());
	        	      
	        	       ge.setGpsdate(sdf.parse(datestr));
	        	       new WP30C().parseL100(ge);
	        	      }
	      
		 }catch(Exception ex){
	    	  
	    	  ex.printStackTrace();
	    	  
	    	   System.out.println(ex.getMessage());
	      }
		}
	}
	
	
	
	public static String printHexBinary(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}

	public static String hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val + "";
	}


}

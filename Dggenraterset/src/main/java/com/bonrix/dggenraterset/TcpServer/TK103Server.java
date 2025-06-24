package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.jobs.CheckAletrs;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TK103Server {
	private static ResourceBundle rb = ResourceBundle.getBundle("socket");

	public static class HandlerTk103 extends SimpleChannelUpstreamHandler {
		private Logger log = Logger.getLogger(HandlerTk103.class);

		LasttrackRepository lasttrackrepository=ApplicationContextHolder.getContext().getBean(LasttrackRepository.class);

		
		DevicemasterRepository devicemasterRepository=ApplicationContextHolder.getContext().getBean(DevicemasterRepository.class);

		
		HistoryRepository histroyrepository=ApplicationContextHolder.getContext().getBean(HistoryRepository.class);

		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			Calendar cal = Calendar.getInstance();
			double speed = 0.0D;
			String speed1 = "0";
			// 00 9512037471
			// BR001 161201 A 2301.9985N07233.5379E000.00938530.000000000000L00000001)
			//Devicemaster device = null;
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
			String msg1 = (String) e.getMessage();
			String msg=DatatypeConverter.printHexBinary(msg1.getBytes("US-ASCII"));
		//	e.getChannel().a
			//System.out.println("SAM:::msg:::" + msg);
			System.out.println("SAJAN Main String :: "+msg);
			
			System.out.println("byteTK103::"+DatatypeConverter.printHexBinary(msg1.getBytes("US-ASCII")));
				if (!msg.trim().equals("")) {
					String imeinumber = msg.substring(60,80);
					System.out.println("SAM:::MSG:::::::::::"+imeinumber);
					System.out.println(convertHexToString(imeinumber));
					 System.out.println("SAJAN ::: imeinumber::::::::::: "+imeinumber);
					 //84-96
					 //96-108
					 
					    String ALARM_STRING=msg.substring(84, 96);
					    System.out.println("SAJAN ::: ALARM_STRING ::::::::::: "+ALARM_STRING);
					    
						String Mains_Fail=hexto8bit(ALARM_STRING.substring(0,2));
						System.out.println("Mains_Fail :: "+Mains_Fail);
						System.out.println("SAJAN ::: Mains_Fail ::::::::::: "+Mains_Fail.substring(2, 3));
						
						String Gen_Fail_to_Start=Mains_Fail.substring(3,4);
						System.out.println("Gen_Fail_to_Start :: "+Gen_Fail_to_Start);
						//System.out.println("SAJAN ::: Gen_Fail_to_Start ::::::::::: "+Gen_Fail_to_Start.substring(3,4));
						
						   // System.out.println("SAJAN ::: MSG ::::::::::: "+msg);
							String Battery_LVD=hexto8bit(ALARM_STRING.substring(2,4));
							System.out.println("SAJAN ::: Battery_LVD Main ::::::::::: "+Battery_LVD);
							//System.out.println("SAJAN ::: Battery_LVD ::::::::::: "+Battery_LVD.substring(0,1));
						
						
					 String datet = msg.substring(80,104);
					System.out.println("SAJAN ::: datet ::::::::::: "+convertHexToString(datet));
					
					String s=convertHexToString(datet);
					String date=s.substring(0, 2);
					String hour=s.substring(2, 4);
					String minut=s.substring(4,6);
					String second=s.substring(6,8);
					String month=s.substring(8,10);
					String year=s.substring(10,12);
					String datestr=date+month+year+hour+minut+second;
					//System.out.println(sdf.parse(datestr));
					
				/*	for(int ii=80;ii<260;ii=ii+4)
					{
						String BATTERY_CURRENT=msg.substring(ii,ii+4);
						System.out.println("SAJAN ::: datet ::::::::::: "+Integer.parseInt(BATTERY_CURRENT, 16));
					}
					
					for(int ii=260;ii<380;ii=ii+8)
					{
						String BATTERY_CURRENT=msg.substring(ii,ii+4);
						System.out.println("SAJAN ::: datet ::::::::::: "+Integer.parseInt(BATTERY_CURRENT, 16));
					}*/
					//System.out.println("SAJAN ::: MSG ::::::::::: "+msg);
					String BATTERY_CURRENT=msg.substring(140,144);
					//System.out.println("SAJAN ::: BATTERY_CURRENT ::::::::::: "+Integer.parseInt(BATTERY_CURRENT, 16));
					
					String LOAD_CURRENT_3=msg.substring(172,176);
					//System.out.println("SAJAN :::  LOAD_CURRENT_3 ::::::::::: "+Integer.parseInt(LOAD_CURRENT_3, 16));
					
					String LOAD_CURRENT_4=msg.substring(176,180);
					//System.out.println("SAJAN ::: LOAD_CURRENT_4 ::::::::::: "+Integer.parseInt(LOAD_CURRENT_4, 16));
					
					
					String RESERVE=msg.substring(180,184);
					//System.out.println("SAJAN ::: RESERVE ::::::::::: "+Integer.parseUnsignedInt(RESERVE,16));
					
					String GEN_VOLATGE_B=msg.substring(224,228);
					//System.out.println("SAJAN ::: GEN_VOLATGE_B ::::::::::: "+Integer.parseUnsignedInt(GEN_VOLATGE_B,16));
					
					String GEN_CURRENT_R=msg.substring(228,232);
					//System.out.println("SAJAN ::: GEN_CURRENT_R ::::::::::: "+Integer.parseUnsignedInt(GEN_CURRENT_R,16));
					
					
					String ROOM_TEMP=msg.substring(264,268);
					//System.out.println("SAJAN ::: ROOM_TEMP ::::::::::: "+Integer.parseUnsignedInt(ROOM_TEMP,16));
					
					
					String BATTERY_TEMP=msg.substring(268,272);
					//System.out.println("SAJAN ::: BATTERY_TEMP ::::::::::: "+Integer.parseUnsignedInt(BATTERY_TEMP,16));
					
					String RESERVE1=msg.substring(272,276);
					//System.out.println("SAJAN ::: RESERVE1 ::::::::::: "+Integer.parseUnsignedInt(RESERVE1,16));
					
					String GEN_BATTERY_VOLTAGE=msg.substring(276,280);
					//System.out.println("SAJAN ::: GEN_BATTERY_VOLTAGE ::::::::::: "+Integer.parseUnsignedInt(GEN_BATTERY_VOLTAGE,16));
					
					String MAINS_RUN_HRS=ieee(msg.substring(280,288));
					//System.out.println("SAJAN ::: MAINS_RUN_HRS ::::::::::: "+msg.substring(280,288)+" :: "+MAINS_RUN_HRS);
					
					
					String RESERVE2=ieee(msg.substring(344,352));
					//System.out.println("SAJAN ::: RESERVE2 ::::::::::: "+msg.substring(344,352)+" :: "+RESERVE2);
					
					String Rectifier_Energy=ieee(msg.substring(352,360));
					System.out.println("SAJAN ::: Rectifier_Energy ::::::::::: "+msg.substring(352,360)+" :: "+Rectifier_Energy);
					
					String BATTERY_ENERGY=ieee(msg.substring(376,384));
					System.out.println("SAJAN ::: BATTERY_ENERGY ::::::::::: "+msg.substring(376,384)+" :: "+BATTERY_ENERGY);
					
					String LOAD1_ENERGY=ieee(msg.substring(400,408));
					//System.out.println("SAJAN ::: LOAD1_ENERGY ::::::::::: "+msg.substring(400,408)+" :: "+LOAD1_ENERGY);
					
					String LOAD2_ENERGY=ieee(msg.substring(408,416));
					System.out.println("SAJAN ::: LOAD2_ENERGY ::::::::::: "+msg.substring(408,416)+" :: "+LOAD2_ENERGY);
					
					String LOAD3_ENERGY=ieee(msg.substring(416,424));
					System.out.println("SAJAN ::: LOAD3_ENERGY ::::::::::: "+msg.substring(416,424)+" :: "+LOAD3_ENERGY);
					
					//System.out.println("SAJAN ::: imeinumber::::::::::: "+convertHexToString(imeinumber));
					
					Devicemaster device = devicemasterRepository.findByImei(convertHexToString(imeinumber));

					//System.out.println("Last Track :: "+device.getImei());
					//System.out.println("Last Track :: "+device.getDp().getProfilename());
					

					DeviceProfile dp = device.getDp();
					JSONObject jo = new JSONObject();
				//	System.out.println(dp.getParameters());
					JSONArray digitaljsonarr = new JSONArray();
					JSONObject parameters = new JSONObject(dp.getParameters());
					
					//System.out.println("parameters Length :: "+parameters.length());
					JSONArray digitals =parameters.getJSONArray("Digital");
					
  
					JSONArray analogjsonarr = new JSONArray();
					JSONArray analogs =  parameters.getJSONArray("Analog");
					System.out.println("Analog Length :: "+analogs.length());
					for (int i = 0; i < analogs.length(); i++) {
						JSONObject obj = (JSONObject) analogs.get(i);
						System.out.println(obj.get("analogname").toString());
						JSONObject analogobj = new JSONObject();
						if (obj.get("analogname").toString().equalsIgnoreCase("BATTERY CURRENT")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(BATTERY_CURRENT, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							System.out.println("JSON BATTERY_CURRENT :: "+analogValue);
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("BATTERY_CURRENT", analogValue+" "+obj.get("Analogunit").toString());
						}else if (obj.get("analogname").toString().equalsIgnoreCase("LOAD CURRENT 3")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(LOAD_CURRENT_3, 16) * Double.parseDouble(obj.get("Analogformula").toString());
								System.out.println("JSON LOAD_CURRENT_3 :: "+analogValue);
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("LOAD_CURRENT_3", analogValue+" "+obj.get("Analogunit").toString());
						}else if (obj.get("analogname").toString().equalsIgnoreCase("LOAD CURRENT 4")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(LOAD_CURRENT_4, 16) * Double.parseDouble(obj.get("Analogformula").toString());
								System.out.println("JSON LOAD_CURRENT_4 :: "+analogValue);
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("LOAD_CURRENT_4", analogValue+" "+obj.get("Analogunit").toString());
						}else if (obj.get("analogname").toString().equalsIgnoreCase("RESERVE")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(RESERVE, 16) * Double.parseDouble(obj.get("Analogformula").toString());
								System.out.println("JSON RESERVE :: "+analogValue);

							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("RESERVE", analogValue+" "+obj.get("Analogunit").toString());
						}else if (obj.get("analogname").toString().equalsIgnoreCase("GEN VOLATGE B")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(GEN_VOLATGE_B, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("GEN_VOLATGE_B", analogValue+" "+obj.get("Analogunit").toString());
						}else if (obj.get("analogname").toString().equalsIgnoreCase("GEN CURRENT R")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(GEN_CURRENT_R, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("GEN_CURRENT_R", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("ROOM TEMP")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(ROOM_TEMP, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("ROOM_TEMP", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("BATTERY TEMP")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(BATTERY_TEMP, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("BATTERY_TEMP", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("RESERVE1")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(RESERVE1, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("RESERVE1", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("GEN BATTERY VOLTAGE")) {
							double analogValue = 0.0D;
							try {
								analogValue=Integer.parseInt(GEN_BATTERY_VOLTAGE, 16) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("GEN_BATTERY_VOLTAGE", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("MAINS RUN HRS")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(MAINS_RUN_HRS) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								System.out.println("MAINS_RUN_HRS ERROR ");
								ex.printStackTrace();
								//System.out.println("MAINS_RUN_HRS :: "+ex.printStackTrace());
								analogValue = 0.0D;
							}
							analogobj.put("MAINS_RUN_HRS", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("RESERVE2")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(RESERVE2) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								System.out.println("RESERVE2 ");
								ex.printStackTrace();
								analogValue = 0.0D;
							}
							analogobj.put("RESERVE2", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("Rectifier Energy")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(Rectifier_Energy) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("Rectifier_Energy", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("BATTERY ENERGY")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(BATTERY_ENERGY) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("BATTERY_ENERGY", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("LOAD1 ENERGY")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(LOAD1_ENERGY) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("LOAD1_ENERGY", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("LOAD2 ENERGY")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(LOAD2_ENERGY) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("LOAD2_ENERGY", analogValue+" "+obj.get("Analogunit").toString());
						}
						else if (obj.get("analogname").toString().equalsIgnoreCase("LOAD3 ENERGY")) {
							double analogValue = 0.0D;
							try {
								analogValue=Double.parseDouble(LOAD3_ENERGY) * Double.parseDouble(obj.get("Analogformula").toString());
							} catch (Exception ex) {
								analogValue = 0.0D;
							}
							analogobj.put("LOAD3_ENERGY", analogValue+" "+obj.get("Analogunit").toString());
						}
						analogjsonarr.put(analogobj);
					}
					
					JSONArray digital = parameters.getJSONArray("Digital");
					for (int i = 0; i < digital.length(); i++) {
						JSONObject obj = (JSONObject) digital.get(i);
						JSONObject digiobj = new JSONObject();
						if (obj.get("parametername").toString().equalsIgnoreCase("Mains_Fail")) {
							System.out.println(" Mains_Fail.substring(2, 3) :: "+ Mains_Fail.substring(2, 3));
							digiobj.put("Mains_Fail", Mains_Fail.substring(2, 3));
						}
						else if (obj.get("parametername").toString().equalsIgnoreCase("Gen_Fail_to_Start")) {
							digiobj.put("Gen_Fail_to_Start", Mains_Fail.substring(3,4));
							System.out.println("Gen_Fail_to_Start"+" :: "+ Mains_Fail.substring(3,4));
						}else if (obj.get("parametername").toString().equalsIgnoreCase("Battery_LVD")) {
							System.out.println("Battery_LVD"+" :: "+ Battery_LVD.substring(0,1));
							digiobj.put("Battery_LVD", Battery_LVD.substring(0,1));
						}
						
						digitaljsonarr.put(digiobj);
					}    
      
					JSONArray rs232arr = new JSONArray();
					JSONArray rs232 = parameters.getJSONArray("Rs232");
					/*for (int i = 0; i < rs232.length(); i++) {
						JSONObject obj = (JSONObject) rs232.get(i);
						JSONObject rs232obj = new JSONObject();
						if (obj.get("parametername").toString().equalsIgnoreCase("rs232")) {
							boolean rs2321 = false;
							if (rs2321) {
								rs2321 = !((Boolean) obj.get("reverse"));
							} else {
								rs2321 = ((Boolean) obj.get("reverse"));
							}
							rs232obj.put("rs232", rs2321);
						}
						rs232arr.put(rs232obj);
					}*/
					Devicemaster dm=devicemasterRepository.findOne(device.getDeviceid());
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogjsonarr);
					jo.put("Rs232", rs232arr);
                    jo.put("DeviceName", dm.getDevicename());
					System.out.println(dm.getDevicename());
					
					//Lasttrack oldtrack=(Lasttrack) lasttrackrepository.findBydeviceId(device.getDeviceid());
				History hist = new History(device.getDeviceid(), device.getUserId(),sdf.parse(datestr),new Date(),new ObjectMapper().readValue(jo.toString(),Map.class),new ObjectMapper().readValue(jo.toString(),Map.class));
				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),sdf.parse(datestr),new Date(),new ObjectMapper().readValue(jo.toString(),Map.class),new ObjectMapper().readValue(jo.toString(),Map.class));
					histroyrepository.saveAndFlush(hist);
					lasttrackrepository.saveAndFlush(lTrack);   
					//CheckAletrs.checkForAlert(lTrack, oldtrack, device);
				
				}
		}

		public static String convertlatlang(String input) {

			String dgr = input.substring(0, 4);
			String dgr2 = input.substring(4, 8);

			String dg = Integer.parseInt(dgr, 16) + "";
			int dg2 = Integer.parseInt(dgr2, 16);

			String rdgr = dg.substring(0, 2);
			double rdgr2 = Float.parseFloat(dg.substring(2, 4)) / 60;

			double remaindgr = dg2 * 0.0001 / 60;
			double fnl = Integer.parseInt(rdgr) + rdgr2 + remaindgr;

			return fnl + "";
		}

		public static String ieee(String ieeeVal)
		{
			Long i = Long.parseLong(ieeeVal, 16); 
			return ""+Float.intBitsToFloat(i.intValue());
		}
		
	}

	public static void main(String[] args) {

	/*	ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("handler", new TK103Server.HandlerTk103());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(Integer.parseInt(rb.getString("Tk103"))));*/
	}
	
	public static String convertHexToString(String hex){

		  StringBuilder sb = new StringBuilder();
		  StringBuilder temp = new StringBuilder();
		  
		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
		  for( int i=0; i<hex.length()-1; i+=2 ){
			  
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);
			  
		      temp.append(decimal);
		  }
		  System.out.println("Decimal : " + temp.toString());
		  
		  return sb.toString();
	  }
	
	   public static String hexto8bit(String ii)
	    {
		   System.out.println("ii  :: "+ii);
	       int ll=Integer.parseInt(ii,16);

	      String kk=Integer.toBinaryString(ll);

	      for(int j=kk.length();j<8;j++)
	      {
	          kk="0"+kk;
	      }
	      System.out.println("kk :  "+kk);
	      return kk;
	  }

}

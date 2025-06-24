package com.bonrix.dggenraterset.TcpServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.common.utils.AlertMessage;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.jobs.chekAlert;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.math.BigDecimal;

public class TK103ServerNew {


	public static class HandlerTk103New extends SimpleChannelUpstreamHandler {
		private Logger log = Logger.getLogger(TK103ServerNew.class);

		NumberFormat formatter = new DecimalFormat("#0.000");
	     SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	     SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");

	     DigitalInputAlertService alertservices = ApplicationContextHolder.getContext()
	 			.getBean(DigitalInputAlertService.class);
	     
		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);

		ParameterRepository parameterrepository = ApplicationContextHolder.getContext()
				.getBean(ParameterRepository.class);

		@SuppressWarnings("unchecked")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {

			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
			String msg1 = (String) e.getMessage();
			String msg = DatatypeConverter.printHexBinary(msg1.getBytes("US-ASCII"));
			
			log.info("BONRIX Main String :: " + msg);
			
			

			log.info("byteTK103::" + DatatypeConverter.printHexBinary(msg1.getBytes("US-ASCII")));
			if (!msg.trim().equals("")) {
				

				String imeinumber = msg.substring(60, 80);
				log.info("BONRIX ::: IMEI Convert ::::::::::: " + convertHexToString(imeinumber));

				 try
			      {
			          PrintWriter localPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter("/home/tomcat8/webapps/ROOT/DGLOG/Delta_" + this.sdf2.format(new Date()) + ".txt", true)));
			          localPrintWriter.println("==========================================================================");
			          localPrintWriter.println(this.sdf3.format(new Date()) + "::" + convertHexToString(imeinumber));
			          localPrintWriter.println(this.sdf3.format(new Date()) + "::" + msg);
			          localPrintWriter.println("==========================================================================");
			          localPrintWriter.close();
			        }
			      catch (Exception localException)
			      {
			        log.info(localException);
			      }
				 
				Devicemaster device = devicemasterRepository.findByImei(convertHexToString(imeinumber));
				
				if(device!=null)
				{
				log.info("BONRIX Main String  OF "+convertHexToString(imeinumber)+" :: "+ msg);
				log.info("BONRIX:::IMEI Bytes:::::::::::  " + imeinumber);
				log.info(convertHexToString(imeinumber));

				String STATUS_STRING = msg.substring(116,128); 
				String hex4bit=hexto4bit(STATUS_STRING);
				String FINAL_STATUS_STRING=reverse(hexto4bit(STATUS_STRING));
				String LOAD_ON_BATTERY=""+FINAL_STATUS_STRING.charAt(0);
				String LOAD_ON_DG=""+FINAL_STATUS_STRING.charAt(2);
				String LOW_BATTRY=""+FINAL_STATUS_STRING.charAt(13);
				String FUSE_FAIL=""+FINAL_STATUS_STRING.charAt(14);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" "+convertHexToString(imeinumber)+" :: --------------------------------------------------------------------------------------------------------");
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" STATUS_STRING Main :: "+STATUS_STRING);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" STATUS_STRING hex4bit :: "+hex4bit);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" STATUS_STRING reverse :: "+reverse(hex4bit));
			    log.info("DELTA DIGITAL  "+convertHexToString(imeinumber)+" FINAL_STATUS_STRING :: "+FINAL_STATUS_STRING);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" Main String  OF "+convertHexToString(imeinumber)+" :: "+ msg);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" LOAD_ON_BATTERY  :: "+LOAD_ON_BATTERY);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+"  LOAD_ON_DG :: "+convertHexToString(imeinumber)+" :: "+LOAD_ON_DG);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" LOW_BATTRY :: "+convertHexToString(imeinumber)+" :: "+LOW_BATTRY);
				log.info("DELTA DIGITAL "+convertHexToString(imeinumber)+" FUSE_FAIL :: "+convertHexToString(imeinumber)+" :: "+FUSE_FAIL); 
				log.info("DELTA DIGITAL :: ----------------------------------------------------");
				
				
				
				String ALARM_STRING = msg.substring(104,116); 
				 log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" "+convertHexToString(imeinumber)+" :: --------------------------------------------------------------------------------------------------------");
					log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" Main String  OF "+convertHexToString(imeinumber)+" :: "+ msg);

				 log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING Main :: "+ALARM_STRING);

				log.info(hexto4bit(ALARM_STRING));
				log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING hexto4bit :: "+hexto4bit(ALARM_STRING));
				log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING reverse :: "+reverse(ALARM_STRING));
				 String finalString= reverse(hexto4bit(ALARM_STRING));
				log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING  finalString :: "+finalString);
				 String Mains_Fail =""+finalString.charAt(2);
				 String Battery_LVD =""+finalString.charAt(8);
				log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING Mains_Fail :: "+Mains_Fail);
				log.info("DELTA ANALOG "+convertHexToString(imeinumber)+" ALARM_STRING Battery_LVD :: "+Battery_LVD);


				String datet = msg.substring(80, 104);

				String s = convertHexToString(datet);
				String date = s.substring(0, 2);
				String hour = s.substring(2, 4);
				String minut = s.substring(4, 6);
				String second = s.substring(6, 8);
				String month = s.substring(8, 10);
				String year = s.substring(10, 12);
				String datestr = date + month + year + hour + minut + second;

				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" MSG ::::::::::: " + msg);
				String BATTERY_CURRENT = msg.substring(140, 144);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" BATTERY_CURRENT ::::::::::: " + Integer.parseInt(BATTERY_CURRENT, 16));

				String SITE_BATTERY_VOLTAGE_DC = msg.substring(156,160);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" Site_Battery_voltage_DC ::::::::::: " + formatter.format(new BigDecimal(new BigInteger(String.valueOf(Integer.parseInt(SITE_BATTERY_VOLTAGE_DC, 16))),1)));
				
				String LOAD_CURRENT_1 = msg.substring(164, 168);
				log.info("BONRIX :::  "+convertHexToString(imeinumber)+" LOAD_CURRENT_1 ::::::::::: " + Integer.parseInt(LOAD_CURRENT_1, 16));
				
				String LOAD_CURRENT_2 = msg.substring(168,172);
				log.info("BONRIX :::  "+convertHexToString(imeinumber)+" LOAD_CURRENT_2 ::::::::::: " + Integer.parseInt(LOAD_CURRENT_2, 16));
				
				String LOAD_CURRENT_3 = msg.substring(172, 176);
				log.info("BONRIX :::  "+convertHexToString(imeinumber)+" LOAD_CURRENT_3 ::::::::::: " + Integer.parseInt(LOAD_CURRENT_3, 16));

				String LOAD_CURRENT_4 = msg.substring(176, 180);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" LOAD_CURRENT_4 ::::::::::: " + Integer.parseInt(LOAD_CURRENT_4, 16));

				String RESERVE = msg.substring(180, 184);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" RESERVE ::::::::::: " + Integer.parseUnsignedInt(RESERVE, 16));

				String GEN_VOLATGE_B = msg.substring(224, 228);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" GEN_VOLATGE_B ::::::::::: " + Integer.parseUnsignedInt(GEN_VOLATGE_B, 16));

				String GEN_CURRENT_R = msg.substring(228, 232);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" GEN_CURRENT_R ::::::::::: " + Integer.parseUnsignedInt(GEN_CURRENT_R, 16));

				String ROOM_TEMP = msg.substring(264, 268);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" ROOM_TEMP ::::::::::: " + Integer.parseUnsignedInt(ROOM_TEMP, 16));

				String BATTERY_TEMP = msg.substring(268, 272);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" BATTERY_TEMP ::::::::::: " + Integer.parseUnsignedInt(BATTERY_TEMP, 16));

				String RESERVE1 = msg.substring(272, 276);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" RESERVE1 ::::::::::: " + Integer.parseUnsignedInt(RESERVE1, 16));

				String GEN_BATTERY_VOLTAGE = msg.substring(276, 280);
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" GEN_BATTERY_VOLTAGE ::::::::::: "+ Integer.parseUnsignedInt(GEN_BATTERY_VOLTAGE, 16));

				String MAINS_RUN_HRS = ieee(msg.substring(280, 288));
				log.info(
						"BONRIX ::: "+convertHexToString(imeinumber)+" MAINS_RUN_HRS ::::::::::: " + msg.substring(280, 288) + " :: " + MAINS_RUN_HRS);

				String RESERVE2 = ieee(msg.substring(344, 352));
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" RESERVE2 ::::::::::: " + msg.substring(344, 352) + " :: " + RESERVE2);

				String Rectifier_Energy = ieee(msg.substring(352, 360));
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+"  Rectifier_Energy ::::::::::: " + msg.substring(352, 360) + " :: "
						+ Rectifier_Energy);

				String BATTERY_ENERGY = ieee(msg.substring(376, 384));
				log.info(
						"BONRIX :::  "+convertHexToString(imeinumber)+" BATTERY_ENERGY ::::::::::: " + msg.substring(376, 384) + " :: " + BATTERY_ENERGY);

				String LOAD1_ENERGY = ieee(msg.substring(400, 408));
				log.info(
						"BONRIX ::: "+convertHexToString(imeinumber)+" LOAD1_ENERGY ::::::::::: " + msg.substring(400, 408) + " :: " + LOAD1_ENERGY);

				String LOAD2_ENERGY = ieee(msg.substring(408, 416));
				log.info(
						"BONRIX ::: "+convertHexToString(imeinumber)+" LOAD2_ENERGY ::::::::::: " + msg.substring(408, 416) + " :: " + LOAD2_ENERGY);

				String LOAD3_ENERGY = ieee(msg.substring(416, 424));
				log.info("BONRIX ::: "+convertHexToString(imeinumber)+" LOAD3_ENERGY ::::::::::: " + msg.substring(416, 424) + " :: " + LOAD3_ENERGY);


				
				log.info("BONRIX :::  "+convertHexToString(imeinumber)+" DEVICE OBJECT :: "+ device);
				

	DeviceProfile dp = device.getDp();
	JSONObject jo = new JSONObject();
	JSONObject gjo = new JSONObject();
	JSONArray digitaljsonarr = new JSONArray();
	JSONObject parameters = new JSONObject(dp.getParameters());

	JSONArray analogjsonarr = new JSONArray();
	JSONArray analogs = parameters.getJSONArray("Analog");
	log.info("Analog Length :: "+convertHexToString(imeinumber)+"  :: " + analogs.length());
	for (int i = 0; i < analogs.length(); i++) {
		try {
			JSONObject obj = (JSONObject) analogs.get(i);
			Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
			JSONObject analogobj = new JSONObject();

			if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("BATTERY_CURRENT")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(BATTERY_CURRENT, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}

				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_CURRENT_3")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(LOAD_CURRENT_3, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_CURRENT_4")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(LOAD_CURRENT_4, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("RESERVE")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(RESERVE, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("GEN_VOLATGE_B")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(GEN_VOLATGE_B, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("GEN_CURRENT_R")) {
				double analogValue = 0.0D;
				try {
					analogValue = Integer.parseUnsignedInt(GEN_CURRENT_R, 16)
							* Double.parseDouble(obj.get("Analogformula").toString());
				} catch (Exception ex) {
					analogValue = 0.0D;
				}
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("ROOM_TEMP")) {
				double analogValue = 0.0D;
				analogValue = Integer.parseUnsignedInt(ROOM_TEMP, 16)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("BATTERY_TEMP")) {
				double analogValue = 0.0D;
				analogValue = Integer.parseUnsignedInt(BATTERY_TEMP, 16)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("RESERVE1")) {
				double analogValue = 0.0D;
				analogValue = Integer.parseUnsignedInt(RESERVE1, 16)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("GEN_BATTERY_VOLTAGE")) {
				double analogValue = 0.0D;
				analogValue = Integer.parseUnsignedInt(GEN_BATTERY_VOLTAGE, 16)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("MAINS_RUN_HRS")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(MAINS_RUN_HRS)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("RESERVE2")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(RESERVE2)
						* Double.parseDouble(obj.get("Analogformula").toString());

				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Rectifier_Energy")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(Rectifier_Energy)
						* Double.parseDouble(obj.get("Analogformula").toString());

				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("BATTERY_ENERGY")) {
				double analogValue = 0.0D;

				analogValue = Double.parseDouble(BATTERY_ENERGY)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD1_ENERGY")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(LOAD1_ENERGY)
						* Double.parseDouble(obj.get("Analogformula").toString());

				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD2_ENERGY")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(LOAD2_ENERGY)
						* Double.parseDouble(obj.get("Analogformula").toString());

				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD3_ENERGY")) {
				double analogValue = 0.0D;
				analogValue = Double.parseDouble(LOAD3_ENERGY)
						* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("SITE_BATTERY_VOLTAGE_DC")) {
			//	BigDecimal analogValue = 0.0;
				double	analogValue = Double.parseDouble(""+new BigDecimal(new BigInteger(String.valueOf(Integer.parseInt(SITE_BATTERY_VOLTAGE_DC, 16))),1))* Double.parseDouble(obj.get("Analogformula").toString());
					//	* Double.parseDouble(obj.get("Analogformula").toString());
				analogobj.put(param.getId().toString(), formatter.format(analogValue));
			}
			else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_CURRENT_1")) {
				//	BigDecimal analogValue = 0.0;
					double	analogValue = Double.parseDouble(""+new BigDecimal(new BigInteger(String.valueOf(Integer.parseInt(LOAD_CURRENT_1, 16))),1))* Double.parseDouble(obj.get("Analogformula").toString());
						//	* Double.parseDouble(obj.get("Analogformula").toString());
					analogobj.put(param.getId().toString(), formatter.format(analogValue));
				}
			else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_CURRENT_2")) {
				//	BigDecimal analogValue = 0.0;
					double	analogValue = Double.parseDouble(""+new BigDecimal(new BigInteger(String.valueOf(Integer.parseInt(LOAD_CURRENT_2, 16))),1))* Double.parseDouble(obj.get("Analogformula").toString());
						//	* Double.parseDouble(obj.get("Analogformula").toString());
					analogobj.put(param.getId().toString(), formatter.format(analogValue));
				}
			analogjsonarr.put(analogobj);
		} catch (Exception ex) {   
			ex.printStackTrace();   
		}

	}

	JSONArray digital = parameters.getJSONArray("Digital");
	for (int i = 0; i < digital.length(); i++) {
		JSONObject obj = (JSONObject) digital.get(i);
		JSONObject digiobj = new JSONObject();
		Parameter param = parameterrepository.findByid(new Long(obj.get("parameterId").toString()));
        

		boolean reverse=(Boolean)obj.get("reverse");
		if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Mains_Fail")) {
			if(reverse==true) {
				//log.info("IN IF Mains_Fail_reverse :: "+ Mains_Fail.substring(2, 3));
				digiobj.put(param.getId().toString(),"1".equals( Mains_Fail)? "0" : "1");

			}
			else
				digiobj.put(param.getId().toString(), Mains_Fail);
		} else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Battery_LVD")) {
			if(reverse==true) {
			//	log.info("IN IF Battery_LVD_reverse :: "+ Mains_Fail.substring(0, 1));
				digiobj.put(param.getId().toString(),"1".equals( Battery_LVD)? "0" : "1");
			}
			else
			digiobj.put(param.getId().toString(), Battery_LVD);
		}
		
else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_ON_BATTERY")) {
			
			if(reverse==true) {
			//	log.info("IN IF Gen_Fail_to_Start_reverse :: "+ Mains_Fail.substring(3, 4));
				digiobj.put(param.getId().toString(),"1".equals( LOAD_ON_BATTERY)? "0" : "1");
			}
			else
			digiobj.put(param.getId().toString(), LOAD_ON_BATTERY);
		} 
		
else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOAD_ON_DG")) {
	
	if(reverse==true) {
	//	log.info("IN IF Gen_Fail_to_Start_reverse :: "+ Mains_Fail.substring(3, 4));
		digiobj.put(param.getId().toString(),"1".equals( LOAD_ON_DG)? "0" : "1");
	}
	else
	digiobj.put(param.getId().toString(), LOAD_ON_DG);
} 
		
else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("LOW_BATTRY")) {
	
	if(reverse==true) {
	//	log.info("IN IF Gen_Fail_to_Start_reverse :: "+ Mains_Fail.substring(3, 4));
		digiobj.put(param.getId().toString(),"1".equals( LOW_BATTRY)? "0" : "1");
	}
	else
	digiobj.put(param.getId().toString(), LOW_BATTRY);
} 
		
else if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("FUSE_FAIL")) {
	
	if(reverse==true) {
	//	log.info("IN IF Gen_Fail_to_Start_reverse :: "+ Mains_Fail.substring(3, 4));
		digiobj.put(param.getId().toString(),"1".equals( FUSE_FAIL)? "0" : "1");
	}
	else
	digiobj.put(param.getId().toString(), FUSE_FAIL);
} 

		digitaljsonarr.put(digiobj);
	}
	//log.info("DIGI :: "+digitaljsonarr.toString());

	JSONArray rs232arr = new JSONArray();
	Devicemaster dm = devicemasterRepository.findOne(device.getDeviceid());
	jo.put("Digital", digitaljsonarr);
	jo.put("Analog", analogjsonarr);
	jo.put("Rs232", rs232arr);
	jo.put("DeviceName", dm.getDevicename());
	log.info(device.getDevicename()+" :: "+device.getDeviceid());

	 Lasttrack oldtrack=null;
	List<Lasttrack> track=lasttrackrepository.findBydeviceId(device.getDeviceid());
	boolean isInsert=false;
	//alertservices.findBymanagerid(deviceid)
	 if(track.size()!=0) {
		 oldtrack=track.get(0); 
		 isInsert=true;
	 }
	 
	  
	 
	History hist = new History(device.getDeviceid(), device.getUserId(), sdf.parse(datestr), new Date(),
			new ObjectMapper().readValue(jo.toString(), Map.class),
			new ObjectMapper().readValue(gjo.toString(), Map.class));
	
	Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), sdf.parse(datestr),
			new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
			new ObjectMapper().readValue(gjo.toString(), Map.class));
	try {
		
		histroyrepository.saveAndFlush(hist);
		lasttrackrepository.saveAndFlush(lTrack);
		chekAlert alet=new chekAlert();
		if(isInsert)
		alet.sendMsg(device,lTrack, oldtrack);

		
	}catch(Exception exception)
	{
		exception.printStackTrace();
	}
	 
}else {
	log.info("can't find IMEI..... "+convertHexToString(imeinumber)+" DEVICE IMEI  "+convertHexToString(imeinumber));
}
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

		public static String ieee(String ieeeVal) {
			Long i = Long.parseLong(ieeeVal, 16);
			return "" + Float.intBitsToFloat(i.intValue());
		}

	}

	public static void main(String[] args) {

		/*
		 * ServerBootstrap bootstrap = new ServerBootstrap( new
		 * NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
		 * Executors.newCachedThreadPool()));
		 * 
		 * bootstrap.setPipelineFactory(new ChannelPipelineFactory() { public
		 * ChannelPipeline getPipeline() throws Exception { ChannelPipeline pipeline =
		 * Channels.pipeline(); pipeline.addLast("decoder", new StringDecoder());
		 * pipeline.addLast("encoder", new StringEncoder()); pipeline.addLast("handler",
		 * new TK103Server.HandlerTk103()); return pipeline; } }); bootstrap.bind(new
		 * InetSocketAddress(Integer.parseInt(rb.getString("Tk103"))));
		 */
	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		//log.info("Decimal : " + temp.toString());

		return sb.toString();
	}

	public static String hexto8bit(String ii) {
		//log.info("ii  :: " + ii);
		int ll = Integer.parseInt(ii, 16);

		String kk = Integer.toBinaryString(ll);

		for (int j = kk.length(); j < 8; j++) {
			kk = "0" + kk;
		}
	//	log.info("kk :  " + kk);
		return kk;
	}
	
	public  static String  intToBinary(int a) {
		String temp = Integer.toBinaryString(a);
		while(temp.length() !=8){ temp = "0"+temp; 
		} 
		//log.info("TEMP :: "+temp); 
		return temp;
		}
	
	public static String reverse(String str)
	{
StringBuilder sb = new StringBuilder();
        for(int i = str.length() - 1; i >= 0; i--)
            sb.append(str.charAt(i));
        return sb.toString();
	}
	public static String  hexto4bit(String ii) {
		String st="";
		for(int i=0;i<ii.length();i++)
		{
			int ll = Integer.parseInt(""+ii.charAt(i), 16);
			String kk = Integer.toBinaryString(ll);
			for (int j = kk.length(); j < 4; j++) 
				kk = "0" + kk;
			st+=kk;
		}
		return st;
	}

	/*
	 * public ChannelHandler HandlerTk103New() { // TODO Auto-generated method stub
	 * return null; }
	 */

}

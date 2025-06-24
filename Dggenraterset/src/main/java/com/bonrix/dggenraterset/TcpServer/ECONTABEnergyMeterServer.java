package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.Utility.StringToolsV3;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.bonrix.dggenraterset.jobs.MyAnalogAlert;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ECONTABEnergyMeterServer extends SimpleChannelUpstreamHandler {

	
	private static final Logger log = Logger.getLogger(ECONTABEnergyMeterServer.class);
	
	public static class HandlerECONTAB extends SimpleChannelUpstreamHandler  {
		
		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);
		
		
		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);
		
		HistoryRepository histroyrepository = ApplicationContextHolder.getContext()
				.getBean(HistoryRepository.class);
		
		DevicemasterRepository deviceReop = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);
		
		ParameterRepository parameterrepository = ApplicationContextHolder.getContext()
				.getBean(ParameterRepository.class);
		
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		
		@SuppressWarnings({ "unchecked", "unused" })  
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			
			String msg = (String) e.getMessage();
			log.info("HandlerECONTAB String :: " + msg);
			
			ObjectMapper mapper = new ObjectMapper();
		 	log.info("HandlerECONTAB :: "+msg);
			String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String imei=msgary[0].substring(5);
			//log.info("WP30CRS485 "+imei+" IMEI :: "+imei);
			String datestr=msgary[10]+msgary[2];
			//log.info("WP30CRS485 "+imei+" DATE :: "+datestr);
			 Devicemaster device = devicemasterRepository.findByImei(msgary[0].substring(5));

			if (msgary[1].contains("$GPRMC")) {

				log.info("HandlerECONTAB "+imei+" :: "+msg);
			//	log.info("WP30CRS485 "+imei+" GPS :: $GPRMC String");
				String[] gpdAnalog= {"6387981","6387982"};
				 List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
				String digitalData=msgary[14];
				boolean Isvalid="A".equals(msgary[3]);
				
				if(Isvalid==true || Isvalid==false) {
					JSONArray analogjsonarr = new JSONArray();
					JSONArray digitaljsonarr = new JSONArray();
					
			//	Devicemaster device = devicemasterRepository.findByImei(imei);
				System.out.println(imei);
				 DeviceProfile profile=device.getDp();
				 JSONObject parameters = new JSONObject(profile.getParameters());
			      JSONArray digital = parameters.getJSONArray("Digital");
			      JSONArray analog =  parameters.getJSONArray("Analog");
			      double analogFormula=1;
			      for (int i = 0; i < analog.length(); i++) {//1.3
			      JSONObject obj = (JSONObject) analog.get(i);
			      if(obj.get("Analoginput").toString().equalsIgnoreCase("6387981"))
			      {  
			    	  log.info("HandlerECONTAB "+imei+" IN FORMULA  :: "+obj.get("Analogformula").toString());
			    	  analogFormula=Double.parseDouble(obj.get("Analogformula").toString());
			    	  log.info("HandlerECONTAB "+imei+" IN FORMULA>>>>>> :: "+obj.get("Analogformula").toString());
			      }
			      }
			      
			      String ACMAINS_FAIL=digitalData.substring(1,2);
			      String Fire=digitalData.substring(2,3);
			      String Door=digitalData.substring(3,4);
			      String DG_Running_Hrs=digitalData.substring(4,5);
			      String DG_Fault=digitalData.substring(5,6);
			      String Battry_Low=digitalData.substring(6,7);
			      String PP_Input_Fail=digitalData.substring(7,8);
			      
			    /*  log.info("HandlerECONTAB "+imei+" GPS Main INPUTS :: "+digitalData);
			      log.info("HandlerECONTAB "+imei+" GPS GREEN ACMAINS_FAIL :: "+digitalData.substring(1,2));
			      log.info("HandlerECONTAB "+imei+" GPS Yellow Ornge Fire ::"+digitalData.substring(2,3));
			      log.info("HandlerECONTAB "+imei+" GPS Yellow Door Door :: "+digitalData.substring(3,4));
			      log.info("HandlerECONTAB "+imei+" GPS Yellow Black DG_Running_Hrs :: "+digitalData.substring(4,5));
			      log.info("HandlerECONTAB "+imei+" GPS Yellow Brown DG_Fault :: "+digitalData.substring(5,6));
			      log.info("HandlerECONTAB "+imei+" GPS Green Black Battry_Low :: "+digitalData.substring(6,7));
			      log.info("HandlerECONTAB "+imei+" GPS Yellow Red :: PP_Input_Fail "+digitalData.substring(7,8));*/
			      
			      for (int i = 0; i <digital.length(); i++) {
			    	  JSONObject digiobj = new JSONObject();
			    	  JSONObject obj = (JSONObject) digital.get(i);
			    	  Double d = Double.parseDouble(obj.get("dioindex").toString());
			    	  Parameter param = parameterrepository.findByid(new Long(obj.get("parameterId").toString()));
			    	  boolean reverse = (Boolean) obj.get("reverse");
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("ACMAINS_FAIL")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(ACMAINS_FAIL) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), ACMAINS_FAIL);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Fire")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(Fire) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), Fire);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Door")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(Door) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), Door);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("DG_Running")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(DG_Running_Hrs) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), DG_Running_Hrs);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("DG_Fault")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(DG_Fault) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), DG_Fault);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Battery_Low")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(Battry_Low) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), Battry_Low);
							}
						}
			    	  
			    	  if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("PP_Input_Fail")) {
							if (reverse == true) {
								digiobj.put(param.getId().toString(), "1".equals(PP_Input_Fail) ? "0" : "1");
							} else {
								digiobj.put(param.getId().toString(), PP_Input_Fail);
							}
						}
			    	  digitaljsonarr.put(digiobj);
			      }
			      String latitude;
			      String longitude;
			      if(msgary[4]=="" || msgary[4]==null)
			      { 	  
			    	 // latitude= StringToolsV3.parseLatitude("0.0","N")+"";
			    	  latitude ="23.033863";
			      }
			      else
			      {
			    	  latitude=StringToolsV3.parseLatitude(msgary[4],"N")+"";
			      }
			    	  
			      if(msgary[6]=="" || msgary[6]==null)
			      { 	  
			    	 // longitude=StringToolsV3.parseLatitude("0.0","E")+"";
			    	  longitude="72.585022";  
			      }
			      else
			      {
			    	  longitude=StringToolsV3.parseLatitude(msgary[6],"E")+"";
			      }
			      
			      JSONObject gjo = new JSONObject();
			       /* gjo.put("latitude", latitude);
					gjo.put("longitude", longitude);*/
					 gjo.put("latitude", "23.033863");
						gjo.put("longitude", "72.585022");
					gjo.put("DeviceDate", new Date());
					gjo.put("fuel", "0");
					
				//	String battry=msgary[15];
					String fuel=msgary[16]; 
					//fuelFormula
					double battry=Double.parseDouble(msgary[15].toString()) * analogFormula;
					// log.info("HandlerECONTAB "+imei+" IN FORMULA>>>>>> :: "+msgary[16]+" :: "+fuel);
					// log.info("HandlerECONTAB "+imei+" IN FORMULA>>>>>> :: "+battry);
					//DeviceProfile profile=device.getDp();
				Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
			//	log.info("WP30CRS485 :: Track LOD "+track.getDeviceId());
				if(track!=null)
				{   
				//	log.info("WP30CRS485 "+imei+" GPRMC  :: Track Not NULL");
				//	log.info("WP30CRS485 "+imei+" GPRMC gjo :: "+gjo.toString());
					JSONObject jo = new JSONObject();
				JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
				JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
				JSONArray AnalogDataArray = new JSONArray(analogArray.toString());
				boolean isInsert=false;
				for (int i = 0; i < analogArray.length(); i++) {
				    JSONObject jsonObj = analogArray.getJSONObject(i);
				   
				    String k = jsonObj.keys().next();
				 //   System.out.println(k);
				    if(gpdAnalogNameList.contains(k))
			        {
				    	// analogArray.remove(i);
				    	System.out.println("Before :: "+jsonObj.toString());
				    	isInsert=true;
			        	JSONObject analogObj = new JSONObject();
			        	if(k.equalsIgnoreCase("6387981"))
			        	{
			        	//	jsonObj.remove("6387981");
			        		jsonObj.put(k,String.valueOf(battry));
			        	//AnalogDataArray.getJSONObject(i).remove("6387981");
			        	}
			        	else if(k.equalsIgnoreCase("6387982")) {
			        		//jsonObj.remove("6387982");  
			        		jsonObj.put(k,fuel);
			        	//	AnalogDataArray.getJSONObject(i).remove("6387982");
			        	}
			        	System.out.println("After :: "+jsonObj.toString());
			        	//AnalogDataArray.put(analogObj);
			        }
				}
				if(!isInsert)
				{
					System.out.println("Not insert");
					JSONObject analogbattryObj = new JSONObject();
					analogbattryObj.put("6387981",String.valueOf(battry));
					JSONObject analogfuelObj = new JSONObject();
					analogfuelObj.put("6387982",fuel);
					AnalogDataArray.put(analogbattryObj);
					AnalogDataArray.put(analogfuelObj);
				}
				
			//	log.info("WP30CRS485 "+imei+" GPS :: "+analogArray.toString());
				jo.put("Digital", digitaljsonarr);
				jo.put("Analog", analogArray);
				//jo.put("Analog", AnalogDataArray);
				jo.put("DeviceName", device.getDevicename());
				
				History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
				  
				History hst=histroyrepository.saveAndFlush(hist);     
				Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
				MyAlerts alert=new MyAlerts();
				alert.sendMsg(device,lTrack,track);
				MyAnalogAlert analogAlert=new MyAnalogAlert();
				analogAlert.sendAnalogAlert(device, lTrack);
				}else
				{
					log.info("WP30CRS485 "+imei+" :: NULL");
					JSONArray analogArray =new JSONArray();
					 for (int i = 0; i <analog.length(); i++) {
						 JSONObject analogJsonObject = new JSONObject();
				    	  JSONObject obj = (JSONObject) analog.get(i);
				    	  log.info("HandlerECONTAB "+imei+" GPRMC  :: parameterId :: "+obj.get("Analoginput").toString());
				    	  Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
				    	  analogJsonObject.put(param.getId().toString(), "0.0");
				    	  analogArray.put(analogJsonObject);
				      }
					 
					JSONObject jo = new JSONObject();
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogArray);
					jo.put("DeviceName", device.getDevicename());
					
					History hist = new History(device.getDeviceid(), device.getUserId(), sdf.parse(datestr), new Date(),
							new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
							new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

					Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), sdf.parse(datestr), new Date(),
							new ObjectMapper().readValue(jo.toString(), Map.class),
							new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
					
					History hst=histroyrepository.saveAndFlush(hist);
				lasttrackrepository.saveAndFlush(lTrack);
				}
			}
			
	}else
	{
		
		 log.info("HandlerECONTAB "+device.getImei()+" :: "+msg);
			String Digital_Alarm_1=msgary[4].split(":")[1];
			String Digital_Alarm_2=msgary[5].split(":")[1];
			String Solid_State_O_P_3=msgary[6].split(":")[1];
			
			String Alarm_1_binary = parseHexBinary(Digital_Alarm_1);
			String Alarm_2_binary =parseHexBinary(Digital_Alarm_2);
			String Solid_State_binary =parseHexBinary(Solid_State_O_P_3);
			
		/*	log.info("HandlerECONTAB "+device.getImei()+" :: "+Alarm_1_binary);
			log.info("HandlerECONTAB "+device.getImei()+" :: "+Alarm_2_binary);
			log.info("HandlerECONTAB "+device.getImei()+" :: "+Solid_State_binary);*/
			
		/*	System.out.println("Binary Value is : " + Alarm_1_binary);
			System.out.println("Binary Value is : " + Alarm_2_binary);
			System.out.println("Binary Value is : " + Solid_State_binary);*/
			List<String> Alarm1List = new ArrayList<String>();
			List<String> Alarm2List=  new ArrayList<String>();
			List<String> Solid_StateList=  new ArrayList<String>();
			for(int s=0;s<Alarm_1_binary.length();s++)
			{
				Alarm1List.add(String.valueOf(Alarm_1_binary.charAt(s)));
				Alarm2List.add(String.valueOf(Alarm_2_binary.charAt(s)));
				Solid_StateList.add(String.valueOf(Solid_State_binary.charAt(s)));
			}
			
			String CommaSeparatedMsg =Alarm1List.stream()
	                .collect(Collectors.joining(","));
			CommaSeparatedMsg+=",";
			//System.out.println("CommaSeparatedMsg : " + CommaSeparatedMsg);
			log.info("HandlerECONTAB "+device.getImei()+" :: "+CommaSeparatedMsg);
			 CommaSeparatedMsg +=Alarm2List.stream()
	                .collect(Collectors.joining(","));
			 CommaSeparatedMsg+=",";
			 System.out.println("CommaSeparatedMsg : " + CommaSeparatedMsg);
			 
			 CommaSeparatedMsg +=Solid_StateList.stream()
	                    .collect(Collectors.joining(","));
			 CommaSeparatedMsg+=",";
			 System.out.println("CommaSeparatedMsg : " + CommaSeparatedMsg);
			 
			
			 String Mains_Phase_1=msgary[7].split(":")[1];
			 CommaSeparatedMsg+=Mains_Phase_1+",";
			 
			 String Mains_Phase_2=msgary[8].split(":")[1];
			 CommaSeparatedMsg+=Mains_Phase_2+",";
			 
			 String Mains_Phase_3=msgary[9].split(":")[1];
			 CommaSeparatedMsg+=Mains_Phase_3+",";
			 
			 String DG_Phase_1=msgary[10].split(":")[1];
			 CommaSeparatedMsg+=DG_Phase_1+",";
			 
			 String DG_Phase_2=msgary[11].split(":")[1];
			 CommaSeparatedMsg+=DG_Phase_2+",";
			 
			 String DG_Phase_3=msgary[12].split(":")[1];
			 CommaSeparatedMsg+=DG_Phase_3+",";
			 
			 String Load_Current_Phase_1=msgary[13].split(":")[1];
			 CommaSeparatedMsg+=Load_Current_Phase_1+",";
			 
			 String Load_Current_Phase_2=msgary[14].split(":")[1];
			 CommaSeparatedMsg+=Load_Current_Phase_2+",";
			 
			 String Load_Current_Phase_3=msgary[15].split(":")[1];
			 CommaSeparatedMsg+=Load_Current_Phase_3+",";
			 
			 String Mains_KWH =msgary[16].split(":")[1]+msgary[17].split(":")[1];
			 CommaSeparatedMsg+=Mains_KWH+",";
			 
			 String DG_KWH =msgary[18].split(":")[1]+msgary[19].split(":")[1];
			 CommaSeparatedMsg+=DG_KWH+",";
			 
			 String DG_Run_Hour =msgary[20].split(":")[1]+msgary[21].split(":")[1];
			 CommaSeparatedMsg+=DG_Run_Hour+",";
			 
			 String Mains_Run_Hour =msgary[22].split(":")[1]+msgary[23].split(":")[1];
			 CommaSeparatedMsg+=Mains_Run_Hour+",";
			 
			 String DG_Battery  =msgary[24].split(":")[1];
			 CommaSeparatedMsg+=DG_Battery+",";
			 
			 String BTS_Battery  =msgary[25].split(":")[1];
			 CommaSeparatedMsg+=BTS_Battery+",";
			 
			 String Fuel_in_Liter  =msgary[26].split(":")[1];
			 CommaSeparatedMsg+=Fuel_in_Liter+",";
			 
			 String Mains_KW_R  =msgary[27].split(":")[1];
			 CommaSeparatedMsg+=Mains_KW_R+",";
			 
			 String Mains_KW_Y  =msgary[28].split(":")[1];
			 CommaSeparatedMsg+=Mains_KW_Y+",";
			 
			 String Mains_KW_B  =msgary[29].split(":")[1];
			 CommaSeparatedMsg+=Mains_KW_B+",";
			 
			 String DG_KW_R  =msgary[30].split(":")[1];
			 CommaSeparatedMsg+=DG_KW_R+",";
			 
			 String DG_KW_Y  =msgary[31].split(":")[1];
			 CommaSeparatedMsg+=DG_KW_Y+",";
			 
			 String DG_KW_B  =msgary[32].split(":")[1];
			 CommaSeparatedMsg+=DG_KW_B+",";
			 
			 String BTS_Run_Hour  =msgary[33].split(":")[1]+msgary[34].split(":")[1];
			 CommaSeparatedMsg+=BTS_Run_Hour+",";
			 
			 String Tamper_Run_Hour  =msgary[35].split(":")[1]+msgary[36].split(":")[1];
			 CommaSeparatedMsg+=Tamper_Run_Hour+",";
			 
			 String Tamper_DG_KWH   =msgary[37].split(":")[1]+msgary[38].split(":")[1];
			 CommaSeparatedMsg+=Tamper_DG_KWH;
			 
			 System.out.println("msgary[0].substring(5) :: "+msgary[0].substring(5));
		
			 
			  
				Calendar cal = Calendar.getInstance();
				
				if( msgary[2].toString().length()==6)
				{
					cal.setTime(sdf.parse(msgary[1] + msgary[2]));
					log.info("HandlerECONTAB "+device.getImei()+" :: "+" DATE "+msgary[1] + msgary[2]);
				}
					else if( msgary[2].toString().length()==5) {
					cal.setTime(sdf.parse(msgary[1] + msgary[2]+"0"));
					log.info("HandlerECONTAB "+device.getImei()+" :: "+" DATE "+msgary[1] + msgary[2]+"0");
					}
					else
						log.info("HandlerECONTAB "+device.getImei()+" :: "+" Invalid Date");   
				
				
				
				cal.add(Calendar.HOUR, 5);
				cal.add(Calendar.MINUTE, 30);
				Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
				log.info("HandlerECONTAB "+device.getImei()+" :: "+" DATE "+insertingdate);
				
				if (device != null) {  
					
					JSONArray analogjsonarr = new JSONArray();
					JSONObject jo = new JSONObject();
					JSONObject gpsjo = new JSONObject();
					DeviceProfile dp = device.getDp();
					JSONArray digitaljsonarr = new JSONArray();
					JSONObject parameters = new JSONObject(dp.getParameters());
					
			
					JSONArray rs232 = parameters.getJSONArray("Analog");
					
					//JSONArray digital = parameters.getJSONArray("Digital");
					
					 String[] hexDataArray = CommaSeparatedMsg.split(",");
					try {
						for (int i = 0; i < rs232.length(); i++) {
							JSONObject analogobj = new JSONObject();
							JSONObject obj = (JSONObject) rs232.get(i);
							Double d = Double.parseDouble(obj.get("analogioindex").toString());
							boolean isFound = hexDataArray[d.intValue()].indexOf("X") !=-1? true: false;
							if (isFound==false) {
								analogobj.put(obj.get("Analoginput").toString(),
										String.valueOf(Double.valueOf(twoDForm.format((Long.parseLong(hexDataArray[d.intValue()], 16) ) /  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue()));
							} else {
								analogobj.put(obj.get("Analoginput").toString(), "00.00");
							}
							analogjsonarr.put(analogobj);
						} 
						
					} catch (Exception e2) {
						e2.printStackTrace();
						log.info("HandlerECONTAB "+device.getImei()+" :: "+" "+e2.getMessage());
						log.info("HandlerECONTAB "+device.getImei()+" :: "+" "+e2.getStackTrace());
						log.info("HandlerECONTAB "+device.getImei()+" :: "+" (Index Out of Bound) ==> Registers are not match with profile");
					}
					log.info("HandlerECONTAB "+device.getImei()+" :: "+analogjsonarr.toString());
					
					
					Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
					JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
					JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
					
					jo.put("Digital", DigitalJsonArray_old);
					jo.put("Analog", analogjsonarr);   
					jo.put("METER_ID", msgary[3]);
					
					Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
							insertingdate, new Date(),
							new ObjectMapper().readValue(jo.toString(), Map.class),
							new ObjectMapper().readValue(gpsjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
					
					
					     
					if (track == null)
					{
						lasttrackrepository.saveAndFlush(lTrack);
					}
					else {
						track.setDeviceDate(insertingdate);
						track.setSystemDate(new Date());
						track.setAnalogdigidata(new ObjectMapper().readValue(jo.toString(), Map.class));
						track.setDevicedata(new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
						lasttrackrepository.saveAndFlush(track);
					}
					History hist= new History(device.getDeviceid(), device.getUserId(), insertingdate,
							new Date(), new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
							(Map<String, Object>) new ObjectMapper().readValue(gpsjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
					histroyrepository.saveAndFlush(hist);
				}
			   
				log.info("HandlerECONTAB "+device.getImei()+" :: "+CommaSeparatedMsg);
			 
			 
	}
			
		}
		 String parseHexBinary(String hex) {
				String digits = "0123456789ABCDEF";
		 		hex = hex.toUpperCase();
				String binaryString = "";
				
				for(int i = 0; i < hex.length(); i++) {
					char c = hex.charAt(i);
					int d = digits.indexOf(c);
					if(d == 0)	binaryString += "0000"; 
					else  binaryString += Integer.toBinaryString(d);
				}
				return binaryString;
			}
	}
	
	static JSONObject convertJson(String json) throws org.codehaus.jackson.JsonParseException, JsonMappingException, IOException
	{

		 JSONObject globalJsonObject=new JSONObject();
			    ObjectMapper mapper = new ObjectMapper();
				Map<String, String> map = mapper.readValue(json, Map.class);
	            System.out.println(map);
	            JSONObject jsonObject = new JSONObject(json);
	            JSONArray analogArray=(JSONArray) jsonObject.get("Analog");
	            JSONArray digitalArray=(JSONArray) jsonObject.get("Digital");
	           JSONObject analogJsonObject=new JSONObject();
	           for (int i = 0; i < analogArray.length(); i++) {
	        	    JSONObject analogJson = analogArray.getJSONObject(i);
	        	    Iterator<String> keys = analogJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        analogJsonObject.put(key, analogJson.get(key));
	        	    }
	        	}
	           globalJsonObject.put("Analog", analogJsonObject);
	           JSONObject digitalJsonObject=new JSONObject();
	           for (int i = 0; i < digitalArray.length(); i++) {
	        	    JSONObject digitalJson = digitalArray.getJSONObject(i);
	        	    Iterator<String> keys = digitalJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        digitalJsonObject.put(key, digitalJson.get(key));
	        	    }
	        	}
	           globalJsonObject.put("Digital", digitalJsonObject);
	           return globalJsonObject;
	}
	
}

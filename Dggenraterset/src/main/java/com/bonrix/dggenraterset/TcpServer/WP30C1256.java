package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class WP30C1256 {


	  
	private static final Logger log = Logger.getLogger(WP30C1256.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	public static class HandlerWP30C1256 extends SimpleChannelUpstreamHandler  {

		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
		
		ParameterRepository parameterrepository = ApplicationContextHolder.getContext().getBean(ParameterRepository.class);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		@SuppressWarnings("unused")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg= (String)e.getMessage();
			  ObjectMapper mapper = new ObjectMapper();
		 	log.info("WP30CRS1256:: Msg :Len: "+msg);
			String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String imei=msgary[0].substring(5);
			String datestr=msgary[10]+msgary[2];
			
			if (msgary[1].contains("$GPRMC")) {
				log.info("WP30CRS1256 "+imei+" :: "+msg);
				String[] gpdAnalog= {"6387981","6387982"};
				 List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
				String digitalData=msgary[14];
				boolean Isvalid="A".equals(msgary[3]);
				
				if(Isvalid==true || Isvalid==false) {
					JSONArray analogjsonarr = new JSONArray();
					JSONArray digitaljsonarr = new JSONArray();
					
				Devicemaster device = devicemasterRepository.findByImei(imei);
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
			    	  analogFormula=Double.parseDouble(obj.get("Analogformula").toString());
			      }
			      }
			      
			      String ACMAINS_FAIL=digitalData.substring(1,2);
			      String Fire=digitalData.substring(2,3);
			      String Door=digitalData.substring(3,4);
			      String DG_Running_Hrs=digitalData.substring(4,5);
			      String DG_Fault=digitalData.substring(5,6);
			      String Battry_Low=digitalData.substring(6,7);
			      String PP_Input_Fail=digitalData.substring(7,8);
			      
			  /*    log.info("WP30CRS1256 "+imei+" GPS Main INPUTS :: "+digitalData);
			      log.info("WP30CRS1256 "+imei+" GPS GREEN ACMAINS_FAIL :: "+digitalData.substring(1,2));
			      log.info("WP30CRS1256 "+imei+" GPS Yellow Ornge Fire ::"+digitalData.substring(2,3));
			      log.info("WP30CRS1256 "+imei+" GPS Yellow Door Door :: "+digitalData.substring(3,4));
			      log.info("WP30CRS1256 "+imei+" GPS Yellow Black DG_Running_Hrs :: "+digitalData.substring(4,5));
			      log.info("WP30CRS1256 "+imei+" GPS Yellow Brown DG_Fault :: "+digitalData.substring(5,6));
			      log.info("WP30CRS1256 "+imei+" GPS Green Black Battry_Low :: "+digitalData.substring(6,7));
			      log.info("WP30CRS1256 "+imei+" GPS Yellow Red :: PP_Input_Fail "+digitalData.substring(7,8));*/
			      
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
					
					String fuel=msgary[16]; 
					
					//fuelFormula
					double battry=Double.parseDouble(msgary[15].toString()) * analogFormula;
					// log.info("WP30CRS1256 "+imei+" IN FORMULA>>>>>> :: "+msgary[16]+" :: "+fuel);
					// log.info("WP30CRS1256 "+imei+" IN FORMULA>>>>>> :: "+battry);
					//DeviceProfile profile=device.getDp();
				Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
				if(track!=null)
				{   
					JSONObject jo = new JSONObject();
				JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
				JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
				JSONArray AnalogDataArray = new JSONArray(analogArray.toString());
				boolean isInsert=false;
				for (int i = 0; i < analogArray.length(); i++) {
				    JSONObject jsonObj = analogArray.getJSONObject(i);
				   
				    String k = jsonObj.keys().next();
				    if(gpdAnalogNameList.contains(k))
			        {
				    	isInsert=true;
			        	JSONObject analogObj = new JSONObject();
			        	if(k.equalsIgnoreCase("6387981"))
			        	{
			        		jsonObj.put(k,String.valueOf(battry));
			        	}
			        	else if(k.equalsIgnoreCase("6387982")) {
			        		jsonObj.put(k,fuel);
			        	}
			        }
				}
				if(!isInsert)
				{
				//	System.out.println("Not insert");
					JSONObject analogbattryObj = new JSONObject();
					analogbattryObj.put("6387981",String.valueOf(battry));
					JSONObject analogfuelObj = new JSONObject();
					analogfuelObj.put("6387982",fuel);
					AnalogDataArray.put(analogbattryObj);
					AnalogDataArray.put(analogfuelObj);
				}
				
				jo.put("Digital", digitaljsonarr);
				jo.put("Analog", analogArray);
				jo.put("DeviceName", device.getDevicename());
				
				/*History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
				  
				History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
				
				History hst=histroyrepository.saveAndFlush(hist);     
				Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
				MyAlerts alert=new MyAlerts();
				alert.sendMsg(device,lTrack,track);
				MyAnalogAlert analogAlert=new MyAnalogAlert();
				analogAlert.sendAnalogAlert(device, lTrack);
				}else
				{
					log.info("WP30CRS1256 "+imei+" :: NULL");
					JSONArray analogArray =new JSONArray();
					 for (int i = 0; i <analog.length(); i++) {
						 JSONObject analogJsonObject = new JSONObject();
				    	  JSONObject obj = (JSONObject) analog.get(i);
				    	  log.info("WP30CRS1256 "+imei+" GPRMC  :: parameterId :: "+obj.get("Analoginput").toString());
				    	  Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
				    	  analogJsonObject.put(param.getId().toString(), "0.0");
				    	  analogArray.put(analogJsonObject);
				      }
					 
					JSONObject jo = new JSONObject();
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogArray);
					jo.put("DeviceName", device.getDevicename());
					
					
					/*History hist = new History(device.getDeviceid(), device.getUserId(),  new Date(), new Date(),
							new ObjectMapper().readValue(jo.toString(), Map.class),
							new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

					Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),  new Date(), new Date(),
							new ObjectMapper().readValue(jo.toString(), Map.class),
							new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
					
					History hist = new History(device.getDeviceid(), device.getUserId(),  new Date(), new Date(),
							new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
							new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
					
					History hst=histroyrepository.saveAndFlush(hist);
				lasttrackrepository.saveAndFlush(lTrack);
				}
			}
			}else {
				log.info("WP30CRS1256 "+imei+" Analog :: Analog String");
			   
			    
			    Devicemaster device = devicemasterRepository.findByImei(imei);
		  		JSONArray AnalogJsonArray = new JSONArray();
			 String[] analog1= new String[]{"6387981","6387982","5557109","6308790","5557111","6308792","5557117","6308794","5557118","6308796","5557124"};
		      String[] analog1Param= {"kWh_of_CH1","A_of_CH1","kWh_of_CH2","A_of_CH2","kWh_of_CH3","A_of_CH3","kWh_of_CH4","A_of_CH4","Voltage"};

			 String[] analog5= new String[]{"6387981","6387982","9160775","9160842","9160931", "9160988","9161078","9161132", "9161269", "9161268","9159066","9159257","6337570","6337574","6337577","6337582","9159712","6337588","237921","9160140","9160254","9160343","9160363","9160381","9160395","9160414","9160441","9160505","9160520","9160551"};
			 List<String> analog5NameList = new ArrayList<>(Arrays.asList(analog5));
		     List<String> analog1NameList = new ArrayList<>(Arrays.asList(analog1));
			 
			 Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
			 if(track!=null)
				{ 
			    if (msgary[3].contains("1")) {   
			    	   
			    	JSONObject jo = new JSONObject();
					JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
					JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
					//log.info("WP30CRS1256 Digital :: "+DigitalJsonArray_old.toString());
					
					JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());
					log.info("WP30CRS1256 OLD :: "+OldAnalogJsonArray.toString());
					for(int a=0;a<OldAnalogJsonArray.length();a++)
					{
						JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
						String k = jsonObj.keys().next();
				        if(analog5NameList.contains(k))
				        {
				        	JSONObject analogObj = new JSONObject();
				        	analogObj.put(k,jsonObj.getString(k));
				        	AnalogJsonArray.put(analogObj);
				        }
					}
					
				      String[] msgStringArray=new String[9];
				     
				      String kWh_of_CH1=msgary[4].split(":")[1]+msgary[5].split(":")[1];
				      msgStringArray[0]=""+ConvertMODBUSRTUValue(kWh_of_CH1);
				     
				      String A_of_CH1=msgary[6].split(":")[1]+msgary[7].split(":")[1];
				      msgStringArray[1]=""+ConvertMODBUSRTUValue(A_of_CH1);
				      
				      String kWh_of_CH2=msgary[8].split(":")[1]+msgary[9].split(":")[1];
				      msgStringArray[2]=""+ConvertMODBUSRTUValue(kWh_of_CH2);
				    
				      String A_of_CH2=msgary[10].split(":")[1]+msgary[11].split(":")[1];
				      msgStringArray[3]=""+ConvertMODBUSRTUValue(A_of_CH2);
				      
				      String kWh_of_CH3=msgary[12].split(":")[1]+msgary[13].split(":")[1];
				      msgStringArray[4]=""+ConvertMODBUSRTUValue(kWh_of_CH3);
				     
				      String A_of_CH3=msgary[14].split(":")[1]+msgary[15].split(":")[1];
				      msgStringArray[5]=""+ConvertMODBUSRTUValue(A_of_CH3);
				      
				      String kWh_of_CH4=msgary[16].split(":")[1]+msgary[17].split(":")[1];
				      msgStringArray[6]=""+ConvertMODBUSRTUValue(kWh_of_CH4);
				     
				      String A_of_CH4=msgary[18].split(":")[1]+msgary[19].split(":")[1];
				      msgStringArray[7]=""+ConvertMODBUSRTUValue(A_of_CH4);
				      
				      String Voltage=msgary[20].split(":")[1]+msgary[21].split(":")[1];
				      msgStringArray[8]=""+ConvertMODBUSRTUValue(Voltage);
				  	
				  	for(int i=0;i<analog1Param.length;i++)
				  	{
				  		JSONObject analogObj = new JSONObject();
				  		if(analog1Param[i].equalsIgnoreCase("kWh_of_CH1"))
				  			 analogObj.put("5557109",ConvertMODBUSRTUValue(kWh_of_CH1));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("A_of_CH1"))
				  			 analogObj.put("6308790",ConvertMODBUSRTUValue(A_of_CH1));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("kWh_of_CH2"))
				  			 analogObj.put("5557111",ConvertMODBUSRTUValue(kWh_of_CH2));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("A_of_CH2"))
				  			 analogObj.put("6308792",ConvertMODBUSRTUValue(A_of_CH2));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("kWh_of_CH3"))
				  			 analogObj.put("5557117",ConvertMODBUSRTUValue(kWh_of_CH3));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("A_of_CH3"))
				  			 analogObj.put("6308794",ConvertMODBUSRTUValue(A_of_CH3));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("kWh_of_CH4"))
				  			 analogObj.put("5557118",ConvertMODBUSRTUValue(kWh_of_CH4));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("A_of_CH4"))
				  			analogObj.put("6308796",ConvertMODBUSRTUValue(A_of_CH4));
				  		
				  		else if(analog1Param[i].equalsIgnoreCase("Voltage"))
				  			analogObj.put("5557124",ConvertMODBUSRTUValue(Voltage));
				  		AnalogJsonArray.put(analogObj);
				  	}
						
							jo.put("Digital", DigitalJsonArray_old);
							jo.put("Analog", AnalogJsonArray);
							jo.put("DeviceName", device.getDevicename());
							log.info("WP30CRS1256   :: "+AnalogJsonArray.toString());
							
							
						/*	History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

							Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hst=histroyrepository.saveAndFlush(hist);
							Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
							//log.info("WP30CRS1256   :: "+t.getDeviceId());
			    
			    	
			    }
			    else if (msgary[3].contains("5")) {
			    	 String dataArray[]=Arrays.copyOfRange(msgary, 4,msgary.length);
				 	    System.out.println(Arrays.toString(dataArray));
				 	    
				 	    String digitalData=dataArray[20].split(":")[1];
				 	   JSONObject jo = new JSONObject();
				 /*   log.info("WP30CRS1256 digitalData "+digitalData);
				    log.info("WP30CRS1256 hexToBin "+hexToBin(digitalData));
				    log.info("WP30CRS1256 reverse "+reverse(hexToBin(digitalData)));*/
				    DeviceProfile profile=device.getDp();
				    List<String> AlarmList = new ArrayList<String>();
				    if (device != null) {  
				    String digitals=reverse(hexToBin(digitalData));
				    
				    String DCEM_COMMUNACTION_FAIL=String.valueOf(digitals.charAt(0));
				    String SMPS_COMMUNICATION_FAIL=""+digitals.charAt(1);
				    String Second_LVD_ALARM=""+digitals.charAt(2);
				    String First_LVD_Alarm=""+digitals.charAt(3);
				    String Ac_Fail_alarm=""+digitals.charAt(8);
				    String DC_LOW=""+digitals.charAt(10);
				    String Rectifier_Fail=""+digitals.charAt(11);
				    String AC_LOW_ALARM=""+digitals.charAt(12);

				/*    log.info("WP30CRS1256 DCEM_COMMUNACTION_FAIL :: "+DCEM_COMMUNACTION_FAIL);
				    log.info("WP30CRS1256 SMPS_COMMUNICATION_FAIL :: "+SMPS_COMMUNICATION_FAIL);
				    log.info("WP30CRS1256 Second_LVD_ALARM :: "+Second_LVD_ALARM);
				    log.info("WP30CRS1256 First_LVD_Alarm :: "+First_LVD_Alarm);
				    log.info("WP30CRS1256 Ac_Fail_alarm :: "+Ac_Fail_alarm);
				    log.info("WP30CRS1256 DC_LOW :: "+DC_LOW);
				    log.info("WP30CRS1256 Rectifier_Fail :: "+Rectifier_Fail);
				    log.info("WP30CRS1256 AC_LOW_ALARM :: "+AC_LOW_ALARM);*/
					
					String DC_Voltage=dataArray[0].split(":")[1];
					 
					 String Batt_Current=dataArray[1].split(":")[1];
					 
					 String Load_Current=dataArray[2].split(":")[1];
					 
					 String R_Voltage=dataArray[3].split(":")[1];
					 
					 String Y_Voltage=dataArray[4].split(":")[1];
					 
					 String B_Voltage=dataArray[5].split(":")[1];
					 
					 String SOC_Percentage=dataArray[6].split(":")[1];
					 
					 String Battery_Capacity=dataArray[7].split(":")[1];
					 
					 String Room_Temperature=dataArray[8].split(":")[1];
					 
					 String Battery_Run_Hour=dataArray[9].split(":")[1];
					 
					 String Main_Run_Hour=dataArray[10].split(":")[1];
					 
					 String Module_Current_1=dataArray[11].split(":")[1];
					 
					 String Module_Current_2=dataArray[12].split(":")[1];
					 
					 String Module_Current_3=dataArray[13].split(":")[1];
					 
					 String Module_Current_4=dataArray[14].split(":")[1];
					 
					 String Module_Current_5=dataArray[15].split(":")[1];
					 
					 String Module_Current_6=dataArray[16].split(":")[1];
					 
					 String Module_Current_7=dataArray[17].split(":")[1];
					 
					 String Module_Current_8=dataArray[18].split(":")[1];
					 
					 String Module_Current_9=dataArray[19].split(":")[1];
					 
					/* log.info("WP30CRS1256 "+imei+" DCEM_COMMUNACTION_FAIL :: "+DCEM_COMMUNACTION_FAIL);
					 log.info("WP30CRS1256 "+imei+" Module_Current_1 :: "+Module_Current_1);
					 log.info("WP30CRS1256 "+imei+" Module_Current_2 :: "+Module_Current_2);
					 log.info("WP30CRS1256 "+imei+" Module_Current_3 :: "+Module_Current_3);
					 log.info("WP30CRS1256 "+imei+" Module_Current_4 :: "+Module_Current_4);
					 log.info("WP30CRS1256 "+imei+" Module_Current_5 :: "+Module_Current_5);*/
						
					 JSONArray analogjsonarr = new JSONArray();
					   JSONObject parameters = new JSONObject(profile.getParameters());
						JSONArray analog = parameters.getJSONArray("Analog");
						
						
						  
						try {
							 String[] analog5Param= {"DCEM_COMMUNACTION_FAIL","SMPS_COMMUNICATION_FAIL","Second_LVD_ALARM","First_LVD_Alarm","Ac_Fail_alarm","DC_LOW","Rectifier_Fail","AC_LOW_ALARM","DC_Voltage","Batt_Current","Load_Current","R_Voltage","Y_Voltage","B_Voltage","SOC_Percentage","Battery_Capacity","Room_Temperature","Battery_Run_Hour","Main_Run_Hour","Module_Current_1","Module_Current_2","Module_Current_3","Module_Current_4","Module_Current_5","Module_Current_6","Module_Current_7","Module_Current_8","Module_Current_9"};

							for(int i=0;i<analog5Param.length;i++)
						  	{
						  		JSONObject analogObj = new JSONObject();
						  		/* if(analog5Param[i].equalsIgnoreCase("DCEM_COMMUNACTION_FAIL"))
						 			 analogObj.put("9160775",DCEM_COMMUNACTION_FAIL);*/
						  		if(analog5Param[i].equalsIgnoreCase("DCEM_COMMUNACTION_FAIL"))
						 			 analogObj.put("9160775","0");  
							    
							     if(analog5Param[i].equalsIgnoreCase("SMPS_COMMUNICATION_FAIL"))
						 			 analogObj.put("9160842",SMPS_COMMUNICATION_FAIL);
							    
							    if(analog5Param[i].equalsIgnoreCase("Second_LVD_ALARM"))
						 			 analogObj.put("9160931",Second_LVD_ALARM);
							    
							    if(analog5Param[i].equalsIgnoreCase("First_LVD_Alarm"))
						 			 analogObj.put("9160988",First_LVD_Alarm);
							    
							    if(analog5Param[i].equalsIgnoreCase("Ac_Fail_alarm"))
						 			 analogObj.put("9161078",Ac_Fail_alarm);
							    
							    if(analog5Param[i].equalsIgnoreCase("DC_LOW"))
						 			 analogObj.put("9161132",DC_LOW);
							    
							    if(analog5Param[i].equalsIgnoreCase("Rectifier_Fail"))
						 			 analogObj.put("9161269",Rectifier_Fail);
							    
							    if(analog5Param[i].equalsIgnoreCase("AC_LOW_ALARM"))
						 			 analogObj.put("9161268",AC_LOW_ALARM);
							    
							    if(analog5Param[i].equalsIgnoreCase("DC_Voltage"))
						 			 analogObj.put("9159066",""+(Integer.parseInt(DC_Voltage,16)*0.1)/10);
							    
							    if(analog5Param[i].equalsIgnoreCase("Batt_Current"))
						 			 analogObj.put("9159257",""+(Integer.parseInt(Batt_Current,16)*0.1)/10);
							    
							    if(analog5Param[i].equalsIgnoreCase("Load_Current"))
						 			 analogObj.put("6337570",""+(Integer.parseInt(Load_Current,16)*0.1)/10);
							    
							    if(analog5Param[i].equalsIgnoreCase("R_Voltage"))
						 			 analogObj.put("6337574",""+((Integer.parseInt(R_Voltage,16)*0.1)/10));
							    
							    if(analog5Param[i].equalsIgnoreCase("Y_Voltage"))
						 			 analogObj.put("6337577",""+(Integer.parseInt(Y_Voltage,16)*0.1/10));
							    
							    if(analog5Param[i].equalsIgnoreCase("B_Voltage"))
						 			 analogObj.put("6337582",""+(Integer.parseInt(B_Voltage,16)*0.1/10));
							      
							    if(analog5Param[i].equalsIgnoreCase("SOC_Percentage"))  
						 			 analogObj.put("9159712",""+Integer.parseInt(SOC_Percentage,16));
							    
							    if(analog5Param[i].equalsIgnoreCase("Battery_Capacity"))
						 			 analogObj.put("6337588",""+Integer.parseInt(Battery_Capacity,16));
							    
							    if(analog5Param[i].equalsIgnoreCase("Room_Temperature"))
						 			 analogObj.put("237921",""+Integer.parseInt(Room_Temperature,16));
							    
							    if(analog5Param[i].equalsIgnoreCase("Battery_Run_Hour"))
						 			 analogObj.put("9160140",""+Integer.parseInt(Battery_Run_Hour,16));
							      
							    if(analog5Param[i].equalsIgnoreCase("Main_Run_Hour"))
						 			 analogObj.put("9160254",""+Integer.parseInt(Main_Run_Hour,16));
							      
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_1"))//sajan
						 			 analogObj.put("9160343",""+twoDForm.format(Integer.parseInt(Module_Current_1,16)*0.01));
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_2"))
						 			 analogObj.put("9160363",""+(Integer.parseInt(Module_Current_2,16)*0.01));
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_3"))
						 			 analogObj.put("9160381",""+Integer.parseInt(Module_Current_3,16)*0.01);
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_4"))
						 			 analogObj.put("9160395",""+(Integer.parseInt(Module_Current_4,16)*0.01));
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_5"))
						 			 analogObj.put("9160414",""+Integer.parseInt(Module_Current_5,16)*0.01);
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_6"))
						 			 analogObj.put("9160441",""+Integer.parseInt(Module_Current_6,16));
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_7"))
						 			 analogObj.put("9160505",""+Integer.parseInt(Module_Current_7,16));
							    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_8"))
						 			 analogObj.put("9160520",""+Integer.parseInt(Module_Current_8,16));
							    
							  				    
							    if(analog5Param[i].equalsIgnoreCase("Module_Current_9"))
						 			 analogObj.put("9160551",""+Integer.parseInt(Module_Current_9,16));
							    
							    AnalogJsonArray.put(analogObj);
							    
						  	}
							
							JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
							JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());
							JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
							//System.out.println("OldAnalogJsonArray :: "+OldAnalogJsonArray.toString());
							for(int a=0;a<OldAnalogJsonArray.length();a++)
							{
								JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
								String k = jsonObj.keys().next();
						    //   log.info("Info Key: " + k + ", value: " + jsonObj.getString(k));
						        if(analog1NameList.contains(k))   
						        {
						        	JSONObject analogObj = new JSONObject();
						        	analogObj.put(k,jsonObj.getString(k));
						        	AnalogJsonArray.put(analogObj);
						        }
							}
							
						//	System.out.println(AnalogJsonArray.toString());
							jo.put("Digital", DigitalJsonArray_old);
							jo.put("Analog", AnalogJsonArray);
							jo.put("DeviceName", device.getDevicename());

							/*History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

							Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hst=histroyrepository.saveAndFlush(hist);
							Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
							
						} catch (Exception e2) {
							e2.printStackTrace();
							log.info("WP30CRS1256 "+device.getImei()+" :: "+" "+e2.getMessage());
							log.info("WP30CRS1256 "+device.getImei()+" :: "+" "+e2.getStackTrace());
							log.info("WP30CRS1256 "+device.getImei()+" :: "+" (Index Out of Bound) ==> Registers are not match with profile");
						}
						//log.info("HandlerECONTAB "+device.getImei()+" :: "+analogjsonarr.toString());
				    }
				}
			}
			}
		}
	}
	
	static String hexToBin(String hex) {
        String binary = "";
        hex = hex.toUpperCase();
 
        HashMap<Character, String> hashMap
            = new HashMap<Character, String>();
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");  
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");
 
        int i;
        char ch;
 
        for (i = 0; i < hex.length(); i++) {
            ch = hex.charAt(i);
            if (hashMap.containsKey(ch))
                binary += hashMap.get(ch);
            else {
                binary = "Invalid Hexadecimal String";
                return binary;
            }
        }
        return binary;
	}

	public static String reverse(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = str.length() - 1; i >= 0; i--)
			sb.append(str.charAt(i));
		return sb.toString();
	}

	static String ConvertMODBUSRTUValue(String hexVal) {
		Long i1 = Long.parseLong(hexVal, 16);
		Float f1 = Float.intBitsToFloat(i1.intValue());
		return String.valueOf(f1);
	}
	
	static JSONObject convertJson(String json) throws org.codehaus.jackson.JsonParseException, JsonMappingException, IOException
	{

		 JSONObject globalJsonObject=new JSONObject();
	//	String json="{\"Analog\":[{\"6387981\":\"54.601\"},{\"6387982\":\"10.50\"},{\"6337564\":\"0.0\"},{\"6337565\":\"0.0\"},{\"6337570\":\"0.0\"},{\"6337573\":\"0.0\"},{\"6337574\":\"0.0\"},{\"6337577\":\"0.0\"},{\"6337582\":\"0.0\"},{\"6337583\":\"0.0\"},{\"6337585\":\"0.0\"},{\"6337588\":\"0.0\"},{\"237921\":\"0.0\"},{\"6387968\":\"0.0\"},{\"6387970\":\"0.0\"},{\"6387973\":\"0.0\"},{\"237931\":\"0.0\"},{\"237932\":\"0.0\"},{\"237933\":\"0.0\"},{\"6387975\":\"0.0\"},{\"6387976\":\"0.0\"},{\"6387980\":\"0.0\"},{\"5557109\":\"0.0\"},{\"5557111\":\"0.0\"},{\"5557117\":\"0.0\"},{\"5557118\":\"0.0\"},{\"5557124\":\"0.0\"},{\"6308790\":\"0.0\"},{\"6308792\":\"0.0\"},{\"6308794\":\"0.0\"},{\"6308796\":\"0.0\"}],\"Digital\":[{\"284945\":\"1\"},{\"6348798\":\"1\"},{\"291934\":\"1\"},{\"6348854\":\"1\"},{\"6348815\":\"1\"},{\"6348821\":\"1\"},{\"6348824\":\"1\"}],\"DeviceName\":\"AmarSociety-GURTUBHV0094\\t\"}";
			    ObjectMapper mapper = new ObjectMapper();
				Map<String, String> map = mapper.readValue(json, Map.class);
	            System.out.println(map);
	            JSONObject jsonObject = new JSONObject(json);
	            JSONArray analogArray=(JSONArray) jsonObject.get("Analog");
	            JSONArray digitalArray=(JSONArray) jsonObject.get("Digital");
	       //    System.out.println(analogArray);
	       //    System.out.println(digitalArray);
	           JSONObject analogJsonObject=new JSONObject();
	           for (int i = 0; i < analogArray.length(); i++) {
	        	    JSONObject analogJson = analogArray.getJSONObject(i);
	        	    Iterator<String> keys = analogJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        System.out.println("Key :" + key + "  Value :" + analogJson.get(key));
	        	        analogJsonObject.put(key, analogJson.get(key));
	        	    }
	        	}
	           System.out.println(analogJsonObject);
	           globalJsonObject.put("Analog", analogJsonObject);
	           JSONObject digitalJsonObject=new JSONObject();
	           for (int i = 0; i < digitalArray.length(); i++) {
	        	    JSONObject digitalJson = digitalArray.getJSONObject(i);
	        	    Iterator<String> keys = digitalJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        System.out.println("Key :" + key + "  Value :" + digitalJson.get(key));
	        	        digitalJsonObject.put(key, digitalJson.get(key));
	        	    }
	        	}
	           System.out.println(digitalJsonObject);
	           globalJsonObject.put("Digital", digitalJsonObject);
	           System.out.println(globalJsonObject);
	           return globalJsonObject;
	            
	
	}
}

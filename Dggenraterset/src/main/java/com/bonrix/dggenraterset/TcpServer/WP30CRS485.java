package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.bonrix.dggenraterset.DTO.DGHashMap;
import com.bonrix.dggenraterset.DTO.WebSocketObj;
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

public class WP30CRS485 {

	private static final Logger log = Logger.getLogger(WP30CRS485.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	public static class HandlerWP30CRS485 extends SimpleChannelUpstreamHandler  {

		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
		   
		ParameterRepository parameterrepository = ApplicationContextHolder.getContext().getBean(ParameterRepository.class);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		static DGHashMap map = new DGHashMap();
		@SuppressWarnings("unused")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg= (String)e.getMessage();
			  ObjectMapper mapper = new ObjectMapper();
			String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String imei=msgary[0].substring(5);
			String datestr=msgary[10]+msgary[2];
			log.info("WP30CRS485 " + msg);
			
			if (msgary[1].contains("$GPRMC")) {
				log.info("WP30CRS485 "+imei+" :: "+msg);
				String[] gpdAnalog= {"6387981","6387982"};
				 List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
				String digitalData=msgary[14];
				boolean Isvalid="A".equals(msgary[3]);
				
				if(Isvalid==true || Isvalid==false) {
					JSONArray analogjsonarr = new JSONArray();
					JSONArray digitaljsonarr = new JSONArray();
					
				Devicemaster device = devicemasterRepository.findByImei(imei);
				
if (device != null) {
					
					WebSocketObj getentry = map.getClient(device.getImei());
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
					
}
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
			      String Main_Power=digitalData.substring(8,9);
			      String Relay=digitalData.substring(11,12);
			        log.info("WP30CRS485 "+imei+" GPS RelayS :: "+Relay);
			     // log.info("WP30CRS485 "+imei+" Main_Power :: "+digitalData.substring(8,9));
			      
			     /* log.info(imei+" : ACMAINS_FAIL "+ACMAINS_FAIL);
			      log.info(imei+" : Fire "+Fire);
			      log.info(imei+" : Door "+Door);
			      log.info(imei+" : DG_Running_Hrs "+DG_Running_Hrs);
			      log.info(imei+" : DG_Fault "+DG_Fault);
			      log.info(imei+" : Battry_Low "+Battry_Low);
			      log.info(imei+" : PP_Input_Fail "+PP_Input_Fail);*/
			      
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
					 gjo.put("latitude", "23.033863");
						gjo.put("longitude", "72.585022");
					gjo.put("DeviceDate", new Date());
					gjo.put("fuel", "0");
					gjo.put("relay", Relay);
					
					String fuel=msgary[16]; 
					double battry=Double.parseDouble(msgary[15].toString()) * analogFormula;
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
				    	// analogArray.remove(i);
				    	System.out.println("Before :: "+jsonObj.toString());
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
					JSONArray analogArray =new JSONArray();
					 for (int i = 0; i <analog.length(); i++) {
						 JSONObject analogJsonObject = new JSONObject();
				    	  JSONObject obj = (JSONObject) analog.get(i);
				    	  Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
				    	  analogJsonObject.put(param.getId().toString(), "0.0");
				    	  analogArray.put(analogJsonObject);
				      }
					 
					JSONObject jo = new JSONObject();
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogArray);
					jo.put("DeviceName", device.getDevicename());

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
				
				log.info("WP30CRS485 "+imei+" Analog :: Analog String");
				Devicemaster device = devicemasterRepository.findByImei(imei);
			  		JSONArray AnalogJsonArray = new JSONArray();
			  		JSONArray DigitalJsonArray = new JSONArray();
				 String[] analog1= new String[]{"6387981","6387982","5557109","6308790","5557111","6308792","5557117","6308794","5557118","6308796","5557124"};
			      String[] analog1Param= {"kWh_of_CH1","A_of_CH1","kWh_of_CH2","A_of_CH2","kWh_of_CH3","A_of_CH3","kWh_of_CH4","A_of_CH4","Voltage"};

				 String[] analog5= new String[]{"6387981","6387982","6337564","6337565","6337570","6337573","6337574","6337577","6337582","6337583","6337585","6337588","237921","6387968","6387970","6387973","237931","237932","237933","6387975","6387976","6387980"};
				 List<String> analog5NameList = new ArrayList<>(Arrays.asList(analog5));
				 List<String> analog1NameList = new ArrayList<>(Arrays.asList(analog1));
				  String[] Digital=new String[] {"284945","6348798","291934","6348854","6348815","6348821","6348824"};
				 
				 String[] analog5Param= {"Voltage_DC","Battery_Current","Load_Current","Rectifier_Current","R_Voltage","Y_voltage","B_voltage","SOC","Temperature_Battery","Battery_Capacity","ROOM_TEMP","Battery_Charging_Energy","Battery_Discharging_Energy","Load_Energy","LOAD1_ENERGY","LOAD2_ENERGY","LOAD3_ENERGY","Load4_Energy","Rectifier_Energy","Mains_Energy"};
				 Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
			  	 if(track!=null)
					{ 
			    if (msgary[3].contains("1")) {   
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
				        
				      //log.info("WP30CRS485 Analog  :: "+Arrays.toString(msgStringArray));
				  	
				  	
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
				    	  
				  	  
						
				    	  //  log.info("WP30CRS485 "+imei+" Analog  :: Track Not NULL");
							JSONObject jo = new JSONObject();
							JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
							JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
							//log.info("WP30CRS485 Digital :: "+DigitalJsonArray_old.toString());
							
							JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());
							//log.info("OLD :: "+OldAnalogJsonArray.toString());
							for(int a=0;a<OldAnalogJsonArray.length();a++)
							{
								JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
								String k = jsonObj.keys().next();
						       // log.info("Info Key: " + k + ", value: " + jsonObj.getString(k));
						        if(analog5NameList.contains(k))
						        {
						        	JSONObject analogObj = new JSONObject();
						        	analogObj.put(k,jsonObj.getString(k));
						        	AnalogJsonArray.put(analogObj);
						        }
							}
							
							jo.put("Digital", DigitalJsonArray_old);
							jo.put("Analog", AnalogJsonArray);
							jo.put("DeviceName", device.getDevicename());
							//log.info(AnalogJsonArray.toString());
							
							
						/*	History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata());*/

							Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
									track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
							
							History hst=histroyrepository.saveAndFlush(hist);
							Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
							log.info("WP30CRS485   :: "+t.getDeviceId());
			    }
		else if (msgary[3].contains("5")) {
			
			log.info("WP30CRS485 "+imei+" Analog 5");
			
			String[] dataStringArray=new String[21];
			
			String Voltage_DC=msgary[4].split(":")[1];
			
			String Battery_Current=msgary[5].split(":")[1];
			
			String Load_Current=msgary[6].split(":")[1];
			
			String Rectifier_Current=msgary[7].split(":")[1];
			
			String R_Voltage=msgary[8].split(":")[1];
			
			String Y_voltage=msgary[9].split(":")[1];
			
			String B_voltage=msgary[10].split(":")[1];
			
			String SOC=msgary[11].split(":")[1];
			
			String Temperature_Battery=msgary[12].split(":")[1];
			
			String Battery_Capacity=msgary[13].split(":")[1];
			
			String ROOM_TEMP=msgary[14].split(":")[1];
			
			
			String Battery_Charging_Energy=msgary[15].split(":")[1];
			
			String Battery_Discharging_Energy=msgary[16].split(":")[1];
			
			String Load_Energy=msgary[17].split(":")[1];
			
			String LOAD1_ENERGY=msgary[18].split(":")[1];
			
			String LOAD2_ENERGY=msgary[19].split(":")[1];
			
			String LOAD3_ENERGY=msgary[20].split(":")[1];
			
			String Load4_Energy=msgary[21].split(":")[1];
			
			String Rectifier_Energy=msgary[22].split(":")[1];
			
			String Mains_Energy=msgary[23].split(":")[1];
			
			JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
			JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());
			for(int a=0;a<OldAnalogJsonArray.length();a++)
			{
				JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
				String k = jsonObj.keys().next();
		       // log.info("Info Key: " + k + ", value: " + jsonObj.getString(k));
		        if(analog1NameList.contains(k))   
		        {
		        	JSONObject analogObj = new JSONObject();
		        	analogObj.put(k,jsonObj.getString(k));
		        	AnalogJsonArray.put(analogObj);
		        }
			}
			
			for(int i=0;i<analog5Param.length;i++)
		  	{
		  		JSONObject analogObj = new JSONObject();
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Voltage_DC")) {
		  			 analogObj.put("6337564",String.valueOf(twoDForm.format((Integer.parseInt(Voltage_DC, 16))*0.01)));
		  		}
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Battery_Current"))
		 			 analogObj.put("6337565",String.valueOf(twoDForm.format((Integer.parseInt(Battery_Current, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Load_Current"))
		 			 analogObj.put("6337570",String.valueOf(twoDForm.format((Integer.parseInt(Load_Current, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Rectifier_Current"))
		 			 analogObj.put("6337573",String.valueOf(twoDForm.format((Integer.parseInt(Rectifier_Current, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("R_Voltage"))
		 			 analogObj.put("6337574",String.valueOf(twoDForm.format((Integer.parseInt(R_Voltage, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Y_voltage"))
		 			 analogObj.put("6337577",String.valueOf(twoDForm.format((Integer.parseInt(Y_voltage, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("B_voltage"))
		 			 analogObj.put("6337582",String.valueOf(twoDForm.format((Integer.parseInt(B_voltage, 16))*0.01)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("SOC"))
		 			 analogObj.put("6337583",String.valueOf(twoDForm.format((Integer.parseInt(SOC, 16))*1)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Temperature_Battery"))
		 			 analogObj.put("6337585",String.valueOf(twoDForm.format((Integer.parseInt(Temperature_Battery, 16))*1)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Battery_Capacity"))
		 			 analogObj.put("6337588",String.valueOf(twoDForm.format((Integer.parseInt(Battery_Capacity, 16))*1)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("ROOM_TEMP"))
		 			 analogObj.put("237921",String.valueOf(twoDForm.format((Integer.parseInt(ROOM_TEMP, 16))*1)));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Battery_Charging_Energy"))
		 			 analogObj.put("6387968",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Battery_Charging_Energy)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Battery_Discharging_Energy"))
		 			 analogObj.put("6387970",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Battery_Discharging_Energy)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Load_Energy"))
		 			 analogObj.put("6387973",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Load_Energy)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("LOAD1_ENERGY"))
		 			 analogObj.put("237931",String.valueOf(Math.abs(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(LOAD1_ENERGY))))));  
		  		
		  		if(analog5Param[i].equalsIgnoreCase("LOAD2_ENERGY"))
		 			 analogObj.put("237932",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(LOAD2_ENERGY)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("LOAD3_ENERGY"))
		 			 analogObj.put("237933",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(LOAD3_ENERGY)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Load4_Energy"))
		 			 analogObj.put("6387975",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Load4_Energy)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Rectifier_Energy"))
		 			 analogObj.put("6387976",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Rectifier_Energy)))));
		  		
		  		if(analog5Param[i].equalsIgnoreCase("Mains_Energy"))
		 			 analogObj.put("6387980",twoDForm.format(Double.parseDouble(String.valueOf(ConvertMODBUSRTUValue(Mains_Energy)))));
		  		
		  		AnalogJsonArray.put(analogObj);
		  	}
			JSONObject jo = new JSONObject();
			log.info("WP30CRS485 "+imei+"   :: "+AnalogJsonArray.toString());
			
			
			JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
//			log.info("WP30CRS485 Digital :: "+DigitalJsonArray_old.toString());
			
			
			
			jo.put("Digital", DigitalJsonArray_old);
			jo.put("Analog", AnalogJsonArray);
			jo.put("DeviceName", device.getDevicename());
			log.info(AnalogJsonArray.toString());
			
			
			
			History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
					new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
					track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

			Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
					new ObjectMapper().readValue(jo.toString(), Map.class),
					track.getGpsdata(),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
			
			History hst=histroyrepository.saveAndFlush(hist);
			Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
			log.info("WP30CRS485   :: "+t.getDeviceId());
			
			
			
			    }else {
			    	log.info("WP30CRS485 "+imei+" Analog :: Invalid Analog String.");
			    }
			    	
			    }else
				{
			    	log.info("WP30CRS485 "+imei+" Analog ::NOT NULL");   
			   }
				
			}
		}
	}
	
	static String ConvertMODBUSRTUValue(String hexVal)
	{
        Long i1 = Long.parseLong(hexVal, 16);
        Float f1 = Float.intBitsToFloat(i1.intValue());
        return String.valueOf(f1);
	}

	public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
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

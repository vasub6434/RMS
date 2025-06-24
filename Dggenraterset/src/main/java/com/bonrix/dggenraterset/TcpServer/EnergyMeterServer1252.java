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

public class EnergyMeterServer1252 {  
	private static final Logger log = Logger.getLogger(EnergyMeterServer1252.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	public static class HandlerEnergyMeterServer1252 extends SimpleChannelUpstreamHandler {
		
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
		 	log.info("EnergyMeterServer1252:: Msg :Len: "+msg);
		 	
		 	String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String imei=msgary[0].substring(5);
			
			if (msgary[1].contains("$GPRMC")) {
				log.info("EnergyMeterServer1252 $GPRMC "+imei+" :: "+msg);
				String[] gpdAnalog= {"6387981","6387982"};
				 List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
				String digitalData=msgary[14];
				boolean Isvalid="A".equals(msgary[3]);
				
				if(Isvalid==true || Isvalid==false) {
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
					 log.info("EnergyMeterServer1252 "+imei+" IN FORMULA>>>>>> :: "+msgary[16]+" :: "+fuel);
					 log.info("EnergyMeterServer1252 "+imei+" IN FORMULA>>>>>> :: "+battry);
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
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/
				
			/*	History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
*/
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
				//analogAlert.sendAnalogAlert(device, lTrack);
				}else
				{
					log.info("EnergyMeterServer1252 "+imei+" :: NULL");
					JSONArray analogArray =new JSONArray();
					 for (int i = 0; i <analog.length(); i++) {
						 JSONObject analogJsonObject = new JSONObject();
				    	  JSONObject obj = (JSONObject) analog.get(i);
				    	  log.info("EnergyMeterServer1252 "+imei+" GPRMC  :: parameterId :: "+obj.get("Analoginput").toString());
				    	  Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
				    	  analogJsonObject.put(param.getId().toString(), "0.0");
				    	  analogArray.put(analogJsonObject);
				      }
					 
					JSONObject jo = new JSONObject();
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogArray);
					jo.put("DeviceName", device.getDevicename());
					
					
				/*	History hist = new History(device.getDeviceid(), device.getUserId(),  new Date(), new Date(),
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
				
				log.info("EnergyMeterServer1252 "+imei+" Analog :: Analog String");
				Devicemaster device = devicemasterRepository.findByImei(imei);
			  		JSONArray AnalogJsonArray = new JSONArray();
				 Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
				 
				 if(track!=null)
					{ 
					 if (msgary[3].contains("5")) {
							
							log.info("EnergyMeterServer1252 "+imei+" Analog 5");
							JSONObject analogObj = new JSONObject();
							
							String Voltage_DC=msgary[4].split(":")[1];
							System.out.println("Voltage_DC :: "+Voltage_DC);
							System.out.println("Voltage_DC :: "+Integer.parseInt(Voltage_DC,16));
							System.out.println("Voltage_DC :: "+Integer.parseInt(Voltage_DC,16)/1000);
							analogObj.put("23853829",String.valueOf(twoDForm.format((Integer.parseInt(Voltage_DC, 16))/100)));
							
							 analogObj = new JSONObject();
							String Current=msgary[5].split(":")[1];
							analogObj.put("23853984",String.valueOf((Integer.parseInt(Current, 16))/100));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_1=msgary[6].split(":")[1];
							analogObj.put("23859688",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_1, 16))/1000)));
						//	analogObj.put("23859688",String.valueOf((twoDForm.format(Integer.parseInt(Cell_voltage_1, 16))/1000));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_2=msgary[7].split(":")[1];
							analogObj.put("23854091",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_2, 16))/1000)));
							//analogObj.put("",String.valueOf((twoDForm.format(Integer.parseInt(Cell_voltage_2, 16)))/1000));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_3=msgary[8].split(":")[1];
							analogObj.put("23854116",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_3, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_4=msgary[9].split(":")[1];
							analogObj.put("23854134",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_4, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_5=msgary[10].split(":")[1];
							analogObj.put("23854159",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_5, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_6=msgary[11].split(":")[1];
							analogObj.put("23854186",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_6, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_7=msgary[12].split(":")[1];
							analogObj.put("23854209",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_7, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_8=msgary[13].split(":")[1];
							analogObj.put("23854238",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_8, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_9=msgary[14].split(":")[1];
							analogObj.put("23854269",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_9, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_10=msgary[15].split(":")[1];
							analogObj.put("23854289",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_10, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_11=msgary[16].split(":")[1];
							analogObj.put("23854309",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_11, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_12=msgary[17].split(":")[1];
							analogObj.put("23854338",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_12, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_13=msgary[18].split(":")[1];
							analogObj.put("23854364",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_13, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_14=msgary[19].split(":")[1];
							analogObj.put("23854385",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_14, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_15=msgary[20].split(":")[1];
							analogObj.put("23854416",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_15, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Cell_voltage_16=msgary[21].split(":")[1];
							analogObj.put("23854441",String.valueOf(twoDForm.format((Integer.parseInt(Cell_voltage_16, 16))/1000)));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String BMS_Cooling_Fan_Temp=msgary[22].split(":")[1];
							analogObj.put("23855254",String.valueOf(twoDForm.format((Integer.parseInt(BMS_Cooling_Fan_Temp, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Battery_Module_Internal_Temp=msgary[23].split(":")[1];
							analogObj.put("23855905",String.valueOf(twoDForm.format((Integer.parseInt(Battery_Module_Internal_Temp, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Maximum_Cell_Temp=msgary[24].split(":")[1];
							analogObj.put("23856473",String.valueOf(twoDForm.format((Integer.parseInt(Maximum_Cell_Temp, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Module_Remaining_AH=msgary[26].split(":")[1];
							analogObj.put("23856705",String.valueOf(twoDForm.format((Integer.parseInt(Module_Remaining_AH, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String Max_Charging_Current_Limit=msgary[27].split(":")[1];
							analogObj.put("23856978",String.valueOf(twoDForm.format((Integer.parseInt(Max_Charging_Current_Limit, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String SOH=msgary[28].split(":")[1];
							analogObj.put("23857056",String.valueOf(twoDForm.format((Integer.parseInt(SOH, 16)))));
							AnalogJsonArray.put(analogObj);
							
							analogObj = new JSONObject();
							String SOC=msgary[29].split(":")[1];
							analogObj.put("23857108",String.valueOf(twoDForm.format((Integer.parseInt(SOC, 16)))));
							AnalogJsonArray.put(analogObj);
							
							JSONObject jo = new JSONObject();
							log.info("EnergyMeterServer1252 "+imei+"   :: "+AnalogJsonArray.toString());
							JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
							
							JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());
							
							jo.put("Digital", DigitalJsonArray_old);
							jo.put("Analog", AnalogJsonArray);
							jo.put("DeviceName", device.getDevicename());
							log.info(AnalogJsonArray.toString());
							
							  
							
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
							log.info("EnergyMeterServer1252   :: "+t.getDeviceId());
							
							    }else {  
							    	log.info("EnergyMeterServer1252 "+imei+" Analog :: Invalid Analog String.");
							    }
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

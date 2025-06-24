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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.DTO.DGHashMap;
import com.bonrix.dggenraterset.DTO.DataHashMap;
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
import com.fasterxml.jackson.databind.node.ArrayNode;

public class LiBattry1255 {  
	public static class HandlerLiBattry1255 extends SimpleChannelUpstreamHandler {
		private Logger log = Logger.getLogger(LiBattry1255.class);

		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);
		ParameterRepository parameterrepository = ApplicationContextHolder.getContext().getBean(ParameterRepository.class);
		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		  
		@SuppressWarnings({ "unchecked", "unused" })  
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String strmsg = (String) e.getMessage();
			log.info("LiBattry1255 String :: " + strmsg);
			String[] msgary = strmsg.split(",");
			String imei=msgary[0].substring(5);
			//log.info("LiBattry1255 "+imei+" IMEI :: "+imei);
			//String datestr=msgary[1]+msgary[2];
			//log.info("LiBattry1255 "+imei+" DATE :: "+datestr);
			
			if (msgary[1].contains("$GPRMC")) {ObjectMapper mapper = new ObjectMapper();
			log.info("LiBattry1255 "+imei+" :: "+strmsg);
		//	log.info("LiBattry1255 "+imei+" GPS :: $GPRMC String");
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
		      double analogFormula=0.01;
		      for (int i = 0; i < analog.length(); i++) { 
		      JSONObject obj = (JSONObject) analog.get(i);
		      if(obj.get("Analoginput").toString().equalsIgnoreCase("6387981"))
		      {  
		    	  log.info("LiBattry1255 "+imei+" IN FORMULA  :: "+obj.get("Analogformula").toString());
		    	  analogFormula=Double.parseDouble(obj.get("Analogformula").toString());
		    	  log.info("LiBattry1255 "+imei+" IN FORMULA>>>>>> :: "+obj.get("Analogformula").toString());
		      }
		      }
		      
		      String ACMAINS_FAIL=digitalData.substring(1,2);
		      String Fire=digitalData.substring(2,3);
		      String Door=digitalData.substring(3,4);
		      String DG_Running_Hrs=digitalData.substring(4,5);
		      String DG_Fault=digitalData.substring(5,6);
		      String Battry_Low=digitalData.substring(6,7);
		      String PP_Input_Fail=digitalData.substring(7,8);
		      
		      log.info("LiBattry1255 "+imei+" GPS Main INPUTS :: "+digitalData);
		      log.info("LiBattry1255 "+imei+" GPS GREEN ACMAINS_FAIL :: "+digitalData.substring(1,2));
		      log.info("LiBattry1255 "+imei+" GPS Yellow Ornge Fire ::"+digitalData.substring(2,3));
		      log.info("LiBattry1255 "+imei+" GPS Yellow Door Door :: "+digitalData.substring(3,4));
		      log.info("LiBattry1255 "+imei+" GPS Yellow Black DG_Running_Hrs :: "+digitalData.substring(4,5));
		      log.info("LiBattry1255 "+imei+" GPS Yellow Brown DG_Fault :: "+digitalData.substring(5,6));
		      log.info("LiBattry1255 "+imei+" GPS Green Black Battry_Low :: "+digitalData.substring(6,7));
		      log.info("LiBattry1255 "+imei+" GPS Yellow Red :: PP_Input_Fail "+digitalData.substring(7,8));
		      
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
				// log.info("LiBattry1255 "+imei+" IN FORMULA>>>>>> :: "+msgary[16]+" :: "+fuel);
				// log.info("LiBattry1255 "+imei+" IN FORMULA>>>>>> :: "+battry);
				//DeviceProfile profile=device.getDp();
			Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
		//	log.info("LiBattry1255 :: Track LOD "+track.getDeviceId());
			if(track!=null)
			{   
			//	log.info("LiBattry1255 "+imei+" GPRMC  :: Track Not NULL");
			//	log.info("LiBattry1255 "+imei+" GPRMC gjo :: "+gjo.toString());
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
			    	//System.out.println("Before :: "+jsonObj.toString());
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
		        //	System.out.println("After :: "+jsonObj.toString());
		        	//AnalogDataArray.put(analogObj);
		        }
			}
			if(!isInsert)
			{
				//System.out.println("Not insert");
				JSONObject analogbattryObj = new JSONObject();
				analogbattryObj.put("6387981",String.valueOf(battry));
				JSONObject analogfuelObj = new JSONObject();
				analogfuelObj.put("6387982",fuel);
				AnalogDataArray.put(analogbattryObj);
				AnalogDataArray.put(analogfuelObj);
			}
			
	 //	log.info("LiBattry1255 "+imei+" GPS :: "+digitaljsonarr.toString());
			jo.put("Digital", digitaljsonarr);
			jo.put("Analog", analogArray);
			//jo.put("Analog", AnalogDataArray);
			jo.put("DeviceName", device.getDevicename());
			
		/*	History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
					new ObjectMapper().readValue(jo.toString(), Map.class),
					new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

			Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
					new ObjectMapper().readValue(jo.toString(), Map.class),
					new ObjectMapper().readValue(gjo.toString(), Map.class),
					new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
			
			/*History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
					new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
					new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

			  
			History hst=histroyrepository.saveAndFlush(hist); */    
			Lasttrack t=lasttrackrepository.saveAndFlush(lTrack);
			/*MyAlerts alert=new MyAlerts();
			alert.sendMsg(device,lTrack,track);
			MyAnalogAlert analogAlert=new MyAnalogAlert();
			analogAlert.sendAnalogAlert(device, lTrack);*/
			}else
			{
			//	log.info("LiBattry1255 "+imei+" :: NULL");
				JSONArray analogArray =new JSONArray();
				 for (int i = 0; i <analog.length(); i++) {
					 JSONObject analogJsonObject = new JSONObject();
			    	  JSONObject obj = (JSONObject) analog.get(i);
			    	//  log.info("LiBattry1255 "+imei+" GPRMC  :: parameterId :: "+obj.get("Analoginput").toString());
			    	  Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
			    	  analogJsonObject.put(param.getId().toString(), "0.0");
			    	  analogArray.put(analogJsonObject);
			      }
				 
				JSONObject jo = new JSONObject();  
				jo.put("Digital", digitaljsonarr);
				jo.put("Analog", analogArray);
				jo.put("DeviceName", device.getDevicename());
				
				/*History hist = new History(device.getDeviceid(), device.getUserId(), sdf.parse(datestr), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));*/

				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
						new ObjectMapper().readValue(jo.toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
				
				/*History hist = new History(device.getDeviceid(), device.getUserId(), sdf.parse(datestr), new Date(),
						new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
						new ObjectMapper().readValue(gjo.toString(), Map.class),new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

				
				History hst=histroyrepository.saveAndFlush(hist);*/
			lasttrackrepository.saveAndFlush(lTrack);
			}
		}}else {
				 
					try {
						//log.info("LiBattry1255 "+msgary[0].substring(4));
					Devicemaster device = devicemasterRepository.findByImei(msgary[0].substring(5));
				
						
						if (device != null) {  
							 Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
							 if(track!=null)
								{ 
							//	 log.info("LiBattry1255 ANALOG TRACK NOT NULL "+device.getImei()+"  "+strmsg);
							JSONObject jo = new JSONObject();
							JSONObject gpsjo = new JSONObject();
							DeviceProfile dp = device.getDp();
							JSONArray digitaljsonarr = new JSONArray();
							JSONObject parameters = new JSONObject(dp.getParameters());
							
							JSONArray rs232arr = new JSONArray();
							JSONArray rs232 = parameters.getJSONArray("Analog");
							JSONArray digital = parameters.getJSONArray("Digital");
							//JSONObject rs232obj = new JSONObject();
							ObjectMapper mapper = new ObjectMapper();
						//	org.codehaus.jackson.node.ArrayNode analogNodearrayNode = mapper.createArrayNode();
							JSONArray analogNodearrayNode = new JSONArray();
						//	ObjectNode rs232obj = mapper.createObjectNode();
							JSONObject rs232obj = new JSONObject();
							org.codehaus.jackson.node.ArrayNode array = mapper.createArrayNode();
							
							JSONObject analogJsonObject = new JSONObject((mapper.writeValueAsString(track.getDevicedata())));
							JSONObject OldAnalogJsonObject =  (JSONObject) analogJsonObject.get("Analog");
							//JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
						//	JSONArray AnalogDataArray = new JSONArray(analogArray.toString());
								    rs232obj.put("6387981",OldAnalogJsonObject.getDouble("6387981"));
								    rs232obj.put("6387982",OldAnalogJsonObject.getDouble("6387982"));
								    
								    JSONObject analogJson  = new JSONObject((mapper.writeValueAsString(track.getAnalogdigidata())));
							try {
								for (int i = 0; i < rs232.length(); i++) {
									JSONObject obj = (JSONObject) rs232.get(i);
									Double d = Double.parseDouble(obj.get("analogioindex").toString());
								//	log.info("EnergyMeter "+device.getImei()+"  "+device.getImei()+" :: d.intValue() :: " + d.intValue());
									boolean isFound = msgary[d.intValue()].split(":")[1].indexOf("X") !=-1? true: false;
									if (isFound==false) {
										rs232obj.put(obj.get("Analoginput").toString(),
												Double.valueOf(twoDForm.format((Integer.parseInt(msgary[d.intValue()].split(":")[1], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
									} else {
									//	log.info("LiBattry1255 ELSE PART Execute ");
										rs232obj.put(obj.get("Analoginput").toString(), "00.00");
									}

								} 
								
							} catch (Exception e2) {
								e2.printStackTrace();
								log.info("LiBattry1255 ANALOG "+device.getImei()+" "+e2.getMessage());
								log.info("LiBattry1255 ANALOG "+device.getImei()+" "+e2.getStackTrace());
								log.info("LiBattry1255 ANALOG "+device.getImei()+" (Index Out of Bound) ==> Registers are not match with profile");
							}
							analogNodearrayNode.put(rs232obj);
							rs232arr.put(rs232obj);
							digitaljsonarr.put(new JSONArray(analogJson.get("Digital").toString()));
						
							Devicemaster dm = devicemasterRepository.findOne(device.getDeviceid());
							jo.put("Digital", digitaljsonarr);
							jo.put("Analog", analogNodearrayNode);   
							//log.info("rs232obj : "+jo.toString());
							try {
								Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(),
										new Date(),
										new ObjectMapper().readValue(jo.toString(), Map.class),
										new ObjectMapper().readValue(gpsjo.toString(), Map.class),
										new ObjectMapper().readValue(jo.toString(), Map.class));
								Lasttrack Track = lasttrackrepository.saveAndFlush(lTrack); 
								//log.info("LiBattry1255 "+lTrack.toString());
								//log.info("LiBattry1255 "+Track.getDeviceId());
								/*History hist  = new History(device.getDeviceid(), device.getUserId(), new Date(),
												new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
												new ObjectMapper().readValue(gpsjo.toString(), Map.class));
								histroyrepository.saveAndFlush(hist);*/
							} catch (Exception e2) {
								e2.printStackTrace();
								log.info("LiBattry1255 "+e2.getMessage());
							}
						}

						}else
						{
							log.info("No Device Found");
						}
				} catch (Exception e1) {
					e1.printStackTrace();
					log.info("LiBattry1255 "+e1.getMessage());
				} 
			
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
		        	      //  System.out.println("Key :" + key + "  Value :" + analogJson.get(key));
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
		        	     //   System.out.println("Key :" + key + "  Value :" + digitalJson.get(key));
		        	        digitalJsonObject.put(key, digitalJson.get(key));
		        	    }
		        	}
		           globalJsonObject.put("Digital", digitalJsonObject);
		           return globalJsonObject;
		}

		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			System.out.println("SAM::: Chanel Close" + e.getCause());
			e.getChannel().close();
		}

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
}

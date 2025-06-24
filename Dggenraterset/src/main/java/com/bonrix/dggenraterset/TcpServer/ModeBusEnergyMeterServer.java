package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
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

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.DTO.DataHashMap;
import com.bonrix.dggenraterset.DTO.LogObject;
import com.bonrix.dggenraterset.DTO.MODBUSDataHashMap;
import com.bonrix.dggenraterset.DTO.ModeBusEnergyMeterHashMap;
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

public class ModeBusEnergyMeterServer {

  
	private static final Logger log = Logger.getLogger(ModeBusEnergyMeterServer.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	public static class HandleModeBusBEnergyMeterServer extends SimpleChannelUpstreamHandler  {

		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
		
		ParameterRepository parameterrepository = ApplicationContextHolder.getContext().getBean(ParameterRepository.class);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		static ModeBusEnergyMeterHashMap ModBusDataHashmap = new ModeBusEnergyMeterHashMap();
		@SuppressWarnings("unused")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg= (String)e.getMessage();
			  ObjectMapper mapper = new ObjectMapper();
		 	log.info("ModeBusBEnergyMeterServer:: Msg : "+msg);
		 	log.info("DatatypeConverter:: Msg : "+DatatypeConverter.printHexBinary(msg.substring(0, 2).getBytes()));
		 	boolean islive = DatatypeConverter.printHexBinary(msg.substring(0, 2).getBytes())
					.equals("2001");
			String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String datestr=msgary[10]+msgary[2];
			
			if (msgary[1].contains("$GPRMC")) {
				String imei=msgary[0].substring(5);
				log.info("ModeBusBEnergyMeterServer GPS "+imei+" :: "+msg);
				
				try {
					LogObject Datagetentry = ModBusDataHashmap.getClient(imei);
					 Date date = Calendar.getInstance().getTime();  
		             DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
		             String entryDate = dateFormat.format(date);  
					if (Datagetentry == null) {
						LogObject stringLog = new LogObject();
						stringLog.setEntryDate(entryDate);
						stringLog.setMsg(msg);
						ModBusDataHashmap.AddClient(imei, stringLog);
					} else {
						Datagetentry.setEntryDate(entryDate);   
						Datagetentry.setMsg(msg);
						ModBusDataHashmap.AddClient(imei, Datagetentry);
					}
					} catch (Exception ex) {
						log.info("EnergyMeter DataHashMap  :: "+ex);
						log.error("EnergyMeter DataHashMap  :: "+ex);
						ex.printStackTrace();
					}
				
		
			}else {
				String imei=msgary[0].substring(7);
				Devicemaster device = devicemasterRepository.findByImei(imei);
				log.info("ModeBusBEnergyMeterServer Analog "+imei+" :: "+msg);
				LogObject Datagetentry = ModBusDataHashmap.getClient(imei);
				if (Datagetentry != null) {
					String dataString=Datagetentry.getMsg()+","+msg;
					//Error
					//String dataString=Datagetentry.getMsg()+","+msg;
					log.info("ModeBusBEnergyMeterServer concat Analog "+imei+" :: "+dataString);
					String[] dataStringArray=dataString.split(",");
					//log.info("ModeBusBEnergyMeterServer SAJAN "+imei+" :: "+dataStringArray[15]);
					//log.info("ModeBusBEnergyMeterServer SAJAN "+imei+" :: "+dataStringArray[16]);
					DeviceProfile dp = device.getDp();
					JSONObject parameters = new JSONObject(dp.getParameters());
					JSONArray analog = parameters.getJSONArray("Analog");   
					JSONArray digital = parameters.getJSONArray("Digital");
					JSONObject AnalogDataObject = new JSONObject();
					JSONArray AnalogJsonArray = new JSONArray();
					JSONArray DigitalJsonArray = new JSONArray();
					JSONObject DigitalDataObject = new JSONObject();
					JSONObject DeviceData=new JSONObject();
					Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
					
					Double latitude = 0.0d;
					Double Langitude = 0.0d;
					boolean isvalid = Boolean.valueOf("A".equals(dataStringArray[3]));
					if (isvalid) {
					if ((dataStringArray[4] == "") || (dataStringArray[4] == null)) {
						latitude = StringToolsV3.parseLatitude("0.0", "N");
					} else {
						latitude = StringToolsV3.parseLatitude(dataStringArray[4], "N");
					}
					if ((dataStringArray[6] == "") || (dataStringArray[6] == null)) {
						Langitude = StringToolsV3.parseLatitude("0.0", "E");
					} else {
						Langitude = StringToolsV3.parseLatitude(dataStringArray[6], "E");
					}
					}
					log.info("latitude : "+imei+" :: "+latitude);
					log.info("Langitude : "+imei+" :: "+Langitude);
					
					JSONObject Gpsobj = new JSONObject();
					Gpsobj.put("latitude", latitude);
					Gpsobj.put("longitude", Langitude);
					Gpsobj.put("speed", dataStringArray[8].equalsIgnoreCase("")?"0":dataStringArray[8]);
					try {
						
						for (int i = 0; i < analog.length(); i++) {
							JSONObject obj = (JSONObject) analog.get(i);
							Double d = Double.parseDouble(obj.get("analogioindex").toString());
							boolean isAtFound = dataStringArray[d.intValue()].contains("@")? true: false;
							Long d1 = Long.parseLong(obj.get("Analoginput").toString());

							if (isAtFound==false)
							{
								String hexString = dataStringArray[d.intValue()];
								if (hexString != null && !hexString.isEmpty() && !hexString.matches("[0-9A-Fa-f]+")) {
									try {
										
										//log.info("IF --> "+imei+" "+d+" "+obj.get("Analoginput").toString().equalsIgnoreCase("1355411238"));
										/* if (obj.get("Analoginput").toString().equalsIgnoreCase("1355411238") || obj.get("Analoginput").toString().equalsIgnoreCase("1355411608")) 
									    	AnalogDataObject.put(obj.get("Analoginput").toString(),LittleEndianToFloat(dataStringArray[d.intValue()]));
										else*/
								    	AnalogDataObject.put(obj.get("Analoginput").toString(),Double.valueOf(twoDForm.format(Double.parseDouble(dataStringArray[d.intValue()]) *  Double.parseDouble(obj.get("Analogformula").toString()))));
									  } catch (NumberFormatException e1) {
										  log.info("Invalid hexadecimal string: " + hexString);
									    }
									}else
								    	AnalogDataObject.put(obj.get("Analoginput").toString(),Double.valueOf(twoDForm.format((Integer.parseInt(dataStringArray[d.intValue()], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
							}else if (isAtFound==true)
							{
								boolean isFound = dataStringArray[d.intValue()].split("@")[0].indexOf("z") !=-1? true: false;
								log.info("IF --> "+imei+" "+d+" "+obj.get("Analoginput").toString().equalsIgnoreCase("1355411238"));
								if (isFound==false) {
									if (obj.get("Analoginput").toString().equalsIgnoreCase("1355411238") || obj.get("Analoginput").toString().equalsIgnoreCase("1355411608")) 
									{
										AnalogDataObject.put(obj.get("Analoginput").toString(),LittleEndianToFloat(dataStringArray[d.intValue()].split("@")[0]));
									}
								else
									AnalogDataObject.put(obj.get("Analoginput").toString(),	Double.valueOf(twoDForm.format((Integer.parseInt(dataStringArray[d.intValue()].split("@")[0], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
								
								}
									else 
									AnalogDataObject.put(obj.get("Analoginput").toString(), Double.valueOf("00.00"));
							}else 
								AnalogDataObject.put(obj.get("Analoginput").toString(), Double.valueOf("00.00"));
						}
						
						AnalogJsonArray.put(AnalogDataObject);
						DeviceData.put("Analog", AnalogJsonArray);
						String Digital_Alarm=dataStringArray[14];
						for (int i = 0; i < digital.length(); i++) {
							JSONObject obj = (JSONObject) digital.get(i);
							boolean reverse =Boolean.parseBoolean(obj.get("reverse").toString());
							char alarm = Digital_Alarm.charAt((int) Double.parseDouble(obj.get("dioindex").toString()));
							if (reverse == true) 
								DigitalDataObject.put(obj.get("parameterId").toString(),alarm=='1'? "0" : "1");
							else
								DigitalDataObject.put(obj.get("parameterId").toString(),String.valueOf(alarm));
							}
						DigitalJsonArray.put(DigitalDataObject);
						DeviceData.put("Digital", DigitalJsonArray);
						 log.info(DeviceData.toString());
						
						 Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
								new Date(), new Date(),
								new ObjectMapper().readValue(DeviceData.toString(), Map.class),
								new ObjectMapper().readValue(Gpsobj.toString(), Map.class),
								new ObjectMapper().readValue(convertJson(DeviceData.toString()).toString(), Map.class));
						lasttrackrepository.saveAndFlush(lTrack);
				/*		MyAlerts alert=new MyAlerts();
						alert.sendMsg(device,lTrack,track);*/
						
						History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
								new ObjectMapper().readValue(DeviceData.toString(), Map.class),
								new ObjectMapper().readValue(Gpsobj.toString(), Map.class),new ObjectMapper().readValue(convertJson(DeviceData.toString()).toString(), Map.class));
						History hst=histroyrepository.saveAndFlush(hist);
					} catch (Exception e2) {
						e2.printStackTrace();
						log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getMessage());
						log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getStackTrace());
						//log.info("ModeBusBEnergyMeterServer "+device.getImei()+" (Index Out of Bound) ==> Registers are not match with profile");
					}
				
				
				
				}else
					log.info("ModeBusBEnergyMeterServer Analog No GPS String Found "+imei+" :: "+msg);
			}
		}
	}
	
	static float LittleEndianToFloat(String inputHex)
	{

        // Step 1: Input hexadecimal string
     //   String inputHex = "40A0";
		log.info("ONGC inputHex "+inputHex);
        // Step 2: Convert the hex string to a byte array
        byte[] originalBytes = hexStringToByteArray(inputHex);

        // Step 3: Swap the bytes (Little-endian assumption)
        byte[] swappedBytes = new byte[]{originalBytes[1], originalBytes[0]};

        // Step 4: Prepend two zero bytes to form a 4-byte array (00 00 10 41)
        byte[] finalBytes = new byte[]{
                (byte) 0x00,
                (byte) 0x00,
                swappedBytes[0],
                swappedBytes[1]
        };

        // Step 5: Wrap the byte array into a ByteBuffer and set byte order to little-endian
        ByteBuffer buffer = ByteBuffer.wrap(finalBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Step 6: Convert the bytes to a float
        float floatValue = buffer.getFloat();

        // Step 7: Output the result
       log.info("ONGC The 32-bit floating-point value is: " + floatValue);
        return floatValue;
    
	}
	
	  public static byte[] hexStringToByteArray(String hex) {
	        int len = hex.length();
	        if (len % 2 != 0) {
	            throw new IllegalArgumentException("Hex string must have even length.");
	        }

	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                                 + Character.digit(hex.charAt(i + 1), 16));
	        }
	        return data;
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
	        	        System.out.println("Key :" + key + "  Value :" + analogJson.get(key));
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
	        	        System.out.println("Key :" + key + "  Value :" + digitalJson.get(key));
	        	        digitalJsonObject.put(key, digitalJson.get(key));
	        	    }
	        	}
	           globalJsonObject.put("Digital", digitalJsonObject);
	           return globalJsonObject;
	}

	
}

package com.bonrix.dggenraterset.TcpServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.exception.BonrixException;
import com.bonrix.dggenraterset.DTO.LogObject;
import com.bonrix.dggenraterset.DTO.MODBUSHashMap;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.GPS;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.Utility.StringToolsV3;
import com.google.gson.Gson;
public class MODBUSRTUEnergyMeter{
public static class HandleMODBUSRTUEnergyMeter extends SimpleChannelUpstreamHandler {   
	

	private Logger log = Logger.getLogger(MODBUSRTUEnergyMeter.class);

	LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
			.getBean(LasttrackRepository.class);

	DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
			.getBean(DevicemasterRepository.class);

	HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	 private SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
	    private SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	static MODBUSHashMap map = new MODBUSHashMap();
	static MODBUSHashMap DataHashmap = new MODBUSHashMap();
	DecimalFormat twoDForm = new DecimalFormat("#.##");
	  
	@SuppressWarnings({ "unchecked", "unused" })  
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
		String strmsg = (String) e.getMessage();
		try  
	      {
	          PrintWriter localPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter("/opt/tomcat8/webDG/ROOT/MODBUSRTUServerLog/MODBUSRTU_" + this.sdf2.format(new Date()) + ".txt", true)));
	          localPrintWriter.println(this.sdf3.format(new Date()) + "::" + strmsg);
	          localPrintWriter.close();
	        }
	      catch (Exception localException)
	      {
	        log.info(localException);
	      }
		
		String[] msgary = strmsg.split(",");
		
		log.info("MODBUSRTU String :: " + strmsg);
		try {
			LogObject Datagetentry = DataHashmap.getClient(msgary[0].substring(5));
			 Date date = Calendar.getInstance().getTime();  
             DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
             String entryDate = dateFormat.format(date);  
			if (Datagetentry == null) {
				LogObject stringLog = new LogObject();
				stringLog.setEntryDate(entryDate);
				stringLog.setMsg(strmsg);
				DataHashmap.AddClient(msgary[0].substring(5), stringLog);
			} else {
				Datagetentry.setEntryDate(entryDate);   
				Datagetentry.setMsg(strmsg);
				DataHashmap.AddClient(msgary[0].substring(5), Datagetentry);
			}
			} catch (Exception ex) {
				log.info("MODBUSRTU DataHashMap  :: "+ex);
				log.error("MODBUSRTU DataHashMap  :: "+ex);
				ex.printStackTrace();
			}
		if (msgary[1].contains("GPRMC")) {
			/*try {
				LogObject Datagetentry = DataHashmap.getClient(msgary[0].substring(5));
				 Date date = Calendar.getInstance().getTime();  
	             DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
	             String entryDate = dateFormat.format(date);  
				if (Datagetentry == null) {
					LogObject stringLog = new LogObject();
					stringLog.setEntryDate(entryDate);
					stringLog.setMsg(strmsg);
					DataHashmap.AddClient(msgary[0].substring(5), stringLog);
				} else {
					Datagetentry.setEntryDate(entryDate);   
					Datagetentry.setMsg(strmsg);
					DataHashmap.AddClient(msgary[0].substring(5), Datagetentry);
				}
				} catch (Exception ex) {
					log.info("MODBUSRTU DataHashMap  :: "+ex);
					log.error("MODBUSRTU DataHashMap  :: "+ex);
					ex.printStackTrace();
				}*/
			try {
			/*	if (strmsg.length() > 10) {
					String imei = msgary[0].substring(5);
					Devicemaster device = devicemasterRepository.findByImei(imei);
					log.info("EnergyMeter GPS  "+device.getImei()+"  GPS String :: " + strmsg);
					log.info("EnergyMeter GPS  "+device.getImei()+" GPS  :: IMEI " + imei);
					//log.info("EnergyMeter "+device.getDeviceid()+" GPS  :: msgary " + msgary);
				//	log.info("EnergyMeter "+device.getDeviceid()+" GPS  :: msgary " +  msgary[15]);
					   
					if (device != null) {
						String datestr = msgary[10] + msgary[2].substring(0, 6);
						log.info("EnergyMeter GPS  "+device.getImei()+" GPS DateTmime :: " + datestr);
						log.info("EnergyMeter GPS  "+device.getImei()+" GPS DateTmime :: " + sdf.parse(datestr));
						log.info("EnergyMeter GPS  "+device.getImei()+" :: Isvalid" + msgary[3]);
						boolean isvalid = Boolean.valueOf("A".equals(msgary[3]));
						
						DeviceProfile dp = device.getDp();
						JSONArray digitaljsonarr = new JSONArray();
						log.info("EnergyMeter GPS  "+device.getImei()+" "+device.getImei()+" device.getDp() :: "+device.getDp().getPrid());
						log.info("EnergyMeter GPS  "+device.getImei()+" Profile Id :: "+dp.getParameters().toString());
						JSONObject parameters = new JSONObject(dp.getGpsdata());
						log.info("EnergyMeter GPS  "+device.getImei()+" parameters :: " +parameters.toString());
						JSONArray gps = parameters.getJSONArray("GPS");
						log.info("EnergyMeter GPS  "+device.getImei()+" :: " +gps.toString());
						Double fuelIndex=0d;
						for (int i = 0; i < gps.length(); i++) {
							JSONObject obj = (JSONObject) gps.get(i);
							fuelIndex= Double.parseDouble(obj.get("gpsindex").toString());
							log.info("EnergyMeter GPS  "+device.getImei()+" :: Fuel Index :: " +fuelIndex);
							}
						

						log.info("EnergyMeter GPS  "+device.getDeviceid()+" "+device.getDeviceid()+" GPS :: Is Live"
								+ DatatypeConverter.printHexBinary(strmsg.substring(0, 2).getBytes()));
						log.info("EnergyMeter GPS  :: Checksumvalue: " + DatatypeConverter
								.printHexBinary(msgary[msgary.length - 1].split("ATL")[1].getBytes()));
						boolean islive = DatatypeConverter.printHexBinary(strmsg.substring(0, 2).getBytes())
								.equals("2001");

						Double latitude = 0.0d;
						Double Langitude = 0.0d;
						if (isvalid) {
							if ((msgary[4] == "") || (msgary[4] == null)) {
								latitude = StringToolsV3.parseLatitude("0.0", "N");
							} else {
								latitude = StringToolsV3.parseLatitude(msgary[4], "N");
							}
							if ((msgary[6] == "") || (msgary[6] == null)) {
								Langitude = StringToolsV3.parseLatitude("0.0", "E");
							} else {
								Langitude = StringToolsV3.parseLatitude(msgary[6], "E");
							}
							System.out.println("EnergyMeter GPS  "+device.getImei()+" GPS latitude:: " + latitude + "Langitude:: " + Langitude);
						} else {
							System.out.println("EnergyMeter GPS  "+device.getImei()+" GPS Cell Id: " + msgary[22] + "");
						}

						String digitalldata = msgary[14];
						log.info("EnergyMeter GPS  "+device.getImei()+" GPS digianalogdata::: " + digitalldata);
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(sdf.parse(datestr));
						cal.add(Calendar.HOUR, 5);
						cal.add(Calendar.MINUTE, 30);
						Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
						JSONObject obj = new JSONObject();
						obj.put("latitude", latitude);
						obj.put("longitude", Langitude);
						obj.put("DeviceDate", insertingdate);
						obj.put("fuel", msgary[fuelIndex.intValue()]);

						
						if (islive) {
							Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());

							if (track == null) {
								log.info("EnergyMeter GPS  "+device.getImei()+" Track NULL :: " + track);
								JSONObject Ljo = new JSONObject();
								Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
										insertingdate, new Date(),
										new ObjectMapper().readValue(Ljo.toString(), Map.class),
										new ObjectMapper().readValue(obj.toString(), Map.class));
								lasttrackrepository.saveAndFlush(lTrack);
								
								History hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
										new Date(), new ObjectMapper().readValue(Ljo.toString(), Map.class),
										new ObjectMapper().readValue(obj.toString(), Map.class));

								histroyrepository.saveAndFlush(hist);

							} else {       
								log.info("EnergyMeter GPS  "+device.getImei()+" Track NOT NULL :: " + track.getAnalogdigidata().toString());
								track.setDeviceDate(insertingdate);
								track.setSystemDate(new Date());   
								track.setGpsdata(new ObjectMapper().readValue(obj.toString(), Map.class));
								lasttrackrepository.saveAndFlush(track);
								Gson gsonObj = new Gson();
								JSONObject Ljo21 = new JSONObject(track.getAnalogdigidata());
								History hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
										new Date(), new ObjectMapper().readValue(Ljo21.toString(), Map.class),
										new ObjectMapper().readValue(obj.toString(), Map.class));

								histroyrepository.saveAndFlush(hist);
								
							}

						}else
						{
							Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
							if (track != null) {
								log.info("EnergyMeter GPS  IN Memory Data Last Track Not Null");
								JSONObject Ljo21 = new JSONObject(track.getAnalogdigidata());
								History hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
										new Date(), new ObjectMapper().readValue(Ljo21.toString(), Map.class),
										new ObjectMapper().readValue(obj.toString(), Map.class));
								histroyrepository.saveAndFlush(hist);
							}else
								log.info("EnergyMeter GPS IN Memory Data Last Track Null");
						}

					} else {
						log.info("EnergyMeter GPS  Unknown1 Device Found:::::: " + imei);
					}
				}*/
			} catch (Exception ex) {
				log.info("EnergyMeter :: "+ex);
				ex.printStackTrace();
			}

		} else {
		if(msgary.length>7) {
			
			try {
				LogObject Datagetentry = DataHashmap.getClient(msgary[0].substring(5));
				 Date date = Calendar.getInstance().getTime();  
	             DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
	             String entryDate = dateFormat.format(date);  
				if (Datagetentry == null) {
					LogObject stringLog = new LogObject();
					stringLog.setEntryDate(entryDate);
					stringLog.setMsg(strmsg);
					DataHashmap.AddClient(msgary[0].substring(5), stringLog);
				} else {
					Datagetentry.setEntryDate(entryDate);   
					Datagetentry.setMsg(strmsg);
					DataHashmap.AddClient(msgary[0].substring(5), Datagetentry);
				}
				} catch (Exception ex) {
					log.info("EnergyMeter DataHashMap  :: "+ex);
					log.error("EnergyMeter DataHashMap  :: "+ex);
					ex.printStackTrace();
				}
				
			
			if(Integer.parseInt(msgary[3])!=2) {
				
				try {
				Devicemaster device = devicemasterRepository.findByImei(msgary[0].substring(5));
				log.info("MODBUSRTU ANALOG Else :: "+device.getImei());
			if (device != null) {
				
					e.getRemoteAddress();
					log.info("MODBUSRTU ANALOG "+device.getImei()+"  "+strmsg);
					  
					Calendar cal = Calendar.getInstance();
					
					if( msgary[2].toString().length()==6)
					{
						cal.setTime(sdf.parse(msgary[1] + msgary[2]));
						log.info("MODBUSRTU ANALOG "+device.getImei()+" DATE "+msgary[1] + msgary[2]);
					}
						else if( msgary[2].toString().length()==5) {
						cal.setTime(sdf.parse(msgary[1] + msgary[2]+"0"));
						
						log.info("MODBUSRTU ANALOG "+device.getImei()+" DATE "+msgary[1] + msgary[2]+"0");
						}
						else
							log.info("MODBUSRTU ANALOG "+device.getImei()+" Invalid Date");   
					
					
					
					cal.add(Calendar.HOUR, 5);
					cal.add(Calendar.MINUTE, 30);
					Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
					log.info("MODBUSRTU ANALOG "+device.getImei()+" DATE "+insertingdate);
					if (device != null) {  
						JSONArray analogjsonarr = new JSONArray();
						JSONObject jo = new JSONObject();
						JSONObject gpsjo = new JSONObject();
						DeviceProfile dp = device.getDp();
						JSONArray digitaljsonarr = new JSONArray();
						JSONObject parameters = new JSONObject(dp.getParameters());
						
						JSONArray rs232arr = new JSONArray();
						JSONArray rs232 = parameters.getJSONArray("Analog");
						JSONObject rs232obj = new JSONObject();
						
						String ch1=msgary[4].split(":")[1]+msgary[5].split(":")[1];
						String ACH1=msgary[6].split(":")[1]+msgary[7].split(":")[1];
						String ch2=msgary[8].split(":")[1]+msgary[9].split(":")[1];
						String ACH2=msgary[10].split(":")[1]+msgary[11].split(":")[1];
						String ch3=msgary[12].split(":")[1]+msgary[13].split(":")[1];
						String ACH3=msgary[14].split(":")[1]+msgary[15].split(":")[1];
						String ch4=msgary[16].split(":")[1]+msgary[17].split(":")[1];
						String ACH4=msgary[18].split(":")[1]+msgary[19].split(":")[1];
						String volt=msgary[20].split(":")[1]+msgary[21].split(":")[1];
						String kWh_of_CH1=ConvertMODBUSRTUValue(ch1);
						String kWh_of_CH2=ConvertMODBUSRTUValue(ch2);
						String kWh_of_CH3=ConvertMODBUSRTUValue(ch3);
						String kWh_of_CH4=ConvertMODBUSRTUValue(ch4);
						String Voltage=ConvertMODBUSRTUValue(volt);
						String A_of_CH1=ConvertMODBUSRTUValue(ACH1);   
						String A_of_CH2=ConvertMODBUSRTUValue(ACH2);
						String A_of_CH3=ConvertMODBUSRTUValue(ACH3);
						String A_of_CH4=ConvertMODBUSRTUValue(ACH4);
						log.info("MODBUSRTU ANALOG ch1 "+ch1);
						log.info("MODBUSRTU ANALOG ch2 "+ch2);
						log.info("MODBUSRTU ANALOG ch3 "+ch3);
						log.info("MODBUSRTU ANALOG ch4 "+ch4);
						log.info("MODBUSRTU ANALOG volt "+volt);
						log.info("MODBUSRTU ANALOG kWh_of_CH1 "+kWh_of_CH1);
						log.info("MODBUSRTU ANALOG kWh_of_CH2 "+kWh_of_CH2);
						log.info("MODBUSRTU ANALOG kWh_of_CH3 "+kWh_of_CH3);
						log.info("MODBUSRTU ANALOG kWh_of_CH4 "+kWh_of_CH4);
						log.info("MODBUSRTU ANALOG "+Voltage);
						
						boolean isFound = strmsg.indexOf("X") !=-1? true: false;
						if (isFound==false) {
							rs232obj.put("5557109",kWh_of_CH1);
							rs232obj.put("5557111",kWh_of_CH2);
							rs232obj.put("5557117",kWh_of_CH3);
							rs232obj.put("5557118",kWh_of_CH4);
							rs232obj.put("5557124",Voltage);
							rs232obj.put("6308790",A_of_CH1);
							rs232obj.put("6308792",A_of_CH2);
							rs232obj.put("6308794",A_of_CH3);
							rs232obj.put("6308796",A_of_CH4);
						} else {
							rs232obj.put("5557109","00.00");
							rs232obj.put("5557111","00.00");
							rs232obj.put("5557117","00.00");
							rs232obj.put("5557118","00.00");
							rs232obj.put("5557124", "00.00");
							rs232obj.put("6308790","00.00");
							rs232obj.put("6308792","00.00");
							rs232obj.put("6308794","00.00");
							rs232obj.put("6308796","00.00");
						}
						rs232arr.put(rs232obj);
log.info(rs232arr.toString());
						Devicemaster dm = devicemasterRepository.findOne(device.getDeviceid());
						jo.put("Digital", digitaljsonarr);
						jo.put("Analog", rs232arr);   
						jo.put("Rs232", analogjsonarr);
						jo.put("DeviceName", dm.getDevicename());
						jo.put("METER_ID", msgary[3]);
						Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());

						if (DatatypeConverter.printHexBinary(msgary[0].substring(0, 2).getBytes("US-ASCII"))
								.equalsIgnoreCase("2001")) {
							
							Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
									insertingdate, new Date(),
									new ObjectMapper().readValue(jo.toString(), Map.class),
									new ObjectMapper().readValue(gpsjo.toString(), Map.class));

							if (track == null)
							{
								lasttrackrepository.saveAndFlush(lTrack);
							}
							else {
								track.setDeviceDate(insertingdate);
								track.setSystemDate(new Date());
								track.setAnalogdigidata(new ObjectMapper().readValue(jo.toString(), Map.class));
								lasttrackrepository.saveAndFlush(track);
							}
							History hist =null;
									if (track == null) {
							 hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
									new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
									(Map<String, Object>) new ObjectMapper().readValue(gpsjo.toString(), GPS.class));
									}else
									{
										 hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
													new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
													new ObjectMapper().readValue(gpsjo.toString(), Map.class));
									}
							
							histroyrepository.saveAndFlush(hist);

						} else {
							JSONObject Ljo21 = new JSONObject(track.getAnalogdigidata());
							History hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
									new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
									new ObjectMapper().readValue(Ljo21.toString(), Map.class));
							histroyrepository.saveAndFlush(hist);
						}

					}
				}else
					log.info("EnergyMeter Device Not FOund");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else {
			
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
}

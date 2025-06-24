package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.GPS;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class ECONTAB {
	
	@Autowired
	LasttrackRepository lasttrackrepository;

	@Autowired
	DevicemasterRepository devicemasterRepository;
	
	@Autowired
	HistoryRepository histroyrepository;

	@Autowired
	DevicemasterRepository deviceReop;
	
	@Autowired
	ParameterRepository parameterrepository;
	
	DecimalFormat twoDForm = new DecimalFormat("#.##");
	
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	
	private static final Logger log = Logger.getLogger(ECONTAB.class);
	
		@RequestMapping(value = "/api/TestEcon", method = RequestMethod.GET)
	public String TestEcon() throws JsonParseException, JsonMappingException, ParseException, IOException {
			//String msg= "ATL865293041088290,$GPRMC,183612.000,A,3043.5308,N,07640.8346,E,0000,195.67,290921,,,A*74,#01111011000100,0,0,0,92.71442,0.0@0,3.96,26,404,2,fbf,e58a,0,,1.1_enr_mtr,,airteliot.com,ATL_";
		//	String msg="ATL865293041088290,051021,053106,1,016385:0000,016386:0000,016387:0800,016388:00F1,016389:00F3,016390:00F1,016391:0000,016392:0000,016393:0000,016394:000C,016395:000C,016396:000B,016397:D9ED,016398:0000,016399:06F5,016400:XXXX,016401:0108,016402:002A,016403:1CB6,016404:002A,016405:0085,016406:0000,016407:00FA,016408:001D,016409:001E,016410:001B,016411:0000,016412:0000,016413:0000,016414:0000,016415:0003,016416:000D,,,,,,,,,ATL#";  
			String msg="ATL865293041088290,061021,091331,1,016385:0000,016386:0000,016387:0800,016388:00F5,016389:00F6,016390:00F5,016391:0000,016392:0000,016393:0000,016394:000C,016395:000D,016396:000B,016397:DACE,016398:0000,016399:06F6,016400:XXXX,016401:0108,016402:0030,016403:1CD2,016404:0014,016405:0085,016406:0000,016407:00FA,016408:001D,016409:001E,016410:001B,016411:0000,016412:0000,016413:0000,016414:0000,016415:0003,016416:000D,016417:0035,016418:002A,016419:0000,,,,,,ATL";
			ObjectMapper mapper = new ObjectMapper();
		 	log.info("WP30CRS485:: Msg :Len: "+msg);
			String[] msgary = msg.split(",");
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			String imei=msgary[0].substring(5);
			//log.info("WP30CRS485 "+imei+" IMEI :: "+imei);
			String datestr=msgary[10]+msgary[2];
			//log.info("WP30CRS485 "+imei+" DATE :: "+datestr);
			//ATL865293041088290,$GPRMC,124701.000,A,3043.5136,N,07640.8141,E,0000,47.35,051021,,,A*4F,#01111011000100,0,0,0,109.3171,0.0@0,3.96,23,404,2,fbf,e58a,0,,1.1_enr_mtr,,airteliot.com,ATL

			if (msgary[1].contains("$GPRMC")) {
			
	}else
	{
			String Digital_Alarm_1=msgary[4].split(":")[1];
			String Digital_Alarm_2=msgary[5].split(":")[1];
			String Solid_State_O_P_3=msgary[6].split(":")[1];
			
			String Alarm_1_binary = parseHexBinary(Digital_Alarm_1);
			String Alarm_2_binary =parseHexBinary(Digital_Alarm_2);
			String Solid_State_binary =parseHexBinary(Solid_State_O_P_3);
			
			System.out.println("Binary Value is : " + Alarm_1_binary);
			System.out.println("Binary Value is : " + Alarm_2_binary);
			System.out.println("Binary Value is : " + Solid_State_binary);
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
			System.out.println("CommaSeparatedMsg : " + CommaSeparatedMsg);
			
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
			 Devicemaster device = devicemasterRepository.findByImei(msgary[0].substring(5));
			 
			  
				Calendar cal = Calendar.getInstance();
				
				if( msgary[2].toString().length()==6)
				{
					cal.setTime(sdf.parse(msgary[1] + msgary[2]));
					log.info("EnergyMeter ANALOG "+device.getImei()+" DATE "+msgary[1] + msgary[2]);
				}
					else if( msgary[2].toString().length()==5) {
					cal.setTime(sdf.parse(msgary[1] + msgary[2]+"0"));
					
					log.info("EnergyMeter ANALOG "+device.getImei()+" DATE "+msgary[1] + msgary[2]+"0");
					}
					else
						log.info("EnergyMeter ANALOG "+device.getImei()+" Invalid Date");   
				
				
				
				cal.add(Calendar.HOUR, 5);
				cal.add(Calendar.MINUTE, 30);
				Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
				log.info("EnergyMeter ANALOG "+device.getImei()+" DATE "+insertingdate);
				
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
										String.valueOf(Double.valueOf(twoDForm.format((Long.parseLong(hexDataArray[d.intValue()], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue()));
							} else {
								analogobj.put(obj.get("Analoginput").toString(), "00.00");
							}
							analogjsonarr.put(analogobj);
						} 
						
					} catch (Exception e2) {
						e2.printStackTrace();
						log.info("EnergyMeter ANALOG "+device.getImei()+" "+e2.getMessage());
						log.info("EnergyMeter ANALOG "+device.getImei()+" "+e2.getStackTrace());
						log.info("EnergyMeter ANALOG "+device.getImei()+" (Index Out of Bound) ==> Registers are not match with profile");
					}
					System.out.println(analogjsonarr.toString());
					
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogjsonarr);   
					jo.put("METER_ID", msgary[3]);
					Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
					
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
					History hist= new History(device.getDeviceid(), device.getUserId(), insertingdate,
							new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
							(Map<String, Object>) new ObjectMapper().readValue(gpsjo.toString(), Map.class));
					histroyrepository.saveAndFlush(hist);
				}
			 
			 
			 
			 
			 
			 System.out.println("CommaSeparatedMsg : " + CommaSeparatedMsg);
			 
			 
	}
			return datestr;
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

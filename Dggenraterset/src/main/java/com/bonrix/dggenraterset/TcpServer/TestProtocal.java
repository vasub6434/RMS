package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Tools.StringTools;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.math.BigDecimal;

public class TestProtocal {

	@Autowired
	@Qualifier("devicemasterRepository")
	static
	DevicemasterRepository devicemasterRepository;
	
	static Devicemaster device = null;
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, ParseException {

		NumberFormat formatter = new DecimalFormat("#0.000");
		Calendar cal = Calendar.getInstance();
		double speed = 0.0D;
		String speed1 = "0";
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		//	String msg = st.nextToken().toString();
			//String msg="2353503100C80064313233343536373839303132333435363738393000E5524452494E4455533031333031373335343031303138000040004200000000001088001A01DE0258004001DE000000000221019B00A60072002C005700000000000000000000000000000000000000000000000000000000000000000000000000000000032A00000000000000000000000000000000000000000000000000000000000000000000000000000000000000004400C0000000000000000000404000000000000000000000413F000040A0000040C00000414000000000000000000000431200002E";
			//String msg="2353503100C80064313233343536373839303132333435363738393000E5524452494E4455533031333131353538333831303138000040004200000000001088001A01DF0258003A01DF000000000221019800A400670028006500000000000000000000000000000000000000000000000000000000000000000000000000000000033400000000000000000000000000000000000000000000000000000000000000000000000000000000000000004402000000000000000000004040000000000000000000004198000040C0000040E00000415000000000000000000000431600002E";
			String msg="23535031003F00643132333435363738393031323334353637383930003F54414841534F4E30323931323135323133393032323000000000000600000000303F0000000002583F2C000000000000013F013F0000003F0127000000000000000100030001000000000000000000000000000000000000000000000000000000000000033F003F003F00000000460A2C00000000000000000000000000443F466700000000000000000000000000000000463F3F00443F3F000000000045193F00000000000000000041500000453F280046423F003F3F00000000000000000000463F02002E";
			if (!msg.trim().equals("")) {
			//	msg = msg.substring(2);
				// System.out.println("SAM:::MSG:::::::::::"+msg);
				//Devicemaster device1 = devicemasterRepository.findByImei("861693031524309");

				String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

				//System.out.println("parameters Length :: "+jsonArray.toString());
				
				//JSONObject parameterss = new ObjectMapper().readValue(jsonArray.toString(),JSONObject.class);
				
			//	System.out.println("parameters Length :: "+parameterss.length());
				String imeinumber = msg.substring(60,80);
				System.out.println(convertHexToString(imeinumber));
				 System.out.println("SAJAN ::: imeinumber::::::::::: "+imeinumber);
				 
				 //RDRINDUS01
				// Devicemaster device = devicemasterRepository.findByImei(convertHexToString(imeinumber));

				//	System.out.println("Last Track :: "+device.getImei());
					
				 String datet = msg.substring(80,104);
				System.out.println("SAJAN ::: datet ::::::::::: "+convertHexToString(datet));
				
				System.out.println("SAJAN ::: MSG ::::::::::: "+msg);
				//msg = msg.substring(104,140);
				String BATTERY_CURRENT=msg.substring(136,140);
				System.out.println("SAJAN ::: datet ::::::::::: "+Integer.parseInt(BATTERY_CURRENT, 16));
				
				String LOAD_CURRENT_3=msg.substring(164,168);
				System.out.println("SAJAN ::: LOAD_CURRENT_3 ::::::::::: "+Integer.parseInt(LOAD_CURRENT_3, 16));
				
				String LOAD_CURRENT_4=msg.substring(168,172);
				System.out.println("SAJAN ::: LOAD_CURRENT_4 ::::::::::: "+Integer.parseInt(LOAD_CURRENT_4, 16));
				
				
				String RESERVE=msg.substring(172,176);
				System.out.println("SAJAN ::: RESERVE ::::::::::: "+Integer.parseUnsignedInt(RESERVE,16));
				
				String GEN_VOLATGE_B=msg.substring(212,216);
				System.out.println("SAJAN ::: GEN_VOLATGE_B ::::::::::: "+Integer.parseUnsignedInt(GEN_VOLATGE_B,16));
				
				String GEN_CURRENT_R=msg.substring(216,220);
				System.out.println("SAJAN ::: GEN_CURRENT_R ::::::::::: "+Integer.parseUnsignedInt(GEN_CURRENT_R,16));
				
				
				String ROOM_TEMP=msg.substring(248,252);
				System.out.println("SAJAN ::: ROOM_TEMP ::::::::::: "+Integer.parseUnsignedInt(ROOM_TEMP,16));
				
				
				String BATTERY_TEMP=msg.substring(252,256);
				System.out.println("SAJAN ::: BATTERY_TEMP ::::::::::: "+Integer.parseUnsignedInt(BATTERY_TEMP,16));
				
				String RESERVE1=msg.substring(256,260);
				System.out.println("SAJAN ::: RESERVE1 ::::::::::: "+Integer.parseUnsignedInt(RESERVE1,16));
				
				String GEN_BATTERY_VOLTAGE=msg.substring(260,264);
				System.out.println("SAJAN ::: GEN_BATTERY_VOLTAGE ::::::::::: "+Integer.parseUnsignedInt(GEN_BATTERY_VOLTAGE,16));
				
				String MAINS_RUN_HRS=msg.substring(264,268);
				System.out.println("SAJAN ::: MAINS_RUN_HRS ::::::::::: "+Integer.parseUnsignedInt(MAINS_RUN_HRS,16));
				
				String SITE_BATTERY_VOLTAGE_DC = msg.substring(156,160);
				//String SITE_BATTERY_VOLTAGE_DC = msg.substring(160,164);
				System.out.println("BONRIX ::: "+convertHexToString(imeinumber)+" Site_Battery_voltage_DC ::::::::::: " + formatter.format(new BigDecimal(new BigInteger(String.valueOf(Integer.parseInt(SITE_BATTERY_VOLTAGE_DC, 16))),1)));
				System.out.println("BONRIX ::: "+convertHexToString(imeinumber)+" Site_Battery_voltage_DC ::::::::::: " + new BigInteger(String.valueOf(Integer.parseInt(SITE_BATTERY_VOLTAGE_DC, 16))));

				
			/*	msg = msg.substring(5);
				boolean dig4 = false;
				
				String msgType = msg.substring(80,104);

				if ((!msgType.equals("BO012")) && (!msgType.equals("BR001"))) {
					msg = msg.substring(14);
				} else {
					dig4 = true;
				}
				if (msgType.equals("BR001")) {
					msg = "1" + msg;
				}
				String year = msg.substring(0, 2);
				String month = msg.substring(2, 4);
				String date = msg.substring(4, 6);

				msg = msg.substring(6);
				boolean dig5 = msg.substring(0, 1).equals("A");
				msg = msg.substring(1);
				String latitude = msg.substring(0, 9);

				msg = msg.substring(9);
				// String latDirection = msg.substring(0, 1);

				msg = msg.substring(1);
				String longitude = msg.substring(0, 10);

				msg = msg.substring(10);
				// String lonDirection = msg.substring(0, 1);
				msg = msg.substring(1);
				speed1 = msg.substring(0, 5);

				msg = msg.substring(5);
				String hour = msg.substring(0, 2);
				String min = msg.substring(2, 4);
				String sec = msg.substring(4, 6);

				String datetime = date + month + year + hour + min + sec;

				msg = msg.substring(6);
				String orientAngle = msg.substring(0, 6);

				Double angle = Double.valueOf(0.0D);
				try {
					angle = Double.valueOf(Double.parseDouble(orientAngle));
				} catch (Exception ex) {
					// log.error("invalid ANGLE:" + ex);
					angle = Double.valueOf(0.0D);
				}
				msg = msg.substring(6);

				String digitalInput = "";
				if (!msgType.equals("BO012")) {
					digitalInput = msg.substring(0, 8);
				} else {
					digitalInput = "00000000";
				}
				msg = msg.substring(8);
				msg = msg.substring(1);

				 Long odometer = Long.valueOf(Long.valueOf(msg, 16).longValue()); 
				Long odometer = (long) 0.0;

				cal.setTime(sdf.parse(datetime));
				cal.add(12, 30);
				cal.add(10, 5);
				speed = Double.parseDouble(speed1);

				double finalodometer = 0.0D;
				try {
					finalodometer = odometer.doubleValue();
				} catch (NumberFormatException nf) {
					// log.error("NFEXEPTION" + nf);
					finalodometer = 0.0D;
				}

				DeviceProfile dp = device.getDp();
				JSONObject jo = new JSONObject();
				JSONArray digitaljsonarr = new JSONArray();
				JSONObject parameters = new ObjectMapper().readValue(dp.getParameters().toString(),
						JSONObject.class);
				JSONArray digitals =parameters.getJSONArray("Digital");
				for (int i = 0; i < digitals.length(); i++) {
					JSONObject obj = (JSONObject) digitals.get(i);
					JSONObject digiobj = new JSONObject();
					if (obj.get("parametername").toString().equalsIgnoreCase("power")) {
						boolean dig1 = false;
						if (digitalInput.substring(0, 1).equals("1")) {
							dig1 = !((Boolean) obj.get("reverse"));
						} else {
							dig1 = ((Boolean) obj.get("reverse"));
						}
						digiobj.put("power", dig1);
					} else if (obj.get("parametername").toString().equalsIgnoreCase("Ignition")) {
						boolean dig2 = false;
						if (digitalInput.substring(1, 2).equals("1")) {
							dig2 = !((Boolean) obj.get("reverse"));
						} else {
							dig2 = ((Boolean) obj.get("reverse"));
						}
						digiobj.put("Ignition", dig2);
					} else if (obj.get("parametername").toString().equalsIgnoreCase("AC")) {
						boolean dig3 = false;
						if (digitalInput.substring(2, 3).equals("1")) {
							dig3 = !((Boolean) obj.get("reverse"));
						} else {
							dig3 = ((Boolean) obj.get("reverse"));
						}
						digiobj.put("AC", dig3);
					} else if (obj.get("parametername").toString().equalsIgnoreCase("SOS")) {
						if (dig4) {
							dig4 = !((Boolean) obj.get("reverse"));
						} else {
							dig4 = ((Boolean) obj.get("reverse"));
						}
						digiobj.put("SOS", dig4);
					} else if (obj.get("parametername").toString().equalsIgnoreCase("GPS")) {
						if (dig5) {
							dig5 = !((Boolean) obj.get("reverse"));
						} else {
							dig5 = ((Boolean) obj.get("reverse"));
						}
						digiobj.put("GPS", dig5);
					}
					digitaljsonarr.put(digiobj);
				}

				JSONArray analogjsonarr = new JSONArray();
				JSONArray analogs =  parameters.getJSONArray("Analog");
				for (int i = 0; i < analogs.length(); i++) {

					JSONObject obj = (JSONObject) analogs.get(i);
					JSONObject analogobj = new JSONObject();
					if (obj.get("analogname").toString().equalsIgnoreCase("Fuel")) {
						String analogParam = digitalInput.substring(5, 8);
						double analogValue = 0.0D;
						try {
							analogValue = Integer.parseInt(analogParam.substring(0, 1)) * 16 * 16;
							analogValue += Integer.valueOf(analogParam.substring(1, 2), 16).intValue() * 16;
							analogValue += Integer.valueOf(analogParam.substring(2, 3), 16).intValue();
						} catch (Exception ex) {
							analogValue = 0.0D;
						}
						analogobj.put("Fuel", analogValue);
					}
					analogjsonarr.put(analogobj);
				}

				JSONArray rs232arr = new JSONArray();
				JSONArray rs232 = parameters.getJSONArray("Rs232");
				for (int i = 0; i < rs232.length(); i++) {
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
				}
				jo.put("Digital", digitaljsonarr);
				jo.put("Analog", analogjsonarr);
				jo.put("Rs232", rs232arr);

				String latitudes = StringTools.parseLatitude(latitude, "N") + "";
				String longitudes = StringTools.parseLatitude(longitude, "E") + "";
				if ((StringTools.roundEightDecimals(Double.parseDouble(latitudes))).equals("0.0")
						|| (StringTools.roundEightDecimals(Double.parseDouble(longitude)).equals("0.0"))
						|| (StringTools.roundEightDecimals(Double.parseDouble(latitudes)).contains("0000"))
						|| (StringTools.roundEightDecimals(Double.parseDouble(longitudes)).contains("0000"))
						|| (StringTools.roundEightDecimals(Double.parseDouble(latitude)).equals("0"))
						|| (StringTools.roundEightDecimals(Double.parseDouble(longitudes)).equals("0"))) {
					System.out.println("SAM:::worng data" + device.getDevicename());
				} else {
					angle = Double.valueOf(Double.parseDouble(orientAngle));
					JSONObject obj=new JSONObject();
					obj.put("latitude", latitudes);
					obj.put("longitude", longitudes);
					obj.put("speed", speed);
					obj.put("angle", angle);
					obj.put("odometer", finalodometer);
				
				}*/
			}
		
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


}

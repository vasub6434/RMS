package com.bonrix.dggenraterset.Oprations;

import java.io.IOException;
/*import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;*/
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/*import java.util.MissingResourceException;
import java.util.ResourceBundle;*/
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.utils.BonrixConstants;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Tools.CoordinatesConverter;
import com.bonrix.dggenraterset.Tools.StringConverter;
import com.bonrix.dggenraterset.Tools.StringTools;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class GT06 {

	private static final Logger log = Logger.getLogger(GT06.class);

	LasttrackServices lasttrackServices = ApplicationContextHolder.getContext().getBean(LasttrackServices.class);
	
	DevicemasterServices Deviceinfoservices= ApplicationContextHolder.getContext().getBean(DevicemasterServices.class);
	
	HistoryServices historyServices=ApplicationContextHolder.getContext().getBean(HistoryServices.class);


	@SuppressWarnings({ "unchecked", "unused" })
	public int parseGT06(String msg, String IMEI_NUM, String Status, String acStatus)
			throws ParseException, NullPointerException, JsonParseException, JsonMappingException, IOException {
		final SimpleDateFormat sdf = new SimpleDateFormat(BonrixConstants.GT06_DATE_TIME_FORMATE);
		Calendar cal = Calendar.getInstance();
		String latitude;
		String longitude;
		
		double speed = 0.0D;
		// log.info("SAM::SAM:: Msg: " + msg);
		String stt = msg;
		msg = msg.substring(6);
		String protocol = null;
		if (stt.contains("7878")) {
			protocol = msg.substring(0, 2);
		} else {
			log.info("PROtocol:: " + msg.substring(0, 2));
			protocol = msg.substring(2, 4);
		}

		msg = msg.substring(2);
		 log.info("SAM::imei is " + IMEI_NUM);
		Devicemaster device = Deviceinfoservices.findByImei(IMEI_NUM.substring(1));
		if (device != null) {
			Lasttrack lasttrack = lasttrackServices.findOne(device.getDeviceid());
			if ((protocol.equals("12")) || (protocol.equals("10")) || (protocol.equals("22") || protocol.equals("94"))) {
				String msg13 = "";
				log.info("SAM::Protocol No" + acStatus.substring(6, 8) + " AC Status:::" + acStatus.substring(20, 22));
				msg13 = Status.substring(8);

				// log.info("SAM::Msg13::: " + msg13);
				// 44
				// 44 ->dec->68->binary->1000100
				// -> (1bit) 1-gps
				// (3bit) 100: SOS 011: Low Battery Alarm 010: Power Cut Alarm 001: Shock Alarm
				// 000: Normal
				// 1 bit 1 power Charge
				// 1 bit 0 ignition
				int contentinfo = Integer.parseInt(msg13.substring(0, 2), 16);
				 log.info("SAM::contentinfo:: " + contentinfo);
				int gsmsignal = Integer.parseInt(msg13.substring(4, 6), 16);
				String convbin = new StringConverter().convertto8bit(contentinfo + "");
				 log.info("SAM::convertedbinary::" + convbin + ":::" + IMEI_NUM + "::" + new Date());
					DeviceProfile dp = device.getDp();
					JSONObject jo = new JSONObject();
					JSONArray digitaljsonarr = new JSONArray();
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
					mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
					JSONObject parameters = new JSONObject(dp.getParameters());
					System.out.println("JSONObject::"+parameters.toString());
					System.out.println("DIGITAL::"+parameters.get("Digital").toString());
					JSONArray digitalsarry = parameters.getJSONArray("Digital");
					for (int i = 0; i < digitalsarry.length(); i++) {
						JSONObject obj = (JSONObject) digitalsarry.get(i);
						JSONObject digiobj = new JSONObject();
						if (obj.get("parametername").toString().equalsIgnoreCase("power")) {
							boolean dig1 = false;
							if (convbin.substring(2, 4).equals("011") || convbin.substring(2, 4).equals("010")
									|| convbin.substring(2, 4).equals("001")) {
								dig1 = obj.getBoolean("reverse");
								log.info("POWER true");
							} else {
								dig1 = !obj.getBoolean("reverse");
							}
							digiobj.put("power", dig1);
						} else if (obj.get("parametername").toString().equalsIgnoreCase("Ignition")) {
							boolean dig2 = false;
							if (convbin.substring(6, 7).equals("1")) {
								dig2 = !obj.getBoolean("reverse");
								log.info("Ignition true");
							} else {
								dig2 = obj.getBoolean("reverse");
							}
							digiobj.put("Ignition", dig2);
						} else if (obj.get("parametername").toString().equalsIgnoreCase("AC")) {
							boolean dig3 = false;
							if (acStatus.substring(6, 8).equals("69") && acStatus.substring(20, 22).equals("00")) {
								dig3 = !obj.getBoolean("reverse");
								log.info("AC true");
							} else {
								dig3 = obj.getBoolean("reverse");
							}
							digiobj.put("AC", dig3);
						} else if (obj.get("parametername").toString().equalsIgnoreCase("SOS")) {
							boolean dig4=false;
							if (convbin.substring(2, 4).equals("100")) {
								dig4 = !obj.getBoolean("reverse");
								log.info("SOS true");
							} else {
								dig4 = obj.getBoolean("reverse");
							}
							digiobj.put("SOS", dig4);
						} else if (obj.get("parametername").toString().equalsIgnoreCase("GPS")) {
							boolean dig5=true;
							if (convbin.substring(1, 2).equals("1")) {
								log.info("GPS "+ obj.getBoolean("reverse"));
								dig5 = !obj.getBoolean("reverse");
								log.info("GPS true");
							} else {
								dig5 = obj.getBoolean("reverse");
							}
							digiobj.put("GPS", dig5);
						}
						digitaljsonarr.put(digiobj);
					}

					JSONArray analogjsonarr = new JSONArray();
					JSONArray analogsarry =parameters.getJSONArray("Analog");
					for (int i = 0; i < analogsarry.length(); i++) {
						JSONObject obj = (JSONObject) analogsarry.get(i);
						JSONObject analogobj = new JSONObject();
						analogjsonarr.put(analogobj);
					}

					JSONArray rs232arr = new JSONArray();
					JSONArray rs232arry = parameters.getJSONArray("Rs232");
					for (int i = 0; i < rs232arry.length(); i++) {
						JSONObject obj = (JSONObject) rs232arry.get(i);
						JSONObject rs232obj = new JSONObject();
						if (obj.get("parametername").toString().equalsIgnoreCase("rs232")) {
							boolean rs2321 = false;
							if (rs2321) {
								rs2321 =!obj.getBoolean("reverse");
							} else {
								rs2321 =obj.getBoolean("reverse");
							}
							rs232obj.put("rs232", rs2321);
						}
						rs232arr.put(rs232obj);
					}
					jo.put("Digital", digitaljsonarr);
					jo.put("Analog", analogjsonarr);
					jo.put("Rs232", rs232arr);
		

				// log.info("SAM::voltage:::" + ana3);
				String info = msg.substring(0, 12);
				msg = msg.substring(12);

				String year = Integer.valueOf(info.substring(0, 2), 16).intValue() + " ";
				String month = Integer.valueOf(info.substring(2, 4), 16).intValue() + "-";
				String date = Integer.valueOf(info.substring(4, 6), 16).intValue() + "-";

				String hour = Integer.valueOf(info.substring(6, 8), 16).intValue() + ":";
				String min = Integer.valueOf(info.substring(8, 10), 16).intValue() + ":";
				String sec = Integer.valueOf(info.substring(10, 12), 16).intValue() + "";

				String datetime = date + month + year + hour + min + sec;
				cal.setTime(sdf.parse(datetime));
				cal.add(Calendar.HOUR, 5);
				cal.add(Calendar.MINUTE, 30);

				// log.info("SAM::DateTime : " + cal.getTime());
				String gpsInfo = msg.substring(0, 24);
				msg = msg.substring(24);

				latitude = gpsInfo.substring(2, 10);
				latitude = Integer.valueOf(latitude, 16).intValue() + "";
				double dbl_lat = Double.parseDouble(latitude) / 30000.0D;
				dbl_lat -= Integer.parseInt(latitude) / 30000;
				String str_lat = dbl_lat + "";
				str_lat = str_lat.substring(2, str_lat.length());

				latitude = (String
						.format("%2s", new Object[] { Integer.valueOf(Integer.parseInt(latitude) / 30000 / 60) })
						.replace(' ', '0')
						+ String.format("%2s",
								new Object[] { Integer.valueOf(Integer.parseInt(latitude) / 30000 % 60) })
								.replace(' ', '0')
						+ str_lat);

				longitude = gpsInfo.substring(10, 18);
				longitude = Integer.valueOf(longitude, 16).intValue() + "";

				speed = Double.parseDouble(Integer.valueOf(gpsInfo.substring(18, 20), 16).intValue() + "");
				if (speed < 5.0D)
					speed = 0.0D;
				
				// log.info("SAM::sppeed from gpsinfo ::" + speed);

				// String statusBits = gpsInfo.substring(20, 24);

				String latd = gpsInfo.substring(20, 22);
				String latdc = new StringConverter().hexto8bit(latd);
				double dbl_lon = Double.parseDouble(longitude) / 30000.0D;
				dbl_lon -= Integer.parseInt(longitude) / 30000;
				String str_lon = dbl_lon + "";
				str_lon = str_lon.substring(2, str_lon.length());

				String longdir = "W";
				String latdir = "N";
				if (latdc.substring(4, 5).equals("0")) {
					longdir = "E";
				}
				if (latdc.substring(5, 6).equals("0")) {
					latdir = "S";
					longitude = (String
							.format("%2s", new Object[] { Integer.valueOf(Integer.parseInt(longitude) / 30000 / 60) })
							.replace(' ', '0')
							+ String.format("%2s",
									new Object[] { Integer.valueOf(Integer.parseInt(longitude) / 30000 % 60) })
									.replace(' ', '0')
							+ str_lon);

				} else {
					longitude = ("0"
							+ String.format("%2s",
									new Object[] { Integer.valueOf(Integer.parseInt(longitude) / 30000 / 60) })
									.replace(' ', '0')
							+ String.format("%2s",
									new Object[] { Integer.valueOf(Integer.parseInt(longitude) / 30000 % 60) })
									.replace(' ', '0')
							+ str_lon);

				}
				List<Double> latlong = CoordinatesConverter.passLetLongVlaues(latitude, longitude, latdir, longdir);

				int statusBits2 = Integer.parseInt(gpsInfo.substring(20, 24), 16);
				// log.info("SAM::gpsInfo.substring(20, 24) " + gpsInfo.substring(20, 24) + "
				// Gps info:: " + gpsInfo);
				String statusbin = Integer.toBinaryString(statusBits2);
				// log.info("SAM::statusbin" + statusbin + " Length:: " + statusbin.length());
				String angle = "0.0";
				try {
					angle = Integer.parseInt(statusbin.substring(statusbin.length() - 10), 2) + "";
				} catch (Exception e) {
					log.error("Angle error");
					// e.printStackTrace();
				}

				// int vald = Integer.parseInt(gpsInfo.substring(1, 2), 16);
				// log.info("SAM::Total GPS satalites :: " + vald);

				// log.info("SAM::LASTLATITUDEIS:::" + StringTools.roundEightDecimals(((Double)
				// latlong.get(0)).doubleValue()));
				// log.info("SAM::LASTLONGITUDEIS:::" + StringTools.roundEightDecimals(((Double)
				// latlong.get(1)).doubleValue()));
				if ((StringTools.roundEightDecimals(((Double) latlong.get(0)).doubleValue()).equals("0.0"))
						|| (StringTools.roundEightDecimals(((Double) latlong.get(1)).doubleValue()).equals("0.0"))
						|| (StringTools.roundEightDecimals(((Double) latlong.get(0)).doubleValue()).contains("0000"))
						|| (StringTools.roundEightDecimals(((Double) latlong.get(1)).doubleValue()).contains("0000"))
						|| (StringTools.roundEightDecimals(((Double) latlong.get(0)).doubleValue()).equals("0"))
						|| (StringTools.roundEightDecimals(((Double) latlong.get(1)).doubleValue()).equals("0"))) {
					log.info("SAM::invalid data of Device:" + device.getDevicename() + ":::::"
							+ StringTools.roundEightDecimals(((Double) latlong.get(0)).doubleValue()) + "::::"
							+ StringTools.roundEightDecimals(((Double) latlong.get(1)).doubleValue()) + ":::"
							+ device.getImei());
					return 1;
				}

				JSONObject obj=new JSONObject();
				obj.put("latitude", latlong.get(0) + "");
				obj.put("longitude", latlong.get(1) + "");
				obj.put("speed", speed);
				obj.put("angle", angle);
				obj.put("odometer", 0.0);
				if(lasttrack!=null)
				obj.put("lastmove",  speed > 5.0D ? new SimpleDateFormat(BonrixConstants.SQL_DATE_TIME_FORMATE).format(new Date()) : lasttrack.getGpsdata().get("lastmove")).toString();
				else 
			    obj.put("lastmove",  new SimpleDateFormat(BonrixConstants.SQL_DATE_TIME_FORMATE).format(new Date()));
				//Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
				History hist = new History(device.getDeviceid(), device.getUserId(),cal.getTime(),new Date(),new ObjectMapper().readValue(jo.toString(),Map.class),new ObjectMapper().readValue(obj.toString(),Map.class));
				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),cal.getTime(),new Date(),new ObjectMapper().readValue(jo.toString(),Map.class),new ObjectMapper().readValue(obj.toString(),Map.class));
				
				log.info("SAM:::mSG: " + lTrack.toString());
				historyServices.saveAndFlush(hist);
				lasttrackServices.saveAndFlush(lTrack);
			} else {
					log.error("not insert in LASTTRACK::::" + device.getDevicename());
				}
			} else {
				log.error("Something Wrong In GT06V3 DATA");
			}
		return 1;
	}

	public static void main(String[] args) throws ParseException {
		// "787822220E080D0C1A2DC602789E4007C8E2680014020194183A9C000DAF00000000EA64CF0D0A",

		try {
			new GT06().parseGT06("78781f12110705090a1dc902000000000000000000000000000000000000000000000000",
					"035548802053686", "78780a1340066400012467de160d0a", "78780a1340066400012467de160d0a");
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
		// 78780a13 40 066400012467de160d0a
		// /78780a13 45 002e00010312f1d40d0a
	}

}

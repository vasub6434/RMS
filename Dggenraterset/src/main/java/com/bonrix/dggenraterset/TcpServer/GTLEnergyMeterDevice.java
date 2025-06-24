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

import javax.xml.crypto.MarshalException;
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
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.bonrix.dggenraterset.jobs.MyAnalogAlert;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GTLEnergyMeterDevice {

	private static final Logger log = Logger.getLogger(GTLEnergyMeterDevice.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");

	public static class HandlerGTLEnergyMeterDevice extends SimpleChannelUpstreamHandler {

		LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);

		ParameterRepository parameterrepository = ApplicationContextHolder.getContext()
				.getBean(ParameterRepository.class);
		DecimalFormat twoDForm = new DecimalFormat("#.##");

		@SuppressWarnings("unused")
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws ParseException, JsonParseException, JsonMappingException, IOException, BonrixException {
			String msg = (String) e.getMessage();
			ObjectMapper mapper = new ObjectMapper();

			String[] msgary = msg.split(",");
			String imei = msgary[0].substring(5);
			log.info("GTLEnergyMeterDevice " + imei + " : " + msg);

			if (msgary[1].contains("$GPRMC")) {
				String[] gpdAnalog = { "6387981", "6387982" };
				List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
				String digitalData = msgary[14];
				boolean Isvalid = "A".equals(msgary[3]);

				if (Isvalid == true || Isvalid == false) {
					JSONArray analogjsonarr = new JSONArray();
					JSONArray digitaljsonarr = new JSONArray();

					Devicemaster device = devicemasterRepository.findByImei(imei);
					DeviceProfile profile = device.getDp();
					JSONObject parameters = new JSONObject(profile.getParameters());
					JSONArray digital = parameters.getJSONArray("Digital");
					JSONArray analog = parameters.getJSONArray("Analog");
					double analogFormula = 1;
					for (int i = 0; i < analog.length(); i++) {// 1.3
						JSONObject obj = (JSONObject) analog.get(i);
						if (obj.get("Analoginput").toString().equalsIgnoreCase("6387981"))
							analogFormula = Double.parseDouble(obj.get("Analogformula").toString());
					}

					String ACMAINS_FAIL = digitalData.substring(1, 2);
					String Fire = digitalData.substring(2, 3);
					String Door = digitalData.substring(3, 4);
					String DG_Running_Hrs = digitalData.substring(4, 5);
					String DG_Fault = digitalData.substring(5, 6);
					String Battry_Low = digitalData.substring(6, 7);
					String PP_Input_Fail = digitalData.substring(7, 8);

					for (int i = 0; i < digital.length(); i++) {
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

					JSONObject gjo = new JSONObject();
					gjo.put("latitude", "23.033863");
					gjo.put("longitude", "72.585022");
					gjo.put("DeviceDate", new Date());
					gjo.put("fuel", "0");

					String fuel = msgary[16];
					double battry = Double.parseDouble(msgary[15].toString()) * analogFormula;
					Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
					if (track != null) {
						JSONObject jo = new JSONObject();
						JSONObject analogJsonObject = new JSONObject(
								(mapper.writeValueAsString(track.getAnalogdigidata())));
						JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
						JSONArray AnalogDataArray = new JSONArray(analogArray.toString());
						boolean isInsert = false;
						for (int i = 0; i < analogArray.length(); i++) {
							JSONObject jsonObj = analogArray.getJSONObject(i);

							String k = jsonObj.keys().next();
							if (gpdAnalogNameList.contains(k)) {
								isInsert = true;
								JSONObject analogObj = new JSONObject();
								if (k.equalsIgnoreCase("6387981"))
									jsonObj.put(k, String.valueOf(battry));
								else if (k.equalsIgnoreCase("6387982"))
									jsonObj.put(k, fuel);
							}
						}
						if (!isInsert) {
							JSONObject analogbattryObj = new JSONObject();
							analogbattryObj.put("6387981", String.valueOf(battry));
							JSONObject analogfuelObj = new JSONObject();
							analogfuelObj.put("6387982", fuel);
							AnalogDataArray.put(analogbattryObj);
							AnalogDataArray.put(analogfuelObj);
						}
						jo.put("Digital", digitaljsonarr);
						jo.put("Analog", analogArray);
						jo.put("DeviceName", device.getDevicename());

						Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(),
								new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
								new ObjectMapper().readValue(gjo.toString(), Map.class),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

						History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
								new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
								new ObjectMapper().readValue(gjo.toString(), Map.class),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

						History hst = histroyrepository.saveAndFlush(hist);
						Lasttrack t = lasttrackrepository.saveAndFlush(lTrack);
						MyAlerts alert = new MyAlerts();
						alert.sendMsg(device, lTrack, track);
						MyAnalogAlert analogAlert = new MyAnalogAlert();
						analogAlert.sendAnalogAlert(device, lTrack);
					} else {
						JSONArray analogArray = new JSONArray();
						for (int i = 0; i < analog.length(); i++) {
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

						Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(),
								new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
								new ObjectMapper().readValue(gjo.toString(), Map.class),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));
						lasttrackrepository.saveAndFlush(lTrack);

						History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
								new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
								new ObjectMapper().readValue(gjo.toString(), Map.class),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

						History hst = histroyrepository.saveAndFlush(hist);
					}
				}
			} else {
				log.info("GTLEnergyMeterDevice " + imei + " Analog :: Analog String");
				Devicemaster device = devicemasterRepository.findByImei(imei);
				JSONArray AnalogJsonArray = new JSONArray();
				JSONArray DigitalJsonArray = new JSONArray();
				String[] analog1 = new String[] { "6387981", "6387982" };
				String[] analog1Param = { "RECTIFIER_OUTPUT_POWER", "RECTIFIER_OUTPUT_VOLTAGE", "BATERY_CAPACITY",
						"BATTERY_CURRENT", "RECTIFIER_OUTPUT_CURRENT", "SITE_BATT_VOLTAGE", "TOTAL_LOAD_CURRENT",
						"LOAD_CURRENT1", "LOAD_CURRENT2", "LOAD_CURRENT3", "LOAD_CURRENT4", "LOAD_CURRENT5",
						"MAINS_VOLTAGE_R", "MAINS_VOLTAGE_Y", "MAINS_VOLTAGE_B", "SOC", "BATTERY_TEMERATURE",
						"Mains_RUN_HRS", "DG_Total_RUNNING_HRS", "BATTERY_RUN_HRS", "RECTIFIER_OUTPUT_ENERGY",
						"TOTAL_LOAD_ENERGY", "LOAD_1_ENERGY", "LOAD_2_ENERGY", "LOAD_3_ENERGY", "LOAD_4_ENERGY",
						"LOAD_5_ENERGY" };

				String[] analog5 = new String[] { "6387981", "6387982" };
				List<String> analog5NameList = new ArrayList<>(Arrays.asList(analog5));
				String[] Digital = new String[] { "284945", "6348798", "291934", "6348854", "6348815", "6348821",
						"6348824" };

				Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
				if (track != null) {
					if (msgary[3].contains("1")) {

						String[] msgStringArray = new String[27];

						String RECTIFIER_OUTPUT_POWER = msgary[5].split(":")[1];
						String RECTIFIER_OUTPUT_VOLTAGE = msgary[6].split(":")[1];
						String BATERY_CAPACITY = msgary[7].split(":")[1];
						String BATTERY_CURRENT = msgary[8].split(":")[1];
						String RECTIFIER_OUTPUT_CURRENT = msgary[9].split(":")[1];
						String SITE_BATT_VOLTAGE = msgary[10].split(":")[1];
						String TOTAL_LOAD_CURRENT = msgary[11].split(":")[1];
						String LOAD_CURRENT1 = msgary[12].split(":")[1];
						String LOAD_CURRENT2 = msgary[13].split(":")[1];
						String LOAD_CURRENT3 = msgary[14].split(":")[1];
						String LOAD_CURRENT4 = msgary[15].split(":")[1];
						String LOAD_CURRENT5 = msgary[16].split(":")[1];
						String MAINS_VOLTAGE_R = msgary[17].split(":")[1];
						String MAINS_VOLTAGE_Y = msgary[18].split(":")[1];
						String MAINS_VOLTAGE_B = msgary[19].split(":")[1];
						String SOC = msgary[20].split(":")[1];
						String BATTERY_TEMERATURE = msgary[21].split(":")[1];
   
						String Mains_RUN_HRS = ieee(msgary[22].split(":")[1] + msgary[23].split(":")[1]);
						String DG_Total_RUNNING_HRS = ieee(msgary[24].split(":")[1] + msgary[25].split(":")[1]);
						String BATTERY_RUN_HRS = ieee(msgary[26].split(":")[1] + msgary[27].split(":")[1]);
						String RECTIFIER_OUTPUT_ENERGY = ieee(msgary[28].split(":")[1] + msgary[29].split(":")[1]);
						String TOTAL_LOAD_ENERGY = ieee(msgary[30].split(":")[1] + msgary[31].split(":")[1]);
						String LOAD_1_ENERGY = ieee(msgary[32].split(":")[1] + msgary[33].split(":")[1]);
						String LOAD_2_ENERGY = ieee(msgary[34].split(":")[1] + msgary[35].split(":")[1]);
						String LOAD_3_ENERGY = ieee(msgary[36].split(":")[1] + msgary[37].split(":")[1]);
						String LOAD_4_ENERGY = ieee(msgary[38].split(":")[1] + msgary[39].split(":")[1]);
						String LOAD_5_ENERGY = ieee(msgary[40].split(":")[1] + msgary[41].split(":")[1]);

						for (int i = 0; i < analog1Param.length; i++) {
							JSONObject analogObj = new JSONObject();

							if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_POWER"))
								analogObj.put("295726687", String
										.valueOf(twoDForm.format(Integer.parseInt(RECTIFIER_OUTPUT_POWER, 16) * 1)));

							if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_VOLTAGE"))
								analogObj.put("295726832",
										String.valueOf(Integer.parseInt(RECTIFIER_OUTPUT_VOLTAGE, 16) * 0.01));

							if (analog1Param[i].equalsIgnoreCase("BATERY_CAPACITY"))
								analogObj.put("295726996", String.valueOf(Integer.parseInt(BATERY_CAPACITY, 16) * 0.1));

							if (analog1Param[i].equalsIgnoreCase("BATTERY_CURRENT"))
								analogObj.put("295727075",
										String.valueOf(twoDForm.format(Integer.parseInt(BATTERY_CURRENT, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_CURRENT"))
								analogObj.put("295727172",
										String.valueOf(Integer.parseInt(RECTIFIER_OUTPUT_CURRENT, 16) * 0.1));

							if (analog1Param[i].equalsIgnoreCase("SITE_BATT_VOLTAGE"))
								analogObj.put("295727268",
										String.valueOf(Integer.parseInt(SITE_BATT_VOLTAGE, 16) * 0.01));

							if (analog1Param[i].equalsIgnoreCase("TOTAL_LOAD_CURRENT"))
								analogObj.put("295727352", String
										.valueOf(twoDForm.format(Integer.parseInt(TOTAL_LOAD_CURRENT, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT1"))
								analogObj.put("295727445",
										String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT1, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT2"))
								analogObj.put("295731584",
										String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT2, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT3"))
								analogObj.put("295727539",
										String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT3, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT4"))
								analogObj.put("295727574",
										String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT4, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT5"))
								analogObj.put("295727645",
										String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT5, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_R"))
								analogObj.put("295727777",
										String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_R, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_Y"))
								analogObj.put("295727843",
										String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_Y, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_B"))
								analogObj.put("295727908",
										String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_B, 16) * 0.1)));

							if (analog1Param[i].equalsIgnoreCase("SOC"))
								analogObj.put("295728006", String.valueOf(Integer.parseInt(SOC, 16) * 1));

							if (analog1Param[i].equalsIgnoreCase("BATTERY_TEMERATURE"))
								analogObj.put("295728090",
										String.valueOf(Integer.parseInt(BATTERY_TEMERATURE, 16) * 0.01));

							if (analog1Param[i].equalsIgnoreCase("Mains_RUN_HRS"))
								analogObj.put("295728182", String.valueOf(Double.parseDouble(Mains_RUN_HRS) * 1));

							if (analog1Param[i].equalsIgnoreCase("DG_Total_RUNNING_HRS"))
								analogObj.put("295728269",
										String.valueOf(Double.parseDouble(DG_Total_RUNNING_HRS) * 1));

							if (analog1Param[i].equalsIgnoreCase("BATTERY_RUN_HRS"))
								analogObj.put("295728363",
										String.valueOf(twoDForm.format(Double.parseDouble(BATTERY_RUN_HRS) * 1)));

							if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_ENERGY"))
								analogObj.put("295728490", String
										.valueOf(twoDForm.format(Double.parseDouble(RECTIFIER_OUTPUT_ENERGY) * 1)));

							if (analog1Param[i].equalsIgnoreCase("TOTAL_LOAD_ENERGY"))
								analogObj.put("295728583", String.valueOf(Double.parseDouble(TOTAL_LOAD_ENERGY) * 1));

							if (analog1Param[i].equalsIgnoreCase("LOAD_1_ENERGY"))
								analogObj.put("295728684",
										String.valueOf(twoDForm.format(Double.parseDouble(LOAD_1_ENERGY) * 1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_2_ENERGY"))
								analogObj.put("295728727",
										String.valueOf(twoDForm.format(Double.parseDouble(LOAD_2_ENERGY) * 1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_3_ENERGY"))
								analogObj.put("295728763",
										String.valueOf(twoDForm.format(Double.parseDouble(LOAD_3_ENERGY) * 1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_4_ENERGY"))
								analogObj.put("295728811",
										String.valueOf(twoDForm.format(Double.parseDouble(LOAD_4_ENERGY) * 1)));

							if (analog1Param[i].equalsIgnoreCase("LOAD_5_ENERGY"))
								analogObj.put("295728870",
										String.valueOf(twoDForm.format(Double.parseDouble(LOAD_5_ENERGY) * 1)));
							AnalogJsonArray.put(analogObj);
						}

						JSONObject jo = new JSONObject();
						JSONObject analogJsonObject = new JSONObject(
								(mapper.writeValueAsString(track.getAnalogdigidata())));
						JSONArray DigitalJsonArray_old = new JSONArray(analogJsonObject.get("Digital").toString());

						JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

						for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
							JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
							String k = jsonObj.keys().next();
							if (analog5NameList.contains(k)) {
								JSONObject analogObj = new JSONObject();
								analogObj.put(k, jsonObj.getString(k));
								AnalogJsonArray.put(analogObj);
							}
						}
						jo.put("Digital", DigitalJsonArray_old);
						jo.put("Analog", AnalogJsonArray);
						jo.put("DeviceName", device.getDevicename());

						Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(),
								new Date(), new ObjectMapper().readValue(jo.toString(), Map.class), track.getGpsdata(),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

						History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
								new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
								track.getGpsdata(),
								new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

						Lasttrack t = lasttrackrepository.saveAndFlush(lTrack);
						History hst = histroyrepository.saveAndFlush(hist);

					}
				}
			}
		}
	}

	public static String ieee(String ieeeVal) {
		Long i = Long.parseLong(ieeeVal, 16);
		return "" + Float.intBitsToFloat(i.intValue());
	}

	static JSONObject convertJson(String json)
			throws org.codehaus.jackson.JsonParseException, JsonMappingException, IOException {

		JSONObject globalJsonObject = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = mapper.readValue(json, Map.class);
		JSONObject jsonObject = new JSONObject(json);
		JSONArray analogArray = (JSONArray) jsonObject.get("Analog");
		JSONArray digitalArray = (JSONArray) jsonObject.get("Digital");
		JSONObject analogJsonObject = new JSONObject();
		for (int i = 0; i < analogArray.length(); i++) {
			JSONObject analogJson = analogArray.getJSONObject(i);
			Iterator<String> keys = analogJson.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				analogJsonObject.put(key, analogJson.get(key));
			}
		}
		globalJsonObject.put("Analog", analogJsonObject);
		JSONObject digitalJsonObject = new JSONObject();
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

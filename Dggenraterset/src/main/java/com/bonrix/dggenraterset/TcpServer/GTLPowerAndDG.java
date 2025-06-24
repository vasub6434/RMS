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
import com.fasterxml.jackson.databind.ObjectMapper;

public class GTLPowerAndDG {
  
	private static final Logger log = Logger.getLogger(GTLPowerAndDG.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");

	public static class HandlerGTLPowerAndDG extends SimpleChannelUpstreamHandler {

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
				log.info("GTLPowerAndDG " + imei + " : " + msg);

				
					log.info("GTLPowerAndDG " + imei + " Analog :: Analog String");
					Devicemaster device = devicemasterRepository.findByImei(imei);
					
					
					Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
						if (msgary[3].contains("1")) {
							if (track != null) {
							JSONArray AnalogJsonArray = new JSONArray();
							JSONArray DigitalJsonArray = new JSONArray();
							String[] analog1 = new String[] { "6387981", "6387982" };
							String[] analog1Param = { "RECTIFIER_OUTPUT_POWER", "RECTIFIER_OUTPUT_VOLTAGE", "BATERY_CAPACITY",
									"BATTERY_CURRENT", "RECTIFIER_OUTPUT_CURRENT", "SITE_BATT_VOLTAGE", "TOTAL_LOAD_CURRENT",
									"LOAD_CURRENT1", "LOAD_CURRENT2", "LOAD_CURRENT3", "LOAD_CURRENT4", "LOAD_CURRENT5",
									"MAINS_VOLTAGE_R", "MAINS_VOLTAGE_Y", "MAINS_VOLTAGE_B", "SOC", "BATTERY_TEMERATURE",
									"Mains_RUN_HRS", "DG_Total_RUNNING_HRS", "BATTERY_RUN_HRS", "RECTIFIER_OUTPUT_ENERGY",
									"TOTAL_LOAD_ENERGY", "LOAD_1_ENERGY", "LOAD_2_ENERGY", "LOAD_3_ENERGY", "LOAD_4_ENERGY",
									"LOAD_5_ENERGY","RECTIFIER_OUTPUT_POWER1" };

							String[] analog2 = new String[] { "313039043", "313039194" ,"313039237", "313039516","313039564", "313039664",
									"313039914", "313039945" ,"313039992", "313044102","313044225", "313044375",
									"313044541", "313044669" ,"313044821", "313045010","313045177", "313045310",
									"313045461","313045581"
							};
							List<String> analog2NameList = new ArrayList<>(Arrays.asList(analog2));
							
							JSONObject analogJsonObject = new JSONObject(
									(mapper.writeValueAsString(track.getAnalogdigidata())));
							String Register_000028=msgary[24].split(":")[1];
							String RECTIFIER_OUTPUT_POWER = Register_000028.substring(0, 4);
							String RECTIFIER_OUTPUT_VOLTAGE = Register_000028.substring(4, 8);
							
							String Register_000030=msgary[4].split(":")[1];
							String BATERY_CAPACITY = Register_000030.substring(0, 4);
							String BATTERY_CURRENT = Register_000030.substring(4, 8);
							
							String Register_000032=msgary[5].split(":")[1];
							String RECTIFIER_OUTPUT_CURRENT = Register_000032.substring(0, 4);
							
							String Register_000034=msgary[6].split(":")[1];
							String SITE_BATT_VOLTAGE = Register_000034.substring(0, 4);
							String TOTAL_LOAD_CURRENT = Register_000034.substring(4, 8);
							
							String Register_000036=msgary[7].split(":")[1];
							String LOAD_CURRENT1 = Register_000036.substring(0, 4);
							String LOAD_CURRENT2 = Register_000036.substring(4, 8);
							
							String Register_000038=msgary[8].split(":")[1];
							String LOAD_CURRENT3 = Register_000038.substring(0, 4);
							String LOAD_CURRENT4 = Register_000038.substring(4, 8);
							
							String Register_000040=msgary[9].split(":")[1];
							String LOAD_CURRENT5 = Register_000040;
							
							String Register_000048=msgary[10].split(":")[1];
							String MAINS_VOLTAGE_R = Register_000048.substring(0, 4);
							String MAINS_VOLTAGE_Y = Register_000048.substring(4, 8);
							
							String Register_000050=msgary[11].split(":")[1];
							String MAINS_VOLTAGE_B = Register_000050;
							
							String Register_000053=msgary[12].split(":")[1];
							String SOC = Register_000053.substring(0, 4);
							String BATTERY_TEMERATURE = Register_000053.substring(4, 8);
							
							String Register_000124=msgary[13].split(":")[1];
							String Mains_RUN_HRS =Register_000124;
							log.info("GTLPowerAndDG  Register_000124 :: "+Register_000124);
							String Register_000126=msgary[14].split(":")[1];
							String DG_Total_RUNNING_HRS =Register_000126;
							
							String Register_000128=msgary[15].split(":")[1];
							log.info("GTLPowerAndDG  Register_000128 :: "+Register_000128);
							String BATTERY_RUN_HRS =Register_000128;
							
							String Register_000146=msgary[16].split(":")[1];
							String RECTIFIER_OUTPUT_ENERGY =Register_000146;
							
							String Register_000152=msgary[17].split(":")[1];
							String TOTAL_LOAD_ENERGY =Register_000152;
							
							String Register_000160=msgary[18].split(":")[1];
							String LOAD_1_ENERGY =Register_000160;
							
							String Register_000162=msgary[19].split(":")[1];
							String LOAD_2_ENERGY =Register_000162;
							
							String Register_000164=msgary[20].split(":")[1];
							String LOAD_3_ENERGY =Register_000164;
							
							String Register_000166=msgary[21].split(":")[1];
							String LOAD_4_ENERGY =Register_000166;
							
							String Register_000168=msgary[22].split(":")[1];
							String LOAD_5_ENERGY =Register_000168;
							
							String Register_000175=msgary[23].split(":")[1];
							String RECTIFIER_OUTPUT_POWER1 =Register_000175;
							
							

							for (int i = 0; i < analog1Param.length; i++) {
								JSONObject analogObj = new JSONObject();

								if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_POWER")) {
									if(!RECTIFIER_OUTPUT_POWER.contains("X") )
									analogObj.put("295726687", String.valueOf(twoDForm.format(Integer.parseInt(RECTIFIER_OUTPUT_POWER, 16) * 1)));
									else if(RECTIFIER_OUTPUT_POWER.equalsIgnoreCase("0000"))
									{								
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295726687")) {
												analogObj.put("295726687",jsonObj.getString("295726687"));
											}
										}
									}	else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295726687")) {
												analogObj.put("295726687",jsonObj.getString("295726687"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_VOLTAGE")) {
									if(!RECTIFIER_OUTPUT_VOLTAGE.contains("X"))
									analogObj.put("295726832",String.valueOf(twoDForm.format(Integer.parseInt(RECTIFIER_OUTPUT_VOLTAGE, 16) * 0.01)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295726832")) {
												analogObj.put("295726832",jsonObj.getString("295726832"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("BATERY_CAPACITY")) {
									if(!BATERY_CAPACITY.contains("X"))
									analogObj.put("295726996", String.valueOf(twoDForm.format(Integer.parseInt(BATERY_CAPACITY, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295726996")) {
												analogObj.put("295726996",jsonObj.getString("295726996"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("BATTERY_CURRENT")) {
									if(!BATTERY_CURRENT.contains("X"))
									analogObj.put("295727075",String.valueOf(twoDForm.format(Integer.parseInt(BATTERY_CURRENT, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727075")) {
												analogObj.put("295727075",jsonObj.getString("295727075"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_CURRENT")) {
									if(!RECTIFIER_OUTPUT_CURRENT.contains("X"))
									analogObj.put("295727172",String.valueOf(twoDForm.format(Integer.parseInt(RECTIFIER_OUTPUT_CURRENT, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727172")) {
												analogObj.put("295727172",jsonObj.getString("295727172"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("SITE_BATT_VOLTAGE")) {
									if(!SITE_BATT_VOLTAGE.contains("X"))
									analogObj.put("295727268",String.valueOf(twoDForm.format(Integer.parseInt(SITE_BATT_VOLTAGE, 16) * 0.01)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727268")) {
												analogObj.put("295727268",jsonObj.getString("295727268"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("TOTAL_LOAD_CURRENT")) {
									if(!TOTAL_LOAD_CURRENT.contains("X"))
									analogObj.put("295727352", String.valueOf(twoDForm.format(Integer.parseInt(TOTAL_LOAD_CURRENT, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727352")) {
												analogObj.put("295727352",jsonObj.getString("295727352"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT1")) {
									if(!LOAD_CURRENT1.contains("X"))
									analogObj.put("295727445",String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT1, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727445")) {
												analogObj.put("295727445",jsonObj.getString("295727445"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT2")) {
									if(!LOAD_CURRENT2.contains("X"))
									analogObj.put("295731584",String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT2, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295731584")) {
												analogObj.put("295731584",jsonObj.getString("295731584"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT3")) {
									if(!LOAD_CURRENT3.contains("X"))
									analogObj.put("295727539",String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT3, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727539")) {
												analogObj.put("295727539",jsonObj.getString("295727539"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT4")) {
									if(!LOAD_CURRENT4.contains("X"))
									analogObj.put("295727574",String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT4, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727574")) {
												analogObj.put("295727574",jsonObj.getString("295727574"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_CURRENT5")) {
									if(!LOAD_CURRENT5.contains("X"))
									analogObj.put("295727645",String.valueOf(twoDForm.format(Integer.parseInt(LOAD_CURRENT5, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727645")) {
												analogObj.put("295727645",jsonObj.getString("295727645"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_R")) {
									if(!MAINS_VOLTAGE_R.contains("X"))
									analogObj.put("295727777",String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_R, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727777")) {
												analogObj.put("295727777",jsonObj.getString("295727777"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_Y")) {
									if(!MAINS_VOLTAGE_Y.contains("X"))
									analogObj.put("295727843",String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_Y, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727843")) {
												analogObj.put("295727843",jsonObj.getString("295727843"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("MAINS_VOLTAGE_B")) {
									if(!MAINS_VOLTAGE_B.contains("X"))
									analogObj.put("295727908",String.valueOf(twoDForm.format(Integer.parseInt(MAINS_VOLTAGE_B, 16) * 0.1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295727908")) {
												analogObj.put("295727908",jsonObj.getString("295727908"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("SOC")) {
									if(!SOC.contains("X"))
									analogObj.put("295728006",String.valueOf(twoDForm.format(Integer.parseInt(SOC, 16) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728006")) {
												analogObj.put("295728006",jsonObj.getString("295728006"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("BATTERY_TEMERATURE")) {
									if(!BATTERY_TEMERATURE.contains("X"))
									analogObj.put("295728090",String.valueOf(twoDForm.format(Integer.parseInt(BATTERY_TEMERATURE, 16) * 0.01)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728090")) {
												analogObj.put("295728090",jsonObj.getString("295728090"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("Mains_RUN_HRS")) {
									if(!Mains_RUN_HRS.contains("X"))
									analogObj.put("295728182", String.valueOf(twoDForm.format(Double.parseDouble(ieee(Mains_RUN_HRS)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										System.out.println("else");
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("295728182")) {
											analogObj.put("295728182",jsonObj.getString("295728182"));
										}
									}
									}
										
								}

								if (analog1Param[i].equalsIgnoreCase("DG_Total_RUNNING_HRS")) {
									if(!DG_Total_RUNNING_HRS.contains("X"))
										analogObj.put("295728269",String.valueOf(twoDForm.format(Double.parseDouble(ieee(DG_Total_RUNNING_HRS)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728269")) {
												analogObj.put("295728269",jsonObj.getString("295728269"));
											}
										}
									}
								}

								if (analog1Param[i].equalsIgnoreCase("BATTERY_RUN_HRS")) {
									if(!BATTERY_RUN_HRS.contains("X"))
									analogObj.put("295728363",String.valueOf(twoDForm.format(Double.parseDouble(ieee(BATTERY_RUN_HRS)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728363")) {
												analogObj.put("295728363",jsonObj.getString("295728363"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_ENERGY")) {
									if(!RECTIFIER_OUTPUT_ENERGY.contains("X"))
									analogObj.put("295728490", String.valueOf(twoDForm.format(Double.parseDouble(ieee(RECTIFIER_OUTPUT_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728490")) {
												analogObj.put("295728490",jsonObj.getString("295728490"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("TOTAL_LOAD_ENERGY")) {
									if(!TOTAL_LOAD_ENERGY.contains("X"))
									analogObj.put("295728583", String.valueOf(twoDForm.format(Double.parseDouble(ieee(TOTAL_LOAD_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728583")) {
												analogObj.put("295728583",jsonObj.getString("295728583"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_1_ENERGY")) {
									if(!LOAD_1_ENERGY.contains("X"))
									analogObj.put("295728684",String.valueOf(twoDForm.format(Double.parseDouble(ieee(LOAD_1_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728684")) {
												analogObj.put("295728684",jsonObj.getString("295728684"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_2_ENERGY")) {
									if(!LOAD_2_ENERGY.contains("X"))
									analogObj.put("295728727",String.valueOf(twoDForm.format(Double.parseDouble(ieee(LOAD_2_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728727")) {
												analogObj.put("295728727",jsonObj.getString("295728727"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_3_ENERGY")) {
									if(!LOAD_3_ENERGY.contains("X"))
									analogObj.put("295728763",String.valueOf(twoDForm.format(Double.parseDouble(ieee(LOAD_3_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728763")) {
												analogObj.put("295728763",jsonObj.getString("295728763"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_4_ENERGY")) {
									if(!LOAD_4_ENERGY.contains("X"))
									analogObj.put("295728811",String.valueOf(twoDForm.format(Double.parseDouble(ieee(LOAD_4_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728811")) {
												analogObj.put("295728811",jsonObj.getString("295728811"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("LOAD_5_ENERGY")) {
									if(!LOAD_5_ENERGY.contains("X"))
									analogObj.put("295728870",String.valueOf(twoDForm.format(Double.parseDouble(ieee(LOAD_5_ENERGY)) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("295728870")) {
												analogObj.put("295728870",jsonObj.getString("295728870"));
											}
										}
									}
								}
								if (analog1Param[i].equalsIgnoreCase("RECTIFIER_OUTPUT_POWER1")) {
									if(!RECTIFIER_OUTPUT_POWER1.contains("X"))
									analogObj.put("313038505", String.valueOf(twoDForm.format(Integer.parseInt(RECTIFIER_OUTPUT_POWER1, 16) * 1)));
									else {
										JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

										for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
											JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
											if (jsonObj.keys().next().equalsIgnoreCase("313038505")) {
												analogObj.put("313038505",jsonObj.getString("313038505"));
											}
										}
									}
								}
								AnalogJsonArray.put(analogObj);
							}

							JSONObject jo = new JSONObject();
							

							JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

							for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
								JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
								String k = jsonObj.keys().next();
								if (analog2NameList.contains(k)) {
									JSONObject analogObj = new JSONObject();
									analogObj.put(k, jsonObj.getString(k));
									AnalogJsonArray.put(analogObj);
								}
							}
							jo.put("Digital", new JSONArray());
							jo.put("Analog", AnalogJsonArray);

							Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(),
									new Date(), new ObjectMapper().readValue(jo.toString(), Map.class), track.getGpsdata(),
									new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

							History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
									new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
									track.getGpsdata(),
									new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

							Lasttrack t = lasttrackrepository.saveAndFlush(lTrack);
							History hst = histroyrepository.saveAndFlush(hist);

						}else {
							JSONObject gjo = new JSONObject();
							gjo.put("latitude", "23.033863");
							gjo.put("longitude", "72.585022");
							gjo.put("fuel", "0");
							DeviceProfile profile = device.getDp();
							JSONObject parameters = new JSONObject(profile.getParameters());
							JSONArray digital = parameters.getJSONArray("Digital");
							JSONArray analog = parameters.getJSONArray("Analog");
							JSONArray analogArray = new JSONArray();
							for (int i = 0; i < analog.length(); i++) {
								JSONObject analogJsonObject = new JSONObject();
								JSONObject obj = (JSONObject) analog.get(i);
								Parameter param = parameterrepository.findByid(new Long(obj.get("Analoginput").toString()));
								analogJsonObject.put(param.getId().toString(), "0.0");
								analogArray.put(analogJsonObject);
							}

							JSONObject jo = new JSONObject();
							jo.put("Digital", new JSONArray());
							jo.put("Analog", analogArray);

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
					}else if (msgary[3].contains("2")) {  
						log.info("GTLPowerAndDG 2 " + imei + " : " + msg);
						log.info("GTLPowerAndDG SAJAN " + imei + " Analog :: Analog String 2  "+msg);
						if (track != null) {
						JSONArray AnalogJsonArray = new JSONArray();
						JSONArray DigitalJsonArray = new JSONArray();
						String[] analog2Param = { "DG_Mains_Ph_1","DG_Mains_Ph_2","DG_Mains_Ph_3","DG_Ph_1","DG_Ph_2",
								 "DG_Ph_3","DG_Load_Current_Ph_1","DG_Load_Current_Ph_2","DG_Load_Current_Ph_3","DG_Mains_KWH",
								 "DG_KWH","DG_Run_HRS","DG_Mains_Run_HRS","DG_Battery","DG_Mains_KW_R",
								 "DG_Mains_KW_Y","DG_Mains_KW_B","DG_KW_R","DG_KW_Y","DG_KW_B",};
						
						String[] analog1 = new String[] { "295726687", "295726832" ,"295726996", "295727075","295727172", "295727268",
								"295727352", "295727445" ,"295731584", "295727539","295727574", "295727645",
								"295727777", "295727843" ,"295727908", "295728006","295728090", "295728182",
								"295728269","295728363","295728490","295728583","295728684",
								"295728727","295728763","295728811","295728870","313038505"
						};
						List<String> analog1NameList = new ArrayList<>(Arrays.asList(analog1));
						
						
						String Register_16388=msgary[4].split(":")[1];
						String DG_Mains_Ph_1 =Register_16388.substring(0, 4);
						String DG_Mains_Ph_2 =Register_16388.substring(4, 8);
						
						String Register_16390=msgary[5].split(":")[1];
						String DG_Mains_Ph_3 =Register_16390;
						
						String Register_16391=msgary[6].split(":")[1];
						String DG_Ph_1 =Register_16391.substring(0, 4);
						String DG_Ph_2 =Register_16391.substring(4, 8);
						
						
						String Register_16393=msgary[7].split(":")[1];
						String DG_Ph_3 =Register_16393;
						
						
						String Register_16394=msgary[8].split(":")[1];
						String DG_Load_Current_Ph_1=Register_16394.substring(0, 4);
						String DG_Load_Current_Ph_2=Register_16394.substring(4, 8);
						
						String Register_16396=msgary[9].split(":")[1];
						String DG_Load_Current_Ph_3=Register_16396;
						
						String Register_16397_98=msgary[10].split(":")[1] + msgary[11].split(":")[1];
						String DG_Mains_KWH=Register_16397_98;
						
						String Register_16399_00=msgary[12].split(":")[1] + msgary[13].split(":")[1];
						String DG_KWH=Register_16399_00;
						
						String Register_16401_402=msgary[14].split(":")[1] + msgary[15].split(":")[1];
						String DG_Run_HRS=Register_16401_402;
						
						String Register_16403_404=msgary[16].split(":")[1] + msgary[17].split(":")[1];
						String DG_Mains_Run_HRS=Register_16403_404;
						
						String Register_16405=msgary[18].split(":")[1];
						String DG_Battery=Register_16405;
						
						String Register_16408=msgary[19].split(":")[1];
						String DG_Mains_KW_R=Register_16408.substring(0, 4);
						String DG_Mains_KW_Y=Register_16408.substring(4, 8);
						
						String Register_16410=msgary[20].split(":")[1];
						String DG_Mains_KW_B=Register_16410;
						
						String Register_16411=msgary[21].split(":")[1];
						String DG_KW_R=Register_16411.substring(0, 4);
						String DG_KW_Y=Register_16411.substring(4, 8);
						
						 String Register_16413=msgary[22].split(":")[1];
						String DG_KW_B=Register_16413;
						
						JSONObject analogJsonObject = new JSONObject(
								(mapper.writeValueAsString(track.getAnalogdigidata())));
						JSONArray OldAnalogJsonArray = new JSONArray(analogJsonObject.get("Analog").toString());

						for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
							JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
							String k = jsonObj.keys().next();
							if (analog1NameList.contains(k)) {
								JSONObject analogObj = new JSONObject();
								analogObj.put(k, jsonObj.getString(k));
								AnalogJsonArray.put(analogObj);
							}
						}
						for (int i = 0; i < analog2Param.length; i++) {
							JSONObject analogObj = new JSONObject();
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_Ph_1")) {
								if(!DG_Mains_Ph_1.contains("X"))
								analogObj.put("313039043", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_Ph_1, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039043")) {
											analogObj.put("313039043",jsonObj.getString("313039043"));
										}
									}
								}
								System.out.println(analogObj.toString());
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_Ph_2")) {
								if(!DG_Mains_Ph_2.contains("X"))
								analogObj.put("313039194", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_Ph_2, 16) * 1)));
							else {
								for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
									JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
									if (jsonObj.keys().next().equalsIgnoreCase("313039194")) {
										analogObj.put("313039194",jsonObj.getString("313039194"));
									}
								}
							}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_Ph_3")) {
								if(!DG_Mains_Ph_3.contains("X"))
								analogObj.put("313039237", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_Ph_3, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039237")) {
											analogObj.put("313039237",jsonObj.getString("313039237"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Ph_1")) {
								if(!DG_Ph_1.contains("X"))
								analogObj.put("313039516", String.valueOf(twoDForm.format(Integer.parseInt(DG_Ph_1, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039516")) {
											analogObj.put("313039516",jsonObj.getString("313039516"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Ph_2")) {
								if(!DG_Ph_2.contains("X"))
								analogObj.put("313039564", String.valueOf(twoDForm.format(Integer.parseInt(DG_Ph_2, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039564")) {
											analogObj.put("313039564",jsonObj.getString("313039564"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Ph_3")) {
								if(!DG_Ph_3.contains("X"))
								analogObj.put("313039664", String.valueOf(twoDForm.format(Integer.parseInt(DG_Ph_3, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039664")) {
											analogObj.put("313039664",jsonObj.getString("313039664"));
										}
									}
								}
						}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Load_Current_Ph_1")) {
								if(!DG_Load_Current_Ph_1.contains("X"))
								analogObj.put("313039914", String.valueOf(twoDForm.format(Integer.parseInt(DG_Load_Current_Ph_1, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039914")) {
											analogObj.put("313039914",jsonObj.getString("313039914"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Load_Current_Ph_2")) {
								if(!DG_Load_Current_Ph_2.contains("X"))
								analogObj.put("313039945", String.valueOf(twoDForm.format(Integer.parseInt(DG_Load_Current_Ph_2, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039945")) {
											analogObj.put("313039945",jsonObj.getString("313039945"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Load_Current_Ph_3")) {
								if(!DG_Load_Current_Ph_3.contains("X"))
								analogObj.put("313039992", String.valueOf(twoDForm.format(Integer.parseInt(DG_Load_Current_Ph_3, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313039992")) {
											analogObj.put("313039992",jsonObj.getString("313039992"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_KWH")) {
								if(!DG_Mains_KWH.contains("X"))
								analogObj.put("313044102", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_KWH, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044102")) {
											analogObj.put("313044102",jsonObj.getString("313044102"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_KWH")) {
								if(!DG_KWH.contains("X"))
								analogObj.put("313044225", String.valueOf(twoDForm.format(Integer.parseInt(DG_KWH, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044225")) {
											analogObj.put("313044225",jsonObj.getString("313044225"));
										}
									}
								}
							}
							if (analog2Param[i].equalsIgnoreCase("DG_Run_HRS")) {
								if(!DG_Run_HRS.contains("X"))
								analogObj.put("313044375", String.valueOf(twoDForm.format(Integer.parseInt(DG_Run_HRS, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044375")) {
											analogObj.put("313044375",jsonObj.getString("313044375"));
										}
									}
								}
							}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_Run_HRS")) {
								if(!DG_Mains_Run_HRS.contains("X"))
								analogObj.put("313044541", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_Run_HRS, 16) * 1)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044541")) {
											analogObj.put("313044541",jsonObj.getString("313044541"));
										}
									}
								}
							}
							if (analog2Param[i].equalsIgnoreCase("DG_Battery")) {
								if(!DG_Battery.contains("X"))
								analogObj.put("313044669", String.valueOf(twoDForm.format(Integer.parseInt(DG_Battery, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044669")) {
											analogObj.put("313044669",jsonObj.getString("313044669"));
										}
									}  
								}
						}
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_KW_R")) {
								if(!DG_Mains_KW_R.contains("X"))
								analogObj.put("313044821", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_KW_R, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313044821")) {
											analogObj.put("313044821",jsonObj.getString("313044821"));
										}
									}
								}
						}
							
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_KW_Y")) {
								if(!DG_Mains_KW_Y.contains("X"))
								analogObj.put("313045010", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_KW_Y, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313045010")) {
											analogObj.put("313045010",jsonObj.getString("313045010"));
										}
									}
								}
						}
							if (analog2Param[i].equalsIgnoreCase("DG_Mains_KW_B")) {
								if(!DG_Mains_KW_B.contains("X"))
								analogObj.put("313045177", String.valueOf(twoDForm.format(Integer.parseInt(DG_Mains_KW_B, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313045177")) {
											analogObj.put("313045177",jsonObj.getString("313045177"));
										}
									}
								}
						}
							if (analog2Param[i].equalsIgnoreCase("DG_KW_R")) {
								if(!DG_KW_R.contains("X"))
								analogObj.put("313045310", String.valueOf(twoDForm.format(Integer.parseInt(DG_KW_R, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313045310")) {
											analogObj.put("313045310",jsonObj.getString("313045310"));
										}
									}
								}
						}
							if (analog2Param[i].equalsIgnoreCase("DG_KW_Y")) {
								if(!DG_KW_Y.contains("X"))
								analogObj.put("313045461", String.valueOf(twoDForm.format(Integer.parseInt(DG_KW_Y, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313045461")) {
											analogObj.put("313045461",jsonObj.getString("313045461"));
										}
									}
								}
						}
							if (analog2Param[i].equalsIgnoreCase("DG_KW_B")) {
								if(!DG_KW_B.contains("X"))
								analogObj.put("313045581", String.valueOf(twoDForm.format(Integer.parseInt(DG_KW_B, 16) /10)));
								else {
									for (int a = 0; a < OldAnalogJsonArray.length(); a++) {
										JSONObject jsonObj = OldAnalogJsonArray.getJSONObject(a);
										if (jsonObj.keys().next().equalsIgnoreCase("313045581")) {
											analogObj.put("313045581",jsonObj.getString("313045581"));
										}
									}
								}
							}
							AnalogJsonArray.put(analogObj);
						}
					
						JSONObject jo = new JSONObject();
						jo.put("Digital", new JSONArray());
						jo.put("Analog", AnalogJsonArray);

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

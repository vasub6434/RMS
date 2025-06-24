package com.bonrix.common.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Repository.AlertmessageshistoryRepository;
import com.bonrix.dggenraterset.Repository.AssignSiteRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.DigitalInputAlertRepositiry;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Repository.SMSTemplateRepository;
import com.bonrix.dggenraterset.Repository.SiteRepository;
import com.bonrix.dggenraterset.Repository.UserGroupRepository;
import com.bonrix.dggenraterset.Repository.UserRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlertMessage {

	LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext().getBean(LasttrackRepository.class);

	DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
			.getBean(DevicemasterRepository.class);

	DigitalInputAlertRepositiry digitalInputRepository = ApplicationContextHolder.getContext()
			.getBean(DigitalInputAlertRepositiry.class);

	SiteRepository siteRepository = ApplicationContextHolder.getContext().getBean(SiteRepository.class);

	UserGroupRepository usergroupRepository = ApplicationContextHolder.getContext().getBean(UserGroupRepository.class);

	UserRepository userRepository = ApplicationContextHolder.getContext().getBean(UserRepository.class);

	AssignSiteRepository assignSiteRepository = ApplicationContextHolder.getContext()
			.getBean(AssignSiteRepository.class);

	SMSTemplateRepository smsRepository = ApplicationContextHolder.getContext().getBean(SMSTemplateRepository.class);

	AlertmessageshistoryRepository alertMessageRepository = ApplicationContextHolder.getContext()
			.getBean(AlertmessageshistoryRepository.class);
	
	
	ParameterRepository parameterRepository = ApplicationContextHolder.getContext()
	.getBean(ParameterRepository.class);

	String sendTmie = "";

	public String alertCheck(Lasttrack ltrack, Lasttrack oldtrack, Devicemaster dmastr)
			throws JsonGenerationException, JsonMappingException, IOException {

		System.out.println("IN MAIN :: "+ltrack.getAnalogdigidata());
		System.out.println("IN MAIN :: "+oldtrack.getAnalogdigidata());
		List<DigitalInputAlert> alert = digitalInputRepository.findBymanagerid(dmastr.getManagerId());
		sendTmie = ltrack.getDeviceDate().toString();
		JsonParser parser = new JsonParser();
		DeviceProfile profile=dmastr.getDp();
		System.out.println("SAJAN :: " + alert.size());
		if (alert.size() > 0 && alert != null) {
			for (DigitalInputAlert alt : alert) {
				List<AssignSite> assignSite = assignSiteRepository.findBySiteidAndeviceid(dmastr.getDeviceid(),
						alt.getSite_id());

				if (alt.getDevice_id() == dmastr.getDeviceid() || assignSite != null) {

					Map<String, Object> ldigitaldata = ltrack.getAnalogdigidata();
					Map<String, Object> olddigitaldata = oldtrack.getAnalogdigidata();
					JSONObject parameters = new JSONObject(profile.getParameters());
					
				      JSONArray digital = parameters.getJSONArray("Digital");
				      System.out.println("IN IF :: "+ldigitaldata.toString());
				      System.out.println("IN IF :: "+olddigitaldata.toString());
					Object lobject = parser.parse(ldigitaldata.get("Digital").toString());
					Object oldobject = parser.parse(olddigitaldata.get("Digital").toString());
					JsonArray ljsonArr = (JsonArray) lobject; // Getting c
					JsonArray oldjsonArr = (JsonArray) oldobject; // Getting c
System.out.println("DIGI  :: "+digital);
					for (int i = 0; i <digital.length(); i++) {
						  
						JSONObject obj = (JSONObject) digital.get(i);
						System.out.println("digital.get(i)  :: "+digital.get(i));

						if (alt.getDigitalinput().equalsIgnoreCase(obj.get("parameterId").toString())) {
							System.out.println("5396 :: "+obj.get("parameterId").toString());
							List<String> mobile = new ArrayList<String>();
							int lMains_Fail =Integer.parseInt(((JsonObject) ljsonArr.get(i)).get(obj.get("parameterId").toString()).toString());
							Double oldMains_Fail = Double.parseDouble(((JsonObject) oldjsonArr.get(i)).get(obj.get("parameterId").toString()).toString());
							MessageTemplate template = smsRepository.findOne(alt.getMessagetemplate_id());
							System.out.println(oldMains_Fail+"  ::  "+lMains_Fail);
							
							//if (lMains_Fail.equalsIgnoreCase(oldMains_Fail))
							if (lMains_Fail!=oldMains_Fail.intValue())
							{
								System.out.println("Data Changed  :: "+lMains_Fail+" :: "+oldMains_Fail);
								if (alt.getUsergroup_id() != 0) {
									List<Object[]> listt = assignSiteRepository.getuserByGroupId(alt.getUsergroup_id(),
											alt.getManagerid());
									listt.forEach((Object[] o) -> {
										System.out.println(o[0].toString() + " :: " + o[1].toString());
										mobile.add(o[1].toString());
									});
								} else if (alt.getUser_id() != 0) {
									User user = userRepository.findByUserIdnew(alt.getUser_id());
									System.out.println(user.getContact());
									mobile.add(user.getContact());
								} else
									System.out.println("In valid Sender Data.");
								

							if (alt.getStatus().equalsIgnoreCase("ON") && lMains_Fail==1) {
								sendFCMnotification(alt, template, mobile);
							}
							if (alt.getStatus().equalsIgnoreCase("OFF") && lMains_Fail==0) {
								sendFCMnotification(alt, template, mobile);
							}
							if (alt.getStatus().equalsIgnoreCase("BOTH")) {
								sendFCMnotification(alt, template, mobile);
							}
							
							}else
								System.out.println("Data NOT Changed  :: "+lMains_Fail+" :: "+oldMains_Fail);
						}
					  }
					
					
					/*if (alt.getDigitalinput().equalsIgnoreCase("Mains_Fail")) {
						List<String> mobile = new ArrayList<String>();
						String lMains_Fail = ((JsonObject) ljsonArr.get(0)).get("Mains_Fail").toString();
						String oldMains_Fail = ((JsonObject) oldjsonArr.get(0)).get("Mains_Fail").toString();
						MessageTemplate template = smsRepository.findOne(alt.getMessagetemplate_id());

						  
						if (lMains_Fail.equalsIgnoreCase(oldMains_Fail))

							if (alt.getUsergroup_id() != 0) {
								List<Object[]> listt = assignSiteRepository.getuserByGroupId(alt.getUsergroup_id(),
										alt.getManagerid());
								listt.forEach((Object[] o) -> {
									System.out.println(o[0].toString() + " :: " + o[1].toString());
									mobile.add(o[1].toString());
								});
							} else if (alt.getUser_id() != 0) {
								User user = userRepository.findByUserIdnew(alt.getUser_id());
								System.out.println(user.getContact());
								mobile.add(user.getContact());
							} else
								System.out.println("In valid Sender Data.");

						if (alt.getStatus().equalsIgnoreCase("ON") && lMains_Fail.equalsIgnoreCase("1")) {
							sendFCMnotification(alt, template, mobile);
						}
						if (alt.getStatus().equalsIgnoreCase("OFF") && lMains_Fail.equalsIgnoreCase("1")) {
							sendFCMnotification(alt, template, mobile);
						}
						if (alt.getStatus().equalsIgnoreCase("BOTH")) {
							sendFCMnotification(alt, template, mobile);
						}
					}

				} else if (alt.getDigitalinput().equalsIgnoreCase("Gen_Fail_to_Start")) {

				} else if (alt.getDigitalinput().equalsIgnoreCase("Battery_LVD")) {

				} else {
					System.out.println("In valid Digital Alert");
				}*/
			}
			}
		}
		return sendTmie;
	}

	void sendFCMnotification(DigitalInputAlert alert, MessageTemplate template, List<String> mobileNos)
			throws IOException {
		for (String mobileNo : mobileNos) {
			try {
				String tempMsg = template.getMessage().toString();
				 Parameter param= parameterRepository.findByid(new Long(alert.getDigitalinput()));
				 System.out.println("HI :::::::::::::  "+param.getPrmname());
				tempMsg = tempMsg.replaceAll("\\<Input\\>", param.getPrmname());
				tempMsg = tempMsg.replaceAll("\\<Status\\>", alert.getStatus());
				tempMsg = tempMsg.replaceAll("\\<DateTime\\>", sendTmie);
				String sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
				String api = "http://fcmlight.saharshsolutions.co.in/sendSingleMessageAction/sendSingleMessage.do?message=<message>&clientName=tvipl&password=tvipl&phNo=<mobile_number>&senderName=tower&title=alert";
				api = api.replaceAll("\\<mobile_number\\>", mobileNo);
				api = api.replaceAll("\\<message\\>", sendMsg);

				String responce = CallAPI.sendGet(api);

				System.out.println(mobileNo);
				AlertMessages msg = new AlertMessages();
				msg.setDeviceid(alert.getDevice_id());
				msg.setEntrytime(new Date());
				msg.setHistoryid(0l);
				msg.setManagerid(alert.getManagerid());
				msg.setMessage(sendMsg);
				msg.setSiteid(alert.getSite_id());
				msg.setUsergroupid(alert.getUsergroup_id());
				msg.setUserid(alert.getUser_id());
				msg.setResponse(responce);
				AlertMessages amsg = alertMessageRepository.saveAndFlush(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}

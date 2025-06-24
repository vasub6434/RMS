package com.bonrix.dggenraterset.jobs;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.utils.CallAPI;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Repository.AlertmessageshistoryRepository;
import com.bonrix.dggenraterset.Repository.AnalogInputAlertRepositiry;
import com.bonrix.dggenraterset.Repository.AssignSiteRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.DigitalInputAlertRepositiry;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Repository.SMSTemplateRepository;
import com.bonrix.dggenraterset.Repository.SiteRepository;
import com.bonrix.dggenraterset.Repository.UserGroupRepository;
import com.bonrix.dggenraterset.Repository.UserRepository;
import com.bonrix.dggenraterset.TcpServer.TK103ServerNew;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AnalogAlert {

	private Logger log = Logger.getLogger(AnalogAlert.class);

	
	LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext().getBean(LasttrackRepository.class);

	 DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
			.getBean(DevicemasterRepository.class);

	AnalogInputAlertRepositiry analogInputRepository = ApplicationContextHolder.getContext()
			.getBean(AnalogInputAlertRepositiry.class);

	SiteRepository siteRepository = ApplicationContextHolder.getContext().getBean(SiteRepository.class);
	

	
	UserGroupRepository usergroupRepository = ApplicationContextHolder.getContext().getBean(UserGroupRepository.class);

	UserRepository userRepository = ApplicationContextHolder.getContext().getBean(UserRepository.class);

	AssignSiteRepository assignSiteRepository = ApplicationContextHolder.getContext()
			.getBean(AssignSiteRepository.class);

	SMSTemplateRepository smsRepository = ApplicationContextHolder.getContext().getBean(SMSTemplateRepository.class);

	AlertmessageshistoryRepository alertMessageRepository = ApplicationContextHolder.getContext()
			.getBean(AlertmessageshistoryRepository.class);
	
	 LasttrackRepository  lasttrackservices = ApplicationContextHolder.getContext()
			.getBean(LasttrackRepository.class);
	 
	 UserRepository  userRepor = ApplicationContextHolder.getContext()
				.getBean(UserRepository.class);
	
	
	ParameterRepository parameterRepository = ApplicationContextHolder.getContext()
	.getBean(ParameterRepository.class);

	String sendTmie = "";

	public String alertCheck(Lasttrack ltrack, Lasttrack oldtrack, Devicemaster dmastr)
			throws JsonGenerationException, JsonMappingException, IOException {

		System.out.println("IN MAIN :: "+ltrack.getAnalogdigidata());
		System.out.println("IN MAIN :: "+oldtrack.getAnalogdigidata());
		List<AnalogInputAlert> alert = analogInputRepository.findBymanagerid(dmastr.getManagerId());
		sendTmie = ltrack.getDeviceDate().toString();
		JsonParser parser = new JsonParser();
		DeviceProfile profile=dmastr.getDp();
		log.info("SAJAN :: " + alert.size());
		if (alert.size() > 0 && alert != null) {
			for (AnalogInputAlert alt : alert) {
				log.info(alt.getAnaloginput()+" :: "+alt.getConditionvalue());
				log.info(alt.toString());
				if(alt.getUsergroup_id()!=0)
				{
					log.info("In User Group Id Settings :: "+alt.getUsergroup_id());
				}else if(alt.getDeviceid()!=0)
				{
					log.info("In Device Id Settings :: "+alt.getDeviceid());
					if(alt.getSite_id()!=0)
					{
						log.info("In Site Id Settings :: "+alt.getSite_id());
					}
				}
				else if(alt.getUser_id()!=0)
				{
					log.info("In User Id Settings :: "+alt.getUser_id());
					if(alt.getSite_id()!=0)
					{
						log.info("In Site Id Settings :: "+alt.getSite_id());
						int count=assignSiteRepository.checkAvailableDeviceInSite(alt.getSite_id(),ltrack.getDeviceId());
						if(count!=0)
						{
						log.info("COUNT :: "+count+"  :: "+ltrack.getDeviceId());
						List<String> mobile = new ArrayList<String>();
						mobile.add(userRepor.findByUserIdnew(alt.getUser_id()).getContact().toString());
						MessageTemplate template = smsRepository.findOne(alt.getMessagetemplate_id());
						JSONObject root = new JSONObject(ltrack.getAnalogdigidata());
						JSONArray mainlArray = root.getJSONArray("Analog");
						log.info("analoglArray : "+mainlArray.getJSONObject(0).getDouble("237914"));
						
						mainlArray.forEach(item -> {
						    JSONObject obj = (JSONObject) item;
						    if(obj.has(alt.getAnaloginput())) {
						    log.info("LOOP : "+ obj.getDouble(alt.getAnaloginput().toString()));
						    log.info(Double.parseDouble(alt.getConditionvalue().toString()));
						    if(Double.parseDouble(alt.getConditionvalue().toString())>obj.getDouble(alt.getAnaloginput().toString()))
						    		{
									try {
										sendFCMnotification(alt, template, mobile,dmastr);
									} catch (IOException e) {
										e.printStackTrace();
									}
						    		}
						    }
						});
						}
					}else if(alt.getUsergroup_id()!=0) {
						
					}
				}else {
					log.info("Invalid Settings.");
				}
			}
		}
		return sendTmie;
	}

	void sendFCMnotification(AnalogInputAlert alert, MessageTemplate template, List<String> mobileNos,Devicemaster dmastr)
			throws IOException {
		for (String mobileNo : mobileNos) {
			AlertMessages msg = new AlertMessages();
			try {
				String tempMsg = template.getMessage().toString();
				 Parameter param= parameterRepository.findByid(new Long(alert.getAnaloginput()));
				log.info("HI :::::::::::::  "+param.getPrmname());
				tempMsg = tempMsg.replaceAll("\\<id\\>", dmastr.getDeviceid().toString());
				tempMsg = tempMsg.replaceAll("\\<device\\>", dmastr.getDevicename());
				tempMsg = tempMsg.replaceAll("\\<input\\>", param.getPrmname());
				tempMsg = tempMsg.replaceAll("\\<value\\>", alert.getConditionvalue().toString());
				tempMsg = tempMsg.replaceAll("\\<datetime\\>", sendTmie);
				String sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
				String api = "http://fcmlight.saharshsolutions.co.in/sendSingleMessageAction/sendSingleMessage.do?message=<message>&clientName=tvipl&password=tvipl&phNo=<mobile_number>&senderName=tower&title=alert";
				api = api.replaceAll("\\<mobile_number\\>", mobileNo);
				api = api.replaceAll("\\<message\\>", sendMsg);
                log.info(tempMsg);
				String responce = CallAPI.sendGet(api);  

				System.out.println(mobileNo);
				
				msg.setDeviceid(alert.getDeviceid());
				msg.setEntrytime(new Date());
				msg.setHistoryid(0l);
				msg.setManagerid(alert.getManagerid());
				msg.setMessage(sendMsg);
				msg.setSiteid(alert.getSite_id());
				msg.setUsergroupid(alert.getUsergroup_id());
				msg.setUserid(alert.getUser_id());
				msg.setResponse("SUCCESS");
				msg.setAlerttype("Analog");
				AlertMessages amsg = alertMessageRepository.saveAndFlush(msg);
			} catch (Exception e) {
				e.printStackTrace();
				msg.setResponse("ERROR");
				AlertMessages amsg = alertMessageRepository.saveAndFlush(msg);
			}

		}
	}
	
	  public static void main(String agr[]) throws JsonGenerationException, JsonMappingException, IOException
	   {
		 
		  /*System.out.println(t.getAnalogdigidata());
		  Map<String, Object> digitaldata=t.getAnalogdigidata();
		  System.out.println("DATA :: "+digitaldata.get("Digital"));*/
		 //  Devicemaster deviceMaste=devicemasterRepository.findOne(2795350l);
		   //DevicemasterRepository.
		  /* Lasttrack old=lasttrackservices.findOne(2795350l);
		   
		   Lasttrack newlt=lasttrackservices.findOne(2795350l);
		   
		   AnalogAlert alert=new AnalogAlert();
		   alert.alertCheck(newlt, old, deviceMaste);*/
	   }

	
}

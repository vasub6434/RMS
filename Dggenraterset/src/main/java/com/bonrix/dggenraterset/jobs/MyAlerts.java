package com.bonrix.dggenraterset.jobs;

import com.bonrix.common.utils.CallAPI;
import com.bonrix.dggenraterset.Model.AlearSummary;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Service.AlearSummaryService;
import com.bonrix.dggenraterset.Service.Alertmessageshistory;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.Utility.PlainTextEmail;
import com.bonrix.dggenraterset.Utility.RMSEmailSystem;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyAlerts {
  private static final Logger log = Logger.getLogger(MyAlerts.class);
  
  DigitalInputAlertService alertservices = (DigitalInputAlertService)ApplicationContextHolder.getContext()
    .getBean(DigitalInputAlertService.class);
  
  DevicemasterServices deviceservices = (DevicemasterServices)ApplicationContextHolder.getContext()
    .getBean(DevicemasterServices.class);
  
  Alertmessageshistory msgService = (Alertmessageshistory)ApplicationContextHolder.getContext()
    .getBean(Alertmessageshistory.class);
  
  SMSTemplateService smsService = (SMSTemplateService)ApplicationContextHolder.getContext()
    .getBean(SMSTemplateService.class);
  
  ParameterServices pRepor = (ParameterServices)ApplicationContextHolder.getContext()
    .getBean(ParameterServices.class);
  
  AlearSummaryService alertSummeryService = (AlearSummaryService)ApplicationContextHolder.getContext()
		    .getBean(AlearSummaryService.class);
  
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  
  String sendTmie = "";
  
  String rulrId = "0";
  
  String deviceName = "";
  
  String status = "";
  
  public String sendMsg(Devicemaster dmastr, Lasttrack ltrack, Lasttrack oldtrack) throws IOException {
    List<DigitalInputAlert> alert = this.alertservices.findBymanagerid(dmastr.getManagerId());
    this.sendTmie = this.formatter.format(ltrack.getDeviceDate());
    JsonParser parser = new JsonParser();
    boolean isAlert = false;
    this.deviceName = dmastr.getDevicename().trim();
   // log.info(String.valueOf(dmastr.getImei()) + " :: " + dmastr.getImei() + " chekAlert :: SAJAN :: " + alert.size());
    if (alert.size() > 0 && alert != null)
      for (DigitalInputAlert alt : alert) {
        if (alt.getDevice_id().compareTo(dmastr.getDeviceid()) == 0) {
          this.rulrId = alt.getNo().toString();
          int compareTo = alt.getDevice_id().compareTo(dmastr.getDeviceid());
          if (compareTo == 0) {
            Map<String, Object> ldigitaldata = ltrack.getAnalogdigidata();
            Map<String, Object> olddigitaldata = oldtrack.getAnalogdigidata();
            JSONObject latestObj = new JSONObject(ldigitaldata);
            JSONObject oldObj = new JSONObject(olddigitaldata);
            JSONArray ljsonArr = (JSONArray)latestObj.get("Digital");
            JSONArray oldjsonArr = (JSONArray)oldObj.get("Digital");
            for (int i = 0; i < ljsonArr.length(); i++) {
              JSONObject lstObj = ljsonArr.getJSONObject(i);
              JSONObject odlObj = oldjsonArr.getJSONObject(i);
              String parameter = lstObj.keys().next();
           //   System.out.println("InfoKey: " + parameter + ", value: " + lstObj.getString(parameter) + " :: " + odlObj.getString(parameter));
              if (!lstObj.getString(parameter).equalsIgnoreCase(odlObj.getString(parameter)))
                if (alt.getDigitalinput().equalsIgnoreCase(parameter)) {
                  MessageTemplate template = this.smsService.findBymid(alt.getMessagetemplate_id());
                  List<String> mobile = new ArrayList<>();
                  String[] mob = alt.getMobileno().split(",");
                  byte b;
                  int j;
                  String[] arrayOfString1;
                  for (j = (arrayOfString1 = mob).length, b = 0; b < j; ) {
                    String mo = arrayOfString1[b];
                    mobile.add(mo);
                    b = (byte)(b + 1);
                  } 
                  if (alt.getStatus().equalsIgnoreCase("ON") && lstObj.getString(parameter).equalsIgnoreCase("1"))
                    sendFCMnotification(alt, template, mobile, "CLEAR",ltrack); 
                  if (alt.getStatus().equalsIgnoreCase("OFF") && lstObj.getString(parameter).equalsIgnoreCase("0"))
                    sendFCMnotification(alt, template, mobile, "ACTIVE",ltrack); 
                  if (alt.getStatus().equalsIgnoreCase("BOTH")) {
                    if (lstObj.getString(parameter).equalsIgnoreCase("1")) {
                      this.status = "CLEAR";
                    } else {
                      this.status = "ACTIVE";
                    } 
                    sendFCMnotification(alt, template, mobile, this.status,ltrack);
                  } 
                }  
            } 
          }   
        } 
      }  
    return "Send With No Error.";
  }
  
  public void sendFCMnotification(DigitalInputAlert alert, MessageTemplate template, List<String> mobileNos, String c, Lasttrack ltrack) throws IOException {
   // log.info(mobileNos.toString());
    String sendMsg = "NA";
    String responce = "NA";
    String smsArray=alert.getNotification();
    for (String mobileNo : mobileNos) {
      try {   
    	  
    	  if (smsArray.contains("fcm")) {
    		    String tempMsg=template.getMessage().toString();
				tempMsg=tempMsg.replaceAll("\\<input1\\>", pRepor.get(Long.parseLong(alert.getDigitalinput())).getPrmname());
				tempMsg=tempMsg.replaceAll("\\<status\\>", status);
				tempMsg=tempMsg.replaceAll("\\<date\\>", sendTmie);
				tempMsg=tempMsg.replaceAll("\\<deviceName\\>", deviceName);
				  sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
				String api = "http://fcmlight.saharshsolutions.co.in/sendSingleMessageAction/sendSingleMessage.do?message=<message>&clientName=tvipl&password=tvipl&phNo=<mobile_number>&senderName=tower&title=Alert";
				api = api.replaceAll("\\<mobile_number\\>", mobileNo);
				api = api.replaceAll("\\<message\\>", sendMsg);
				   responce=CallAPI.sendGet(api);
    		} else {     
    		    String tempMsg = template.getMessage().toString();
    	        tempMsg = tempMsg.replaceAll("\\<input1\\>", this.pRepor.get(Long.valueOf(Long.parseLong(alert.getDigitalinput()))).getPrmname());
    	        tempMsg = tempMsg.replaceAll("\\<status\\>", status);
    	        tempMsg = tempMsg.replaceAll("\\<date\\>", this.sendTmie);
    	        tempMsg = tempMsg.replaceAll("\\<deviceName\\>", this.deviceName);
    	        sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
    	        String api="https://bulksmsapi.smartping.ai//?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=<mobile_number>&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=<message>";
    	        api = api.replaceAll("\\<mobile_number\\>", mobileNo);
    	        api = api.replaceAll("\\<message\\>", sendMsg);
    	        responce = CallAPI.sendGet(api);  
    	        if( this.pRepor.get(Long.valueOf(Long.parseLong(alert.getDigitalinput()))).getPrmname().equalsIgnoreCase("Door"))
    	        {
    	        	RMSEmailSystem email=new RMSEmailSystem();
    	    		email.EmailSystem("signotox_noc@signotox.com","RMS Door Alert", tempMsg);
    	        }
    		}
    	  
    	  
    	  
    	  
        
      } catch (Exception e) { 
        log.info(e.getMessage());    
        e.printStackTrace();
      } 
    } 
    Date alertTime=new Date();
    String mobileNoString = String.join(",", (Iterable)mobileNos);
    AlertMessages msg = new AlertMessages();
    msg.setDeviceid(alert.getDevice_id());
    msg.setEntrytime(alertTime);
    msg.setHistoryid(Long.valueOf(0L));
    msg.setManagerid(alert.getManagerid());
    msg.setMessage(URLDecoder.decode(sendMsg, "UTF-8"));
    msg.setSiteid(alert.getSite_id());  
    msg.setUsergroupid(Long.valueOf(Long.parseLong(alert.getDigitalinput())));
    msg.setUserid(alert.getUser_id());
    msg.setResponse(responce);
    msg.setRuleid(this.rulrId);
    msg.setAlerttype("DIGITAL");
    msg.setSentmobile(mobileNoString);
    AlertMessages alertMessages1 = this.msgService.savealertMessage(msg);
     
    
    AlearSummary alearSummary = new AlearSummary();

    if (status.equalsIgnoreCase("ACTIVE")) {
        String digitalInput = alert.getDigitalinput();
       // if (digitalInput != null && !digitalInput.isEmpty()) {
            long parameterId = Long.parseLong(digitalInput);
            alearSummary = alertSummeryService.findByDeviceIdAndParameterIdAndIsActive(alert.getDevice_id(), parameterId, false);
            
            if (alearSummary == null) {
            	AlearSummary alearSummaryNew = new AlearSummary();
            	alearSummaryNew.setDeviceId(alert.getDevice_id());
            	alearSummaryNew.setEntryTime(new Date());
            	alearSummaryNew.setStartTime(ltrack.getSystemDate());
            	alearSummaryNew.setManagerId(alert.getManagerid());
            	alearSummaryNew.setParameterId(parameterId);
            	alearSummaryNew.setParametername(pRepor.get(parameterId).getPrmname());
            	alearSummaryNew.setIsactive(true);
                alertSummeryService.saveOrUpdate(alearSummaryNew);
            } else {
                log.info("AlearSummary is not null " + alert.getDevice_id());
            }
      /*  } else {
            log.error("Invalid digital input for alert: " + alert.getDevice_id());
        }*/
    } else {
        alearSummary = alertSummeryService.findByDeviceIdAndParameterIdAndIsActiveAndEndTimeNull(
                alert.getDevice_id(), 
                Long.valueOf(Long.parseLong(alert.getDigitalinput())), 
                true);
        
        if (alearSummary != null) {
            alearSummary.setEndTime(ltrack.getSystemDate());
            alearSummary.setIsactive(false);  
            Date startTime = alearSummary.getStartTime();  // Start time from AlearSummary
            Date endTime = ltrack.getSystemDate();  
            if (endTime != null && startTime != null) {
                Duration duration = Duration.between( startTime.toInstant(), endTime.toInstant());
                alearSummary.setDuration(duration.getSeconds());
            } 
            alertSummeryService.saveOrUpdate(alearSummary);
        } else {
            log.info("AlearSummary is null " + alert.getDevice_id());
        }
    
    }
    
  }
}

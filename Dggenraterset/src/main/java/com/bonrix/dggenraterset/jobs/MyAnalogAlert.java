package com.bonrix.dggenraterset.jobs;

import com.bonrix.common.utils.CallAPI;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Service.Alertmessageshistory;
import com.bonrix.dggenraterset.Service.AnalogInputAlertService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.Utility.GmailEmailSender;
import com.bonrix.dggenraterset.jobs.MyAnalogAlert;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyAnalogAlert {
  private static final Logger log = Logger.getLogger(MyAnalogAlert.class);
  
  DigitalInputAlertService alertservices = (DigitalInputAlertService)ApplicationContextHolder.getContext()
    .getBean(DigitalInputAlertService.class);
  
  DevicemasterServices deviceservices = (DevicemasterServices)ApplicationContextHolder.getContext().getBean(DevicemasterServices.class);
  
  Alertmessageshistory msgService = (Alertmessageshistory)ApplicationContextHolder.getContext().getBean(Alertmessageshistory.class);
  
  SMSTemplateService smsService = (SMSTemplateService)ApplicationContextHolder.getContext().getBean(SMSTemplateService.class);
  
  ParameterServices pRepor = (ParameterServices)ApplicationContextHolder.getContext().getBean(ParameterServices.class);
  
  LasttrackServices lasttrackservices = (LasttrackServices)ApplicationContextHolder.getContext().getBean(LasttrackServices.class);
  
  AnalogInputAlertService analogAlertSer = (AnalogInputAlertService)ApplicationContextHolder.getContext()
    .getBean(AnalogInputAlertService.class);
  
  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  String sendTmie = "";
  
  String rulrId = "0";
  
  String deviceName = "";
  
  public void sendAnalogAlert(Devicemaster dmastr, Lasttrack ltrack) throws ParseException {
    this.sendTmie = ltrack.getDeviceDate().toString();
    List<AnalogInputAlert> alert = this.analogAlertSer.findBymanagerid(dmastr.getManagerId());
    this.deviceName = dmastr.getDevicename();
    if (alert.size() > 0 && alert != null)
      for (AnalogInputAlert alt : alert) {
        if (alt.getDeviceid().compareTo(dmastr.getDeviceid()) == 0) {
        	rulrId = "" + alt.getNo();
          List<Object[]> altTimeObject = this.analogAlertSer.getAlertGenerationTime(alt.getNo().longValue());
          if (altTimeObject != null && altTimeObject.size() != 0) {
            Date startDate = sdf.parse(((Object[])altTimeObject.get(0))[1].toString());
            Date endDate = new Date();
            long duration = endDate.getTime() - startDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (diffInMinutes >= alt.getTimedifference().longValue()) {
              Map<String, Object> map = ltrack.getAnalogdigidata();
              JSONObject jSONObject = new JSONObject(map);
              JSONArray jSONArray = (JSONArray)jSONObject.get("Analog");
              for (int j = 0; j < jSONArray.length(); j++) {
                JSONObject lstObj = jSONArray.getJSONObject(j);
                String parameter = lstObj.keys().next();
                if (alt.getAnaloginput().equalsIgnoreCase(parameter)) {
                  MessageTemplate template = this.smsService.findBymid(alt.getMessagetemplate_id());
                  if (alt.getConditionstring().equalsIgnoreCase("Cross Below")) {
                    long condtionValue = alt.getConditionvalue().longValue();
                    double analogValue = Double.parseDouble(lstObj.getString(parameter));
                    if (analogValue < condtionValue) {
                      List<String> mobile = new ArrayList<>();
                      String[] mob = alt.getMobileno().split(",");
                      String[] getEmail_id = alt.getEmail_id().split(",");
                      List<String> mails = new ArrayList<>();
                      byte b;
                      int k;
                      String[] arrayOfString1;
                      for (k = (arrayOfString1 = getEmail_id).length, b = 0; b < k; ) {
                        String mail = arrayOfString1[b];
                        mails.add(mail);
                        b = (byte)(b + 1);
                      } 
                      try {
                        SendMail(mails, template, alt);
                      } catch (Exception e1) {
                        log.info(dmastr.getDeviceid() + " " + e1.getMessage());
                        log.info(dmastr.getDeviceid() + " " + e1.getLocalizedMessage());
                        log.info(dmastr.getDeviceid() + " " + e1.getStackTrace());
                        e1.printStackTrace();
                      } 
                      for (k = (arrayOfString1 = mob).length, b = 0; b < k; ) {
                        String mo = arrayOfString1[b];
                        mobile.add(mo);
                        b = (byte)(b + 1);
                      } 
                      try {
                        sendAnalogSMSAlert(alt, template, mobile);
                      } catch (IOException e) {
                        log.info(dmastr.getDeviceid() + " " + e.getMessage());
                        log.info(dmastr.getDeviceid() + " " + e.getLocalizedMessage());
                        log.info(dmastr.getDeviceid() + " " + e.getStackTrace());
                        e.printStackTrace();
                      } 
                    } 
                  } else {
                    alt.getConditionstring().equalsIgnoreCase("Cross Above");
                  } 
                } 
              } 
            } 
            continue;
          } 
          Map<String, Object> ldigitaldata = ltrack.getAnalogdigidata();
          JSONObject lastTrackObj = new JSONObject(ldigitaldata);
          JSONArray lastTrackjsonArr = (JSONArray)lastTrackObj.get("Analog");
          for (int i = 0; i < lastTrackjsonArr.length(); i++) {
            JSONObject lstObj = lastTrackjsonArr.getJSONObject(i);
            String parameter = lstObj.keys().next();
            if (alt.getAnaloginput().equalsIgnoreCase(parameter)) {
              MessageTemplate template = this.smsService.findBymid(alt.getMessagetemplate_id());
              if (alt.getConditionstring().equalsIgnoreCase("Cross Below")) {
                long condtionValue = alt.getConditionvalue().longValue();
                double analogValue = Double.parseDouble(lstObj.getString(parameter));
                if (analogValue < condtionValue) {
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
                  try {
                    sendAnalogSMSAlert(alt, template, mobile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                } 
              } else if (alt.getConditionstring().equalsIgnoreCase("Cross Above")) {
                long condtionValue = alt.getConditionvalue().longValue();
                double analogValue = Double.parseDouble(lstObj.getString(parameter));
                if (analogValue > condtionValue) {
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
                  try {
                    sendAnalogSMSAlert(alt, template, mobile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                } 
              } 
            } 
          } 
        } 
      }  
  }
  
  public void sendAnalogSMSAlert(AnalogInputAlert alert, MessageTemplate template, List<String> mobileNos) throws IOException {
    String sendMsg = "NA";
    String responce = "NA";
    for (String mobileNo : mobileNos) {
      try {
        String tempMsg = template.getMessage().toString();
        tempMsg = tempMsg.replaceAll("\\<analogType\\>", 
            this.pRepor.get(Long.valueOf(Long.parseLong(alert.getAnaloginput()))).getPrmname());
        tempMsg = tempMsg.replaceAll("\\<status\\>", alert.getConditionstring());
        tempMsg = tempMsg.replaceAll("\\<time\\>", this.sendTmie);
        tempMsg = tempMsg.replaceAll("\\<deviceName\\>", this.deviceName);
        tempMsg = tempMsg.replaceAll("\\<conditionvalue\\>", alert.getConditionvalue().toString());
        sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
        String api = "https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=<mobile_number>&senderId=VTLNOC&ContentID=1307160913127645260&EntityID=1301157960116941398&message=<message>";
        api = api.replaceAll("\\<mobile_number\\>", mobileNo);
        api = api.replaceAll("\\<message\\>", sendMsg);
        responce = CallAPI.sendGet(api);
      } catch (Exception e) {
        e.printStackTrace();
        log.info(e.getMessage());
        e.printStackTrace();
        log.error(e.getStackTrace());
      } 
    } 
    String mobileNoString = String.join(",", (Iterable)mobileNos);
    AlertMessages msg = new AlertMessages();
    msg.setDeviceid(alert.getDeviceid());
    msg.setEntrytime(new Date());
    msg.setHistoryid(Long.valueOf(0L));
    msg.setManagerid(alert.getManagerid());
    msg.setMessage(sendMsg);
    msg.setSiteid(alert.getSite_id());
    msg.setUsergroupid(alert.getUsergroup_id());
    msg.setUserid(alert.getUser_id());
    msg.setResponse(responce);
    msg.setRuleid(this.rulrId);
    msg.setAlerttype("ANALOG");
    msg.setSentmobile(mobileNoString);
    AlertMessages amsg = this.msgService.savealertMessage(msg);
  }
  
  public void SendMail(List<String> getEmail_id, MessageTemplate template, AnalogInputAlert alert) throws Exception {
    String sendMsg = "NA";
    String responce = "NA";
    for (String emailId : getEmail_id) {
      String tempMsg = template.getMessage().toString();
      tempMsg = tempMsg.replaceAll("\\<analogType\\>", 
          this.pRepor.get(Long.valueOf(Long.parseLong(alert.getAnaloginput()))).getPrmname());
      tempMsg = tempMsg.replaceAll("\\<status\\>", alert.getConditionstring());
      tempMsg = tempMsg.replaceAll("\\<time\\>", this.sendTmie);
      tempMsg = tempMsg.replaceAll("\\<deviceName\\>", this.deviceName);
      tempMsg = tempMsg.replaceAll("\\<conditionvalue\\>", alert.getConditionvalue().toString());
      sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
      GmailEmailSender email = new GmailEmailSender();
      log.info("RMSJOB getInputHistory Email Sending.......");
      email.EmailSystem(emailId, URLEncoder.encode("Low Battery Alert", "UTF-8"), sendMsg, "");
      log.info("RMSJOB getInputHistory Email To:::::::" + emailId);
    } 
    String getEmail_idString = String.join(",", (Iterable)getEmail_id);
    AlertMessages msg = new AlertMessages();
    msg.setDeviceid(alert.getDeviceid());
    msg.setEntrytime(new Date());
    msg.setHistoryid(Long.valueOf(0L));
    msg.setManagerid(alert.getManagerid());
    msg.setMessage(sendMsg);
    msg.setSiteid(alert.getSite_id());
    msg.setUsergroupid(alert.getUsergroup_id());
    msg.setUserid(alert.getUser_id());
    msg.setResponse(responce);
    msg.setRuleid(this.rulrId);
    msg.setAlerttype("ANALOG");
    msg.setSentmobile(getEmail_idString);
    AlertMessages amsg = this.msgService.savealertMessage(msg);
  }
}

package com.bonrix.dggenraterset.Controller;

import com.bonrix.dggenraterset.Controller.IHController;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.IHReport;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.AnalogInputAlertService;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.IHReportService;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Utility.GmailEmailSender;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.bonrix.dggenraterset.Utility.SendAwsSES;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.joda.time.ReadablePartial;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@RestController
public class IHController {
  @Autowired
  DevicemasterServices devicemasterservices;
  
  @Autowired
  DevicemasterRepository deviceReop;
  
  @Autowired
  HistoryServices hstservices;
  
  @Autowired
  ParameterRepository prepo;
  
  @Autowired
  IHReportService ihreportService;
  
  @Autowired
  AssignSiteService assSiteService;
  
  @Autowired
  LasttrackServices lasttrackservices;
  
  @Autowired
  SiteServices siteService;
  
  @Autowired
  AnalogInputAlertService analogAlertSer;
  
  @Autowired 
  DeviceProfileRepository DpService;
  
  @Autowired
  DigitalInputAlertService alertservices;
  
  private static final Logger log = Logger.getLogger(IHController.class);
  
  static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
  
  static final DateFormat dDf = new SimpleDateFormat("yyyy-MM-dd");
  
  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM");
  
  @RequestMapping(value = {"/api/inputHistory/{startDate}/{endDate}/{paramId}/{device_ids}"}, produces = {"application/json"})
  public String inputHistory(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String paramId, @PathVariable String device_ids) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
    JSONObject dataJson = new JSONObject();
    String[] digitals = paramId.split(",");
    String s = startDate;
    String e = endDate;
    LocalDate start = LocalDate.parse(s);
    LocalDate end = LocalDate.parse(e);
    List<LocalDate> totalDates = new ArrayList<>();
    JSONArray DataamainJSON = new JSONArray();
    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
    String[] devices = new String[deviceList.size()];
    if (deviceList.size() != 0)
      for (int analog = 0; analog < deviceList.size(); analog++) {
        Object[] result = deviceList.get(analog);
        devices[analog] = (String)result[0];
      }  
    while (!start.isAfter(end)) {
    	totalDates.add(start);
    	start = start.plusDays(1);  
    }
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("INPUTHIST " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (LocalDate date : totalDates) {
        for (int d = 0; d < digitals.length; d++) {
          List<Object[]> lst = this.hstservices.getdata(date.toString(), 
              date.toString(), digitals[d], Long.parseLong(devices[ds]));
          JSONArray mainJSON = new JSONArray();
          if (lst != null) {
            String lstatus = null;
            int ct = 0;
            for (int i = 0; i < lst.size(); i++) {
              Object[] obj = lst.get(i);
              if (i == 0) {
                lstatus = obj[3].toString();
                JSONObject jo = new JSONObject();
                jo.put("Start Date", obj[1].toString());
                jo.put("Start Status", obj[3].toString());
                mainJSON.put(ct, jo);
                ct++;
              } else {
                if (!lstatus.equalsIgnoreCase(obj[3].toString())) {
                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
                  innerJSON12.put("End Date", obj[1].toString());
                  mainJSON.put(ct - 1, innerJSON12);
                  JSONObject jo2 = new JSONObject();
                  jo2.put("Start Date", obj[1].toString());
                  jo2.put("Start Status", obj[3].toString());
                  mainJSON.put(ct, jo2);
                  ct++;
                } 
                lstatus = obj[3].toString();
              } 
            } 
          } 
          for (int j = 0; j < mainJSON.length(); j++) {
            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
            jsonObject1.put("Parameter", this.prepo.findByid((new Long(digitals[d])).longValue()).getPrmname());
            if (j != mainJSON.length() - 1) {
              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                jsonObject1.put("Close", 
                    JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                jsonObject1.put("Open", "00");
              } else {
                jsonObject1.put("Close", "00");
                jsonObject1.put("Open", 
                    JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
              } 
            } else {
              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
              Date date21 = df1.parse(date.toString());
              Date date22 = df1.parse(df1.format(new Date()));
              int date23 = date21.compareTo(date22);
              if (date23 == 0) {
                jsonObjectLast.put("End Date", df.format(new Date()));
                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                  jsonObject1.put("Close", 
                      JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                  jsonObject1.put("Open", "00");
                } else {
                  jsonObject1.put("Close", "00");
                  jsonObject1.put("Open", 
                      JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                } 
              } else if (date23 == -1) {
                jsonObjectLast.put("End Date", df
                    .format(df.parse(String.valueOf(String.valueOf(date.toString())) + " 23:59:59")));
                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
                        df.parse(df.format(
                            df.parse(String.valueOf(String.valueOf(date.toString())) + " 23:59:59")))));
                  jsonObject1.put("Open", "00");
                } else {
                  jsonObject1.put("Close", "00");
                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
                        df.parse(df.format(
                            df.parse(String.valueOf(String.valueOf(date.toString())) + " 23:59:59")))));
                } 
              } 
            } 
            jsonObject1.put("DeviceName", deviceObject.getDevicename());
            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
            DataamainJSON.put(jsonObject1);
            IHReport report = new IHReport();
            report.setClose(jsonObject1.get("Close").toString());
            report.setDeviceId(deviceObject.getDeviceid().longValue());
            report.setEndDate(jsonObject1.get("End Date").toString());
            report.setEntryDate(df.parse(String.valueOf(startDate) + " 00:00:00"));
            report.setOpen(jsonObject1.get("Open").toString());
            report.setParameterId((new Long(digitals[d])).longValue());
            report.setStartDate(jsonObject1.get("Start Date").toString());
            report.setStartStatus(jsonObject1.get("Start Status").toString());
            this.ihreportService.saveReport(report);
          } 
        } 
      } 
    } 
    dataJson.put("data", DataamainJSON);
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/getInputHistory/{startDate}/{endDate}/{paramId}/{device_ids}"}, produces = {"application/json"})
  public String getInputHistory(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String paramId, @PathVariable String device_ids) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
    JSONObject dataJson = new JSONObject();
    String[] digitals = paramId.split(",");
    String s = startDate;
    String e = endDate;
    LocalDate start = LocalDate.parse(s);
    LocalDate end = LocalDate.parse(e);
    String[] devices = device_ids.split(",");
    List<LocalDate> totalDates = new ArrayList<>();
    JSONArray mainJSON = new JSONArray();
    while (!start.isAfter(end)) {
    	totalDates.add(start);
    	start = start.plusDays(1);  
    }
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("getInputHistory " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (LocalDate date : totalDates) {
        for (int d = 0; d < digitals.length; d++) {
          List<Object[]> lst = this.ihreportService.getIRReport(date.toString(), 
              date.toString(), Long.parseLong(digitals[d]), Long.parseLong(devices[ds]));
          System.out.println(lst.size());
          if (lst != null)
            for (int i = 0; i < lst.size(); i++) {
              Object[] obj = lst.get(i);
              JSONObject jo = new JSONObject();
              jo.put("Start Date", obj[0].toString());
              jo.put("deviceDescription", deviceObject.getDevicedescription());
              jo.put("End Date", obj[1].toString());
              jo.put("Start Status", obj[2].toString());
              jo.put("Parameter", this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname());
              jo.put("Open", (obj[4].toString()));
              jo.put("Close", (obj[5].toString()));
            /*  jo.put("Open", (obj[4].toString().length() > 0) ? convertMinutesToTime(Integer.parseInt(obj[4].toString())) : "");
              jo.put("Close", (obj[5].toString().length() > 0) ? convertMinutesToTime(Integer.parseInt(obj[5].toString())) : "");*/
              jo.put("DeviceName", deviceObject.getDevicename());
              mainJSON.put(jo);
            }  
        } 
      } 
    } 
    dataJson.put("data", mainJSON);
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/MailInputHistory"}, produces = {"application/json"})
  public String MailInputHistory() throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException {
    JSONObject dataJson = new JSONObject();
    ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
    String EMAIL_IDS = rb.getString("EMAIL_IDS");
    String SEP = System.getProperty("file.separator");
    URL resource = getClass().getResource("/");
    String path = resource.getPath();
    path = path.replace("WEB-INF/classes/", "");
    String dir = String.valueOf(String.valueOf(path)) + "maildailyreport" + SEP;
    System.out.println(dir);
    File file = new File(dir);
    if (!file.exists())
      file.mkdir(); 
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
    String fileName = String.valueOf(getYesterdayDateString()) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
      ".csv";
    log.info(String.valueOf(String.valueOf(dir)) + fileName);
    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(dir)) + fileName);
    CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
			CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    List<String[]> data = (List)new ArrayList<>();
    data.add(new String[] { "Device Name", "Group Name", "Input Name", "Start Time(Minutes)", "End Time(Minutes)", 
          "Clear", "Active" });
    String paramId = "284945,6348798,291934,6348854,6348815,6348821,6348824";
    String[] digitals = paramId.split(",");
    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
    String[] devices = new String[deviceList.size()];
    if (deviceList.size() != 0)
      for (int analog = 0; analog < deviceList.size(); analog++) {
        Object[] result = deviceList.get(analog);
        devices[analog] = (String)result[0];
      }  
    log.info("RMSJOB :: deviceList.size() " + deviceList.size());
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("getInputHistory " + devices[ds]);
      Site site = null;
      List<AssignSite> aSite = null;
      aSite = this.assSiteService.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      if (aSite.size() != 0)
        site = this.siteService.findBysiteid(((AssignSite)aSite.get(0)).getSiteid()); 
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (int d = 0; d < digitals.length; d++) {
        List<Object[]> lst = this.ihreportService.getIRReport(getYesterdayDateString(), 
            getYesterdayDateString(), Long.parseLong(digitals[d]), Long.parseLong(devices[ds]));
        if (lst != null)
          for (int j = 0; j < lst.size(); j++) {
            Object[] obj = lst.get(j);
            data.add(
                new String[] { deviceObject.getDevicename(), 
                  (site == null) ? "Not Set" : site.getSite_name(), 
                  this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname(), 
                  obj[0].toString(), 
                  obj[1].toString(), 
                  (obj[4] == null) ? "00" : obj[4].toString(), 
                  (obj[5] == null) ? "00" : obj[5].toString() });
          }  
      } 
    } 
    writer.writeAll(data);
    writer.close();
    String bcc = rb.getString("EMAIL_IDS");
    log.info("RMSJOB getInputHistory Email To:::::::" + bcc);
    String[] bcc1 = bcc.split(",");
    log.info("RMSJOB getInputHistory Email To:::::::" + Arrays.toString((Object[])bcc1));
    for (int i = 0; i < bcc1.length; i++) {
      SendAwsSES send = new SendAwsSES();
      log.info("RMSJOB getInputHistory Email Sending.......");
      send.sendMail(bcc1[i], "Input History Report", "Kindly Find Attached", String.valueOf(dir), fileName);
      log.info("RMSJOB getInputHistory Email To:::::::" + bcc1[i]);
    } 
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/MailDGInputHistoryReport"}, produces = {"application/json"})
  public String MailDGInputHistoryReport() throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException {
    ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
    String SEP = System.getProperty("file.separator");
    URL resource = getClass().getResource("/");
    String path = resource.getPath();
    path = path.replace("WEB-INF/classes/", "");
    String dir = String.valueOf(String.valueOf(path)) + "mailDGreport" + SEP;
    System.out.println(dir);
    File file = new File(dir);
    if (!file.exists())
      file.mkdir(); 
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
    String fileName = String.valueOf(getYesterdayDateString()) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
      ".csv";
    log.info("RMSJOB MailDGInputHistoryReport:: " + String.valueOf(dir) + fileName);
    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(dir)) + fileName);
    CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
			CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    List<String[]> data = (List)new ArrayList<>();
    data.add(new String[] { "Device Name", "Group Name", "Input Name", "Report Date", "Active(Minutes)", "Clear(Minutes)" });
    JSONObject dataJson = new JSONObject();
    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
    String[] devices = new String[deviceList.size()];
    if (deviceList.size() != 0)
      for (int analog = 0; analog < deviceList.size(); analog++) {
        Object[] result = deviceList.get(analog);
        devices[analog] = (String)result[0];
      }  
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("RMSJOB MailDGInputHistoryReport getInputHistory " + ds + " :: " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      int openTime = 0;
      int closeTime = 0;
      Site site = null;
      List<AssignSite> aSite = null;
      aSite = this.assSiteService.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      if (aSite.size() != 0)
        site = this.siteService.findBysiteid(((AssignSite)aSite.get(0)).getSiteid()); 
      List<Object[]> lst = this.ihreportService.getIRReport(
          LocalDate.parse(getYesterdayDateString()).toString(), 
          LocalDate.parse(getYesterdayDateString()).toString(), 6348854L, Long.parseLong(devices[ds]));
      if (lst != null)
        for (int j = 0; j < lst.size(); j++) {
          Object[] obj = lst.get(j);
          if (obj[4] != null && 
            obj[4].toString().length() != 0)
            openTime += Integer.parseInt(obj[4].toString()); 
          if (obj[5] != null && 
            obj[5].toString().length() != 0)
            closeTime += Integer.parseInt(obj[5].toString()); 
        }  
      data.add(new String[] { deviceObject.getDevicename(), (site == null) ? "Not Set" : site.getSite_name(), 
            this.prepo.findByid((new Long("6348854")).longValue()).getPrmname(), 
            LocalDate.parse(getYesterdayDateString()).toString(), 
            String.valueOf(openTime), 
            String.valueOf(closeTime) });
    } 
    writer.writeAll(data);
    writer.close();
    String bcc = rb.getString("EMAIL_IDS");
    log.info("RMSJOB MailDGInputHistoryReport Email To:::::::" + bcc);
    String[] bcc1 = bcc.split(",");
    log.info("RMSJOB MailDGInputHistoryReport Email To:::::::" + Arrays.toString((Object[])bcc1));
    for (int i = 0; i < bcc1.length; i++) {
      SendAwsSES send = new SendAwsSES();
      log.info("RMSJOB MailDGInputHistoryReport Email Sending.......");
      send.sendMail(bcc1[i], "DG Running Input History Report", "Kindly Find Attached", String.valueOf(dir), fileName);
      log.info("RMSJOB MailDGInputHistoryReport Email To:::::::" + bcc1[i]);
    } 
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/getDGInputHistory/{startDate}/{endDate}/{paramId}/{device_ids}"}, produces = {"application/json"})
  public String getDGInputHistory(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String paramId, @PathVariable String device_ids) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
    JSONObject dataJson = new JSONObject();
    String[] digitals = paramId.split(",");
    String s = startDate;
    String e = endDate;
    LocalDate start = LocalDate.parse(s);
    LocalDate end = LocalDate.parse(e);
    String[] devices = device_ids.split(",");
    List<LocalDate> totalDates = new ArrayList<>();
    JSONArray mainJSON = new JSONArray();
    while (!start.isAfter(end)) {
    	totalDates.add(start);
    	start = start.plusDays(1);  
    }
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("getInputHistory " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (LocalDate date : totalDates) {
        for (int d = 0; d < digitals.length; d++) {
          List<Object[]> lst = this.ihreportService.getIRReport(date.toString(), 
              date.toString(), 6348854L, Long.parseLong(devices[ds]));
          System.out.println(lst.size());
          if (lst != null)
            for (int i = 0; i < lst.size(); i++) {
              Object[] obj = lst.get(i);
              System.out.println(obj[1].toString());
              JSONObject jo = new JSONObject();
              jo.put("StartDate", obj[0].toString());
              jo.put("deviceDescription", deviceObject.getDevicedescription());
              jo.put("EndDate", obj[1].toString());
              jo.put("StartStatus", obj[2].toString());
              jo.put("Parameter", this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname());
              jo.put("Open", obj[4].toString());
              jo.put("Close", obj[5].toString());
              jo.put("DeviceName", deviceObject.getDevicename());
              mainJSON.put(jo);
            }  
        } 
      } 
    } 
    dataJson.put("data", mainJSON);
    return mainJSON.toString();
  }
  
  @RequestMapping(value = {"/api/getDGInputHistoryReport/{startDate}/{endDate}/{paramId}/{device_ids}"}, produces = {"application/json"})
  public String getDGInputHistoryReport(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String paramId, @PathVariable String device_ids) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
    JSONObject dataJson = new JSONObject();
    String[] digitals = paramId.split(",");
    String s = startDate;
    String e = endDate;
    LocalDate start = LocalDate.parse(s);
    LocalDate end = LocalDate.parse(e);
    String[] devices = device_ids.split(",");
    List<LocalDate> totalDates = new ArrayList<>();
    JSONArray mainJSON = new JSONArray();
    while (!start.isAfter(end)) {
    	totalDates.add(start);
    	start = start.plusDays(1);  
    }
    for (int ds = 0; ds < devices.length; ds++) {
      log.info("getInputHistory " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (LocalDate date : totalDates) {
        for (int d = 0; d < digitals.length; d++) {
          List<Object[]> lst = this.ihreportService.getIRReport(date.toString(), 
              date.toString(), 6348854L, Long.parseLong(devices[ds]));
          System.out.println(lst.size());
          if (lst != null)
            for (int i = 0; i < lst.size(); i++) {
              Object[] obj = lst.get(i);
              System.out.println(obj[1].toString());
              JSONObject jo = new JSONObject();
              jo.put("deviceDescription", deviceObject.getDevicedescription());
              jo.put("StartDate", date.toString());
              jo.put("StartStatus", obj[2].toString());
              jo.put("Parameter", this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname());
              jo.put("open", (obj[4].toString()));
			  jo.put("close", (obj[5].toString()));
              jo.put("DeviceName", deviceObject.getDevicename());
              mainJSON.put(jo);
            }  
        } 
      } 
    } 
    dataJson.put("data", mainJSON);
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/getInputHistoryTest/{startDate}"}, produces = {"application/json"})
  public String getInputHistoryTest(@PathVariable String startDate) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
	  log.info("RMSJOB :: InsertInputHistoryRrport is called......");  
	  String reportDate=startDate;
	    JSONObject dataJson = new JSONObject();
	    JSONArray DataamainJSON = new JSONArray();
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	   if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	   for (int ds = 0; ds < devices.length; ds++) {
	     log.info("RMSJOB doScheduledWork :: Device " + ds);
	     Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	     HashMap<String, List<Object[]>> data = new LinkedHashMap<>();
	        	List<Object[]> Mains_Fail=new ArrayList<Object[]>();
				List<Object[]> Door =new ArrayList<Object[]>();
				List<Object[]> DG_Fault =new ArrayList<Object[]>();
				List<Object[]> PP_Input_Fail=new ArrayList<Object[]>();
				List<Object[]> Fire=new ArrayList<Object[]>();
				List<Object[]> DG_Running=new ArrayList<Object[]>();
				List<Object[]> Battery_Low =new ArrayList<Object[]>();
				List<Object> ALlDigitalList=new ArrayList<Object>();
				final List<Object[]> DataLst = (List<Object[]>)ihreportService.getInputHistoryReportdata(reportDate,
						reportDate, Long.parseLong(devices[ds]));
				if (DataLst != null) {
					for (int i = 0; i < DataLst.size(); ++i) {
						final Object[] obj = DataLst.get(i);
						Object[] Mains_FailObj= {obj[0].toString(),obj[1].toString()};
						Mains_Fail.add(Mains_FailObj);
						data.put("284945", Mains_Fail);
						
						Object[] DoorObj= {obj[0].toString(),obj[2].toString()};
						Door.add(DoorObj);
						data.put("291934", Door);
						
						Object[] DG_FaultObj= {obj[0].toString(),obj[3].toString()};
						DG_Fault.add(DG_FaultObj);
						data.put("6348815", DG_Fault);
						
						Object[] PP_Input_FailObj= {obj[0].toString(),obj[4].toString()};
						PP_Input_Fail.add(PP_Input_FailObj);
						data.put("6348824", PP_Input_Fail);
						
						Object[] FireObj= {obj[0].toString(),obj[5].toString()};
						Fire.add(FireObj);
						data.put("6348798", Fire);
						
						Object[] DG_RunningObj= {obj[0].toString(),obj[6].toString()};
						DG_Running.add(DG_RunningObj);
						data.put("6348854", DG_Running);
						
						Object[] Battery_LowObj= {obj[0].toString(),obj[7].toString()};
						Battery_Low.add(Battery_LowObj);
						data.put("6348821", Battery_Low);
					}
				}
	          if (ALlDigitalList != null) {
	        	  for( Map.Entry<String, List<Object[]>> mapData : data.entrySet()) {
						List<Object[]> lst=(List<Object[]>) mapData.getValue();
						final JSONArray mainJSON = new JSONArray();
	            String lstatus = null;
	            if (lst != null) {
	            int ct = 0;
	            for (int i = 0; i < lst.size(); i++) {
	              Object[] obj = lst.get(i);
	              if (i == 0) {
	                lstatus = obj[1].toString();
	                JSONObject jo = new JSONObject();
	                jo.put("Start Date", obj[0].toString());
	                jo.put("Start Status", obj[1].toString());
	                mainJSON.put(ct, jo);
	                ct++;
	              } else {
	                if (!lstatus.equalsIgnoreCase(obj[1].toString())) {
	                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
	                  innerJSON12.put("End Date", obj[0].toString());
	                  mainJSON.put(ct - 1, innerJSON12);
	                  JSONObject jo2 = new JSONObject();
	                  jo2.put("Start Date", obj[0].toString());
	                  jo2.put("Start Status", obj[1].toString());
	                  mainJSON.put(ct, jo2);
	                  ct++;
	                } 
	                lstatus = obj[1].toString();
	              } 
	            } 
	          
	          for (int j = 0; j < mainJSON.length(); j++) {
	            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
	            jsonObject1.put("Parameter", mapData.getKey());
	            if (j != mainJSON.length() - 1) {
	              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
	              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
	              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
	              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	                jsonObject1.put("Open", "");
	              } else {
	                jsonObject1.put("Close", "");
	                jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	              } 
	            } else {
	              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
	              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
	              Date date21 = df1.parse(reportDate.toString());
	              Date date22 = df1.parse(df1.format(new Date()));
	              int date23 = date21.compareTo(date22);
	              if (date23 == 0) {
	                jsonObjectLast.put("End Date", df.format(new Date()));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {//1=Close,0=Open
	                  jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                } 
	              } else if (date23 == -1) {
	                jsonObjectLast.put("End Date", df
	                    .format(df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                } 
	              } 
	            } 
	            
	            jsonObject1.put("DeviceName", deviceObject.getDevicename());
	            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
	            DataamainJSON.put(jsonObject1);
	           IHReport report = new IHReport();
	            report.setClose(jsonObject1.get("Close").toString());
	            report.setDeviceId(deviceObject.getDeviceid().longValue());
	            report.setEndDate(jsonObject1.get("End Date").toString());
	            report.setEntryDate(df.parse(String.valueOf(reportDate) + " 00:00:00"));
	            report.setOpen(jsonObject1.get("Open").toString());
	            report.setParameterId((new Long(mapData.getKey()).longValue()));
	            report.setStartDate(jsonObject1.get("Start Date").toString());
	            report.setStartStatus(jsonObject1.get("Start Status").toString());
	            this.ihreportService.saveReport(report);  
	            dataJson.put("data", DataamainJSON);
	          } 
	        	  
		        }
	        } 
	          }
	    	}
	    log.info("RMSJOB doScheduledWork :: END TASK " );
		return reportDate;
  }
  
  @RequestMapping(value = {"/api/getInputHistoryInfraTest/{startDate}"}, produces = {"application/json"})
  public String getInputHistoryInfraTest(@PathVariable String startDate) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
	  log.info("RMSJOB :: InsertInputHistoryRrport is called......");  
	  String reportDate=startDate;
	    JSONObject dataJson = new JSONObject();
	    JSONArray DataamainJSON = new JSONArray();
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(1156892664L));
	    String[] devices = new String[deviceList.size()];
	   if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	   for (int ds = 0; ds < devices.length; ds++) {
	     log.info("RMSJOB doScheduledWork :: Device " + ds);
	     Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	     HashMap<String, List<Object[]>> data = new LinkedHashMap<>();
	        	List<Object[]> Mains_Fail=new ArrayList<Object[]>();
				List<Object[]> Door =new ArrayList<Object[]>();
				List<Object[]> DG_Fault =new ArrayList<Object[]>();
				List<Object[]> PP_Input_Fail=new ArrayList<Object[]>();
				List<Object[]> Fire=new ArrayList<Object[]>();
				List<Object[]> DG_Running=new ArrayList<Object[]>();
				List<Object[]> Battery_Low =new ArrayList<Object[]>();
				List<Object> ALlDigitalList=new ArrayList<Object>();
				final List<Object[]> DataLst = (List<Object[]>)ihreportService.getInputHistoryReportdata(reportDate,
						reportDate, Long.parseLong(devices[ds]));
				if (DataLst != null) {
					for (int i = 0; i < DataLst.size(); ++i) {
						final Object[] obj = DataLst.get(i);
						Object[] Mains_FailObj= {obj[0].toString(),obj[1].toString()};
						Mains_Fail.add(Mains_FailObj);
						data.put("284945", Mains_Fail);
						
						/*Object[] DoorObj= {obj[0].toString(),obj[2].toString()};
						Door.add(DoorObj);
						data.put("291934", Door);
						
						Object[] DG_FaultObj= {obj[0].toString(),obj[3].toString()};
						DG_Fault.add(DG_FaultObj);
						data.put("6348815", DG_Fault);
						
						Object[] PP_Input_FailObj= {obj[0].toString(),obj[4].toString()};
						PP_Input_Fail.add(PP_Input_FailObj);
						data.put("6348824", PP_Input_Fail);
						
						Object[] FireObj= {obj[0].toString(),obj[5].toString()};
						Fire.add(FireObj);
						data.put("6348798", Fire);
						
						Object[] DG_RunningObj= {obj[0].toString(),obj[6].toString()};
						DG_Running.add(DG_RunningObj);
						data.put("6348854", DG_Running);
						
						Object[] Battery_LowObj= {obj[0].toString(),obj[7].toString()};
						Battery_Low.add(Battery_LowObj);
						data.put("6348821", Battery_Low);*/
					}
				}
	          if (ALlDigitalList != null) {
	        	  for( Map.Entry<String, List<Object[]>> mapData : data.entrySet()) {
						List<Object[]> lst=(List<Object[]>) mapData.getValue();
						final JSONArray mainJSON = new JSONArray();
	            String lstatus = null;
	            if (lst != null) {
	            int ct = 0;
	            for (int i = 0; i < lst.size(); i++) {
	              Object[] obj = lst.get(i);
	              if (i == 0) {
	                lstatus = obj[1].toString();
	                JSONObject jo = new JSONObject();
	                jo.put("Start Date", obj[0].toString());
	                jo.put("Start Status", obj[1].toString());
	                mainJSON.put(ct, jo);
	                ct++;
	              } else {
	                if (!lstatus.equalsIgnoreCase(obj[1].toString())) {
	                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
	                  innerJSON12.put("End Date", obj[0].toString());
	                  mainJSON.put(ct - 1, innerJSON12);
	                  JSONObject jo2 = new JSONObject();
	                  jo2.put("Start Date", obj[0].toString());
	                  jo2.put("Start Status", obj[1].toString());
	                  mainJSON.put(ct, jo2);
	                  ct++;
	                } 
	                lstatus = obj[1].toString();
	              } 
	            } 
	          
	          for (int j = 0; j < mainJSON.length(); j++) {
	            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
	            jsonObject1.put("Parameter", mapData.getKey());
	            if (j != mainJSON.length() - 1) {
	              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
	              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
	              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
	              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	                jsonObject1.put("Open", "");
	              } else {
	                jsonObject1.put("Close", "");
	                jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	              } 
	            } else {
	              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
	              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
	              Date date21 = df1.parse(reportDate.toString());
	              Date date22 = df1.parse(df1.format(new Date()));
	              int date23 = date21.compareTo(date22);
	              if (date23 == 0) {
	                jsonObjectLast.put("End Date", df.format(new Date()));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {//1=Close,0=Open
	                  jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                } 
	              } else if (date23 == -1) {
	                jsonObjectLast.put("End Date", df
	                    .format(df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                } 
	              } 
	            } 
	            
	            jsonObject1.put("DeviceName", deviceObject.getDevicename());
	            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
	            DataamainJSON.put(jsonObject1);
	           IHReport report = new IHReport();
	            report.setClose(jsonObject1.get("Close").toString());
	            report.setDeviceId(deviceObject.getDeviceid().longValue());
	            report.setEndDate(jsonObject1.get("End Date").toString());
	            report.setEntryDate(df.parse(String.valueOf(reportDate) + " 00:00:00"));
	            report.setOpen(jsonObject1.get("Open").toString());
	            report.setParameterId((new Long(mapData.getKey()).longValue()));
	            report.setStartDate(jsonObject1.get("Start Date").toString());
	            report.setStartStatus(jsonObject1.get("Start Status").toString());
	            this.ihreportService.saveReport(report);  
	            dataJson.put("data", DataamainJSON);
	          } 
	        	  
		        }
	        } 
	          }
	    	}
	    log.info("RMSJOB doScheduledWork :: END TASK " );
		return reportDate;
  }
  public static String convertMinutesToTime(int totalMinutes) {
    int totalSeconds = totalMinutes * 60;
    int hours = totalSeconds / 3600;
    int remainingSeconds = totalSeconds % 3600;
    int minutes = remainingSeconds / 60;
    int seconds = remainingSeconds % 60;
    System.out.println(hours);
    System.out.printf("%d minutes is equivalent to: %d hour(s), %d minute(s), %d second(s)%n", new Object[] { Integer.valueOf(totalMinutes), Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds) });
    return String.valueOf(hours) + " Hour " + minutes + " Min" + " " + seconds + " Sec";
  }
  
  @RequestMapping(value = {"/api/MailMonthlyDGrunningActiveMinutes"}, produces = {"application/json"})
  public String MailMonthlyDGrunningActiveMinutes() throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException {
    JSONObject dataJson = new JSONObject();
    String SEP = System.getProperty("file.separator");
    URL resource = getClass().getResource("/");
    String path = resource.getPath();
    path = path.replace("WEB-INF/classes/", "");
    String dir = String.valueOf(String.valueOf(path)) + "maildailyreport" + SEP;
    System.out.println(dir);
    File file = new File(dir);
    if (!file.exists())
      file.mkdir(); 
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
    String fileName = String.valueOf(getYesterdayDateString()) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
      ".csv";
    log.info(String.valueOf(String.valueOf(dir)) + fileName);
    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(dir)) + fileName);
    CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
			CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    List<String[]> data = (List)new ArrayList<>();
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM");
    String[] heade = { "Device Name", "Zone", "Site ID" };
    LocalDate start = YearMonth.now().atDay(1);
    LocalDate end = YearMonth.now().atEndOfMonth();
    List<String> totalDates = new ArrayList<>();
    List<String> QueryDates = new ArrayList<>();
    while (!start.isAfter(end)) {
      totalDates.add(formatter.format(df1.parse(start.toString())));
      QueryDates.add(start.toString());
      start = start.plusDays(1L);
    } 
    totalDates.add("MTD");
    String[] stringArray = totalDates.<String>toArray(new String[0]);
    List headelist = new ArrayList(Arrays.asList((Object[])heade));
    headelist.addAll(Arrays.asList(stringArray));
    String[] header = (String[])headelist.toArray((Object[])new String[0]);
    System.out.println(Arrays.toString((Object[])header));
    data.add(header);
    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
    String[] devices = new String[deviceList.size()];
    if (deviceList.size() != 0)
      for (int analog = 0; analog < deviceList.size(); analog++) {
        Object[] result = deviceList.get(analog);
        devices[analog] = (String)result[0];
      }  
    for (int ds = 0; ds < devices.length; ds++) {
      int MTD = 0;
      Site site = null;
      List<AssignSite> aSite = null;
      aSite = this.assSiteService.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      if (aSite.size() != 0)
        site = this.siteService.findBysiteid(((AssignSite)aSite.get(0)).getSiteid()); 
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      List<String> dataList = new ArrayList<>();
      dataList.add(deviceObject.getDevicename());
      dataList.add((site == null) ? "Not Set" : site.getSite_name());
      dataList.add(deviceObject.getAltdevicename());
      for (String entryDate : QueryDates) {
        List<Object[]> lst = this.ihreportService.getIRReport(entryDate, 
            entryDate, 6348854L, Long.parseLong(devices[ds]));
        int active = 0;
        if (lst != null)
          for (int i = 0; i < lst.size(); i++) {
            Object[] obj = lst.get(i);
            int hour = 0;
            if (obj[4] != null && 
              !obj[4].toString().equalsIgnoreCase(""))
              hour = Integer.parseInt(obj[4].toString()); 
            active += hour;
            MTD += hour;
          }  
        dataList.add(String.valueOf(active));
        System.out.println(String.valueOf(devices[ds]) + " :: " + entryDate + " :: " + dataList.toString());
      } 
      dataList.add(String.valueOf(MTD));
      System.out.println(dataList.toString());
      data.add(dataList.<String>toArray(new String[0]));
    } 
    writer.writeAll(data);
    writer.close();
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/SendMailInputHistoryRrportTest"}, produces = {"application/json"})
  public String SendMailInputHistoryRrportTest() throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException {
	  log.info("RMSJOB :: SendMailInputHistoryRrport is called......");
	    ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");   
	    String SEP = System.getProperty("file.separator");
	    URL resource = getClass().getResource("/");
	    String path = resource.getPath();
	    path = path.replace("WEB-INF/classes/", "");
	    String dir = String.valueOf(String.valueOf(path)) + "maildailyreport" + SEP;
	    System.out.println(dir);
	    File file = new File(dir);
	    if (!file.exists())
	      file.mkdir(); 
	    Date date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
	    String fileName = String.valueOf(getYesterdayDateString()) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
	      ".csv";
	    log.info("RMSJOB SendMailInputHistoryRrport :: " + String.valueOf(dir) + fileName);
	    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(dir)) + fileName);
	    CSVWriter writer = new CSVWriter(outputfile, ',', '"', "\n");
	    List<String[]> data = (List)new ArrayList<>();
	    data.add(new String[] { "Device Name", "Group Name", "Device Description", "Input Name", "Start Time", "End Time", 
	          "Active(Minutes)", "Clear(Minutes)" });
	    String paramId = "284945,6348798,291934,6348854,6348815,6348821,6348824";
	    String[] digitals = paramId.split(",");
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	    if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	    log.info("RMSJOB SendMailInputHistoryRrport :: deviceList.size() " + deviceList.size());
	    for (int ds = 0; ds < devices.length; ds++) {
	      log.info("RMSJOB ::  getInputHistory " + devices[ds]);
	      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	      for (int d = 0; d < digitals.length; d++) {
	        Site site = null;
	        List<AssignSite> aSite = null;
	        aSite = this.assSiteService.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	        if (aSite.size() != 0)
	          site = this.siteService.findBysiteid(((AssignSite)aSite.get(0)).getSiteid()); 
	        List<Object[]> lst = this.ihreportService.getIRReport(getYesterdayDateString(), 
	            getYesterdayDateString(), Long.parseLong(digitals[d]), Long.parseLong(devices[ds]));
	        if (lst != null)
	          for (int j = 0; j < lst.size(); j++) {
	            Object[] obj = lst.get(j);
	            data.add(
	                new String[] { deviceObject.getDevicename(), 
	                  (site == null) ? "Not Set" : site.getSite_name(), 
	                  deviceObject.getDevicedescription(), 
	                  this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname(), 
	                  obj[0].toString(), 
	                  obj[1].toString(), 
	                  (obj[4] == null) ? "00" : obj[4].toString(), 
	                  (obj[5] == null) ? "00" : obj[5].toString() });
	          }  
	      } 
	    } 
	    writer.writeAll(data);
	    writer.close();
	    String bcc = rb.getString("EMAIL_IDS");
	    log.info("RMSJOB getInputHistory Email To:::::::" + bcc);
	    String[] bcc1 = bcc.split(",");
	    log.info("RMSJOB getInputHistory Email To:::::::" + Arrays.toString((Object[])bcc1));
	    for (int i = 0; i < bcc1.length; i++) {
	    	GmailEmailSender email = new GmailEmailSender();
	      log.info("RMSJOB getInputHistory Email Sending.......");
	   //   send.sendMail(bcc1[i], "Input History Report", "Kindly Find Attached", String.valueOf(dir), fileName);
	      email.EmailSystem(bcc1[i], "Input History Report", "Kindly Find Attached", String.valueOf(dir) + fileName);
	      log.info("RMSJOB getInputHistory Email To:::::::" + bcc1[i]);
	    } 
	    return "Success";
}
  
  @Async
  @RequestMapping(value = {"/api/NewInputHistoryRrportTest/{reportDate}"}, produces = {"application/json"})
  public String NewInputHistoryRrportTest(@PathVariable String reportDate) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException {
	  log.info("RMSJOB :: SendMailInputHistoryRrport is called......");

	    log.info("RMSJOB :: doScheduledWork is called......");
	    JSONObject dataJson = new JSONObject();
	    String paramId = "284945,6348798,291934,6348854,6348815,6348821,6348824";
	    String[] digitals = paramId.split(",");
	    JSONArray DataamainJSON = new JSONArray();
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	   if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	   for (int ds = 0; ds < devices.length; ds++) {
	     log.info("RMSJOB doScheduledWork :: Device " + ds);
	     Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	     HashMap<String, List<Object[]>> data = new LinkedHashMap<>();
	        	List<Object[]> Mains_Fail=new ArrayList<Object[]>();
				List<Object[]> Door =new ArrayList<Object[]>();
				List<Object[]> DG_Fault =new ArrayList<Object[]>();
				List<Object[]> PP_Input_Fail=new ArrayList<Object[]>();
				List<Object[]> Fire=new ArrayList<Object[]>();
				List<Object[]> DG_Running=new ArrayList<Object[]>();
				List<Object[]> Battery_Low =new ArrayList<Object[]>();
				List<Object> ALlDigitalList=new ArrayList<Object>();
				final List<Object[]> DataLst = (List<Object[]>)ihreportService.getInputHistoryReportdata(reportDate,
						reportDate, Long.parseLong(devices[ds]));
				if (DataLst != null) {
					for (int i = 0; i < DataLst.size(); ++i) {
						final Object[] obj = DataLst.get(i);
						Object[] Mains_FailObj= {obj[0].toString(),obj[1].toString()};
						Mains_Fail.add(Mains_FailObj);
						data.put("284945", Mains_Fail);
						
						Object[] DoorObj= {obj[0].toString(),obj[2].toString()};
						Door.add(DoorObj);
						data.put("291934", Door);
						
						Object[] DG_FaultObj= {obj[0].toString(),obj[3].toString()};
						DG_Fault.add(DG_FaultObj);
						data.put("6348815", DG_Fault);
						
						Object[] PP_Input_FailObj= {obj[0].toString(),obj[4].toString()};
						PP_Input_Fail.add(PP_Input_FailObj);
						data.put("6348824", PP_Input_Fail);
						
						Object[] FireObj= {obj[0].toString(),obj[5].toString()};
						Fire.add(FireObj);
						data.put("6348798", Fire);
						
						Object[] DG_RunningObj= {obj[0].toString(),obj[6].toString()};
						DG_Running.add(DG_RunningObj);
						data.put("6348854", DG_Running);
						
						Object[] Battery_LowObj= {obj[0].toString(),obj[7].toString()};
						Battery_Low.add(Battery_LowObj);
						data.put("6348821", Battery_Low);
					}
				}
	          if (ALlDigitalList != null) {
	        	  for( Map.Entry<String, List<Object[]>> mapData : data.entrySet()) {
						List<Object[]> lst=(List<Object[]>) mapData.getValue();
						final JSONArray mainJSON = new JSONArray();
	            String lstatus = null;
	            if (lst != null) {
	            int ct = 0;
	            for (int i = 0; i < lst.size(); i++) {
	              Object[] obj = lst.get(i);
	              if (i == 0) {
	                lstatus = obj[1].toString();
	                JSONObject jo = new JSONObject();
	                jo.put("Start Date", obj[0].toString());
	                jo.put("Start Status", obj[1].toString());
	                mainJSON.put(ct, jo);
	                ct++;
	              } else {
	                if (!lstatus.equalsIgnoreCase(obj[1].toString())) {
	                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
	                  innerJSON12.put("End Date", obj[0].toString());
	                  mainJSON.put(ct - 1, innerJSON12);
	                  JSONObject jo2 = new JSONObject();
	                  jo2.put("Start Date", obj[0].toString());
	                  jo2.put("Start Status", obj[1].toString());
	                  mainJSON.put(ct, jo2);
	                  ct++;
	                } 
	                lstatus = obj[1].toString();
	              } 
	            } 
	            
	          
	          
	          for (int j = 0; j < mainJSON.length(); j++) {
	            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
	            jsonObject1.put("Parameter", mapData.getKey());
	            if (j != mainJSON.length() - 1) {
	              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
	              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
	              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
	              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	                jsonObject1.put("Open", "");
	              } else {
	                jsonObject1.put("Close", "");
	                jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	              } 
	            } else {
	              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
	              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
	              Date date21 = df1.parse(reportDate.toString());
	              Date date22 = df1.parse(df1.format(new Date()));
	              int date23 = date21.compareTo(date22);
	              if (date23 == 0) {
	                jsonObjectLast.put("End Date", df.format(new Date()));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {//1=Close,0=Open
	                  jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                } 
	              } else if (date23 == -1) {
	                jsonObjectLast.put("End Date", df
	                    .format(df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                } 
	              } 
	            } 
	            
	            
	            jsonObject1.put("DeviceName", deviceObject.getDevicename());
	            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
	            DataamainJSON.put(jsonObject1);
	           IHReport report = new IHReport();
	            report.setClose(jsonObject1.get("Close").toString());
	            report.setDeviceId(deviceObject.getDeviceid().longValue());
	            report.setEndDate(jsonObject1.get("End Date").toString());
	            report.setEntryDate(df.parse(String.valueOf(reportDate) + " 00:00:00"));
	            report.setOpen(jsonObject1.get("Open").toString());
	            report.setParameterId((new Long(mapData.getKey()).longValue()));
	            report.setStartDate(jsonObject1.get("Start Date").toString());
	            report.setStartStatus(jsonObject1.get("Start Status").toString());
	            this.ihreportService.saveReport(report);  
	            dataJson.put("data", DataamainJSON);
	          } 
	        	  
		        }
	        } 
	          }
	   // } 
	    	}
	   // log.info("RMSJOB doScheduledWork :: DataamainJSON " + DataamainJSON.toString());
	   
	  
	    return dataJson.toString();
}
  
  @RequestMapping(value = {"/api/TestDGMail/{reportDate}"}, produces = {"application/json"})
  public String TestDGMail(@PathVariable String reportDate) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException, MessagingException, InterruptedException {
	    log.info("RMSJOB :: SendMailDGInputHistoryREport TestDGMail is called......");
	    String SEP = System.getProperty("file.separator");
	    URL resource = getClass().getResource("/");
	    String path = resource.getPath();
	    path = path.replace("WEB-INF/classes/", "");
	    String dir = String.valueOf(String.valueOf(path)) + "maildailyreport" + SEP;
	  // System.out.println(dir);
	    File file = new File(dir);
	    if (!file.exists())
	      file.mkdir(); 
	    Date date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
	    String fileName = String.valueOf(reportDate) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
	      ".csv";
	   // log.info(String.valueOf(String.valueOf(dir)) + fileName);
	    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(dir)) + fileName);
	    CSVWriter writer = new CSVWriter(outputfile);
	    List<String[]> data = (List)new ArrayList<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM");
	    String[] heade = { "Device Name", "Zone", "Site ID" };
	    java.time.LocalDate start = YearMonth.now().atDay(1);
	    java.time.LocalDate end = YearMonth.now().atEndOfMonth();
	    List<String> totalDates = new ArrayList<>();
	    List<String> QueryDates = new ArrayList<>();
	    while (!start.isAfter(end)) {
	      totalDates.add(formatter.format(df1.parse(start.toString())));
	      QueryDates.add(start.toString());
	      start = start.plusDays(1L);
	    } 
	    totalDates.add("MTD");
	    String[] stringArray = totalDates.<String>toArray(new String[0]);
	    List headelist = new ArrayList(Arrays.asList((Object[])heade));
	    headelist.addAll(Arrays.asList(stringArray));
	    String[] header = (String[])headelist.toArray((Object[])new String[0]);
	    data.add(header);
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	    if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;;
	      }  
	    for (int ds = 0; ds < devices.length; ds++) {
	      int MTD = 0;
	      Site site = null;
	      List<AssignSite> aSite = null;
	      aSite = this.assSiteService.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	      if (aSite.size() != 0)
	        site = this.siteService.findBysiteid(((AssignSite)aSite.get(0)).getSiteid()); 
	      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	      List<String> dataList = new ArrayList<>();
	      dataList.add(deviceObject.getDevicename());
	      dataList.add((site == null) ? "Not Set" : site.getSite_name());
	      dataList.add(deviceObject.getAltdevicename());
	      for (String entryDate : QueryDates) {
	        List<Object[]> lst = this.ihreportService.getIRReport(entryDate, 
	            entryDate, 6348854L, Long.parseLong(devices[ds]));
	        int active = 0;
	          for (int j = 0; j < lst.size(); j++) {
	            Object[] obj = lst.get(j);
	            
	            double hour = 0;
	            if (obj[4] != null && !obj[4].toString().equalsIgnoreCase("")) {
	            	 Pattern pattern = Pattern.compile("(\\d+) Days: (\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
	                 Matcher matcher = pattern.matcher(obj[4].toString());
	                 if (matcher.find()) {
	                     String hours = matcher.group(2);
	                     String minutes = matcher.group(3);
	                     String seconds = matcher.group(4);
	                     hour = convertToTotalHours(Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds)); 
	                 }
	            }  
	            active += hour;
	            MTD += hour;
	          }  
	        dataList.add(String.valueOf(active));
	      } 
	      dataList.add(String.valueOf(MTD));
	      data.add(dataList.<String>toArray(new String[0]));
	    } 
	    writer.writeAll(data);
	    writer.close();
	    ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
	    String bcc = rb.getString("EMAIL_IDS");
	    String[] bcc1 = bcc.split(",");
	    for (int i = 0; i < bcc1.length; i++) {
		    SendMailTest(bcc1[i], String.valueOf(dir) + fileName,"DG Running Input History Report","Kindly Find Attached");
	     // TimeUnit.MINUTES.sleep(1);
	    }
		return "Success....."; 
}  
  
  //getInputHistoryByProfile
  @RequestMapping(value = {"/api/getInputHistoryBYProfile/{startDate}/{endDate}/{profileId}"}, produces = {"application/json"})
  public String getInputHistoryBYProfile(@PathVariable String startDate, @PathVariable String endDate, @PathVariable Long profileId) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
    JSONObject dataJson = new JSONObject();
    String[] digitals =DpService.getparameterIdByProfile(profileId);
   
    String s = startDate;
    String e = endDate;
    LocalDate start = LocalDate.parse(s);
    LocalDate end = LocalDate.parse(e);
   
    String[] devices=DpService.getDeviceIdsByProfile(profileId);
    List<LocalDate> totalDates = new ArrayList<>();
  
    JSONArray mainJSON = new JSONArray();
    while (!start.isAfter(end)) {
    	totalDates.add(start);
    	start = start.plusDays(1);  
    }
    for (int ds = 0; ds < devices.length; ds++) {
     // log.info("getInputHistory " + devices[ds]);
      Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
      for (LocalDate date : totalDates) {
        for (int d = 0; d < digitals.length; d++) {
          List<Object[]> lst = this.ihreportService.getIRReport(date.toString(), 
              date.toString(), Long.parseLong(digitals[d]), Long.parseLong(devices[ds]));
         // System.out.println(lst.size());
          if (lst != null)
            for (int i = 0; i < lst.size(); i++) {
              Object[] obj = lst.get(i);
              JSONObject jo = new JSONObject();
              jo.put("Start Date", obj[0].toString());
              jo.put("deviceDescription", deviceObject.getDevicedescription());
              jo.put("End Date", obj[1].toString());
              jo.put("Start Status", obj[2].toString());
              jo.put("Parameter", this.prepo.findByid((new Long(obj[3].toString())).longValue()).getPrmname());
              jo.put("Open", (obj[4].toString()));
              jo.put("Close", (obj[5].toString()));
            /*  jo.put("Open", (obj[4].toString().length() > 0) ? convertMinutesToTime(Integer.parseInt(obj[4].toString())) : "");
              jo.put("Close", (obj[5].toString().length() > 0) ? convertMinutesToTime(Integer.parseInt(obj[5].toString())) : "");*/
              jo.put("DeviceName", deviceObject.getDevicename());
              mainJSON.put(jo);
            }  
        } 
      } 
    } 
    dataJson.put("data", mainJSON);
    return dataJson.toString();
  }
  
  @RequestMapping(value = {"/api/saveInputHistory/{startDate}"}, produces = {"application/json"})
  public String saveInputHistory(@PathVariable String startDate) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
	  log.info("RMSJOB :: InsertInputHistoryRrport is called......");  
	  String reportDate=startDate;
	    JSONObject dataJson = new JSONObject();
	    JSONArray DataamainJSON = new JSONArray();
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	   if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	   for (int ds = 0; ds < devices.length; ds++) {
	     log.info("RMSJOB doScheduledWork :: Device " + ds);
	     Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
	     HashMap<String, List<Object[]>> data = new LinkedHashMap<>();
	        	List<Object[]> Mains_Fail=new ArrayList<Object[]>();
				List<Object[]> Door =new ArrayList<Object[]>();
				List<Object[]> DG_Fault =new ArrayList<Object[]>();
				List<Object[]> PP_Input_Fail=new ArrayList<Object[]>();
				List<Object[]> Fire=new ArrayList<Object[]>();
				List<Object[]> DG_Running=new ArrayList<Object[]>();
				List<Object[]> Battery_Low =new ArrayList<Object[]>();
				List<Object> ALlDigitalList=new ArrayList<Object>();
				final List<Object[]> DataLst = (List<Object[]>)ihreportService.getInputHistoryReportdata(reportDate,
						reportDate, Long.parseLong(devices[ds]));
				if (DataLst != null) {
					for (int i = 0; i < DataLst.size(); ++i) {
						final Object[] obj = DataLst.get(i);
						Object[] Mains_FailObj= {obj[0].toString(),obj[1].toString()};
						Mains_Fail.add(Mains_FailObj);
						data.put("284945", Mains_Fail);
						
						Object[] DoorObj= {obj[0].toString(),obj[2].toString()};
						Door.add(DoorObj);
						data.put("291934", Door);
						
						Object[] DG_FaultObj= {obj[0].toString(),obj[3].toString()};
						DG_Fault.add(DG_FaultObj);
						data.put("6348815", DG_Fault);
						
						Object[] PP_Input_FailObj= {obj[0].toString(),obj[4].toString()};
						PP_Input_Fail.add(PP_Input_FailObj);
						data.put("6348824", PP_Input_Fail);
						
						Object[] FireObj= {obj[0].toString(),obj[5].toString()};
						Fire.add(FireObj);
						data.put("6348798", Fire);
						
						Object[] DG_RunningObj= {obj[0].toString(),obj[6].toString()};
						DG_Running.add(DG_RunningObj);
						data.put("6348854", DG_Running);
						
						Object[] Battery_LowObj= {obj[0].toString(),obj[7].toString()};
						Battery_Low.add(Battery_LowObj);
						data.put("6348821", Battery_Low);
					}
				}   
	          if (ALlDigitalList != null) {
	        	  for( Map.Entry<String, List<Object[]>> mapData : data.entrySet()) {
						List<Object[]> lst=(List<Object[]>) mapData.getValue();
						final JSONArray mainJSON = new JSONArray();
	            String lstatus = null;
	            if (lst != null) {
	            int ct = 0;
	            for (int i = 0; i < lst.size(); i++) {
	              Object[] obj = lst.get(i);
	              if (i == 0) {
	                lstatus = obj[1].toString();
	                JSONObject jo = new JSONObject();
	                jo.put("Start Date", obj[0].toString());
	                jo.put("Start Status", obj[1].toString());
	                mainJSON.put(ct, jo);
	                ct++;
	              } else {
	                if (!lstatus.equalsIgnoreCase(obj[1].toString())) {
	                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
	                  innerJSON12.put("End Date", obj[0].toString());
	                  mainJSON.put(ct - 1, innerJSON12);
	                  JSONObject jo2 = new JSONObject();
	                  jo2.put("Start Date", obj[0].toString());
	                  jo2.put("Start Status", obj[1].toString());
	                  mainJSON.put(ct, jo2);
	                  ct++;
	                } 
	                lstatus = obj[1].toString();
	              } 
	            } 
	          
	          for (int j = 0; j < mainJSON.length(); j++) {
	            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
	            jsonObject1.put("Parameter", mapData.getKey());
	            if (j != mainJSON.length() - 1) {
	              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
	              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
	              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
	              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	                jsonObject1.put("Open", "");
	              } else {
	                jsonObject1.put("Close", "");
	                jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	              } 
	            } else {
	              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
	              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
	              Date date21 = df1.parse(reportDate.toString());
	              Date date22 = df1.parse(df1.format(new Date()));
	              int date23 = date21.compareTo(date22);
	              if (date23 == 0) {
	                jsonObjectLast.put("End Date", df.format(new Date()));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {//1=Close,0=Open
	                  jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                } 
	              } else if (date23 == -1) {
	                jsonObjectLast.put("End Date", df
	                    .format(df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
	                        df.parse(df.format(
	                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
	                } 
	              } 
	            } 
	            
	            jsonObject1.put("DeviceName", deviceObject.getDevicename());
	            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
	            DataamainJSON.put(jsonObject1);
	           IHReport report = new IHReport();
	            report.setClose(jsonObject1.get("Close").toString());
	            report.setDeviceId(deviceObject.getDeviceid().longValue());
	            report.setEndDate(jsonObject1.get("End Date").toString());
	            report.setEntryDate(df.parse(String.valueOf(reportDate) + " 00:00:00"));
	            report.setOpen(jsonObject1.get("Open").toString());
	            report.setParameterId((new Long(mapData.getKey()).longValue()));
	            report.setStartDate(jsonObject1.get("Start Date").toString());
	            report.setStartStatus(jsonObject1.get("Start Status").toString());
	            this.ihreportService.saveReport(report);  
	            dataJson.put("data", DataamainJSON);
	          } 
	        	  
		        }
	        } 
	          }
	    	}
	    log.info("RMSJOB doScheduledWork :: END TASK " );
		return reportDate;
  }

   public static void SendMailTest(String to,String file,String subject,String body)
   {
	   final String username = "saharshalarmalert@gmail.com";//change accordingly
	      final String password = "mxfcllvhmtrtpbef";//change accordingly

	      Properties props = new Properties();
	      props.put("mail.smtp.host", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	      props.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Recipient's email address
        message.setSubject(subject);
        
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        multipart.addBodyPart(messageBodyPart);
        
        // Attach a file
        messageBodyPart = new MimeBodyPart();
        String filename = file;
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Set the message content
        message.setContent(multipart);

        Transport.send(message);

        System.out.println("Email sent successfully!");

    } catch (MessagingException e) {
        throw new RuntimeException(e);
    }
   }
  
  public static double convertToTotalHours(int hours, int minutes, int seconds) {
      double hoursFromMinutes = minutes / 60.0;
      
      double hoursFromSeconds = seconds / 3600.0;

      return hours + hoursFromMinutes + hoursFromSeconds;
  }
  
  public static String getYesterdayDateString() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    cal.add(5, -1);
    return dateFormat.format(cal.getTime());
  }
  
  public static String SumofTIME(String[] time) {
    int hours = 0, minutes = 0, seconds = 0;
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = time).length, b = 0; b < i; ) {
      String string = arrayOfString[b];
      String[] temp = string.split(":");
      hours += Integer.valueOf(temp[0]).intValue();
      minutes += Integer.valueOf(temp[1]).intValue();
      seconds += Integer.valueOf(temp[2]).intValue();
      b++;
    } 
    if (seconds == 60) {
      minutes++;
      seconds = 0;
    } else if (seconds > 59) {
      minutes += seconds / 60;
      seconds %= 60;
    } 
    if (minutes == 60) {
      hours++;
      minutes = 0;
    } else if (minutes > 59) {
      hours += minutes / 60;
      minutes %= 60;
    } 
    String output = "";
    output = String.valueOf(hours);
    output = output.concat(" Hour : " + ((String.valueOf(minutes).length() == 1) ? ("0" + String.valueOf(minutes)) : String.valueOf(minutes)));
    output = output.concat(" Minutes : " + ((String.valueOf(seconds).length() == 1) ? ("0" + String.valueOf(seconds)) : (String.valueOf(String.valueOf(seconds)) + " Seconds")));
    return output;
  }
}

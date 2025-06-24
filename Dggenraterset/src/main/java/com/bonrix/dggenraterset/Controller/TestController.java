package com.bonrix.dggenraterset.Controller;
import com.bonrix.common.utils.CallAPI;
import com.bonrix.dggenraterset.Controller.TestController;
import com.bonrix.dggenraterset.DTO.LogObject;
import com.bonrix.dggenraterset.Model.AlearSummary;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.IHReport;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.AlearSummaryService;
import com.bonrix.dggenraterset.Service.Alertmessageshistory;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.IHReportService;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Utility.GmailEmailSender;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.bonrix.dggenraterset.Utility.RMSEmailSystem;
import com.bonrix.dggenraterset.Utility.StringToolsV3;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import java.time.LocalTime;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@Transactional
@RestController
public class TestController {
  private static final Logger log = Logger.getLogger(TestController.class);
  
  static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
  
  SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
  
  NumberFormat formatter = new DecimalFormat("#0.000");
  SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
  
  String sendTmie = "";
  
  String rulrId = "0";
  
  String deviceName = "";
  
  @Autowired
	ParameterRepository reo;
  
  @Autowired
	LasttrackServices lasttrackservices;
  
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
  SiteServices siteService;
  
  @Autowired
  AssignSiteService assSiteService;
  @Autowired
  LasttrackRepository lasttrackrepository;
  
  @Autowired
  DevicemasterRepository devicemasterRepository;
  
  @Autowired
  SMSTemplateService smsService;
  
  @Autowired
  DigitalInputAlertService alertservices;
  
  @Autowired
  AlearSummaryService alertSummeryService;
 
  @Autowired
  ParameterServices pRepor;
  
  @Autowired
  Alertmessageshistory msgService;
  
  DecimalFormat twoDForm = new DecimalFormat("#.##");
  
  static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  @Async  
  @RequestMapping(value = {"/api/TestDeltaAlert/{reportDate}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String RMSTestMail(@PathVariable("reportDate") String reportDate) throws IOException, JSONException, ParseException {


	  log.info("RMSJOB :: InsertInputHistoryRrport is called......");  

	    JSONObject dataJson = new JSONObject();
	    String paramId = "284945,6348798,291934,6348854,6348815,6348821,6348824";
	   // String paramId = "6348854";
	   String[] digitals = paramId.split(",");
	    JSONArray DataamainJSON = new JSONArray();   
	   // String reportDate=getYesterdayDateString();
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	   if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
	      }  
	   for (int ds = 0; ds < devices.length; ds++) {
	     log.info("RMSJOB doScheduledWork :: START  TASK  Device " + ds);
	     Devicemaster deviceObject = this.deviceReop.findBydeviceid(Long.valueOf(Long.parseLong(devices[ds])));
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
						
						Object[] DoorObj= {obj[0].toString(),obj[2].toString()};
						Door.add(DoorObj);
						
						Object[] DG_FaultObj= {obj[0].toString(),obj[3].toString()};
						DG_Fault.add(DG_FaultObj);
						
						Object[] PP_Input_FailObj= {obj[0].toString(),obj[4].toString()};
						PP_Input_Fail.add(PP_Input_FailObj);
						
						Object[] FireObj= {obj[0].toString(),obj[5].toString()};
						Fire.add(FireObj);
						
						Object[] DG_RunningObj= {obj[0].toString(),obj[6].toString()};
						DG_Running.add(DG_RunningObj);
						
						Object[] Battery_LowObj= {obj[0].toString(),obj[7].toString()};
						Battery_Low.add(Battery_LowObj);
					}
				}
				ALlDigitalList.add(Mains_Fail);
				ALlDigitalList.add(Door);
				ALlDigitalList.add(DG_Fault);
				ALlDigitalList.add(PP_Input_Fail);
				ALlDigitalList.add(Fire);
				ALlDigitalList.add(DG_Running);
				ALlDigitalList.add(Battery_Low);
	          if (ALlDigitalList != null) {
	        	  for (int cont = 0; cont < ALlDigitalList.size(); ++cont) {
						List<Object[]> lst=(List<Object[]>) ALlDigitalList.get(cont);
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
	            
	            log.info("RMSJOB doScheduledWork ::"+mainJSON.toString());
	          
	          for (int j = 0; j < mainJSON.length(); j++) {
	            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
	            jsonObject1.put("Parameter", this.prepo.findByid((new Long(digitals[cont])).longValue()).getPrmname());
	            if (j != mainJSON.length() - 1) {
	              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
	              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
	              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
	              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                jsonObject1.put("Open", 
	                    JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	                jsonObject1.put("Close", "");
	              } else {
	                jsonObject1.put("Open", "");
	                jsonObject1.put("Close", 
	                    JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
	              } 
	            } else {
	              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
	              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
	              Date date21 = df1.parse(reportDate.toString());
	              Date date22 = df1.parse(df1.format(new Date()));
	              int date23 = date21.compareTo(date22);
	              if (date23 == 0) {
	                jsonObjectLast.put("End Date", df.format(new Date()));
	                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
	                  jsonObject1.put("Close", 
	                      JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
	                  jsonObject1.put("Open", "");
	                } else {
	                  jsonObject1.put("Close", "");
	                  jsonObject1.put("Open", 
	                      JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
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
	            report.setParameterId((new Long(digitals[cont])).longValue());
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
	   
    return dataJson.toString();
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/TestRMSNEWEmail")
	public String TestRMSNEWEmail() {
	  ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
	    String bcc = rb.getString("EMAIL_IDS");
	    log.info("RMSJOB MOnthyReport Email To:::::::" + bcc);
	    String[] bcc1 = bcc.split(",");
	    log.info("RMSJOB MOnthyReport Email To:::::::" + Arrays.toString((Object[])bcc1));
	    for (int i = 0; i < bcc1.length; i++) {  
	    	GmailEmailSender email = new GmailEmailSender();
	      log.info("RMSJOB MOnthyReport Email Sending.......");
	     // send.sendMail(bcc1[i], "DG Running Input History Report", "Kindly Find Attached", String.valueOf(dir), fileName);
	      email.EmailSystem(bcc1[i], "Test DG Running Input History Report", "Kindly Find Attached", "/opt/2024-05-22_03-00-00.csv");
	      log.info("RMSJOB MOnthyReport Email To:::::::" + bcc1[i]);
	    } 
	  return "Succes";
	}
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/SendMailDGInputHistoryREportSajan")
	public String SendMailDGInputHistoryREportSajan() throws IOException, ParseException {
	    log.info("RMSJOB :: SendMailDGInputHistoryREport is called......");
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
	   // System.out.println(Arrays.toString((Object[])header));
	    data.add(header);
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	    if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        //devices[analog] = (String)result[0];
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
	      ArrayList<String> totaltimestamps = new ArrayList<String>();
	      for (String entryDate : QueryDates) {
	        List<Object[]> lst = this.ihreportService.getIRReport(entryDate, 
	            entryDate, 6348854L, Long.parseLong(devices[ds]));  
	        int active = 0;
	        ArrayList<String> timestamps = new ArrayList<String>();
	          for (int j = 0; j < lst.size(); j++) {
	        	  Object[] obj = lst.get(j);
	        	  timestamps.add(obj[4].toString());
	          }  
	          
	          // Initialize total time components
	          int totalHours = 0;
	          int totalMinutes = 0;
	          int totalSeconds = 0;

	          // Parse each timestamp and sum the time components
	          for (String timestamp : timestamps) {
	          	 // Regular expression to match hours, minutes, and seconds
	              Pattern pattern = Pattern.compile("(\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
	              Matcher matcher = pattern.matcher(timestamp);

	              if (matcher.find()) {
	                  int hours = Integer.parseInt(matcher.group(1));
	                  int minutes = Integer.parseInt(matcher.group(2));
	                  int seconds = Integer.parseInt(matcher.group(3));
	                  totalHours += hours;
	                  totalMinutes += minutes;
	                  totalSeconds += seconds;
	                 /* System.out.println("Hours: " + hours);
	                  System.out.println("Minutes: " + minutes);
	                  System.out.println("Seconds: " + seconds);*/
	              } else {
	                  System.out.println("No match found.");
	              }

	            
	          }
	          totalMinutes += totalSeconds / 60;
	          totalSeconds = totalSeconds % 60;

	          totalHours += totalMinutes / 60;
	          totalMinutes = totalMinutes % 60;

	          // Format and output the result
	          String formattedTime = String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
	        //  System.out.println("Total Time: " + formattedTime);
	          totaltimestamps.add(formattedTime);
	        dataList.add(totalHours+" Hours: "+totalMinutes+" Minutes: "+totalSeconds+" Seconds");
	      //  System.out.println(String.valueOf(devices[ds]) + " :: " + entryDate + " :: " + dataList.toString());
	      } 
	      System.out.println(Arrays.toString(totaltimestamps.toArray()));
	      Duration totalDuration = Duration.ZERO;
	      // Loop through the timestamps and sum them up
	        for (String timestamp : totaltimestamps) {
	            LocalTime time = LocalTime.parse(timestamp);
	            totalDuration = totalDuration.plusSeconds(time.toSecondOfDay());
	        }

	        // Calculate hours, minutes, and seconds from the total duration
	        long totalSeconds = totalDuration.getSeconds();
	        long hours = totalSeconds / 3600;
	        long minutes = (totalSeconds % 3600) / 60;
	        long seconds = totalSeconds % 60;

	        System.out.println(String.format("Total time: %02d:%02d:%02d", hours, minutes, seconds));
	      dataList.add(String.format("%02d:%02d:%02d", hours, minutes, seconds));
	      //System.out.println(dataList.toString());
	      data.add(dataList.<String>toArray(new String[0]));
	    } 
	    writer.writeAll(data);
	    writer.close();
	    ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
	    String bcc = rb.getString("EMAIL_IDS");
	    /*log.info("RMSJOB MOnthyReport Email To:::::::" + bcc);
	    String[] bcc1 = bcc.split(",");
	    log.info("RMSJOB MOnthyReport Email To:::::::" + Arrays.toString((Object[])bcc1));
	    for (int i = 0; i < bcc1.length; i++) {
	    	GmailEmailSender email = new GmailEmailSender();
	      log.info("RMSJOB MOnthyReport Email Sending.......");
	     // send.sendMail(bcc1[i], "DG Running Input History Report", "Kindly Find Attached", String.valueOf(dir), fileName);
	      email.EmailSystem(bcc1[i], "DG Running Input History Report", "Kindly Find Attached", String.valueOf(dir) + fileName);
	      TimeUnit.MINUTES.sleep(1);
	      log.info("RMSJOB MOnthyReport Email To:::::::" + bcc1[i]);
	    }*/
		return bcc; 
}
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/getVNTLiveGride",produces = {"application/json"})
	public String getVNTLiveGride() throws IOException, ParseException {
	    log.info("getVNTLiveGride is called......");
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    List<Devicemaster> deviceList=deviceReop.findAll();
	    //List<Object[]> deviceList= devicemasterservices.getDevieByProfile(6442449L);
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setDateFormat(sdf1);
	    ArrayNode analogNodearrayNode = mapper.createArrayNode();
	    log.info(deviceList.size());
	    String digitls[]={"DCEM COMMUNACTION FAIL","SMPS COMMUNICATION FAIL","Second LVD ALARM",
	    		"First LVD Alarm","SPD Fail Alarm","DC LOW","Rectifier Fail","AC FAIL ALARM"};
	    ObjectNode GolblaanalogNode = mapper.createObjectNode();
	    List myDigitalList = Arrays.asList(digitls);
	    for(Devicemaster device:deviceList)
	    {
	    	if(device.getDp().getPrid()==6442449) {
	    	Lasttrack lastTrack = lasttrackservices.findOne(device.getDeviceid());
	    	Map<String, Object> data = lastTrack.getAnalogdigidata();
	    	 List<Map<String, String>> analogData = (List<Map<String, String>>) data.get("Analog");

	    	 ObjectNode analogNode = mapper.createObjectNode();
	    	 for (Map<String, String> analogItem : analogData) {
	             for (Map.Entry<String, String> entry : analogItem.entrySet()) {
	                 String key = entry.getKey();
	                 String value = entry.getValue();
	                /// System.out.println("Analog Key: " + key + ", Value: " + value);
	                 String Unit=this.prepo.getLasttrackUnitsNew(device.getDp().getPrid().longValue(),key)==null?"":this.prepo.getLasttrackUnitsNew(device.getDp().getPrid().longValue(),key);
	                 if(myDigitalList.contains(this.prepo.findByid(Long.parseLong(key)).getPrmname()))
	                 {
	                	 if(value.equalsIgnoreCase("0") || value.equalsIgnoreCase("0.0")) {
	                		 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),"Clear");
	                		 //System.out.println("IN IF");
	                	 }
	                		 else
	                		 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),"Active");
	                 }else
	                 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),twoDForm.format(Float.parseFloat(value)));

	                 analogNode.put("Dvice_Name",device.getDevicename());
	                 analogNode.put("Data_Time", lastTrack.getDeviceDate().toString());
	             }
	         }
	    	 analogNodearrayNode.add((JsonNode)analogNode);
	    }
	    }
	    ObjectNode objectNode = mapper.createObjectNode();
	    mapper.setDateFormat(df1);
	    objectNode.putPOJO("data", analogNodearrayNode);
	    return mapper.writeValueAsString(objectNode); 
}
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/TestRMSDGreport")
 	public String TestRMSDGreport() throws IOException, ParseException, InterruptedException {
	    log.info("RMSJOB :: SendMailDGInputHistoryREport is called......");
	    String SEP = System.getProperty("file.separator");
	    URL resource = getClass().getResource("/");
	    String path = resource.getPath();
	    path = path.replace("WEB-INF/classes/", "");
	    String dir = String.valueOf(String.valueOf(String.valueOf(path))) + "maildailyreport" + SEP;
	    System.out.println(dir);
	    File file = new File(dir);
	    if (!file.exists())
	      file.mkdir(); 
	    Date date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
	    String fileName = String.valueOf(String.valueOf(getYesterdayDateString())) + "_" + String.valueOf(String.valueOf(dateFormat.format(date))) + 
	      ".csv";
	    log.info(String.valueOf(String.valueOf(String.valueOf(dir))) + fileName);
	    FileWriter outputfile = new FileWriter(String.valueOf(String.valueOf(String.valueOf(dir))) + fileName);
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
	    System.out.println(Arrays.toString((Object[])header));
	    data.add(header);
	    List<Object[]> deviceList = this.devicemasterservices.getMyDeviced(Long.valueOf(2052069L));
	    String[] devices = new String[deviceList.size()];
	    if (deviceList.size() != 0)
	      for (int analog = 0; analog < deviceList.size(); analog++) {
	        Object[] result = deviceList.get(analog);
	        BigInteger bigIntegerValue = new BigInteger(result[0].toString());
	        devices[analog] = ""+bigIntegerValue;
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
	          double hour = 0.0D;
	          if (obj[4] != null && !obj[4].toString().equalsIgnoreCase("")) {
	            Pattern pattern = Pattern.compile("(\\d+) Days: (\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
	            Matcher matcher = pattern.matcher(obj[4].toString());
	            if (matcher.find()) {
	              String hours = matcher.group(2);
	              String minutes = matcher.group(3);
	              String seconds = matcher.group(4);
	              hour = convertToTotalMinutes(Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));
	            } 
	          } 
	          active = (int)(active + hour);
	          MTD = (int)(MTD + hour);
	        } 
	        dataList.add(String.valueOf(active));
	        System.out.println(String.valueOf(String.valueOf(devices[ds])) + " :: " + entryDate + " :: " + dataList.toString());
	      } 
	      dataList.add(String.valueOf(MTD));
	      System.out.println(dataList.toString());
	      data.add(dataList.<String>toArray(new String[0]));
	    } 
	    writer.writeAll(data);
	    writer.close();
	 /*   ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
	    String bcc = rb.getString("EMAIL_IDS");
	    log.info("RMSJOB MOnthyReport Email To:::::::" + bcc);
	    String[] bcc1 = bcc.split(",");
	    log.info("RMSJOB MOnthyReport Email To:::::::" + Arrays.toString((Object[])bcc1));
	    for (int i = 0; i < bcc1.length; i++) {
	      GmailEmailSender email = new GmailEmailSender();
	      log.info("RMSJOB MOnthyReport Email Sending.......");
	      email.EmailSystem(bcc1[i], "DG Running Input History Report", "Kindly Find Attached", String.valueOf(String.valueOf(dir)) + fileName);
	      TimeUnit.MINUTES.sleep(1L);
	      log.info("RMSJOB MOnthyReport Email To:::::::" + bcc1[i]);*/
		return fileName;
	    } 

	  
  @RequestMapping(method = RequestMethod.GET, value = "/api/getVNTLiveGrideNew",produces = {"application/json"})
	public String getVNTLiveGrideNew() throws IOException, ParseException {
	    log.info("getVNTLiveGride is called......");
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    List<Devicemaster> deviceList=deviceReop.findAll();
	    //List<Object[]> deviceList= devicemasterservices.getDevieByProfile(6442449L);
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setDateFormat(sdf1);
	    ArrayNode analogNodearrayNode = mapper.createArrayNode();
	    log.info(deviceList.size());
	    String digitls[]={"DCEM COMMUNACTION FAIL","SMPS COMMUNICATION FAIL","Second LVD ALARM",
	    		"First LVD Alarm","SPD Fail Alarm","DC LOW","Rectifier Fail","AC FAIL ALARM"};
	    ObjectNode GolblaanalogNode = mapper.createObjectNode();
	    List myDigitalList = Arrays.asList(digitls);
	    for(Devicemaster device:deviceList)
	    {//6442449
	    	if(device.getDp().getPrid()==6442449) {
	    		 ObjectNode analogNode = mapper.createObjectNode();
	    	Lasttrack lastTrack = lasttrackservices.findOne(device.getDeviceid());
	    	Map<String, Object> data = lastTrack.getAnalogdigidata();
	    	 List<Map<String, String>> analogData = (List<Map<String, String>>) data.get("Analog");
int digitalCount=0;
	    	
	    	 for (Map<String, String> analogItem : analogData) {
	             for (Map.Entry<String, String> entry : analogItem.entrySet()) {
	                 String key = entry.getKey();
	                 String value = entry.getValue();
	                /// System.out.println("Analog Key: " + key + ", Value: " + value);
	                 String Unit=this.prepo.getLasttrackUnitsNew(device.getDp().getPrid().longValue(),key)==null?"":this.prepo.getLasttrackUnitsNew(device.getDp().getPrid().longValue(),key);
	                 if(myDigitalList.contains(this.prepo.findByid(Long.parseLong(key)).getPrmname()))
	                 {
	                	 if(value.equalsIgnoreCase("0") || value.equalsIgnoreCase("0.0")) {
	                		 digitalCount++;
	                		 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),"Clear");
	                		 System.out.println("clear "+digitalCount);
	                	 }
	                		 else
	                		 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),"Active");
	                 }else
	                 analogNode.put(this.prepo.findByid(Long.parseLong(key)).getPrmname().replace(" ","_"),twoDForm.format(Float.parseFloat(value)));

	                 analogNode.put("Dvice_Name",device.getDevicename());
	                 analogNode.put("Data_Time", lastTrack.getDeviceDate().toString());
	             }
	         }
	    	 if(digitalCount!=8) {
 	        analogNode.put("Status",checkDigitalValuesContinuity(device.getDeviceid()));
	    	 analogNodearrayNode.add((JsonNode)analogNode);
	    	 }
	    }
	    }
	    ObjectNode objectNode = mapper.createObjectNode();
	    mapper.setDateFormat(df1);
	    objectNode.putPOJO("data", analogNodearrayNode);
	    return mapper.writeValueAsString(objectNode); 
}
  
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/TestServer")
	public String TestServer() {

		String imei="861657073567567";
		Devicemaster device = devicemasterRepository.findByImei(imei);
		 
			String dataString="ATL861657073567567,$GPRMC,102344,A,2302.0366,N,07240.5381,E,0.0,0,241224,,,*22,#01111011000000,54.22,-70.00,0,80.92,33,4.1,31,404,857,1c,511a16ATL+, ATLMB861657073567567,102411,241224,@01,03,0000,01,4260@01,03,0001,01,a3d7@01,03,0002,01,3e99@01,03,0003,01,999a@01,03,0004,01,45ab@01,03,0005,01,9e1d@01,03,0006,01,40d0@01,03,0007,01,0000@01,03,0008,01,3df1@01,03,0009,01,a9fc@ATLMBx ATLMB861657073567567,102411,241224,@01,03,000a,01,0000@01,03,000b,01,0000@01,03,000c,01,4565@01,03,000d,01,edfc@01,03,000e,01,4013@01,03,000f,01,3333@01,03,0010,01,4258@01,03,0011,01,ae14@01,03,0012,01,0000@01,03,0013,01,0000@ATLMB}";
			log.info("ModeBusBEnergyMeterServer concat Analog "+imei+" :: "+dataString);
			String[] dataStringArray=dataString.split(",");
			DeviceProfile dp = device.getDp();
			JSONObject parameters = new JSONObject(dp.getParameters());
			JSONArray analog = parameters.getJSONArray("Analog");
			JSONArray digital = parameters.getJSONArray("Digital");
			JSONObject AnalogDataObject = new JSONObject();
			JSONArray AnalogJsonArray = new JSONArray();
			JSONArray DigitalJsonArray = new JSONArray();
			JSONObject DigitalDataObject = new JSONObject();
			JSONObject DeviceData=new JSONObject();
			double fuel=Double.parseDouble(dataStringArray[16].toString()); 
			double battry=Double.parseDouble(dataStringArray[15].toString()); 
			AnalogDataObject.put("6387981",String.valueOf(battry));
			AnalogDataObject.put("6387982",String.valueOf(fuel));
			try {
				for (int i = 0; i < analog.length(); i++) {
					JSONObject obj = (JSONObject) analog.get(i);
					//String[] indexArray=obj.get("analogioindex").toString().split(",");
					/*if(indexArray.length>1)
					{
						Double index1=Double.parseDouble(indexArray[0]);
						Double index2=Double.parseDouble(indexArray[1]);
						boolean isFound1 = dataStringArray[index1.intValue()].split("@")[0].indexOf("X") !=-1? true: false;
						boolean isFound2 = dataStringArray[index2.intValue()].split("@")[0].indexOf("X") !=-1? true: false;
						if (isFound1==false && isFound2==false) {
							String combineRegister=dataStringArray[index1.intValue()].concat(dataStringArray[index2.intValue()]);
							int registerValue=Integer.parseInt(combineRegister);
							AnalogDataObject.put(obj.get("Analoginput").toString(),
									Double.valueOf(twoDForm.format(registerValue *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
						}else {
							AnalogDataObject.put(obj.get("Analoginput").toString(), "00.00");
						}
					}*/
					
					Double d = Double.parseDouble(obj.get("analogioindex").toString());
					boolean isFound = dataStringArray[d.intValue()].split("@")[0].indexOf("X") !=-1? true: false;
					//System.out.println(isFound);
					if (isFound==false) {
						
						AnalogDataObject.put(obj.get("Analoginput").toString(),
								Double.valueOf(twoDForm.format((Integer.parseInt(dataStringArray[d.intValue()].split("@")[0], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
					} else {
						AnalogDataObject.put(obj.get("Analoginput").toString(), "00.00");
					}

				} 
				AnalogJsonArray.put(AnalogDataObject);
				DeviceData.put("Analog", AnalogJsonArray);
				 
			/*	String Digital_Alarm_1=reverse(hexto4bit(msgary[4].split(":")[1]));
				String Digital_Alarm_2=reverse(hexto4bit(msgary[5].split(":")[1]));
				String SolidState=reverse(hexto4bit(msgary[6].split(":")[1]));*/
				String Digital_Alarm=dataStringArray[14];
				for (int i = 0; i < digital.length(); i++) {
					JSONObject obj = (JSONObject) digital.get(i);
					
					Double d = Double.parseDouble(obj.get("dioindex").toString());
					boolean reverse =Boolean.parseBoolean(obj.get("reverse").toString());
					//char alarm=Digital_Alarm.charAt(Integer.parseInt(obj.get("dioindex").toString()));
					// Parse the string as a double first, then cast to int
					char alarm = Digital_Alarm.charAt((int) Double.parseDouble(obj.get("dioindex").toString()));
					System.out.println((int) Double.parseDouble(obj.get("dioindex").toString()));
					System.out.println(alarm);
					if (reverse == true) 
						DigitalDataObject.put(obj.get("parameterId").toString(),alarm=='1'? "0" : "1");
					else
						DigitalDataObject.put(obj.get("parameterId").toString(),String.valueOf(alarm));
					}
				DigitalJsonArray.put(DigitalDataObject);
				DeviceData.put("Digital", DigitalJsonArray);
				
			//	Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
				Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
						new Date(), new Date(),
						new ObjectMapper().readValue(DeviceData.toString(), Map.class),
						new ObjectMapper().readValue(new JSONObject().toString(), Map.class),new ObjectMapper().readValue(convertJson(DeviceData.toString()).toString(), Map.class));
				lasttrackrepository.saveAndFlush(lTrack);
				/*if (track == null)
				{
					lasttrackrepository.saveAndFlush(lTrack);
				}
				else {
					track.setDeviceDate(new Date());
					track.setSystemDate(new Date());
					track.setAnalogdigidata(new ObjectMapper().readValue(new JSONObject().toString(), Map.class));
					lasttrackrepository.saveAndFlush(track);
					
					History hist =new History(device.getDeviceid(), device.getUserId(), new Date(),
							new Date(), new ObjectMapper().readValue(DeviceData.toString(), Map.class),
							new ObjectMapper().readValue(new JSONObject().toString(), Map.class));
				}
				*/
				
			} catch (Exception e2) {
				e2.printStackTrace();
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getMessage());
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getStackTrace());
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" (Index Out of Bound) ==> Registers are not match with profile");
			}
	  return "Succes";
	}
  
  //@RequestMapping(method = RequestMethod.GET, value = "/api/ConvertModeBusValue/{hexval}")

  @RequestMapping(method = RequestMethod.GET, value = "/api/ConvertModeBusValue258/{id}")
	public String ConvertModeBusValue258(@PathVariable String id)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	  return ConvertMODBUSRTUValue(id);
	}
  
  @RequestMapping(value = {"/api/GetDashboardSAJANeRR/{id}"}, produces = {"application/json"})
  public String GetDashboardSAJANeRR(@PathVariable Long id) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Lasttrack lastTrack = this.lasttrackservices.findOne(id);
    Devicemaster device = this.deviceReop.findBydeviceid(lastTrack.getDeviceId());
    
    JSONObject analogJsonObject = new JSONObject((
        new ObjectMapper()).writeValueAsString(lastTrack.getAnalogdigidata()));
    JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
    ObjectMapper mapper = new ObjectMapper();
    
    ArrayNode analogNodearrayNode = mapper.createArrayNode();
    analogArray.forEach(SingleAnalogObject -> {
          JSONObject analogObject = (JSONObject)SingleAnalogObject;
          for (int i = 0; i < analogObject.names().length(); i++) {
        	  ObjectNode analogNode = mapper.createObjectNode();
            analogNode.put(this.reo.findByid((new Long(analogObject.names().getString(i))).longValue()).getPrmname(), analogObject.get(analogObject.names().getString(i)).toString());
           
            log.info(device.getDp().getPrid().longValue());
            log.info(analogObject.names());
            log.info(analogObject.names().getString(i));
           // log.info();
           // log.info();
            analogNode.put("Unit", this.reo.getLasttrackUnitsNew(device.getDp().getPrid().longValue(), analogObject.names().getString(i)).toString());
            analogNode.put("ParameterId", analogObject.names().getString(i).toString());
            analogNodearrayNode.add((JsonNode)analogNode);
          } 
        });
    ObjectNode objectNode = mapper.createObjectNode();
    mapper.setDateFormat(df);
    objectNode.putPOJO("Data", analogNodearrayNode);
    objectNode.putPOJO("DeviceId", lastTrack.getDeviceId());
    objectNode.putPOJO("DeviceDate", lastTrack.getDeviceDate());
    objectNode.putPOJO("DeviceName", device.getDevicename());
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/TestModebusServer")
	public String TestModebusServer() {
	  
	///	String msg="ATL862211074210105,$GPRMC,092845,A,2301.2283,N,07238.2378,E,0.0,0,220325,,,*22,#01111011000000,0.00,-70.00,0,0.10,33,4.2,24,404,5,61b3,c13be15ATL, ATLMB862211074210105,092902,220325,@01,03,0001,04,999a@02,03,0001,04,409c@03,03,0001,04,45aa@04,03,0001,04,zzzzzzzz@05,03,0001,04,zzzzzzzz@ATLMB2";
		String msg="ATL862211074210105,$GPRMC,092845,A,2301.2283,N,07238.2378,E,0.0,0,220325,,,*22,#01111011000000,0.00,-70.00,0,0.10,33,4.2,24,404,5,61b3,c13be15ATL, ATLMB862211074210105,092902,220325,@01,03,0001,04,999a@02,03,0001,04,409c@03,03,0001,04,zzzzzzzz@04,03,0001,04,zzzzzzzz@05,03,0001,04,zzzzzzzz@ATLMB2";

		String imei="862211074210105";
		Devicemaster device = devicemasterRepository.findByImei(imei);
		log.info("ModeBusBEnergyMeterServer Analog "+imei+" :: "+msg);
		LogObject Datagetentry = null;
		if (Datagetentry == null) {//Error
			//String dataString=Datagetentry.getMsg()+","+msg;
			//log.info("ModeBusBEnergyMeterServer concat Analog "+imei+" :: "+dataString);
			String[] dataStringArray=msg.split(",");
			log.info("ModeBusBEnergyMeterServer SAJAN "+imei+" :: "+dataStringArray[15]);
			log.info("ModeBusBEnergyMeterServer SAJAN "+imei+" :: "+dataStringArray[16]);
			DeviceProfile dp = device.getDp();
			JSONObject parameters = new JSONObject(dp.getParameters());
			JSONArray analog = parameters.getJSONArray("Analog");   
			JSONArray digital = parameters.getJSONArray("Digital");
			JSONObject AnalogDataObject = new JSONObject();
			JSONArray AnalogJsonArray = new JSONArray();
			JSONArray DigitalJsonArray = new JSONArray();
			JSONObject DigitalDataObject = new JSONObject();
			JSONObject DeviceData=new JSONObject();
			Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
			
			Double latitude = 0.0d;
			Double Langitude = 0.0d;
			boolean isvalid = Boolean.valueOf("A".equals(dataStringArray[3]));
			if (isvalid) {
			if ((dataStringArray[4] == "") || (dataStringArray[4] == null)) {
				latitude = StringToolsV3.parseLatitude("0.0", "N");
			} else {
				latitude = StringToolsV3.parseLatitude(dataStringArray[4], "N");
			}
			if ((dataStringArray[6] == "") || (dataStringArray[6] == null)) {
				Langitude = StringToolsV3.parseLatitude("0.0", "E");
			} else {
				Langitude = StringToolsV3.parseLatitude(dataStringArray[6], "E");
			}
			}
			log.info("latitude : "+latitude);
			log.info("Langitude : "+Langitude);
			
			JSONObject Gpsobj = new JSONObject();
			Gpsobj.put("latitude", latitude);
			Gpsobj.put("longitude", Langitude);
			try {
				
				for (int i = 0; i < analog.length(); i++) {
					JSONObject obj = (JSONObject) analog.get(i);
					Double d = Double.parseDouble(obj.get("analogioindex").toString());
					boolean isAtFound = dataStringArray[d.intValue()].contains("@")? true: false;
					
					if (isAtFound==false)
					{
						String hexString = dataStringArray[d.intValue()];
						if (hexString != null && !hexString.isEmpty() && !hexString.matches("[0-9A-Fa-f]+")) {
							try {
						    	AnalogDataObject.put(obj.get("Analoginput").toString(),Double.valueOf(twoDForm.format(Double.parseDouble(dataStringArray[d.intValue()]) *  Double.parseDouble(obj.get("Analogformula").toString()))));
							  } catch (NumberFormatException e) {
								  log.info("Invalid hexadecimal string: " + hexString);
							    }
							}else
						    	AnalogDataObject.put(obj.get("Analoginput").toString(),Double.valueOf(twoDForm.format((Integer.parseInt(dataStringArray[d.intValue()], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
					}else if (isAtFound==true)
					{
						boolean isFound = dataStringArray[d.intValue()].split("@")[0].indexOf("z") !=-1? true: false;
						if (isFound==false) 
							AnalogDataObject.put(obj.get("Analoginput").toString(),	Double.valueOf(twoDForm.format((Integer.parseInt(dataStringArray[d.intValue()].split("@")[0], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString()))).doubleValue());
						 else 
							AnalogDataObject.put(obj.get("Analoginput").toString(), "00.00");
					}else 
						AnalogDataObject.put(obj.get("Analoginput").toString(), "00.00");
				}
				
				AnalogJsonArray.put(AnalogDataObject);
				DeviceData.put("Analog", AnalogJsonArray);
				String Digital_Alarm=dataStringArray[14];
				for (int i = 0; i < digital.length(); i++) {
					JSONObject obj = (JSONObject) digital.get(i);
					boolean reverse =Boolean.parseBoolean(obj.get("reverse").toString());
					char alarm = Digital_Alarm.charAt((int) Double.parseDouble(obj.get("dioindex").toString()));
					if (reverse == true) 
						DigitalDataObject.put(obj.get("parameterId").toString(),alarm=='1'? "0" : "1");
					else
						DigitalDataObject.put(obj.get("parameterId").toString(),String.valueOf(alarm));
					}
				DigitalJsonArray.put(DigitalDataObject);
				DeviceData.put("Digital", DigitalJsonArray);
				 log.info(DeviceData.toString());
				/*Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
						new Date(), new Date(),
						new ObjectMapper().readValue(DeviceData.toString(), Map.class),
						new ObjectMapper().readValue(Gpsobj.toString(), Map.class),
						new ObjectMapper().readValue(convertJson(DeviceData.toString()).toString(), Map.class));
				lasttrackrepository.saveAndFlush(lTrack);*/
		/*		MyAlerts alert=new MyAlerts();
				alert.sendMsg(device,lTrack,track);*/
				
			} catch (Exception e2) {
				e2.printStackTrace();
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getMessage());
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" "+e2.getStackTrace());
				log.info("ModeBusBEnergyMeterServer "+device.getImei()+" (Index Out of Bound) ==> Registers are not match with profile");
			}
		}
	  
	  
	  return "Succes";
	}
  
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/TestAlertMessageSummery")
  public String TestAlertMessageSummery() throws IOException {
      String status = "";
      Devicemaster dmastr = devicemasterRepository.findBydeviceid(10L);
      Lasttrack oldtrack = lasttrackrepository.findOne(9L);
      Lasttrack ltrack = lasttrackrepository.findOne(10L);

      List<DigitalInputAlert> alert = this.alertservices.findBymanagerid(dmastr.getManagerId());
      this.sendTmie = formatter1.format(ltrack.getDeviceDate());
      JsonParser parser = new JsonParser();
      boolean isAlert = false;
      this.deviceName = dmastr.getDevicename().trim();
      log.info(String.valueOf(dmastr.getImei()) + " :: " + dmastr.getImei() + " chekAlert :: SAJAN :: " + alert.size());

      // If there are alerts
      if (!alert.isEmpty()) {
          for (DigitalInputAlert alt : alert) {
              // Ensure device ID matches
              if (alt.getDevice_id().equals(dmastr.getDeviceid())) {
                  this.rulrId = alt.getNo().toString();
                  
                  // Compare latest and old track data for Digital inputs
                  Map<String, Object> ldigitaldata = ltrack.getAnalogdigidata();
                  Map<String, Object> olddigitaldata = oldtrack.getAnalogdigidata();
                  JSONObject latestObj = new JSONObject(ldigitaldata);
                  JSONObject oldObj = new JSONObject(olddigitaldata);
                  
                  // Get the "Digital" data arrays
                  JSONArray ljsonArr = latestObj.getJSONArray("Digital");
                  JSONArray oldjsonArr = oldObj.getJSONArray("Digital");

                  // Loop through each digital input (using the parameter ID as key)
                  for (int i = 0; i < ljsonArr.length(); i++) {
                      // Get the keys for both current and old digital data
                      JSONObject lstObj = ljsonArr.getJSONObject(i);
                      JSONObject odlObj = oldjsonArr.getJSONObject(i);

                      Iterator<String> keys = lstObj.keys();
                      while (keys.hasNext()) {
                          String parameter = keys.next();
                          String currentValue = lstObj.getString(parameter);
                          String oldValue = odlObj.getString(parameter);
         	             System.out.println(i+" InfoKey: " + parameter + ", value: " + lstObj.getString(parameter) + " :: " + odlObj.getString(parameter));

                          // If the value has changed, process the alert
                          if (!currentValue.equalsIgnoreCase(oldValue)) {
                              // Check if this digital input matches the alert
                              if (alt.getDigitalinput().equalsIgnoreCase(parameter)) {
                                  // Fetch the message template
                                  MessageTemplate template = this.smsService.findBymid(alt.getMessagetemplate_id());
                                  List<String> mobile = Arrays.asList(alt.getMobileno().split(","));

                                  // Logic for sending notifications based on status
                                  if (alt.getStatus().equalsIgnoreCase("ON") && currentValue.equalsIgnoreCase("1")) {
                                      sendFCMnotification(alt, template, mobile, "CLEAR", ltrack);
                                  }
                                  if (alt.getStatus().equalsIgnoreCase("OFF") && currentValue.equalsIgnoreCase("0")) {
                                      sendFCMnotification(alt, template, mobile, "ACTIVE", ltrack);
                                  }
                                  if (alt.getStatus().equalsIgnoreCase("BOTH")) {
                                      status = currentValue.equalsIgnoreCase("1") ? "CLEAR" : "ACTIVE";
                                      sendFCMnotification(alt, template, mobile, status, ltrack);
                                  }
                              }
                          }
                      }
                  }
              }
          }
      }
      return "Success";
  }



  
/*  @RequestMapping(method = RequestMethod.GET, value = "/api/TestAlertMessageSummery")
 	public String TestAlertMessageSummery() throws IOException {
	  String status = "";
	  Devicemaster dmastr=devicemasterRepository.findBydeviceid(10l);
	  Lasttrack oldtrack=lasttrackrepository.findOne(9L);
	  Lasttrack ltrack=lasttrackrepository.findOne(10L);
	  

	    List<DigitalInputAlert> alert = this.alertservices.findBymanagerid(dmastr.getManagerId());
	    this.sendTmie = formatter1.format(ltrack.getDeviceDate());
	    JsonParser parser = new JsonParser();
	    boolean isAlert = false;
	    this.deviceName = dmastr.getDevicename().trim();
	    log.info(String.valueOf(dmastr.getImei()) + " :: " + dmastr.getImei() + " chekAlert :: SAJAN :: " + alert.size());
	    if (alert.size() > 0 && alert != null)
	      for (DigitalInputAlert alt : alert) {
	        if (alt.getDevice_id().compareTo(dmastr.getDeviceid()) == 0) {
	          this.rulrId = alt.getNo().toString();
	          int compareTo = alt.getDevice_id().compareTo(dmastr.getDeviceid());
	          System.out.println(compareTo);
	          if (compareTo == 0) {
	            Map<String, Object> ldigitaldata = ltrack.getAnalogdigidata();
	            Map<String, Object> olddigitaldata = oldtrack.getAnalogdigidata();
	            JSONObject latestObj = new JSONObject(ldigitaldata);
	            JSONObject oldObj = new JSONObject(olddigitaldata);
	            JSONArray ljsonArr = (JSONArray)latestObj.get("Digital");
	            JSONArray oldjsonArr = (JSONArray)oldObj.get("Digital");
	            System.out.println(ljsonArr.toString());
	            for (int i = 0; i < ljsonArr.length(); i++) {
	              JSONObject lstObj = ljsonArr.getJSONObject(i);
	              JSONObject odlObj = oldjsonArr.getJSONObject(i);
	              String parameter = lstObj.keys().next();
	             System.out.println(i+" InfoKey: " + parameter + ", value: " + lstObj.getString(parameter) + " :: " + odlObj.getString(parameter));
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
	                      status = "CLEAR";
	                    } else {
	                      status = "ACTIVE";
	                    } 
	                    sendFCMnotification(alt, template, mobile, status,ltrack);
	                  } 
	                }  
	            } 
	          }   
	        } 
	      }  
 	  return "Succes";
 	}*/
  
  
  public void sendFCMnotification(DigitalInputAlert alert, MessageTemplate template, List<String> mobileNos, String status, Lasttrack ltrack) throws IOException {
	    log.info(mobileNos.toString());
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
	    	        tempMsg = tempMsg.replaceAll("\\<input1\\>", pRepor.get(Long.valueOf(Long.parseLong(alert.getDigitalinput()))).getPrmname());
	    	        tempMsg = tempMsg.replaceAll("\\<status\\>", status);
	    	        tempMsg = tempMsg.replaceAll("\\<date\\>", this.sendTmie);
	    	        tempMsg = tempMsg.replaceAll("\\<deviceName\\>", this.deviceName);
	    	        sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
	    	        String api="https://bulksmsapi.smartping.ai//?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=<mobile_number>&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=<message>";
	    	        api = api.replaceAll("\\<mobile_number\\>", mobileNo);
	    	        api = api.replaceAll("\\<message\\>", sendMsg);
	    	       // responce = CallAPI.sendGet(api);  
	    	        if( this.pRepor.get(Long.valueOf(Long.parseLong(alert.getDigitalinput()))).getPrmname().equalsIgnoreCase("Door"))
	    	        {
	    	        	/*RMSEmailSystem email=new RMSEmailSystem();
	    	    		email.EmailSystem("signotox_noc@signotox.com","RMS Door Alert", tempMsg);*/
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
  
  static String ConvertMODBUSRTUValue(String hexVal)
	{
      Long i1 = Long.parseLong(hexVal, 16);
      Float f1 = Float.intBitsToFloat(i1.intValue());
      return String.valueOf(f1);
	}
  
	static JSONObject convertJson(String json) throws org.codehaus.jackson.JsonParseException, JsonMappingException, IOException
	{

		 JSONObject globalJsonObject=new JSONObject();
			    ObjectMapper mapper = new ObjectMapper();
				Map<String, String> map = mapper.readValue(json, Map.class);
	            System.out.println(map);
	            JSONObject jsonObject = new JSONObject(json);
	            JSONArray analogArray=(JSONArray) jsonObject.get("Analog");
	            JSONArray digitalArray=(JSONArray) jsonObject.get("Digital");
	           JSONObject analogJsonObject=new JSONObject();
	           for (int i = 0; i < analogArray.length(); i++) {
	        	    JSONObject analogJson = analogArray.getJSONObject(i);
	        	    Iterator<String> keys = analogJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        System.out.println("Key :" + key + "  Value :" + analogJson.get(key));
	        	        analogJsonObject.put(key, analogJson.get(key));
	        	    }   
	        	}
	           globalJsonObject.put("Analog", analogJsonObject);
	           JSONObject digitalJsonObject=new JSONObject();
	           for (int i = 0; i < digitalArray.length(); i++) {
	        	    JSONObject digitalJson = digitalArray.getJSONObject(i);
	        	    Iterator<String> keys = digitalJson.keys();
	        	    while (keys.hasNext()) {
	        	        String key = keys.next();
	        	        System.out.println("Key :" + key + "  Value :" + digitalJson.get(key));
	        	        digitalJsonObject.put(key, digitalJson.get(key));
	        	    }
	        	}
	           globalJsonObject.put("Digital", digitalJsonObject);
	           return globalJsonObject;
	}
	
  public String checkDigitalValuesContinuity(Long deviceId) {
	  LocalDateTime now = LocalDateTime.now();  
	  List<Object[]>  results = hstservices.deltameterrpt(deviceId, df1.format(new Date()), df1.format(new Date()), "6337570");
    //  System.out.println(results.size());
      if (results.isEmpty()) {
          return "Offilne";
      }

      String firstValue = (String) results.get(0)[0]; // Assuming digitalvalue is the first column
      boolean isContinuous = true;

      for (Object[] result : results) {
          String currentValue = (String) result[0];
          if (!currentValue.equals(firstValue)) {
              isContinuous = false;
              break;
          }
      }
      log.info(isContinuous ? "All digital values are continuous and the same: " + firstValue 
                          : "Digital values are not continuous.");
      return isContinuous ? "Offilne": "Online";
  }
  
  public static int convertToTotalMinutes(int hours, int minutes, int seconds) {
	    int totalMinutes = (hours * 60) + minutes + (seconds / 60);
	    return totalMinutes;
	}
  
  public static String getYesterdayDateString() {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal = Calendar.getInstance();
	    cal.add(5, -1);
	    return dateFormat.format(cal.getTime());
	  }
}

package com.bonrix.dggenraterset.Controller;

import com.bonrix.dggenraterset.Controller.DeviceCntroller;
import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.ProfileAnalogSetting;
import com.bonrix.dggenraterset.Model.ProfileDigitalSetting;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.SocketMessageLog;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.SocketMessageLogRepository;
import com.bonrix.dggenraterset.Service.AnalogDataServices;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DeviceFailureNoticeService;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.ProfileAnalogSettingService;
import com.bonrix.dggenraterset.Service.ProfileDigitalSettingService;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Service.UserService;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@Transactional
@RestController
public class DeviceCntroller {
  @Autowired
  ApiService apiService;
  
  @Autowired
  DevicemasterServices devicemasterservices;
  
  @Autowired
  DeviceProfileServices profileService;
  
  @Autowired
  HistoryServices historyservices;
  
  @Autowired
  AssignSiteService assignsiteservices;
  
  @Autowired
  LasttrackServices lasttrackservices;
  
  @Autowired
  DeviceFailureNoticeService Dfnservices;
  
  @Autowired
  ProfileDigitalSettingService profileDigitalService;
  
  @Autowired
  ProfileAnalogSettingService profileAnalogService;
  
  @Autowired
  SiteServices Siteservices;
  
  @Autowired
  AssignSiteService assSiteService;
  
  @Autowired
  UserService userService;
  
  @Autowired
  DevicemasterRepository Devicerepository;
  
  @Autowired
  AnalogDataServices AnalogDataservice;
  
  @Autowired
  SocketMessageLogRepository socketmsgrepository;
  
  private static final Logger log = Logger.getLogger(DeviceCntroller.class);
  
  static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
  
  @RequestMapping(value = {"/api/DeletrDeviceListsajan/{deviceId}/{key}"}, produces = {"application/json"})
  public String deletesajanDevice(@PathVariable long deviceId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
    	 this.devicemasterservices.deleteBydeviceId(Long.valueOf(deviceId));    
     /* int flag = 0;
      List history = this.historyservices.findBydeviceId(Long.valueOf(deviceId));
      if (history.size() != 0) {
        flag = 1;
        return (new SpringException(false, "Please Delete History Data")).toString();
      } 
      List lastTrack = this.lasttrackservices.findBydeviceId(Long.valueOf(deviceId));
      if (lastTrack.size() != 0) {
        flag = 1;
        return (new SpringException(false, "Please Delete LastTrack Data")).toString();
      } 
      List assignuserdevice = this.devicemasterservices.findByDevice_id(Long.valueOf(deviceId));
      if (assignuserdevice.size() != 0) {
        flag = 1;
        return (new SpringException(false, "Please Delete AssignUserDevice Data")).toString();
      } 
      List assignsite = this.assignsiteservices.findBydeviceid(Long.valueOf(deviceId));
      if (assignsite.size() != 0) {
        flag = 1;
        return (new SpringException(false, "Please Delete AssignSite Data")).toString();
      } 
      if (flag == 0)
       
    } */
    }
    return (new SpringException(true, "Success")).toString();
  }
  
  @RequestMapping(value = {"/api/GetDeviceList2/{adminid}/{key}"}, produces = {"application/json"})
  public String getcomponetlist(@PathVariable Long adminid, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    List lst = null;
    if (api != null)
      lst = JsonUtills.RemoveListDuplicate(this.devicemasterservices.getdeviceByadminId(adminid.longValue())); 
    return JsonUtills.ListToDataTable(lst);
  }
  
  @RequestMapping(value = {"/api/AddDevice/{devicname}/{devicemodel}/{imei}/{cardno}/{desc}/{adminId}/{managetId}/{SiteId}/{prid}/{altdevicename}/{key}"}, produces = {"application/json"})
  public String addDevice(@PathVariable String devicname, @PathVariable String imei, @PathVariable String devicemodel, @PathVariable String cardno, @PathVariable String desc, @PathVariable long adminId, @PathVariable long managetId, @PathVariable long SiteId, @PathVariable long prid, @PathVariable String altdevicename, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Devicemaster dm = this.devicemasterservices.findByImei(imei);
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null && dm == null) {
      DeviceProfile dp = new DeviceProfile();
      dp = this.profileService.get(Long.valueOf(prid));
      Devicemaster mst = new Devicemaster();
      mst.setDevicedescription(desc);
      mst.setDevicemodel(devicemodel);
      mst.setDevicename(devicname);
      mst.setDp(dp);
      mst.setFlagcondition(Boolean.valueOf(true));
      mst.setImei(imei);
      mst.setSimcardno(cardno);
      mst.setUserId(Long.valueOf(adminId));
      mst.setManagerId(Long.valueOf(managetId));
      mst.setAltdevicename(altdevicename);
      this.devicemasterservices.saveDevice(mst);
      if (SiteId != 0L) {
        AssignSite assgn = new AssignSite();
        assgn.setDeviceid(mst.getDeviceid());
        assgn.setManagerid(Long.valueOf(managetId));
        assgn.setSiteid(Long.valueOf(SiteId));
        this.Siteservices.saveassignsite(assgn);
      } 
    } else {
      return (new SpringException(true, "Device Aleredy Exits!")).toString();
    } 
    return (new SpringException(true, "Device Successfully Added!")).toString();
  }
  
  @RequestMapping(value = {"/api/AddDeltaDevice/{devicname}/{imei}/{cardno}/{desc}/{adminId}/{managetId}/{SiteId}/{prid}/{altdevicename}/{key}"}, produces = {"application/json"})
  public String addDevice(@PathVariable String devicname, @PathVariable String imei, @PathVariable String cardno, @PathVariable String desc, @PathVariable long adminId, @PathVariable long managetId, @PathVariable long SiteId, @PathVariable long prid, @PathVariable String altdevicename, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Devicemaster dm = this.devicemasterservices.findByImei(imei);
    Apikey api = this.apiService.findBykeyValue(key);
    if (api == null && dm == null) {
      DeviceProfile dp = new DeviceProfile();
      dp = this.profileService.get(Long.valueOf(prid));
      Devicemaster mst = new Devicemaster();
      mst.setDevicedescription(desc);
      mst.setDevicemodel("Delta");
      mst.setDevicename(devicname);
      mst.setDp(dp);
      mst.setFlagcondition(Boolean.valueOf(true));
      mst.setImei(imei);
      mst.setSimcardno(cardno);
      mst.setUserId(Long.valueOf(adminId));
      mst.setManagerId(Long.valueOf(managetId));
      mst.setAltdevicename(altdevicename);
      this.devicemasterservices.saveDevice(mst);
      if (SiteId != 0L) {
        AssignSite assgn = new AssignSite();
        assgn.setDeviceid(mst.getDeviceid());
        assgn.setManagerid(Long.valueOf(managetId));
        assgn.setSiteid(Long.valueOf(SiteId));
        this.Siteservices.saveassignsite(assgn);
      } 
    } else {
      return (new SpringException(true, "Device Aleredy Exits!")).toString();
    } 
    return (new SpringException(true, "Device Successfully Added!")).toString();
  }
  
  @RequestMapping(value = {"/api/UpdateDevice/{deviceid}/{devicemodel}/{devicname}/{imei}/{cardno}/{desc}/{adminId}/{managetId}/{prid}/{altdevicename}/{key}"}, produces = {"application/json"})
  public String updateDevice(@PathVariable Long deviceid, @PathVariable String devicname, @PathVariable String devicemodel, @PathVariable String imei, @PathVariable String cardno, @PathVariable String desc, @PathVariable long adminId, @PathVariable long managetId, @PathVariable long prid, @PathVariable String altdevicename, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      DeviceProfile dp = new DeviceProfile();
      dp = this.profileService.get(Long.valueOf(prid));
      Devicemaster mst = new Devicemaster();
      mst.setDeviceid(deviceid);
      mst.setDevicedescription(desc);
      mst.setDevicemodel(devicemodel);
      mst.setDevicename(devicname);
      mst.setDp(dp);
      mst.setFlagcondition(Boolean.valueOf(true));
      mst.setImei(imei);
      mst.setSimcardno(cardno);
      mst.setUserId(Long.valueOf(adminId));
      mst.setManagerId(Long.valueOf(managetId));
      mst.setAltdevicename(altdevicename);
      this.devicemasterservices.updateDevice(mst);
    } else {
      return (new SpringException(true, "Some thing Wrong !")).toString();
    } 
    return (new SpringException(true, "Device Successfully Added!")).toString();
  }
  
  @RequestMapping(value = {"/api/deletebytable/{device_id}/{tableno}/{key}"}, produces = {"application/json"})
  public String deletebytable(@PathVariable Long device_id, @PathVariable int tableno, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      if (tableno == 1)
        return this.devicemasterservices.deletelastrack(device_id); 
      if (tableno == 2)
        return this.devicemasterservices.deletehistory(device_id); 
      if (tableno == 3)
        return this.devicemasterservices.deleteassignuserdevice(device_id); 
      if (tableno == 4) {
        List<Object[]> list = this.devicemasterservices.getsiteidbydeviceid(device_id);
        if (list.size() != 0)
          for (int i = 0; i < list.size(); i++) {
            Object[] result = list.get(i);
            this.devicemasterservices.deleteassignusers(Long.valueOf(Long.parseLong(result[3].toString())));
            this.devicemasterservices
              .deleteassignsiteusergroup(Long.valueOf(Long.parseLong(result[3].toString())));
          }  
        return this.devicemasterservices.deleteassignsite(device_id);
      } 
      return (new SpringException(true, "Some thing Wrong !")).toString();
    } 
    return (new SpringException(true, "Some thing Wrong !")).toString();
  }
  
  @RequestMapping(value = {"/api/GetDeviceList/{managetId}/{key}"}, produces = {"application/json"})
  public String GetDeviceList(@PathVariable long managetId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Apikey api = this.apiService.findBykeyValue(key);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Object[]> lst = null;
    JSONArray jarray = new JSONArray();
    long userID = 2L;
    List<Object[]> deviceFailureAdminlist = this.Dfnservices.getAdminDeviceFailureData(managetId);
    String adminFailureminute = "0";
    String adminWarningminute = "0";
    if (deviceFailureAdminlist.size() == 0) {
      adminFailureminute = "0";
      adminWarningminute = "0";
    } else {
      for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
        adminFailureminute = DeviceAdminresult[0].toString();
        adminWarningminute = DeviceAdminresult[1].toString();
      } 
    } 
    if (api != null) {
      lst = this.devicemasterservices.getusersBymanagerId();
      String deviceDate = "NA";
      Date d1 = null;
      Date d2 = null;
      Date d = new Date();
      String strDate = format.format(d);
      String usedBefore = "";
      int finalMinutes = 0;
      int warningCount = 0;
      int failureCount = 0;
      String deviceStat = "";
      for (Object[] result : lst) {
        JSONObject jo = new JSONObject();
        List<Object[]> deviceDateList = this.Dfnservices
          .getDeviceDataById(Long.parseLong(result[5].toString()));
        if (deviceDateList.size() == 1) {
          for (Object[] dateresult : deviceDateList)
            deviceDate = dateresult[1].toString(); 
          try {
            d1 = format.parse(deviceDate);
            d2 = format.parse(strDate);
            long diff = d2.getTime() - d1.getTime();
            if (diff <= 0L) {
              usedBefore = "days=0,hrs=0,min=0,sec=0";
            } else {
              long diffMinutes = diff / 60000L % 60L;
              int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
              finalMinutes = days * 1440;
              long diffSeconds = diff / 1000L % 60L;
              long diffHours = diff / 3600000L % 24L;
              long diffDays = diff / 86400000L;
              usedBefore = String.valueOf(diffDays) + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds + 
                "sec ";
            } 
          } catch (Exception e) {
            e.printStackTrace();
          } 
          if (finalMinutes > Integer.parseInt(adminWarningminute) && 
            finalMinutes < Integer.parseInt(adminFailureminute)) {
            warningCount++;
            deviceStat = "Warning";
          } else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
            failureCount++;
            deviceStat = "Failure";
          } else {
            failureCount = 0;
            warningCount = 0;
            deviceStat = "NA";
          } 
        } else {
          deviceDate = "NA";
          usedBefore = "NA";
          deviceStat = "NA";
        } 
        jo.put("devicename", result[0].toString());
        jo.put("imei", result[1].toString());
        jo.put("simcardno", result[2].toString());
        jo.put("devicedescription", result[3].toString());
        jo.put("devicemodel", result[4].toString());
        jo.put("deviceid", result[5].toString());
        jo.put("deviceDate", deviceDate);
        jo.put("usedBefore", usedBefore);
        jo.put("deviceStatus", deviceStat);
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/GetDeviceInfoBySite/{siteId}/{managerId}/{key}"}, produces = {"application/json"})
  public String GetDeviceInfoBySite(@PathVariable long siteId, @PathVariable long managerId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Apikey api = this.apiService.findBykeyValue(key);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Object[]> lst = null;
    JSONArray jarray = new JSONArray();
    long userID = 2L;
    List<Object[]> deviceFailureManagerlist = this.Dfnservices.getAdminDeviceFailureData(managerId);
    String adminFailureminute = "0";
    String adminWarningminute = "0";
    if (deviceFailureManagerlist.size() == 0) {
      List<Object[]> deviceFailureAdminlist = this.Dfnservices.getAdminDeviceFailureData(userID);
      for (Object[] DeviceManagerresult : deviceFailureAdminlist) {
        adminFailureminute = DeviceManagerresult[0].toString();
        adminWarningminute = DeviceManagerresult[1].toString();
      } 
    } else {
      for (Object[] DeviceAdminresult : deviceFailureManagerlist) {
        adminFailureminute = DeviceAdminresult[0].toString();
        adminWarningminute = DeviceAdminresult[1].toString();
      } 
    } 
    if (api != null) {
      lst = this.devicemasterservices.getDeviceBySite(siteId);
      String deviceDate = "NA";
      Date d1 = null;
      Date d2 = null;
      Date d = new Date();
      String strDate = format.format(d);
      String usedBefore = "";
      int finalMinutes = 0;
      int warningCount = 0;
      int failureCount = 0;
      String deviceStat = "";
      for (Object[] result : lst) {
        JSONObject jo = new JSONObject();
        List<Object[]> deviceDateList = this.Dfnservices
          .getDeviceDataById(Long.parseLong(result[0].toString()));
        if (deviceDateList.size() == 1) {
          for (Object[] dateresult : deviceDateList)
            deviceDate = dateresult[1].toString(); 
          try {
            d1 = format.parse(deviceDate);
            d2 = format.parse(strDate);
            long diff = d2.getTime() - d1.getTime();
            if (diff < 0L) {
              diff = 0L;
              deviceStat = "success";
            } 
            long diffMinutes = diff / 60000L % 60L;
            int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            finalMinutes = days * 1440;
            long diffSeconds = diff / 1000L % 60L;
            long diffHours = diff / 3600000L % 24L;
            long diffDays = diff / 86400000L;
            usedBefore = String.valueOf(diffDays) + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds + 
              "sec ";
          } catch (Exception e) {
            e.printStackTrace();
          } 
          if (finalMinutes > Integer.parseInt(adminWarningminute) && 
            finalMinutes < Integer.parseInt(adminFailureminute)) {
            warningCount++;
            deviceStat = "Warning";
          } else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
            failureCount++;
            deviceStat = "Failure";
          } else {
            failureCount = 0;
            warningCount = 0;
            deviceStat = "NA";
          } 
        } else {
          deviceDate = "NA";
          usedBefore = "NA";
          deviceStat = "NA";
        } 
        jo.put("devicename", result[3].toString());
        jo.put("imei", result[6].toString());
        jo.put("simcardno", result[5].toString());
        jo.put("devicedescription", result[3].toString());
        jo.put("devicemodel", result[4].toString());
        jo.put("deviceid", result[0].toString());
        jo.put("deviceDate", deviceDate);
        jo.put("usedBefore", usedBefore);
        jo.put("deviceStatus", deviceStat);
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/GetmanagerDeviceList/{managetId}/{key}"}, produces = {"application/json"})
  public String getmanagercomponetlist(@PathVariable long managetId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Apikey api = this.apiService.findBykeyValue(key);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Object[]> lst = null;
    JSONArray jarray = new JSONArray();
    List<Object[]> deviceFailureAdminlist = this.Dfnservices.getAdminDeviceFailureData(managetId);
    String adminFailureminute = "0";
    String adminWarningminute = "0";
    if (deviceFailureAdminlist.size() == 0) {
      adminFailureminute = "0";
      adminWarningminute = "0";
    } else {
      for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
        adminFailureminute = DeviceAdminresult[0].toString();
        adminWarningminute = DeviceAdminresult[1].toString();
      } 
    } 
    if (api != null) {
      lst = this.devicemasterservices.getBymanagerId(managetId);
      String deviceDate = "NA";
      Date d1 = null;
      Date d2 = null;
      Date d = new Date();
      String strDate = format.format(d);
      String usedBefore = "";
      int finalMinutes = 0;
      int warningCount = 0;
      int failureCount = 0;
      String deviceStat = "";
      for (Object[] result : lst) {
        JSONObject jo = new JSONObject();
        List<Object[]> deviceDateList = this.Dfnservices
          .getDeviceDataById(Long.parseLong(result[5].toString()));
        if (deviceDateList.size() == 1) {
          for (Object[] dateresult : deviceDateList)
            deviceDate = dateresult[1].toString(); 
          try {
            d1 = format.parse(deviceDate);
            d2 = format.parse(strDate);
          } catch (ParseException e) {
            e.printStackTrace();
          } 
          long diff = d2.getTime() - d1.getTime();
          if (diff <= 0L) {
            diff = 0L;
            deviceStat = "Success";
          } 
          long diffSeconds = diff / 1000L % 60L;
          long diffMinutes = diff / 60000L % 60L;
          long diffHours = diff / 3600000L % 24L;
          long diffDays = diff / 86400000L;
          usedBefore = String.valueOf(diffDays) + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds + "sec ";
          if (finalMinutes > Integer.parseInt(adminWarningminute) && 
            finalMinutes < Integer.parseInt(adminFailureminute)) {
            warningCount++;
            deviceStat = "Warning";
          } else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
            failureCount++;
            deviceStat = "Failure";
          } else {
            failureCount = 0;
            warningCount = 0;
            deviceStat = "Never";
          } 
        } else {
          deviceDate = "NA";
          usedBefore = "NA";
          deviceStat = "NA";
        } 
        jo.put("devicename", result[0].toString());
        jo.put("imei", result[1].toString());
        jo.put("simcardno", result[2].toString());
        jo.put("devicedescription", result[3].toString());
        jo.put("devicemodel", result[4].toString());
        jo.put("deviceid", result[5].toString());
        jo.put("deviceDate", deviceDate);
        jo.put("usedBefore", usedBefore);
        jo.put("deviceStatus", deviceStat);
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/getdevicelocation/{rol}/{id}/{key}"}, produces = {"application/json"})
  public String getdevicelocation(@PathVariable String key, @PathVariable String rol, @PathVariable long id) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Apikey api = this.apiService.findBykeyValue(key);
    List<Object[]> list = null;
    if (rol.equalsIgnoreCase("ROLE_MANAGER")) {
      list = this.lasttrackservices.getDeviceLocationByManager(id);
    } else if (rol.equalsIgnoreCase("ROLE_USER")) {
      list = this.lasttrackservices.getDeviceLocationByUser(id);
    } 
    if (list != null && list.size() != 0) {
      JSONArray jarray = new JSONArray();
      for (Object[] result : list) {
        Date TodayDate = new Date();
        Date deviceDate = sdf.parse(result[4].toString());
        long difference = TodayDate.getTime() - deviceDate.getTime();
        long differenceHours = difference / 3600000L % 24L;
        long difference_In_Time = TodayDate.getTime() - deviceDate.getTime();
        long difference_In_Seconds = difference_In_Time / 1000L % 60L;
        long difference_In_Minutes = difference_In_Time / 60000L % 60L;
        long difference_In_Hours = difference_In_Time / 3600000L % 24L;
        long difference_In_Days = difference_In_Time / 86400000L % 365L;
        System.out.println(String.valueOf(String.valueOf(difference_In_Days)) + " days, " + difference_In_Hours + " hours, " + 
            difference_In_Minutes + " minutes, " + difference_In_Seconds + " seconds");
        JSONObject jo = new JSONObject();
        jo.put("deviceid", result[0].toString());
        jo.put("devicename", result[1].toString());
        jo.put("latitude", result[2].toString());
        jo.put("longitude", result[3].toString());
        jo.put("devicedate", 
            String.valueOf(String.valueOf(result[4].toString())) + "(" + difference_In_Days + " days, " + 
            difference_In_Hours + " hours, " + difference_In_Minutes + " minutes, " + 
            difference_In_Seconds + " seconds" + ")");
        jo.put("status", (differenceHours < 24L) ? "Online" : "Offline");
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  public String getDeviceInfoBySitealocation(@PathVariable("siteId") long siteId, @PathVariable("managerId") long managerId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    List<Object[]> list = this.lasttrackservices.getDeviceDataByIdLocation(managerId, siteId);
    if (list.size() != 0 && api != null) {
      JSONArray jarray = new JSONArray();
      for (Object[] result : list) {
        JSONObject jo = new JSONObject();
        jo.put("deviceid", result[0].toString());
        jo.put("devicename", result[1].toString());
        jo.put("latitude", result[2].toString());
        jo.put("longitude", result[3].toString());
        jo.put("devicedate", result[4].toString());
        jarray.put(jo);
        System.out.println(result[0].toString());
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/GetDeviceListmanager/{managetId}/{key}"}, produces = {"application/json"})
  public String GetDeviceListmanager(@PathVariable long managetId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    Apikey api = this.apiService.findBykeyValue(key);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Object[]> lst = null;
    JSONArray jarray = new JSONArray();
    List<Object[]> deviceFailureAdminlist = this.Dfnservices.getAdminDeviceFailureData(managetId);
    String adminFailureminute = "0";
    String adminWarningminute = "0";
    if (deviceFailureAdminlist.size() == 0) {
      adminFailureminute = "0";
      adminWarningminute = "0";
    } else {
      for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
        adminFailureminute = DeviceAdminresult[0].toString();
        adminWarningminute = DeviceAdminresult[1].toString();
      } 
    } 
    if (api != null) {
      lst = this.devicemasterservices.getBymanagerId(managetId);
      String deviceDate = "NA";
      Date d1 = null;
      Date d2 = null;
      Date d = new Date();
      String strDate = format.format(d);
      String usedBefore = "";
      int finalMinutes = 0;
      int warningCount = 0;
      int failureCount = 0;
      int success = 0;
      long diffMinutes = 0L;
      long diffMinutes2 = 0L;
      long diff = 0L;
      String deviceStat = "";
      for (Object[] result : lst) {
        JSONObject jo = new JSONObject();
        List<Object[]> deviceDateList = this.Dfnservices
          .getDeviceDataById(Long.parseLong(result[5].toString()));
        if (deviceDateList.size() == 1) {
          for (Object[] dateresult : deviceDateList)
            deviceDate = dateresult[1].toString(); 
          try {
            d1 = format.parse(deviceDate);
            d2 = format.parse(strDate);
            diff = d2.getTime() - d1.getTime();
            if (diff <= 0L) {
              usedBefore = "days=0,hrs=0,min=0,sec=0";
            } else {
              int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
              finalMinutes = days * 1440;
              long diffSeconds = diff / 1000L % 60L;
              diffMinutes = diff / 60000L % 60L;
              long diffHours = diff / 3600000L % 24L;
              long diffDays = diff / 86400000L;
              diffMinutes2 = diff / 60000L;
              usedBefore = String.valueOf(diffDays) + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds + 
                "sec ";
            } 
          } catch (Exception e) {
            e.printStackTrace();
          } 
          if (diffMinutes2 == 0L) {
            success++;
            deviceStat = "success";
          } else if (diffMinutes2 > Integer.parseInt(adminWarningminute) && 
            diffMinutes2 < Integer.parseInt(adminFailureminute)) {
            warningCount++;
            deviceStat = "Warning";
          } else if (diffMinutes2 > Integer.parseInt(adminFailureminute)) {
            failureCount++;
            deviceStat = "Failure";
          } 
        } else {
          deviceDate = "NA";
          usedBefore = "NA";
          deviceStat = "Never";
        } 
        jo.put("devicename", result[0].toString());
        jo.put("imei", result[1].toString());
        jo.put("simcardno", result[2].toString());
        jo.put("devicedescription", result[3].toString());
        jo.put("devicemodel", result[4].toString());
        jo.put("deviceid", result[5].toString());
        jo.put("deviceDate", deviceDate);
        jo.put("usedBefore", usedBefore);
        jo.put("deviceStatus", deviceStat);
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/getDevieByManager/{managerId}/{key}"}, produces = {"application/json"})
  public String getDevieByManager(@PathVariable long managerId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      JSONArray jarray = new JSONArray();
      List<Object[]> deviceList = this.devicemasterservices.getDevieByManager(managerId);
      for (Object[] deviceResult : deviceList) {
        List<Object[]> profListNew = this.devicemasterservices
          .getProfileByDevice(Long.parseLong(deviceResult[2].toString()));
        for (Object[] profileResultNew : profListNew) {
          JSONObject jo = new JSONObject();
          jo.put("deviceName", deviceResult[1].toString());
          jo.put("profileName", profileResultNew[1].toString());
          jo.put("profileId", profileResultNew[0].toString());
          jarray.put(jo);
        } 
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(method = {RequestMethod.POST}, value = {"api/saveProfileDigitalSetting/{devicename}/{uid}"}, produces = {"application/json"})
  public String saveProfileDigitalSetting(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable String devicename, @PathVariable String uid) throws JsonParseException, JsonMappingException, IOException {
    String[] digitaldata = request.getParameterValues("digitaldata[]");
    String[] dreverse = request.getParameterValues("dreverse[]");
    JSONObject jo = new JSONObject();
    JSONArray digitaljsonarr = new JSONArray();
    for (int i = 0; i < digitaldata.length; i++) {
      String[] digidata = digitaldata[i].split("#");
      JSONObject digitaljo = new JSONObject();
      digitaljo.put("criticalness", Boolean.parseBoolean(dreverse[i]));
      digitaljo.put("parameterId", digidata[0]);
      digitaljo.put("parametername", digidata[1]);
      digitaljsonarr.put(digitaljo);
      ProfileDigitalSetting pds = new ProfileDigitalSetting();
      pds.setProfilename(devicename);
      pds.setCriticalness(dreverse[i]);
      pds.setParamname(digidata[1]);
      pds.setUserId(Long.valueOf(Long.parseLong(uid)));
      this.profileDigitalService.save(pds);
    } 
    jo.put("Digital", digitaljsonarr);
    return "ProfileDigitalSetting";
  }
  
  @RequestMapping(method = {RequestMethod.POST}, value = {"api/saveProfileAnalogSetting/{devicename}/{uid}"}, produces = {"application/json"})
  public String saveProfileAnalogSetting(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable String devicename, @PathVariable String uid) throws JsonParseException, JsonMappingException, IOException {
    String[] analogdata = request.getParameterValues("analogdata[]");
    String[] warnMins = request.getParameterValues("warnMin[]");
    String[] failMins = request.getParameterValues("failMin[]");
    String[] warnSign = request.getParameterValues("warnSign[]");
    String[] failSign = request.getParameterValues("failSign[]");
    JSONObject jo = new JSONObject();
    JSONArray analogjsonarr = new JSONArray();
    for (int i = 0; i < analogdata.length; i++) {
      String[] analogdataNew = analogdata[i].split("#");
      JSONObject analogjo = new JSONObject();
      analogjo.put("parameterId", analogdataNew[0]);
      analogjo.put("parametername", analogdataNew[1]);
      analogjo.put("warnMins", warnMins[i]);
      analogjo.put("failMins", failMins[i]);
      analogjo.put("warnSign", warnSign[i]);
      analogjo.put("failSign", failSign[i]);
      analogjsonarr.put(analogjo);
      ProfileAnalogSetting pas = new ProfileAnalogSetting();
      pas.setProfilename(devicename);
      pas.setWarnMin(warnMins[i]);
      pas.setFailMin(failMins[i]);
      pas.setParamname(analogdataNew[1]);
      pas.setRuleSignature(failSign[i]);
      pas.setUserId(Long.valueOf(Long.parseLong(uid)));
      this.profileAnalogService.save(pas);
    } 
    jo.put("Analog", analogjsonarr);
    System.out.println("JSONString::" + jo.toString());
    model.addAttribute("jsonresponse", "AnalogProfileSetting Sucessfully saved");
    return "AnalogProfileSetting Sucessfully saved";
  }
  
  @RequestMapping(value = {"/api/getDigitalSettList/{key}/{managId}"}, produces = {"application/json"})
  public String getDigitalSettList(HttpServletRequest request, @PathVariable String key, @PathVariable long managId) {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      List<Object[]> list = new ArrayList();
      list = this.profileDigitalService.getDigitalSettingList(managId);
      JSONArray jarray = new JSONArray();
      for (Object[] result1 : list) {
        JSONObject jo = new JSONObject();
        jo.put("setId", result1[0].toString());
        jo.put("criticalness", result1[1].toString());
        jo.put("paramName", result1[2].toString());
        jo.put("profileName", result1[3].toString());
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/getAnalogSettList/{key}/{managId}"}, produces = {"application/json"})
  public String getAnalogSettList(HttpServletRequest request, @PathVariable String key, @PathVariable long managId) {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      List<Object[]> list = new ArrayList();
      list = this.profileAnalogService.getAnalogSettingList(managId);
      JSONArray jarray = new JSONArray();
      for (Object[] result1 : list) {
        JSONObject jo = new JSONObject();
        jo.put("setId", result1[0].toString());
        jo.put("failMin", result1[1].toString());
        jo.put("paramName", result1[2].toString());
        jo.put("profName", result1[3].toString());
        jo.put("ruleSign", result1[4].toString());
        jo.put("warnMin", result1[5].toString());
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/delAnalogSett/{settid}/{key}"})
  public String delAnalogSett(@PathVariable Long settid, @PathVariable String key) {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      this.profileAnalogService.delete(settid);
      return (new SpringException(true, "Setting Successfully Deleted.")).toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/delDigitalSett/{settid}/{key}"})
  public String delDigitalSett(@PathVariable Long settid, @PathVariable String key) {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      this.profileDigitalService.delete(settid);
      return (new SpringException(true, "Setting Successfully Deleted.")).toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/getAnalogSettingsDevice/{managerId}/{key}"}, produces = {"application/json"})
  public String getAnalogSettingsDevice(@PathVariable long managerId, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      JSONArray jarray = new JSONArray();
      List<Object[]> deviceList = this.devicemasterservices.getAnalogSettingsDevice(managerId);
      if (deviceList != null)
        for (Object[] deviceResult : deviceList) {
          JSONObject jo = new JSONObject();
          jo.put("id", deviceResult[0].toString());
          jo.put("name", deviceResult[1].toString());
          jarray.put(jo);
        }  
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"/api/UpdateCommonDevice/{deviceid}/{devicemodel}/{devicname}/{imei}/{cardno}/{desc}/{adminId}/{managetId}/{prid}/{altdevicename}/{key}"}, produces = {"application/json"})
  public String UpdateCommonDevice(@PathVariable Long deviceid, @PathVariable String devicname, @PathVariable String devicemodel, @PathVariable String imei, @PathVariable String cardno, @PathVariable String desc, @PathVariable long adminId, @PathVariable long managetId, @PathVariable long prid, @PathVariable String altdevicename, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      DeviceProfile dp = new DeviceProfile();
      dp = this.profileService.get(Long.valueOf(prid));
      Devicemaster mst = new Devicemaster();
      mst.setDeviceid(deviceid);
      mst.setDevicedescription(desc);
      mst.setDevicemodel(devicemodel);
      mst.setDevicename(devicname);
      mst.setDp(dp);
      mst.setFlagcondition(Boolean.valueOf(true));
      mst.setImei(imei);
      mst.setSimcardno(cardno);
      mst.setUserId(Long.valueOf(adminId));
      mst.setManagerId(Long.valueOf(managetId));
      mst.setAltdevicename(altdevicename);
      this.devicemasterservices.updateDevice(mst);
    } else {
      return (new SpringException(true, "Some thing Wrong !")).toString();
    } 
    return (new SpringException(true, "Device Successfully Added!")).toString();
  }
  
  @RequestMapping(value = {"/api/AddCommonDevice/{devicname}/{devicemodel}/{imei}/{cardno}/{desc}/{adminId}/{managetId}/{SiteId}/{prid}/{altdevicename}/{key}"}, produces = {"application/json"})
  public String AddCommonDevice(@PathVariable String devicname, @PathVariable String imei, @PathVariable String devicemodel, @PathVariable String cardno, @PathVariable String desc, @PathVariable long adminId, @PathVariable long managetId, @PathVariable long SiteId, @PathVariable long prid, @PathVariable String altdevicename, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Devicemaster dm = this.devicemasterservices.findByImei(imei);
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null && dm == null) {
      DeviceProfile dp = new DeviceProfile();
      dp = this.profileService.get(Long.valueOf(prid));
      Devicemaster mst = new Devicemaster();
      mst.setDevicedescription(desc);
      mst.setDevicemodel(devicemodel);
      mst.setDevicename(devicname);
      mst.setDp(dp);
      mst.setFlagcondition(Boolean.valueOf(true));
      mst.setImei(imei);
      mst.setSimcardno(cardno);
      mst.setUserId(Long.valueOf(adminId));
      mst.setManagerId(Long.valueOf(managetId));
      mst.setAltdevicename(altdevicename);
      this.devicemasterservices.saveDevice(mst);
      if (SiteId != 0L) {
        AssignSite assgn = new AssignSite();
        assgn.setDeviceid(mst.getDeviceid());
        assgn.setManagerid(Long.valueOf(managetId));
        assgn.setSiteid(Long.valueOf(SiteId));
        this.Siteservices.saveassignsite(assgn);
      } 
    } else {
      return (new SpringException(true, "Device Aleredy Exits!")).toString();
    } 
    return (new SpringException(true, "Device Successfully Added!")).toString();
  }
  
  @RequestMapping(value = {"/api/getDeviceById/{deviceId}"}, produces = {"application/json"})
  public String getDeviceById(@PathVariable long deviceId) throws JsonGenerationException, JsonMappingException, IOException {
    JSONArray jarray = new JSONArray();
    Devicemaster deviceList = this.devicemasterservices.findOne(Long.valueOf(deviceId));
    if (deviceList != null)
      return JsonUtills.JavaToJson(deviceList); 
    return jarray.toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/VodeoconDigitalData/{id}"}, produces = {"application/json"})
  public String VodeoconDigitalData(@PathVariable long id) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode analogNodearrayNode = mapper.createArrayNode();
    List<Object[]> grideData = this.lasttrackservices.GetVodeoconLastTrack(id);
    Object[] result1 = grideData.get(0);
    ObjectNode DIGITALNode = mapper.createObjectNode();
    if (result1[0] != null)
    {
    	DIGITALNode.putPOJO("ACMAINS_FAIL", 
    	          result1[0].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/acmains_fail_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/acmains_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[1] != null)
    {
    	 DIGITALNode.putPOJO("Fire", 
    	          result1[1].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/fire_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/fire_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[2] != null)
    {
    	  DIGITALNode.putPOJO("Door", 
    	          result1[2].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/door_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/door_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[3] != null)
    {
    	 DIGITALNode.putPOJO("DG_Running_Hrs", 
    	          result1[3].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/dg_running_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/dg_running_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[4] != null)
    {
    	 DIGITALNode.putPOJO("DG_Fault", 
    	          result1[4].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/dg_fault_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/dg_fault_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[5] != null)
    {
    	 DIGITALNode.putPOJO("Battry_Low", 
    	          result1[5].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/battery_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/battery_off.png'  class='center' alt='Image Not Found'>");
    }
    if (result1[6] != null)
    {
    	 DIGITALNode.putPOJO("PP_Input_Fail", result1[6].toString().equalsIgnoreCase("1") ? 
    	          "<img src='../../img/digital/pp_input_fail_on.png'  class='center' alt='Image Not Found'>" : 
    	          "<img src='../../img/digital/pp_input_fail_off.png'  class='center' alt='Image Not Found'>");
    }
    analogNodearrayNode.add((JsonNode)DIGITALNode); 
    
    
    
    
    
    
    
 /*   if (result1[0] != null && result1[1] != null && result1[2] != null && result1[3] != null && result1[4] != null && 
      result1[5] != null && result1[6] != null) {
      ObjectNode DIGITALNode = mapper.createObjectNode();
      DIGITALNode.putPOJO("ACMAINS_FAIL", 
          result1[0].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/acmains_fail_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/acmains_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Fire", 
          result1[1].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/fire_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/fire_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Door", 
          result1[2].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/door_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/door_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("DG_Running_Hrs", 
          result1[3].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/dg_running_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/dg_running_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("DG_Fault", 
          result1[4].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/dg_fault_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/dg_fault_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Battry_Low", 
          result1[5].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/battery_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/battery_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("PP_Input_Fail", result1[6].toString().equalsIgnoreCase("1") ? 
          "<img src='../../img/digital/pp_input_fail_on.png'  class='center' alt='Image Not Found'>" : 
          "<img src='../../img/digital/pp_input_fail_off.png'  class='center' alt='Image Not Found'>");
      analogNodearrayNode.add((JsonNode)DIGITALNode);
    } else {
      ObjectNode DIGITALNode = mapper.createObjectNode();
      DIGITALNode.putPOJO("devicedate", "");
      DIGITALNode.putPOJO("ACMAINS_FAIL", 
          "<img src='../../img/digital/acmains_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Fire", 
          "<img src='../../img/digital/fire_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Door", 
          "<img src='../../img/digital/door_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("DG_Running_Hrs", 
          "<img src='../../img/digital/dg_running_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("DG_Fault", 
          "<img src='../../img/digital/dg_fault_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("Battry_Low", 
          "<img src='../../img/digital/battery_off.png'  class='center' alt='Image Not Found'>");
      DIGITALNode.putPOJO("PP_Input_Fail", 
          "<img src='../../img/digital/pp_input_fail_off.png'  class='center' alt='Image Not Found'>");
      analogNodearrayNode.add((JsonNode)DIGITALNode);
    } */
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
  }
  
  @RequestMapping(value = {"/api/getdevicelocationGroup/{userId}"}, produces = {"application/json"})
  public String getdevicelocationGroup(@PathVariable long userId) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    List<Long> devices = new ArrayList<>();
    User user = this.userService.getuserbyid(userId);
    JSONObject dataObject = new JSONObject();
    List<Object[]> deliveList = this.Devicerepository.managetDeviceList(this.userService.getuserbyid(userId).getAddedby().longValue(), 
        userId);
    if(user.getId()==1244785750)
    log.info("SAJANdeliveList : "+deliveList.size());
    deliveList.forEach(o -> devices.add(Long.valueOf(Long.parseLong(o[0].toString()))));
    List<Site> getsitelist = this.historyservices.getsitelistbymid(user.getAddedby());
    if(user.getId()==1244785750)
    log.info("SAJANgetsitelist : "+getsitelist.size());
    for (Site site : getsitelist) {
      JSONArray dataArray = new JSONArray();
      List<Object[]> assignDeviceList = this.assignsiteservices.getAssignedDeviceBySite(site.getSiteid().longValue());
      for (Object[] assignDevice : assignDeviceList) {
    	  if(user.getId()==1244785750)
    	  log.info("SAJANgetsitelist : "+devices.contains(Long.valueOf(Long.parseLong(assignDevice[0].toString()))));
        if (devices.contains(Long.valueOf(Long.parseLong(assignDevice[0].toString())))) {
          List<Object[]> result = this.lasttrackservices.getDeviceLocation(userId, 
              Long.parseLong(assignDevice[0].toString()));
          if (!result.isEmpty()) {
            Date TodayDate = new Date();
            Date deviceDate = sdf.parse(((Object[])result.get(0))[4].toString());
            long difference_In_Time = TodayDate.getTime() - deviceDate.getTime();
            long difference_In_Seconds = difference_In_Time / 1000L % 60L;
            long difference_In_Minutes = difference_In_Time / 60000L % 60L;
            long difference_In_Hours = difference_In_Time / 3600000L % 24L;
            long difference_In_Days = difference_In_Time / 86400000L % 365L;
            long differenceHours = TimeUnit.MILLISECONDS
              .toHours(TodayDate.getTime() - deviceDate.getTime());
            JSONObject jo = new JSONObject();
            jo.put("deviceid", ((Object[])result.get(0))[0].toString());
            jo.put("devicename", ((Object[])result.get(0))[1].toString());
            jo.put("latitude", ((Object[])result.get(0))[2].toString());
            jo.put("longitude", ((Object[])result.get(0))[3].toString());
            jo.put("devicedate", 
                String.valueOf(((Object[])result.get(0))[4].toString()) + "(" + difference_In_Days + " days, " + difference_In_Hours + 
                " hours, " + difference_In_Minutes + " minutes, " + difference_In_Seconds + 
                " seconds" + ")");
            jo.put("status", (differenceHours < 2L) ? "Online" : "Offline");
            dataArray.put(jo);
          } 
        } 
      }   
      if(dataArray.length()>0)
      dataObject.put(site.getSite_name(), dataArray);
    } 
    return dataObject.toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/VodeoconAnalogVoltlData/{DeviceId}"}, produces = {"application/json"})
  public String VodeoconAnalogVoltlData(@PathVariable long DeviceId) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode analogNodearrayNode = mapper.createArrayNode();
    List<Object[]> BatteryData = this.lasttrackservices.GetVodeoconAnalogVoltlData("6387981", DeviceId);
    List<Object[]> FuelData = this.lasttrackservices.GetVodeoconAnalogVoltlData("6387982", DeviceId);
    Devicemaster Alnalogdevice = this.Devicerepository.findBydeviceid(Long.valueOf(DeviceId));
    ObjectNode DIGITALNode = mapper.createObjectNode();
    if (BatteryData.size() != 0 ) {
      Object[] BatteryResult = BatteryData.get(0);
      DIGITALNode.putPOJO("Battry", 
          "Battery : " + BigDecimal.valueOf(Double.parseDouble(BatteryResult[0].toString())).setScale(3, 
            RoundingMode.HALF_UP) + " Volt");
      //analogNodearrayNode.add((JsonNode)DIGITALNode);
    }/*else
    	 DIGITALNode.putPOJO("Battry", "0 Volt");*/
    
    if (FuelData.size() != 0) {
        Object[] FuelResult = FuelData.get(0);
        Analogdata analog = this.AnalogDataservice.findBydevice(Alnalogdevice);
     //   ObjectNode DIGITALNode = mapper.createObjectNode();
        if (analog != null) {
          double gridefuel = JsonUtills.fuel(analog, Double.parseDouble(FuelResult[0].toString()));
          DIGITALNode.putPOJO("Fuel","Fuel : " + BigDecimal.valueOf(gridefuel).setScale(3, RoundingMode.HALF_UP) + " Ltr");
          log.info("analog :: " + analog.getAnaloglevel1());
        } else {
          DIGITALNode.putPOJO("Fuel", "Fuel : " + FuelResult[0].toString() + " Volt");
        } 
     //   analogNodearrayNode.add((JsonNode)DIGITALNode);
      }/*else
    	  DIGITALNode.putPOJO("Fuel", "0");*/
    
    analogNodearrayNode.add((JsonNode)DIGITALNode);
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/getUserprofileBySiteId/{userId}/{siteId}"}, produces = {"application/json"})
  public String getUserprofileBySiteId(@PathVariable("userId") long userId, @PathVariable("siteId") long siteId) throws JsonGenerationException, JsonMappingException, IOException {
    List<Object[]> list = this.devicemasterservices.getUserprofileBySiteId(userId, siteId);
    JSONArray jarray = new JSONArray();
    for (Object[] result : list) {
      JSONObject jo = new JSONObject();
      jo.put("profileName", result[0].toString());
      jo.put("profileId", result[1].toString());
      jarray.put(jo);
    } 
    return jarray.toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/getAssignDeviceByProfile/{userId}/{profileId}"}, produces = {"application/json"})
  public String getAssignDeviceByProfile(@PathVariable("userId") long userId, @PathVariable("profileId") long profileId) throws JsonGenerationException, JsonMappingException, IOException {
    List<Object[]> list = this.devicemasterservices.getprofileDevice(userId, profileId);
    JSONArray jarray = new JSONArray();
    for (Object[] result : list) {
      JSONObject jo = new JSONObject();
      jo.put("deviceId", result[0].toString());
      jo.put("deviceName", String.valueOf(String.valueOf(result[1].toString())) + "(" + result[2].toString() + ")");
      jarray.put(jo);
    } 
    return jarray.toString();
  }
  
  @RequestMapping(value = {"/api/getUserProfile/{userId}"}, produces = {"application/json"})
  public String getUserProfile(@PathVariable Long userId) {
    JSONArray finalMainJSON = new JSONArray();
    List<Object[]> devicelist = this.devicemasterservices.getDeviceAssignForUser(userId.longValue());
    if (devicelist.size() != 0)
      for (Object[] deviceData : devicelist) {
        JSONObject deviceObject = new JSONObject();
        Devicemaster device = this.devicemasterservices
          .findOne(Long.valueOf(Long.parseLong(deviceData[1].toString())));
        deviceObject.put("ProfileId", device.getDp().getPrid());
        deviceObject.put("ProfileName", device.getDp().getProfilename());
        finalMainJSON.put(deviceObject);
      }  
    return finalMainJSON.toString();
  }
  
  @RequestMapping(value = {"/api/getDeviceDataWithLastTrackData/{userId}"}, produces = {"application/json"})
  public String getDeviceDataWithLastTrackData(@PathVariable long userId) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    List<Long> devices = new ArrayList<>();
    User user = this.userService.getuserbyid(userId);
    JSONObject dataObject = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();
    List<Object[]> deliveList = this.Devicerepository.managetDeviceList(this.userService.getuserbyid(userId).getAddedby().longValue(), 
        userId);
    deliveList.forEach(o -> devices.add(Long.valueOf(Long.parseLong(o[0].toString()))));
    JSONArray dataArray = new JSONArray();
    List<Site> getsitelist = this.historyservices.getsitelistbymid(user.getAddedby());
    for (Site site : getsitelist) {
      List<Object[]> assignDeviceList = this.assignsiteservices.getAssignedDeviceBySite(site.getSiteid().longValue());
      for (Object[] assignDevice : assignDeviceList) {
        if (devices.contains(Long.valueOf(Long.parseLong(assignDevice[0].toString())))) {
          List<Object[]> result = this.lasttrackservices.getDeviceLocation(userId, 
              Long.parseLong(assignDevice[0].toString()));
          if (!result.isEmpty()) {
            Date TodayDate = new Date();
            Date deviceDate = sdf.parse(((Object[])result.get(0))[4].toString());
            long difference_In_Time = TodayDate.getTime() - deviceDate.getTime();
            long difference_In_Seconds = difference_In_Time / 1000L % 60L;
            long difference_In_Minutes = difference_In_Time / 60000L % 60L;
            long difference_In_Hours = difference_In_Time / 3600000L % 24L;
            long difference_In_Days = difference_In_Time / 86400000L % 365L;
            long differenceHours = TimeUnit.MILLISECONDS
              .toHours(TodayDate.getTime() - deviceDate.getTime());
            Devicemaster deviceList = this.devicemasterservices.findOne(Long.valueOf(((Object[])result.get(0))[0].toString()));
            SocketMessageLog sktmsg = (SocketMessageLog)this.socketmsgrepository.findOne(Long.valueOf(((Object[])result.get(0))[0].toString()));
            JSONObject jo = new JSONObject();
            jo.put("deviceid", ((Object[])result.get(0))[0].toString());
            jo.put("imei", deviceList.getImei());
            jo.put("devicename", ((Object[])result.get(0))[1].toString());
            jo.put("devicedate", ((Object[])result.get(0))[4].toString());
            jo.put("status", (differenceHours < 2L) ? "Online" : "Offline");
            jo.put("Site", site.getSite_name());
            Lasttrack ltrack = this.lasttrackservices.findOne(deviceList.getDeviceid());
            JSONObject gpdJsonObject = new JSONObject(mapper.writeValueAsString(ltrack.getGpsdata()));
            jo.put("relayStatus", (sktmsg == null) ? "NA" : sktmsg.getMessage());
            log.info("gpdJsonObject :: " + gpdJsonObject);
            if (gpdJsonObject.has("relay")) {
              if (gpdJsonObject.get("relay").toString().equalsIgnoreCase("0")) {
                jo.put("gpdJsonObject", "OFF");
              } else {
                jo.put("gpdJsonObject", "ON");
              } 
            } else {
              jo.put("gpdJsonObject", "OFF");
            } 
            dataArray.put(jo);
          } 
        } 
      } 
    } 
    return dataArray.toString();
  }
}

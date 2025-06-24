package com.bonrix.dggenraterset.Controller;

import com.bonrix.dggenraterset.Controller.AlertMsgController;
import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.Tag;
import com.bonrix.dggenraterset.Model.TagType;
import com.bonrix.dggenraterset.Model.UserGroup;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.UserService;
import com.bonrix.dggenraterset.Utility.Candle;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.bonrix.dggenraterset.Utility.MovingAvg;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***
 * 
 * 
 * 
 * @author Sajan
 * This Controller is Updated on 30-01-2025
 *
 */
@CrossOrigin(origins = {"*"})
@Transactional
@RestController
public class AlertMsgController {
  @Autowired
  HistoryServices hstServide;
  
  @Autowired
  ParameterServices parameterservices;
  
  @Autowired
  UserService userService;
  
  @Autowired
  LasttrackServices lasttrackservices;
  
  @Autowired
  ApiService apiService;
  
  @Autowired
  DevicemasterServices devicemasterservices;
  
  private Logger log = Logger.getLogger(AlertMsgController.class);
  
  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  
  @RequestMapping(value = {"/api/messagetemp/{userid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<MessageTemplate> getmessagelistByuserid(@PathVariable Long userid) {
    return this.hstServide.getlistByUser_id(userid);
  }
  
  @RequestMapping(value = {"/api/gettagtype"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<TagType> gettagtype() {
    return this.hstServide.findAll();
  }
  
  @RequestMapping(value = {"/api/getsubtagbytag/{tag}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<Tag> getsubtagbytag(@PathVariable String tag) {
    return this.hstServide.getsubtagbytag(tag);
  }
  
  @RequestMapping(method = {RequestMethod.POST}, value = {"/api/savemessagetemplate"})
  @ExceptionHandler({SpringException.class})
  public String savemessagetemplate(@RequestBody MessageTemplate messagetemplate) {
    this.hstServide.savemsgtemplate(messagetemplate);
    return (new SpringException(true, "Message Template Successfully Saved ! ")).toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"api/updatemessagetemplate/{mid}/{templatename}/{templatetype}/{message}/{userid}/{role}"})
  public String updateusers(@PathVariable long mid, @PathVariable String templatename, @PathVariable String templatetype, @PathVariable String message, @PathVariable long userid, @PathVariable String role) {
    MessageTemplate msgt = new MessageTemplate();
    msgt.setCreatedby(role);
    msgt.setTemplatename(templatename);
    msgt.setTemplatetype(templatetype);
    msgt.setUserid(Long.valueOf(userid));
    msgt.setMessage(message);
    msgt.setMid(Long.valueOf(mid));
    this.hstServide.updatemsgtemplate(msgt);
    return (new SpringException(true, "MessageTemplate Sucessfully Updated!")).toString();
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/deletemessagetemplate/{m_id}"})
  public String deletemessagetemplate(@PathVariable long m_id) {
    return this.hstServide.deletemessagetemplate(Long.valueOf(m_id));
  }
  
  @RequestMapping(value = {"/api/getparameterbydigital/{prmtype}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<Parameter> getparameterbydigital(@PathVariable String prmtype) {
    return this.parameterservices.getlistByprmtype(prmtype);
  }
  
  @RequestMapping(value = {"/api/getmessagetemplatebytype/{templetetype}/{uid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<MessageTemplate> getmessagetemplatebytype(@PathVariable String templetetype, @PathVariable Long uid) {
    return this.hstServide.getmessagetemplatebytype(templetetype, uid);
  }
  
  @RequestMapping(value = {"/api/getstafflistbymid/{manager_id}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String getstafflistbymid(@PathVariable Long manager_id) {
    List<Object[]> list = new ArrayList();
    list = this.userService.getUserlistnew(manager_id);
    JSONArray jarray = new JSONArray();
    if (!list.isEmpty()) {
      String id = "";
      String enabled = "";
      String username = "";
      String contact = "";
      String email = "";
      String name = "";
      for (Object[] result1 : list) {
        id = result1[0].toString();
        enabled = result1[1].toString();
        username = result1[3].toString();
        contact = result1[4].toString();
        email = result1[5].toString();
        name = result1[6].toString();
        JSONObject jo = new JSONObject();
        jo.put("id", id);
        jo.put("username", username);
        jo.put("contact", contact);
        jo.put("email", email);
        jo.put("enabled", enabled);
        jo.put("name", name);
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return jarray.toString();
  }
  
  @RequestMapping(value = {"/api/getstaffgrouplistbymid/{managerid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<UserGroup> getstaffgrouplistbymid(@PathVariable Long managerid) {
    return this.hstServide.getstaffgrouplistbymid(managerid);
  }
  
  @RequestMapping(value = {"/api/getsitelistbymid/{managerid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<Site> getsitelistbymid(@PathVariable Long managerid) {
    return this.hstServide.getsitelistbymid(managerid);
  }
  
  @RequestMapping(value = {"/api/getdevicelistbymid/{manager_id}"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public List<Devicemaster> getdevicelistbymid(@PathVariable Long manager_id) {
    return this.hstServide.getdevicelistbymid(manager_id);
  }
  
  @RequestMapping(value = {"/api/getemaillistBytemplatebytype/{templetetype}/{uid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<EmailTemplate> getemaillistBytemplatebytype(@PathVariable String templetetype, @PathVariable Long uid) {
    return this.hstServide.getemaillistBytemplatebytype(templetetype, uid);
  }
  
  @RequestMapping(value = {"api/savedigitalinputalert"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public String savedigitalinputalert(@RequestParam Long Uid, @RequestParam Long Staffgroup, @RequestParam String Staffselect, @RequestParam Long Stafflist, @RequestParam String Deviceselect, @RequestParam Long Devicelist, @RequestParam Long Site, @RequestParam String Digitalinputs, @RequestParam String Status, @RequestParam String Mobilenumber, @RequestParam String Emailaddress, @RequestParam Long Smstemplate, @RequestParam Long Emailtemplate, @RequestParam String Alertlimit, @RequestParam String Starttime, @RequestParam String Endtime, @RequestParam String Notification) {
    DigitalInputAlert dia = new DigitalInputAlert();
    dia.setUser_id(Stafflist);
    dia.setUsergroup_id(Staffgroup);
    dia.setDeviceid(Devicelist);
    dia.setSite_id(Site);
    dia.setDigitalinput(Digitalinputs);
    dia.setStatus(Status);
    dia.setMobileno(Mobilenumber);
    dia.setEmail_id(Emailaddress);
    dia.setAlertlimit(Alertlimit);
    dia.setMessagetemplate_id(Smstemplate);
    dia.setEmailtemplate_id(Emailtemplate);
    dia.setTiming(String.valueOf(Starttime) + "#" + Endtime);
    dia.setNotification(Notification);
    dia.setManagerid(Uid);
    this.hstServide.savedigitalinputalert(dia);
    return "Susessfully Saved";
  }
  
  @RequestMapping(value = {"/api/editdigitalinputalert/{no}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<DigitalInputAlert> editdigitalinputalert(@PathVariable Long no) {
    return this.hstServide.editdigitalinputalert(no);
  }
  
  @RequestMapping(value = {"api/updatedigitalinputalert"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public String updatedigitalinputalert(@RequestParam Long No, @RequestParam Long Uid, @RequestParam Long Staffgroup, @RequestParam String Staffselect, @RequestParam Long Stafflist, @RequestParam String Deviceselect, @RequestParam Long Devicelist, @RequestParam Long Site, @RequestParam String Digitalinputs, @RequestParam String Status, @RequestParam String Mobilenumber, @RequestParam String Emailaddress, @RequestParam Long Smstemplate, @RequestParam Long Emailtemplate, @RequestParam String Alertlimit, @RequestParam String Starttime, @RequestParam String Endtime, @RequestParam String Notification) {
    DigitalInputAlert dia = new DigitalInputAlert();
    dia.setNo(No);
    dia.setUser_id(Stafflist);
    dia.setUsergroup_id(Staffgroup);
    dia.setDeviceid(Devicelist);
    dia.setSite_id(Site);
    dia.setDigitalinput(Digitalinputs);
    dia.setStatus(Status);
    dia.setMobileno(Mobilenumber);
    dia.setEmail_id(Emailaddress);
    dia.setAlertlimit(Alertlimit);
    dia.setMessagetemplate_id(Smstemplate);
    dia.setEmailtemplate_id(Emailtemplate);
    String newtiming = String.valueOf(Starttime) + "#" + Endtime;
    dia.setTiming(newtiming);
    dia.setNotification(Notification);
    dia.setManagerid(Uid);
    this.hstServide.updatedigitalinputalert(dia);
    return "SuccessFully Updated";
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/deletedigitalinputalert/{no}"})
  public String deletedigitalinputalert(@PathVariable long no) {
    return this.hstServide.deletedigitalinputalert(Long.valueOf(no));
  }
  
  @RequestMapping(value = {"/api/displaydigitalinputalert/{managerid}/{key}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String displaydigitalinputalert(@PathVariable Long managerid, @PathVariable String key) {
    List<Object[]> list = this.hstServide.displaydigitalinputalert(managerid);
    JSONArray jarray2 = new JSONArray();
    JSONObject digitalobj2 = new JSONObject();
    for (int i = 0; i < list.size(); i++) {
      JSONObject dalertobj = new JSONObject();
      Object[] result = list.get(i);
      dalertobj.put("no", result[0]);
      dalertobj.put("alertlimit", result[1]);
      dalertobj.put("devicename", result[2]);
      dalertobj.put("digitalinput", result[3].toString());
      dalertobj.put("emai_id", result[4]);
      dalertobj.put("emailtemplatename", result[5]);
      dalertobj.put("managerid", result[6]);
      dalertobj.put("messagetemplatename", result[7]);
      dalertobj.put("mobileno", result[8]);
      dalertobj.put("notification", result[9]);
      dalertobj.put("sitename", result[10]);
      dalertobj.put("status", result[11]);
      dalertobj.put("timing", result[12]);
      dalertobj.put("username", result[13]);
      dalertobj.put("usergroupname", result[14]);
      jarray2.put(dalertobj);
    } 
    digitalobj2.put("data", jarray2);
    return digitalobj2.toString();
  }
  
  @RequestMapping(value = {"/api/GetDeltaMeterRPT23/{id}/{startdate}/{enddate}/{prmname}"}, produces = {"application/json"})
  public String getEnergyMeterRpt24(@PathVariable Long id, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String prmname) throws JsonGenerationException, JsonMappingException, IOException {
    Devicemaster devicemaster = this.devicemasterservices.get(id);
    List list = hstServide.deltameterrpt(id, startdate, enddate, prmname);
	List list5 = hstServide.getprofileanalogunit(devicemaster.getDp().getPrid(), prmname);
	List list6 = hstServide.getperametername(Long.parseLong(prmname));
	String analogunit = (String) list5.get(0);
    JSONArray finalMainJSON = new JSONArray();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
    	  LinkedHashMap<String, String> myTreeHashMap = new LinkedHashMap<String, String>();
			Object[] result = (Object[]) list.get(i);
			 myTreeHashMap.put("sr",""+ i + 1);
		        myTreeHashMap.put("Prmname", list6.get(0).toString());
		        myTreeHashMap.put("deviceDate", result[1].toString());
		        myTreeHashMap.put("data", result[0].toString());
		        myTreeHashMap.put("analogunit", analogunit);
		        finalMainJSON.put(myTreeHashMap);
      } 
      return finalMainJSON.toString();
    } 
    return finalMainJSON.toString();
  }
  
  @RequestMapping(value = {"/api/GetDeltaMeterRPT25/{id}/{startdate}/{enddate}/{prmname}"}, produces = {"application/json"})
  public String getEnergyMeterRpt26(@PathVariable Long id, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String prmname) throws JsonGenerationException, JsonMappingException, IOException {
    return JsonUtills.ListToGraphJson3(this.hstServide.deltameterrpt2(id, startdate, enddate, prmname));
  }
  
  @RequestMapping(value = {"/api/getalertmessagebymid/{managerid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String getalertmessagebymid(@PathVariable Long managerid) {
    List<Object[]> list = this.hstServide.getalertmessagebymid(managerid);
    JSONArray jarray2 = new JSONArray();
    JSONObject viewalertobj2 = new JSONObject();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        JSONObject viewalertobj = new JSONObject();
        Object[] result = list.get(i);
        viewalertobj.put("alertid", result[0]);
        viewalertobj.put("alerttype", result[1]);
        viewalertobj.put("rulename", result[2]);
        viewalertobj.put("message", result[3]);
        viewalertobj.put("response", result[4]);
        viewalertobj.put("entrytime", result[5]);
        viewalertobj.put("devicename", result[6]);
        viewalertobj.put("sitename", result[7]);
        viewalertobj.put("groupname", result[8]);
        viewalertobj.put("username", result[9]);
        viewalertobj.put("mobileno", result[10]);
        jarray2.put(viewalertobj);
      } 
      viewalertobj2.put("data", jarray2);
      System.out.println("hhi :" + viewalertobj2.toString());
      return viewalertobj2.toString();
    } 
    viewalertobj2.put("data", false);
    return viewalertobj2.toString();
  }
  
  @RequestMapping(value = {"/api/getdeviceassigbyuid3/{user_id}/{key}"}, produces = {"application/json"})
  public String getdeviceassigbyuid3(@PathVariable Long user_id, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    JSONArray arry = new JSONArray();
    
    List<Object[]> list = this.hstServide.assigndeviceprofilebyuid(user_id);
   
    Apikey api = this.apiService.findBykeyValue(key);
  //  log.info("assigndeviceprofilebyuid : "+api.getKeyValue());
    log.info("Apikey : "+list.size());
    if (list.size() != 0) {
   // if (list.size() != 0 && api != null) {
      JSONObject obj1 = new JSONObject();
      JSONArray jarray1 = new JSONArray();
      JSONArray jarray3 = new JSONArray();
      JSONArray jcolumn4 = new JSONArray();
      jcolumn4.put("#");
      jarray1.put(jcolumn4);
      JSONArray jcolumn1 = new JSONArray();
      jcolumn1.put("Site Name");
      jarray1.put(jcolumn1);
      JSONArray jcolumn2 = new JSONArray();
      jcolumn2.put("Device Time");
      jarray1.put(jcolumn2);
      int flag = 0;
      int flag2 = 0;
      int flag3 = 0;
      for (Object[] result : list) {
        List<Object[]> list2 = this.hstServide.getdevicebyprid(Long.valueOf(Long.parseLong(result[1].toString())), user_id);
        int i = 1;
        for (Object[] result1 : list2) {
          List<Object[]> list3 = this.hstServide.getdevicekeyvalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
          log.info("Process Device iD ==> "+result1[0]);
          List<Object[]> list4 = this.hstServide
            .getdevicekeyvaldigitalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
          JSONArray jarray2 = new JSONArray();
          jarray2.put(i);
          i++;
          jarray2.put(result1[1].toString());
          jarray2.put(result1[2].toString());
          for (Object[] result2 : list3) {
            if (flag == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
              JSONArray jcolumn = new JSONArray();
              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[1].toString()),result2[0].toString());
              String analogunit1 ="";
              if(list6.size()!=0)
               analogunit1 = (String) list6.get(0);
              jcolumn.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
              jarray1.put(jcolumn);
            } 
            jarray2.put(
                "<span style='float:right;'>" + result2[1].toString().replaceAll("\"", "") + "</span>");
          } 
          for (Object[] result4 : list4) {
            if (flag2 == 0) {
              JSONArray jcolumnd = new JSONArray();
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result4[0].toString())));
              jcolumnd.put(prmnamelist.getPrmname());
              jarray1.put(jcolumnd);
            } 
            if (result4[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
              jarray2.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
              continue;
            } 
            jarray2.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
          } 
          flag++;
          flag2++;
          jarray2.put("<button type='button' id='lbtn' class='btn  btn-info btn-sm' onclick='devicehistory(" + 
              result1[0].toString() + ",\"" + result1[1].toString() + 
              "\")' ><i class='fa fa-book' aria-hidden='true'></i>&nbsp;History</button> <button type='button' id='btn2' class='btn btn-primary btn-sm' onclick='deviceinfo(" + 
              result1[0].toString() + 
              ")' ><i class='fa fa-briefcase' aria-hidden='true'></i>&nbsp;Device info.</button>");
          jarray3.put(jarray2);
          obj1.put("columns", jarray1);
          obj1.put("data", jarray3);
        } 
        if (flag3 == 0) {
          JSONArray jcolumn3 = new JSONArray();
          jcolumn3.put("Action");
          jarray1.put(jcolumn3);
          flag3++;
        } 
      } 
      return obj1.toString();
    } 
    return "No Data Present";
  }
  
  @RequestMapping(value = {"/api/getdevicehistorybyuid4/{deviceid}/{startdate}/{enddate}/{key}"}, produces = {"application/json"})
  public String getdevicehistorybyuid4(@PathVariable Long deviceid, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      List<Object[]> list = this.hstServide.getdhistorynobydid(deviceid, startdate, enddate);
      JSONObject obj1 = new JSONObject();
      JSONArray jarray1 = new JSONArray();
      JSONArray jarray2 = new JSONArray();
      JSONArray jarray3 = new JSONArray();
      int flag = 0;
      int flag2 = 0;
      int i = 1;
      JSONArray jcolumn4 = new JSONArray();
      jcolumn4.put("#");
      jarray1.put(jcolumn4);
      JSONArray jcolumnname = new JSONArray();
      jcolumnname.put("Device Name");
      jarray1.put(jcolumnname);
      JSONArray jcolumn2 = new JSONArray();
      jcolumn2.put("Device Date");
      jarray1.put(jcolumn2);
      for (Object[] result : list) {
        JSONArray jarray24 = new JSONArray();
        List<Object[]> list1 = this.hstServide.getdhistoryanalogbyno(Long.valueOf(Long.parseLong(result[0].toString())));
        String storedate = null;
        jarray24.put(i);
        jarray24.put(result[3].toString());
        i++;
        if (list1.size() != 0)
          for (Object[] result1 : list1) {
            if (flag == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result1[0].toString())));
              JSONArray jcolumnd = new JSONArray();
              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[2].toString()),result1[0].toString());
              log.info("Parameter ::  "+result1[0]);
              log.info("Profile ::  "+result[2]);
              log.info("Analog ::  "+result[0]);
				String analogunit1 = (String) list6.get(0);
              jcolumnd.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
              jarray1.put(jcolumnd);
            } 
            if (storedate == null) {
              jarray24.put(result1[3].toString());
              storedate = "datestored";
            } 
            jarray24.put(
                "<span style='float:right;'>" + result1[1].toString().replaceAll("\"", "") + "</span>");
          }  
        List<Object[]> list2 = this.hstServide.getdhistorydigitalbyno(Long.valueOf(Long.parseLong(result[0].toString())));
        if (list2.size() != 0)
          for (Object[] result2 : list2) {
            if (flag2 == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
              JSONArray jcolumnd2 = new JSONArray();
              jcolumnd2.put(prmnamelist.getPrmname());
              jarray1.put(jcolumnd2);
            } 
            if (result2[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
              jarray24.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
              continue;
            } 
            jarray24.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
          }  
        flag++;
        flag2++;
        jarray3.put(jarray24);
      } 
      obj1.put("columns", jarray1);
      obj1.put("data", jarray3);
      return obj1.toString();
    } 
    return "Invalid Key";
  }
  
  @RequestMapping(value = {"/api/gethistorydigitaldata/{no}"}, produces = {"application/json"})
  public String gethistorydigitaldata(@PathVariable Long no) throws JsonGenerationException, JsonMappingException, IOException {
    History historydigitaldata = this.hstServide.findOne(no);
    String json = (new ObjectMapper()).writeValueAsString(historydigitaldata.getAnalogdigidata());
    return json;
  }
  
  @RequestMapping(value = {"/api/getlasttrackdigitaldata/{device_id}"}, produces = {"application/json"})
  public String getlasttrackdigitaldata(@PathVariable Long device_id) throws JsonGenerationException, JsonMappingException, IOException {
    Lasttrack lastTrack = this.lasttrackservices.findOne(device_id);
    String json = (new ObjectMapper()).writeValueAsString(lastTrack.getAnalogdigidata());
    return json;
  }
  
  @RequestMapping(value = {"/api/getadminprofile"}, produces = {"application/json"})
  public String getadminprofile() throws JsonGenerationException, JsonMappingException, IOException {
    List<Object[]> list = this.hstServide.adminprofiledevice();
    JSONObject obj = new JSONObject();
    JSONArray jarray = new JSONArray();
    JSONArray jarray3 = new JSONArray();
    JSONArray jcolumn1 = new JSONArray();
    jcolumn1.put("Sr.No");
    jarray.put(jcolumn1);
    JSONArray jcolumn2 = new JSONArray();
    jcolumn2.put("Profile Name");
    jarray.put(jcolumn2);
    JSONArray jcolumn3 = new JSONArray();
    jcolumn3.put("No. Of Devices");
    jarray.put(jcolumn3);
    JSONArray jcolumn4 = new JSONArray();
    jcolumn4.put("Action");
    jarray.put(jcolumn4);
    int i = 1;
    for (Object[] result : list) {
      JSONArray jarray2 = new JSONArray();
      jarray2.put(i);
      i++;
      jarray2.put(result[1].toString());
      jarray2.put(result[2].toString());
      jarray2.put("<button type='button' id='edit-btn' class='btn  btn-success' onclick='viewdevices(" + 
          result[0].toString() + ",\"" + result[1].toString() + 
          "\")' ><i class='fa fa-motorcycle text-white' aria-hidden='true'></i>&nbsp;View Devices</button>");
      jarray3.put(jarray2);
    } 
    obj.put("columns", jarray);
    obj.put("data", jarray3);
    return obj.toString();
  }
  
  @RequestMapping(value = {"/api/getpdevicetrack/{prid}"}, produces = {"application/json"})
  public String getpdevicetrack(@PathVariable Long prid) throws JsonGenerationException, JsonMappingException, IOException {
    List<Object[]> list = this.hstServide.getdevicebyonlyprid(prid);
    if (list.size() != 0) {
      JSONObject obj1 = new JSONObject();
      JSONArray jarray1 = new JSONArray();
      JSONArray jarray3 = new JSONArray();
      JSONArray jcolumn6 = new JSONArray();
      jcolumn6.put("#");
      jarray1.put(jcolumn6);
      JSONArray jcolumn2 = new JSONArray();
      jcolumn2.put("Site Name");
      jarray1.put(jcolumn2);
      JSONArray jcolumn3 = new JSONArray();
      jcolumn3.put("Manager Name");
      jarray1.put(jcolumn3);
      JSONArray jcolumn4 = new JSONArray();
      jcolumn4.put("Device Time");
      jarray1.put(jcolumn4);
      int flag = 0;
      int flag2 = 0;
      int i = 1;
      for (Object[] result : list) {
        JSONArray jarray2 = new JSONArray();
        List<Object[]> list3 = this.hstServide.getdevicekeyvalbydid(Long.valueOf(Long.parseLong(result[0].toString())));
        List<Object[]> list4 = this.hstServide.getdevicekeyvaldigitalbydid(Long.valueOf(Long.parseLong(result[0].toString())));
        jarray2.put(i);
        i++;
        jarray2.put(result[1].toString());
        jarray2.put(result[2].toString());
        jarray2.put(result[3].toString());
        for (Object[] result2 : list3) {
          if (flag == 0) {
            Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
            JSONArray jcolumn = new JSONArray();
            List list6 = hstServide.getprofileanalogunit(prid, result2[0].toString());
			String analogunit1 = (String) list6.get(0);
            jcolumn.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
            jarray1.put(jcolumn);
          } 
          jarray2.put(result2[1].toString().replaceAll("\"", ""));
        } 
        flag++;
        for (Object[] result4 : list4) {
          if (flag2 == 0) {
            JSONArray jcolumnd = new JSONArray();
            Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result4[0].toString())));
            jcolumnd.put(prmnamelist.getPrmname());
            jarray1.put(jcolumnd);
          } 
          if (result4[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
            jarray2.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
            continue;
          } 
          jarray2.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
        } 
        flag2++;
        jarray2.put(
            "<button type='button' id='btn' class='btn  btn-info btn-sm' style='margin-bottom: 5px;' onclick='devicehistory(" + 
            result[0].toString() + ",\"" + result[1].toString() + 
            "\")' ><i class='fa fa-book' aria-hidden='true'></i>&nbsp;History</button><button type='button' id='btn2'  style='margin-bottom: 5px;' class='btn btn-warning btn-sm' onclick='viewdigitaldata(" + 
            result[0].toString() + 
            ")' ><i class='fa fa-file-code-o' aria-hidden='true'></i>&nbsp;Raw Json</button> <button type='button' id='btn2' class='btn btn-danger btn-sm' onclick='viewprofilejson(" + 
            result[0].toString() + 
            ")' ><i class='fa fa-clipboard' aria-hidden='true'></i>&nbsp;Formatted Json</button> <button type='button' id='btn2' class='btn btn-primary btn-sm' onclick='deviceinfo(" + 
            result[0].toString() + 
            ")' ><i class='fa fa-briefcase' aria-hidden='true'></i>&nbsp;Device info.</button>");
        jarray3.put(jarray2);
      } 
      JSONArray jcolumn5 = new JSONArray();
      jcolumn5.put("Actions");
      jarray1.put(jcolumn5);
      obj1.put("columns", jarray1);
      obj1.put("data", jarray3);
      return obj1.toString();
    } 
    return "No Data Present";
  }
  
  @RequestMapping(value = {"/api/getdevicehistorybyuid5/{deviceid}/{startdate}/{enddate}/{key}"}, produces = {"application/json"})
  public String getdevicehistorybyuid5(@PathVariable Long deviceid, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      List<Object[]> list = this.hstServide.getdhistorynobydid(deviceid, startdate, enddate);
      JSONObject obj1 = new JSONObject();
      JSONArray jarray1 = new JSONArray();
      JSONArray jarray2 = new JSONArray();
      JSONArray jarray3 = new JSONArray();
      int flag = 0;
      int flag2 = 0;
      int i = 1;
      JSONArray jcolumn4 = new JSONArray();
      jcolumn4.put("#");
      jarray1.put(jcolumn4);
      JSONArray jcolumn2 = new JSONArray();
      jcolumn2.put("Device Date");
      jarray1.put(jcolumn2);
      for (Object[] result : list) {
        JSONArray jarray24 = new JSONArray();
        List<Object[]> list1 = this.hstServide.getdhistoryanalogbyno(Long.valueOf(Long.parseLong(result[0].toString())));
        String storedate = null;
        jarray24.put(i);
        i++;
        if (list1.size() != 0)
          for (Object[] result1 : list1) {
            if (flag == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result1[0].toString())));
              JSONArray jcolumnd = new JSONArray();
              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[2].toString()),
						result1[0].toString());
            String analogunit1 = (String) list6.get(0);
              jcolumnd.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
              jarray1.put(jcolumnd);
            } 
            if (storedate == null) {
              jarray24.put(result1[3].toString());
              storedate = "datestored";
            } 
            jarray24.put(
                "<span style='float:right'>" + result1[1].toString().replaceAll("\"", "") + "</span>");
          }  
        List<Object[]> list2 = this.hstServide.getdhistorydigitalbyno(Long.valueOf(Long.parseLong(result[0].toString())));
        if (list2.size() != 0)
          for (Object[] result2 : list2) {
            if (flag2 == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
              JSONArray jcolumnd2 = new JSONArray();
              jcolumnd2.put(prmnamelist.getPrmname());
              jarray1.put(jcolumnd2);
            } 
            if (result2[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
              jarray24.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
              continue;
            } 
            jarray24.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
          }  
        flag++;
        flag2++;
        jarray24.put("<button type='button' id='hbtn' class='btn  btn-info btn-sm' onclick='historyjson(" + 
            result[0].toString() + 
            ")' ><i class='fa fa-book' aria-hidden='true'></i>&nbsp;History Json</button>");
        jarray3.put(jarray24);
      } 
      JSONArray jcolumn3 = new JSONArray();
      jcolumn3.put("Action");
      jarray1.put(jcolumn3);
      obj1.put("columns", jarray1);
      obj1.put("data", jarray3);
      return obj1.toString();
    } 
    return "Invalid Key";
  }
  
  @RequestMapping(value = {"/api/getallamanagerlist/{key}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String getallamanagerlist(HttpServletRequest request, @PathVariable String key) {
    Apikey api = this.apiService.findBykeyValue(key);
    if (api != null) {
      List<Object[]> list = new ArrayList();
      list = this.userService.getAllmangerlist();
      JSONArray jarray = new JSONArray();
      for (Object[] result1 : list) {
        JSONObject jo = new JSONObject();
        jo.put("id", result1[0].toString());
        jo.put("username", result1[1].toString());
        jo.put("password", result1[2].toString());
        if (result1[3].toString().equalsIgnoreCase(null)) {
          jo.put("email", "Not Known");
        } else {
          jo.put("email", result1[3].toString());
        } 
        if (result1[4].toString().equalsIgnoreCase(null)) {
          jo.put("contact", "Not Known");
        } else {
          jo.put("contact", result1[3].toString());
        } 
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    return (new SpringException(false, "Invalid Key")).toString();
  }
  
  @RequestMapping(value = {"api/dmanagerlogin"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public String savedigitalinputalert(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
    System.out.println("da:" + password);
    System.out.println("das: " + username);
    List<Object[]> list = this.userService.getuserverify(username, password);
    JSONObject jo = new JSONObject();
    JSONArray jarray = new JSONArray();
    if (list.size() != 0) {
      HttpSession session = request.getSession();
      Apikey api = new Apikey();
      UUID key = UUID.randomUUID();
      for (Object[] result : list) {
        api = this.apiService.findByuid(Long.valueOf(Long.parseLong(result[0].toString())));
        if (api == null) {
          api = new Apikey();
          api.setCreateDate(new Date());
          api.setKeyValue(key.toString());
          session.setAttribute("LoginKEY", key);
          api.setUid(Long.parseLong(result[0].toString()));
          this.apiService.saveObject(api);
          jo.put("api", api.getKeyValue());
        } else {
          api.setCreateDate(new Date());
          api.setKeyValue(key.toString());
          session.setAttribute("LoginKEY", key);
          this.apiService.saveObject(api);
          jo.put("api", api.getKeyValue());
        } 
        jo.put("responseCode", "SUCCESS");
        jo.put("uId", Long.parseLong(result[0].toString()));
        jo.put("role", result[2].toString());
        jo.put("username", result[1].toString());
        jarray.put(jo);
      } 
      return jarray.toString();
    } 
    jo.put("responseCode", "FAIL");
    jarray.put(jo);
    return jarray.toString();
  }
  
  @RequestMapping(value = {"/api/getdeviceassigbymanagerid3/{manager_id}/{key}"}, produces = {"application/json"})
  public String getdeviceassigbymanagerid3(@PathVariable Long manager_id, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
    Apikey api = this.apiService.findBykeyValue(key);
    List<Object[]> list = this.hstServide.assigndeviceprofilebymanagerid(manager_id);
    if (list.size() != 0 && api != null) {
      JSONObject obj1 = new JSONObject();
      JSONArray jarray1 = new JSONArray();
      JSONArray jarray3 = new JSONArray();
      JSONArray jcolumn4 = new JSONArray();
      jcolumn4.put("#");
      jarray1.put(jcolumn4);
      JSONArray jcolumn1 = new JSONArray();
      jcolumn1.put("Site Name");
      jarray1.put(jcolumn1);
      JSONArray jcolumn2 = new JSONArray();
      jcolumn2.put("Device Time");
      jarray1.put(jcolumn2);
      for (Object[] result : list) {
        List<Object[]> list2 = this.hstServide.getdevicebyprmid(Long.valueOf(Long.parseLong(result[1].toString())), manager_id);
        int flag = 0;
        int flag2 = 0;
        int i = 1;
        for (Object[] result1 : list2) {
          List<Object[]> list3 = this.hstServide.getdevicekeyvalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
          List<Object[]> list4 = this.hstServide
            .getdevicekeyvaldigitalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
          JSONArray jarray2 = new JSONArray();
          jarray2.put(i);
          i++;
          jarray2.put(result1[1].toString());
          jarray2.put(result1[2].toString());
          for (Object[] result2 : list3) {
            if (flag == 0) {
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
              JSONArray jcolumn = new JSONArray();
              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[1].toString()),
						result2[0].toString());
				String analogunit1 = (String) list6.get(0);
              jcolumn.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
              jarray1.put(jcolumn);
            } 
            jarray2.put(
                "<span style='float:right'>" + result2[1].toString().replaceAll("\"", "") + "</span>");
          } 
          for (Object[] result4 : list4) {
            if (flag2 == 0) {
              JSONArray jcolumnd = new JSONArray();
              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result4[0].toString())));
              jcolumnd.put(prmnamelist.getPrmname());
              jarray1.put(jcolumnd);
            } 
            if (result4[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
              jarray2.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
              continue;
            } 
            jarray2.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
          } 
          flag++;
          flag2++;
          jarray2.put("<button type='button' id='lbtn' class='btn  btn-info btn-sm' onclick='devicehistory(" + 
              result1[0].toString() + ",\"" + result1[1].toString() + 
              "\")' ><i class='fa fa-book' aria-hidden='true'></i>&nbsp;History</button> <button type='button' id='btn2' class='btn btn-primary btn-sm' onclick='deviceinfo(" + 
              result1[0].toString() + 
              ")' ><i class='fa fa-briefcase' aria-hidden='true'></i>&nbsp;Device info.</button>");
          jarray3.put(jarray2);
          obj1.put("columns", jarray1);
          obj1.put("data", jarray3);
        } 
        JSONArray jcolumn3 = new JSONArray();
        jcolumn3.put("Action");
        jarray1.put(jcolumn3);
      } 
      return obj1.toString();
    } 
    return "No Data Present";
  }
  
  @RequestMapping(value = {"/api/displayanaloginputalert/{managerid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String displayanaloginputalert(@PathVariable Long managerid) {
    List<Object[]> list = this.hstServide.displayanaloginputalert(managerid);
    JSONArray jarray2 = new JSONArray();
    JSONObject digitalobj2 = new JSONObject();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        JSONObject dalertobj = new JSONObject();
        Object[] result = list.get(i);
        dalertobj.put("no", result[0]);
        dalertobj.put("alertlimit", result[1]);
        dalertobj.put("devicename", result[2]);
        dalertobj.put("analoginput", result[3]);
        dalertobj.put("emai_id", result[4]);
        dalertobj.put("emailtemplatename", result[5]);
        dalertobj.put("managerid", result[6]);
        dalertobj.put("messagetemplatename", result[7]);
        dalertobj.put("mobileno", result[8]);
        dalertobj.put("notification", result[9]);
        dalertobj.put("sitename", result[10]);
        dalertobj.put("conditionstring", result[11]);
        dalertobj.put("conditionvalue", result[12]);
        dalertobj.put("timedifference", result[13]);
        dalertobj.put("avgtime", result[14]);
        dalertobj.put("timing", result[15]);
        dalertobj.put("username", result[16]);
        dalertobj.put("usergroupname", result[17]);
        dalertobj.put("analoginputname", result[18]);
        jarray2.put(dalertobj);
      } 
      digitalobj2.put("data", jarray2);
      return digitalobj2.toString();
    } 
    digitalobj2.put("data", false);
    return digitalobj2.toString();
  }
  
  @RequestMapping(value = {"api/saveanaloginputalert"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public String saveanaloginputalert(@RequestParam Long Uid, @RequestParam Long Staffgroup, @RequestParam String Staffselect, @RequestParam Long Stafflist, @RequestParam String Deviceselect, @RequestParam Long Devicelist, @RequestParam Long Site, @RequestParam String Analoginputs, @RequestParam String Mobilenumber, @RequestParam String Emailaddress, @RequestParam Long Smstemplate, @RequestParam Long Emailtemplate, @RequestParam Long Alertlimit, @RequestParam String Condition, @RequestParam Long Conditionvalue, @RequestParam Long Timedifference, @RequestParam Long Avgtime, @RequestParam String Starttime, @RequestParam String Endtime, @RequestParam String Notification) {
    AnalogInputAlert ana = new AnalogInputAlert();
    ana.setUser_id(Stafflist);
    ana.setUsergroup_id(Staffgroup);
    ana.setDeviceid(Devicelist);
    ana.setSite_id(Site);
    ana.setAnaloginput(Analoginputs);
    ana.setMobileno(Mobilenumber);
    ana.setEmail_id(Emailaddress);
    ana.setAlertlimit(Alertlimit);
    ana.setConditionstring(Condition);
    ana.setConditionvalue(Conditionvalue);
    ana.setTimedifference(Timedifference);
    ana.setAvgtime(Avgtime);
    ana.setMessagetemplate_id(Smstemplate);
    ana.setEmailtemplate_id(Emailtemplate);
    ana.setTiming(String.valueOf(Starttime) + "#" + Endtime);
    ana.setNotification(Notification);
    ana.setManagerid(Uid);
    this.hstServide.saveanaloginputalert(ana);
    return "Susessfully Saved";
  }
  
  @RequestMapping(value = {"/api/editanaloginputalert/{no}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public List<AnalogInputAlert> editanaloginputalert(@PathVariable Long no) {
    return this.hstServide.editanaloginputalert(no);
  }
  
  @RequestMapping(value = {"api/updateanaloginputalert"}, produces = {"application/json"}, method = {RequestMethod.POST})
  public String updateanaloginputalert(@RequestParam Long No, @RequestParam Long Uid, @RequestParam Long Staffgroup, @RequestParam String Staffselect, @RequestParam Long Stafflist, @RequestParam String Deviceselect, @RequestParam Long Devicelist, @RequestParam Long Site, @RequestParam String Analoginputs, @RequestParam String Mobilenumber, @RequestParam String Emailaddress, @RequestParam Long Smstemplate, @RequestParam Long Emailtemplate, @RequestParam Long Alertlimit, @RequestParam String Condition, @RequestParam Long Conditionvalue, @RequestParam Long Timedifference, @RequestParam Long Avgtime, @RequestParam String Starttime, @RequestParam String Endtime, @RequestParam String Notification) {
    AnalogInputAlert ana = new AnalogInputAlert();
    ana.setNo(No);
    ana.setUser_id(Stafflist);
    ana.setUsergroup_id(Staffgroup);
    ana.setDeviceid(Devicelist);
    ana.setSite_id(Site);
    ana.setAnaloginput(Analoginputs);
    ana.setMobileno(Mobilenumber);
    ana.setEmail_id(Emailaddress);
    ana.setAlertlimit(Alertlimit);
    ana.setConditionstring(Condition);
    ana.setConditionvalue(Conditionvalue);
    ana.setTimedifference(Timedifference);
    ana.setAvgtime(Avgtime);
    ana.setMessagetemplate_id(Smstemplate);
    ana.setEmailtemplate_id(Emailtemplate);
    String newtiming = String.valueOf(Starttime) + "#" + Endtime;
    ana.setTiming(newtiming);
    ana.setNotification(Notification);
    ana.setManagerid(Uid);
    this.hstServide.updateanaloginputalert(ana);
    return "SuccessFully Updated";
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/api/deleteanaloginputalert/{no}"})
  public String deleteanaloginputalert(@PathVariable long no) {
    return this.hstServide.deleteanaloginputalert(Long.valueOf(no));
  }
  
  @RequestMapping(value = {"/api/GetDeltaMeterRPT26/{id}/{startdate}/{enddate}/{prmname}"}, produces = {"application/json"})
  public String getEnergyMeterRpt27(@PathVariable Long id, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String prmname) throws JsonGenerationException, JsonMappingException, IOException {
    return JsonUtills.ListToGraphJson3(this.hstServide.deltameterrpt3(id, startdate, enddate, prmname));
  }
  
  @RequestMapping(value = {"/api/GetDeltaMeter/{id}/{startdate}/{enddate}/{prmname}"}, produces = {"application/json"})
  public String GetDeltaMeter(@PathVariable Long id, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String prmname) throws JsonGenerationException, JsonMappingException, IOException {
    Devicemaster devicemaster = this.devicemasterservices.get(id);
    List<Object[]> list = this.hstServide.deltameterrpt(id, startdate, enddate, prmname);
    List<Object[]> list5 = this.hstServide.getprofileanalogunit(devicemaster.getDp().getPrid(), prmname);
    List<Object[]> list6 = this.hstServide.getperametername(Long.valueOf(Long.parseLong(prmname)));
    Object[] analogunit = list5.get(0);
    JSONArray finalMainJSON = new JSONArray();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        LinkedHashMap<String, String> myTreeHashMap = new LinkedHashMap<>();
        Object[] result = list.get(i);
        myTreeHashMap.put("sr", i + "");
        myTreeHashMap.put("Prmname", ""+list6.get(0));
        myTreeHashMap.put("deviceDate", result[2].toString());
        myTreeHashMap.put("data", result[1].toString());
        myTreeHashMap.put("analogunit", ""+analogunit);
        finalMainJSON.put(myTreeHashMap);
      } 
      return finalMainJSON.toString();
    } 
    return finalMainJSON.toString();
  }
  
  @RequestMapping(value = {"/api/GetCandleIntervalRPT/{id}/{startdate}/{enddate}/{prmname}/{minutes}"}, produces = {"application/json"})
  public String GetCandleIntervalRPT(@PathVariable Long id, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String prmname, @PathVariable int minutes) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
    ObjectMapper Obj = new ObjectMapper();
    ArrayList<Date> dates = MovingAvg.gerDateRange(String.valueOf(startdate) + " 00:00:00", String.valueOf(enddate) + " 23:59:59", minutes);
    ArrayList<Candle> data = new ArrayList<>();
    for (int s = 0; s < dates.size(); s++) {
      if (s != dates.size() - 1) {
        List<?> list = this.hstServide.deltameterrpt(id, sdf.format(dates.get(s)), sdf.format(dates.get(s + 1)), prmname);
        List<Double> analogList = new ArrayList<>();
        Collections.reverse(list);
        if (list.size() != 0) {
          for (int i = 0; i < list.size(); i++) {
            Object[] result = (Object[])list.get(i);
            analogList.add(Double.valueOf(Double.parseDouble(result[0].toString())));
          } 
          for (Double model : analogList)
            System.out.println(model); 
          System.out.println("===================");
          MovingAvg mAvg = new MovingAvg();
          Candle cndl = mAvg.movingavrage(analogList);
          cndl.setDate(sdf.format(dates.get(s)));
          data.add(cndl);
        } 
      } 
    } 
    return Obj.writeValueAsString(data);
  }
  
  @RequestMapping(value = {"/api/getalertmessagebyUserId/{userId}/{alertType}/{deviceId}/{startDate}/{endDate}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String getalertmessagebyUserId(@PathVariable long userId, @PathVariable String alertType, @PathVariable String deviceId, @PathVariable String startDate, @PathVariable String endDate) {
    Iterable<Long> devideList = (Iterable<Long>)Arrays.<String>asList(deviceId.split(",")).stream().map(s -> Long.valueOf(Long.parseLong(s.trim())))
      .collect(Collectors.toList());
    List<Object[]> list = this.hstServide.getalertmessagebyUserId(userId, alertType, devideList, startDate, endDate);
    JSONArray jarray2 = new JSONArray();
    JSONObject viewalertobj2 = new JSONObject();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        JSONObject viewalertobj = new JSONObject();
        Object[] result = list.get(i);
        viewalertobj.put("alertid", result[0]);
        viewalertobj.put("alerttype", result[1]);
        viewalertobj.put("message", result[2]);
        viewalertobj.put("entrytime", result[3]);
        viewalertobj.put("devicename", result[4]);
        viewalertobj.put("sitename", result[5]);
        viewalertobj.put("mobileno", result[6]);
        jarray2.put(viewalertobj);
      } 
      viewalertobj2.put("data", jarray2);
    } else {
      viewalertobj2.put("data", jarray2);
    } 
    return viewalertobj2.toString();
  }
  
  @RequestMapping(value = {"/api/displaydigitalinputalertUserIdBy/{userId}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String displaydigitalinputalertUserIdBy(@PathVariable Long userId) {
	  JSONObject digitalobj2 = new JSONObject();
	  try
	  {
    List<Object[]> list = this.hstServide.displaydigitalinputalertByUserId(userId);
    JSONArray jarray2 = new JSONArray();
    
    log.info("displaydigitalinputalertUserIdBy : "+list.size());
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        JSONObject dalertobj = new JSONObject();
  	  log.info("displaydigitalinputalertUserIdBy : "+i);

        Object[] result = list.get(i);
  	  log.info("displaydigitalinputalertUserIdBy : "+i+ " : "+ this.parameterservices.get(Long.valueOf(Long.parseLong(result[3].toString()))).getPrmname());

        dalertobj.put("no", result[0]);
        dalertobj.put("alertlimit", result[1]);
        dalertobj.put("devicename", result[2]);
        dalertobj.put("digitalinput", this.parameterservices.get(Long.valueOf(Long.parseLong(result[3].toString()))).getPrmname());
        dalertobj.put("emai_id", result[4]);
        dalertobj.put("emailtemplatename", result[5]);
        dalertobj.put("managerid", result[6]);
        dalertobj.put("messagetemplatename", result[7]);
        dalertobj.put("mobileno", result[8]);
        dalertobj.put("notification", result[9]);
        dalertobj.put("sitename", result[10]);
        dalertobj.put("status", result[11]);
        dalertobj.put("timing", result[12]);
        dalertobj.put("username", result[13]);
        dalertobj.put("usergroupname", result[14]);
        jarray2.put(dalertobj);
      } 
      digitalobj2.put("data", jarray2);
    } else {
      digitalobj2.put("data", jarray2);
    } 
   
	  }catch(Exception e)
	  {
		  log.info("displaydigitalinputalertUserIdBy : "+ e.getMessage());
		  log.info("displaydigitalinputalertUserIdBy : "+ e.getLocalizedMessage());
	  }
	  return digitalobj2.toString();
  }
  
  @RequestMapping(value = {"/api/UpdatedigitalinputalertMobileNo/{mobileno}/{alertId}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String UpdatedigitalinputalertMobileNo(@PathVariable String mobileno, @PathVariable Long alertId) {
    this.hstServide.updateNumber(mobileno, alertId);
    JSONObject digitalobj2 = new JSONObject();
    digitalobj2.put("Message", "Update Successfully.");
    return digitalobj2.toString();
  }
  
  @RequestMapping(value = {"/api/displayanaloginputalertByUserId/{userId}"}, produces = {"application/json"}, method = {RequestMethod.GET})
  public String displayanaloginputalertByUserId(@PathVariable Long userId) {
    List<Object[]> list = this.hstServide.displayanaloginputalertByUserId(userId);
    JSONArray jarray2 = new JSONArray();
    JSONObject digitalobj2 = new JSONObject();
    if (list.size() != 0) {
      for (int i = 0; i < list.size(); i++) {
        JSONObject dalertobj = new JSONObject();
        Object[] result = list.get(i);
        dalertobj.put("no", result[0]);
        dalertobj.put("alertlimit", result[1]);
        dalertobj.put("devicename", result[2]);
        dalertobj.put("analoginput", result[3]);
        dalertobj.put("emai_id", result[4]);
        dalertobj.put("emailtemplatename", result[5]);
        dalertobj.put("managerid", result[6]);
        dalertobj.put("messagetemplatename", result[7]);
        dalertobj.put("mobileno", result[8]);
        dalertobj.put("notification", result[9]);
        dalertobj.put("sitename", result[10]);
        dalertobj.put("conditionstring", result[11]);
        dalertobj.put("conditionvalue", result[12]);
        dalertobj.put("timedifference", result[13]);
        dalertobj.put("avgtime", result[14]);
        dalertobj.put("timing", result[15]);
        dalertobj.put("username", result[16]);
        dalertobj.put("usergroupname", result[17]);
        dalertobj.put("analoginputname", result[18]);
        jarray2.put(dalertobj);
      } 
      digitalobj2.put("data", jarray2);
    } else {
      digitalobj2.put("data", jarray2);
    } 
    return digitalobj2.toString();
  }
  
  @RequestMapping(value = {"/api/UpdateAnaloglinputalertMobileNo/{mobileno}/{alertId}/{emailid}/{volatege}/{time}"}, method = {RequestMethod.POST})
  public void UpdateAnaloglinputalertMobileNo(@PathVariable String mobileno, @PathVariable Long alertId, @PathVariable String emailid, @PathVariable Long volatege, @PathVariable Long time) {
    this.hstServide.updateAnalogAlertNumber(mobileno, alertId, volatege, emailid, time);
  }
}

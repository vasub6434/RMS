package com.bonrix.dggenraterset.Controller;

import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.Site;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.codehaus.jackson.node.ObjectNode;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.codehaus.jackson.node.ArrayNode;

import com.bonrix.dggenraterset.DTO.EngDTO;
import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import org.codehaus.jackson.JsonNode;
import java.text.ParseException;
import org.json.JSONException;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;

import org.codehaus.jackson.map.JsonMappingException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import java.util.Iterator;
import org.json.JSONArray;
import java.util.List;

import com.bonrix.dggenraterset.Utility.GmailEmailSender;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.bonrix.dggenraterset.jobs.RMSJob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;

import org.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import com.bonrix.dggenraterset.Model.History;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.bonrix.dggenraterset.Model.SpringException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;

import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.AnalogDataServices;
import com.bonrix.dggenraterset.Service.HistoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import com.bonrix.dggenraterset.Service.ReportService;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = { "*" })
@Transactional
@RestController
public class ReportController
{
    @Autowired
    ReportService rptService;
    @Autowired
    HistoryServices hstservices;
    @Autowired
    ParameterServices paramService;
    @Autowired
    ParameterRepository prepo;
    @Autowired
    DevicemasterRepository deviceReop;
    @Autowired
    DeviceProfileRepository profileRepo;
	@Autowired
	AnalogDataServices AnalogDataservice;
    
    final SimpleDateFormat df;
    final SimpleDateFormat df1;
    final SimpleDateFormat df2;
    DateFormat df5;
    private static final Logger log = Logger.getLogger(ReportController.class);
    public ReportController() {
        this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.df1 = new SimpleDateFormat("yyyy-MM-dd");
        this.df2 = new SimpleDateFormat("EEE, d MMM yyyy");
        this.df5 = new SimpleDateFormat("E, MMM dd yyyy");
    }   
           
    @PostMapping
    @RequestMapping(method = { RequestMethod.POST }, value = { "/api/sajandata/{key}" })
    @ExceptionHandler({ SpringException.class })
    public String sajandata() {
        return new SpringException(true, "Parameter Sucessfully Added").toString();
    }
    
    @RequestMapping(value = { "/api/GetDeviceRpt/{dId}" }, produces = { "application/json" })
    public String GetDeviceMyDRpt(@PathVariable final Long dId) throws JsonGenerationException, JsonMappingException, IOException {
        List<History> history = null;
        final JSONArray aar = null;
        history = (List<History>)this.rptService.findBydeviceId(dId);
        for (final History hst : history) {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonFromMap = mapper.writeValueAsString((Object)hst.getAnalogdigidata());
            final JSONObject jsonObj = new JSONObject(jsonFromMap);
            final JSONArray arry = jsonObj.getJSONArray("Analog");
            final JSONObject jobject = new JSONObject((Object)arry.getJSONObject(0));
            final JSONObject jobject2 = new JSONObject(arry.get(0).toString());
            System.out.println(jobject2);
        }
        return JsonUtills.ListToJava((List)history);
    }
    
    @RequestMapping(value = { "/api/mydata/{startDate}/{endDate}/{paramId}" }, produces = { "application/json" })
    public String datalist(@PathVariable final String startDate, @PathVariable final String endDate, @PathVariable final String paramId) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
        final List<Object[]> lst = (List<Object[]>)this.hstservices.getdata(startDate, endDate, paramId,0L);
        final JSONArray mainJSON = new JSONArray();
        if (lst != null) {
            String lstatus = null;
            int ct = 0;
            for (int i = 0; i < lst.size(); ++i) {
                final Object[] obj = lst.get(i);
                if (i == 0) {
                    lstatus = obj[3].toString();
                    final JSONObject jo = new JSONObject();
                    jo.put("Start Date", (Object)obj[1].toString());
                    jo.put("Start Status", (Object)obj[3].toString());
                    mainJSON.put(ct, (Object)jo);
                    ++ct;
                }
                else {
                    if (lstatus.equalsIgnoreCase(obj[3].toString())) {
                        System.out.println("SAME");
                    }
                    else {
                        final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
                        innerJSON12.put("End Date", (Object)obj[1].toString());
                        mainJSON.put(ct - 1, (Object)innerJSON12);
                        final JSONObject jo2 = new JSONObject();
                        jo2.put("Start Date", (Object)obj[1].toString());
                        jo2.put("Start Status", (Object)obj[3].toString());
                        mainJSON.put(ct, (Object)jo2);
                        ++ct;
                    }
                    lstatus = obj[3].toString();
                }
            }
        }
        for (int j = 0; j < mainJSON.length(); ++j) {
            final JSONObject jsonObject1 = mainJSON.getJSONObject(j);
            if (j != mainJSON.length() - 1) {
                final Date JSONStartDate = this.df.parse(jsonObject1.getString("Start Date"));
                final Date JSONendDate = this.df.parse(jsonObject1.getString("End Date"));
                JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                    jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                    jsonObject1.put("OFF", (Object)"");
                }
                else {
                    jsonObject1.put("ON", (Object)"");
                    jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                }
            }
            else {
                final JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
                final Date JSONStartDate2 = this.df.parse(jsonObjectLast.getString("Start Date"));
                final Date date21 = this.df1.parse(endDate);
                final Date date22 = this.df1.parse(this.df1.format(new Date()));
                final int date23 = date21.compareTo(date22);
                if (date23 == 0) {
                    jsonObjectLast.put("End Date", (Object)this.df.format(new Date()));
                    if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                        jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                        jsonObject1.put("OFF", (Object)"");
                    }
                    else {
                        jsonObject1.put("ON", (Object)"");
                        jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                    }
                }
                else if (date23 == -1) {
                    jsonObjectLast.put("End Date", (Object)this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")));
                    if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                        jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                        jsonObject1.put("OFF", (Object)"");
                    }
                    else {
                        jsonObject1.put("ON", (Object)"");
                        jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                    }
                }
            }
        }
        return mainJSON.toString();
    }
    
    @RequestMapping(value = { "/api/GetDigitalHistoryData/{deviceId}/{startdate}/{enddate}" }, produces = { "application/json" }, method = { RequestMethod.GET })
    public String MyGetDigitalHistoryData(@PathVariable final Long deviceId, @PathVariable final String startdate, @PathVariable final String enddate) throws JsonGenerationException, JsonMappingException, IOException {
        final Devicemaster device = this.deviceReop.findBydeviceid(deviceId);
        final ObjectMapper mapper = new ObjectMapper();
        final ArrayNode analogNodearrayNode = mapper.createArrayNode();
        final List<Object[]> lst = (List<Object[]>)this.hstservices.getDigitalHistory(deviceId, startdate, enddate);
        if (lst != null) {
            for (int i = 0; i < lst.size(); ++i) {
                final ObjectNode DIGITALNode = mapper.createObjectNode();
                final Object[] obj = lst.get(i);
                DIGITALNode.putPOJO("#", (Object)(i + 1));
                DIGITALNode.putPOJO("Site Name", (Object)device.getDevicename());
                DIGITALNode.putPOJO("Device Date", (Object)obj[1].toString());
                final JSONArray jsonarray = new JSONArray(obj[3].toString());
                for (int j = 0; j < jsonarray.length(); ++j) {
                    final JSONObject jsonobject = jsonarray.getJSONObject(j);
                    final String k = jsonobject.keys().next();
                    System.out.println("Info :: Key: " + this.prepo.findByid((long)new Long(k)).getPrmname() + ", value: " + jsonobject.getString(k));
                    DIGITALNode.putPOJO(this.prepo.findByid((long)new Long(k)).getPrmname(), (Object)jsonobject.getString(k));
                    if (jsonobject.getString(k).toString().equalsIgnoreCase("1")) {
                        DIGITALNode.putPOJO(this.prepo.findByid((long)new Long(k)).getPrmname(), (Object)"<button type='button' class='btn btn-success btn-xs'>ON</button>");
                    }
                    else {
                        DIGITALNode.putPOJO(this.prepo.findByid((long)new Long(k)).getPrmname(), (Object)"<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
                    }
                }
                analogNodearrayNode.add((JsonNode)DIGITALNode);
            }
        }
        else {
            final ObjectNode DIGITALNode2 = mapper.createObjectNode();
            analogNodearrayNode.add((JsonNode)DIGITALNode2);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString((Object)analogNodearrayNode);
    }
    
    @RequestMapping(value = { "/api/myRPTdata/{startDate}/{endDate}/{paramId}" }, produces = { "application/json" })
    public String myRPTdata(@PathVariable final String startDate, @PathVariable final String endDate, @PathVariable final String paramId) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
        final JSONArray GmainJSON = new JSONArray();
        List<LocalDate> totalDates = new ArrayList<LocalDate>();
        totalDates = (List<LocalDate>)JsonUtills.getBetweenDate(startDate, endDate);
        for (final LocalDate nxtDate : totalDates) {
            System.out.println(nxtDate);
            final List<Object[]> lst = (List<Object[]>)this.hstservices.getdata(nxtDate.toString(), nxtDate.toString(), paramId,0L);
            final JSONArray mainJSON = new JSONArray();
            if (lst != null) {
                String lstatus = null;
                int ct = 0;
                for (int i = 0; i < lst.size(); ++i) {
                    final Object[] obj = lst.get(i);
                    if (i == 0) {
                        lstatus = obj[3].toString();
                        final JSONObject jo = new JSONObject();
                        jo.put("Start Date", (Object)obj[1].toString());
                        jo.put("Start Status", obj[3]);
                        mainJSON.put(ct, (Object)jo);
                        ++ct;
                    }
                    else {
                        if (lstatus.equalsIgnoreCase(obj[3].toString())) {
                            System.out.println();
                        }
                        else {
                            final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
                            final Date myDateOne = this.df.parse(innerJSON12.getString("Start Date"));
                            final Date myDateTwo = this.df.parse(obj[1].toString());
                            final DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
                            final int retVal = dateTimeComparator.compare((Object)myDateOne, (Object)myDateTwo);
                            final JSONObject jo2 = new JSONObject();
                            boolean flage = false;
                            if (retVal == 0) {
                                if (!innerJSON12.has("End Date")) {
                                    innerJSON12.put("End Date", obj[1]);
                                }
                                mainJSON.put(ct - 1, (Object)innerJSON12);
                                jo2.put("Start Date", obj[1]);
                            }
                            else {
                                innerJSON12.put("End Date", (Object)(String.valueOf(this.df1.format(this.df1.parse(innerJSON12.getString("Start Date").toString()))) + " 23:59:59"));
                                mainJSON.put(ct - 1, (Object)innerJSON12);
                                final JSONObject jo3 = new JSONObject();
                                jo2.put("Start Date", (Object)(String.valueOf(JsonUtills.getNextDate(this.df1.format(this.df1.parse(innerJSON12.getString("Start Date").toString())))) + " 00:00:00"));
                                jo2.put("End Date", obj[1]);
                                flage = true;
                            }
                            jo2.put("Start Status", obj[3]);
                            mainJSON.put(ct, (Object)jo2);
                            ++ct;
                        }
                        lstatus = obj[3].toString();
                    }
                }
            }
            for (int j = 0; j < mainJSON.length(); ++j) {
                final JSONObject jsonObject1 = mainJSON.getJSONObject(j);
                if (j != mainJSON.length() - 1) {
                    final Date JSONStartDate = this.df.parse(jsonObject1.getString("Start Date"));
                    final Date JSONendDate = this.df.parse(jsonObject1.getString("End Date"));
                    JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
                    if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                        jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                        jsonObject1.put("OFF", (Object)"");
                    }
                    else {
                        jsonObject1.put("ON", (Object)"");
                        jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                    }
                }
                else {
                    final JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
                    final Date JSONStartDate2 = this.df.parse(jsonObjectLast.getString("Start Date"));
                    final Date date21 = this.df1.parse(endDate);
                    final Date date22 = this.df1.parse(this.df1.format(new Date()));
                    final int date23 = date21.compareTo(date22);
                    if (date23 == 0) {
                        jsonObjectLast.put("End Date", (Object)this.df.format(new Date()));
                        if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                            jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                            jsonObject1.put("OFF", (Object)"");
                        }
                        else {
                            jsonObject1.put("ON", (Object)"");
                            jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                        }
                    }
                    else if (date23 == -1) {
                        jsonObjectLast.put("End Date", (Object)this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")));
                        if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
                            jsonObject1.put("ON", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                            jsonObject1.put("OFF", (Object)"");
                        }
                        else {
                            jsonObject1.put("ON", (Object)"");
                            jsonObject1.put("OFF", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                        }
                    }
                }
            }
            for (int j = 0; j < mainJSON.length(); ++j) {
                final JSONArray ImainJSON = new JSONArray();
                final JSONObject jsonObject2 = mainJSON.getJSONObject(j);
                ImainJSON.put((Object)this.df5.format(this.df.parse(jsonObject2.getString("Start Date"))));
                ImainJSON.put((Object)jsonObject2.getString("Start Date"));
                ImainJSON.put((Object)jsonObject2.getString("End Date"));
                if (jsonObject2.getString("Start Status").toString().equalsIgnoreCase("1")) {
                    ImainJSON.put((Object)"ON");
                }
                else {
                    ImainJSON.put((Object)"OFF");
                }
                GmainJSON.put((Object)ImainJSON);
            }
        }
        return GmainJSON.toString();
    }
    
    @RequestMapping(value = { "/api/GetNewDeviceGraph/{dId}" }, produces = { "application/json" })
    public String GetNewDeviceGraph(@PathVariable final Long dId) throws JsonGenerationException, JsonMappingException, IOException {
        List<History> history = null;
        final JSONArray aar = null;
        history = (List<History>)this.rptService.findBydeviceId(dId);
        for (final History hst : history) {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonFromMap = mapper.writeValueAsString((Object)hst.getAnalogdigidata());
            final JSONObject jsonObj = new JSONObject(jsonFromMap);
            final JSONArray arry = jsonObj.getJSONArray("Digital");
            final JSONObject jobject = new JSONObject((Object)arry.getJSONObject(0));
            final JSONObject jobject2 = new JSONObject(arry.get(0).toString());
            System.out.println(jobject2);
        }
        return JsonUtills.ListToJava((List)history);
    }
    
    @RequestMapping(value = { "/api/GetNewDeviceGraph444/{dId}" }, produces = { "application/json" })
    public String GetNewDeviceGraph444(@PathVariable final Long dId) throws JsonGenerationException, JsonMappingException, IOException {
        List<History> history = null;
        final JSONArray aar = null;
        history = (List<History>)this.rptService.findBydeviceId(dId);
        for (final History hst : history) {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonFromMap = mapper.writeValueAsString((Object)hst.getAnalogdigidata());
            final JSONObject jsonObj = new JSONObject(jsonFromMap);
            final JSONArray arry = jsonObj.getJSONArray("Digital");
            final JSONObject jobject = new JSONObject((Object)arry.getJSONObject(0));
            final JSONObject jobject2 = new JSONObject(arry.get(0).toString());
            System.out.println(jobject2);
        }
        return JsonUtills.ListToJava((List)history);
    }
    
    @RequestMapping(value = { "/api/GetNewDeviceGraph555/{dId}" }, produces = { "application/json" })
    public String GetNewDeviceGraph555(@PathVariable final Long dId) throws JsonGenerationException, JsonMappingException, IOException {
        List<History> history = null;
        final JSONArray aar = null;
        history = (List<History>)this.rptService.findBydeviceId(dId);
        for (final History hst : history) {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonFromMap = mapper.writeValueAsString((Object)hst.getAnalogdigidata());
            final JSONObject jsonObj = new JSONObject(jsonFromMap);
            final JSONArray arry = jsonObj.getJSONArray("Digital");
            final JSONObject jobject = new JSONObject((Object)arry.getJSONObject(0));
            final JSONObject jobject2 = new JSONObject(arry.get(0).toString());
            System.out.println(jobject2);
        }
        return JsonUtills.ListToJava((List)history);
    }
    
    @RequestMapping(value = { "/api/mydata1/{startDate}/{endDate}/{paramId}" }, produces = { "application/json" })
    public String datalist1(@PathVariable final String startDate, @PathVariable final String endDate, @PathVariable final String paramId) throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
        final JSONArray mainJSON = new JSONArray();
        final List<Object[]> lst = (List<Object[]>)this.hstservices.getdata(startDate, endDate, paramId,0L);
        final Parameter param = this.paramService.get(new Long(paramId));
        System.out.println("LST :: " + lst.size());
        if (lst != null) {
            String lstatus = null;
            int ct = 0;
            for (int i = 0; i < lst.size(); ++i) {
                final Object[] obj = lst.get(i);
                if (i == 0) {
                    lstatus = obj[3].toString();
                    final JSONObject jo = new JSONObject();
                    jo.put("Start Date", (Object)obj[1].toString());
                    if (obj[3].toString().equalsIgnoreCase("1")) {
                        jo.put("Status", (Object)"<button type='button' class='btn btn-success btn-xs'>ON</button>");
                    }
                    else {
                        jo.put("Status", (Object)"<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
                    }
                    mainJSON.put(ct, (Object)jo);
                    ++ct;
                }
                else {
                    if (lstatus.equalsIgnoreCase(obj[3].toString())) {
                        System.out.println("");
                    }
                    else {
                        final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
                        innerJSON12.put("End Date", obj[1]);
                        mainJSON.put(ct - 1, (Object)innerJSON12);
                        final JSONObject jo2 = new JSONObject();
                        jo2.put("Start Date", obj[1]);
                        if (obj[3].toString().equalsIgnoreCase("1")) {
                            jo2.put("Status", (Object)"<button type='button' class='btn btn-success btn-xs'>ON</button>");
                        }
                        else {
                            jo2.put("Status", (Object)"<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
                        }
                        mainJSON.put(ct, (Object)jo2);
                        ++ct;
                    }
                    lstatus = obj[3].toString();
                }
            }
        }
        for (int j = 0; j < mainJSON.length(); ++j) {
            final JSONObject jsonObject1 = mainJSON.getJSONObject(j);
            if (j != mainJSON.length() - 1) {
                final Date JSONStartDate = this.df.parse(jsonObject1.getString("Start Date"));
                final Date JSONendDate = this.df.parse(jsonObject1.getString("End Date"));
                jsonObject1.put("Prmname", (Object)param.getPrmname());
                JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
                if (jsonObject1.getString("Status").toString().equalsIgnoreCase("1")) {
                    jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                }
                else {
                    jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
                }
            }
            else {
                final JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
                final Date JSONStartDate2 = this.df.parse(jsonObjectLast.getString("Start Date"));
                jsonObject1.put("Prmname", (Object)param.getPrmname());
                final Date date21 = this.df1.parse(endDate);
                final Date date22 = this.df1.parse(this.df1.format(new Date()));
                final int date23 = date21.compareTo(date22);
                if (date23 == 0) {
                    jsonObjectLast.put("End Date", (Object)this.df.format(new Date()));
                    if (jsonObject1.getString("Status").toString().equalsIgnoreCase("1")) {
                        jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                    }
                    else {
                        jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
                    }
                }
                else if (date23 == -1) {
                    jsonObjectLast.put("End Date", (Object)this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")));
                    if (jsonObject1.getString("Status").toString().equalsIgnoreCase("1")) {
                        jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                    }
                    else {
                        jsonObject1.put("Time", (Object)JsonUtills.getTimeDiffrence(JSONStartDate2, this.df.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
                    }
                }
            }
        }
        final JSONArray finalMainJSON = new JSONArray();
        for (int k = 0; k < mainJSON.length(); ++k) {
            final LinkedHashMap<String, String> myTreeHashMap = new LinkedHashMap<String, String>();
            final JSONObject jsonObject2 = mainJSON.getJSONObject(k);
            myTreeHashMap.put("Prmname", jsonObject2.getString("Prmname"));
            myTreeHashMap.put("Status", jsonObject2.getString("Status"));
            myTreeHashMap.put("Start Time", jsonObject2.getString("Start Date"));
            myTreeHashMap.put("End Time", jsonObject2.getString("End Date"));
            myTreeHashMap.put("Total Time", jsonObject2.getString("Time"));
            finalMainJSON.put((Map)myTreeHashMap);
        }
        return finalMainJSON.toString();
    }
    
    @RequestMapping(value = { "/api/GetProfile/{fId}" }, produces = { "application/json" })
    public String GetProfile(@PathVariable final Long fId) throws JsonGenerationException, JsonMappingException, IOException {
    	DeviceProfile profile=profileRepo.findByProfile(fId);
    	int[] ordinal = { 1 };
    	ObjectMapper mapper = new ObjectMapper();   
		ArrayNode analogNodearrayNode = mapper.createArrayNode();
    	ObjectNode BUTTONNode = mapper.createObjectNode();
    	
    	JSONObject digitalJsonObject = new JSONObject(new ObjectMapper().writeValueAsString(profile.getParameters()));
    	JSONArray analogArray = new JSONArray(digitalJsonObject.get("Analog").toString());
    	
    	analogArray.forEach(SingleAnalogObject -> {
    		JSONObject analogObject = (JSONObject) SingleAnalogObject;
    		ObjectNode PROFILENode = mapper.createObjectNode();
    		    PROFILENode.put("Parameter",  analogObject.get("analogname").toString());
				PROFILENode.put("Profile Index", ordinal[0]);
				PROFILENode.put("Address","<input class='form-control' type='text' id='address"+ordinal[0]+"' name='fname'>");
				PROFILENode.put("No Of Bytes","<input class='form-control'  type='text' id='bytes"+ordinal[0]+"' name='fname'>");
				PROFILENode.put("Meter Type","<input class='form-control'  type='text' id='registertype"+ordinal[0]+"' name='fname'>");
				PROFILENode.put("Register Type","<select class='form-control' id='newregistertype"+ordinal[0]+"'>" + 
						"  <option value='01'>01</option>" + 
						"  <option value='02'>02</option>" +  
						"  <option value='03'>03</option>" +  
						"  <option value='04'>04</option>" +  
						"</select>");
			//	PROFILENode.put("Register Type","<input class='form-control'  type='text' id='newregistertype"+ordinal[0]+"' name='fname'>");
				analogNodearrayNode.add(PROFILENode);
				ordinal[0]++;
    	});
    	analogNodearrayNode.add(BUTTONNode);
    	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
    }
    
    @RequestMapping(value = { "/api/GetSingleAnalogGraph/{deviceId}/{date1}/{date2}" }, produces = { "application/json" })
    public String GetSingleAnalogGraph(@PathVariable long deviceId,@PathVariable String date1,@PathVariable String date2) throws JsonGenerationException, JsonMappingException, IOException {
       List historyList= hstservices.getAnalogRawData(deviceId, date1,date2);
       Devicemaster  device=deviceReop.findBydeviceid(deviceId);
       Analogdata analog=	AnalogDataservice.findBydevice(device);
		JSONArray jarray = new JSONArray();

       if (historyList.size() != 0) {  
			for (int i = 0; i < historyList.size(); i++) {
				
				JSONObject leadmap = new JSONObject();
				Object[] result = (Object[]) historyList.get(i);
				double fuel=JsonUtills.fuel(analog,Double.parseDouble( result[1].toString()) );
				leadmap.put("date", result[0]);
				leadmap.put("analogvalue", result[1]);
				leadmap.put("distance",BigDecimal.valueOf( fuel).setScale(3, RoundingMode.HALF_UP));
				jarray.put(leadmap);
			}
       }
        return jarray.toString();
    }

    
    
}
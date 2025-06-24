package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.MaintenanceStaff;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.MaintenanceStaffService;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Service.SiteServices;

@CrossOrigin(origins = { "*" })
@Transactional  
@RestController
public class LiveGrideController {

	private static final Logger log = Logger.getLogger(LiveGrideController.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final DateFormat dDf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
	@Autowired
	DevicemasterServices devicemasterservices;
	
	@Autowired
	AssignSiteService assSiteService;
	
	@Autowired
	SiteServices siteService;
	
	@Autowired
	SMSTemplateService smsService;
	
	@Autowired
	LasttrackServices lasttrackservices;
	
	@Autowired
	MaintenanceStaffService staffService;
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/api/VodeoconNewLiveGrideAllData/{id}" }, produces = {
	"application/json" })
public String VodeoconNewLiveGrideAllData(@PathVariable long id)
	throws JsonGenerationException, JsonMappingException, IOException, ParseException {
  
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(df);
	ArrayNode analogNodearrayNode = mapper.createArrayNode();
	List<Object[]> listt=  devicemasterservices.getMyDeviced(id);
	for (Object[] device : listt)    
	{  
		List<Object[]> grideData = smsService.GetVodeoconNewLiveGride(new Long( device[0].toString()));//VideoconLastTrack
		
	for (Object[] result1 : grideData) {
		
		String ACMAINS_FAIL_Time="";  
		String Fire_Time="";
		String Door_Time="";
		String DG_Running_Hrs_Time="";
		String DG_Fault_Time="";
		String Battry_Low_Time="";
		String PP_Input_Fail_Time="";
		
		Site site = null;   
		List<AssignSite> aSite = null;
		
		List<Object[]> ACMAINS_FAIL_Object=smsService.getliveGrideNewAlertTime(new Long( device[0].toString()),284945);
		if(ACMAINS_FAIL_Object.size()!=0) {
			Object[] time=ACMAINS_FAIL_Object.get(0);
			ACMAINS_FAIL_Time=time[0].toString();
		}
		
		List<Object[]> Fire_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348798);
		if(Fire_Time_Object.size()!=0) {
			Object[] time=Fire_Time_Object.get(0);
			Fire_Time=time[0].toString();
		}
		
		List<Object[]> Door_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),291934);
		if(Door_Time_Object.size()!=0) {
			Object[] time=Door_Time_Object.get(0);
			Door_Time=time[0].toString();
		}
		
		List<Object[]> DG_Running_Hrs_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348854);
		if(DG_Running_Hrs_Time_Object.size()!=0) {
			Object[] time=DG_Running_Hrs_Time_Object.get(0);
			DG_Running_Hrs_Time=time[0].toString();
		}
		
		List<Object[]> DDG_Fault_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348815);
		if(DDG_Fault_Time_Object.size()!=0) {
			Object[] time=DDG_Fault_Time_Object.get(0);
			DG_Fault_Time=time[0].toString();
		}
		
		List<Object[]> Battry_Low_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348821);
		if(Battry_Low_Time_Object.size()!=0) {
			Object[] time=Battry_Low_Time_Object.get(0);
			Battry_Low_Time=time[0].toString();
		}
		
		List<Object[]> PP_Input_Fail_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348824);
		if(PP_Input_Fail_Time_Object.size()!=0) {
			Object[] time=PP_Input_Fail_Time_Object.get(0);
			PP_Input_Fail_Time=time[0].toString();
		}
		
		aSite = assSiteService.findBydeviceid(Long.parseLong(result1[0].toString()));
		if (aSite.size() != 0)
			site = siteService.findBysiteid(aSite.get(0).getSiteid());

		List<Object[]> voltage=lasttrackservices.GetAnalogValue(new Long( result1[0].toString()),dDf.format(new Date()), "6387981");
	List<Object[]> fuel=lasttrackservices.GetAnalogValue(new Long( result1[0].toString().toString()),dDf.format(new Date()), "6387982");
		
		
		String grideVoltage="0";
		String gridefuelVoltage="0";
		if(voltage.size()!=0)
		{
			Object[] vlt=voltage.get(0);
			grideVoltage=vlt[1].toString();
		}
		
		if(fuel.size()!=0)
		{
			Object[] ful=fuel.get(0);
			gridefuelVoltage=ful[1].toString();
		}
		if(result1[4]!=null||result1[5]!=null||result1[6]!=null||result1[7]!=null||result1[8]!=null||result1[9]!=null||result1[10]!=null)
		{
		
		    Date TodayDate = new Date();   
			Date deviceDate = sdf1.parse(result1[3].toString());  
			long difference = TodayDate.getTime() - deviceDate.getTime(); 
			System.out.println(TodayDate.getTime()+" :: "+deviceDate.getTime());
			System.out.println(sdf1.format(TodayDate)+" :: "+sdf1.format(deviceDate));
			long differenceHours =TimeUnit.MILLISECONDS.toHours(difference);
			log.info("differenceHours :: "+ result1[0]+" :: "+difference+" :: "+differenceHours);
		ObjectNode DIGITALNode = mapper.createObjectNode();
		DIGITALNode.putPOJO("deviceId", result1[0].toString());
		DIGITALNode.putPOJO("devicename", result1[1].toString());
		DIGITALNode.putPOJO("sitename", result1[2].toString());
		DIGITALNode.putPOJO("sitegroup", site == null ? "Not Set" : site.getSite_name());
		DIGITALNode.putPOJO("devicedate", result1[3].toString());
		DIGITALNode.putPOJO("status", differenceHours<2?"Online":"Offline");  
		DIGITALNode.putPOJO("Voltage",BigDecimal.valueOf( Double.parseDouble(grideVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("fuel",BigDecimal.valueOf( Double.parseDouble(gridefuelVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("ACMAINS_FAIL", result1[4].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b><br>"+ACMAINS_FAIL_Time+"</center>");
		DIGITALNode.putPOJO("Fire", result1[5].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b><br>"+Fire_Time+"</center>");
		DIGITALNode.putPOJO("Door", result1[6].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b><br>"+Door_Time+"</center>");
		DIGITALNode.putPOJO("DG_Running_Hrs", result1[7].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b><br>"+DG_Running_Hrs_Time+"</center>");
		DIGITALNode.putPOJO("DG_Fault", result1[8].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b><br>"+DG_Fault_Time+"</center>");
		DIGITALNode.putPOJO("Battry_Low", result1[9].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b><br>"+Battry_Low_Time+"</center>");
		DIGITALNode.putPOJO("PP_Input_Fail", result1[10].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b><br>"+PP_Input_Fail_Time+"</center>");
		analogNodearrayNode.add(DIGITALNode);
		
	}
	}
	}
	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
}
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/api/NewVodeoconNewLiveGrideAllData/{id}" }, produces = {
	"application/json" })
public String NewVodeoconNewLiveGrideAllData(@PathVariable long id)
	throws JsonGenerationException, JsonMappingException, IOException, ParseException {
  
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(df);
	ArrayNode analogNodearrayNode = mapper.createArrayNode();
	List<Object[]> listt=  devicemasterservices.getMyDeviced(id);
	for (Object[] device : listt)    
	{  
		List<Object[]> grideData = smsService.GetVodeoconNewLiveNewGride(new Long( device[0].toString()));//VideoconLastTrack
		
	for (Object[] result1 : grideData) {
		
		String ACMAINS_FAIL_Time="";  
		String Fire_Time="";
		String Door_Time="";
		String DG_Running_Hrs_Time="";
		String DG_Fault_Time="";
		String Battry_Low_Time="";
		String PP_Input_Fail_Time="";
		
		Site site = null;   
		List<AssignSite> aSite = null;
		
		List<Object[]> ACMAINS_FAIL_Object=smsService.getliveGrideNewAlertTime(new Long( device[0].toString()),284945);
		if(ACMAINS_FAIL_Object.size()!=0) {
			Object[] time=ACMAINS_FAIL_Object.get(0);
			ACMAINS_FAIL_Time=time[0].toString();
		}
		
		List<Object[]> Fire_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348798);
		if(Fire_Time_Object.size()!=0) {
			Object[] time=Fire_Time_Object.get(0);
			Fire_Time=time[0].toString();
		}
		
		List<Object[]> Door_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),291934);
		if(Door_Time_Object.size()!=0) {
			Object[] time=Door_Time_Object.get(0);
			Door_Time=time[0].toString();
		}
		
		List<Object[]> DG_Running_Hrs_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348854);
		if(DG_Running_Hrs_Time_Object.size()!=0) {
			Object[] time=DG_Running_Hrs_Time_Object.get(0);
			DG_Running_Hrs_Time=time[0].toString();
		}
		
		List<Object[]> DDG_Fault_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348815);
		if(DDG_Fault_Time_Object.size()!=0) {
			Object[] time=DDG_Fault_Time_Object.get(0);
			DG_Fault_Time=time[0].toString();
		}
		
		List<Object[]> Battry_Low_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348821);
		if(Battry_Low_Time_Object.size()!=0) {
			Object[] time=Battry_Low_Time_Object.get(0);
			Battry_Low_Time=time[0].toString();
		}
		
		List<Object[]> PP_Input_Fail_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348824);
		if(PP_Input_Fail_Time_Object.size()!=0) {
			Object[] time=PP_Input_Fail_Time_Object.get(0);
			PP_Input_Fail_Time=time[0].toString();
		}
		
		aSite = assSiteService.findBydeviceid(Long.parseLong(result1[0].toString()));
		if (aSite.size() != 0)
			site = siteService.findBysiteid(aSite.get(0).getSiteid());

		List<Object[]> voltage=lasttrackservices.GetAnalogValue(new Long( result1[0].toString()),dDf.format(new Date()), "6387981");
	List<Object[]> fuel=lasttrackservices.GetAnalogValue(new Long( result1[0].toString().toString()),dDf.format(new Date()), "6387982");
		
		
		String grideVoltage="0";
		String gridefuelVoltage="0";
		if(voltage.size()!=0)
		{
			Object[] vlt=voltage.get(0);
			grideVoltage=vlt[1].toString();
		}
		
		if(fuel.size()!=0)
		{
			Object[] ful=fuel.get(0);
			gridefuelVoltage=ful[1].toString();
		}
		if(result1[4]!=null||result1[5]!=null||result1[6]!=null||result1[7]!=null||result1[8]!=null||result1[9]!=null||result1[10]!=null)
		{
		
		    Date TodayDate = new Date();   
			Date deviceDate = sdf1.parse(result1[3].toString());  
			long difference = TodayDate.getTime() - deviceDate.getTime(); 
			//System.out.println(TodayDate.getTime()+" :: "+deviceDate.getTime());
			//System.out.println(sdf1.format(TodayDate)+" :: "+sdf1.format(deviceDate));
			long differenceHours =TimeUnit.MILLISECONDS.toHours(difference);
			log.info("differenceHours :: "+ result1[0]+" :: "+difference+" :: "+differenceHours);
		ObjectNode DIGITALNode = mapper.createObjectNode();
		DIGITALNode.putPOJO("deviceId", result1[0].toString());
		DIGITALNode.putPOJO("devicename", result1[1].toString());
		DIGITALNode.putPOJO("sitename", result1[2].toString());
		DIGITALNode.putPOJO("sitegroup", site == null ? "Not Set" : site.getSite_name());
		DIGITALNode.putPOJO("devicedate", result1[3].toString());
		DIGITALNode.putPOJO("status", differenceHours<2?"Online":"Offline");  
		DIGITALNode.putPOJO("Voltage",BigDecimal.valueOf( Double.parseDouble(grideVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("fuel",BigDecimal.valueOf( Double.parseDouble(gridefuelVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("ACMAINS_FAIL", result1[4].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b><br>"+ACMAINS_FAIL_Time+"</center>");
		DIGITALNode.putPOJO("Fire", result1[5].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b><br>"+Fire_Time+"</center>");
		DIGITALNode.putPOJO("Door", result1[6].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b><br>"+Door_Time+"</center>");
		DIGITALNode.putPOJO("DG_Running_Hrs", result1[7].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b><br>"+DG_Running_Hrs_Time+"</center>");
		DIGITALNode.putPOJO("DG_Fault", result1[8].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b><br>"+DG_Fault_Time+"</center>");
		DIGITALNode.putPOJO("Battry_Low", result1[9].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b><br>"+Battry_Low_Time+"</center>");
		DIGITALNode.putPOJO("PP_Input_Fail", result1[10].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b><br>"+PP_Input_Fail_Time+"</center>");
		analogNodearrayNode.add(DIGITALNode);
		
	}
	}
	}
	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
}
	
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/api/NewVodeoconSelectedLiveGrideAllData/{id}" }, produces = {
	"application/json" })
public String NewVodeoconSelectedLiveGrideAllData(@PathVariable long id)
	throws JsonGenerationException, JsonMappingException, IOException, ParseException {
  
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(df);
	ArrayNode analogNodearrayNode = mapper.createArrayNode();
	List<Object[]> grideData=   smsService.GetVodeoconSelectedLiveGride(id);
	System.out.println(grideData.size());
	for (Object[] result1 : grideData) {
		System.out.println(result1[1]);
		
		String Fire_Time="";
		String Door_Time="";
		
		Site site = null;   
		List<AssignSite> aSite = null;
		
		
		List<Object[]> Fire_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348798);
		if(Fire_Time_Object.size()!=0) {
			Object[] time=Fire_Time_Object.get(0);
			Fire_Time=time[0].toString();
		}
		
		List<Object[]> Door_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),291934);
		if(Door_Time_Object.size()!=0) {
			Object[] time=Door_Time_Object.get(0);
			Door_Time=time[0].toString();
		}
		
		
		aSite = assSiteService.findBydeviceid(Long.parseLong(result1[0].toString()));
		if (aSite.size() != 0)
			site = siteService.findBysiteid(aSite.get(0).getSiteid());

		List<Object[]> voltage=lasttrackservices.GetAnalogValue(new Long( result1[0].toString()),dDf.format(new Date()), "6387981");
	List<Object[]> fuel=lasttrackservices.GetAnalogValue(new Long( result1[0].toString().toString()),dDf.format(new Date()), "6387982");
	
	String grideVoltage="0";
	String gridefuelVoltage="0";
	if(voltage.size()!=0)
	{
		Object[] vlt=voltage.get(0);
		grideVoltage=vlt[1].toString();
	}
	
	if(fuel.size()!=0)
	{
		Object[] ful=fuel.get(0);
		gridefuelVoltage=ful[1].toString();
	}
	MaintenanceStaff staff =staffService.getdeviceMaintenanceStaffByid(new Long(result1[0].toString())) ;

	Date TodayDate = new Date();   
	Date deviceDate = sdf1.parse(result1[3].toString());  
	long difference = TodayDate.getTime() - deviceDate.getTime(); 
	long differenceHours =TimeUnit.MILLISECONDS.toHours(difference);
ObjectNode DIGITALNode = mapper.createObjectNode();
DIGITALNode.putPOJO("deviceId", result1[0].toString());
DIGITALNode.putPOJO("devicename", result1[1].toString());
DIGITALNode.putPOJO("sitename", result1[2].toString());
DIGITALNode.putPOJO("sitegroup", site == null ? "Not Set" : site.getSite_name());
DIGITALNode.putPOJO("devicedate", result1[3].toString());
DIGITALNode.putPOJO("status", differenceHours<2?"Online":"Offline");  
DIGITALNode.putPOJO("Voltage",BigDecimal.valueOf( Double.parseDouble(grideVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
DIGITALNode.putPOJO("fuel",BigDecimal.valueOf( Double.parseDouble(gridefuelVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
DIGITALNode.putPOJO("Fire", result1[4].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b><br>"+Fire_Time+"</center>");
DIGITALNode.putPOJO("Door", result1[5].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b><br>"+Door_Time+"</center>");
DIGITALNode.putPOJO("mobile", staff == null ? "Not Set" : staff.getMobile());
analogNodearrayNode.add(DIGITALNode);
System.out.println(staff == null ? "Not Set" : staff.getMobile());
	}
	
	
	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
}
	
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/api/NewVodeoconNewLiveGrideAllFastDataNew/{id}" }, produces = {
	"application/json" })
public String NewVodeoconNewLiveGrideAllFastDataNew(@PathVariable long id)
	throws JsonGenerationException, JsonMappingException, IOException, ParseException {
  
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(df);
	ArrayNode analogNodearrayNode = mapper.createArrayNode();
	StringBuilder query=new StringBuilder();
	query.append("SELECT device_id,devicename,altdevicename, To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date,  \r\n" + 
			"      devicedata->'Digital'->>'284945'  AS acmains_fail," + 
			"      devicedata->'Digital'->>'6348798' AS fire," + 
			"	devicedata->'Digital'->>'291934'  AS door, " + 
			"	devicedata->'Digital'->>'6348854' AS dg_running_hrs, " + 
			"	devicedata->'Digital'->>'6348815' AS dg_fault, " + 
			"	devicedata->'Digital'->>'6348821' AS battry_low, " + 
			"	 devicedata->'Digital'->>'6348824'  AS pp_input_fail,coalesce(maintenancestaff.mobile,'Not Set')  " + 
			"	FROM   lasttrack  JOIN   devicemaster ON  devicemaster.deviceid=lasttrack.device_id        left JOIN  maintenancestaff    on maintenancestaff.deviceid=lasttrack.device_id  " + 
			"	 WHERE (  devicedata->'Digital'->>'284945'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348798'='0' " + 
			"	OR     devicedata->'Digital'->>'291934'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348854'='0' " + 
			"	OR     devicedata->'Digital'->>'6348815'='0' " + 
			"	OR     devicedata->'Digital'->>'6348821'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348824'='0'  " + 
			"	or device_date < current_TIMESTAMP - interval '2 HOUR')  " + 
			"						 AND  lasttrack.device_id IN ( ");
	
	List<Object[]> listt=  devicemasterservices.getMyDeviced(id);  
	for(int d=0;d<listt.size();d++)
	{
		Object[] device=listt.get(d);
		if(d<listt.size()-1)
			query.append(Integer.parseInt(device[0].toString())+",");
		else
			query.append(Integer.parseInt(device[0].toString()));
	}
	
	query.append(")");
	log.info(query.toString());
	
	List<Object[]> grideData = lasttrackservices.getLastrackData(query.toString());
for (Object[] result1 : grideData) {
		
		String ACMAINS_FAIL_Time="";  
		String Fire_Time="";
		String Door_Time="";
		String DG_Running_Hrs_Time="";
		String DG_Fault_Time="";
		String Battry_Low_Time="";
		String PP_Input_Fail_Time="";
		
		Site site = null;   
		List<AssignSite> aSite = null;
		
		List<Object[]> ACMAINS_FAIL_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),284945);
		if(ACMAINS_FAIL_Object.size()!=0) {
			Object[] time=ACMAINS_FAIL_Object.get(0);
			ACMAINS_FAIL_Time=time[0].toString();
		}
		
		List<Object[]> Fire_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348798);
		if(Fire_Time_Object.size()!=0) {
			Object[] time=Fire_Time_Object.get(0);
			Fire_Time=time[0].toString();
		}
		
		List<Object[]> Door_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),291934);
		if(Door_Time_Object.size()!=0) {
			Object[] time=Door_Time_Object.get(0);
			Door_Time=time[0].toString();
		}
		
		List<Object[]> DG_Running_Hrs_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348854);
		if(DG_Running_Hrs_Time_Object.size()!=0) {
			Object[] time=DG_Running_Hrs_Time_Object.get(0);
			DG_Running_Hrs_Time=time[0].toString();
		}
		
		List<Object[]> DDG_Fault_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348815);
		if(DDG_Fault_Time_Object.size()!=0) {
			Object[] time=DDG_Fault_Time_Object.get(0);
			DG_Fault_Time=time[0].toString();
		}
		
		List<Object[]> Battry_Low_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348821);
		if(Battry_Low_Time_Object.size()!=0) {
			Object[] time=Battry_Low_Time_Object.get(0);
			Battry_Low_Time=time[0].toString();
		}
		
		List<Object[]> PP_Input_Fail_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348824);
		if(PP_Input_Fail_Time_Object.size()!=0) {
			Object[] time=PP_Input_Fail_Time_Object.get(0);
			PP_Input_Fail_Time=time[0].toString();
		}
		
		aSite = assSiteService.findBydeviceid(Long.parseLong(result1[0].toString()));
		if (aSite.size() != 0)
			site = siteService.findBysiteid(aSite.get(0).getSiteid());

		List<Object[]> voltage=lasttrackservices.GetAnalogValue(new Long( result1[0].toString()),dDf.format(new Date()), "6387981");
	List<Object[]> fuel=lasttrackservices.GetAnalogValue(new Long( result1[0].toString().toString()),dDf.format(new Date()), "6387982");
		
		
		String grideVoltage="0";
		String gridefuelVoltage="0";
		if(voltage.size()!=0)
		{
			Object[] vlt=voltage.get(0);
			grideVoltage=vlt[1].toString();
		}
		
		if(fuel.size()!=0)
		{
			Object[] ful=fuel.get(0);
			gridefuelVoltage=ful[1].toString();
		}
		if(result1[4]!=null||result1[5]!=null||result1[6]!=null||result1[7]!=null||result1[8]!=null||result1[9]!=null||result1[10]!=null)
		{
		
		    Date TodayDate = new Date();   
			Date deviceDate = sdf1.parse(result1[3].toString());  
			long difference = TodayDate.getTime() - deviceDate.getTime(); 
			//System.out.println(TodayDate.getTime()+" :: "+deviceDate.getTime());
			//System.out.println(sdf1.format(TodayDate)+" :: "+sdf1.format(deviceDate));
			long differenceHours =TimeUnit.MILLISECONDS.toHours(difference);
			log.info("differenceHours :: "+ result1[0]+" :: "+difference+" :: "+differenceHours);
		ObjectNode DIGITALNode = mapper.createObjectNode();
		DIGITALNode.putPOJO("deviceId", result1[0].toString());
		DIGITALNode.putPOJO("devicename", result1[1].toString());
		DIGITALNode.putPOJO("sitename", result1[2].toString());
		DIGITALNode.putPOJO("sitegroup", site == null ? "Not Set" : site.getSite_name());
		DIGITALNode.putPOJO("devicedate", result1[3].toString());
		DIGITALNode.putPOJO("status", differenceHours<2?"Online":"Offline");  
		DIGITALNode.putPOJO("Voltage",BigDecimal.valueOf( Double.parseDouble(grideVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("fuel",BigDecimal.valueOf( Double.parseDouble(gridefuelVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("ACMAINS_FAIL", result1[4].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b><br>"+ACMAINS_FAIL_Time+"</center>");
		DIGITALNode.putPOJO("Fire", result1[5].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b><br>"+Fire_Time+"</center>");
		DIGITALNode.putPOJO("Door", result1[6].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b><br>"+Door_Time+"</center>");
		DIGITALNode.putPOJO("DG_Running_Hrs", result1[7].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b><br>"+DG_Running_Hrs_Time+"</center>");
		DIGITALNode.putPOJO("DG_Fault", result1[8].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b><br>"+DG_Fault_Time+"</center>");
		DIGITALNode.putPOJO("Battry_Low", result1[9].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b><br>"+Battry_Low_Time+"</center>");
		DIGITALNode.putPOJO("PP_Input_Fail", result1[10].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b><br>"+PP_Input_Fail_Time+"</center>");
		DIGITALNode.putPOJO("Contact", result1[11].toString());
		analogNodearrayNode.add(DIGITALNode);
		
	}
	}
	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
}
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/api/NewVodeoconNewLiveGrideAllFastData/{id}" }, produces = {
	"application/json" })
public String NewVodeoconNewLiveGrideAllFastData(@PathVariable long id)
	throws JsonGenerationException, JsonMappingException, IOException, ParseException {
  
	ObjectMapper mapper = new ObjectMapper();
	mapper.setDateFormat(df);
	ArrayNode analogNodearrayNode = mapper.createArrayNode();
	StringBuilder query=new StringBuilder();
	query.append("SELECT device_id,devicename,altdevicename, To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date,  \r\n" + 
			"      devicedata->'Digital'->>'284945'  AS acmains_fail," + 
			"      devicedata->'Digital'->>'6348798' AS fire," + 
			"	devicedata->'Digital'->>'291934'  AS door, " + 
			"	devicedata->'Digital'->>'6348854' AS dg_running_hrs, " + 
			"	devicedata->'Digital'->>'6348815' AS dg_fault, " + 
			"	devicedata->'Digital'->>'6348821' AS battry_low, " + 
			"	 devicedata->'Digital'->>'6348824'  AS pp_input_fail,coalesce(maintenancestaff.mobile,'Not Set')  " + 
			"	FROM   lasttrack  JOIN   devicemaster ON  devicemaster.deviceid=lasttrack.device_id        left JOIN  maintenancestaff    on maintenancestaff.deviceid=lasttrack.device_id  " + 
			"	 WHERE (  devicedata->'Digital'->>'284945'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348798'='0' " + 
			"	OR     devicedata->'Digital'->>'291934'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348854'='0' " + 
			"	OR     devicedata->'Digital'->>'6348815'='0' " + 
			"	OR     devicedata->'Digital'->>'6348821'='0'  " + 
			"	OR     devicedata->'Digital'->>'6348824'='0'  " + 
			"	or device_date < current_TIMESTAMP - interval '2 HOUR')  " + 
			"						 AND  lasttrack.device_id IN ( ");
	
	List<Object[]> listt=  devicemasterservices.getMyDeviced(id);  
	for(int d=0;d<listt.size();d++)
	{
		Object[] device=listt.get(d);
		if(d<listt.size()-1)
			query.append(Integer.parseInt(device[0].toString())+",");
		else
			query.append(Integer.parseInt(device[0].toString()));
	}
	
	query.append(")");
	log.info(query.toString());
	
	List<Object[]> grideData = lasttrackservices.getLastrackData(query.toString());
for (Object[] result1 : grideData) {
		
		String ACMAINS_FAIL_Time="";  
		String Fire_Time="";
		String Door_Time="";
		String DG_Running_Hrs_Time="";
		String DG_Fault_Time="";
		String Battry_Low_Time="";
		String PP_Input_Fail_Time="";
		
		Site site = null;   
		List<AssignSite> aSite = null;
		
		List<Object[]> ACMAINS_FAIL_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),284945);
		if(ACMAINS_FAIL_Object.size()!=0) {
			Object[] time=ACMAINS_FAIL_Object.get(0);
			ACMAINS_FAIL_Time=time[0].toString();
		}
		
		List<Object[]> Fire_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348798);
		if(Fire_Time_Object.size()!=0) {
			Object[] time=Fire_Time_Object.get(0);
			Fire_Time=time[0].toString();
		}
		
		List<Object[]> Door_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),291934);
		if(Door_Time_Object.size()!=0) {
			Object[] time=Door_Time_Object.get(0);
			Door_Time=time[0].toString();
		}
		
		List<Object[]> DG_Running_Hrs_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348854);
		if(DG_Running_Hrs_Time_Object.size()!=0) {
			Object[] time=DG_Running_Hrs_Time_Object.get(0);
			DG_Running_Hrs_Time=time[0].toString();
		}
		
		List<Object[]> DDG_Fault_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348815);
		if(DDG_Fault_Time_Object.size()!=0) {
			Object[] time=DDG_Fault_Time_Object.get(0);
			DG_Fault_Time=time[0].toString();
		}
		
		List<Object[]> Battry_Low_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348821);
		if(Battry_Low_Time_Object.size()!=0) {
			Object[] time=Battry_Low_Time_Object.get(0);
			Battry_Low_Time=time[0].toString();
		}
		
		List<Object[]> PP_Input_Fail_Time_Object=smsService.getliveGrideNewAlertTime(new Long( result1[0].toString()),6348824);
		if(PP_Input_Fail_Time_Object.size()!=0) {
			Object[] time=PP_Input_Fail_Time_Object.get(0);
			PP_Input_Fail_Time=time[0].toString();
		}
		
		aSite = assSiteService.findBydeviceid(Long.parseLong(result1[0].toString()));
		if (aSite.size() != 0)
			site = siteService.findBysiteid(aSite.get(0).getSiteid());

		List<Object[]> voltage=lasttrackservices.GetAnalogValue(new Long( result1[0].toString()),dDf.format(new Date()), "6387981");
	List<Object[]> fuel=lasttrackservices.GetAnalogValue(new Long( result1[0].toString().toString()),dDf.format(new Date()), "6387982");
		
		
		String grideVoltage="0";
		String gridefuelVoltage="0";
		if(voltage.size()!=0)
		{
			Object[] vlt=voltage.get(0);
			grideVoltage=vlt[1].toString();
		}
		
		if(fuel.size()!=0)
		{
			Object[] ful=fuel.get(0);
			gridefuelVoltage=ful[1].toString();
		}
		if(result1[4]!=null||result1[5]!=null||result1[6]!=null||result1[7]!=null||result1[8]!=null||result1[9]!=null||result1[10]!=null)
		{
		
		    Date TodayDate = new Date();   
			Date deviceDate = sdf1.parse(result1[3].toString());  
			long difference = TodayDate.getTime() - deviceDate.getTime(); 
			//System.out.println(TodayDate.getTime()+" :: "+deviceDate.getTime());
			//System.out.println(sdf1.format(TodayDate)+" :: "+sdf1.format(deviceDate));
			long differenceHours =TimeUnit.MILLISECONDS.toHours(difference);
			log.info("differenceHours :: "+ result1[0]+" :: "+difference+" :: "+differenceHours);
		ObjectNode DIGITALNode = mapper.createObjectNode();
		DIGITALNode.putPOJO("deviceId", result1[0].toString());
		DIGITALNode.putPOJO("devicename", result1[1].toString());
		DIGITALNode.putPOJO("sitename", result1[2].toString());
		DIGITALNode.putPOJO("sitegroup", site == null ? "Not Set" : site.getSite_name());
		DIGITALNode.putPOJO("devicedate", result1[3].toString());
		DIGITALNode.putPOJO("status", differenceHours<2?"Online":"Offline");  
		DIGITALNode.putPOJO("Voltage",BigDecimal.valueOf( Double.parseDouble(grideVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("fuel",BigDecimal.valueOf( Double.parseDouble(gridefuelVoltage)).setScale(3, RoundingMode.HALF_UP)+" Volt");
		DIGITALNode.putPOJO("ACMAINS_FAIL", result1[4].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>ACMAINS FAIL</b><br>"+ACMAINS_FAIL_Time+"</center>");
		DIGITALNode.putPOJO("Fire", result1[5].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Fire</b><br>"+Fire_Time+"</center>");
		DIGITALNode.putPOJO("Door", result1[6].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Door</b><br>"+Door_Time+"</center>");
		DIGITALNode.putPOJO("DG_Running_Hrs", result1[7].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Running</b><br>"+DG_Running_Hrs_Time+"</center>");
		DIGITALNode.putPOJO("DG_Fault", result1[8].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>DG Fault</b><br>"+DG_Fault_Time+"</center>");
		DIGITALNode.putPOJO("Battry_Low", result1[9].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>Battery Low</b><br>"+Battry_Low_Time+"</center>");
		DIGITALNode.putPOJO("PP_Input_Fail", result1[10].toString().equalsIgnoreCase("1") ? "<center style='font-size: 12px;'><img src='../../img/02.png' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b></center>" : "<center style='font-size: 12px;'><img src='../../img/01.gif' height='25px' width='25px' class='center' alt='Image Not Found'><br><b>PP Input Fail</b><br>"+PP_Input_Fail_Time+"</center>");
		DIGITALNode.putPOJO("Contact", result1[11].toString());
		analogNodearrayNode.add(DIGITALNode);
		
	}
	}
	return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
}
}

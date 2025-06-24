package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "*")
@Transactional
@RestController
public class DeviceProfileController {
	
 private static final Logger log = Logger.getLogger(DeviceProfileController.class);
	@Autowired
	DeviceProfileServices DeviceProfileservices;
	@Autowired
	ParameterServices Parameterservices;
	@Autowired
	ApiService apiService;
	
	
	
	@RequestMapping(method=RequestMethod.POST,value="api/deviceprofile")@ExceptionHandler(SpringException.class)
	public String savedata(@RequestBody DeviceProfile bs)
	{ 
		log.info("jsxkaxbkz");
		DeviceProfileservices.save(bs);
		return new SpringException(true, "Componet Sucessfully Added").toString();
	}
	
	@RequestMapping(value="api/deviceprofile" ,produces={"application/json"})
	public List<DeviceProfile> getdeviceprofilelist()
	{
	     return DeviceProfileservices.getlist();
	}
	
	
	/*@RequestMapping(value="user/deviceprofile/{key}" ,produces={"application/json"})
	public List<DeviceProfile> getdeviceprofilelist(HttpServletRequest request,@PathVariable String key)
	{
		String key1=null;
		Apikey api = new Apikey();
		 HttpSession session = request.getSession();
		 System.out.println("Session_KEY::--->>"+ session.getAttribute("LoginKEY")+"  "+"key....>>"+key);
		 key1=session.getAttribute("LoginKEY").toString();
		 api = apiService.findBykeyValue(key1);
		 System.out.println("api_KEY::--->>"+ api.getKeyValue());
		 
		 if(key.equals(key1)) {
			 return DeviceProfileservices.getlist();
		 }else {
			 
			 String listString = list.stream().map(Object::toString).collect(Collectors.joining(", "));
			 
			// return new SpringException(false, "Invalid Key").toString();
		 }
	}*/
	
		@RequestMapping(value="api/deviceprofile/{id}",method=RequestMethod.GET)
		public DeviceProfile getdeviceprofile(@PathVariable("id") Long id) {
			return DeviceProfileservices.get(id);
		}
	
		@RequestMapping(value = "api/multipalDeviceProfiles", method = RequestMethod.POST)
		public List<DeviceProfile> getDeviceProfiles(@RequestBody List<Long> ids) {
		    return DeviceProfileservices.getDeviceProfilesByIds(ids);
		}

		//set Content-Type=application/json ANd set raw=raw={"macaddress":"asuiags", "scannerName":"asuiags","latlang":"asuiags", "cordinate":"asuiags" }(here put key and value and pass arry of it)
		/*@RequestMapping(method=RequestMethod.PUT,value="/deviceprofile/{id}")
		public String updatedeviceprofile(@RequestBody DeviceProfile bs,@PathVariable Long id)
		{
			bs.setPrid(id);
			return DeviceProfileservices.update(bs);
		}*/
		
		@RequestMapping(method=RequestMethod.GET,value="api/deldeviceprofile/{id}/{key}")
		public String deletedeviceprofile(@PathVariable long id,HttpServletRequest request,@PathVariable String key)
		{
			
			Apikey api=apiService.findBykeyValue(key);
			
			if(api!=null)
			{
				 return DeviceProfileservices.delete(id);
			 }else {
				 return new SpringException(false, "Invalid Key").toString();
			 }
		}
		
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "api/savedeviceprofile",produces={"application/json"})
		@ExceptionHandler(SpringException.class)
		public @ResponseBody ModelAndView savedeviceprofile(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws JsonParseException, JsonMappingException, IOException {
			log.info("in servlet...");
			String[] unit = request.getParameterValues("unit[]");
			String[] formula = request.getParameterValues("formula[]");
			String[] analogdata = request.getParameterValues("analogdata[]");
			String[] digitaldata = request.getParameterValues("digitaldata[]");
			String[] dreverse = request.getParameterValues("dreverse[]");
			String[] rs232data = request.getParameterValues("rs232data[]");
			String[] rs232unit = request.getParameterValues("rs232unit[]");
			String[] analogioindex = request.getParameterValues("analogioindex[]");
			String[] dioindex = request.getParameterValues("dioindex[]");
			String[] rs232ioindex = request.getParameterValues("rs232ioindex[]");
			
			// log.info("Analog length"+analogdata.length);
			// log.info("Digi length"+digitaldata.length);
			// log.info("RS232 length"+rs232data.length);
			 
			 JSONObject jo = new JSONObject();
			 
			 JSONArray analogjsonarr = new JSONArray();
			 for(int i=0;i<analogdata.length;i++){
			  // log.info("Analog unit"+unit[i]);
	         //  log.info("Analog formula"+formula[i]);
	         //  log.info("Analog analogdata"+analogdata[i]);
	           
	           JSONObject analogjo = new JSONObject();
	           analogjo.put("Analoginput", analogdata[i]);
	           analogjo.put("Analogunit", unit[i]);
	           analogjo.put("Analogformula", formula[i]);
	           analogjo.put("analogioindex", Integer.parseInt(analogioindex[i]));
	           String profilename=Parameterservices.get(Long.parseLong(analogdata[i])).getPrmname();
	         //  log.info("profilename:::  "+profilename);
	           analogjo.put("analogname", profilename);
	           analogjsonarr.put(analogjo);
	         }
			 	jo.put("Analog", analogjsonarr);
			 
			 
			 JSONArray digitaljsonarr = new JSONArray();
			 for(int i=0;i<digitaldata.length;i++){
		          // log.info("digital dreverse"+dreverse[i]);
		       //    log.info("digital digitaldata"+digitaldata[i]);
		           String digidata[]= digitaldata[i].split("#");
		           JSONObject digitaljo = new JSONObject();
		           digitaljo.put("reverse", Boolean.parseBoolean(dreverse[i]));
		           digitaljo.put("parameterId", digidata[0]);
		           digitaljo.put("parametername", digidata[1]);
		           digitaljo.put("dioindex",Integer.parseInt(dioindex[i]));
		           digitaljsonarr.put(digitaljo);
		        }
			    jo.put("Digital", digitaljsonarr);
			  
			  JSONArray rs232jsonarr = new JSONArray();
			  for(int i=0;i<rs232data.length;i++){
				  
		          // log.info("rs232 rs232data"+rs232data[i]);
		          // log.info("rs232 rsreverse "+rsreverse[i]);
		           
		           JSONObject rs232jo = new JSONObject();
		           String rs232dataa[]= rs232data[i].split("#");
		           rs232jo.put("rs232unit", rs232unit[i]);
		           rs232jo.put("parameterId", rs232dataa[0]);
		           rs232jo.put("parametername", rs232dataa[1]);
		           rs232jo.put("rs232ioindex", Integer.parseInt(rs232ioindex[i]));
		           rs232jsonarr.put(rs232jo);
		        }
			  jo.put("Rs232", rs232jsonarr);
			  
			  log.info("JSONString::"+jo.toString());
			  
			  String devicename = request.getParameter("devicename");
			  String gpsdata="{}";
			  Map<String, Object> parameters = new ObjectMapper().readValue(jo.toString(), HashMap.class);
			  Map<String, Object> gpsdataaa = new ObjectMapper().readValue(gpsdata, Map.class);
			  DeviceProfile dp= new DeviceProfile();
			  dp.setParameters(parameters);
			  dp.setProfilename(devicename);
			  dp.setGpsdata(gpsdataaa);
			  DeviceProfileservices.save(dp);
			  model.addAttribute("jsonresponse","DeviceProfile Sucessfully saved");
		        return new ModelAndView("redirect:/welcome#/deviceprofilelist", model);
		}
		
		//new code with min and max
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "api/saveDeviceProfileNew", produces = {"application/json"})
		@ExceptionHandler(SpringException.class)
		public @ResponseBody ModelAndView saveDeviceProfileNew(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JsonParseException, JsonMappingException, IOException {
		    log.info("in servlet...");

		    String[] unit = request.getParameterValues("unit[]");
		    String[] formula = request.getParameterValues("formula[]");
		    String[] analogdata = request.getParameterValues("analogdata[]");
		    String[] digitaldata = request.getParameterValues("digitaldata[]");
		    String[] dreverse = request.getParameterValues("dreverse[]");
		    String[] analogioindex = request.getParameterValues("analogioindex[]");
		    String[] dioindex = request.getParameterValues("dioindex[]");
		    String[] min = request.getParameterValues("min[]");
		    String[] max = request.getParameterValues("max[]");
		    String[] cumulative = request.getParameterValues("cumulative[]");

		    JSONObject jo = new JSONObject();

		    JSONArray analogjsonarr = new JSONArray();
		    for (int i = 0; i < analogdata.length; i++) {
		        JSONObject analogjo = new JSONObject();
		        analogjo.put("Analoginput", analogdata[i]);
		        analogjo.put("Analogunit", unit[i]);
		        analogjo.put("Analogformula", formula[i]);
		        analogjo.put("analogioindex", Integer.parseInt(analogioindex[i]));
		        analogjo.put("min", (min != null && i < min.length) ? min[i] : "0");
		        analogjo.put("max", (max != null && i < max.length) ? max[i] : "0");
		        analogjo.put("cumulative", (cumulative != null && i < cumulative.length) && Boolean.parseBoolean(cumulative[i]));

		        String profilename = Parameterservices.get(Long.parseLong(analogdata[i])).getPrmname();
		        analogjo.put("analogname", profilename);

		        analogjsonarr.put(analogjo);
		    }
		    jo.put("Analog", analogjsonarr);

		    JSONArray digitaljsonarr = new JSONArray();
		    for (int i = 0; i < digitaldata.length; i++) {
		        String digidata[] = digitaldata[i].split("#");
		        JSONObject digitaljo = new JSONObject();
		        digitaljo.put("reverse", Boolean.parseBoolean(dreverse[i]));
		        digitaljo.put("parameterId", digidata[0]);
		        digitaljo.put("parametername", digidata[1]);
		        digitaljo.put("dioindex", Integer.parseInt(dioindex[i]));
		        digitaljsonarr.put(digitaljo);
		    }
		    jo.put("Digital", digitaljsonarr);

		    log.info("JSONString::" + jo.toString());

		    String devicename = request.getParameter("devicename");
		    String gpsdata = "{}";
		    Map<String, Object> parameters = new ObjectMapper().readValue(jo.toString(), HashMap.class);
		    Map<String, Object> gpsdataaa = new ObjectMapper().readValue(gpsdata, Map.class);

		    DeviceProfile dp = new DeviceProfile();
		    dp.setParameters(parameters);
		    dp.setProfilename(devicename);
		    dp.setGpsdata(gpsdataaa);

		    DeviceProfileservices.save(dp);

		    model.addAttribute("jsonresponse", "DeviceProfile Sucessfully saved");
		    return new ModelAndView("redirect:/welcome#/deviceprofilelist", model);
		}

		
		
		
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "api/updatedeviceprofile",produces={"application/json"})
		public @ResponseBody String updatedeviceprofile(HttpServletRequest request,HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		 
			log.info("in servlet...");
			String[] unit = request.getParameterValues("unit[]");
			String[] formula = request.getParameterValues("formula[]");
			String[] analogdata = request.getParameterValues("analogdata[]");
			String[] digitaldata = request.getParameterValues("digitaldata[]");
			String[] dreverse = request.getParameterValues("dreverse[]");
			String[] rs232data = request.getParameterValues("rs232data[]");
			String[] rsreverse = request.getParameterValues("rsreverse[]");
			int pid=Integer.parseInt(request.getParameter("d_id"));
			 
			 JSONObject jo = new JSONObject();
			 
			 JSONArray analogjsonarr = new JSONArray();
			 for(int i=0;i<analogdata.length;i++){
			   log.info("Analog unit"+unit[i]);
	           log.info("Analog formula"+formula[i]);
	           log.info("Analog analogdata"+analogdata[i]);
	           
	           JSONObject analogjo = new JSONObject();
	           analogjo.put("Analoginput", analogdata[i]);
	           String profilename=Parameterservices.get(Long.parseLong(analogdata[i])).getPrmname();
	           log.info("profilename:::  "+profilename);
	           analogjo.put("analogname", profilename);
	           analogjo.put("Analogunit", unit[i]);
	           analogjo.put("Analogformula", formula[i]);
	          
	           analogjsonarr.put(analogjo);
	         }
			 	jo.put("Analog", analogjsonarr);
			 
			 
			 JSONArray digitaljsonarr = new JSONArray();
			 for(int i=0;i<digitaldata.length;i++){
				  
		           log.info("digital dreverse"+dreverse[i]);
		           log.info("digital digitaldata"+digitaldata[i]);
		           
		           JSONObject digitaljo = new JSONObject();
		           digitaljo.put("digitaldreverse", dreverse[i]);
		           digitaljo.put("digitaldata", digitaldata[i]);
		           digitaljsonarr.put(digitaljo);
		        }
			    jo.put("Digital", digitaljsonarr);
			  
			  JSONArray rs232jsonarr = new JSONArray();
			  for(int i=0;i<rs232data.length;i++){
				  
		           log.info("rs232 rs232data"+rs232data[i]);
		           log.info("rs232 rsreverse "+rsreverse[i]);
		           
		           JSONObject rs232jo = new JSONObject();
		           rs232jo.put("rs232data", rs232data[i]);
		           rs232jo.put("rsreverse", rsreverse[i]);
		           rs232jsonarr.put(rs232jo);
		        }
			  jo.put("Rs232", rs232jsonarr);
			  
			  log.info("JSONString::"+jo.toString());
			  
			  String devicename = request.getParameter("devicename");
			  String gpsdata="{}";
			  Map<String, Object> parameters = new ObjectMapper().readValue(jo.toString(), Map.class);
			  Map<String, Object> gpsdataaa = new ObjectMapper().readValue(gpsdata, Map.class);
			  Long l = new Long(pid);
			  if(l!=0) {
				  DeviceProfile dp= new DeviceProfile();
				  dp.setPrid(l);
				  dp.setParameters(parameters);
				  dp.setProfilename(devicename);
				  dp.setGpsdata(gpsdataaa);
				  return DeviceProfileservices.update(dp); 
			  }else {
				  return "noo.....";
			  }
			 
		}

		//new update code
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "api/updateDeviceProfileNew", produces = {"application/json"})
		public @ResponseBody String updateDeviceProfileNew(HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {

		    log.info("in servlet...");

		    String[] unit = request.getParameterValues("unit[]");
		    String[] formula = request.getParameterValues("formula[]");
		    String[] analogdata = request.getParameterValues("analogdata[]");
		    String[] digitaldata = request.getParameterValues("digitaldata[]");
		    String[] dreverse = request.getParameterValues("dreverse[]");
		    String[] analogioindex = request.getParameterValues("analogioindex[]");
		    String[] min = request.getParameterValues("min[]");
		    String[] max = request.getParameterValues("max[]");
		    String[] cumulative = request.getParameterValues("cumulative[]");
		    String[] dioindex = request.getParameterValues("dioindex[]");

		    int pid = Integer.parseInt(request.getParameter("d_id"));

		    JSONObject jo = new JSONObject();
		    JSONArray analogjsonarr = new JSONArray();
		
		    for (int i = 0; i < analogdata.length; i++) {
		        log.info("Analog unit: " + unit[i]);
		        log.info("Analog formula: " + formula[i]);
		        log.info("Analog analogdata: " + analogdata[i]);

		        JSONObject analogjo = new JSONObject();
		        analogjo.put("Analoginput", analogdata[i]);
		        analogjo.put("Analogunit", unit[i]);
		        analogjo.put("Analogformula", formula[i]);
		        analogjo.put("analogioindex", Integer.parseInt(analogioindex[i]));
		        analogjo.put("min", (min != null && i < min.length) ? min[i] : "0");
		        analogjo.put("max", (max != null && i < max.length) ? max[i] : "0");
		        analogjo.put("cumulative", (cumulative != null && i < cumulative.length) && Boolean.parseBoolean(cumulative[i]));

		        String profilename = Parameterservices.get(Long.parseLong(analogdata[i])).getPrmname();
		        log.info("profilename::: " + profilename);
		        analogjo.put("analogname", profilename);

		        analogjsonarr.put(analogjo);
		    }
		    jo.put("Analog", analogjsonarr);

		    JSONArray digitaljsonarr = new JSONArray();
		    for (int i = 0; i < digitaldata.length; i++) {
		        log.info("digital dreverse: " + dreverse[i]);
		        log.info("digital digitaldata: " + digitaldata[i]);

		        JSONObject digitaljo = new JSONObject();
		        String digidata[] = digitaldata[i].split("#");
		        digitaljo.put("reverse", Boolean.parseBoolean(dreverse[i]));
		        digitaljo.put("parameterId", digidata[0]);
		        digitaljo.put("parametername", digidata[1]);
		        digitaljo.put("dioindex", Integer.parseInt(dioindex[i]));
		        digitaljsonarr.put(digitaljo);
		    }
		    jo.put("Digital", digitaljsonarr);

		    log.info("JSONString::" + jo.toString());

		    String devicename = request.getParameter("devicename");
		    String gpsdata = "{}";

		    Map<String, Object> parameters = new ObjectMapper().readValue(jo.toString(), Map.class);
		    Map<String, Object> gpsdataaa = new ObjectMapper().readValue(gpsdata, Map.class);
		    Long l = Long.valueOf(pid);

		    if (l != 0) {
		        DeviceProfile dp = new DeviceProfile();
		        dp.setPrid(l);
		        dp.setParameters(parameters);
		        dp.setProfilename(devicename);
		        dp.setGpsdata(gpsdataaa);
		        return DeviceProfileservices.update(dp);
		    } else {
		        return "Invalid, try again...";
		    }
		}

		
		
		/*@RequestMapping(method = RequestMethod.POST, value = "/saveanalogsetting",produces={"application/json"})
		public @ResponseBody String saveanalogsetting(HttpServletRequest request,HttpServletResponse response) {
		 
			log.info("in servlet...");
			String[] analogdata = request.getParameterValues("analogdata[]");
			String[] msg = request.getParameterValues("msg[]");
			
			String range1 = request.getParameter("range1");
			double value = Double.parseDouble(range1);
			
			String range2 = request.getParameter("range2");
			double value1 = Double.parseDouble(range2);
			
			String level1 = request.getParameter("level1");
			double value2 = Double.parseDouble(level1);
			
			String level2 = request.getParameter("level2");
			double value3 = Double.parseDouble(level2);
			
	
			Analogdata ad = new Analogdata();
			ad.setAnaloglevel1(value2);
			ad.setAnaloglevel2(value3);
			ad.setAnalogrange1(value);
			ad.setAnalogrange2(value1);
			ad.setAnalogdigi(analogdata);
			 
			DeviceProfileservices.save(ad);
			
			return "hello";
		}*/   
	   
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "api/savedeviceprofile1/{anunit}/{aninput}/{anindex}/{anformula}/{dginput}/{dgreverse}/{dgindex}/{gpsindex}/{gpsinput}/{prname}", produces = {
		"application/json" })
		@ExceptionHandler(SpringException.class)
		public void savedeviceprofile(ModelMap model, @PathVariable("anunit") String[] anunit,
		@PathVariable("aninput") String[] aninput, @PathVariable("anindex") String[] anindex,
		@PathVariable("anformula") String[] anformula, @PathVariable("dginput") String[] dginput,
		@PathVariable("dgreverse") String[] dgreverse, @PathVariable("dgindex") String[] dgindex,
		@PathVariable("gpsindex") String gpsindex, @PathVariable("gpsinput") String gpsinput,@PathVariable("prname") String prname)
		throws JsonParseException, JsonMappingException, IOException {
		log.info("in servlet...");
		String[] unit = anunit;
		String[] formula = anformula;
		String[] analogioindex = anindex;
		String[] analoginput = aninput;
		String[] digitaldata = dginput;
		String[] dreverse = dgreverse;
		String[] dioindex = dgindex;
		String gpsinput1 = gpsinput;
		String gpsindex1 = gpsindex;

		JSONObject jo = new JSONObject();

		JSONArray analogjsonarr = new JSONArray();
		for (int i = 0; i < analoginput.length; i++) {
		if(analoginput[i].toString().equalsIgnoreCase("NA"))
		break;
		else  
		{
		JSONObject analogjo = new JSONObject();
		analogjo.put("Analoginput", analogioindex[i]);
		analogjo.put("Analogunit", unit[i]);
		analogjo.put("Analogformula", formula[i]);
		analogjo.put("analogioindex", Integer.parseInt(analogioindex[i]));
		String profilename = Parameterservices.get(Long.parseLong(analoginput[i])).getPrmname();
		analogjo.put("analogname", profilename);
		analogjsonarr.put(analogjo);
		}
		}
		jo.put("Analog", analogjsonarr);

		JSONArray digitaljsonarr = new JSONArray();
		for (int i = 0; i < digitaldata.length; i++) {
		if(digitaldata[i].toString().equalsIgnoreCase("NA"))
		break;
		else
		{
		String profilename = Parameterservices.get(Long.parseLong(dginput[i])).getPrmname();
		JSONObject digitaljo = new JSONObject();
		digitaljo.put("reverse", Boolean.parseBoolean(dgreverse[i]));
		digitaljo.put("parameterId", dginput[i]);
		digitaljo.put("parametername", profilename);
		digitaljo.put("dioindex", Integer.parseInt(dgindex[i]));
		digitaljsonarr.put(digitaljo);
		}
		}
		jo.put("Digital", digitaljsonarr);

		JSONObject jo1 = new JSONObject();
		JSONArray gpsarray = new JSONArray();
		if(gpsinput1.equalsIgnoreCase("NA")) {
		gpsinput1="NA";
		gpsindex1="NA";
		}else {

		JSONObject gpsjo = new JSONObject();
		gpsjo.put("Gpsinput", gpsinput1.toString());
		gpsjo.put("Gpsindex",gpsindex1.toString());
		gpsarray.put(gpsjo);
		}
		jo1.put("Gps", gpsarray);
		System.out.println("Real  : " + jo.toString()); // Ok

		Map<String, Object> parameters = new ObjectMapper().readValue(jo.toString(), HashMap.class);
		Map<String, Object> gpsdataaa = new ObjectMapper().readValue(jo1.toString(), Map.class);
		DeviceProfile dp = new DeviceProfile();
		dp.setParameters(parameters);
		dp.setGpsdata(gpsdataaa);
		dp.setProfilename(prname);
		DeviceProfileservices.save(dp);
		}
		
}

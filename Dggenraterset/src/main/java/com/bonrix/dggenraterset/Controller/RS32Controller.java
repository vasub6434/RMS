package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class RS32Controller {
	@Autowired
	LasttrackServices lasttrackservices;

	@Autowired
	ParameterRepository reo;

	@Autowired
	DevicemasterRepository deviceReop;

	@Autowired
	SiteServices Siteservices;
	
	
	
	private Logger log = Logger.getLogger(RS32Controller.class);


	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@RequestMapping(value = "/api/GetDashboard/{id}", produces = { "application/json" })
	public String getcomponetlist(@PathVariable Long id)   
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		Lasttrack lastTrack = lasttrackservices.findOne(id);
		Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
		DeviceProfile dp = device.getDp();
		Map<String, Object> profile=dp.getParameters();
		ObjectMapper mapper = new ObjectMapper();
		int PintArray[] = null;
		
		for (Entry<String, Object> entry : profile.entrySet()) { 
			if(entry.getKey().equalsIgnoreCase("Analog"))
			{
				JSONObject parameters = new JSONObject(dp.getParameters());
				JSONArray analogParameter = parameters.getJSONArray("Analog");
				PintArray = new int[analogParameter.length()];
				for (int i = 0; i < analogParameter.length(); i++) {
					JSONObject obj = (JSONObject) analogParameter.get(i);
				log.info("PROFILE :: "+obj.get("Analoginput").toString());
					PintArray[i]=Integer.parseInt(obj.get("Analoginput").toString());
				}
			}
    } 
		
		JSONObject analogJsonObject = new JSONObject(
				new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
		JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
		
log.info("PintArray :: "+Arrays.toString(PintArray));
		final JSONArray analogNodearrayNode = new JSONArray();
		int DintArray[] = new int[ PintArray.length];

int DintArray1[] = new int[ PintArray.length];
for (int i=0; i<PintArray.length; i++) 
	DintArray1[i] = PintArray[i]; 
/*
try
{*/
System.out.println(Arrays.toString(DintArray1));
	analogArray.forEach(SingleAnalogObject -> {
		JSONObject analogObject = (JSONObject) SingleAnalogObject;
		System.out.println(analogObject.toString());
		for (int i = 0; i < DintArray1.length; i++) {
			System.out.println(analogObject.names().getString(i));
			ObjectNode analogNode = mapper.createObjectNode();
			analogNode.put(reo.findByid(new Long(DintArray1[i])).getPrmname(),
					analogObject.get(analogObject.names().getString(i)).toString());
		analogNode.put("Unit",reo.getLasttrackUnitsNew(device.getDp().getPrid(), analogObject.names().getString(i)).toString());
		analogNode.put("ParameterId",analogObject.names().getString(i).toString());
		analogNodearrayNode.put(analogNode);
		}
	});
/*}catch(Exception e)
{
	log.info("RS323 ERROR :: "+e);
}*/
	
		ObjectNode objectNode = mapper.createObjectNode();
		mapper.setDateFormat(df);
		objectNode.putPOJO("Data", analogNodearrayNode);
		objectNode.putPOJO("DeviceId", lastTrack.getDeviceId());
		objectNode.putPOJO("DeviceDate", lastTrack.getDeviceDate());
		objectNode.putPOJO("DeviceName", device.getDevicename());
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
	}
	

	

	@RequestMapping(value = "/api/GetDashboard2/{id}", produces = { "application/json" })
	public String getcomponetlist2(@PathVariable String id)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		ObjectMapper mapper = new ObjectMapper();
		System.out.println(id); 
		ArrayNode analogNodearrayNode = mapper.createArrayNode();
		
		//String deviceids="281,282";
	  
		String[] sdeviceid=id.split(",");//splits the string based on whitespace  
		//using java foreach loop to print elements of string array  
		for(String w:sdeviceid){  
		System.out.println(w);  
		Lasttrack lastTrack = lasttrackservices.findOne(Long.valueOf(w));
	
		if(lastTrack!=null) {
		Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
	
		JSONObject analogJsonObject = new JSONObject(
				new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));

		JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
	
		JSONArray analogArray2 = new JSONArray(analogJsonObject.get("Digital").toString());
		
		ObjectNode analogNode = mapper.createObjectNode();

		analogArray.forEach(SingleAnalogObject -> {
			JSONObject analogObject = (JSONObject) SingleAnalogObject;
		
			
			analogNode.put("DeviceId",w);
			analogNode.put("DeviceName",device.getDevicename());
			analogNode.put("DeviceDate", lastTrack.getDeviceDate().toString());

			for (int i = 0; i < analogObject.names().length(); i++) {
			
			analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),analogObject.get(analogObject.names().getString(i)).toString() );
			analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"a1",reo.getLasttrackUnitsNew(device.getDp().getPrid(), analogObject.names().getString(i)).toString());
			analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"b1",reo.findByid(new Long(analogObject.names().getString(i))).getId().toString());
						}
		

		});
		analogArray2.forEach(SingleAnalogObject2 -> {
			JSONObject analogObject2 = (JSONObject) SingleAnalogObject2;
		
			

			for (int k = 0; k < analogObject2.names().length(); k++) {
			
				analogNode.put(reo.findByid(new Long(analogObject2.names().getString(k))).getPrmname(),
			    analogObject2.get(analogObject2.names().getString(k)).toString());
			
			}
		

		});
		analogNodearrayNode.add(analogNode);
		
		}
		}
		ObjectNode objectNode = mapper.createObjectNode();
		mapper.setDateFormat(df);
		objectNode.putPOJO("Data", analogNodearrayNode);
		//objectNode.putPOJO("DeviceId", lastTrack.getDeviceId());
		//objectNode.putPOJO("DeviceDate", lastTrack.getDeviceDate());
		//objectNode.putPOJO("DeviceName", device.getDevicename());
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
	}
	
	
	
	
	@RequestMapping(value = "/api/GetDashboard3/{siteid}", produces = { "application/json" })
	public String getcomponetlist3(@PathVariable String siteid)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		
		
		System.out.println(siteid);		
		List<Object[]> deviceList = new ArrayList<>();
		deviceList = Siteservices.GetDeviceIdBySite(Long.valueOf(siteid));

		ObjectMapper mapper = new ObjectMapper();

		ArrayNode analogNodearrayNode = mapper.createArrayNode();
		for(Object[] deviceListresult : deviceList) {
			System.out.println("deviceId:::"+ deviceListresult[0].toString());
		
			Lasttrack lastTrack = lasttrackservices.findOne(Long.valueOf(deviceListresult[0].toString()));
			if(lastTrack!=null) {
			
			Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
			JSONObject analogJsonObject = new JSONObject(
					new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
			
			JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
			JSONArray analogArray2 = new JSONArray(analogJsonObject.get("Digital").toString());
			
			
			ObjectNode analogNode = mapper.createObjectNode();
			analogArray.forEach(SingleAnalogObject -> {
				JSONObject analogObject = (JSONObject) SingleAnalogObject;
			
				
				analogNode.put("DeviceId",deviceListresult[0].toString());
				analogNode.put("DeviceName",device.getDevicename());
				analogNode.put("DeviceDate", lastTrack.getDeviceDate().toString());

				for (int i = 0; i < analogObject.names().length(); i++) {
				
					analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),analogObject.get(analogObject.names().getString(i)).toString());
					analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"1a",reo.getLasttrackUnitsNew(device.getDp().getPrid(), analogObject.names().getString(i)).toString());		
					analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"b1",reo.findByid(new Long(analogObject.names().getString(i))).getId().toString());
				}
				

			});
			analogArray2.forEach(SingleAnalogObject2 -> {
				JSONObject analogObject2 = (JSONObject) SingleAnalogObject2;
			
				

				for (int k = 0; k < analogObject2.names().length(); k++) {
				
					analogNode.put(reo.findByid(new Long(analogObject2.names().getString(k))).getPrmname(),
				    analogObject2.get(analogObject2.names().getString(k)).toString());
				
				}
				});
			analogNodearrayNode.add(analogNode);
		}
	
		}
		
		ObjectNode objectNode = mapper.createObjectNode();
		mapper.setDateFormat(df);
		objectNode.putPOJO("Data", analogNodearrayNode);
	
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
	}
	
	
	@RequestMapping(value = "/api/GetDashboard4/{id}", produces = { "application/json" })
	public String getcomponetlist4(@PathVariable Long id)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		Lasttrack lastTrack = lasttrackservices.findOne(id);
		Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
		JSONObject digitalJsonObject = new JSONObject(
				new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));

		JSONArray digitalArray = new JSONArray(digitalJsonObject.get("Digital").toString());

		ObjectMapper mapper = new ObjectMapper();

		ArrayNode digitalNodearrayNode = mapper.createArrayNode();

		digitalArray.forEach(SingleDigitalObject -> {
			JSONObject digitalObject = (JSONObject) SingleDigitalObject;
			for (int i = 0; i < digitalObject.names().length(); i++) {
				ObjectNode digitalNode = mapper.createObjectNode();
				digitalNode.put(reo.findByid(new Long(digitalObject.names().getString(i))).getPrmname(),digitalObject.get(digitalObject.names().getString(i)).toString());
				digitalNode.put("ParameterId",digitalObject.names().getString(i).toString());
				digitalNodearrayNode.add(digitalNode);
			}
		});

		ObjectNode objectNode = mapper.createObjectNode();
		mapper.setDateFormat(df);
		objectNode.putPOJO("Data", digitalNodearrayNode);
		objectNode.putPOJO("DeviceId", lastTrack.getDeviceId());
		objectNode.putPOJO("DeviceDate", lastTrack.getDeviceDate());
		objectNode.putPOJO("DeviceName", device.getDevicename());
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
	}
	
	@RequestMapping(value = "/api/GetDashboard5/{id}", produces = { "application/json" })
	public String getcomponetlist5(@PathVariable Long id)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		return null;
	}
	
	
	
	
	
	
	
}

package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;   
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.AnalogDataServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.bonrix.dggenraterset.jobs.AnalogAlert;
import com.google.gson.Gson;
/**
 * @author Sajan
 *
 */
@CrossOrigin(origins = "*")
@Transactional
@RestController
public class DigitalController {

	private Logger log = Logger.getLogger(DigitalController.class);

	
	@Autowired
	LasttrackServices lasttrackservices;

	@Autowired
	DevicemasterServices devicemasterservices;
	
	@Autowired
	ParameterRepository reo;

	@Autowired
	DevicemasterRepository deviceReop;
	
	@Autowired
	AnalogDataServices AnalogDataservice;

	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@RequestMapping(value = "/api/GetRadientData/{id}", produces = { "application/json" })
	public String GetRadientData(@PathVariable long id) throws JsonParseException, JsonMappingException, IOException {
		Lasttrack lastTrack = lasttrackservices.findOne(id);
		Gson g = new Gson();
		JSONObject digitalJsonObject = new JSONObject(new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
		System.out.println(digitalJsonObject);		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(digitalJsonObject.toString(), new TypeReference<Map<String, Object>>() {});
		return g.toJson(map);
	}
	
	@RequestMapping(value = "/analogAPI/checkit", produces = { "application/json" })
	public String checkit()
			throws JsonGenerationException, JsonMappingException, IOException {
		  Devicemaster deviceMaste=devicemasterservices.findOne(2795350l);
		Lasttrack old=lasttrackservices.findOne(2795350l);
		   Lasttrack newlt=lasttrackservices.findOne(2795350l);
		   AnalogAlert alert=new AnalogAlert();
		   alert.alertCheck(newlt, old, deviceMaste);
		 return "Hello Success...!";
	}
	
	
	@RequestMapping(value = "/api/GetDigitalDashboard/{id}", produces = { "application/json" })
	public String getcomponetlist(@PathVariable long id)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode analogNodearrayNode = mapper.createArrayNode();
		List<Object[]> listt=  devicemasterservices.getMyDeviced(id);
		//System.out.println("SAJAN :: "+listt.size());
		listt.forEach((Object[] o)->{
			List<Lasttrack> lastTracks = lasttrackservices.findBydeviceId(new Long( o[0].toString()));
			mapper.setDateFormat(df);
				for(Lasttrack lastTrack : lastTracks){
					Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
					JSONObject digitalJsonObject;
						try {
							digitalJsonObject = new JSONObject(new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
							JSONArray analogArray = new JSONArray(digitalJsonObject.get("Digital").toString());
							ObjectNode DIGITALNode = mapper.createObjectNode();
							
							DIGITALNode.putPOJO("Sr no", lastTrack.getDeviceId());
							DIGITALNode.putPOJO("Last Update", lastTrack.getDeviceDate());
							DIGITALNode.putPOJO("Site Name", device.getDevicename());
							analogArray.forEach(SingleAnalogObject -> {
								JSONObject analogObject = (JSONObject) SingleAnalogObject;
								for (int i = 0; i < analogObject.names().length(); i++) {
									ObjectNode analogNode = mapper.createObjectNode();
									analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
											analogObject.get(analogObject.names().getString(i)).toString());
										 if(analogObject.get(analogObject.names().getString(i)).toString().equalsIgnoreCase("1")) {
												DIGITALNode.putPOJO(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
														"<img src='../../img/02.png' class='center' alt='Image Not Found'><center><a target='_blank' href='../report/DigitalParameterRPT.html?paramname=\""+reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"\"&paramId=\""+analogObject.names().getString(i)+"\"' >View</a></center>");
												}else
												{
													DIGITALNode.putPOJO(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
															"<img src='../../img/01.png' class='center' alt='Image Not Found'><center><a target='_blank' href='../report/DigitalParameterRPT.html?paramname=\""+reo.findByid(new Long(analogObject.names().getString(i))).getPrmname()+"\"&paramId=\""+analogObject.names().getString(i)+"\"&deviceId=\""+analogObject.names().getString(i)+"\"' >View</a></center>");
												}
								}
							});
							DIGITALNode.putPOJO("Detais","<a  class='btn btn-success' target='_blank' href='../report/DigitalInfo.html?deviceid=\""+lastTrack.getDeviceId()+"\"  '><i class='fa fa-info-circle' aria-hidden='true'>&nbsp;</i>More Info</a>");
							analogNodearrayNode.add(DIGITALNode);
						} catch (JsonGenerationException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
		});
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
	}

	@RequestMapping(value = "/api/GetDeviceList/{userId}", produces = { "application/json" })
	public String getDeviceList(@PathVariable Long userId)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONArray arry=new JSONArray();
		List<Object[]> listt=  devicemasterservices.getMyDeviced(userId);
		System.out.println("SAJAN :: "+listt.size());
		listt.forEach((Object[] o)->{
			JSONObject obj=new JSONObject();
			obj.put("deviceId", o[0].toString());
			obj.put("deviceName", o[1].toString());
			arry.put(obj);
		});
		 return arry.toString();
	}
	
	@RequestMapping(value = "/api/GetFuelDashboard/{id}", produces = { "application/json" })
	public String GetFuelDashboard(@PathVariable long id)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode analogNodearrayNode = mapper.createArrayNode();
		List<Object[]> listt=  devicemasterservices.getMyDeviced(id);
		log.info("SAJAN :: "+listt.size());
		listt.forEach((Object[] o)->{
			List<Lasttrack> lastTracks = lasttrackservices.findBydeviceId(new Long( o[0].toString()));
			mapper.setDateFormat(df);
				for(Lasttrack lastTrack : lastTracks){
					Devicemaster  device=deviceReop.findBydeviceid(lastTrack.getDeviceId());
					
						try {  
							JSONObject gpsJsonObject = new JSONObject(new ObjectMapper().writeValueAsString(lastTrack.getGpsdata()));
							ObjectNode GPSLNode = mapper.createObjectNode();
							if(gpsJsonObject.length()!=0) {
								Analogdata analog=	AnalogDataservice.findBydevice(device);
								
								log.info(gpsJsonObject.toString());
								log.info(gpsJsonObject.length());
								GPSLNode.putPOJO("Sr no", lastTrack.getDeviceId().toString());
								GPSLNode.putPOJO("Device Name",  device.getDevicename());
								GPSLNode.putPOJO("Update Date", lastTrack.getDeviceDate().toString());
								GPSLNode.putPOJO("Fuel(Voltage)", gpsJsonObject.get("fuel").toString());
								
								if(analog!=null) {
									double fuel=JsonUtills.fuel(analog,Double.parseDouble( gpsJsonObject.get("fuel").toString()) );
									log.info("analog :: "+analog.getAnaloglevel1());
								GPSLNode.putPOJO("Fuel(LTR)",BigDecimal.valueOf( fuel).setScale(3, RoundingMode.HALF_UP));
								}else
									GPSLNode.putPOJO("Fuel(LTR)", "00.00");
							}else {
								GPSLNode.putPOJO("Sr no", lastTrack.getDeviceId().toString());
								GPSLNode.putPOJO("Device Name",  device.getDevicename());
								GPSLNode.putPOJO("Update Date", lastTrack.getDeviceDate().toString());
								GPSLNode.putPOJO("Fuel(Voltage)", "00.00");
								GPSLNode.putPOJO("Fuel(LTR)", "00.00");
							}
						analogNodearrayNode.add(GPSLNode);
						} catch (JsonGenerationException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
		});
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
	}
}

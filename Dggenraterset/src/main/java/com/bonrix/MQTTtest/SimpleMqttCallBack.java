package com.bonrix.MQTTtest;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.JsonMappingException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.bonrix.dggenraterset.jobs.MyAnalogAlert;
import com.fasterxml.jackson.databind.ObjectMapper;



public class SimpleMqttCallBack implements MqttCallback {

	LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext().getBean(LasttrackRepository.class);

	DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext().getBean(DevicemasterRepository.class);

	HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
	
	ParameterRepository parameterrepository = ApplicationContextHolder.getContext().getBean(ParameterRepository.class);
	
	public void connectionLost(Throwable throwable) {
		System.out.println("Connection to MQTT broker lost!");
		if (throwable != null) {
			System.out.println("Reason: " + throwable.getMessage());
			throwable.printStackTrace();
		}
	}

	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		String payload = new String(mqttMessage.getPayload());
		System.out.println("--------------------------------------------------");
		System.out.println("| Message Received from MQTT Broker");
		System.out.println("| Topic: " + topic);
		System.out.println("| Message: " + payload);
		//System.out.println("| QoS: " + mqttMessage.getQos());
		System.out.println("--------------------------------------------------");
		JSONObject incoming = new JSONObject(payload);

        String msg = incoming.getString("msg");
		String[] msgary = msg.split(",");
		String imei=msgary[0].substring(3);
		System.out.println(imei);
		String battary=msgary[10];
		System.out.println(battary);
		String TEMPTURE=msgary[16];
		System.out.println(TEMPTURE);
		String ODOMETER=msgary[18];
		System.out.println(ODOMETER);
		String SPEED=msgary[19];
		System.out.println(SPEED);
		String FUEL=msgary[23];
		System.out.println(FUEL);
		String SITE_BATTERY_VOLTAGE_DC=msgary[35];
		System.out.println(SITE_BATTERY_VOLTAGE_DC);
		String Rectifier_Power=msgary[39];
		System.out.println(Rectifier_Power);
		
		String digitalData=msgary[14].substring(1);;
		System.out.println(digitalData);
		
		Devicemaster device = devicemasterRepository.findByImei(imei);
		Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());
		
		//System.out.println(imei);
		 DeviceProfile profile=device.getDp();
		 JSONObject parameters = new JSONObject(profile.getParameters());
	      JSONArray digital = parameters.getJSONArray("Digital");
	      JSONArray analog =  parameters.getJSONArray("Analog");
	      JSONArray rs232 = parameters.getJSONArray("Analog");
	      JSONObject rs232obj = new JSONObject();
	      for (int i = 0; i < rs232.length(); i++) {
				JSONObject obj = (JSONObject) rs232.get(i);
				Double d = Double.parseDouble(obj.get("analogioindex").toString());
				rs232obj.put(obj.get("Analoginput").toString(),
				Double.valueOf((Integer.parseInt(msgary[d.intValue()], 16) ) *  Double.parseDouble(obj.get("Analogformula").toString())));
	
			}
		System.out.println(rs232obj);
		String ACMAINS_FAIL = digitalData.substring(0, 1);
		String Battry_Low = digitalData.substring(3, 4);
		JSONObject digiobj = new JSONObject();
		for (int i = 0; i < digital.length(); i++) {

			JSONObject obj = (JSONObject) digital.get(i);
			Double d = Double.parseDouble(obj.get("dioindex").toString());
			Parameter param = parameterrepository.findByid(new Long(obj.get("parameterId").toString()));
			boolean reverse = (Boolean) obj.get("reverse");

			if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("ACMAINS_FAIL")) {
				if (reverse == true) {
					digiobj.put(param.getId().toString(), "1".equals(ACMAINS_FAIL) ? "0" : "1");
				} else {
					digiobj.put(param.getId().toString(), ACMAINS_FAIL);
				}
			}
			if (param.getPrmname().replace(" ", "_").equalsIgnoreCase("Battery_Low")) {
				if (reverse == true) {
					digiobj.put(param.getId().toString(), "1".equals(Battry_Low) ? "0" : "1");
				} else {
					digiobj.put(param.getId().toString(), Battry_Low);
				}
			}
		}

		JSONArray AnalogJsonArray = new JSONArray();
		JSONArray DigitalJsonArray = new JSONArray();
		AnalogJsonArray.put(rs232obj);
		DigitalJsonArray.put(digiobj);
		JSONObject jo = new JSONObject();
		jo.put("Digital", DigitalJsonArray);
		jo.put("Analog", AnalogJsonArray);
		jo.put("DeviceName", device.getDevicename());

		// log.info(AnalogJsonArray.toString());
		Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
				new ObjectMapper().readValue(jo.toString(), Map.class),
				new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
				new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

		History hist = new History(device.getDeviceid(), device.getUserId(), new Date(), new Date(),
				new ObjectMapper().readValue(jo.toString(), Map.class),
				new ObjectMapper().readValue(new JSONObject().toString(), Map.class),
				new ObjectMapper().readValue(convertJson(jo.toString()).toString(), Map.class));

		History hst = histroyrepository.saveAndFlush(hist);
		Lasttrack t = lasttrackrepository.saveAndFlush(lTrack);
		
		MyAlerts alert=new MyAlerts();
		alert.sendMsg(device,lTrack,track);
		MyAnalogAlert analogAlert=new MyAnalogAlert();
		analogAlert.sendAnalogAlert(device, lTrack);

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
	
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		try {
			System.out.println("Message delivery complete. Token: " + iMqttDeliveryToken.getMessageId());
		} catch (Exception e) {
			System.out.println("Error in delivery callback: " + e.getMessage());
		}
	}
}
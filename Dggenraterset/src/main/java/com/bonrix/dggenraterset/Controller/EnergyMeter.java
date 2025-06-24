package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class EnergyMeter {
	
	@Autowired 
	LasttrackServices lasttrackservices;
	
	@Autowired 
	DevicemasterServices devicemasterservices;
	
	@Autowired 
	ParameterServices parameterservices;
	
	
	
	
	@RequestMapping(value="/api/GetEnergyMeterDashboard/{id}" ,produces={"application/json"})
	public String getEnergyMeterlist(@PathVariable Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		List<Lasttrack> lastTrack=null;
		lastTrack=lasttrackservices.findBydeviceId(id);
		JSONObject extdata = new JSONObject();

		Lasttrack track=lastTrack.get(0);
		if(track!=null) {
			if( JsonUtills.mapToJson(track.getAnalogdigidata()).getJSONArray("Rs232").length()!=0) {
        JSONObject object = JsonUtills.mapToJson(track.getAnalogdigidata()).getJSONArray("Rs232").getJSONObject(0);
        System.out.println("object :: "+object);
        JSONObject param = new JSONObject();
        Iterator iter = object.keys();
		    while(iter.hasNext()){
		        String key = (String)iter.next();
		        String value = object.getString(key);
		        com.bonrix.dggenraterset.Model.Parameter parameter=parameterservices.get(new Long(key));
		        param.put(parameter.getPrmname(), value);
		    }
		    extdata.put("DeviceDate",track.getDeviceDate());
		    extdata.put("SystemDate",  track.getSystemDate());
		    extdata.put("DeviceName",  JsonUtills.mapToJson(track.getAnalogdigidata()).get("DeviceName"));
		    extdata.put("METER_ID",  JsonUtills.mapToJson(track.getAnalogdigidata()).get("METER_ID"));  
		    extdata.put("Status", "SUCCESS");
		    extdata.put("RS323", param);
		}
		else
			  extdata.put("Status", "FAIL");
		}
		return extdata.toString();	
	}
}

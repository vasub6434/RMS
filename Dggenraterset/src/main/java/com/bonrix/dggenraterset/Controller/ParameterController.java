package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Utility.JsonUtills;

import org.json.JSONArray;
import org.json.JSONObject;

@CrossOrigin(origins = "*")
@Transactional
@RestController

public class ParameterController {

	@Autowired
	ParameterServices Parameterservices;

	@Autowired
	ApiService apiService;
	
	@Autowired
	SiteServices Siteservices;

	@RequestMapping(method = RequestMethod.POST, value = "/user/parameter")
	@ExceptionHandler(SpringException.class)
	public String savedata(@RequestBody Parameter bs) {
		Parameterservices.save(bs);
		return new SpringException(true, "Parameter Sucessfully Added").toString();
	}

	@RequestMapping(value = "/user/saveParameter/{pname}/{type}/{key}", produces = { "application/json" })
	public String saveParameter(@PathVariable String pname, @PathVariable String type, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			Parameter pm = new Parameter();
			pm.setPrmname(pname);
			pm.setPrmtype(type);
			Parameterservices.save(pm);
			return new SpringException(true, "Parameter Sucessfully Added").toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(value = "/user/parameterlist/{key}", produces = { "application/json" })
	public String getparameterlist(HttpServletRequest request, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			return JsonUtills.ListToJava(Parameterservices.getlist());
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/user/parameter/{id}", method = RequestMethod.GET)
	public Parameter getparameter(@PathVariable long id) {
		return Parameterservices.get(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "user/updateparameter/{id}/{parameterName}/{parametertype}/{key}")
	public String updateparameter(HttpServletRequest request, @PathVariable long id, @PathVariable String parameterName,
			@PathVariable String parametertype, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			Parameter pm = new Parameter();
			pm.setId(id);
			pm.setPrmname(parameterName);
			pm.setPrmtype(parametertype);
			Parameterservices.save(pm);
			return new SpringException(true, "Parameter Sucessfully Updated").toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/parameter/{id}")
	public String deleteparameter(@PathVariable long id) {
		return Parameterservices.delete(id);
	}

	@RequestMapping(value = "/user/parameters/{prmtype}", produces = { "application/json" }, method = RequestMethod.GET)
	public List<Parameter> getparameterlistByprmtype(@PathVariable String prmtype) {
		return Parameterservices.getlistByprmtype(prmtype);
	}
	
	
	@RequestMapping(value = "/api/getLastTrackBySite/{siteId}/{key}", produces = { "application/json" })
	public String getLastTrackBySite(HttpServletRequest request,@PathVariable long siteId,@PathVariable String key) {
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> deviceList = new ArrayList<>();
			deviceList = Siteservices.GetDeviceIdBySite(siteId);
			
			 JSONObject mainJson = new JSONObject();
			  JSONArray DataArray = new JSONArray();
			  JSONArray ColumnsArray = new JSONArray();
			  
			  JSONArray Auto_ColumnsArray = new JSONArray();
			  Auto_ColumnsArray.put("#");
			
			  JSONArray DeviceName_ColumnsArray = new JSONArray();
			  DeviceName_ColumnsArray.put("Site Name");
			  
			  JSONArray History_ColumnsArray = new JSONArray();
			  History_ColumnsArray.put("Action");
			  
			  JSONArray DeviceDate_ColumnsArray = new JSONArray();
			  DeviceDate_ColumnsArray.put("Device Date");
			  
	      	  ColumnsArray.put(Auto_ColumnsArray);
	      	  ColumnsArray.put(DeviceName_ColumnsArray);
	   //   	  ColumnsArray.put(SyatemDate_ColumnsArray);
	      	  ColumnsArray.put(DeviceDate_ColumnsArray);
	      	
	      	  String analogAdd="0";
			  String digitalAdd="0";
	      	  int auto_num = 1;
			for(Object[] deviceListresult : deviceList) {
				System.out.println("deviceId:::"+ deviceListresult[0].toString());
				long deviceId = Long.parseLong(deviceListresult[0].toString());
				List<Object[]> deviceProfileList = new ArrayList<>();
				deviceProfileList = Parameterservices.getDeviceProfileByDeviceId(deviceId);
				long prof_Id=0L;
				for(Object[] prof_result : deviceProfileList) {
					prof_Id=Long.parseLong(prof_result[1].toString());
				}
				List<Object[]> list = new ArrayList<>();
				list = Parameterservices.getLasttrackByDeviceId(Long.parseLong(deviceListresult[0].toString()));
				for(Object[] result : list) {
					 String DeviceName=result[3].toString();
					 JSONArray Inner_DataArray = new JSONArray();
					 JSONObject resultJson = new JSONObject(result[2].toString());
					 JSONArray Analog_Array = resultJson.getJSONArray("Analog");
					 JSONArray Digital_Array = resultJson.getJSONArray("Digital");
					 Inner_DataArray.put(auto_num);
					 Inner_DataArray.put(result[3].toString());
					 Inner_DataArray.put(result[0].toString());
					 for (int j = 0; j < Analog_Array.length(); j++) {
				            JSONObject newobj = Analog_Array.getJSONObject(j);
				            String k = newobj.keys().next();
				            JSONObject param_obj = new JSONObject(Parameterservices.get(Long.parseLong(k)));
				            param_obj.getString("prmname");
				            String unitlist = Parameterservices.getLasttrackUnits(prof_Id,k);
				  //          Inner_DataArray.put(newobj.getString(k));
				            Inner_DataArray.put("<span class='span6 pull-right' style='text-align:right'>"+newobj.getString(k)+"</span>");
				            JSONArray Inner_ColumnsArray = new JSONArray();
				            if(ColumnsArray.toString().contains(param_obj.getString("prmname")) == false) {
				            	Inner_ColumnsArray.put(param_obj.getString("prmname")+" "+"("+unitlist+")");
				            	ColumnsArray.put(Inner_ColumnsArray);
				            }else {
				            	analogAdd="1";
				            }
				        }
					 for (int s = 0; s < Digital_Array.length(); s++) {
						 JSONObject digitalnewobj = Digital_Array.getJSONObject(s);
						 String digitalkey = digitalnewobj.keys().next();
						 JSONObject dig_param_obj = new JSONObject(Parameterservices.get(Long.parseLong(digitalkey)));
						 dig_param_obj.getString("prmname");
						 if(digitalnewobj.getString(digitalkey).equalsIgnoreCase("0")) {
							 Inner_DataArray.put("<button type='button' class='btn btn-danger btn-xs'>OFF</button>");
						 }else {
							 Inner_DataArray.put("<button type='button' class='btn btn-success btn-xs'>ON</button>");
						 }
						 JSONArray Dig_Inner_ColumnsArray = new JSONArray();
						 if(ColumnsArray.toString().contains(dig_param_obj.getString("prmname")) == false) {
							 Dig_Inner_ColumnsArray.put(dig_param_obj.getString("prmname"));
						 	 ColumnsArray.put(Dig_Inner_ColumnsArray);
						 }else {
							 digitalAdd="1";
			            }
					 }
					 Inner_DataArray.put("<button type=button class='btn btn-primary btn-sm' onclick=devicehistory("+deviceId+",\""+DeviceName+"\")>History</button>  <button type='button' id='btn2' class='btn btn-primary btn-sm' onclick='deviceinfo("+deviceId+")' ><i class='fa fa-briefcase' aria-hidden='true'></i>&nbsp;Device info.</button>");
					 DataArray.put(Inner_DataArray);
					 auto_num++;
				}
			}
			 ColumnsArray.put(History_ColumnsArray);
			 mainJson.put("data", DataArray);
			 mainJson.put("columns", ColumnsArray);
			 System.out.println("Main_Json::"+mainJson.toString());
			 return mainJson.toString(); 
		}else{
		  return new SpringException(false, "Invalid Key").toString();
		}
		
	}
}

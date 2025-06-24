package com.bonrix.dggenraterset.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.Dashboarddetails;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Service.AnalogDataServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class AnalogDataController {
	@Autowired
	AnalogDataServices AnalogDataservice;
	@Autowired
	DevicemasterServices Deviceinfoservices;
	@Autowired
	ParameterServices Parameterservices;

	@RequestMapping(method = RequestMethod.POST, value = "/api/analogdata/{did}/{managerid}/{key}")
	@ExceptionHandler({ SpringException.class })
	public String savedata(@RequestBody Analogdata bs, @PathVariable("did") Long did,
			@PathVariable("managerid") long managerid, @PathVariable("key") String key) {
		bs.setDevice(Deviceinfoservices.findOne(did));
		bs.setManagerid(managerid);
		AnalogDataservice.save(bs);
		return new SpringException(true, "Analog Data Sucessfully Added").toString();
	}

	@RequestMapping(value = "/api/analogdata", produces = { "application/json" })
	public String getcomponetlist() throws JsonProcessingException {
		JSONArray arr_strJson = new JSONArray(AnalogDataservice.getlist());
		System.out.println(arr_strJson.toString());
		JSONObject tcallerWbJsom = new JSONObject();
		tcallerWbJsom.put("data", arr_strJson);
		return tcallerWbJsom.toString();
	}

	@RequestMapping(value = "/api/analogdata/{id}", method = RequestMethod.GET)
	public Analogdata getcomponet(@PathVariable int id) {
		return AnalogDataservice.get(id);
	}

	// set Content-Type=application/json ANd set raw=raw={"macaddress":"asuiags",
	// "scannerName":"asuiags","latlang":"asuiags", "cordinate":"asuiags" }(here put
	// key and value and pass arry of it)
	@RequestMapping(method = RequestMethod.PUT, value = "/api/analogdata/{id}")
	public String updatecomponet(@RequestBody Analogdata bs, @PathVariable int id) {
		bs.setId(id);
		return AnalogDataservice.update(bs);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/api/analogdata/{id}/{key}")
	public String deletecomponet(@PathVariable int id, @PathVariable String key) {
		return AnalogDataservice.delete(id);
	}

	@RequestMapping(value = "/api/AnalogDashboard", produces = { "application/json" })
	public List<Dashboarddetails> AnalogDashboard(Authentication auth) {
		BonrixUser currentUser = (BonrixUser) auth.getPrincipal();
		Set<UserRole> liss = currentUser.getUserRole();
		System.out.println(liss);
		System.out.println(currentUser.getUsername());// currentUser.getUserId()
		return null;
	}

	@RequestMapping(value = "/api/digitallist", produces = { "application/json" })
	public String getDigitaldatalist() {
		List<Object[]> list = new ArrayList<>();
		list = AnalogDataservice.getDigitaldatalist();
		JSONArray jarray = new JSONArray();

		String id = "";
		String prmname = "";
		String prmtype = "";
		for (Object[] result1 : list) {
			id = result1[0].toString();
			prmname = result1[1].toString();
			prmtype = result1[2].toString();

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("prmname", prmname);
			jo.put("prmtype", prmtype);

			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/analoglist", produces = { "application/json" })
	public String getAnalogdatalist() {
		List<Object[]> list = new ArrayList<>();
		list = AnalogDataservice.getAnalogdatalist();
		JSONArray jarray = new JSONArray();

		String id = "";
		String prmname = "";
		String prmtype = "";
		for (Object[] result1 : list) {
			id = result1[0].toString();
			prmname = result1[1].toString();
			prmtype = result1[2].toString();

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("prmname", prmname);
			jo.put("prmtype", prmtype);

			jarray.put(jo);
		}
		return jarray.toString();
	}

}

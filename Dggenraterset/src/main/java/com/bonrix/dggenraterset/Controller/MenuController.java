package com.bonrix.dggenraterset.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.MenuAllocationMst;
import com.bonrix.dggenraterset.Model.MenuMst;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.MenuService;
import com.bonrix.dggenraterset.Service.MenuallocationmService;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class MenuController {
	
	@Autowired
	MenuService menuServide;
	
	@Autowired
	 MenuallocationmService alloService;
	
	@RequestMapping(value = "/api/getmenu/{userid}", produces = { "application/json" }, method = RequestMethod.GET)
	public String getmenu(@PathVariable Long userid) {
		List<Object[]> list = menuServide.getMenu(userid);

		JSONArray jarray = new JSONArray();
		System.out.println("list : "+list.size());
		if (!list.isEmpty()) {
			for (Object[] result1 : list) {
						JSONObject jo = new JSONObject();
						jo.put("id", result1[0].toString());
						jo.put("menuname", result1[1].toString());
						jo.put("url", result1[2].toString());
						jo.put("allocation", result1[3].toString());
						jo.put("userid", result1[4]==null?0:result1[4].toString());
						jarray.put(jo);
			}
		}
		return jarray.toString();
	}
	
	@RequestMapping(value = "/api/allocatemenu", produces = { "application/json" }, method = RequestMethod.POST)
	public String allocatemenu(@RequestParam(value="userid") Long userid,
			@RequestParam(value="allocated[]") Integer[] allocated) {
		
		System.out.println(userid);
		System.out.println(allocated[0]);

		menuServide.deleteMenu(userid);
		
		for(int i=0;i<allocated.length;i++)
		{
			if(allocated[i]!=0)
			{
			MenuAllocationMst mst=new MenuAllocationMst();
			mst.setManagerId(userid);
			mst.setMid(Long.valueOf(""+allocated[i]));
			mst.setUserId(0L);
			alloService.newMenu(mst);
			}
		}
		return new SpringException(true, "Menu Succssfully Allocated.!").toString();
	}
	
	
	@RequestMapping(value = "/api/getUsermenu/{userid}", produces = { "application/json" }, method = RequestMethod.GET)
	public String getUsermenu(@PathVariable Long userid) {
		List<Object[]> list = menuServide.getUsermenu(userid);
		JSONArray jarray = new JSONArray();
		if (!list.isEmpty()) {
			for (Object[] result1 : list) {
						Map<String, String> jsonOrderedMap  = new LinkedHashMap<>();
						jsonOrderedMap.put("id", result1[0].toString());
						jsonOrderedMap.put("menuname", result1[1].toString());
						jsonOrderedMap.put("url", result1[2].toString());
						jsonOrderedMap.put("allocation", result1[3].toString());
						jsonOrderedMap.put("userid", result1[4]==null?"0":result1[4].toString());
						jarray.put(jsonOrderedMap);
			}
		}
		return jarray.toString();
	}
	
	@RequestMapping(value = "/api/allocateUsermenu", produces = { "application/json" }, method = RequestMethod.POST)
	public String allocateUsermenu(@RequestParam(value="userid") Long userid,
			@RequestParam(value="allocated[]") Integer[] allocated) {
		
		System.out.println(userid);
		System.out.println(allocated[0]);

		menuServide.deleteUserMenu(userid);
		
		for(int i=0;i<allocated.length;i++)
		{
			if(allocated[i]!=0)
			{
			MenuAllocationMst mst=new MenuAllocationMst();
			mst.setManagerId(0L);
			mst.setMid(Long.valueOf(""+allocated[i]));
			mst.setUserId(userid);
			alloService.newMenu(mst);
			}
		}
		return new SpringException(true, "Menu Succssfully Allocated.!").toString();
	}
	
	@RequestMapping(value = "/api/getDrawmenu/{userid}/{role}", produces = { "application/json" }, method = RequestMethod.GET)
	public String getDrawmenu(@PathVariable Long userid,@PathVariable String role) {
		List<Object[]> list =null;
		if (role.equalsIgnoreCase("ROLE_USER"))
			list = menuServide.getUsermenu(userid);
		else if (role.equalsIgnoreCase("ROLE_MANAGER"))
			list = menuServide.getMenu(userid);

		JSONArray jarray = new JSONArray();
		System.out.println("list : "+list.size());
		if (!list.isEmpty()) {
			for (Object[] result1 : list) {  
				if(result1[4]!=null)
				{
					JSONObject jo = new JSONObject();
					Map<String, String> jsonOrderedMap  = new LinkedHashMap<>();
					jsonOrderedMap.put("id", result1[0].toString());
					jsonOrderedMap.put("menuname", result1[1].toString());
					jsonOrderedMap.put("url", result1[2].toString());
					jsonOrderedMap.put("allocation", result1[3].toString());
					jsonOrderedMap.put("userid", result1[4].toString());
					jarray.put(jsonOrderedMap);
				}
						
			}
		}
		return jarray.toString();
	}
	
	
}

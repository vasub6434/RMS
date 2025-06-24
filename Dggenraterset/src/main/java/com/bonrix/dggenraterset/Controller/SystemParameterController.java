package com.bonrix.dggenraterset.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.DisplaySetting;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.SystemParameter;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DisplaySettingService;
import com.bonrix.dggenraterset.Service.SystemParameterServices;
import com.bonrix.dggenraterset.Service.UserService;
import com.bonrix.dggenraterset.Utility.SecurityConfig;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class SystemParameterController {

	
	
	@Autowired
	SystemParameterServices SysParameterservices;
	
	@Autowired
	DisplaySettingService displaysettingservice;
	
	@Autowired
	ApiService apiService;
	
	@Autowired
	UserService userService;
	
	String httpResponse = "";
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/api/sysparameter/{prmname}/{prmvalue}/{prmnotes}")
	@ExceptionHandler(SpringException.class)
	public String savesysdata(@PathVariable String prmname,@PathVariable String prmvalue,@PathVariable String prmnotes) throws UnsupportedEncodingException {
		System.out.println("sysparameter_save");
		String prm =URLDecoder.decode(prmvalue, "UTF-8");
		System.out.println("prmvalue::"+prmvalue+" "+"prm::"+prm);
		
		/*Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(prmvalue);
		String decodedString = new String(decodedByte);
		System.out.println("decodedString::"+decodedString);*/
		
		SystemParameter bs = new SystemParameter();
		
		bs.setPrmname(prmname);
		bs.setPrmvalue(prm);
		bs.setPrmnotes(prmnotes);
		bs.setAddedon(new Date());
		
		SysParameterservices.save(bs);
		return new SpringException(true, "Parameter Sucessfully Added").toString();
		
	}
	
	@RequestMapping(value="/api/sysparameter" ,produces={"application/json"})
	public List<SystemParameter> getsysparameterlist()
	{
		return SysParameterservices.getlist();
	}
	
	@RequestMapping(value="/api/sysparameter/{id}",method=RequestMethod.GET)
	public SystemParameter getsysparameter(@PathVariable long id) {
		return SysParameterservices.get(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/updatesysparameter/{id}/{prmname}/{prmvalue}/{prmnotes}")
	@ExceptionHandler(SpringException.class)
	public String updatesysparameter(@PathVariable long id,@PathVariable String prmname,@PathVariable String prmvalue,@PathVariable String prmnotes) {
		System.out.println("jsxkaxbkz");
		SystemParameter bs = new SystemParameter();
		
		bs.setId(id);
		bs.setPrmname(prmname);
		bs.setPrmnotes(prmnotes);
		bs.setPrmvalue(prmvalue);
		bs.setAddedon(new Date());
		
		SysParameterservices.save(bs);
		return new SpringException(true, "Parameter Sucessfully Added").toString();
		
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/api/delsysparameter/{id}")
	public String deletesysparameter(@PathVariable long id)
	{
		return SysParameterservices.delete(id);
	}
	
	@RequestMapping(value = "/api/getDisplaySettingList/{hostName}", produces = { "application/json" })
	public String getDisplaySettingList(HttpServletRequest request,@PathVariable String hostName) {

			List<Object[]> list = new ArrayList<>();
			list = displaysettingservice.DisplaySettingList(hostName);
			JSONArray jarray = new JSONArray();
			for (Object[] result1 : list) {
				JSONObject jo = new JSONObject();
				jo.put("id", result1[0].toString());
				jo.put("aboutus", result1[1].toString());
				jo.put("address", result1[2].toString());
				jo.put("extraInfo1", result1[3].toString());
				jo.put("extraInfo2", result1[4].toString());
				jo.put("homeurl", result1[5].toString());
				jo.put("image2", result1[6].toString());
				jo.put("logoImage", result1[7].toString());
				jo.put("subTitle", result1[8].toString());
				jo.put("title", result1[9].toString());
				jo.put("url", result1[10].toString());
				jo.put("userName", result1[11].toString());
				jarray.put(jo);
			}
			return jarray.toString();
	}
	
	
	@RequestMapping(value = "/api/getAdminDisplaySettingList/{key}", produces = { "application/json" })
	public String getAdminDisplaySettingList(HttpServletRequest request,@PathVariable String key) {
		
		Apikey api=apiService.findBykeyValue(key);
		if(api!=null)
		{
			List<Object[]> list = new ArrayList<>();
			list = displaysettingservice.AdminDisplaySettingList();
			JSONArray jarray = new JSONArray();
			for (Object[] result1 : list) {
				JSONObject jo = new JSONObject();
				jo.put("id", result1[0].toString());
				jo.put("aboutus", result1[1].toString());
				jo.put("address", result1[2].toString());
				jo.put("extraInfo1", result1[3].toString());
				jo.put("extraInfo2", result1[4].toString());
				jo.put("homeurl", result1[5].toString());
				jo.put("image2", result1[6].toString());
				jo.put("logoImage", result1[7].toString());
				jo.put("subTitle", result1[8].toString());
				jo.put("title", result1[9].toString());
				jo.put("url", result1[10].toString());
				jo.put("userName", result1[11].toString());
				jarray.put(jo);
			}
			return jarray.toString();
		}else {
			 return new SpringException(false, "Invalid Key").toString();
		 }
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/user/saveDisplaySetting/{key}")
	@ExceptionHandler(SpringException.class)
	public String saveDisplaySetting(@RequestBody DisplaySetting dsetting,@PathVariable String key) {
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			 displaysettingservice.save(dsetting);
			 return new SpringException(true, "Setting Sucessfully Added").toString();
		}else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/delSetting/{id}/{key}")
	public String delSetting(@PathVariable Long id,@PathVariable String key) {
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			displaysettingservice.delete(id);
			 return new SpringException(true, "Setting Successfully Deleted.").toString();
		}else
			return new SpringException(false, "Invalid Key").toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/forgetPassword/{userName}/{mobileNum}")
	public String forgetPassword(@PathVariable String userName,@PathVariable String mobileNum) throws IOException {
		
		List<Object[]> list = displaysettingservice.getUserBymobile(userName, mobileNum);
		if (list.size() == 0) {
			return "0";
		}
		
		String pass = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
		String enctpass = new SecurityConfig().passwordEncoder().encode(pass);
		String msg="Your New Password is  "+ pass;
		int value = 1;
	    msg = URLEncoder.encode(msg, "UTF-8");
	    String username = "http://topsms.highspeedsms.com/sendsms.aspx?mobile=8155045500&pass=bonrixgps&senderid=BONRIX&to=<mobile_number>&msg=<message>";
	    System.out.println("dypara::" + username);
	    
	    username = username.replaceAll("\\<mobile_number\\>", mobileNum.trim());
	    username = username.replaceAll("\\<message\\>", msg.trim());
	    
	    System.out.println("COMPLETE URL ::" + username);
	    int n = 0;
	    String smsURL = "";
	    String token = "";
	    URL sms = new URL(username);
	    HttpURLConnection httpConn = (HttpURLConnection)sms.openConnection();
	    try
	    {
	      BufferedReader httpReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
	      httpResponse = "";
	      String line = "";
	      while ((line = httpReader.readLine()) != null)
	      {
	        httpResponse = line;
	        System.out.println("Conformation From SMS Provider: :" + httpResponse);
	        if (httpResponse.equalsIgnoreCase("Message dropped but Not Sent".trim())) {
	          value = 0;
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      value = 0;
	    }
	    for (Object[] result : list) {
	    	User usr=new User();
	    	usr.setName(result[5].toString());
	    	usr.setAddedby(Long.parseLong(result[7].toString()));
	    	usr.setContact(result[6].toString());
	    	usr.setEmail(result[4].toString());
	    	usr.setUsername(userName);
	    	usr.setPassword(enctpass.toString());
	    	usr.setId(Long.parseLong(result[0].toString()));
	    	usr.setEnabled(Boolean.parseBoolean(result[1].toString()));
	    	userService.update(usr);
	    }
	    return new SpringException(true, "Success").toString();
	}
}

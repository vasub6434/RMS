package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DashBoardLayoutServices;
import com.bonrix.dggenraterset.Service.DeviceFailureNoticeService;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DynamicDashBoardLayoutService;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.LoginService;
import com.bonrix.dggenraterset.Service.ParameterServices;
import com.bonrix.dggenraterset.Service.ProfileAnalogSettingService;
import com.bonrix.dggenraterset.Service.ProfileDigitalSettingService;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Utility.JsonUtills;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class APIController {

	@Autowired
	ApiService apiService;

	@Autowired
	DevicemasterServices devicemasterservices;

	@Autowired
	DeviceProfileServices profileService;

	@Autowired
	HistoryServices historyservices;

	@Autowired
	AssignSiteService assignsiteservices;
	@Autowired
	LasttrackServices lasttrackservices;

	@Autowired
	DeviceFailureNoticeService Dfnservices;

	@Autowired
	ProfileDigitalSettingService profileDigitalService;

	@Autowired
	ProfileAnalogSettingService profileAnalogService;

	@Autowired
	LoginService loginService;

	@Autowired
	DevicemasterRepository deviceReop;

	@Autowired
	ParameterRepository parameterRepo;

	@Autowired
	ParameterRepository reo;

	@Autowired
	SiteServices Siteservices;

	@Autowired
	DashBoardLayoutServices dLServices;

	@Autowired
	DynamicDashBoardLayoutService dynamicdashlayout;

	@Autowired
	SiteServices siteService;

	@Autowired
	ParameterServices parameterService;

	@Autowired
	DevicemasterServices DeviceMasterservices;

	private Logger log = Logger.getLogger(APIController.class);
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	static final DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");
// 161.97.65.187:8080/android/ANDROUserLogin/vcon/123456
	@RequestMapping(value = "/android/ANDROUserLogin/{username}/{password}", produces = { "application/json" })
	public String getcomponetlist(HttpServletRequest request, @PathVariable String username,
			@PathVariable String password) throws JsonGenerationException, JsonMappingException, IOException {
		User user = loginService.Login(username);
		log.info("BONRIX LOGIN API  :: " + username);
		if (user != null) {
			JSONObject jo = new JSONObject();
			JSONArray jarray = new JSONArray();

			HttpSession session = request.getSession();
			Apikey api = new Apikey();
			UUID key = UUID.randomUUID();
			for (UserRole s : user.getUserRole()) {
				if (BCrypt.checkpw(password, user.getPassword())) {
					api = apiService.findByuid(user.getId());
					if (api == null) {
						api = new Apikey();
						api.setCreateDate(new Date());
						api.setKeyValue(key.toString());
						session.setAttribute("LoginKEY", key);
						api.setUid(user.getId());
						apiService.saveObject(api);
						jo.put("api", api.getKeyValue());
					} else {
						api.setCreateDate(new Date());
						api.setKeyValue(key.toString());
						session.setAttribute("LoginKEY", key);
						apiService.saveObject(api);
						jo.put("api", api.getKeyValue());
					}
					jo.put("responseCode", "SUCCESS");
					jo.put("uId", user.getId());
					jo.put("role", s.getRole());
					jo.put("username", user.getUsername());
					jo.put("mobileno", user.getContact());
					jo.put("email", user.getEmail());
					jarray.put(jo);
				} else {
					jo.put("responseCode", "FAIL");
					jarray.put(jo);
				}
			}
			return "{\"Status\":\"Success\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Invalid Credentials...!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
	}

	@RequestMapping(value = "/android/GetAnalogDashboard/{id}/{apiKey}", produces = { "application/json" })
	public String GetAnalogDashboard(@PathVariable Long id, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			Lasttrack lastTrack = lasttrackservices.findOne(id);
			Devicemaster device = deviceReop.findBydeviceid(lastTrack.getDeviceId());
			JSONObject analogJsonObject = new JSONObject(
					new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));

			JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
			ObjectMapper mapper = new ObjectMapper();

			ArrayNode analogNodearrayNode = mapper.createArrayNode();
			mapper.setDateFormat(df);
			analogArray.forEach(SingleAnalogObject -> {
				JSONObject analogObject = (JSONObject) SingleAnalogObject;
				for (int i = 0; i < analogObject.names().length(); i++) {
					ObjectNode analogNode = mapper.createObjectNode();
					analogNode.put(parameterRepo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
							analogObject.get(analogObject.names().getString(i)).toString());
					analogNode.put("Unit",
							parameterRepo
									.getLasttrackUnitsNew(device.getDp().getPrid(), analogObject.names().getString(i))
									.toString());
					analogNode.put("ParameterId", analogObject.names().getString(i).toString());
					analogNode.put("DeviceId", lastTrack.getDeviceId());
					analogNode.put("DeviceDate", lastTrack.getDeviceDate().toString());
					analogNode.put("DeviceName", device.getDevicename());
					analogNodearrayNode.add(analogNode);
				}
			});
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + analogNodearrayNode.toString()
					+ "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
	}

	@RequestMapping(value = "/android/GetAndroidDigitalDashboard/{id}/{apiKey}", produces = { "application/json" })
	public String GetAndroidDigitalDashboard(@PathVariable long id, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode analogNodearrayNode = mapper.createArrayNode();
			List<Object[]> listt = devicemasterservices.getMyDeviced(id);
			listt.forEach((Object[] o) -> {
				List<Lasttrack> lastTracks = lasttrackservices.findBydeviceId(new Long(o[0].toString()));
				mapper.setDateFormat(df);
				for (Lasttrack lastTrack : lastTracks) {
					Devicemaster device = deviceReop.findBydeviceid(lastTrack.getDeviceId());
					JSONObject digitalJsonObject;
					try {
						digitalJsonObject = new JSONObject(
								new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
						JSONArray analogArray = new JSONArray(digitalJsonObject.get("Digital").toString());
						ObjectNode DIGITALNode = mapper.createObjectNode();
						ObjectNode dynamicNodearrayNode = mapper.createObjectNode();

						mapper.setDateFormat(df);
						ObjectNode analogNode = mapper.createObjectNode();

						DIGITALNode.put("deviceId", lastTrack.getDeviceId());
						DIGITALNode.put("LastUpdate", lastTrack.getDeviceDate().toString());
						DIGITALNode.put("SiteName", device.getDevicename());
						analogArray.forEach(SingleAnalogObject -> {
							JSONObject analogObject = (JSONObject) SingleAnalogObject;
							for (int i = 0; i < analogObject.names().length(); i++) {
								analogNode.put(
										parameterRepo.findByid(new Long(analogObject.names().getString(i)))
												.getPrmname(),
										analogObject.get(analogObject.names().getString(i)).toString());
								if (analogObject.get(analogObject.names().getString(i)).toString()
										.equalsIgnoreCase("1"))
									dynamicNodearrayNode.put(parameterRepo.findByid(new Long(analogObject.names().getString(i)))
											.getPrmname(), "ON");
								else
									dynamicNodearrayNode.put(parameterRepo.findByid(new Long(analogObject.names().getString(i)))
											.getPrmname(), "OFF");
							}
						});
						DIGITALNode.put("DynamicDate", dynamicNodearrayNode);
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
			return "{\"Status\":\"True\",\"Message\":\"Data Retrieved.!\",\"Data\":" + analogNodearrayNode.toString()
					+ "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
	}

	@RequestMapping(value = {
			"/android/GetAndroidDigitalHistoryData/{deviceId}/{startdate}/{enddate}/{apiKey}" }, produces = {
					"application/json" }, method = { RequestMethod.GET })
	public String GetAndroidDigitalHistoryData(@PathVariable final Long deviceId, @PathVariable final String startdate,
			@PathVariable final String enddate, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			Devicemaster device = this.deviceReop.findBydeviceid(deviceId);
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode analogNodearrayNode = mapper.createArrayNode();
			List<Object[]> lst = (List<Object[]>) this.historyservices.getDigitalHistory(deviceId, startdate, enddate);
			if (lst != null) {
				for (int i = 0; i < lst.size(); ++i) {
					final ObjectNode DIGITALNode = mapper.createObjectNode();
					ObjectNode dynamicNodearrayNode = mapper.createObjectNode();

					final Object[] obj = lst.get(i);
					DIGITALNode.put("Site Name", device.getDevicename());
					DIGITALNode.put("Device Date", obj[1].toString());
					final JSONArray jsonarray = new JSONArray(obj[3].toString());
					for (int j = 0; j < jsonarray.length(); ++j) {
						final JSONObject jsonobject = jsonarray.getJSONObject(j);
						final String k = jsonobject.keys().next();
						System.out.println("Info :: Key: " + parameterRepo.findByid((long) new Long(k)).getPrmname()
								+ ", value: " + jsonobject.getString(k));
						/*DIGITALNode.put(parameterRepo.findByid((long) new Long(k)).getPrmname(),
								jsonobject.getString(k));*/
						if (jsonobject.getString(k).toString().equalsIgnoreCase("1"))
							dynamicNodearrayNode.put(parameterRepo.findByid((long) new Long(k)).getPrmname(), "ON");
						else
							dynamicNodearrayNode.put(parameterRepo.findByid((long) new Long(k)).getPrmname(), "OFF");
					}
					DIGITALNode.put("DynamicDate", dynamicNodearrayNode);
					analogNodearrayNode.add((JsonNode) DIGITALNode);
				}
			} else {
				final ObjectNode DIGITALNode2 = mapper.createObjectNode();
				analogNodearrayNode.add((JsonNode) DIGITALNode2);
			}
			return "{\"Status\":\"True\",\"Message\":\"Data Retrieved.!\",\"Data\":" + analogNodearrayNode.toString()
					+ "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
	}

	@RequestMapping(value = "/android/GetAndroidDeviceList/{userId}/{apiKey}", produces = { "application/json" })
	public String GetAndroidDeviceList(@PathVariable long userId, @PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = devicemasterservices.getMyDeviced(userId);
			System.out.println("SAJAN :: " + listt.size());
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("deviceId", o[0].toString());
				obj.put("deviceName", o[1].toString());
				arry.put(obj);
			});
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + arry.toString() + "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";

	}

	@RequestMapping(value = {
			"/android/GetAndroidDigitalOnOffData/{startDate}/{endDate}/{paramId}/{apiKey}" }, produces = {
					"application/json" })
	public String GetAndroidDigitalOnOffData(@PathVariable final String startDate, @PathVariable final String endDate,
			@PathVariable final String paramId, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException, JSONException, ParseException {
		// final JSONArray GmainJSON = new JSONArray();
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode GmainJSON = mapper.createArrayNode();
			List<LocalDate> totalDates = new ArrayList<LocalDate>();
			totalDates = (List<LocalDate>) JsonUtills.getBetweenDate(startDate, endDate);
			for (final LocalDate nxtDate : totalDates) {
				System.out.println(nxtDate);
				final List<Object[]> lst = (List<Object[]>) this.historyservices.getdata(nxtDate.toString(),
						nxtDate.toString(), paramId, 0L);  
				final JSONArray mainJSON = new JSONArray();
				if (lst != null) {
					String lstatus = null;
					int ct = 0;
					for (int i = 0; i < lst.size(); ++i) {
						final Object[] obj = lst.get(i);
						if (i == 0) {
							lstatus = obj[3].toString();
							final JSONObject jo = new JSONObject();
							jo.put("Start Date", (Object) obj[1].toString());
							jo.put("Start Status", obj[3]);
							mainJSON.put(ct, (Object) jo);
							++ct;
						} else {
							if (lstatus.equalsIgnoreCase(obj[3].toString())) {
								System.out.println();
							} else {
								final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
								final Date myDateOne = this.df.parse(innerJSON12.getString("Start Date"));
								final Date myDateTwo = this.df.parse(obj[1].toString());
								final DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
								final int retVal = dateTimeComparator.compare((Object) myDateOne, (Object) myDateTwo);
								final JSONObject jo2 = new JSONObject();
								boolean flage = false;
								if (retVal == 0) {
									if (!innerJSON12.has("End Date")) {
										innerJSON12.put("End Date", obj[1]);
									}
									mainJSON.put(ct - 1, (Object) innerJSON12);
									jo2.put("Start Date", obj[1]);
								} else {
									innerJSON12.put("End Date",
											(Object) (String
													.valueOf(this.df1.format(this.df1
															.parse(innerJSON12.getString("Start Date").toString())))
													+ " 23:59:59"));
									mainJSON.put(ct - 1, (Object) innerJSON12);
									final JSONObject jo3 = new JSONObject();
									jo2.put("Start Date", (Object) (String
											.valueOf(JsonUtills.getNextDate(this.df1.format(
													this.df1.parse(innerJSON12.getString("Start Date").toString()))))
											+ " 00:00:00"));
									jo2.put("End Date", obj[1]);
									flage = true;
								}
								jo2.put("Start Status", obj[3]);
								mainJSON.put(ct, (Object) jo2);
								++ct;
							}
							lstatus = obj[3].toString();
						}
					}
				}
				for (int j = 0; j < mainJSON.length(); ++j) {
					final JSONObject jsonObject1 = mainJSON.getJSONObject(j);
					if (j != mainJSON.length() - 1) {
						final Date JSONStartDate = this.df.parse(jsonObject1.getString("Start Date"));
						final Date JSONendDate = this.df.parse(jsonObject1.getString("End Date"));
						JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
						if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
							jsonObject1.put("ON", (Object) JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
							jsonObject1.put("OFF", (Object) "");
						} else {
							jsonObject1.put("ON", (Object) "");
							jsonObject1.put("OFF", (Object) JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
						}
					} else {
						final JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
						final Date JSONStartDate2 = this.df.parse(jsonObjectLast.getString("Start Date"));
						final Date date21 = this.df1.parse(endDate);
						final Date date22 = this.df1.parse(this.df1.format(new Date()));
						final int date23 = date21.compareTo(date22);
						if (date23 == 0) {
							jsonObjectLast.put("End Date", (Object) this.df.format(new Date()));
							if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
								jsonObject1.put("ON", (Object) JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
								jsonObject1.put("OFF", (Object) "");
							} else {
								jsonObject1.put("ON", (Object) "");
								jsonObject1.put("OFF",
										(Object) JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
							}
						} else if (date23 == -1) {
							jsonObjectLast.put("End Date",
									(Object) this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")));
							if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
								jsonObject1.put("ON", (Object) JsonUtills.getTimeDiffrence(JSONStartDate2, this.df
										.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
								jsonObject1.put("OFF", (Object) "");
							} else {
								jsonObject1.put("ON", (Object) "");
								jsonObject1.put("OFF", (Object) JsonUtills.getTimeDiffrence(JSONStartDate2, this.df
										.parse(this.df.format(this.df.parse(String.valueOf(endDate) + " 23:59:59")))));
							}
						}
					}
				}
				for (int j = 0; j < mainJSON.length(); ++j) {
					ObjectNode ImainJSON = mapper.createObjectNode();
					JSONObject jsonObject2 = mainJSON.getJSONObject(j);
					ImainJSON.put("StartDate", jsonObject2.getString("Start Date"));
					ImainJSON.put("EndDate", jsonObject2.getString("End Date"));

					if (jsonObject2.getString("Start Status").toString().equalsIgnoreCase("1")) {
						ImainJSON.put("Status", "ON");
					} else {
						ImainJSON.put("Status", "OFF");
					}
					GmainJSON.add(ImainJSON);
				}
			}
			return "{\"Status\":\"True\",\"Message\":\"Data Retrieved.!\",\"Data\":" + GmainJSON.toString() + "}";
		} else
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
	}

	@RequestMapping(value = "/android/GetManagerSiteList/{managerId}/{key}", produces = { "application/json" })
	public String GetManagerSiteList(@PathVariable Long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = siteService.GetSiteByManager(managerId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				arry.put(obj);
			});
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + arry.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/siteViseDeviceSummary/{siteId}/{key}", method = RequestMethod.GET)
	public String siteViseDeviceSummary(@PathVariable Long siteId, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			long userID = 2;
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			String adminFailureminute = "";
			String adminWarningminute = "";
			for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				adminFailureminute = DeviceAdminresult[0].toString();
				adminWarningminute = DeviceAdminresult[1].toString();
			}
			List<Object[]> deviceCountList = siteService.GetDeviceIdBySite(siteId);
			int deviceCount = 0;
			String deviceIdNew = "0";
			int deviceIdealCount = 0;
			int deviceNeverUsedCount = 0;
			int warningCount = 0;
			int failureCount = 0;
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			int finalMinutes = 0;
			for (Object[] result1 : deviceCountList) {
				deviceIdNew = result1[0].toString();
				List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
				for (Object[] DeviceDataresult : idealDeviceList) {
					deviceDate = DeviceDataresult[1].toString();
				}
				try {
					d1 = format.parse(deviceDate);
					d2 = format.parse(strDate);
					long diff = d2.getTime() - d1.getTime();
					long diffMinutes = diff / (60 * 1000) % 60;
					int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					finalMinutes = days * 1440;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (finalMinutes > Integer.parseInt(adminWarningminute)
						&& finalMinutes < Integer.parseInt(adminFailureminute)) {
					warningCount++;
				} else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
					failureCount++;
				} else {
					failureCount = 0;
					warningCount = 0;
				}
				if (idealDeviceList.size() == 1) {
					deviceIdealCount++;
				}
				if (idealDeviceList.size() == 0) {
					deviceNeverUsedCount++;
				}
				deviceCount++;
			}
			jo.put("failureCount", failureCount);
			jo.put("warningCount", warningCount);
			jo.put("deviceCount", deviceCount);
			jo.put("deviceIdealCount", deviceIdealCount);
			jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
			jarray.put(jo);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/GetDeviceInfoBySite/{siteId}/{managerId}/{key}", produces = {
			"application/json" })
	public String GetDeviceInfoBySite(@PathVariable long siteId, @PathVariable long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException, java.text.ParseException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> lst = null;
			JSONArray jarray = new JSONArray();
			long userID = 2;
			List<Object[]> deviceFailureManagerlist = Dfnservices.getAdminDeviceFailureData(managerId);
			String adminFailureminute = "0";
			String adminWarningminute = "0";
			if (deviceFailureManagerlist.size() == 0) {
				List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
				for (Object[] DeviceManagerresult : deviceFailureAdminlist) {
					adminFailureminute = DeviceManagerresult[0].toString();
					adminWarningminute = DeviceManagerresult[1].toString();
				}
			} else {
				for (Object[] DeviceAdminresult : deviceFailureManagerlist) {
					adminFailureminute = DeviceAdminresult[0].toString();
					adminWarningminute = DeviceAdminresult[1].toString();
				}
			}
			lst = devicemasterservices.getDeviceBySite(siteId);
			String deviceDate = "NA";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			String usedBefore = "";
			int finalMinutes = 0;
			int warningCount = 0;
			int failureCount = 0;
			String deviceStat = "";
			for (Object[] result : lst) {
				JSONObject jo = new JSONObject();
				List<Object[]> deviceDateList = Dfnservices.getDeviceDataById(Long.parseLong(result[0].toString()));
				if (deviceDateList.size() == 1) {
					for (Object[] dateresult : deviceDateList) {
						deviceDate = dateresult[1].toString();
					}
					try {
						d1 = format.parse(deviceDate);
						d2 = format.parse(strDate);
						long diff = d2.getTime() - d1.getTime();
						if (diff < 0) {
							diff = 0;
							deviceStat = "success";
						}
						long diffMinutes = diff / (60 * 1000) % 60;
						int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						finalMinutes = days * 1440;
						long diffSeconds = diff / 1000 % 60;
						// long diffMinutes = diff / (60 * 1000) % 60;
						long diffHours = diff / (60 * 60 * 1000) % 24;
						long diffDays = diff / (24 * 60 * 60 * 1000);

						// usedBefore=String.valueOf(days);
						usedBefore = "" + diffDays + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds
								+ "sec ";
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (finalMinutes > Integer.parseInt(adminWarningminute)
							&& finalMinutes < Integer.parseInt(adminFailureminute)) {
						warningCount++;
						deviceStat = "Warning";
					} else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
						failureCount++;
						deviceStat = "Failure";
					} else {
						failureCount = 0;
						warningCount = 0;
						deviceStat = "NA";
					}
				} else {
					deviceDate = "NA";
					usedBefore = "NA";
					deviceStat = "NA";
				}
				jo.put("devicename", result[3].toString());
				jo.put("imei", result[6].toString());
				jo.put("simcardno", result[5].toString());
				jo.put("devicedescription", result[3].toString());
				jo.put("devicemodel", result[4].toString());
				jo.put("deviceid", result[0].toString());
				jo.put("deviceDate", deviceDate);
				jo.put("usedBefore", usedBefore);
				jo.put("deviceStatus", deviceStat);
				jarray.put(jo);
			}
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/getSiteViseDeviceInfo/{siteId}/{key}", produces = { "application/json" })
	public String getSiteViseDeviceInfo(HttpServletRequest request, @PathVariable long siteId,
			@PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = DeviceMasterservices.getDeviceBySite(siteId);

			JSONObject mainJson = new JSONObject();
			JSONArray DataArray = new JSONArray();
			JSONArray ColumnsArray = new JSONArray();

			JSONArray Auto_ColumnsArray = new JSONArray();
			Auto_ColumnsArray.put("#");
			JSONArray DeviceName_ColumnsArray = new JSONArray();
			DeviceName_ColumnsArray.put("Device Name");
			JSONArray DeviceModel_ColumnsArray = new JSONArray();
			DeviceModel_ColumnsArray.put("Device Model");
			JSONArray SimNum_ColumnsArray = new JSONArray();
			SimNum_ColumnsArray.put("SimCard Num");
			JSONArray DeviceIMEI_ColumnsArray = new JSONArray();
			DeviceIMEI_ColumnsArray.put("IMEI");
			JSONArray DeviceAction_ColumnsArray = new JSONArray();
			DeviceAction_ColumnsArray.put("Action");

			ColumnsArray.put(Auto_ColumnsArray);
			ColumnsArray.put(DeviceName_ColumnsArray);
			ColumnsArray.put(DeviceModel_ColumnsArray);
			ColumnsArray.put(SimNum_ColumnsArray);
			ColumnsArray.put(DeviceIMEI_ColumnsArray);
			ColumnsArray.put(DeviceAction_ColumnsArray);
			int auto_num = 1;
			for (Object[] deviceListresult : list) {
				JSONArray Inner_DataArray = new JSONArray();
				Inner_DataArray.put(auto_num);
				Inner_DataArray.put(deviceListresult[3].toString());
				Inner_DataArray.put(deviceListresult[4].toString());
				Inner_DataArray.put(deviceListresult[5].toString());
				Inner_DataArray.put(deviceListresult[6].toString());
				Inner_DataArray.put(deviceListresult[3].toString() + "," + deviceListresult[0].toString());
				DataArray.put(Inner_DataArray);
				auto_num++;
			}
			mainJson.put("data", DataArray);
			mainJson.put("columns", ColumnsArray);
			System.out.println("Main_Json::" + mainJson.toString());
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + mainJson.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/getLastTrackBySite/{siteId}/{key}", produces = { "application/json" })
	public String getLastTrackBySite(HttpServletRequest request, @PathVariable long siteId, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> deviceList = new ArrayList<>();
			deviceList = siteService.GetDeviceIdBySite(siteId);

			JSONObject mainJson = new JSONObject();
			JSONArray DataArray = new JSONArray();
			JSONArray ColumnsArray = new JSONArray();

			JSONArray Auto_ColumnsArray = new JSONArray();
			Auto_ColumnsArray.put("#");
			JSONArray DeviceName_ColumnsArray = new JSONArray();
			DeviceName_ColumnsArray.put("DeviceName");
			JSONArray History_ColumnsArray = new JSONArray();
			History_ColumnsArray.put("History");
			JSONArray DeviceDate_ColumnsArray = new JSONArray();
			DeviceDate_ColumnsArray.put("DeviceDate");

			ColumnsArray.put(Auto_ColumnsArray);
			ColumnsArray.put(DeviceName_ColumnsArray);
			ColumnsArray.put(DeviceDate_ColumnsArray);

			String analogAdd = "0";
			String digitalAdd = "0";
			int auto_num = 1;
			for (Object[] deviceListresult : deviceList) {
				long deviceId = Long.parseLong(deviceListresult[0].toString());
				List<Object[]> deviceProfileList = new ArrayList<>();
				deviceProfileList = parameterRepo.getDeviceProfileByDeviceId(deviceId);
				long prof_Id = 0L;
				for (Object[] prof_result : deviceProfileList) {
					prof_Id = Long.parseLong(prof_result[1].toString());
				}
				List<Object[]> list = new ArrayList<>();
				list = parameterRepo.getLasttrackByDeviceId(Long.parseLong(deviceListresult[0].toString()));
				for (Object[] result : list) {
					String DeviceName = result[3].toString();
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
						JSONObject param_obj = new JSONObject(parameterService.get(Long.parseLong(k)));
						param_obj.getString("prmname");
						String unitlist = parameterService.getLasttrackUnits(prof_Id, k);
						Inner_DataArray.put(newobj.getString(k));
						JSONArray Inner_ColumnsArray = new JSONArray();
						if (ColumnsArray.toString().contains(param_obj.getString("prmname")) == false) {
							Inner_ColumnsArray.put(param_obj.getString("prmname") + " " + "(" + unitlist + ")");
							ColumnsArray.put(Inner_ColumnsArray);
						} else {
							analogAdd = "1";
						}
					}
					for (int s = 0; s < Digital_Array.length(); s++) {
						JSONObject digitalnewobj = Digital_Array.getJSONObject(s);
						String digitalkey = digitalnewobj.keys().next();
						JSONObject dig_param_obj = new JSONObject(parameterService.get(Long.parseLong(digitalkey)));
						dig_param_obj.getString("prmname");
						Inner_DataArray.put(digitalnewobj.getString(digitalkey));

						JSONArray Dig_Inner_ColumnsArray = new JSONArray();
						if (ColumnsArray.toString().contains(dig_param_obj.getString("prmname")) == false) {
							Dig_Inner_ColumnsArray.put(dig_param_obj.getString("prmname"));
							ColumnsArray.put(Dig_Inner_ColumnsArray);
						} else {
							digitalAdd = "1";
						}
					}
					Inner_DataArray.put(deviceId + "," + DeviceName);
					DataArray.put(Inner_DataArray);
					auto_num++;
				}
			}
			ColumnsArray.put(History_ColumnsArray);
			mainJson.put("data", DataArray);
			mainJson.put("columns", ColumnsArray);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + mainJson.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/deviceSummarymanager/{managerid}/{key}", method = RequestMethod.GET)
	public String deviceSummarymanager(@PathVariable("managerid") long managerid, @PathVariable String key)
			throws java.text.ParseException {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();

			List<Object[]> deviceFailureAdminlist = Dfnservices.getManagerDeviceFailureData(managerid);
			String adminFailureminute = "";
			String adminWarningminute = "";
			for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				adminFailureminute = DeviceAdminresult[0].toString();
				adminWarningminute = DeviceAdminresult[1].toString();
			}
			int warningCount = 0;
			int failureCount = 0;
			int success = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> list = Dfnservices.getmanagerDeviceFailureDateDiff(managerid);
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			Long diffMinutes = null;
			long diffMinutes2 = 0;
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[2].toString();
				deviceDate = result1[0].toString();
				sysDate = result1[1].toString();
				int finalMinutes = 0;
				try {
					d1 = format.parse(deviceDate);
					d2 = format.parse(strDate);
					long diff = d2.getTime() - d1.getTime();
					diffMinutes = diff / (60 * 1000) % 60;
					diffMinutes2 = diff / (60 * 1000);

					int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					finalMinutes = days * 1440;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (diffMinutes2 > Integer.parseInt(adminWarningminute)
						&& diffMinutes2 < Integer.parseInt(adminFailureminute)) {
					warningCount++;
				} else if (diffMinutes2 > Integer.parseInt(adminFailureminute)) {
					failureCount++;
				} else if (diffMinutes2 == 0) {
					success++;
				}
			}
			List<Object[]> deviceCountList = Dfnservices.getTotalManagerDeviceCount(managerid);
			int deviceCount = 0;
			String deviceIdNew = "0";
			int deviceIdealCount = 0;
			int deviceNeverUsedCount = 0;
			for (Object[] result1 : deviceCountList) {
				deviceIdNew = result1[0].toString();
				List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
				if (idealDeviceList.size() == 1) {
					deviceIdealCount++;
				}
				if (idealDeviceList.size() == 0) {
					deviceNeverUsedCount++;
				}
				deviceCount++;
			}
			jo.put("failureCount", failureCount);
			jo.put("warningCount", warningCount);
			jo.put("deviceCount", deviceCount);
			jo.put("deviceIdealCount", deviceIdealCount);
			jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
			jo.put("success", success);
			jarray.put(jo);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/GetDeviceListmanager/{managerId}/{key}", produces = { "application/json" })
	public String GetDeviceListmanager(@PathVariable long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException, java.text.ParseException {
		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> lst = null;
			JSONArray jarray = new JSONArray();
			List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(managerId);
			String adminFailureminute = "0";
			String adminWarningminute = "0";
			if (deviceFailureAdminlist.size() == 0) {
				adminFailureminute = "0";
				adminWarningminute = "0";
			} else {
				for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
					adminFailureminute = DeviceAdminresult[0].toString();
					adminWarningminute = DeviceAdminresult[1].toString();
				}
			}
			lst = devicemasterservices.getBymanagerId(managerId);
			String deviceDate = "NA";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			String usedBefore = "";
			int finalMinutes = 0;
			int warningCount = 0;
			int failureCount = 0;
			int success = 0;
			long diffMinutes = 0;
			long diffMinutes2 = 0;
			long diff = 0;
			String deviceStat = "";
			for (Object[] result : lst) {
				JSONObject jo = new JSONObject();
				List<Object[]> deviceDateList = Dfnservices.getDeviceDataById(Long.parseLong(result[5].toString()));
				if (deviceDateList.size() == 1) {
					for (Object[] dateresult : deviceDateList) {
						deviceDate = dateresult[1].toString();
					}
					try {
						d1 = format.parse(deviceDate);
						d2 = format.parse(strDate);
						diff = d2.getTime() - d1.getTime();
						int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						finalMinutes = days * 1440;

						long diffSeconds = diff / 1000 % 60;
						diffMinutes = diff / (60 * 1000) % 60;
						long diffHours = diff / (60 * 60 * 1000) % 24;
						long diffDays = diff / (24 * 60 * 60 * 1000);

						diffMinutes2 = diff / (60 * 1000);
						usedBefore = "" + diffDays + "days " + diffHours + "hrs," + diffMinutes + "min," + diffSeconds
								+ "sec ";
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (diffMinutes2 == 0) {
						success++;
						deviceStat = "success";
					} else if (diffMinutes2 > Integer.parseInt(adminWarningminute)
							&& diffMinutes2 < Integer.parseInt(adminFailureminute)) {
						warningCount++;
						deviceStat = "Warning";
					} else if (diffMinutes2 > Integer.parseInt(adminFailureminute)) {
						failureCount++;
						deviceStat = "Failure";
					}
				} else {
					deviceDate = "NA";
					usedBefore = "NA";
					deviceStat = "Never";
				}
				jo.put("devicename", result[0].toString());
				jo.put("imei", result[1].toString());
				jo.put("simcardno", result[2].toString());
				jo.put("devicedescription", result[3].toString());
				jo.put("devicemodel", result[4].toString());
				jo.put("deviceid", result[5].toString());
				jo.put("deviceDate", deviceDate);
				jo.put("usedBefore", usedBefore);
				jo.put("deviceStatus", deviceStat);
				jarray.put(jo);
			}
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/deviceSummaryCritical/{userID}/{key}", method = RequestMethod.GET)
	public String deviceSummaryCritical(@PathVariable long userID, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();

			List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			String adminFailureminute = "";
			String adminWarningminute = "";
			for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				adminFailureminute = DeviceAdminresult[0].toString();
				adminWarningminute = DeviceAdminresult[1].toString();
			}
			int warningCount = 0;
			int failureCount = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				int finalMinutes = 0;
				try {
					d1 = format.parse(deviceDate);
					d2 = format.parse(strDate);
					long diff = d2.getTime() - d1.getTime();
					long diffMinutes = diff / (60 * 1000) % 60;
					int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					finalMinutes = days * 1440;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<Object[]> deviceCountList = Dfnservices.getTotalDeviceCount();
			int deviceCount = 0;
			String deviceIdNew = "0";
			int deviceIdealCount = 0;
			int deviceNeverUsedCount = 0;
			for (Object[] result1 : deviceCountList) {
				deviceIdNew = result1[0].toString();
				List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
				if (idealDeviceList.size() == 1) {
					deviceIdealCount++;
				}
				if (idealDeviceList.size() == 0) {
					deviceNeverUsedCount++;
				}
				deviceCount++;
			}
			jo.put("failureCount", failureCount);
			jo.put("warningCount", warningCount);
			jo.put("deviceCount", deviceCount);
			jo.put("deviceIdealCount", deviceIdealCount);
			jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
			jarray.put(jo);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/deviceDigitalSummaryCritical/{userID}/{key}", method = RequestMethod.GET)
	public String deviceDigitalSummaryCritical(@PathVariable long userID, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();
			List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			String adminFailureminute = "";
			String adminWarningminute = "";
			for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				adminFailureminute = DeviceAdminresult[0].toString();
				adminWarningminute = DeviceAdminresult[1].toString();
			}
			int warningCount = 0;
			int failureCount = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			String paramId = "";
			String paramName = "";
			String criticalness = "";
			String devNewID = "";
			String paramStat = "";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				List<Object[]> analogWarnFail = Dfnservices.paramDigitalWarnFailByProfile(devicename);
				for (Object[] rs : analogWarnFail) {
					JSONObject blinkObj = new JSONObject();
					int val = 2;
					paramId = rs[0].toString();
					paramName = rs[1].toString();
					criticalness = rs[2].toString();
					if (criticalness.equalsIgnoreCase("ON")) {
						val = 1;
					} else if (criticalness.equalsIgnoreCase("OFF")) {
						val = 0;
					} else {
						val = 3;
					}
					List<Object[]> digilasttrackParams = Dfnservices.getLasttrackDigital(Long.parseLong(deviceId),
							paramId);
					for (Object[] rs2 : digilasttrackParams) {
						devNewID = rs2[0].toString();
						paramStat = rs2[1].toString();
						if (!(Integer.parseInt(paramStat) == val)) {
							blinkObj.put("Stat", "OFF");
						} else {
							blinkObj.put("Stat", "ON");
						}
						blinkObj.put("deviceId", deviceId);
						blinkObj.put("paramName", paramName);
						blinkObj.put("deviceName", devicename);
						blinkObj.put("deviceDate", deviceDate);
						blinkArray.put(blinkObj);
					}
				}
			}
			jo.put("Data", blinkArray);
			jarray.put(jo);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/deviceAnalogSummaryCritical/{userID}//{key}", method = RequestMethod.GET)
	public String deviceAnalogSummaryCritical(@PathVariable long userID, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();
			List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			String adminFailureminute = "";
			String adminWarningminute = "";
			for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				adminFailureminute = DeviceAdminresult[0].toString();
				adminWarningminute = DeviceAdminresult[1].toString();
			}
			int warningCount = 0;
			int failureCount = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			String paramName = "";
			String warnMin = "";
			String failMin = "";
			String sign_rule = "";
			String paramId = "";
			String paramStat = "";
			Date d1 = null;
			Date d2 = null;
			Date d = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				JSONObject blinkObj = new JSONObject();
				List<Object[]> analogWarnFail = Dfnservices.paramWarnFailByProfile(devicename);
				for (Object[] rs : analogWarnFail) {
					paramId = rs[0].toString();
					warnMin = rs[2].toString();
					failMin = rs[3].toString();
					paramName = rs[4].toString();
					sign_rule = rs[6].toString();
					List<Object[]> analoglasttrackParams = Dfnservices.getLasttrackAnalog(Long.parseLong(deviceId),
							paramId);
					if (analoglasttrackParams.size() == 0) {
						blinkObj.put("deviceId", deviceId);
						blinkObj.put("deviceName", devicename);
						blinkObj.put("deviceDate", deviceDate);
						blinkArray.put(blinkObj);
					} else {
						for (Object[] analog : analoglasttrackParams) {
							paramStat = analog[1].toString();
							System.out.println("paramStat:::" + paramStat);
							if (Float.parseFloat(paramStat) > Float.parseFloat(warnMin)
									&& Float.parseFloat(paramStat) < Float.parseFloat(failMin)) {
								blinkObj.put("Parameter", paramName);
								blinkObj.put("Stat", "WARN");
								blinkObj.put("adminMin", warnMin);
								blinkObj.put("settMin", paramStat);
								blinkObj.put("signRule", sign_rule);
							} else if (Float.parseFloat(paramStat) > Float.parseFloat(failMin)) {
								blinkObj.put("Parameter", paramName);
								blinkObj.put("Stat", "FAIL");
								blinkObj.put("adminMin", failMin);
								blinkObj.put("settMin", paramStat);
								blinkObj.put("signRule", sign_rule);
							} else {
								blinkObj.put("Parameter", paramName);
								blinkObj.put("Stat", "NA");
								blinkObj.put("adminMin", "0");
								blinkObj.put("settMin", "0");
								blinkObj.put("signRule", " ");
							}
							blinkObj.put("deviceId", deviceId);
							blinkObj.put("deviceName", devicename);
							blinkObj.put("deviceDate", deviceDate);
							blinkArray.put(blinkObj);
						}
					}
				}
			}
			jo.put("Data", blinkArray);
			jarray.put(jo);
			return "{\"Status\":\"True\",\"Message\":\"Data Reteived.\",\"Data\":" + jarray.toString() + "}";
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Invalid Key!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// Ashish AJ
	@RequestMapping(value = "/android/GetAndroidManagerLayout/{managerId}/{apiKey}", produces = { "application/json" })
	public String getmanagerlayout(HttpServletRequest request, @PathVariable("managerId") long managerId,
			@PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getmanagerlayout(managerId);
			JSONArray jarray = new JSONArray();
			JSONObject obj1 = new JSONObject();
			String id = "";
			String name = "";
			String view = "";
			String viewtype = "";
			String profile = "";
			String htmlname = "";

			for (Object[] result1 : list) {
				id = result1[0].toString();
				name = result1[1].toString();
				view = result1[2].toString();
				viewtype = result1[3].toString();
				profile = result1[4].toString();
				htmlname = result1[5].toString();
				JSONObject jo = new JSONObject();
				String[] profileid = profile.split(",", 50);
				StringBuilder profile1 = new StringBuilder();
				if (profile != null) {
					for (String ids : profileid) {
						if (ids.equalsIgnoreCase("NA")) {
							jo.put("profile1", profile1.toString());
						} else {
							List list2 = dLServices.getprofilename(Long.parseLong(ids));
							for (int j = 0; j < list2.size(); j++) {
								profile1.append(((String) list2.get(j)));
								profile1.append(",");
							}
							jo.put("profile1", profile1.toString());
						}
					}
				}
				jo.put("id", id);
				jo.put("name", name);
				jo.put("view", view);
				jo.put("viewtype", viewtype);
				jo.put("htmlname", htmlname);
				jarray.put(jo);
			}
			if (jarray.length() == 0) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				obj1.put("Message", "Data Retrived");
				obj1.put("Status", "True");
				obj1.put("data", jarray);
				return obj1.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";

		}

	}

	@RequestMapping(value = "/android/GetAndroidUserLayout/{userid}/{apiKey}", produces = { "application/json" })
	public String getuseridlayout(HttpServletRequest request, @PathVariable("userid") long userid,
			@PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getuserlayout(userid);
			JSONArray jarray = new JSONArray();
			JSONObject obj1 = new JSONObject();

			String id = "";
			String name = "";
			String view = "";
			String viewtype = "";
			String profile = "";
			String htmlname = "";
			String dyname = "";

			for (Object[] result1 : list) {
				id = result1[0].toString();
				name = result1[1].toString();
				view = result1[2].toString();
				viewtype = result1[3].toString();
				profile = result1[4].toString();
				htmlname = result1[5].toString();
				dyname = result1[6].toString();
				JSONObject jo = new JSONObject();
				String[] profileid = profile.split(",", 50);
				StringBuilder profile1 = new StringBuilder();
				if (profile != null) {
					for (String ids : profileid) {
						if (ids.equalsIgnoreCase("NA")) {
							jo.put("profile1", profile1.toString());
						} else {
							List list2 = dLServices.getprofilename(Long.parseLong(ids));
							for (int j = 0; j < list2.size(); j++) {
								profile1.append(((String) list2.get(j)));
								profile1.append(",");
							}
							jo.put("profile1", profile1.toString());
						}
					}
				}
				jo.put("id", id);
				jo.put("name", name);
				jo.put("view", view);
				jo.put("viewtype", viewtype);
				jo.put("htmlname", htmlname);
				jo.put("dyname", dyname);
				jarray.put(jo);
			}

			if (jarray.length() == 0) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				obj1.put("Message", "Data Retrived");
				obj1.put("Status", "True");
				obj1.put("data", jarray);
				return obj1.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/AndroidManagerLayoutView/{dlid}/{managerid}/{apiKey}", produces = {
			"application/json" })
	public String managerlayoutview(HttpServletRequest request, @PathVariable("dlid") long dlid,
			@PathVariable("managerid") long managerid, @PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getdlayoutview(dlid, managerid);
			JSONArray jarray = new JSONArray();
			JSONObject obj2 = new JSONObject();
			String dyid = "";
			String view = "";
			String viewtype = "";
			String deviceid = "";
			String siteid = "";
			String profile = "";
			for (Object[] result1 : list) {
				dyid = result1[0].toString();
				view = result1[2].toString();
				viewtype = result1[3].toString();
				deviceid = result1[4].toString();
				siteid = result1[5].toString();
				profile = result1[6].toString();
				JSONObject jo = new JSONObject();
				jo.put("dyid", dyid);
				jo.put("view", view);
				jo.put("viewtype", viewtype);
				jo.put("deviceid", deviceid);
				jo.put("siteid", siteid);
				jo.put("profile", profile);
				jarray.put(jo);
			}

			if (jarray.length() == 0) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				obj2.put("Message", "Data Retrived");
				obj2.put("Status", "True");
				obj2.put("data", jarray);
				return obj2.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/UserAndroidlayoutView/{dlid}/{userid}/{apiKey}", produces = {
			"application/json" })
	public String userlayoutview(HttpServletRequest request, @PathVariable("dlid") long dlid,
			@PathVariable("userid") long userid, @PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getdulayoutview(dlid, userid);
			JSONArray jarray = new JSONArray();
			JSONObject obj2 = new JSONObject();
			String dyid = "";
			String view = "";
			String viewtype = "";
			String deviceid = "";
			String siteid = "";
			String profile = "";
			for (Object[] result1 : list) {
				dyid = result1[0].toString();
				view = result1[2].toString();
				viewtype = result1[3].toString();
				deviceid = result1[4].toString();
				siteid = result1[5].toString();
				profile = result1[6].toString();
				JSONObject jo = new JSONObject();
				jo.put("dyid", dyid);
				jo.put("view", view);
				jo.put("viewtype", viewtype);
				jo.put("deviceid", deviceid);
				jo.put("siteid", siteid);
				jo.put("profile", profile);
				jarray.put(jo);
			}
			if (jarray.length() == 0) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				obj2.put("Message", "Data Retrived");
				obj2.put("Status", "True");
				obj2.put("data", jarray);
				return obj2.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/GetAndroidDeviceDashboard/{id}/{apiKey}", produces = { "application/json" })
	public String getcomponetlist2(@PathVariable String id, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode analogNodearrayNode = mapper.createArrayNode();
			String[] sdeviceid = id.split(",");
			for (String w : sdeviceid) {
				System.out.println(w);
				Lasttrack lastTrack = lasttrackservices.findOne(Long.valueOf(w));

				if (lastTrack != null) {
					Devicemaster device = deviceReop.findBydeviceid(lastTrack.getDeviceId());
					JSONObject analogJsonObject = new JSONObject(
							new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
					JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
					JSONArray analogArray2 = new JSONArray(analogJsonObject.get("Digital").toString());
					ObjectNode analogNode = mapper.createObjectNode();

					analogArray.forEach(SingleAnalogObject -> {
						JSONObject analogObject = (JSONObject) SingleAnalogObject;
						analogNode.put("DeviceId", w);
						analogNode.put("DeviceName", device.getDevicename());
						analogNode.put("DeviceDate", lastTrack.getDeviceDate().toString());

						for (int i = 0; i < analogObject.names().length(); i++) {
							analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
									analogObject.get(analogObject.names().getString(i)).toString());
							analogNode.put(
									reo.findByid(new Long(analogObject.names().getString(i))).getPrmname() + "a1",
									reo.getLasttrackUnitsNew(device.getDp().getPrid(),
											analogObject.names().getString(i)).toString());
							analogNode.put(
									reo.findByid(new Long(analogObject.names().getString(i))).getPrmname() + "b1",
									reo.findByid(new Long(analogObject.names().getString(i))).getId().toString());
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
			objectNode.putPOJO("Status", "True");
			objectNode.putPOJO("Message", "Retrived");
			objectNode.putPOJO("Data", analogNodearrayNode);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// GetDeltaMeterRPT26
	@RequestMapping(value = "/android/GetAndroidDeltaRPT/{id}/{startdate}/{enddate}/{prmname}/{apiKey}", produces = {
			"application/json" })
	public String getEnergyMeterRpt27(@PathVariable Long id, @PathVariable String startdate,
			@PathVariable String enddate, @PathVariable String prmname, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			if ((JsonUtills.ListToGraphJson3(historyservices.deltameterrpt3(id, startdate, enddate, prmname)))
					.isEmpty()) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				JSONObject obj2 = new JSONObject();
				obj2.put("Message", "Data Retrived");
				obj2.put("Status", "True");
				obj2.put("data",JsonUtills.ListToGraphJson4(historyservices.deltameterrpt3(id, startdate, enddate, prmname)));
				
				return obj2.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// GetDeltaMeterRPT23
	@RequestMapping(value = "/android/GetAndroidDeltaMeterGraph2/{id}/{startdate}/{enddate}/{prmname}/{apiKey}", produces = {
			"application/json" })
	public String getEnergyMeterRpt24(@PathVariable Long id, @PathVariable String startdate,
			@PathVariable String enddate, @PathVariable String prmname, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			JSONArray jarray2 = new JSONArray();
			JSONObject energyobj2 = new JSONObject();
			JSONArray jarray3 = new JSONArray();

			List list = historyservices.deltameterrpt(id, startdate, enddate, prmname);
			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject energyobj = new JSONObject();
					Object[] result = (Object[]) list.get(i);
					JSONArray jarray = new JSONArray();
					jarray.put(i + 1);
					jarray.put((result[1].toString()).split(" ")[0]);

					Devicemaster devicemaster = devicemasterservices.get(id);
					List list5 = historyservices.getprofileanalogunit(devicemaster.getDp().getPrid(), prmname);
					String analogunit = (String) list5.get(0);
					jarray.put(analogunit);
					jarray.put(result[2]);
					jarray2.put(jarray);
				}
				energyobj2.put("Message", "Data Retrived");
				energyobj2.put("Status", "True");
				energyobj2.put("data", jarray2);
				return energyobj2.toString();
			} else {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// GetDeltaMeterRPT25
	@RequestMapping(value = "/android/GetAndroidDeltaMeterGraph/{id}/{startdate}/{enddate}/{prmname}/{apiKey}", produces = {
			"application/json" })
	public String getEnergyMeterRpt26(@PathVariable Long id, @PathVariable String startdate,
			@PathVariable String enddate, @PathVariable String prmname, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject obj2 = new JSONObject();
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			if ((JsonUtills.ListToGraphJson3(historyservices.deltameterrpt3(id, startdate, enddate, prmname)))
					.isEmpty()) {
				return "{\"Status\":\"True\",\"Message\":\"No Data!\",\"Data\":[{\"response\":\"No Data\"}]}";
			} else {
				obj2.put("Message", "Data Retrived");
				obj2.put("Status", "True");
				obj2.put("data",JsonUtills.ListToGraphJson4(historyservices.deltameterrpt2(id, startdate, enddate, prmname)));
				return obj2.toString();
			}
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// more info
	@RequestMapping(value = "/android/GetAndroidDigitaldataByDeviceid/{id}/{apiKey}", produces = { "application/json" })
	public String getcomponetlist4(@PathVariable Long id, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		Apikey api = apiService.findBykeyValue(apiKey);
		if (api != null) {
			Lasttrack lastTrack = lasttrackservices.findOne(id);
			Devicemaster device = deviceReop.findBydeviceid(lastTrack.getDeviceId());
			JSONObject digitalJsonObject = new JSONObject(
					new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
			JSONArray digitalArray = new JSONArray(digitalJsonObject.get("Digital").toString());
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode digitalNodearrayNode = mapper.createArrayNode();

			digitalArray.forEach(SingleDigitalObject -> {
				JSONObject digitalObject = (JSONObject) SingleDigitalObject;
				for (int i = 0; i < digitalObject.names().length(); i++) {
					ObjectNode digitalNode = mapper.createObjectNode();
					digitalNode.put(reo.findByid(new Long(digitalObject.names().getString(i))).getPrmname(),
							digitalObject.get(digitalObject.names().getString(i)).toString());
					digitalNode.put("ParameterId", digitalObject.names().getString(i).toString());
					digitalNodearrayNode.add(digitalNode);
				}
			});

			ObjectNode objectNode = mapper.createObjectNode();
			mapper.setDateFormat(df);
			objectNode.putPOJO("Data", digitalNodearrayNode);
			objectNode.putPOJO("DeviceId", lastTrack.getDeviceId());
			objectNode.putPOJO("DeviceDate", lastTrack.getDeviceDate());
			objectNode.putPOJO("DeviceName", device.getDevicename());
			objectNode.putPOJO("Status", "True");
			objectNode.putPOJO("Message", "Retrived");
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// dynamic devicedasboard site wise
	@RequestMapping(value = "/android/GetAndroidManagerSiteList/{managerId}/{apiKey}", produces = {
			"application/json" })
	public String GetAndroidManagerSiteList(@PathVariable Long managerId, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(apiKey);
		JSONObject obj2 = new JSONObject();
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetSiteByManager(managerId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				arry.put(obj);
			});
			obj2.put("Message", "Data Retrived");
			obj2.put("Status", "True");
			obj2.put("data", arry);
			return obj2.toString();

		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/GetAndroidSiteName/{ids}/{apiKey}", produces = { "application/json" })
	public String getsitename(HttpServletRequest request, @PathVariable String ids, @PathVariable String apiKey) {
		Apikey api = apiService.findBykeyValue(apiKey);
		JSONObject obj2 = new JSONObject();
		if (api != null) {
			String[] sids = ids.split(",");
			JSONArray jarray = new JSONArray();
			for (String w : sids) {
				List list = dynamicdashlayout.getSiteList(Long.valueOf(w));
				String siteId = "";
				String siteName = "";

				for (int j = 0; j < list.size(); j++) {
					siteId = w;
					siteName = ((String) list.get(j));
					JSONObject jo = new JSONObject();
					jo.put("siteId", siteId);
					jo.put("siteName", siteName);
					jarray.put(jo);
				}
			}
			obj2.put("Message", "Data Retrived");
			obj2.put("Status", "True");
			obj2.put("data", jarray);
			return obj2.toString();
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	@RequestMapping(value = "/android/GetAndroidSiteDashboard/{siteid}/{apiKey}", produces = { "application/json" })
	public String getcomponetlist3(@PathVariable String siteid, @PathVariable String apiKey)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		Apikey api = apiService.findBykeyValue(apiKey);
		JSONObject obj2 = new JSONObject();
		if (api != null) {
			List<Object[]> deviceList = new ArrayList<>();
			deviceList = Siteservices.GetDeviceIdBySite(Long.valueOf(siteid));
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode analogNodearrayNode = mapper.createArrayNode();
			for (Object[] deviceListresult : deviceList) {
				Lasttrack lastTrack = lasttrackservices.findOne(Long.valueOf(deviceListresult[0].toString()));
				if (lastTrack != null) {
					Devicemaster device = deviceReop.findBydeviceid(lastTrack.getDeviceId());
					JSONObject analogJsonObject = new JSONObject(
							new ObjectMapper().writeValueAsString(lastTrack.getAnalogdigidata()));
					JSONArray analogArray = new JSONArray(analogJsonObject.get("Analog").toString());
					JSONArray analogArray2 = new JSONArray(analogJsonObject.get("Digital").toString());

					ObjectNode analogNode = mapper.createObjectNode();
					analogArray.forEach(SingleAnalogObject -> {
						JSONObject analogObject = (JSONObject) SingleAnalogObject;
						analogNode.put("DeviceId", deviceListresult[0].toString());
						analogNode.put("DeviceName", device.getDevicename());
						analogNode.put("DeviceDate", lastTrack.getDeviceDate().toString());

						for (int i = 0; i < analogObject.names().length(); i++) {
							analogNode.put(reo.findByid(new Long(analogObject.names().getString(i))).getPrmname(),
									analogObject.get(analogObject.names().getString(i)).toString());
							analogNode.put(
									reo.findByid(new Long(analogObject.names().getString(i))).getPrmname() + "1a",
									reo.getLasttrackUnitsNew(device.getDp().getPrid(),
											analogObject.names().getString(i)).toString());
							analogNode.put(
									reo.findByid(new Long(analogObject.names().getString(i))).getPrmname() + "b1",
									reo.findByid(new Long(analogObject.names().getString(i))).getId().toString());
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
			objectNode.putPOJO("Status", "True");
			objectNode.putPOJO("Message", "Retrived");
			objectNode.putPOJO("Data", analogNodearrayNode);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		} else {
			return "{\"Status\":\"False\",\"Message\":\"Session Expired!\",\"Data\":[{\"response\":\"Error In Data\"}]}";
		}
	}

	// Ashish Aj

}

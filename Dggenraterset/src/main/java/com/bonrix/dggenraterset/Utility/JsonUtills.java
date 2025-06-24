package com.bonrix.dggenraterset.Utility;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.config.Task;

import com.bonrix.dggenraterset.Controller.RS32Controller;
import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.SqlJson;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtills {
	private static Logger log = Logger.getLogger(JsonUtills.class);
	static HistoryServices hstservices = ApplicationContextHolder.getContext().getBean(HistoryServices.class);

private static ObjectMapper mapper;
	
	static {
		try {
			  
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String JavaToJson(Object object) {
		String jsonResult = "";
		try {
			jsonResult = mapper.writeValueAsString(object);
			System.out.println(jsonResult);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	public static String convertJsonToJava() {
		return "";
	}

	public static String ListToDataTable(List list) {
		JSONObject json = new JSONObject();
		json.put("data", list);
		return json.toString();
	}

	public static String ListToJava(List list) {
		Type type = new TypeToken<List<Task>>() {
		}.getType();
		String json = new GsonBuilder().serializeNulls().create().toJson(list, type);
		return json;
	}
	
	public static String ListToGraphJson2(List list) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SqlJson sj=new SqlJson();
			Object[] result=(Object[]) list.get(i);
			//var pp= parseInt(value.split(" ")[0],16)/100;
		    sj.setKey(result[0].toString());
	
		    sj.setValue(""+ Float.parseFloat((Integer.toString(Integer.parseInt((result[1].toString()).split(" ")[0],16)/100))));
		    ja.put(sj);		
		    }
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JSONObject obj=new JSONObject(gson.toJson(ja));
		return obj.get("myArrayList").toString();
			}
	
	
	 public static JSONObject mapToJson(Map<?, ?> data)
	    {
	        JSONObject object = new JSONObject();

	        for (Map.Entry<?, ?> entry : data.entrySet())
	        {
	            /*
	             * Deviate from the original by checking that keys are non-null and
	             * of the proper type. (We still defer validating the values).
	             */
	            String key = (String) entry.getKey();
	            if (key == null)
	            {
	                throw new NullPointerException("key == null");
	            }
	            try
	            {
	                object.put(key, wrap(entry.getValue()));
	            }
	            catch (JSONException e)
	            {
	                e.printStackTrace();
	            }
	        }

	        return object;
	    }

	    public static JSONArray collectionToJson(Collection data)
	    {
	        JSONArray jsonArray = new JSONArray();
	        if (data != null)
	        {
	            for (Object aData : data)
	            {
	                jsonArray.put(wrap(aData));
	            }
	        }
	        return jsonArray;
	    }

	    public static JSONArray arrayToJson(Object data) throws JSONException
	    {
	        if (!data.getClass().isArray())
	        {
	            throw new JSONException("Not a primitive data: " + data.getClass());
	        }
	        final int length = Array.getLength(data);
	        JSONArray jsonArray = new JSONArray();
	        for (int i = 0; i < length; ++i)
	        {
	            jsonArray.put(wrap(Array.get(data, i)));
	        }

	        return jsonArray;
	    }

	    private static Object wrap(Object o)
	    {
	        if (o == null)
	        {
	            return null;
	        }
	        if (o instanceof JSONArray || o instanceof JSONObject)
	        {
	            return o;
	        }
	        try
	        {
	            if (o instanceof Collection)
	            {
	                return collectionToJson((Collection) o);
	            }
	            else if (o.getClass().isArray())
	            {
	                return arrayToJson(o);
	            }
	            if (o instanceof Map)
	            {
	                return mapToJson((Map) o);
	            }
	            if (o instanceof Boolean ||
	                    o instanceof Byte ||
	                    o instanceof Character ||
	                    o instanceof Double ||
	                    o instanceof Float ||
	                    o instanceof Integer ||
	                    o instanceof Long ||
	                    o instanceof Short ||
	                    o instanceof String)
	            {
	                return o;
	            }
	            if (o.getClass().getPackage().getName().startsWith("java."))
	            {
	                return o.toString();
	            }
	        }
	        catch (Exception ignored)
	        {
	        }
	        return null;
	    }
	    
	    public static String ListToGraphJson3(List list) {
			JSONArray ja = new JSONArray();

			for (int i = 0; i < list.size(); i++) {
			
			    SqlJson sj=new SqlJson();
				Object[] result=(Object[]) list.get(i);
		        sj.setKey(result[0].toString());
			    sj.setValue(result[1].toString());
			    ja.put(sj);		
			    }
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JSONObject obj=new JSONObject(gson.toJson(ja));
			return obj.get("myArrayList").toString();
				}
	    
	    
	    public static Object ListToGraphJson4(List list) {
			JSONArray ja = new JSONArray();

			for (int i = 0; i < list.size(); i++) {
			    SqlJson sj=new SqlJson();
				Object[] result=(Object[]) list.get(i);
		        sj.setKey(result[0].toString());
			    sj.setValue(result[1].toString());
			    ja.put(sj);		
			    }
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JSONObject obj=new JSONObject(gson.toJson(ja));
			return obj.get("myArrayList");
				}
	    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	        long diffInMillies = date2.getTime() - date1.getTime();
	        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	    }
	    
	    public static String getTimeDiffrence(Date stsrtDate,Date endDate)
	    {
	    	//long timeInMilliSeconds =endDate.getTime() - stsrtDate.getTime();
	    	//System.out.println(timeInMilliSeconds);
	    	/*long seconds = timeInMilliSeconds / 1000;
	    	long minutes = seconds / 60;
	    	long hours = minutes / 60;
	    	long days = hours / 24;
			String time = hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;*/
			//long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliSeconds);
			//return ""+minutes;
	    	long timeInMilliSeconds = endDate.getTime() - stsrtDate.getTime();
	        long seconds = timeInMilliSeconds / 1000L;
	        long minutes = seconds / 60L;
	        long hours = minutes / 60L;
	        long days = hours / 24L;
	        String time = String.valueOf(days) + " Days: " + (hours % 24L) + " Hour: " + (minutes % 60L) + " Minutes: " + (seconds % 60L) + 
	          " Seconds";
	        return time;
	    }
	    
	 
	    
	    public static String dateToMlSeconds(Date stsrtDate)
	    {
	    	long timeInMilliSeconds =stsrtDate.getTime();
	    	//System.out.println(timeInMilliSeconds);
	    	long seconds = timeInMilliSeconds / 1000;
	    	long minutes = seconds / 60;
	    	long hours = minutes / 60;
	    	long days = hours / 24;
			String time = days + " Days: " + hours % 24 + " Hour: " + minutes % 60 + " Minutes: " + seconds % 60
					+ " Seconds";
			//System.out.println(time);
			System.out.println("SAJAN :: "+hours+" :: "+minutes+" :: "+seconds);
			return ""+minutes;
	    	
	    }
	    
	    public static String getNextDate(String curDate) throws ParseException
	    {
	    	final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  	  final Date date = format.parse(curDate);
	  	  final Calendar calendar = Calendar.getInstance();
	  	  calendar.setTime(date);
	  	  calendar.add(Calendar.DAY_OF_YEAR, 1);
	  	  return format.format(calendar.getTime());
	    }
	      
	    public static List<LocalDate> getBetweenDate(String s,String e)
	    {
		LocalDate start = LocalDate.parse(s);
		LocalDate end = LocalDate.parse(e);
		List<LocalDate> totalDates = new ArrayList<>();
		while (!start.isAfter(end)) {
		    totalDates.add(start);
		    start = start.plusDays(1);
		}
		return totalDates;
}
	    
	    public static double fuel(Analogdata analogData,double value)
	    {
	    	double fuel = analogData.getAnaloglevel1().doubleValue() + (value - analogData.getAnalogrange1().doubleValue()) * ((analogData.getAnaloglevel2().doubleValue() - analogData.getAnaloglevel1().doubleValue()) / (analogData.getAnalogrange2().doubleValue() - analogData.getAnalogrange1().doubleValue()));
			return fuel;
	    	
	    }
	    
	    public static List RemoveListDuplicate(List list) {
			List myList = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {

				Object[] a1 = null;
				Object[] a2 = null;

				if (i < list.size() - 1) {
					a1 = (Object[]) list.get(i);
					a2 = (Object[]) list.get(i + 1);
				}

				else {
					a1 = (Object[]) list.get(i);
					a2 = (Object[]) list.get(i - 1);
				}

				if (a1[2].toString().equalsIgnoreCase(a2[2].toString())) {
				} else {
					myList.add(a1);
				}
			}
			return myList;
		}
	    
	    public static JSONObject CalaculateOccurrence (JSONArray arr) {
			int active = 0;
			int clear = 0;
			JSONObject AC = new JSONObject();

			for (Object o : arr) {
			//	log.info("IN JSON CalaculateDIffrence :: ");
				JSONObject element = (JSONObject) o;  
				element.getString("Start Status");
				// http://localhost:8080/Dggenraterset/api/sajan/2019-08-04/2019-08-04/761816/921235
				if (element.getString("Start Status").equalsIgnoreCase("0")) {

					//String stDate = element.getString("Start Date");
					//String stopDate = element.getString("End Date");
					active++;

				} else if (element.getString("Start Status").equalsIgnoreCase("1")) {
					//String stDate = element.getString("Start Date");
					//String stopDate = element.getString("End Date");
					clear++;
				} else {
					//System.out.println("OOOOOOOO");
				}

			}
			System.out.println(active + " :: " + clear);
			if(arr.length()==1)
			AC.append("active", 0);
			else
				AC.append("active", active);
			AC.append("clear", clear);
			return AC;

		}
	    
	    public static JSONArray GetDeltaDigitalsData(String startDate, String endDate, String param, long deviceId) {
		//	JSONObject AC = new JSONObject();
			List<Object[]> lst = (List<Object[]>) hstservices.getSingleDigitalParmData(startDate, endDate, param, deviceId);
			JSONArray mainJSON = new JSONArray();
			if (lst != null && lst.size()!=0) {
				String lstatus = null;
				int ct = 0;  
				for (int i = 0; i < lst.size(); ++i) {
					//log.info("IN JSON LOOP :: "+i);
					final Object[] obj = lst.get(i);
					if (i == 0) {
						lstatus = obj[3].toString();
						final JSONObject jo = new JSONObject();
						jo.put("Start Date", (Object) obj[1].toString());
						jo.put("Start Status", (Object) obj[3].toString());
						mainJSON.put(ct, (Object) jo);
						++ct;
					} else {
						if (lstatus.equalsIgnoreCase(obj[3].toString())) {
							
						} else {
							final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
							innerJSON12.put("End Date", (Object) obj[1].toString());
							mainJSON.put(ct - 1, (Object) innerJSON12);
							final JSONObject jo2 = new JSONObject();
							jo2.put("Start Date", (Object) obj[1].toString());
							jo2.put("Start Status", (Object) obj[3].toString());
							mainJSON.put(ct, (Object) jo2);
							++ct;
						}
						lstatus = obj[3].toString();
					}
				}
			}
		/*	if(param.equalsIgnoreCase("237935"))
			System.out.println(mainJSON);*/
	if(mainJSON.length()!=0)
	{
			JSONObject tot_obj = mainJSON.getJSONObject(mainJSON.length() - 1);
			tot_obj.put("End Date", endDate + " 23:59:59");
			mainJSON.put(mainJSON.length() - 1, tot_obj);
			/*if(param.equalsIgnoreCase("761793"))
				System.out.println(mainJSON);
			 AC = CalaculateOccurrence(mainJSON);*/
	}
			return mainJSON;
			//return mainJSON.toString();
		}
	    
	    public static JSONObject calculateOnOffCount(JSONArray analogArray)
	    {  
	    	//log.info(analogArray);
	    	JSONObject result=new JSONObject();
			JSONObject joF =(JSONObject) analogArray.get(0);
			JSONObject joFLst =(JSONObject) analogArray.get(analogArray.length()-1);
			System.out.println("Before :: "+analogArray);
			if(joF.get("Start Status").equals("1"))
			analogArray.remove(0);
			else
				System.out.println("Not First Clear");
			
			/*if(joFLst.get("Start Status").equals("0"))
				analogArray.remove(analogArray.length()-1);
				else
					System.out.println("Not Last Clear");*/
			
			System.out.println("After :: "+analogArray);
			
				int[] statusArray=new int[analogArray.length()];
				//System.out.println(analogArray.get(1));
				for (int i = 0; i < analogArray.length(); i++) {
				//	System.out.println(i);
					JSONObject jo =(JSONObject) analogArray.get(i);
				//	System.out.println(jo.get("Start Date")+" : "+jo.get("Start Status"));
					statusArray[i]=Integer.parseInt(jo.get("Start Status").toString());
				}
				int active=countOnOffccurrences(statusArray,0);
				int clear=countOnOffccurrences(statusArray,1);
		      //  log.info(java.util.Arrays.toString(statusArray));
		      //  log.info(active);//3
		      //  log.info(clear);//2
				result.put("active",active );
				result.put("clear",clear );
				return result;
		}
	    
	    
	    static int countOnOffccurrences(int arr[], int x)
	    {
	        int res = 0;
	        for (int i=0; i<arr.length; i++)
	            if (x == arr[i])
	              res++;
	        return res;
	    }
	    
	    public static JSONArray GetDeltaAnalogDataACT(String startDate, String endDate, String param, long deviceId) {
			JSONObject AC = new JSONObject();
			List<Object[]> lst = hstservices.getDeltaDataReportSingleParam(startDate,deviceId, param);
			JSONArray mainJSON = new JSONArray();
			if (lst != null && lst.size()!=0) {
				float lstatus = 0;
				int ct = 0;  
				for (int i = 0; i < lst.size(); ++i) {
					//log.info("IN JSON LOOP :: "+i);
					final Object[] obj = lst.get(i);
					if (i == 0) {
						 lstatus= Float.parseFloat(obj[0].toString());
						//lstatus = obj[3].toString();
						final JSONObject jo = new JSONObject();
						jo.put("Start Date", (Object) obj[1].toString());
						jo.put("Start Status", (Object) obj[0].toString());
						mainJSON.put(ct, (Object) jo);
						++ct;
					} else {
						if (lstatus==Float.parseFloat((obj[0].toString()))) {
							
						} else {
							final JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
							innerJSON12.put("End Date", (Object) obj[1].toString());
							mainJSON.put(ct - 1, (Object) innerJSON12);
							final JSONObject jo2 = new JSONObject();
							jo2.put("Start Date", (Object) obj[1].toString());
							jo2.put("Start Status", (Object) obj[0].toString());
							mainJSON.put(ct, (Object) jo2);
							++ct;
						}
						lstatus = Float.parseFloat(obj[0].toString());
					}
				}
			}
			//if(param.equalsIgnoreCase("761793"))
			//System.out.println(mainJSON);
	if(mainJSON.length()!=0)
	{
			JSONObject tot_obj = mainJSON.getJSONObject(mainJSON.length() - 1);
			tot_obj.put("End Date", endDate + " 23:59:59");
			mainJSON.put(mainJSON.length() - 1, tot_obj);
			if(param.equalsIgnoreCase("761793"))
				System.out.println(mainJSON);
			 AC = CalaculateOccurrence(mainJSON);
	}
			return mainJSON;
		}
	    
	    public static long dateToSecond(Date stsrtDate,Date endDate)
	    {
	    	long diff = endDate.getTime() - stsrtDate.getTime();
	    	long diffSeconds = diff / 1000;   
	    	return diffSeconds;
	    }
	    
	    public static String calculateSecondTime(long seconds) {
	    	int day = (int)TimeUnit.SECONDS.toDays(seconds);        
	    	 long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
	    	 long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
	    	 long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

	        return  hours + " Hour " +  minute + " Minute " ;
	       // return  day +" Day " +hours + " Hour " +  minute + " Minute "+ second + " Seconds " ;

	    }
}

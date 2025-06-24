package com.bonrix.dggenraterset.TcpServer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.common.utils.AlertMessage;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.GPSElement;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;

public class L100 {
	private static final Logger log = Logger.getLogger(L100.class);
	  Calendar cal = Calendar.getInstance();
	  double speed = 0.0D;
	  
	  LasttrackRepository lasttrackrepository = ApplicationContextHolder.getContext()
				.getBean(LasttrackRepository.class);

		DevicemasterRepository devicemasterRepository = ApplicationContextHolder.getContext()
				.getBean(DevicemasterRepository.class);

		HistoryRepository histroyrepository = ApplicationContextHolder.getContext().getBean(HistoryRepository.class);
	  
	  public int parseL100(GPSElement msg) throws JsonParseException, JsonMappingException, IOException
	  {
		  String imeinumber = msg.getImei();
	     // device = new devicemasterRepository.(imeinumber);
	      Devicemaster device = devicemasterRepository.findByImei(imeinumber);
	    DeviceProfile profile=device.getDp();
	      String latitude = msg.getLatitude();
	      String longitude = msg.getLangitude();
	      Double angle = Double.valueOf(0.0D);
	      
	      boolean dig1 = false;
	      boolean dig2 = false;
	      boolean dig3 = false;
	      boolean dig4 = false;	
	      boolean dig5 = false;
	      boolean dig6 = false;
	      boolean dig7 = false;
	      boolean dig8 = false;
	      this.cal.setTime(msg.getGpsdate());
	      this.cal.add(12, 330);
	      double finalodometer = 0.0D;
	      
			JSONObject jo = new JSONObject();
	      JSONArray rs232arr = new JSONArray();
	      JSONObject rs232obj = new JSONObject();
	      rs232arr.put(rs232obj);
	      
	      Map<String, Object> prof= profile.getParameters();
	      JSONObject parameters = new JSONObject(profile.getParameters());
	      JSONArray digital = parameters.getJSONArray("Digital");
	      JSONArray analog = parameters.getJSONArray("Analog");

	    //  System.out.println(" SAJAN Analog ::::::::::::::::  "+profile.getProfilename());
	    //  System.out.println(" SAJAN Analog ::::::::::::::::  "+digital.toString());
	      JSONObject digitalReverse = new JSONObject();
	      JSONObject analogData = new JSONObject();
			JSONArray analogjsonarr = new JSONArray();
			JSONArray digitaljsonarr = new JSONArray();

			
	      for (int i = 0; i <digital.length(); i++) {
	    	  JSONObject obj = (JSONObject) digital.get(i);
				
	    	  Double d = Double.parseDouble(obj.get("dioindex").toString());
				if(i==0 && d.intValue()==1)
				{
					
					boolean reverse=(boolean) obj.get("reverse");
					//System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig1())
					  dig1=false;
						else
							dig1=true;
					}
					  else	
						  dig1=msg.getDig1();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig1? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==1 && d.intValue()==2) {
					
					boolean reverse=(boolean) obj.get("reverse");
					//System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig2())
					  dig2=false;
						else
							dig2=true;
					}
					  else
						  dig2=msg.getDig2();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig2? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==2 && d.intValue()==3) {
					
					boolean reverse=(boolean) obj.get("reverse");
					//System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig3())
					  dig3=false;
						else
							dig3=true;
					}
					  else
						  dig3=msg.getDig3();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig3? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==3 && d.intValue()==4) {
					
					boolean reverse=(boolean) obj.get("reverse");
					//System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig4())
					  dig4=false;
						else
							dig4=true;
					}
					  else
						  dig4=msg.getDig4();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig4? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==4 && d.intValue()==5) {
					
					boolean reverse=(boolean) obj.get("reverse");
				//	System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig5())
					  dig5=false;
						else
							dig5=true;
					}
					  else
						  dig5=msg.getDig5();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig5? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==5 && d.intValue()==6) {
					
					boolean reverse=(boolean) obj.get("reverse");
					System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig6())
					  dig6=false;
						else
							dig6=true;
					}
					  else
						  dig6=msg.getDig6();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig6? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}else if(i==6 && d.intValue()==7) {
					
					
					boolean reverse=(boolean) obj.get("reverse");
					System.out.println("D :: "+d.intValue()+" :: "+reverse);
					if(reverse)
					{
						if(msg.getDig7())
					  dig7=false;
						else
							dig7=true;
					}
					  else
						  dig7=msg.getDig7();
					
					digitalReverse = new JSONObject();
					digitalReverse.put(obj.get("parameterId").toString(), dig7? 1 : 0);
					digitaljsonarr.put(digitalReverse);
					
				}
				
				
	      }
	     /// digitaljsonarr.put(digitalReverse);
	      
	      for (int j = 0; j <analog.length(); j++) {
	    	  JSONObject analogObj = (JSONObject) analog.get(j);
	    	  Double d = Double.parseDouble(analogObj.get("analogioindex").toString());
	    		  if(j==0 && d.intValue()==1)
					{
	    			  analogData=new JSONObject();
						analogData.put(analogObj.get("Analoginput").toString(), msg.getAna1());
						 analogjsonarr.put(analogData);
					}else  if(j==1 && d.intValue()==2)
					{
						analogData=new JSONObject();
						analogData.put(analogObj.get("Analoginput").toString(), msg.getAna2());
						 analogjsonarr.put(analogData);
					}else  if(j==2 && d.intValue()==3) {
						analogData=new JSONObject();
						analogData.put(analogObj.get("Analoginput").toString(), msg.getAna3());
						 analogjsonarr.put(analogData);
					}else  if(j==3 && d.intValue()==4) {
						analogData=new JSONObject();
						analogData.put(analogObj.get("Analoginput").toString(), msg.getAna4());
						 analogjsonarr.put(analogData);
					}else  if(j==4 && d.intValue()==5) {
						analogData=new JSONObject();
						analogData.put(analogObj.get("Analoginput").toString(), msg.getAna5());
						 analogjsonarr.put(analogData);
					}
			}
	      analogjsonarr.put(analogData);
	        jo.put("Digital", digitaljsonarr);
			jo.put("Analog", analogjsonarr);
			jo.put("Rs232", rs232arr);
			System.out.println(msg.getAna1());
	     // System.out.println(digitalReverse.toString());
	     // System.out.println(jo.toString());
	      //Assignment Operatio
	      
	      Date insertingdate = cal.getTime().compareTo(new Date()) > 0 ? new Date() : cal.getTime();
	      
	      
	      JSONObject jogps = new JSONObject();
	      jogps.put("latitude", msg.getLatitude());
	      jogps.put("longitude", msg.getLangitude());
	      jogps.put("speed", msg.getSpeed());
	      jogps.put("angle", msg.getAngle());
	      System.out.println("SAJAN GPS :: "+jogps);
	      History hist = new History(device.getDeviceid(), device.getUserId(), insertingdate,
					new Date(), new ObjectMapper().readValue(jo.toString(), Map.class),
					new ObjectMapper().readValue(jogps.toString(), Map.class));
	      

			Lasttrack lTrack = new Lasttrack(device.getDeviceid(), device.getUserId(),
					insertingdate, new Date(),
					new ObjectMapper().readValue(jo.toString(), Map.class),
					new ObjectMapper().readValue(jogps.toString(), Map.class));

			Lasttrack track = lasttrackrepository.findOne(device.getDeviceid());

			AlertMessage alert=new AlertMessage();
			alert.alertCheck(lTrack, track, device);
			
			if (track == null)
				lasttrackrepository.saveAndFlush(lTrack);
			else {
				track.setDeviceDate(insertingdate);
				track.setSystemDate(new Date());
				track.setAnalogdigidata(new ObjectMapper().readValue(jo.toString(), Map.class));
				track.setGpsdata(new ObjectMapper().readValue(jogps.toString(), Map.class));
				lasttrackrepository.saveAndFlush(track);
			}
			histroyrepository.saveAndFlush(hist);
		return 0;
	  
	  }
}

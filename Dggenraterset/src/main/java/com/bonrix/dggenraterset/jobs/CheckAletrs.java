package com.bonrix.dggenraterset.jobs;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;

public class CheckAletrs {

	static LasttrackRepository lasttrackrepository=ApplicationContextHolder.getContext().getBean(LasttrackRepository.class);
	static LasttrackServices lasttrackservices=ApplicationContextHolder.getContext().getBean(LasttrackServices.class);
	
	static DevicemasterServices devicemasterService = ApplicationContextHolder.getContext()
			.getBean(DevicemasterServices.class);
	//LasttrackServices lservice=new Last
   public static void main(String agr[]) throws JsonGenerationException, JsonMappingException, IOException
   {
	 
	  /*System.out.println(t.getAnalogdigidata());
	  Map<String, Object> digitaldata=t.getAnalogdigidata();
	  System.out.println("DATA :: "+digitaldata.get("Digital"));*/
	   Devicemaster deviceMaste=devicemasterService.findOne(2795350l);
	   //DevicemasterRepository.
	   Lasttrack old=lasttrackservices.findOne(2795350l);
	   
	   Lasttrack newlt=lasttrackservices.findOne(2795350l);
	   
	   AnalogAlert alert=new AnalogAlert();
	   alert.alertCheck(newlt, old, deviceMaste);
   }
	
	 public static String checkForAlert(Lasttrack ltrack,Lasttrack oldtrack,Devicemaster device)
	 {
		 
		 return null;
	 }
}

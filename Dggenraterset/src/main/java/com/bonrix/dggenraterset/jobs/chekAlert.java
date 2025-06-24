package com.bonrix.dggenraterset.jobs;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bonrix.common.utils.CallAPI;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Service.Alertmessageshistory;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DigitalInputAlertService;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Utility.ApplicationContextHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class chekAlert {
	private static final Logger log = Logger.getLogger(chekAlert.class);

	DigitalInputAlertService alertservices = ApplicationContextHolder.getContext()
			.getBean(DigitalInputAlertService.class);
	
	DevicemasterServices deviceservices = ApplicationContextHolder.getContext()
			.getBean(DevicemasterServices.class);
	
	Alertmessageshistory msgService = ApplicationContextHolder.getContext()
			.getBean(Alertmessageshistory.class);
	
	SMSTemplateService smsService = ApplicationContextHolder.getContext()
			.getBean(SMSTemplateService.class);
	String sendTmie="";
	 String rulrId="0";
	 String deviceName="";
	 String status="";
	public  String sendMsg(Devicemaster dmastr,Lasttrack ltrack,Lasttrack oldtrack) throws IOException
	{
		log.info("chekAlert :: dmastr.getManagerId() :: "+dmastr.getManagerId());
		 List<DigitalInputAlert> alert=alertservices.findBymanagerid(dmastr.getManagerId());
		 sendTmie=ltrack.getDeviceDate().toString();
		 JsonParser parser = new JsonParser();
		 boolean isAlert=false;
		 deviceName=dmastr.getDevicename().trim();
		 log.info(""+dmastr.getImei()+" chekAlert :: SAJAN :: "+alert.size());
		   
		 if(alert.size()>0 && alert!=null)
		 {
			 for(DigitalInputAlert alt:alert)
			 {
				 if(alt.getDevice_id().compareTo(dmastr.getDeviceid())==0)
				 {
					 log.info(""+dmastr.getImei()+" chekAlert :: In Device Compare True  :: "+alt.getDigitalinput());
				 rulrId=""+alt.getNo();
				// List<AssignSite> assignSite=assigSitenService.findBysiteidAnddeviceid(dmastr.getDeviceid(),alt.getSite_id());
				 log.info(alt.getMobileno());
				 log.info(""+dmastr.getImei()+" chekAlert ::  alt.getDevice_id() :: "+alt.getDevice_id());
				 log.info(""+dmastr.getImei()+" chekAlert ::  alt.getDigitalinput() :: "+alt.getDigitalinput());
				 log.info(""+dmastr.getImei()+" chekAlert ::  dmastr.getDeviceid() :: "+dmastr.getDeviceid());
				 log.info(""+dmastr.getImei()+" chekAlert ::  dalt.getDevice_id()==dmastr.getDeviceid() :: "+(alt.getDevice_id()==dmastr.getDeviceid()));
				 int compareTo=alt.getDevice_id().compareTo(dmastr.getDeviceid());
				 log.info(""+dmastr.getImei()+" chekAlert :: compareTo :: "+compareTo);
				 if(compareTo==0){
					 
				log.info(alt.getDigitalinput());
				 Map<String, Object> ldigitaldata=ltrack.getAnalogdigidata();
				  Map<String, Object> olddigitaldata=oldtrack.getAnalogdigidata();
				
				  Object lobject = parser.parse(ldigitaldata.get("Digital").toString());  
				  Object oldobject = parser.parse(olddigitaldata.get("Digital").toString());  
				  JsonArray  ljsonArr = (JsonArray) lobject;   // Getting c
				  JsonArray  oldjsonArr = (JsonArray) oldobject;   // Getting c
				  log.info(""+dmastr.getImei()+"  ljsonArr "+ljsonArr);
				  log.info(""+dmastr.getImei()+"  oldjsonArr "+oldjsonArr);
					    if(alt.getDigitalinput().equalsIgnoreCase("Mains_Fail"))
					    {
					    	List<String> mobile = new ArrayList<String>();
					    	  String lMains_Fail=((JsonObject) ljsonArr.get(0)).get("237935").toString();
					    	  String oldMains_Fail=((JsonObject) oldjsonArr.get(0)).get("237935").toString();
					    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
					    	log.info(""+dmastr.getImei()+" Mains_Fail chekAlert :: lMains_Fail :: "+lMains_Fail);
					    	log.info(""+dmastr.getImei()+" Mains_Fail chekAlert :: oldMains_Fail :: "+oldMains_Fail);
					    	log.info(""+dmastr.getImei()+" Mains_Fail chekAlert :: template :: "+template.getMessage());
					    	log.info(""+dmastr.getImei()+" Mains_Fail chekAlert :: NOT CONDITION :: "+!lMains_Fail.equalsIgnoreCase(oldMains_Fail));
					    	  if(!lMains_Fail.equalsIgnoreCase(oldMains_Fail))
					    	  {
					    		  String[] mob=alt.getMobileno().split(",");
					    		for(String mo:mob)
					    			mobile.add(mo);
					    		
			    				}
				    			  else
				    			  log.info(""+dmastr.getImei()+" chekAlert :: Invalid Sender Data.");
					    		  
					    		  if(alt.getStatus().equalsIgnoreCase("ON") && lMains_Fail.equalsIgnoreCase("1"))
					    		  {
					    			  sendFCMnotification(alt,template,mobile,status);
					    		  }
					    		   if(alt.getStatus().equalsIgnoreCase("OFF") && lMains_Fail.equalsIgnoreCase("0"))
					    		   {
					    			  sendFCMnotification(alt,template,mobile,status);
					    		   }
					    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
					    		   {
					    			   if(lMains_Fail.equalsIgnoreCase("1"))
					    				   status="CLOSE";
					    				   else
					    					   status="OPEN";
					    			   
					    			  sendFCMnotification(alt,template,mobile,status);
					    		   }
					    	  
					    }
					    if(alt.getDigitalinput().equalsIgnoreCase("Battery_LVD"))
					    {
					    	try
					    	{
					    		 log.info(""+dmastr.getImei()+"  IN Battery_LVD ");
					    		  log.info(""+dmastr.getImei()+"  ljsonArr "+ljsonArr);
								  log.info(""+dmastr.getImei()+"  oldjsonArr "+oldjsonArr);
						    	List<String> mobile = new ArrayList<String>();
						    	  String lBattery_LVD=((JsonObject) ljsonArr.get(1)).get("237937").toString();
						    	  String oldBattery_LVD=((JsonObject) oldjsonArr.get(1)).get("237937").toString();
						    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
						    	log.info(""+dmastr.getImei()+" Battery_LVD chekAlert  :: lBattery_LVD :: "+lBattery_LVD);
						    	log.info(""+dmastr.getImei()+" Battery_LVD chekAlert  :: oldBattery_LVD :: "+oldBattery_LVD);
						    	log.info(""+dmastr.getImei()+" Battery_LVD chekAlert  :: template :: "+template.getMessage());
						    	log.info(""+dmastr.getImei()+" Battery_LVD chekAlert  :: NOT CONDITION :: "+!lBattery_LVD.equalsIgnoreCase(oldBattery_LVD));
						    	  if(!lBattery_LVD.equalsIgnoreCase(oldBattery_LVD))
						    	  {
						    		  String[] mob=alt.getMobileno().split(",");
						    		for(String mo:mob)
						    			mobile.add(mo);
						    		
				    				}
					    			  else
					    			  log.info(""+dmastr.getImei()+" chekAlert :: In valid Sender Data.");
						    		  
						    		  if(alt.getStatus().equalsIgnoreCase("ON") && lBattery_LVD.equalsIgnoreCase("1"))
						    		  {
						    			  sendFCMnotification(alt,template,mobile,"OPEN");
						    		  }
						    		   if(alt.getStatus().equalsIgnoreCase("OFF") && lBattery_LVD.equalsIgnoreCase("0"))
						    		   {
						    			  sendFCMnotification(alt,template,mobile,"CLOSE");
						    		   }
						    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
						    		   {
						    			   if(lBattery_LVD.equalsIgnoreCase("1"))
						    				   status="OPEN";
						    				   else
						    					   status="CLOSE";
						    			   
						    			  sendFCMnotification(alt,template,mobile,status);
						    		   }
					    	}catch (Exception e) {
					    		e.printStackTrace();
								log.error(""+dmastr.getImei()+" :: "+e.getMessage());
							}

					    	
					    	  
					    	  
					    
					    }
					     if(alt.getDigitalinput().equalsIgnoreCase("LOAD_ON_BATTERY"))
					    {
					    	 try
						    	{
					    		 log.info(""+dmastr.getImei()+"  IN LOAD_ON_BATTERY ");
					    		  log.info(""+dmastr.getImei()+"  ljsonArr "+ljsonArr);
					    		 List<String> mobile = new ArrayList<String>();
						    	  String lLOAD_ON_BATTERY=((JsonObject) ljsonArr.get(2)).get("761793").toString();
						    	  String oldLOAD_ON_BATTERY=((JsonObject) oldjsonArr.get(2)).get("761793").toString();
						    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
						    	log.info(""+dmastr.getImei()+" LOAD_ON_BATTERY chekAlert :: lLOAD_ON_BATTERY :: "+lLOAD_ON_BATTERY);
						    	log.info(""+dmastr.getImei()+" LOAD_ON_BATTERY chekAlert :: oldLOAD_ON_BATTERY :: "+oldLOAD_ON_BATTERY);
						    	log.info(""+dmastr.getImei()+" LOAD_ON_BATTERY chekAlert :: template :: "+template.getMessage());
						    	log.info(""+dmastr.getImei()+" LOAD_ON_BATTERY chekAlert :: NOT CONDITION :: "+!lLOAD_ON_BATTERY.equalsIgnoreCase(oldLOAD_ON_BATTERY));
						    	  if(!lLOAD_ON_BATTERY.equalsIgnoreCase(oldLOAD_ON_BATTERY))
						    	  {
						    		  String[] mob=alt.getMobileno().split(",");
						    		for(String mo:mob)
						    			mobile.add(mo);
						    		
				    				}
					    			  else
					    			  log.info(""+dmastr.getImei()+" chekAlert :: In valid Sender Data.");
						    		  
						    		  if(alt.getStatus().equalsIgnoreCase("ON") && oldLOAD_ON_BATTERY.equalsIgnoreCase("1"))
						    		  {
						    			  sendFCMnotification(alt,template,mobile,"OPEN");
						    		  }
						    		   if(alt.getStatus().equalsIgnoreCase("OFF") && oldLOAD_ON_BATTERY.equalsIgnoreCase("0"))
						    		   {
						    			  sendFCMnotification(alt,template,mobile,"CLOSE");
						    		   }
						    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
						    		   {
						    			   if(lLOAD_ON_BATTERY.equalsIgnoreCase("1"))
						    				   status="OPEN";
						    				   else
						    					   status="CLOSE";
						    			  sendFCMnotification(alt,template,mobile,status);
						    		   }
						    	}catch (Exception e) {
						    		e.printStackTrace();
									log.error(""+dmastr.getImei()+" :: "+e.getMessage());
						    	}
					    	
					    	
					    } 
					     if(alt.getDigitalinput().equalsIgnoreCase("LOAD_ON_DG"))
					    {
					    	 try
						    	{
					    		 log.info(""+dmastr.getImei()+"  IN LOAD_ON_DG ");
					    	List<String> mobile = new ArrayList<String>();
					    	  String lLOAD_ON_DG=((JsonObject) ljsonArr.get(3)).get("761816").toString();
					    	  String oldLOAD_ON_DG=((JsonObject) oldjsonArr.get(3)).get("761816").toString();
					    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
					    	log.info(""+dmastr.getImei()+" LOAD_ON_DG chekAlert :: lLOAD_ON_DG :: "+lLOAD_ON_DG);
					    	log.info(""+dmastr.getImei()+" LOAD_ON_DG chekAlert :: oldLOAD_ON_DG :: "+oldLOAD_ON_DG);
					    	log.info(""+dmastr.getImei()+" LOAD_ON_DG chekAlert :: template :: "+template.getMessage());
					    	log.info(""+dmastr.getImei()+" LOAD_ON_DG chekAlert :: NOT CONDITION :: "+!lLOAD_ON_DG.equalsIgnoreCase(oldLOAD_ON_DG));
					    	  if(!lLOAD_ON_DG.equalsIgnoreCase(oldLOAD_ON_DG))
					    	  {
					    		  String[] mob=alt.getMobileno().split(",");
					    		for(String mo:mob)
					    			mobile.add(mo);
					    		
			    				}
				    			  else
				    			  log.info(""+dmastr.getImei()+" chekAlert :: In valid Sender Data.");
					    		  
					    		  if(alt.getStatus().equalsIgnoreCase("ON") && lLOAD_ON_DG.equalsIgnoreCase("1"))
					    			  sendFCMnotification(alt,template,mobile,"OPEN");
					    		   if(alt.getStatus().equalsIgnoreCase("OFF") && lLOAD_ON_DG.equalsIgnoreCase("0"))
					    			  sendFCMnotification(alt,template,mobile,"CLOSE");
					    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
					    		   {
					    			   if(lLOAD_ON_DG.equalsIgnoreCase("1"))
					    				   status="OPEN";
					    				   else
					    					   status="CLOSE";
					    			  sendFCMnotification(alt,template,mobile,status);
					    		   }
						    	}catch (Exception e) {
						    		e.printStackTrace();
									log.error(""+dmastr.getImei()+" :: "+e.getMessage());
						    	}
					    	  
					    } 
					     if(alt.getDigitalinput().equalsIgnoreCase("LOW_BATTRY"))
					    {
					    	 try
						    	{
					    		 log.info(""+dmastr.getImei()+"  IN LOW_BATTRY ");
					    		  log.info(""+dmastr.getImei()+"  ljsonArr "+ljsonArr);
								  log.info(""+dmastr.getImei()+"  oldjsonArr "+oldjsonArr);
					    	List<String> mobile = new ArrayList<String>();
					    	  String lLOW_BATTRY=((JsonObject) ljsonArr.get(4)).get("761827").toString();
					    	  String oldLOW_BATTRY=((JsonObject) oldjsonArr.get(4)).get("761827").toString();
					    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
					    	log.info(""+dmastr.getImei()+" LOW_BATTRY chekAlert :: lLOW_BATTRY :: "+lLOW_BATTRY);
					    	log.info(""+dmastr.getImei()+" LOW_BATTRY chekAlert :: oLOW_BATTRY :: "+oldLOW_BATTRY);
					    	log.info(""+dmastr.getImei()+" LOW_BATTRY chekAlert :: template :: "+template.getMessage());
					    	log.info(""+dmastr.getImei()+" LOW_BATTRY chekAlert :: NOT CONDITION :: "+!lLOW_BATTRY.equalsIgnoreCase(oldLOW_BATTRY));
					    	  if(!lLOW_BATTRY.equalsIgnoreCase(oldLOW_BATTRY))
					    	  {
					    		  String[] mob=alt.getMobileno().split(",");
					    		for(String mo:mob)
					    			mobile.add(mo);
					    		
			    				}   
				    			  else
				    			  log.info(""+dmastr.getImei()+" chekAlert :: In valid Sender Data.");
					    		  
					    		  if(alt.getStatus().equalsIgnoreCase("ON") && lLOW_BATTRY.equalsIgnoreCase("1"))
					    			  sendFCMnotification(alt,template,mobile,"OPEN");
					    		   if(alt.getStatus().equalsIgnoreCase("OFF") && lLOW_BATTRY.equalsIgnoreCase("0"))
					    			  sendFCMnotification(alt,template,mobile,"CLOSE");
					    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
					    		   {
					    			   if(lLOW_BATTRY.equalsIgnoreCase("0"))
							    			  sendFCMnotification(alt,template,mobile,"OPEN");
							    			   else if(lLOW_BATTRY.equalsIgnoreCase("1"))
									    			  sendFCMnotification(alt,template,mobile,"CLOSE");
					    			   /*if(lLOW_BATTRY.equalsIgnoreCase("0"))
					    			  sendFCMnotification(alt,template,mobile,"CLOSE");
					    			   else if(lLOW_BATTRY.equalsIgnoreCase("1"))
							    			  sendFCMnotification(alt,template,mobile,"OPEN");*/
					    		   }
						    	}catch (Exception e) {
						    		e.printStackTrace();
									log.error(""+dmastr.getImei()+" :: "+e.getMessage());
						    	}
					    	
					    } 
					     if(alt.getDigitalinput().equalsIgnoreCase("FUSE_FAIL"))
					    { 
					    	 try
						    	{
					    		 log.info(""+dmastr.getImei()+"  IN FUSE_FAIL ");
					    		  log.info(""+dmastr.getImei()+"  ljsonArr "+ljsonArr);
								  log.info(""+dmastr.getImei()+"  oldjsonArr "+oldjsonArr);
					    	List<String> mobile = new ArrayList<String>();
					    	  String lFUSE_FAIL=((JsonObject) ljsonArr.get(5)).get("11489123").toString();
					    	  String oldlFUSE_FAIL=((JsonObject) oldjsonArr.get(5)).get("11489123").toString();
					    	  MessageTemplate template=smsService.findBymid(alt.getMessagetemplate_id());
					    	log.info(""+dmastr.getImei()+" FUSE_FAIL chekAlert :: lFUSE_FAIL :: "+lFUSE_FAIL);
					    	log.info(""+dmastr.getImei()+" FUSE_FAIL chekAlert :: oldFUSE_FAIL :: "+oldlFUSE_FAIL);
					    	log.info(""+dmastr.getImei()+" FUSE_FAIL chekAlert :: template :: "+template.getMessage());
					    	log.info(""+dmastr.getImei()+" FUSE_FAIL chekAlert :: NOT CONDITION :: "+!lFUSE_FAIL.equalsIgnoreCase(oldlFUSE_FAIL));
					    	  if(!lFUSE_FAIL.equalsIgnoreCase(oldlFUSE_FAIL))
					    	  {
					    		  String[] mob=alt.getMobileno().split(",");
					    		for(String mo:mob)
					    			mobile.add(mo);
					    		
			    				}
				    			  else
				    			  log.info(""+dmastr.getImei()+" chekAlert :: In valid Sender Data.");
					    		  
					    		  if(alt.getStatus().equalsIgnoreCase("ON") && lFUSE_FAIL.equalsIgnoreCase("1"))
					    			  sendFCMnotification(alt,template,mobile,"OPEN");
					    		   if(alt.getStatus().equalsIgnoreCase("OFF") && lFUSE_FAIL.equalsIgnoreCase("0"))
					    			  sendFCMnotification(alt,template,mobile,"CLOSE");
					    		   if(alt.getStatus().equalsIgnoreCase("BOTH"))
					    		   {
					    			   if(lFUSE_FAIL.equalsIgnoreCase("1"))
					    				   status="OPEN";
					    				   else
					    					   status="CLOSE";
					    			  sendFCMnotification(alt,template,mobile,status);
					    		   }
					    		   
						    	}catch (Exception e) {
						    		e.printStackTrace();
									log.error(""+dmastr.getImei()+" :: "+e.getMessage());
						    	}
					    	   
					    }
			 }
			 }else
				 log.info(""+dmastr.getImei()+" chekAlert :: In Device Compare False");   
		 }
			 
		 }
		return "Send With No Error.";
	}
	
	public void sendFCMnotification(DigitalInputAlert alert,MessageTemplate template,List<String> mobileNos,String status) throws IOException
	 {
		 for (String mobileNo : mobileNos) {
			try {
				String tempMsg=template.getMessage().toString();
				tempMsg=tempMsg.replaceAll("\\<Input\\>", alert.getDigitalinput());
				tempMsg=tempMsg.replaceAll("\\<Status\\>", status);
				tempMsg=tempMsg.replaceAll("\\<DateTime\\>", sendTmie);
				tempMsg=tempMsg.replaceAll("\\<Device\\>", deviceName);
				 String sendMsg = URLEncoder.encode(tempMsg, "UTF-8");
				String api = "http://fcmlight.saharshsolutions.co.in/sendSingleMessageAction/sendSingleMessage.do?message=<message>&clientName=tvipl&password=tvipl&phNo=<mobile_number>&senderName=tower&title=Alert";
				api = api.replaceAll("\\<mobile_number\\>", mobileNo);
				api = api.replaceAll("\\<message\\>", sendMsg);
				  String responce=CallAPI.sendGet(api);
				     
					log.info(""+mobileNo+" chekAlert :: sendFCMnotification "+mobileNo);
					AlertMessages msg=new AlertMessages();
					 msg.setDeviceid(alert.getDevice_id());
					 msg.setEntrytime(new Date());
					 msg.setHistoryid(0l);
					 msg.setManagerid(alert.getManagerid());
					 msg.setMessage(URLDecoder.decode(sendMsg, "UTF-8"));
					 msg.setSiteid(alert.getSite_id());
					 msg.setUsergroupid(alert.getUsergroup_id());
					 msg.setUserid(alert.getUser_id());
					 msg.setResponse(responce);
					 msg.setRuleid(rulrId);
					 msg.setAlerttype("DIGITAL");
					 msg.setSentmobile(mobileNo);
					 AlertMessages amsg= msgService.savealertMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
	 }
}

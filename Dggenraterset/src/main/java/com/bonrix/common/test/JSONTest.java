package com.bonrix.common.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] gpdAnalog= {"6387981","6387982"};
		String battry="25";
		String fuel="50";
		 List<String> gpdAnalogNameList = new ArrayList<>(Arrays.asList(gpdAnalog));
		String analog="[{\"5557109\":\"0.003\"},{\"6308790\":\"0.0\"},{\"5557111\":\"0.002\"},{\"6308792\":\"0.0\"},{\"5557117\":\"2624.303\"},{\"6308794\":\"33.85\"},{\"5557118\":\"299.512\"},{\"6308796\":\"2.7\"},{\"5557124\":\"54.08\"},{\"6337564\":\"53.6\"},{\"6337565\":\"1.4\"},{\"6337570\":\"31.7\"},{\"6337573\":\"33.6\"},{\"6337574\":\"231\"},{\"6337577\":\"0\"},{\"6337582\":\"0\"},{\"6337583\":\"100\"},{\"6337585\":\"37\"},{\"6337588\":\"600\"},{\"237921\":\"37\"},{\"6387968\":\"77.52\"},{\"6387970\":\"4.04\"},{\"6387973\":\"1543.9\"},{\"237931\":8.390752e+36},{\"237932\":\"0\"},{\"237933\":\"0\"},{\"6387975\":\"0\"},{\"6387976\":\"1608.24\"},{\"6387980\":\"1720.81\"}]";
	//	String analog="[{\"5557109\":\"0.003\"},{\"6308790\":\"0.0\"},{\"5557111\":\"0.002\"},{\"6308792\":\"0.0\"},{\"5557117\":\"2624.303\"},{\"6308794\":\"33.85\"},{\"5557118\":\"299.512\"},{\"6308796\":\"2.7\"},{\"5557124\":\"54.08\"},{\"6337564\":\"53.6\"},{\"6337565\":\"1.4\"},{\"6337570\":\"31.7\"},{\"6337573\":\"33.6\"},{\"6337574\":\"231\"},{\"6337577\":\"0\"},{\"6337582\":\"0\"},{\"6337583\":\"100\"},{\"6337585\":\"37\"},{\"6337588\":\"600\"},{\"237921\":\"37\"},{\"6387968\":\"77.52\"},{\"6387970\":\"4.04\"},{\"6387973\":\"1543.9\"},{\"237931\":8.390752e+36},{\"237932\":\"0\"},{\"237933\":\"0\"},{\"6387975\":\"0\"},{\"6387976\":\"1608.24\"},{\"6387980\":\"1720.81\"},{\"6387981\":\"25\"},{\"6387982\":\"50\"}]";
		JSONArray analogArray = new JSONArray(analog); 
		JSONArray jsonDataArray = new JSONArray(analog); 
		System.out.println(analogArray.length());
		boolean isInsert=false;
		for (int i = 0; i < analogArray.length(); i++) {
		    JSONObject jsonObj = analogArray.getJSONObject(i);
		    String k = jsonObj.keys().next();
		    System.out.println(i);
		    if(gpdAnalogNameList.contains(k))
	        {
		    	isInsert=true;
	        	JSONObject analogObj = new JSONObject();
	        	if(k.equalsIgnoreCase("6387981"))
	        	analogObj.put(k,battry+"25");
	        	else if(k.equalsIgnoreCase("6387982"))
	        		analogObj.put(k,fuel+"50");
	        	  
	        	jsonDataArray.put(analogObj);
	        }
		}
		if(!isInsert)
		{
			JSONObject analogbattryObj = new JSONObject();
			analogbattryObj.put("6387981",battry);
			JSONObject analogfuelObj = new JSONObject();
			analogfuelObj.put("6387982",fuel);
			jsonDataArray.put(analogbattryObj);
			jsonDataArray.put(analogfuelObj);
		}
		System.out.println(jsonDataArray);

	}

}

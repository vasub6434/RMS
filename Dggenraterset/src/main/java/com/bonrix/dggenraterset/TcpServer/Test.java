package com.bonrix.dggenraterset.TcpServer;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Formatter;

import com.bonrix.dggenraterset.Utility.StringToolsV3;

public class Test {

	public static void main(String[] args) throws ParseException {
		String msg="ATL862211074210105,$GPRMC,092845,A,2301.2283,N,07238.2378,E,0.0,0,220325,,,*22,#01111011000000,0.00,-70.00,0,0.10,33,4.2,24,404,5,61b3,c13be15ATL, ATLMB862211074210105,092902,220325,@01,03,0001,04,zzzzzzzz@02,03,0001,04,zzzzzzzz@03,03,0001,04,45aa@04,03,0001,04,zzzzzzzz@05,03,0001,04,zzzzzzzz@ATLMB2";
		String[] msgary = msg.split(",");
		String digitalData=msgary[14];
		String imei="";
		 String ACMAINS_FAIL=digitalData.substring(1,2);
	      String Fire=digitalData.substring(2,3);
	      String Door=digitalData.substring(3,4);
	      String DG_Running_Hrs=digitalData.substring(4,5);
	      String DG_Fault=digitalData.substring(5,6);
	      String Battry_Low=digitalData.substring(6,7);
	      String PP_Input_Fail=digitalData.substring(7,8);
	      
	     /* System.out.println("CV2EnergyMeterDevice "+imei+" GPS Main INPUTS :: "+digitalData);
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS GREEN ACMAINS_FAIL :: "+digitalData.substring(1,2));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Yellow Ornge Fire ::"+digitalData.substring(2,3));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Yellow Door Door :: "+digitalData.substring(3,4));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Yellow Black DG_Running_Hrs :: "+digitalData.substring(4,5));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Yellow Brown DG_Fault :: "+digitalData.substring(5,6));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Green Black Battry_Low :: "+digitalData.substring(6,7));
	      System.out.println("CV2EnergyMeterDevice "+imei+" GPS Yellow Red :: PP_Input_Fail "+digitalData.substring(7,8));*/
	      System.out.println(ConvertMODBUSRTUValue("3EEC0831"));
	      
	      Double latitude = 0.0d;
			Double Langitude = 0.0d;
			boolean isvalid = Boolean.valueOf("A".equals(msgary[3]));
			if (isvalid) {
			if ((msgary[4] == "") || (msgary[4] == null)) {
				latitude = StringToolsV3.parseLatitude("0.0", "N");
			} else {
				latitude = StringToolsV3.parseLatitude(msgary[4], "N");
			}
			if ((msgary[6] == "") || (msgary[6] == null)) {
				Langitude = StringToolsV3.parseLatitude("0.0", "E");
			} else {
				Langitude = StringToolsV3.parseLatitude(msgary[6], "E");
			}
			}
			System.out.println(latitude);
			System.out.println(Langitude);
	      
	}
	static String ConvertMODBUSRTUValue(String hexVal)
	{
        Long i1 = Long.parseLong(hexVal, 16);
        Float f1 = Float.intBitsToFloat(i1.intValue());
        return String.valueOf(f1);
	}
	
	
}

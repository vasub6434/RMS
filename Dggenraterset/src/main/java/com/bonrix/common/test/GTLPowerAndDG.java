package com.bonrix.common.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.TcpServer.GTLEnergyMeterDevice;
import com.bonrix.dggenraterset.jobs.MyAlerts;
import com.bonrix.dggenraterset.jobs.MyAnalogAlert;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GTLPowerAndDG {

	public static void main(String[] args) {
		
		final Logger log = Logger.getLogger(GTLPowerAndDG.class);
		//String msg ="ATL865293041119301,060180,015454,1,000030:1770FFD6,000032:05F30000,000034:150D064E,000036:000002D4,000038:02D80000,000040:00000000,000048:08BB0842,000050:089E,000053:005A0CD6,000124:XXXXXXXX,000126:00000000,000128:400B4286,000146:449C0D1F,000152:449CDD45,000160:3E8A3D71,000162:44246333,000164:44200666,000166:00000000,000168:00000000,000175:0365,000028:21E51504,ATLD";
		String msg="ATL865293041119301,060180,015812,2,016388:00DF00DD,016890:XXXX,016391:0000FD30,016393:XXXX,016394:00000000,016396:XXXX,016397:0000,016398:XXXX,016399:0000,016400:XXXX,016401:0000,016402:XXXX,016403:XXXX,016404:XXXX,016405:2C17,016408:XXXXXXXX,016410:XXXX,016411:XXXXXXXX,016413:XXXX,ATLM";
		
		ObjectMapper mapper = new ObjectMapper();

		String[] msgary = msg.split(",");
		String imei = msgary[0].substring(5);
		log.info("GTLPowerAndDG " + imei + " : " + msg);

		
			if (msgary[3].contains("1")) {   
				log.info("GTLEnergyMeterDevice " + imei + " Analog :: Analog String 1");
				
				String Register_000028=msgary[24].split(":")[1];
				String RECTIFIER_OUTPUT_POWER = Register_000028.substring(0, 4);
				String RECTIFIER_OUTPUT_VOLTAGE = Register_000028.substring(4, 8);
				
				String Register_000030=msgary[4].split(":")[1];
				String BATERY_CAPACITY = Register_000030.substring(0, 4);
				String BATTERY_CURRENT = Register_000030.substring(4, 8);
				
				String Register_000032=msgary[5].split(":")[1];
				String RECTIFIER_OUTPUT_CURRENT = Register_000032;
				
				String Register_000034=msgary[6].split(":")[1];
				String SITE_BATT_VOLTAGE = Register_000034.substring(0, 4);
				String TOTAL_LOAD_CURRENT = Register_000034.substring(4, 8);
				
				String Register_000036=msgary[7].split(":")[1];
				String LOAD_CURRENT1 = Register_000036.substring(0, 4);
				String LOAD_CURRENT2 = Register_000036.substring(4, 8);
				
				String Register_000038=msgary[8].split(":")[1];
				String LOAD_CURRENT3 = Register_000038.substring(0, 4);
				String LOAD_CURRENT4 = Register_000038.substring(4, 8);
				
				String Register_000040=msgary[9].split(":")[1];
				String LOAD_CURRENT5 = Register_000040;
				
				String Register_000048=msgary[10].split(":")[1];
				String MAINS_VOLTAGE_R = Register_000048.substring(0, 4);
				String MAINS_VOLTAGE_Y = Register_000048.substring(4, 8);
				
				String Register_000050=msgary[11].split(":")[1];
				String MAINS_VOLTAGE_B = Register_000050;
				
				String Register_000053=msgary[12].split(":")[1];
				String SOC = Register_000053.substring(0, 4);
				String BATTERY_TEMERATURE = Register_000053.substring(4, 8);
				
				String Register_000124=msgary[13].split(":")[1];
				String Mains_RUN_HRS =ieee(Register_000124);
				
				String Register_000126=msgary[14].split(":")[1];
				String DG_Total_RUNNING_HRS =ieee(Register_000126);
				
				String Register_000128=msgary[15].split(":")[1];
				String BATTERY_RUN_HRS =ieee(Register_000128);
				
				String Register_000146=msgary[16].split(":")[1];
				String RECTIFIER_OUTPUT_ENERGY =ieee(Register_000146);
				
				String Register_000152=msgary[17].split(":")[1];
				String TOTAL_LOAD_ENERGY =ieee(Register_000152);
				
				String Register_000160=msgary[18].split(":")[1];
				String LOAD_1_ENERGY =ieee(Register_000160);
				
				String Register_000162=msgary[19].split(":")[1];
				String LOAD_2_ENERGY =ieee(Register_000162);
				
				String Register_000164=msgary[20].split(":")[1];
				String LOAD_3_ENERGY =ieee(Register_000164);
				
				String Register_000166=msgary[21].split(":")[1];
				String LOAD_4_ENERGY =ieee(Register_000166);
				
				String Register_000168=msgary[22].split(":")[1];
				String LOAD_5_ENERGY =ieee(Register_000168);
				
				String Register_000175=msgary[23].split(":")[1];
				String RECTIFIER_OUTPUT_POWER1 =ieee(Register_000175);
				
				
				
				
			}else if (msgary[3].contains("2")) {
				log.info("GTLEnergyMeterDevice " + imei + " Analog :: Analog String 2");
				String Register_16388=msgary[4].split(":")[1];
				String DG_Mains_Ph_1 =Register_16388.substring(0, 4);
				String DG_Mains_Ph_2 =Register_16388.substring(4, 8);
				
				String Register_16390=msgary[5].split(":")[1];
				String DG_Mains_Ph_3 =Register_16390;
				
				String Register_16391=msgary[6].split(":")[1];
				String DG_Ph_1 =Register_16391.substring(0, 4);
				String DG_Ph_2 =Register_16391.substring(4, 8);
				
				
				String Register_16393=msgary[7].split(":")[1];
				String DG_Ph_3 =Register_16393;
				
				
				String Register_16394=msgary[8].split(":")[1];
				String DG_Load_Current_Ph_1=Register_16394.substring(0, 4);
				String DG_Load_Current_Ph_2=Register_16394.substring(4, 8);
				
				String Register_16396=msgary[9].split(":")[1];
				String DG_Load_Current_Ph_3=Register_16396;
				
				String Register_16397_98=msgary[10].split(":")[1] + msgary[11].split(":")[1];
				String DG_Mains_KWH=Register_16397_98;
				
				String Register_16399_00=msgary[12].split(":")[1] + msgary[13].split(":")[1];
				String DG_KWH=Register_16399_00;
				
				String Register_16401_402=msgary[14].split(":")[1] + msgary[15].split(":")[1];
				String DG_Run_HRS=Register_16401_402;
				
				String Register_16403_404=msgary[16].split(":")[1] + msgary[17].split(":")[1];
				String DG_Mains_Run_HRS=Register_16403_404;
				
				String Register_16405=msgary[18].split(":")[1];
				String DG_Battery=Register_16405;
				
				String Register_16408=msgary[19].split(":")[1];
				String DG_Mains_KW_R=Register_16408.substring(0, 4);
				String DG_Mains_KW_Y=Register_16408.substring(4, 8);
				
				String Register_16410=msgary[20].split(":")[1];
				String DG_Mains_KW_B=Register_16410;
				
				String Register_16411=msgary[21].split(":")[1];
				String DG_KW_R=Register_16411.substring(0, 4);
				String DG_KW_Y=Register_16411.substring(4, 8);
				
				 String Register_16413=msgary[22].split(":")[1];
				String DG_KW_B=Register_16413;

				
				
				
			}else {
				    	log.info("GTLPowerAndDG "+imei+" Analog :: Invalid Analog String.");
				    }

		
	
	
	}
	public static String ieee(String ieeeVal) {
		Long i = Long.parseLong(ieeeVal, 16);
		return "" + Float.intBitsToFloat(i.intValue());
	}
}

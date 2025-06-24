package com.bonrix.dggenraterset.DTO;

import java.util.Date;
import java.util.Map;

public class EnergyMeterDTO {

	private Boolean livedata;
	private String imei;
	private Date devicedate;
	//latitude,logitude,speed(IN KNOTS),
	private Map<String, Object> gpsdata;
	
	
}

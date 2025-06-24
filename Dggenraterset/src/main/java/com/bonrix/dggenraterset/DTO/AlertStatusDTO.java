package com.bonrix.dggenraterset.DTO;

public class AlertStatusDTO {

	private String alertName;
	private long count;
	
	
	public AlertStatusDTO(String alertName, long count) {
		super();
		this.alertName = alertName;
		this.count = count;
	}


	public String getAlertName() {
		return alertName;
	}


	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}


	public long getCount() {
		return count;
	}


	public void setCount(long count) {
		this.count = count;
	}
	
	
}
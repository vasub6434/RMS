package com.bonrix.dggenraterset.DTO;

public class AlertTrendDto {

	private String day; 
	private long alerts; 

	public AlertTrendDto() {
	}

	public AlertTrendDto(String day, long alerts) {
		this.day = day;
		this.alerts = alerts;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public long getAlerts() {
		return alerts;
	}

	public void setAlerts(long alerts) {
		this.alerts = alerts;
	}
}
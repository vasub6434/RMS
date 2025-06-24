package com.bonrix.dggenraterset.DTO;

import java.util.Map;

public class AlertFrequency {

	private String day;
	private int hour;
	private long hourlyFrequency;

	public AlertFrequency(String day, int hour, long hourlyFrequency) {
		this.day = day;
		this.hour = hour;
		this.hourlyFrequency = hourlyFrequency;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public long getHourlyFrequency() {
		return hourlyFrequency;
	}

	public void setHourlyFrequency(long hourlyFrequency) {
		this.hourlyFrequency = hourlyFrequency;
	}

}
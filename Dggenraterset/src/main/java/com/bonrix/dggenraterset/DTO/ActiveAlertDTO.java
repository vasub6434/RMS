package com.bonrix.dggenraterset.DTO;

public class ActiveAlertDTO {
	private Long deviceId;
	private String sitename;
	private String alertname;
	private String since;

	public ActiveAlertDTO(Long deviceId, String sitename, String alertname, String since) {
		this.deviceId = deviceId;
		this.sitename = sitename;
		this.alertname = alertname;
		this.since = since;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getAlertname() {
		return alertname;
	}

	public void setAlertname(String alertname) {
		this.alertname = alertname;
	}

	public String getSince() {
		return since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	
}
package com.bonrix.dggenraterset.DTO;

public class RecentAlertLogDTO {
	private String siteName;
	private Long deviceId;
	private String alertName;
	private String startTime;
	private String endTime;
	private String duration;
	private String status;

	public RecentAlertLogDTO(String siteName, Long deviceId, String alertName, String startTime, String endTime,
			String duration, String status) {
		this.siteName = siteName;
		this.deviceId = deviceId;
		this.alertName = alertName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.status = status;
	}

	// getters only
	public String getSiteName() {
		return siteName;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public String getAlertName() {
		return alertName;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getDuration() {
		return duration;
	}

	public String getStatus() {
		return status;
	}
}

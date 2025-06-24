package com.bonrix.dggenraterset.DTO;

public class AlertCountBySiteDTO {

	private String site;
	private String alert;
	private Long count;

	public AlertCountBySiteDTO(String site, String alert, Long count) {
		this.site = site;
		this.alert = alert;
		this.count = count;
	}

	
	public String getSite() {
		return site;
	}

	public String getAlert() {
		return alert;
	}

	public Long getCount() {
		return count;
	}
}

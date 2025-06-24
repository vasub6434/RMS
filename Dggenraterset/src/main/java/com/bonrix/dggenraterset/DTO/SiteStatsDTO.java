package com.bonrix.dggenraterset.DTO;

public class SiteStatsDTO {

	private String sitename;
	private int count;

	public SiteStatsDTO() {
	}

	public SiteStatsDTO(String sitename, int count) {
		this.sitename = sitename;
		this.count = count;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	
}
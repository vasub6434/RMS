package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="dynamiclayout")
public class DynamicDashBoardLayout {
		
	@Id
	@GeneratedValue
	@Column
	private Long dyid;
	
	@Column
	private String dyname;
	
	@Column
	private Long dlid;
	
	@Column
	private String deviceid;
	
	@Column
	private String siteid;
	
	@Column
	private Long managerid;
	
	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	public Long getDlid() {
		return dlid;
	}

	public void setDlid(Long dlid) {
		this.dlid = dlid;
	}

	public Long getDyid() {
		return dyid;
	}

	public void setDyid(Long dyid) {
		this.dyid = dyid;
	}

	public String getDyname() {
		return dyname;
	}

	public void setDyname(String dyname) {
		this.dyname = dyname;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}


}
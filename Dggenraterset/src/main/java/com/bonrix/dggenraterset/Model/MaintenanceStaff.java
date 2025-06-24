package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Maintenancestaff")
public class MaintenanceStaff {

	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue
	@Column
	long id;
	
	@Column(name = "name")
	String name;
	
	@Column(name = "mobile")
	String mobile;
	
	@Column(name = "deviceid")   
	long deviceid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(long deviceid) {
		this.deviceid = deviceid;
	}

}

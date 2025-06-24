package com.bonrix.dggenraterset.Model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ihreport")
public class IHReport {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	long id;
	
	@Column(name = "entrydate")
	Date entryDate;
	
	@Column(name = "startdate")
	String startDate;
	
	
	@Column(name = "enddate")
	String endDate;
	
	@Column(name = "status")
	String StartStatus;
	
	@Column(name = "parameterid")
	long ParameterId;
	
	
	@Column(name = "open")
	String Open;
	
	@Column(name = "close")
	String Close;
	
	@Column(name = "deviceid")
	long DeviceId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartStatus() {
		return StartStatus;
	}

	public void setStartStatus(String startStatus) {
		StartStatus = startStatus;
	}

	public long getParameterId() {
		return ParameterId;
	}

	public void setParameterId(long parameterId) {
		ParameterId = parameterId;
	}

	public String getClose() {
		return Close;
	}

	public void setClose(String close) {
		Close = close;
	}

	public String getOpen() {
		return Open;
	}

	public void setOpen(String open) {
		Open = open;
	}

	public long getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(long deviceId) {
		DeviceId = deviceId;
	}
	
	
}

package com.bonrix.dggenraterset.Model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alearsummary")
public class AlearSummary {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "srno")
	private Long srno;

	@Column(name = "parameterid")
	long parameterId;

	@Column(name = "parametername")
	String parametername;

	@Column(name = "entrytime")
	Date entryTime;

	@Column(name = "starttime")
	Date startTime;

	@Column(name = "endtime")
	Date endTime= null;;

	@Column(name = "duration")
	long duration=0;

	@Column(name = "deviceid")
	long deviceId;

	@Column(name = "managerid")
	long managerId;

	@Column(name = "isactive")
	boolean isActive;

	public Long getSrno() {
		return srno;
	}

	public void setSrno(Long srno) {
		this.srno = srno;
	}

	public long getParameterId() {
		return parameterId;
	}

	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}

	public String getParametername() {
		return parametername;
	}

	public void setParametername(String parametername) {
		this.parametername = parametername;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}

	public boolean isIsactive() {
		return isActive;
	}

	public void setIsactive(boolean isactive) {
		this.isActive = isactive;
	}

}

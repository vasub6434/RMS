package com.bonrix.dggenraterset.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "socketmessagelog")
public class SocketMessageLog {

	@Id
	//@GeneratedValue
	@Column(name = "socketid")
	long socketid;

	@Column(name = "deviceId")
	long deviceId;

	@Column(name = "managerId")
	long managerId;

	@Column(name = "message")
	String message;

	@Column(name = "responce")
	String responce;
	
	@Column(name = "commandtime")
	Date commandtime;

	public long getSocketid() {
		return socketid;
	}

	public void setSocketid(long socketid) {
		this.socketid = socketid;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponce() {
		return responce;
	}

	public void setResponce(String responce) {  
		this.responce = responce;
	}

	public Date getCommandtime() {
		return commandtime;
	}

	public void setCommandtime(Date commandtime) {
		this.commandtime = commandtime;
	}
	
	

}

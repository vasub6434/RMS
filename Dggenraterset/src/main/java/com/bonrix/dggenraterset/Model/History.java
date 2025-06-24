package com.bonrix.dggenraterset.Model;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "history")
public class History {

	public History(Long deviceId, Long userId, Date deviceDate, Date systemDate, Map<String, Object> analogdigidata,
			Map<String, Object> gpsdata) throws JsonParseException, JsonMappingException, IOException {
		super();
		this.deviceId = deviceId;
		this.userId = userId;
		this.DeviceDate = deviceDate;
		this.SystemDate = systemDate;
		this.gpsdata = gpsdata;
		this.analogdigidata = analogdigidata;
		if (this.devicedata == null)
		       this.devicedata = new ObjectMapper().readValue(new JSONObject().toString(), Map.class);
	}
	
	public History(Long deviceId, Long userId, Date deviceDate, Date systemDate, Map<String, Object> analogdigidata,
			Map<String, Object> gpsdata,Map<String, Object> devicedata) {
		super();
		this.deviceId = deviceId;
		this.userId = userId;
		this.DeviceDate = deviceDate;
		this.SystemDate = systemDate;
		this.gpsdata = gpsdata;
		this.analogdigidata = analogdigidata;
		this.devicedata=devicedata;
	}

	public History() {

	}

	@Id
	@GeneratedValue
	@Column(name = "no")
	private Long no;
	@Column(name = "DeviceId")
	private Long deviceId;
	@Column(name = "userId")
	private Long userId;
	@Column(name = "DeviceDate")
	private Date DeviceDate;
	@Column(name = "SystemDate")
	private Date SystemDate;
	@Type(type = "JsonDataUserType")
	@Column(name = "digitaldata")
	private Map<String, Object> analogdigidata;

	@Type(type = "JsonDataUserType")
	@Column(name = "gpsdata")
	private Map<String, Object> gpsdata;

	@Type(type = "JsonDataUserType")
	@Column(name = "devicedata")
	private Map<String, Object> devicedata;

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDeviceDate() {
		return DeviceDate;
	}

	public void setDeviceDate(Date deviceDate) {
		DeviceDate = deviceDate;
	}

	public Date getSystemDate() {
		return SystemDate;
	}

	public void setSystemDate(Date systemDate) {
		SystemDate = systemDate;
	}

	public Map<String, Object> getAnalogdigidata() {
		return analogdigidata;
	}

	public void setAnalogdigidata(Map<String, Object> analogdigidata) {
		this.analogdigidata = analogdigidata;
	}

	public Map<String, Object> getGpsdata() {
		return gpsdata;
	}

	public void setGpsdata(Map<String, Object> gpsdata) {
		this.gpsdata = gpsdata;
	}

	public Map<String, Object> getDevicedata() {
		return devicedata;
	}

	public void setDevicedata(Map<String, Object> devicedata) {
		this.devicedata = devicedata;
	}

}

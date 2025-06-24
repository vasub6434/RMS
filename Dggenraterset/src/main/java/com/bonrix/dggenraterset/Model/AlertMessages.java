package com.bonrix.dggenraterset.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alertmessageshistory")
public class AlertMessages {

	@Id
	@GeneratedValue
	@Column   
	private Long alertid;

	@Column(name = "deviceid")
	private Long deviceid;

	@Column(name = "historyid")
	private Long historyid;

	@Column(name = "entrytime")
	private Date entrytime;

	@Column(name = "userid")
	private Long userid;

	@Column(name = "usergroupid")
	private Long usergroupid;

	@Column(name = "siteid")
	private Long siteid;

	@Column(name = "managerid")
	private Long managerid;
  
	@Column
	private String message;
	
	@Column(name = "response")
	private String response;

	
	@Column(name = "ruleid")
	private String ruleid;
	
	@Column(name = "alerttype")
	private String alerttype;
	
	@Column(name = "sentmobile")
	private String sentmobile;
	
	
	
	public String getAlerttype() {
		return alerttype;
	}

	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getRuleid() {
		return ruleid;
	}

	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}

	

	public Long getAlertid() {
		return alertid;
	}

	public void setAlertid(Long alertid) {
		this.alertid = alertid;
	}

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public Long getHistoryid() {
		return historyid;
	}

	public void setHistoryid(Long historyid) {
		this.historyid = historyid;
	}

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getUsergroupid() {
		return usergroupid;
	}

	public void setUsergroupid(Long usergroupid) {
		this.usergroupid = usergroupid;
	}

	public Long getSiteid() {
		return siteid;
	}

	public void setSiteid(Long siteid) {
		this.siteid = siteid;
	}

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSentmobile() {
		return sentmobile;
	}

	public void setSentmobile(String sentmobile) {
		this.sentmobile = sentmobile;
	}

	}

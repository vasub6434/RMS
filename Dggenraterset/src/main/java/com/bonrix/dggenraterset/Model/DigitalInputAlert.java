package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "digitalinputalert")
public class DigitalInputAlert {
	@Id
	@GeneratedValue
	@Column   
	private Long no;

	@Column
	private Long user_id;

	@Column
	private Long usergroup_id;

	@Column
	private Long deviceid;

	@Column
	private Long site_id;

	@Column
	private String digitalinput;

	@Column
	private String status;

	@Column
	private String mobileno;

	@Column
	private String email_id;

	@Column
	private String alertlimit;

	@Column
	private Long messagetemplate_id;

	@Column
	private Long emailtemplate_id;

	@Column
	private String timing;

	@Column
	private String notification;

	@Column
	private Long managerid;

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getUsergroup_id() {
		return usergroup_id;
	}

	public void setUsergroup_id(Long usergroup_id) {
		this.usergroup_id = usergroup_id;
	}

	public Long getDevice_id() {
		return deviceid;
	}

	public void setDevice_id(Long deviceid) {
		this.deviceid = deviceid;
	}

	public Long getSite_id() {
		return site_id;
	}

	public void setSite_id(Long site_id) {
		this.site_id = site_id;
	}

	public String getDigitalinput() {
		return digitalinput;
	}

	public void setDigitalinput(String digitalinput) {
		this.digitalinput = digitalinput;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getAlertlimit() {
		return alertlimit;
	}

	public void setAlertlimit(String alertlimit) {
		this.alertlimit = alertlimit;
	}

	public Long getMessagetemplate_id() {
		return messagetemplate_id;
	}

	public void setMessagetemplate_id(Long messagetemplate_id) {
		this.messagetemplate_id = messagetemplate_id;
	}

	public Long getEmailtemplate_id() {
		return emailtemplate_id;
	}

	public void setEmailtemplate_id(Long emailtemplate_id) {
		this.emailtemplate_id = emailtemplate_id;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

}

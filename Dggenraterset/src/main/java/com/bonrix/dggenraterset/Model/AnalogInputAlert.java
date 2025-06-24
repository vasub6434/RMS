package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "analoginputalert")
public class AnalogInputAlert {
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
	private String analoginput;



	@Column
	private String mobileno;

	@Column
	private String email_id;

	@Column
	private String conditionstring;
	
	@Column
	private Long conditionvalue;

	@Column
	private Long timedifference;

	@Column
	private Long alertlimit;

	@Column
	private Long avgtime;

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

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public Long getSite_id() {
		return site_id;
	}

	public void setSite_id(Long site_id) {
		this.site_id = site_id;
	}

	public String getAnaloginput() {
		return analoginput;
	}

	public void setAnaloginput(String analoginput) {
		this.analoginput = analoginput;
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

	public String getConditionstring() {
		return conditionstring;
	}

	public void setConditionstring(String conditionstring) {
		this.conditionstring = conditionstring;
	}

	public Long getConditionvalue() {
		return conditionvalue;
	}

	public void setConditionvalue(Long conditionvalue) {
		this.conditionvalue = conditionvalue;
	}

	public Long getTimedifference() {
		return timedifference;
	}

	public void setTimedifference(Long timedifference) {
		this.timedifference = timedifference;
	}

	public Long getAlertlimit() {
		return alertlimit;
	}

	public void setAlertlimit(Long alertlimit) {
		this.alertlimit = alertlimit;
	}

	public Long getAvgtime() {
		return avgtime;
	}

	public void setAvgtime(Long avgtime) {
		this.avgtime = avgtime;
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

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	@Override
	public String toString() {
		return "AnalogInputAlert [no=" + no + ", user_id=" + user_id + ", usergroup_id=" + usergroup_id + ", deviceid="
				+ deviceid + ", site_id=" + site_id + ", analoginput=" + analoginput + ", mobileno=" + mobileno
				+ ", email_id=" + email_id + ", conditionstring=" + conditionstring + ", conditionvalue="
				+ conditionvalue + ", timedifference=" + timedifference + ", alertlimit=" + alertlimit + ", avgtime="
				+ avgtime + ", messagetemplate_id=" + messagetemplate_id + ", emailtemplate_id=" + emailtemplate_id
				+ ", timing=" + timing + ", notification=" + notification + ", managerid=" + managerid + "]";
	}
	
	


	}

package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "profileAnalogSetting")
public class ProfileAnalogSetting {
	
	@Id
	@GeneratedValue
	@Column
	private Long pridSettId;
	
	@Column
	private String profilename;
	
	@Column
	private String paramname;
	
	@Column
	private String warnMin;
	
	@Column
	private String failMin;
	
	@Column
	private String ruleSignature;
	
	@Column
	private Long userId;
	

	public Long getPridSettId() {
		return pridSettId;
	}

	public void setPridSettId(Long pridSettId) {
		this.pridSettId = pridSettId;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getWarnMin() {
		return warnMin;
	}

	public void setWarnMin(String warnMin) {
		this.warnMin = warnMin;
	}

	public String getFailMin() {
		return failMin;
	}

	public void setFailMin(String failMin) {
		this.failMin = failMin;
	}

	public String getRuleSignature() {
		return ruleSignature;
	}

	public void setRuleSignature(String ruleSignature) {
		this.ruleSignature = ruleSignature;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}

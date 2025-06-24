package com.bonrix.dggenraterset.Model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.bonrix.dggenraterset.Utility.JsonDataUserType;


@Entity
@Table(name = "profileDigitalSetting")
@TypeDef(name = "JsonDataUserType", typeClass = JsonDataUserType.class)
public class ProfileDigitalSetting {
	
	@Id
	@GeneratedValue
	@Column
	private Long pridSettId;
	
	@Column
	private String profilename;
	
	@Column
	private String paramname;
	
	@Column
	private String criticalness;
	
	@Column
	private Long userId;
	/*@Type(type = "JsonDataUserType")
	@Column
	private Map<String, Object> digitalSetting;*/
	

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

	public String getCriticalness() {
		return criticalness;
	}

	public void setCriticalness(String criticalness) {
		this.criticalness = criticalness;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/*public Map<String, Object> getDigitalSetting() {
		return digitalSetting;
	}

	public void setDigitalSetting(Map<String, Object> digitalSetting) {
		this.digitalSetting = digitalSetting;
	}*/


}

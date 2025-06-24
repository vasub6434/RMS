package com.bonrix.dggenraterset.Model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Type;

import com.bonrix.dggenraterset.Utility.JsonDataUserType;

@Entity
@Table(name = "deviceprofile")
@TypeDef(name = "JsonDataUserType", typeClass = JsonDataUserType.class)
public class DeviceProfile {
	
	@Id
	@GeneratedValue
	@Column
	private Long prid;
	
	@Column
	private String profilename;
	
	@Type(type = "JsonDataUserType")
	@Column
	private Map<String, Object> parameters;
	
	

	@Type(type = "JsonDataUserType")
	@Column
	private Map<String, Object> gpsdata;

	public Long getPrid() {
		return prid;
	}

	public void setPrid(Long prid) {
		this.prid = prid;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getGpsdata() {
		return gpsdata;
	}

	public void setGpsdata(Map<String, Object> gpsdata) {
		this.gpsdata = gpsdata;
	}
	

}

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
@Table(name = "imagecordinator")
@TypeDef(name = "JsonDataUserType", typeClass = JsonDataUserType.class)
public class Imagecordinator {

	@Id
	@GeneratedValue 
	@Column
	private Long no;
	
	@Column
	private Integer deviceid_fk;
	
	@Column
	//Analog,Digital,Rs232
	private String parameterType;
	
	@Column
	//ac,power,fuel
	private String parameter;
	
	@Column
	@Type(type = "JsonDataUserType")
	private Map<String, Object> cordinate;
	
	@Column
	private Integer imgId_fk;
	
	
	public Long getNo() {
		return no;
	}


	public void setNo(Long no) {
		this.no = no;
	}


	public Integer getDeviceid_fk() {
		return deviceid_fk;
	}


	public void setDeviceid_fk(Integer deviceid_fk) {
		this.deviceid_fk = deviceid_fk;
	}


	public String getParameterType() {
		return parameterType;
	}


	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}


	public String getParameter() {
		return parameter;
	}


	public void setParameter(String parameter) {
		this.parameter = parameter;
	}


	public Map<String, Object> getCordinate() {
		return cordinate;
	}


	public void setCordinate(Map<String, Object> cordinate) {
		this.cordinate = cordinate;
	}


	public Integer getImgId_fk() {
		return imgId_fk;
	}


	public void setImgId_fk(Integer imgId_fk) {
		this.imgId_fk = imgId_fk;
	}


	
}

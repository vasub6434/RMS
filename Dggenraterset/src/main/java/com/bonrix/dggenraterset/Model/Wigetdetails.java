package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wigetdetails")
public class Wigetdetails {

	@Id
	@GeneratedValue
	@Column
	private Long id;
	@Column
	private String  Wigetname;
	@Column
	@Enumerated(EnumType.ORDINAL)
	private com.bonrix.common.enums.Parameter parameter;
	@Column
	private String  Wigeturl;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWigetname() {
		return Wigetname;
	}
	public void setWigetname(String wigetname) {
		Wigetname = wigetname;
	}
	public com.bonrix.common.enums.Parameter getParameter() {
		return parameter;
	}
	public void setParameter(com.bonrix.common.enums.Parameter parameter) {
		this.parameter = parameter;
	}
	public String getWigeturl() {
		return Wigeturl;
	}
	public void setWigeturl(String wigeturl) {
		Wigeturl = wigeturl;
	}
	
	
}

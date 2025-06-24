package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="devicewigetallocation")
public class Devicewigetallocation {
	
	@Id
	@GeneratedValue
	@Column
	private Long id;
	@Column
	@Enumerated(EnumType.ORDINAL)
	private com.bonrix.common.enums.Parameter parameter;
	
	@Column
	private Long dpid_fk;
	
	@Column
	private String parametertype;
	
	@Column
	private Long wigetid_fk;
	
	
	@Column
	private Long  userid_fk;
	
	
	public Long getUserid_fk() {
		return userid_fk;
	}

	public void setUserid_fk(Long userid_fk) {
		this.userid_fk = userid_fk;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public com.bonrix.common.enums.Parameter getParameter() {
		return parameter;
	}

	public void setParameter(com.bonrix.common.enums.Parameter parameter) {
		this.parameter = parameter;
	}

	public Long getDpid_fk() {
		return dpid_fk;
	}

	public void setDpid_fk(Long dpid_fk) {
		this.dpid_fk = dpid_fk;
	}

	public String getParametertype() {
		return parametertype;
	}

	public void setParametertype(String parametertype) {
		this.parametertype = parametertype;
	}

	public Long getWigetid_fk() {
		return wigetid_fk;
	}

	public void setWigetid_fk(Long wigetid_fk) {
		this.wigetid_fk = wigetid_fk;
	}

	
	
	

}

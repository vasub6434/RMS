package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="assigndashboardusers")
public class AssignDashboardUsers {
  
	@Id
	@GeneratedValue
	@Column
	private Long assid;
	
	@Column
	private Long managerdashid;
	
	@Column
	private Long userid;
	
	public Long getManagerdashId() {
		return managerdashid;
	}
	public void setManagerdashId(Long managerdashId) {
		this.managerdashid = managerdashId;
	}
	
	public Long getAssid() {
		return assid;
	}

	public void setAssid(Long assid) {
		this.assid = assid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	
}
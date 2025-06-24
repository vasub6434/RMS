package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assignusergroup")
public class AssignUserGroup {

	
	@Id
	@GeneratedValue
	@Column
	private Long id;
	
	@Column
	private Long usrgroupid;
	
	@Column
	private Long userid;
	
	@Column
	private Long managerid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsrgroupid() {
		return usrgroupid;
	}

	public void setUsrgroupid(Long usrgroupid) {
		this.usrgroupid = usrgroupid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}
	
}

package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usergroup")
public class UserGroup {

	@Id
	@GeneratedValue
	@Column
	private Long usergroupid;
	
	@Column
	private String usergroupname;
	
	@Column
	private Long managerid;
	
	@Column
	private Boolean isActive;

	public Long getUsergroupid() {
		return usergroupid;
	}

	public void setUsergroupid(Long usergroupid) {
		this.usergroupid = usergroupid;
	}

	public String getUsergroupname() {
		return usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	

	
}

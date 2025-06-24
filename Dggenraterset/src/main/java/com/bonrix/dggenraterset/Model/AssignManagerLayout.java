package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="assignmanagerlayout")
public class AssignManagerLayout {
	
	@Id
	@GeneratedValue
	@Column
	private Long assignid;
	
	public Long getDlid() {
		return dlid;
	}

	public void setDlid(Long dlid) {
		this.dlid = dlid;
	}

	@Column
	private Long dlid;
	
	@Column
	private Long managerid;

	public Long getManagerid() {
		return managerid;
	}

	public void setManagerid(Long managerid) {
		this.managerid = managerid;
	}

	public Long getAssignid() {
		return assignid;
	}

	public void setAssignid(Long assignid) {
		this.assignid = assignid;
	}


	
}
package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dashboarddetails")
public class Dashboarddetails {
	
	@Id
	@Column
	@GeneratedValue
	private Long id;
	
	@Column
	private String dashboardname;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDashboardname() {
		return dashboardname;
	}

	public void setDashboardname(String dashboardname) {
		this.dashboardname = dashboardname;
	}

	

}

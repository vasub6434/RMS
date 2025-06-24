package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dashboarddata")
public class Dashboarddata {

	@Id
	@GeneratedValue
	@Column
	private Long id;
	
	@Column
	private Integer dashboardid_fk;
	
	@Column
	private Long deviceid_fk;
	
	
	
}

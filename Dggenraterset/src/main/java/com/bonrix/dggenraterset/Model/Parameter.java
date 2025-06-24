package com.bonrix.dggenraterset.Model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parameter")
public class Parameter {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String prmname;
	
	@Column
	private String prmtype;
	
	public String getPrmname() {
		return prmname;
	}

	public void setPrmname(String prmname) {
		this.prmname = prmname;
	}

	public String getPrmtype() {
		return prmtype;
	}

	public void setPrmtype(String prmtype) {
		this.prmtype = prmtype;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

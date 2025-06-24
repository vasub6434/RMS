package com.bonrix.dggenraterset.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "systemparameter")
public class SystemParameter {

	
	@Id
	@GeneratedValue
	@Column
	private Long id;
	
	@Column
	private String prmname;
	
	@Column
	private String prmvalue;
	
	@Column
	private String prmnotes;
	
	@Column
	private Date addedon;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrmname() {
		return prmname;
	}

	public void setPrmname(String prmname) {
		this.prmname = prmname;
	}

	public String getPrmvalue() {
		return prmvalue;
	}

	public void setPrmvalue(String prmvalue) {
		this.prmvalue = prmvalue;
	}

	public String getPrmnotes() {
		return prmnotes;
	}

	public void setPrmnotes(String prmnotes) {
		this.prmnotes = prmnotes;
	}

	public Date getAddedon() {
		return addedon;
	}

	public void setAddedon(Date addedon) {
		this.addedon = addedon;
	}
	
}

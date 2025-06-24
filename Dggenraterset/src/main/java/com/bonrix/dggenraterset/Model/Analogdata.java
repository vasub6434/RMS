package com.bonrix.dggenraterset.Model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "analog_data")
public class Analogdata {
	  
	private Integer id;
	
	private Double analogrange1;

	private Double analogrange2;

	private Double analoglevel1;
	
	private Double analoglevel2;
	
	private long managerid;
	
	private Devicemaster device ;
	
	
	
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "deviceid_fk")
	public Devicemaster getDevice() {
		return device;
	}

	public void setDevice(Devicemaster device) {
		this.device = device;
	}
	
	@Id
	@GeneratedValue
	@Column
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="range1")
	public Double getAnalogrange1() {
		return analogrange1;
	}

	public void setAnalogrange1(Double analogrange1) {
		this.analogrange1 = analogrange1;
	}
	@Column(name="range2")
	public Double getAnalogrange2() {
		return analogrange2;
	}

	public void setAnalogrange2(Double analogrange2) {
		this.analogrange2 = analogrange2;
	}
	@Column(name="level1")
	public Double getAnaloglevel1() {
		return analoglevel1;
	}

	public void setAnaloglevel1(Double analoglevel1) {
		this.analoglevel1 = analoglevel1;
	}

	@Column(name="level2")
	public Double getAnaloglevel2() {
		return analoglevel2;
	}

	public void setAnaloglevel2(Double analoglevel2) {
		this.analoglevel2 = analoglevel2;
	}

	public long getManagerid() {
		return managerid;
	}

	public void setManagerid(long managerid) {
		this.managerid = managerid;
	}

}

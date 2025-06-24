package com.bonrix.dggenraterset.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "devicefailurenotice")
public class DeviceFailureNotice {

	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long no;

	
	@Column(name = "userid")
	private Long userid;
	

	@Column(name = "failureminute")
	private Long failureminute;

	@Column(name = "warningminute")
	private Long warningminute;

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getFailureminute() {
		return failureminute;
	}

	public void setFailureminute(Long failureminute) {
		this.failureminute = failureminute;
	}

	public Long getWarningminute() {
		return warningminute;
	}

	public void setWarningminute(Long warningminute) {
		this.warningminute = warningminute;
	}


}
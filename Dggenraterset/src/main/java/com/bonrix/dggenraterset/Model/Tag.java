package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "tag")
public class Tag {
	@Id
	@GeneratedValue
	@Column
	 private int no;
	@Column 
	private String tag;
	@Column    
	private String subTag;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getSubTag() {
		return subTag;
	}
	public void setSubTag(String subTag) {
		this.subTag = subTag;
	}

	
}

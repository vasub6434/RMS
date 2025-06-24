package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "componet_image")
public class Componet_image {

	@Id
	@GeneratedValue
	@Column
	private Long id;
	
	@Column
	private Long componetId_fk;
	
	@Column
	private Long imageId_fk;

	public Long getComponetId_fk() {
		return componetId_fk;
	}

	public void setComponetId_fk(Long componetId_fk) {
		this.componetId_fk = componetId_fk;
	}

	public Long getImageId_fk() {
		return imageId_fk;
	}

	public void setImageId_fk(Long imageId_fk) {
		this.imageId_fk = imageId_fk;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

}

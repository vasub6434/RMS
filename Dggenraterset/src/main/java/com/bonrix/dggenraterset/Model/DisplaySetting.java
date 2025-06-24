package com.bonrix.dggenraterset.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "displaysetting")
public class DisplaySetting {
	
	@Id
	@GeneratedValue
	@Column
	private Long id;
	
	@Column
	private String url;
	
	@Column
	private String title;
	
	@Column
	private String subTitle;
	
	@Column
	private String address;
	
	@Column
	private String aboutus;
	
	@Column
	private String logoImage;
	
	@Column
	private String image2;
	
	@Column
	private String userName;
	
	@Column
	private String homeurl;
	
	@Column
	private String extraInfo1;
	
	@Column
	private String extraInfo2;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAboutus() {
		return aboutus;
	}

	public void setAboutus(String aboutus) {
		this.aboutus = aboutus;
	}

	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHomeurl() {
		return homeurl;
	}

	public void setHomeurl(String homeurl) {
		this.homeurl = homeurl;
	}

	public String getExtraInfo1() {
		return extraInfo1;
	}

	public void setExtraInfo1(String extraInfo1) {
		this.extraInfo1 = extraInfo1;
	}

	public String getExtraInfo2() {
		return extraInfo2;
	}

	public void setExtraInfo2(String extraInfo2) {
		this.extraInfo2 = extraInfo2;
	}

}

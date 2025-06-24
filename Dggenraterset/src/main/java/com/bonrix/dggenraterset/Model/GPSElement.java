package com.bonrix.dggenraterset.Model;
import java.util.Date;

public class GPSElement {

	private String imei;
	
	private String latitude;
	private String langitude;
	private String speed;
	private Date gpsdate;
	private String altitude="0";
	
	private Boolean isvalid=true;
	private Boolean islive=true;
	
	private Boolean dig1=false;
	private Boolean dig2=false;
	private Boolean dig3=false;
	private Boolean dig4=false;
	private Boolean dig5=false;
	private Boolean dig6=false;
	private Boolean dig7=false;
	private Boolean dig8=false;
	
	private String ana1="0";
	private String ana2="0";
	private String ana3="0";
	private String ana4="0";
	private String ana5="0";
	
	private String angle="0";
	private String odometer="0";
	
	
	
	public Boolean getIslive() {
		return islive;
	}
	public void setIslive(Boolean islive) {
		this.islive = islive;
	}
	
	public Boolean getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(Boolean isvalid) {
		this.isvalid = isvalid;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLangitude() {
		return langitude;
	}
	public void setLangitude(String langitude) {
		this.langitude = langitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public Date getGpsdate() {
		return gpsdate;
	}
	public void setGpsdate(Date gpsdate) {
		this.gpsdate = gpsdate;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public Boolean getDig1() {
		return dig1;
	}
	public void setDig1(Boolean dig1) {
		this.dig1 = dig1;
	}
	public Boolean getDig2() {
		return dig2;
	}
	public void setDig2(Boolean dig2) {
		this.dig2 = dig2;
	}
	public Boolean getDig3() {
		return dig3;
	}
	public void setDig3(Boolean dig3) {
		this.dig3 = dig3;
	}
	public Boolean getDig4() {
		return dig4;
	}
	public void setDig4(Boolean dig4) {
		this.dig4 = dig4;
	}
	public Boolean getDig5() {
		return dig5;
	}
	public void setDig5(Boolean dig5) {
		this.dig5 = dig5;
	}
	public String getAna1() {
		return ana1;
	}
	public void setAna1(String ana1) {
		this.ana1 = ana1;
	}
	public String getAna2() {
		return ana2;
	}
	public void setAna2(String ana2) {
		this.ana2 = ana2;
	}
	public String getAna3() {
		return ana3;
	}
	public void setAna3(String ana3) {
		this.ana3 = ana3;
	}
	public String getAna4() {
		return ana4;
	}
	public void setAna4(String ana4) {
		this.ana4 = ana4;
	}
	public String getAna5() {
		return ana5;
	}
	public void setAna5(String ana5) {
		this.ana5 = ana5;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getOdometer() {
		return odometer;
	}
	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}
	
	public Boolean getDig6() {
		return dig6;
	}
	public void setDig6(Boolean dig6) {
		this.dig6 = dig6;
	}
	public Boolean getDig7() {
		return dig7;
	}
	public void setDig7(Boolean dig7) {
		this.dig7 = dig7;
	}
	public Boolean getDig8() {
		return dig8;
	}
	public void setDig8(Boolean dig8) {
		this.dig8 = dig8;
	}
	
	
	
	
	
	
	
}

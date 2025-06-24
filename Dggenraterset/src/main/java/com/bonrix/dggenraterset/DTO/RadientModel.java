package com.bonrix.dggenraterset.DTO;

public class RadientModel {

	String Fual;
	String Battery;
	String Count;
	String IMEI;
	String Signal;
	String Location;
	String Cell_ID;
	public String getFual() {
		return Fual;
	}
	public void setFual(String fual) {
		Fual = fual;
	}
	public String getBattery() {
		return Battery;
	}
	public void setBattery(String battery) {
		Battery = battery;
	}
	public String getCount() {
		return Count;
	}
	public void setCount(String count) {
		Count = count;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getSignal() {
		return Signal;
	}
	public void setSignal(String signal) {
		Signal = signal;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getCell_ID() {
		return Cell_ID;
	}
	public void setCell_ID(String cell_ID) {
		Cell_ID = cell_ID;
	}
	@Override
	public String toString() {
		return "RadientModel [Fual=" + Fual + ", Battery=" + Battery + ", Count=" + Count + ", IMEI=" + IMEI
				+ ", Signal=" + Signal + ", Location=" + Location + ", Cell_ID=" + Cell_ID + "]";
	}
	
	
}

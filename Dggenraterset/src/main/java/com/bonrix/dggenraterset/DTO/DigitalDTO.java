package com.bonrix.dggenraterset.DTO;

public class DigitalDTO {
	
	String Prmname;
	String Status;
	String Start_Date;
	String End_Date;
	String Total_Time;
	
	public DigitalDTO(String prmname, String status, String start_Date, String end_Date, String total_Time) {
		super();
		Prmname = prmname;
		Status = status;
		Start_Date = start_Date;
		End_Date = end_Date;
		Total_Time = total_Time;
	}

	public String getPrmname() {
		return Prmname;
	}

	public void setPrmname(String prmname) {
		Prmname = prmname;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getStart_Date() {
		return Start_Date;
	}

	public void setStart_Date(String start_Date) {
		Start_Date = start_Date;
	}

	public String getEnd_Date() {
		return End_Date;
	}

	public void setEnd_Date(String end_Date) {
		End_Date = end_Date;
	}

	public String getTotal_Time() {
		return Total_Time;
	}

	public void setTotal_Time(String total_Time) {
		Total_Time = total_Time;
	}
}

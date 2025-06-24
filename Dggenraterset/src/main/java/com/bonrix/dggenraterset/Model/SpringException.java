package com.bonrix.dggenraterset.Model;

@SuppressWarnings("serial")
public class SpringException extends Exception
{
	public SpringException(boolean status, String message) {
		this.status = status;
		this.excptionmessage = message;
	}
	private boolean status;
	private String excptionmessage;
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getExcptionmessage() {
		return excptionmessage;
	}

	public void setExcptionmessage(String excptionmessage) {
		this.excptionmessage = excptionmessage;
	}
		@Override
		public String getMessage() {
			System.out.println("In message");
			return super.getMessage();
		}
	@Override
	public String toString() {
		return "{\"status\":"+ status +", \"message\":\"" + excptionmessage + "\"}";
	} 
	
}

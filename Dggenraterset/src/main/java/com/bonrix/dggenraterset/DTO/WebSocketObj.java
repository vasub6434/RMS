package com.bonrix.dggenraterset.DTO;
import java.net.Socket;
import java.util.Date;

import org.jboss.netty.channel.MessageEvent;

public class WebSocketObj {

	public String imei;
	public Boolean status;
	public Date entDate;  
	public MessageEvent skt;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getEntDate() {
		return entDate;
	}

	public void setEntDate(Date entDate) {
		this.entDate = entDate;
	}

	public MessageEvent getSkt() {
		return skt;
	}

	public void setSkt(MessageEvent skt) {
		this.skt = skt;
	}

}
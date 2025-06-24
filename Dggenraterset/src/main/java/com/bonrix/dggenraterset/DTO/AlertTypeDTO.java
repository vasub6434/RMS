package com.bonrix.dggenraterset.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

public class AlertTypeDTO {
    private boolean success;  
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date from;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date to;
    private Long profileId;
    private int totalAlerts;
    private List<AlertType> alertTypes;

    // Constructor
    public AlertTypeDTO(boolean success, Date from, Date to,Long profileId ,int totalAlerts, List<AlertType> alertTypes) {
        this.success = success;
        this.from = from;
        this.to = to;
        this.profileId=profileId;
        this.totalAlerts = totalAlerts;
        this.alertTypes = alertTypes;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public Date getFrom() {
        return from;
    }

    public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public Date getTo() {
        return to;
    }

    
    public int getTotalAlerts() {
        return totalAlerts;
    }

    public List<AlertType> getAlertTypes() {
        return alertTypes;
    }
}
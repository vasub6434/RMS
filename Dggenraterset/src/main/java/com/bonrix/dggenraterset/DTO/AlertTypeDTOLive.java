package com.bonrix.dggenraterset.DTO;

import java.util.List;

/**
 * Simplified version of AlertTypeDTO without date fields  
 * to match the existing API implementation
 */
public class AlertTypeDTOLive {
    private boolean success;
    private Long profileId;
    private int totalAlerts;
    private List<AlertType> alertTypes;
    
    // Constructor
    public AlertTypeDTOLive(boolean success, Long profileId, int totalAlerts, List<AlertType> alertTypes) {
        this.success = success;
        this.profileId = profileId;
        this.totalAlerts = totalAlerts;
        this.alertTypes = alertTypes;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Long getProfileId() {
        return profileId;
    }
    
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
    
    public int getTotalAlerts() {
        return totalAlerts;
    }
    
    public void setTotalAlerts(int totalAlerts) {
        this.totalAlerts = totalAlerts;
    }
    
    public List<AlertType> getAlertTypes() {
        return alertTypes;
    }
    
    public void setAlertTypes(List<AlertType> alertTypes) {
        this.alertTypes = alertTypes;
    }
}
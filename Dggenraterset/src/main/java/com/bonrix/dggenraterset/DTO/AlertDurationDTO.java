package com.bonrix.dggenraterset.DTO;

public class AlertDurationDTO {
    private String alertType;
    private int avgDurationMinutes;
    
    public AlertDurationDTO(String alertType, int avgDurationMinutes) {
        this.alertType = alertType;
        this.avgDurationMinutes = avgDurationMinutes;
    }
    
    public String getAlertType() {
        return alertType;
    }
    
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }
    
    public int getAvgDurationMinutes() {
        return avgDurationMinutes;
    }
    
    public void setAvgDurationMinutes(int avgDurationMinutes) {
        this.avgDurationMinutes = avgDurationMinutes;
    }
}
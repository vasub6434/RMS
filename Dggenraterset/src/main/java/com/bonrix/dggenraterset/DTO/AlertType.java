package com.bonrix.dggenraterset.DTO;

public class AlertType {

    private String alertType;
    private int count;
    private double percentage;

    // Constructor
    public AlertType(String alertType, int count) {
        this.alertType = alertType;
        this.count = count;
    }

    // Getters and Setters
    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}

package com.bonrix.dggenraterset.DTO;

public class AlertSummary {

	 private Long totalAlerts;
	    private Long activeCount;
	    private Long clearCount;
	    private Long criticalCount;

	    // Constructor
	    public AlertSummary(Long totalAlerts, Long activeCount, Long clearCount, Long criticalCount) {
	        this.totalAlerts = totalAlerts;
	        this.activeCount = activeCount;
	        this.clearCount = clearCount;
	        this.criticalCount = criticalCount;
	    }

	    // Getters and Setters
	    public Long getTotalAlerts() {
	        return totalAlerts;
	    }

	    public void setTotalAlerts(Long totalAlerts) {
	        this.totalAlerts = totalAlerts;
	    }

	    public Long getActiveCount() {
	        return activeCount;
	    }

	    public void setActiveCount(Long activeCount) {
	        this.activeCount = activeCount;
	    }

	    public Long getClearCount() {
	        return clearCount;
	    }

	    public void setClearCount(Long clearCount) {
	        this.clearCount = clearCount;
	    }

	    public Long getCriticalCount() {
	        return criticalCount;
	    }

	    public void setCriticalCount(Long criticalCount) {
	        this.criticalCount = criticalCount;
	    }
	    
	    
}





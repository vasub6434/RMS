package com.bonrix.dggenraterset.DTO;

public class SuccessResponse {

	 private boolean success = true;
	    private AlertSummary summary;
	    private Period period;

	    // Constructor
	    public SuccessResponse(AlertSummary summary, String fromDate, String toDate) {
	        this.summary = summary;
	        this.period = new Period(fromDate, toDate);
	    }

	    // Getters and Setters
	    public boolean isSuccess() {
	        return success;
	    }

	    public void setSuccess(boolean success) {
	        this.success = success;
	    }

	    public AlertSummary getSummary() {
	        return summary;
	    }

	    public void setSummary(AlertSummary summary) {
	        this.summary = summary;
	    }

	    public Period getPeriod() {
	        return period;
	    }

	    public void setPeriod(Period period) {
	        this.period = period;
	    }

	    // Inner class for Period
	    public static class Period {
	        private String from;
	        private String to;

	        // Constructor
	        public Period(String from, String to) {
	            this.from = from;
	            this.to = to;
	        }

	        // Getters and Setters
	        public String getFrom() {
	            return from;
	        }

	        public void setFrom(String from) {
	            this.from = from;
	        }

	        public String getTo() {
	            return to;
	        }

	        public void setTo(String to) {
	            this.to = to;
	        }
	    }
}

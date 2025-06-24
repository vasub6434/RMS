package com.bonrix.dggenraterset.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AlertTimelineResponse {

    private boolean success;
    private Period period;
    private List<TimelineData> timeline;
    private Summary summary;

    // Constructor
    public AlertTimelineResponse(boolean success, Period period, List<TimelineData> timeline, Summary summary) {
        this.success = success;
        this.period = period;
        this.timeline = timeline;
        this.summary = summary;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
  
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<TimelineData> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineData> timeline) {
        this.timeline = timeline;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    // Period Inner Class
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

    // TimelineData Inner Class
    public static class TimelineData {
        private String timestamp;
        private int new_alerts;
        private int resolved_alerts;

        // Constructor
        public TimelineData(String timestamp, int new_alerts, int resolved_alerts) {
            this.timestamp = timestamp;
            this.new_alerts = new_alerts;
            this.resolved_alerts = resolved_alerts;
        }

        // Getters and Setters
        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public int getNew_alerts() {
            return new_alerts;
        }

        public void setNew_alerts(int new_alerts) {
            this.new_alerts = new_alerts;
        }

        public int getResolved_alerts() {
            return resolved_alerts;
        }

        public void setResolved_alerts(int resolved_alerts) {
            this.resolved_alerts = resolved_alerts;
        }
    }

    // Summary Inner Class
    public static class Summary {
        private int total_new_alerts;
        private int total_resolved_alerts;

        // Constructor
        public Summary(int total_new_alerts, int total_resolved_alerts) {
            this.total_new_alerts = total_new_alerts;
            this.total_resolved_alerts = total_resolved_alerts;
        }

        // Getters and Setters
        public int getTotal_new_alerts() {
            return total_new_alerts;
        }

        public void setTotal_new_alerts(int total_new_alerts) {
            this.total_new_alerts = total_new_alerts;
        }

        public int getTotal_resolved_alerts() {
            return total_resolved_alerts;
        }

        public void setTotal_resolved_alerts(int total_resolved_alerts) {
            this.total_resolved_alerts = total_resolved_alerts;
        }
    }
}

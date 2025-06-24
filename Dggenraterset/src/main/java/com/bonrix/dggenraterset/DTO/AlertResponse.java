package com.bonrix.dggenraterset.DTO;

import java.util.List;

import com.bonrix.dggenraterset.Model.AlertMessages;


public class AlertResponse {
    private boolean success;
    private int count;
    private Metrics metrics;
    private List<AlertMessages> data;

    public AlertResponse(boolean success, int count, Metrics metrics, List<AlertMessages> data) {
        this.success = success;
        this.count = count;
        this.metrics = metrics;
        this.data = data;
        
        
    }

    
    public AlertResponse(String string, int intValue, double d) {
		// TODO Auto-generated constructor stub
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public Metrics getMetrics() {
		return metrics;
	}


	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}


	public List<AlertMessages> getData() {
		return data;
	}


	public void setData(List<AlertMessages> data) {
		this.data = data;
	}


	public static class Metrics {
        private int activeAlerts;
        private int criticalAlerts;
        private int mtr;
        private int resolvedToday;

        public Metrics(int activeAlerts, int criticalAlerts, int mtr, int resolvedToday) {
            this.activeAlerts = activeAlerts;
            this.criticalAlerts = criticalAlerts;
            this.mtr = mtr;
            this.resolvedToday = resolvedToday;
            
            
        }

		public int getActiveAlerts() {
			return activeAlerts;
		}

		public void setActiveAlerts(int activeAlerts) {
			this.activeAlerts = activeAlerts;
		}

		public int getCriticalAlerts() {
			return criticalAlerts;
		}

		public void setCriticalAlerts(int criticalAlerts) {
			this.criticalAlerts = criticalAlerts;
		}

		public int getMtr() {
			return mtr;
		}

		public void setMtr(int mtr) {
			this.mtr = mtr;
		}

		public int getResolvedToday() {
			return resolvedToday;
		}

		public void setResolvedToday(int resolvedToday) {
			this.resolvedToday = resolvedToday;
		}
    }
}
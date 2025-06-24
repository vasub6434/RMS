package com.bonrix.dggenraterset.DTO;

public class AnalogParameter {
    private String analogName;
    private String unit;
    private String max;
    private String min;

    public AnalogParameter(String analogName, String unit, String max, String min) {
        this.analogName = analogName;
        this.unit = unit;
        this.max = max;
        this.min = min;
    }

    public String getAnalogName() {
        return analogName;
    }

    public String getUnit() {
        return unit;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }
}

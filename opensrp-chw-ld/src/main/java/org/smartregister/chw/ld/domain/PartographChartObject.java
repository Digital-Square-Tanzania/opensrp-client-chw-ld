package org.smartregister.chw.ld.domain;

public class PartographChartObject {
    private long dateTime;
    private int value;

    public PartographChartObject(long dateTime, int value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

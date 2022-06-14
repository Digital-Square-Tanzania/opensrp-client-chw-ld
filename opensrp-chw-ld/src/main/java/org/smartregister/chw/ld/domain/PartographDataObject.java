package org.smartregister.chw.ld.domain;

public class PartographDataObject {
    private long dateTime;
    private String value;

    public PartographDataObject(long dateTime, String value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

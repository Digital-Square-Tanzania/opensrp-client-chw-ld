package org.smartregister.chw.ld.domain;

public class PartographOxytocinObject {
    private long dateTime;
    private String oxytocinUL;
    private String oxytocinDropsPerMinute;

    public PartographOxytocinObject(long dateTime, String oxytocinUL, String oxytocinDropsPerMinute) {
        this.dateTime = dateTime;
        this.oxytocinUL = oxytocinUL;
        this.oxytocinDropsPerMinute = oxytocinDropsPerMinute;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getOxytocinUL() {
        return oxytocinUL;
    }

    public void setOxytocinUL(String oxytocinUL) {
        this.oxytocinUL = oxytocinUL;
    }

    public String getOxytocinDropsPerMinute() {
        return oxytocinDropsPerMinute;
    }

    public void setOxytocinDropsPerMinute(String oxytocinDropsPerMinute) {
        this.oxytocinDropsPerMinute = oxytocinDropsPerMinute;
    }
}

package org.smartregister.chw.ld.domain;

public class PartographChartBloodPressureObject {
    private long dateTime;
    private int systolic;
    private int diastolic;

    public PartographChartBloodPressureObject(long dateTime, int systolic, int diastolic) {
        this.dateTime = dateTime;
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }
}

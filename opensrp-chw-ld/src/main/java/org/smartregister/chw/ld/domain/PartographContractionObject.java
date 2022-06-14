package org.smartregister.chw.ld.domain;

public class PartographContractionObject extends PartographDataObject {
    private int contractionsFrequency;
    private String contractionsLengthInTime;


    public PartographContractionObject(long dateTime, String value, int contractionsFrequency, String contractionsLengthInTime) {
        super(dateTime, value);
        this.contractionsFrequency = contractionsFrequency;
        this.contractionsLengthInTime = contractionsLengthInTime;
    }

    public int getContractionsFrequency() {
        return contractionsFrequency;
    }

    public void setContractionsFrequency(int contractionsFrequency) {
        this.contractionsFrequency = contractionsFrequency;
    }

    public String getContractionsLengthInTime() {
        return contractionsLengthInTime;
    }

    public void setContractionsLengthInTime(String contractionsLengthInTime) {
        this.contractionsLengthInTime = contractionsLengthInTime;
    }
}

package org.smartregister.chw.ld.domain;

import java.io.Serializable;
import java.util.Date;

public class MemberObject implements Serializable {

    private String familyHeadName;
    private String familyHeadPhoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String gender;
    private String uniqueId;
    private String age;
    private String relationalid;
    private String details;
    private String dateChwLDTest;
    private String feverLDChw;
    private String feverDuration;
    private String dateHfLDTest;
    private Date ldTestDate;
    private String ldTreat;
    private String famLlin;
    private String llin2Days;
    private String llinCondition;
    private String ldEduChw;
    private String baseEntityId;
    private String relationalId;
    private String primaryCareGiver;
    private String primaryCareGiverName;
    private String primaryCareGiverPhone;
    private String familyHead;
    private String familyBaseEntityId;
    private String familyName;
    private String phoneNumber;
    private String gestAge;
    private String deliveryDate;
    private String ancMember;
    private String pncMember;
    private String ldFollowUpDate;

    public MemberObject() {
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateChwLDTest() {
        return dateChwLDTest;
    }

    public void setDateChwLDTest(String dateChwLDTest) {
        this.dateChwLDTest = dateChwLDTest;
    }

    public String getFeverLDChw() {
        return feverLDChw;
    }

    public void setFeverLDChw(String feverLDChw) {
        this.feverLDChw = feverLDChw;
    }

    public String getFeverDuration() {
        return feverDuration;
    }

    public void setFeverDuration(String feverDuration) {
        this.feverDuration = feverDuration;
    }

    public String getDateHfLDTest() {
        return dateHfLDTest;
    }

    public void setDateHfLDTest(String dateHfLDTest) {
        this.dateHfLDTest = dateHfLDTest;
    }

    public Date getLDTestDate() {
        return ldTestDate;
    }

    public void setLDTestDate(Date ldTestDate) {
        this.ldTestDate = ldTestDate;
    }

    public String getLDTreat() {
        return ldTreat;
    }

    public void setLDTreat(String ldTreat) {
        this.ldTreat = ldTreat;
    }

    public String getFamLlin() {
        return famLlin;
    }

    public void setFamLlin(String famLlin) {
        this.famLlin = famLlin;
    }

    public String getLlin2Days() {
        return llin2Days;
    }

    public void setLlin2Days(String llin2Days) {
        this.llin2Days = llin2Days;
    }

    public String getLlinCondition() {
        return llinCondition;
    }

    public void setLlinCondition(String llinCondition) {
        this.llinCondition = llinCondition;
    }

    public String getLDEduChw() {
        return ldEduChw;
    }

    public void setLDEduChw(String ldEduChw) {
        this.ldEduChw = ldEduChw;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }

    public void setFamilyBaseEntityId(String familyBaseEntityId) {
        this.familyBaseEntityId = familyBaseEntityId;
    }

    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    public void setPrimaryCareGiver(String primaryCareGiver) {
        this.primaryCareGiver = primaryCareGiver;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGestAge() {
        return gestAge;
    }

    public void setGestAge(String gestAge) {
        this.gestAge = gestAge;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public String getFamilyHeadName() {
        return familyHeadName;
    }

    public void setFamilyHeadName(String familyHeadName) {
        this.familyHeadName = familyHeadName;
    }

    public String getFamilyHeadPhoneNumber() {
        return familyHeadPhoneNumber;
    }

    public void setFamilyHeadPhoneNumber(String familyHeadPhoneNumber) {
        this.familyHeadPhoneNumber = familyHeadPhoneNumber;
    }

    public String getPrimaryCareGiverName() {
        return primaryCareGiverName;
    }

    public void setPrimaryCareGiverName(String primaryCareGiverName) {
        this.primaryCareGiverName = primaryCareGiverName;
    }

    public String getPrimaryCareGiverPhone() {
        return primaryCareGiverPhone;
    }

    public void setPrimaryCareGiverPhone(String primaryCareGiverPhone) {
        this.primaryCareGiverPhone = primaryCareGiverPhone;
    }

    public String getAncMember() {
        return ancMember;
    }

    public void setAncMember(String ancMember) {
        this.ancMember = ancMember;
    }

    public String getPncMember() {
        return pncMember;
    }

    public void setPncMember(String pncMember) {
        this.pncMember = pncMember;
    }

    public String getLDFollowUpDate() {
        return ldFollowUpDate;
    }

    public void setLDFollowUpDate(String ldFollowUpDate) {
        this.ldFollowUpDate = ldFollowUpDate;
    }
}

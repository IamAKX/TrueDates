package com.neosao.truedates.model;

import java.io.Serializable;

public class MemberInterests implements Serializable {
    private String memberInterestCode;
    private String interestCode;
    private String memberInterestValue;
    private String isActive;
    private String isDelete;
    private String interestName;
    private String maxInterestValue;

    public MemberInterests(String memberInterestCode, String interestCode, String memberInterestValue, String isActive, String isDelete, String interestName, String maxInterestValue) {
        this.memberInterestCode = memberInterestCode;
        this.interestCode = interestCode;
        this.memberInterestValue = memberInterestValue;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.interestName = interestName;
        this.maxInterestValue = maxInterestValue;
    }

    public MemberInterests() {
    }

    public String getMemberInterestCode() {
        return memberInterestCode;
    }

    public void setMemberInterestCode(String memberInterestCode) {
        this.memberInterestCode = memberInterestCode;
    }

    public String getInterestCode() {
        return interestCode;
    }

    public void setInterestCode(String interestCode) {
        this.interestCode = interestCode;
    }

    public String getMemberInterestValue() {
        return memberInterestValue;
    }

    public void setMemberInterestValue(String memberInterestValue) {
        this.memberInterestValue = memberInterestValue;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getMaxInterestValue() {
        return maxInterestValue;
    }

    public void setMaxInterestValue(String maxInterestValue) {
        this.maxInterestValue = maxInterestValue;
    }
}

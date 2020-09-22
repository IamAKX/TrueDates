package com.neosao.truedates.model;

public class IntrestModel {
    private String code;
    private String interestValue;
    private String interestName;

    public IntrestModel(String code, String interestValue, String interestName) {
        this.code = code;
        this.interestValue = interestValue;
        this.interestName = interestName;
    }

    public String getCode() {
        return code;
    }

    public String getInterestValue() {
        return interestValue;
    }

    public String getInterestName() {
        return interestName;
    }
}

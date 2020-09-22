package com.neosao.truedates.model.options;

public class Interest {
    private String code;
    private String interestValue;
    private String interestName;

    public Interest(String code, String interestValue, String interestName) {
        this.code = code;
        this.interestValue = interestValue;
        this.interestName = interestName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInterestValue(String interestValue) {
        this.interestValue = interestValue;
    }

    public void setInterestName(String interestName) {
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

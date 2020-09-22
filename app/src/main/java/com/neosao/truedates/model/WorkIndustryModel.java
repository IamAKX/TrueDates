package com.neosao.truedates.model;

public class WorkIndustryModel {
    private String code;
    private String industryName;

    public WorkIndustryModel(String code, String industryName) {
        this.code = code;
        this.industryName = industryName;
    }

    public String getCode() {
        return code;
    }

    public String getIndustryName() {
        return industryName;
    }
}

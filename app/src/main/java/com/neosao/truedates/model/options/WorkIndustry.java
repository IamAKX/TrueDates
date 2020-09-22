package com.neosao.truedates.model.options;

public class WorkIndustry {
    private String code;
    private String industryName;

    public WorkIndustry(String code, String industryName) {
        this.code = code;
        this.industryName = industryName;
    }

    public String getCode() {
        return code;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}

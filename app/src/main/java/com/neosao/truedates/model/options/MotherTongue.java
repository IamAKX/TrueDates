package com.neosao.truedates.model.options;

public class MotherTongue {
    private String code;
    private String mothertoungeName;

    public MotherTongue(String code, String mothertoungeName) {
        this.code = code;
        this.mothertoungeName = mothertoungeName;
    }

    public String getCode() {
        return code;
    }

    public String getMothertoungeName() {
        return mothertoungeName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMothertoungeName(String mothertoungeName) {
        this.mothertoungeName = mothertoungeName;
    }
}

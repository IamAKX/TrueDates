package com.neosao.truedates.model;

public class ReligionModel {
    private String code;
    private String religionName;

    public ReligionModel(String code, String religionName) {
        this.code = code;
        this.religionName = religionName;
    }

    public String getCode() {
        return code;
    }

    public String getReligionName() {
        return religionName;
    }
}

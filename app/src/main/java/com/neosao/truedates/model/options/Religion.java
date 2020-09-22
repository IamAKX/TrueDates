package com.neosao.truedates.model.options;

public class Religion {
    private String code;
    private String religionName;

    public Religion(String code, String religionName) {
        this.code = code;
        this.religionName = religionName;
    }

    public String getCode() {
        return code;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }
}

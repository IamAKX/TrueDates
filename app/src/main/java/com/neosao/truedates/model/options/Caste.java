package com.neosao.truedates.model.options;

public class Caste {
    private String code;
    private String casteName;

    public Caste(String code, String casteName) {
        this.code = code;
        this.casteName = casteName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCasteName(String casteName) {
        this.casteName = casteName;
    }

    public String getCode() {
        return code;
    }

    public String getCasteName() {
        return casteName;
    }
}

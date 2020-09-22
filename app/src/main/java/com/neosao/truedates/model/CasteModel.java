package com.neosao.truedates.model;

public class CasteModel {
    private String code;
    private String casteName;

    public CasteModel(String code, String casteName) {
        this.code = code;
        this.casteName = casteName;
    }

    public String getCode() {
        return code;
    }

    public String getCasteName() {
        return casteName;
    }
}

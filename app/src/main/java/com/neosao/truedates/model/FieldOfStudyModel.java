package com.neosao.truedates.model;

public class FieldOfStudyModel {
    private String code;
    private String fieldName;

    public FieldOfStudyModel(String code, String fieldName) {
        this.code = code;
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public String getFieldName() {
        return fieldName;
    }
}

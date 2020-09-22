package com.neosao.truedates.model.options;

public class FieldOfStudy {
    private String code;
    private String fieldName;

    public FieldOfStudy(String code, String fieldName) {
        this.code = code;
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

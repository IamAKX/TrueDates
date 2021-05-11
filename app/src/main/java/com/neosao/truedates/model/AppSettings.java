package com.neosao.truedates.model;

import java.io.Serializable;

public class AppSettings implements Serializable {
    private String upiId;
    private int addAfterProfile;

    public AppSettings(String upiId, int addAfterProfile) {
        this.upiId = upiId;
        this.addAfterProfile = addAfterProfile;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public int getAddAfterProfile() {
        return addAfterProfile;
    }

    public void setAddAfterProfile(int addAfterProfile) {
        this.addAfterProfile = addAfterProfile;
    }

    @Override
    public String toString() {
        return "AppSettings{" +
                "upiId='" + upiId + '\'' +
                ", addAfterProfile=" + addAfterProfile +
                '}';
    }
}

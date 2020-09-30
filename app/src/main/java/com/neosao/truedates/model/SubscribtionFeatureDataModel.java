package com.neosao.truedates.model;

public class SubscribtionFeatureDataModel {
    private String featureCode;
    private String featureName;
    private String featureLogo;
    private String featureStatus;
    private String isCount;
    private String quantity;
    private String range;

    public SubscribtionFeatureDataModel(String featureCode, String featureName, String featureLogo, String featureStatus, String isCount, String quantity, String range) {
        this.featureCode = featureCode;
        this.featureName = featureName;
        this.featureLogo = featureLogo;
        this.featureStatus = featureStatus;
        this.isCount = isCount;
        this.quantity = quantity;
        this.range = range;
    }

    public SubscribtionFeatureDataModel() {
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureLogo() {
        return featureLogo;
    }

    public void setFeatureLogo(String featureLogo) {
        this.featureLogo = featureLogo;
    }

    public String getFeatureStatus() {
        return featureStatus;
    }

    public void setFeatureStatus(String featureStatus) {
        this.featureStatus = featureStatus;
    }

    public String getIsCount() {
        return isCount;
    }

    public void setIsCount(String isCount) {
        this.isCount = isCount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "SubscribtionFeatureDataModel{" +
                "featureCode='" + featureCode + '\'' +
                ", featureName='" + featureName + '\'' +
                ", featureLogo='" + featureLogo + '\'' +
                ", featureStatus='" + featureStatus + '\'' +
                ", isCount='" + isCount + '\'' +
                ", quantity='" + quantity + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}

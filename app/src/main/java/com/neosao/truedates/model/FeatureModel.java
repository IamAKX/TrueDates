package com.neosao.truedates.model;

import java.util.ArrayList;

public class FeatureModel {
    private String packageCode;
    private String featureTitle;
    private String featureCode;
    private String featureIcon;
    ArrayList<FeaturePriceModel> priceData;

    public FeatureModel() {
    }

    public FeatureModel(String packageCode, String featureTitle, String featureCode, String featureIcon, ArrayList<FeaturePriceModel> priceData) {
        this.packageCode = packageCode;
        this.featureTitle = featureTitle;
        this.featureCode = featureCode;
        this.featureIcon = featureIcon;
        this.priceData = priceData;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getFeatureTitle() {
        return featureTitle;
    }

    public void setFeatureTitle(String featureTitle) {
        this.featureTitle = featureTitle;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getFeatureIcon() {
        return featureIcon;
    }

    public void setFeatureIcon(String featureIcon) {
        this.featureIcon = featureIcon;
    }

    public ArrayList<FeaturePriceModel> getPriceData() {
        return priceData;
    }

    public void setPriceData(ArrayList<FeaturePriceModel> priceData) {
        this.priceData = priceData;
    }

    @Override
    public String toString() {
        return "FeatureModel{" +
                "packageCode='" + packageCode + '\'' +
                ", featureTitle='" + featureTitle + '\'' +
                ", featureCode='" + featureCode + '\'' +
                ", featureIcon='" + featureIcon + '\'' +
                ", priceData=" + priceData +
                '}';
    }
}

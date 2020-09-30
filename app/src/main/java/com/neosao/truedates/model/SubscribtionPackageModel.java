package com.neosao.truedates.model;

import java.util.ArrayList;

public class SubscribtionPackageModel {
    private String packageCode;
    private String packageName;
    private String packageDescription;
    private String packageLogo;
    private ArrayList<SubscribtionFeatureDataModel> featureData;
    private ArrayList<SubscribtionPriceDataModel> priceData;

    public SubscribtionPackageModel() {
    }

    public SubscribtionPackageModel(String packageCode, String packageName, String packageDescription, String packageLogo, ArrayList<SubscribtionFeatureDataModel> featureData, ArrayList<SubscribtionPriceDataModel> priceData) {
        this.packageCode = packageCode;
        this.packageName = packageName;
        this.packageDescription = packageDescription;
        this.packageLogo = packageLogo;
        this.featureData = featureData;
        this.priceData = priceData;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getPackageLogo() {
        return packageLogo;
    }

    public void setPackageLogo(String packageLogo) {
        this.packageLogo = packageLogo;
    }

    public ArrayList<SubscribtionFeatureDataModel> getFeatureData() {
        return featureData;
    }

    public void setFeatureData(ArrayList<SubscribtionFeatureDataModel> featureData) {
        this.featureData = featureData;
    }

    public ArrayList<SubscribtionPriceDataModel> getPriceData() {
        return priceData;
    }

    public void setPriceData(ArrayList<SubscribtionPriceDataModel> priceData) {
        this.priceData = priceData;
    }

    @Override
    public String toString() {
        return "SubscribtionPackageModel{" +
                "packageCode='" + packageCode + '\'' +
                ", packageName='" + packageName + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", packageLogo='" + packageLogo + '\'' +
                ", featureData=" + featureData +
                ", priceData=" + priceData +
                '}';
    }
}

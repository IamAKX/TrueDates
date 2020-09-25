package com.neosao.truedates.model;

public class FeatureSliderModel {
    String featureCode;
    String featureName;
    String featureLogo;
    String featureDescription;

    public FeatureSliderModel(String featureCode, String featureName, String featureLogo, String featureDescription) {
        this.featureCode = featureCode;
        this.featureName = featureName;
        this.featureLogo = featureLogo;
        this.featureDescription = featureDescription;
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

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }
}

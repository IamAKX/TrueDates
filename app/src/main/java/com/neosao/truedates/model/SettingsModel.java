package com.neosao.truedates.model;

public class SettingsModel {
    private String maxDistance;
    private String minAgeFilter;
    private String maxAgeFilter;
    private String isInstagramActive;
    private String instagramLink;

    public SettingsModel(String maxDistance, String minAgeFilter, String maxAgeFilter, String isInstagramActive, String instagramLink) {
        this.maxDistance = maxDistance;
        this.minAgeFilter = minAgeFilter;
        this.maxAgeFilter = maxAgeFilter;
        this.isInstagramActive = isInstagramActive;
        this.instagramLink = instagramLink;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getMinAgeFilter() {
        return minAgeFilter;
    }

    public void setMinAgeFilter(String minAgeFilter) {
        this.minAgeFilter = minAgeFilter;
    }

    public String getMaxAgeFilter() {
        return maxAgeFilter;
    }

    public void setMaxAgeFilter(String maxAgeFilter) {
        this.maxAgeFilter = maxAgeFilter;
    }

    public String getIsInstagramActive() {
        return isInstagramActive;
    }

    public void setIsInstagramActive(String isInstagramActive) {
        this.isInstagramActive = isInstagramActive;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }
}

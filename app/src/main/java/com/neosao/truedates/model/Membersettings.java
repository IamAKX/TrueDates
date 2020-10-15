package com.neosao.truedates.model;

import java.io.Serializable;

public class Membersettings implements Serializable {
    private String id;
    private String code;
    private String memberCode;
    private String currentLocation;
    private String latitude;
    private String longitude;
    private String minAgeFilter;
    private String maxAgeFilter;
    private String maxDistance;
    private String showMe;
    private String contactNumber;
    private String isActive;
    private String isDelete;
    private String addID;
    private String editID;
    private String deleteID;
    private String addIP;
    private String editIP;
    private String deleteIP;
    private String addDate;
    private String editDate;
    private String deleteDate;
    private String isInstagramActive;
    private String instagramLink;

    public Membersettings(String id, String code, String memberCode, String currentLocation, String latitude, String longitude, String minAgeFilter, String maxAgeFilter, String maxDistance, String showMe, String contactNumber, String isActive, String isDelete, String addID, String editID, String deleteID, String addIP, String editIP, String deleteIP, String addDate, String editDate, String deleteDate, String isInstagramActive, String instagramLink) {
        this.id = id;
        this.code = code;
        this.memberCode = memberCode;
        this.currentLocation = currentLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minAgeFilter = minAgeFilter;
        this.maxAgeFilter = maxAgeFilter;
        this.maxDistance = maxDistance;
        this.showMe = showMe;
        this.contactNumber = contactNumber;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.addID = addID;
        this.editID = editID;
        this.deleteID = deleteID;
        this.addIP = addIP;
        this.editIP = editIP;
        this.deleteIP = deleteIP;
        this.addDate = addDate;
        this.editDate = editDate;
        this.deleteDate = deleteDate;
        this.isInstagramActive = isInstagramActive;
        this.instagramLink = instagramLink;
    }

    public Membersettings() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getShowMe() {
        return showMe;
    }

    public void setShowMe(String showMe) {
        this.showMe = showMe;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getAddID() {
        return addID;
    }

    public void setAddID(String addID) {
        this.addID = addID;
    }

    public String getEditID() {
        return editID;
    }

    public void setEditID(String editID) {
        this.editID = editID;
    }

    public String getDeleteID() {
        return deleteID;
    }

    public void setDeleteID(String deleteID) {
        this.deleteID = deleteID;
    }

    public String getAddIP() {
        return addIP;
    }

    public void setAddIP(String addIP) {
        this.addIP = addIP;
    }

    public String getEditIP() {
        return editIP;
    }

    public void setEditIP(String editIP) {
        this.editIP = editIP;
    }

    public String getDeleteIP() {
        return deleteIP;
    }

    public void setDeleteIP(String deleteIP) {
        this.deleteIP = deleteIP;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
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

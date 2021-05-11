package com.neosao.truedates.model;

import java.io.Serializable;

public class UserBasicDetails implements Serializable {
    private String userID;
    private String userName;
    private String profileImage;
    private String emailAddress;

    public UserBasicDetails() {
    }

    public UserBasicDetails(String userID, String userName, String profileImage, String emailAddress) {
        this.userID = userID;
        this.userName = userName;
        this.profileImage = profileImage;
        this.emailAddress = emailAddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "UserBasicDetails{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}

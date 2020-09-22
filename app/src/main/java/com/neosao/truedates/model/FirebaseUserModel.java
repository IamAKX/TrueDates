package com.neosao.truedates.model;

public class FirebaseUserModel {
    private String loginProvider;
    private boolean isUserAuthComplete;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImage;
    private String firebaseUUID;

    public FirebaseUserModel(String loginProvider, boolean isUserAuthComplete, String name, String email, String phoneNumber, String profileImage, String firebaseUUID) {
        this.loginProvider = loginProvider;
        this.isUserAuthComplete = isUserAuthComplete;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.firebaseUUID = firebaseUUID;
    }

    public String getLoginProvider() {
        return loginProvider;
    }

    public boolean isUserAuthComplete() {
        return isUserAuthComplete;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getFirebaseUUID() {
        return firebaseUUID;
    }

    @Override
    public String toString() {
        return "FirebaseUserModel{" +
                "loginProvider='" + loginProvider + '\'' +
                ", isUserAuthComplete=" + isUserAuthComplete +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", firebaseUUID='" + firebaseUUID + '\'' +
                '}';
    }
}

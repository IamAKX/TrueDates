package com.neosao.truedates.model;

import java.util.List;

public class UserModel {
    private String isActive;
    private Object isDelete;
    private String code;
    private String userId;
    private String name;
    private String birthDate;
    private String age;
    private String contactNumber;
    private String zodiacSign;
    private String bodyType;
    private String lookingFor;
    private String height;
    private String motherTounge;
    private String caste;
    private String religion;
    private String diet;
    private String pets;
    private String drink;
    private String smoke;
    private String haveKids;
    private String wantKids;
    private String gender;
    private String firebaseId;
    private String email;
    private String maritalStatus;
    private String uniqueId;
    private String about;
    private String relationshipStatus;
    private List<MemberPhotos> memberPhotos;
    private List<Membersettings> membersettings;
    private List<MemberInterests> memberInterests;
    private List<MemberWork> memberWork;

    private String registerType;

    public UserModel() {
    }

    public UserModel(String isActive, Object isDelete, String code, String userId, String name, String birthDate, String age, String contactNumber, String zodiacSign, String bodyType, String lookingFor, String height, String motherTounge, String caste, String religion, String diet, String pets, String drink, String smoke, String haveKids, String wantKids, String gender, String firebaseId, String email, String maritalStatus, String uniqueId, String about, String relationshipStatus, List<MemberPhotos> memberPhotos, List<Membersettings> membersettings, List<MemberInterests> memberInterests, List<MemberWork> memberWork, String registerType) {
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.code = code;
        this.userId = userId;
        this.name = name;
        this.birthDate = birthDate;
        this.age = age;
        this.contactNumber = contactNumber;
        this.zodiacSign = zodiacSign;
        this.bodyType = bodyType;
        this.lookingFor = lookingFor;
        this.height = height;
        this.motherTounge = motherTounge;
        this.caste = caste;
        this.religion = religion;
        this.diet = diet;
        this.pets = pets;
        this.drink = drink;
        this.smoke = smoke;
        this.haveKids = haveKids;
        this.wantKids = wantKids;
        this.gender = gender;
        this.firebaseId = firebaseId;
        this.email = email;
        this.maritalStatus = maritalStatus;
        this.uniqueId = uniqueId;
        this.about = about;
        this.relationshipStatus = relationshipStatus;
        this.memberPhotos = memberPhotos;
        this.membersettings = membersettings;
        this.memberInterests = memberInterests;
        this.memberWork = memberWork;
        this.registerType = registerType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Object getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Object isDelete) {
        this.isDelete = isDelete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getZodiacSign() {
        return zodiacSign;
    }

    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMotherTounge() {
        return motherTounge;
    }

    public void setMotherTounge(String motherTounge) {
        this.motherTounge = motherTounge;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getHaveKids() {
        return haveKids;
    }

    public void setHaveKids(String haveKids) {
        this.haveKids = haveKids;
    }

    public String getWantKids() {
        return wantKids;
    }

    public void setWantKids(String wantKids) {
        this.wantKids = wantKids;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public List<MemberPhotos> getMemberPhotos() {
        return memberPhotos;
    }

    public void setMemberPhotos(List<MemberPhotos> memberPhotos) {
        this.memberPhotos = memberPhotos;
    }

    public List<Membersettings> getMembersettings() {
        return membersettings;
    }

    public void setMembersettings(List<Membersettings> membersettings) {
        this.membersettings = membersettings;
    }

    public List<MemberInterests> getMemberInterests() {
        return memberInterests;
    }

    public void setMemberInterests(List<MemberInterests> memberInterests) {
        this.memberInterests = memberInterests;
    }

    public List<MemberWork> getMemberWork() {
        return memberWork;
    }

    public void setMemberWork(List<MemberWork> memberWork) {
        this.memberWork = memberWork;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }
}
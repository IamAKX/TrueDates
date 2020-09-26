package com.neosao.truedates.model;

public class MemberWork {
    private String memberWorkCode;
    private String fieldStudyCode;
    private String highestQualification;
    private String industryCode;
    private String experienceYears;
    private String monthlyIncome;
    private String isActive;
    private String isDelete;
    private String fieldName;
    private String industryName;
    private String universityName;

    public MemberWork() {
    }

    public MemberWork(String memberWorkCode, String fieldStudyCode, String highestQualification, String industryCode, String experienceYears, String monthlyIncome, String isActive, String isDelete, String fieldName, String industryName, String universityName) {
        this.memberWorkCode = memberWorkCode;
        this.fieldStudyCode = fieldStudyCode;
        this.highestQualification = highestQualification;
        this.industryCode = industryCode;
        this.experienceYears = experienceYears;
        this.monthlyIncome = monthlyIncome;
        this.isActive = isActive;
        this.isDelete = isDelete;
        this.fieldName = fieldName;
        this.industryName = industryName;
        this.universityName = universityName;
    }

    public String getMemberWorkCode() {
        return memberWorkCode;
    }

    public void setMemberWorkCode(String memberWorkCode) {
        this.memberWorkCode = memberWorkCode;
    }

    public String getFieldStudyCode() {
        return fieldStudyCode;
    }

    public void setFieldStudyCode(String fieldStudyCode) {
        this.fieldStudyCode = fieldStudyCode;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}

package com.neosao.truedates.model;

public class MemberPhotos {
    private String photoCode;
    private String memberPhoto;
    private String index;
    private int isDefault;

    public MemberPhotos() {
    }

    public MemberPhotos(String photoCode, String memberPhoto, String index, int isDefault) {
        this.photoCode = photoCode;
        this.memberPhoto = memberPhoto;
        this.index = index;
        this.isDefault = isDefault;
    }

    public String getPhotoCode() {
        return photoCode;
    }

    public void setPhotoCode(String photoCode) {
        this.photoCode = photoCode;
    }

    public String getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(String memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}

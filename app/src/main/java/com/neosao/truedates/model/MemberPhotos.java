package com.neosao.truedates.model;

public class MemberPhotos {
    private String photoCode;
    private String memberPhoto;

    public MemberPhotos(String photoCode, String memberPhoto) {
        this.photoCode = photoCode;
        this.memberPhoto = memberPhoto;
    }

    public MemberPhotos() {
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

    @Override
    public String toString() {
        return "MemberPhotos{" +
                "photoCode='" + photoCode + '\'' +
                ", memberPhoto='" + memberPhoto + '\'' +
                '}';
    }
}

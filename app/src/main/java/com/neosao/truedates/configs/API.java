package com.neosao.truedates.configs;

public interface API {

    public final String BASE_URL = "http://neosao.com/testing/dating/Api/Api/";

    public final String GET_ALL_OPTIONS_LIST = BASE_URL + "getAllOptionsList";
    public final String REGISTER_USER = BASE_URL + "registration";
    public final String UPLOAD_IMAGE = BASE_URL + "saveMemberPhoto";
    public final String GET_USER_PROFILE = BASE_URL + "getMyProfile";
}
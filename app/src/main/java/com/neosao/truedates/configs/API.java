package com.neosao.truedates.configs;

public interface API {

    public final String BASE_URL = "http://neosao.com/testing/dating/Api/Api/";

    public final String GET_ALL_OPTIONS_LIST = BASE_URL + "getAllOptionsList";
    public final String REGISTER_USER = BASE_URL + "registration";
    public final String UPLOAD_IMAGE = BASE_URL + "saveMemberPhoto";
    public final String GET_USER_PROFILE = BASE_URL + "getMyProfile";
    public final String UPDATE_USER_PROFILE = BASE_URL + "updateMemberDetails";
    public final String FEATURE_SLIDER = BASE_URL + "getFeatuesSlider";

    // Settings
    public final String UPDATE_LOCATION = BASE_URL + "updateMyLocation";
    public final String UPDATE_AGE_RANGE = BASE_URL + "updateAgeFilter";
    public final String UPDATE_MAX_DISTANCE = BASE_URL + "updateMaxDistance";
    public final String UPDATE_SHOW_ME = BASE_URL + "updateShowMe";
    public final String UPDATE_INSTAGRAM = BASE_URL + "updateInstagramSettings";
}
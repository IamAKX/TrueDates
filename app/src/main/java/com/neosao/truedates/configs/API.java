package com.neosao.truedates.configs;

public interface API {

    String BASE_URL = "http://neosao.com/testing/dating/Api/Api/";

    String GET_ALL_OPTIONS_LIST = BASE_URL + "getAllOptionsList";
    String REGISTER_USER = BASE_URL + "registration";
    String UPLOAD_IMAGE = BASE_URL + "saveMemberPhoto";
    String GET_USER_PROFILE = BASE_URL + "getMyProfile";
    String UPDATE_USER_PROFILE = BASE_URL + "updateMemberDetails";
    String FEATURE_SLIDER = BASE_URL + "getFeatuesSlider";
    String LOGIN_PROCESS = BASE_URL + "loginProcess";

    // Settings
    String UPDATE_LOCATION = BASE_URL + "updateMyLocation";
    String UPDATE_AGE_RANGE = BASE_URL + "updateAgeFilter";
    String UPDATE_MAX_DISTANCE = BASE_URL + "updateMaxDistance";
    String UPDATE_SHOW_ME = BASE_URL + "updateShowMe";
    String UPDATE_INSTAGRAM = BASE_URL + "updateInstagramSettings";

    String SET_DEFAULT_PHOTO = BASE_URL + "updateDefaultPhoto";
    String DELETE_PHOTO = BASE_URL + "deleteMemberPhoto";
}
package com.neosao.truedates.configs;

public interface API {

    String BASE_URL = "https://truedates.in/Api/Api/";
    String TEST_BASE_URL = "http://neosao.com/testing/dating/Api/TestApi/";

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
    String UPDATE_PHONE_NUMBER = BASE_URL + "updateMyContactNumber";
    String GET_SUBSCRIBTION_PACKAGES = BASE_URL + "getSubscriptionPackages";
    String GET_FEATURE_PACKAGES = BASE_URL + "getFeaturePackageList";
    String BUY_SUBSCRIBTION_PACKAGES = BASE_URL + "purchaseSubscriptionPackage";
    String BUY_FEATURE_PACKAGES = BASE_URL + "purchaseFeaturePackage";

    String SET_DEFAULT_PHOTO = BASE_URL + "updateDefaultPhoto";
    String DELETE_PHOTO = BASE_URL + "deleteMemberPhoto";
    String FEATURE_DEDUCTION = BASE_URL + "performFeatureDeduction";
    String GET_PROFILE_LIST = BASE_URL + "getListsProfiles";
    String GET_PROFILE_WHO_LIKED_ME_LIST = BASE_URL + "getWhoLikedMeList";
    String GET_MATCHED_PROFILE = BASE_URL + "getMatchingProfiles";
    String GET_RECENT_MATCHED_PROFILE = BASE_URL + "getRecentMatchingProfiles";

    String GET_ADMOB_SETTING = BASE_URL + "getAdmobSettings";

    String SEND_REG_OTP = BASE_URL + "sendRegisterOTP";
    String VERIFY_REG_OTP = BASE_URL + "verifyRegisterOTP";
    String CHECK_PROFILE_EXISTS = BASE_URL + "checkProfileExists";
    String GET_APP_SETTINGS = BASE_URL + "getAppSettings";
}

package com.softdesign.devintensive.utils;

/**
 * Интерфейс, содержащий все необходимые константы
 */
public interface ConstantManager {
    String PREFIX_TAG = "DevIntensive: ";

    String EDIT_MODE_KEY = "com.softdesign.devintensive.utils.ConstantManager.editMode";

    String USER_PHONE_KEY = "com.softdesign.devintensive.utils.ConstantManager.userKey1";
    String USER_MAIL_KEY = "com.softdesign.devintensive.utils.ConstantManager.userKey2";
    String USER_VK_KEY = "com.softdesign.devintensive.utils.ConstantManager.userKey3";
    String USER_REPO_KEY = "com.softdesign.devintensive.utils.ConstantManager.userKey4";
    String USER_SELF_KEY = "com.softdesign.devintensive.utils.ConstantManager.userKey5";
    String USER_PHOTO_KEY = "com.softdesign.devintensive.utils.ConstantManager.userPhotoKey";
    String USER_AVATAR_KEY = "com.softdesign.devintensive.utils.ConstantManager.userAvatarKey";
    String LOGIN_KEY = "com.softdesign.devintensive.utils.ConstantManager.userLoginKey";
    String AUTH_TOKEN = "com.softdesign.devintensive.utils.ConstantManager.userKey7";
    String USER_ID = "com.softdesign.devintensive.utils.ConstantManager.userID";
    String USER_RATING = "com.softdesign.devintensive.utils.ConstantManager.userRating";
    String USER_CODE_LINES = "com.softdesign.devintensive.utils.ConstantManager.userCodeLines";
    String USER_PROJECTS = "com.softdesign.devintensive.utils.ConstantManager.userProjects";
    String USER_FIO = "com.softdesign.devintensive.utils.ConstantManager.userFIO";
    String PARCEBLE_INFORMATION = "com.softdesign.devintensive.utils.ConstantManager.userProfileInformation";

    int LOAD_PROFILE_DATA = 1;
    int REQUEST_CAMERA_PICTURE = 91;
    int REQUEST_GALLARY_PICTURE = 92;
    int PERMISSION_REQUEST_KEY = 93;
    int CAMERA_REQUEST_PERMISSION_CODE = 94;
    long DELAY_MILLIS = 1500;

    int MAGIC_ERROR = 100000;
    int SERVER_NOT_RESPONSE = 100001;
    int LOAD_SUCCESS = 100002;
    int WRONG_TOKEN = 100003;
    int ERROR_WRITE_BASE = 100004;
    int WRONG_USER_OR_PASSWD = 100005;
}

package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smalew on 26.06.16.
 */
public class PreferencesManager {
    private static final String TAG = ConstantManager.PREFIX_TAG + "PreferenceManager";

    private SharedPreferences mSharedPreferences;

    private static final String LOGIN_KEY = ConstantManager.LOGIN_KEY;

    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_REPO_KEY,
            ConstantManager.USER_SELF_KEY};

    private static final String[] USER_STATISTIC_VALUES = {
            ConstantManager.USER_RATING,
            ConstantManager.USER_CODE_LINES,
            ConstantManager.USER_PROJECTS};

    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApp.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userData.get(i));
        }
        editor.apply();
    }
    public List<String> loadUserProfileData() {

        List<String> result = new ArrayList<>();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            result.add(mSharedPreferences.getString(USER_FIELDS[i], null));
        }
        return result;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }
    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive.res.drawable.nav_header_bg.jpg"));
    }
    public void saveUserAvatar(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }
    public Uri loadUserAvatar(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resource://com.softdesign.devintensive.res.drawable.empty_avatar.png" ));
    }

    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN, token);
        editor.apply();
    }
    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN, "");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID, userId);
        editor.apply();
    }
    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID, "null");
    }

    public void saveUserStatistic(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_STATISTIC_VALUES.length; i++) {
            editor.putString(USER_STATISTIC_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }
    public List<String> loadUserStatistic(){
        List<String> result = new ArrayList<>();

        for (int i = 0; i < USER_STATISTIC_VALUES.length; i++) {
            result.add(mSharedPreferences.getString(USER_STATISTIC_VALUES[i], "0"));
        }
        return result;
    }

    public void saveLoginEmail(String email){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(LOGIN_KEY, email);
        editor.apply();
    }
    public String loadLoginEmail(){
        return mSharedPreferences.getString(LOGIN_KEY, "");
    }

    public void saveFIO(String fio){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_FIO, fio);
        editor.apply();
    }
    public String loadFIO(){
        return mSharedPreferences.getString(ConstantManager.USER_FIO, "Family Name");
    }
}

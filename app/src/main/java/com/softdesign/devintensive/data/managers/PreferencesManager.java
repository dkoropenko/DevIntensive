package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

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
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
                                            ConstantManager.USER_MAIL_KEY,
                                            ConstantManager.USER_VK_KEY,
                                            ConstantManager.USER_REPO_KEY,
                                            ConstantManager.USER_SELF_KEY};

    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApp.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userData){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userData.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData(){
        List<String> result = new ArrayList<>();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            result.add(mSharedPreferences.getString(USER_FIELDS[i], null));
        }
        return result;
    }
}

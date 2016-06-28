package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevIntensiveApp extends Application {
    private static final String TAG = ConstantManager.PREFIX_TAG + "DevIntensiveApp";

    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}

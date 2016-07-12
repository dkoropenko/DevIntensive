package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevIntensiveApp extends Application {
    private static final String TAG = ConstantManager.PREFIX_TAG + "DevIntensiveApp";

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
    public static Context getContext() {return  sContext; }
}

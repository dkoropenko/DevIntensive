package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DevIntensiveApp extends Application {
    private static final String TAG = ConstantManager.PREFIX_TAG + "DevIntensiveApp";

    public static SharedPreferences sSharedPreferences;
    private static Context sContext;
     static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database database = helper.getWritableDb();
        sDaoSession = new DaoMaster(database).newSession();

        Stetho.initializeWithDefaults(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
    public static Context getContext() {return  sContext; }
    public static DaoSession getDaoSession() { return sDaoSession; }
}

package com.softdesign.devintensive.data.managers;

/**
 * Created by smalew on 26.06.16.
 */
public class DataManager {

    private static DataManager instance = null;
    private PreferencesManager mPreferencesManager;

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}

package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Created by smalew on 26.06.16.
 */
public class DataManager {
    private static final String TAG = ConstantManager.PREFIX_TAG + "DataManager";

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

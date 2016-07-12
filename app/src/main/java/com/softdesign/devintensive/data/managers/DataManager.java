package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by smalew on 26.06.16.
 */
public class DataManager {
    private static final String TAG = ConstantManager.PREFIX_TAG + "DataManager";

    private static DataManager instance = null;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    //region ========== Network =============
    public Call<UserModelRes> loginUser (UserLoginReq req){
        return mRestService.loginUser(req);
    }

    public Call<LoginModelRes> isValid (String userId) {return mRestService.isValid(userId); }

    public Call<ResponseBody> uploadPhoto(MultipartBody.Part file){
        return mRestService.uploadPhoto(getPreferencesManager().getUserId(), file);
    }
    //endRegion
}

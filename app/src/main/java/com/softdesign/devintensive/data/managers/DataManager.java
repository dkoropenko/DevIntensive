package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApp;

import java.util.ArrayList;
import java.util.List;

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
    private PicassoCache mPicassoCache;
    private Context mContext;
    private DaoSession mDaoSession;

    private DataManager() {
        this.mContext = DevIntensiveApp.getContext();
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicassoCache = new PicassoCache(mContext);
        this.mDaoSession = DevIntensiveApp.getDaoSession();
    }

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }



    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public PicassoCache getPicassoCache() { return mPicassoCache; }

    //region ========== Network =============
    public Call<UserModelRes> loginUser (UserLoginReq req){
        return mRestService.loginUser(req);
    }

    public Call<LoginModelRes> isValid (String userId) {return mRestService.isValid(userId); }

    public Call<ResponseBody> uploadPhoto(MultipartBody.Part file){
        return mRestService.uploadPhoto(getPreferencesManager().getUserId(), file);    }

    public Call<UserListRes> getUsersListFromNetwork(){
        return mRestService.getUsers();
    }
    //endRegion

    //region ========== Databese =============

    public DaoSession getDaoSession() { return mDaoSession; }

    public List<User> getUserListFromDatabase() {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class).
                    where(UserDao.Properties.Id.gt(0)).
                    orderDesc(UserDao.Properties.CodeLines).
                    build().
                    list();
        }catch (Exception e){
            e.printStackTrace();
        }

        return userList;
    }
}

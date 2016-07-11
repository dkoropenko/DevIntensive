package com.softdesign.devintensive.data.network.interseptor;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by smalew on 10.07.16.
 */
public class HeadInterseptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        DataManager dataManager = DataManager.getInstance();
        String token = dataManager.getPreferencesManager().getAuthToken();
        String userId = dataManager.getPreferencesManager().getUserId();

        Request original = chain.request();

        Request.Builder builder = original.newBuilder().
                header("X-Access-Token", token).
                header("Request-User-Id", userId).
                header("User-Agent", "DevIntensive");

        Request request = builder.build();

        return chain.proceed(request);
    }
}

package com.softdesign.devintensive.data.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.softdesign.devintensive.data.network.interseptor.HeadInterseptor;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.DevIntensiveApp;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder sRetrofinBuilder = new Retrofit.Builder().
            baseUrl(AppConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        sHttpClient.addInterceptor(new HeadInterseptor());
        sHttpClient.addInterceptor(loggingInterceptor);
        sHttpClient.connectTimeout(AppConfig.MAX_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        sHttpClient.readTimeout(AppConfig.MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        sHttpClient.cache(new Cache(DevIntensiveApp.getContext().getCacheDir(), Integer.MAX_VALUE));
        sHttpClient.addNetworkInterceptor(new StethoInterceptor());

        Retrofit retrofit = sRetrofinBuilder.
                client(sHttpClient.build()).build();

        return retrofit.create(serviceClass);
    }
}

package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestService {

    @POST ("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);

    @Multipart
    @POST ("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody req,
                              @Part MultipartBody.Part file);
}

package com.softdesign.devintensive.data.network.req;

import android.net.Uri;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by smalew on 12.07.16.
 */
public class UploadFile {
    private MultipartBody.Part mBody;

    public UploadFile() {
    }

    public MultipartBody.Part photo(Uri uri){
        File file = new File(uri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        return mBody;
    }

    public MultipartBody.Part avatar(Uri uri){
        File file = new File(uri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        return mBody;
    }


}

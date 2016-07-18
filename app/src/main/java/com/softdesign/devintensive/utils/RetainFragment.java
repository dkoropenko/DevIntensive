package com.softdesign.devintensive.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

/**
 * Created by smalew on 15.07.16.
 */
public class RetainFragment extends Fragment {

    List<User> mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<User> getModel() {
        return mModel;
    }

    public void setModel(List<User> model) {
        mModel = model;
    }
}

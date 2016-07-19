package com.softdesign.devintensive.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.activities.UserListActivity;
import com.softdesign.devintensive.ui.activities.UsersProfileActivity;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;

import java.util.ArrayList;
import java.util.List;


public class LoadDatestoUi extends ChronosOperation<List<User>> {


    private List<User> mUserList;

    public LoadDatestoUi() {
        mUserList = new ArrayList<>();
    }

    @Nullable
    @Override
    public List<User> run() {
        mUserList = DataManager.getInstance().getUserListFromDatabase();

        return mUserList;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<User>> {
    }
}

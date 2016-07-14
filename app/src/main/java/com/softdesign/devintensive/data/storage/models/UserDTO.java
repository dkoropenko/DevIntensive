package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smalew on 14.07.16.
 */
public class UserDTO implements Parcelable {

    private String mPhoto;
    private String mFullName;
    private String mRating;
    private String mCodeLine;
    private String mProjects;
    private List<String> mGit;
    private String mSelf;

    public UserDTO(UserListRes.UserData user) {

        List<String> values = new ArrayList<>();

        mPhoto = user.getPublicInfo().getPhoto();
        mFullName = user.getFullName();
        mRating = String.valueOf(user.getProfileValues().getRating());
        mCodeLine = String.valueOf(user.getProfileValues().getLinesCode());
        mProjects = String.valueOf(user.getProfileValues().getProjects());

        for (UserModelRes.Repo gitLink: user.getRepositories().getRepo()) {
            values.add(gitLink.getGit());
        }
        mGit = values;
        mSelf = user.getPublicInfo().getBio();
    }

    protected UserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mRating = in.readString();
        mCodeLine = in.readString();
        mProjects = in.readString();
        if (in.readByte() == 0x01) {
            mGit = new ArrayList<String>();
            in.readList(mGit, String.class.getClassLoader());
        } else {
            mGit = null;
        }
        mSelf = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRating);
        dest.writeString(mCodeLine);
        dest.writeString(mProjects);
        if (mGit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mGit);
        }
        dest.writeString(mSelf);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodeLine() {
        return mCodeLine;
    }

    public String getProjects() {
        return mProjects;
    }

    public List<String> getGit() {
        return mGit;
    }

    public String getSelf() {
        return mSelf;
    }
}

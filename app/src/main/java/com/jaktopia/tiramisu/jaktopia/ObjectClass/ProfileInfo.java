package com.jaktopia.tiramisu.jaktopia.ObjectClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class ProfileInfo implements Parcelable {
    String userIconUrl;
    String userFullName;
    String userInfo;
    int userPostCount;

    public ProfileInfo() {
    }

    public ProfileInfo(Parcel source) {
        userIconUrl = source.readString();
        userFullName = source.readString();
        userInfo = source.readString();
        userPostCount = source.readInt();
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public int getUserPostCount() {
        return userPostCount;
    }

    public void setUserPostCount(int userPostCount) {
        this.userPostCount = userPostCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userIconUrl);
        dest.writeString(userFullName);
        dest.writeString(userInfo);
        dest.writeInt(userPostCount);
    }

    public static final Creator<ProfileInfo> CREATOR = new Creator<ProfileInfo>() {
        @Override
        public ProfileInfo createFromParcel(Parcel source) {
            return new ProfileInfo(source);
        }

        @Override
        public ProfileInfo[] newArray(int size) {
            return new ProfileInfo[size];
        }
    };
}

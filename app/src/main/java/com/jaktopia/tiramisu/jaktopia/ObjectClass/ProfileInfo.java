package com.jaktopia.tiramisu.jaktopia.ObjectClass;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class ProfileInfo {
    String userIconUrl;
    String userFullName;
    String userInfo;
    int userPostCount;

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
}

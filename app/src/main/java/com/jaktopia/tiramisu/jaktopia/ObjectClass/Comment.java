package com.jaktopia.tiramisu.jaktopia.ObjectClass;

import java.util.HashMap;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class Comment {
    int userId;
    String username;
    String content;
    long postTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
}

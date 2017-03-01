package com.jaktopia.tiramisu.jaktopia.ObjectClass;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class Event {
    int userId;
    int eventId;
    String userIconUrl;
    String eventName;
    String categoryName;
    String location;
    String photoUrl;
    int isFavorite;
    int favoriteCount;
    String firstName;
    String caption;
    HashMap<String, String> lastComment = new HashMap<String, String>();
    int moreCommentCount;
    long postTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public HashMap<String, String> getLastComment() {
        return lastComment;
    }

    public void setLastComment(HashMap<String, String> lastComment) {
        this.lastComment = lastComment;
    }

    public int getMoreCommentCount() {
        return moreCommentCount;
    }

    public void setMoreCommentCount(int moreCommentCount) {
        this.moreCommentCount = moreCommentCount;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
}




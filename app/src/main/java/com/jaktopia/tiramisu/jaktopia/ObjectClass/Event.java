package com.jaktopia.tiramisu.jaktopia.ObjectClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class Event implements Parcelable {
    private int userId;
    private int eventId;
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

    public Event() {
    }

    public Event(Parcel source) {
        userId = source.readInt();
        eventId = source.readInt();
        userIconUrl = source.readString();
        eventName = source.readString();
        categoryName = source.readString();
        location = source.readString();
        photoUrl = source.readString();
        isFavorite = source.readInt();
        favoriteCount = source.readInt();
        firstName = source.readString();
        caption = source.readString();
        final int N = source.readInt();
        for (int i = 0; i < N; i++) {
            String key = source.readString();
            String value = source.readString();
            lastComment.put(key, value);
        }
        moreCommentCount = source.readInt();
        postTime = source.readLong();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(eventId);
        dest.writeString(userIconUrl);
        dest.writeString(eventName);
        dest.writeString(categoryName);
        dest.writeString(location);
        dest.writeString(photoUrl);
        dest.writeInt(isFavorite);
        dest.writeInt(favoriteCount);
        dest.writeString(firstName);
        dest.writeString(caption);
        final int N = lastComment.size();
        if (N > 0)
            for (Map.Entry<String, String> entry : lastComment.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        dest.writeInt(moreCommentCount);
        dest.writeLong(postTime);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}




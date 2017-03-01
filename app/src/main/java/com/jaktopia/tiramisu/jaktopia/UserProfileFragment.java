package com.jaktopia.tiramisu.jaktopia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.ProfileRecyclerAdapter;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.TimelineRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class UserProfileFragment extends Fragment {
    Context mContext;
    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    TimelineRecyclerAdapter timelineRecyclerAdapter;

    ProfileInfo profileInfo;
    List<Event> events = new ArrayList<Event>();

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mContext = getActivity();
        createDummyData();
        profileRecyclerAdapter = new ProfileRecyclerAdapter(mContext, profileInfo, events);
        //timelineRecyclerAdapter = new TimelineRecyclerAdapter(mContext, events);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.profile_recycler_view);
        recyclerView.setAdapter(profileRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
    }

    private void createDummyData() {
        profileInfo = new ProfileInfo();
        profileInfo.setUserIconUrl("http://i.imgur.com/i8OdQNH.jpg");
        profileInfo.setUserFullName("Luthfi Soeahdak");
        profileInfo.setUserInfo("Suka berpetualang");
        profileInfo.setUserPostCount(25);

        Event eventObj = new Event();
        eventObj.setUserIconUrl("http://i.imgur.com/i8OdQNH.jpg");
        eventObj.setEventName("Food Festival");
        eventObj.setCategoryName("Food");
        eventObj.setLocation("Pakuwon City");
        eventObj.setIsFavorite(1);
        eventObj.setFavoriteCount(50);
        eventObj.setFirstName("Luthfi Soehadak");
        eventObj.setCaption("makanan gratis, segera sikat");
        eventObj.setMoreCommentCount(0);
        eventObj.setPostTime(0);

        events.add(eventObj);

        Event eventObj2 = new Event();
        eventObj2.setUserIconUrl("http://i.imgur.com/i8OdQNH.jpg");
        eventObj2.setEventName("Food Festival 2");
        eventObj2.setCategoryName("Food");
        eventObj2.setLocation("Pakuwon City");
        eventObj2.setIsFavorite(0);
        eventObj2.setFavoriteCount(23);
        eventObj2.setFirstName("Luthfi Soehadak");
        eventObj2.setCaption("makanan gratis dan enak, segera sikat");
        eventObj2.setMoreCommentCount(0);
        eventObj2.setPostTime(0);

        events.add(eventObj2);
    }
}

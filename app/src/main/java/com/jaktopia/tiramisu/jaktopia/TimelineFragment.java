package com.jaktopia.tiramisu.jaktopia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.TimelineRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsoehadak on 2/18/17.
 */

public class TimelineFragment extends Fragment {
    Context mContext;
    RecyclerView recyclerView;
    Button postBtn;
    TimelineRecyclerAdapter timelineRecyclerAdapter;

    List<Event> events = new ArrayList<Event>();

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mContext = getActivity();
        createDummyData();
        timelineRecyclerAdapter = new TimelineRecyclerAdapter(mContext, events);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_timeline_layout, container, false);
        postBtn = (Button)view.findViewById(R.id.fragment_timeline_post_button);
        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_timeline_recycler_view);
        recyclerView.setAdapter(timelineRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext ));
        return view;
    }

    private void createDummyData() {
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

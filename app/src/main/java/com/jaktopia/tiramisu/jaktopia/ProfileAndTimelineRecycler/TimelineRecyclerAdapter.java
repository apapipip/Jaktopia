package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class TimelineRecyclerAdapter extends RecyclerView.Adapter<VHEventitem> {
    Context mContext;
    LayoutInflater inflater;
    List<Event> events = new ArrayList<Event>();

    public TimelineRecyclerAdapter(Context mContext, List<Event> events) {
        this.mContext = mContext;
        this.events = events;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public VHEventitem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_timeline_layout, parent, false);
        return new VHEventitem(view);
    }

    @Override
    public void onBindViewHolder(VHEventitem holder, int position) {
        /* set user icon */
        // user profpict get from url <Picasso>
        /* set category icon */
        if(events.get(position).getCategoryName() == "Accident")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_accident);
        else if(events.get(position).getCategoryName() == "Food")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_food);
        else if(events.get(position).getCategoryName() == "Education")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_education);
        else if(events.get(position).getCategoryName() == "Sport")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_sport);
        else if(events.get(position).getCategoryName() == "Traffic")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_traffic);
        else if(events.get(position).getCategoryName() == "Entertainment")
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_entertainment);
        /* set event name */
        holder.eventNameTxt.setText(events.get(position).getEventName());
        /* set category name and event location */
        holder.categoryNameAndLocationTxt.setText(events.get(position).getCategoryName() + " at " + events.get(position).getLocation());
        /* set post time */
        long time = System.currentTimeMillis()/1000 - events.get(position).getPostTime();
        holder.postTimeTxt.setText("5 minutes ago");
        /* set event photo */
        if(events.get(position).getPhotoUrl() != "") {
            // get photo using picasso
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
            params.setMargins(0,0,0,0);
            holder.eventPhotoImg.setLayoutParams(params);
        }
        /* set favorite count */
        holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount() + " favorites");
        /* set user caption */
        holder.posterCaptionTxt.setText(events.get(position).getFirstName() + " " + events.get(position).getCaption());
        /* set comment if there any */
        //holder.commentTxt.setText();
        holder.commentTxt.setVisibility(GONE);
        /* set more comment if there any */
        if(events.get(position).getMoreCommentCount() > 0)
            holder.moreCommentTxt.setText(events.get(position).getMoreCommentCount() + " more comments");
        else
            holder.moreCommentTxt.setVisibility(GONE);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

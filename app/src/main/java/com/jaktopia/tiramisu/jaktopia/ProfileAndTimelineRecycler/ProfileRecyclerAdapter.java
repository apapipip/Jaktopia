package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context mContext;
    LayoutInflater inflater;
    ProfileInfo userInfo;
    List<Event> events = new ArrayList<Event>();

    public ProfileRecyclerAdapter(Context mContext, ProfileInfo userInfo, List<Event> events) {
        this.mContext = mContext;
        this.userInfo = userInfo;
        this.events = events;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.profile_header_layout, parent, false);
            return new VHProfileHeader(view);
        } else if(viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.event_timeline_layout, parent, false);
            return new VHEventitem(view);
        }
        throw new RuntimeException("no type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHProfileHeader) {
            VHProfileHeader vhProfileHeader = (VHProfileHeader)holder;
            vhProfileHeader.usernameTxt.setText(userInfo.getUserFullName());
            vhProfileHeader.userInfoTxt.setText(userInfo.getUserInfo());
            vhProfileHeader.userPostCountTxt.setText(userInfo.getUserPostCount()+" events");

        } else if(holder instanceof VHEventitem) {
            VHEventitem vhEventitem = (VHEventitem)holder;
            position -= 1;
            /* set user icon */
            // user profpict get from url <Picasso>
            /* set category icon */
            if(events.get(position).getCategoryName() == "Accident")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_accident);
            else if(events.get(position).getCategoryName() == "Food")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_food);
            else if(events.get(position).getCategoryName() == "Education")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_education);
            else if(events.get(position).getCategoryName() == "Sport")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_sport);
            else if(events.get(position).getCategoryName() == "Traffic")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_traffic);
            else if(events.get(position).getCategoryName() == "Entertainment")
                vhEventitem.categoryIconImg.setImageResource(R.drawable.ic_category_entertainment);
            /* set event name */
            vhEventitem.eventNameTxt.setText(events.get(position).getEventName());
            /* set category name and event location */
            vhEventitem.categoryNameAndLocationTxt.setText(events.get(position).getCategoryName() + " at " + events.get(position).getLocation());
            /* set post time */
            long time = System.currentTimeMillis()/1000 - events.get(position).getPostTime();
            vhEventitem.postTimeTxt.setText("5 minutes ago");
            /* set event photo */
            if(events.get(position).getPhotoUrl() != "") {
                // get photo using picasso
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
                params.setMargins(0,0,0,0);
                vhEventitem.eventPhotoImg.setLayoutParams(params);
            }
            /* set favorite count */
            vhEventitem.favoriteCountTxt.setText(events.get(position).getFavoriteCount() + " favorites");
            /* set user caption */
            vhEventitem.posterCaptionTxt.setText(events.get(position).getFirstName() + " " + events.get(position).getCaption());
            /* set comment if there any */
            //holder.commentTxt.setText();
            vhEventitem.commentTxt.setVisibility(GONE);
            /* set more comment if there any */
            if(events.get(position).getMoreCommentCount() > 0)
                vhEventitem.moreCommentTxt.setText(events.get(position).getMoreCommentCount() + " more comments");
            else
                vhEventitem.moreCommentTxt.setVisibility(GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return events.size()+1;
    }
}

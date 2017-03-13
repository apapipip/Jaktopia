package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaktopia.tiramisu.jaktopia.R;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class VHEventItem extends RecyclerView.ViewHolder {
    ImageView userIconImg;
    ImageView categoryIconImg;
    TextView eventNameTxt;
    TextView categoryNameAndLocationTxt;
    TextView postTimeTxt;
    ImageView eventPhotoImg;
    Button favoriteIconBtn;
    TextView favoriteCountTxt;
    TextView posterCaptionTxt;
    TextView commentTxt;
    TextView comment2Txt;
    ImageView moreIconImg;
    TextView moreCommentTxt;
    CardView commentCardView;

    public VHEventItem(View itemView) {
        super(itemView);

        userIconImg = (ImageView)itemView.findViewById(R.id.event_timeline_user_icon);
        categoryIconImg = (ImageView)itemView.findViewById(R.id.event_timeline_category_icon);
        eventNameTxt = (TextView)itemView.findViewById(R.id.event_timeline_event_name);
        categoryNameAndLocationTxt = (TextView)itemView.findViewById(R.id.event_timeline_category_name_and_location);
        postTimeTxt = (TextView)itemView.findViewById(R.id.event_timeline_time);
        eventPhotoImg = (ImageView)itemView.findViewById(R.id.event_timeline_event_photo);
        favoriteIconBtn = (Button)itemView.findViewById(R.id.event_timeline_favorite_icon);
        favoriteCountTxt = (TextView)itemView.findViewById(R.id.event_timeline_favorite_count);
        posterCaptionTxt = (TextView)itemView.findViewById(R.id.event_timeline_poster_caption);
        commentTxt = (TextView)itemView.findViewById(R.id.event_timeline_comment);
        comment2Txt = (TextView)itemView.findViewById(R.id.event_timeline_comment_2);
        moreIconImg = (ImageView)itemView.findViewById(R.id.event_timeline_more_icon);
        moreCommentTxt = (TextView)itemView.findViewById(R.id.event_timeline_more_comment);
        commentCardView = (CardView)itemView.findViewById(R.id.event_timeline_comment_card);
    }
}

package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaktopia.tiramisu.jaktopia.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class VHProfileHeader extends RecyclerView.ViewHolder {
    CircleImageView userIconImg;
    TextView usernameTxt;
    TextView userInfoTxt;
    TextView userPostCountTxt;

    public VHProfileHeader(View itemView) {
        super(itemView);
        userIconImg = (CircleImageView)itemView.findViewById(R.id.profile_header_user_icon);
        usernameTxt = (TextView)itemView.findViewById(R.id.profile_header_user_name);
        userInfoTxt = (TextView)itemView.findViewById(R.id.profile_header_user_info);
        userPostCountTxt = (TextView)itemView.findViewById(R.id.profile_header_user_post_count);
    }
}

package com.jaktopia.tiramisu.jaktopia.CommentRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaktopia.tiramisu.jaktopia.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lsoehadak on 2/27/17.
 */
public class VHCommentHeader extends RecyclerView.ViewHolder {
    CircleImageView userIconImg;
    TextView captionTxt;
    TextView postTimeTxt;

    public VHCommentHeader(View itemView) {
        super(itemView);
        userIconImg = (CircleImageView)itemView.findViewById(R.id.comment_header_poster_icon);
        captionTxt = (TextView)itemView.findViewById(R.id.comment_header_caption);
        postTimeTxt = (TextView)itemView.findViewById(R.id.comment_header_time);
    }
}

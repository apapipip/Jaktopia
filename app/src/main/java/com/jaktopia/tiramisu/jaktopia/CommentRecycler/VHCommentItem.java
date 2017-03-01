package com.jaktopia.tiramisu.jaktopia.CommentRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaktopia.tiramisu.jaktopia.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class VHCommentItem extends RecyclerView.ViewHolder {
    CircleImageView userIconImg;
    TextView commentContentTxt;
    TextView commentTimeTxt;

    public VHCommentItem(View itemView) {
        super(itemView);
        userIconImg = (CircleImageView)itemView.findViewById(R.id.comment_item_poster_icon);
        commentContentTxt = (TextView)itemView.findViewById(R.id.comment_item_content);
        commentTimeTxt = (TextView)itemView.findViewById(R.id.comment_item_time);
    }
}

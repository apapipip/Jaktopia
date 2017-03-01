package com.jaktopia.tiramisu.jaktopia.CommentRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Comment;
import com.jaktopia.tiramisu.jaktopia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context mContext;
    LayoutInflater inflater;
    List<Comment> comments = new ArrayList<Comment>();

    public CommentRecyclerAdapter(Context mContext, List<Comment> comments) {
        this.mContext = mContext;
        this.comments = comments;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.comment_header_layout, parent, false);
            return new VHCommentHeader(view);
        } else if(viewType == TYPE_ITEM){
            View view = inflater.inflate(R.layout.comment_item_layout, parent, false);
            return new VHCommentItem(view);
        }
        throw new RuntimeException("no type found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHCommentHeader) {
            VHCommentHeader vhCommentHeader = (VHCommentHeader)holder;
            /* set user icon */
            // get pict from url using picasso
            /* set caption */
            vhCommentHeader.captionTxt.setText(comments.get(position).getUsername() + " " + comments.get(position).getContent());
            /* set time */
            vhCommentHeader.postTimeTxt.setText("5 minutes ago");
        } else if(holder instanceof VHCommentItem) {
            VHCommentItem vhCommentItem = (VHCommentItem)holder;
            /* set user icon */
            // get pict from url using picasso
            /* set caption */
            vhCommentItem.commentContentTxt.setText(comments.get(position).getUsername() + " " + comments.get(position).getContent());
            /* set time */
            vhCommentItem.commentTimeTxt.setText("5 minutes ago");
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
        return comments.size();
    }
}

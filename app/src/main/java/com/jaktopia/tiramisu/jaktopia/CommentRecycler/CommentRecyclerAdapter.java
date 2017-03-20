package com.jaktopia.tiramisu.jaktopia.CommentRecycler;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaktopia.tiramisu.jaktopia.ObjectClass.Comment;
import com.jaktopia.tiramisu.jaktopia.PeekProfileActivity;
import com.jaktopia.tiramisu.jaktopia.R;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof VHCommentHeader) {
            VHCommentHeader vhCommentHeader = (VHCommentHeader)holder;
            /* set user icon */
            if(comments.get(position).getUserIconUrl() != "null")
                Picasso.with(mContext).load(comments.get(position).getUserIconUrl()).into(vhCommentHeader.userIconImg);
            /* set caption */
            if (Build.VERSION.SDK_INT >= 24) {
                vhCommentHeader.captionTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + comments.get(position).getUsername()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + comments.get(position).getContent()
                                + "</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                vhCommentHeader.captionTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + comments.get(position).getUsername()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + comments.get(position).getContent()
                                + "</font>"));
            }
            /* set time */
            vhCommentHeader.postTimeTxt.setText("5 minutes ago");

            vhCommentHeader.userIconImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PeekProfileActivity.class);
                    intent.putExtra("user_id", comments.get(position).getUserId());
                    mContext.startActivity(intent);
                }
            });
        } else if(holder instanceof VHCommentItem) {
            VHCommentItem vhCommentItem = (VHCommentItem)holder;
            /* set user icon */
            if(comments.get(position).getUserIconUrl() != "null")
                Picasso.with(mContext).load(comments.get(position).getUserIconUrl()).into(vhCommentItem.userIconImg);
            /* set caption */
            if (Build.VERSION.SDK_INT >= 24) {
                vhCommentItem.commentContentTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + comments.get(position).getUsername()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + comments.get(position).getContent()
                                + "</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                vhCommentItem.commentContentTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + comments.get(position).getUsername()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + comments.get(position).getContent()
                                + "</font>"));
            }
            /* set time */
            vhCommentItem.commentTimeTxt.setText("5 minutes ago");

            vhCommentItem.userIconImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PeekProfileActivity.class);
                    intent.putExtra("user_id", comments.get(position).getUserId());
                    mContext.startActivity(intent);
                }
            });
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

    public void setComments(List<Comment> comments) {
        //this.comments.clear();
        this.comments = comments;
    }
}

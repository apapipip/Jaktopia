package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.CommentActivity;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Comment;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.PeekProfileActivity;
import com.jaktopia.tiramisu.jaktopia.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class TimelineRecyclerAdapter extends RecyclerView.Adapter<VHEventItem> implements IVolleyCallBack {
    Context mContext;
    LayoutInflater inflater;
    List<Event> events = new ArrayList<Event>();
    List<Comment> lastComments = new ArrayList<Comment>();

    String favoritePostReqUrl;
    RequestQueue requestQueue;

    int clickedItemPosition;
    VHEventItem clickedHolder;

    public TimelineRecyclerAdapter(Context mContext, List<Event> events, List<Comment> lastComments) {
        this.mContext = mContext;
        this.events = events;
        this.lastComments = lastComments;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public VHEventItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_timeline_layout, parent, false);
        return new VHEventItem(view);
    }

    @Override
    public void onBindViewHolder(final VHEventItem holder, final int position) {
        /* set user icon */
        if(!events.get(position).getUserIconUrl().equals("null"))
            Picasso.with(mContext).load(events.get(position).getUserIconUrl()).placeholder(R.drawable.image_placeholder).
                    error(R.drawable.image_placeholder).
                    into(holder.userIconImg);

        /* set category icon */
        if(events.get(position).getCategoryName().equals("Accident"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_accident);
        else if(events.get(position).getCategoryName().equals("Food"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_food);
        else if(events.get(position).getCategoryName().equals("Education"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_education);
        else if(events.get(position).getCategoryName().equals("Sport"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_sport);
        else if(events.get(position).getCategoryName().equals("Traffic"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_traffic);
        else if(events.get(position).getCategoryName().equals("Entertainment"))
            holder.categoryIconImg.setImageResource(R.drawable.ic_category_entertainment);

        /* set event name */
        holder.eventNameTxt.setText(events.get(position).getEventName());
        /* set category name and event location */
        if (Build.VERSION.SDK_INT >= 24) {
            holder.categoryNameAndLocationTxt.setText(Html.fromHtml(
                    "<font face=\"sans-serif-light\">"
                        + events.get(position).getCategoryName()
                        + " at "
                        + "</font>"
                        + "<font face=\"sans-serif-medium\">"
                        + events.get(position).getLocation()
                        + "</font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.categoryNameAndLocationTxt.setText(Html.fromHtml(
                    "<font face=\"sans-serif-light\">"
                        + events.get(position).getCategoryName()
                        + " at "
                        + "</font>"
                        + "<font face=\"sans-serif-medium\">"
                        + events.get(position).getLocation()
                        + "</font>"));
        }
        /* set post time */
        long time = System.currentTimeMillis()/1000 - events.get(position).getPostTime();
        holder.postTimeTxt.setText("5 minutes ago");

        /* set event photo */
        if(!events.get(position).getPhotoUrl().equals("null")) {
            Picasso.with(mContext).load(events.get(position).getPhotoUrl()).placeholder(R.drawable.image_placeholder).
                    error(R.drawable.image_placeholder).
                    into(holder.eventPhotoImg);
            holder.eventPhotoImg.setVisibility(View.VISIBLE);
        } else {
            /*
            holder.eventPhotoImg.getLayoutParams().width = 0;
            holder.eventPhotoImg.getLayoutParams().height = 0;
            RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams)holder.eventPhotoImg.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            holder.eventPhotoImg.setLayoutParams(layoutParams);
            holder.eventPhotoImg.setImageResource(android.R.color.transparent);
            */
            holder.eventPhotoImg.setVisibility(GONE);
        }

        /* set favorite button icon */
        if(events.get(position).getIsFavorite() == 0)
            holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
        else
            holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);

        /* set favorite count */
        holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount() + " favorites");
        /* set user caption */
        if (Build.VERSION.SDK_INT >= 24) {
            holder.posterCaptionTxt.setText(Html.fromHtml(
                    "<font face=\"sans-serif-medium\">"
                        + "<b>"
                        + events.get(position).getFirstName()
                        + " "
                        + "</font>"
                        + "</b>"
                        + "<font face=\"sans-serif-light\">"
                        + events.get(position).getCaption()
                        + "</font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.posterCaptionTxt.setText(Html.fromHtml(
                    "<font face=\"sans-serif-medium\">"
                        + "<b>"
                        + events.get(position).getFirstName()
                        + " "
                        + "</font>"
                        + "</b>"
                        + "<font face=\"sans-serif-light\">"
                        + events.get(position).getCaption()
                        + "</font>"));
        }

        /* set comment if there any */
        int commentCount = 0;
        for(int i=0;i<lastComments.size();i++) {
            //Log.e("cek id", lastComments.get(i).getEventId() + " - " + events.get(position).getEventId() + " at " + position);
            if(commentCount == 0) {
                if(lastComments.get(i).getEventId() == events.get(position).getEventId()) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        holder.commentTxt.setText(Html.fromHtml(
                                "<font face=\"sans-serif-medium\">"
                                        + "<b>"
                                        + lastComments.get(i).getUsername()
                                        + " "
                                        + "</font>"
                                        + "</b>"
                                        + "<font face=\"sans-serif-light\">"
                                        + lastComments.get(i).getContent()
                                        + "</font>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.commentTxt.setText(Html.fromHtml(
                                "<font face=\"sans-serif-medium\">"
                                        + "<b>"
                                        + lastComments.get(i).getUsername()
                                        + " "
                                        + "</font>"
                                        + "</b>"
                                        + "<font face=\"sans-serif-light\">"
                                        + lastComments.get(i).getContent()
                                        + "</font>"));
                    }
                    commentCount++;
                }
            } else {
                if(lastComments.get(i).getEventId() == events.get(position).getEventId()) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        holder.comment2Txt.setText(Html.fromHtml(
                                "<font face=\"sans-serif-medium\">"
                                        + "<b>"
                                        + lastComments.get(i).getUsername()
                                        + " "
                                        + "</font>"
                                        + "</b>"
                                        + "<font face=\"sans-serif-light\">"
                                        + lastComments.get(i).getContent()
                                        + "</font>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        holder.comment2Txt.setText(Html.fromHtml(
                                "<font face=\"sans-serif-medium\">"
                                        + "<b>"
                                        + lastComments.get(i).getUsername()
                                        + " "
                                        + "</font>"
                                        + "</b>"
                                        + "<font face=\"sans-serif-light\">"
                                        + lastComments.get(i).getContent()
                                        + "</font>"));
                    }
                    commentCount++;
                }
            }
        }
        if(commentCount == 2) {
            holder.commentTxt.setVisibility(View.VISIBLE);
            holder.comment2Txt.setVisibility(View.VISIBLE);
        } else if(commentCount == 1){
            holder.commentTxt.setVisibility(View.VISIBLE);
            holder.comment2Txt.setVisibility(GONE);
        } else {
            holder.commentTxt.setVisibility(GONE);
            holder.comment2Txt.setVisibility(GONE);
        }

        /* set more comment icon */
        if(events.get(position).getMoreCommentCount() > 0)
            holder.moreIconImg.setVisibility(View.VISIBLE);
        else
            holder.moreIconImg.setVisibility(GONE);

        /* set more comment if there any */
        if(events.get(position).getMoreCommentCount() > 0) {
            holder.moreCommentTxt.setText(events.get(position).getMoreCommentCount() + " more comments");
            holder.moreCommentTxt.setVisibility(View.VISIBLE);
        }
        else {
            holder.moreCommentTxt.setVisibility(GONE);
        }

        /* set listener to profile picture */
        holder.userIconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PeekProfileActivity.class);
                intent.putExtra("user_id", events.get(position).getUserId());
                mContext.startActivity(intent);
            }
        });

        /* set listener to comment card view */
        holder.commentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("event_id", events.get(position).getEventId());
                mContext.startActivity(intent);
            }
        });

        /* set listener to favorite button and text  */
        holder.favoriteIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItemPosition = position;
                clickedHolder = holder;
                if(events.get(position).getIsFavorite() == 0) {
                    events.get(position).setIsFavorite(1);
                    holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);
                    events.get(position).setFavoriteCount(events.get(position).getFavoriteCount()+1);
                    holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount()+" favorites");
                    postFavoriteStatusToAPI(0, events.get(position).getEventId());
                } else {
                    events.get(position).setIsFavorite(0);
                    holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
                    events.get(position).setFavoriteCount(events.get(position).getFavoriteCount()-1);
                    holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount()+" favorites");
                    postFavoriteStatusToAPI(1, events.get(position).getEventId());
                }
            }
        });

        holder.favoriteCountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItemPosition = position;
                clickedHolder = holder;
                if(events.get(position).getIsFavorite() == 0) {
                    events.get(position).setIsFavorite(1);
                    holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);
                    events.get(position).setFavoriteCount(events.get(position).getFavoriteCount()+1);
                    holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount()+" favorites");
                    postFavoriteStatusToAPI(0, events.get(position).getEventId());
                } else {
                    events.get(position).setIsFavorite(0);
                    holder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
                    events.get(position).setFavoriteCount(events.get(position).getFavoriteCount()-1);
                    holder.favoriteCountTxt.setText(events.get(position).getFavoriteCount()+" favorites");
                    postFavoriteStatusToAPI(1, events.get(position).getEventId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed() {
        if(events.get(clickedItemPosition).getIsFavorite() == 1) {
            events.get(clickedItemPosition).setIsFavorite(0);
            events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount() - 1);
            clickedHolder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
            clickedHolder.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount() + " favorites");
        } else {
            events.get(clickedItemPosition).setIsFavorite(1);
            events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount() + 1);
            clickedHolder.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);
            clickedHolder.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount() + " favorites");
        }
    }

    private void postFavoriteStatusToAPI(int favoriteStatus, int eventId) {
        /* create JSONObject data */
        JSONObject favoriteObj = new JSONObject();
        try {
            favoriteObj.put("accountID", 1);
            favoriteObj.put("eventID", eventId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(favoriteStatus == 0)
            favoritePostReqUrl = "https://aegis.web.id/jaktopia/api/v1/favorite/add";
        else
            favoritePostReqUrl = "https://aegis.web.id/jaktopia/api/v1/favorite/remove";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, favoritePostReqUrl, favoriteObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String successStatus = response.getString("success");
                            String message = response.getString("message");
                            if(successStatus.equals("true")) {
                                onSuccess();
                            } else {
                                onFailed();
                            }
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                        onFailed();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setLastComments(List<Comment> lastComments) {
        this.lastComments = lastComments;
    }
}

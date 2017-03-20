package com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/27/17.
 */

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IVolleyCallBack {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context mContext;
    LayoutInflater inflater;
    ProfileInfo userInfo;
    List<Event> events = new ArrayList<Event>();
    List<Comment> lastComments = new ArrayList<Comment>();

    String favoritePostReqUrl;
    RequestQueue requestQueue;

    int clickedItemPosition;
    VHEventItem clickedHolder;

    public ProfileRecyclerAdapter(Context mContext, ProfileInfo userInfo, List<Event> events, List<Comment> lastComments) {
        this.mContext = mContext;
        this.userInfo = userInfo;
        this.events = events;
        this.lastComments = lastComments;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.profile_header_layout, parent, false);
            return new VHProfileHeader(view);
        } else if(viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.event_timeline_layout, parent, false);
            return new VHEventItem(view);
        }
        throw new RuntimeException("no type found");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof VHProfileHeader) {
            VHProfileHeader vhProfileHeader = (VHProfileHeader)holder;
            //if(userInfo == null) Log.e("adkjhaskdhaskdhas", "null");
            if(!userInfo.getUserIconUrl().equals("null"))
                Picasso.with(mContext).load(userInfo.getUserIconUrl()).placeholder(R.drawable.image_placeholder).
                        error(R.drawable.image_placeholder).
                        into(vhProfileHeader.userIconImg);
            vhProfileHeader.usernameTxt.setText(userInfo.getUserFullName());
            vhProfileHeader.userInfoTxt.setText(userInfo.getUserInfo());
            vhProfileHeader.userPostCountTxt.setText(userInfo.getUserPostCount()+" events");

        } else if(holder instanceof VHEventItem) {
            int newPosition = position - 1;
            final VHEventItem vhEventItem = (VHEventItem)holder;
            /* set user icon */
            if(events.get(newPosition).getUserIconUrl() != "null")
                Picasso.with(mContext).load(events.get(newPosition).getUserIconUrl()).placeholder(R.drawable.image_placeholder).
                        error(R.drawable.image_placeholder).
                        into(vhEventItem.userIconImg);

            /* set category icon */
            if(events.get(newPosition).getCategoryName().equals("Accident"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_accident);
            else if(events.get(newPosition).getCategoryName().equals("Food"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_food);
            else if(events.get(newPosition).getCategoryName().equals("Education"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_education);
            else if(events.get(newPosition).getCategoryName().equals("Sport"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_sport);
            else if(events.get(newPosition).getCategoryName().equals("Traffic"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_traffic);
            else if(events.get(newPosition).getCategoryName().equals("Entertainment"))
                vhEventItem.categoryIconImg.setImageResource(R.drawable.ic_category_entertainment);

            /* set event name */
            vhEventItem.eventNameTxt.setText(events.get(newPosition).getEventName());
            /* set category name and event location */
            if (Build.VERSION.SDK_INT >= 24) {
                vhEventItem.categoryNameAndLocationTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-light\">"
                                + events.get(newPosition).getCategoryName()
                                + " at "
                                + "</font>"
                                + "<font face=\"sans-serif-medium\">"
                                + events.get(newPosition).getLocation()
                                + "</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                vhEventItem.categoryNameAndLocationTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-light\">"
                                + events.get(newPosition).getCategoryName()
                                + " at "
                                + "</font>"
                                + "<font face=\"sans-serif-medium\">"
                                + events.get(newPosition).getLocation()
                                + "</font>"));
            }
            /* set post time */
            long time = System.currentTimeMillis()/1000 - events.get(newPosition).getPostTime();
            vhEventItem.postTimeTxt.setText("5 minutes ago");

            /* set event photo */
            if(!events.get(newPosition).getPhotoUrl().equals("null")) {
                Picasso.with(mContext).load(events.get(newPosition).getPhotoUrl()).placeholder(R.drawable.image_placeholder).
                        error(R.drawable.image_placeholder).
                        into(vhEventItem.eventPhotoImg);
                vhEventItem.eventPhotoImg.setVisibility(View.VISIBLE);
            } else {
                /*
                vhEventItem.eventPhotoImg.getLayoutParams().width = 0;
                vhEventItem.eventPhotoImg.getLayoutParams().height = 0;
                RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams)vhEventItem.eventPhotoImg.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                vhEventItem.eventPhotoImg.setLayoutParams(layoutParams);
                vhEventItem.eventPhotoImg.setImageResource(android.R.color.transparent);
                */
                vhEventItem.eventPhotoImg.setVisibility(View.GONE);
            }

            /* set favorite button icon */
            if(events.get(newPosition).getIsFavorite() == 0)
                vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
            else
                vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);

            /* set favorite count */
            vhEventItem.favoriteCountTxt.setText(events.get(newPosition).getFavoriteCount() + " favorites");
            /* set user caption */
            if (Build.VERSION.SDK_INT >= 24) {
                vhEventItem.posterCaptionTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + events.get(newPosition).getFirstName()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + events.get(newPosition).getCaption()
                                + "</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                vhEventItem.posterCaptionTxt.setText(Html.fromHtml(
                        "<font face=\"sans-serif-medium\">"
                                + "<b>"
                                + events.get(newPosition).getFirstName()
                                + " "
                                + "</font>"
                                + "</b>"
                                + "<font face=\"sans-serif-light\">"
                                + events.get(newPosition).getCaption()
                                + "</font>"));
            }
            /* set comment if there any */
            int commentCount = 0;
            for(int i=0;i<lastComments.size();i++) {
                //Log.e("cek id", lastComments.get(i).getEventId() + " - " + events.get(position).getEventId() + " at " + position);
                if(commentCount == 0) {
                    if(lastComments.get(i).getEventId() == events.get(newPosition).getEventId()) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            vhEventItem.commentTxt.setText(Html.fromHtml(
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
                            vhEventItem.commentTxt.setText(Html.fromHtml(
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
                    if(lastComments.get(i).getEventId() == events.get(newPosition).getEventId()) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            vhEventItem.comment2Txt.setText(Html.fromHtml(
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
                            vhEventItem.comment2Txt.setText(Html.fromHtml(
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
                vhEventItem.commentTxt.setVisibility(View.VISIBLE);
                vhEventItem.comment2Txt.setVisibility(View.VISIBLE);
            } else if(commentCount == 1){
                vhEventItem.commentTxt.setVisibility(View.VISIBLE);
                vhEventItem.comment2Txt.setVisibility(GONE);
            } else {
                vhEventItem.commentTxt.setVisibility(GONE);
                vhEventItem.comment2Txt.setVisibility(GONE);
            }

            /* set more comment icon */
            if(events.get(newPosition).getMoreCommentCount() > 0)
                vhEventItem.moreIconImg.setVisibility(View.VISIBLE);
            else
                vhEventItem.moreIconImg.setVisibility(GONE);

            /* set more comment if there any */
            if(events.get(newPosition).getMoreCommentCount() > 0) {
                vhEventItem.moreCommentTxt.setText(events.get(newPosition).getMoreCommentCount() + " more comments");
                vhEventItem.moreCommentTxt.setVisibility(View.VISIBLE);
            }
            else {
                vhEventItem.moreCommentTxt.setVisibility(GONE);
            }

            /* ================================= */
            /* set listener to comment card view */
            /* ================================= */
            vhEventItem.commentCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra("event_id", events.get(position-1).getEventId());
                    mContext.startActivity(intent);
                }
            });

            /* ========================================= */
            /* set listener to favorite button and text  */
            /* ========================================= */
            vhEventItem.favoriteIconBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedItemPosition = position-1;
                    clickedHolder = vhEventItem;
                    if(events.get(clickedItemPosition).getIsFavorite() == 0) {
                        events.get(clickedItemPosition).setIsFavorite(1);
                        vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);
                        events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount()+1);
                        vhEventItem.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount()+" favorites");
                    } else {
                        events.get(clickedItemPosition).setIsFavorite(0);
                        vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
                        events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount()-1);
                        vhEventItem.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount()+" favorites");
                    }
                }
            });

            vhEventItem.favoriteCountTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedItemPosition = position-1;
                    clickedHolder = vhEventItem;
                    if(events.get(clickedItemPosition).getIsFavorite() == 0) {
                        events.get(clickedItemPosition).setIsFavorite(1);
                        vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like_on_click);
                        events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount()+1);
                        vhEventItem.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount()+" favorites");
                        postFavoriteStatusToAPI(0, events.get(clickedItemPosition).getEventId());
                    } else {
                        events.get(clickedItemPosition).setIsFavorite(0);
                        vhEventItem.favoriteIconBtn.setBackgroundResource(R.drawable.ic_like);
                        events.get(clickedItemPosition).setFavoriteCount(events.get(clickedItemPosition).getFavoriteCount()-1);
                        vhEventItem.favoriteCountTxt.setText(events.get(clickedItemPosition).getFavoriteCount()+" favorites");
                        postFavoriteStatusToAPI(1, events.get(clickedItemPosition).getEventId());
                    }
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
        return events.size()+1;
    }

    @Override
    public void onFailed() {
        // revert like status
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

    @Override
    public void onSuccess() {

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

    public void setUserInfo(ProfileInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setLastComments(List<Comment> lastComments) {
        this.lastComments = lastComments;
    }
}

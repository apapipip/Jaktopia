package com.jaktopia.tiramisu.jaktopia;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Comment;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.ProfileRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class PeekProfileActivity extends AppCompatActivity {
    SwipeRefreshLayout refreshLyt;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    ProgressBar progressBar;

    int userId;
    ProfileInfo userInfo;
    List<Event> events = new ArrayList<Event>();
    List<Comment> lastComments = new ArrayList<Comment>();

    String lastCommentReqUrl;
    String profileReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peek_profile);

        refreshLyt = (SwipeRefreshLayout)findViewById(R.id.peek_profile_refresh_layout);
        toolbar = (Toolbar)findViewById(R.id.peek_profile_toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.peek_profile_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.peek_profile_progress_bar);

        /* set toolbar element */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);

        /* get eventId from intent */
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", 0);

        /* set swipe refresh layout listener */
        refreshLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLyt.setRefreshing(true);
                getProfileFromAPI(volleyCallBack);
                getLastCommentFromAPI(volleyCallBack);
            }
        });


        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                /* show recyclerView and hide progressBar */
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                refreshLyt.setRefreshing(false);

                profileRecyclerAdapter.setUserInfo(userInfo);
                profileRecyclerAdapter.setEvents(new ArrayList<Event>(events));
                profileRecyclerAdapter.setLastComments(new ArrayList<Comment>(lastComments));
                profileRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                refreshLyt.setRefreshing(false);
            }
        };

        profileRecyclerAdapter = new ProfileRecyclerAdapter(this, userInfo, new ArrayList<Event>(events), new ArrayList<Comment>(lastComments));
        recyclerView.setAdapter(profileRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
         /* get data from API */
        getProfileFromAPI(volleyCallBack);
        getLastCommentFromAPI(volleyCallBack);

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProfileFromAPI(final IVolleyCallBack volleyCallBack) {
        /* clear events list */
        events.clear();

         /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);

        profileReqUrl = "https://aegis.web.id/jaktopia/api/v1/profile/" + userId;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, profileReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObj = response.getJSONObject("data");
                            JSONArray eventDataArr = dataObj.getJSONArray("events");
                            JSONArray profileDataArr = dataObj.getJSONArray("profile");
                            for(int i=0;i<profileDataArr.length();i++) {
                                userInfo = new ProfileInfo();
                                JSONObject profileDataObj = profileDataArr.getJSONObject(i);
                                userInfo.setUserFullName(profileDataObj.getString("account_username"));
                                userInfo.setUserIconUrl(profileDataObj.getString("account_photo"));
                                userInfo.setUserInfo(profileDataObj.getString("account_description"));
                                userInfo.setUserPostCount(Integer.parseInt(profileDataObj.getString("sum_events")));
                            }

                            for(int i = 0; i< eventDataArr.length(); i++) {
                                Event event = new Event();
                                JSONObject eventDataObj = eventDataArr.getJSONObject(i);
                                event.setUserId(eventDataObj.getInt("account_id"));
                                event.setEventId(eventDataObj.getInt("event_id"));
                                event.setUserIconUrl(eventDataObj.getString("account_photo"));
                                event.setEventName(eventDataObj.getString("event_name"));
                                event.setCategoryName(eventDataObj.getString("category_name"));
                                event.setLocation(eventDataObj.getString("location_name"));
                                event.setPhotoUrl(eventDataObj.getString("event_photo"));
                                if(eventDataObj.getBoolean("is_favorited"))
                                    event.setIsFavorite(1);
                                else
                                    event.setIsFavorite(0);
                                event.setFavoriteCount(eventDataObj.getInt("sum_favorite"));
                                event.setFirstName(eventDataObj.getString("account_username"));
                                if(eventDataObj.has("event_caption") && !eventDataObj.isNull("event_caption"))
                                    event.setCaption(eventDataObj.getString("event_caption"));
                                else
                                    event.setCaption("");
                                event.setMoreCommentCount(eventDataObj.getInt("left_comments"));

                                /* add event to allEvents list */
                                events.add(event);
                            }
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            volleyCallBack.onFailed();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    private void getLastCommentFromAPI(final IVolleyCallBack volleyCallBack) {
        /* clear last comment */
        lastComments.clear();

        lastCommentReqUrl = "https://aegis.web.id/jaktopia/api/v1/two_comment";
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, lastCommentReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArr = response.getJSONArray("data");
                            for(int i=0;i<dataArr.length();i++) {
                                Comment comment = new Comment();
                                JSONObject dataObj = dataArr.getJSONObject(i);
                                comment.setEventId(dataObj.getInt("event_id"));
                                comment.setContent(dataObj.getString("comment_content"));
                                comment.setUsername(dataObj.getString("account_name"));
                                comment.setUserId(dataObj.getInt("account_id"));

                                //Log.e("eventID", ""+comment.getEventId());

                                lastComments.add(comment);
                            }
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}

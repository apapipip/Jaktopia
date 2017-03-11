package com.jaktopia.tiramisu.jaktopia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.TimelineRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/18/17.
 */

public class TimelineFragment extends Fragment {
    Context mContext;

    SwipeRefreshLayout refreshLyt;
    Button postBtn;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TimelineRecyclerAdapter timelineRecyclerAdapter;
    Menu menu;

    List<Event> events = new ArrayList<Event>();
    boolean isFavorite;

    String eventTimelineReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        mContext = getActivity();
        isFavorite = false;
        timelineRecyclerAdapter = new TimelineRecyclerAdapter(mContext, new ArrayList<Event>(events));

        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //recyclerView.scrollToPosition(0);
                refreshLyt.setRefreshing(false);

                recyclerView.getRecycledViewPool().clear();
                timelineRecyclerAdapter.setEvents(new ArrayList<Event>(events));
                timelineRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                refreshLyt.setRefreshing(false);
            }
        };
    }

    @Override
    public void onResume() {
        /* get data from API */
        if(!isFavorite) {
            getEventFromAPI(volleyCallBack);
        }
        else {
            getFavoriteEventFromAPI(volleyCallBack);
        }

        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_timeline_layout, container, false);
        refreshLyt = (SwipeRefreshLayout)view.findViewById(R.id.timeline_refresh_layout);
        postBtn = (Button)view.findViewById(R.id.timeline_post_button);
        recyclerView = (RecyclerView)view.findViewById(R.id.timeline_recycler_view);
        progressBar = (ProgressBar)view.findViewById(R.id.timeline_progress_bar);

        /* set post button listener */
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostEventActivity.class);
                startActivity(intent);
            }
        });

        /* set swipe refresh layout listener */
        refreshLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLyt.setRefreshing(true);
                if(!isFavorite)
                    getEventFromAPI(volleyCallBack);
                else
                    getFavoriteEventFromAPI(volleyCallBack);
            }
        });

        recyclerView.setAdapter(timelineRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
    }

    @Override /* inflate menu to toolbar */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.timeline_menu, menu);
        this.menu = menu;
        if(isFavorite)
            menu.getItem(1).setIcon(R.drawable.ic_favorites_onclick);
        else
            menu.getItem(1).setIcon(R.drawable.ic_favorites);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.timeline_setting_menu) {
            Intent intent = new Intent(mContext, SettingActivity.class);
            startActivity(intent);
        } else if(id == R.id.timeline_favorite_menu) {
            if(isFavorite) {
                menu.getItem(1).setIcon(R.drawable.ic_favorites);
                isFavorite = false;
                getEventFromAPI(volleyCallBack);
            }
            else {
                menu.getItem(1).setIcon(R.drawable.ic_favorites_onclick);
                isFavorite = true;
                getFavoriteEventFromAPI(volleyCallBack);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getEventFromAPI(final IVolleyCallBack volleyCallBack) {
        /* clear events list */
        events.clear();

        /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);

        eventTimelineReqUrl = "https://aegis.web.id/jaktopia/api/v1/event/1";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, eventTimelineReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArr = response.getJSONArray("data");
                            for(int i=0;i<dataArr.length();i++) {
                                Event event = new Event();
                                JSONObject dataObj = dataArr.getJSONObject(i);
                                event.setUserId(dataObj.getInt("account_id"));
                                event.setEventId(dataObj.getInt("event_id"));
                                event.setUserIconUrl(dataObj.getString("account_photo"));
                                event.setEventName(dataObj.getString("event_name"));
                                event.setCategoryName(dataObj.getString("category_name"));
                                event.setLocation(dataObj.getString("location_name"));
                                event.setPhotoUrl(dataObj.getString("event_photo"));
                                if(dataObj.getBoolean("is_favorited"))
                                    event.setIsFavorite(1);
                                else
                                    event.setIsFavorite(0);
                                event.setFavoriteCount(dataObj.getInt("sum_favorite"));
                                event.setFirstName(dataObj.getString("account_username"));
                                if(dataObj.has("event_caption") && !dataObj.isNull("event_caption"))
                                    event.setCaption(dataObj.getString("event_caption"));
                                else
                                    event.setCaption("");
                                event.setMoreCommentCount(dataObj.getInt("left_comments"));

                                /* add event to allEvents list */
                                events.add(event);
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
                        Log.e("Volley", "Error");
                        volleyCallBack.onFailed();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    private void getFavoriteEventFromAPI(final IVolleyCallBack volleyCallBack) {
        /* clear events list */
        events.clear();

        /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);

        eventTimelineReqUrl = "https://aegis.web.id/jaktopia/api/v1/favorite/1";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, eventTimelineReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArr = response.getJSONArray("data");
                            for(int i=0;i<dataArr.length();i++) {
                                Event event = new Event();
                                JSONObject dataObj = dataArr.getJSONObject(i);
                                event.setUserId(dataObj.getInt("account_id"));
                                event.setEventId(dataObj.getInt("event_id"));
                                event.setUserIconUrl(dataObj.getString("account_photo"));
                                event.setEventName(dataObj.getString("event_name"));
                                event.setCategoryName(dataObj.getString("category_name"));
                                event.setLocation(dataObj.getString("location_name"));
                                event.setPhotoUrl(dataObj.getString("event_photo"));
                                if(dataObj.getBoolean("is_favorited"))
                                    event.setIsFavorite(1);
                                else
                                    event.setIsFavorite(0);
                                event.setFavoriteCount(dataObj.getInt("sum_favorite"));
                                event.setFirstName(dataObj.getString("account_username"));
                                if(dataObj.has("event_caption") && !dataObj.isNull("event_caption"))
                                    event.setCaption(dataObj.getString("event_caption"));
                                else
                                    event.setCaption("");
                                event.setMoreCommentCount(dataObj.getInt("left_comments"));

                                /* add event to allEvents list */
                                events.add(event);
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
                        Log.e("Volley", "Error");
                        volleyCallBack.onFailed();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

}

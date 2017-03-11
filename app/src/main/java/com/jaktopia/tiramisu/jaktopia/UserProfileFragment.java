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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.ProfileAndTimelineRecycler.ProfileRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class UserProfileFragment extends Fragment {
    Context mContext;

    SwipeRefreshLayout refreshLyt;
    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    ProgressBar progressBar;

    ProfileInfo userInfo;
    List<Event> events = new ArrayList<Event>();

    String profileReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        mContext = getActivity();
        userInfo = new ProfileInfo();
        profileRecyclerAdapter = new ProfileRecyclerAdapter(mContext, userInfo, new ArrayList<Event>(events));

        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                refreshLyt.setRefreshing(false);

                profileRecyclerAdapter.setUserInfo(userInfo);
                profileRecyclerAdapter.setEvents(new ArrayList<Event>(events));
                profileRecyclerAdapter.notifyDataSetChanged();
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
        getProfileFromAPI(volleyCallBack);

        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        refreshLyt = (SwipeRefreshLayout)view.findViewById(R.id.profile_refresh_layout);
        recyclerView = (RecyclerView)view.findViewById(R.id.profile_recycler_view);
        progressBar = (ProgressBar)view.findViewById(R.id.profile_progress_bar);

        /* set swipe refresh layout listener */
        refreshLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLyt.setRefreshing(true);
                getProfileFromAPI(volleyCallBack);
            }
        });

        recyclerView.setAdapter(profileRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
    }

    @Override /* inflate menu to toolbar */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.timeline_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.timeline_setting_menu) {
            Intent intent = new Intent(mContext, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProfileFromAPI(final IVolleyCallBack volleyCallBack) {
        /* clear events list */
        events.clear();

        /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);

        profileReqUrl = "https://aegis.web.id/jaktopia/api/v1/profile/1";
        requestQueue = Volley.newRequestQueue(mContext);
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
}

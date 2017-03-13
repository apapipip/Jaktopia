package com.jaktopia.tiramisu.jaktopia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.MapData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class MapEventFragment extends Fragment {
    Context mContext;
    List<MapData> mapDatas = new ArrayList<MapData>();

    MapView mapView;
    GoogleMap googleMap;

    GPSTracker gpsTracker;
    double userLatitude;
    double userLongitude;

    String mapDataReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_layout, container, false);
        mapView = (MapView)view.findViewById(R.id.map_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        googleMap = map;
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(userLatitude, userLongitude)).title("My location"));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(userLatitude, userLongitude)).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        for(int i=0;i<mapDatas.size();i++) {
                            //Log.e("lat:", mapDatas.get(i).getLatitude()+" at " + i);
                            //Log.e("long:", mapDatas.get(i).getLongitude()+" at " + i);
                            //Log.e("name", mapDatas.get(i).getEventName());
                            BitmapDescriptor bitmapDescriptor;
                            if(mapDatas.get(i).getEventName().equals("Food")){
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_food);
                            } else if(mapDatas.get(i).getEventName().equals("Accident")){
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_accident);
                            } else if(mapDatas.get(i).getEventName().equals("Education")){
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_education);
                            } else if(mapDatas.get(i).getEventName().equals("Sport")){
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_sport);
                            } else if(mapDatas.get(i).getEventName().equals("Traffic")){
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_traffic);
                            } else {
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_entertainment);
                            }
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(mapDatas.get(i).getLatitude(), mapDatas.get(i).getLongitude())).
                                    title(mapDatas.get(i).getEventName()).snippet(mapDatas.get(i).getCategoryName()).icon(bitmapDescriptor));
                        }
                    }
                });
            }

            @Override
            public void onFailed() {

            }
        };

        gpsTracker = new GPSTracker(mContext);
        if(gpsTracker.isCanGetLocation()) {
            userLatitude = gpsTracker.getLatitude();
            userLongitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingAlertDialog();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(gpsTracker.isCanGetLocation()) {
            userLatitude = gpsTracker.getLatitude();
            userLongitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingAlertDialog();
        }
        getMapDataFromAPI(volleyCallBack);
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override /* inflate menu to toolbar */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_menu, menu);
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

    private void getMapDataFromAPI(final IVolleyCallBack volleyCallBack) {
        mapDataReqUrl = "https://aegis.web.id/jaktopia/api/v1/map";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, mapDataReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArr = response.getJSONArray("data");
                            for(int i=0;i<dataArr.length();i++) {
                                MapData mapData = new MapData();
                                JSONObject dataObj = dataArr.getJSONObject(i);
                                mapData.setCategoryId(dataObj.getInt("event_id"));
                                mapData.setEventName(dataObj.getString("event_name"));
                                mapData.setCategoryId(dataObj.getInt("category_id"));
                                mapData.setCategoryName(dataObj.getString("category_name"));
                                mapData.setLocationName(dataObj.getString("location_name"));
                                mapData.setLatitude(dataObj.getDouble("location_latitude"));
                                mapData.setLongitude(dataObj.getDouble("location_longitude"));

                                mapDatas.add(mapData);
                                //Log.e("lat:", mapDatas.get(i).getLatitude()+" at " + i);
                                //Log.e("long:", mapDatas.get(i).getLongitude()+" at " + i);
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

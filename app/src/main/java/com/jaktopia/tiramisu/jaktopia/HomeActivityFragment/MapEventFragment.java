package com.jaktopia.tiramisu.jaktopia.HomeActivityFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.jaktopia.tiramisu.jaktopia.GPSTracker.GPSTracker;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.MapData;
import com.jaktopia.tiramisu.jaktopia.R;
import com.jaktopia.tiramisu.jaktopia.SettingActivity;

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
    List<Boolean> selectedCategoryId = new ArrayList<Boolean>();

    MapView mapView;
    GoogleMap googleMap;
    TextView chosenCategoryTxt;
    Button foodBtn;
    Button accidentBtn;
    Button educationBtn;
    Button sportBtn;
    Button trafficBtn;
    Button entertainmentBtn;

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
        mapView = (MapView) view.findViewById(R.id.map_map_view);
        chosenCategoryTxt = (TextView) view.findViewById(R.id.map_chosen_category);
        foodBtn = (Button) view.findViewById(R.id.map_food_button);
        accidentBtn = (Button) view.findViewById(R.id.map_accident_button);
        educationBtn = (Button) view.findViewById(R.id.map_education_button);
        sportBtn = (Button) view.findViewById(R.id.map_sport_button);
        trafficBtn = (Button) view.findViewById(R.id.map_traffic_button);
        entertainmentBtn = (Button) view.findViewById(R.id.map_entertainment_button);

        /* initialize selected category */
        initSelectedCategory();

        /* set onClickListener for each category button */
        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(1, !selectedCategoryId.get(1));
                showEventMarker();
            }
        });

        accidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(0, !selectedCategoryId.get(0));
                showEventMarker();
            }
        });

        educationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(5, !selectedCategoryId.get(5));
                showEventMarker();
            }
        });

        sportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(2, !selectedCategoryId.get(2));
                showEventMarker();
            }
        });

        trafficBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(3, !selectedCategoryId.get(3));
                showEventMarker();
            }
        });

        entertainmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryId.set(4, !selectedCategoryId.get(4));
                showEventMarker();
            }
        });


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
                showEventMarker();
            }

            @Override
            public void onFailed() {

            }
        };

        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.isCanGetLocation()) {
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
        if (gpsTracker.isCanGetLocation()) {
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
        if (id == R.id.timeline_setting_menu) {
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
                            for (int i = 0; i < dataArr.length(); i++) {
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

    private void showEventMarker() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(userLatitude, userLongitude)).title("My location"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(userLatitude, userLongitude)).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                for (int i = 0; i < mapDatas.size(); i++) {
                    if (selectedCategoryId.get(mapDatas.get(i).getCategoryId() - 1)) {
                        BitmapDescriptor bitmapDescriptor;
                        if (mapDatas.get(i).getCategoryName().equals("Food")) {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_food);
                        } else if (mapDatas.get(i).getCategoryName().equals("Accident")) {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_accident);
                        } else if (mapDatas.get(i).getCategoryName().equals("Education")) {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_education);
                        } else if (mapDatas.get(i).getCategoryName().equals("Sport")) {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_sport);
                        } else if (mapDatas.get(i).getCategoryName().equals("Traffic")) {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_traffic);
                        } else {
                            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_entertainment);
                        }
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(mapDatas.get(i).getLatitude(), mapDatas.get(i).getLongitude())).
                                title(mapDatas.get(i).getEventName()).snippet(mapDatas.get(i).getCategoryName()).icon(bitmapDescriptor));
                    }
                }
            }
        });
    }

    private void initSelectedCategory() {
        /* set true for all 6 category */
        for (int i = 0; i < 6; i++) {
            selectedCategoryId.add(true);
        }
    }

}

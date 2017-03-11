package com.jaktopia.tiramisu.jaktopia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by lsoehadak on 2/19/17.
 */

public class MapEventFragment extends Fragment {
    Context mContext;

    MapView mapView;
    GoogleMap googleMap;

    GPSTracker gpsTracker;
    double userLatitude;
    double userLongitude;

    String mapDataReqUrl;
    RequestQueue requestQueue;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mContext = getActivity();

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

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
            }
        });

        return view;
    }

    private void getMapDataFromAPI() {
        mapDataReqUrl = "https://aegis.web.id/jaktopia/api/v1/map";
        requestQueue = Volley.newRequestQueue(mContext);
    }
}

package com.jaktopia.tiramisu.jaktopia;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.GPSTracker.GPSTracker;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class PostEventActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView chosenCategoryTxt;
    Button foodCategoryBtn;
    Button sportCategoryBtn;
    Button educationCategoryBtn;
    Button trafficCategoryBtn;
    Button accidentCategoryBtn;
    Button entertainmentCategoryBtn;
    EditText eventNameEdt;
    EditText eventDescEdt;
    EditText eventLocationEdt;
    Button postBtn;

    String userId;
    int chosenCategoryId;
    String chosenCategoryName;
    double userLatitude;
    double userLongitude;

    GPSTracker gpsTracker;

    String postEventReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        toolbar = (Toolbar) findViewById(R.id.post_event_toolbar);
        chosenCategoryTxt = (TextView) findViewById(R.id.post_event_chosen_category);
        foodCategoryBtn = (Button) findViewById(R.id.map_food_button);
        sportCategoryBtn = (Button) findViewById(R.id.map_sport_button);
        educationCategoryBtn = (Button) findViewById(R.id.map_education_button);
        trafficCategoryBtn = (Button) findViewById(R.id.map_traffic_button);
        accidentCategoryBtn = (Button) findViewById(R.id.map_accident_button);
        entertainmentCategoryBtn = (Button) findViewById(R.id.map_entertainment_button);
        eventNameEdt = (EditText) findViewById(R.id.post_event_insert_name);
        eventDescEdt = (EditText) findViewById(R.id.post_event_insert_description);
        eventLocationEdt = (EditText) findViewById(R.id.post_event_insert_location);
        postBtn = (Button) findViewById(R.id.post_event_post_button);

        /* get user id value from sharedpref */
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfileData", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "-1");

        /* set toolbar element */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);

        /* set category default value */
        chosenCategoryId = 2;
        chosenCategoryName = "Food";
        setChosenCategoryTxt();

        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onFailed() {

            }
        };

        foodCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 2;
                chosenCategoryName = "Food";
                setChosenCategoryTxt();
            }
        });

        sportCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 3;
                chosenCategoryName = "Sport";
                setChosenCategoryTxt();
            }
        });

        educationCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 6;
                chosenCategoryName = "Education";
                setChosenCategoryTxt();
            }
        });

        trafficCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 4;
                chosenCategoryName = "Traffic";
                setChosenCategoryTxt();
            }
        });

        accidentCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 1;
                chosenCategoryName = "Accident";
                setChosenCategoryTxt();
            }
        });

        entertainmentCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategoryId = 5;
                chosenCategoryName = "Entertainment";
                setChosenCategoryTxt();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = new GPSTracker(PostEventActivity.this);
                if (gpsTracker.isCanGetLocation()) {
                    userLatitude = gpsTracker.getLatitude();
                    userLongitude = gpsTracker.getLongitude();
                    //Toast.makeText(PostEventActivity.this, "Your location now is Lat : " + gpsTracker.getLatitude()
                    //+ "Long : " + gpsTracker.getLongitude(), Toast.LENGTH_SHORT ).show();
                } else {
                    gpsTracker.showSettingAlertDialog();
                }
                postEventToAPI(volleyCallBack);
            }
        });
    }

    private void setChosenCategoryTxt() {
        chosenCategoryTxt.setText(chosenCategoryName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postEventToAPI(final IVolleyCallBack volleyCallBack) {
        /* create JSONObject */
        JSONObject eventObj = new JSONObject();
        try {
            eventObj.put("eventName", eventNameEdt.getText());
            eventObj.put("eventPhoto", null);
            eventObj.put("eventCaption", eventDescEdt.getText());
            eventObj.put("categoryID", chosenCategoryId);
            eventObj.put("locationName", eventLocationEdt.getText());
            eventObj.put("locationLatitude", userLatitude);
            eventObj.put("locationLongitude", userLongitude);
            eventObj.put("accountID", 1);

            Log.e("eventName", eventNameEdt.getText() + "");
            Log.e("eventCaption", eventDescEdt.getText() + "");
            Log.e("categoryID", chosenCategoryId + "");
            Log.e("locationName", eventLocationEdt.getText() + "");
            Log.e("locationLatitude", userLatitude + "");
            Log.e("locationLongitude", userLongitude + "");
            Log.e("accountID", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postEventReqUrl = "https://aegis.web.id/jaktopia/api/v1/event";
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, postEventReqUrl, eventObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String successStatus = response.getString("success");
                            String message = response.getString("message");
                            if (successStatus.equals("true")) {
                                volleyCallBack.onSuccess();
                            } else {
                                volleyCallBack.onFailed();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error. Failed to post event", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}

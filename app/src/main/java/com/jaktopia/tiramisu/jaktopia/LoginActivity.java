package com.jaktopia.tiramisu.jaktopia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdt;
    EditText passwordEdt;
    Button signInBtn;
    Button signOutBtn;

    String loginUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = (EditText)findViewById(R.id.login_insert_email);
        passwordEdt = (EditText)findViewById(R.id.login_insert_password);
        signInBtn = (Button)findViewById(R.id.login_sign_in_button);
        signOutBtn = (Button)findViewById(R.id.login_sign_up_button);

        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed() {
                passwordEdt.getText().clear();
            }
        };

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(volleyCallBack);
            }
        });
    }

    private void login(final IVolleyCallBack volleyCallBack) {
        /* create JSONObject */
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("email", emailEdt.getText());
            loginData.put("password", passwordEdt.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loginUrl = "https://aegis.web.id/jaktopia/api/v1/login";
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if(message.equals("Success to login")) {
                                JSONArray dataArr = response.getJSONArray("data");
                                for (int i=0; i<dataArr.length(); i++) {
                                    JSONObject dataObj = dataArr.getJSONObject(i);
                                    SharedPreferences.Editor editor = getSharedPreferences("UserProfileData", MODE_PRIVATE).edit();
                                    editor.putString("userId", dataObj.getString("account_id"));
                                    editor.commit();
                                }
                                volleyCallBack.onSuccess();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                volleyCallBack.onFailed();
                            }
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

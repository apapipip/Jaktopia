package com.jaktopia.tiramisu.jaktopia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameEdt;
    EditText emailEdt;
    EditText passwordEdt;
    EditText passwordConfirmationEdt;
    Button signUpBtn;

    String registerReqUrl;
    RequestQueue requestQueue;
    IVolleyCallBack volleyCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEdt = (EditText) findViewById(R.id.register_insert_username);
        emailEdt = (EditText) findViewById(R.id.register_insert_email);
        passwordEdt = (EditText) findViewById(R.id.register_insert_password);
        passwordConfirmationEdt = (EditText) findViewById(R.id.register_insert_confirm_password);
        signUpBtn = (Button) findViewById(R.id.register_sign_up_button);
    }

    private void Register() {
        /* create JSONObject */
        JSONObject registerObj = new JSONObject();
        try {
            registerObj.put("email", emailEdt.getText());
            registerObj.put("username", usernameEdt.getText());
            registerObj.put("password", passwordEdt.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        registerReqUrl = "https://aegis.web.id/jaktopia/api/v1/register";
        requestQueue = Volley.newRequestQueue(this);
    }
}

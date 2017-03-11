package com.jaktopia.tiramisu.jaktopia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button profileBtn;
    Button notificationBtn;
    Button timelineBtn;
    Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar)findViewById(R.id.setting_toolbar);
        profileBtn = (Button)findViewById(R.id.setting_profile_button);
        notificationBtn = (Button)findViewById(R.id.setting_notification_button);
        timelineBtn = (Button)findViewById(R.id.setting_timeline_button);
        aboutBtn = (Button)findViewById(R.id.setting_about_button);

        /* set toolbar element */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

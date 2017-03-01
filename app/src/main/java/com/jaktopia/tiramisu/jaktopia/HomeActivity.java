package com.jaktopia.tiramisu.jaktopia;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.android.volley.RequestQueue;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Event;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.ProfileInfo;
import com.jaktopia.tiramisu.jaktopia.PagerAdapter.HomePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    HomePagerAdapter mPagerAdapter;

    String eventTimelineReqUrl;
    RequestQueue requestQueue;

    List<Event> allEvents = new ArrayList<Event>();
    List<Event> userEvents = new ArrayList<Event>();
    ProfileInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        tabLayout = (TabLayout)findViewById(R.id.home_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.home_view_pager);

        /* ===================== */
        /* set viewPager element */
        /* ===================== */
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), allEvents, userInfo, 4);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* =================== */
        /* set toolbar element */
        /* =================== */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* ===================== */
        /* set tabLayout element */
        /* ===================== */
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_onclick));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map_unclicked));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_profile_unclicked));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_comingsoon_unclicked));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) tab.setIcon(R.drawable.ic_home_onclick);
                else if(tab.getPosition() == 1) tab.setIcon(R.drawable.ic_map_onclick);
                else if(tab.getPosition() == 2) tab.setIcon(R.drawable.ic_profile_onclick);
                else if(tab.getPosition() == 3) tab.setIcon(R.drawable.ic_comingsoon_onclick);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) tab.setIcon(R.drawable.ic_home_unclicked);
                else if(tab.getPosition() == 1) tab.setIcon(R.drawable.ic_map_unclicked);
                else if(tab.getPosition() == 2) tab.setIcon(R.drawable.ic_profile_unclicked);
                else if(tab.getPosition() == 3) tab.setIcon(R.drawable.ic_comingsoon_unclicked);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override /* inflate menu to toolbar */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

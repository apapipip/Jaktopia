package com.jaktopia.tiramisu.jaktopia;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jaktopia.tiramisu.jaktopia.PagerAdapter.HomePagerAdapter;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbarTitle;
    TabLayout tabLayout;
    ViewPager viewPager;

    HomePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        toolbarTitle = (TextView)findViewById(R.id.home_title);
        tabLayout = (TabLayout)findViewById(R.id.home_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.home_view_pager);
        viewPager.setOffscreenPageLimit(0);

        /* ===================== */
        /* set viewPager element */
        /* ===================== */
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), 4);
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
                if(tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.ic_home_onclick);
                    toolbarTitle.setText("HOME");
                }
                else if(tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.ic_map_onclick);
                    toolbarTitle.setText("MAP");
                }
                else if(tab.getPosition() == 2) {
                    tab.setIcon(R.drawable.ic_profile_onclick);
                    toolbarTitle.setText("PROFILE");
                }
                else if(tab.getPosition() == 3) {
                    tab.setIcon(R.drawable.ic_comingsoon_onclick);
                    toolbarTitle.setText("COMING SOON");
                }
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
}

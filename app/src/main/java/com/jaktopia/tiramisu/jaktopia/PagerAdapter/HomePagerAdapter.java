package com.jaktopia.tiramisu.jaktopia.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jaktopia.tiramisu.jaktopia.HomeActivityFragment.MapEventFragment;
import com.jaktopia.tiramisu.jaktopia.HomeActivityFragment.TimelineFragment;
import com.jaktopia.tiramisu.jaktopia.HomeActivityFragment.UpcomingEventFragment;
import com.jaktopia.tiramisu.jaktopia.HomeActivityFragment.UserProfileFragment;

/**
 * Created by lsoehadak on 2/17/17.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int numOfPages;
    TimelineFragment timelineFragment;
    MapEventFragment mapEventFragment;
    UserProfileFragment userProfileFragment;
    UpcomingEventFragment upcomingEventFragment;

    public HomePagerAdapter(FragmentManager fm, int numOfPages) {
        super(fm);
        this.numOfPages = numOfPages;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            if(timelineFragment == null)
                timelineFragment = new TimelineFragment();
            return timelineFragment;
        } else if(position == 1) {
            mapEventFragment = new MapEventFragment();
            return mapEventFragment;
        } else if(position == 2) {
            if(userProfileFragment == null)
                userProfileFragment = new UserProfileFragment();
            return userProfileFragment;
        } else {
            upcomingEventFragment = new UpcomingEventFragment();
            return upcomingEventFragment;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return numOfPages;
    }
}

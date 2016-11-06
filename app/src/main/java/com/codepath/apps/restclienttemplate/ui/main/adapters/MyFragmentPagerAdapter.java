package com.codepath.apps.restclienttemplate.ui.main.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.ui.main.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.utils.DataHolder;
import com.codepath.apps.restclienttemplate.utils.FragmentFactory;

/**
 * Created by hison7463 on 11/4/16.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;
    private String[] titles = {"Home", "Mentions"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : {;
                Fragment fragment = FragmentFactory.getFragment(Constants.HOME_TIMELINE_FRAGMENT);
                DataHolder.getInstance().put(Constants.HOME_TIMELINE_FRAGMENT, fragment);
                return fragment;
            }
            case 1 : {
                return FragmentFactory.getFragment(Constants.MENTION_TIMELINE_FRAGMENT);
            }
        }
        return FragmentFactory.getFragment(Constants.HOME_TIMELINE_FRAGMENT);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

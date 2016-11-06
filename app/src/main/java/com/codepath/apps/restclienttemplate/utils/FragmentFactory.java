package com.codepath.apps.restclienttemplate.utils;

import android.support.v4.app.Fragment;

import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.ui.main.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.ui.main.fragments.MentionTimelineFragment;

/**
 * Created by hison7463 on 11/4/16.
 */

public class FragmentFactory {

    public static Fragment getFragment(String name) {
        switch (name) {
            case Constants.HOME_TIMELINE_FRAGMENT: {
                return new HomeTimelineFragment();
            }
            case Constants.MENTION_TIMELINE_FRAGMENT: {
                return new MentionTimelineFragment();
            }
            default: {
                return new HomeTimelineFragment();
            }
        }
    }
}

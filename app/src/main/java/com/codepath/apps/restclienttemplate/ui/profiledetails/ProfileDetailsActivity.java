package com.codepath.apps.restclienttemplate.ui.profiledetails;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;

import butterknife.BindView;

/**
 * Created by hison7463 on 11/5/16.
 */

public class ProfileDetailsActivity extends AppCompatActivity {

    private static final String TAG = ProfileDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        setSupportActionBar(toolbar);
        Log.d(TAG, "Profile");
        disPlayFragment();
    }

    private void disPlayFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new ProfileDetailsView()).commit();
    }

    private void initToolbar() {
        //TODO
    }
}

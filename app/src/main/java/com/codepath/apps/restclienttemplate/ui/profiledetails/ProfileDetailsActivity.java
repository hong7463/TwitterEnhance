package com.codepath.apps.restclienttemplate.ui.profiledetails;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hison7463 on 11/5/16.
 */

public class ProfileDetailsActivity extends AppCompatActivity {

    private static final String TAG = ProfileDetailsActivity.class.getSimpleName();
    private User currentUser;
    private Profile profile;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        ButterKnife.bind(this);
        currentUser = (User) Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.USER));
        profile = (Profile) Parcels.unwrap(getIntent().getParcelableExtra(Constants.PROFILE));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(currentUser != null) {
            getSupportActionBar().setTitle(currentUser.getName());
        }
        else if(profile != null) {
            getSupportActionBar().setTitle(profile.getName());
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

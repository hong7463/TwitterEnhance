package com.codepath.apps.restclienttemplate.ui.userlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().getBoolean(Constants.REQUEST_FOR_FOLLOWERS)) {
            getSupportActionBar().setTitle("Followers");
        }
        else {
            getSupportActionBar().setTitle("Followings");
        }
        AppUtils.disPlayFragment(this, new UserListView());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

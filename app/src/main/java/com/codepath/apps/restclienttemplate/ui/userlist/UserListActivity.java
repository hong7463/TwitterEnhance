package com.codepath.apps.restclienttemplate.ui.userlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.utils.AppUtils;

import butterknife.BindView;

public class UserListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setSupportActionBar(toolbar);

        AppUtils.disPlayFragment(this, new UserListView());
    }
}

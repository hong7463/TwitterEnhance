package com.codepath.apps.restclienttemplate.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.ui.main.adapters.MainRecyclerAdapter;
import com.codepath.apps.restclienttemplate.ui.main.adapters.MyFragmentPagerAdapter;
import com.codepath.apps.restclienttemplate.ui.profiledetails.ProfileDetailsActivity;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.codepath.apps.restclienttemplate.utils.DataHolder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainTimeline extends AppCompatActivity implements ComposeDialog.ComposeFinishListener{

    private static final String TAG = MainTimeline.class.getSimpleName();

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer)
    NavigationView navigationView;
    private ImageView navImg;
    private TextView name;
    private TextView followers;
    private TextView followings;
    private ActionBarDrawerToggle toggle;
    private RecyclerView navRecycler;

    private List<Tweet> userTweets = new ArrayList<>();
    private MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, userTweets);
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timeline);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        toolbar.setTitle("test");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        findView();
        initUI();
        initTabLayout();
    }

    private void initTabLayout() {
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void findView() {
        navImg = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_profile_img);
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        followers = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_followers);
        followings = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_followings);
        navRecycler = (RecyclerView) navigationView.getHeaderView(0).findViewById(R.id.nav_recycler);
    }

    private void initUI() {
        AppUtils.getClient().getProfile(new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                Log.d(TAG, object.toString());
                profile = Profile.fromJsonObject(object);
                DataHolder.getInstance().put(Constants.PROFILE, profile);
                Glide.with(MainTimeline.this).load(profile.getProfileImg()).into(navImg);
                name.setText(profile.getName());
                followings.setText(profile.getFollowings() + "");
                followers.setText(profile.getFollowers() + "");

//                setupNavRecycler();
            }
        });

        navImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Intent intent = new Intent(MainTimeline.this, ProfileDetailsActivity.class);
                intent.putExtra(Constants.PROFILE, Parcels.wrap(profile));
                startActivity(intent);
                drawerLayout.closeDrawers();
            }
        });
        navRecycler.setLayoutManager(layoutManager);
        navRecycler.setAdapter(adapter);
        navRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreTweets(userTweets.get(userTweets.size() - 1).getId() - 1);
            }
        });
    }

    private void setupNavRecycler() {
        RequestParams params = new RequestParams();
        params.put("user_id", profile.getId());
        AppUtils.getClient().getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                userTweets.addAll(Tweet.fromJsonArray(array));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadMoreTweets(long maxId) {
        RequestParams params = new RequestParams();
        params.put("user_id", profile.getId());
        params.put("max_id", maxId);
        AppUtils.getClient().getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                int start = userTweets.size();
                userTweets.addAll(Tweet.fromJsonArray(array));
                adapter.notifyItemRangeInserted(start, array.length());
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callAfterPost(long sinceId) {

    }
}

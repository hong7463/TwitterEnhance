package com.codepath.apps.restclienttemplate.ui.profiledetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.ui.main.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.ui.main.adapters.MainRecyclerAdapter;
import com.codepath.apps.restclienttemplate.ui.userlist.UserListActivity;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hison7463 on 11/5/16.
 */

public class ProfileDetailsView extends Fragment {

    private static final String TAG = ProfileDetailsView.class.getSimpleName();

    @BindView(R.id.profile_details_img)
    ImageView profileImg;
    @BindView(R.id.profile_details_followers)
    TextView followers;
    @BindView(R.id.profile_details_followings)
    TextView followings;
    @BindView(R.id.profile_details_name)
    TextView name;
    @BindView(R.id.profile_details_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.profile_details_profile_container)
    LinearLayout profileContainer;

    private User currentUser;
    private Profile profile;
    private List<Tweet> tweets = new ArrayList<>();
    private MainRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_details_view, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) Parcels.unwrap(getActivity().getIntent().getExtras().getParcelable(Constants.USER));
        profile = (Profile) Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.PROFILE));
    }

    @Override
    public void onStart() {
        super.onStart();
        initUI();
        if(currentUser != null) {
            callForTweets(currentUser.getId());
        }
        else if(profile != null) {
            callForTweets(profile.getId());
        }
    }

    private void initUI() {
        if(currentUser != null) {
            Glide.with(getActivity()).load(currentUser.getProfile_image_url()).into(profileImg);
            followers.setText(currentUser.getFollowers() + "");
            followings.setText(currentUser.getFollowings() + "");
            name.setText(currentUser.getName());
            adapter = new MainRecyclerAdapter(getActivity(), tweets);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    callForTweets(currentUser.getId(), tweets.get(tweets.size() - 1).getId() - 1);
                }
            });
            Glide.with(getActivity()).load(currentUser.getBackGroundImg()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Bitmap bitmap = Bitmap.createBitmap(resource);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    profileContainer.setBackground(drawable);
                }
            });
        }
        else if(profile != null) {
            Glide.with(getActivity()).load(profile.getProfileImg()).into(profileImg);
            followers.setText(profile.getFollowers() + "");
            followings.setText(profile.getFollowings() + "");
            name.setText(profile.getName());
            adapter = new MainRecyclerAdapter(getActivity(), tweets);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    callForTweets(profile.getId(), tweets.get(tweets.size() - 1).getId() - 1);
                }
            });
            Glide.with(getActivity()).load(profile.getBackGroundImg()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Bitmap bitmap = Bitmap.createBitmap(resource);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    profileContainer.setBackground(drawable);
                }
            });
        }
    }

    private void callForTweets(long userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        AppUtils.getClient().getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                tweets.addAll(Tweet.fromJsonArray(array));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void callForTweets(long userId, long maxId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("max_id", maxId);
        AppUtils.getClient().getUserTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                int start = tweets.size();
                tweets.addAll(Tweet.fromJsonArray(array));
                adapter.notifyItemRangeInserted(start, array.length());
            }
        });
    }

    @OnClick(R.id.profile_details_followers_container)
    public void navToFollowers() {
        Intent intent = new Intent(getActivity(), UserListActivity.class);
        intent.putExtra(Constants.REQUEST_FOR_FOLLOWERS, true);
        if(currentUser != null) {
            intent.putExtra(Constants.USER_ID, currentUser.getId());
        }
        else if(profile != null) {
            intent.putExtra(Constants.USER_ID, profile.getId());
        }
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.profile_details_followings_container)
    public void navToFollowings() {
        Intent intent = new Intent(getActivity(), UserListActivity.class);
        intent.putExtra(Constants.REQUEST_FOR_FOLLOWERS, false);
        if(currentUser != null) {
            intent.putExtra(Constants.USER_ID, currentUser.getId());
        }
        else if(profile != null) {
            intent.putExtra(Constants.USER_ID, profile.getId());
        }
        getActivity().startActivity(intent);
    }
}

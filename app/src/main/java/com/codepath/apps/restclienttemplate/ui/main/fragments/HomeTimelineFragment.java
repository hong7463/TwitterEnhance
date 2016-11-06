package com.codepath.apps.restclienttemplate.ui.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.ui.main.ComposeDialog;
import com.codepath.apps.restclienttemplate.ui.main.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.ui.main.adapters.MainRecyclerAdapter;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.codepath.apps.restclienttemplate.utils.DataHolder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hison7463 on 11/4/16.
 */

public class HomeTimelineFragment extends Fragment implements ComposeDialog.ComposeFinishListener{

    private final static String TAG = HomeTimelineFragment.class.getSimpleName();

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_fab)
    FloatingActionButton fab;
    @BindView(R.id.activity_main_timeline)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private List<Tweet> tweets = new ArrayList<>();
    private MainRecyclerAdapter adapter;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.home_timeline_view, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        initClickListener();
        callTimeline();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeDialog dialog = new ComposeDialog();
                dialog.setSinceId(tweets.get(0).getId());
                dialog.setUrl(((Profile)DataHolder.getInstance().retrieve(Constants.PROFILE)).getProfileImg());
                dialog.setTargetFragment(HomeTimelineFragment.this, 100);
                dialog.show(getFragmentManager(), "compose");
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                callAfterPost(tweets.get(0).getId());
                callTimeline();
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initiating recycler");
        adapter = new MainRecyclerAdapter(getActivity(), tweets);
        adapter.setFragmentManager(getFragmentManager());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                callTimeline(tweets.get(tweets.size() - 1).getId() - 1);
            }
        });
    }

    private void callTimeline(long maxId) {
        RequestParams params = new RequestParams();
        params.put("max_id", maxId);
        AppUtils.getClient().getHomeTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                int start = tweets.size();
                tweets.addAll(Tweet.fromJsonArray(array));
                adapter.notifyItemRangeInserted(start, array.length());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                if(object != null) {
                    Log.d(TAG, object.toString());
                }
                else {
                    Log.d(TAG, "fail");
                }
            }
        });
    }

    @Override
    public void callAfterPost(long since_id) {
        if(!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        RequestParams params = new RequestParams();
        params.put("since_id", since_id);
        AppUtils.getClient().getHomeTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                tweets.addAll(0, Tweet.fromJsonArray(array));
                DataHolder.getInstance().put(Constants.SINCE_ID, tweets.get(0).getId());
                adapter.notifyItemRangeInserted(0, array.length());
                recyclerView.scrollToPosition(0);
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void callTimeline() {
        progressBar.setVisibility(View.VISIBLE);
        AppUtils.getClient().getHomeTimeline(0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, json.toString());
                tweets.clear();
                tweets.addAll(Tweet.fromJsonArray(json));
                DataHolder.getInstance().put(Constants.SINCE_ID, tweets.get(0).getId());
                adapter.notifyDataSetChanged();
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public MainRecyclerAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}

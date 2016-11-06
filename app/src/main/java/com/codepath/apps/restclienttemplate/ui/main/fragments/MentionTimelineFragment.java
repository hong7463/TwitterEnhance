package com.codepath.apps.restclienttemplate.ui.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.ui.main.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.ui.main.adapters.MentionRecyclerAdapter;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
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

public class MentionTimelineFragment extends Fragment {

    private static final String TAG = MentionTimelineFragment.class.getSimpleName();

    @BindView(R.id.mention_timeline_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Tweet> mentions = new ArrayList<>();
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private MentionRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.mention_timeline_view, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
    }

    private void initRecyclerView() {
        adapter = new MentionRecyclerAdapter(mentions, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMoreMentions(mentions.get(mentions.size() - 1).getId() - 1);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMentions(mentions.get(0).getId());
            }
        });
    }

    private void getMoreMentions(long maxId) {
        RequestParams params = new RequestParams();
        params.put("max_id", maxId);
        params.put("count", 20);
        AppUtils.getClient().getMentionTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                int start = mentions.size();
                mentions.addAll(Tweet.fromJsonArray(array));
                adapter.notifyItemRangeInserted(start, mentions.size());
            }
        });
    }

    private void refreshMentions(long sinceId) {
        RequestParams params = new RequestParams();
        params.put("since_id", sinceId);
        AppUtils.getClient().getMentionTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                mentions.addAll(0, Tweet.fromJsonArray(array));
                adapter.notifyItemRangeInserted(0, array.length());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initDate() {
        RequestParams params = new RequestParams();
        params.put("count", 20);
        AppUtils.getClient().getMentionTimeline(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                Log.d(TAG, array.toString());
                Log.d(TAG, array.length() + "");
                mentions.addAll(Tweet.fromJsonArray(array));
                adapter.notifyDataSetChanged();
            }
        });
    }
}

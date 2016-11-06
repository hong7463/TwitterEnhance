package com.codepath.apps.restclienttemplate.ui.userlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Followers;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.ui.main.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.ui.userlist.apapter.UserListRecyclerAdapter;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hison7463 on 11/5/16.
 */

public class UserListView extends Fragment {

    private static final String TAG = UserListView.class.getSimpleName();

    @BindView(R.id.user_list_recycler)
    RecyclerView recyclerView;

    private List<User> users = new ArrayList<>();
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private UserListRecyclerAdapter adapter;
    private Followers followers;
    private boolean requestForFollowers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_list_view, container, false);
        ButterKnife.bind(this, view);
        initUI();
        requestForFollowers = getActivity().getIntent().getExtras().getBoolean(Constants.REQUEST_FOR_FOLLOWERS);
        if(requestForFollowers) {
            callForUserList(getActivity().getIntent().getExtras().getLong(Constants.USER_ID));
        }
        else {
            callForFollowingsList(getActivity().getIntent().getExtras().getLong(Constants.USER_ID));
        }
        return view;
    }

    private void initUI() {
        adapter = new UserListRecyclerAdapter(getActivity(), users);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //TODO
                if (requestForFollowers) {
                    callForUserList(getActivity().getIntent().getExtras().getLong(Constants.USER_ID), followers.getNextCursor());
                }
                else {
                    callForFollowingsList(getActivity().getIntent().getExtras().getLong(Constants.USER_ID), followers.getNextCursor());
                }
            }
        });
    }

    private void callForUserList(final long userId, long nextCursor) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("cursor", nextCursor);
        AppUtils.getClient().getFollowers(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int start = users.size();
                    users.addAll(User.fromJsonArray(response.getJSONArray("users")));
                    adapter.notifyItemRangeInserted(start, users.size() - start);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callForUserList(long userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        AppUtils.getClient().getFollowers(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                followers = Followers.fromJsonObject(response);
                users.addAll(followers.getUsers());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void callForFollowingsList(final long userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        AppUtils.getClient().getFollowings(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                followers = Followers.fromJsonObject(object);
                users.addAll(followers.getUsers());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void callForFollowingsList(final long userId, long nextCursor) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("cursor", nextCursor);
        AppUtils.getClient().getFollowings(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                followers = Followers.fromJsonObject(object);
                int start = users.size();
                users.addAll(followers.getUsers());
                adapter.notifyItemRangeInserted(start, users.size() - start);
            }
        });
    }
}

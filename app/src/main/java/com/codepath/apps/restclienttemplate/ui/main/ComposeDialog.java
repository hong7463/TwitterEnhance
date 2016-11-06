package com.codepath.apps.restclienttemplate.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.ui.main.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.codepath.apps.restclienttemplate.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hison7463 on 10/30/16.
 */

public class ComposeDialog extends DialogFragment {

    private static final String TAG = ComposeDialog.class.getSimpleName();
    private long sinceId;
    private String url;
    private String screenName;

    @BindView(R.id.compose_close)
    ImageView close;
    @BindView(R.id.compose_name)
    TextView name;
    @BindView(R.id.compose_user_name)
    TextView user_name;
    @BindView(R.id.compose_profile)
    ImageView profile;
    @BindView(R.id.compose_status)
    EditText status;
    @BindView(R.id.compose_number)
    TextView number;
    @BindView(R.id.compose_btn)
    Button tweet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_dialog, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = 140 - s.length();
                number.setText(String.valueOf(num));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Glide.with(getActivity()).load(url).into(profile);
        if (screenName != null) {
            status.setText(screenName + " ");
            number.setText((140 - status.getText().toString().length()) + "");
            status.setSelection(status.getText().toString().length());
        }
    }

    @OnClick(R.id.compose_btn)
    public void sendTweet() {
        if(getTag().equals("reply")) {
            RequestParams params = new RequestParams();
            params.put("status", status.getText().toString());
            params.put("in_reply_to_status_id", status.getId());
            AppUtils.getClient().postReply(params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, response.toString());
                    ((HomeTimelineFragment) getTargetFragment()).callAfterPost(sinceId);
                    dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    Log.d(TAG, object.toString());
                }
            });
        }
        else if(getTag().equals("compose")) {
            AppUtils.getClient().postTweet(status.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, statusCode + "");
                    ((HomeTimelineFragment) getTargetFragment()).callAfterPost(sinceId);
                    dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, errorResponse.toString());
                }
            });
        }
    }

    public interface ComposeFinishListener {
        public void callAfterPost(long sinceId);
    }

    @OnClick(R.id.compose_close)
    public void close() {
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public long getSinceId() {
        return sinceId;
    }

    public void setSinceId(long sinceId) {
        this.sinceId = sinceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}

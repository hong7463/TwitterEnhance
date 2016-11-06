package com.codepath.apps.restclienttemplate.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.Entity;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Profile;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.ui.main.ComposeDialog;
import com.codepath.apps.restclienttemplate.ui.profiledetails.ProfileDetailsActivity;
import com.codepath.apps.restclienttemplate.ui.widgets.RoundCornorTransform;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.codepath.apps.restclienttemplate.utils.DataHolder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hison7463 on 10/30/16.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private final static String TAG = MainRecyclerAdapter.class.getSimpleName();
    private Context context;
    private List<Tweet> list;
    private FragmentManager fragmentManager;

    public MainRecyclerAdapter(Context context, List<Tweet> list) {
        this.context = context;
        this.list = list;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        MainViewHolder viewHolder = new MainViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final Tweet tweet = list.get(position);
        final User user = tweet.getUser();
        Entity entity = tweet.getEntity();
        Media media = null;
        if(entity != null && entity.getMedias() != null && entity.getMedias().size() > 0) {
            media = entity.getMedias().get(0);
        }
        Glide.with(context).load(user.getProfile_image_url()).centerCrop().into(holder.profileImg);
        holder.name.setText(user.getName());
        holder.atName.setText("@" + user.getAtName());
        holder.text.setText(Html.fromHtml(tweet.getText()));
        holder.date.setText(AppUtils.getRelativeTimeAgo(tweet.getCreated()));
        if(media != null && media.getMediaUrl() != null) {
            holder.thumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(media.getMediaUrl()).centerCrop().into(holder.thumb);
        }
        else {
            holder.thumb.setVisibility(View.GONE);
        }
        Log.d(TAG, tweet.isFavorite() + "");
        if(tweet.isFavorite()) {
            holder.like.setImageResource(R.drawable.heart_red);
        }
        else {
            holder.like.setImageResource(R.drawable.heart);
        }
        holder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileDetailsActivity.class);
                intent.putExtra(Constants.USER, Parcels.wrap(user));
                Log.d(TAG, user.toString());
                context.startActivity(intent);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click");
//                Intent intent = new Intent(context, DetailsActivity.class);
//                intent.putExtra("tweet", Parcels.wrap(tweet));
//                context.startActivity(intent);
            }
        });
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeDialog dialog = new ComposeDialog();
                dialog.setSinceId((Long) DataHolder.getInstance().retrieve(Constants.SINCE_ID));
                dialog.setUrl(((Profile)DataHolder.getInstance().retrieve(Constants.PROFILE)).getProfileImg());
                dialog.setScreenName("@" + tweet.getUser().getAtName());
                dialog.setTargetFragment((Fragment) DataHolder.getInstance().retrieve(Constants.HOME_TIMELINE_FRAGMENT), 999);
                dialog.show(fragmentManager, "reply");
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.isFavorite()) {
                    Log.d(TAG, tweet.getId() + "");
                    holder.like.setImageResource(R.drawable.heart_red);
                    RequestParams params = new RequestParams();
                    params.put("id", tweet.getId());
                    AppUtils.getClient().postLike(params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "added to favourites", Toast.LENGTH_SHORT).show();
                            tweet.setFavorite(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d(TAG, errorResponse.toString());
                        }
                    });
                }
                else {
                    Log.d(TAG, tweet.getId() + "");
                    holder.like.setImageResource(R.drawable.heart);
                    RequestParams params = new RequestParams();
                    params.put("id", tweet.getId());
                    AppUtils.getClient().destroyLike(params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "removed from favourites", Toast.LENGTH_SHORT).show();
                            tweet.setFavorite(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d(TAG, errorResponse.toString());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_view)
        View view;
        @BindView(R.id.list_item_img)
        ImageView profileImg;
        @BindView(R.id.list_item_name)
        TextView name;
        @BindView(R.id.list_item_text)
        TextView text;
        @BindView(R.id.list_item_date)
        TextView date;
        @BindView(R.id.list_item_at_name)
        TextView atName;
        @BindView(R.id.list_item_thumb)
        ImageView thumb;
        @BindView(R.id.list_item_reply)
        ImageView reply;
        @BindView(R.id.list_item_retweet)
        ImageView retweet;
        @BindView(R.id.list_item_like)
        ImageView like;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

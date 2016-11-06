package com.codepath.apps.restclienttemplate.ui.main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hison7463 on 11/4/16.
 */

public class MentionRecyclerAdapter extends RecyclerView.Adapter<MentionRecyclerAdapter.MentionViewHolder> {

    private List<Tweet> mentions;
    private Context context;

    public MentionRecyclerAdapter(List<Tweet> mentions, Context context) {
        this.mentions = mentions;
        this.context = context;
    }

    @Override
    public MentionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mention_list_item, parent, false);
        MentionViewHolder viewHolder = new MentionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MentionViewHolder holder, int position) {
        Tweet tweet = mentions.get(position);
        Glide.with(context).load(tweet.getUser().getProfile_image_url()).into(holder.userImg);
        holder.screenName.setText("@" + tweet.getUser().getAtName());
        holder.date.setText(AppUtils.getRelativeTimeAgo(tweet.getCreated()));
        holder.text.setText(tweet.getText());
    }

    @Override
    public int getItemCount() {
        return mentions.size();
    }

    public class MentionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mention_user_img)
        ImageView userImg;
        @BindView(R.id.mention_screen_name)
        TextView screenName;
        @BindView(R.id.mention_date)
        TextView date;
        @BindView(R.id.mention_text)
        TextView text;

        public MentionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

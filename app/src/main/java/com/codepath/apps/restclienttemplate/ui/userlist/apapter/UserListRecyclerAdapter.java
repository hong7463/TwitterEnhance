package com.codepath.apps.restclienttemplate.ui.userlist.apapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.common.Constants;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.ui.profiledetails.ProfileDetailsActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hison7463 on 11/5/16.
 */

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.UserListViewHolder> {

    private static final String TAG = UserListRecyclerAdapter.class.getSimpleName();

    private Context context;
    private List<User> users;

    public UserListRecyclerAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        UserListViewHolder viewHolder = new UserListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        final User user = users.get(position);
        Log.d(TAG, (context == null) + "");
        Glide.with(context).load(user.getProfile_image_url()).into(holder.profileImg);
        holder.name.setText(user.getName());
        holder.screenName.setText("@" + user.getAtName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileDetailsActivity.class);
                intent.putExtra(Constants.USER, Parcels.wrap(user));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.user_list_profile_img)
        ImageView profileImg;
        @BindView(R.id.user_list_name)
        TextView name;
        @BindView(R.id.user_list_screen_name)
        TextView screenName;
        @BindView(R.id.user_list_layout)
        View view;

        public UserListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}

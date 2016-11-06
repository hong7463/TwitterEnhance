package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hison7463 on 10/30/16.
 */
@Parcel
public class User {

    String name;
    String profile_image_url;
    String atName;
    long id;
    long followers;
    long followings;
    String backGroundImg;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getAtName() {
        return atName;
    }

    public void setAtName(String atName) {
        this.atName = atName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowings() {
        return followings;
    }

    public void setFollowings(long followings) {
        this.followings = followings;
    }

    public String getBackGroundImg() {
        return backGroundImg;
    }

    public void setBackGroundImg(String backGroundImg) {
        this.backGroundImg = backGroundImg;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", atName='" + atName + '\'' +
                ", id=" + id +
                ", followers=" + followers +
                ", followings=" + followings +
                ", backGroundImg='" + backGroundImg + '\'' +
                '}';
    }

    public static User fromJsonObject(JSONObject object) {
        User user = new User();
        try {
            user.setName(object.getString("name"));
            user.setProfile_image_url(object.getString("profile_image_url"));
            user.setAtName(object.getString("screen_name"));
            user.setId(object.getLong("id"));
            user.setFollowers(object.getLong("followers_count"));
            user.setFollowings(object.getLong("friends_count"));
            user.setBackGroundImg(object.getString("profile_background_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<User> fromJsonArray(JSONArray array) {
        List<User> users = new ArrayList<>(array.length());
        for(int i = 0; i < array.length(); i++) {
            try {
                users.add(User.fromJsonObject(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }
}

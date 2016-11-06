package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by hison7463 on 11/5/16.
 */
@Parcel
public class Profile {

    long followers;
    long followings;
    long id;
    String profileImg;
    String name;
    String backGroundImg;

    public Profile() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackGroundImg() {
        return backGroundImg;
    }

    public void setBackGroundImg(String backGroundImg) {
        this.backGroundImg = backGroundImg;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "followers=" + followers +
                ", followings=" + followings +
                ", id=" + id +
                ", profileImg='" + profileImg + '\'' +
                ", name='" + name + '\'' +
                ", backGroundImg='" + backGroundImg + '\'' +
                '}';
    }

    public static Profile fromJsonObject(JSONObject object) {
        Profile res = new Profile();
        try {
            res.setId(object.getLong("id"));
            res.setFollowers(object.getLong("followers_count"));
            res.setFollowings(object.getLong("friends_count"));
            res.setProfileImg(object.getString("profile_image_url"));
            res.setName(object.getString("name"));
            res.setBackGroundImg(object.getString("profile_background_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }
}

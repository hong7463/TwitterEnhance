package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hison7463 on 11/4/16.
 */
@Parcel
public class UserMention {

    String name;
    long id;
    String screenName;

    public UserMention() {
    }

    public UserMention(String name, long id, String screenName) {
        this.name = name;
        this.id = id;
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreen_name(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public String toString() {
        return "UserMention{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", screen_name='" + screenName + '\'' +
                '}';
    }

    public static UserMention fromJsonObject(JSONObject object) {

        UserMention mention = new UserMention();
        try {
            mention.setId(object.getLong("id"));
            mention.setName(object.getString("name"));
            mention.setScreen_name(object.getString("screen_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mention;
    }

    public static List<UserMention> fromJsonArray(JSONArray array) {
        List<UserMention> mentions = new ArrayList<>(array.length());
        for(int i = 0; i < array.length(); i++) {
            try {
                mentions.add(UserMention.fromJsonObject(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return mentions;
    }
}

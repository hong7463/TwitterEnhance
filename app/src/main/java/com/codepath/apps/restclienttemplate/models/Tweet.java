package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

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
public class Tweet {

    long id;
    String text;
    User user;
    String created;
    Entity entity;
    ExtendedEntity extendedEntity;
    boolean favorite;

    public Tweet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Entity getEntity() {
        return entity;
    }

    public ExtendedEntity getExtendedEntity() {
        return extendedEntity;
    }

    public void setExtendedEntity(ExtendedEntity extendedEntity) {
        this.extendedEntity = extendedEntity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", created='" + created + '\'' +
                ", entity=" + entity +
                ", extendedEntity=" + extendedEntity +
                ", favorite=" + favorite +
                '}';
    }

    public static Tweet fromJsonObject(JSONObject object) {
        Tweet tweet = new Tweet();
        try {
            tweet.setId(object.getLong("id"));
            tweet.setText(object.getString("text"));
            if(!object.isNull("favorited")) {
                tweet.setFavorite(object.getBoolean("favorited"));
            }
            tweet.setCreated(object.getString("created_at"));
            tweet.setUser(User.fromJsonObject(object.getJSONObject("user")));
            tweet.setEntity(Entity.fromJsonObject(object.getJSONObject("entities")));
            if(!object.isNull("extended_entities")) {
                tweet.setExtendedEntity(ExtendedEntity.fromJsonObject(object.getJSONObject("extended_entities")));
            }
        } catch (JSONException e) {
            return tweet;
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray array) {
        List<Tweet> res = new ArrayList<Tweet>(array.length());
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                res.add(Tweet.fromJsonObject(object));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return res;
    }
}

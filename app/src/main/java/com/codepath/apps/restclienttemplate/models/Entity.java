package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hison7463 on 10/31/16.
 */
@Parcel
public class Entity {

    List<Media> medias;
    List<UserMention> userMentions;

    public Entity() {
    }

    public Entity(List<Media> medias, List<UserMention> userMentions) {
        this.medias = medias;
        this.userMentions = userMentions;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }


    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "medias=" + medias +
                ", userMentions=" + userMentions +
                '}';
    }

    public static Entity fromJsonObject(JSONObject object) {
        Entity res = new Entity();
        try {
            res.setMedias(Media.fromJsonArray(object.getJSONArray("media")));
            res.setUserMentions(UserMention.fromJsonArray(object.getJSONArray("user_mentions")));
        } catch (JSONException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }
}

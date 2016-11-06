package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hison7463 on 11/5/16.
 */

public class Followers {

    long nextCursor;
    long previousCursor;
    List<User> users;

    public Followers() {
    }

    public long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public long getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(long previousCursor) {
        this.previousCursor = previousCursor;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Followers{" +
                "nextCursor=" + nextCursor +
                ", previousCursor=" + previousCursor +
                ", users=" + users +
                '}';
    }

    public static Followers fromJsonObject(JSONObject object) {
        Followers followers = new Followers();
        try {
            followers.setUsers(User.fromJsonArray(object.getJSONArray("users")));
            followers.setNextCursor(object.getLong("next_cursor"));
            followers.setPreviousCursor(object.getLong("previous_cursor"));
        } catch (JSONException e) {
            e.printStackTrace();
            return followers;
        }
        return followers;
    }
}

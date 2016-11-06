package com.codepath.apps.restclienttemplate.utils;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "SCsamLmoi6wMQSDrDNEgQKy2i";       // Change this
	public static final String REST_CONSUMER_SECRET = "l9CK9lBbMVwBQGbK4g5W8Wr94ZyMGo1qE7WlBuWUsIkMKSLABM"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://haisenhong"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	public void getTweetDetail(long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/show.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().get(apiUrl, params, handler);
	}

	public void getHomeTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getProfile(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getFollowers(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		getClient().get(apiUrl, params, handler);
	}

	public void getFollowings(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		getClient().get(apiUrl, params, handler);
	}

	public void postReply(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		getClient().post(apiUrl, params, handler);
	}

	public void postLike(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/create.json");
		getClient().post(apiUrl, params, handler);
	}

	public void destroyLike(RequestParams params, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		getClient().post(apiUrl, params, handler);
	}
}

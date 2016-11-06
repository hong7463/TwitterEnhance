package com.codepath.apps.restclienttemplate.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.ui.main.MainTimeline;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.utils.AppUtils;
import com.codepath.apps.restclienttemplate.utils.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(!AppUtils.checkNetwork()) {
			Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
		}
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Log In");
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		// Intent i = new Intent(this, PhotosActivity.class);
		// startActivity(i);
		final Intent intent = new Intent(this, MainTimeline.class);
		startActivity(intent);
		finish();
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		if(!AppUtils.checkNetwork()) {
			Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
			return;
		}
		getClient().connect();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}

package com.example.vitalii.test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vitalii.test.R;
import com.example.vitalii.test.util.CircleTransform;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ScreenOneActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String EMAIL = "email";
    private static final String USER_NAME = "user_posts";
    private CallbackManager callbackManager;
    private SharedPreferences preferences;
    private TextView tvName, tvEmail;
    private ImageView ivUserPic;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.login));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        tvName = header.findViewById(R.id.tvName);
        tvEmail = header.findViewById(R.id.tvEmail);
        ivUserPic = header.findViewById(R.id.ivUserPic);

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, USER_NAME));
        callbackManager = CallbackManager.Factory.create();

        callbackManager = CallbackManager.Factory.create();

        checkIsDataGot();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    isDataEmpty();
                }
            }
        };
        accessTokenTracker.startTracking();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkIsDataGot() {
        if (AccessToken.getCurrentAccessToken() == null) {
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            getDataFromFB();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e("onError", exception.getMessage());
                        }
                    });
        } else {
            preferences = getPreferences(Context.MODE_PRIVATE);
            String name = preferences.getString(NAME, "");
            String email = preferences.getString(EMAIL, "");
            String url = preferences.getString(URL, "");

            if (name.equals("") || email.equals("") || url.equals("")) {
                isDataEmpty();
            } else {
                fillData(name, email, url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        accessTokenTracker.stopTracking();
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fillData(String name, String email, String url) {
        tvName.setText(name);
        tvEmail.setText(email);
        Picasso.get().load(url).transform(new CircleTransform()).
                resize(200, 200).into(ivUserPic);
    }

    private void getDataFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                (object, response) -> {

                    try {
                        String name = object.getString(NAME);
                        String email = object.getString(EMAIL);
                        JSONObject picture = object.getJSONObject("picture");
                        JSONObject data = picture.getJSONObject("data");
                        String url = data.getString(URL);

                        fillData(name, email, url);

                        preferences = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = preferences.edit();
                        ed.putString(NAME, name);
                        ed.putString(EMAIL, email);
                        ed.putString(URL, url);
                        ed.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        parameters.putBoolean("redirect", false);
        parameters.putString("height", "200");
        parameters.putString("type", "normal");
        parameters.putString("width", "200");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void isDataEmpty() {
        preferences = getPreferences(Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        tvName.setText(getString(R.string.guest));
        tvEmail.setText(getString(R.string.guest_com));
        ivUserPic.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

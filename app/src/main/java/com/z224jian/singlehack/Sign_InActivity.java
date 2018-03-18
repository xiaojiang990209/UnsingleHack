package com.z224jian.singlehack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yawen on 2018-03-17.
 */

public class Sign_InActivity extends BaseActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView info;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.sign_in);


        info = (TextView)findViewById(R.id.info);
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "public_profile", "user_birthday");

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();

                Toast.makeText(getApplicationContext(), "Loggin in...", Toast.LENGTH_SHORT).show();

                info.setText("Login succeed.");
                String userid = loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.d("tttttt", object.getString("id"));

                                    String fnm = object.getString("first_name");
                                    String lnm = object.getString("last_name");
                                    String gender = object.getString("gender");
                                    String id = object.getString("id");
                                    String birthday = object.getString("birthday");
                                    JSONObject jsonObject = object.getJSONObject("location");
                                    String location = jsonObject.getString("name");

                                    nextActivity(profile, fnm, lnm, gender, id, birthday, location);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        displayUserInfo(object);
//                    }
//                });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "first_name, last_name, id");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }
        };
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile, String fnm, String lnm, String gender, String id, String birthday, String location) {
        if(fnm != null && lnm != null && gender != null && id != null && birthday != null && location != null) {
            Intent main = new Intent(Sign_InActivity.this, ProfileActivity.class);
            main.putExtra("first_name", fnm);
            main.putExtra("last_name", lnm);
            main.putExtra("gender", gender);
            main.putExtra("birthday", birthday);
            main.putExtra("location", location);
            main.putExtra("id", id);
            main.putExtra("imageUrl", profile.getProfilePictureUri(200,200). toString());

            startActivity(main);
        }
    }

//    public void displayUserInfo(JSONObject object)
//    {
//        String first_name, last_name, id;
//        first_name = "";
//        last_name = "";
//        id = "";
//        try {
//            first_name = object.getString("first_name");
//            last_name = object.getString("last_name");
//            id = object.getString("id");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        TextView tv_name, tv_id;
//        tv_name = (TextView)findViewById(R.id.TV_name);
//        tv_id = (TextView)findViewById(R.id.TV_id);
//
//        tv_name.setText(first_name + " " + last_name);
//        tv_id.setText("ID:"+id);
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

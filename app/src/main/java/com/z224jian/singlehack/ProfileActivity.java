package com.z224jian.singlehack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;

/**
 * Created by yawen on 2018-03-17.
 */

public class ProfileActivity extends BaseActivity{

    private ShareDialog shareDialog;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shareDialog = new ShareDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });

        Bundle inBundle = getIntent().getExtras();
        String first_name = inBundle.get("first_name").toString();
        String last_ame = inBundle.get("last_ame").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        TextView nameView = (TextView) findViewById(R.id.first_nameAndLast_name);
        nameView.setText("" + first_name + " " + last_ame);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LoginManager.getInstance().logOut();
                Intent login = new Intent(ProfileActivity.this, Sign_InActivity.class);
                startActivity(login);
                finish();
            }
        });
        new ProfileActivity.DownloadImage((ImageButton) findViewById(R.id.imageButton)).execute(imageUrl);
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;

        public DownloadImage(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }
}

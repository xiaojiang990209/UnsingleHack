package com.androidapp.lichiheng.studyparis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidapp.lichiheng.studyparis.Models.Gender;
import com.androidapp.lichiheng.studyparis.Models.Post;
import com.androidapp.lichiheng.studyparis.Models.TimePeriod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lichi on 2018/3/16.
 */

public class UploadActivity extends AppCompatActivity{
    private DatabaseReference mDatabase;
    private FirebaseUser currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("lichiheng1998@gmail.com", "wobuzhidao").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("tag", "signInWithEmail:onComplete:" + task.isSuccessful());
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w("tag", "signInWithEmail:failed", task.getException());
                }
            }
        });
        currUser = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Post newPost = new Post(currUser.getUid(), "Csca", "1234",
                                Gender.MALE, "03-06-2018 20:01", "03-06-2018 22:01");
        writePost(newPost);
    }
    protected void writePost(Post post){
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", currUser.getEmail());
        userInfo.put("name", currUser.getDisplayName());
        mDatabase.child("Location").child(post.getLocation()).child(currUser.getUid()).setValue(userInfo);
        mDatabase.child("Course").child(post.getCourseCode()).child(currUser.getUid()).setValue(userInfo);
        mDatabase.child("Gender").child(post.getGender()).child(currUser.getUid()).setValue(userInfo);
        mDatabase.child("Post").push().setValue(post.toMap());
    }
}

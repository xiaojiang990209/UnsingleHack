package com.z224jian.singlehack;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.z224jian.singlehack.Model.Post;
import com.google.firebase.database.Query;

/**
 * Created by orion on 2018-03-17.
 */

import java.util.*;

public class Matcher {
    private Post postInfo;
    private DatabaseReference mDatabase;
    private SortedMap<String, Integer> matchedPost;

    Matcher (Post new_post, DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
        matchedPost = new TreeMap<>();
        this.postInfo = new_post;
    }

    String doMatch(boolean withLocation, boolean withCourseCode, boolean withGender)
    {
        ArrayList<Query> queryList = new ArrayList<>();
        // Operate every checked filter
        if (withLocation) {
            queryList.add(mDatabase.child("Location").orderByChild(postInfo.getLocation()));
        }
        if (withCourseCode) {
            queryList.add(mDatabase.child("Course").orderByChild(postInfo.getCourseCode()));
        }
        if (withGender) {
            queryList.add(mDatabase.child("Gender").orderByChild(postInfo.getGender()));
        }
        // Add ValueEventListener to query
        for (Query query: queryList){
            query.addEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String pid = snapshot.getKey();
                        if (matchedPost.containsKey(pid)) {
                            matchedPost.put(pid,matchedPost.get(pid) + 1);
                        } else {
                            matchedPost.put(pid, 0);
                        }
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (matchedPost.isEmpty()) {
            return "None";
        } else {
            return matchedPost.lastKey();
        }
    }

}
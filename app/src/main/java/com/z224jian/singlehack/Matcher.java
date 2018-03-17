package com.z224jian.singlehack;

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
    private Query query;

    Matcher (Post new_post, DatabaseReference mDatabase) {
        this.postInfo = new_post;
        this.mDatabase = mDatabase;
        matchedPost = new TreeMap<>();

        // Add ValueEventListener to query
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String pid = snapshot.child("pid").getValue(String.class);
                    if (matchedPost.containsKey(pid)) {
                        matchedPost.put (pid, matchedPost.get(pid) + 1);
                    } else {
                        matchedPost.put (pid, 0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // No implementation for now
            }
        });
    }


    String doMatch(boolean withLocation, boolean withCourseCode, boolean withTime
            , boolean withGender)
    {
        // Operate every checked filter
        if (withLocation) {
            query = mDatabase.child("Location").equalTo(postInfo.getLocation());
        }
        if (withCourseCode) {
            query = mDatabase.child("Course").equalTo(postInfo.getCourseCode());
        }
        if (withGender) {
            query = mDatabase.child("Gender").equalTo(postInfo.getGender());
        }

        if (matchedPost.isEmpty()) {
            return null;
        } else {
            return matchedPost.lastKey();
        }
    }

}
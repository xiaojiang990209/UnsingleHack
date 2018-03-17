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
    Post postInfo;
    private DatabaseReference mDatabase;
    SortedMap<Post, Integer> matchedPost;
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
                    Post item = snapshot.getValue(Post.class);
                    if (matchedPost.containsKey(item)) {
                        matchedPost.put (item, matchedPost.get(item) + 1);
                    } else {
                        matchedPost.put (item, 0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // No implementation for now
            }
        });
    }


    Post doMatch(boolean withLocation, boolean withCourseCode, boolean withTime
            , boolean withGender)
    {
        // Operate every checked filter
        if (withLocation) {
            query = mDatabase.child("Location").equalTo(postInfo.location);
        }
        if (withCourseCode) {
            query = mDatabase.child("Location").equalTo(postInfo.courseCode);
        }
        if (withGender) {
            query = mDatabase.child("Location").equalTo(postInfo.genderPreference);
        }

        if (matchedPost.isEmpty()) {
            return null;
        } else {
            return matchedPost.lastKey();
        }
    }

}

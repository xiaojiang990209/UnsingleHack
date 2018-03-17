package com.z224jian.singlehack;

import android.util.Log;

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
            queryList.add(mDatabase.orderByChild("Location").equalTo(postInfo.getLocation()));
        }
        if (withCourseCode) {
            queryList.add(mDatabase.orderByChild("Course").equalTo(postInfo.getCourseCode()));
        }
        if (withGender) {
            queryList.add(mDatabase.orderByChild("Gender").equalTo(postInfo.getGender()));
        }
        // Add ValueEventListener to query
        for (Query query: queryList){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String pid = snapshot.getKey();
                        Log.i("key", pid);
                        if (matchedPost.containsKey(pid)) {
                            matchedPost.put(pid,matchedPost.get(pid) + 1);
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
        if (matchedPost.isEmpty()) {
            return "None";
        } else {
            return matchedPost.lastKey();
        }
    }

}
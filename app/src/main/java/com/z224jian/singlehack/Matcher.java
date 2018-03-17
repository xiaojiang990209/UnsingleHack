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
<<<<<<< HEAD
    private SortedMap<Post, Integer> matchedPost;
=======
    private SortedMap<String, Integer> matchedPost;
>>>>>>> 259589fdbcbb501e0131d19c2e8de9dee40a6ecd
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
<<<<<<< HEAD
                    Post item = snapshot.getValue(Post.class);
                    if (matchedPost.containsKey(item)) {
                        matchedPost.put (item, matchedPost.get(item) + 1);
                    } else {
                        matchedPost.put (item, 0);
=======
                    String pid = snapshot.child("pid").getValue(String.class);
                    if (matchedPost.containsKey(pid)) {
                        matchedPost.put (pid, matchedPost.get(pid) + 1);
                    } else {
                        matchedPost.put (pid, 0);
>>>>>>> 259589fdbcbb501e0131d19c2e8de9dee40a6ecd
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // No implementation for now
            }
        });
    }


<<<<<<< HEAD
    Post doMatch(boolean withLocation, boolean withCourseCode, boolean withTime
=======
    String doMatch(boolean withLocation, boolean withCourseCode, boolean withTime
>>>>>>> 259589fdbcbb501e0131d19c2e8de9dee40a6ecd
            , boolean withGender)
    {
        // Operate every checked filter
        if (withLocation) {
<<<<<<< HEAD
            query = mDatabase.child("Location").equalTo(postInfo.location);
        }
        if (withCourseCode) {
            query = mDatabase.child("Location").equalTo(postInfo.courseCode);
        }
        if (withGender) {
            query = mDatabase.child("Location").equalTo(postInfo.genderPreference);
=======
            query = mDatabase.child("Location").equalTo(postInfo.getLocation());
        }
        if (withCourseCode) {
            query = mDatabase.child("Course").equalTo(postInfo.getCourseCode());
        }
        if (withGender) {
            query = mDatabase.child("Gender").equalTo(postInfo.getGender());
>>>>>>> 259589fdbcbb501e0131d19c2e8de9dee40a6ecd
        }

        if (matchedPost.isEmpty()) {
            return null;
        } else {
            return matchedPost.lastKey();
        }
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 259589fdbcbb501e0131d19c2e8de9dee40a6ecd

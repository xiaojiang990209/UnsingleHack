package com.z224jian.singlehack;

import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.z224jian.singlehack.models.Post;
import com.google.firebase.database.Query;

/**
 * Created by orion on 2018-03-17.
 */

import java.util.*;

public class Matcher {
    private Post postInfo;
    private DatabaseReference mDatabase;
    private SortedMap<String, Integer> matchedPost;
    private ArrayList<Query> queryList;
    private TextView mMatchedField;
    private String pid;

    Matcher (Post new_post, String pid, DatabaseReference mDatabase, TextView mMatchedField) {
        this.mDatabase = mDatabase;
        matchedPost = new TreeMap<>();
        this.postInfo = new_post;
        this.mMatchedField = mMatchedField;
        this.pid = pid;
    }
    public void createQuerys(boolean withLocation, boolean withCourseCode, boolean withGender) {
        queryList = new ArrayList<>();
        // Operate every checked filter
        if (withLocation) {
            queryList.add(mDatabase.child("Location").child(postInfo.location));
        }
        if (withCourseCode) {
            queryList.add(mDatabase.child("Course").child(postInfo.courseCode));
        }
        if (withGender) {
            queryList.add(mDatabase.child("Gender").child(postInfo.genderPreference));
        }
        // Add ValueEventListener to query
        for (Query query: queryList){
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String postId = snapshot.getKey();
                        if (!postId.equals(pid)) {
                            if (matchedPost.containsKey(postId)) {
                                matchedPost.put(postId,matchedPost.get(postId) + 1);
                            } else {
                                matchedPost.put(postId, 0);
                            }
                        }
                    }
                    mMatchedField.setText(doMatch());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    String doMatch()
    {
        if (matchedPost.isEmpty()) {
            return "None";
        } else {
            String matchedPid = matchedPost.lastKey();
            int count = matchedPost.get(matchedPost.lastKey()) + 1;
            if(count == queryList.size()){
                return matchedPid;
            }
            return "None";
        }
    }

}
package com.z224jian.singlehack;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.z224jian.singlehack.Model.Post;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by z224jian on 16/03/18.
 */

public class NewPostActivity extends BaseActivity
        implements TimePickerFragment.TimePickedListener,
                   DatePickerFragment.DatePickedListener{
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private Spinner mLocationField;
    private EditText mTimeFieldFrom;
    private EditText mTimeFieldTo;
    private EditText mDateField;
    private Spinner mGenderField;
    //... Add course code field here.
    // For now, use an EditText
    private Spinner mCourseField;
    private FloatingActionButton mSubmitButton;
    private boolean isFromField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Initialize FirebaseApp
        FirebaseApp.initializeApp(this);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mLocationField = findViewById(R.id.location_field);
        mTimeFieldFrom = findViewById(R.id.time_field_from);
        mTimeFieldTo = findViewById(R.id.time_field_to);
        mDateField = findViewById(R.id.date_field);
        mCourseField = findViewById(R.id.course_field);
        mGenderField = findViewById(R.id.gender_field);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        isFromField = false;

        // Initialize spinner options
        ArrayAdapter<CharSequence> locations_adapter = ArrayAdapter.createFromResource(this,
                R.array.available_locations, android.R.layout.simple_spinner_item);
        locations_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationField.setAdapter(locations_adapter);

        ArrayAdapter<CharSequence> courses_adapter = ArrayAdapter.createFromResource(this, 
                R.array.available_courses, android.R.layout.simple_spinner_item);
        courses_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseField.setAdapter(courses_adapter);

        ArrayAdapter<CharSequence> genders_adapter = ArrayAdapter.createFromResource(this,
                R.array.available_genders, android.R.layout.simple_spinner_item);
        genders_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderField.setAdapter(genders_adapter);

        // Setup onclick events
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        // Onclick waking up the time picker dialog
        mTimeFieldFrom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isFromField = true;
                showTimePickerDialog(view);
            }
        });
        mTimeFieldTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        mDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
    }

    private void setEditingEnabled(boolean enabled) {
        mLocationField.setEnabled(enabled);
        mTimeFieldFrom.setEnabled(enabled);
        mTimeFieldTo.setEnabled(enabled);
        mCourseField.setEnabled(enabled);
        mGenderField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    private void submitPost() {
        final String location = mLocationField.getSelectedItem() == null ?
                "" : mLocationField.getSelectedItem().toString();
        final String timeFrom = mTimeFieldFrom.getText().toString();
        final String timeTo = mTimeFieldTo.getText().toString();
        final String date = mDateField.getText().toString();
        final String gender = mGenderField.getSelectedItem() == null ?
                "" : mGenderField.getSelectedItem().toString();
        final String course = mCourseField.getSelectedItem() == null ?
                "" : mCourseField.getSelectedItem().toString();

        // Time from is required
        if (TextUtils.isEmpty(timeFrom)) {
            mTimeFieldFrom.setError(REQUIRED);
            return;
        }
        // Time to is required
        if (TextUtils.isEmpty(timeTo)) {
            mTimeFieldTo.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(date)) {
            mDateField.setError(REQUIRED);
            return;
        }
        // User didnt select location
        if (mLocationField.getSelectedItem() == null) {
            Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // User didnt select gender
        if (mGenderField.getSelectedItem() == null) {
            Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // User didnt select course_code 
        if (mCourseField.getSelectedItem() == null) {
            Toast.makeText(this, "Course code is required", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Handle posting process...
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = "testid";
        writeNewPost(userId, location, course, timeFrom, timeTo, date, gender);
        setEditingEnabled(true);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimePicked(String time){
        if (isFromField) {
            mTimeFieldFrom.setText(time);
            isFromField = false;
        } else {
            mTimeFieldTo.setText(time);
        }
    }

    @Override
    public void onDatePicked(String time) {
        mDateField.setText(time);
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String location, String courseCode,
                              String timeFrom, String timeTo, String date, String genderPreference) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Post").push().getKey();
        Post post = new Post(userId, location, courseCode, timeFrom, timeTo, date, genderPreference);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Post/" + key, postValues);
        childUpdates.put("/Location/" + location + "/test/" + key, postValues);
        childUpdates.put("/Gender/" + genderPreference + "/test/" + key, postValues);
        childUpdates.put("/Course/" + courseCode + "/test/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

}

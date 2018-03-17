package com.z224jian.singlehack;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.z224jian.singlehack.models.Post;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by z224jian on 16/03/18.
 */

public class NewPostActivity extends BaseActivity implements TimePickerFragment.TimePickedListener{
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    // Current user
    private FirebaseUser currUser;

    private Spinner mLocationField;
    private EditText mTimeFieldFrom;
    private EditText mTimeFieldTo;
    private Spinner mGenderField;
    //... Add course code field here.
    // For now, use an EditText
    private Spinner mCourseField;
    private FloatingActionButton mSubmitButton;
    // Indicate which time field is open
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

        // Login by current user.
        currUser = login();

        mLocationField = findViewById(R.id.location_field);
        mTimeFieldFrom = findViewById(R.id.time_field_from);
        mTimeFieldTo = findViewById(R.id.time_field_to);
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
    }

    private void submitPost() {
        final String location = mLocationField.getSelectedItem() == null ?
                "" : mLocationField.getSelectedItem().toString();
        final String timeFrom = mTimeFieldFrom.getText().toString();
        final String timeTo = mTimeFieldTo.getText().toString();
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
        // Get a post id
        String pid = mDatabase.child("Post").push().getKey();
        Calendar now = Calendar.getInstance();
        String year = String.valueOf(now.get(Calendar.YEAR));
        String month = String.valueOf(now.get(Calendar.MONTH) +1);
        String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        String fromInString = month + "-" + day + "-" + year + " " + timeFrom;
        String toInString = month + "-" + day + "-" + year + " " + timeFrom;
        Post post = new Post(currUser.getUid(), location, course, gender, fromInString, toInString);
        // Upload to post
        Map<String, String> postMap = post.toMap();
        mDatabase.child("Post").child(pid).setValue(postMap);
        mDatabase.child("Location").child(post.getLocation()).child("pid").setValue(pid);
        mDatabase.child("Course").child(post.getCourseCode()).child("pid").setValue(pid);
        mDatabase.child("Gender").child(post.getGender()).child("pid").setValue(pid);
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
    // Demo login. Will be replaced once the auth module is ready.
    public FirebaseUser login(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("lichiheng1998@gmail.com", "1234567890");
        return auth.getCurrentUser();
    }
}

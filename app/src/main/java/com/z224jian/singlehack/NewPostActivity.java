package com.z224jian.singlehack;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.nio.charset.Charset;

/**
 * Created by z224jian on 16/03/18.
 */

public class NewPostActivity extends BaseActivity {
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private Spinner mLocationField;
    private EditText mTimeFieldFrom;
    private EditText mTimeFieldTo;
    private Spinner mGenderField;
    //... Add course code field here.
    // For now, use an EditText
    private Spinner mCourseField;
    private FloatingActionButton mSubmitButton;

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
        mCourseField = findViewById(R.id.course_field);
        mGenderField = findViewById(R.id.gender_field);
        mSubmitButton = findViewById(R.id.fab_submit_post);

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
    }

    private void setEditingEnabled(boolean enabled) {

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


    }

}

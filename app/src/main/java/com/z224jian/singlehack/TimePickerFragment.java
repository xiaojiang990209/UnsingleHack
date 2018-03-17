package com.z224jian.singlehack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by lichi on 2018/3/16.
 */

public final class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener{

    private TimePickedListener listener;

    public static interface TimePickedListener {
        void onTimePicked(String time);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        listener = (TimePickedListener) getActivity();
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, hour,
                minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String time = String.valueOf(hourOfDay);
        time += ":" + String.valueOf(minute);
        listener.onTimePicked(time);
    }
}

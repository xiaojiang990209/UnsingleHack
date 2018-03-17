package com.z224jian.singlehack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by z224jian on 17/03/18.
 */

public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener{

    private DatePickedListener listener;

    public static interface DatePickedListener {
        void onDatePicked(String time);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        listener = (DatePickedListener) getActivity();
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year,
                month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String time = (String.valueOf(year) + "-");
        time += (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)) + "-";
        time += String.valueOf(day);
        listener.onDatePicked(time);
    }

}

package com.geterdone.android.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.TimeZone;


public class DatePickerFragment extends DialogFragment {
    private Calendar mCalendar = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker if no calendar is provided
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mCalendar.setTimeZone(TimeZone.getDefault());
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (TaskEditorActivity) getActivity(), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }

    void setCalendar(Calendar cal) {
        mCalendar = cal;
    }
}

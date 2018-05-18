package com.example.androiddeveloper.gardenapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

/**
 * Created by Android Developer on 4/10/2018.
 */

public class TimePickerFragment extends DialogFragment {

    TimePickerDialog.OnTimeSetListener onTimeSet;

    public TimePickerFragment() {

    }

    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        onTimeSet = ontime;
    }

    @SuppressLint("NewApi")
    private int hour, minute;

    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, onTimeSet, hour, minute, false);
    }
}
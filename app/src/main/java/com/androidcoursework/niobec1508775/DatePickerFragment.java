package com.androidcoursework.niobec1508775;

/**
 * Created by Cyril on 03/02/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        GregorianCalendar calTime = new GregorianCalendar(year, month, day, 0, 0);
        Date date = calTime.getTime();
        String dateAsString = DateFormat.getDateInstance().format(date);
        ((TextView) (getActivity().findViewById(R.id.textViewDate))).setText("No later than: " + dateAsString);
        SearchActivity callingActivity = (SearchActivity) getActivity();
        callingActivity.onDateMaxSearch(date);
    }
}
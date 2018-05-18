package com.example.androiddeveloper.gardenapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookGarden extends Fragment {
    EditText _txtbookname, _txtbooksname, _txtbooktime, _txtbookdate, _txtbookadtionalinfo, _txtbooknoofperson;
    ImageButton btntime;
    ImageButton btndate;
    Button btnsaveodr;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    int count = 1;
    private ProgressDialog pDialog;

    public BookGarden() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_book_garden, container, false);

        prefs = rootView.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String id = prefs.getString("id", null);
        final String emailText = prefs.getString("email", null);

        _txtbookname = (EditText) rootView.findViewById(R.id.txtbookname);
        _txtbooksname = (EditText) rootView.findViewById(R.id.txtbooksname);
        _txtbooktime = (EditText) rootView.findViewById(R.id.txtbooktime);
        _txtbookdate = (EditText) rootView.findViewById(R.id.txtbookdate);
        _txtbookadtionalinfo = (EditText) rootView.findViewById(R.id.txtbookadtionalinfo);
        _txtbooknoofperson = (EditText) rootView.findViewById(R.id.txtbooknoofperson);
        btnsaveodr = (Button) rootView.findViewById(R.id.btnbook);
        btntime = rootView.findViewById(R.id.btntime);
        btndate = rootView.findViewById(R.id.btndate);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        btnsaveodr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == false) {
                    bookfail();
                    return;
                }
                pDialog = new ProgressDialog(getActivity());
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Creating Account...");
                pDialog.setCancelable(false);
                pDialog.show();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("odrcount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String k = String.valueOf(dataSnapshot.getValue());
                        count = Integer.parseInt(k);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                DatabaseReference booking = firebaseDatabase.getReference().child("booking");
                count++;
                DatabaseReference request = booking.child("odrid" + count);
                firebaseDatabase.getReference().child("odrcount").setValue(count);

                Map<String, String> map = new HashMap<>();
                Date currentTime = Calendar.getInstance().getTime();
                map.put("requesttime", currentTime.toString().trim());
                map.put("requeststate", "pending");
                map.put("uid", id.trim());


                map.put("name", _txtbookname.getText().toString().trim());
                map.put("sname", _txtbooksname.getText().toString().trim());
                map.put("email", emailText.trim());
                map.put("noofpersons", _txtbooknoofperson.getText().toString().trim());
                map.put("date", _txtbookdate.getText().toString().trim());
                map.put("time", _txtbooktime.getText().toString().trim());
                map.put("aditionalinfo", _txtbookadtionalinfo.getText().toString().trim());
                map.put("odrid", "odrid" + count);
                request.setValue(map);
                _txtbookname.setText("");
                _txtbooksname.setText("");
                _txtbooktime.setText("");
                _txtbookdate.setText("");
                _txtbookadtionalinfo.setText("");
                _txtbooknoofperson.setText("");
                pDialog.dismiss();
                Toast.makeText(rootView.getContext(), "Order Submited Successfully ", Toast.LENGTH_LONG).show();


            }
        });
        return rootView;
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {

            String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            _txtbookdate.setText(date1);
        }
    };


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getActivity().getFragmentManager(), "Date Picker");
    }

    private void showTimePicker() {
        //DatePickerFragment date = new DatePickerFragment();
        TimePickerFragment time = new TimePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", calender.HOUR_OF_DAY);
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("minute", calender.get(Calendar.MINUTE));
        time.setArguments(args);


        time.setCallBack(ontime);
        time.show(getActivity().getFragmentManager(), "Time Picker");

    }

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hour, int minute) {
            // TODO Auto-generated method stub

            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            _txtbooktime.setText(time1);
        }
    };


/*
    protected Dialog  showDialog(int id) {


    Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        switch (id) {
            case Date_id:
                return new DatePickerDialog(getActivity(),date_listener, year,month,day);


            case Time_id:
                return new TimePickerDialog(getActivity(), time_listener, hour,
                        minute, false);
        }
        return null;
    }
*/


    /*    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                        + "/" + String.valueOf(year);
                _txtbookdate.setText(date1);
            }
        };
        TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
                _txtbooktime.setText(time1);
            }
        };*/
    public boolean validate() {
        boolean valid = true;

        String txtbookname = _txtbookname.getText().toString();
        String txtbooksname = _txtbooksname.getText().toString();
        String txtbooktime = _txtbooktime.getText().toString();
        String txtbookdate = _txtbookdate.getText().toString();

        if (txtbookname.isEmpty() || txtbookname.length() < 3) {
            _txtbookname.setError("enter name");
            valid = false;
        } else {
            _txtbookname.setError(null);
        }
        if (txtbooksname.isEmpty() || txtbooksname.length() < 3) {
            _txtbooksname.setError("enter surname");
            valid = false;
        } else {
            _txtbooksname.setError(null);
        }
        if (txtbooktime.isEmpty()) {
            _txtbooktime.setError("enter time");
            valid = false;
        } else {
            _txtbooktime.setError(null);
        }
        if (txtbookdate.isEmpty()) {
            _txtbookdate.setError("enter Date");
            valid = false;
        } else {
            _txtbookdate.setError(null);
        }

        return valid;
    }

    public void bookfail() {
        Toast.makeText(getActivity(), "Please fill * field", Toast.LENGTH_LONG).show();

    }

   /* private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        *//**
     * Set Up Current Date Into dialog
     *//*
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        *//**
     * Set Call back to capture selected date
     *//*
        date.setCallBack(ondate);
        date.show(getActivity().getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Toast.makeText(getContext(),
                    String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                            + "-" + String.valueOf(dayOfMonth),
                    Toast.LENGTH_LONG).show();
        }
    };*/


}

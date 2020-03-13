package com.valentine.eventlogger.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.valentine.eventlogger.R;

import java.util.Calendar;

public class UserFIlterActivity extends AppCompatActivity {

    private static final String TAG = "UserFIlterActivity";

    private FrameLayout startDateFrame, endDateFrame;
    private TextView startDateDate, endDateDate;
    private Button resetStartDate, resetEndDate, activateFilter;
    private String dateHolder;
    private int userChoice; /*userDate is 0 if the user pressed Startdate, and 1 if user pressed endDate*/
    private DatePickerDialog.OnDateSetListener mDateSetListerner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_filter);

        startDateDate = findViewById(R.id.StartDateDate);
        startDateDate.setText("");
        endDateDate = findViewById(R.id.EndDateDate);
        endDateDate.setText("");

        startDateFrame = findViewById(R.id.StartDateFrame);
        endDateFrame = findViewById(R.id.EndDateFrame);
        resetStartDate = findViewById(R.id.ResetStartDate);
        resetEndDate = findViewById(R.id.ResetEndDate);
        activateFilter = findViewById(R.id.ActivateFilterButton);

        startDateFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHolder = "";
                userChoice = 0;
                userDatePicker();
            }
        });

        endDateFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHolder = "";
                userChoice = 1;
                userDatePicker();
            }
        });

        resetStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateDate.setText("");
            }
        });

        resetEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDateDate.setText("");
            }
        });

        activateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filterStartDate = startDateDate.getText().toString();
                String filterEndDate = endDateDate.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("startDate", filterStartDate);
                returnIntent.putExtra("endDate", filterEndDate);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        mDateSetListerner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "/" + month + "/" + day);
                dateHolder = year + "-" + month + "-" + day;

                if (userChoice == 0){
                    startDateDate.setText(dateHolder);
                }
                if (userChoice == 1){
                    endDateDate.setText(dateHolder);
                }
            }
        };
    }


    private void userDatePicker(){

        Log.d(TAG, "userDatePicker: Activated");

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                UserFIlterActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListerner,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}

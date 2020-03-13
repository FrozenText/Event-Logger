package com.valentine.eventlogger.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.MainActivity;
import com.valentine.eventlogger.R;
import com.valentine.eventlogger.RVAdapters.DEL_RecyclerViewAdapter;
import com.valentine.eventlogger.models.UserEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisplayEventLogActivity extends AppCompatActivity {

    private static final String TAG = "DisplayEventLogActivity";

    static UserEventDao logDao;

    ArrayList<Date> mLoggedTime;
    ArrayList<String> mLoggedEvent;
    ArrayList<String> mLoggedDescription;
    ArrayList<Integer> mId;

    Date filterDateStart,filterDateEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_log);

        filterDateStart = null;
        filterDateEnd = null;

        initEventLog();



    }

    public void initEventLog() {

        logDao = MainActivity.getLogdb().getUserEventDao();
        mLoggedTime = new ArrayList<>();
        mLoggedEvent = new ArrayList<>();
        mLoggedDescription = new ArrayList<>();
        mId = new ArrayList<>();

        /*Adds the list backwards, so that the newest event comes first*/
        for (int i = logDao.getEvents().size()-1 ; i >= 0; i--) {
            Log.d(TAG, "initEventlog for loop number: " + i);

            UserEvent e = logDao.getEvents().get(i);

            mLoggedTime.add(e.getEventdate());
            mLoggedEvent.add(e.getEvent());
            mLoggedDescription.add(e.getDescription());
            mId.add(e.getEventid());

        }


        if (filterDateStart != null){
            Log.d(TAG, "initEventLog: Filterdatestart is: " + filterDateStart.toString());
            for (int i = mLoggedTime.size() - 1; i>=0; i--){
                if (mLoggedTime.get(i).before(filterDateStart)){
                    mLoggedTime.remove(i);
                    mLoggedDescription.remove(i);
                    mLoggedEvent.remove(i);
                    mId.remove(i);
                }
            }
        }
        if (filterDateEnd != null){
            Log.d(TAG, "initEventLog: Filterdate end is: " + filterDateEnd.toString());
            for (int i = mLoggedTime.size() - 1; i>=0; i--){
                if (mLoggedTime.get(i).after(filterDateEnd)){
                    mLoggedTime.remove(i);
                    mLoggedDescription.remove(i);
                    mLoggedEvent.remove(i);
                    mId.remove(i);
                }
            }
        }

        initRecyclerView();
    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.DEL_RecyclerView);
        DEL_RecyclerViewAdapter adapter = new DEL_RecyclerViewAdapter(mLoggedTime, mLoggedEvent, mLoggedDescription, mId, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void startFilterActivity(View view){

        Intent intent = new Intent(this,UserFIlterActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult: Activated");

        if(resultCode == RESULT_OK){

            String str_startDate = data.getStringExtra("startDate");
            String str_endDate = data.getStringExtra("endDate");
            Calendar cal = Calendar.getInstance();

            if (!(str_startDate.equals("")) && str_startDate != null){
                try {
                    String[] ymdDateHolder = str_startDate.split("-");
                    int year = Integer.parseInt(ymdDateHolder[0]);
                    int month = Integer.parseInt(ymdDateHolder[1]) - 1;
                    int day = Integer.parseInt(ymdDateHolder[2]);
                    cal.set(year,month,day);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    filterDateStart = cal.getTime();
                } catch (Exception e) {
                    Log.d(TAG, "Startdate filter exeption" + e);
                }
            }else{
                Log.d(TAG, "Startdate is empty");
                filterDateStart = null;
            }
            if (!(str_endDate.equals("")) && str_endDate != null){
                try {
                    String[] ymdDateHolder = str_endDate.split("-");
                    int year = Integer.parseInt(ymdDateHolder[0]);
                    int month = Integer.parseInt(ymdDateHolder[1]) - 1;
                    int day = Integer.parseInt(ymdDateHolder[2]);
                    cal.set(year,month,day);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    filterDateEnd = cal.getTime();
                } catch (Exception e) {
                    Log.d(TAG, "enddate filter exeption" + e);
                }
            }else{
                Log.d(TAG, "Enddate is empty");
                filterDateEnd = null;
            }

        }

        initEventLog();
    }

}
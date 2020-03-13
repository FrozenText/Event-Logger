package com.valentine.eventlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.valentine.eventlogger.Activity.CreateEventActivity;
import com.valentine.eventlogger.Activity.DisplayEventLogActivity;
import com.valentine.eventlogger.Activity.EditEventActivity;
import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.RVAdapters.M_RecyclerViewAdapter;
import com.valentine.eventlogger.models.UserEvent;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static AppDatabase eventdb, logdb;
    public static UserEventDao eventDao, logDao;

    ArrayList<String> mEventText;
    ArrayList<Integer> mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventdb = Room.databaseBuilder(this, AppDatabase.class, "eventdb")
                .allowMainThreadQueries()
                .build();
        logdb = Room.databaseBuilder(this, AppDatabase.class, "logdb")
                .allowMainThreadQueries()
                .build();

        eventDao = eventdb.getUserEventDao();
        logDao = logdb.getUserEventDao();

        initEvents();

    }

    public void initEvents() {

        mEventText = new ArrayList<>();
        mId = new ArrayList<>();

        for (int i = 0; i < eventDao.getEvents().size(); i++) {
            Log.d(TAG, "initEvents for loop number: " + i);

            UserEvent e = eventDao.getEvents().get(i);

            mEventText.add(e.getEvent());
            mId.add(e.getEventid());

        }

        initRecyclerView();
    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.M_RecyclerView);
        M_RecyclerViewAdapter adapter = new M_RecyclerViewAdapter(mEventText, mId, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**Chages Activity to DisplayEventLog*/
    public void goToDisplayEventLog(View view){

        Intent intent = new Intent(this, DisplayEventLogActivity.class);
        startActivity(intent);
    }

    /**Chages Activity to CreateEvent*/
    public void goToCreateEvent(View view){

        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivityForResult(intent, 1);
    }

    /**Changes Activity to Edit*/
    public void goToEditEvent(View view){

        Intent intent = new Intent(this, EditEventActivity.class);
        startActivity(intent);
    }


    public static AppDatabase getEventdb(){
        return eventdb;
    }

    public static AppDatabase getLogdb(){
        return logdb;
    }
}

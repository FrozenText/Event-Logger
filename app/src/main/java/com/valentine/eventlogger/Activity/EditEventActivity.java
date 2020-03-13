package com.valentine.eventlogger.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.valentine.eventlogger.AppDatabase;
import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.MainActivity;
import com.valentine.eventlogger.R;
import com.valentine.eventlogger.RVAdapters.EE_RecyclerViewAdapter;
import com.valentine.eventlogger.models.UserEvent;

import java.util.ArrayList;

public class EditEventActivity extends AppCompatActivity {

    private static final String TAG = "EditEventActivity";

    UserEventDao eventDao;
    final static int maxEvents = 20;

    private ArrayList<String> mEditEventText;
    private ArrayList<Integer> mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        AppDatabase edb = MainActivity.getEventdb();
        eventDao = edb.getUserEventDao();

        initEvents();
    }

    public void initEvents() {

        mEditEventText = new ArrayList<>();
        mId = new ArrayList<>();

        for (int i = 0; i < eventDao.getEvents().size(); i++) {
            Log.d(TAG, "initEvents for loop number: " + i);

            UserEvent e = eventDao.getEvents().get(i);

            mEditEventText.add(e.getEvent());
            mId.add(e.getEventid());

        }

        initRecyclerView();
    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.EE_RecyclerView);
        EE_RecyclerViewAdapter adapter = new EE_RecyclerViewAdapter(mEditEventText, mId, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}

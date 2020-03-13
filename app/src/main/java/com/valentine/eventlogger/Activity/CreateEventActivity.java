package com.valentine.eventlogger.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.valentine.eventlogger.AppDatabase;
import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.MainActivity;
import com.valentine.eventlogger.R;
import com.valentine.eventlogger.models.UserEvent;


public class CreateEventActivity extends AppCompatActivity {

    private static final String TAG = "CreateEventActivity";

    UserEventDao eventDao;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        AppDatabase edb = MainActivity.getEventdb();
        eventDao = edb.getUserEventDao();
        editText = findViewById(R.id.CreateEventEditText);

        /*Had problems with the softkeyboard not showing upp when activity was called, hence why
         this odd piece of code here to make it come up(sollution taken from:
        https://stackoverflow.com/questions/5105354/how-to-show-soft-keyboard-when-edittext-is-focused
        22-08-2019)*/
        new Handler().postDelayed(new Runnable() {
            public void run() {
                editText = findViewById(R.id.CreateEventEditText);

                editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 100);

    }



    public void createNewEvent(View view){

        Log.d(TAG,"createNewEvent: called");

        String newEventText = editText.getText().toString();
        Log.d(TAG, "created event text: \"" + newEventText + "\"");

        if (!(newEventText.trim().matches("")) /*&& eventDao.getEvents().size() < maxEvents*/) {
            UserEvent event = new UserEvent();
            event.setEvent(newEventText);

            eventDao.insert(event);
            Toast toast = Toast.makeText(CreateEventActivity.this, "Event " + event.getEvent() + " have been created!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            editText.setText("");

        } else {
            Toast.makeText(view.getContext(), "Please name the event.", Toast.LENGTH_LONG).show();
        }
    }

}

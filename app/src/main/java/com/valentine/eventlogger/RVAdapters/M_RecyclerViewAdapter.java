package com.valentine.eventlogger.RVAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.MainActivity;
import com.valentine.eventlogger.R;
import com.valentine.eventlogger.models.UserEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class M_RecyclerViewAdapter extends RecyclerView.Adapter<M_RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "M_RecyclerViewAdapter";

    private UserEventDao eventDao, logDao;

    private ArrayList<String> mEventText;
    private ArrayList<Integer> mId;
    private Context mContext;

    public M_RecyclerViewAdapter(ArrayList<String> mEventText, ArrayList<Integer> mId, Context mContext) {
        this.mEventText = mEventText;
        this.mId = mId;
        this.mContext = mContext;
        eventDao = MainActivity.getEventdb().getUserEventDao();
        logDao = MainActivity.getLogdb().getUserEventDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_layout_listitem, parent,false);
        M_RecyclerViewAdapter.ViewHolder holder = new M_RecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");

        holder.eventText.setText(mEventText.get(position));

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logEvent(eventDao.getEventById(mId.get(position)),"");

                holder.frameLayout.setEnabled(false);
                holder.eventText.setEnabled(false);
                holder.frameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.frameLayout.setEnabled(true);
                        holder.eventText.setEnabled(true);
                    }
                }, 2000);
            }
        });

        holder.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDescriptionToEvent(mId.get(position));
            }
        });
    }

    /**Adds event to log database when called*/
    public void logEvent(UserEvent event, String description){

        Log.d(TAG, "logEvent: called");

        UserEvent newEvent = new UserEvent();
        newEvent.setEvent(event.getEvent());
        newEvent.setDescription(description);
        newEvent.setEventdate(Calendar.getInstance().getTime());

        logDao.insert(newEvent);

        Toast.makeText(mContext,"Event \"" + newEvent.getEvent() + "\" logged!",Toast.LENGTH_SHORT).show();

    }

    /**Confirmation popup activating when you press the log button*/
    public void addDescriptionToEvent(final int eventId){

        Log.d(TAG, "addDescriptionToEvent: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle("Add description");
        builder.setMessage("What description to the event \"" + eventDao.getEventById(eventId).getEvent() + "\" would you like to add?");

//      Edittext window
        final EditText input = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Add description", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                logEvent(eventDao.getEventById(eventId),input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    public int getItemCount() {
        return mEventText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView eventText;
        Button logButton;
        FrameLayout frameLayout;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.eventText);
            logButton = itemView.findViewById(R.id.logButton);
            frameLayout = itemView.findViewById(R.id.m_frame_layout);
            parentLayout = itemView.findViewById(R.id.m_parent_layout);
        }
    }
}

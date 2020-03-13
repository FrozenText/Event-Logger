package com.valentine.eventlogger.RVAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class EE_RecyclerViewAdapter extends RecyclerView.Adapter<EE_RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "EE_RecyclerViewAdapter";

    private UserEventDao eventDao;

    private ArrayList<String> mEditEventText;
    private ArrayList<Integer> mId;
    private Context mContext;

    public EE_RecyclerViewAdapter(ArrayList<String> mEditEventText, ArrayList<Integer> mId, Context mContext) {
        this.mEditEventText = mEditEventText;
        this.mId = mId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ee_layout_listitem, parent,false);
        EE_RecyclerViewAdapter.ViewHolder holder = new EE_RecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");

        final EditText eText = holder.editEventText;

        eText.setText(mEditEventText.get(position));

        holder.editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"editEventButton clicked;");

                eventDao = MainActivity.getEventdb().getUserEventDao();
                if (!(eText.getText().toString().trim().matches(""))) {
                    changeEventText(eText, mId.get(position));
                }else{
                    removeEvent(mId.get(position));
                }

            }
        });
    }

    public void removeEvent(final int eventID){
        Log.d(TAG, "removeEvent: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle("Delete all events");
        builder.setMessage("Are you sure that you would want to delete the event?");
        builder.setPositiveButton("Delete event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                eventDao.delete(eventDao.getEventById(eventID));

                Toast.makeText(mContext, "Event Deleted!", Toast.LENGTH_SHORT).show();
                eventDao.update();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void changeEventText(EditText newEventText, int eventID){
        Log.d(TAG,"changeEventText: called:");

        UserEvent event = eventDao.getEventById(eventID);
        event.setEvent(newEventText.getText().toString());
        eventDao.update(event);

        Toast.makeText(mContext,"Event updated!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return mEditEventText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText editEventText;
        Button editEventButton;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editEventText = itemView.findViewById(R.id.editEventText);
            editEventButton = itemView.findViewById(R.id.editEventButton);
            parentLayout = itemView.findViewById(R.id.m_parent_layout);
        }
    }

}

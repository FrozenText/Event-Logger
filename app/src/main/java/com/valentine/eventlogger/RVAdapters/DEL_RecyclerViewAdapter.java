package com.valentine.eventlogger.RVAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.MainActivity;
import com.valentine.eventlogger.R;

import java.util.ArrayList;
import java.util.Date;

public class DEL_RecyclerViewAdapter extends RecyclerView.Adapter<DEL_RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DEL_RecyclerViewAdapter";

    private UserEventDao logDao;
    private ArrayList<Date> mLoggedTime;
    private ArrayList<String> mLoggedEvent, mLoggedDescription;
    private ArrayList<Integer> mId;
    /*private SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); for high api*/
    private Context mContext;

    public DEL_RecyclerViewAdapter(ArrayList<Date> mLoggedTime , ArrayList<String> mLoggedEvent, ArrayList<String> mLoggedDescription, ArrayList<Integer> mId, Context mContext) {
        this.mLoggedTime = mLoggedTime;
        this.mLoggedEvent = mLoggedEvent;
        this.mLoggedDescription = mLoggedDescription;
        this.mId = mId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.del_layout_listitem, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");

        /*holder.loggedTime.setText(DateFormat.format(mLoggedTime.get(position))); for high api*/

        /*lower api metod*/
        String[] dateHolder = mLoggedTime.get(position).toString().split(" ");

        String parsedDate = dateHolder[0] + " " + dateHolder[2] + " " + dateHolder[1] + " " + dateHolder[5] + " "
                + dateHolder[3];
        holder.loggedTime.setText(parsedDate);


        holder.loggedEvent.setText(mLoggedEvent.get(position));
        holder.loggedDescription.setText(mLoggedDescription.get(position));

        holder.loggedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    removeFromLog(mId.get(position), mContext);
                }catch (Exception e) {
                    Toast.makeText(mContext,"Logged event have alreddy been removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void removeFromLog(final int eventID, Context context){

        logDao = MainActivity.getLogdb().getUserEventDao();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Remove logged event");
        builder.setMessage("Are you sure you would like to remove the logged event?\n" +
                "This cannot be undone.");
        builder.setPositiveButton("Remove logged Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    logDao.delete(logDao.getEventById(eventID));
                }catch (Exception e) {
                    Toast.makeText(mContext,"Logged event have allreddy been removed", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public int getItemCount() {
        return mLoggedEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView loggedTime;
        TextView loggedEvent;
        TextView loggedDescription;
        Button loggedButton;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            loggedTime = itemView.findViewById(R.id.LoggedTime);
            loggedEvent = itemView.findViewById(R.id.LoggedEvent);
            loggedDescription = itemView.findViewById(R.id.LoggedDescription);
            loggedButton = itemView.findViewById(R.id.LoggedEventButton);
            parentLayout = itemView.findViewById(R.id.del_parent_layout);
        }
    }

}

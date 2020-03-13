package com.valentine.eventlogger.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.valentine.eventlogger.TypeConverter.DateConverter;

import java.util.Date;


@Entity(tableName = "events")
@TypeConverters(DateConverter.class)
public class UserEvent {

    @PrimaryKey(autoGenerate = true)
    public int eventid;

    @ColumnInfo(name = "event")
    public String event;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "event_date")
    public Date eventdate;

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventdate() {
        return eventdate;
    }

    public void setEventdate(Date eventdate) {
        this.eventdate = eventdate;
    }
}

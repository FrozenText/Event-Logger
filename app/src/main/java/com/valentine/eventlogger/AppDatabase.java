package com.valentine.eventlogger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.valentine.eventlogger.Dao.UserEventDao;
import com.valentine.eventlogger.models.UserEvent;

@Database(entities = {UserEvent.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserEventDao getUserEventDao();
}

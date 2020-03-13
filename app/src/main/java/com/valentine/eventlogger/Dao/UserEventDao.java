package com.valentine.eventlogger.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.valentine.eventlogger.models.UserEvent;

import java.util.List;

@Dao
public interface UserEventDao {

    @Insert
    void insert(UserEvent... events);

    @Update
    void update(UserEvent... events);

    @Delete
    void delete(UserEvent event);

    @Query("SELECT * FROM events")
    List<UserEvent> getEvents();

    @Query("SELECT * FROM events WHERE eventid = :id")
    UserEvent getEventById(int id);
}

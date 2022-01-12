package com.example.addverb.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = entity.class,version = 1,exportSchema = false)
public abstract class db extends RoomDatabase{
    private static db db;
    public static synchronized db getDb(Context context){
        if(db==null){
            db= Room.databaseBuilder(context,db.class,"countries_db").build();
        }
        return db;
    }
    public abstract countriesDao countriesDao();

}

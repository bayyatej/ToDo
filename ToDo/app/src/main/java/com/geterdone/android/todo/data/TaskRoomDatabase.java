package com.geterdone.android.todo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskRoomDatabase extends RoomDatabase {
    private static TaskRoomDatabase INSTANCE;

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "task_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TaskDao taskDao();
}

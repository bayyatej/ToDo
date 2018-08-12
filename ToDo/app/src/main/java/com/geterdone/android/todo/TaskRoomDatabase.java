package com.geterdone.android.todo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskRoomDatabase extends RoomDatabase
{
	public abstract TaskDao taskDao();

	private static TaskRoomDatabase INSTANCE;

	public static TaskRoomDatabase getDatabase(final Context context)
	{
		if (INSTANCE == null)
		{
			synchronized (TaskRoomDatabase.class)
			{
				if (INSTANCE == null)
				{
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
													TaskRoomDatabase.class, "task_database").build();
				}
			}
		}
		return INSTANCE;
	}
}

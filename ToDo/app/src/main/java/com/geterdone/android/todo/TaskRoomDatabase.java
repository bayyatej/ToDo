package com.geterdone.android.todo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskRoomDatabase extends RoomDatabase
{
	public abstract TaskDao taskDao();

	private static TaskRoomDatabase INSTANCE;
	private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback()
	{
		@Override
		public void onOpen(@NonNull SupportSQLiteDatabase db)
		{
			super.onOpen(db);
			new PopulateDbAsync(INSTANCE).execute();
		}
	};

	private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>
	{
		private final TaskDao mDao;

		PopulateDbAsync(TaskRoomDatabase db)
		{
			mDao = db.taskDao();
		}

		@Override
		protected Void doInBackground(final Void... params)
		{
			mDao.deleteAll();
			Task task = new Task("Crush it", "24/7");
			mDao.insert(task);
			task = new Task("Wheel, Snipe, Celly", "Ferda");
			mDao.insert(task);
			return null;
		}
	}

	public static TaskRoomDatabase getDatabase(final Context context)
	{
		if (INSTANCE == null)
		{
			synchronized (TaskRoomDatabase.class)
			{
				if (INSTANCE == null)
				{
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
													TaskRoomDatabase.class, "task_database")
							.addCallback(sRoomDatabaseCallback).build();
				}
			}
		}
		return INSTANCE;
	}
}

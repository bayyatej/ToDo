package com.geterdone.android.todo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Task task);

	@Update
	void update(Task task);

	@Delete
	void delete(Task task);

	@Query("DELETE FROM task_table")
	void deleteAll();

	//todo update to get tasks created in a particular list
//	@Query("SELECT * from task_table WHERE parentKey = :taskKey ORDER BY priority ASC")
	@Query("SELECT * from task_table ORDER BY id ASC")
	LiveData<List<Task>> getAllTasks();

}

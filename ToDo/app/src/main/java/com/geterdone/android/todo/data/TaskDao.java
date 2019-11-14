package com.geterdone.android.todo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table WHERE id = :id")
    Task getTaskById(int id);

    //todo update to get tasks created in a particular list
//	@Query("SELECT * from task_table WHERE parentKey = :taskKey ORDER BY priority ASC")
    @Query("SELECT * FROM task_table ORDER BY priority DESC, date ASC")
    LiveData<List<Task>> getAllTasks();

}

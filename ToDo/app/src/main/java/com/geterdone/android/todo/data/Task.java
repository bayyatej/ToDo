package com.geterdone.android.todo.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "task_table")
public class Task
{
	@PrimaryKey(autoGenerate = true)
	private int id;
	//todo parentKey support
	@NonNull
	@ColumnInfo(name = "priority")
	private Integer mPriority = 0;
	/*
	@ColumnInfo(name = "parentKey")
	private Integer parentKey;*/
	@NonNull
	@ColumnInfo(name = "name")
	private String mTaskName;
	@ColumnInfo(name = "date")
	private long mTaskDate;

	public Task(@NonNull String taskName, @NonNull long taskDate, @NonNull Integer priority)
	{
		this.mTaskName = taskName;
		this.mTaskDate = taskDate;
		this.mPriority = priority;
	}

	public int getId()
	{
		return this.id;
	}

	public Integer getPriority()
	{
		return this.mPriority;
	}
/*
	public int getParentKey()
	{
		return this.parentKey;
	}*/

	public String getTaskName()
	{
		return this.mTaskName;
	}

	public long getTaskDate()
	{
		return this.mTaskDate;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setPriority(@NonNull Integer priority)
	{
		this.mPriority = priority;
	}

	/*
	public void setParentKey(Integer parentKey)
	{
		this.parentKey = parentKey;
	}*/
}

package com.geterdone.android.todo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "task_table")
public class Task
{
	@PrimaryKey(autoGenerate = true)
	private int id;
	@NonNull
	@ColumnInfo(name = "priority")
	private Integer priority;
	@ColumnInfo(name = "parentKey")
	private Integer parentKey;
	@NonNull
	@ColumnInfo(name = "name")
	private String mTaskName;
	@ColumnInfo(name = "date")
	private String mTaskDate;

	public Task(@NonNull String taskName, @NonNull String taskDate)
	{
		this.mTaskName = taskName;
		this.mTaskDate = taskDate;
	}

	public int getTaskId()
	{
		return this.id;
	}

	public int getTaskPriority()
	{
		return this.priority;
	}

	public int getTaskParent()
	{
		return this.parentKey;
	}

	public String getTaskName()
	{
		return this.mTaskName;
	}

	public String getTaskDate()
	{
		return this.mTaskDate;
	}
}

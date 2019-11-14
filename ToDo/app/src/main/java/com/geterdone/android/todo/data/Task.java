package com.geterdone.android.todo.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //todo parentKey support
    @NonNull
    @ColumnInfo(name = "priority")
    private Integer mPriority;
    /*
    @ColumnInfo(name = "parentKey")
    private Integer parentKey;*/
    @NonNull
    @ColumnInfo(name = "name")
    private String mTaskName;
    @ColumnInfo(name = "date")
    private long mTaskDate;
    @ColumnInfo(name = "end_date")
    private long mTaskEndDate;
    @ColumnInfo(name = "repeat_frequency")
    private String mRepeatFrequency;

    public Task(@NonNull String taskName, @NonNull long taskDate, @NonNull Integer priority) {
        this.mTaskName = taskName;
        this.mTaskDate = taskDate;
        this.mPriority = priority;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPriority() {
        return this.mPriority;
    }
/*
	public int getParentKey()
	{
		return this.parentKey;
	}*/

    public void setPriority(@NonNull Integer priority) {
        this.mPriority = priority;
    }

    public String getRepeatFrequency() {
        return this.mRepeatFrequency;
    }

    public void setRepeatFrequency(String frequency) {
        this.mRepeatFrequency = frequency;
    }

    public String getTaskName() {
        return this.mTaskName;
    }

    public void setTaskName(String name) {
        this.mTaskName = name;
    }

    public long getTaskDate() {
        return this.mTaskDate;
    }

    public void setTaskDate(long date) {
        this.mTaskDate = date;
    }

    public long getTaskEndDate() {
        return this.mTaskEndDate;
    }

    public void setTaskEndDate(long endDate) {
        this.mTaskEndDate = endDate;
    }

	/*
	public void setParentKey(Integer parentKey)
	{
		this.parentKey = parentKey;
	}*/
}

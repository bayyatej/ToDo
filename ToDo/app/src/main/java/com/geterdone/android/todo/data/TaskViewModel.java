package com.geterdone.android.todo.data;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TaskViewModel extends AndroidViewModel
{
	private TaskRepository mRepository;
	private LiveData<List<Task>> mAllTasks;

	public TaskViewModel(Application application)
	{
		super(application);
		mRepository = new TaskRepository(application);
		mAllTasks = mRepository.getAllTasks();
	}

	public LiveData<List<Task>> getAllTasks()
	{
		return mAllTasks;
	}

	public void insert(Task task)
	{
		mRepository.insert(task);
	}

	public void update(Task task)
	{
		mRepository.update(task);
	}

	public void delete(Task task)
	{
		mRepository.delete(task);
	}

	public Task getTaskById(int mId)
	{
		return mRepository.getTaskById(mId);
	}
}

package com.geterdone.android.todo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.geterdone.android.todo.data.Task;
import com.geterdone.android.todo.data.TaskViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
	public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
	private TaskViewModel mTaskViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
				startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
			}
		});
		RecyclerView recyclerView = findViewById(R.id.recyclerview);
		final TaskListAdapter adapter = new TaskListAdapter(this);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
		mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>()
		{
			@Override
			public void onChanged(@Nullable List<Task> tasks)
			{
				adapter.setTasks(tasks);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
		{
			Task task = new Task(data.getStringExtra(NewTaskActivity.EXTRA_NAME), data
					.getStringExtra(NewTaskActivity.EXTRA_DATE));
			mTaskViewModel.insert(task);
		} else
		{
			Snackbar.make(findViewById(R.id.main_activity_coordinator), "Task Not Saved",
						  Snackbar.LENGTH_LONG).show();
		}
	}
}

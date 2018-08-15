package com.geterdone.android.todo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.geterdone.android.todo.data.Task;
import com.geterdone.android.todo.data.TaskViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
	public static final int TASK_EDITOR_ACTIVITY_REQUEST_CODE = 1;
	private static final String CHANNEL_ID = "Get 'er done";
	private TaskViewModel mTaskViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createNotificationChannel();

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = findViewById(R.id.fab);
		RecyclerView recyclerView = findViewById(R.id.recyclerview);
		final TaskListAdapter adapter = new TaskListAdapter(this);

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(MainActivity.this, TaskEditorActivity.class);
				intent.putExtra("action", "add");
				startActivityForResult(intent, TASK_EDITOR_ACTIVITY_REQUEST_CODE);
			}
		});

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

	/*
		Helper Methods
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TASK_EDITOR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
		{
			Task task = new Task(data.getStringExtra(TaskEditorActivity.EXTRA_NAME), data
					.getLongExtra(TaskEditorActivity.EXTRA_DATE, 0), data.getIntExtra("priority", 0));
			int id = data.getIntExtra("taskId", -1);
			switch (data.getStringExtra("action"))
			{
				case "add":
					mTaskViewModel.insert(task);
					//todo set notification
					break;
				case "edit":
					if (id != -1)
					{
						task.setId(id);
						mTaskViewModel.update(task);
						//todo update notification
					}
					break;
				case "delete":
					if (id != -1)
					{
						task.setId(id);
						mTaskViewModel.delete(task);
						//todo delete notification
					}
					break;
				default:
					Snackbar.make(findViewById(R.id.main_activity_coordinator), "Task Not Saved",
								  Snackbar.LENGTH_LONG).show();
					break;
			}
		} else
		{
			Snackbar.make(findViewById(R.id.main_activity_coordinator), "Task Not Saved",
						  Snackbar.LENGTH_LONG).show();
		}
	}

	private void createNotificationChannel()
	{
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			CharSequence name = getString(R.string.channel_name);
			String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			if (notificationManager != null)
			{
				notificationManager.createNotificationChannel(channel);
			}
		}
	}

	/*
		Menu Methods
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.settings_menu_item:
				//todo launch settings menu activity
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
}

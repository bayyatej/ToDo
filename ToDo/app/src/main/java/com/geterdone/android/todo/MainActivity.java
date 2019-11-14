package com.geterdone.android.todo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.AlarmManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geterdone.android.todo.data.Task;
import com.geterdone.android.todo.data.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*
        todo add support for lists
        todo add support for preset list types
        todo add repeating task support in MainActivity
        todo support android app links
        todo support swipe gestures
        todo add data binding to app
        todo launch settings menu
     */
    public static final int TASK_EDITOR_ACTIVITY_REQUEST_CODE = 1;
    public static final String CHANNEL_ID = "Get 'er done";
    public static TaskViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskEditorActivity.class);
                intent.putExtra("action", "add");
                startActivityForResult(intent, TASK_EDITOR_ACTIVITY_REQUEST_CODE);
            }
        });

        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });
    }

    /*
        Helper Methods
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TASK_EDITOR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Task task;
            String name = data.getStringExtra(TaskEditorActivity.EXTRA_NAME);
            long date = data.getLongExtra(TaskEditorActivity.EXTRA_DATE, 0);
            long endDate = data.getLongExtra("endDate", 0);
            Integer priority = data.getIntExtra("priority", 0);
            String frequency = data.getStringExtra("frequency");
            int id = data.getIntExtra("taskId", -1);
            switch (data.getStringExtra("action")) {
                case "add":
                    task = new Task(name, date, priority);
                    task.setRepeatFrequency(frequency);
                    task.setTaskEndDate(endDate);
                    mTaskViewModel.insert(task);
                    scheduleNotification(task);
                    break;
                case "edit":
                    if (id != -1) {
                        task = mTaskViewModel.getTaskById(id);
                        task.setTaskName(name);
                        task.setTaskDate(date);
                        task.setPriority(priority);
                        task.setRepeatFrequency(frequency);
                        task.setTaskEndDate(endDate);
                        mTaskViewModel.update(task);
                        scheduleNotification(task);
                    }
                    break;
                case "delete":
                    if (id != -1) {
                        task = mTaskViewModel.getTaskById(id);
                        task.setTaskName(name);
                        task.setTaskDate(date);
                        task.setPriority(priority);
                        mTaskViewModel.delete(task);
                        cancelNotification(task);
                    }
                    break;
                default:
                    Snackbar.make(findViewById(R.id.main_activity_coordinator), "Task Not Saved",
                            Snackbar.LENGTH_LONG).show();
                    break;
            }
        } else {
            Snackbar.make(findViewById(R.id.main_activity_coordinator), "Task Not Saved",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void scheduleNotification(Task task) {
        long date = task.getTaskDate();
        ComponentName receiver = new ComponentName(this, TaskNotificationPublisher.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        if (date > 0) {
            Intent intent = new Intent(this, TaskNotificationPublisher.class);
            intent.putExtra("name", task.getTaskName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), task.getId(), intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date, pendingIntent);
                } else {
                    AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, date, pendingIntent);
                }
            } else {
                Log.i("schedule failed", "scheduleNotification: ");
            }
        }
    }

    private void cancelNotification(Task task) {
        Intent intent = new Intent(this, TaskNotificationPublisher.class);
        intent.putExtra("name", task.getTaskName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getId(), intent, 0);
        pendingIntent.cancel();
    }

    /*
        Menu Methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_menu_item) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}

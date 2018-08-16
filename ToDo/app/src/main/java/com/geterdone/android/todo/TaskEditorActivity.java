package com.geterdone.android.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.geterdone.android.todo.data.Task;
import com.geterdone.android.todo.data.TaskViewModel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class TaskEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
	//todo validate input

	public static final String EXTRA_NAME = "com.geterdone.android.tasklistsql.NAME";
	public static final String EXTRA_DATE = "com.geterdone.android.tasklistsql.DATE";

	private EditText mTaskNameEditText;
	private TextView mTaskDateTextView;
	private Button mTaskTimeBtn;

	private TaskViewModel mTaskViewModel;
	private Calendar mCal;
	private String mTimeDisplayString;
	private String mDateDisplayString;
	private String mDateTimeDisplayString;
	private String mAction;
	private boolean mDateSet;
	private boolean mTimeSet;
	private long mDateTime;
	private int mPriority;
	private int mId;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
		setupWidgets();
	}

	/*
		Helper methods
	 */
	private void setupWidgets()
	{
		ActionBar actionBar = getSupportActionBar();
		Intent intent = getIntent();
		mTaskNameEditText = findViewById(R.id.editor_task_name_edit_text);
		mTaskDateTextView = findViewById(R.id.editor_task_date_text_view);
		mTaskTimeBtn = findViewById(R.id.task_time_button);
		Spinner mPrioritySpinner = findViewById(R.id.editor_task_priority_spinner);
		Button taskDateBtn = findViewById(R.id.task_date_button);
		mCal = Calendar.getInstance(TimeZone.getDefault());
		mAction = intent.getStringExtra("action");
		mId = intent.getIntExtra("taskId", -1);
		Task task = mTaskViewModel.getTaskById(mId);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.editor_task_priority_array, android.R.layout.simple_spinner_item);

		taskDateBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DatePickerFragment datePickerFragment = new DatePickerFragment();
				datePickerFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		mTaskTimeBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TimePickerFragment timePickerFragment = new TimePickerFragment();
				timePickerFragment.show(getSupportFragmentManager(), "timePicker");
			}
		});
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPrioritySpinner.setAdapter(spinnerAdapter);
		mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mPriority = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				mPriority = 0;
			}
		});

		//Set action bar title based on whether user is editing or adding a task
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			switch (mAction)
			{
				case "add":
					actionBar.setTitle("Add New Task");
					break;
				case "edit":
					mDateTime = task.getTaskDate();
					mPriority = task.getPriority();
					mDateSet = true;
					mTimeSet = true;
					DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
					dateFormatter.setTimeZone(TimeZone.getDefault());
					DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT);
					timeFormatter.setTimeZone(TimeZone.getDefault());
					mDateDisplayString = getDateString(dateFormatter);
					mTimeDisplayString = getTimeString(timeFormatter);
					mDateTimeDisplayString = mDateDisplayString + "\n" + mTimeDisplayString;
					mPrioritySpinner.setSelection(mPriority, false);

					actionBar.setTitle("Edit Task");
					mTaskNameEditText.setText(task.getTaskName());
					mTaskDateTextView.setText(mDateTimeDisplayString);
					mTaskDateTextView.setVisibility(View.VISIBLE);
					mTaskTimeBtn.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
		}
	}

	private String getDateString(DateFormat formatter)
	{
		Date date = mCal.getTime();
		if (formatter == null)
		{
			formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
			formatter.setTimeZone(TimeZone.getDefault());
		}
		return formatter.format(date);
	}

	private String getTimeString(DateFormat formatter)
	{
		Date time = mCal.getTime();
		if (formatter == null)
		{
			formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
			formatter.setTimeZone(TimeZone.getDefault());
		}
		return formatter.format(time);
	}

	private void saveProduct()
	{
		Intent saveIntent = new Intent();
		if (mDateTime <= 0 || TextUtils.isEmpty
				(mTaskNameEditText.getText()))
		{
			setResult(RESULT_CANCELED, saveIntent);
		} else
		{
			String name = mTaskNameEditText.getText().toString().trim();
			saveIntent.putExtra(EXTRA_NAME, name);
			saveIntent.putExtra(EXTRA_DATE, mDateTime);
			saveIntent.putExtra("action", mAction);
			saveIntent.putExtra("taskId", mId);
			saveIntent.putExtra("priority", mPriority);
			setResult(RESULT_OK, saveIntent);
		}
	}

	private void deleteProduct()
	{
		Intent deleteIntent = new Intent();
		mAction = "delete";
		deleteIntent.putExtra("action", mAction);
		deleteIntent.putExtra("taskId", mId);
		setResult(RESULT_OK, deleteIntent);
	}

	/*
		Menu methods
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.editor_menu_save:
				saveProduct();
				finish();
				return true;
			case R.id.editor_menu_delete:
				deleteProduct();
				finish();
				return true;
			case android.R.id.home:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*
		Interface methods
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
	{
		mDateSet = true;
		if (!mTimeSet)
		{
			mCal.clear(Calendar.HOUR_OF_DAY);
			mCal.clear(Calendar.MINUTE);
		}
		mCal.set(year, month, dayOfMonth);
		mDateTime = mCal.getTimeInMillis();
		mDateDisplayString = getDateString(null);

		mTaskDateTextView.setText(mDateDisplayString);
		if (mTaskDateTextView.getVisibility() == View.GONE)
		{
			mTaskDateTextView.setVisibility(View.VISIBLE);
		}
		if (mTaskTimeBtn.getVisibility() == View.GONE)
		{
			mTaskTimeBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		Calendar cal = Calendar.getInstance();
		Date currTime = cal.getTime();
		mTimeSet = true;
		mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCal.set(Calendar.MINUTE, minute);
		if (currTime.after(mCal.getTime()))
		{
			TimePickerFragment timePickerFragment = new TimePickerFragment();
			timePickerFragment.show(getSupportFragmentManager(), "timePicker");
		} else
		{
			mDateTime = mCal.getTimeInMillis();
			mTimeDisplayString = getTimeString(null);
			mDateTimeDisplayString = mDateDisplayString + "\n" + mTimeDisplayString;

			mTaskDateTextView.setText(mDateTimeDisplayString);
		}
	}

}

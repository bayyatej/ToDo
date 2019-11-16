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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.geterdone.android.todo.data.Task;
import com.geterdone.android.todo.data.TaskViewModel;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TaskEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //todo fix task not saving without due date
    public static final String EXTRA_NAME = "com.geterdone.android.tasklistsql.NAME";
    public static final String EXTRA_DATE = "com.geterdone.android.tasklistsql.DATE";

    private EditText mTaskNameEditText;
    private TextView mTaskDateTextView;
    private Switch mRepeatSwitch;
    private Spinner mRepeatFrequencySpinner;

    private TaskViewModel mTaskViewModel;
    private Calendar mCal;
    private String mTimeDisplayString;
    private String mDateDisplayString;
    private String mDateTimeDisplayString;
    private String mAction;
    private String mFrequency;
    private boolean mTimeSet;
    private boolean mEndDateSet = false;
    private boolean mFrequencySpinnerInitialized = false;
    private long mDateTime;
    private long mRepeatEndDateTime;
    private int mPriority;
    private int mId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        setupWidgets();
    }

    /*
        Helper methods
     */
    private void setupWidgets() {
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        mTaskNameEditText = findViewById(R.id.editor_task_name_edit_text);
        mTaskDateTextView = findViewById(R.id.editor_task_date_text_view);
        mRepeatSwitch = findViewById(R.id.task_repeat_switch);
        mRepeatFrequencySpinner = findViewById(R.id.task_repeat_frequency_spinner);
        Spinner mPrioritySpinner = findViewById(R.id.editor_task_priority_spinner);
        Button taskNotificationBtn = findViewById(R.id.task_notification_button);
        mCal = Calendar.getInstance(TimeZone.getDefault());
        mAction = intent.getStringExtra("action");
        mId = intent.getIntExtra("taskId", -1);
        Task task = mTaskViewModel.getTaskById(mId);
        ArrayAdapter<CharSequence> prioritySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.editor_task_priority_array, android.R.layout.simple_spinner_item);

        taskNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setupFrequencySpinner();
                } else {
                    mFrequency = "";
                    mRepeatEndDateTime = 0;
                    mFrequencySpinnerInitialized = false;
                }
            }
        });

        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPrioritySpinner.setAdapter(prioritySpinnerAdapter);
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriority = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriority = 0;
            }
        });

        //Set action bar title based on whether user is editing or adding a task
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            switch (mAction) {
                case "add":
                    actionBar.setTitle("Add New Task");
                    break;
                case "edit":
                    mDateTime = task.getTaskDate();
                    mPriority = task.getPriority();
                    mTimeSet = true;
                    Date dateTime = new Date(mDateTime);
                    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    dateFormatter.setTimeZone(TimeZone.getDefault());
                    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT);
                    timeFormatter.setTimeZone(TimeZone.getDefault());
                    mDateDisplayString = getDateString(dateFormatter, dateTime);
                    mTimeDisplayString = getTimeString(timeFormatter, dateTime);
                    mDateTimeDisplayString = mDateDisplayString + "\n" + mTimeDisplayString;
                    mPrioritySpinner.setSelection(mPriority, false);

                    if (!TextUtils.isEmpty(task.getRepeatFrequency())) {
                        mRepeatSwitch.setChecked(true);
                        int priorityPos = Arrays.asList(getResources().getStringArray(R.array.editor_task_frequency_array)).indexOf(task.getRepeatFrequency());
                        mPrioritySpinner.setSelection(priorityPos);
                    }

                    actionBar.setTitle("Edit Task");
                    mTaskNameEditText.setText(task.getTaskName());
                    mTaskDateTextView.setText(mDateTimeDisplayString);
                    mTaskDateTextView.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private String getDateString(DateFormat formatter, Date date) {
        if (date == null) {
            date = mCal.getTime();
        }
        if (formatter == null) {
            formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            formatter.setTimeZone(TimeZone.getDefault());
        }
        return formatter.format(date);
    }

    private String getTimeString(DateFormat formatter, Date time) {
        if (time == null) {
            time = mCal.getTime();
        }

        if (formatter == null) {
            formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
            formatter.setTimeZone(TimeZone.getDefault());
        }
        return formatter.format(time);
    }

    private void saveTask() {
        Intent saveIntent = new Intent();
        if (mDateTime <= 0 || TextUtils.isEmpty
                (mTaskNameEditText.getText())) {
            setResult(RESULT_CANCELED, saveIntent);
        } else {
            String name = mTaskNameEditText.getText().toString().trim();
            saveIntent.putExtra(EXTRA_NAME, name);
            saveIntent.putExtra(EXTRA_DATE, mDateTime);
            saveIntent.putExtra("action", mAction);
            saveIntent.putExtra("taskId", mId);
            saveIntent.putExtra("priority", mPriority);
            saveIntent.putExtra("frequency", mFrequency);
            saveIntent.putExtra("endDate", mRepeatEndDateTime);
            setResult(RESULT_OK, saveIntent);
        }
    }

    private void deleteTask() {
        Intent deleteIntent = new Intent();
        mAction = "delete";
        deleteIntent.putExtra("action", mAction);
        deleteIntent.putExtra("taskId", mId);
        setResult(RESULT_OK, deleteIntent);
    }

    private void setupFrequencySpinner() {
        mRepeatFrequencySpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> frequencySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.editor_task_frequency_array, android.R.layout.simple_spinner_item);

        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatFrequencySpinner.setAdapter(frequencySpinnerAdapter);
        mRepeatFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal = Calendar.getInstance();
                mFrequency = mRepeatFrequencySpinner.getSelectedItem().toString();
                switch (mFrequency) {
                    case "None":
                        break;
                    case "Daily":
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case "Weekly":
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case "Monthly":
                        cal.add(Calendar.MONTH, 1);
                        break;
                    case "Annually":
                        cal.add(Calendar.YEAR, 1);
                        break;
                    case "Custom":
                        //todo implement custom
                        break;
                }
                if (mFrequencySpinnerInitialized && !mFrequency.equals("None")) {
                    mEndDateSet = true;
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setCalendar(cal);
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
                mFrequencySpinnerInitialized = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFrequency = "";
                mRepeatEndDateTime = 0;
            }
        });
    }

    /*
        Menu methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editor_menu_save:
                saveTask();
                finish();
                return true;
            case R.id.editor_menu_delete:
                deleteTask();
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (!mTimeSet) {
            mCal.clear(Calendar.HOUR_OF_DAY);
            mCal.clear(Calendar.MINUTE);
        }
        mCal.set(year, month, dayOfMonth);
        if (mEndDateSet) {
            mRepeatEndDateTime = mCal.getTimeInMillis();
        } else {
            mDateTime = mCal.getTimeInMillis();
            mDateDisplayString = getDateString(null, null);
            mTaskDateTextView.setText(mDateDisplayString);
            if (mTaskDateTextView.getVisibility() == View.GONE) {
                mTaskDateTextView.setVisibility(View.VISIBLE);
            }
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        Date currTime = cal.getTime();
        mTimeSet = true;
        mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCal.set(Calendar.MINUTE, minute);
        if (currTime.after(mCal.getTime())) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");
        } else {
            mDateTime = mCal.getTimeInMillis();
            mDateTime = mDateTime - mDateTime % 60000; // rounds down to the nearest minute
            mTimeDisplayString = getTimeString(null, null);
            mDateTimeDisplayString = mDateDisplayString + "\n" + mTimeDisplayString;

            mTaskDateTextView.setText(mDateTimeDisplayString);
        }
        if (mRepeatSwitch.getVisibility() == View.GONE) {
            mRepeatSwitch.setVisibility(View.VISIBLE);
        }
    }
}

package com.geterdone.android.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class TaskEditorActivity extends AppCompatActivity
{
	public static final String EXTRA_NAME = "com.geterdone.android.tasklistsql.NAME";
	public static final String EXTRA_DATE = "com.geterdone.android.tasklistsql.DATE";
	private EditText mTaskNameEditText;
	private EditText mTaskDateEditText;
	private String mAction;
	private int mId;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		ActionBar actionBar = getSupportActionBar();
		Intent intent = getIntent();
		mTaskNameEditText = findViewById(R.id.task_name_edit_text);
		mTaskDateEditText = findViewById(R.id.task_date_edit_text);
		mAction = intent.getStringExtra("action");
		mId = intent.getIntExtra("taskId", -1);

		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			switch (mAction)
			{
				case "add":
					actionBar.setTitle("Add New Task");
					break;
				case "edit":
					actionBar.setTitle("Edit Task");
					mTaskNameEditText.setText(intent.getStringExtra("name"));
					mTaskDateEditText.setText(intent.getStringExtra("date"));
					break;
				default:
					break;
			}
		}
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
				Intent saveIntent = new Intent();
				if (TextUtils.isEmpty(mTaskDateEditText.getText()) || TextUtils.isEmpty
						(mTaskNameEditText.getText()))
				{
					setResult(RESULT_CANCELED, saveIntent);
				} else
				{
					String name = mTaskNameEditText.getText().toString().trim();
					String date = mTaskDateEditText.getText().toString().trim();
					saveIntent.putExtra(EXTRA_NAME, name);
					saveIntent.putExtra(EXTRA_DATE, date);
					saveIntent.putExtra("action", mAction);
					saveIntent.putExtra("taskId", mId);
					setResult(RESULT_OK, saveIntent);
				}
				finish();
				return true;
			case R.id.editor_menu_delete:
				Intent deleteIntent = new Intent();
				mAction = "delete";
				deleteIntent.putExtra("action", mAction);
				deleteIntent.putExtra("taskId", mId);
				setResult(RESULT_OK, deleteIntent);
				finish();
				return true;
			case android.R.id.home:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

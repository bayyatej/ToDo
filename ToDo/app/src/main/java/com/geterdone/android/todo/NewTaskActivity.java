package com.geterdone.android.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTaskActivity extends AppCompatActivity
{
	public static final String EXTRA_NAME = "com.geterdone.android.tasklistsql.NAME";
	public static final String EXTRA_DATE = "com.geterdone.android.tasklistsql.DATE";
	private EditText mTaskNameEditText,
			mTaskDateEditText;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
		{
			actionBar.setTitle("Add New Task");
		}
		mTaskNameEditText = findViewById(R.id.task_name_edit_text);
		mTaskDateEditText = findViewById(R.id.task_date_edit_text);

		final Button button = findViewById(R.id.button_save);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
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
					setResult(RESULT_OK, saveIntent);
				}
				finish();
			}
		});
	}
}

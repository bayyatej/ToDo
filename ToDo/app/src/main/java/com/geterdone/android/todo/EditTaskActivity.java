package com.geterdone.android.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class EditTaskActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
		{
			actionBar.setTitle("Edit Task");
		}

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String date = intent.getStringExtra("date");

		EditText nameEditText = findViewById(R.id.task_name_edit_text);
		EditText dateEditText = findViewById(R.id.task_date_edit_text);

		nameEditText.setText(name);
		dateEditText.setText(date);
	}
}

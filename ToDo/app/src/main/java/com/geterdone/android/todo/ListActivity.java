package com.geterdone.android.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ListActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		//todo initialize adapter

		// improves performance if changes in content do not change size. may have to remove
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(linearLayoutManager);
		//todo set adapter
	}

	/*
		Options menu methods
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.settings_menu_item:
				//Launch Settings Activity
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

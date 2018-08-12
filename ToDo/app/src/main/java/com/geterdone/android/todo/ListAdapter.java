package com.geterdone.android.todo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{
	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		public TextView mTaskNameTextView,
				mTaskDateTextView;

		public ViewHolder(View itemView)
		{
			super(itemView);
			mTaskNameTextView = itemView.findViewById(R.id.task_name_text_view);
			mTaskDateTextView = itemView.findViewById(R.id.task_date_text_view);
		}
	}

	/*
		todo impement below methods
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{

	}

	@Override
	public int getItemCount()
	{
		return 0;
	}
}

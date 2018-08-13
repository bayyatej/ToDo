package com.geterdone.android.todo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geterdone.android.todo.data.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>
{
	class TaskViewHolder extends RecyclerView.ViewHolder
	{
		private final TextView taskNameTextView;
		private final TextView taskDateTextView;

		private TaskViewHolder(View itemView)
		{
			super(itemView);
			taskNameTextView = itemView.findViewById(R.id.task_name_text_view);
			taskDateTextView = itemView.findViewById(R.id.task_date_text_view);
			itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(mContext, NewTaskActivity.class);
					mContext.startActivity(intent);
				}
			});
		}
	}

	private final LayoutInflater mInflater;
	private List<Task> mTasks;
	private final Context mContext;

	TaskListAdapter(Context context)
	{
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View itemView = mInflater.inflate(R.layout.task_list_item, parent, false);
		return new TaskViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull TaskViewHolder holder, int position)
	{
		if (mTasks != null)
		{
			Task current = mTasks.get(position);
			holder.taskNameTextView.setText(current.getTaskName());
			holder.taskDateTextView.setText(current.getTaskDate());
		} else
		{
			holder.taskDateTextView.setText("No Task");
		}
	}

	void setTasks(List<Task> tasks)
	{
		mTasks = tasks;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		if (mTasks != null)
		{
			return mTasks.size();
		} else
		{
			return 0;
		}
	}
}

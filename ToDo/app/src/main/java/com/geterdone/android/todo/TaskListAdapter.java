package com.geterdone.android.todo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geterdone.android.todo.data.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>
{
	class TaskViewHolder extends RecyclerView.ViewHolder
	{
		private final LinearLayout mTaskContainer;
		private final LinearLayout mTaskDetails;
		private final ImageView mTaskDoneView;
		private final TextView mTaskNameTextView;
		private final TextView mTaskDateTextView;

		private TaskViewHolder(View itemView)
		{
			super(itemView);
			mTaskNameTextView = itemView.findViewById(R.id.task_name_text_view);
			mTaskDateTextView = itemView.findViewById(R.id.task_date_text_view);
			mTaskContainer = itemView.findViewById(R.id.task_container);
			mTaskDetails = itemView.findViewById(R.id.task_details);
			mTaskDoneView = itemView.findViewById(R.id.task_complete);
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

	@NonNull
	@Override
	public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View itemView = mInflater.inflate(R.layout.task_list_item, parent, false);
		return new TaskViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position)
	{
		if (mTasks != null)
		{
			final Task current = mTasks.get(position);
			long dateTime = current.getTaskDate();
			int priority = current.getPriority();
			Date date = new Date(dateTime);
			DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
			dateTimeFormatter.setTimeZone(TimeZone.getDefault());
			String formattedDateString = dateTimeFormatter.format(date);
			holder.mTaskNameTextView.setText(current.getTaskName());
			holder.mTaskDateTextView.setText(formattedDateString);
			final int id = current.getId();

			holder.mTaskDetails.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(mContext, TaskEditorActivity.class);
					intent.putExtra("action", "edit");
					intent.putExtra("taskId", id);
					((Activity) mContext).startActivityForResult(intent, MainActivity.TASK_EDITOR_ACTIVITY_REQUEST_CODE);
				}
			});
			holder.mTaskDoneView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					MainActivity.mTaskViewModel.delete(current);
					mTasks.remove(position);
					notifyItemRemoved(position);
					Intent intent = new Intent(mContext, TaskNotificationPublisher.class);
					intent.putExtra("name", current.getTaskName());
					PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
					pendingIntent.cancel();
				}
			});

			switch (priority)
			{
				case 0:
					holder.mTaskContainer.setBackgroundColor(holder.mTaskContainer.getContext().getResources().getColor(R.color.lowPriority));
					break;
				case 1:
					holder.mTaskContainer.setBackgroundColor(holder.mTaskContainer.getContext().getResources().getColor(R.color.mediumPriority));
					break;
				case 2:
					holder.mTaskContainer.setBackgroundColor(holder.mTaskContainer.getContext().getResources().getColor(R.color.highPriority));
					break;
			}
		} else
		{
			holder.mTaskDateTextView.setText(R.string.add_a_task_when_empty);
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

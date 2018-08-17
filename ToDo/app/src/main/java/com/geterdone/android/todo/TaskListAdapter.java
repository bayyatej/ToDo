package com.geterdone.android.todo;

import android.app.Activity;
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
		private int mPos;

		private TaskViewHolder(View itemView)
		{
			super(itemView);
			mTaskNameTextView = itemView.findViewById(R.id.task_name_text_view);
			mTaskDateTextView = itemView.findViewById(R.id.task_date_text_view);
			mTaskContainer = itemView.findViewById(R.id.task_container);
			mTaskDetails = itemView.findViewById(R.id.task_details);
			mTaskDoneView = itemView.findViewById(R.id.task_complete);

			mTaskDetails.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(mContext, TaskEditorActivity.class);
					intent.putExtra("action", "edit");
					intent.putExtra("taskId", mPos);
					((Activity) mContext).startActivityForResult(intent, MainActivity.TASK_EDITOR_ACTIVITY_REQUEST_CODE);
				}
			});
			mTaskDoneView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					mTasks.remove(mPos - 1);
					notifyItemRemoved(mPos - 1);
					notifyItemRangeChanged(mPos - 1, mTasks.size());
				}
			});
		}

		private void setPos(int pos)
		{
			mPos = pos;
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
	public void onBindViewHolder(@NonNull TaskViewHolder holder, int position)
	{
		if (mTasks != null)
		{
			Task current = mTasks.get(position);
			long dateTime = current.getTaskDate();
			int priority = current.getPriority();
			Date date = new Date(dateTime);
			DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
			dateTimeFormatter.setTimeZone(TimeZone.getDefault());
			String formattedDateString = dateTimeFormatter.format(date);
			holder.mTaskNameTextView.setText(current.getTaskName());
			holder.mTaskDateTextView.setText(formattedDateString);
			holder.setPos(current.getId());

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

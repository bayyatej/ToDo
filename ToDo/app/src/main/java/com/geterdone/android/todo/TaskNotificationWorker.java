package com.geterdone.android.todo;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;

public class TaskNotificationWorker extends Worker
{
	@Override
	public Result doWork()
	{
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), MainActivity.CHANNEL_ID);
		builder.setSmallIcon(R.drawable.ic_check_black_24dp);
		builder.setContentTitle("ToDo");
		builder.setContentText("You have a task due!");
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);
		builder.setContentIntent(pendingIntent);
		builder.setAutoCancel(true);
		notificationManagerCompat.notify(0, builder.build());
		return Result.SUCCESS;
	}
}

package com.geterdone.android.todo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TaskNotificationPublisher extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent mainActivityIntent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID);
		builder.setSmallIcon(R.drawable.ic_check_white_24dp);
		builder.setContentTitle("ToDo");
		builder.setContentText("Reminder: " + intent.getStringExtra("name") + " is due!");
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);
		builder.setContentIntent(pendingIntent);
		builder.setAutoCancel(true);
		notificationManagerCompat.notify(0, builder.build());
	}


}

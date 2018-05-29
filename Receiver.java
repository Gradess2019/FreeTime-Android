package com.example.testapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.testapp.schedule.ScheduleItem;
import com.example.testapp.main.TaskItem;

import java.util.ArrayList;
import java.util.Calendar;


public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ItemInfo item = null;
        String itemClass = intent.getStringExtra("class");
        int arrayId = intent.getIntExtra("array_id", -1);
        int id = intent.getIntExtra("id", -1);
        String ticker = "Не реализовано";
        ArrayList<TaskItem> taskItems;
        ArrayList<ScheduleItem> scheduleItems;
        if (itemClass.equals("task")) {
            taskItems = FileHandler.loadTasksData(context);
            if (taskItems != null && !taskItems.isEmpty() && arrayId < taskItems.size()) {
                item = taskItems.get(arrayId);
                ticker = "Напоминание о задаче";
            }
        } else if (itemClass.equals("schedule_fragment")) {
            scheduleItems = FileHandler.loadSchedulesData(context);
            Log.d("myLogs", "HELLLOOOO");
            if (scheduleItems != null && !scheduleItems.isEmpty() && arrayId < scheduleItems.size()) {
                item = scheduleItems.get(arrayId);
                ticker = "Напоминание о расписании";
            }

        }

        Calendar calendar = Calendar.getInstance();

        if (item != null) {
            byte deltaHour = (byte) (TimeHandler.getHour(item.getTime()) - calendar.get(Calendar.HOUR_OF_DAY));
            byte deltaMinutes = (byte) (TimeHandler.getMinutes(item.getTime()) - calendar.get(Calendar.MINUTE));
            if (deltaHour == 0 && Math.abs(deltaMinutes) < 2) {
                PendingIntent appIntent = PendingIntent.getActivity(context,
                        0,
                        new Intent(context, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "None")
                        .setSmallIcon(R.drawable.clock)
                        .setContentIntent(appIntent)
                        .setContentTitle(item.getName())
                        .setContentText(item.getTime())
                        .setTicker(ticker)
                        .setAutoCancel(true);

                Notification notification = builder.build();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                //noinspection ConstantConditions
                notificationManager.notify(id, notification);
            }

        }


    }
}

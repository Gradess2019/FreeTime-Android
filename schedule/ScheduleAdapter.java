package com.example.testapp.schedule;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testapp.FileHandler;
import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.Receiver;

import java.io.File;
import java.util.ArrayList;

class ScheduleAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<ScheduleItem> items;
    private File fileSave;
    private MainActivity mainActivity;

    ScheduleAdapter(Activity activity, ArrayList<ScheduleItem> items, File fileSave) {
        this.items = items;
        this.activity = activity;
        this.fileSave = fileSave;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ScheduleItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.schedule_item, viewGroup, false);
        }
        final FloatingActionButton fabAddSchedule = activity.findViewById(R.id.fabAddSchedule);
        final ScheduleItem item = getItem(i);
        if (i == getCount() - 1 && !getItem(i).getTimeEnd().equals("00:00")) {
            view.findViewById(R.id.bAddSchedule).setVisibility(View.VISIBLE);
            fabAddSchedule.setVisibility(View.VISIBLE);
        } else if (getItem(i).getTimeEnd().equals("00:00")) {
            fabAddSchedule.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.bAddSchedule).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.bAddSchedule).setVisibility(View.GONE);
        }

        view.findViewById(R.id.bAddSchedule).setOnClickListener(this);
        view.findViewById(R.id.bRemoveSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCount() == 1) {
                    fabAddSchedule.setVisibility(View.VISIBLE);
                }
                ScheduleHandler.removeItem(ScheduleAdapter.this.items, i, activity, fileSave);
                AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);

                PendingIntent intentForDestroyAlarm = PendingIntent.getBroadcast(activity,
                        i + CreateScheduleDialog.INCREMENT_ID,
                        new Intent(mainActivity, Receiver.class),
                        PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(intentForDestroyAlarm);
                ScheduleAdapter.this.notifyDataSetChanged();
            }
        });

        ((TextView) view.findViewById(R.id.tvScheduleName)).setText(item.getName());
        ((TextView) view.findViewById(R.id.tvTime)).setText(item.getTime());
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bAddSchedule: {
                CreateScheduleDialog createScheduleDialog = new CreateScheduleDialog();
                createScheduleDialog.setItemsAndAdapter(items, this, mainActivity);
                createScheduleDialog.show(activity.getFragmentManager(), "Add Schedule");
            }
        }
    }

    public void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

}

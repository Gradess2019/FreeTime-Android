package com.example.testapp.main;

import android.app.Activity;
import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testapp.R;

import java.util.ArrayList;


public class TaskAdapter extends BaseAdapter {
    private ArrayList<TaskItem> items;
    private Activity activity;

    TaskAdapter(Activity activity, ArrayList<TaskItem> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.task_item, viewGroup, false);
        }
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvTime = view.findViewById(R.id.tvTaskTimeInfo);
        TextView tvDate = view.findViewById(R.id.tvTaskDateInfo);
        TaskItem item = (TaskItem) getItem(i);

        tvName.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/CaptionFont.ttf"));

        tvName.setText(item.getName());
        tvTime.setText(item.getTime());
        tvDate.setText(item.getDate());
        return view;
    }
}

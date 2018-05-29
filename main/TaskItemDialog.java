package com.example.testapp.main;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapp.R;

public class TaskItemDialog extends DialogFragment {
    TextView tvDate;
    TextView tvTime;

    String dateInfo;
    String timeInfo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_info_task, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        tvDate = v.findViewById(R.id.tvDateInfo);
        tvTime = v.findViewById(R.id.tvTimeInfo);

        tvDate.setText(dateInfo);
        tvTime.setText(timeInfo);

        Button bClose = v.findViewById(R.id.bClose);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItemDialog.this.dismiss();
            }
        });
        return v;
    }

    public void initialize(TaskItem item) {
        dateInfo = item.getDate();
        timeInfo = item.getTime();
    }
}


package com.example.testapp.schedule;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapp.R;

public class ShowInfoScheduleDialog extends DialogFragment {

    private TextView tvNameSchedule;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private Button bClose;

    private String name;
    private String timeStart;
    private String timeEnd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info_schedule, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        tvNameSchedule = view.findViewById(R.id.tvScheduleTitle);
        tvTimeStart = view.findViewById(R.id.tvScheduleInfoTimeStart);
        tvTimeEnd = view.findViewById(R.id.tvScheduleInfoTimeEnd);
        bClose = view.findViewById(R.id.bCloseScheduleInfo);

        tvNameSchedule.setText(name);
        tvTimeStart.setText(timeStart);
        tvTimeEnd.setText(timeEnd);

        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowInfoScheduleDialog.this.dismiss();
            }
        });

        return view;
    }

    public void initialize(ScheduleItem item) {
        name = item.getName();
        timeStart = item.getTimeStart();
        timeEnd = item.getTimeEnd();
    }
}

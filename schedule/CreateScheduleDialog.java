package com.example.testapp.schedule;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.EventListener;
import com.example.testapp.FileHandler;
import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.Receiver;
import com.example.testapp.SettingsFragment;
import com.example.testapp.TimeHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateScheduleDialog extends DialogFragment
        implements View.OnClickListener, EventListener {

    private ArrayList<ScheduleItem> items;
    private ScheduleAdapter adapter;
    private EditText etName;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private final String FILE_NAME = "Schedule.json";
    private File fileSave;

    private AlarmManager alarmManager;
    private MainActivity mainActivity;
    public static final int INCREMENT_ID = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_schedule, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etName = view.findViewById(R.id.etNameSchedule);
        tvTimeStart = view.findViewById(R.id.tvTimeStartSchedule);
        tvTimeEnd = view.findViewById(R.id.tvTimeEndSchedule);


        tvTimeStart.setOnClickListener(this);
        tvTimeEnd.setOnClickListener(this);
        (view.findViewById(R.id.bCancelScheduleDialog)).setOnClickListener(this);
        (view.findViewById(R.id.bAddScheduleDialog)).setOnClickListener(this);

        if (!items.isEmpty()) {
            tvTimeStart.setText((items.get(items.size() - 1)).getTimeEnd());
        }
        fileSave = new File(getActivity().getFilesDir(), FILE_NAME);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bCancelScheduleDialog: {
                CreateScheduleDialog.this.dismiss();
                break;
            }
            case R.id.bAddScheduleDialog: {
                if (etName.getText().toString().equals("")) {
                    showToast("Введите название!", Toast.LENGTH_SHORT);
                } else if (!TimeHandler.isCorrectTime(tvTimeStart.getText().toString()) ||
                        !TimeHandler.isCorrectTime(tvTimeEnd.getText().toString())) {
                    showToast("Установите время!", Toast.LENGTH_SHORT);
                } else if (!TimeHandler.isValidTime(tvTimeStart.getText().toString(),
                        tvTimeEnd.getText().toString())) {
                    showToast("Некорректный диапазон времени!", Toast.LENGTH_SHORT);
                } else {
                    ScheduleHandler.addNewItem(items,
                            etName.getText().toString(),
                            tvTimeStart.getText().toString(),
                            tvTimeEnd.getText().toString(),
                            fileSave);

                    adapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                            FileHandler.STATES_PREFERENCES_NAME,
                            Context.MODE_PRIVATE
                    );

                    boolean notificationsEnabled = sharedPreferences.getBoolean(SettingsFragment.SWITCH_STATE_NAME, false);
                    if (sharedPreferences.contains(SettingsFragment.SWITCH_STATE_NAME)
                            && notificationsEnabled) {
                        createNewAlarm(items.get(items.size() - 1));
                    }
                    CreateScheduleDialog.this.dismiss();
                }
                break;
            }
            case R.id.tvTimeStartSchedule: {
                TimeHandler.showTime(getActivity(), tvTimeStart);
                break;
            }
            case R.id.tvTimeEndSchedule: {
                TimeHandler.showTime(getActivity(), tvTimeEnd);
                break;
            }
        }
    }


    public void setItemsAndAdapter(ArrayList<ScheduleItem> items, ScheduleAdapter adapter, MainActivity mainActivity) {
        this.items = items;
        this.adapter = adapter;
        this.mainActivity = mainActivity;
        alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void showToast(String message, int length) {
        Toast.makeText(getActivity().getApplicationContext(), message, length).show();
    }

    private void createNewAlarm(ScheduleItem item) {
        Intent intent = new Intent(mainActivity, Receiver.class);
        intent.putExtra("class", "schedule_fragment");
        intent.putExtra("array_id", items.size() - 1);
        intent.putExtra("id", (items.size() - 1 + INCREMENT_ID));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity, (items.size() - 1 + INCREMENT_ID),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                TimeHandler.getHour(item.getTimeStart()),
                TimeHandler.getMinutes(item.getTimeStart()),
                0);

        Log.d("myLogs", calendar.get(Calendar.YEAR) + " "
                + calendar.get(Calendar.MONTH) + " "
                + calendar.get(Calendar.DATE) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + " "
                + calendar.get(Calendar.MINUTE) + " ");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}

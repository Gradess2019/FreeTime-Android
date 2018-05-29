package com.example.testapp.main;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.EventListener;
import com.example.testapp.FileHandler;
import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.Receiver;
import com.example.testapp.SettingsFragment;
import com.example.testapp.TimeHandler;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Класс, выполняющий добавление новых задач
 */
public class TaskBuilder extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener, EventListener {

    /**
     * Поле для установки даты для задачи
     */
    private TextView tvDate;

    /**
     * Поле для установки времени для задачи
     */
    private TextView tvTime;

    /**
     * Поле для установки имени задачи
     */
    private EditText etTaskName;

    /**
     * Адаптер для вывода задач в ListView
     */
    private TaskAdapter taskAdapter;

    /**
     * Поле для хранения задач
     */
    private ArrayList<TaskItem> items;

    /**
     * Поле для вывода задач на экран
     */
    private ListView listViewTasks;

    /**
     * Поле имени JSON-файла, который хранит задачи
     */
    private final String FILE_NAME = "Tasks.json";

    /**
     * Поле файла, который хранит задачи
     */
    private File fileSave;

    /**
     * Поле родительского Активити
     */
    private Activity activity;

    private MainActivity mainActivity;

    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "myLogs";

    private static ArrayList<PendingIntent> alarmIntents = new ArrayList<>();
    private AlarmManager alarmManager;


    /**
     * Вызывается при создании View и инициализирует все View-компоненты
     *
     * @return View объект GUI
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_task, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        tvDate = v.findViewById(R.id.tvDateInfo);
        tvTime = v.findViewById(R.id.tvTimeInfo);
        Button bCancel = v.findViewById(R.id.bCancel);
        Button bAdd = v.findViewById(R.id.bAdd);
        etTaskName = v.findViewById(R.id.etTaskName);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bAdd.setOnClickListener(this);
        return v;
    }

    /**
     * Вызывается при вызове метода dismiss() (закрытие диалога)
     * Устанавливает значения GUI по умолчанию
     */
    @Override
    public void onStop() {
        super.onStop();
        etTaskName.setText("");
        tvDate.setText(R.string.tvSetDate);
        tvTime.setText(R.string.tvSetTime);
    }

    /**
     * Вызывается при нажатии на Button "Готово" {@link TaskBuilder#onClick(View)}
     * Добавляет новую задачу в ArrayList {@link TaskBuilder#items} и
     * сохраняет данные в JSON-файл {@link TaskBuilder#fileSave}
     */
    private void addTask() {
        items.add(new TaskItem(etTaskName.getText().toString(),
                tvDate.getText().toString(),
                tvTime.getText().toString()));
        try (Writer writer = new FileWriter(fileSave)) {
            new GsonBuilder().create().toJson(items, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                FileHandler.STATES_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );

        boolean notificationsEnabled = sharedPreferences.getBoolean(SettingsFragment.SWITCH_STATE_NAME, false);
        if (sharedPreferences.contains(SettingsFragment.SWITCH_STATE_NAME)
                && notificationsEnabled) {
            createNewAlarm();
        }
        taskAdapter.notifyDataSetChanged();
    }

    private void createNewAlarm() {

        Intent intent = new Intent(mainActivity, Receiver.class);
        intent.putExtra("class", "task");
        intent.putExtra("array_id", items.size() - 1);
        intent.putExtra("id", (int) (items.size() + (Math.random() * 100000)));

        PendingIntent contentIntent = PendingIntent.getBroadcast(mainActivity, (int) (items.size() + (Math.random() * 100000)),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmIntents.add(contentIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(TimeHandler.getYear(items.get(items.size() - 1).getDate()),
                TimeHandler.getMonth(items.get(items.size() - 1).getDate()) - 1,
                TimeHandler.getDay(items.get(items.size() - 1).getDate()),
                TimeHandler.getHour(items.get(items.size() - 1).getTime()),
                TimeHandler.getMinutes(items.get(items.size() - 1).getTime()),
                0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);
    }

    /**
     * Если JSON-файл {@link TaskBuilder#fileSave} существует, то метод производит загрузку
     * сохранения в ArrayList {@link TaskBuilder#items}, иначе создаёт новый файл.
     */
    private void loadTasks() {
        if (fileSave.exists()) {
            Type listType = new TypeToken<ArrayList<TaskItem>>() {
            }.getType();
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileSave))) {
                items = new GsonBuilder().create().fromJson(inputStreamReader, listType);
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            createNewSaveFile();
        }
        resetAdapter();
    }

    /**
     * Сбрасывает старый Адаптер {@link TaskBuilder#taskAdapter} и назначает новый на
     * ListView {@link TaskBuilder#listViewTasks}
     */
    private void resetAdapter() {
        if (items == null)
            items = new ArrayList<>();
        taskAdapter = new TaskAdapter(activity, items);
        listViewTasks.setAdapter(taskAdapter);
    }

    /**
     * Создаёт новый JSON-файл {@link TaskBuilder#fileSave}
     */
    private void createNewSaveFile() {
        try {
            Log.d(TAG, "File created: " + fileSave.createNewFile());
            Log.d(TAG, "File writable: " + fileSave.setWritable(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //для дебага
    public void removeSave() {
        activity.deleteFile(FILE_NAME);
        items = new ArrayList<>();
        createNewSaveFile();
        resetAdapter();
        for (PendingIntent intent : alarmIntents) {
            alarmManager.cancel(intent);
        }
    }


    /**
     * Инициализирует класс TaskBuilder
     *
     * @param activity      Активити родителя
     * @param listViewTasks ListView, который выводит на экран задачи
     */
    public void initialize(Activity activity, ListView listViewTasks, MainActivity mainActivity) {
        this.listViewTasks = listViewTasks;
        this.activity = activity;
        this.listViewTasks.setOnItemClickListener(this);
        this.mainActivity = mainActivity;
        alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        fileSave = new File(activity.getFilesDir(), FILE_NAME);
        loadTasks();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDateInfo: {
                TimeHandler.showCalendar(getActivity(), tvDate);
                break;
            }
            case R.id.tvTimeInfo: {
                TimeHandler.showTime(getActivity(), tvTime);
                break;
            }
            case R.id.bAdd: {
                if (etTaskName.getText().toString().equals(""))
                    showToast("Введите название задачи!", Toast.LENGTH_SHORT);
                else if (tvDate.getText().toString().equals("Установить дату"))
                    showToast("Установите дату!", Toast.LENGTH_SHORT);
                else if (tvTime.getText().toString().equals("Установить время"))
                    showToast("Установите время!", Toast.LENGTH_SHORT);
                else {
                    addTask();
                    TaskBuilder.super.dismiss();
                }
                break;
            }

            case R.id.bClose: {
                dismiss();
                break;
            }

            case R.id.bCancel: {
                TaskBuilder.super.dismiss();
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TaskItemDialog taskItemDialog = new TaskItemDialog();
        taskItemDialog.initialize((TaskItem) adapterView.getAdapter().getItem(position));
        taskItemDialog.show(activity.getFragmentManager(), "ShowTask");
    }

    @Override
    public void showToast(String message, int length) {
        Toast.makeText(getActivity().getApplicationContext(), message, length).show();
    }
}

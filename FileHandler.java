package com.example.testapp;

import android.content.Context;

import com.example.testapp.schedule.ScheduleItem;
import com.example.testapp.main.TaskItem;
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

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileHandler {

    private static final String TASKS_FILE_NAME = "Tasks.json";
    private static final String SCHEDULES_FILE_NAME = "Schedule.json";
    private static final String INTENTS_FILE_NAME = "Intents.json";
    public static final String STATES_PREFERENCES_NAME = "states";

    public static ArrayList<TaskItem> loadTasksData(Context context) {
        Type taskType = new TypeToken<ArrayList<TaskItem>>() {
        }.getType();
        ArrayList<TaskItem> taskItems = new ArrayList<>();
        File tasksFile = new File(context.getFilesDir(), TASKS_FILE_NAME);
        if (tasksFile.exists()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(tasksFile))) {
                taskItems = new GsonBuilder().create().fromJson(inputStreamReader, taskType);
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                tasksFile.createNewFile();
                tasksFile.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return taskItems;
    }

    public static ArrayList<ItemInfo> loadData(Context context, String fileName) {
        Type taskType = new TypeToken<ArrayList<ItemInfo>>() {
        }.getType();
        File file = new File(context.getFilesDir(), fileName);
        ArrayList<ItemInfo> items = new ArrayList<>();
        if (file.exists()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file))) {
                items = new GsonBuilder().create().fromJson(inputStreamReader, taskType);
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public static ArrayList<ScheduleItem> loadSchedulesData(Context context) {
        Type scheduleType = new TypeToken<ArrayList<ScheduleItem>>() {
        }.getType();
        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();
        File schedulesFile = new File(context.getFilesDir(), SCHEDULES_FILE_NAME);
        if (schedulesFile.exists()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(schedulesFile))) {
                scheduleItems = new GsonBuilder().create().fromJson(inputStreamReader, scheduleType);
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                schedulesFile.createNewFile();
                schedulesFile.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return scheduleItems;
    }

    public static void saveTasksData(Context context, ArrayList<TaskItem> items) {
        File fileSave = new File(context.getFilesDir(), TASKS_FILE_NAME);
        if (!fileSave.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                fileSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (Writer writer = new FileWriter(fileSave)) {
            new GsonBuilder().create().toJson(items, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSchedulesData(Context context, ArrayList<ScheduleItem> items) {
        File fileSave = new File(context.getFilesDir(), SCHEDULES_FILE_NAME);
        if (!fileSave.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                fileSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (Writer writer = new FileWriter(fileSave)) {
            new GsonBuilder().create().toJson(items, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getSchedulesFile(Context context) {
        return new File(context.getFilesDir(), SCHEDULES_FILE_NAME);
    }
}

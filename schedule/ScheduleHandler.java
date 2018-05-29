package com.example.testapp.schedule;

import android.app.Activity;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class ScheduleHandler {
    public static void addNewItem(ArrayList<ScheduleItem> items, String name,
                                  String timeStart, String timeEnd, File fileSave) {
        items.add(new ScheduleItem(name,timeStart,timeEnd));
        save(items, fileSave);
    }

    public static void clearSaveFile(Activity activity, File fileSave) {
        String name = fileSave.getName();
        activity.deleteFile(fileSave.getName());
        fileSave = new File(activity.getFilesDir(), name);
    }

    public static void removeItem(ArrayList<ScheduleItem> items, Activity activity, File fileSave) {
        clearSaveFile(activity, fileSave);
        save(items, fileSave);
    }

    public static void removeItem(ArrayList<ScheduleItem> items, int index,
                                  Activity activity, File fileSave) {
        clearSaveFile(activity, fileSave);
        items.remove(index);
        save(items, fileSave);
    }

    public static void save(ArrayList<ScheduleItem> items, File fileSave) {
        try (Writer writer = new FileWriter(fileSave)) {
            new GsonBuilder().create().toJson(items, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

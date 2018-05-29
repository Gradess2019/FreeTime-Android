package com.example.testapp.main;

import com.example.testapp.ItemInfo;

public class TaskItem extends ItemInfo {

    private String date;

    TaskItem(String name, String date, String time) {
        super(name, time);
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}

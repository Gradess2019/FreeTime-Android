package com.example.testapp;

public class ItemInfo{
    private String name;
    private String time;
//    private int priority;

    protected ItemInfo(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}

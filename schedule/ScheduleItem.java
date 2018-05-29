package com.example.testapp.schedule;

import com.example.testapp.ItemInfo;

public class ScheduleItem extends ItemInfo {
    private String timeStart;
    private String timeEnd;
    ScheduleItem(String name, String timeStart, String timeEnd) {
        super(name, timeStart + " - " + timeEnd);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}

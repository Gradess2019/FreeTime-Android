package com.example.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeHandler {

    /**
     * Вызывается при нажатии на TextView "Установить дату"
     * Выводит на экран DatePicker для выбора даты
     */
    public static void showCalendar(Activity activity, final TextView tvDate) {
        final DatePicker datePicker = new DatePicker(activity);
        datePicker.setCalendarViewShown(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvDate.setText(getStringDate(datePicker));
                dialogInterface.dismiss();
            }
        });
        builder.setView(datePicker);
        builder.create().show();
    }

    /**
     * @param datePicker Объект, из которого получаем выбранную дату
     * @return Возвращаем дату в виде строки
     */
    private static String getStringDate(DatePicker datePicker) {
        String result = ((datePicker.getDayOfMonth() < 10) ?
                "0" + datePicker.getDayOfMonth() : String.valueOf(datePicker.getDayOfMonth())) + "/";
        //Прибавляем 1, т.к. месяца индексируются от 0 до 11
        result += ((datePicker.getMonth() + 1 < 10) ?
                "0" + (datePicker.getMonth() + 1) : String.valueOf(datePicker.getMonth() + 1)) + "/";
        return result + datePicker.getYear();
    }

    /**
     * Вызывается при нажатии на TextView "Установить время"
     * Выводит на экран TimePicker для выбора времени
     */
    public static void showTime(Activity activity, final TextView tvTime) {
        final TimePicker timePicker = new TimePicker(activity);
        timePicker.setIs24HourView(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvTime.setText(getStringTime(timePicker));
                dialogInterface.dismiss();
            }
        });
        builder.setView(timePicker);
        builder.create().show();
    }

    /**
     * @param timePicker Объект, из которого получаем выбранное время
     * @return Вовзращаем время в виде строки
     */
    private static String getStringTime(TimePicker timePicker) {
        return ((timePicker.getCurrentHour() < 10) ? "0" + timePicker.getCurrentHour() :
                String.valueOf(timePicker.getCurrentHour())) + ":" +
                ((timePicker.getCurrentMinute() < 10) ? "0" + timePicker.getCurrentMinute() :
                        String.valueOf(timePicker.getCurrentMinute()));
    }

    public static boolean isValidTime(String startTime, String endTime) {
        int subStartHour = getHour(startTime);
        int subStartMinutes = getMinutes(startTime);
        int subEndHour = getHour(endTime);
        int subEndMinutes = getMinutes(endTime);

        return subEndHour == 0 && subEndMinutes == 0 ||
                (subStartHour <= subEndHour &&
                        (subStartHour != subEndHour || subStartMinutes <= subEndMinutes));
    }

    public static boolean isCorrectTime(String time) {
        if (time.equals("Установить время")) {
            return false;
        } else {
            return true;
        }
    }

    public static int getHour(String time) {
        return Integer.parseInt(time.substring(0, 2));
    }

    public static int getMinutes(String time) {
        return Integer.parseInt(time.substring(3, 5));
    }

    public static int getYear(String date) {
        return Integer.parseInt(date.substring(6, 10));
    }

    public static int getMonth(String date) {
        return Integer.parseInt(date.substring(3, 5));
    }

    public static int getDay(String date) {
        return Integer.parseInt(date.substring(0, 2));
    }
}

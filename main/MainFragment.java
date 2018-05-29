package com.example.testapp.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.testapp.MainActivity;
import com.example.testapp.R;

/**
 * Класс основного фрагмента
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    /** Поле таймера*/
    private Timer timer;

    /** Билдер, выполняющий добавление новых задач*/
    private TaskBuilder dialogAddTask;

    private MainActivity mainActivity;
    //Лог
    private final String TAG = "myLogs";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Вызывается при создании View и инициализирует все View-компоненты,
     * в т.ч. таймер {@link MainFragment#timer} и билдер {@link MainFragment#dialogAddTask}
     * @return View объект GUI
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Activity activity = getActivity();
        timer = new Timer(activity, view);
        if (view != null) {
            ListView listViewTasks = view.findViewById(R.id.listTasks);
            dialogAddTask = new TaskBuilder();
            dialogAddTask.initialize(getActivity(), listViewTasks, mainActivity);

            FloatingActionButton fabAddTask = view.findViewById(R.id.fabAddTask);
            fabAddTask.setOnClickListener(this);

            //Для теста
            Button bClear = view.findViewById(R.id.bClear);
            bClear.setOnClickListener(this);
        }
        return view;
    }

    /**
     * Вызывается перед onResume() для обновления таймера {@link MainFragment#timer}
     */
    @Override
    public void onStart() {
        super.onStart();
        timer.rebuildTimer();
    }

    /**
     * Слушатель View-компонентов
     * @param view компонент, выкинувший событие клика
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddTask: {
                dialogAddTask.show(getActivity().getFragmentManager(), "AddTask");
                break;
            }
            case R.id.bClear: {
                dialogAddTask.removeSave();
                break;
            }
        }
    }

    public void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }
}



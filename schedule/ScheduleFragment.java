package com.example.testapp.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.testapp.FileHandler;
import com.example.testapp.MainActivity;
import com.example.testapp.R;

import java.util.ArrayList;


@SuppressWarnings("FieldCanBeLocal")
public class ScheduleFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<ScheduleItem> items;
    private ListView lvSchedule;
    private ScheduleAdapter adapter;
    private MainActivity mainActivity;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        items = FileHandler.loadSchedulesData(getActivity());

        if (items == null) {
            items = new ArrayList<>();
        }
        adapter = new ScheduleAdapter(getActivity(), items, FileHandler.getSchedulesFile(getActivity()));
        adapter.setMainActivity(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        FloatingActionButton fabAddSchedule = view.findViewById(R.id.fabAddSchedule);
        fabAddSchedule.setOnClickListener(this);
        lvSchedule = view.findViewById(R.id.lvSchedule);
        lvSchedule.setAdapter(adapter);
        lvSchedule.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddSchedule: {
                CreateScheduleDialog createScheduleDialog = new CreateScheduleDialog();
                createScheduleDialog.setItemsAndAdapter(items, adapter, mainActivity);
                createScheduleDialog.show(getActivity().getFragmentManager(), "Add Schedule");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ShowInfoScheduleDialog taskItemDialog = new ShowInfoScheduleDialog();
        taskItemDialog.initialize((ScheduleItem) adapterView.getAdapter().getItem(position));
        taskItemDialog.show(getActivity().getFragmentManager(), "ShowTask");
    }

    public void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }
}

package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsFragment extends Fragment {

    @SuppressWarnings("FieldCanBeLocal")
    private Switch switchNotifications;
    private static SharedPreferences sharedPreferences;
    public static final String SWITCH_STATE_NAME = "switchNotifications";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        sharedPreferences = getActivity().getSharedPreferences(
                FileHandler.STATES_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SWITCH_STATE_NAME, checked);
                editor.apply();
            }
        });
        return view;
    }
}

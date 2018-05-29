package com.example.testapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.testapp.main.MainFragment;
import com.example.testapp.schedule.ScheduleFragment;

import java.util.ArrayList;


/**
 * Главный класс всего GUI
 */
class UserInterface implements
        NavigationView.OnNavigationItemSelectedListener {

    /**
     * Поле основного layout
     */
    private DrawerLayout drawerLayout;
    /**
     * Массив для хранения фрагментов
     */
    private final ArrayList<Fragment> fragments;

    /**
     * Основное Активити
     */
    private AppCompatActivity activity;

    /**
     * Конструктор GUI
     *
     * @param activity Основное Активити
     */
    UserInterface(MainActivity activity) {
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        ((MainFragment)fragments.get(0)).setMainActivity(activity);
        fragments.add(new ScheduleFragment());
        ((ScheduleFragment)fragments.get(1)).setMainActivity(activity);
        fragments.add(new SettingsFragment());

        this.activity = activity;
        activity.setContentView(R.layout.main);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            drawerLayout = activity.findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        } else {
            throw new RuntimeException("Toolbar is null at 50 line");
        }

        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.main_tab);

        replaceFragment(fragments.get(0));
    }

    /**
     * Геттер DrawerLayout
     * @return Возвращаем текущий DrawerLayout
     */
    DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    /**
     * Заменяет текущий фрагмент
     * @param fragment Фрагмент, который будет установлен вместо текущего
     */
    private void replaceFragment(Fragment fragment) {
        activity.getSupportFragmentManager().
                beginTransaction().
                replace(R.id.content_frame, fragment).
                commitNow();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_tab: {
                replaceFragment(fragments.get(0));
                break;
            }
            case R.id.schedule: {
                replaceFragment(fragments.get(1));
                break;
            }
            case R.id.options: {
                replaceFragment(fragments.get(2));
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

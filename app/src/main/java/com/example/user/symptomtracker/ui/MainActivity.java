package com.example.user.symptomtracker.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.user.symptomtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Set up and manage navigation between destinations by Fragment transactions through
 * bottom navigation
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.dest_fragment_container)
    FrameLayout fragmentContainer;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    fragmentReplaceWithToday();
                    return true;
                case R.id.navigation_overview:
                    fragmentReplaceWithOverview();
                    return true;
                case R.id.navigation_resolved:
                    fragmentReplaceWithResolved();
                    return true;
            }

            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null){
            addDefaultFragmentToContainer();
            navigationView.setSelectedItemId(R.id.navigation_overview);
        }
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @OnClick(R.id.fab_add_new)
    public void addNewSymptom(){
        Intent intent = new Intent(MainActivity.this, AddSymptomActivity.class);
        startActivity(intent);
    }

    /**
     * Add the OverviewFragment as the default to container as the views are first initialized
     */
    private void addDefaultFragmentToContainer() {
        OverviewFragment overviewFragment = new OverviewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.dest_fragment_container, overviewFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with OverviewFragment
     */
    private void fragmentReplaceWithOverview() {
        OverviewFragment overviewFragment = new OverviewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, overviewFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with TodayFragment
     */
    private void fragmentReplaceWithToday() {
        TodayFragment todayFragment = new TodayFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, todayFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with ResolvedFragment
     */
    private void fragmentReplaceWithResolved() {
        ResolvedFragment resolvedFragment = new ResolvedFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, resolvedFragment)
                .commit();
    }

}
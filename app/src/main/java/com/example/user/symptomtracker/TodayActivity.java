package com.example.user.symptomtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.message)
    TextView textMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    textMessage.setText("TODO: Today");
                    return true;
                case R.id.navigation_overview:
                    textMessage.setText("TODO: Overview");
                    return true;
                case R.id.navigation_resolved:
                    textMessage.setText("TODO: Resolved");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        ButterKnife.bind(this);

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

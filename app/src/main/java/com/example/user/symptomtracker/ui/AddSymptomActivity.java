package com.example.user.symptomtracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.symptomtracker.R;

import butterknife.ButterKnife;

public class AddSymptomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);
        ButterKnife.bind(this);

        setTitle(R.string.title_add_new);
    }
}

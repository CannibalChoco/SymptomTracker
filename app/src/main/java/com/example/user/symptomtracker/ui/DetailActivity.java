package com.example.user.symptomtracker.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;


import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_ID = "id";
    private int symptomId;

    private AppDatabase db;
    private SymptomEntity symptom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(getApplicationContext());

        symptomId = getIntent().getIntExtra(KEY_ID, 0);
        retrieveSymptom();


    }

    private void retrieveSymptom() {
        final LiveData<SymptomEntity> symptomLiveData = db.symptomDao().loadSymptomById(symptomId);
        symptomLiveData.observe(this, new Observer<SymptomEntity>() {
            @Override
            public void onChanged(@Nullable SymptomEntity symptomEntity) {
                symptom = symptomLiveData.getValue();
                setTitle(symptom.getName());
            }
        });
    }
}

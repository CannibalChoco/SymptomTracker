package com.example.user.symptomtracker.ui;

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

    @BindView(R.id.symptomName)
    TextView symptomName;

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

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final SymptomEntity s = db.symptomDao().loadSymptomById(symptomId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        symptom = s;
                        symptomName.setText(symptom.getName());
                        setTitle(symptom.getName());
                    }
                });
            }
        });
    }
}

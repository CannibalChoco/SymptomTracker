package com.example.user.symptomtracker.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.utils.GraphUtils;
import com.jjoe64.graphview.GraphView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_ID = "id";
    private int symptomId;

    private AppDatabase db;
    private SymptomEntity symptom;
    private List<TreatmentEntity> treatments;

    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.treatment1)
    TextView treatment1;
    @BindView(R.id.treatment2)
    TextView treatment2;
    @BindView(R.id.treatment1Effect)
    TextView treatment1Effect;
    @BindView(R.id.treatment2Effect)
    TextView treatment2Effect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(getApplicationContext());

        symptomId = getIntent().getIntExtra(KEY_ID, 0);
        retrieveSymptom();
        retrieveTreatment();
    }

    private void retrieveSymptom() {
        final LiveData<SymptomEntity> symptomLiveData = db.symptomDao().loadSymptomById(symptomId);
        symptomLiveData.observe(this, new Observer<SymptomEntity>() {
            @Override
            public void onChanged(@Nullable SymptomEntity symptomEntity) {
                symptom = symptomLiveData.getValue();
                setTitle(symptom.getName());
                makeGraph();
            }
        });
    }

    private void retrieveTreatment(){
        final LiveData<List<TreatmentEntity>> treatmentLiveData = db.treatmentDao().getAllTreatments(symptomId);
        treatmentLiveData.observe(this, new Observer<List<TreatmentEntity>>() {
            @Override
            public void onChanged(@Nullable List<TreatmentEntity> treatmentEntities) {
                treatments = treatmentEntities;
                treatment1.setText(treatments.get(0).getName());
                treatment2.setText(treatments.get(1).getName());
                treatment1Effect.setText(String.valueOf(treatments.get(0).getTakesEffectIn()));
                treatment2Effect.setText(String.valueOf(treatments.get(1).getTakesEffectIn()));
            }
        });
    }

    private void makeGraph(){
        GraphUtils.initGraphView(graph, GraphUtils.getRandomDataPoints());
    }
}

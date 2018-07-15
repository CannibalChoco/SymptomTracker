package com.example.user.symptomtracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contains form for user to fill out when adding a symptom
 * Gets and validates the user input before information can be added to database
 */
public class AddSymptomActivity extends AppCompatActivity {

    @BindView(R.id.editSymptomName)
    EditText editSymptomName;
    @BindView(R.id.editCurrentTreatments)
    EditText editCurrentTreatment;
    @BindView(R.id.editPastTreatments)
    EditText editPastTreatment;
    @BindView(R.id.radioStatusDoctor)
    RadioButton radioStatusDoctor;
    @BindView(R.id.radioGroupStatus)
    RadioGroup radioGroupStatus;
    @BindView(R.id.radioStatusNew)
    RadioButton radioStatusNew;
    @BindView(R.id.radioStatusReoccurring)
    RadioButton radioStatusReoccurring;
    @BindView(R.id.radioStatusChronic)
    RadioButton radioStatusChronic;
    @BindView(R.id.btnSaveInDb)
    Button saveInDb;

    private String symptomName;
    private boolean doctorIsInformed;
    private boolean isChronic;
    private boolean isReoccurring;

    private AppDatabase db;

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);
        ButterKnife.bind(this);

        setTitle(R.string.title_add_new);

        db = AppDatabase.getInstance(getApplicationContext());
    }

    @OnClick(R.id.btnSaveInDb)
    public void saveInDb(){
        getEnteredData();

        final SymptomEntity symptom = new SymptomEntity(symptomName,
                isChronic,
                isReoccurring,
                doctorIsInformed,
                true);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.symptomDao().insertSymptom(symptom);
                id = db.symptomDao().getSymptomsId(symptomName);

                final TreatmentEntity treatment = new TreatmentEntity(id, "vitamin Z",
                        3600, 0, false);

                db.treatmentDao().insertTreatment(treatment);

                final TreatmentEntity treatment2 = new TreatmentEntity(id, "bath",
                        360000, 0, false);

                db.treatmentDao().insertTreatment(treatment2);

                // TODO: refactor. The same code in OverviewFragment
                Intent intent = new Intent(AddSymptomActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_ID, id);
                startActivity(intent);
                }
        });


    }

    private void getEnteredData (){

        symptomName = editSymptomName.getText().toString();
        doctorIsInformed = radioStatusDoctor.isChecked();
        isChronic = radioStatusChronic.isChecked();
        isReoccurring = radioStatusReoccurring.isChecked();
    }
}

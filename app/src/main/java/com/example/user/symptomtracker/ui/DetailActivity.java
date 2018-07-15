package com.example.user.symptomtracker.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.ui.adapter.NotesAdapter;
import com.example.user.symptomtracker.utils.GraphUtils;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_ID = "id";
    private int symptomId;

    private AppDatabase db;
    private SymptomEntity symptom;
    private List<TreatmentEntity> treatments;

    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.notesRecyclerView)
    RecyclerView notesRecyclerView;
    // TODO: remove debug fields; replace with recyclerviews
    @BindView(R.id.treatment1)
    TextView treatment1;
    @BindView(R.id.treatment2)
    TextView treatment2;
    @BindView(R.id.treatment1Effect)
    TextView treatment1Effect;
    @BindView(R.id.treatment2Effect)
    TextView treatment2Effect;

    @BindView(R.id.statusIsResolved)
    TextView statusIsResolved;
    @BindView(R.id.switchResolved)
    Switch switchResolved;
    @BindView(R.id.statusChronic)
    TextView statusIsChronic;
    @BindView(R.id.statusDoctorInformed)
    TextView statusDoctorInformed;

    @BindDrawable(R.drawable.backgroubd_status_attention)
    Drawable backgroundStatusAttention;
    @BindDrawable(R.drawable.background_status_good)
    Drawable backgroundStatusGood;
    @BindDrawable(R.drawable.background_status_default)
    Drawable backgroundStatusDefault;

    @BindColor(R.color.color_status_attention)
    int colorStatusAttention;
    @BindColor(R.color.color_status_good)
    int colorStatusGood;
    @BindColor(R.color.color_status_default)
    int colorStatusDefault;

    NotesAdapter notesAdapter;
    LinearLayoutManager notesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(getApplicationContext());

        symptomId = getIntent().getIntExtra(KEY_ID, 0);

        setUpNotesRecyclerView();

        retrieveSymptom();
        retrieveTreatment();
    }

    private void setUpNotesRecyclerView() {
        notesAdapter = new NotesAdapter(new ArrayList<NoteEntity>());
        notesLayoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(notesLayoutManager);
        notesRecyclerView.setHasFixedSize(true);
    }

    /**
     * Get new symptom data from Db whenever changes are observed
     */
    private void retrieveSymptom() {
        final LiveData<SymptomEntity> symptomLiveData = db.symptomDao().loadSymptomById(symptomId);
        symptomLiveData.observe(this, new Observer<SymptomEntity>() {
            @Override
            public void onChanged(@Nullable SymptomEntity symptomEntity) {
                symptom = symptomLiveData.getValue();
                setTitle(symptom.getName());
                setIsResolvedInUi(symptom.isResolved());
                switchResolved.setChecked(symptom.isResolved());
                setIsChronicInUi(symptom.isChronic());
                setDoctorIsInformedInUi(symptom.isDoctorIsInformed());
                makeGraph();

                retrieveNotes();
            }
        });
    }

    /**
     * Update isChronic with opposite value in db
     */
    @OnLongClick(R.id.statusChronic)
    public boolean setStatusChronic(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.symptomDao().updateIsChronic(symptomId, !symptom.isChronic());
            }
        });

        return true;
    }

    /**
     * Update doctorIsInformed with opposite value in db
     */
    @OnLongClick(R.id.statusDoctorInformed)
    public boolean setStatusDoctorIsInformed(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.symptomDao().updateDoctorIsInformed(symptomId, !symptom.isDoctorIsInformed());
            }
        });

        return true;
    }

    /**
     * Update isResolved with opposite value in db
     */
    @OnClick({R.id.switchResolved})
    public void setStatusResolved(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.symptomDao().updateIsResolved(symptomId, !symptom.isResolved());
            }
        });
    }

    @OnClick(R.id.addNote)
    public void addNote(){
        final NoteEntity note = new NoteEntity("Catching cold increases the pain", symptomId,
                new Date().getTime());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.noteDao().insertNote(note);
            }
        });

    }

    private void setIsChronicInUi(boolean isChronic){
        if(isChronic){
            statusIsChronic.setBackground(backgroundStatusDefault);
            statusIsChronic.setText(R.string.status_chronic);
            statusIsChronic.setTextColor(colorStatusDefault);
        } else {
            statusIsChronic.setBackground(backgroundStatusGood);
            statusIsChronic.setText(R.string.status_not_chronic);
            statusIsChronic.setTextColor(colorStatusGood);
        }
    }

    private void setDoctorIsInformedInUi(boolean doctorIsInformed){
        if(doctorIsInformed){
            statusDoctorInformed.setBackground(backgroundStatusGood);
            statusDoctorInformed.setText(R.string.status_doctor_informed);
            statusDoctorInformed.setTextColor(colorStatusGood);
        }else{
            statusDoctorInformed.setBackground(backgroundStatusAttention);
            statusDoctorInformed.setText(R.string.status_doctor_not_informed);
            statusDoctorInformed.setTextColor(colorStatusAttention);
        }
    }

    private void setIsResolvedInUi(boolean isResolved){
        if(isResolved){
            statusIsResolved.setText(R.string.status_resolved);
        } else {
            statusIsResolved.setText(R.string.status_not_resolved);
        }
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

    private void retrieveNotes() {
        final LiveData<List<NoteEntity>> notes = db.noteDao().loadAllNotesForSymptom(symptomId);
        notes.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                notesAdapter.replaceDataSet(noteEntities);
            }
        });
    }

    private void makeGraph(){
        GraphUtils.initGraphView(graph, GraphUtils.getRandomDataPoints());
    }
}

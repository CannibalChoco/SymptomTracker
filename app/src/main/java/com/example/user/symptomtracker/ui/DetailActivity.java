package com.example.user.symptomtracker.ui;

import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.Repository;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.ui.DialogFragments.AddNoteDialog;
import com.example.user.symptomtracker.ui.adapter.NotesAdapter;
import com.example.user.symptomtracker.utils.GraphUtils;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModel;
import com.example.user.symptomtracker.viewmodel.DetailActivityViewModelFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.example.user.symptomtracker.ui.MainActivity.DUMMY_AD_ID;

public class DetailActivity extends AppCompatActivity implements AddNoteDialog.OnSaveNote {

    public static final String KEY_ID = "id";
    public static final String FRAGMENT_ADD_NOTE = "fragmentAddNote";
    public static final String FRAGMENT_ADD_CURRENT_TREATMENT = "fragmentAddCurrentTreatment";
    public static final String FRAGMENT_ADD_PAST_TREATMENT = "fragmentAddPastTreatment";

    public static final int NUM_PAGES = 2;

    private int symptomId;

    private AppDatabase db;
    private SymptomEntity symptom;

    @BindView(R.id.graph)
    BarChart graph;
    @BindView(R.id.notesRv)
    RecyclerView notesRecyclerView;

    @BindView(R.id.statusIsResolved)
    TextView statusIsResolved;
    @BindView(R.id.switchResolved)
    Switch switchResolved;
    @BindView(R.id.statusChronic)
    TextView statusIsChronic;
    @BindView(R.id.statusDoctorInformed)
    TextView statusDoctorInformed;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabs;
    @BindView(R.id.adView)
    AdView bannerAd;

    private TreatmentPagerAdapter treatmentPagerAdapter;

    @BindDrawable(R.drawable.background_status_attention)
    Drawable backgroundStatusAttention;
    @BindDrawable(R.drawable.background_status_good)
    Drawable backgroundStatusGood;
    @BindDrawable(R.drawable.background_status_good1)
    Drawable backgroundStatusGood1;
    @BindDrawable(R.drawable.background_status_default)
    Drawable backgroundStatusDefault;

    @BindColor(R.color.colorStatusAttention)
    int colorStatusAttention;
    @BindColor(R.color.colorStatusGood)
    int colorStatusGood;
    @BindColor(R.color.colorStatusDefault)
    int colorStatusDefault;

    private NotesAdapter notesAdapter;

    private DetailActivityViewModel model;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(getApplicationContext());
        repository = Repository.getInstance(db);
        symptomId = getIntent().getIntExtra(KEY_ID, 0);

        DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(db, symptomId);
        model = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        setUpNotesRecyclerView();
        setUpTreatmentsRecyclerViews();
        retrieveSymptom();
        retrieveSeverity();
        retrieveNotes();

        MobileAds.initialize(this, DUMMY_AD_ID);

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
    }


    private void setUpNotesRecyclerView() {
        notesAdapter = new NotesAdapter(new ArrayList<>());
        LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(notesLayoutManager);
        notesRecyclerView.setHasFixedSize(true);
    }

    private void setUpTreatmentsRecyclerViews() {
        treatmentPagerAdapter = new TreatmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(treatmentPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    /**
     * Get new symptom data from Db whenever changes are observed
     */
    private void retrieveSymptom() {
        model.getSymptomEntity().observe(this, symptomEntity -> {
            symptom = symptomEntity;
            setTitle(symptom.getName());
            setIsResolvedInUi(symptom.isResolved());
            switchResolved.setChecked(symptom.isResolved());
            setDoctorIsInformedInUi(symptom.isDoctorIsInformed());
            setIsChronicInUi(symptom.isChronic());
        });
    }

    private void retrieveSeverity(){
        model.getSeverityList().observe(this, severityEntities ->
                GraphUtils.initBarChart(graph, severityEntities));
    }

    private void retrieveNotes() {
        model.getNoteList().observe(this, noteEntities ->
                notesAdapter.replaceDataSet(noteEntities));
    }

    /**
     * Update isChronic with opposite value in db
     */
    @OnLongClick(R.id.statusChronic)
    public boolean setStatusChronic() {
        repository.setStatusIsChronic(symptomId, !symptom.isChronic());
        return true;
    }

    /**
     * Update doctorIsInformed with opposite value in db
     */
    @OnLongClick(R.id.statusDoctorInformed)
    public boolean setStatusDoctorIsInformed() {
        repository.setStatusDoctorIsInformed(symptomId, !symptom.isDoctorIsInformed());
        return true;
    }

    /**
     * Update isResolved with opposite value in db
     */
    @OnClick({R.id.switchResolved})
    public void setStatusResolved() {
        repository.setStatusResolved(symptomId, !symptom.isResolved());
    }

    @OnClick(R.id.addNote)
    public void addNote() {
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        addNoteDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        addNoteDialog.setOnSAveNoteListener(this);
        addNoteDialog.show(getSupportFragmentManager(), FRAGMENT_ADD_NOTE);
    }

    private void setIsChronicInUi(boolean isChronic) {
        if (isChronic) {
            statusIsChronic.setText(R.string.status_chronic);
            statusIsChronic.setTextColor(colorStatusDefault);
            statusIsChronic.setBackground(backgroundStatusDefault);
        } else {
            statusIsChronic.setText(R.string.status_not_chronic);
            statusIsChronic.setTextColor(colorStatusGood);
            statusIsChronic.setBackground(backgroundStatusGood);
        }
    }

    private void setDoctorIsInformedInUi(boolean doctorIsInformed) {
        if (doctorIsInformed) {
            statusDoctorInformed.setText(R.string.status_doctor_informed);
            statusDoctorInformed.setTextColor(colorStatusGood);
            statusDoctorInformed.setBackground(backgroundStatusGood1);
        } else {
            statusDoctorInformed.setText(R.string.status_doctor_not_informed);
            statusDoctorInformed.setTextColor(colorStatusAttention);
            statusDoctorInformed.setBackground(backgroundStatusAttention);
        }
    }

    private void setIsResolvedInUi(boolean isResolved) {
        if (isResolved) {
            statusIsResolved.setText(R.string.status_resolved);
        } else {
            statusIsResolved.setText(R.string.status_not_resolved);
        }
    }

    @Override
    public void onSaveNote(String note) {
        final NoteEntity noteEntity = new NoteEntity(note, symptomId,
                new Date().getTime());
        repository.saveNote(noteEntity);
    }


    /**
     * A simple pager adapter that represents 2 TreatmentFragment objects, in
     * sequence.
     */
    private class TreatmentPagerAdapter extends FragmentStatePagerAdapter {
        public TreatmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TreatmentFragment fragment = new TreatmentFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(TreatmentFragment.KEY_FRAGMENT_ID, position);
            bundle.putInt(TreatmentFragment.KEY_SYMPTOM_ID, symptomId);
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.label_current_treatments);
                case 1:
                    return getString(R.string.label_past_treatments);
                default:
                    return null;
            }
        }
    }
}

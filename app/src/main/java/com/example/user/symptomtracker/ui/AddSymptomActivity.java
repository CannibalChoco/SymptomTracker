package com.example.user.symptomtracker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.user.symptomtracker.AppExecutors;
import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.Date;

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
    @BindView(R.id.addNote)
    EditText editAddNote;

    @BindView(R.id.btnSaveInDb)
    Button saveInDb;

    private String symptomName;
    private boolean doctorIsInformed;
    private boolean isChronic;
    private boolean isReoccurring;

    private String note;

    private AppDatabase db;

    private boolean dataHasChanged;

    private int id;

    /**
     * Detect when a view is clicked to keep track if user has made any changes
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dataHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);
        ButterKnife.bind(this);

        setTitle(R.string.title_add_new);

        db = AppDatabase.getInstance(getApplicationContext());

        setTouchListener();
    }

    @OnClick(R.id.btnSaveInDb)
    public void saveInDb(){
        getEnteredData();

        final SymptomEntity symptom = new SymptomEntity(symptomName,
                isChronic,
                isReoccurring,
                doctorIsInformed,
                false);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.symptomDao().insertSymptom(symptom);
                id = db.symptomDao().getSymptomsId(symptomName);

                if (!note.isEmpty()){
                    db.noteDao().insertNote(new NoteEntity(note, id, new Date().getTime()));
                }

                // TODO: refactor. The same code in OverviewFragment
                Intent intent = new Intent(AddSymptomActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_ID, id);
                startActivity(intent);
                }
        });


    }

    @Override
    public void onBackPressed() {
        if (!dataHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_msg_unsaved_changes);
        builder.setPositiveButton(R.string.dialog_action_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.dialog_action_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the symptom.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getEnteredData (){

        symptomName = editSymptomName.getText().toString();
        doctorIsInformed = radioStatusDoctor.isChecked();
        isChronic = radioStatusChronic.isChecked();
        isReoccurring = radioStatusReoccurring.isChecked();

        note = editAddNote.getText().toString();
    }

    private void setTouchListener() {
        editSymptomName.setOnTouchListener(touchListener);
        editAddNote.setOnTouchListener(touchListener);
        radioGroupStatus.setOnTouchListener(touchListener);
        radioStatusChronic.setOnTouchListener(touchListener);
        radioStatusReoccurring.setOnTouchListener(touchListener);
    }
}

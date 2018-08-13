package com.example.user.symptomtracker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.SymptomTrackerApplication;
import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.utils.UnsavedChangeDialogUtils;
import com.example.user.symptomtracker.utils.WidgetUtils;

import java.lang.ref.WeakReference;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contains form for user to fill out when adding a symptom
 * Gets and validates the user input before information can be added to database
 */
public class AddSymptomActivity extends AppCompatActivity {


    public static final String KEY_DATA_HAS_CHANGED = "DATA_HAS_CHANGED";
    @BindView(R.id.editSymptomName)
    TextInputEditText editSymptomName;
    @BindView(R.id.radioStatusDoctor)
    CheckBox radioStatusDoctor;
    @BindView(R.id.radioStatusChronic)
    CheckBox checkStatusChronic;
    @BindView(R.id.addNote)
    TextInputEditText editAddNote;

    private String symptomName;
    private boolean doctorIsInformed;
    private boolean isChronic;
    private boolean isReoccurring;

    private String note;

    private AppDatabase db;

    private boolean dataHasChanged;

    /**
     * Detect when a view is clicked to keep track if user has made any changes
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
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

        if (savedInstanceState != null){
            dataHasChanged = savedInstanceState.getBoolean(KEY_DATA_HAS_CHANGED);
        }

        setTitle(R.string.title_add_new);

        db = AppDatabase.getInstance(getApplicationContext());

        setTouchListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_DATA_HAS_CHANGED, dataHasChanged);
    }

    @Override
    public void onBackPressed() {
        if (!dataHasChanged) {
            super.onBackPressed();
            return;
        }

        // If there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_or_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveInDb();
                return true;
            case R.id.action_cancel:
                showUnsavedChangesDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveInDb(){
        getEnteredData();

        if(symptomName.isEmpty()){
            editSymptomName.setError(getString(R.string.error_must_provide_name));
            editSymptomName.requestFocus();
        } else {
            final SymptomEntity symptom = new SymptomEntity(symptomName,
                    isChronic,
                    isReoccurring,
                    doctorIsInformed,
                    false,
                    System.currentTimeMillis(),
                    SymptomEntity.NO_RESOLVED_TIMESTAMP);

            // save
            new SaveSymptomAsyncTask(this, db, symptom, note).execute();
            finish();
        }
    }

    @NonNull
    private DialogInterface.OnClickListener getDiscardClickListener() {
        return (dialogInterface, i) -> {
            // User clicked "Discard" button, close the current activity.
            finish();
        };
    }

    private void showUnsavedChangesDialog() {
        // Create and show the AlertDialog
        AlertDialog alertDialog = UnsavedChangeDialogUtils.constructDialog(this,
                getDiscardClickListener(), UnsavedChangeDialogUtils.ACTION_CANCEL_DIALOG);
        alertDialog.show();
    }

    private void getEnteredData (){

        symptomName = editSymptomName.getText().toString();

        doctorIsInformed = radioStatusDoctor.isChecked();
        isChronic = checkStatusChronic.isChecked();
        isReoccurring = false;

        note = editAddNote.getText().toString();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        editSymptomName.setOnTouchListener(touchListener);
        editAddNote.setOnTouchListener(touchListener);
        checkStatusChronic.setOnTouchListener(touchListener);
    }

    /**
     * AsyncTask for saving the symptom. Up on insertion completion, launches the DetailActivity
     * for newly inserted symptom
     */
    private static class SaveSymptomAsyncTask extends AsyncTask<Void, Void, Integer> {
        private final WeakReference<Context> context;
        private final WeakReference<SymptomEntity> symptom;
        private final AppDatabase db;
        private final String note;

        public SaveSymptomAsyncTask(Context context, AppDatabase db, SymptomEntity symptom, String note) {
            this.context = new WeakReference<>(context);
            this.symptom = new WeakReference<>(symptom);
            this.db = db;
            this.note = note;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Integer id = (int) db.symptomDao().insertSymptom(symptom.get());

            if (!note.isEmpty()){
                db.noteDao().insertNote(new NoteEntity(note, id, new Date().getTime()));
            }

            return id;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Intent intent = new Intent(context.get(),
                    DetailActivity.class);
            intent.putExtra(DetailActivity.KEY_ID, integer);
            context.get().startActivity(intent);

            WidgetUtils.updateWidget(SymptomTrackerApplication.getInstance());
            }
    }

}

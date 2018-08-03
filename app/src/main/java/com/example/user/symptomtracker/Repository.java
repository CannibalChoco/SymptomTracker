package com.example.user.symptomtracker;

import android.arch.lifecycle.LiveData;

import com.example.user.symptomtracker.database.AppDatabase;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.List;

/**
 * Class implemented by example from
 * https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/DataRepository.java
 */
public class Repository {

    private static Repository sInstance;

    private static AppDatabase db;
    private static AppExecutors executors;

    private Repository(final AppDatabase database) {
        db = database;
        executors = AppExecutors.getInstance();
    }

    public static Repository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(database);
                }
            }
        }
        return sInstance;
    }

    public int getSymptomCount(){
        final int[] count = new int[1];
        executors.diskIO().execute(() -> count[0] = db.symptomDao().getSymptomCount());
        return count[0];
    }

    public List<Symptom> getUnresolvedSymptoms(){
        return db.symptomDao().loadUnresolvedSymptoms();
    }

    public void updateTreatment(TreatmentEntity treatment){
        executors.diskIO().execute(() -> db.treatmentDao().updateTreatment(treatment));
    }

    public void updateNote(int id, String text){
        executors.diskIO().execute(() ->
        db.noteDao().updateNote(id, text));
    }

    public void updateSymptomName(int id, String name){
        executors.diskIO().execute(() -> db.symptomDao().updateName(id, name));
    }

    public void deleteAllSymptomDataForId(int id){
        executors.diskIO().execute(() -> {
            db.treatmentDao().deleteTreatmentsForSymptom(id);
            db.noteDao().deleteNotesForSymptom(id);
            db.noteDao().deleteNotesForSymptom(id);
            db.symptomDao().deleteSymptom(id);
        });
    }

    public void saveSeverity(SeverityEntity severity){
        executors.diskIO().execute(() ->
                db.severityDao().insertSeverity(severity));
    }

    public void saveTreatment(TreatmentEntity treatment){
        executors.diskIO().execute(() ->
                db.treatmentDao().insertTreatment(treatment));
    }

    public void setStatusResolved(int symptomId, boolean isResolved){
        executors.diskIO().execute(() -> {
            db.symptomDao().updateIsResolved(symptomId, isResolved);
            if (!isResolved){
                db.symptomDao().updateNotResolvedTimestamp(symptomId, System.currentTimeMillis());
            }
        });
    }

    public void setStatusDoctorIsInformed(int symptomId, boolean doctorIsInformed){
        executors.diskIO().execute(() ->
                db.symptomDao().updateDoctorIsInformed(symptomId, doctorIsInformed));
    }

    public void setStatusIsChronic(int symptomId, boolean isChronic){
        executors.diskIO().execute(() ->
        db.symptomDao().updateIsChronic(symptomId, isChronic));
    }

    public void saveNote(NoteEntity note){
        executors.diskIO().execute(() -> db.noteDao().insertNote(note));
    }

    public LiveData<SymptomEntity> getSymptomEntityById(int id){
        return db.symptomDao().loadSymptomById(id);
    }

    public LiveData<List<SeverityEntity>> getSeverityForSymptom(int id){
        return db.severityDao().loadSeverityForSymptom(id);
    }

    public LiveData<List<NoteEntity>> getNotesForSymptom(int id){
        return db.noteDao().loadNotesForSymptom(id);
    }

    public LiveData<List<TreatmentEntity>> getCurrentTreatments(int id){
        return db.treatmentDao().loadTreatmentsByIsActive(id,true);
    }

    public LiveData<List<TreatmentEntity>> getPastTreatments(int id){
        return db.treatmentDao().loadTreatmentsByIsActive(id,false);
    }

    public LiveData<List<Symptom>> getUnresolvedSymptomLiveData(){
        return db.symptomDao().loadUnresolvedSymptomLiveData();
    }
}

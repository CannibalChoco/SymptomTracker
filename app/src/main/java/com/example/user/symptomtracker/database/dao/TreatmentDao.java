package com.example.user.symptomtracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.List;

@Dao
public interface TreatmentDao {

    @Query("SELECT * FROM treatments WHERE symptom_id = :symptomId")
    LiveData<List<TreatmentEntity>> loadTreatmentsForSymptom(int symptomId);

    @Query("SELECT * FROM treatments WHERE symptom_id = :symptomId AND is_active = :isActive")
    LiveData<List<TreatmentEntity>> loadTreatmentsByIsActive(int symptomId, boolean isActive);

    @Query("DELETE FROM treatments WHERE symptom_id = :symptomId")
    void deleteTreatmentsForSymptom(int symptomId);

    @Query("UPDATE treatments SET was_successful = :wasSuccessful WHERE id = :id")
    void updateTreatmentSuccess(int id, int wasSuccessful);

    @Insert
    void insertTreatment(TreatmentEntity treatment);
}

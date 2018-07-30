package com.example.user.symptomtracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Insert
    void insertTreatment(TreatmentEntity treatment);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTreatment(TreatmentEntity treatment);

    @Delete
    void deleteTreatment(TreatmentEntity treatment);

}

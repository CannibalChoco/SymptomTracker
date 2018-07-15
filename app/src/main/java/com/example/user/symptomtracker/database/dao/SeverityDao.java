package com.example.user.symptomtracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.symptomtracker.database.entity.SeverityEntity;

import java.util.List;

@Dao
public interface SeverityDao {

    @Query("SELECT * FROM severity WHERE symptom_id = :symptomId ORDER BY timestamp ASC")
    LiveData<List<SeverityEntity>> loadSeverityForSymptom(int symptomId);

    @Insert
    void insertSeverity(SeverityEntity severity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSeverity(SeverityEntity severity);

    @Delete
    void deleteSeverity(SeverityEntity severity);
}

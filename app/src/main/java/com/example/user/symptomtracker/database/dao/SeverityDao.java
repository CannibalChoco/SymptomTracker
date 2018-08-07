package com.example.user.symptomtracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.symptomtracker.database.entity.SeverityEntity;

import java.util.List;

@Dao
public interface SeverityDao {

    @Query("SELECT * FROM severity WHERE symptom_id = :symptomId ORDER BY timestamp ASC")
    LiveData<List<SeverityEntity>> loadSeverityForSymptom(int symptomId);

    @Query("UPDATE severity SET severity = :newSeverity, timestamp = :newTimestamp WHERE id = :id ")
    void updateSeverityForId(int id, int newSeverity, long newTimestamp);

    @Insert
    void insertSeverity(SeverityEntity severity);
}

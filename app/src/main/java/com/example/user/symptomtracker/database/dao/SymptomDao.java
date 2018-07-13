package com.example.user.symptomtracker.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.symptomtracker.database.entity.SymptomEntity;

import java.util.List;

@Dao
public interface SymptomDao {

    @Query("SELECT * FROM symptom")
    LiveData<List<SymptomEntity>> loadAllSymptoms();

    @Insert
    void insertSymptom(SymptomEntity symptom);

    @Delete
    void deleteSymptom(SymptomEntity symptom);

    @Query("SELECT * FROM symptom WHERE id = :id")
    SymptomEntity loadSymptomById(int id);
}

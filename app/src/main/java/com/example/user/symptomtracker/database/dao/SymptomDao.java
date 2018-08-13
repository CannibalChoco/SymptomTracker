package com.example.user.symptomtracker.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.user.symptomtracker.database.entity.Symptom;
import com.example.user.symptomtracker.database.entity.SymptomEntity;

import java.util.List;

@Dao
public interface SymptomDao {

    @Transaction
    @Query("SELECT * FROM symptom WHERE NOT is_resolved")
    LiveData<List<Symptom>> loadUnresolvedSymptomLiveData();

    @Transaction
    @Query("SELECT * FROM symptom WHERE is_resolved")
    LiveData<List<Symptom>> loadResolvedSymptomLiveData();

    @Transaction
    @Query("SELECT * FROM symptom WHERE NOT is_resolved")
    List<Symptom> loadUnresolvedSymptoms();

    @Query("SELECT * FROM symptom WHERE NOT is_resolved AND not_resolved_timestamp < :timeWeekAgo " +
            "AND NOT doctor_is_informed")
    List<SymptomEntity> loadAllUnresolvedSymptoms(long timeWeekAgo);

    @Insert
    long insertSymptom(SymptomEntity symptom);

    @Query("DELETE FROM symptom WHERE id = :id")
    void deleteSymptom(int id);

    /**
     * Get a symptom data by symptoms Id
     * @param id symptoms Id
     * @return the SymptomEntity of given Id
     */
    @Query("SELECT * FROM symptom WHERE id = :id")
    LiveData<SymptomEntity> loadSymptomById(int id);

    /**
     * Update the value of isResolved
     * @param id the symptoms, that is to be updated, id
     * @param isResolved the new value
     */
    @Query("UPDATE symptom SET is_resolved = :isResolved WHERE id = :id")
    void updateIsResolved(int id, boolean isResolved);

    /**
     * Update symptoms name
     * @param id the symptoms, that is to be updated, id
     * @param name the new value
     */
    @Query("UPDATE symptom SET name = :name WHERE id = :id")
    void updateName(int id, String name);

    /**
     * Update symptoms status of isChronic
     * @param id the symptoms, that is to be updated, id
     * @param isChronic the new value
     */
    @Query("UPDATE symptom SET is_chronic = :isChronic WHERE id = :id")
    void updateIsChronic(int id, boolean isChronic);

    /**
     * Update symptoms status of doctorIsInformed
     * @param id the symptoms, that is to be updated, id
     * @param doctorIsInformed the new value
     */
    @Query("UPDATE symptom SET doctor_is_informed = :doctorIsInformed WHERE id =:id")
    void updateDoctorIsInformed(int id, boolean doctorIsInformed);

    /**
     * Update the timestamp for unresolved
     * @param id symptom ID
     * @param timestamp current time in milliseconds or -1 if symptom is resolved
     */
    @Query("UPDATE symptom SET not_resolved_timestamp = :timestamp WHERE id = :id")
    void updateNotResolvedTimestamp(int id, long timestamp);

    /**
     * Update the timestamp for resolved
     * @param id symptom ID
     * @param timestamp current time in milliseconds or -1 if symptom is unresolved
     */
    @Query("UPDATE symptom SET resolved_timestamp = :timestamp WHERE id = :id")
    void updateResolvedTimestamp(int id, long timestamp);

}

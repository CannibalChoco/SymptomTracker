package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Provides all necessary information for a symptoms treatments. All entities of this class
 * will be mapped to a parent object by parents ID
 */

@Entity(tableName = "treatments",
        foreignKeys = {
                @ForeignKey(entity = SymptomEntity.class,
                        parentColumns = "id",
                        childColumns = "symptom_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "symptom_id")})
public class TreatmentEntity {

    public static final long TIME_NOT_SELECTED = -1;

    public static final int WAS_SUCCESSFUL_NOT_SET = 0;
    public static final int WAS_SUCCESSFUL_YES = 1;
    public static final int WAS_SUCCESSFUL_NO = 2;

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "symptom_id")
    private int symptomId;
    private String name;
    @ColumnInfo(name = "takes_effect_in")
    private long takesEffectIn;
    @ColumnInfo(name = "was_successful")
    private int wasSuccessful;
    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public TreatmentEntity(int id, int symptomId, String name, long takesEffectIn, int wasSuccessful,
                            boolean isActive) {
        this.id = id;
        this.symptomId = symptomId;
        this.name = name;
        this.takesEffectIn = takesEffectIn;
        this.wasSuccessful = wasSuccessful;
        this.isActive = isActive;
    }

    @Ignore
    public TreatmentEntity(int symptomId, String name, long takesEffectIn, int wasSuccessful,
                           boolean isActive) {
        this.symptomId = symptomId;
        this.name = name;
        this.takesEffectIn = takesEffectIn;
        this.wasSuccessful = wasSuccessful;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(int symptomId) {
        this.symptomId = symptomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTakesEffectIn() {
        return takesEffectIn;
    }

    public void setTakesEffectIn(long takesEffectIn) {
        this.takesEffectIn = takesEffectIn;
    }

    public int getWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(int wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

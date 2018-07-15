package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * The main entity. Additional data will be mapped with SymptomEntity id field
 */
@Entity (tableName = "symptom")
public class SymptomEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @ColumnInfo(name = "is_chronic")
    private boolean isChronic;
    @ColumnInfo(name = "is_reoccurring")
    private boolean isReoccurring;
    @ColumnInfo(name = "doctor_is_informed")
    private boolean doctorIsInformed;
    @ColumnInfo(name = "is_resolved")
    private boolean isResolved;

    public SymptomEntity(int id, String name, boolean isChronic, boolean isReoccurring,
                         boolean doctorIsInformed, boolean isResolved) {
        this.id = id;
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
        this.isResolved = isResolved;
    }

    @Ignore
    public SymptomEntity(String name, boolean isChronic, boolean isReoccurring,
                         boolean doctorIsInformed, boolean isResolved) {
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
        this.isResolved = isResolved;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isChronic() {
        return isChronic;
    }

    public boolean isReoccurring() {
        return isReoccurring;
    }

    public boolean isDoctorIsInformed() {
        return doctorIsInformed;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}

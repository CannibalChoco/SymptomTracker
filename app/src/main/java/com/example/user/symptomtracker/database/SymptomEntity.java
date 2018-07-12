package com.example.user.symptomtracker.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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

    public SymptomEntity(int id, String name, boolean isChronic, boolean isReoccurring,
                         boolean doctorIsInformed) {
        this.id = id;
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
    }

    @Ignore
    public SymptomEntity(String name, boolean isChronic, boolean isReoccurring, boolean doctorIsInformed) {
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
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
}

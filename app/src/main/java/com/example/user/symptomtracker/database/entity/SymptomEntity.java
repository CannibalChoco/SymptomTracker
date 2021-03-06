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

    public static final int NO_RESOLVED_TIMESTAMP = -1;

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
    @ColumnInfo(name = "not_resolved_timestamp")
    private long notResolvedTimestamp;
    @ColumnInfo(name = "resolved_timestamp")
    private long resolvedTimeStamp;

    public SymptomEntity(int id, String name, boolean isChronic, boolean isReoccurring,
                         boolean doctorIsInformed, boolean isResolved, long notResolvedTimestamp,
                         long resolvedTimeStamp) {
        this.id = id;
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
        this.isResolved = isResolved;
        this.notResolvedTimestamp = notResolvedTimestamp;
        this.resolvedTimeStamp = resolvedTimeStamp;
    }

    @Ignore
    public SymptomEntity(String name, boolean isChronic, boolean isReoccurring,
                         boolean doctorIsInformed, boolean isResolved, long notResolvedTimestamp,
                         long resolvedTimeStamp) {
        this.name = name;
        this.isChronic = isChronic;
        this.isReoccurring = isReoccurring;
        this.doctorIsInformed = doctorIsInformed;
        this.isResolved = isResolved;
        this.notResolvedTimestamp = notResolvedTimestamp;
        this.resolvedTimeStamp = resolvedTimeStamp;
    }

    public long getNotResolvedTimestamp() {
        return notResolvedTimestamp;
    }

    public void setNotResolvedTimestamp(long notResolvedTimestamp) {
        this.notResolvedTimestamp = notResolvedTimestamp;
    }

    public long getResolvedTimeStamp() {
        return resolvedTimeStamp;
    }

    public void setResolvedTimeStamp(long resolvedTimeStamp) {
        this.resolvedTimeStamp = resolvedTimeStamp;
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

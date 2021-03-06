package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Provides all necessary information for a symptoms severity by timestamp. All entities of this class
 * will be mapped to a parent object by parents ID
 */
@Entity(tableName = "severity",
        foreignKeys = {
                @ForeignKey(entity = SymptomEntity.class,
                        parentColumns = "id",
                        childColumns = "symptom_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "symptom_id")})
public class SeverityEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "symptom_id")
    private int symptomId;
    private int severity;
    private long timestamp;

    public SeverityEntity(int id, int symptomId, int severity, long timestamp) {
        this.id = id;
        this.symptomId = symptomId;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    @Ignore
    public SeverityEntity(int symptomId, int severity, long timestamp) {
        this.symptomId = symptomId;
        this.severity = severity;
        this.timestamp = timestamp;
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

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

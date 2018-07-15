package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = {
                @ForeignKey(entity = SymptomEntity.class,
                        parentColumns = "id",
                        childColumns = "symptom_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "symptom_id")})
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    @ColumnInfo(name = "symptom_id")
    private int symptomId;
    private long timestamp;

    @Ignore
    public NoteEntity(String content, int symptomId, long timestamp) {
        this.content = content;
        this.symptomId = symptomId;
        this.timestamp = timestamp;
    }

    public NoteEntity(int id, String content, int symptomId, long timestamp) {
        this.id = id;
        this.content = content;
        this.symptomId = symptomId;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(int symptomId) {
        this.symptomId = symptomId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Class contains all details for a symptom
 */
public class Symptom {
    @Embedded
    private SymptomEntity symptom;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = SeverityEntity.class)
    private List<SeverityEntity> severity;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = NoteEntity.class)
    private List<NoteEntity> notes;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = TreatmentEntity.class)
    private List<TreatmentEntity> treatments;

    public SymptomEntity getSymptom() {
        return symptom;
    }

    public List<SeverityEntity> getSeverity() {
        return severity;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public List<TreatmentEntity> getTreatments() {
        return treatments;
    }

    public void setSymptom(SymptomEntity symptom) {
        this.symptom = symptom;
    }

    public void setSeverity(List<SeverityEntity> severity) {
        this.severity = severity;
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
    }

    public void setTreatments(List<TreatmentEntity> treatments) {
        this.treatments = treatments;
    }
}

package com.example.user.symptomtracker.database.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Class contains all details for a symptom
 */
public class Symptom{
    @Embedded
    private SymptomEntity symptom;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = SeverityEntity.class)
    private List<SeverityEntity> severityList;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = NoteEntity.class)
    private List<NoteEntity> notesList;

    @Relation(parentColumn = "id",
                entityColumn = "symptom_id",
                entity = TreatmentEntity.class)
    private List<TreatmentEntity> treatmentsList;

    public Symptom() {
    }

    public SymptomEntity getSymptom() {
        return symptom;
    }

    public void setSymptom(SymptomEntity symptom) {
        this.symptom = symptom;
    }

    public List<SeverityEntity> getSeverityList() {
        return severityList;
    }

    public void setSeverityList(List<SeverityEntity> severityList) {
        this.severityList = severityList;
    }

    public List<NoteEntity> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<NoteEntity> notesList) {
        this.notesList = notesList;
    }

    public List<TreatmentEntity> getTreatmentsList() {
        return treatmentsList;
    }

    public void setTreatmentsList(List<TreatmentEntity> treatmentsList) {
        this.treatmentsList = treatmentsList;
    }
}

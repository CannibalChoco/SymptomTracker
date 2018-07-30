package com.example.user.symptomtracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.symptomtracker.database.entity.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes WHERE symptom_id = :symptomId ORDER BY timestamp DESC")
    LiveData<List<NoteEntity>> loadNotesForSymptom(int symptomId);

    @Query("DELETE FROM notes WHERE symptom_id = :symptomId")
    void deleteNotesForSymptom(int symptomId);

    @Query("UPDATE notes SET content = :text WHERE id = :id")
    void updateNote(int id, String text);

    @Insert
    void insertNote(NoteEntity note);
}

package com.example.user.symptomtracker.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.symptomtracker.database.dao.NoteDao;
import com.example.user.symptomtracker.database.dao.SeverityDao;
import com.example.user.symptomtracker.database.dao.SymptomDao;
import com.example.user.symptomtracker.database.dao.TreatmentDao;
import com.example.user.symptomtracker.database.entity.NoteEntity;
import com.example.user.symptomtracker.database.entity.SeverityEntity;
import com.example.user.symptomtracker.database.entity.SymptomEntity;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

@Database(entities = {SymptomEntity.class, SeverityEntity.class, TreatmentEntity.class,
                    NoteEntity.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "symptomTracker";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE symptom ADD COLUMN resolved_timestamp INTEGER NOT NULL DEFAULT -1");
        }
    };

    public abstract SymptomDao symptomDao();

    public abstract SeverityDao severityDao();

    public abstract TreatmentDao treatmentDao();

    public abstract NoteDao noteDao();
}

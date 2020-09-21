package com.ionshield.tetris;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.ionshield.tetris.DatabaseContract.DatabaseEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME + " TEXT," +
                    DatabaseEntry.COLUMN_SCORE + " INTEGER," +
                    DatabaseEntry.COLUMN_DIFFICULTY + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Scores.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void truncate(SQLiteDatabase db) {
        String sql = "DELETE FROM " + DatabaseEntry.TABLE_NAME;
        db.execSQL(sql);
    }

    public SQLiteStatement getInsertStmt(SQLiteDatabase db, String name, int score, int difficulty) {
        SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DatabaseEntry.TABLE_NAME + "(" + DatabaseEntry.COLUMN_NAME + ", " + DatabaseEntry.COLUMN_SCORE + ", " + DatabaseEntry.COLUMN_DIFFICULTY + ") VALUES (?, ?, ?)");
        stmt.bindString(1, name);
        stmt.bindLong(2, score);
        stmt.bindLong(3, difficulty);

        return stmt;
    }
}

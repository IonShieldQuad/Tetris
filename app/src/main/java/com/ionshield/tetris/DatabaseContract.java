package com.ionshield.tetris;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {}

    public static class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "scores";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_DIFFICULTY = "difficulty";
    }

}

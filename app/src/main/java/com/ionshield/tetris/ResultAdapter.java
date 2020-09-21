package com.ionshield.tetris;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ResultAdapter extends CursorAdapter {
    public static final String[] difficulties = new String[]{"Easy", "Normal", "Hard"};

    public ResultAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.text_view_part, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvScore = view.findViewById(R.id.tvScore);
        TextView tvDifficulty = view.findViewById(R.id.tvDifficulty);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_NAME));
        long score = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_SCORE));
        long difficulty = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DIFFICULTY));

        tvName.setText(name);
        tvScore.setText(String.valueOf(score));
        try {
            tvDifficulty.setText(difficulties[(int) difficulty]);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            tvDifficulty.setText("");
        }
    }
}

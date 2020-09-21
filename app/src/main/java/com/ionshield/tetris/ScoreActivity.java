package com.ionshield.tetris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ScoreActivity extends AppCompatActivity {
    DatabaseHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        cursor = query();

        updateList(cursor);
    }

    private void updateList(Cursor cursor) {
        ListView listView = findViewById(R.id.listView);

        ResultAdapter adapter = new ResultAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    private Cursor query() {
        return db.rawQuery("SELECT * FROM " + DatabaseContract.DatabaseEntry.TABLE_NAME + " ORDER BY "+ DatabaseContract.DatabaseEntry.COLUMN_DIFFICULTY + " DESC, " + DatabaseContract.DatabaseEntry.COLUMN_SCORE + " DESC", new String[]{});
    }


    public void deleteAll(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                helper.truncate(db);
                cursor = query();
                updateList(cursor);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

package com.ionshield.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TetrisActivity extends AppCompatActivity {
    private TetrisActivity that = this;
    private TetrisController controller;
    private TetrisView tetrisView;
    private NextShapeView nextShapeView;
    private TextView scoreView;
    private Handler handler;
    private boolean isGameOver = false;
    private boolean recordScores;
    private String difficulty;
    private String name;

    private Runnable runnable;

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        findViewById(R.id.downButton).setOnTouchListener(new RepeatListener(true, 500, 100));
        findViewById(R.id.leftButton).setOnTouchListener(new RepeatListener(true, 500, 100));
        findViewById(R.id.rightButton).setOnTouchListener(new RepeatListener(true, 500, 100));

        Intent intent = getIntent();
        name = intent.getStringExtra("playerName");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = preferences.getString("difficulty", "med");
        recordScores = preferences.getBoolean("scores_enabled", false);

        if (recordScores) {
            helper = new DatabaseHelper(this);
            db = helper.getWritableDatabase();
        }

        if (savedInstanceState != null) {
            controller = (TetrisController)savedInstanceState.getSerializable("controller");
            if (name == null) {
                name = savedInstanceState.getString("name", "Null");
            }
        }
        else {
            controller = new TetrisController(difficulty);
        }

        tetrisView = findViewById(R.id.tetrisView);
        tetrisView.setController(controller);

        nextShapeView = findViewById(R.id.nextShapeView);
        nextShapeView.setController(controller);

        scoreView = findViewById(R.id.scoreTextView);

        handler = new Handler();
        runCoreLoop(0);
    }

    private void update() {
        tetrisView.invalidate();
        nextShapeView.invalidate();
        scoreView.setText(String.valueOf(controller.getScore()));
    }

    private void runCoreLoop(long delay)
    {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isGameOver) return;
                        boolean res = controller.step();
                        update();

                        if (res) {
                            runCoreLoop(controller.getDelay());
                        }
                        else {
                            isGameOver = true;
                            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
                            recordScores();
                        }
                    }
                });
            }
        };
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putSerializable("controller", controller);
        outState.putString("name", name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void recordScores() {
        if (recordScores) {
            if (name == null) {
                name = "Null";
            }
            SQLiteStatement stmt = helper.getInsertStmt(db, name, controller.getScore(), getDifficultyIndex());
            stmt.executeInsert();
        }
    }

    public int getDifficultyIndex() {
        switch (difficulty) {
            case "low":
                return 0;
            case "med":
                return 1;
            case "high":
                return 2;
        }
        return -1;
    }

    public void moveLeft(View view) {
        if (isGameOver) {
            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
            return;
        }
        controller.tryMoveLeft();
        update();
    }

    public void moveDown(View view) {
        if (isGameOver) {
            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
            return;
        }
        boolean res = controller.step();
        update();

        if (!res) {
            isGameOver = true;
            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
            recordScores();
        }
    }

    public void moveRight(View view) {
        if (isGameOver) {
            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
            return;
        }
        controller.tryMoveRight();
        update();
    }

    public void rotate(View view) {
        if (isGameOver) {
            Toast.makeText(that, "Game Over", Toast.LENGTH_LONG).show();
            return;
        }
        controller.tryRotateCWAround(new PointInt2D(0, 0));
        update();
    }
}

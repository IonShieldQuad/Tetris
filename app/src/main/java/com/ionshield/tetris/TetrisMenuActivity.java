package com.ionshield.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TetrisMenuActivity extends AppCompatActivity {
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris_menu);
        playerName = getIntent().getStringExtra("playerName");
    }

    public void leaderboardClicked(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void playClicked(View view) {
        Intent intent = new Intent(this, TetrisActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
    }
}

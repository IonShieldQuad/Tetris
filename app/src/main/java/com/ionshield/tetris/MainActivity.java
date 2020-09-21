package com.ionshield.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonClicked(View view) {

        EditText textbox = findViewById(R.id.editText);

        Intent intent = new Intent(this, TetrisMenuActivity.class);
        String playerName = textbox.getText().toString();
        intent.putExtra("playerName", playerName);
        startActivity(intent);
    }
}



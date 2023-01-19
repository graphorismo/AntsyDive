package ru.graphorismo.antsydive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    private AntsyDiveView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new AntsyDiveView(this);
        setContentView(gameView);
    }

    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
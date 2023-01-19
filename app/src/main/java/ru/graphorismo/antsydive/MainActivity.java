package ru.graphorismo.antsydive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPlay = findViewById(R.id.mainActivity_button_play);
        buttonPlay.setOnClickListener( (view) -> {
            switchToGameActivity();
        });
    }

    private void switchToGameActivity(){
        Intent gameActivityIntent = new Intent(this, GameActivity.class);
        startActivity(gameActivityIntent);
        finish();
    }
}
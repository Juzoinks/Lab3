package com.example.buzzpals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buzzpals.R;
import com.example.buzzpals.StartGame;

public class GameOver extends AppCompatActivity {


    TextView tvPoints, tvPersonalBest;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int points = getIntent().getExtras().getInt("points");

        tvPoints = findViewById(R.id.tvPoints);
        tvPersonalBest = findViewById(R.id.tvPersonalBest);

        tvPoints.setText("" + points);

        sharedPreferences = getSharedPreferences("pref", 0);

        int pointsSP = sharedPreferences.getInt("pointsSP", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(points > pointsSP){

            pointsSP = points;

            editor.putInt("pointsSP", pointsSP);

            editor.commit();
        }

        tvPersonalBest.setText("" + pointsSP);
    }

    public void restart(View view) {
        // Create an Intent object to launch StartGame Activity
        Intent intent = new Intent(GameOver.this, StartGame.class);
        startActivity(intent);
        // Finish GameOver Activity
        finish();
    }

    public void exit(View view) {
        // Create an Intent to start the StartGame Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);  // Start the StartGame activity
        finish();  // Optionally, finish the current activity (if you want to remove it from the back stack)
    }

}

package com.example.buzzpals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class DictionaryActivity extends AppCompatActivity {

    private int[] ImageIds = {
            R.drawable.apple,
            R.drawable.bunny,
            R.drawable.bus,
            R.drawable.cat,
            R.drawable.door,
            R.drawable.horse,
            R.drawable.pencil,
            R.drawable.snake,
            R.drawable.strawberry,
            R.drawable.train,
    };

    private String[] words = {
            "Apple",
            "Bunny",
            "Bus",
            "Cat",
            "Door",
            "Horse",
            "Pencil",
            "Snake",
            "Strawberry",
            "Train"
    };

    private int currentIndex = 0;

    private ImageView imageView;
    private TextView textView;

    private TextToSpeech tts; // Text-to-Speech engine

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        imageView = findViewById(R.id.ivDictionaryImage);
        textView = findViewById(R.id.tvDictionaryWord);

        // Initialize TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.UK);
            }
        });

        updateDictionaryEntry();
    }

    public void nextDictionaryEntry(View view) {
        currentIndex++;

        if (currentIndex >= words.length) {
            showEndOfDictionaryPopup();
        } else {
            updateDictionaryEntry();
        }
    }

    public void speakWord(View view) {
        String wordToSpeak = words[currentIndex];
        tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void showEndOfDictionaryPopup() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Let's Play!")
                .setMessage("You've finished the dictionary. Ready to play?")
                .setCancelable(false)
                .setPositiveButton("Play", (dialog, which) -> {
                    Intent intent = new Intent(DictionaryActivity.this, StartGame.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Back to Menu", (dialog, which) -> {
                    Intent intent = new Intent(DictionaryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void updateDictionaryEntry() {
        imageView.setImageResource(ImageIds[currentIndex]);
        textView.setText(words[currentIndex]);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}

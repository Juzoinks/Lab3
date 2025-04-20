package com.example.buzzpals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class StartGame extends AppCompatActivity {

    TextView tvTimer;
    TextView tvResult;
    ImageView ivShowImage;
    HashMap<String, Integer> map = new HashMap<>();
    ArrayList<String> techList = new ArrayList<>();
    int index;
    Button btn1, btn2, btn3, btn4;
    TextView tvPoints;
    int points;
    CountDownTimer countDownTimer;
    long millisUntilFinished;

    // Text-to-Speech
    private TextToSpeech tts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        tvTimer = findViewById(R.id.tvTimer);
        tvResult = findViewById(R.id.tvResult);
        ivShowImage = findViewById(R.id.ivShowImage);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        tvPoints = findViewById(R.id.tvPoints);
        index = 0;

        // Initialize words
        techList.add("apple");
        techList.add("bunny");
        techList.add("bus");
        techList.add("cat");
        techList.add("door");
        techList.add("horse");
        techList.add("pencil");
        techList.add("snake");
        techList.add("strawberry");
        techList.add("train");

        // Map images
        map.put("apple", R.drawable.apple);
        map.put("bunny", R.drawable.bunny);
        map.put("bus", R.drawable.bus);
        map.put("cat", R.drawable.cat);
        map.put("door", R.drawable.door);
        map.put("horse", R.drawable.horse);
        map.put("pencil", R.drawable.pencil);
        map.put("snake", R.drawable.snake);
        map.put("strawberry", R.drawable.strawberry);
        map.put("train", R.drawable.train);

        Collections.shuffle(techList);

        // TTS setup
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        millisUntilFinished = 10000;
        points = 0;
        startGame();
    }

    private void startGame() {
        resetButtons();
        millisUntilFinished = 20000;
        tvTimer.setText((millisUntilFinished / 1000) + "s");
        tvPoints.setText(points + " / " + techList.size());
        generateQuestions(index);
        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                btn1.setBackgroundColor(Color.parseColor("#2196f3"));
                btn2.setBackgroundColor(Color.parseColor("#2196f3"));
                btn3.setBackgroundColor(Color.parseColor("#2196f3"));
                btn4.setBackgroundColor(Color.parseColor("#2196f3"));
                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn3.setEnabled(true);
                btn4.setEnabled(true);

                index++;
                if (index > techList.size() - 1) {
                    ivShowImage.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.GONE);
                    btn3.setVisibility(View.GONE);
                    btn4.setVisibility(View.GONE);
                    Intent intent = new Intent(StartGame.this, GameOver.class);
                    intent.putExtra("points", points);
                    startActivity(intent);
                    finish();
                } else {
                    countDownTimer = null;
                    startGame();
                }
            }
        }.start();
    }

    private void generateQuestions(int index) {
        String correctAnswer = techList.get(index);

        ArrayList<String> techListTemp = new ArrayList<>(techList);
        techListTemp.remove(correctAnswer);
        Collections.shuffle(techListTemp);

        ArrayList<String> newList = new ArrayList<>();
        newList.add(techListTemp.get(0));
        newList.add(techListTemp.get(1));
        newList.add(techListTemp.get(2));
        newList.add(correctAnswer);

        Collections.shuffle(newList);

        btn1.setText(newList.get(0));
        btn2.setText(newList.get(1));
        btn3.setText(newList.get(2));
        btn4.setText(newList.get(3));

        ivShowImage.setImageResource(map.get(correctAnswer));
    }

    private void resetButtons() {
        Button[] buttons = {btn1, btn2, btn3, btn4};
        for (Button btn : buttons) {
            btn.setBackgroundColor(Color.parseColor("#2196f3"));
            btn.setEnabled(true);
        }
        tvResult.setText("");
    }

    public void nextQuestion(View view) {
        countDownTimer.cancel();
        resetButtons();
        index++;
        if (index > techList.size() - 1) {
            ivShowImage.setVisibility(View.GONE);
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            Intent intent = new Intent(StartGame.this, GameOver.class);
            intent.putExtra("points", points);
            startActivity(intent);
            finish();
        } else {
            countDownTimer = null;
            startGame();
        }
    }

    public void answerSelected(View view) {
        view.setBackgroundColor(Color.parseColor("#17243e"));
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        countDownTimer.cancel();

        String answer = ((Button) view).getText().toString().trim();
        String correctAnswer = techList.get(index);

        if (answer.equals(correctAnswer)) {
            points++;
            tvPoints.setText(points + " / " + techList.size());
            tvResult.setText("Correct");
        } else {
            tvResult.setText("Wrong");
        }
    }

    // 🔊 Speak current word when sound button is clicked
    public void speakCurrentWord(View view) {
        if (tts != null && !techList.isEmpty()) {
            String currentWord = techList.get(index);
            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // 🧹 Clean up TTS when activity is destroyed
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}

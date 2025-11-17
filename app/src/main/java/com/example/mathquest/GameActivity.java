package com.example.mathquest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    int num1, num2, correctAnswer;
    Random random = new Random();
    ProgressBar progressBar;
    TextView questionText, timerText, scoreText;
    ProgressBar questionProgressBar;
    TextInputEditText answerInput;
    Button validateButton, nextButton;
    int questionCount = 1;
    final int totalQuestions = 10 ;

    CountDownTimer countDownTimer;
    int timeLeft = 30;
    int score = 0;
    String level;
    int miniLevel = 1;
    boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        questionText = findViewById(R.id.questionText);
        timerText = findViewById(R.id.timerText);
        progressBar = findViewById(R.id.progressBar);
        questionProgressBar = findViewById(R.id.questionProgressBar);
        answerInput = findViewById(R.id.answerInput);
        validateButton = findViewById(R.id.validateButton);
        nextButton = findViewById(R.id.nextButton);

        scoreText = findViewById(R.id.scoreText);

        questionProgressBar.setProgress(questionCount);

        level = getIntent().getStringExtra("level");
        miniLevel = getIntent().getIntExtra("miniLevel", 1);

        validateButton.setOnClickListener(v -> validateAnswer());
        nextButton.setOnClickListener(v -> nextQuestion());

        generateQuestion();
        startTimer();
    }

    private void generateQuestion() {
        String operation = "+";
        
        // Progressive complexity based on mini-level
        // Complexity increases gradually: mini-level 1 = easiest, higher = harder

        if (level != null) {
            switch (level) {
                case "facile":
                    // Facile: 3 mini-levels, only addition
                    // Level 1: 1-5, Level 2: 1-8, Level 3: 1-10
                    int maxNum = 5 + (miniLevel - 1) * 2; // 5, 7, 9 -> capped at 10
                    if (maxNum > 10) maxNum = 10;
                    num1 = random.nextInt(maxNum) + 1;
                    num2 = random.nextInt(maxNum) + 1;
                    correctAnswer = num1 + num2;
                    operation = "+";
                    break;

                case "moyen":
                    // Moyen: 6 mini-levels, addition and subtraction
                    // Level 1-2: 1-20, Level 3-4: 1-35, Level 5-6: 1-50
                    int maxNumMoyen;
                    if (miniLevel <= 2) {
                        maxNumMoyen = 20;
                    } else if (miniLevel <= 4) {
                        maxNumMoyen = 35;
                    } else {
                        maxNumMoyen = 50;
                    }
                    num1 = random.nextInt(maxNumMoyen) + 1;
                    num2 = random.nextInt(maxNumMoyen) + 1;
                    
                    // Early levels: more addition, later levels: more subtraction
                    boolean useSubtraction = (miniLevel >= 3 && random.nextBoolean()) || 
                                           (miniLevel >= 5 && random.nextDouble() > 0.3);
                    
                    if (useSubtraction && num1 >= num2) {
                        correctAnswer = num1 - num2;
                        operation = "-";
                    } else {
                        // Ensure num1 >= num2 for subtraction, or just do addition
                        if (num1 < num2) {
                            int temp = num1;
                            num1 = num2;
                            num2 = temp;
                        }
                        correctAnswer = num1 + num2;
                        operation = "+";
                    }
                    break;

                case "difficile":
                    // Difficile: 9 mini-levels, addition, subtraction, multiplication
                    // Level 1-3: 1-30, Level 4-6: 1-60, Level 7-9: 1-100
                    int maxNumDifficile;
                    if (miniLevel <= 3) {
                        maxNumDifficile = 30;
                    } else if (miniLevel <= 6) {
                        maxNumDifficile = 60;
                    } else {
                        maxNumDifficile = 100;
                    }
                    
                    num1 = random.nextInt(maxNumDifficile) + 1;
                    
                    // Operation selection based on mini-level
                    int op;
                    if (miniLevel <= 2) {
                        // Levels 1-2: only addition
                        op = 0;
                    } else if (miniLevel <= 4) {
                        // Levels 3-4: addition and subtraction
                        op = random.nextInt(2);
                    } else {
                        // Levels 5-9: all operations
                        op = random.nextInt(3);
                    }
                    
                    if (op == 0) {
                        num2 = random.nextInt(Math.min(maxNumDifficile, 50)) + 1;
                        correctAnswer = num1 + num2;
                        operation = "+";
                    } else if (op == 1) {
                        num2 = random.nextInt(Math.min(maxNumDifficile, 50)) + 1;
                        if (num1 < num2) {
                            int temp = num1;
                            num1 = num2;
                            num2 = temp;
                        }
                        correctAnswer = num1 - num2;
                        operation = "-";
                    } else {
                        // Multiplication: smaller numbers
                        num2 = random.nextInt(10) + 1;
                        correctAnswer = num1 * num2;
                        operation = "×";
                    }
                    break;
            }
        }

        // Afficher la question
        questionText.setText(num1 + " " + operation + " " + num2 + " = ?");
        
        // Reset UI for new question
        answerInput.setText("");
        answerInput.setEnabled(true);
        validateButton.setEnabled(true);
        nextButton.setEnabled(false);
        isAnswered = false;
    }

    private void validateAnswer() {
        if (isAnswered) {
            return;
        }

        String answerText = answerInput.getText().toString().trim();
        
        if (answerText.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une réponse", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userAnswer = Integer.parseInt(answerText);
            
            if (userAnswer == correctAnswer) {
                Toast.makeText(this, "Félicitations ! C'est correcte", Toast.LENGTH_SHORT).show();
                score++;
                scoreText.setText("Score : " + score);
            } else {
                Toast.makeText(this, "Oops! Essayer de nouveau", Toast.LENGTH_SHORT).show();
            }
            
            isAnswered = true;
            answerInput.setEnabled(false);
            validateButton.setEnabled(false);
            nextButton.setEnabled(true);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextQuestion() {
        if (questionCount < totalQuestions) {
            questionCount++;
            questionProgressBar.setProgress(questionCount);
            generateQuestion();
            
            // Reset timer
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            timeLeft = 30;
            startTimer();
        } else {
            // Level completed
            saveProgress();
            Toast.makeText(this, "Niveau terminé! Score: " + score + "/10", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void saveProgress() {
        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        // Mark this mini-level as completed
        editor.putBoolean(level + "_level_" + miniLevel + "_completed", true);
        
        // Unlock next level if it exists
        int maxLevels = 0;
        switch (level) {
            case "facile":
                maxLevels = 3;
                break;
            case "moyen":
                maxLevels = 6;
                break;
            case "difficile":
                maxLevels = 9;
                break;
        }
        
        if (miniLevel < maxLevels) {
            int nextLevel = miniLevel + 1;
            int currentUnlocked = prefs.getInt(level + "_last_unlocked", 1);
            if (nextLevel > currentUnlocked) {
                editor.putInt(level + "_last_unlocked", nextLevel);
            }
        }
        
        // Save score for this level
        editor.putInt(level + "_level_" + miniLevel + "_score", score);
        
        editor.apply();
    }
    private void updateProgressInFirebase(int score) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        String levelKey = getIntent().getStringExtra("level"); // "facile", "moyen", "difficile"
        int miniLevel = getIntent().getIntExtra("miniLevel", 1); // 1,2,3…

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid)
                .child("progress")
                .child(levelKey);

        // 1️⃣ Marquer le mini-niveau comme completed
        userRef.child("miniLevels")
                .child("level_" + miniLevel)
                .child("completed")
                .setValue(true);

        // 2️⃣ Mettre à jour le score
        userRef.child("miniLevels")
                .child("level_" + miniLevel)
                .child("score")
                .setValue(score);

        // 3️⃣ Débloquer le prochain level si nécessaire
        userRef.child("lastUnlocked").get().addOnSuccessListener(snapshot -> {
            int lastUnlocked = snapshot.getValue(Integer.class);

            if (miniLevel == lastUnlocked) {
                userRef.child("lastUnlocked").setValue(lastUnlocked + 1);
            }
        });
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeft = 30;
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                timerText.setText("Temps : " + timeLeft + "s");
                progressBar.setProgress(timeLeft);
            }

            @Override
            public void onFinish() {
                timerText.setText("Temps écoulé !");
                progressBar.setProgress(0);
                if (!isAnswered) {
                    String answerText = answerInput.getText().toString().trim();
                    if (answerText.isEmpty()) {
                        // Time expired with no answer - mark as wrong
                        Toast.makeText(GameActivity.this, "Oops! Essayer de nouveau", Toast.LENGTH_SHORT).show();
                        isAnswered = true;
                        answerInput.setEnabled(false);
                        validateButton.setEnabled(false);
                        nextButton.setEnabled(true);
                    } else {
                        validateAnswer();
                    }
                }
            }
        }.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
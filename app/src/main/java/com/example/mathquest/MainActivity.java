package com.example.mathquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CardView easyButton, mediumButton, hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String playerName = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("player_name", null);

        if (playerName == null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            android.view.LayoutInflater inflater = getLayoutInflater();
            android.view.View dialogView = inflater.inflate(R.layout.dialog_name, null);
            builder.setView(dialogView);

            final android.widget.EditText input = dialogView.findViewById(R.id.inputName);

            builder.setCancelable(false)
                    .setPositiveButton("✅ OK", (dialog, which) -> {
                        String name = input.getText().toString().trim();
                        if (name.isEmpty()) {
                            android.widget.Toast.makeText(this, "Veuillez entrer un nom valide ⚠️", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                    .edit()
                                    .putString("player_name", name)
                                    .apply();
                            android.widget.Toast.makeText(this, "Bienvenue " + name + " 🎮", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });

            android.app.AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.WHITE));
            dialog.show();
        }

        easyButton = findViewById(R.id.btnEasy);
        mediumButton = findViewById(R.id.btnMedium);
        hardButton = findViewById(R.id.btnHard);
        registerForContextMenu(easyButton);
        registerForContextMenu(mediumButton);
        registerForContextMenu(hardButton);
        easyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
            intent.putExtra("level", "facile");
            startActivity(intent);
        });

        easyButton.setOnLongClickListener(v -> {
            showLevelMenu("facile");
            return true;
        });

        mediumButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
            intent.putExtra("level", "moyen");
            startActivity(intent);
        });

        mediumButton.setOnLongClickListener(v -> {
            showLevelMenu("moyen");
            return true;
        });

        hardButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
            intent.putExtra("level", "difficile");
            startActivity(intent);
        });

        hardButton.setOnLongClickListener(v -> {
            showLevelMenu("difficile");
            return true;
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            Toast.makeText(this, "Profil sélectionné", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_settings) {
            Toast.makeText(this, "Paramètres sélectionnés", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "À propos sélectionné", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        if (v.getId() == R.id.btnEasy) {
            menu.setHeaderTitle("Menu - Niveau Facile");
        } else if (v.getId() == R.id.btnMedium) {
            menu.setHeaderTitle("Menu - Niveau Moyen");
        } else if (v.getId() == R.id.btnHard) {
            menu.setHeaderTitle("Menu - Niveau Difficile");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                Toast.makeText(this, "Ce niveau vous permet de pratiquer les calculs mentaux 🧠", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_reset:
                getSharedPreferences("GamePrefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();
                Toast.makeText(this, "Scores réinitialisés ✅", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showLevelMenu(String levelType) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_level_menu, null);
        builder.setView(dialogView);

        TextView levelTitle = dialogView.findViewById(R.id.levelTitle);
        Button btnViewInfo = dialogView.findViewById(R.id.btnViewInfo);
        Button btnResetScore = dialogView.findViewById(R.id.btnResetScore);

        // Set level title
        String levelName = "";
        switch (levelType) {
            case "facile":
                levelName = "Facile";
                break;
            case "moyen":
                levelName = "Moyen";
                break;
            case "difficile":
                levelName = "Difficile";
                break;
        }
        levelTitle.setText("Niveau " + levelName);

        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.WHITE));

        btnViewInfo.setOnClickListener(v -> {
            dialog.dismiss();
            showLevelInfo(levelType);
        });

        btnResetScore.setOnClickListener(v -> {
            dialog.dismiss();
            showResetConfirmation(levelType);
        });

        dialog.show();
    }

    private void showLevelInfo(String levelType) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_level_info, null);
        builder.setView(dialogView);

        TextView currentScore = dialogView.findViewById(R.id.currentScore);
        TextView unlockedLevels = dialogView.findViewById(R.id.unlockedLevels);
        TextView completedLevels = dialogView.findViewById(R.id.completedLevels);
        TextView equationTypes = dialogView.findViewById(R.id.equationTypes);

        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);

        // Calculate total score
        int totalScore = 0;
        int maxLevels = 0;
        String operations = "";

        switch (levelType) {
            case "facile":
                maxLevels = 3;
                operations = "Addition (+)";
                for (int i = 1; i <= maxLevels; i++) {
                    totalScore += prefs.getInt(levelType + "_level_" + i + "_score", 0);
                }
                break;
            case "moyen":
                maxLevels = 6;
                operations = "Addition (+), Soustraction (-)";
                for (int i = 1; i <= maxLevels; i++) {
                    totalScore += prefs.getInt(levelType + "_level_" + i + "_score", 0);
                }
                break;
            case "difficile":
                maxLevels = 9;
                operations = "Addition (+), Soustraction (-), Multiplication (×)";
                for (int i = 1; i <= maxLevels; i++) {
                    totalScore += prefs.getInt(levelType + "_level_" + i + "_score", 0);
                }
                break;
        }

        // Count unlocked levels
        int unlocked = prefs.getInt(levelType + "_last_unlocked", 1);

        // Count completed levels
        int completed = 0;
        for (int i = 1; i <= maxLevels; i++) {
            if (prefs.getBoolean(levelType + "_level_" + i + "_completed", false)) {
                completed++;
            }
        }

        currentScore.setText("Score actuel: " + totalScore);
        unlockedLevels.setText("Niveaux déverrouillés: " + unlocked + " / " + maxLevels);
        completedLevels.setText("Niveaux complétés: " + completed + " / " + maxLevels);
        equationTypes.setText("Types d'équations: " + operations);

        builder.setPositiveButton("Fermer", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.WHITE));
        dialog.show();
    }

    private void showResetConfirmation(String levelType) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚠️ Réinitialiser le score");
        builder.setMessage("Êtes-vous sûr de vouloir réinitialiser le score de ce niveau ? Cette action est irréversible.");

        builder.setPositiveButton("Oui, je suis sûr", (dialog, which) -> {
            resetLevelScore(levelType);
            Toast.makeText(this, "Score réinitialisé ✅", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetLevelScore(String levelType) {
        SharedPreferences prefs = getSharedPreferences("GameProgress", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int maxLevels = 0;
        switch (levelType) {
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

        // Reset all scores for this level type
        for (int i = 1; i <= maxLevels; i++) {
            editor.putInt(levelType + "_level_" + i + "_score", 0);
            editor.putBoolean(levelType + "_level_" + i + "_completed", false);
        }

        // Reset unlocked levels (keep only first level unlocked)
        editor.putInt(levelType + "_last_unlocked", 1);

        editor.apply();
    }

}
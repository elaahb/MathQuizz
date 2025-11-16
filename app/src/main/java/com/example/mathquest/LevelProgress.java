package com.example.mathquest;
import java.util.HashMap;
import java.util.Map;
public class LevelProgress {

    private int lastUnlocked;
    private Map<String, MiniLevel> miniLevels;

    // Constructeur vide Firebase
    public LevelProgress() {}

    public LevelProgress(int totalLevels) {
        this.lastUnlocked = 1;  // Seul le niveau 1 est débloqué
        miniLevels = new HashMap<>();

        for (int i = 1; i <= totalLevels; i++) {
            miniLevels.put("level_" + i, new MiniLevel());
        }
    }

    // GETTERS + SETTERS
    public int getLastUnlocked() { return lastUnlocked; }
    public void setLastUnlocked(int lastUnlocked) { this.lastUnlocked = lastUnlocked; }

    public Map<String, MiniLevel> getMiniLevels() { return miniLevels; }
    public void setMiniLevels(Map<String, MiniLevel> miniLevels) { this.miniLevels = miniLevels; }
}

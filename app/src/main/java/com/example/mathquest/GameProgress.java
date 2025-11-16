package com.example.mathquest;
import java.util.HashMap;
import java.util.Map;
public class GameProgress {

    private String userId;
    private String email;
    private long lastUpdate;

    private int hearts;
    private int coins;

    private Map<String, LevelProgress> progress;

    public GameProgress() {}

    public GameProgress(String userId, String email) {
        this.userId = userId;
        this.email = email;
        this.lastUpdate = System.currentTimeMillis();

        this.hearts = 3;
        this.coins = 0;

        progress = new HashMap<>();
        progress.put("facile", new LevelProgress(3));
        progress.put("moyen", new LevelProgress(6));
        progress.put("difficile", new LevelProgress(9));
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }

    public int getHearts() { return hearts; }
    public void setHearts(int hearts) { this.hearts = hearts; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }

    public Map<String, LevelProgress> getProgress() { return progress; }
    public void setProgress(Map<String, LevelProgress> progress) { this.progress = progress; }
}
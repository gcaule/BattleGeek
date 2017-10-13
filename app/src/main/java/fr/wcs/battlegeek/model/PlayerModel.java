package fr.wcs.battlegeek.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

import fr.wcs.battlegeek.controller.AI;

@IgnoreExtraProperties
public class PlayerModel {

    private String name;
    private int totalGameTime = 0;

    private HashMap<String, Integer> gameTime = new HashMap<>();
    private HashMap<String, Integer> bestTime = new HashMap<>();
    private HashMap<String, Integer> gameParts = new HashMap<>();
    private HashMap<String, Integer> victories = new HashMap<>();
    private HashMap<String, Integer> defeats = new HashMap<>();
    private HashMap<String, Integer> ratio = new HashMap<>();

    public PlayerModel() {
    }

    public PlayerModel(String name) {
        this.name = name;
        init();
    }

    private void init() {
        this.totalGameTime = 0;
        for(AI.Level level : AI.Level.values()) {
            this.gameTime.put(level.toString(), 0);
            this.bestTime.put(level.toString(), -1);
            this.gameParts.put(level.toString(), 0);
            this.victories.put(level.toString(), 0);
            this.defeats.put(level.toString(), 0);
            this.ratio.put(level.toString(), 0);
        }
    }

    public void addVictory(AI.Level level) {
        this.gameParts.put(level.toString(), gameParts.get(level.toString()) + 1);
        this.victories.put(level.toString(), victories.get(level.toString()) + 1);
        this.updateRatio(level);
    }

    public void addDefeat(AI.Level level) {
        this.gameParts.put(level.toString(), gameParts.get(level.toString()) + 1);
        this.defeats.put(level.toString(), defeats.get(level.toString()) + 1);
        this.updateRatio(level);
    }

    public void addGameTime(AI.Level level, Result.Type result, int time) {
        this.totalGameTime += time;
        int lastBestTime = this.bestTime.get(level.toString());
        this.bestTime.put(level.toString(), time < lastBestTime && result == Result.Type.VICTORY ? time : lastBestTime);
        this.gameTime.put(level.toString(), gameTime.get(level.toString()) + time);
    }

    private void updateRatio(AI.Level level) {
        int newRatio = (int)((float) victories.get(level.toString()) / (float) gameParts.get(level.toString()) * 100);
        ratio.put(level.toString(), newRatio);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalGameTime() {
        return totalGameTime;
    }

    public void setTotalGameTime(int totalGameTime) {
        this.totalGameTime = totalGameTime;
    }

    public HashMap<String, Integer> getGameTime() {
        return gameTime;
    }

    public void setGameTime(HashMap<String, Integer> gameTime) {
        this.gameTime = gameTime;
    }

    public HashMap<String, Integer> getBestTime() {
        return bestTime;
    }

    public void setBestTime(HashMap<String, Integer> bestTime) {
        this.bestTime = bestTime;
    }

    public HashMap<String, Integer> getGameParts() {
        return gameParts;
    }

    public void setGameParts(HashMap<String, Integer> gameParts) {
        this.gameParts = gameParts;
    }

    public HashMap<String, Integer> getVictories() {
        return victories;
    }

    public void setVictories(HashMap<String, Integer> victories) {
        this.victories = victories;
    }

    public HashMap<String, Integer> getDefeats() {
        return defeats;
    }

    public void setDefeats(HashMap<String, Integer> defeats) {
        this.defeats = defeats;
    }

    public HashMap<String, Integer> getRatio() {
        return ratio;
    }

    public void setRatio(HashMap<String, Integer> ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "PlayerModel{" +
                "name='" + name + '\'' +
                ", totalGameTime=" + totalGameTime +
                ", gameTime=" + gameTime +
                ", bestTime=" + bestTime +
                ", gameParts=" + gameParts +
                ", victories=" + victories +
                ", defeats=" + defeats +
                ", ratio=" + ratio +
                '}';
    }
}

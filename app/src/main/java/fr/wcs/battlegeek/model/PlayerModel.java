package fr.wcs.battlegeek.model;

import java.util.HashMap;

import fr.wcs.battlegeek.controller.AI;

public class PlayerModel {

    private String name;
    private String score;
    private int totalGameTime;
    private int bestTime = 2_147_483_647;


    private HashMap<AI.Level, Integer> gameParts = new HashMap<>();
    private HashMap<AI.Level, Integer> victories = new HashMap<>();
    private HashMap<AI.Level, Integer> defeats = new HashMap<>();
    private HashMap<AI.Level, Integer> ratio = new HashMap<>();

    public PlayerModel() {
        init();
    }

    public PlayerModel(String name, String score) {

        this.name = name;
        this.score = score;
        init();
    }

    private void init() {
        this.totalGameTime = 0;
        for(AI.Level level : AI.Level.values()) {
            this.gameParts.put(level, 0);
            this.victories.put(level, 0);
            this.defeats.put(level, 0);
            this.ratio.put(level, 0);
        }
    }

    public void addVictory(AI.Level level) {
        this.gameParts.put(level, gameParts.get(level) + 1);
        this.victories.put(level, victories.get(level) + 1);
        this.updateRatio(level);
    }

    public void addDefeat(AI.Level level) {
        this.gameParts.put(level, gameParts.get(level) + 1);
        this.defeats.put(level, defeats.get(level) + 1);
        this.updateRatio(level);
    }

    public void addGameTime(int time) {
        this.totalGameTime += time;
        this.bestTime = time < bestTime ? time : bestTime;
    }

    private void updateRatio(AI.Level level) {
        int newRatio = (int)((float) victories.get(level) / (float) gameParts.get(level) * 100);
        ratio.put(level, newRatio);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getTotalGameTime() {
        return totalGameTime;
    }

    public HashMap<AI.Level, Integer> getGameParts() {
        return gameParts;
    }

    public HashMap<AI.Level, Integer> getVictories() {
        return victories;
    }

    public HashMap<AI.Level, Integer> getDefeats() {
        return defeats;
    }

    public HashMap<AI.Level, Integer> getRatio() {
        return ratio;
    }
}

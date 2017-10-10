package fr.wcs.battlegeek.model;

public class PlayerModel {

    private String name, score;

    public PlayerModel() {
    }

    public PlayerModel(String name, String score) {

        this.name = name;
        this.score = score;

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
}
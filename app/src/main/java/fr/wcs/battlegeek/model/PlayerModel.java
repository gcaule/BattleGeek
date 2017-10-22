package fr.wcs.battlegeek.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;
import java.util.HashMap;

import fr.wcs.battlegeek.controller.AI;

@IgnoreExtraProperties
public class PlayerModel {

    // Enumeration for Comparators
    public enum ComparatorFactor {
        BEST_TIME, VICTORIES, RATIO, SHOTS_COUNT, NAME;
    }
    // Level for Sorting
    private static AI.Level comparatorLevel = AI.Level.I;

    private String name;
    private long totalGameTime = 0;

    private HashMap<String, Long> gameTime = new HashMap<>();
    private HashMap<String, Long> bestTime = new HashMap<>();
    private HashMap<String, Integer> gameParts = new HashMap<>();
    private HashMap<String, Integer> victories = new HashMap<>();
    private HashMap<String, Integer> defeats = new HashMap<>();
    private HashMap<String, Integer> ratio = new HashMap<>();
    private HashMap<String, Integer> bestShotsCount = new HashMap<>();


    public PlayerModel() {
    }

    public PlayerModel(String name) {
        this.name = name;
        init();
    }

    private void init() {
        this.totalGameTime = 0;
        for(AI.Level level : AI.Level.values()) {
            this.gameTime.put(level.toString(), 0L);
            this.bestTime.put(level.toString(), 2_147_483_647L);
            this.gameParts.put(level.toString(), 0);
            this.victories.put(level.toString(), 0);
            this.defeats.put(level.toString(), 0);
            this.ratio.put(level.toString(), 0);
            this.bestShotsCount.put(level.toString(), 2_147_483_647);
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

    public void addShotsCount(AI.Level level, int shotsCount) {
        int lastBestShotsCount = this.bestShotsCount.get(level.toString());
        this.bestShotsCount.put(level.toString(),
                shotsCount < lastBestShotsCount ? shotsCount : lastBestShotsCount);
    }

    public void addGameTime(AI.Level level, Result.Type result, long time) {
        this.totalGameTime += time;
        long lastBestTime = this.bestTime.get(level.toString());
        this.bestTime.put(level.toString(),
                time < lastBestTime && result == Result.Type.VICTORY ? time : lastBestTime);
        this.gameTime.put(level.toString(), gameTime.get(level.toString()) + time);
    }

    private void updateRatio(AI.Level level) {
        int newRatio = (int)((float) victories.get(level.toString())
                / (float) gameParts.get(level.toString()) * 100);
        ratio.put(level.toString(), newRatio);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalGameTime() {
        return totalGameTime;
    }

    public void setTotalGameTime(long totalGameTime) {
        this.totalGameTime = totalGameTime;
    }

    public HashMap<String, Long> getGameTime() {
        return gameTime;
    }

    public void setGameTime(HashMap<String, Long> gameTime) {
        this.gameTime = gameTime;
    }

    public HashMap<String, Long> getBestTime() {
        return bestTime;
    }

    public void setBestTime(HashMap<String, Long> bestTime) {
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

    public HashMap<String, Integer> getBestShotsCount() {
        return bestShotsCount;
    }

    public void setBestShotsCount(HashMap<String, Integer> bestShotsCount) {
        this.bestShotsCount = bestShotsCount;
    }

    public static AI.Level getComparatorLevel() {
        return comparatorLevel;
    }

    /**
     * Method setting the Comparator Level for Sorting
     * @param level
     */
    public static void setComparatorLevel(AI.Level level) {
        PlayerModel.comparatorLevel = level;
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

    /**
     * Method used by Collection.sort() to Sort the Players by Best Time
     */
    public static Comparator<PlayerModel> bestTimeComparator = new Comparator<PlayerModel>() {
        @Override
        public int compare(PlayerModel playerModel, PlayerModel comparedPlayerModel) {
            return (int)(-1 * playerModel.bestTime.get(comparatorLevel.toString())
                    - comparedPlayerModel.bestTime.get(comparatorLevel.toString()));
        }
    };

    /**
     * Method used by Collection.sort() to Sort the Players by Victories
     */
    public static Comparator<PlayerModel> victoriesComparator = new Comparator<PlayerModel>() {
        @Override
        public int compare(PlayerModel playerModel, PlayerModel comparedPlayerModel) {
            return -1 * playerModel.victories.get(comparatorLevel.toString())
                    - comparedPlayerModel.victories.get(comparatorLevel.toString());
        }
    };

    /**
     * Method used by Collection.sort() to Sort the Players by Ratio
     */
    public static Comparator<PlayerModel> ratioComparator = new Comparator<PlayerModel>() {
        @Override
        public int compare(PlayerModel playerModel, PlayerModel comparedPlayerModel) {
            return comparedPlayerModel.ratio.get(comparatorLevel.toString())
                    - playerModel.ratio.get(comparatorLevel.toString());
        }
    };

    /**
     * Method used by Collection.sort() to Sort the Players by Shots Count
     */
    public static Comparator<PlayerModel> bestShotsCountComparator = new Comparator<PlayerModel>() {
        @Override
        public int compare(PlayerModel playerModel, PlayerModel comparedPlayerModel) {
            return -1 * playerModel.bestShotsCount.get(comparatorLevel.toString())
                    - comparedPlayerModel.bestShotsCount.get(comparatorLevel.toString());
        }
    };

    public static Comparator<PlayerModel> nameComparator = new Comparator<PlayerModel>() {
        @Override
        public int compare(PlayerModel playerModel, PlayerModel comparedPlayerModel) {
            return playerModel.getName().toLowerCase().compareTo(comparedPlayerModel.getName().toLowerCase());
        }
    };
}
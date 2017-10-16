package fr.wcs.battlegeek.model;

/**
 * Created by adphi on 16/10/17.
 */

public class Bonus {
    /**
     *  Bonus:
     *  '-' Bonus Move
     *  '+' Bonus Cross Shoot
     *  '=' Bonus Replay
     */
    public enum Type {
        MOVE ("-"),
        REPLAY ("="),
        CROSS_FIRE("+");

        private String name = "";

        Type(String name){
            this.name = name;
        }

        public String toString(){
            return name;
        }
    }

    /**
     * Return the Bonus' Type from symbol, if not return null
     * @param symbol
     * @return
     */
    public static Bonus.Type getBonus(char symbol) {
        try {
            String stringSymbol = String.valueOf(symbol);
            return Bonus.Type.valueOf(stringSymbol);
        }
        catch (Exception e) {
            return null;
        }
    }


}

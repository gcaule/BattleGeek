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
        BOMB("+");

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
        if (symbol == '-') return Type.MOVE;
        else if (symbol == '=') return Type.REPLAY;
        else if (symbol == '+') return Type.BOMB;
        else return null;
    }


}

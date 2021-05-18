package com.game.service;

public class ExpLvlCalculator {

    public static Integer untilNextLvlExp(Integer experience, Integer level) {
        Integer result = (50 * (level + 1) * (level + 2)) - experience;
        return result;
    }

    public static Integer lvl(Integer experience) {
        Integer result = (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
        return result;
    }
}
